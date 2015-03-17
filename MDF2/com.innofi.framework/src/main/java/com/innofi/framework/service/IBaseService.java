package com.innofi.framework.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.context.ApplicationContext;

import com.bstek.dorado.web.DoradoContext;
import com.innofi.component.cache.ICacheService;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.component.sie.service.IWfProcessInterceptor;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.spring.context.FrameworkContext;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          业务服务层基类接口,提供查询，添加、修改、删除等
 * @found date: 2010-11-23
 * @found time: 20:40:56
 */
public interface IBaseService<T extends BasePojo, PK extends Serializable> extends ICacheService,IWfProcessInterceptor {

    /**
     * 获取当前DaoSupport对象
     *
     * @return DaoSupport对象
     */
    public abstract DaoSupport getDaoSupport();
	
    /**
     * 获取当前服务类对应的实体对象class
     *
     * @return 实体对象class
     */
    public Class getEntityType();

    /**
     * 根据指定属性名称，属性值判断对象是否存在
     *
     * @param propertyName  属性名称
     * @param propertyValue 属性值
     * @return true存在，false不存在
     */
    public boolean checkEntityExist(String propertyName, String propertyValue);

    /**
     * 根据对象列表获取对象ID字符串形式，多个ID以,分隔
     *
     * @param entityList   对象列表
     * @param propertyName 属性名称
     * @param separator    多个属性间的分隔符，默认用','分隔
     * @return '001','002','003'...
     */
    public String getPropertyValueString(Collection<T> entityList, String propertyName, String separator);

    /**
     * 获取对象属性值列表
     *
     * @param entityList 对象列表
     * @return List<Object> 对象属性值列表
     */
    public List<Object> getPropertyValueList(Collection<T> entityList, String propertyName);

    /**
     * 保存对象
     *
     * @param entity 具体实体对象
     */
    public void save(T entity);

    /**
     * 批量保存对象
     *
     * @param entityList 在批量执行完save后会执行session.flush()
     */
    public void saveForBranch(List<T> entityList);

    /**
     * 更新对象
     *
     * @param entity 具体实体对象
     */
    public void update(T entity);

    /**
     * 批量更新对象
     *
     * @param entityList 在批量执行完update后会执行session.flush()
     */
    public void updateForBranch(List<T> entityList);

    /**
     * 删除对象
     *
     * @param entity 具体实体对象
     */
    public void delete(T entity);

    /**
     * 按id删除对象.
     *
     * @param id 对象id
     */
    public void delete(final PK id);

    /**
     * 按id获取对象.
     *
     * @param id 对象id
     * @return id对应的实体对象
     */
    public T get(final PK id);

    /**
     * 加载查询结果集的动态属性
     *
     * @param entityList
     */
    public List<T> loadDynamicProperty(Collection<T> entityList);



    /**
     * 根据传入属性名称，属性值，匹配方式属性过滤器
     *
     * @param propertyName  属性名称
     * @param propertyValue 属性值
     * @param matchType     匹配类型
     * @return 属性过滤器列表
     */
    public List<PropertyFilter> buildPropertyFilter(String propertyName, Object propertyValue, String matchType);

    /**
     * 根据传入参数Map，创建等于属性过滤器
     *
     * @param parameter 前台传入的查询参数,key属性名,value属性值
     * @return 等于属性过滤器列表
     */
    public List<PropertyFilter> buildEqPropertyFilters(Map<String, Object> parameter);

    /**
     * 添加属性过滤器，
     *
     * @param propertyFilters 属性过滤器列表
     * @param propertyName    属性名称
     * @param propertyValue   属性值
     * @param matchType       属性类型
     * @param overwrite       是否重写，true覆盖第一次添加属性名相同的条件,fasle重复添加该属性条件执行and查询
     */
    public void addPropertyFilter(List<PropertyFilter> propertyFilters, String propertyName, Object propertyValue, String matchType, Boolean overwrite);

    /**
     * 添加OR关系属性过滤器，
     *
     * @param propertyFilters 属性过滤器列表
     * @param propertyName    属性名称
     * @param propertyValue   属性值
     * @param matchType       属性类型
     * @param overwrite       是否重写，true覆盖第一次添加属性名相同的条件,fasle重复添加该属性条件执行and查询
     */
    public void addOrPropertyFilter(List<PropertyFilter> propertyFilters, String propertyName, Object propertyValue, String matchType, Boolean overwrite);

    /**
     * 根据传入的propertyName从parameters参数Map中取得开始日期值，根据约定propertyName+"End从parameters参数Map中取得结束日期值<br/>
     * 按照"yyyy-MM-dd HH:mm:ss"对日期值进行格式化，格式化后值为：开始日期值+" 00:00:00"、结束日期值+" 23:59:59"<br/>
     * 如果propertyName取不到日期值,propertyName+End能够取到日期值，那么格式化后值为:结束日期值+" 00:00:00"、结束日期值+" 23:59:59"<br/>
     * 如果propertyName能够取到日期值,propertyName+End取不到日期值，那么格式化后值为:开始日期值+" 00:00:00"、开始日期值+" 23:59:59"<br/>
     * 如果propertyName、propertyName+"End"均取不到日期值，那么将不添加该属性过滤器
     * 该属性过滤的匹配方式为between
     *
     * @param propertyName 属性名称
     * @param parameters   参数Map
     */
    public void addDateRangePropertyFilter(List<PropertyFilter> propertyFilters, String propertyName, Map<String, Object> parameters);


    /**
     * 删除属性过滤器，
     *
     * @param propertyFilters 属性过滤器列表
     * @param propertyName    属性名称
     */
    public void removePropertyFilter(List<PropertyFilter> propertyFilters, String propertyName);


    /**
     * 按属性过滤条件删除对象.
     *
     * @param filters
     * @return 返回删除结果记录条数
     */
    public int deleteByPropertyFilters(List<PropertyFilter> filters);


    /**
     * 获取全部对象,支持排序,分页
     *
     * @param orders 排序规则器列表 ,此参数可选，如输入null则不进行排序
     */
    public List<T> getAll(List<PropertyOrder> orders);


    /**
     * 按属性查找对象列表,支持多种匹配方式.
     *
     * @param propertyName 属性名称
     * @param value        属性值
     * @param matchType    匹配方式,目前支持的取值见PropertyFilter的MatcheType enum,此参数为可选参数，如不输入执行相等比较.
     */
    public List<T> findByProperty(final String propertyName, final Object value, final String matchType);

    /**
     * 按属性查找对象列表,支持多种匹配方式.
     *
     * @param propertyName 属性名称
     * @param value        属性值
     * @param matchType    匹配方式,目前支持的取值见PropertyFilter的MatcheType enum.
     * @param orders       排序规则
     */
    public List<T> findByProperty(final String propertyName, final Object value, final String matchType, List<PropertyOrder> orders);


    /**
     * 按属性查找唯一对象,匹配方式为相等.
     *
     * @param propertyName 属性名称
     * @param value        属性值
     * @param matchType    匹配方式,目前支持的取值见PropertyFilter的MatcheType enum,此参数为可选参数，如不输入执行相等比较.
     * @return 返回唯一符合条件的结果，如存在多条抛出Runtime异常
     */
    public T findUniqueByProperty(final String propertyName, final Object value, final String matchType);

    /**
     * 按id列表获取对象.
     *
     * @param ids id列表
     * @return id列表中的所有对象
     */
    public List<T> findByIds(List<PK> ids);

    /**
     * 按属性过滤条件列表分页查找对象.
     *
     * @param filters PropertyFilter propertyName 属性名称 propertyValue 属性值 matchType 匹配条件 {见com.innofi.framework.dao.hibernateimpl.QueryConstants}
     * @param orders  PropertyOrder  propertyName 属性名称  orderRule 排序规则  {见com.innofi.framework.dao.hibernateimpl.QueryConstants}<br/>
     *                该参数可选，如不输入或输入null则不进行排序
     * @return 符合条件的记录
     */
    public List<T> findByPropertyFilters(List<PropertyFilter> filters, List<PropertyOrder> orders);

    /**
     * 按属性过滤条件列表分页查找对象.
     *
     * @param filters PropertyFilter propertyName 属性名称 propertyValue 属性值 matchType 匹配条件 {见com.innofi.framework.dao.hibernateimpl.QueryConstants}
     * @param orders  PropertyOrder  propertyName 属性名称  orderRule 排序规则  {见com.innofi.framework.dao.hibernateimpl.QueryConstants}<br/>
     *                该参数可选，如不输入或输入null则不进行排序
     * @param page    分页参数
     * @return 符合条件的记录
     */
    public void findByPage_Filters(final List<PropertyFilter> filters, List<PropertyOrder> orders, final Page page);

    /**
     * 按HQL进行查询.
     *
     * @param hql    hql 例:from UserInfo u where id = ?
     * @param values 数量可变的查询参数,按顺序绑定.
     */
    @SuppressWarnings("unchecked")
    public List<T> findByHql(String hql, Object... values);

    /**
     * 按HQL分页查询.
     *
     * @param hql    hql 例: from UserInfo u where dept.id = :deptId and username like :uname order by id
     * @param values 命名参数,按名称绑定.
     */
    @SuppressWarnings("unchecked")
    public List<T> findByNamedHql(final String hql, final Map<String, ?> values);


    /**
     * 按HQL分页查询.
     *
     * @param hql    hql 例:from UserInfo u where id = ?
     * @param page   分页参数.不支持其中的orderBy参数
     * @param values 数量可变的查询参数,按顺序绑定.
     */
    @SuppressWarnings("unchecked")
    public void findByHql_Page(final String hql, final Page page, final Object... values);

    /**
     * 按HQL分页查询.
     *
     * @param hql    hql 例: from UserInfo u where dept.id = :deptId and username like :uname order by id
     * @param page   分页参数.不支持其中的orderBy参数
     * @param values 命名参数,按名称绑定.
     */
    @SuppressWarnings("unchecked")
    public void findByNamedHql_Page(final String hql, final Page page, final Map<String, ?> values);

    /**
     * 按HQL查询唯一对象.
     *
     * @param hql    hql 例:from UserInfo u where id = ?
     * @param values 数量可变的查询参数,按顺序绑定.
     */
    public <X> X findUniqueByHql(final String hql, final Object... values);


    /**
     * 按HQL查询唯一对象.
     *
     * @param hql    hql 例: from UserInfo u where dept.id = :deptId and username like :uname order by id
     * @param values 命名参数,按名称绑定.
     */
    public <X> X findUniqueByNamedHql(final String hql, final Map<String, ?> values);

    /**
     * 执行查询sql语句返回唯一的实体对象
     *
     * @param sql     查询sql语句 select * from table where column = ?
     * @param objects sql语句中实际参数
     * @return 唯一的实体对象，当结果不唯一时抛出RuntimeException及错误信息
     */
    public <X> X queryBeanForUnique(String sql, Object... objects);

    /**
     * 执行moduleId、sqlId对应sql返回唯一的实体对象
     *
     * @param moduleId 模块唯一标识
     * @param sqlId    sql唯一标识
     * @param params   参数列表 paramName 对应sql中定义的#paramName# value实际参数值
     * @return 唯一的实体对象，当结果不唯一时抛出RuntimeException及错误信息
     */
    public <X> X dynamicQueryBeanForUnique(String moduleId, String sqlId, Map<String, String> params);

    /**
     * 执行查询sql语句返回实体对象列表
     *
     * @param sql     查询sql语句 select * from table where column = ?
     * @param objects sql语句中实际参数
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public List<T> queryBeanForList(String sql, Object... objects);
    
    /**
     * 执行moduleId、sqlId对应sql返回实体对象列表
     *
     * @param moduleId 模块唯一标识
     * @param sqlId    sql唯一标识
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public List<T> dynamicQueryBeanForList(String moduleId, String sqlId);

    /**
     * 执行moduleId、sqlId对应sql返回实体对象列表
     *
     * @param moduleId 模块唯一标识
     * @param sqlId    sql唯一标识
     * @param params   参数列表 paramName 对应sql中定义的#paramName# value实际参数值
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public List<T> dynamicQueryBeanForList(String moduleId, String sqlId, Map<String, String> params);
    
    /**
     * 执行查询sql语句返回实体对象列表
     *
     * @param sql     查询sql语句 select * from table where column = ?
     * @param objects sql语句中实际参数
     * @param page    分页对象
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public void queryBeanForPage(String sql, Page<T> page, Object... objects);

    /**
     * 执行moduleId、sqlId对应sql返回实体对象列表
     *
     * @param moduleId 模块唯一标识
     * @param sqlId    sql唯一标识
     * @param params   参数列表 paramName 对应sql中定义的#paramName# value实际参数值
     * @param page     分页对象
     */
    public void dynamicQueryBeanForPage(String moduleId, String sqlId, Map<String, String> params, Page<T> page);

    /**
     * 获取Id字段名称
     * @return
     */
    public String getIdPropertyName();

    /**
     * 获取Dorado上下文对象
     *
     * @return
     */
    public DoradoContext getDoradoContext();

    /**
     * 根据给出的beanId来获取在Spring当中配置的bean
     *
     * @param beanId 给出的beanId
     * @return 返回找到的bean对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getSpringBean(String beanId);

    /**
     * 取得ApplicationContext对象.
     * <p>
     * 本地模式返回ClassPathXmlApplicationContext对象 仅用于本地测试 无法使用ContextHold的全部功能
     * Web模式返回WebApplicationContext,可使用全部功能
     * 根据web.xml中对的配置顺序,在Spring启动完成后可用.
     * </p>
     *
     * @return 返回当前应用Spring的ApplicationContext对象
     */
    public ApplicationContext getSpringBeanFactory();

    /**
     * 获得框架上下文对象 FrameworkContext
     *
     * @return 当前线程对应的FrameworkContext
     */
    public FrameworkContext getContext();

    /**
     * 将一个key-value对放到当前线程Request中的Attribute当中
     *
     * @param key key值
     * @param obj 具体对象
     */
    public void setRequestAttribute(String key, Object obj);

    /**
     * 从当前线程中Request中取Attribute值
     *
     * @param key Attribute值对应的key
     * @return 返回与该key对应的值对象
     */
    public Object getRequestAttribute(String key);

    /**
     * 将一个对象放入缓存当中，同时如果缓存当中有存在相同key的对象，则进行覆盖
     *
     * @param key 对象的key
     * @param obj 具体对象
     */
    public void putCacheObject(String key, Object obj);

    /**
     * 将一个对象放入临时缓存当中，同时如果缓存当中有存在相同key的对象，则进行覆盖，<br>
     * 默认情况下，位于临时缓存中对象生命周期为1800秒，也就是半小时
     *
     * @param key 对象的key
     * @param obj 具体对象
     */
    public void putTemporaryCacheObject(String key, Object obj);

    /**
     * 获取key对应的缓存对象
     *
     * @param key 对象的key
     */
    public Object getCacheObject(String key);

    /**
     * 从临时缓存当中获取一个对象
     *
     * @param key 要移除的对象的key值
     */
    public Object getTemporaryCacheObject(String key);

    /**
     * 从缓存当中移除一个对象
     *
     * @param key 要移除的对象的key值
     */
    public void removeCacheObject(String key);

    /**
     * 从临时缓存当中移除一个对象
     *
     * @param key 要移除的对象的key值
     */
    public void removeTemporaryCacheObject(String key);

    /**
     * 按属性过滤条件列表获取对象总数.
     *
     * @param filters 可选参数，属性名称 propertyValue 属性值 matchType 匹配条件 {见com.innofi.framework.dao.hibernateimpl.QueryConstants}
     * @return 对象总数
     */
    public long countByPropertyFilters(List<PropertyFilter> filters);


    /**
     * 获取代码翻译类
     *
     * @return IdfCodeTransfer 代码翻译类
     */
    public IdfCodeTransfer getIdfCodeTransfer();

    /**
     * 检查修改删除数据权限
     *
     * @param entityClass   实体对象类全路径
     * @param idValues      记录编号列表
     * @param operationType 操作类型 2:修改 3:删除
     * @return 返回无权限id列表
     */
    public List<String> checkUpdDelPermission(String entityClass, List<String> idValues, String operationType);

    ///////////////////////////////////MAP///////////////////////////////////////////////////////////////////////////////////////

    /**
     * 执行moduleId、sqlId对应sql返回Map结果
     *
     * @param moduleId 模块唯一标识
     * @param sqlId    sql唯一标识
     * @param params   参数列表 paramName 对应sql中定义的#paramName# value实际参数值
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public Map<String, Object> dynamicQueryMapForUnique(String moduleId, String sqlId, Map<String, String> params);

    /**
     * 执行moduleId、sqlId对应sql返回Map列表
     *
     * @param moduleId 模块唯一标识
     * @param sqlId    sql唯一标识
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public List<Map<String, Object>> dynamicQueryMapForList(String moduleId, String sqlId);

    /**
     * 执行moduleId、sqlId对应sql返回Map列表
     *
     * @param moduleId 模块唯一标识
     * @param sqlId    sql唯一标识
     * @param params   参数列表 paramName 对应sql中定义的#paramName# value实际参数值
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public List<Map<String, Object>> dynamicQueryMapForList(String moduleId, String sqlId, Map<String, String> params);


    /**
     * 此方法支持分页，执行moduleId、sqlId对应sql返回Map列表
     *
     * @param querySql 查询sql
     * @param page     分页对象
     * @return 返回结果Page对象，每个元素为一个Map key 字段名称大写 字段对应类型对象
     */
    public void queryMapForList(String querySql, Page<Map<String, Object>> page);

    /**
     * 此方法支持分页，执行moduleId、sqlId对应sql返回Map列表
     *
     * @param moduleId 模块唯一标识
     * @param sqlId    sql唯一标识
     * @param params   参数列表 paramName 对应sql中定义的#paramName# value实际参数值
     * @param page     分页对象
     */
    public void dynamicQueryMapForList(String moduleId, String sqlId, Map<String, String> params, Page<Map<String, Object>> page);


    /**
     * 根据查询分页和DetachedCriteria接口查询对象
     * @param page  分页对象
     * @param criteria DetachedCriteria对象
     */
    public void findByPageAndCriteria(Page page , DetachedCriteria criteria);
    
    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#changeDaoSupportDataSource(String)
     */
    public void changeDaoSupportDataSource(String dataSourceName);

    /**
     * 查询两个版本信息
     *
     * @param versionIds 版本编号
     * @return key propertyLabel 属性显示名称 ，version1Value版本1值 , version2Value值
     */
    //public List<Map<String, Object>> queryCompareRecordInfo(String versionIds);


    /**
     * 根据类获取元数据Table对象
     *
     * @return
     */
    // public MdTable getMdTable(Class clazz);


    /**
     * 查询历史记录
     *
     * @param page      分页信息
     * @param objectIds 对象id映射 key id属性名称 value id值
     */
    //public void queryHistoryRecord(Page<Map<String, Object>> page, Map<String, String> objectIds);


}
