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
 * sql包装类
 */
public class SqlWrapper {

    public SqlWrapper(String sql, Object[] args) {
        super();
        this.sql = sql;
        this.args = args;
    }

    private String sql;

    private Object[] args;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
