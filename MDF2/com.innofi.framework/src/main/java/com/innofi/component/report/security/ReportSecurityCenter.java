/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.RowMapper;

import com.innofi.component.dbconsole.GroupDocument.Group;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.component.report.domain.Report;
import com.innofi.component.report.domain.ReportMember;
import com.innofi.component.report.security.member.ReportDeptMember;
import com.innofi.component.report.security.member.ReportGroupMember;
import com.innofi.component.report.security.member.ReportSecurityMember;
import com.innofi.component.report.security.member.ReportUserMember;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 报表权限验证中心
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportSecurityCenter extends DaoSupport implements InitializingBean{

	private static final String REPORT_SECURITY_RESOURCES = "REPORT_SECURITY_RESOURCES";

	/**
	 * 服务启动时初始化报表权限缓存数据
	 * 
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.initReportSecurityData();
	}

	/**
	 * 报表缓存数据刷新
	 * 
	 */
	public void initReportSecurityData() {
		List<ReportSecurityMember> reportSecurityResources = null;
		String sql = "select b.id_,b.ename_,b.group_,b.datasource_name_,b.cmnt_,b.upload_file_name_,rr.type_,rr.member_id_ "
				+ "from bdf_report_members rr " + "left join sys_reports b on b.id_=rr.master_id_";
		reportSecurityResources = this.getJdbcDao().query(sql, new RowMapper<ReportSecurityMember>() {
			public ReportSecurityMember mapRow(ResultSet rs, int rowNum) throws SQLException {
				Report report = new Report();
				report.setId(rs.getString("ID_"));
				report.setEname(rs.getString("ENAME_"));
				report.setGroup(rs.getString("GROUP_"));
				report.setDataSourceName(rs.getString("DATASOURCE_NAME_"));
				report.setCmnt(rs.getString("CMNT_"));
				report.setUploadFileName(rs.getString("UPLOAD_FILE_NAME_"));
				String memberType = rs.getString("TYPE_");
				String memberId = rs.getString("MEMBER_ID_");
				if (ReportMember.USER_MEMBER.equals(memberType)) {
					ReportUserMember userMember = new ReportUserMember();
					userMember.setUsername(memberId);
//					userMember.setReport(report);
					return userMember;
				} else if (ReportMember.DEPT_MEMBER.equals(memberType)) {
					ReportDeptMember deptMember = new ReportDeptMember();
					deptMember.setDeptId(memberId);
//					deptMember.setReport(report);
					return deptMember;
				} else if (ReportMember.GROUP_MEMBER.equals(memberType)) {
					ReportGroupMember groupMember = new ReportGroupMember();
//					groupMember.setReport(report);
					groupMember.setGroupId(memberId);
					return groupMember;
				}
				return null;
			}
		});
		Map<String, List<ReportSecurityMember>> resources = new HashMap<String, List<ReportSecurityMember>>();
		List<ReportSecurityMember> tempList = null;
		String memberType = null;
		for (ReportSecurityMember rsm : reportSecurityResources) {
//			if (rsm instanceof ReportUserMember) {
//				tempList = resources.get(rsm.getReport().getId() + "-U");
//				memberType = "U";
//			} else if (rsm instanceof ReportDeptMember) {
//				tempList = resources.get(rsm.getReport().getId() + "-D");
//				memberType = "D";
//			} else {
//				tempList = resources.get(rsm.getReport().getId() + "-G");
//				memberType = "G";
//			}
//
//			if (tempList == null) {
//				tempList = new ArrayList<ReportSecurityMember>();
//				resources.put(rsm.getReport().getId() + "-" + memberType, tempList);
//			}
//			tempList.add(rsm);
		}
		//applicationCache.putCacheObject(REPORT_SECURITY_RESOURCES, resources);
	}

	/**
	 * 根据报表名称，验证当前登录用户是否有报表的访问权限
	 * 
	 * @param reportId
	 *            报表名称
	 * @return 若有权限返回true，否则返回false
	 */
	@SuppressWarnings("unchecked")
	public boolean checkAuth(String reportId) {
		Map<String, List<ReportSecurityMember>> resources=null ;//(Map<String, List<ReportSecurityMember>>) applicationCache .getCacheObject(REPORT_SECURITY_RESOURCES)
		List<ReportSecurityMember> tempList = resources.get(reportId + "-U");
		String username = ContextHolder.getContext().getLoginUsername();
		boolean limited = false;
		if (tempList != null && tempList.size() > 0) {
			ReportUserMember rum = null;
			for (ReportSecurityMember rsm : tempList) {
				rum = (ReportUserMember) rsm;
				if (rum.getUsername().equals(username)) {
					return true;
				}
			}
			limited = true;
		}
		tempList = resources.get(reportId + "-D");
		if (tempList != null && tempList.size() > 0) {
			ReportDeptMember rdm = null;
//			Collection<Dept> depts = ContextHolder.getContext().getLoginUser().getDepts();
//			if (depts != null) {
//				Set<String> deptIds = new HashSet<String>();
//				for (Dept dept : depts) {
//					deptIds.add(dept.getId());
//				}
//				for (ReportSecurityMember rsm : tempList) {
//					rdm = (ReportDeptMember) rsm;
//					if (deptIds.contains(rdm.getDeptId())) {
//						return true;
//					}
//				}
//			}
			limited = true;
		}
		tempList = resources.get(reportId + "-G");
		if (tempList != null && tempList.size() > 0) {
			ReportGroupMember rgm = null;
			Collection<Group> groups = null;//ContextHolder.getContext().getLoginUser().getGroups();

			if (groups != null) {
				Set<String> groupIds = new HashSet<String>();
				for (Group group : groups) {
					groupIds.add(group.getId());
				}
				for (ReportSecurityMember rsm : tempList) {
					rgm = (ReportGroupMember) rsm;
					if (groupIds.contains(rgm.getGroupId())) {
						return true;
					}
				}
			}
			limited = true;
		}
		if (limited) {
			return false;
		}
		return true;
	}
}
