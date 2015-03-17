package com.innofi.framework.properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 一个用来加载IDF框架properties配置的文件类，<br>
 * 该类会自动扫描classpath下所有default类型以及用户自定义的idf的properties文件，并将其加载<br>
 * 用户自定义的IDF的properties配置文件会覆盖default类型的properties文件内容
 */
public class PropertiesConfigurationLoader extends PropertyPlaceholderConfigurer {

    private String idfPropertiesFile = "META-INF/mdf.properties";

    /*
      * 覆盖父类方法，用来加载所有idf的properties文件<br>
      * 首先加载位于classpath当中所有的default类型的idf的properties文件,<br>
      * 然后再加载位于classpath:META-INF下的idf的properties文件
      * */
    protected void loadProperties(Properties properties) throws IOException {
        this.properties = properties;
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourceResolver.getResources("classpath*:com/innofi/framework/**/*.properties");

        for (Resource res : resources) {
            Properties prop = new Properties();
            InputStream inputStream = res.getInputStream();
            prop.load(inputStream);
            CollectionUtils.mergePropertiesIntoMap(prop, properties);
            inputStream.close();
        }

        resources = resourceResolver.getResources("classpath*:META-INF/idf*.properties");
        for (Resource res : resources) {
            Properties prop = new Properties();
            InputStream inputStream = res.getInputStream();
            prop.load(inputStream);
            CollectionUtils.mergePropertiesIntoMap(prop, properties);
            inputStream.close();
        }

        resources = resourceResolver.getResources("classpath*:META-INF/system-env.properties");
        for (Resource res : resources) {
            Properties prop = new Properties();
            InputStream inputStream = res.getInputStream();
            prop.load(inputStream);
            CollectionUtils.mergePropertiesIntoMap(prop, properties);
            inputStream.close();
        }

        resources = resourceResolver.getResources("classpath*:META-INF/system-ext.properties");
        for (Resource res : resources) {
            Properties prop = new Properties();
            InputStream inputStream = res.getInputStream();
            prop.load(inputStream);
            CollectionUtils.mergePropertiesIntoMap(prop, properties);
            inputStream.close();
        }

        idfPropertiesFile = ClassUtils.getDefaultClassLoader().getResource(idfPropertiesFile).toString();
        File f = new File(this.idfPropertiesFile);
        if (f.exists()) {
            Properties p = new Properties();
            FileInputStream fin = new FileInputStream(f);
            p.load(fin);
            CollectionUtils.mergePropertiesIntoMap(p, properties);
            fin.close();
        }
    }

    private Properties properties = null;

    /**
     * 取得Spring管理的properties.
     *
     * @return Spring管理的properties
     */
    public Properties getProperties() {
        return properties;
    }

}
