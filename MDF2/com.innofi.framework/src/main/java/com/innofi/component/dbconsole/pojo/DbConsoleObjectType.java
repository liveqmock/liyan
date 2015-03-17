package com.innofi.component.dbconsole.pojo;

import com.innofi.framework.pojo.BasePojo;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 12-7-12
 * @found time: 下午11:27
 * <p/>
 * 数据库对象类型
 */
public class DbConsoleObjectType extends BasePojo {

    private static final long serialVersionUID = -2732085450376580467L;

    protected String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
