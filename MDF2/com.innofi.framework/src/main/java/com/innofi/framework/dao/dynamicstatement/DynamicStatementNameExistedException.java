package com.innofi.framework.dao.dynamicstatement;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 1.0-SNAPSHOT
 * @author liumy2009@126.com
 * @found date: 2012-06-10
 * @found time: 23:55:59
 * <p/>
 * SQL重复定义异常
 */
public class DynamicStatementNameExistedException extends RuntimeException {

    private static final String MESSAGE = "sqlId定义重复";

    private Throwable cause = null;

    public DynamicStatementNameExistedException(String moduleId, String sqlId) {
        super("sqlId定义重复(" + moduleId + "模块下的" + sqlId + "已存在)");
    }

    public DynamicStatementNameExistedException(String moduleId, String sqlId, Throwable cause) {
        super("sqlId定义重复(" + moduleId + "模块下的" + sqlId + "已存在)");
        this.cause = cause;
    }

    public Throwable getCause() {
        return this.cause;
    }

    public String getMessage() {
        if (super.getMessage() != null)
            return super.getMessage();
        if (this.cause != null) {
            return this.cause.toString();
        }
        return null;
    }
}
