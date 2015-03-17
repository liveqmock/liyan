package com.innofi.framework.spring.context;

import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.innofi.framework.exception.ExceptionHandle;
import com.innofi.framework.properties.PropertiesConfigurationLoader;
import com.innofi.framework.utils.console.ConsoleUtil;

import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 一个用来决定会话上下文信息保存地的filter类，默认所有的会话信息放在HttpSession当中<br>
 * 同时该Filter还负责执行用户自定义的FilterInterceptor
 */
public class ContextPersistenceFilter extends CharacterEncodingFilter {

    private static final String FILTER_APPLIED = "_mdf_context_persistence_filter";

    private ExceptionHandle exceptionHandle;
    private boolean forceEagerSessionCreation = true;
    private FrameworkContextRepository frameworkContextRepository = new HttpSessionFrameworkContextRepository();
    private Collection<FilterInterceptor> filterInterceptorSet;

    @Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		FilterConfig filterConfig = getFilterConfig();

        String repositoryImpl = filterConfig.getInitParameter("frameworkContextRepository");
        if (StringUtils.hasText(repositoryImpl)) {
            try {
                frameworkContextRepository = (FrameworkContextRepository) Class.forName(repositoryImpl).newInstance();
                frameworkContextRepository.setServletContext(filterConfig.getServletContext());
                exceptionHandle = ContextHolder.getSpringBeanFactory().getBean(ExceptionHandle.class);
                PropertiesConfigurationLoader propLoader = ContextHolder.getSpringBeanFactory().getBean(PropertiesConfigurationLoader.class);
                filterInterceptorSet = ContextHolder.getSpringBeanFactory().getBeansOfType(FilterInterceptor.class).values();
            } catch (Exception e) {
            	e.printStackTrace();
                throw new ServletException("名为" + repositoryImpl + "的FrameworkContextRepository实现类不合法！");
            }
        }
	}

    
    
    @Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {

        if (request.getAttribute(FILTER_APPLIED) != null) {
            // 保证当前filter只会在请求当中经历一次
        	filterChain.doFilter(request, response);
            return;
        }

        request.setAttribute(FILTER_APPLIED, Boolean.TRUE);// 将一个过滤标志放进去，以保证该filter只处理一次请求

        if (forceEagerSessionCreation) {
            request.getSession();
        }

        HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request, response);
        FrameworkContext frameworkContext = frameworkContextRepository.loadFrameworkContext(holder);
        ContextHolder.setHttpRequestResponseHolder(holder);

        try {
            ContextHolder.setContext(frameworkContext);
            doFilterInterceptors(request, response, true);
            super.doFilterInternal(holder.getRequest(), holder.getResponse(), filterChain);
        } catch (Exception ex) {
        	if(ex instanceof ClientRunnableException){
        		ClientRunnableException cex = (ClientRunnableException) ex;
        		throw cex;
        	}
        	exceptionHandle.handleException(holder, ex);
        } finally {
            try {
				doFilterInterceptors(request, response, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
            frameworkContextRepository.saveFrameworkContext(ContextHolder.getContext(), request, response);
            ContextHolder.clearContext();
            request.removeAttribute(FILTER_APPLIED);
        }
	}

    private void doFilterInterceptors(HttpServletRequest request, HttpServletResponse response, boolean before) throws Exception {
        for (FilterInterceptor fi : filterInterceptorSet) {
            if (before) {
                fi.before(request, response);
            } else {
                fi.after(request, response);
            }
        }
    }
}
