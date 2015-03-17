package com.innofi.component.dbconsole.pojo;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 12-7-14
 * @found time: 下午1:39
 * <p/>
 * 外键对象
 */
public class DbConsoleFk {

    /*
     * 列名
     */
    private String columnName;

    /*
     * fk名称
     */
    private String fkName;

    /*
    * 外键表名
    */
    private String fkTableName;

    /*
    * 外键更新规则
    */
    private String fkUpdateRule;

    /*
     * 外键删除规则
     */
    private String fkDeleteRule;

    /*
     * 外键表Catalog
     */
    private String fkTableCatalog;

    /*
     * 外键表Schema
     */
    private String fkTableSchema;

    /*
     * 外键表列名
     */
    private String fkColumnName;

    /*
     * 外键序号
     */
    private String fkSeq;

    /*
     * 类型export被引用 import引用
     */
    private String type;


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFkName() {
        return fkName;
    }

    public void setFkName(String fkName) {
        this.fkName = fkName;
    }

    public String getFkTableName() {
        return fkTableName;
    }

    public void setFkTableName(String fkTableName) {
        this.fkTableName = fkTableName;
    }

    public String getFkUpdateRule() {
        return fkUpdateRule;
    }

    public void setFkUpdateRule(String fkUpdateRule) {
        this.fkUpdateRule = fkUpdateRule;
    }

    public String getFkDeleteRule() {
        return fkDeleteRule;
    }

    public void setFkDeleteRule(String fkDeleteRule) {
        this.fkDeleteRule = fkDeleteRule;
    }

    public String getFkTableCatalog() {
        return fkTableCatalog;
    }

    public void setFkTableCatalog(String fkTableCatalog) {
        this.fkTableCatalog = fkTableCatalog;
    }

    public String getFkTableSchema() {
        return fkTableSchema;
    }

    public void setFkTableSchema(String fkTableSchema) {
        this.fkTableSchema = fkTableSchema;
    }

    public String getFkColumnName() {
        return fkColumnName;
    }

    public void setFkColumnName(String fkColumnName) {
        this.fkColumnName = fkColumnName;
    }

    public String getFkSeq() {
        return fkSeq;
    }

    public void setFkSeq(String fkSeq) {
        this.fkSeq = fkSeq;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
