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
 * SQLServer2008数据库实现方言类
 */
public class SQLServer2005Dialect extends AbstractDbDialect {
    private String DISTINCT = "distinct";
    private String SELECT = "select";
    private String FROM = "from";

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#support(java.sql.Connection)
     */
    public boolean support(Connection connection) {
        return support(connection, "sql server", "9");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getTableRenameSql(String, String, String)
     */
    public String getTableRenameSql(String schema, String tableName, String newTableName) {
        return " EXEC SP_RENAME '" + tableName + "', '" + newTableName + "'";
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
        String pkName = dbConsoleColumn.getPkName();
        List<String> primaryKeys = dbConsoleColumn.getListPrimaryKey();
        StringBuilder sql = new StringBuilder(" alter table " + tableName + " add " + columnName);
        String columnTypeSql = this.generateColumnTypeSql(columnType, columnSize);
        sql.append(columnTypeSql);
        if (!isnullAble) {
            sql.append(";");
            sql.append("ALTER TABLE  " + tableName + " ALTER COLUMN " + columnName + columnTypeSql + " NOT NULL ");
        }
        if (isprimaryKey) {
            if (primaryKeys.size() == 1) {
                sql.append(";");
                sql.append(this.generateAlterPrimaryKeySql(tableName, primaryKeys));
            } else {
                sql.append(";");
                sql.append(this.generateDropPrimaryKeySql(tableName, pkName));
                sql.append(";");
                sql.append(this.generateAlterPrimaryKeySql(tableName, primaryKeys));
            }
        }
        return sql.toString();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getLimitSql(String, int, int)
     */
    
    public String getLimitSql(String sql, int pageIndex, int limitSize) {
        return getPaginationSql(sql, pageIndex, limitSize);
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
        boolean oldNullAble = oldDbConsoleColumn.isIsnullAble();
        boolean isprimaryKey = newDbConsoleColumn.isIsprimaryKey();
        boolean oldPrimaryKey = oldDbConsoleColumn.isIsprimaryKey();
        List<String> primaryKeys = newDbConsoleColumn.getListPrimaryKey();
        String pkName = newDbConsoleColumn.getPkName();
        String cType = this.generateColumnTypeSql(columnType, columnSize);
        StringBuilder sql = new StringBuilder();
        if (!oldColumnName.equals(newColumnName)) {
            sql.append(" sp_rename '" + tableName + "." + oldColumnName + "','" + newColumnName + "','column'");
        }
        sql.append(";");
        if (isnullAble && oldNullAble == false && isprimaryKey != true) {
            sql.append("ALTER TABLE  " + tableName + " ALTER COLUMN " + newColumnName + cType + " NOT NULL ");
        } else {
            sql.append("ALTER TABLE  " + tableName + " ALTER COLUMN " + newColumnName + cType + this.generateCreateDefinitionSql(isnullAble));
        }
        if (isprimaryKey != oldPrimaryKey) {
            if (primaryKeys.size() == 1 && isprimaryKey == true) {
                sql.append(";");
                sql.append(this.generateAlterPrimaryKeySql(tableName, primaryKeys));
            } else {
                sql.append(";");
                sql.append(this.generateDropPrimaryKeySql(tableName, pkName));
                sql.append(";");
                sql.append(this.generateAlterPrimaryKeySql(tableName, primaryKeys));
            }

        }
        if (isnullAble && oldNullAble == false) {
            sql.append(";");
            sql.append("ALTER TABLE  " + tableName + " ALTER COLUMN " + newColumnName + cType + "  NULL ");
        } else if (!isnullAble && oldNullAble == true && isprimaryKey == false) {
            sql.append(";");
            sql.append("ALTER TABLE  " + tableName + " ALTER COLUMN " + newColumnName + cType + " NOT NULL ");
        }
        return sql.toString();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getTableRenameSql(String, String, String)
     */
    
    public String getDropSequenceSql(String schema, String sequenceName) {
        throw new FrameworkJdbcRuntimeException("SqlServer不支持此操作!");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getLimitSql(String, com.innofi.framework.dao.pagination.Page)
     */
    public String getLimitSql(String sql, Page page) {
        return this.getPaginationSql(sql, page.getPageNo(), page.getPageSize());
    }

    private String generateCreateDefinitionSql(boolean isnullAble) {
        StringBuilder sql = new StringBuilder(" ");
        if (isnullAble) {
            sql.append(" NULL ");
        } else {
            sql.append(" NOT NULL ");
        }
        return sql.toString();
    }

    private String generateDropPrimaryKeySql(String tableName, String pkName) {
        return "ALTER TABLE " + tableName + " DROP CONSTRAINT " + pkName;
    }

    private void replaceDistinctWithGroupBy(StringBuilder sql) {
        int distinctIndex = sql.indexOf(DISTINCT);
        if (distinctIndex > 0) {
            sql.delete(distinctIndex, distinctIndex + DISTINCT.length() + 1);
            sql.append(" group by").append(getSelectFieldsWithoutAliases(sql));
        }
    }

    private CharSequence getSelectFieldsWithoutAliases(StringBuilder sql) {
        String select = sql.substring(sql.indexOf(SELECT) + SELECT.length(), sql.indexOf(FROM));

        // Strip the as clauses
        return stripAliases(select);
    }

    private String stripAliases(String str) {
        return str.replaceAll("\\sas[^,]+(,?)", "$1");
    }

    private void insertRowNumberFunction(StringBuilder sql, CharSequence orderby) {
        // Find the end of the select statement
        //int selectEndIndex = sql.indexOf(SELECT) + SELECT.length();
        // Insert after the select statement the row_number() function:
        //sql.insert(selectEndIndex, " ROW_NUMBER() OVER (" + orderby + ") as __hibernate_row_nr__,");
        int fromStartIndex = sql.indexOf(FROM);
        sql.insert(fromStartIndex, " ,ROW_NUMBER() OVER (" + orderby + ") as __hibernate_row_nr__ ");
    }

    private String getPaginationSql(String sql, int pageNo, int pageSize) {
        int startNo = (pageNo - 1) * pageSize + 1;
        int endNo = pageNo * pageSize;
        StringBuilder sb = new StringBuilder(sql.trim().toLowerCase());

        int orderByIndex = sb.indexOf("order by");
        CharSequence orderby = orderByIndex > 0 ? sb.subSequence(orderByIndex, sb.length())
                : "ORDER BY CURRENT_TIMESTAMP";

        // Delete the order by clause at the end of the query
        if (orderByIndex > 0) {
            sb.delete(orderByIndex, orderByIndex + orderby.length());
        }

        replaceDistinctWithGroupBy(sb);

        insertRowNumberFunction(sb, orderby);

        // Wrap the query within a with statement:
        sb.insert(0, "WITH query AS (").append(") SELECT * FROM query ");
        sb.append("WHERE __hibernate_row_nr__ BETWEEN " + startNo + " AND " + endNo + "");
        return sb.toString();
    }


}
