/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.framework.controller;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.bstek.dorado.util.Assert;

/**
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public class ControllerResolver extends AbstractController {
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String requestURI = request.getRequestURI();
		int lastIndexOfSlash = requestURI.lastIndexOf("/");
		String actionPath = requestURI.substring(lastIndexOfSlash + 1);
		int cp1=actionPath.lastIndexOf(".c");
		int cp2=actionPath.lastIndexOf(".c;");
		int cp3=actionPath.lastIndexOf(".c?");
		if(cp2>-1){
			actionPath = actionPath.substring(0,cp2);
		}else if(cp3>-1){
			actionPath = actionPath.substring(0,cp3);
		}else if(cp1>-1){
			actionPath = actionPath.substring(0,cp1);
		}
		int indexOfStop = actionPath.lastIndexOf(".");
		String beanId = actionPath.substring(0,indexOfStop);
		String methodName = actionPath.substring(indexOfStop + 1);
		methodName = CONTROLLER_METHOD_PREFIX + Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
		Object bean = super.getApplicationContext().getBean(beanId);
		Assert.notNull(bean);
		Class<?> clazz = bean.getClass();
		Method method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);	
		method.invoke(bean,request,response);
		return null;
	}

	private static final String CONTROLLER_METHOD_PREFIX = "do";
}
