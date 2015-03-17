package com.innofi.framework.dao.hibernate;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 12-6-11
 * @found time: 下午11:31
 * <p/>
 * 实体对象属性与数据库字段映射信息对象
 */
public class PropertyMapping {

    /**
     * 是否主键
     */
    private boolean isIdentifierProperty;

    /**
     * 属性名称
     */
    private String propertyName;

    /**
     * 字段名称
     */
    private String columnName;

    public String getPropertyName() {
        return propertyName;
    }

    public String getColumnName() {
        if (columnName == null) {
            return "";
        } else {
            return columnName.toLowerCase();
        }
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isIdentifierProperty() {
        return isIdentifierProperty;
    }

    public void setIdentifierProperty(boolean identifierProperty) {
        isIdentifierProperty = identifierProperty;
    }

}
