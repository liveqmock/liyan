package com.innofi.component.dbconsole.service;

import com.innofi.component.dbconsole.PrefsDocument;
import com.innofi.component.dbconsole.executer.QueryTask;
import com.innofi.component.dbconsole.pojo.*;
import com.innofi.framework.dao.pagination.Page;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy
 * @version 2.0
 *          <p/>
 *          数据库控制台服务
 * @found date: 2012-5-20
 * @found time: 下午3:47:07
 */
public interface IDbConsoleService {

    /**
     * 加载当前数据库信息
     *
     * @param groupId  工作组
     * @param connName 连接名称
     * @return 当前数据库信息
     */
    public DbConsoleDbInfo loadDbInfo(String groupId, String connName);

    /**
     * 加载登录用户保存的工作组
     *
     * @return 工作组列表
     */
    public List<DbConsoleGroup> loadGroups();

    /**
     * 根据工作组编号获取组下连接列表
     *
     * @param groupId 工作组编号
     * @return 连接列表
     */
    public List<DbConsoleConnection> loadConnections(String groupId);

    /**
     * 加载数据库模板列表
     *
     * @return 模板列表
     */
    public List<DbConsoleTemplate> loadDbTemplates();

    /**
     * 加载数据库对象类型
     * @param groupId    工作组编号
     * @param connName   连接名称
     * @return 返回对象类型列表
     */
    public List<DbConsoleObjectType> loadObjectTypes(String groupId, String connName);

    /**
     * 加载数据库模式,加载*
     *
     * @param groupId  工作组编号
     * @param connName 数据库连接名称
     * @return 当前数据库中的Schemes
     */
    public List<DbConsoleSchema> loadSchemas(String groupId, String connName);

    /**
     * 加载数据库模式，不加载*
     *
     * @param groupId  工作组编号
     * @param connName 数据库连接名称
     * @return 当前数据库中的Schemes
     */
    public List<DbConsoleSchema> loadSchemasForSqlEditor(String groupId, String connName);


    /**
     * 加载数据库当中Table、View、Sequence对象
     *
     * @param groupId    工作组编号
     * @param connName   连接名称
     * @param schema     schema名称
     * @param objectName 对象名称
     * @param types      对象类型
     * @return 符合条件的对象列表
     */
    public List<DbConsoleTable> loadObjects(String groupId, String connName, String schema, String objectName, String types);

    /**
     * 加载数据库当指定 schema 下的 Table
     *
     * @param groupId  工作组编号
     * @param connName 连接名称
     * @param schema   schema名称
     * @return 符合条件的对象列表
     */
    public List<DbConsoleTable> loadTablesBySchema(String groupId, String connName, String schema);

    /**
     * 加载具体某个sequence
     *
     * @param groupId  工作组编号
     * @param connName 连接名称
     * @param schema   schema名称
     * @param seqName  sequence名称
     * @return
     */
    public DbConsoleSequence loadSequence(String groupId, String connName, String schema, String seqName);

    /**
     * 根据指定表名加载表中所有列
     *
     * @param groupId   工作组编号
     * @param connName  连接名称
     * @param schema    schema名称
     * @param tableName 数据表名称
     * @return 该表中所有列对象
     */
    public List<DbConsoleColumn> loadColumns(String groupId, String connName, String schema, String tableName);

    /**
     * 查找默认配置的数据库类型
     *
     * @param groupId  工作组编号
     * @param connName 连接名称
     * @return 返回默认配置的数据库类型的集合
     */
    public List<DbConsoleColumn> loadColumnType(String groupId, String connName);

    /**
     * 加载对象DDL
     *
     * @param groupId    工作组编号
     * @param connName   连接名称
     * @param type       类型 TABLE,VIEW,SEQUENCE
     * @param schema     模式名称
     * @param objectName 对象名称
     * @return 返回对象DDL语句
     */
    public String loadObjectDDL(String groupId, String connName, String type, String schema, String objectName);

    /**
     * 插入表新列
     *
     * @param groupId    工作组
     * @param connName   连接名称
     * @param schema     模式
     * @param tableName  表名
     * @param columnInfo 列对象
     */
    public void insertColumn(String groupId, String connName, String schema, String tableName, DbConsoleColumn columnInfo);


    /**
     * 更新表列
     *
     * @param groupId       工作组
     * @param connName      连接名称
     * @param schema        模式
     * @param tableName     表名
     * @param oldColumnInfo 修改之前列对象
     * @param newColumnInfo 新列对象
     */
    public void updateColumn(String groupId, String connName, String schema, String tableName, DbConsoleColumn oldColumnInfo, DbConsoleColumn newColumnInfo);

    /**
     * 删除表列
     *
     * @param groupId    工作组
     * @param connName   连接名称
     * @param schema     模式
     * @param tableName  表名
     * @param columnName 列名
     */
    public void dropColumn(String groupId, String connName, String schema, String tableName, String columnName);

    /**
     * 格式化给定SQL
     *
     * @param sql 需要格式化的SQL语句
     * @return 格式化后的SQL语句
     */
    public String formatSql(String sql);

    /**
     * 测试数据库连接是否可以连通
     *
     * @param groupId  工作组编号
     * @param connName 数据库连接名称
     * @return 不成功返回错误信息，成功返回null
     */
    public String testConnection(String groupId, String connName);

    /**
     * 获得数据库控制台配置选项
     * @return
     */
    public PrefsDocument getPrefsDocument();

    /**
     * 保存工作组及组中的数据库连接信息
     *
     * @param prefsDocument 数据库控制台配置选项文档
     */
    public void saveConfig(com.innofi.component.dbconsole.PrefsDocument prefsDocument) throws IOException;

    /**
     * 打开数据连接，会将连接对应的数据源缓存起来
     *
     * @param groupId  工作组编号
     * @param connName 数据库连接名称
     * @return 不成功返回错误信息，成功返回null
     */
    public String openConnection(String groupId, String connName);

    /**
     * 修改表名称
     *
     * @param groupId      工作组
     * @param connName     连接名称
     * @param schema       模式 可选参数
     * @param tableName    原表名
     * @param newTableName 新表名
     */
    public void renameTableName(String groupId, String connName, String schema, String tableName, String newTableName);


    /**
     * 修改SeqName
     *
     * @param groupId    工作组
     * @param connName   连接名称
     * @param schema     模式 可选参数
     * @param seqName    原序列名
     * @param newSeqName 新序列名
     */
    public void renameSequence(String groupId, String connName, String schema, String seqName, String newSeqName);

    /**
     * 重置sequence值
     *
     * @param groupId  工作组
     * @param connName 连接名称
     * @param schema   模式 可选参数
     * @param seqName  原序列名
     */
    public void resetSequence(String groupId, String connName, String schema, String seqName, String newSeqValue);

    /**
     * 删除指定表
     *
     * @param groupId   工作组
     * @param connName  连接名称
     * @param schema    模式 可选参数
     * @param tableName 指定表名
     */
    public boolean dropTable(String groupId, String connName, String schema, String tableName);

    /**
     * 清空表内数据
     *
     * @param groupId   工作组
     * @param connName  连接名称
     * @param schema    模式 可选参数
     * @param tableName 指定表名
     */
    public void clearTableData(String groupId, String connName, String schema, String tableName);

    /**
     * 删除指定表
     *
     * @param groupId      工作组
     * @param connName     连接名称
     * @param schema       模式 可选参数
     * @param sequenceName 指定Sequence名称
     */
    public boolean dropSequence(String groupId, String connName, String schema, String sequenceName);

    /**
     * 执行查询，异步处理方法
     *
     * @param groupId   工作组
     * @param connName  连接名称
     * @param querySql  查询sql语句
     * @param pageNo    当前页面编号
     * @param limitSize 提取页数
     */
    public String executeQuery(String groupId, String connName, String querySql, int pageNo, int limitSize);

    /**
     * 执行更新语句
     *
     * @param groupId     工作组编号
     * @param connName    连接名称
     * @param reader      Sql脚本
     * @param stopOnError 出错是否停止
     * @param autoCommit  是否自动提交
     */
    public ProcessResult<String[]> execute(String groupId, String connName, Reader reader, boolean stopOnError, boolean autoCommit);

    /**
     * 获取当前查询状态
     *
     * @return 任务的token
     */
    public String getQueryStatus(String token);

    /**
     * 加载查询结果
     *
     * @param token 任务的token
     * @return
     */
    public List<Map<String, Object>> loadTaskQueryData(String token);

    /**
     * 加载任务结果表格信息
     *
     * @param token 任务的token
     * @return
     */
    public ProcessResult<DbConsoleTable> getTaskResultMetaInfo(String token);


    /**
     * 停止任务执行
     *
     * @param token 任务的token
     */
    public void stopQuery(String token);

    /**
     * 添加任务
     *
     * @param queryTask
     */
    public String addTask(QueryTask queryTask);

    /**
     * 删除任务
     *
     * @param token
     * @return
     */
    public QueryTask removeTask(String token);

    /**
     * 创建新的数据表
     *
     * @param groupId   工作组编号
     * @param connName  连接名称
     * @param tableName 新表名称
     * @return
     */
    public int createNewTable(String groupId, String connName, String tableName);

    /**
     * 添加数据浏览器记录sql
     *
     * @param tableName
     * @param map
     * @throws Exception
     */
    public SqlWrapper getInsertDataSql(String tableName, Map<String, Object> map) throws Exception;

    /**
     * 更新数据浏览器记录sql
     *
     * @param tableName
     * @param map
     * @param oldMap
     * @throws Exception
     */
    public SqlWrapper getUpdateDataSql(String tableName, Map<String, Object> map, Map<String, Object> oldMap) throws Exception;

    /**
     * 删除数据浏览器记录sql
     *
     * @param tableName
     * @param oldMap
     * @throws Exception
     */
    public SqlWrapper getDeleteDataSql(String tableName, Map<String, Object> oldMap) throws Exception;

    /**
     * 加载数据表数据
     *
     * @param page 分页对象
     * @param map  查询参数
     * @throws Exception
     */
    public void loadQueryTableData(Page page, Map<String, Object> map) throws Exception;

    /**
     * 对sqlwrapper进行更新
     *
     * @param groupId        组编号
     * @param connName       数据库连接名称
     * @param listSqlWrapper sql包装类
     * @return 返回更新的数量
     * @throws Exception
     */
    public int[] updateSql(String groupId, String connName, final List<SqlWrapper> listSqlWrapper);

}
