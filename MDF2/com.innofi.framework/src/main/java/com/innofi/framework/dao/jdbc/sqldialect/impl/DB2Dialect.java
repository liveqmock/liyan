package com.innofi.framework.dao.jdbc.sqldialect.impl;


import com.innofi.framework.dao.pagination.Page;
import com.innofi.component.dbconsole.pojo.DbConsoleColumn;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;

import java.io.StringReader;
import java.sql.Connection;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2012-06-10
 * @found time: 20:40:56
 * DB2数据库实现方言类
 */
public class DB2Dialect extends AbstractDbDialect {

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#support(java.sql.Connection)
     */
    public boolean support(Connection connection) {
        return support(connection, "db2", null);
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
        if (!isnullAble) {
            sql.append(";");
            sql.append("ALTER TABLE " + tableName + " ALTER COLUMN " + columnName + " SET NOT NULL");
        }
        return sql.toString();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getUpdateColumnSql(com.innofi.component.dbconsole.pojo.DbConsoleColumn, com.innofi.component.dbconsole.pojo.DbConsoleColumn)
     */
    public String getUpdateColumnSql(DbConsoleColumn oldDbConsoleColumn, DbConsoleColumn newDbConsoleColumn) {
        return null;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getDropSequenceSql(String, String)
     */

    public String getDropSequenceSql(String schema, String sequenceName) {
        if (StringUtil.hasText(schema)) {
            return "DROP SEQUENCE " + schema + "." + sequenceName;
        }
        return "DROP SEQUENCE " + sequenceName;
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

    private String generateCreateDefinitionSql(boolean isnullAble) {
        if (isnullAble) {
            return " NULL ";
        }
        return " ";
    }

    public String getPaginationSql(String sql, int pageNo, int pageSize) {
        int startNo = (pageNo - 1) * pageSize;
        int endNo = pageNo * pageSize;

        if (startNo == 0) {
            return sql + " fetch first " + pageSize + " rows only";
        }

        CCJSqlParserManager sqlParserManager = new CCJSqlParserManager();//SQL解析器
        Select select = null;
		try {
			select = (Select) sqlParserManager.parse(new StringReader(sql));
		} catch (JSQLParserException e) {
			e.printStackTrace();
		}
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        
        StringBuffer sqlBuffer = new StringBuffer(500);
        sqlBuffer.append("select * from (select a.*,rownumber() over(");
        if(Validator.isNotNull(plainSelect.getOrderByElements())){
        	sqlBuffer.append("order by "+plainSelect.getOrderByElements().toString());
        }
        sqlBuffer.append(" ) as rowid from (");

        sqlBuffer.append(" select " + plainSelect.getSelectItems().toString());
        sqlBuffer.append(" from " +plainSelect.getFromItem().toString());
        if (null != plainSelect.getJoins()) {
        	for(Object o : plainSelect.getJoins()){
                Join join = (Join) o;
                sqlBuffer.append(" " + join.toString() + " ");
        	}
        }
        if (null != plainSelect.getWhere()) {
        	sqlBuffer.append(" WHERE " + plainSelect.getWhere());
        }

        if (null != plainSelect.getGroupByColumnReferences()) {
        	sqlBuffer.append(" GROUP BY " + plainSelect.getGroupByColumnReferences().toString());
        }

        if (null != plainSelect.getHaving()) {
        	sqlBuffer.append(" HAVING " + plainSelect.getHaving().toString());
        }
        
    	sqlBuffer.append(") a) tmp where tmp.rowid >="+startNo+" and tmp.rowid <= "+endNo);
    	
        sql = StringUtil.replace(sqlBuffer.toString(), "[", "");
        sql = StringUtil.replace(sql, "]", "");

        return sql;
    }

    private static boolean hasDistinct(String sql) {
        return (sql.toLowerCase().indexOf("select distinct") >= 0);
    }

}
