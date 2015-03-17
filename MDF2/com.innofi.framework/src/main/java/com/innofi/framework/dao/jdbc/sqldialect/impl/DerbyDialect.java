package com.innofi.framework.dao.jdbc.sqldialect.impl;


import com.innofi.framework.dao.pagination.Page;
import com.innofi.component.dbconsole.pojo.DbConsoleColumn;
import com.innofi.framework.utils.string.StringUtil;

import java.sql.Connection;
import java.util.List;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 2012-06-10
 * @found time: 20:40:56
 * Derby数据库实现方言类
 */
public class DerbyDialect extends AbstractDbDialect {

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#support(java.sql.Connection)
     */
    public boolean support(Connection connection) {
        return support(connection, "derby", null);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getTableRenameSql(String, String, String)
     */
    public String getTableRenameSql(String schema, String tableName, String newTableName) {
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
        StringBuilder sql = new StringBuilder(" ALTER TABLE " + tableName + " ADD " + columnName);
        sql.append(this.generateColumnTypeSql(columnType, columnSize));
        sql.append(this.generateCreateDefinitionSql(isnullAble));
        updateNotNull(tableName, columnName, isnullAble, sql);
        return sql.toString();
    }

    private void updateNotNull(String tableName, String columnName, boolean isnullAble, StringBuilder sql) {
        if (!isnullAble) {
            sql.append(";");
            sql.append("ALTER TABLE " + tableName + " ALTER COLUMN " + columnName + " NOT NULL");
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getDropSequenceSql(String, String)
     */
    
    public String getDropSequenceSql(String schema, String sequenceName) {
        if (StringUtil.hasText(schema)) {
            return "DROP SEQUENCE " + schema + "." + sequenceName + " RESTRICT";
        }
        return "DROP SEQUENCE " + sequenceName + " RESTRICT";
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
        boolean isPrimaryKey = newDbConsoleColumn.isIsprimaryKey();
        boolean oldPrimaryKey = oldDbConsoleColumn.isIsprimaryKey();
        String pkName = oldDbConsoleColumn.getPkName();

        List<String> primaryKeys = newDbConsoleColumn.getListPrimaryKey();
        String cType = generateColumnTypeSql(columnType, columnSize);
        StringBuilder sql = new StringBuilder();

        if (!oldColumnName.toLowerCase().equals(newColumnName.toLowerCase())) {
            sql.append(" RENAME COLUMN " + tableName + "." + oldColumnName + " TO " + newColumnName);
        }


        if (!oldDbConsoleColumn.getColumnSize().equals(newDbConsoleColumn.getColumnSize())) {
            sql.append(";");
            sql.append("ALTER TABLE  " + tableName + " ALTER COLUMN " + newColumnName + " SET DATA TYPE " + cType);
        }

        if (isnullAble && oldNullAble == false) {
            sql.append(";");
            sql.append("ALTER TABLE  " + tableName + " ALTER COLUMN  " + newColumnName + "  NULL ");
        } else if (!isnullAble && oldNullAble == true) {
            sql.append(";");
            sql.append("ALTER TABLE  " + tableName + " ALTER COLUMN " + newColumnName + " NOT NULL ");
        }

        if (isPrimaryKey != oldPrimaryKey) {
            if (StringUtil.hasText(pkName)) {
                sql.append(";");
                sql.append("ALTER TABLE " + tableName + " DROP CONSTRAINT " + pkName);
            }
            sql.append(";");
            sql.append(this.generateAlterPrimaryKeySql(tableName, primaryKeys));
        }

        return sql.toString();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getLimitSql(String, com.innofi.framework.dao.pagination.Page)
     */
    public String getLimitSql(String sql, Page page) {
        return getPaginationSql(sql, page.getPageNo(), page.getPageSize());
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getLimitSql(String, int, int)
     */
    
    public String getLimitSql(String sql, int pageIndex, int limitSize) {
        return getPaginationSql(sql, pageIndex, limitSize);
    }

/*
    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#generatorDDL(com.innofi.component.dbconsole.pojo.DbConsoleTable)
     *//*


    public String generatorDDL(DbConsoleTable table) {

    }
*/

    private String generateCreateDefinitionSql(boolean isnullAble) {
        if (isnullAble) {
            return " DEFAULT NULL ";
        }
        return " ";
    }

    private String getRowNumber(String sql) {
        StringBuffer rownumber = new StringBuffer(50).append("ROW_NUMBER() OVER(");
        rownumber.append(") as rownumber_,");
        return rownumber.toString();
    }

    public String getPaginationSql(String sql, int pageNo, int pageSize) {
        sql = sql.toUpperCase();
        int startNo = (pageNo - 1) * pageSize;
        int endNo = pageNo * pageSize;
        int startOfSelect = sql.indexOf("SELECT");
        int startOfFrom = sql.indexOf("FROM");
        int startOfOrderBy = sql.indexOf("ORDER BY");
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100).append(sql.substring(0, startOfSelect)).append("SELECT * FROM ( SELECT ").append(getRowNumber(sql));

        if (hasDistinct(sql)) {
            pagingSelect.append(" row_.* FROM ( ").append(sql.substring(startOfSelect)).append(" ) as row_");
        } else if (sql.indexOf("*") == -1) {
            pagingSelect.append(sql.substring(startOfSelect + 6));
        } else if (startOfOrderBy == -1) {
            pagingSelect.append(sql.substring(startOfFrom + 4) + "." + sql.substring(startOfSelect + 7));
        } else {
            pagingSelect.append(sql.substring(startOfFrom + 4, startOfOrderBy - 1) + "." + sql.substring(startOfSelect + 7));
        }

        pagingSelect.append(" ) as temp_ WHERE rownumber_ ");

        pagingSelect.append("BETWEEN " + startNo + " AND " + endNo);
        return pagingSelect.toString();
    }

    private static boolean hasDistinct(String sql) {
        return (sql.indexOf("SELECT DISTINCT") >= 0);
    }

}
