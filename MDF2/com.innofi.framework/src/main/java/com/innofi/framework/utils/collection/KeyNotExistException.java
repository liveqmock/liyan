package com.innofi.framework.utils.collection;

/**
 * 无效键值异常
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-11-23
 * found time: 20:40:56
 */
public class KeyNotExistException extends RuntimeException {
    public KeyNotExistException(Object key) {
        super("Key \"" + key + "\" does not exist!");
    }
}