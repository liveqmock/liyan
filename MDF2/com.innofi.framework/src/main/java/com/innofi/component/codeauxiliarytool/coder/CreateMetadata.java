package com.innofi.component.codeauxiliarytool.coder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.innofi.framework.pojo.metadata.MdField;
import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.utils.variable.VariableHelper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.innofi.component.codeauxiliarytool.coder.domain.ColumnInfo;
import com.innofi.component.codeauxiliarytool.coder.domain.TableInfo;
import com.innofi.component.metadata.field.service.IMdFieldService;
import com.innofi.component.metadata.table.service.IMdTableService;
import com.innofi.framework.dao.DaoSupport;

/**
 * 创建元数据管理
 * Copyright (c) 2012 正信嘉华管理顾问有限公司
 * all rights reserved.
 * author: puxuanguo
 * found date: 2012-12-1
 * found time: 11:25:38
 */

@Component("createMetadata")
public class CreateMetadata {

    @Resource
    private IMdFieldService mdFieldServcie;

    @Resource
    private IMdTableService mdTableServcie;

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    DaoSupport daoSupport;

    public DaoSupport getDaoSupport() {
        return daoSupport;
    }


    /**
     * 检查字段元数据是否存在
     *
     * @param tableName 表名称
     * @return true存在，false不存在
     */
    @Expose
    public String checkExsitFieldMetaData(String tableName) {
        MdTable tablePojo = mdTableServcie.findLastVersionMdTableByTableName(tableName);
        return VariableHelper.parseString(tablePojo != null);
    }

    /**
     * 查询所有的表信息
     *
     * @return 返回TableInfo的集合
     * @throws Exception
     */
    @DataProvider
    public Collection<TableInfo> loadTableInfos() throws Exception {

        List<TableInfo> tablesList = new ArrayList<TableInfo>();
        DataSource ds = daoSupport.getJdbcDao().getDataSource();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DataSourceUtils.getConnection(ds);
            DatabaseMetaData metaData = conn.getMetaData();
            String url = metaData.getURL();
            String schema = null;
            if (url.toLowerCase().contains("oracle")) {
                schema = metaData.getUserName();
            }
            rs = metaData.getTables(null, schema, "%", new String[]{"TABLE"});
            TableInfo tableInfo = null;
            while (rs.next()) {
                tableInfo = new TableInfo();

                if (rs.getString("TABLE_NAME").indexOf("BIN$") == -1) {
                    tableInfo.setTableName(rs.getString("TABLE_NAME"));
                    tablesList.add(tableInfo);
                }
            }
            return tablesList;
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeConnection(conn);
        }
    }

    /*
     * 检查是否唯一
     */
    @Expose
    public boolean checkIsUnique(Map<String, Object> parameter) throws ClassNotFoundException {

        List list = getDaoSupport().getJdbcDao().queryForList("select " + parameter.get("fieldName").toString() + " from " + parameter.get("tableName").toString() + " where" +
                " " + parameter.get("fieldName").toString() + " ='" + parameter.get("fieldValue").toString() + "'");
        if (list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询表的列信息
     *
     * @param tableName
     * @return 返回列信息的集合
     * @throws Exception
     */
    @DataProvider
    public Collection<ColumnInfo> loadColumnInfos(String tableName) throws Exception {

        String comments = "";
        List<ColumnInfo> list = new ArrayList<ColumnInfo>();
        if (StringUtils.hasText(tableName)) {
            list = this.findMultiColumnInfos("select * from " + tableName);
            List<String> primaryKeys = this.findTablePrimaryKeys(tableName);
            for (ColumnInfo ci : list) {
                ci.setIsprimaryKey(FrameworkConstants.COMM_N);
                ci.setTableName(tableName);
                comments = this.findCommentInfo(ci.getColumnName(), tableName);
                ci.setRemarks(comments);
                ci.setValidators("无验证");
                for (String key : primaryKeys) {
                    if (key.toLowerCase().equals(ci.getColumnName().toLowerCase())) {
                        ci.setIsprimaryKey(FrameworkConstants.COMM_Y);
                    }
                }
            }
        }
        return list;

    }

    /*
    * 把数据库表插入到field表中进行管理
    */
    @Expose
    public void saveColumnInfos(Map<String, Object> parameter) {
        Collection<ColumnInfo> entityList = (Collection<ColumnInfo>) parameter.get("ds");
        String tableName = (String) parameter.get("tableName");

        MdTable mdTable = mdTableServcie.findLastVersionMdTableByTableName(tableName);

        //创建表元数据
        if (mdTable == null) {
            mdTable = new MdTable();
            mdTable.setTableName(tableName);
            mdTable.setVerNo(VariableHelper.parseBigDecimal(1));
            mdTableServcie.save(mdTable);
        }

        String tableId = mdTable.getId();

        List<MdField> mdFieldPojos = mdFieldServcie.findMdFieldsByTableId(tableId);

        //不为null，删除
        if (mdFieldPojos.size() > 0) {
            for (MdField mdFieldPojo : mdFieldPojos) {
                mdFieldServcie.delete(mdFieldPojo);
            }
        }

        //创建字段元数据
        for (ColumnInfo col : entityList) {
            MdField mdField = new MdField();
            mdField.setTableId(tableId);
            mdField.setTableName(col.getTableName());
            mdField.setFieldName(col.getColumnName());
            mdField.setFieldDesc(col.getRemarks());
            mdField.setFieldType(col.getColumnType());
            mdField.setFieldLen(VariableHelper.parseBigDecimal(col.getColumnSize() == null ? "0" : col.getColumnSize()));
            mdField.setIsNull(col.getIsnullAble());
            mdField.setIsPk(col.getIsprimaryKey());
            mdFieldServcie.save(mdField);
        }

    }

    /*
    * 查找表的列信息
    */
    public List<ColumnInfo> findMultiColumnInfos(String sql) throws Exception {
        if (!StringUtils.hasText(sql)) {
            return null;
        }
        DataSource ds = daoSupport.getJdbcDao().getDataSource();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DataSourceUtils.getConnection(ds);
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            List<ColumnInfo> columnNames = new ArrayList<ColumnInfo>();
            ColumnInfo columnInfo = null;
            for (int i = 1; i <= count; i++) {
                columnInfo = new ColumnInfo();

                columnInfo.setColumnName(rsmd.getColumnLabel(i));
                columnInfo.setColumnType(rsmd.getColumnTypeName(i));
                columnInfo.setTableName(rsmd.getTableName(i));
                if (rsmd.getPrecision(i) > 0 && !columnInfo.getColumnType().equals("DATETIME")
                        && !columnInfo.getColumnType().equals("TIMESTAMP")
                        && !columnInfo.getColumnType().equals("DATE")) {
                    columnInfo.setColumnSize(String.valueOf(rsmd.getPrecision(i)));
                }
                if (rsmd.getScale(i) > 0 && !columnInfo.getColumnType().equals("DATETIME")
                        && !columnInfo.getColumnType().equals("TIMESTAMP")
                        && !columnInfo.getColumnType().equals("DATE")) {
                    columnInfo.setColumnSize(columnInfo.getColumnSize() + "," + rsmd.getScale(i));
                }
                int flagI = rsmd.isNullable(i);
                if (flagI == 0) {
                    columnInfo.setIsnullAble(FrameworkConstants.COMM_N);
                } else {
                    columnInfo.setIsnullAble(FrameworkConstants.COMM_Y);
                }
                columnNames.add(columnInfo);
            }
            return columnNames;
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(st);
            DataSourceUtils.releaseConnection(conn,ds);
        }
    }

    /*
      * 获取oracle 备注字段 puxuanguo
      */
    public String findCommentInfo(String column, String tableName) throws Exception {

        String commments = "";
        List rows = daoSupport.getJdbcDao().queryForList("select comments from user_col_comments where table_name ='" + tableName
                + "' and column_name ='" + column + "'");
        Iterator it = rows.iterator();
        while (it.hasNext()) {
            Map userMap = (Map) it.next();
            if (StringUtils.hasText((String) userMap.get("comments"))) {
                commments = userMap.get("comments").toString();
            }
        }
        return commments;
    }

    /*
      * 查询主键
      */
    public List<String> findTablePrimaryKeys(String tableName) throws Exception {
        List<String> primaryKeys = new ArrayList<String>();
        Connection con = null;
        ResultSet rs = null;
        try {
            DataSource ds = daoSupport.getJdbcDao().getDataSource();
            con = ds.getConnection();
            DatabaseMetaData metaData = con.getMetaData();
            rs = metaData.getPrimaryKeys(null, null, tableName.toUpperCase());
            while (rs.next()) {
                primaryKeys.add(rs.getString("COLUMN_NAME").toUpperCase());
            }
            return primaryKeys;
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeConnection(con);
        }
    }

}
