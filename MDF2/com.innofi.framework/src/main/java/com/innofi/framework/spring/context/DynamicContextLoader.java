package com.innofi.framework.spring.context;

import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.string.StringUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 用于自动感知IDF项目各模块Spring配置文件<br>
 * 通过动态加载器，IDF框架各模块只需要将jar放在classpath中，框架将自动加载其Spring配置文件
 */
public class DynamicContextLoader {

    /*
     * 模块/组件说明配置文件存放位置
     */
    private static final String PACKAGE_PROPERTIES_LOCATION = "META-INF/mdf-package.properties";

    /*
     * 当前模块/组件Spring配置文件在mdf-package.properties配置文件中的key
     * value为文件Spring配置文件所在位置
     */
    private static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    /*
     * 当前模块/组件依赖在mdf-package.properties配置文件中的key
     * value为当前模块/组件所依赖的其它模块/组件
     */
    private static final String CONTEXT_CONFIG_DEPENDS = "depends";

    /*
     * 当前模块/组件的名称
     */
    private static final String CONTEXT_CONFIG_NAME = "name";

    /*
     * 数据源配置文件所在位置
     */
    private static final String DATA_SOURCES_CONFIG = "classpath:META-INF/datasources.xml";


    /**
     * 初始化Spring ApplicationContext 需要在WEB-INF/web.xml中配置
     * <context-param>
     * <param-name>contextConfigLocation</param-name>
     * <param-value></param-value>
     * </context-param>
     *
     * @param applicationContext @see org.springframework.web.context.ConfigurableWebApplicationContext
     */
    public void loadContext(ConfigurableWebApplicationContext applicationContext) {
        String[] moduleLocations = getModuleLocations();
        String[] configLocations = applicationContext.getConfigLocations();
        configLocations = (String[]) ArrayUtils.addAll(moduleLocations, configLocations);
        applicationContext.setConfigLocations(configLocations);
    }


    /**
     * 本地测试使用，获得模块需加载Spring bean 配置文件
     * @return
     */
    public static String[] getModuleLocations() {
        /*
        * 保存模块/组件对应的package.properties key为模块/组件名称 value为properties集合
        */
        Map<String, Properties> packageMap = new HashMap<String, Properties>();
        InputStream in = null;
        try {
            Enumeration<URL> defaultContextFileResources = ClassUtils.getDefaultClassLoader().getResources(PACKAGE_PROPERTIES_LOCATION);

            while (defaultContextFileResources.hasMoreElements()) {
                URL url = defaultContextFileResources.nextElement();
                URLConnection con = url.openConnection();
                con.setUseCaches(false);
                in = con.getInputStream();
                Properties properties = new Properties();
                properties.load(in);

                String packageName = properties.getProperty(CONTEXT_CONFIG_NAME);
                if (StringUtils.isEmpty(packageName)) {
                    throw new IllegalArgumentException(
                            "Package name undefined in [" + url.getPath()
                                    + "].");
                }
                packageMap.put(packageName, properties);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ex) {

                }
            }
        }

        /*
         * 检查模块/组件依赖模块是否已被加载
         * 说明->符号表示依赖
         * A->B A被配置到项目当中 ， 此时检查B是否也被加载到项目中，没有则抛出IllegalArgumentException异常提示用户
         */
        for (Properties properties : packageMap.values()) {
            calculateDepends(properties, packageMap);
        }

        String[] moduleLocations = {};
        for (Properties properties : packageMap.values()) {
            String configLocation = properties.getProperty(CONTEXT_CONFIG_LOCATION);
            if (StringUtils.isNotEmpty(configLocation)) {
                if(configLocation.indexOf(",")>-1){
                    String[] configLocations = StringUtil.split(configLocation);
                    moduleLocations = (String[])ArrayUtils.addAll(moduleLocations,configLocations);
                }else{
                    moduleLocations = (String[]) ArrayUtils.add(moduleLocations, configLocation);
                }
                ConsoleUtil.info("Loading module " + properties.getProperty(CONTEXT_CONFIG_NAME));
            }
        }
        moduleLocations = (String[]) ArrayUtils.add(moduleLocations, DATA_SOURCES_CONFIG);
        return moduleLocations;
    }

    /*
     * 检查IDF模块/组件依赖
     *
     * @param properties    属性集合
     * @param packageMap    已加载模块/组件映射 key模块/组件名称 value属性集合
     */
    private static void  calculateDepends(Properties properties, Map<String, Properties> packageMap) {
        String depends = properties.getProperty(CONTEXT_CONFIG_DEPENDS);
        if (StringUtils.isNotEmpty(depends)) {
            for (String dependsPackage : depends.split(",")) {
                dependsPackage = StringUtils.trim(dependsPackage);
                Properties dependsProperties = packageMap.get(dependsPackage);
                if (dependsProperties == null) {
                    throw new IllegalArgumentException("mdf module " + properties.getProperty(CONTEXT_CONFIG_NAME) + " depends package  [" + dependsPackage + "] not found.");
                }
                calculateDepends(dependsProperties, packageMap);
            }
        }
    }
}
