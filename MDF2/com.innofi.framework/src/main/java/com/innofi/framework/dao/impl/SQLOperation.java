/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.dao.impl;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          接口说明
 * @found date: 14-8-31
 * @found time: 下午1:42
 */
public class SQLOperation {

    final String sql;
    final Object[] params;

    public SQLOperation(String sql, Object... params) {
        this.sql = sql;
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public Object[] getParams() {
        return params;
    }

}