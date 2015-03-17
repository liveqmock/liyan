/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.jasper.listener;

import java.util.Map;

import com.innofi.component.report.domain.Report;

import net.sf.jasperreports.engine.JasperReport;


/**
 * 生成报表事件监听器，用于在报表生成之前做数据预处理事件监听.
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public interface JasperReportGenerateListener {

	/**
	 * 根据报表模版被名称，判断当前事件监听类是否支持监听。返回true时，会调用监听类中的beforeGenerate方法,<br>
	 * 如果返回false，则不执行该方法.
	 * @param reportConfigName 报表模版名称
	 * @return 若当前事件监听类是支持监听返回true, 否则false
	 */
	public abstract boolean support(String reportConfigName);
	
	/**
	 * 制定报表模版生成时执行方法，如果在该方法中从添加新的报表参数，可以将参数存入parameters中即可，这样在最终生成报表时会取到。
	 * @param reportConfig  数据库报表模版配置信息
	 * @param jasperReport  JasperReport对象
	 * @param parameters   预配置参数信息。
	 * @throws Exception
	 */
	public void beforeGenerate(Report reportConfig,JasperReport jasperReport,Map<String, Object> parameters)throws Exception;
	
}