package com.innofi.component.dbconsole.pojo;

import com.innofi.framework.pojo.BasePojo;

import java.util.List;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          <p/>
 *          工作组对象
 * @found date: 2012-5-20
 * @found time: 下午3:47:07
 */
public class DbConsoleGroup extends BasePojo {

    private static final long serialVersionUID = -6943272881875420849L;

    private String id;

    private String name;

    private List<DbConsoleConnection> dbConsoleConnections;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DbConsoleConnection> getDbConsoleConnections() {
        return dbConsoleConnections;
    }

    public void setDbConsoleConnections(List<DbConsoleConnection> dbConsoleConnections) {
        this.dbConsoleConnections = dbConsoleConnections;
    }


}
