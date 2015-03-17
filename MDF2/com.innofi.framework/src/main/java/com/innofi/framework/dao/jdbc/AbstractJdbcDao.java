package com.innofi.framework.dao.jdbc;

import com.innofi.component.dbconsole.pojo.*;
import com.innofi.framework.dao.dynamicstatement.DynamicStatementContext;
import com.innofi.framework.dao.jdbc.sqldialect.DBDialect;
import com.innofi.framework.dao.jdbc.sqldialect.DBDialectUtil;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.dao.pagination.PaginationQuery;
import com.innofi.framework.dao.pagination.impl.PaginationQueryImpl;
import com.innofi.framework.exception.DAOException;
import com.innofi.framework.exception.FrameworkJdbcRuntimeException;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.sql.SqlUtil;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;
import com.innofi.framework.utils.variable.VariableHelper;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * Jdbc Dao 基类继承JdbcTemplate中的功能并进行了扩展
 */
public abstract class AbstractJdbcDao extends NamedParameterJdbcTemplate implements IJdbcDao {

    protected Logger _log = LoggerFactory.getLogger(AbstractJdbcDao.class);

    protected DataSource dataSource;

    public AbstractJdbcDao(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
        DynamicStatementContext.initCtx();//初始化动态sql/hql语句定义上下文
    }
    
    
    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#getCurrentDbType()
     */
    public String getCurrentDbType() {
        String dbType = null;
        Connection connection = getConnection();
        try {
            String url = connection.getMetaData().getURL();
            if (url.toLowerCase().indexOf("db2") > -1) {
                dbType = Database.DB2.toString().toLowerCase();
            } else if (url.toLowerCase().indexOf("oracle") > -1) {
                dbType = Database.ORACLE.toString().toLowerCase();
            } else if (url.toLowerCase().indexOf("derby") > -1) {
                dbType = Database.DERBY.toString().toLowerCase();
            } else if (url.toLowerCase().indexOf("microsoft") > -1) {
                dbType = Database.SQL_SERVER.toString().toLowerCase();
            } else if (url.toLowerCase().indexOf("mysql") > -1) {
                dbType = Database.MYSQL.toString().toLowerCase();
            } else {
                dbType = Database.DEFAULT.toString().toLowerCase();
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, getDataSource());
        }

        return dbType;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#getDBDialect()
     */
    public DBDialect getDBDialect() {
        Connection connection = getConnection();
        DBDialect dbDialect = null;
        try {
            dbDialect = DBDialectUtil.getDBDialect(connection);
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, getDataSource());
        }
        return dbDialect;
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#getFormatSql(String, String, Map)
     */
    public String getFormatSql(String moduleId, String sqlName, Map<String,String> params) {
        return getSql(getCurrentDbType(), moduleId, sqlName, params);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#loadDbInfo()
     */
    public DbConsoleDbInfo loadDbInfo() {
        DbConsoleDbInfo dbConsoleDbInfo = new DbConsoleDbInfo();
        Connection conn = getConnection();

        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            //数据库名称和版本
            String value = dbmd.getDatabaseProductName();
            value += "[" + dbmd.getDatabaseMajorVersion();
            value += "." + dbmd.getDatabaseMinorVersion() + "]";
            dbConsoleDbInfo.setProductName(value);
            //JDBC驱动名称和版本
            value = dbmd.getDriverName();
            value += "[" + dbmd.getDriverMajorVersion();
            value += "." + dbmd.getDriverMinorVersion() + "]";
            dbConsoleDbInfo.setJdbcDriver(value);
            //JDBC规范版本
            int iValue = dbmd.getJDBCMajorVersion();
            value = String.valueOf(iValue);
            iValue = dbmd.getJDBCMinorVersion();
            value += "." + iValue;
            dbConsoleDbInfo.setJdbcNormVersion(value);
            //允许在 SQL 语句中使用的最大字符数
            dbConsoleDbInfo.setMaxColumnNameLength(VariableHelper.parseString(dbmd.getMaxColumnNameLength()));
            //是否支持事务
            dbConsoleDbInfo.setSupportsTrans(dbmd.supportsTransactions());
            //是否支持使用存储过程
            dbConsoleDbInfo.setSupportsProcedure(dbmd.supportsStoredProcedures());
            //是否支持Statement Pooling
            dbConsoleDbInfo.setSupportsStatPool(dbmd.supportsStatementPooling());
            //是否支持批量更新
            dbConsoleDbInfo.setSupportsBatchUpdate(dbmd.supportsBatchUpdates());
            //是否支持执行语句后检索自动生成的键
            dbConsoleDbInfo.setSupportsGetGenerKey(dbmd.supportsGetGeneratedKeys());
            //是否支持SQL类型之间的转换(CONVERT)
            dbConsoleDbInfo.setSupportsConvert(dbmd.supportsConvert());
            value = "高级";
            boolean support = dbmd.supportsANSI92FullSQL();
            if (!support) {
                value = "中级";
                support = dbmd.supportsANSI92IntermediateSQL();
                if (!support) {
                    value = "初级";
                }
            }
            dbConsoleDbInfo.setSupportsANSI92SQLGrade(value);
            //是否支持比较表达式中的子查询
            dbConsoleDbInfo.setSupportsSubqueriesInComparisons(dbmd.supportsSubqueriesInComparisons());
            //是否支持 EXISTS 表达式中的子查询
            dbConsoleDbInfo.setSupportsSubqueriesInExists(dbmd.supportsSubqueriesInExists());
            //非SQL92标准的关键字
            dbConsoleDbInfo.setSqlKeywords(dbmd.getSQLKeywords());
            //可用的系统函数
            dbConsoleDbInfo.setSystemFunctions(dbmd.getSystemFunctions());
            //可用于数值类型的数学函数
            dbConsoleDbInfo.setNumericFunctions(dbmd.getNumericFunctions());
            //可用于字符串类型的函数
            dbConsoleDbInfo.setStringFunctions(dbmd.getStringFunctions());
            //可用于时间和日期类型的函数
            dbConsoleDbInfo.setTimeDateFunctions(dbmd.getTimeDateFunctions());
            //是否支持 IN 语句中的子查询
            dbConsoleDbInfo.setSupportsSubqueriesInIns(dbmd.supportsSubqueriesInIns());
            //是否支持相关子查询
            dbConsoleDbInfo.setSupportsCorrelatedSubqueries(dbmd.supportsCorrelatedSubqueries());
            //是否支持量化表达式 (quantified expression) 中的子查询
            dbConsoleDbInfo.setSupportsSubqueriesInQuantifieds(dbmd.supportsSubqueriesInQuantifieds());
            //是否支持 SELECT FOR UPDATE
            dbConsoleDbInfo.setSupportsSelectForUpdate(dbmd.supportsSelectForUpdate());
            //是否支持 UNION
            dbConsoleDbInfo.setSupportsUnion(dbmd.supportsUnion());
            //是否支持 UNION ALL
            dbConsoleDbInfo.setSupportsUnionAll(dbmd.supportsUnionAll());
            //是否支持 GROUP BY
            dbConsoleDbInfo.setSupportsGroupBy(dbmd.supportsGroupBy());
            //是否支持的外连接
            dbConsoleDbInfo.setSupportsOuterJoins(dbmd.supportsOuterJoins());
            //是否为外连接提供受限制的支持
            dbConsoleDbInfo.setSupportsLimitedOuterJoins(dbmd.supportsLimitedOuterJoins());
            //是否支持完全嵌套的外连接
            dbConsoleDbInfo.setSupportsFullOuterJoins(dbmd.supportsFullOuterJoins());
            //是否为每个表使用一个文件
            dbConsoleDbInfo.setUsesLocalFilePerTable(dbmd.usesLocalFilePerTable());
            //是否支持保存点
            dbConsoleDbInfo.setSupportsSavepoints(dbmd.supportsSavepoints());
            //是否支持列不在SELECT语句中而在ORDER BY子句中
            dbConsoleDbInfo.setSupportsOrderByUnrelated(dbmd.supportsOrderByUnrelated());
            //是否NULL值排序在开头
            dbConsoleDbInfo.setNullsAreSortedHigh(dbmd.nullsAreSortedHigh());
            //是否NULL值排序在末尾
            dbConsoleDbInfo.setNullsAreSortedLow(dbmd.nullsAreSortedLow());
            //NULL值是否始终排在末尾，不管排序顺序如何
            dbConsoleDbInfo.setNullsAreSortedAtEnd(dbmd.nullsAreSortedAtEnd());
            //NULL值是否始终排在开头，不管排序顺序如何
            dbConsoleDbInfo.setNullsAreSortedAtEnd(dbmd.nullsAreSortedAtStart());
            //允许索引最大列数
            dbConsoleDbInfo.setMaxColumnsInIndex(getDbLimitInfo(dbmd.getMaxColumnsInIndex()));
            //允许在 SQL 语句中使用的最大字符数
            dbConsoleDbInfo.setMaxStatementLength(getDbLimitInfo(dbmd.getMaxStatementLength()));
            //允许列名称的最大字符数
            dbConsoleDbInfo.setMaxColumnNameLength(getDbLimitInfo(dbmd.getMaxColumnNameLength()));
            //允许在 GROUP BY 子句中的最大列数
            dbConsoleDbInfo.setMaxColumnsInGroupBy(getDbLimitInfo(dbmd.getMaxColumnsInGroupBy()));
            //允许在索引中的最大列数
            dbConsoleDbInfo.setMaxIndexLength(getDbLimitInfo(dbmd.getMaxIndexLength()));
            //允许在 ORDER BY 子句的最大列数
            dbConsoleDbInfo.setMaxColumnsInOrderBy(getDbLimitInfo(dbmd.getMaxColumnsInOrderBy()));
            //允许在 SELECT 列表中的最大列数
            dbConsoleDbInfo.setMaxColumnsInSelect(getDbLimitInfo(dbmd.getMaxColumnsInSelect()));
            //最大并发连接数
            dbConsoleDbInfo.setMaxConnections(getDbLimitInfo(dbmd.getMaxConnections()));
            //允许在表中的最大列数
            dbConsoleDbInfo.setMaxColumnsInTable(getDbLimitInfo(dbmd.getMaxColumnsInTable()));
            //允许游标名称的最大字符数
            dbConsoleDbInfo.setMaxCursorNameLength(getDbLimitInfo(dbmd.getMaxCursorNameLength()));
            //允许索引（包括索引的所有部分）的最大字节数
            dbConsoleDbInfo.setMaxIndexLength(getDbLimitInfo(dbmd.getMaxIndexLength()));
            //允许存储过程名称的最大字符数
            dbConsoleDbInfo.setMaxProcedureNameLength(getDbLimitInfo(dbmd.getMaxProcedureNameLength()));
            //允许在单行的最大字节数
            dbConsoleDbInfo.setMaxRowSize(getDbLimitInfo(dbmd.getMaxRowSize()));
            //允许在模式名称的最大字符数
            dbConsoleDbInfo.setMaxSchemaNameLength(getDbLimitInfo(dbmd.getMaxSchemaNameLength()));
            //允许在表名称的最大字符数
            dbConsoleDbInfo.setMaxTableNameLength(getDbLimitInfo(dbmd.getMaxTableNameLength()));
            //获取此数据库允许在 SELECT 语句中使用的表的最大数
            dbConsoleDbInfo.setMaxTablesInSelect(getDbLimitInfo(dbmd.getMaxTablesInSelect()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dbConsoleDbInfo;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#loadSchemas()
     */
    public List<String> loadSchemas() {

        List<String> list = new ArrayList<String>();

        Connection conn = getConnection();
        ResultSet rs = null;

        try {
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            /*
             * 取得模式数据
             */
            rs = databaseMetaData.getSchemas();
            while (rs.next()) {
                String schema = rs.getString("TABLE_SCHEM");
                list.add(schema);
            }
            rs.close();

            // 如果SCHEMA数据为空，则取CATALOG
            if (list.size() == 0) {
                rs = databaseMetaData.getCatalogs();
                while (rs.next()) {
                    String schema = rs.getString("TABLE_CAT");
                    list.add(schema);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FrameworkJdbcRuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
        }
        return list;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#createNewTable(String)
     */
    public int createNewTable(String tableName) {
        String createSql = getDBDialect().getCreateDefaultTableSql(tableName);
        return update(createSql);
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#loadObjectTypes()
     */
    public List<String> loadObjectTypes() {
        List<String> objectTypes = new ArrayList<String>();
        String productName = null;
        Connection conn = getConnection();
        try {
            productName = conn.getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FrameworkJdbcRuntimeException(e);
        }

        String dbId = getDbId(productName);
        String types = ContextHolder.getIdfProperty(OBJECT_TYPE_SELECTABLE_KEY + "." + dbId);
        if (!StringUtil.hasText(types)) {
            objectTypes.add("TABLE");
            objectTypes.add("VIEW");
            objectTypes.add("SYNONYM");
            objectTypes.add("SYSTEM VIEW");
            objectTypes.add("SYSTEM TABLE");
        } else {
            List<String> temp = Arrays.asList(StringUtil.split(types.toUpperCase(), ","));
            for (String element : temp) {
                if (element == null) continue;
                element = StringUtil.trimLeading(StringUtil.trimTrailing(element.trim()));
                if (!StringUtil.hasText(element)) continue;
                objectTypes.add(element);
            }
        }
        return objectTypes;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#loadTableList(String, String, String, boolean)
     */

    public List<DbConsoleTable> loadTableList(String schema, String table, String types, boolean lazy) {
        List<DbConsoleTable> dbConsoleTables = new ArrayList<DbConsoleTable>();
        if (!StringUtil.hasText(types)) types = "*";
        try {
            dbConsoleTables = getTableList(null, schema, table, types.split(","), lazy);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FrameworkJdbcRuntimeException(e);
        }
        return dbConsoleTables;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#loadTablePrimaryKeys(String, String)
     */

    public DbConsolePk loadTablePrimaryKeys(String schema, String tableName) {
        Connection connection = getConnection();
        ResultSet keysRs = null;
        List<DbConsolePkColumn> cols = new ArrayList<DbConsolePkColumn>();
        String pkName = null;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            keysRs = metaData.getPrimaryKeys(null, schema, tableName);
            while (keysRs.next()) {
                pkName = keysRs.getString("PK_NAME");
                String colName = keysRs.getString("COLUMN_NAME");
                int sequence = keysRs.getInt("KEY_SEQ");
                DbConsolePkColumn pkColumn = new DbConsolePkColumn(colName, sequence);
                cols.add(pkColumn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(keysRs);
        }
        return new DbConsolePk(pkName, cols);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#executeUpdateSql(String[])
     */
    public int[] executeUpdateSql(String[] sqls) {
        final String[] fsqls = this.getFormatArrays(sqls);
        DataSource ds = getDataSource();
        PlatformTransactionManager txManager = new DataSourceTransactionManager(ds);
        final TransactionTemplate transactionTemplate = new TransactionTemplate(txManager);
        return transactionTemplate.execute(new TransactionCallback<int[]>() {
            public int[] doInTransaction(TransactionStatus status) {
                int[] i = batchUpdate(fsqls);
                return i;
            }
        });
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#executeUpdateSql(String[])
     */
    public int[] executeUpdateSql(final SqlWrapper sqlWrapper) {

        DataSource ds = getDataSource();
        PlatformTransactionManager txManager = new DataSourceTransactionManager(ds);
        final TransactionTemplate transactionTemplate = new TransactionTemplate(txManager);
        return transactionTemplate.execute(new TransactionCallback<int[]>() {
            public int[] doInTransaction(TransactionStatus status) {
                int i = update(sqlWrapper.getSql(), sqlWrapper.getArgs());
                int[] res = new int[1];
                res[0] = i;
                return res;
            }
        });
    }


    public <T> void paginationQuery(String sql, String countSql, Object[] params, Page<T> page, RowMapper<T> rowMapper) {
        page.setEntityCount(queryForInt(countSql, params));
        Assert.notNull(rowMapper, "请使用paginationQuery(String,String,Object[],Pagination<Map<String, Object>>)函数");
        int pageSize = page.getPageSize();
        int pageIndex = page.getPageNo();

        List<T> resultList = queryDataInPage(sql, params, pageIndex, pageSize, rowMapper);
        page.setEntities(resultList);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#loadTableForeignKeys(String, String)
     */

    public List<DbConsoleFk> loadTableForeignKeys(String schema, String tableName) {
        Connection connection = getConnection();
        ResultSet foreignKeysRs = null;
        List<DbConsoleFk> fks = new ArrayList<DbConsoleFk>();
        String pkName = null;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            foreignKeysRs = metaData.getExportedKeys(null, schema, tableName);
            while (foreignKeysRs.next()) {
                extrctFkInfo(foreignKeysRs, fks, "export");
            }
            foreignKeysRs = metaData.getExportedKeys(null, schema, tableName);
            while (foreignKeysRs.next()) {
                extrctFkInfo(foreignKeysRs, fks, "import");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fks;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#loadColumnType()
     */

    public List<String> loadColumnType() {
        List<String> list = new ArrayList<String>();
        Connection conn = null;
        ResultSet resultSet = null;
        try {
            conn = getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            resultSet = metaData.getTypeInfo();
            while (resultSet.next()) {
                String typeName = resultSet.getString("TYPE_NAME").toUpperCase();
                list.add(typeName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FrameworkJdbcRuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(resultSet);
        }
        return list;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#loadColumns(String, String)
     */

    public List<DbConsoleColumn> loadColumns(String schema, String tableName) {
        DbConsolePk pk = loadTablePrimaryKeys(schema, tableName);
        List<DbConsoleFk> fks = loadTableForeignKeys(schema, tableName);

        List<DbConsoleColumn> dbConsoleColumns = new ArrayList<DbConsoleColumn>();
        Connection conn = getConnection();
        ResultSet colRs = null;
        try {
            colRs = loadColumnInfo(schema, tableName, pk, fks, dbConsoleColumns, conn, colRs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FrameworkJdbcRuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(colRs);
        }
        return dbConsoleColumns;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#loadSequence(String, String)
     */

    public DbConsoleSequence loadSequence(String schema, String name) {
        List<DbConsoleSequence> sequences = loadSequences(schema, name);
        if (sequences.size() == 0) return null;
        return sequences.get(0);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#loadSequences(String, String)
     */

    public List<DbConsoleSequence> loadSequences(String schema, String name) {
        List<DbConsoleSequence> sequences = new ArrayList<DbConsoleSequence>();
        StringBuffer sequenceSql = new StringBuffer();

        Map<String,String> params = new HashMap<String,String>();

        if (StringUtil.hasText(schema) && !schema.equals("*")) {
            params.put("schema", schema);
        }

        if (StringUtil.hasText(name)) {
            params.put("sequencename", name);
        }

        sequenceSql.append(getFormatSql("dbconsole-dynamic-statement", "loadSequence", params));

        List<Map<String, Object>> records = queryForList(sequenceSql.toString());

        for (Map record : records) {
            String sequenceName = (String) record.get("SEQUENCENAME");
            String schemaName = (String) record.get("SCHEMANAME");
            DbConsoleSequence sequence = new DbConsoleSequence(schemaName, sequenceName);
            sequence.setStartValue(VariableHelper.parseString(record.get("STARTVALUE")));
            sequence.setMinValue(VariableHelper.parseString(record.get("MINIMUMVALUE")));
            sequence.setMaxValue(VariableHelper.parseString(record.get("MAXIMUMVALUE")));
            sequence.setIncrement(VariableHelper.parseString(record.get("INCREMENTVALUE")));
            sequence.setCycle(VariableHelper.parseString(record.get("CYCLEOPTION")));
            sequence.setCurrentValue(VariableHelper.parseString(record.get("CURRENTVALUE")));
            sequence.setDataType(VariableHelper.parseString(record.get("DATATYPE")));
            sequences.add(sequence);
        }
        return sequences;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#insertColumn(String, String, com.innofi.component.dbconsole.pojo.DbConsoleColumn)
     */

    public void insertColumn(String schema, String tableName, DbConsoleColumn columnInfo) {
        DbConsoleColumn dbColumnInfo = new DbConsoleColumn();
        try {
            BeanUtils.copyProperties(dbColumnInfo, columnInfo);
            String columnName = columnInfo.getColumnName();
            boolean isprimaryKey = columnInfo.isIsprimaryKey();
            DbConsolePk dbConsolePk = loadTablePrimaryKeys(schema, tableName);

            if (isprimaryKey) {
                List<String> primaryKeys = dbConsolePk.getColumns();
                primaryKeys.add(columnName);
                dbColumnInfo.setListPrimaryKey(primaryKeys);
                dbColumnInfo.setPkName(dbConsolePk.getPkName());
            }
            DBDialect dBDialect = getDBDialect();
            String sql = dBDialect.getNewColumnSql(dbColumnInfo);
            String[] sqls = sql.split(";");
            executeUpdateSql(sqls);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkJdbcRuntimeException(e);
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#updateColumn(String, String, com.innofi.component.dbconsole.pojo.DbConsoleColumn, com.innofi.component.dbconsole.pojo.DbConsoleColumn)
     */

    public void updateColumn(String schema, String tableName, DbConsoleColumn oldColumnInfo, DbConsoleColumn newColumnInfo) {
        DbConsoleColumn oldDbColumnInfo = new DbConsoleColumn();
        DbConsoleColumn newDbColumnInfo = new DbConsoleColumn();
        try {
            BeanUtils.copyProperties(oldDbColumnInfo, oldColumnInfo);
            BeanUtils.copyProperties(newDbColumnInfo, newColumnInfo);
            boolean oldPrimaryKey = oldColumnInfo.isIsprimaryKey();
            boolean newPrimaryKey = newColumnInfo.isIsprimaryKey();
            List<String> primaryKeys = null;
            if (oldPrimaryKey != newPrimaryKey) {
                DbConsolePk pk = loadTablePrimaryKeys(schema, tableName);
                primaryKeys = pk.getColumns();
                if (newPrimaryKey && !oldPrimaryKey) {
                    primaryKeys.add(newDbColumnInfo.getColumnName().toUpperCase());
                } else if (!newPrimaryKey && oldPrimaryKey) {
                    primaryKeys.remove(newDbColumnInfo.getColumnName().toUpperCase());
                }
                newDbColumnInfo.setListPrimaryKey(primaryKeys);
                newDbColumnInfo.setPkName(pk.getPkName());
            }
            DBDialect dBDialect = getDBDialect();
            String sql = dBDialect.getUpdateColumnSql(oldDbColumnInfo, newDbColumnInfo);
            String[] sqls = sql.split(";");
            executeUpdateSql(sqls);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkJdbcRuntimeException(e);
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#dropColumn(String, String, String)
     */

    public void dropColumn(String schema, String tableName, String columnName) {
        String sql = "ALTER TABLE " + schema + "." + tableName + " DROP COLUMN " + columnName;
        executeUpdateSql(new String[]{sql});
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#dropTable(String, String)
     */

    public boolean dropTable(String schema, String table) {
        String sql = " DROP TABLE " + table;
        if (StringUtil.hasText(schema)) {
            sql = " DROP TABLE " + schema + "." + table;
        }
        if (this.update(sql) > 0) return true;
        return false;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#renameTableName(String, String, String)
     */

    public void renameTableName(String schema, String tableName, String newTableName) {
        DBDialect dbDialect = getDBDialect();
        String sql = dbDialect.getTableRenameSql(schema, tableName, newTableName);
        String[] sqls = new String[]{sql};
        this.executeUpdateSql(sqls);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#dropSequence(String, String)
     */

    public boolean dropSequence(String schema, String sequence) {
        DBDialect dbDialect = getDBDialect();
        String sql = dbDialect.getDropSequenceSql(schema, sequence);
        if (this.update(sql) > 0) return true;
        return false;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#clearTableData(String, String)
     */

    public void clearTableData(String schema, String tableName) {
        String sql = " DELETE FROM " + tableName;
        if (StringUtil.hasText(schema)) {
            sql = " DELETE FROM " + schema + "." + tableName;
        }
        String[] sqls = new String[]{sql};
        executeUpdateSql(sqls);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#getTableRecordCount(String, String)
     */
    public int getTableRecordCount(String schema, String table) throws FrameworkJdbcRuntimeException {

        String tableName = "";
        if (Validator.isEmpty(schema)) {
            tableName = table;
        } else {
            tableName = schema + "." + table;
        }

        if (Validator.isEmpty(tableName)) {
            throw new FrameworkJdbcRuntimeException("表名不能为空!");
        }

        String countSql = "select count(*) as countNum from " + tableName;

        int recordCount = super.getJdbcOperations().queryForInt(countSql);

        _log.debug("to execute count table is [" + tableName + "] count result is[" + countSql + "]");

        return recordCount;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#queryMapForUnique(String, String, Map)
     */
    public Map<String, Object> queryMapForUnique(String moduleId, String sqlId, Map<String,String> params) {
        List<Map<String, Object>> queryResult = queryMapForList(moduleId, sqlId, params);
        if (queryResult.size() > 1) {
            throw new FrameworkJdbcRuntimeException("查询结果不唯一，请检查SQL的条件是否正确！");
        } else if (queryResult.size() == 0) {
            return new HashMap<String, Object>();
        }
        return queryResult.get(0);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#queryMapForList(String, String)
     */
    public List<Map<String, Object>> queryMapForList(String moduleId, String sqlId) {
        return queryMapForList(moduleId, sqlId, new HashMap<String, String>());
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#queryMapForList(String, String, Map)
     */
    public List<Map<String, Object>> queryMapForList(String moduleId, String sqlId, Map<String,String> params) {
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(Page.BIG_PAGE_SIZE, 1);
        queryMapForList(moduleId, sqlId, params, page);
        return page.getEntities();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#queryMapForList(String, com.innofi.framework.dao.pagination.Page)
     */
    public void queryMapForList(String querySql, Page<Map<String, Object>> page) {
        DBDialect dbDialect = getDBDialect();
        String countSql = SqlUtil.getCountSql(querySql);
        int totalCount = queryForInt(countSql);
        String pageSql = dbDialect.getLimitSql(querySql, page);
        List<Map<String, Object>> result = query(pageSql, new ColumnMapRowMapper());
        page.setEntities(result);
        page.setEntityCount(totalCount);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#queryMapForList(String, String, Map, com.innofi.framework.dao.pagination.Page)
     */
    public void queryMapForList(String moduleId, String sqlId, Map<String,String> params, Page<Map<String, Object>> page) {
        String sql = getFormatSql(moduleId, sqlId, params);
        DBDialect dbDialect = getDBDialect();
        String countSql = SqlUtil.getCountSql(sql);
        int totalCount = queryForInt(countSql);
        System.out.println("sql is ["+sql+"]");
        String pageSql = dbDialect.getLimitSql(sql, page);
        System.out.println("page sql is ["+pageSql+"]");
        List<Map<String, Object>> result = query(pageSql, new ColumnMapRowMapper());
        page.setEntities(result);
        page.setEntityCount(totalCount);
    }

    /**
     * 运行sql脚本
     *
     * @param reader 脚本对应的reader对象
     */

    public ProcessResult<String[]> runScript(Reader reader, boolean stopOnError, boolean autoCommit) {
        StringBuffer _log = new StringBuffer(300);
        StringBuffer _comment_log = new StringBuffer(300);
        StringBuffer _error_log = new StringBuffer(500);
        int sqlTotalCount = 0; // sql总数
        int succeedCount = 0;   //成功数
        int failedCount = 0;    //失败数
        double totalConsumeTime = 0D; //总耗时

        Connection conn = null;
        Statement statement = null;

        LineNumberReader lineReader = new LineNumberReader(reader);
        String line = null;

        List<String> commands = new ArrayList<String>();


        try {
            StringBuffer allScript = new StringBuffer();
            boolean hasMutilComment = false;
            StringBuffer mutilComment = new StringBuffer();
            while ((line = lineReader.readLine()) != null) {
                boolean commentStart = false;
                boolean commentEnd = false;
                String trimmedLine = line.trim();
                if (trimmedLine.indexOf("/*") > -1 && !hasMutilComment) {
                    hasMutilComment = true;
                    allScript.append(trimmedLine.substring(0, trimmedLine.indexOf("/*")) + "\n");
                    mutilComment.append(trimmedLine.substring(trimmedLine.indexOf("/*"), trimmedLine.length()) + "\n");
                    commentStart = true;
                }
                if (!hasMutilComment) {
                    allScript.append(trimmedLine + "\n");
                }
                if (trimmedLine.indexOf("*/") > -1 && hasMutilComment) {
                    allScript.append(trimmedLine.substring(trimmedLine.indexOf("*/") + 2, trimmedLine.length()) + "\n");
                    mutilComment.append(trimmedLine.substring(0, trimmedLine.indexOf("*/") + 2) + "\n");
                    hasMutilComment = false;
                    commentEnd = true;
                }
                if (hasMutilComment && !commentStart && !commentEnd) {
                    mutilComment.append(trimmedLine + "\n");
                }
            }

            if (StringUtil.hasText(mutilComment.toString())) {
                println(_comment_log, String.format("忽略多行注释【%s】", mutilComment.toString()));
            }

            lineReader = new LineNumberReader(new StringReader(allScript.toString()));

            StringBuffer command = new StringBuffer();
            while ((line = lineReader.readLine()) != null) {
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--")) {
                    println(_comment_log, String.format("忽略单行注释【%s】", trimmedLine));
                } else if (trimmedLine.length() >= 1 && !(trimmedLine.startsWith("//")) && trimmedLine.endsWith(";")) {
                    command.append(line.substring(0, line.lastIndexOf(";")));
                    command.append(" ");
                    commands.add(StringUtil.trimLeading(command.toString().toUpperCase()));
                    command = new StringBuffer();
                    sqlTotalCount++;
                } else {
                    command.append(line);
                    command.append(" ");
                }
            }
            if (commands.size() == 0) {
                commands.add(command.toString());
                sqlTotalCount++;
            }

        } catch (IOException e) {
            e.printStackTrace();
            println(_error_log, String.format("执行出现错误，错误信息【%s】", e.getMessage()));
            ProcessResult<String[]> result = new ProcessResult<String[]>();
            String[] logs = new String[4];
            logs[0] = _log.toString();
            logs[1] = _error_log.toString();
            logs[2] = _comment_log.toString();
            logs[3] = String.format("执行完成，成功【%s】，失败【%s】, 总数【%s】 执行总耗时【%ss】;", succeedCount, failedCount, sqlTotalCount, totalConsumeTime);
            result.setData(logs);
            return result;
        }


        conn = getConnection();
        for (String command : commands) {
            boolean hasError = false;
            try {
                statement = conn.createStatement();
                int updateAffectedCount = -1;
                long startTime = System.currentTimeMillis();

                if (StringUtil.trimLeading(command.toString().toUpperCase()).startsWith("SELECT")) {
                    boolean succeed = statement.execute(command.toString());
                    if (!succeed) {
                        hasError = true;
                    }
                } else {
                    updateAffectedCount = statement.executeUpdate(command.toString());
                }

                long endTime = System.currentTimeMillis();
                double consumeTime = (endTime - startTime) / 1000;
                totalConsumeTime += consumeTime;

                if (StringUtil.trimLeading(command.toString().toUpperCase()).startsWith("SELECT")) {
                    println(_log, String.format("查询执行成功,耗时【%ss】SQL语句【%s】;", consumeTime, command));
                } else if (StringUtil.trimLeading(command.toString().toUpperCase()).startsWith("UPDATE")) {
                    println(_log, String.format("更新执行成功,耗时【%ss】影响记录【%s】条 SQL语句【%s】;", consumeTime, updateAffectedCount, command));
                } else if (StringUtil.trimLeading(command.toString().toUpperCase()).startsWith("CREATE")) {
                    println(_log, String.format("创建对象成功,耗时【%ss】SQL语句【%s】;", consumeTime, command));
                } else if (StringUtil.trimLeading(command.toString().toUpperCase()).startsWith("DROP")) {
                    println(_log, String.format("删除对象成功,耗时【%ss】SQL语句【%s】;", consumeTime, command));
                } else if (StringUtil.trimLeading(command.toString().toUpperCase()).startsWith("INSERT")) {
                    println(_log, String.format("记录插入成功,耗时【%ss】SQL语句【%s】;", consumeTime, command));
                } else if (StringUtil.trimLeading(command.toString().toUpperCase()).startsWith("DELETE")) {
                    println(_log, String.format("删除数据成功,耗时【%ss】影响记录【%s】条 SQL语句【%s】;", consumeTime, updateAffectedCount, command));
                } else {
                    println(_log, String.format("SQL执行成功,耗时【%ss】SQL语句【%s】;", consumeTime, command));
                }

                if (autoCommit && (!(conn.getAutoCommit()))) {
                    conn.commit();
                }
            } catch (SQLException e) {
                e.fillInStackTrace();
                hasError = true;
                failedCount++;
                println(_error_log, String.format("执行出现错误，错误信息【%s】SQL语句【%s】", e.getMessage(), command));
            } finally {
                JdbcUtils.closeStatement(statement);
            }

            if (stopOnError && hasError) {
                break;
            } else if (!hasError) {
                succeedCount++;
            }
        }

        ProcessResult<String[]> result = new ProcessResult<String[]>();
        String[] logs = new String[4];
        logs[0] = _log.toString();
        logs[1] = _error_log.toString();
        logs[2] = _comment_log.toString();
        logs[3] = String.format("执行完成，成功【%s】，失败【%s】, 总数【%s】 执行总耗时【%ss】;", succeedCount, failedCount, sqlTotalCount, totalConsumeTime);

        result.setData(logs);
        return result;
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#execute(org.springframework.jdbc.core.ConnectionCallback)
     */
    public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {

        return super.getJdbcOperations().execute(action);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#execute(org.springframework.jdbc.core.StatementCallback)
     */
    public <T> T execute(StatementCallback<T> action)
            throws DataAccessException {

        return super.getJdbcOperations().execute(action);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#execute(String)
     */
    public void execute(String sql) throws DataAccessException {
        super.getJdbcOperations().execute(sql);

    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, org.springframework.jdbc.core.ResultSetExtractor)
     */
    public <T> T query(String sql, ResultSetExtractor<T> rse)
            throws DataAccessException {

        return super.getJdbcOperations().query(sql, rse);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, org.springframework.jdbc.core.RowCallbackHandler)
     */
    public void query(String sql, RowCallbackHandler rch)
            throws DataAccessException {
        super.getJdbcOperations().query(sql, rch);

    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, org.springframework.jdbc.core.RowMapper)
     */
    public <T> List<T> query(String sql, RowMapper<T> rowMapper)
            throws DataAccessException {

        return super.getJdbcOperations().query(sql, rowMapper);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForObject(String, org.springframework.jdbc.core.RowMapper)
     */
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper)
            throws DataAccessException {

        return super.getJdbcOperations().queryForObject(sql, rowMapper);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForObject(String, Class)
     */
    public <T> T queryForObject(String sql, Class<T> requiredType)
            throws DataAccessException {

        return super.getJdbcOperations().queryForObject(sql, requiredType);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForMap(String)
     */
    public Map<String, Object> queryForMap(String sql) throws DataAccessException {

        return super.getJdbcOperations().queryForMap(sql);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForLong(String)
     */
    public long queryForLong(String sql) throws DataAccessException {

        return super.getJdbcOperations().queryForLong(sql);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForInt(String)
     */
    public int queryForInt(String sql) throws DataAccessException {

        return super.getJdbcOperations().queryForInt(sql);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForList(String, Class)
     */
    public <T> List<T> queryForList(String sql, Class<T> elementType)
            throws DataAccessException {

        return super.getJdbcOperations().queryForList(sql, elementType);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForList(String)
     */
    public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {

        return super.getJdbcOperations().queryForList(sql);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForRowSet(String)
     */
    public SqlRowSet queryForRowSet(String sql) throws DataAccessException {

        return super.getJdbcOperations().queryForRowSet(sql);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#update(String)
     */
    public int update(String sql) throws DataAccessException {

        return super.getJdbcOperations().update(sql);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#batchUpdate(String[])
     */
    public int[] batchUpdate(String[] sql) throws DataAccessException {
        return this.getJdbcOperations().batchUpdate(sql);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#execute(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.PreparedStatementCallback)
     */
    public <T> T execute(PreparedStatementCreator psc,
                         PreparedStatementCallback<T> action) throws DataAccessException {

        return super.getJdbcOperations().execute(psc, action);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#execute(String, org.springframework.jdbc.core.PreparedStatementCallback)
     */
    public <T> T execute(String sql, PreparedStatementCallback<T> action)
            throws DataAccessException {

        return super.getJdbcOperations().execute(sql, action);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.ResultSetExtractor)
     */
    public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse)
            throws DataAccessException {

        return super.getJdbcOperations().query(psc, rse);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.ResultSetExtractor)
     */
    public <T> T query(String sql, PreparedStatementSetter pss,
                       ResultSetExtractor<T> rse) throws DataAccessException {

        return super.getJdbcOperations().query(sql, pss, rse);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, Object[], int[], org.springframework.jdbc.core.ResultSetExtractor)
     */
    public <T> T query(String sql, Object[] args, int[] argTypes,
                       ResultSetExtractor<T> rse) throws DataAccessException {

        return super.getJdbcOperations().query(sql, args, argTypes, rse);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, Object[], org.springframework.jdbc.core.ResultSetExtractor)
     */
    public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse)
            throws DataAccessException {

        return super.getJdbcOperations().query(sql, args, rse);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, org.springframework.jdbc.core.ResultSetExtractor, Object[])
     */
    public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args)
            throws DataAccessException {

        return super.getJdbcOperations().query(sql, rse, args);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.RowCallbackHandler)
     */
    public void query(PreparedStatementCreator psc, RowCallbackHandler rch)
            throws DataAccessException {
        super.getJdbcOperations().query(psc, rch);

    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.RowCallbackHandler)
     */
    public void query(String sql, PreparedStatementSetter pss,
                      RowCallbackHandler rch) throws DataAccessException {

        super.getJdbcOperations().query(sql, pss, rch);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, Object[], int[], org.springframework.jdbc.core.RowCallbackHandler)
     */
    public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) throws DataAccessException {
        super.getJdbcOperations().query(sql, args, argTypes, rch);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, Object[], org.springframework.jdbc.core.RowCallbackHandler)
     */
    public void query(String sql, Object[] args, RowCallbackHandler rch)
            throws DataAccessException {

        super.getJdbcOperations().query(sql, args, rch);

    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, org.springframework.jdbc.core.RowCallbackHandler, Object[])
     */
    public void query(String sql, RowCallbackHandler rch, Object... args)
            throws DataAccessException {
        super.getJdbcOperations().query(sql, rch, args);

    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.RowMapper)
     */
    public <T> List<T> query(PreparedStatementCreator psc,
                             RowMapper<T> rowMapper) throws DataAccessException {

        return super.getJdbcOperations().query(psc, rowMapper);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.RowMapper)
     */
    public <T> List<T> query(String sql, PreparedStatementSetter pss,
                             RowMapper<T> rowMapper) throws DataAccessException {

        return super.getJdbcOperations().query(sql, pss, rowMapper);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, Object[], int[], org.springframework.jdbc.core.RowMapper)
     */
    public <T> List<T> query(String sql, Object[] args, int[] argTypes,
                             RowMapper<T> rowMapper) throws DataAccessException {

        return super.getJdbcOperations().query(sql, args, argTypes, rowMapper);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, Object[], org.springframework.jdbc.core.RowMapper)
     */
    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper)
            throws DataAccessException {

        return super.getJdbcOperations().query(sql, args, rowMapper);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#query(String, org.springframework.jdbc.core.RowMapper, Object[])
     */
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args)
            throws DataAccessException {

        return super.getJdbcOperations().query(sql, rowMapper, args);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForObject(String, Object[], int[], org.springframework.jdbc.core.RowMapper)
     */
    public <T> T queryForObject(String sql, Object[] args, int[] argTypes,
                                RowMapper<T> rowMapper) throws DataAccessException {

        return super.getJdbcOperations().queryForObject(sql, args, argTypes, rowMapper);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForObject(String, Object[], org.springframework.jdbc.core.RowMapper)
     */
    public <T> T queryForObject(String sql, Object[] args,
                                RowMapper<T> rowMapper) throws DataAccessException {

        return super.getJdbcOperations().queryForObject(sql, args, rowMapper);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForObject(String, org.springframework.jdbc.core.RowMapper, Object[])
     */
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper,
                                Object... args) throws DataAccessException {

        return super.getJdbcOperations().queryForObject(sql, rowMapper, args);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForObject(String, Object[], int[], Class)
     */
    public <T> T queryForObject(String sql, Object[] args, int[] argTypes,
                                Class<T> requiredType) throws DataAccessException {

        return super.getJdbcOperations().queryForObject(sql, args, argTypes, requiredType);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForObject(String, Object[], Class)
     */
    public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType)
            throws DataAccessException {

        return super.getJdbcOperations().queryForObject(sql, args, requiredType);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForObject(String, Class, Object[])
     */
    public <T> T queryForObject(String sql, Class<T> requiredType,
                                Object... args) throws DataAccessException {

        return super.getJdbcOperations().queryForObject(sql, requiredType, args);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForMap(String, Object[], int[])
     */
    public Map<String, Object> queryForMap(String sql, Object[] args,
                                           int[] argTypes) throws DataAccessException {

        return super.getJdbcOperations().queryForMap(sql, args, argTypes);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForMap(String, Object[])
     */
    public Map<String, Object> queryForMap(String sql, Object... args)
            throws DataAccessException {

        return super.getJdbcOperations().queryForMap(sql, args);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForLong(String, Object[], int[])
     */
    public long queryForLong(String sql, Object[] args, int[] argTypes)
            throws DataAccessException {

        return super.getJdbcOperations().queryForLong(sql, args, argTypes);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForLong(String, Object[])
     */
    public long queryForLong(String sql, Object... args)
            throws DataAccessException {

        return super.getJdbcOperations().queryForLong(sql, args);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForInt(String, Object[], int[])
     */
    public int queryForInt(String sql, Object[] args, int[] argTypes)
            throws DataAccessException {

        return super.getJdbcOperations().queryForInt(sql, args, argTypes);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForInt(String, Object[])
     */
    public int queryForInt(String sql, Object... args)
            throws DataAccessException {

        return super.getJdbcOperations().queryForInt(sql, args);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForList(String, Object[], int[], Class)
     */
    public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes,
                                    Class<T> elementType) throws DataAccessException {

        return super.getJdbcOperations().queryForList(sql, args, argTypes, elementType);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForList(String, Object[], Class)
     */
    public <T> List<T> queryForList(String sql, Object[] args,
                                    Class<T> elementType) throws DataAccessException {

        return super.getJdbcOperations().queryForList(sql, args, elementType);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForList(String, Class, Object[])
     */
    public <T> List<T> queryForList(String sql, Class<T> elementType,
                                    Object... args) throws DataAccessException {

        return super.getJdbcOperations().queryForList(sql, elementType, args);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForList(String, Object[], int[])
     */
    public List<Map<String, Object>> queryForList(String sql, Object[] args,
                                                  int[] argTypes) throws DataAccessException {

        return super.getJdbcOperations().queryForList(sql, args, argTypes);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForList(String, Object[])
     */
    public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {
        return super.getJdbcOperations().queryForList(sql, args);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForRowSet(String, Object[], int[])
     */
    public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes)
            throws DataAccessException {

        return super.getJdbcOperations().queryForRowSet(sql, args, argTypes);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#queryForRowSet(String, Object[])
     */
    public SqlRowSet queryForRowSet(String sql, Object... args)
            throws DataAccessException {

        return super.getJdbcOperations().queryForRowSet(sql, args);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#update(org.springframework.jdbc.core.PreparedStatementCreator)
     */
    public int update(PreparedStatementCreator psc) throws DataAccessException {

        return super.getJdbcOperations().update(psc);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#update(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.support.KeyHolder)
     */
    public int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder)
            throws DataAccessException {

        return super.getJdbcOperations().update(psc, generatedKeyHolder);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#update(String, org.springframework.jdbc.core.PreparedStatementSetter)
     */
    public int update(String sql, PreparedStatementSetter pss)
            throws DataAccessException {

        return super.getJdbcOperations().update(sql, pss);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#update(String, Object[], int[])
     */
    public int update(String sql, Object[] args, int[] argTypes)
            throws DataAccessException {

        return super.getJdbcOperations().update(sql, args, argTypes);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#update(String, Object[])
     */
    public int update(String sql, Object... args) throws DataAccessException {

        return super.getJdbcOperations().update(sql, args);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#batchUpdate(String, org.springframework.jdbc.core.BatchPreparedStatementSetter)
     */
    public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss)
            throws DataAccessException {

        return super.getJdbcOperations().batchUpdate(sql, pss);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#batchUpdate(String, java.util.List)
     */
    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {

        return super.getJdbcOperations().batchUpdate(sql, batchArgs);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#batchUpdate(String, java.util.List, int[])
     */
    public int[] batchUpdate(String sql, List<Object[]> batchArgs,
                             int[] argTypes) {

        return super.getJdbcOperations().batchUpdate(sql, batchArgs, argTypes);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#batchUpdate(String, java.util.Collection, int, org.springframework.jdbc.core.ParameterizedPreparedStatementSetter)
     */
    public <T> int[][] batchUpdate(String sql, Collection<T> batchArgs,
                                   int batchSize, ParameterizedPreparedStatementSetter<T> pss) {

        return super.getJdbcOperations().batchUpdate(sql, batchArgs, batchSize, pss);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#execute(org.springframework.jdbc.core.CallableStatementCreator, org.springframework.jdbc.core.CallableStatementCallback)
     */
    public <T> T execute(CallableStatementCreator csc,
                         CallableStatementCallback<T> action) throws DataAccessException {

        return super.getJdbcOperations().execute(csc, action);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#execute(String, org.springframework.jdbc.core.CallableStatementCallback)
     */
    public <T> T execute(String callString, CallableStatementCallback<T> action)
            throws DataAccessException {

        return super.getJdbcOperations().execute(callString, action);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.jdbc.core.JdbcOperations#call(org.springframework.jdbc.core.CallableStatementCreator, java.util.List)
     */
    public Map<String, Object> call(CallableStatementCreator csc,
                                    List<SqlParameter> declaredParameters) throws DataAccessException {

        return super.getJdbcOperations().call(csc, declaredParameters);
    }


    /**
     * 通过Sping DataSourceUtils获得Connection具有被Spring事物管理
     *
     * @return
     */
    public Connection getConnection() {
        return DataSourceUtils.getConnection(getDataSource());
    }

    /**
     * 根据模块唯一标识，sql唯一标识获得对应sql方法
     *
     * @param moduleId 模块唯一标识
     * @param sqlName SQL名称
     * @return 两者对应的sql语句
     * @throws Exception
     */
    protected String getSql(String dbType, String moduleId, String sqlName, Map<String,String> params) {
        return DynamicStatementContext.getSql(dbType, moduleId, sqlName, params);
    }

    /**
     * 获得数据源
     *
     * @return
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 删除select语句
     *
     * @param sql SQL语句
     * @return 删除select语句后的SQL语句
     */
    protected String removeSelect(String sql) {
        Assert.hasText(sql);
        while (true) {
            int Pos = sql.toLowerCase().indexOf("from");
            String firstStr = "";
            String secondStr = "";
            if (Pos != -1) {
                firstStr = removeParenthesis(sql.substring(0, Pos));

                secondStr = sql.substring(Pos);
                int beginpos = firstStr.lastIndexOf("(");
                if (beginpos >= 0) {
                    int endpos = secondStr.indexOf(")");
                    sql = firstStr.substring(0, beginpos)
                            + secondStr.substring(endpos + 1);

                } else {

                    return sql.substring(Pos);
                }

            } else {
                Assert.isTrue(Pos != -1, " sql : " + sql
                        + " must has a keyword 'from'");
                return sql.substring(Pos);
            }
        }
    }


    /**
     * 去掉参数string中有 ( )之间的部分
     *
     * @param sql 输入sql
     * @return 返回取出（）之间以后的sql
     */
    protected String removeParenthesis(String sql) {
        while (true) {
            int Pos = sql.toLowerCase().indexOf(")");
            String firstStr = "";
            String secondStr = "";
            if (Pos != -1) {
                firstStr = sql.substring(0, Pos);
                secondStr = sql.substring(Pos);
                int beginpos = firstStr.lastIndexOf("(");
                if (beginpos >= 0) {
                    if (secondStr.length() > 1)
                        sql = firstStr.substring(0, beginpos)
                                + secondStr.substring(1);
                    else
                        sql = firstStr.substring(0, beginpos);
                } else {
                    return sql;
                }

            } else {
                return sql;
            }

        }
    }

    /**
     * 去除sql order by 子句，用于pagedQuery.
     */
    protected String removeOrders(String sql) {
        Assert.hasText(sql);
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 查询当页的记录.
     *
     * @param <T>       结果对象类型
     * @param sql       查询sql
     * @param params    查询参数
     * @param pageIndex 开始页
     * @param pageSize  每页大小
     * @param rowMapper 行映射器
     * @return 查询结果列表, 仅包含当页的记录
     */
    protected <T> List<T> queryDataInPage(String sql, Object[] params, int pageIndex, int pageSize, final RowMapper<T> rowMapper) {
        final int safePageSize = (pageSize < 1) ? 65535 : pageSize;
        PaginationQuery<T> pq = this.createPaginationQuery();
        pq.sql(sql).parameters(params).rowMapper(rowMapper).pageNo(pageIndex).pageSize(safePageSize);
        return pq.list();
    }


    protected void print(StringBuffer logWriter, Object o) {
        if (o != null) {
            if (logWriter != null) logWriter.append(o.toString());
        }
    }

    protected void println(StringBuffer logWriter, Object o) {
        if (o != null) {
            if (logWriter != null) logWriter.append(o.toString() + "\n");
        }
    }

    protected String getDbLimitInfo(int limitCount) {
        if (limitCount == 0) {
            return "无限制/未知";
        }
        return VariableHelper.parseString(limitCount);
    }

    protected final String getDbId(String productName) {
        String dbId = productName.replaceAll("[ \\(\\)\\[\\]/$,.'=\"]", "_").toLowerCase();

        if (productName.startsWith("DB2")) {
            // DB2 for Host-Systems
            // apparently DB2 for z/OS identifies itself as "DB2" whereas
            // DB2 for AS/400 identifies itself as "DB2 UDB for AS/400"
            if (productName.contains("AS/400") || productName.contains("iSeries")) {
                dbId = "db2i";
            } else if (productName.equals("DB2")) {
                dbId = "db2h";
            } else {
                // Everything else is LUW (Linux, Unix, Windows)
                dbId = "db2";
            }
        } else if (productName.startsWith("HSQL")) {
            // As the version number is appended to the productname
            // we need to ignore that here. The properties configured
            // in workbench.settings using the DBID are (currently) identically
            // for all HSQL versions.
            dbId = "hsql_database_engine";
        }
        return dbId;
    }


    protected List<DbConsoleTable> getTableList(String catalogPattern, String schemaPattern, String namePattern, String[] types, boolean lazy) throws SQLException {
        if (!StringUtil.hasText(schemaPattern) || "*".equals(schemaPattern) || "%".equals(schemaPattern))
            schemaPattern = null;
        if (!StringUtil.hasText(namePattern) || "*".equals(namePattern) || "%".equals(namePattern)) namePattern = null;
        if (StringUtil.hasText(schemaPattern)) schemaPattern = StringUtil.replace(schemaPattern, "*", "%");
        if (StringUtil.hasText(namePattern)) namePattern = StringUtil.replace(namePattern, "*", "%");
        if (!StringUtil.hasText(catalogPattern)) catalogPattern = null;

        List<DbConsoleTable> dbConsoleTables = new ArrayList<DbConsoleTable>();

        if (types.length == 0 || "*".equals(types[0])) {
            types = loadObjectTypes().toArray(new String[0]);
        }

        Connection conn = getConnection();
        ResultSet tableRs = null;
        try {


            tableRs = conn.getMetaData().getTables(StringUtil.trimQuotes(catalogPattern), StringUtil.trimQuotes(schemaPattern), StringUtil.trimQuotes(namePattern), types);

            while (tableRs.next()) {
                String catalog = tableRs.getString(1);
                String schema = tableRs.getString(2);
                String name = tableRs.getString(3);
                String tableType = tableRs.getString(4);
                String remarks = tableRs.getString(5);
                String fqName = schema + "." + name;

                if (name == null) continue;
                // 过滤掉无效元素
                if (name.indexOf('/') > -1 || name.indexOf('$') > -1) {
                    continue;
                }

                DbConsoleTable table = new DbConsoleTable();

                if (!lazy) {
                    DbConsolePk pk = loadTablePrimaryKeys(schema, name);
                    List<DbConsoleFk> fks = loadTableForeignKeys(schema, name);
                    List<DbConsoleColumn> columns = loadColumns(schema, name);
                    table.setPrimaryKey(pk);
                    table.setFks(fks);
                    table.setColumns(columns);
                }

                table.setCatalog(catalog);
                table.setSchema(schema);
                table.setTable(name);
                table.setType(tableType);
                table.setComment(remarks);
                table.setFullQualifyName(fqName);

                dbConsoleTables.add(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkJdbcRuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(tableRs);
        }
        return dbConsoleTables;
    }

    private ResultSet loadColumnInfo(String schema, String tableName, DbConsolePk pk, List<DbConsoleFk> fks, List<DbConsoleColumn> dbConsoleColumns, Connection conn, ResultSet colRs) throws SQLException {
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        colRs = databaseMetaData.getColumns(null, schema, tableName, "%");
        ResultSetMetaData rsmeta = colRs.getMetaData();
        boolean jdbc4 = false;

        if (rsmeta.getColumnCount() > 22) {
            String name = rsmeta.getColumnName(23);
            // HSQLDB 1.8 returns 23 columns, but is not JDBC4, so I need to check for the name as well.
            jdbc4 = name.equals("IS_AUTOINCREMENT");
        }

        while (colRs != null && colRs.next()) {
            String colName = StringUtil.trimWhole(colRs.getString("COLUMN_NAME"));
            String typeName = colRs.getString("TYPE_NAME");

            String remarks = ""; //oracle注释
            if (getCurrentDbType().toLowerCase().equals("oracle")) {
                Map commments = queryForMap("select comments from user_col_comments where table_name ='" + tableName
                        + "' and column_name ='" + colName + "'");
                if (commments.get("comments") != null) {
                    remarks = commments.get("comments").toString();
                }
            }

            int sqlType = colRs.getInt("DATA_TYPE");
            int size = colRs.getInt("COLUMN_SIZE");
            int digits = -1;
            try {
                digits = colRs.getInt("DECIMAL_DIGITS");
            } catch (Exception e) {
                digits = -1;
            }
            if (colRs.wasNull()) digits = -1;

            String defaultValue = colRs.getString("COLUMN_DEF");
            if (defaultValue != null) {
                defaultValue = defaultValue.trim();
            }

            int position = -1;
            try {
                position = colRs.getInt("ORDINAL_POSITION");
            } catch (SQLException e) {
                position = -1;
            }

            String display = SqlUtil.getSqlTypeDisplay(typeName, sqlType, size, digits);
            String nullable = colRs.getString("IS_NULLABLE");
            String increment = jdbc4 ? colRs.getString("IS_AUTOINCREMENT") : "NO";
            boolean autoincrement = StringUtil.stringToBool(increment);

            DbConsoleColumn col = new DbConsoleColumn();

            col.setTableName(tableName);
            col.setColumnName(colName);
            col.setRemarks(remarks);
            col.setTypeDisplay(display);


            if ("VARCHAR () FOR BIT DATA".equalsIgnoreCase(typeName)) { //derby 数据类型处理
                col.setColumnType(typeName.substring(0, typeName.indexOf("(") + 1) + size + typeName.substring(typeName.indexOf(")"), typeName.length()));
            } else if ("CHAR () FOR BIT DATA".equalsIgnoreCase(typeName)) {
                col.setColumnType(typeName.substring(0, typeName.indexOf("(") + 1) + size + typeName.substring(typeName.indexOf(")"), typeName.length()));
            } else {
                col.setColumnType(typeName);
            }

            col.setColumnSeq(position + "");
            if (digits == -1) {
                col.setColumnSize(size + "");
            } else {
                col.setColumnSize(size + "," + digits);
            }
            col.setIsnullAble("YES".equalsIgnoreCase(nullable));
            col.setDefaultValue(defaultValue);
            col.setIsautoincrement(autoincrement);

            setPkInfo(pk, col);

            setFkInfo(fks, col);
            dbConsoleColumns.add(col);
        }
        return colRs;
    }

    private void extrctFkInfo(ResultSet foreignKeysRs, List<DbConsoleFk> fks, String type) throws SQLException {
        String fkTableCatalog = foreignKeysRs.getString(5); // FKTABLE_CAT
        String fkTableSchema = foreignKeysRs.getString(6); // FKTABLE_SCHEM
        String fkTableName = foreignKeysRs.getString(7); // FKTABLE_NAME
        String fkColumnName = foreignKeysRs.getString(8); // FKCOLUMN_NAME
        Integer fkSeq = Integer.valueOf(foreignKeysRs.getInt(9)); // KEY_SEQ
        Integer updateRule = Integer.valueOf(foreignKeysRs.getInt(10)); // UPDATE_RULE
        Integer deleteRule = Integer.valueOf(foreignKeysRs.getInt(11)); // DELETE_RULE
        String fkName = foreignKeysRs.getString(12); // FK_NAME
        foreignKeysRs.getString(13); // PK_NAME
        Integer.valueOf(foreignKeysRs.getInt(14)); // DEFERRABILITY
        DbConsoleFk fk = new DbConsoleFk();
        fk.setFkColumnName(fkName);
        fk.setFkSeq(fkSeq + "");
        fk.setFkTableCatalog(fkTableCatalog);
        fk.setFkTableSchema(fkTableSchema);
        fk.setFkTableName(fkTableName);
        fk.setFkUpdateRule(SqlUtil.getFkUpdateRuleForDisplay(updateRule));
        fk.setFkDeleteRule(SqlUtil.getFkDeleteRuleForDisplay(deleteRule));
        fk.setFkName(fkName);
        fk.setColumnName(fkColumnName);
        fk.setType(type);
        fks.add(fk);
    }

    private void setFkInfo(List<DbConsoleFk> fks, DbConsoleColumn col) {
        for (DbConsoleFk fk : fks) {
            if (fk.getType().equals("export")) continue;
            if (fk.getColumnName().equalsIgnoreCase(col.getColumnName())) {
                col.setIsFk(true);
                col.setFkColumnName(fk.getFkColumnName());
                col.setFkSeq(fk.getFkSeq());
                col.setFkName(fk.getFkName());
                col.setFkTableCatalog(fk.getFkTableCatalog());
                col.setFkUpdateRule(fk.getFkUpdateRule());
                col.setFkTableSchema(fk.getFkTableSchema());
                col.setFkDeleteRule(fk.getFkDeleteRule());
            }
        }
    }

    private void setPkInfo(DbConsolePk pk, DbConsoleColumn col) {
        List<DbConsolePkColumn> pkColumns = pk.getColumnDbConsoles();
        col.setPkName(pk.getPkName());
        col.setListPrimaryKey(pk.getColumns());
        for (DbConsolePkColumn pkColumn : pkColumns) {
            col.setPkSeq(pkColumn.getSeq() + "");
            if (pkColumn.getColumn().equalsIgnoreCase(col.getColumnName())) {
                col.setIsprimaryKey(true);
            }
        }
    }

    private String[] getFormatArrays(String[] args) {
        String[] newString = new String[]{};
        List<String> list = new ArrayList<String>();
        for (String s : args) {
            if (org.apache.commons.lang.StringUtils.isNotEmpty(s.trim())) {
                list.add(s);
            }
        }
        newString = list.toArray(new String[list.size()]);
        return newString;
    }

    /**
     * 创建一个分页查询对象.
     *
     * @return 空的分页查询对象
     */
    public <T> PaginationQuery<T> createPaginationQuery() {
        return new PaginationQueryImpl<T>(this);
    }


}