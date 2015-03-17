package com.innofi.framework.utils.sql;

import com.innofi.framework.exception.SQLParserRuntimeException;
import com.innofi.framework.pojo.SimpleSqlPojo;
import com.innofi.framework.utils.date.DateUtil;
import com.innofi.framework.utils.string.StringUtil;
//import gudusoft.gsqlparser.EDbVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.engine.jdbc.internal.DDLFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.springframework.util.Assert;

import java.sql.DatabaseMetaData;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 12-7-13
 * @found time: 上午10:58
 * <p/>
 * SQL工具类
 */
public class SqlUtil {

    private static Logger _log = LoggerFactory.getLogger(SqlUtil.class);

    /**
     * 数据库类型和Dorado7类型映射，
     */
    private static final Map<String,String> columnTypeMap;
    static {
        columnTypeMap=new HashMap<String, String>();
        columnTypeMap.put("DATETIME", "Date");
        columnTypeMap.put("TIMESTAMP", "Date");
        columnTypeMap.put("DATE", "Date");
        columnTypeMap.put("TIME", "Date");

        columnTypeMap.put("BOOLEAN", "Boolean");
        columnTypeMap.put("DECIMAL", "BigDecimal");
        columnTypeMap.put("DOUBLE", "Double");
        columnTypeMap.put("FLOAT", "Float");

        columnTypeMap.put("INT", "Integer");
        columnTypeMap.put("NUMBER", "Integer");
        columnTypeMap.put("SMALLINT", "Integer");
        columnTypeMap.put("INTEGER", "Integer");
        columnTypeMap.put("BIGINT", "Integer");
        columnTypeMap.put("BIGINT", "Integer");
        columnTypeMap.put("TINYINT", "Integer");
        columnTypeMap.put("TINYINT UNSIGNED", "Integer");
        columnTypeMap.put("INT UNSIGNED", "Integer");

    }

    /**
     * 根据数据库类型获得Dorado类型
     * @param columnType 数据库类型
     * @return dorado对应类型
     */
    public static String getDroadoType(String columnType){
        for(Map.Entry<String, String> entry:columnTypeMap.entrySet()){
            if(columnType.toLowerCase().endsWith(entry.getKey().toLowerCase())){
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * select语句格式化，返回格式化以后的Sql
     *
     * @param sql Sql语句
     * @return 格式化以后的
     */
    public static String selectFormatSql(String sql) {
        Formatter formatter = new BasicFormatterImpl();
        return formatter.format(sql);
    }

    /**
     * DDL语句格式化，返回格式化以后的Sql
     *
     * @param sql Sql语句
     * @return 格式化以后的
     */
    public static String formatDDLSql(String sql) {
        Formatter formatter = new DDLFormatterImpl();
        return formatter.format(sql);
    }

    /**
     * 获得记录总数SQL
     *
     * @param sql 查询语句
     * @return 总数sql
     */
    public static String getCountSql(String sql) {
        String countSql =" select count (*) from (" +sql+") A ";
        return countSql;
    }

    /**
     * 获得记录总数HQL
     *
     * @param hql 查询语句
     * @return 总数sql
     */
    public static String getCountHql(String hql){
        String countHql = " select count (*) " + removeSelect(removeOrders(hql));
        return countHql;
    }

    /**
     * returns true if the passed JDBC data type (from java.sql.Types)
     * indicates a data type which maps to a integer type
     */
    public static boolean isIntegerType(int aSqlType) {
        return (aSqlType == Types.BIGINT ||
                aSqlType == Types.INTEGER ||
                aSqlType == Types.SMALLINT ||
                aSqlType == Types.TINYINT);
    }

    /**
     * Returns true if the given JDBC type indicates some kind of
     * character data (including CLOBs)
     */
    public static boolean isCharacterType(int aSqlType) {
        return (aSqlType == Types.VARCHAR ||
                aSqlType == Types.CHAR ||
                aSqlType == Types.CLOB ||
                aSqlType == Types.LONGVARCHAR ||
                aSqlType == Types.NVARCHAR ||
                aSqlType == Types.NCHAR ||
                aSqlType == Types.LONGNVARCHAR ||
                aSqlType == Types.NCLOB);
    }

    /**
     * Returns true if the given JDBC type is a CHAR or VARCHAR type
     *
     * @param aSqlType
     */
    public static boolean isCharacterTypeWithLength(int aSqlType) {
        return (aSqlType == Types.VARCHAR ||
                aSqlType == Types.CHAR ||
                aSqlType == Types.NVARCHAR ||
                aSqlType == Types.NCHAR);
    }

    /**
     * returns true if the passed data type (from java.sql.Types)
     * indicates a data type which can hold numeric values with
     * decimals
     */
    public static boolean isDecimalType(int aSqlType, int aScale, int aPrecision) {
        if (aSqlType == Types.DECIMAL ||
                aSqlType == Types.DOUBLE ||
                aSqlType == Types.FLOAT ||
                aSqlType == Types.NUMERIC ||
                aSqlType == Types.REAL) {
            return (aScale > 0);
        } else {
            return false;
        }
    }

    // 是否是数值类型..
    public static boolean isNumberType(int jdbcType) {
        boolean result = false;
        switch (jdbcType) {
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.BIGINT:
            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.INTEGER:
            case Types.NUMERIC:
                result = true;
                break;
            default:
                result = false;
        }
        return result;
    }

    // 是否是日期类型
    public static boolean isDateType(int jdbcType) {
        boolean result = false;
        switch (jdbcType) {
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                result = true;
                break;
            default:
                result = false;
        }
        return result;
    }

    // 是否是CLOB类型
    public static boolean isClobType(int jdbcType) {
        boolean result = false;
        switch (jdbcType) {
            case Types.LONGVARCHAR:
                // case Types.LONGNVARCHAR:
            case Types.CLOB:
                result = true;
                break;
            default:
                result = false;
        }
        return result;
    }

    // 是否是BLOB类型
    public static boolean isBlobType(int jdbcType) {
        boolean result = false;
        switch (jdbcType) {
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB:
                // case Types.LONGNVARCHAR:
                result = true;
                break;
            default:
                result = false;
        }
        return result;
    }

    // 是否是字符类型的
    public static boolean isStringType(int jdbcType) {
        boolean result = false;
        switch (jdbcType) {
            case Types.CHAR:
            case Types.VARCHAR:
                result = true;
                break;
            default:
                result = false;
        }
        return result;
    }

    // 是否是不能够直接显示的类型
    public static boolean isLongType(int jdbcType) {
        boolean result = false;
        result = !isDateType(jdbcType) && !isStringType(jdbcType)
                && !isNumberType(jdbcType) && jdbcType != Types.BOOLEAN;
        return result;
    }

    // 通过JDBC SQLType代码，取得类型名称
    public static String getJdbcTypeName(int jdbcType) {
        String typeName = "";
        switch (jdbcType) {
            case Types.ARRAY:
                typeName = "ARRAY";
                break;
            case Types.BOOLEAN:
                typeName = "BOOLEAN";
                break;
            case Types.BIGINT:
                typeName = "BIGINT";
                break;
            case Types.BINARY:
                typeName = "BINARY";
                break;
            case Types.BIT:
                typeName = "BIT";
                break;
            case Types.BLOB:
                typeName = "BLOB";
                break;
            case Types.CHAR:
                typeName = "CHAR";
                break;
            case Types.CLOB:
                typeName = "CLOB";
                break;
            case Types.DATALINK:
                typeName = "DATALINK";
                break;
            case Types.DATE:
                typeName = "DATE";
                break;
            case Types.DECIMAL:
                typeName = "DECIMAL";
                break;
            case Types.DISTINCT:
                typeName = "DISTINCT";
                break;
            case Types.DOUBLE:
                typeName = "DOUBLE";
                break;
            case Types.FLOAT:
                typeName = "FLOAT";
                break;
            case Types.INTEGER:
                typeName = "INTEGER";
                break;
            case Types.JAVA_OBJECT:
                typeName = "JAVA_OBJECT";
                break;
            case Types.LONGVARBINARY:
                typeName = "LONGVARBINARY";
                break;
            case Types.LONGVARCHAR:
                typeName = "LONGVARCHAR";
                break;
            // 以下为JDK1.6才添加的内容
            case Types.LONGNVARCHAR:
                typeName = "LONGNVARCHAR";
                break;
            case Types.NCHAR:
                typeName = "NCHAR";
                break;
            case Types.NCLOB:
                typeName = "NCLOB";
                break;
            case Types.NVARCHAR:
                typeName = "NCLOB";
                break;
            case Types.ROWID:
                typeName = "ROWID";
                break;
            case Types.SQLXML:
                typeName = "SQLXML";
                break;
            case Types.NULL:
                typeName = "NULL";
                break;
            case Types.NUMERIC:
                typeName = "NUMERIC";
                break;
            case Types.OTHER:
                typeName = "OTHER";
                break;
            case Types.REAL:
                typeName = "REAL";
                break;
            case Types.REF:
                typeName = "REF";
                break;
            case Types.SMALLINT:
                typeName = "SMALLINT";
                break;
            case Types.STRUCT:
                typeName = "STRUCT";
                break;
            case Types.TIME:
                typeName = "TIME";
                break;
            case Types.TIMESTAMP:
                typeName = "TIMESTAMP";
                break;
            case Types.TINYINT:
                typeName = "TINYINT";
                break;
            case Types.VARBINARY:
                typeName = "VARBINARY";
                break;
            case Types.VARCHAR:
                typeName = "VARCHAR";
                break;
        }
        return typeName;
    }

    // 通过JDBC SQLType的类型名称 取得 类型代码
    public static int getJdbcTypeName(String jdbcTypeName) {
        int type = 0;
        if ("ARRAY".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.ARRAY;
        } else if ("BIGINT".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.BIGINT;
        } else if ("BINARY".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.BINARY;
        } else if ("BIT".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.BIT;
        } else if ("BLOB".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.BLOB;
        } else if ("BOOLEAN".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.BOOLEAN;
        } else if ("CHAR".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.CHAR;
        } else if ("CLOB".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.CLOB;
        } else if ("DATE".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.DATE;
        } else if ("DATALINK".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.DATALINK;
        } else if ("DECIMAL".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.DECIMAL;
        } else if ("DISTINCT".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.DISTINCT;
        } else if ("DOUBLE".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.DOUBLE;
        } else if ("FLOAT".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.FLOAT;
        } else if ("INTEGER".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.INTEGER;
        } else if ("JAVA_OBJECT".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.JAVA_OBJECT;
        } else if ("LONGVARBINARY".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.LONGVARBINARY;
        } else if ("LONGVARCHAR".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.LONGVARCHAR;
        } else if ("NULL".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.NULL;
        } else if ("NUMERIC".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.NUMERIC;
        } else if ("OTHER".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.OTHER;
        } else if ("REAL".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.REAL;
        } else if ("REF".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.REF;
        } else if ("SMALLINT".equals(jdbcTypeName)) {
            type = Types.SMALLINT;
        } else if ("STRUCT".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.STRUCT;
        } else if ("TIME".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.TIME;
        } else if ("TIMESTAMP".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.TIMESTAMP;
        } else if ("TINYINT".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.TINYINT;
        } else if ("VARBINARY".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.VARBINARY;
        } else if ("VARCHAR".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.VARCHAR;
            // 以下为JDK1.6中才添加的内容..
        } else if ("LONGNVARCHAR".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.LONGNVARCHAR;
        } else if ("NCHAR".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.NCHAR;
        } else if ("NCLOB".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.NCLOB;
        } else if ("ROWID".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.ROWID;
        } else if ("SQLXML".equalsIgnoreCase(jdbcTypeName)) {
            type = Types.SQLXML;
        } else {
            throw new RuntimeException("jdbcTypeName 无效~");
        }
        return type;
    }

    // 通过JDBC SQLType代码及字符类型的值，取得特定类型的对象值
    public static Object getSQLValueObject(int type, String value, String param) {
        Object objValue = null;
        switch (type) {
            case Types.VARCHAR:
                objValue = value;
                break;
            case Types.BOOLEAN:
                objValue = Boolean.valueOf(value);
                break;
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.SMALLINT:
            case Types.TINYINT:
                objValue = Integer.valueOf(value);
                break;
            case Types.FLOAT:
                objValue = Float.valueOf(value);
                break;
            case Types.DECIMAL:
            case Types.NUMERIC:
            case Types.DOUBLE:
                objValue = Double.valueOf(value);
                break;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                Date date = DateUtil.parseDate(value, param);
                if (type == Types.DATE) {
                    objValue = new java.sql.Date(date.getTime());
                } else if (type == Types.TIME) {
                    objValue = new Time(date.getTime());
                } else {
                    objValue = new Timestamp(date.getTime());
                }
                break;
            default:
                String msg = "无法处理的类型，" + getJdbcTypeName(type);
                _log.error("getSQLValueObject出错：" + msg);
                throw new RuntimeException(msg);
        }
        return objValue;
    }

    /**
     * Construct the SQL display name for the given SQL datatype.
     * This is used when re-recreating the source for a table
     */
    public static String getSqlTypeDisplay(String typeName, int sqlType, int size, int digits) {
        String display = typeName;

        switch (sqlType) {
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.NVARCHAR:
            case Types.NCHAR:
                // Postgres' text datatype does not have a size parameter
                if ("text".equals(typeName)) return "text";
                // Some drivers already include the column size in the data type;

                if (display.indexOf('(') > -1) return display;

                if (size > 0) {
                    display = typeName + "(" + size + ")";
                }

                break;

            case Types.DOUBLE:
            case Types.REAL:
                display = typeName;
                break;

            case Types.FLOAT:
                if (size > 0) {
                    display = typeName + "(" + size + ")";
                } else {
                    display = typeName;
                }
                break;

            case Types.DECIMAL:
            case Types.NUMERIC:
                if ("money".equalsIgnoreCase(typeName)) // SQL Server
                {
                    display = typeName;
                } else if ((typeName.indexOf('(') == -1)) {
                    if (digits > 0 && size > 0) {
                        display = typeName + "(" + size + "," + digits + ")";
                    } else if (size <= 0 && digits > 0) {
                        display = typeName + "(" + digits + ")";
                    } else if (size > 0 && digits <= 0) {
                        display = typeName + "(" + size + ")";
                    }
                }
                break;

            case Types.OTHER:
                if (typeName.toUpperCase().startsWith("NVARCHAR")) {
                    display = typeName + "(" + size + ")";
                } else if ("NCHAR".equalsIgnoreCase(typeName)) {
                    display = typeName + "(" + size + ")";
                } else if ("UROWID".equalsIgnoreCase(typeName)) {
                    display = typeName + "(" + size + ")";
                } else if ("RAW".equalsIgnoreCase(typeName)) {
                    display = typeName + "(" + size + ")";
                } else
                    break;

            default:
                display = typeName;
                if ("VARCHAR () FOR BIT DATA".equalsIgnoreCase(typeName)) {
                    display = typeName.substring(0, typeName.indexOf("(") + 1) + size + typeName.substring(typeName.indexOf(")"), typeName.length());
                } else if ("CHAR () FOR BIT DATA".equalsIgnoreCase(typeName)) {
                    display = typeName.substring(0, typeName.indexOf("(") + 1) + size + typeName.substring(typeName.indexOf(")"), typeName.length());
                }
                break;
        }
        return display;
    }

    public static String getFkUpdateRuleForDisplay(int rule) {
        String display = null;
        switch (rule) {
            case DatabaseMetaData.importedKeyNoAction:
            case DatabaseMetaData.importedKeyRestrict:
                display = "不能更新";
                break;
            case DatabaseMetaData.importedKeyCascade:
                display = "与主键关联更新";
                break;
            case DatabaseMetaData.importedKeySetDefault:
                display = "更新后值为默认值";
                break;
            case DatabaseMetaData.importedKeySetNull:
                display = "更新后值未null";
                break;
        }
        return display;
    }

    public static String getFkDeleteRuleForDisplay(int rule) {
        String display = null;
        switch (rule) {
            case DatabaseMetaData.importedKeyNoAction:
            case DatabaseMetaData.importedKeyRestrict:
                display = "不能删除";
                break;
            case DatabaseMetaData.importedKeyCascade:
                display = "与主键关联删除";
                break;
            case DatabaseMetaData.importedKeySetDefault:
                display = "删除后值为默认值";
                break;
            case DatabaseMetaData.importedKeySetNull:
                display = "删除后值未null";
                break;
        }
        return display;
    }

    /**
     * 解析传入的sql为sql的对象
     * @param sql 传入的sql语句
     * @return sql对象
     */
    public static SimpleSqlPojo parse(String sql) throws SQLParserRuntimeException{
        if(!isSimpleSql(sql)){
            throw new SQLParserRuntimeException("非简单SQL语句，不可用SimpleSqlParser进行解析！");
        }
        final String querySql = sql.trim();

        String cols = parseCols(querySql);
        String tables = parseTables(querySql);
        String conditions = parseRestrictions(querySql);
        String groupBy = parseGroupCols(querySql);
        String orderBy = parseOrderCols(querySql);

        SimpleSqlPojo pojo = new SimpleSqlPojo();
        pojo.setCols(cols);
        pojo.setTables(tables);
        pojo.setRestrictions(conditions);
        pojo.setGroupCols(groupBy);
        pojo.setOrderCols(orderBy);

        return pojo;
    }

    /**
     * 判断是否简单sql
     * @param sql 所判断的sql语句
     * @return 若为简单sql，则返回true；否则返回false
     */
    public static boolean isSimpleSql(String sql){
        if(sql==null){
            return false;
        }
        if( StringUtil.countMatches(sql.toLowerCase(), "select")!=1 ){
            return false;
        }
        if( StringUtil.countMatches(sql.toLowerCase(), "from")!=1 ){
            return false;
        }
        return true;
    }

    /**
     * 解析选择的列
     *
     * @param sql
     * @return
     */
    public static String parseCols(String sql) {
        String regex = "(select)(.+)(from)";
        String cols = getMatchedString(regex, sql);
        return cols;
    }

    /**
     * 解析选择的表
     *
     * @param sql
     * @return
     */
    public static  String parseTables(String sql) {
        String regex = "";

        if (isContains(sql, "\\s+where\\s+")) {
            regex = "(from)(.+)(where)";
        } else {
            regex = "(from)(.+)($)";
        }

        String tables = getMatchedString(regex, sql);
        return tables;
    }

    /**
     * 解析查找条件
     *
     * @param sql
     * @return
     */
    public static String parseRestrictions(String sql) {
        String regex = "";

        if (isContains(sql, "\\s+where\\s+")) {
            // 包括Where，有条件

            if (isContains(sql, "group\\s+by")) {
                // 条件在where和group by之间
                regex = "(where)(.+)(group\\s+by)";
            } else if (isContains(sql, "order\\s+by")) {
                // 条件在where和order by之间
                regex = "(where)(.+)(order\\s+by)";
            } else {
                // 条件在where到字符串末尾
                regex = "(where)(.+)($)";
            }
        } else {
            // 不包括where则条件无从谈起，返回即可
            return null;
        }

        String conditions = getMatchedString(regex, sql);
        return conditions;
    }

    /**
     * 解析GroupBy的字段
     *
     * @param sql
     * @return
     */
    public static String parseGroupCols(String sql) {
        String regex = "";

        if (isContains(sql, "group\\s+by")) {
            // 包括GroupBy，有分组字段

            if (isContains(sql, "order\\s+by")) {
                // group by 后有order by
                regex = "(group\\s+by)(.+)(order\\s+by)";
            } else {
                // group by 后无order by
                regex = "(group\\s+by)(.+)($)";
            }
        } else {
            // 不包括GroupBy则分组字段无从谈起，返回即可
            return null;
        }

        String groupCols = getMatchedString(regex, sql);
        return groupCols;
    }

    /**
     * 解析OrderBy的字段
     *
     * @param sql
     * @return
     */
    public static String parseOrderCols(String sql) {
        String regex = "";

        if (isContains(sql, "order\\s+by")) {
            regex = "(order\\s+by)(.+)($)";
        } else {
            return null;
        }

        String orderCols = getMatchedString(regex, sql);
        return orderCols;
    }


    /**
     * 从文本text中找到regex首次匹配的字符串，不区分大小写
     *
     * @param regex：       正则表达式
     * @param text：欲查找的字符串
     * @return regex首次匹配的字符串，如未匹配返回空
     */
    private static String getMatchedString(String regex, String text) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            return matcher.group(2);
        }

        return null;
    }

    /*
     * 看word是否在lineText中存在，支持正则表达式
     *
     */
    private static boolean isContains(String lineText, String word) {
        Pattern pattern = Pattern.compile(word, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(lineText);
        return matcher.find();
    }


    /*
     * 看word是否在array中存在
     */
    private static boolean ifWordInArrayIgnoreCase(String word,String[] array){
        if(array==null){
            return false;
        }
        for(int i=0;i<array.length;i++){
            if(word.trim().equalsIgnoreCase(array[i].trim())){
                return true;
            }
        }
        return false;
    }

    // 判断字符串是否 是/包含 html内容..
    public static boolean isHTMLContent(String content) {
        String[] tags = new String[] { "<html>", "<head>", "<title>",
                "<script", "<meta", "<link", "<body", "<table", "<tr", "<td",
                "<form", "<img", "<input", "<frame", "<iframe", "<frameset",
                "<hr", "<dd", "<dl", "<dt", "<dir", "<ll", "<li", "<ul","<span" };

        content = content.toLowerCase();
        for (String tag : tags) {
            if (content.contains(tag)) {
                _log.debug("[" + content + "] isHTML=" + true);
                return true;
            }
        }
        return false;
    }

    /*
    * 删除select语句
    */
    private static String removeSelect(String hql) {
        Assert.hasText(hql);
        while (true) {
            int Pos = hql.toLowerCase().indexOf("from");
            String firstStr = "";
            String secondStr = "";
            if (Pos != -1) {
                firstStr = removeParenthesis(hql.substring(0, Pos));

                secondStr = hql.substring(Pos);
                int beginpos = firstStr.lastIndexOf("(");
                if (beginpos >= 0) {
                    int endpos = secondStr.indexOf(")");
                    hql = firstStr.substring(0, beginpos)
                            + secondStr.substring(endpos + 1);

                } else {

                    return hql.substring(Pos);
                }

            } else {
                Assert.isTrue(Pos != -1, " sql : " + hql
                        + " must has a keyword 'from'");
                return hql.substring(Pos);
            }
        }
    }


    /*
     *去掉参数String中有 ( )之间的部分
     */
    private static String removeParenthesis(String input) {
        while (true) {
            int Pos = input.toLowerCase().indexOf(")");
            String firstStr = "";
            String secondStr = "";
            if (Pos != -1) {
                firstStr = input.substring(0, Pos);
                secondStr = input.substring(Pos);
                int beginPos = firstStr.lastIndexOf("(");
                if (beginPos >= 0) {
                    if (secondStr.length() > 1)
                        input = firstStr.substring(0, beginPos)
                                + secondStr.substring(1);
                    else
                        input = firstStr.substring(0, beginPos);
                } else {
                    return input;
                }

            } else {
                return input;
            }

        }
    }

    /*
     * 去除sql的orderby 子句，用于pagedQuery.
     *
     * @see # pagedQuery(String, int, int, Object[])
     */
    private static String removeOrders(String hql) {
        Assert.hasText(hql);
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(hql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }






}
