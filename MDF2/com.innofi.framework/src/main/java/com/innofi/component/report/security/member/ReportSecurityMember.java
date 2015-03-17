/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.security.member;

import java.io.Serializable;

import com.innofi.component.report.domain.Report;


/**
 * 报表权限成员服务抽象Bean
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportSecurityMember implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4597750085611768584L;
	private Report report;

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}
}
