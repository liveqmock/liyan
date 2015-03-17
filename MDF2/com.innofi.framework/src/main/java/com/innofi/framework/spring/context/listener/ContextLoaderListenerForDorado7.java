package com.innofi.framework.spring.context.listener;

import com.bstek.dorado.web.loader.DoradoLoader;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import javax.servlet.ServletContext;
import java.util.List;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 通过扩展标准Spring的ContextLoaderListener，实现加载项目模块配置文件及dorado模块配置文件
 */
public class ContextLoaderListenerForDorado7 extends ContextLoaderListener {

    private static final Log logger = LogFactory.getLog(ContextLoaderListenerForDorado7.class);


    private DoradoLoader doradoLoader;

    public ContextLoaderListenerForDorado7() {
        doradoLoader = DoradoLoader.getInstance();
    }

    @Override
    protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {
        try {
            doradoLoader.preload(servletContext, true);
            List<String> doradoContextLocations = doradoLoader.getContextLocations(false);
            String[] realResourcesPath = doradoLoader.getRealResourcesPath(doradoContextLocations);
            applicationContext.setConfigLocations(realResourcesPath);
            super.customizeContext(servletContext,applicationContext);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

}
