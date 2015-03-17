package com.innofi.framework.properties;

import com.innofi.framework.utils.resource.ResourceUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classpath属性文件加载器
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public class ClassPathProperties {

    private String propertyFileName;

    private Properties properties;

    private boolean loaded = false;

    public ClassPathProperties() {

    }

    public ClassPathProperties(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    public String getPropertyFileName() {
        return this.propertyFileName;
    }

    public void setPropertyFileName(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    /**
     * 获得属性对象方法
     *
     * @return 返回一个properites文件
     */
    public Properties getProperties() {
        if (!loaded) {
            load();
        }
        return properties;
    }

    /**
     * 根据key获得对应的属性值
     *
     * @param key 属性key
     * @return key对应的属性值
     */
    public String getProperty(String key) {
        if (!loaded) {
            load();
        }
        String value = null;
        if (properties != null) {
            value = (String) properties.get(key);
        }
        return value;

    }

    private void load() {
        if (propertyFileName.indexOf(ResourceUtil.CLASSPATH_URL_PREFIX) > -1) {
            propertyFileName = propertyFileName.replace(ResourceUtil.CLASSPATH_URL_PREFIX, "");
        }
        InputStream is = ClassPathProperties.class.getClassLoader().getResourceAsStream(this.propertyFileName);
        properties = new Properties();
        try {
            properties.load(is);
            loaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}