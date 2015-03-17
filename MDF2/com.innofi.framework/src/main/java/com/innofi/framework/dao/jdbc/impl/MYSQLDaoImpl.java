package com.innofi.framework.dao.jdbc.impl;


import com.innofi.framework.dao.jdbc.AbstractJdbcDao;
import com.innofi.framework.exception.FrameworkJdbcRuntimeException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司 all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 2011-11-23
 * @found time: 20:40:56
 * 提供MYSQL数据库数据访问执行实现
 */
public class MYSQLDaoImpl extends AbstractJdbcDao {


    public MYSQLDaoImpl(DataSource dataSource) throws SQLException {
        super(dataSource);
    }

    /**
     * 判断指定表是否存在
     *
     * @param schema    模式  可选参数
     * @param tableName 指定表名
     * @return 存在则返回true；不存在则返回false
     * @throws com.innofi.framework.exception.FrameworkJdbcRuntimeException
     *          框架Jdbc运行时异常
     */
    
    public boolean ifTableExist(String schema, String tableName) throws FrameworkJdbcRuntimeException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 判断满足条件的索引是否存在
     *
     * @param schema    模式  可选参数
     * @param tableName 指定表名
     * @param cols      指定表上的索引字段
     * @return 存在则返回true；不存在则返回false
     * @throws com.innofi.framework.exception.FrameworkJdbcRuntimeException
     *          框架Jdbc运行时异常
     */
    
    public boolean ifIndexExist(String schema, String tableName, String... cols) throws FrameworkJdbcRuntimeException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 判断指定名称的索引是否存在
     *
     * @param schema    模式  可选参数
     * @param indexName 索引名称
     * @return 存在则返回true；不存在则返回false
     * @throws com.innofi.framework.exception.FrameworkJdbcRuntimeException
     *          框架Jdbc运行时异常
     */
    
    public boolean ifIndexExist(String schema, String indexName) throws FrameworkJdbcRuntimeException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 获取数据表字段名,按字段名进行排序
     *
     * @param schema 模式  可选参数
     * @param table  指定表名
     * @return 表中字段名数组
     * @throws com.innofi.framework.exception.FrameworkJdbcRuntimeException
     *
     */
    
    public List<String> getOrderColumnsByTable(String schema, String table) throws FrameworkJdbcRuntimeException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 获取数据表字段名，此方法不进行排序
     *
     * @param schema 模式 可选参数
     * @param table  指定表名
     * @return 表中字段名数组
     * @throws com.innofi.framework.exception.FrameworkJdbcRuntimeException
     *
     */
    
    public List<String> getColumnsByTable(String schema, String table) throws FrameworkJdbcRuntimeException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 删除指定索引
     *
     * @param schema 模式 可选参数
     * @param index  指定索引名
     * @throws com.innofi.framework.exception.FrameworkJdbcRuntimeException
     *
     */
    
    public boolean dropIndex(String schema, String index) throws FrameworkJdbcRuntimeException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }


    /**
     * 获取字符串类型，数据库定义
     *
     * @param length 长度
     * @return 返回string类型当前数据库对应定义字符串
     */
    
    public String getDefStringType(int length) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 获得日期类型，数据库定义
     *
     * @return 返回Date类型当前数据库对应定义字符串
     */
    
    public String getDefDateType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 获得数字类型，数据库定义
     *
     * @return 返回Nuber类型当前数据库对应定义字符串
     */
    
    public String getDefNumberType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	public boolean ifTableColumnExist(String schema, String tableName,
			String columnName) throws FrameworkJdbcRuntimeException {
		// TODO Auto-generated method stub
		return false;
	}
}
