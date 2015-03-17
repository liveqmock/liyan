package com.innofi.component.dbconsole.executer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.JdbcUtils;

import com.innofi.framework.exception.DAOException;
import com.innofi.framework.exception.SysMessage;
import com.innofi.framework.dao.jdbc.sqldialect.DBDialect;
import com.innofi.framework.dao.jdbc.sqldialect.DBDialectUtil;
import com.innofi.component.dbconsole.pojo.DbConsoleColumn;
import com.innofi.component.dbconsole.pojo.DbConsoleTable;
import com.innofi.component.dbconsole.pojo.ProcessResult;
import com.innofi.framework.utils.sql.SqlUtil;
import com.innofi.framework.utils.variable.VariableHelper;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 12-7-16
 * @found time: 上午9:29
 * <p/>
 * Jdbc任务执行器
 */
public class QueryTask extends Observable implements Runnable {

    private Logger _log = LoggerFactory.getLogger(QueryTask.class);

    /**
     * 任务未启动
     */
    public static final int TASK_STATUS_NONE = 0;

    /**
     * 任务正在运行
     */
    public static final int TASK_STATUS_RUNNING = 1;

    /**
     * 任务已完成
     */
    public static final int TASK_STATUS_FINISH = 2;

    /**
     * 任务正在终止
     */
    public static final int TASK_STATUS_STOPING = 3;

    /**
     * 任务已终止
     */
    public static final int TASK_STATUS_STOP = 4;

    /**
     * 任务已超时
     */
    public static final int TASK_STATUS_TIMEOUT = 5;

    /**
     * 任务执行出错
     */
    public static final int TASK_STATUS_ERROR = 6;

    /*
     * 任务名称
     */
    private String name;

    /*
     * 执行查询的结果
     */
    private ProcessResult<DbConsoleTable> result;

    /*
     * 任务自身状态
     */
    private int status;

    /*
     * 超时时间
     */
    private long timeOut;

    /*
     * Jdbc访问数据库连接
     */
    private Connection connection;

    /*
     * 执行停止任务命令
     */
    private boolean execStop = false;

    /*
     * 是否超时
     */
    private boolean timeOuted = false;

    /*
     * 执行查询用时
     */
    private double consumeTime;


    /**
     * 执行查询操作的构造方法
     *
     * @param connection    数据库连接
     * @param processResult 处理结果保存对象
     */
    public QueryTask(QueryTaskObserver observer, String name, Connection connection, ProcessResult<DbConsoleTable> processResult) {
        this.name = name;
        this.result = processResult;
        this.timeOut = processResult.getTimeOut();
        this.connection = connection;
        this.addObserver(observer);
        observer.setQueryTask(this);
        setStatus(TASK_STATUS_NONE);
    }


    /**
     * 获得任务名称
     *
     * @return 任务名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获得任务超时时间，单位毫秒
     *
     * @return 超时时间
     */
    public long getTimeOut() {
        return timeOut;
    }

    /**
     * 执行查询的方法
     */
    public void run() {
        setStatus(TASK_STATUS_RUNNING);
        QueryExecutor queryExecutor = new QueryExecutor();
        long beginTime = System.currentTimeMillis();
        queryExecutor.start();
        boolean calledStopQueryExecutorMethod = false;
        while (!isFinish()) { //不为已完成或已终止轮询
            if (_log.isDebugEnabled()) {
                _log.debug("-----【" + name + "】轮询等待查询任务结束,当前任务状态【" + getTaskStatusName(status) + "】！");
            }
            try {
                Thread.sleep(500);
                long currentTime = System.currentTimeMillis();
                if (timeOuted = ((beginTime + timeOut) < currentTime) && !isFinish()) {//处理超时，停止查询器
                    if (!calledStopQueryExecutorMethod) {
                        stopQueryExecutor(queryExecutor, TASK_STATUS_TIMEOUT);
                        calledStopQueryExecutorMethod = true;
                    }
                }

                if (execStop && !timeOuted && !isFinish()) {//处理停止操作，停止查询器
                    if (!calledStopQueryExecutorMethod) {
                        stopQueryExecutor(queryExecutor, TASK_STATUS_STOP);
                        calledStopQueryExecutorMethod = true;
                    }
                }
            } catch (InterruptedException e) {
                stopTask();//接到命令停止查询任务
            }
        }
        _log.info("查询任务中的轮询任务结束");
    }

    /**
     * 获得当前任务状态
     *
     * @return 当前任务状态
     */
    public String getCurrentStatusName() {
        return getTaskStatusName(this.status);
    }

    /**
     * 获得当前任务状态
     *
     * @return 当前任务状态
     */
    public int getCurrentStatus() {
        return this.status;
    }

    /**
     * 停止查询任务
     */
    public void stopTask() {
        execStop = true;
    }

    public String getTaskStatusName(int status) {
        String taskStatusName;
        switch (status) {
            case QueryTask.TASK_STATUS_RUNNING:
                taskStatusName = "运行中...";
                break;
            case QueryTask.TASK_STATUS_STOP:
                taskStatusName = "已终止";
                break;
            case QueryTask.TASK_STATUS_FINISH:
                taskStatusName = "已完成";
                break;
            case QueryTask.TASK_STATUS_TIMEOUT:
                taskStatusName = "已超时";
                break;
            case QueryTask.TASK_STATUS_STOPING:
                taskStatusName = "停止中...";
                break;
            case QueryTask.TASK_STATUS_NONE:
                taskStatusName = "未启动";
                break;
            case QueryTask.TASK_STATUS_ERROR:
                taskStatusName = "执行出错";
                break;
            default:
                taskStatusName = "未知";
                break;
        }
        return taskStatusName;
    }

    /*
    * 停止查询器进行查询操作
    * @param queryExecutor
    * @param 当前任务状态
    */
    private void stopQueryExecutor(QueryExecutor queryExecutor, int taskStatus) {
        this.setStatus(TASK_STATUS_STOPING);
        queryExecutor.stopExecutor(taskStatus);
    }

    /*
     * 设置当前任务状态
     *
     * @param status
     */
    private synchronized void setStatus(int status) {
        this.status = status;
        setChanged();
        notifyObservers(status);
    }

    /**
     * 查询执行器
     */
    class QueryExecutor extends Thread {

        private int taskStatus = -1;

        private Statement statement;

        private List<ResultSet> resultSetList = new ArrayList<ResultSet>();

        private boolean queryExecuted;

        public QueryExecutor() {

        }

        public void run() {
            executeQuery();
        }

        public void executeQuery() {
            try {
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String querySql = result.getQuerySql();//查询sql
                String countSql = SqlUtil.getCountSql(querySql);//总记录sql

                ResultSet countResult = statement.executeQuery(countSql);
                resultSetList.add(countResult);
                countResult.next();
                int totalCount = countResult.getInt(1);

                _log.info("【" + name + "】任务，执行查询SQL【" + querySql + "】，总记录条数【" + totalCount + "】,提取限制条数【" + result.getLimitSize() + "】");

                DBDialect dbDialect = DBDialectUtil.getDBDialect(connection);//数据库方言

                String pageSql = dbDialect.getLimitSql(querySql, result.getPageNo(), result.getLimitSize()); //分页Sql

                long startTime = System.currentTimeMillis();
                ResultSet queryResultSet = statement.executeQuery(pageSql); //查询结果
                long endTime = System.currentTimeMillis();

                consumeTime = (endTime - startTime)/1000;

                ResultSetMetaData resultSetMetaData = queryResultSet.getMetaData();//查询结果元数据

                List<DbConsoleColumn> columns = extractTableColumnInfo(resultSetMetaData);//提取表列信息

                DbConsoleTable dbConsoleTable = extractTableDataInfoByResultSet(queryResultSet, result.getLimitSize(), columns);

                dbConsoleTable.setColumns(columns);

                result.setConsumeTime(consumeTime);
                result.setTotalCount(totalCount);
                result.setData(dbConsoleTable);

                for (ResultSet rs : resultSetList) {
                    JdbcUtils.closeResultSet(rs);
                }
                JdbcUtils.closeStatement(statement);
                JdbcUtils.closeConnection(connection);

            } catch (SQLException e) {
                if (timeOuted) {
                    result.setMessage("任务超时，已退出！");
                } else if (taskStatus == TASK_STATUS_STOP) {
                    result.setMessage("任务收到终止命令,已退出！");
                } else if(status!=TASK_STATUS_TIMEOUT){
                	DAOException ex = new DAOException(e);
                    result.setMessage(new SysMessage("task.exec",new String []{ex.getMessage(),ex.getSQLState(),String.valueOf(ex.getErrorCode())}).getMsgInfo());
                    this.taskStatus = TASK_STATUS_ERROR;
                }
            } catch (Exception e) {
                result.setMessage(MessageFormat.format("任务执行过程中出现错误，错误消息【{0}】", e.getMessage()));
                this.taskStatus = TASK_STATUS_ERROR;
            }
            if (this.taskStatus != -1) {
                setStatus(taskStatus);//其它任务状态
            } else {
                result.setMessage("执行成功,花费时间【" + consumeTime + "ms】!");
                setStatus(TASK_STATUS_FINISH);//任务完成
            }
        }

        /*
         * 提起表列信息
         * @param resultSetMetaData
         * @return
         * @throws SQLException
         */
        private List<DbConsoleColumn> extractTableColumnInfo(ResultSetMetaData resultSetMetaData) throws SQLException {
            int columnCount = resultSetMetaData.getColumnCount();
            // 通过ResultSet取得列信息集合
            List<DbConsoleColumn> columns = new ArrayList<DbConsoleColumn>(columnCount);

            for (int i = 0; i < columnCount; i++) {
                int idx = i + 1;
                String name = resultSetMetaData.getColumnName(idx);
                int type = resultSetMetaData.getColumnType(idx);
                String typeName = resultSetMetaData.getColumnTypeName(idx);
                int size = resultSetMetaData.getColumnDisplaySize(idx);
                columns.add(new DbConsoleColumn(name, typeName, VariableHelper.parseString(type), VariableHelper.parseString(size), true));
            }

            return columns;
        }

        public void stopExecutor(int taskStatus) {
            this.taskStatus = taskStatus;
            for (ResultSet rs : resultSetList) {
                JdbcUtils.closeResultSet(rs);
            }
            try {
                statement.cancel();
            } catch (SQLException e) {

            } finally {
                JdbcUtils.closeStatement(statement);
                JdbcUtils.closeConnection(connection);
            }
        }


        /*
         * 提取表数据
         * @param rs
         * @param limit
         * @param columns
         * @return
         * @throws SQLException
         */
        private DbConsoleTable extractTableDataInfoByResultSet(ResultSet rs, int limit, List<DbConsoleColumn> columns) throws SQLException {
            List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>(limit + 10);
            while (rs.next()) {
                Map<String, Object> row = new HashMap<String, Object>();
                for (DbConsoleColumn column : columns) {
                    Object value = readFieldValue(rs, column);
                    row.put(column.getColumnName(), value);
                }
                rows.add(row);
            }
            return new DbConsoleTable(rows);
        }

        /*
         * 从ResultSet中读取 指定列的值
         *
         * @param rs @param column @return @throws SQLException
         */
        private Object readFieldValue(ResultSet rs, DbConsoleColumn column) throws SQLException {
            int type = VariableHelper.parseInt(column.getColumnType());
            String field = column.getColumnName();
            // 读取字段值
            Object value = null;
            if (SqlUtil.isDateType(type)) {
                value = rs.getTimestamp(field);
            } else if (SqlUtil.isNumberType(type)) {
                value = rs.getString(field);
            } else if (SqlUtil.isStringType(type)) {
                value = rs.getString(field);
            } else if (SqlUtil.isBlobType(type)) {
                value = "[LONGVARBINARY]";
            } else if (SqlUtil.isClobType(type)) {
                value = "[LONGVARCHAR]";
            } else {
                value = "[" + SqlUtil.getJdbcTypeName(type) + "]";
            }
            // 处理 NULL 值
            if (rs.wasNull() && !SqlUtil.isNumberType(type)
                    && !SqlUtil.isDateType(type)) {
                value = "[NULL]";
            } else {
                if (SqlUtil.isStringType(type)) {
                    // 检查是否是html内容...
                    if (SqlUtil.isHTMLContent(value.toString())) {
                        value = "[HTML]";
                    }
                }
            }
            return value;
        }
    }

    /**
     * 获得查询处理结果
     *
     * @return 查询处理结果
     */
    public ProcessResult<DbConsoleTable> getResult() {
        return result;
    }

    /**
     * 任务是否已结束
     *
     * @return
     */
    private boolean isFinish() {
        return this.status == TASK_STATUS_FINISH || this.status == TASK_STATUS_ERROR || this.status == TASK_STATUS_STOP || this.status == TASK_STATUS_TIMEOUT;
    }

}
