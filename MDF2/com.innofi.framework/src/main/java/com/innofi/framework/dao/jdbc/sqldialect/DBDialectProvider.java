package com.innofi.framework.dao.jdbc.sqldialect;

import java.sql.Connection;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 2012-06-10
 * @found time: 23:55:59
 * 数据库方言提供者接口
 */
public interface DBDialectProvider {

    /**
     * 数据库方言提供者实现类在Spring上下文中的beanId
     */
    public static final String SERVICE_ID = "mdf.dbDialectProvider";

    /**
     * 根据数据连接返回对应的方言对象，无支持的方言时抛出IllegalStateException异常
     *
     * @param connection 数据库连接
     * @return 支持该连接的数据库方言对象
     */
    public DBDialect provide(Connection connection);

}
