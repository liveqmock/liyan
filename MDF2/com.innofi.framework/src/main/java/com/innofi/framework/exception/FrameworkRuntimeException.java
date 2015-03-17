package com.innofi.framework.exception;

/**
 * 框架运行时异常基类
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-11-22
 * found time: 22:37:16
 */
public class FrameworkRuntimeException extends RuntimeException {

    public FrameworkRuntimeException() {

    }

    public FrameworkRuntimeException(String message) {
        super(message);
    }


    public FrameworkRuntimeException(Throwable t) {
        super(t);
    }

}