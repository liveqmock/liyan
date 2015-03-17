package com.innofi.framework.datasource;

import javax.sql.DataSource;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 数据源注册器.
 * <p>
 * 此类用于向系统中的数据源仓库注册一个数据源.
 * </p>
 */
public class DataSourceRegister {

    public DataSourceRegister() {

    }

    private String name = null;
    private DataSource dataSource = null;
    private boolean asDefault = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isAsDefault() {
        return asDefault;
    }

    public void setAsDefault(boolean asDefault) {
        this.asDefault = asDefault;
    }

}
