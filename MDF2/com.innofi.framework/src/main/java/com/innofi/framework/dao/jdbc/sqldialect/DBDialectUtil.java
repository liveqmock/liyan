package com.innofi.framework.dao.jdbc.sqldialect;

import com.innofi.framework.spring.context.ContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 2012-06-10
 * @found time: 23:55:59
 * 获得数据库方言工具类
 */
public final class DBDialectUtil {

    /**
     * 根据数据连接返回对应的方言对象，无支持的方言时抛出IllegalStateException异常
     *
     * @param connection 数据库连接
     * @return 支持该连接的数据库方言对象
     */
    public static final DBDialect getDBDialect(Connection connection) throws SQLException {
        DBDialectProvider dbDialectProvider = ContextHolder.getSpringBean(DBDialectProvider.SERVICE_ID);
        return dbDialectProvider.provide(connection);
    }

    /**
     * 根据数据连接返回对应的方言对象，无支持的方言时抛出IllegalStateException异常
     *
     * @param dataSource 数据源
     * @return 支持该数据源的数据库方言对象
     */
    public static final DBDialect getDBDialect(DataSource dataSource) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        DBDialect dbDialect = jdbcTemplate.execute(new ConnectionCallback<DBDialect>() {
            public DBDialect doInConnection(Connection connection)
                    throws SQLException, DataAccessException {
                return DBDialectUtil.getDBDialect(connection);
            }
        });
        return dbDialect;
    }

}
