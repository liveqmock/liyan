package com.innofi.framework.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.web.DoradoContext;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.spring.context.FrameworkContext;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          Action基类接口
 * @found date: 2010-11-23
 * @found time: 20:40:56
 */
public interface IBaseAction {

    /**
     * 获取Dorado上下文对象
     *
     * @return
     */
    public DoradoContext getDoradoContext();

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
     * @return 等于数属性器列表
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
     * 添加机构in条件
     *
     * @param propertyFilters 属性过滤器列表
     * @param propertyName    机构属性名称
     * @param propertyValue   机构编码
     */
    public void addPropertyFilterOfOrgIn(List<PropertyFilter> propertyFilters, String propertyName, Object propertyValue);

    /**
     * 返回机构过滤条件字符串
     *
     * @param filterBuffer 过滤条件buffer
     * @param fieldName    机构属性名称
     * @param orgCode      机构编码
     */
    public void buildSqlOrHqlFilterOfOrgIn(StringBuffer filterBuffer, String fieldName, String orgCode);


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
     * 根据Dorado的Page对象转换为com.innofi.framework.model.dao.pagination.Page对象
     *
     * @param page dorado配置对象
     * @return 转换后的com.innofi.framework.model.dao.pagination.Page对象
     */
    public com.innofi.framework.dao.pagination.Page translateDoradoPageToInnofiPage(Page page);

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
     * 获取请求对象HttpServletRequest
     *
     * @return 当前线程对应的HttpServletRequest
     */
    public HttpServletRequest getRequest();

    /**
     * 获得框架上下文对象 FrameworkContext
     *
     * @return 当前线程对应的FrameworkContext
     */
    public FrameworkContext getContext();

    /**
     * 得到当前线程中的HttpServletResponse对象
     *
     * @return 返回当前线程的HttpServletResponse对象
     */
    public HttpServletResponse getResponse();

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
     * 从当前线程中Request中取parameter值
     *
     * @param key parameter的key
     * @return 与key对象的字符串值
     */
    public String getRequestParameter(String key);

    /**
     * 检查修改删除数据权限
     *
     * @param entityClass   实体对象类全路径
     * @param idValues      记录编号列表
     * @param operationType 操作类型 2:修改 3:删除
     */
    public List<String> checkUpdDelPermission(String entityClass, List<String> idValues, String operationType);

    /**
     * 获取查询方案
     * @param parameters 查询参数
     *                   key viewUrl 视图路径
     *                   key userId  当前用户编号
     */
    public List<Record> getQuerySchemas(Map parameters)throws Exception;

    /**
     * 保存查询方案公共方法
     * @param querySchemas  查询方案数据
     * @param parameters    参数
     */
    public void saveQuerySchemas(List<Record> querySchemas,Map<String,String> parameters);

    /**
     * 保存用户自定义表格显示列信息
     * @param parameters
     */
    public void saveDataGridDisplayColumn(Map<String,String> parameters);

}
