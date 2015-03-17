package com.innofi.framework.spring.context.listener;

import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.spring.context.DynamicContextLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 通过扩展标准Spring的ContextLoaderListener，实现加载项目模块配置文件功能
 */
public class ContextLoaderListener extends org.springframework.web.context.ContextLoaderListener {
    private static final Log logger = LogFactory.getLog(ContextLoaderListener.class);

    public ContextLoaderListener() {

    }

    @Override
    protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {
        try {
            DynamicContextLoader loader = new DynamicContextLoader();
            loader.loadContext(applicationContext);
            String webRootPath = servletContext.getRealPath("/");
            ContextHolder.setWebRootPath(webRootPath);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

}