/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.domain;

import com.innofi.framework.pojo.BasePojo;


/**
 * 定时报表数据库实体类
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportJob extends BasePojo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1255365563593905791L;
	
	private Report report;
	private String jobId;
	private String type;
	private String fileName;
	
	public Report getReport() {
		return report;
	}
	public String getJobId() {
		return jobId;
	}
	public String getType() {
		return type;
	}
	public String getFileName() {
		return fileName;
	}
	public void setReport(Report report) {
		this.report = report;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
