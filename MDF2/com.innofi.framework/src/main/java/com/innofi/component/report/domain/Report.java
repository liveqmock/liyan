/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.domain;

import java.util.ArrayList;
import java.util.Collection;

import com.innofi.framework.pojo.BasePojo;
//import com.innofi.framework.pojo.Variable;

/**
 * 报表实体对象，与数据库字段对应
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class Report extends BasePojo{
	
	/**
	 * 报表分组字段数据库枚举表分类Key值
	 */
	public static final String REPORT_GROUP_CATEGORY="REPORT_GROUP_CATEGORY";
	private static final long serialVersionUID = 1758582839319022875L;
	
	private String ename;
	private String group;
	private String dataSourceName;
	private String cmnt;
	private String uploadFileName;
	
	private String jasperName;
	
	//private Collection<Variable> variables = new ArrayList<Variable>();
	private Collection<ReportMember> userMembers = new ArrayList<ReportMember>();
	private Collection<ReportMember> deptMembers = new ArrayList<ReportMember>();	
	private Collection<ReportMember> groupMembers = new ArrayList<ReportMember>();
	
	public Report(String id){
		super(id);
	}
	public Report(){
		
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public String getCmnt() {
		return cmnt;
	}
	public void setCmnt(String cmnt) {
		this.cmnt = cmnt;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
/*	public Collection<Variable> getVariables() {
		return variables;
	}
	public void setVariables(Collection<Variable> variables) {
		this.variables = variables;
	}*/
	public Collection<ReportMember> getUserMembers() {
		return userMembers;
	}
	public void setUserMembers(Collection<ReportMember> userMembers) {
		this.userMembers = userMembers;
	}
	public Collection<ReportMember> getDeptMembers() {
		return deptMembers;
	}
	public void setDeptMembers(Collection<ReportMember> deptMembers) {
		this.deptMembers = deptMembers;
	}
	public Collection<ReportMember> getGroupMembers() {
		return groupMembers;
	}
	public void setGroupMembers(Collection<ReportMember> groupMembers) {
		this.groupMembers = groupMembers;
	}
	public String getJasperName() {
		return jasperName;
	}
	public void setJasperName(String jasperName) {
		this.jasperName = jasperName;
	}
}
