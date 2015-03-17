package com.innofi.component.dbconsole.pojo;

import com.innofi.framework.pojo.BasePojo;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          <p/>
 *          数据库信息模板对象
 * @found date: 2012-5-20
 * @found time: 下午3:47:07
 */
public class DbConsoleTemplate extends BasePojo {

    private static final long serialVersionUID = -4230045303473727187L;

    private String type;
    private String driver;
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
