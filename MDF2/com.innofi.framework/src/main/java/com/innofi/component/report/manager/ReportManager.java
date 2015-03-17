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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.pagination.Page;

import com.innofi.component.report.domain.Report;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 报表监控中心数据库持久化服务类
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportManager extends DaoSupport {
	public static final String REPORT_TABLE="sys_reports";
	public static final String REPORT_TABLE_ID="ID_";
	public static final String REPORT_TABLE_FIELDS="ID_,ENAME_,GROUP_,DATASOURCE_NAME_,CREATE_USER_,CREATE_DATE_,UPDATE_USER_,UPDATE_DATE_,CMNT_,UPLOAD_FILE_NAME_,UPDATE_ORG_,CREATE_ORG_";
	
	/**
	 * 新增报表配置数据
	 * 
	 * @param report
	 */
	public void insertReport(Report report) {
		report.setId(new VMID().toString());
		report.setCrtDate(new Date());
		report.setCrtUserCode(ContextHolder.getContext().getLoginUsername());
		report.setCrtOrgCode(ContextHolder.getContext().getLoginUser().getCrtOrgCode());
		String uploadFile = report.getUploadFileName()+"#"+report.getJasperName();
		Object[] args = new Object[]{
				report.getEname(),
				report.getGroup(),
				report.getDataSourceName(),
				report.getCrtUserCode(),
				report.getCrtDate(),
				report.getCmnt(),
				uploadFile,
				report.getCrtOrgCode(),
				report.getId()
		};
		String sql = " insert into "+REPORT_TABLE+"(ENAME_,GROUP_,DATASOURCE_NAME_,CREATE_USER_,CREATE_DATE_,CMNT_,UPLOAD_FILE_NAME_,CREATE_ORG_,"+REPORT_TABLE_ID+") values(?,?,?,?,?,?,?,?,?)";
		this.getJdbcDao().update(sql, args);
	}

	/**
	 * 更新报表配置
	 * 
	 * @param report
	 */
	public void updateReport(Report report) {
		report.setUpdDate(new Date());
		report.setUpdUserCode(ContextHolder.getContext().getLoginUsername());
		report.setUpdOrgCode(ContextHolder.getContext().getLoginUser().getUpdOrgCode());
		String uploadFile = report.getUploadFileName()+"#"+report.getJasperName();
		String sql = " update "+REPORT_TABLE+" set ENAME_=?,GROUP_=?,DATASOURCE_NAME_=?,UPDATE_USER_=?,UPDATE_DATE_=?,CMNT_=?,UPLOAD_FILE_NAME_=?,UPDATE_ORG_=? where "+REPORT_TABLE_ID+"=?";
		Object[] args = new Object[]{
				report.getEname(),
				report.getGroup(),
				report.getDataSourceName(),
				ContextHolder.getContext().getLoginUser().getUpdOrgCode(),
				report.getUpdUserCode(),
				report.getUpdDate(),
				report.getCmnt(),
				uploadFile,
				report.getUpdOrgCode(),
				report.getId()
		};
		this.getJdbcDao().update(sql, args);
	}

	/**
	 * 删除报表配置
	 * 
	 * @param report
	 */
	public void deleteReport(Report report) {
		String sql = "delete from "+REPORT_TABLE+" where "+REPORT_TABLE_ID+"=?";
		this.getJdbcDao().update(sql, report.getId());
	}
	
	/**
	 * 根据条件分页查询报表配置数据
	 * 
	 * @param parameters
	 * @param pageIndex
	 * @param pageSize
	 * @return 返回存有Report的分页对象
	 */
	public Page<Report> findReportsByCondition(Map<String, Object> parameters,int pageIndex,int pageSize){
		String sql = " select "+REPORT_TABLE_FIELDS + " from "+REPORT_TABLE+" where 1=1 ";
		String countSql = " select count(*) from "+REPORT_TABLE+" where 1=1 ";
		ArrayList<Object> args = new ArrayList<Object>();
		String whereClause = "";
		
		if(parameters.get("ename")!=null){
			args.add("%"+parameters.get("ename")+"%");
			whereClause += " and ename_ like ?";
		}
		if(parameters.get("group")!=null){
			args.add("%"+parameters.get("group")+"%");
			whereClause += " and group_  like ?";
		}
		
		sql += whereClause;
		countSql += whereClause;
		Page<Report> pg = new Page<Report>(pageSize,pageIndex);
		this.getJdbcDao().paginationQuery(sql, countSql, args.toArray(), pg, new ReportRowMapper());
		return pg;
	}
	
	/**
	 * 根据报表ID查询报表配置
	 * 
	 * @param id
	 * @return 返回report对象
	 */
	public Report findReportById(String id){
		String sql = " select "+REPORT_TABLE_FIELDS + " from "+REPORT_TABLE+" where ID_=? ";
		List<Report> results = this.getJdbcDao().query(sql, new Object[]{id}, new ReportRowMapper());
		if(results.size()>0){
			return results.get(0);
		}
		return null;
	}
	
	/**
	 * 根据报表英文名称查询报表数据
	 * 
	 * @param ename
	 * @return 返回report对象
	 */
	public Report findReportByEname(String ename){
		String sql = " select "+REPORT_TABLE_FIELDS + " from "+REPORT_TABLE+" where ENAME_=? ";
		List<Report> results = this.getJdbcDao().query(sql, new Object[]{ename}, new ReportRowMapper());
		if(results.size()>0){
			return results.get(0);
		}
		return null;
	}
	
	
	
	/**
	 * 报表对象数据库字段映射器
	 * 
	 * @author tode.yu@bstek.com
	 * @version 1.0
	 */
	protected class ReportRowMapper implements RowMapper<Report>{
		public Report mapRow(ResultSet rs, int num) throws SQLException {
			Report report = new Report();
			String uploadFile = rs.getString("UPLOAD_FILE_NAME_");
			String[] idNames = uploadFile.split("#");
			report.setId(rs.getString("ID_"));
			report.setEname(rs.getString("ENAME_"));
			report.setGroup(rs.getString("GROUP_"));
			report.setDataSourceName(rs.getString("DATASOURCE_NAME_"));
			report.setCrtDate(rs.getDate("CREATE_DATE_"));
			report.setCrtUserCode(rs.getString("CREATE_USER_"));
			report.setUpdUserCode(rs.getString("UPDATE_USER_"));
			report.setUpdDate(rs.getDate("UPDATE_DATE_"));
			report.setCmnt(rs.getString("CMNT_"));
			report.setCrtOrgCode(rs.getString("CREATE_ORG_"));
			report.setUpdOrgCode(rs.getString("UPDATE_ORG_"));
			report.setUploadFileName(idNames[0]);
			report.setJasperName(idNames[1]);
			return report;
		}
	}
}
