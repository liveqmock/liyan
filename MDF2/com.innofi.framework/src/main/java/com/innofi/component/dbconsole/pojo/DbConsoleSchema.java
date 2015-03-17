package com.innofi.component.dbconsole.pojo;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 12-7-1
 * @found time: 下午8:45
 * <p/>
 * 数据库模式对象
 */
public class DbConsoleSchema {

    private static final long serialVersionUID = -7505462205388149349L;

    private String connName;

    private String schemaName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getConnName() {
        return connName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setConnName(String connName) {
        this.connName = connName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

}
