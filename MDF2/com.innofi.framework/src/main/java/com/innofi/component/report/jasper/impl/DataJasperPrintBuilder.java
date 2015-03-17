/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.jasper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.innofi.component.report.domain.Report;
import com.innofi.component.report.jasper.JasperReportBuilder;

/**
 * Jasper报表构建器,通过指定的报表模版,以及给定的数据,生成对应的报表
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class DataJasperPrintBuilder {

	/**
	 * Report报表生成
	 * @param report
	 * @param jasperReport
	 * @param parameters
	 * @param data
	 * @return 返回JasperPrint对象
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JasperPrint buildJasperPrint(Report report,JasperReport jasperReport, Map<String, Object> parameters,Object data)throws Exception {
		JRDataSource dataSource = null;
		parameters.put(JasperReportBuilder.JASPER_SPRING_DATASOURCE_KEY, "-dorado-provider-");
		if(data instanceof Collection){
			dataSource = new JRBeanCollectionDataSource((Collection)data);
		}else{
			ArrayList list = new ArrayList();
			list.add(data);
			dataSource = new JRBeanCollectionDataSource(list);
		}
		return JasperFillManager.fillReport(jasperReport, parameters,
				dataSource);
	}
}
