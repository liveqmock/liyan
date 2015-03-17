package com.innofi.framework.dao.jdbc.sqldialect.impl;

import com.innofi.framework.dao.jdbc.sqldialect.DBDialect;
import com.innofi.framework.dao.jdbc.sqldialect.DBDialectProvider;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 2012-06-10
 * @found time: 23:55:59
 * 数据库方言提供者实现类
 */
public class MultiDBDialectProvider implements DBDialectProvider {

    private static final Logger log = LoggerFactory.getLogger(MultiDBDialectProvider.class);

    private List<DBDialect> dbDialects = null;

    /*
    * (non-Javadoc)
    *
    * @see com.innofi.framework.dao.jdbc.sqldialect.DBDialectProvider#provide(java.sql.Connection)
    */
    public DBDialect provide(Connection connection) {
        for (DBDialect dbDialect : this.dbDialects) {
            if (dbDialect.support(connection)) {
                return dbDialect;
            }
        }
        String connectionDesc = "UNKNOWN";
        try {
            connectionDesc = this.getConnectionDesc(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("未能获取数据库连接描述信息", e);
        }
        throw new IllegalStateException("未找到到指定数据库连接的方言." + connectionDesc);
    }

    protected String getConnectionDesc(Connection connection) throws SQLException {
        DatabaseMetaData md = connection.getMetaData();
        StringBuilder sb = new StringBuilder();
        sb.append("{databaseProductName : " + md.getDatabaseProductName() + ",");
        sb.append("databaseProductVersion : " + md.getDatabaseProductVersion()
                + ",");
        sb.append("databaseMajorVersion : " + md.getDatabaseMajorVersion()
                + ",");
        sb.append("databaseMinorVersion : " + md.getDatabaseMinorVersion()
                + "}");
        return sb.toString();
    }


    public List<DBDialect> getDbDialects() {
        return dbDialects;
    }

    public void setDbDialects(List<DBDialect> dbDialects) {
        this.dbDialects = dbDialects;
    }

}
