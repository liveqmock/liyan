package com.innofi.framework.dao.jdbc.sqldialect.impl;

import com.innofi.framework.dao.jdbc.IJdbcDao;
import com.innofi.framework.dao.jdbc.sqldialect.DBDialect;
import com.innofi.component.dbconsole.pojo.DbConsoleColumn;
import com.innofi.component.dbconsole.pojo.DbConsoleFk;
import com.innofi.component.dbconsole.pojo.DbConsolePk;
import com.innofi.component.dbconsole.pojo.DbConsoleTable;
import com.innofi.framework.utils.sql.SqlUtil;
import com.innofi.framework.utils.string.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 2012-06-10
 * @found time: 23:55:59
 * 定义一个抽象数据库方言，所有的方言实现继承此类
 */
public abstract class AbstractDbDialect implements DBDialect {

    private static final Logger _log = LoggerFactory.getLogger(AbstractDbDialect.class);

    /**
     * 判断是否是支持的数据库类型
     *
     * @param connection     数据连接
     * @param dbProductName  数据库名称
     * @param dbMajorVersion 数据版本号
     * @return 返回当前方言是否支持当前数据库
     */
    public boolean support(Connection connection, String dbProductName, String dbMajorVersion) {
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String databaseProductName = databaseMetaData.getDatabaseProductName();
            int databaseMajorVersion = databaseMetaData.getDatabaseMajorVersion();
            if(_log.isDebugEnabled()){
                _log.debug(String.format("{databaseProductName=%s,databaseMajorVersion=%s}", databaseProductName, databaseMajorVersion));
            }
            boolean containsMysql = StringUtils.containsIgnoreCase(databaseProductName, dbProductName);
            if (StringUtils.isNotEmpty(dbMajorVersion)) {
                return containsMysql && Integer.valueOf(dbMajorVersion) == databaseMajorVersion;
            }
            return containsMysql;
        } catch (SQLException e) {
            return false;
        }
    }

    /* (non-Javadoc)
      * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#query(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Object[], int, int, org.springframework.jdbc.core.RowMapper)
      */
    public <T> List<T> query(IJdbcDao jdbcDao, String sql,
                             Object[] parameters, int pageNo, int pageSize,
                             RowMapper<T> rowMapper) {
        String paginationSql = this.getLimitSql(sql, pageNo, pageSize);
        _log.debug(paginationSql);
        return jdbcDao.query(paginationSql, parameters, rowMapper);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#getCreateDefaultTableSql(String)
     */
    public String getCreateDefaultTableSql(String tableName) {
        return " CREATE TABLE " + tableName + " (ID_ VARCHAR(50) NOT NULL, PRIMARY KEY (ID_))";
    }

    /**
     * 生成列类型Sql
     *
     * @param columnType 列类型
     * @param columnSize 长度/精度
     * @return 列定义Sql
     */
    public String generateColumnTypeSql(String columnType, String columnSize) {
        StringBuilder cType = new StringBuilder(" ");

        if (columnType.indexOf("FOR BIT DATA")>-1) {
            cType.append(columnType.substring(0,columnType.indexOf("(")+1) + columnSize + columnType.substring(columnType.indexOf(")"),columnType.length()));
        }else if (StringUtils.isEmpty(columnSize)) {
            cType.append(columnType);
        } else {
            String[] cs = columnSize.split(",");
            if (cs.length == 2) {
                cType.append(columnType + "(" + cs[0] + "," + cs[1] + ")");
            } else if (cs.length == 1) {
                cType.append(columnType + "(" + cs[0] + ")");
            }
        }
        cType.append(" ");
        return cType.toString();
    }

    /**
     * 获得修改表主键Sql
     *
     * @param tableName   表名
     * @param primaryKeys 主键列
     * @return 修改表主键Sql
     */
    public String generateAlterPrimaryKeySql(String tableName, List<String> primaryKeys) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sql = new StringBuilder(" ");
        if (primaryKeys == null) {
            return sb.toString();
        }
        int i = 1;
        for (String s : primaryKeys) {
            if (primaryKeys.size() == i) {
                sb.append(s);
            } else {
                sb.append(s + ",");
            }
            i++;
        }
        if (primaryKeys.size() > 0) {
            sql.append(" ALTER TABLE ");
            sql.append(tableName);
            sql.append(" ADD CONSTRAINT PK_");
            sql.append(tableName);
            sql.append(" PRIMARY KEY (" + StringUtil.trimWhole(sb.toString()) + ")");
        }
        return sql.toString();
    }

    /**
     * 获得修改表外键Sql
     *
     * @param  tableName   表名
     * @param  fk          外键对象
     * @return 修改表主键Sql
     */
    public String generateAlterForeignKeySql(String tableName, DbConsoleFk fk) {
        StringBuilder sql = new StringBuilder(" ");
        sql.append(" ALTER TABLE "+tableName+" ADD CONSTRAINT "+fk.getColumnName()+"_FK ");
        sql.append(" FOREIGN KEY ("+fk.getColumnName()+") REFERENCES "+fk.getFkTableName()+" ("+fk.getFkColumnName()+")");
        return sql.toString();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#generatorDDL(com.innofi.component.dbconsole.pojo.DbConsoleTable)
     */
    public String generatorDDL(DbConsoleTable table) {
        List<DbConsoleColumn> columns = table.getColumns();
        String tableName = table.getTableName();
        if (columns.size() > 0 && StringUtil.hasText(tableName)) {
            StringBuilder tableDDL = new StringBuilder("CREATE TABLE " + tableName + "(");
            StringBuilder pkDDL = new StringBuilder();
            StringBuilder fkDDL = new StringBuilder();
            StringBuilder all = new StringBuilder();
            int index = 0;
            DbConsolePk pk = table.getPrimaryKey();
            List<DbConsoleFk> fks = table.getFks();
            List pkColumns = pk.getColumns();
            pkDDL.append(generateAlterPrimaryKeySql(table.getTableName(), pkColumns));

            for (DbConsoleFk fk : fks) {
                fkDDL.append(generateAlterForeignKeySql(tableName, fk)+";\n");
            }

            for (DbConsoleColumn column : columns) {
                String columnName = column.getColumnName();
                String typeDisplay = column.getTypeDisplay();
                if (index != columns.size() - 1) {
                    tableDDL.append(columnName + " " + typeDisplay + ", ");
                } else {
                    tableDDL.append(columnName + " " + typeDisplay + ")");
                }
                index++;
            }

            String formatDDL = SqlUtil.formatDDLSql(tableDDL.toString());

            all.append(formatDDL+";\n");

            if(StringUtil.hasText(pkDDL.toString())){
                all.append(pkDDL.toString()+";\n");
            }

            if(StringUtil.hasText(fkDDL.toString())){
                all.append(fkDDL);
            }

            return all.toString();
        }
        return "";
    }
}
