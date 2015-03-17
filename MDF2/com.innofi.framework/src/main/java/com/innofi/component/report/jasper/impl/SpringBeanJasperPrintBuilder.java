/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.jasper.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.innofi.component.report.domain.Report;
import com.innofi.component.report.jasper.JasperReportAnalzyAssistBean;
import com.innofi.component.report.jasper.JasperReportUtils;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 报表生成构建器,报表中的数据来自SpringBean中的某个方法
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class SpringBeanJasperPrintBuilder {

	/**
	 * 
	 * @param report
	 * @param jasperReport
	 * @param parameters
	 * @param beanId
	 * @param methodName
	 * @return 返回JasperPrint对象
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public JasperPrint buildJasperPrint(Report report, JasperReport jasperReport, Map<String, Object> parameters, String beanId, String methodName)throws Exception {
		JRDataSource dataSource = null;
		Collection datas = null;
		if(parameters.size() != 0)
		   datas = this.findDatas(beanId.trim(),methodName.trim(),parameters);
		if(datas != null)
			dataSource = new JRBeanCollectionDataSource(datas);
		else
			dataSource = new JRBeanCollectionDataSource(new ArrayList());
		return JasperFillManager.fillReport(jasperReport, parameters,
				dataSource);
	}
	
	/**
	 * 调用配置在Jasper模版文件的参数列表中指定SpringBeanId和方法，获取模版需要的数据。<br>
	 * 如果没有配置SPRING_DATASOURCE参数，则任务该模版不需要调用springBean来获取数据.
	 * 
	 * @param beanId
	 * @param methodName
	 * @param parameters 参数集合
	 * @return 返回集合
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private Collection findDatas(String beanId,String methodName,Map<String,Object> parameters) throws Exception{
		Object instance = ContextHolder.getSpringBeanFactory().getBean(beanId);
		Method method = null;
		Object results = null;
		JasperReportAnalzyAssistBean assistBean = JasperReportUtils.getMethodVariableNames(instance.getClass().getName(), methodName);
		Object[] objs = this.buildParameters(assistBean, parameters);		
		method = instance.getClass().getMethod(methodName.trim(), assistBean.parameterTypesToClassArray());
		results = method.invoke(instance, objs);
		return (Collection) results;
	}
	
	/**
	 * 解析调用方法的参数信息
	 * 
	 * @param assistBean
	 * @param parameters
	 * @return 返回参数数组
	 */
	@SuppressWarnings("rawtypes")
	private Object[] buildParameters(JasperReportAnalzyAssistBean assistBean, Map<String,Object> parameters){
		Object[] objs = new Object[assistBean.getParameterNames().size()];
		String name = null;
		Class type = null;
		for(int i=0;i<assistBean.getParameterNames().size() ;i++){
			name = assistBean.getParameterNames().get(i);
			type = assistBean.getParameterTypes().get(i);
			if(type == Map.class){
				objs[i] = parameters;
			}else{
				objs[i] = parameters.get(name);
			}
		}
		return objs;
	}
}
