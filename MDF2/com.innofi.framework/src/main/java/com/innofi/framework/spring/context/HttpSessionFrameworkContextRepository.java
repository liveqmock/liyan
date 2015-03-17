package com.innofi.framework.spring.context;

import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 一个FrameworkContextRepository默认实现类，将当前会话信息FrameworkContext存放在HttpSession当中
 */
public class HttpSessionFrameworkContextRepository implements FrameworkContextRepository {
    public static final String FRAMEWORK_CONTEXT_KEY = "__FRAMEWORK_CONTEXT_KEY";
    private String frameContextContextBeanId = "mdf.defaultFrameWorkContext";
    private WebApplicationContext applicationContext;

    /**
     * (non-Javadoc)
     *
     * @see FrameworkContextRepository#loadFrameworkContext(org.springframework.security.web.context.HttpRequestResponseHolder)
     */
    public FrameworkContext loadFrameworkContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        HttpSession httpSession = request.getSession(false);
        FrameworkContext frameContext = readFrameworkContextFromSession(httpSession);
        if (frameContext == null) {
            frameContext = (FrameworkContext) applicationContext.getBean(frameContextContextBeanId);
        }
        frameContext.resetStatus();
        return frameContext;
    }

    /**
     * (non-Javadoc)
     *
     * @see FrameworkContextRepository#saveFrameworkContext(FrameworkContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void saveFrameworkContext(FrameworkContext context, HttpServletRequest request, HttpServletResponse response) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return;
        }
        if (httpSession.getAttribute(FRAMEWORK_CONTEXT_KEY) == null) {
            httpSession.setAttribute(FRAMEWORK_CONTEXT_KEY, context);
            return;
        }
        if (context != null && context.isNeedSave()) {
            httpSession.setAttribute(FRAMEWORK_CONTEXT_KEY, context);
        }
    }

    /**
     * 从session中取FrameworkContext对象
     *
     * @param httpSession
     * @return 返回FrameworkContext对象
     */
    private FrameworkContext readFrameworkContextFromSession(HttpSession httpSession) {
        if (httpSession == null) return null;
        Object contextFromSession = httpSession.getAttribute(FRAMEWORK_CONTEXT_KEY);
        if (contextFromSession == null) {
            return null;
        } else {
            return (FrameworkContext) contextFromSession;
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see FrameworkContextRepository#setServletContext(javax.servlet.ServletContext)
     */
    public void setServletContext(ServletContext servletContext) {
        this.applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }

}
