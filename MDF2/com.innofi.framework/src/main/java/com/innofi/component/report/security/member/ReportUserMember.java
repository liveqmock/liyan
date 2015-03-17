/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.security.member;

/**
 * 报表权限用户成员
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportUserMember extends ReportSecurityMember{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8978337044072598962L;
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
