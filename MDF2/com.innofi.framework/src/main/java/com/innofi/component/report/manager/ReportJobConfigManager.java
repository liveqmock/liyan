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
import java.util.Collection;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.innofi.framework.dao.DaoSupport;

import com.innofi.component.report.domain.ReportJobConfig;

/**
 * 定时报表的监听器配置数据库访问服务类，用于定义定时任务执行后调用的监听器信息
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportJobConfigManager extends DaoSupport{

	public static final String CONFIG_TABLE="bdf_report_job_configs";
	public static final String CONFIG_TABLE_FIELDS="ID_,JOB_ID_,CONFIG_ID_";
	
	
	/**
	 * 保存可调用的新的可调用监听器
	 * 
	 * @param reportJobConfig
	 */
	public void insertReportJobConfig(ReportJobConfig reportJobConfig){
		reportJobConfig.setId(new VMID().toString());
		Object[] args = new Object[]{
				reportJobConfig.getId(),
				reportJobConfig.getJobId(),
				reportJobConfig.getConfigId()
		};
		String sql = " insert into "+CONFIG_TABLE+"(ID_,JOB_ID_,CONFIG_ID_) values(?,?,?)";
		this.getJdbcDao().update(sql, args);
	}
	
	/**
	 * 根据关系ID删除定时任务和监听器关系
	 * 
	 * @param reportJobConfig
	 */
	public void deleteReportJobConfig(ReportJobConfig reportJobConfig){
		String sql = " delete from "+CONFIG_TABLE +" where ID_=?";
		this.getJdbcDao().update(sql, reportJobConfig.getId());
	}
	
	/**
	 * 根据定时任务ID查询与之关联的所有监听器配置
	 * 
	 * @param jobId 定时任务ID
	 * @return 返回ReportJobConfig的集合
	 */
	public Collection<ReportJobConfig> findReportJobConfigByJobId(String jobId){
		String sql = "select "+CONFIG_TABLE_FIELDS+" from "+CONFIG_TABLE+" where JOB_ID_=?";
		return this.getJdbcDao().query(sql, new Object[]{jobId}, new ReportJobConfigRowMapper());
	}
	
	/**
	 * 根据定时任务ID和监听器配置ID查询该任务监听关系
	 * 
	 * @param jobId 定时任务ID
	 * @param configId 监听器ID
	 * @return 返回ReportJobConfig对象
	 */
	public ReportJobConfig findReportJobConfig(String jobId,String configId){
		String sql = "select "+CONFIG_TABLE_FIELDS+" from "+CONFIG_TABLE+" where JOB_ID_=? AND CONFIG_ID_=?";
		List<ReportJobConfig> list = this.getJdbcDao().query(sql, new Object[]{jobId,configId}, new ReportJobConfigRowMapper());
		if(list.size()>0)
			return list.get(0);
		return null;
	}
	
	/**
	 * 定时任务监听器数据库映射服务类
	 * 
	 * @author tode.yu@bstek.com
	 * @version 1.0
	 */
	protected class ReportJobConfigRowMapper implements RowMapper<ReportJobConfig>{
		public ReportJobConfig mapRow(ResultSet rs,int num)throws SQLException{
			ReportJobConfig rv = new ReportJobConfig();
			rv.setId(rs.getString("ID_"));
			rv.setJobId(rs.getString("JOB_ID_"));
			rv.setConfigId(rs.getString("CONFIG_ID_"));
			return rv;
		}
	}
}
