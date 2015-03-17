/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.innofi.framework.dao.DaoSupport;

import com.innofi.component.report.domain.Report;
import com.innofi.component.report.domain.ReportJob;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 定时报表配置类，用于定义定时任务可以调用的报表关系
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportJobManager extends DaoSupport{

	public static final String JOB_TABLE="bdf_report_jobs";
	
	/**
	 * 保存新的定时任务报表关系
	 * 
	 * @param reportJob
	 */
	public void insertReportJob(ReportJob reportJob){
		reportJob.setCrtUserCode(ContextHolder.getContext().getLoginUsername());
		reportJob.setCrtDate(new Date());
		String sql=" insert into "+JOB_TABLE+"(REPORT_ID_,JOB_ID_,TYPE_,FILE_NAME_,CREATE_USER_,CREATE_DATE_) values(?,?,?,?,?,?)";
		Object[] args = new Object[]{
				reportJob.getReport().getId(),
				reportJob.getJobId(),
				reportJob.getType(),
				reportJob.getFileName(),
				reportJob.getCrtUserCode(),
				reportJob.getCrtUserCode()
		};
		this.getJdbcDao().update(sql, args);
	}
	
	/**
	 * 更新定时任务报表关系
	 * 
	 * @param reportJob
	 */
	public void updateReportJob(ReportJob reportJob){
		reportJob.setUpdDate(new Date());
		reportJob.setUpdUserCode(ContextHolder.getContext().getLoginUsername());
		String sql=" update "+JOB_TABLE+" set TYPE_=?,FILE_NAME_=?,UPDATE_USER_=?,UPDATE_DATE_=? where REPORT_ID_=? and JOB_ID_=?";
		Object[] args = new Object[]{
				reportJob.getType(),
				reportJob.getFileName(),
				reportJob.getUpdUserCode(),
				reportJob.getUpdDate(),
				reportJob.getReport().getId(),
				reportJob.getJobId()
		};
		this.getJdbcDao().update(sql, args);
	}
	
	/**
	 * 根据定时任务ID和报表ID删除定时任务报表关系
	 * 
	 * @param reportJob
	 */
	public void deleteReportJob(ReportJob reportJob){
		String sql = " delete from "+JOB_TABLE+" where JOB_ID_=? and REPORT_ID_=?";
		this.getJdbcDao().update(sql, reportJob.getJobId(),reportJob.getReport().getId());
	}
	
	/**
	 * 根据定时任务ID删除与之相关的所有报表关系
	 * 
	 * @param jobId
	 */
	public void deleteReportJobByJobId(String jobId){
		String sql = " delete from "+JOB_TABLE+" where JOB_ID_=?";
		this.getJdbcDao().update(sql, jobId);
	}
	
	/**
	 * 根据报表ID删除与之所有相关的定时报表关系
	 * @param reportId
	 */
	public void deleteReportJobByReportId(String reportId){
		String sql = " delete from "+JOB_TABLE+" where REPORT_ID_=?";
		this.getJdbcDao().update(sql, reportId);
	}
	
	/**
	 * 根据定时任务ID和报表ID查询出该关系的具体配置
	 * 
	 * @param jobId
	 * @param reportId
	 * @return 返回ReportJob对象
	 */
	public ReportJob findJobReport(String jobId,String reportId){
		String sql=" select r.ID_,r.ENAME_,r.DATASOURCE_NAME_,j.JOB_ID_,j.TYPE_,j.FILE_NAME_,j.CREATE_USER_,j.CREATE_DATE_,j.UPDATE_DATE_,j.UPDATE_USER_ from "+JOB_TABLE +" j left join "+ReportManager.REPORT_TABLE+" r on j.report_id_=r.id_ where j.job_id_=? and j.report_id_=?";
		List<ReportJob> list =  this.getJdbcDao().query(sql, new Object[]{jobId,reportId}, new ReportJobRowMapper());
		if(list.size()>0)
			return list.get(0);
		return null;
	}
	
	/**
	 * 根据定时任务ID查询所有符合条件的定时任务报表的关系
	 * 
	 * @param jobId
	 * @return 返回ReportJob的集合
	 */
	public Collection<ReportJob> findJobReports(String jobId){
		String sql=" select r.ID_,r.ENAME_,r.DATASOURCE_NAME_,r.UPLOAD_FILE_NAME_,j.JOB_ID_,j.TYPE_,j.FILE_NAME_,j.CREATE_USER_,j.CREATE_DATE_,j.UPDATE_DATE_,j.UPDATE_USER_ from "+JOB_TABLE +" j left join "+ReportManager.REPORT_TABLE+" r on j.report_id_=r.id_ where j.job_id_=?";
		return this.getJdbcDao().query(sql, new Object[]{jobId}, new ReportJobRowMapper());
	}

	/**
	 * 定时任务报表关系数据库实体映射服务类
	 * 
	 * @author tode.yu@bstek.com
	 * @version 1.0
	 */
	protected class ReportJobRowMapper implements RowMapper<ReportJob>{
		public ReportJob mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReportJob reportJob = new ReportJob();
			reportJob.setJobId(rs.getString("JOB_ID_"));
			reportJob.setType(rs.getString("TYPE_"));
			reportJob.setFileName(rs.getString("FILE_NAME_"));
			reportJob.setCrtUserCode(rs.getString("CREATE_USER_"));
			reportJob.setCrtDate(rs.getTimestamp("CREATE_DATE_"));
			reportJob.setUpdUserCode(rs.getString("UPDATE_USER_"));
			reportJob.setUpdDate(rs.getTimestamp("UPDATE_DATE_"));
			
			Report report = new Report();
			report.setId(rs.getString("ID_"));
			report.setEname(rs.getString("ENAME_"));
			report.setDataSourceName(rs.getString("DATASOURCE_NAME_"));
			
			String uploadFile = rs.getString("UPLOAD_FILE_NAME_");
			String[] idNames = uploadFile.split("#");
			report.setUploadFileName(idNames[0]);
			report.setJasperName(idNames[1]);
			reportJob.setReport(report);
			
			return reportJob;
		}
	}
}
