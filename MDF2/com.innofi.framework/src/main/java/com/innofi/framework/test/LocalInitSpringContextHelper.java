package com.innofi.framework.test;

import com.innofi.framework.properties.PropertiesConfigurationLoader;
import com.innofi.framework.spring.context.DynamicContextLoader;
import com.innofi.framework.spring.context.SpringContextHolder;
import com.innofi.framework.utils.resource.ResourceUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2012-5-24
 * @found time: 20:40:56
 * 本地初始化SpringContext帮助类，方便本地调试，请将innofi-framework.xml放入classpath:META-INF目录下，<p/>
 * 如需加载其它spring配置文件，可在innofi-framework.xml中通过<import/>标签加载。
 */
public class LocalInitSpringContextHelper {

    private static PropertiesConfigurationLoader propertiesConfiguration;

    private static final String INNOFI_FRAMEWORK_CONFIG = "mdf-framework.xml";

    private static final String META_INF = "META-INF";

    private static final String CLASSPATH_URL_PREFIX = "classpath*:";

    private static boolean isLocalRun = false;

    public static void initSpringContextHolder() {

        try {
            SpringContextHolder.getApplicationContext();//没有异常说明已经初始化，则不重复执行初始化
        } catch (IllegalStateException e) {
            isLocalRun = true;
            File f = null;
            try {
                f = ResourceUtil.getFile(CLASSPATH_URL_PREFIX + META_INF + "/" + INNOFI_FRAMEWORK_CONFIG);
                if (f != null) {
                    String[] moduleLocations = DynamicContextLoader.getModuleLocations();
                    ApplicationContext context = new ClassPathXmlApplicationContext(moduleLocations);
                    SpringContextHolder.localSetApplicationContext(context);
                }
            } catch (FileNotFoundException fe) {
                fe.printStackTrace();
                throw new RuntimeException("请在classpath下建立META-INF目录，并放入mdf-framework.xml配置文件!");
            } catch (Exception allE) {
                allE.printStackTrace();
                throw new RuntimeException("初始化ContextHolder出现异常，请检查[mdf-framework.xml]相关配置!");
            }
        }
    }

    public static boolean isLocalRun() {
        return isLocalRun;
    }

}
