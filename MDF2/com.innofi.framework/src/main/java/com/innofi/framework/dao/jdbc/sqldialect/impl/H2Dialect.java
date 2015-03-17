package com.innofi.framework.dao.jdbc.sqldialect.impl;

import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.exception.FrameworkJdbcRuntimeException;
import com.innofi.component.dbconsole.pojo.DbConsoleColumn;

import java.sql.Connection;
import java.util.List;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 2012-06-10
 * @found time: 23:55:59
 * H2数据库实现方言类
 */
public class H2Dialect extends AbstractDbDialect {

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#support(java.sql.Connection)
     */
    public boolean support(Connection connection) {
        return support(connection, "h2", null);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getTableRenameSql(String , String , String )
     */
    public String getTableRenameSql(String schema , String tableName, String newTableName) {
        return "RENAME TABLE " + tableName + " TO " + newTableName;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getNewColumnSql(com.innofi.component.dbconsole.pojo.DbConsoleColumn)
     */
    public String getNewColumnSql(DbConsoleColumn dbConsoleColumn) {
        String tableName = dbConsoleColumn.getTableName();
        String columnName = dbConsoleColumn.getColumnName();
        String columnType = dbConsoleColumn.getColumnType();
        String columnSize = dbConsoleColumn.getColumnSize();
        boolean isnullAble = dbConsoleColumn.isIsnullAble();
        boolean isprimaryKey = dbConsoleColumn.isIsprimaryKey();
        List<String> primaryKeys = dbConsoleColumn.getListPrimaryKey();
        StringBuilder sql = new StringBuilder(" alter table " + tableName + " add " + columnName);
        sql.append(this.generateColumnTypeSql(columnType, columnSize));
        sql.append(this.generateCreateDefinitionSql(isnullAble));
        if (isprimaryKey) {
            if (primaryKeys.size() == 1) {
                sql.append(";");
                sql.append(this.generateAlterPrimaryKeySql(tableName, primaryKeys));
            } else {
                sql.append(";");
                sql.append(this.generateDropPrimaryKeySql(tableName));
                sql.append(";");
                sql.append(this.generateAlterPrimaryKeySql(tableName, primaryKeys));
            }
        }
        return sql.toString();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getUpdateColumnSql(com.innofi.component.dbconsole.pojo.DbConsoleColumn, com.innofi.component.dbconsole.pojo.DbConsoleColumn)
     */
    public String getUpdateColumnSql(DbConsoleColumn oldDbConsoleColumn, DbConsoleColumn newDbConsoleColumn) {
        String tableName = newDbConsoleColumn.getTableName();
        String newColumnName = newDbConsoleColumn.getColumnName();
        String oldColumnName = oldDbConsoleColumn.getColumnName();
        String columnType = newDbConsoleColumn.getColumnType();
        String columnSize = newDbConsoleColumn.getColumnSize();
        boolean isnullAble = newDbConsoleColumn.isIsnullAble();
        boolean isprimaryKey = newDbConsoleColumn.isIsprimaryKey();
        boolean oldPrimaryKey = oldDbConsoleColumn.isIsprimaryKey();
        List<String> primaryKeys = newDbConsoleColumn.getListPrimaryKey();
        String cType = this.generateColumnTypeSql(columnType, columnSize);
        StringBuilder sql = new StringBuilder();
        if (!oldColumnName.equals(newColumnName)) {
            sql.append(" ALTER TABLE " + tableName + " ALTER COLUMN  " + oldColumnName + " RENAME to " + newColumnName);
        }
        sql.append(";");
        sql.append("ALTER TABLE  " + tableName + " ALTER COLUMN   " + newColumnName + cType);
        if (!isnullAble) {
            sql.append(" NOT NULL ");
        }
        if (isprimaryKey != oldPrimaryKey) {
            if (primaryKeys.size() == 1 && isprimaryKey == true) {
                sql.append(";");
                sql.append(this.generateAlterPrimaryKeySql(tableName, primaryKeys));
            } else {
                sql.append(";");
                sql.append(this.generateDropPrimaryKeySql(tableName));
                sql.append(";");
                sql.append(this.generateAlterPrimaryKeySql(tableName, primaryKeys));
            }

        }
        if (isnullAble) {
            sql.append(";");
            sql.append("ALTER TABLE  " + tableName + " ALTER COLUMN   " + newColumnName + " SET NULL");
        }
        return sql.toString();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getTableRenameSql(String,String, String)
     */
    
    public String getDropSequenceSql(String schema, String sequenceName) {
        throw new FrameworkJdbcRuntimeException("H2不支持此操作!");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getLimitSql(String, com.innofi.framework.dao.pagination.Page)
     */
    public String getLimitSql(String sql, Page page) {
        return this.getPaginationSql(sql, page.getPageNo(), page.getPageSize());
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getLimitSql(String , int , int)
     */
    
    public String getLimitSql(String sql, int pageIndex, int limitSize) {
        return getPaginationSql(sql,pageIndex,limitSize);
    }

    private String generateCreateDefinitionSql(boolean isnullAble) {
        if (isnullAble) {
            return "  NULL ";
        } else {
            return " NOT NULL ";
        }
    }

    private String generateDropPrimaryKeySql(String tableName) {
        return " ALTER TABLE " + tableName + " DROP PRIMARY KEY";
    }

    private String getPaginationSql(String sql, int pageNo, int pageSize) {
        int startNo = (pageNo - 1) * pageSize;
        return sql + " LIMIT " + startNo + "," + pageSize;
    }

}
