/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.security.member;

/**
 * 报表权限群组权限成员
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportGroupMember extends ReportSecurityMember{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6247084310475963290L;
	private String groupId;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}