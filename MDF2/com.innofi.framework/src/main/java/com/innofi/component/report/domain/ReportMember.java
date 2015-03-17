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
 * 报表成员数据库实体类，存放报表权限成员和定时报表发送邮件接收者
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportMember extends BasePojo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3486922934562720167L;

	/**
	 * 用户类型的成员
	 */
	public static final String USER_MEMBER="U";
	
	/**
	 * 组类型的成员
	 */
	public static final String GROUP_MEMBER="G";
	
	/**
	 * 部门类型的成员
	 */
	public static final String DEPT_MEMBER="D";
	
	private String masterId;
	private String type;
	private String memberId;
	
	private String memberName;

	public String getMasterId() {
		return masterId;
	}

	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
}
