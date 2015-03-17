package com.innofi.framework.dao.jdbc.sqldialect.impl;

import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.exception.FrameworkJdbcRuntimeException;
import com.innofi.component.dbconsole.pojo.DbConsoleColumn;

import java.sql.Connection;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 2012-06-10
 * @found time: 23:55:59
 * Infomix数据库实现方言类
 */
public class InformixDialect extends AbstractDbDialect {

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#support(java.sql.Connection)
     */
    public boolean support(Connection connection) {
        return support(connection, "Informix Dynamic Server", null);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getTableRenameSql(String , String, String)
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
        // TODO Auto-generated method stub
        throw new RuntimeException("暂不支持");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getUpdateColumnSql(com.innofi.component.dbconsole.pojo.DbConsoleColumn, com.innofi.component.dbconsole.pojo.DbConsoleColumn)
     */
    public String getUpdateColumnSql(DbConsoleColumn oldDbConsoleColumn,
                                     DbConsoleColumn newDbConsoleColumn) {
        // TODO Auto-generated method stub
        throw new RuntimeException("暂不支持");
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
        return getLimitSql(sql,pageIndex,limitSize);
    }


    private String getPaginationSql(String sql, int pageNo, int pageSize) {
        if (pageNo < 1) {
            throw new RuntimeException("page no 不能小于1");
        }
        int startNo = (pageNo - 1) * pageSize;
        sql = sql.trim().toLowerCase();
        final String select = "select";
        int indexOfSelect = sql.indexOf(select);
        if (indexOfSelect != -1) {
            indexOfSelect += select.length();
            return sql.substring(0, indexOfSelect + 1) + " skip " + startNo
                    + " first " + pageSize + sql.substring(indexOfSelect);
        }
        throw new RuntimeException("未找到select");
    }


}
