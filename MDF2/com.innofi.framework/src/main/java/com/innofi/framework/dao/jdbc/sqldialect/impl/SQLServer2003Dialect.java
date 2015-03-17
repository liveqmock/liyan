package com.innofi.framework.dao.jdbc.sqldialect.impl;

import java.sql.Connection;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 2012-06-10
 * @found time: 23:55:59
 * SQLServer2003数据库实现方言类
 */
public class SQLServer2003Dialect extends SQLServer2005Dialect {


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialect#support(java.sql.Connection)
     */
    public boolean support(Connection connection) {
        return support(connection, "sql server", "8");
    }


}


