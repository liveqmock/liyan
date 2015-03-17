/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.service;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.innofi.framework.dao.pagination.Page;
import com.innofi.component.report.domain.Report;
import com.innofi.component.report.domain.ReportJob;
import com.innofi.component.report.domain.ReportJobConfig;
import com.innofi.component.report.domain.ReportMember;
import com.innofi.component.report.job.ReportJobListener;

/**
 * 报表服务类,提供报表所有模块的所有API
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public interface IReportService {
	
	/**
	 * 报表文件构建
	 * 
	 * @param report
	 * @param targetType
	 * @param dataProviderName
	 * @param parameters
	 * @param outputStream
	 * @param datas
	 * @throws Exception
	 */
	public void buildFile(Report report,String targetType,String dataProviderName,Map<String,Object> parameters,OutputStream outputStream,Object datas)throws Exception;
	
	/**
	 * 检查当前登录用户是否由制定报表配置的访问权限
	 * 
	 * @param report
	 */
	public void checkAuth(Report report);
	
	/**
	 * 刷新报表权限缓存
	 */
	public void initReportSecurityData();
	
	/**
	 * 新增报表上数据
	 * 
	 * @param report
	 */
	public void saveReport(Report report);

	/**
	 * 更新报表数据
	 * 
	 * @param report
	 */
	public void updateReport(Report report);

	/**
	 * 删除报表数据
	 * 
	 * @param report
	 */
	public void deleteReport(Report report);
	
	/**
	 * 分页查询报表数据
	 * 
	 * @param parameters
	 * @param pageIndex
	 * @param pageSize
	 * @return 返回存有Report的分页对象
	 */
	public Page<Report> findReportsByCondition(Map<String, Object> parameters,int pageIndex,int pageSize);
	
	/**
	 * 根据报表ID查询报表数据
	 * 
	 * @param id
	 * @return 返回Report对象
	 */
	public Report findReportById(String id);
	
	/**
	 * 根据报表英文名称查询报表数据
	 * 
	 * @param ename
	 * @return 返回Report对象
	 */
	public Report findReportByEname(String ename);
	
	/**
	 * 保存报表成员
	 * 
	 * @param reportMember
	 */
	public void saveReportMember(ReportMember reportMember);
	
	/**
	 * 保存定时任务发送邮件接收者成员
	 * 
	 * @param memberIds 成员ID
	 * @param type 成员类型
	 * @param jobId 定时任务ID
	 * @param configId 执行类ID
	 * @param id 
	 */
	public void saveReportJobMember(List<String> memberIds,String type,String jobId,String configId,String id);
	/**
	 * 根据报表成员删除报表成员信息
	 * 
	 * @param reportMember
	 */
	public void deleteReportSecurityMember(ReportMember reportMember);
	
	/**
	 * 删除定时任务配置的邮件接收者成员
	 * @param ids
	 */
	public void deleteReportJobMember(List<String> ids);
	/**
	 * 根据条件查选报表成员信息
	 * 
	 * @param masterId
	 * @param type 
	 * @return 返回报表成员信息的集合
	 */
	public Collection<ReportMember> findReportMembers(String masterId,String type);
	
	/**
	 * 保存定时报表配置
	 * @param reportJob
	 */
	public void saveReportJob(ReportJob reportJob);
	
	/**
	 * 更新定时报表配置
	 * 
	 * @param reportJob
	 */
	public void updateReportJob(ReportJob reportJob);
	
	/**
	 * 删除定时报表配置
	 * @param reportJob
	 */
	public void deleteReportJob(ReportJob reportJob);
	
	/**
	 * 根据定时任务查找与之关联的所有报表关系
	 * 
	 * @param jobId
	 * @return 返回ReportJob的集合
	 */
	public Collection<ReportJob> findJobReports(String jobId);
	
	/**
	 * 根据定时任务ID和报表ID查询定时报表明细
	 * 
	 * @param jobId
	 * @param reportId
	 * @return 返回ReportJob
	 */
	public ReportJob findJobReport(String jobId,String reportId);
	
	/**
	 * 保存定时报表配置
	 * 
	 * @param reportJobConfig
	 */
	public void saveReportJobConfig(ReportJobConfig reportJobConfig);
	
	/**
	 * 删除定时报表配置
	 * @param reportJobConfig
	 */
	public void deleteReportJobConfig(ReportJobConfig reportJobConfig);

	/**
	 * 根据定时任务ID查询定时报表配置
	 * 
	 * @param jobId
	 * @return 返回ReportJobConfig的集合
	 */
	public Collection<ReportJobConfig> findReportJobConfigByJobId(String jobId);
	
	/**
	 * 根据定时任务ID和监听器ID查询定时任务报表配置
	 * 
	 * @param jobId
	 * @param configId
	 * @return 返回ReportJobConfig的对象
	 */
	public ReportJobConfig findReportJobConfig(String jobId,String configId);
	
	/**
	 * 获取系统定义的定时报表监听器
	 * @return 返回ReportJobListener的集合
	 */
	public Collection<ReportJobListener> findReportJobListeners();
}
