/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.manager;

import java.rmi.dgc.VMID;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

//import com.bstek.bdf.domain.Dept;
//import com.bstek.bdf.domain.Group;
//import com.bstek.bdf.domain.User;
//import com.bstek.bdf.manager.listener.DeptManagerListener;
//import com.bstek.bdf.manager.listener.GroupManagerListener;
//import com.bstek.bdf.manager.listener.UserManagerListener;
//import com.bstek.bdf.service.GroupService;
import com.innofi.framework.dao.DaoSupport;

import com.innofi.component.report.domain.ReportMember;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 报表成员实体数据库持久化服务类，<br>
 * 用于持久化报表权限的用户成员和定时报表发送邮件的成员
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportMemberManager extends DaoSupport {//implements DeptManagerListener,UserManagerListener,GroupManagerListener{

	private static final String MEMBER_TABLE="bdf_report_members";
	
	/**
	 * 添加新的报表成员关系
	 * 
	 * @param reportMember
	 */
	public void insertReportMember(ReportMember reportMember){
		reportMember.setId(new VMID().toString());
		reportMember.setCrtDate(new Date());
		reportMember.setCrtUserCode(ContextHolder.getContext().getLoginUsername());
		String sql = " insert into "+MEMBER_TABLE+"(ID_,MASTER_ID_,TYPE_,MEMBER_ID_,CREATE_USER_,CREATE_DATE_) values(?,?,?,?,?,?)";
		Object[] args = new Object[]{
				reportMember.getId(),
				reportMember.getMasterId(),
				reportMember.getType(),
				reportMember.getMemberId(),
				reportMember.getCrtUserCode(),
				reportMember.getCrtDate()
		};
		this.getJdbcDao().update(sql, args);
	}
	
	/**
	 * 根据报表成员关系ID删除报表成员
	 * 
	 * @param reportMember
	 */
	public void deleteReportMember(ReportMember reportMember){
		String sql = " delete from "+MEMBER_TABLE+" where ID_=?";
		this.getJdbcDao().update(sql, reportMember.getId());
	}
	
	/**
	 * 根据外键ID删除与该外键相关的所有成员
	 * 
	 * @param masterId
	 */
	public void deleteReportMemberByMasterId(String masterId){
		String sql = " delete from "+MEMBER_TABLE+" where MASTER_ID_=?";
		this.getJdbcDao().update(sql, masterId);
	}
	
	/**
	 * 根据条件查选报表成员
	 * 
	 * @param masterId
	 * @param type
	 * @return 返回ReportMember的集合
	 */
	public Collection<ReportMember> findReportMembers(String masterId,String type){
		String sql = " select ID_,MASTER_ID_,TYPE_,MEMBER_ID_,CREATE_USER_,CREATE_DATE_ from " + MEMBER_TABLE + " where 1=1 ";
		String whereClause = "";
		ArrayList<Object> args = new ArrayList<Object>();
		if(masterId != null){
			whereClause += " and MASTER_ID_=? ";
			args.add(masterId);
		}
		if(type != null){
			whereClause += " and type_=? ";
			args.add(type);
		}
		sql += whereClause;
		return this.getJdbcDao().query(sql, args.toArray(), new ReportMemberRowMapper());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bstek.bdf.manager.listener.DeptManagerListener#afterDeleteDept(java.lang.String)
	 */
	public void afterDeleteDept(String deptId) throws Exception {
		this.deleteGroupMemeberByMemberId(deptId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bstek.bdf.manager.listener.UserManagerListener#afterDeleteUser(java.lang.String)
	 */
	public void afterDeleteUser(String username) {
		this.deleteGroupMemeberByMemberId(username);
	}

	/*
	 * (non-Javadoc)
	 * @see com.bstek.bdf.manager.listener.GroupManagerListener#afterDeleteGroup(java.lang.String)
	 */
	public void afterDeleteGroup(String groupId) throws Exception {
		this.deleteGroupMemeberByMemberId(groupId);
	}
	
	private void deleteGroupMemeberByMemberId(String memberId){
		String allocationMember = " delete from " + MEMBER_TABLE + " where MEMBER_ID_=?";
		this.getJdbcDao().update(allocationMember,new Object[] { memberId });
	}

	/**
	 * 报表权限成员对象数据库映射字器
	 * 
	 * @author tode.yu@bstek.com
	 * @version 1.0
	 */
	protected class ReportMemberRowMapper implements RowMapper<ReportMember>{
		public ReportMember mapRow(ResultSet rs, int num)
				throws SQLException {
			ReportMember rm = new ReportMember();
			rm.setId(rs.getString("ID_"));
			rm.setType(rs.getString("TYPE_"));
			rm.setMemberId(rs.getString("MEMBER_ID_"));
			rm.setMasterId(rs.getString("MASTER_ID_"));
			rm.setCrtUserCode(rs.getString("CREATE_USER_"));
			rm.setCrtDate(rs.getTimestamp("CREATE_DATE_"));
//			if(rm.getType().equals(ReportMember.USER_MEMBER)){
//				User user = frameworkService.findUserByUsername(rm.getMemberId());
//				if(user != null){
//					rm.setMemberName(user.getCname());
//				}
//			}else if(rm.getType().equals(ReportMember.DEPT_MEMBER)){
//				Dept dept = frameworkService.findDeptById(rm.getMemberId());
//				if(dept != null){
//					rm.setMemberName(dept.getName());
//				}
//			}else if(rm.getType().equals(ReportMember.GROUP_MEMBER)){
//				Group group = groupService.findGroupById(rm.getMemberId());
//				if(group != null){
//					rm.setMemberName(group.getName());
//				}
//			}
			return rm;
		}
	}
	
//	private FrameworkService frameworkService;
//	private GroupService groupService;
//
//	public void setFrameworkService(FrameworkService frameworkService) {
//		this.frameworkService = frameworkService;
//	}
//	public void setGroupService(GroupService groupService) {
//		this.groupService = groupService;
//	}
}
