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
 * 生成报表事件监听器，所有报表生成之前，都会调用做数据预处理事件监听.
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public interface GlobalJasperReportGenerateListener {
	
	/**
	 * 全局报表生成时执行的方法,该方法会在所有报表生成之前都会被调用。
	 * @param reportConfig 数据库报表模版配置信息
	 * @param jasperReport JasperReport对象
	 * @param parameters 预配置参数信息。
	 * @throws Exception
	 */
	public void beforeGlobalGenerate(Report reportConfig,JasperReport jasperReport,Map<String, Object> parameters)throws Exception;
	
}
