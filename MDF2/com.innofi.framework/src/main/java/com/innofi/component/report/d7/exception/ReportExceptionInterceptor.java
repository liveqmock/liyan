/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.d7.exception;

import org.springframework.security.web.context.HttpRequestResponseHolder;

import com.innofi.component.report.exception.ReportException;

/**
 * 报表模块异常拦截器，用于处理报表模块抛出的所有业务性异常com.bstek.bdf.report.exception.ReportException.<br>
 * 跳转到指定的页面.
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportExceptionInterceptor {//implements ExceptionInterceptor{
	
	private String accessDeniedPath;

	/*
	 * (non-Javadoc)
	 * @see com.bstek.bdf.exception.ExceptionInterceptor#handleException(org.springframework.security.web.context.HttpRequestResponseHolder, java.lang.Throwable)
	 */
	public void handleException(HttpRequestResponseHolder holder,
			Throwable exception) {
		try {
			
			holder.getRequest().setAttribute("exceptionMessage", exception.getMessage());
			holder.getRequest().getRequestDispatcher(accessDeniedPath).forward(holder.getRequest(), holder.getResponse());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.bstek.bdf.exception.ExceptionInterceptor#support(java.lang.Throwable)
	 */
	public boolean support(Throwable ex) {
		if(ex instanceof ReportException)
			return true;
		return false;
	}

	public void setAccessDeniedPath(String accessDeniedPath) {
		this.accessDeniedPath = accessDeniedPath;
	}

}
