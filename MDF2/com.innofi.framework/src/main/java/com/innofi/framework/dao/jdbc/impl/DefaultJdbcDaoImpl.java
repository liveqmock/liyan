package com.innofi.framework.dao.jdbc.impl;

import com.innofi.framework.dao.jdbc.AbstractJdbcDao;
import com.innofi.framework.dao.jdbc.IJdbcDao;
import com.innofi.framework.exception.FrameworkJdbcRuntimeException;
import com.innofi.component.dbconsole.pojo.DbConsoleTable;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司 all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0 扩展JdbcTemplate的功能
 *          <p/>
 *          扩展功能包括分页查询,按属性过滤条件列表查询. 可在Service层直接使用,.
 * @found date: 2012-11-23
 * @found time: 20:40:56
 */
public class DefaultJdbcDaoImpl extends AbstractJdbcDao {

    protected final String implPackage = "com.innofi.framework.dao.jdbc.impl";

    protected final String implSuffix = "DaoImpl";

    protected IJdbcDao daoImpl;


    public DefaultJdbcDaoImpl(DataSource dataSource){
        super(dataSource);
        //daoImpl = getDaoImpl();
    }

    /**
     * 加载指定模式下，指定表名视图及数据表
     *
     * @param schema 可为null，null加载所有模式
     * @param table  可为null，null加载所有，非null like查询
     * @return 符合条件的视图及表
     */
    @Override
    public List<DbConsoleTable> loadTableList(String schema, String table, String types, boolean lazy) {
        return daoImpl.loadTableList(schema, table, types, lazy);
    }

    /**
     * 判断指定表是否存在
     *
     * @param schema    主题
     * @param tableName 指定表名
     * @return 存在则返回true；不存在则返回false
     * @throws com.innofi.framework.exception.FrameworkJdbcRuntimeException
     *          框架Jdbc运行时异常
     */
    public boolean ifTableExist(String schema, String tableName) throws FrameworkJdbcRuntimeException {
        return daoImpl.ifTableExist(schema, tableName);
    }

    /**
     * 判断满足条件的索引是否存在
     *
     * @param schema    主题
     * @param tableName 指定表名
     * @param cols      指定表上的索引字段
     * @return 存在则返回true；不存在则返回false
     * @throws FrameworkJdbcRuntimeException 框架Jdbc运行时异常
     */
    public boolean ifIndexExist(String schema, String tableName, String... cols) throws FrameworkJdbcRuntimeException {
        return daoImpl.ifIndexExist(schema, tableName, cols);
    }

    /**
     * 判断指定名称的索引是否存在
     *
     * @param schema    主题
     * @param indexName 索引名称
     * @return 存在则返回true；不存在则返回false
     * @throws FrameworkJdbcRuntimeException 框架Jdbc运行时异常
     */
    public boolean ifIndexExist(String schema, String indexName) throws FrameworkJdbcRuntimeException {
        return daoImpl.ifIndexExist(schema, indexName);
    }

    /**
     * 获取数据表字段名,按字段名进行排序
     *
     * @param schema 主题
     * @param table  指定表名
     * @return 表中字段名数组
     * @throws FrameworkJdbcRuntimeException
     */
    public List<String> getOrderColumnsByTable(String schema, String table) throws FrameworkJdbcRuntimeException {
        return daoImpl.getOrderColumnsByTable(schema, table);
    }

    /**
     * 获取数据表字段名，此方法不进行排序
     *
     * @param schema 主题
     * @param table  指定表名
     * @return 表中字段名数组
     * @throws FrameworkJdbcRuntimeException
     */
    public List<String> getColumnsByTable(String schema, String table) throws FrameworkJdbcRuntimeException {
        return daoImpl.getColumnsByTable(schema, table);
    }

    /**
     * 删除指定索引
     *
     * @param schema 主题
     * @param index  指定索引名
     * @throws FrameworkJdbcRuntimeException
     */
    public boolean dropIndex(String schema, String index) throws FrameworkJdbcRuntimeException {
        return daoImpl.dropIndex(schema, index);
    }

    /**
     * 删除指定表
     *
     * @param schema 主题
     * @param table  指定表名
     * @throws FrameworkJdbcRuntimeException
     */
    public boolean dropTable(String schema, String table) throws FrameworkJdbcRuntimeException {
        IJdbcDao dao = getDaoImpl();
        return dao.dropTable(schema, table);
    }

    /**
     * 获取字符串类型，数据库定义
     *
     * @param length 长度
     * @return
     */
    public String getDefStringType(int length) {
        return daoImpl.getDefStringType(length);
    }

    /**
     * 获得日期类型，数据库定义
     *
     * @return
     */
    public String getDefDateType() {
        return daoImpl.getDefDateType();
    }

    /**
     * 获得数字类型，数据库定义
     *
     * @return
     */
    public String getDefNumberType() {
        return daoImpl.getDefNumberType();
    }

    /*
     * 根据当前数据库类型获取JdbcDao对应的实现类
     * @return JdbcDao对应的实现类
     */
    private IJdbcDao getDaoImpl() {
        Class clazz = null;
        IJdbcDao jdbcDao = null;
        try {
            String fqClassName = implPackage + "." + super.getCurrentDbType().toUpperCase() + implSuffix;
            clazz = Class.forName(fqClassName);
            Constructor constructor = clazz.getConstructor(new Class[]{DataSource.class});
            jdbcDao = (IJdbcDao) constructor.newInstance(super.getDataSource());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkJdbcRuntimeException(e);
        }
        return jdbcDao;
    }

    @Override
    public boolean ifTableColumnExist(String schema, String tableName, String columnName) throws FrameworkJdbcRuntimeException {
        return daoImpl.ifTableColumnExist(schema, tableName, columnName);
    }

}