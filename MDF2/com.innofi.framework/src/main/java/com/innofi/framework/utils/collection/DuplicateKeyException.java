package com.innofi.framework.utils.collection;

/**
 * 键值重复异常
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-11-23
 * found time: 20:40:56
 */
public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException(Object key) {
        super("Can not add a object with the duplicate key \"" + key + "\"!");
    }
}