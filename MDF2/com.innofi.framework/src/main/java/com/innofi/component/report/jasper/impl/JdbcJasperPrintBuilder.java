/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.jasper.impl;

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.StringUtils;

import com.innofi.framework.dao.DaoSupport;

import com.innofi.component.report.domain.Report;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 报表生成器,报表中的数据是通过JDBC的方式提供
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class JdbcJasperPrintBuilder extends DaoSupport{
	
	/**
	 * 
	 * @param report
	 * @param jasperReport
	 * @param parameters
	 * @return 返回JasperPrint对象
	 * @throws Exception
	 */
	public JasperPrint buildJasperPrint(Report report,JasperReport jasperReport, Map<String, Object> parameters)throws Exception {
		Connection conn = null;
		JasperPrint jasperPrint = null;
		String dataSourceName = report.getDataSourceName();
		DataSource dataSource = null;
		if(StringUtils.hasText(dataSourceName)){
			ContextHolder.setCurrentDataSourceName(dataSourceName);
		}
		try{
			dataSource = this.getJdbcDao().getDataSource();
			conn=DataSourceUtils.getConnection(dataSource);
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					conn);
		}finally{
			DataSourceUtils.releaseConnection(conn, dataSource);
			ContextHolder.removeCurrentDataSourceName();
		}
		return jasperPrint;
	}

}
