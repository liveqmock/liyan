/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.security.member;

/**
 * 报表权限部门成员
 *  
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportDeptMember extends ReportSecurityMember{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5123591029928592302L;
	private String deptId;
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
}
