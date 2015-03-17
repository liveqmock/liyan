package com.innofi.framework.exception;

/**
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-11-22
 * found time: 22:37:16
 * 框架 HibernateDao 运行时异常基类
 */
public class FrameworkHibernateDaoRuntimeException extends FrameworkRuntimeException {

    public FrameworkHibernateDaoRuntimeException() {
        super();
    }

    public FrameworkHibernateDaoRuntimeException(String message) {
        super(message);
    }

    public FrameworkHibernateDaoRuntimeException(Throwable t) {
        super(t);
    }

}
