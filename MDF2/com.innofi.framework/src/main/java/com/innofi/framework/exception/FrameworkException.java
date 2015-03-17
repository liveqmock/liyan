package com.innofi.framework.exception;

/**
 * 框架异常的基类
 * Copyright (c) 2010 正信嘉华管理顾问有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-11-22
 * found time: 22:37:16
 */
public class FrameworkException extends Exception {

    public FrameworkException() {
    }

    public FrameworkException(String message) {
        super(message);
    }

    public FrameworkException(Throwable t) {
        super(t);
    }
}
