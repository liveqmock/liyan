package com.innofi.framework.dao.jdbc.sqldialect.impl;

import org.hibernate.dialect.InformixDialect;

import java.sql.Types;


/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 2012-06-10
 * @found time: 23:55:59
 * 为Informix提供hibernate支持
 */
public class HiInformixMapDialect extends InformixDialect {
    public HiInformixMapDialect() {
        super();
        registerColumnType(Types.BLOB, "BYTE");
        registerColumnType(Types.CLOB, "TEXT");
    }
}
