package com.innofi.component.dbconsole.pojo;

import java.util.List;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          <p/>
 *          数据库列对象
 * @found date: 2012-5-20
 * @found time: 下午3:47:07
 */
public class DbConsoleColumn {

    /*
     * 表名
     */
    private String tableName;
    /*
     * 列名
     */
    private String columnName;
    /*
     * 列类型
     */
    private String columnType;
    /*
     * 列在表中索引
     */
    private String columnSeq;
    /*
     * 列类型显示
     */
    private String typeDisplay;

    /*
    * 列长度
    */
    private String columnSize;

    /*
     * 默认值
     */
    private String defaultValue;

    /*
     * 是否可为null
     */

    private boolean isnullAble;

    /*
     * 是否pk
     */
    private boolean isprimaryKey;

    /*
     * 是否自动增长
     */
    private boolean isautoincrement;

    /*
     * 注释
     */
    private String remarks;

    /*
     * PK名称
     */
    private String pkName;

    /*
    * 主键序号
    */
    private String pkSeq;

    /*
     * 是否外键
     */
    private boolean isFk;

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
     * 外键表名
     */
    private String fkTableName;

    /*
     * 外键表列名
     */
    private String fkColumnName;

    /*
     * 外键序号
     */
    private String fkSeq;

    /*
     * 外键名称
     */
    private String fkName;

    /*
     * PK类表
     */
    private List<String> listPrimaryKey;

    public DbConsoleColumn() {
    }

    public DbConsoleColumn(String columnName, String typeName,String columnType, String columnSize, boolean isnullAble) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.columnSize = columnSize;
        this.isnullAble = isnullAble;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(String columnSize) {
        this.columnSize = columnSize;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isIsnullAble() {
        return isnullAble;
    }

    public void setIsnullAble(boolean isnullAble) {
        this.isnullAble = isnullAble;
    }

    public boolean isIsprimaryKey() {
        return isprimaryKey;
    }

    public void setIsprimaryKey(boolean isprimaryKey) {
        this.isprimaryKey = isprimaryKey;
    }

    public boolean isIsautoincrement() {
        return isautoincrement;
    }

    public void setIsautoincrement(boolean isautoincrement) {
        this.isautoincrement = isautoincrement;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public String getPkSeq() {
        return pkSeq;
    }

    public void setPkSeq(String pkSeq) {
        this.pkSeq = pkSeq;
    }

    public boolean getIsFk() {
        return isFk;
    }

    public void setIsFk(boolean fk) {
        isFk = fk;
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

    public String getFkTableName() {
        return fkTableName;
    }

    public void setFkTableName(String fkTableName) {
        this.fkTableName = fkTableName;
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

    public String getFkName() {
        return fkName;
    }

    public void setFkName(String fkName) {
        this.fkName = fkName;
    }

    public List<String> getListPrimaryKey() {
        return listPrimaryKey;
    }

    public void setListPrimaryKey(List<String> listPrimaryKey) {
        this.listPrimaryKey = listPrimaryKey;
    }

    public String getColumnSeq() {
        return columnSeq;
    }

    public void setColumnSeq(String columnSeq) {
        this.columnSeq = columnSeq;
    }

    public String getTypeDisplay() {
        return typeDisplay;
    }

    public void setTypeDisplay(String typeDisplay) {
        this.typeDisplay = typeDisplay;
    }

}
