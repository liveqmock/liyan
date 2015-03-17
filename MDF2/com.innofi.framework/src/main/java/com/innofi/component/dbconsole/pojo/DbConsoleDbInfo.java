package com.innofi.component.dbconsole.pojo;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 12-7-18
 * @found time: 下午1:07
 * <p/>
 * 数据库信息对象
 */
public class DbConsoleDbInfo {
    /*
     *数据库名称和版本
     */
    private String productName;
    /*
     *JDBC驱动名称和版本
     */
    private String jdbcDriver;
    /*
     *JDBC规范版本
     */
    private String jdbcNormVersion;
    /*
     *允许在 SQL 语句中使用的最大字符数
     */
    private String maxStatementLength;
    /*
     *非SQL92标准的关键字
     */
    private String sqlKeywords;
    /*
     *可用的系统函数
     */
    private String systemFunctions;
    /*
     *可用于数值类型的数学函数
     */
    private String numericFunctions;
    /*
     *可用于字符串类型的函数
     */
    private String stringFunctions;
    /*
     *可用于时间和日期类型的函数
     */
    private String timeDateFunctions;
    /*
    *获取此数据库允许用于列名称的最大字符数
    */
    private String maxColumnNameLength;
    /*
     *获取此数据库允许在 GROUP BY 子句中使用的最大列数
     */
    private String maxColumnsInGroupBy;
    /*
     *获取此数据库允许在索引中使用的最大列数
     */
    private String maxColumnsInIndex;
    /*
     *获取此数据库允许在 ORDER BY 子句中使用的最大列数
     */
    private String maxColumnsInOrderBy;
    /*
     *获取此数据库允许在 SELECT 列表中使用的最大列数
     */
    private String maxColumnsInSelect;
    /*
     *获取此数据库允许在表中使用的最大列数
     */
    private String maxColumnsStringable;
    /*
     *获取连接到此数据库的并发连接的可能最大数
     */
    private String maxConnections;
    /*
     *获取此数据库允许用于游标名称的最大字符数
     */
    private String maxCursorNameLength;
    /*
     *获取此数据库允许用于索引（包括索引的所有部分）的最大字节数
     */
    private String maxIndexLength;
    /*
     *获取此数据库允许用于过程名称的最大字符数
     */
    private String maxProcedureNameLength;
    /*
     *获取此数据库允许在单行中使用的最大字节数
     */
    private String maxRowSize;
    /*
     *获取此数据库允许在模式名称中使用的最大字符数
     */
    private String maxSchemaNameLength;
    /*
     *获取此数据库允许在表名称中使用的最大字符数
     */
    private String maxTableNameLength;
    /*
     *获取此数据库允许在 SELECT 语句中使用的表的最大数
     */
    private String maxTablesInSelect;
    /*
     *获取此数据库允许在用户名称中使用的最大字符数
     */
    private String maxUserNameLength;
    /*
     *是否支持事务
     */
    private boolean supportsTrans;
    /*
     *是否支持使用存储过程
     */
    private boolean supportsProcedure;
    /*
     *是否支持Statement Pooling
     */
    private boolean supportsStatPool;
    /*
     *是否支持批量更新
     */
    private boolean supportsBatchUpdate;
    /*
     *是否支持执行语句后检索自动生成的键
     */
    private boolean supportsGetGenerKey;
    /*
     *是否支持SQL类型之间的转换(CONVERT)
     */
    private boolean supportsConvert;
    /*
     *支持ANSI92 QL语法的级别
     */
    private String supportsANSI92SQLGrade;
    /*
     *是否支持比较表达式中的子查询
     */
    private boolean supportsSubqueriesInComparisons;
    /*
     *是否支持 EXISTS 表达式中的子查询
     */
    private boolean supportsSubqueriesInExists;
    /*
     *是否支持 IN 语句中的子查询
     */
    private boolean supportsSubqueriesInIns;
    /*
     *是否支持相关子查询
     */
    private boolean supportsCorrelatedSubqueries;
    /*
     *是否支持量化表达式 (quantified expression) 中的子查询
     */
    private boolean supportsSubqueriesInQuantifieds;
    /*
     *是否支持 SELECT FOR UPDATE
     */
    private boolean supportsSelectForUpdate;
    /*
     *是否支持 UNION
     */
    private boolean supportsUnion;
    /*
     *是否支持 UNION ALL
     */
    private boolean supportsUnionAll;
    /*
     *是否支持 GROUP BY
     */
    private boolean supportsGroupBy;
    /*
     *是否支持的外连接
     */
    private boolean supportsOuterJoins;
    /*
     *是否为外连接提供受限制的支持
     */
    private boolean supportsLimitedOuterJoins;
    /*
     *是否支持完全嵌套的外连接
     */
    private boolean supportsFullOuterJoins;
    /*
     *获取当前用户是否可以调用 getProcedures 方法返回的所有过程
     */
    private boolean allProceduresAreCallable;
    /*
     *获取当前用户是否可以使用 SELECT 语句中的 getTables 方法返回的所有表
     */
    private boolean allTablesAreSelectable;
    /*
     *获取此数据库是否为每个表使用一个文件
     */
    private boolean usesLocalFilePerTable;
    /*
     *获取此数据库是否支持保存点
     */
    private boolean supportsSavepoStrings;
    /*
     *获取此数据库是否支持使用不在 SELECT 语句中而在 ORDER BY 子句中的列
     */
    private boolean supportsOrderByUnrelated;
    /*
     *获取 NULL 值是否被高排序 
     */
    private boolean nullsAreSortedHigh;
    /*
     *获取 NULL 值是否被低排序
     */
    private boolean nullsAreSortedLow;
    /*
     *获取 NULL 值是否始终排在末尾，不管排序顺序如何
     */
    private boolean nullsAreSortedAtEnd;
    /*
     *获取 NULL 值是否始终排在开头，不管排序顺序如何
     */
    private boolean nullsAreSortedAtStart;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcNormVersion() {
        return jdbcNormVersion;
    }

    public void setJdbcNormVersion(String jdbcNormVersion) {
        this.jdbcNormVersion = jdbcNormVersion;
    }

    public String getMaxStatementLength() {
        return maxStatementLength;
    }

    public void setMaxStatementLength(String maxStatementLength) {
        this.maxStatementLength = maxStatementLength;
    }

    public String getSqlKeywords() {
        return sqlKeywords;
    }

    public void setSqlKeywords(String sqlKeywords) {
        this.sqlKeywords = sqlKeywords;
    }

    public String getSystemFunctions() {
        return systemFunctions;
    }

    public void setSystemFunctions(String systemFunctions) {
        this.systemFunctions = systemFunctions;
    }

    public String getNumericFunctions() {
        return numericFunctions;
    }

    public void setNumericFunctions(String numericFunctions) {
        this.numericFunctions = numericFunctions;
    }

    public String getStringFunctions() {
        return stringFunctions;
    }

    public void setStringFunctions(String stringFunctions) {
        this.stringFunctions = stringFunctions;
    }

    public String getTimeDateFunctions() {
        return timeDateFunctions;
    }

    public void setTimeDateFunctions(String timeDateFunctions) {
        this.timeDateFunctions = timeDateFunctions;
    }

    public String getMaxColumnNameLength() {
        return maxColumnNameLength;
    }

    public void setMaxColumnNameLength(String maxColumnNameLength) {
        this.maxColumnNameLength = maxColumnNameLength;
    }

    public String getMaxColumnsInGroupBy() {
        return maxColumnsInGroupBy;
    }

    public void setMaxColumnsInGroupBy(String maxColumnsInGroupBy) {
        this.maxColumnsInGroupBy = maxColumnsInGroupBy;
    }

    public String getMaxColumnsInIndex() {
        return maxColumnsInIndex;
    }

    public void setMaxColumnsInIndex(String maxColumnsInIndex) {
        this.maxColumnsInIndex = maxColumnsInIndex;
    }

    public String getMaxColumnsInOrderBy() {
        return maxColumnsInOrderBy;
    }

    public void setMaxColumnsInOrderBy(String maxColumnsInOrderBy) {
        this.maxColumnsInOrderBy = maxColumnsInOrderBy;
    }

    public String getMaxColumnsInSelect() {
        return maxColumnsInSelect;
    }

    public void setMaxColumnsInSelect(String maxColumnsInSelect) {
        this.maxColumnsInSelect = maxColumnsInSelect;
    }

    public String getMaxColumnsStringable() {
        return maxColumnsStringable;
    }

    public void setMaxColumnsInTable(String maxColumnsStringable) {
        this.maxColumnsStringable = maxColumnsStringable;
    }

    public String getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(String maxConnections) {
        this.maxConnections = maxConnections;
    }

    public String getMaxCursorNameLength() {
        return maxCursorNameLength;
    }

    public void setMaxCursorNameLength(String maxCursorNameLength) {
        this.maxCursorNameLength = maxCursorNameLength;
    }

    public String getMaxIndexLength() {
        return maxIndexLength;
    }

    public void setMaxIndexLength(String maxIndexLength) {
        this.maxIndexLength = maxIndexLength;
    }

    public String getMaxProcedureNameLength() {
        return maxProcedureNameLength;
    }

    public void setMaxProcedureNameLength(String maxProcedureNameLength) {
        this.maxProcedureNameLength = maxProcedureNameLength;
    }

    public String getMaxRowSize() {
        return maxRowSize;
    }

    public void setMaxRowSize(String maxRowSize) {
        this.maxRowSize = maxRowSize;
    }

    public String getMaxSchemaNameLength() {
        return maxSchemaNameLength;
    }

    public void setMaxSchemaNameLength(String maxSchemaNameLength) {
        this.maxSchemaNameLength = maxSchemaNameLength;
    }

    public String getMaxTableNameLength() {
        return maxTableNameLength;
    }

    public void setMaxTableNameLength(String maxTableNameLength) {
        this.maxTableNameLength = maxTableNameLength;
    }

    public String getMaxTablesInSelect() {
        return maxTablesInSelect;
    }

    public void setMaxTablesInSelect(String maxTablesInSelect) {
        this.maxTablesInSelect = maxTablesInSelect;
    }

    public String getMaxUserNameLength() {
        return maxUserNameLength;
    }

    public void setMaxUserNameLength(String maxUserNameLength) {
        this.maxUserNameLength = maxUserNameLength;
    }

    public boolean isSupportsTrans() {
        return supportsTrans;
    }

    public void setSupportsTrans(boolean supportsTrans) {
        this.supportsTrans = supportsTrans;
    }

    public boolean isSupportsProcedure() {
        return supportsProcedure;
    }

    public void setSupportsProcedure(boolean supportsProcedure) {
        this.supportsProcedure = supportsProcedure;
    }

    public boolean isSupportsStatPool() {
        return supportsStatPool;
    }

    public void setSupportsStatPool(boolean supportsStatPool) {
        this.supportsStatPool = supportsStatPool;
    }

    public boolean isSupportsBatchUpdate() {
        return supportsBatchUpdate;
    }

    public void setSupportsBatchUpdate(boolean supportsBatchUpdate) {
        this.supportsBatchUpdate = supportsBatchUpdate;
    }

    public boolean isSupportsGetGenerKey() {
        return supportsGetGenerKey;
    }

    public void setSupportsGetGenerKey(boolean supportsGetGenerKey) {
        this.supportsGetGenerKey = supportsGetGenerKey;
    }

    public boolean isSupportsConvert() {
        return supportsConvert;
    }

    public void setSupportsConvert(boolean supportsConvert) {
        this.supportsConvert = supportsConvert;
    }

    public String getSupportsANSI92SQLGrade() {
        return supportsANSI92SQLGrade;
    }

    public void setSupportsANSI92SQLGrade(String supportsANSI92SQLGrade) {
        this.supportsANSI92SQLGrade = supportsANSI92SQLGrade;
    }

    public boolean isSupportsSubqueriesInComparisons() {
        return supportsSubqueriesInComparisons;
    }

    public void setSupportsSubqueriesInComparisons(boolean supportsSubqueriesInComparisons) {
        this.supportsSubqueriesInComparisons = supportsSubqueriesInComparisons;
    }

    public boolean isSupportsSubqueriesInExists() {
        return supportsSubqueriesInExists;
    }

    public void setSupportsSubqueriesInExists(boolean supportsSubqueriesInExists) {
        this.supportsSubqueriesInExists = supportsSubqueriesInExists;
    }

    public boolean isSupportsSubqueriesInIns() {
        return supportsSubqueriesInIns;
    }

    public void setSupportsSubqueriesInIns(boolean supportsSubqueriesInIns) {
        this.supportsSubqueriesInIns = supportsSubqueriesInIns;
    }

    public boolean isSupportsCorrelatedSubqueries() {
        return supportsCorrelatedSubqueries;
    }

    public void setSupportsCorrelatedSubqueries(boolean supportsCorrelatedSubqueries) {
        this.supportsCorrelatedSubqueries = supportsCorrelatedSubqueries;
    }

    public boolean isSupportsSubqueriesInQuantifieds() {
        return supportsSubqueriesInQuantifieds;
    }

    public void setSupportsSubqueriesInQuantifieds(boolean supportsSubqueriesInQuantifieds) {
        this.supportsSubqueriesInQuantifieds = supportsSubqueriesInQuantifieds;
    }

    public boolean isSupportsSelectForUpdate() {
        return supportsSelectForUpdate;
    }

    public void setSupportsSelectForUpdate(boolean supportsSelectForUpdate) {
        this.supportsSelectForUpdate = supportsSelectForUpdate;
    }

    public boolean isSupportsUnion() {
        return supportsUnion;
    }

    public void setSupportsUnion(boolean supportsUnion) {
        this.supportsUnion = supportsUnion;
    }

    public boolean isSupportsUnionAll() {
        return supportsUnionAll;
    }

    public void setSupportsUnionAll(boolean supportsUnionAll) {
        this.supportsUnionAll = supportsUnionAll;
    }

    public boolean isSupportsGroupBy() {
        return supportsGroupBy;
    }

    public void setSupportsGroupBy(boolean supportsGroupBy) {
        this.supportsGroupBy = supportsGroupBy;
    }

    public boolean isSupportsOuterJoins() {
        return supportsOuterJoins;
    }

    public void setSupportsOuterJoins(boolean supportsOuterJoins) {
        this.supportsOuterJoins = supportsOuterJoins;
    }

    public boolean isSupportsLimitedOuterJoins() {
        return supportsLimitedOuterJoins;
    }

    public void setSupportsLimitedOuterJoins(boolean supportsLimitedOuterJoins) {
        this.supportsLimitedOuterJoins = supportsLimitedOuterJoins;
    }

    public boolean isSupportsFullOuterJoins() {
        return supportsFullOuterJoins;
    }

    public void setSupportsFullOuterJoins(boolean supportsFullOuterJoins) {
        this.supportsFullOuterJoins = supportsFullOuterJoins;
    }

    public boolean isAllProceduresAreCallable() {
        return allProceduresAreCallable;
    }

    public void setAllProceduresAreCallable(boolean allProceduresAreCallable) {
        this.allProceduresAreCallable = allProceduresAreCallable;
    }

    public boolean isAllTablesAreSelectable() {
        return allTablesAreSelectable;
    }

    public void setAllTablesAreSelectable(boolean allTablesAreSelectable) {
        this.allTablesAreSelectable = allTablesAreSelectable;
    }

    public boolean isUsesLocalFilePerTable() {
        return usesLocalFilePerTable;
    }

    public void setUsesLocalFilePerTable(boolean usesLocalFilePerTable) {
        this.usesLocalFilePerTable = usesLocalFilePerTable;
    }

    public boolean isSupportsSavepoStrings() {
        return supportsSavepoStrings;
    }

    public void setSupportsSavepoints(boolean supportsSavepoStrings) {
        this.supportsSavepoStrings = supportsSavepoStrings;
    }

    public boolean isSupportsOrderByUnrelated() {
        return supportsOrderByUnrelated;
    }

    public void setSupportsOrderByUnrelated(boolean supportsOrderByUnrelated) {
        this.supportsOrderByUnrelated = supportsOrderByUnrelated;
    }

    public boolean isNullsAreSortedHigh() {
        return nullsAreSortedHigh;
    }

    public void setNullsAreSortedHigh(boolean nullsAreSortedHigh) {
        this.nullsAreSortedHigh = nullsAreSortedHigh;
    }

    public boolean isNullsAreSortedLow() {
        return nullsAreSortedLow;
    }

    public void setNullsAreSortedLow(boolean nullsAreSortedLow) {
        this.nullsAreSortedLow = nullsAreSortedLow;
    }

    public boolean isNullsAreSortedAtEnd() {
        return nullsAreSortedAtEnd;
    }

    public void setNullsAreSortedAtEnd(boolean nullsAreSortedAtEnd) {
        this.nullsAreSortedAtEnd = nullsAreSortedAtEnd;
    }

    public boolean isNullsAreSortedAtStart() {
        return nullsAreSortedAtStart;
    }

    public void setNullsAreSortedAtStart(boolean nullsAreSortedAtStart) {
        this.nullsAreSortedAtStart = nullsAreSortedAtStart;
    }

}
