package com.innofi.framework.spring.context;

import com.innofi.framework.utils.console.ConsoleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司 all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 2012-5-24
 * @found time: 20:40:56
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候中取出ApplicaitonContext.
 */
public class SpringContextHolder implements ApplicationContextAware {

    private Logger _log = LoggerFactory.getLogger(SpringContextHolder.class);

    private static ApplicationContext applicationContext;

    private static boolean isInitialized = false;


    public static void localSetApplicationContext(ApplicationContext localAppContext) {
        ConsoleUtil.info("SpringContextHolder Local initialization");
        if (!isInitialized) {
            applicationContext = localAppContext;
            isInitialized = true;
        } else {
            throw new RuntimeException("SpringContextHolder的ApplicationContext已经注册，不能重复注册，请检查配置！");
        }
    }

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name, clazz);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return (T) applicationContext.getBeansOfType(clazz);
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("SpringContextHolder未初始化，请检查innofi-framework配置文件，添加代码片段：<context:component-scan base-package=\"com.innofi\" />！");
        }
    }

}
