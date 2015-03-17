/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.domain;

import java.util.Collection;

import com.innofi.framework.pojo.BasePojo;

/**
 * 定时报表配置方案数据库实体类
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportJobConfig extends BasePojo{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5382798320581618710L;
	
	private String jobId;
	private String configId;
	
	private String url;
	private String desc;
	
	private Collection<ReportMember> userMembers;
	private Collection<ReportMember> deptMembers;
	private Collection<ReportMember> groupMembers;
	
	public String getJobId() {
		return jobId;
	}
	public String getConfigId() {
		return configId;
	}
	public String getUrl() {
		return url;
	}
	public String getDesc() {
		return desc;
	}
	public Collection<ReportMember> getUserMembers() {
		return userMembers;
	}
	public Collection<ReportMember> getDeptMembers() {
		return deptMembers;
	}
	public Collection<ReportMember> getGroupMembers() {
		return groupMembers;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public void setConfigId(String configId) {
		this.configId = configId;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void setUserMembers(Collection<ReportMember> userMembers) {
		this.userMembers = userMembers;
	}
	public void setDeptMembers(Collection<ReportMember> deptMembers) {
		this.deptMembers = deptMembers;
	}
	public void setGroupMembers(Collection<ReportMember> groupMembers) {
		this.groupMembers = groupMembers;
	}
	
}
