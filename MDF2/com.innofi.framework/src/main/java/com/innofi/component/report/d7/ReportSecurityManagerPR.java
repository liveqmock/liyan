/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.d7;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.innofi.component.report.domain.ReportMember;

/**
 * 报表权限页面数据库交互服务类
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportSecurityManagerPR extends ReportManagerPR {

	/**
	 * 根据报表名称，加载指定类型的报表成员信息
	 * 
	 * @param parameter
	 * @return 返回ReportMember集合
	 * @throws Exception
	 */
	@DataProvider
	public Collection<ReportMember> findReportMembers(
			Map<String, Object> parameter) throws Exception {
		String reportId = (String) parameter.get("reportId");
		String type = (String) parameter.get("type");
		return this.getReportService().findReportMembers(reportId, type);
	}

	/**
	 * 保存报表成员信息
	 * 
	 * @param reportId
	 * @param type
	 * @param operate
	 * @param memberIds
	 * @throws Exception
	 */
	@Expose
	public void saveReportMembers(String reportId, String type, String operate,
			List<String> memberIds) throws Exception {
		if (operate.equals("add")) {
			ReportMember rm = null;
			for (String memberId : memberIds) {
				rm = new ReportMember();
				rm.setMemberId(memberId);
				rm.setMasterId(reportId);
				rm.setType(type);
				this.getReportService().saveReportMember(rm);
			}
		} else if (operate.equals("delete")) {
			ReportMember rm = null;
			for (String memberId : memberIds) {
				rm = new ReportMember();
				rm.setId(memberId);
				rm.setMasterId(reportId);
				this.getReportService().deleteReportSecurityMember(rm);
			}
		}
	}

	/**
	 * 刷新报表权限缓存
	 */
	@Expose
	public void refreshCache() {
		this.getReportService().initReportSecurityData();
	}
}
