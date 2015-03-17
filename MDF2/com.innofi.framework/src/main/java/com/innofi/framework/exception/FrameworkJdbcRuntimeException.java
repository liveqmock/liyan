package com.innofi.framework.exception;

/**
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-11-22
 * found time: 22:37:16
 * 框架 JdbcDao 运行时异常基类
 */
public class FrameworkJdbcRuntimeException extends FrameworkRuntimeException {

    public FrameworkJdbcRuntimeException() {
        super();
    }

    public FrameworkJdbcRuntimeException(String message) {
        super(message);
    }

    public FrameworkJdbcRuntimeException(Throwable t) {
        super(t);
    }

}
