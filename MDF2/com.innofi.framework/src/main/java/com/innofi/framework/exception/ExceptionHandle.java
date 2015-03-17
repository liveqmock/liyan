package com.innofi.framework.exception;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * 一个用于记录Exception信息，同时加载用户自定义ExceptionInterceptor的类
 * <p/>
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * <p/>
 * all rights reserved.
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0 2012-5-20 上午11:37:06
 * @since JDK1.6
 */
public class ExceptionHandle implements ApplicationContextAware {

    private String logDir;
    private WebApplicationContext webApplicationContext;
    private Collection<IExceptionInterceptor> exceptionInterceptorSet;

    /**
     * 将异常信息记录到日志文件并调用对应的ExceptionInterceptor类对抛出的异常进行处理
     *
     * @param holder
     * @param ex
     * @throws java.io.IOException
     */
    public void handleException(HttpRequestResponseHolder holder, Exception ex) throws IOException, ServletException {
        Throwable throwable = getThrowableCause(ex);
        for (IExceptionInterceptor interceptor : exceptionInterceptorSet) {
            if (interceptor.support(throwable)) {
                interceptor.handleException(holder, throwable);
            }
        }
        throw new ServletException(ex);
    }

    /**
     * 找到异常的根
     *
     * @param throwable
     * @return 返回throwable
     */
    private Throwable getThrowableCause(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause == null) {
            return throwable;
        } else {
            return getThrowableCause(cause);
        }
    }

    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        Assert.hasText(logDir, "logDir不能为空!");
        if (context instanceof WebApplicationContext) {
            webApplicationContext = (WebApplicationContext) context;
            if (!new File(logDir).exists()) {
                logDir = webApplicationContext.getServletContext().getRealPath(
                        logDir);
            }

        } else {
            if (!new File(logDir).exists()) {
                logDir = System.getProperty("user.home") + logDir;
            }
        }
        logDir += File.separator + "modules";
        exceptionInterceptorSet = context.getBeansOfType(IExceptionInterceptor.class).values();
    }

    public String getLogDir() {
        return logDir;
    }

    public void setLogDir(String logDir) {
        this.logDir = logDir;
    }

}
