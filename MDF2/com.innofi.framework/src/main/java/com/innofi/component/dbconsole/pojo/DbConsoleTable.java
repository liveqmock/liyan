package com.innofi.component.dbconsole.pojo;

import com.innofi.framework.utils.string.DbConsoleStringTokenizer;
import com.innofi.framework.utils.string.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          <p/>
 *          数据库表、视图对象
 * @found date: 2012-5-20
 * @found time: 下午3:47:07
 */
public class DbConsoleTable implements Comparable<DbConsoleTable> {

    /*
    * 数据表名称
    */
    private String tableName;
    /*
     * 数据表归属schema
     */
    private String schema;
    /*
     * 数据表规程catalog
     */
    private String catalog;
    /*
     * 完全限定命名
     */
    private String fullQualifyName;

    private String server; // for SQL Server syntax  todo 暂不清楚用处

    /*
     * 类型目前包括：TABLE,VIEW,SEQUENCE
     */
    private String type;

    /*
     * 表的注释
     */
    private String tableComment;

    /*
     * 主键对象
     */
    private DbConsolePk primaryKey;

    private List<DbConsoleFk> fks;

    /*
     * 表包含的列集合
     */
    private List<DbConsoleColumn> columns;

    /*
     * 表中的数据
     */
    private List<Map<String, Object>> data;

    /*
     * DDL语句
     */
    private String source;


    /*
     * DBMS specific tablespace options
     */
    private String tableSpace;

    public DbConsoleTable(List<Map<String, Object>> data) {
        this.data = data;
    }

    public DbConsoleTable(String aName) {
        this.setTable(aName);
    }

    public DbConsoleTable(String aName, char catalogSeparator) {
        this.setTable(aName, catalogSeparator);
    }

    public DbConsoleTable() {
        this.schema = null;
        this.catalog = null;
        this.tableName = null;
    }

    public DbConsoleTable(String aSchema, String aTable) {
        this.setCatalog(null);
        this.setTable(aTable);
        this.setSchema(aSchema);
    }

    public DbConsoleTable(String aCatalog, String aSchema, String aTable) {
        this.setTable(aTable);
        this.setCatalog(aCatalog);
        this.setSchema(aSchema);
    }

    public String getFullQualifyName() {
        return fullQualifyName;
    }

    public void setFullQualifyName(String fullQualifyName) {
        this.fullQualifyName = fullQualifyName;
    }

    /**
     * Return the tablespace used for this dbConsoleTable (if applicable)
     */
    public String getTablespace() {
        return tableSpace;
    }

    public void setTablespace(String tableSpaceName) {
        this.tableSpace = tableSpaceName;
    }


    public void setComment(String comment) {
        this.tableComment = comment;
    }


    public String getComment() {
        return tableComment;
    }


    public String getObjectType() {
        if (type == null) return "TABLE";
        return type.toUpperCase();
    }


    public String getObjectName() {
        return getTableName();
    }

    public DbConsoleTable createCopy() {
        DbConsoleTable copy = new DbConsoleTable();
        copy.primaryKey = this.primaryKey == null ? null : this.primaryKey.createCopy();
        copy.schema = this.schema;
        copy.tableName = this.tableName;
        copy.catalog = this.catalog;
        copy.server = this.server;
        copy.type = this.type;
        copy.tableComment = this.tableComment;
        return copy;
    }

    public String getTableName() {
        return tableName;
    }

    public final void setTable(String aTable) {
        setTable(aTable, '.');
    }

    public final void setTable(String aTable, char catalogSeparator) {
        if (aTable == null) {
            this.tableName = null;
            this.schema = null;
            return;
        }

        List<String> elements = new ArrayList<String>(4);
        DbConsoleStringTokenizer tok = new DbConsoleStringTokenizer(catalogSeparator, "\"", true);
        tok.setSourceString(aTable);
        while (tok.hasMoreTokens()) {
            elements.add(tok.nextToken());
        }

        if (elements.size() == 1) {
            setTableName(aTable);
        } else if (elements.size() == 2) {
            setSchema(elements.get(0));
            setTableName(elements.get(1));
        } else if (elements.size() == 3) {
            setCatalog(elements.get(0));
            setSchema(elements.get(1));
            setTableName(elements.get(2));
        } else if (elements.size() == 4) {
            setServerPart(elements.get(0));
            setCatalog(elements.get(1));
            setSchema(elements.get(2));
            setTableName(elements.get(3));
        }
    }

    /**
     * For a dbConsoleTable specified using SQL Server's extended syntax
     * that allows 4 elements, return the first element. Usually this is the
     * name of the linked server.
     *
     * @return the name of the server part if specified, null otherwise
     */
    public String getServerPart() {
        return server;
    }

    public void setServerPart(String name) {
        if (!StringUtil.hasText(name)) {
            server = null;
        } else {
            server = name.trim();
        }
    }

    private void setTableName(String name) {
        if (name == null) return;
        this.tableName = StringUtil.trimQuotes(name).trim();
    }

    public String getSchema() {
        return schema;
    }

    public final void setSchema(String aSchema) {
        if (!StringUtil.hasText(aSchema)) {
            this.schema = null;
        } else {
            this.schema = StringUtil.trimQuotes(aSchema).trim();
        }
    }

    public String getCatalog() {
        if (catalog == null) return null;
        StringBuilder result = new StringBuilder(catalog.length() + 2);
        result.append('\"');
        result.append(catalog);
        result.append('\"');
        return result.toString();
    }

    public final void setCatalog(String aCatalog) {
        if (!StringUtil.hasText(aCatalog)) {
            this.catalog = null;
        } else {
            this.catalog = StringUtil.trimQuotes(aCatalog).trim();
        }
    }

    public String getPrimaryKeyName() {
        if (this.primaryKey == null) return null;
        return this.primaryKey.getPkName();
    }

    public DbConsolePk getPrimaryKey() {
        return this.primaryKey;
    }

    public void setPrimaryKey(DbConsolePk dbConsolePk) {
        this.primaryKey = dbConsolePk;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static DbConsoleTable findTableByName(List<DbConsoleTable> dbConsoleTables, String toFind) {
        return findTableByName(dbConsoleTables, new DbConsoleTable(toFind));
    }

    public static DbConsoleTable findTableByName(List<DbConsoleTable> dbConsoleTables, DbConsoleTable toFind) {
        if (dbConsoleTables == null) return null;

        for (DbConsoleTable dbConsoleTable : dbConsoleTables) {
            if (dbConsoleTable.getTableName().equalsIgnoreCase(toFind.getTableName())) return dbConsoleTable;
        }
        return null;
    }

    public List<DbConsoleColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DbConsoleColumn> columns) {
        this.columns = columns;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<DbConsoleFk> getFks() {
        return fks;
    }

    public void setFks(List<DbConsoleFk> fks) {
        this.fks = fks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbConsoleTable that = (DbConsoleTable) o;
        if (catalog != null ? !catalog.equals(that.catalog) : that.catalog != null) return false;
        if (primaryKey != null ? !primaryKey.equals(that.primaryKey) : that.primaryKey != null) return false;
        if (schema != null ? !schema.equals(that.schema) : that.schema != null) return false;
        if (server != null ? !server.equals(that.server) : that.server != null) return false;
        if (tableComment != null ? !tableComment.equals(that.tableComment) : that.tableComment != null) return false;
        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) return false;
        if (tableSpace != null ? !tableSpace.equals(that.tableSpace) : that.tableSpace != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tableName != null ? tableName.hashCode() : 0;
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + (catalog != null ? catalog.hashCode() : 0);
        result = 31 * result + (server != null ? server.hashCode() : 0);
        result = 31 * result + (primaryKey != null ? primaryKey.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (tableComment != null ? tableComment.hashCode() : 0);
        result = 31 * result + (tableSpace != null ? tableSpace.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DbConsoleTable{" +
                "tableName='" + tableName + '\'' +
                ", schema='" + schema + '\'' +
                ", catalog='" + catalog + '\'' +
                ", server='" + server + '\'' +
                ", primaryKey=" + primaryKey +
                ", type='" + type + '\'' +
                ", tableComment='" + tableComment + '\'' +
                ", tableSpace='" + tableSpace + '\'' +
                '}';
    }

    public int compareTo(DbConsoleTable dbConsoleTable) {
        return 0;
    }

}
