package com.innofi.framework.spring.context;

import com.innofi.framework.dao.hibernate.HibernateSessionFactoryBean;
import com.innofi.framework.dao.hibernate.IHibernateDao;
import com.innofi.framework.dao.hibernate.impl.HibernateDaoImpl;
import com.innofi.framework.dao.jdbc.IJdbcDao;
import com.innofi.framework.dao.jdbc.impl.DefaultJdbcDaoImpl;
import com.innofi.framework.datasource.DataSourceRepository;
import com.innofi.framework.properties.PropertiesConfigurationLoader;
import com.innofi.framework.properties.SystemProperties;
import com.innofi.framework.utils.validate.Validator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.util.*;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * IDF框架上下文环境持有者.<br/>
 * 此类用于:<br/>
 * <ul>
 * <li>获取或设置当前用户会话信息{@link FrameworkContext}对象的辅助类.</li>
 * <li>获取当前线程的Request、Response以及特殊的模块id（MID）</li>
 * <li>由于其被配置到Spring中(idf_framework.xml),且实现了{@link ApplicationContextAware}接口</p>,
 * 因此其内部的唯一实例保存了ApplicationContext,可以据此感知Spring.</p>
 * 详情请参见{@link #getSpringBeanFactory}和{@link #getSpringBean}两个函数.</li>
 * </ul>
 */
public class ContextHolder implements ApplicationContextAware {

    /*
     * 保存FrameworkContext的线程局部变量.
     */
    private static final ThreadLocal<FrameworkContext> frameworkContextThreadLocal = new ThreadLocal<FrameworkContext>();
    /*
     * 框架特有的模块ID值,该值仅生命周期与当前Request相同
     */
    private static final ThreadLocal<String> midThreadLocal = new ThreadLocal<String>();
    /*
     * 保存HttpServletResponse的线程局部变量.
     */
    private static final ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<HttpServletResponse>();
    /*
     * 保存HttpServletRequest的线程局部变量.
     */
    private static final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
    /*
     * 用于在当前线程中存放当前线程操作采用的数据源名称
     */
    private static final ThreadLocal<String> currentDataSourceNameThreadLocal = new ThreadLocal<String>();
    /*
     * 保存数据源名称与JdbcDao实现类映射 key 数据源名称 value jdbcDao实现类
     */
    private static Map<String, IJdbcDao> jdbcDaoMap = new HashMap<String, IJdbcDao>();
    /*
     * 保存数据源名称与hibernateDao实现类映射 key 数据源名称 value hibernateDao实现类
     */
    private static Map<String, IHibernateDao> hibernateDaoMap = new HashMap<String, IHibernateDao>();
    /*
     * properties文件加载器
     */
    private static PropertiesConfigurationLoader propertiesConfiguration;
    /*
     * 上下文对象
     */
    private static ApplicationContext applicationContext = null;
    /*
     * 框架临时文件存放目录
     */
    private static String IDF_TEMP_DIR;
    /*
     * WebRoot路径
     */
    private static String webRootPath;

    /*
     * 数据源资源库
     */
    private static DataSourceRepository dataSourceRepository;


     /*
      * (non-Javadoc)
      *
      * @see
      * org.springframework.context.ApplicationContextAware#setApplicationContext
      * (org.springframework.context.ApplicationContext)
      */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        propertiesConfiguration = applicationContext.getBean(PropertiesConfigurationLoader.class);
        dataSourceRepository = applicationContext.getBean(DataSourceRepository.class);
        Properties systemProperties = System.getProperties();
        this.applicationContext = applicationContext;
        String userHomeDir = (String) systemProperties.get("user.home");//当前用户home路径
        if (Validator.isNotNull(userHomeDir)) {
            IDF_TEMP_DIR = userHomeDir + File.separator + "idf_tempdir";//缓存临时文件夹
        }
        File f = new File(IDF_TEMP_DIR);
        if (!f.exists()) {
            f.mkdirs();
        }

        for (Map.Entry<String, DataSource> entry : dataSourceRepository.getDataSources().entrySet()) {
            String dataSourceName = entry.getKey();
            DataSource dataSource = entry.getValue();
            IJdbcDao jdbcDaoImpl = new DefaultJdbcDaoImpl(dataSource);
            jdbcDaoMap.put(dataSourceName,jdbcDaoImpl);
        }

        Collection<HibernateSessionFactoryBean> sessionFactoryBeanList = applicationContext.getBeansOfType(HibernateSessionFactoryBean.class).values();

        for (HibernateSessionFactoryBean bean : sessionFactoryBeanList) {
            String dataSourceName = bean.getDataSourceName();
            IHibernateDao hibernateDaoImpl = new HibernateDaoImpl(bean.getObject(),bean.getConfiguration());
            hibernateDaoMap.put(dataSourceName,hibernateDaoImpl);
        }
    }


    /**
     * 获取jdbcDao实例
     * @param dataSourceName 数据源名称
     * @return 数据源对应的JdbcDao对象
     */
    public static IJdbcDao getJdbcDao(String dataSourceName){
        if(Validator.isNotNull(dataSourceName)){
            return jdbcDaoMap.get(dataSourceName);
        }
        dataSourceName = dataSourceRepository.getRealDataSourceName();
        Assert.notNull(dataSourceName,"默认数据源不能为null,请指定默认数据源!");
        return jdbcDaoMap.get(dataSourceName);
    }

    /**
     * 获取jdbcDao实例
     * @param dataSourceName 数据源名称
     * @return 数据源对应的HibernateDao对象
     */
    public static IHibernateDao getHibernateDao(String dataSourceName){
        if(Validator.isNotNull(dataSourceName)){
            return hibernateDaoMap.get(dataSourceName);
        }
        dataSourceName = dataSourceRepository.getRealDataSourceName();
        return hibernateDaoMap.get(dataSourceName);
    }


    public static String getIdfTempFileStorePath() {
        return IDF_TEMP_DIR;
    }

    /**
     * 获取当前用户语言环境
     *
     * @return 当前用户语言环境
     */
    public static Locale getLocale() {
        return Locale.CHINA;
    }

    /**
     * 根据给出的beanId来获取在Spring当中配置的bean
     *
     * @param beanId 给出的beanId
     * @return 返回找到的bean对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSpringBean(String beanId) {
        return (T) getSpringBeanFactory().getBean(beanId);
    }

    /**
     * 根据给出的beanId和对象clazz来获取在Spring当中配置的bean
     *
     * @param beanId 给出的beanId
     * @param clazz  对象clazz
     * @return 返回找到的bean对象
     */
    public static <T> T getSpringBean(String beanId, Class<T> clazz) {
        return getSpringBeanFactory().getBean(beanId, clazz);
    }

    /**
     * 根据给出的对象clazz来获取在Spring当中配置的bean
     *
     * @param clazz 对象clazz
     * @return 返回找到的bean对象
     */
    public static <T> T getSpringBean(Class<T> clazz) {
        return getSpringBeanFactory().getBean(clazz);
    }

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
    public static ApplicationContext getSpringBeanFactory() {
        return applicationContext;
    }

    /**
     * 获取当前线程范围内使用的数据源名称.
     *
     * @return 数据源名称
     */
    public static final String getCurrentDataSourceName() {
        return currentDataSourceNameThreadLocal.get();
    }

    /**
     * 设置当前线程范围内使用的数据源名称.
     *
     * @param dataSourceName 数据源名称
     */
    public static final void setCurrentDataSourceName(String dataSourceName) {
        currentDataSourceNameThreadLocal.set(dataSourceName);
    }

    /**
     * 获取请求对象HttpServletRequest
     *
     * @return 当前线程对应的HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        return requestThreadLocal.get();
    }

    /**
     * 获得框架上下文对象 FrameworkContext
     *
     * @return 当前线程对应的FrameworkContext
     */
    public static FrameworkContext getContext() {
        return frameworkContextThreadLocal.get();
    }

    public static void setContext(FrameworkContext context) {
        frameworkContextThreadLocal.set(context);
    }

    /**
     * 得到当前线程中的HttpServletResponse对象
     *
     * @return 返回当前线程的HttpServletResponse对象
     */
    public static HttpServletResponse getResponse() {
        return responseThreadLocal.get();
    }

    /**
     * 将一个key-value对放到当前线程Request中的Attribute当中
     *
     * @param key key值
     * @param obj 具体对象
     */
    public static void setRequestAttribute(String key, Object obj) {
        requestThreadLocal.get().setAttribute(key, obj);
    }

    /**
     * 从当前线程中Request中取Attribute值
     *
     * @param key Attribute值对应的key
     * @return 返回与该key对应的值对象
     */
    public static Object getRequestAttribute(String key) {
        return requestThreadLocal.get().getAttribute(key);
    }

    /**
     * 从当前线程中Request中取parameter值
     *
     * @param key parameter的key
     * @return 与key对象的字符串值
     */
    public static String getRequestParameter(String key) {
        return requestThreadLocal.get().getParameter(key);
    }

    public static void removeCurrentDataSourceName() {
        currentDataSourceNameThreadLocal.remove();
    }

    /**
     * 清空上下文
     */
    public static void clearContext() {
        frameworkContextThreadLocal.remove();
        responseThreadLocal.remove();
        midThreadLocal.remove();
        requestThreadLocal.remove();
        currentDataSourceNameThreadLocal.remove();
    }

    public static void setHttpRequestResponseHolder(HttpRequestResponseHolder requestResponseHolder) {
        responseThreadLocal.set(requestResponseHolder.getResponse());
        requestThreadLocal.set(requestResponseHolder.getRequest());
    }

    public static String getIdfProperty(String key) {
        return propertiesConfiguration.getProperties().getProperty(key);
    }

    /**
     * 获取SystemProperties对象{@link com.innofi.framework.properties.SystemProperties}
     *
     * @return SystemProperties
     */
    public static SystemProperties getSystemProperties() {
        return getSpringBean("systemProperties");
    }

    /**
     * 获取WebRoot路径
     *
     * @return WebRoot根路径
     */
    public static String getWebRootPath() {
        return webRootPath;
    }

    /**
     * 设置WebRoot路径
     *
     * @param webRootPath WebRoot根路径
     */
    public static void setWebRootPath(String webRootPath) {
        ContextHolder.webRootPath = webRootPath;
    }


}
