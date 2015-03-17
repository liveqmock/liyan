package com.innofi.framework.dao;

import com.innofi.framework.dao.hibernate.IHibernateDao;
import com.innofi.framework.dao.impl.EntityOperation;
import com.innofi.framework.dao.impl.SQLOperation;
import com.innofi.framework.dao.jdbc.IJdbcDao;
import com.innofi.framework.dao.jdbc.sqldialect.DBDialect;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.exception.DAOException;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.reflect.ReflectionUtil;
import com.innofi.framework.utils.sql.SqlUtil;

import java.io.Serializable;
import java.util.*;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * BaseDao支持类，当需模块需要使用非系统默认数据源时，可继承此类，并覆盖getModuleFixDataSourceName方法
 * 返回在datasources.xml中配置的，当前模块期望使用的数据源名称，通过getBaseDao方法即可获得与数据源名称对应BaseDao实现类
 */
public abstract class DaoSupport<T extends BasePojo, PK extends Serializable> {

    /**
     * 实体对象Class类型
     */
    protected Class entityType = null;

    /**
     * 当前数据源名称
     */
    protected String dataSourceName;

    public DaoSupport(){
        Class clazz = ReflectionUtil.getSuperClassGenricType(getClass(), 0);
        if(!clazz.getName().equals("java.lang.Object")){
            entityType = clazz;
        }else {
            entityType = java.lang.Object.class;
        }
        dataSourceName = getDataSourceName();
    }

    /**
     * 获取当前DAO使用的数据源名称
     *
     * @return 当前使用的数据源名称
     */
    public String getDataSourceName() {
        return dataSourceName;
    }

    /**
     * 更换当前DAO使用的数据源
     *
     * @param dataSourceName 要更换的数据源名称
     */
    public void changeDataSource(String dataSourceName) {
    	ContextHolder.setCurrentDataSourceName(dataSourceName);
        this.dataSourceName = dataSourceName;
    }




    /**
     * 获取JdbcDao对象，通过该对象可使用jdbc相关API进行数据库操作
     *
     * @return IJdbcDao对象
     */
    public IJdbcDao getJdbcDao() {
        return ContextHolder.getJdbcDao(dataSourceName);
    }

    /**
     * 获取HibernateDao对象，通过该对象可使用hibernate相关API进行数据库操作
     *
     * @return IHibernateDao对象
     */
    public IHibernateDao getHibernateDao() {
        return ContextHolder.getHibernateDao(dataSourceName);
    }

    /**
     * 获取当前DAO持久化对象class
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Class<T> getEntityType() {
        return this.entityType;
    }



//-----------------------------------------------------未梳理逻辑--------------------------------------------------------
    /**
     * 保存动态对象
     *
     * @param clazz  实体对象类
     * @param entity 实体对象
     */
    public void saveDynamicPojo(Class clazz, T entity) {
        EntityOperation<?> op = new EntityOperation(clazz);
        SQLOperation sqlo = null;
        try {
            sqlo = op.insertEntity(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        getJdbcDao().update(sqlo.getSql(), sqlo.getParams());
    }


    /**
     * 更新动态对象
     *
     * @param clazz  实体对象类
     * @param entity 实体对象
     */
    public void updateDynamicPojo(Class clazz, T entity) {
        EntityOperation<?> op = new EntityOperation(clazz);
        try {
            SQLOperation sqlo = op.updateEntity(entity);
            getJdbcDao().update(sqlo.getSql(), sqlo.getParams());
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }


    /**
     * 执行查询sql语句返回实体对象列表
     *
     * @param entityClazz 实体对象类型
     * @param sqlId       查询sql语句 select * from table where column = ?
     * @param params      sql语句中实际参数
     * @param page        分页对象
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public void queryBeanForPage(Class<T> entityClazz, String moduleId, String sqlId, Map<String, String> params, Page<T> page) {
        String sql = getJdbcDao().getFormatSql(moduleId, sqlId, params);
        ConsoleUtil.info("dynamic statement sql is ["+sql+"]");
        queryBeanForPage(entityClazz, sql, page);
    }


    /**
     * 执行moduleId、sqlId对应sql返回唯一的实体对象
     *
     * @param entityClazz 实体对象类型
     * @param moduleId    模块唯一标识
     * @param sqlId       sql唯一标识
     * @param params      参数列表 paramName 对应sql中定义的#paramName# value实际参数值
     * @return 唯一的实体对象，当结果不唯一时抛出RuntimeException及错误信息
     */
    public T queryBeanForUnique(Class<T> entityClazz, String moduleId, String sqlId, Map<String, String> params) {
        List<T> beanList = queryBeanForList(entityClazz, moduleId, sqlId, params);
        if (beanList.size() > 1) {
            throw new RuntimeException("查询结果不唯一，请检查SQL的条件是否正确！");
        } else if (beanList.size() == 0) {
            return null;
        }
        return beanList.get(0);
    }

    /**
     * 执行查询sql语句返回实体对象列表
     *
     * @param entityClazz 实体对象类型
     * @param sql         查询sql语句 select * from table where column = ?
     * @param objects     sql语句中实际参数
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public List<T> queryBeanForList(Class<T> entityClazz, String sql, Object... objects) {
        Page page = new Page(Page.BIG_PAGE_SIZE, 1);
        queryBeanForPage(entityClazz, sql, page, objects);
        return page.getEntities();
    }

    /**
     * 执行moduleId、sqlId对应sql返回实体对象列表
     *
     * @param entityClazz 实体对象类型
     * @param moduleId    模块唯一标识
     * @param sqlId       sql唯一标识
     * @param params      参数列表 paramName 对应sql中定义的#paramName# value实际参数值
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public List<T> queryBeanForList(Class<T> entityClazz, String moduleId, String sqlId, Map<String, String> params) {
        Page page = new Page(Page.BIG_PAGE_SIZE, 1);
        queryBeanForPage(entityClazz, moduleId, sqlId, params, page);
        return page.getEntities();
    }

    /**
     * 执行查询sql语句返回实体对象列表
     *
     * @param entityClazz 实体对象类型
     * @param sql         查询sql语句 select * from table where column = ?
     * @param objects     sql语句中实际参数
     * @param page        分页对象
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public void queryBeanForPage(Class<T> entityClazz, String sql, Page<T> page, Object... objects) {
        DBDialect dbDialect = getJdbcDao().getDBDialect();
        String countSql = SqlUtil.getCountSql(sql);
        int totalCount = getJdbcDao().queryForObject(countSql, Integer.class,objects);
        String pageSql = dbDialect.getLimitSql(sql, page);
        List<Map<String, Object>> queryResult = getJdbcDao().queryForList(pageSql, objects);
        List<T> result = this.getHibernateDao().mapToBean(queryResult, entityClazz);
        page.setEntities(result);
        page.setEntityCount(totalCount);
    }


}
