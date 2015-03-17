/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.jasper;

import java.util.ArrayList;

/**
 * JasperReport 方法解析服务类
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class JasperReportAnalzyAssistBean {

	private ArrayList<String> parameterNames;
	@SuppressWarnings("rawtypes")
	private ArrayList<Class> parameterTypes;
	@SuppressWarnings("rawtypes")
	public JasperReportAnalzyAssistBean() {
		parameterNames = new ArrayList<String>();
		parameterTypes = new ArrayList<Class>();
	}
	@SuppressWarnings("rawtypes")
	public void add(String name,Class type){
		parameterNames.add(name);
		parameterTypes.add(type);
	}
	@SuppressWarnings("rawtypes")
	public void clear(){
		parameterNames = new ArrayList<String>();
		parameterTypes = new ArrayList<Class>();
	}
	public String getName(int i){
		return parameterNames.get(i);
	}
	@SuppressWarnings("rawtypes")
	public Class getType(int i){
		return parameterTypes.get(i);
	}
	public ArrayList<String> getParameterNames(){
		return this.parameterNames;
	}
	@SuppressWarnings("rawtypes")
	public ArrayList<Class> getParameterTypes(){
		return this.parameterTypes;
	}
	@SuppressWarnings("rawtypes")
	public Class[] parameterTypesToClassArray(){
		Class[] clazzs = new Class[this.parameterTypes.size()];
		for(int i=0;i<this.parameterTypes.size();i++){
			clazzs[i] = this.parameterTypes.get(i);
		}
		return clazzs;
	}
}
