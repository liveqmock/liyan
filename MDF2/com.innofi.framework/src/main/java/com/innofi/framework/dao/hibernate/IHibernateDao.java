package com.innofi.framework.dao.hibernate;

import com.innofi.framework.dao.pagination.Page;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.mapping.PersistentClass;
import org.springframework.orm.hibernate4.HibernateOperations;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * Hibernate Dao数据库访问接口
 */
public interface IHibernateDao<T, ID extends Serializable> extends HibernateOperations {

    /**
     * 取得当前Session.
     */
    public Session getSession();

    /**
     * 取得Configuration.
     *
     * @return Configuration
     */
    public Configuration getConfiguration();

    /**
     * 取得对象的主键名.
     */
    public String getIdPropertyName(Class<T> clazz);

    /**
     * 获取全部对象.
     *
     * @param clazz 实体对象Class
     */
    public List<T> getAll(Class<T> clazz);

    /**
     * 按id列表获取对象.
     *
     * @param clazz 实体对象Class
     * @param ids   id列表
     * @return id列表中的所有对象
     */
    public List<T> findByIds(Class<T> clazz, List<ID> ids);

    /**
     * 按属性查找对象列表,支持多种匹配方式.
     *
     * @param clazz        实体对象clazz
     * @param propertyName 属性名称
     * @param value        属性值
     * @param matchType    匹配方式,目前支持的取值见PropertyFilter的MatcheType enum.
     */
    public List<T> findByProperty(Class<T> clazz, final String propertyName, final Object value, final String matchType);

    /**
     * 按属性查找对象列表,支持多种匹配方式.
     *
     * @param clazz        实体对象clazz
     * @param propertyName 属性名称
     * @param value        属性值
     * @param matchType    匹配方式,目前支持的取值见PropertyFilter的MatcheType enum.
     * @param orders       排序规则
     */
    public List<T> findByProperty(Class<T> clazz, final String propertyName, final Object value, final String matchType, List<PropertyOrder> orders);

    /**
     * 按Criteria查询对象列表.
     *
     * @param clazz      实体对象clazz
     * @param orders     PropertyOrder  propertyName 属性名称  orderRule 排序规则  {见com.innofi.framework.dao.hibernateimpl.QueryConstants} <br/>
     *                   该参数可选，如不输入或输入null则不进行排序
     * @param criterions 数量可变的Criterion.
     */
    public List<T> findByCriterions(Class<T> clazz, List<PropertyOrder> orders, Criterion... criterions);

    /**
     * 按Criteria分页查询.
     *
     * @param clazz      实体对象clazz
     * @param page       分页参数.
     * @param orders     PropertyOrder  propertyName 属性名称  orderRule 排序规则  {见com.innofi.framework.dao.hibernateimpl.QueryConstants} <br/>
     *                   该参数可选，如不输入或输入null则不进行排序
     * @param criterions 数量可变的Criterion.
     */
    @SuppressWarnings("unchecked")
    public void findPage_Criterion(Class<T> clazz, List<PropertyOrder> orders, Page<T> page, final Criterion... criterions);

    /**
     * 按属性过滤条件列表分页查找对象.
     *
     * @param filters PropertyFilter propertyName 属性名称 propertyValue 属性值 matchType 匹配条件 {见com.innofi.framework.dao.hibernateimpl.QueryConstants}
     * @param orders  PropertyOrder  propertyName 属性名称  orderRule 排序规则  {见com.innofi.framework.dao.hibernateimpl.QueryConstants}<br/>
     *                该参数可选，如不输入或输入null则不进行排序
     * @return 符合条件的记录
     */
    public List<T> findByFilters(Class<T> clazz, List<PropertyFilter> filters, List<PropertyOrder> orders);

    /**
     * 按属性过滤条件列表分页查找对象.
     *
     * @param filters PropertyFilter propertyName 属性名称 propertyValue 属性值 matchType 匹配条件 {见com.innofi.framework.dao.hibernateimpl.QueryConstants}
     * @param orders  PropertyOrder  propertyName 属性名称  orderRule 排序规则  {见com.innofi.framework.dao.hibernateimpl.QueryConstants}<br/>
     *                该参数可选，如不输入或输入null则不进行排序
     * @param page    分页参数
     * @return 符合条件的记录
     */
    public void findByPage_Filters(Class<T> clazz, final List<PropertyFilter> filters, List<PropertyOrder> orders, final Page page);

    /**
     * 按HQL查询对象列表.
     *
     * @param clazz  实体对象clazz
     * @param hql    hql 例:from UserInfo u where id = ?
     * @param values 命名参数,按名称绑定.
     */
    public List<T> find(Class<T> clazz, final String hql, Object... values);

    /**
     * 按HQL查询对象列表.
     *
     * @param clazz  实体对象clazz
     * @param hql    hql 例: from UserInfo u where dept.id = :deptId and username like :uname order by id
     * @param values 命名参数,按名称绑定.
     */
    public List<T> find(Class<T> clazz, final String hql, final Map<String, ?> values);

    /**
     * 按HQL查询唯一对象.
     *
     * @param clazz  实体对象clazz
     * @param hql    hql 例:from UserInfo u where id = ?
     * @param values 数量可变的查询参数,按顺序绑定.
     */
    public <X> X findUnique(Class<T> clazz, final String hql, final Object... values);

    /**
     * 按HQL查询唯一对象.
     *
     * @param clazz  实体对象clazz
     * @param hql    hql 例: from UserInfo u where dept.id = :deptId and username like :uname order by id
     * @param values 命名参数,按名称绑定.
     */
    public <X> X findUnique(Class<T> clazz, final String hql, final Map<String, ?> values);


    /**
     * 按HQL分页查询. 支持order by
     *
     * @param clazz  实体对象clazz
     * @param hql    hql 例:from UserInfo u where id = ?
     * @param values 数量可变的查询参数,按顺序绑定.
     * @return 符合条件的查询结果
     */
    public List<T> findByHql(Class<T> clazz, final String hql, final Object... values);


    /**
     * 按HQL分页查询.支持order by
     *
     * @param clazz  实体对象clazz
     * @param hql    hql 例: from UserInfo u where dept.id = :deptId and username like :uname order by id
     * @param values 命名参数,按名称绑定.
     */
    public List<T> findByNamedHql(Class<T> clazz, final String hql, final Map<String, ?> values);

    /**
     * 按HQL分页查询.
     *
     * @param clazz  实体对象clazz
     * @param hql    hql 例:from UserInfo u where id = ?
     * @param page   分页参数.不支持其中的orderBy参数
     * @param values 数量可变的查询参数,按顺序绑定.
     */
    @SuppressWarnings("unchecked")
    public void findByHql_Page(Class<T> clazz, final String hql, final Page page, final Object... values);

    /**
     * 按HQL分页查询.
     *
     * @param clazz  实体对象clazz
     * @param page   分页参数.不支持其中的orderBy参数.
     * @param hql    hql 例: from UserInfo u where dept.id = :deptId and username like :uname order by id
     * @param values 命名参数,按名称绑定.
     */
    @SuppressWarnings("unchecked")
    public void findByNamedHql_Page(Class<T> clazz, final String hql, final Page page, final Map<String, ?> values);

    /**
     * 根据对象获取总记录数
     *
     * @param clazz 对象Class
     * @return 总记录数
     */
    public long countByPropertyFilters(Class<T> clazz, List<PropertyFilter> filters);

    /**
     * 执行HQL进行批量修改/删除操作.
     *
     * @param hql    hql 例: update UserInfo u set u = ? where id = ?
     * @param values 参数
     * @return 影响记录条数
     */
    public int batchExecute(final String hql, final Object... values);

    /**
     * 执行HQL进行批量修改/删除操作.
     *
     * @param hql    hql 例: update UserInfo u set u = ? where id = ?
     * @param values 参数
     * @return 更新记录数.
     */
    public int batchExecute(final String hql, final Map<String, ?> values);

    /**
     * 根据查询HQL与参数列表创建Query对象.
     * <p/>
     * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
     *
     * @param values 数量可变的参数,按顺序绑定.
     */
    public Query createQuery(Class<T> clazz, final String queryString, final Object... values);


    /**
     * 根据文件路径获取Blob类型数据
     *
     * @param filePath 文件路径
     * @return Blob数据
     */
    public Blob createBlobByFilePath(String filePath);

    /**
     * 根据文件路径获取Blob类型数据
     *
     * @param inputStream 输入流
     * @return Blob数据
     */
    public Blob createBlobByInputStream(InputStream inputStream);

    /**
     * 根据文件路径获取Blob类型数据
     *
     * @param filePath 文件路径
     * @return Clob数据
     */
    public Clob createClobByFilePath(String filePath);


    /**
     * 根据查询HQL与参数列表创建Query对象.
     * <p/>
     * 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设置.
     * 留意可以连续设置,如下：
     * <p/>
     * <p/>
     * <pre>
     * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * <p/>
     * 调用方式如下：
     * <p/>
     * <p/>
     * <pre>
     *        dao.createQuery(hql,new Object[arg0,arg1,arg2])
     * </pre>
     *
     * @param clazz       实体对象clazz
     * @param queryString hibernateimpl hql
     * @param values      命名参数,按名称绑定.
     */
    public Query createQuery(Class<T> clazz, final String queryString, final Map<String, ?> values);

    /**
     * 根据Criterion条件创建Criteria.
     * <p/>
     * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
     *
     * @param clazz      实体对象clazz
     * @param criterions 数量可变的Criterion.
     */
    public Criteria createCriteria(Class<T> clazz, Criterion... criterions);

    /**
     * 按Criteria查询唯一对象.
     *
     * @param clazz      实体对象clazz
     * @param criterions 数量可变的Criterion.
     */
    public T findUnique(Class<T> clazz, final Criterion... criterions);



    /**
     * 将Map对象转换为clazz对象
     *
     * @param records List每个元素为一个Map
     * @param clazz   要转换的实体对象
     * @return 转换实体对象后的List
     */
    public List<T> mapToBean(List<Map<String, Object>> records, Class<T> clazz);

    /**
     * 执行count查询获得本次Criteria查询所能获得的对象总数.
     */
    @SuppressWarnings("unchecked")
    public long countCriteriaResult(final DetachedCriteria c);


    /**
     * 获取根据实体对象Class获取hibernate持久化对象
     *
     * @param clazz 实体对象class
     * @return hibernate持久化对象
     */
    public PersistentClass getPersistenceClass(Class clazz);

    /**
     * 获取实体的hibernate映射信息
     *
     * @param clazz 实体对象class
     * @return map中key为数据库字段名称 value为PropertyMapping对象
     */
    public HashMap<String, PropertyMapping> getEntityHbmMapping(Class clazz);

    /**
     * 获取实体的hibernate属性映射信息
     *
     * @param persistentClass hibernate持久化对象
     * @return map中key为数据库字段名称 value为PropertyMapping对象
     */
    public HashMap<String, PropertyMapping> getEntityHbmMapping(PersistentClass persistentClass);

    /**
     * 替换参数后的sql语句
     *
     * @param moduleId 模块唯一标识
     * @param hqlName  hql名称
     * @param params   hql语句中的实参
     * @return
     * @throws Exception
     */
    public String getFormatHql(String moduleId, String hqlName, Map<String, String> params);


}
