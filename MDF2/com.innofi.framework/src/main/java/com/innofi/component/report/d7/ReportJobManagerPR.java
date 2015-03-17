/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.d7;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.innofi.component.report.domain.Report;
import com.innofi.component.report.domain.ReportJob;
import com.innofi.component.report.domain.ReportJobConfig;
import com.innofi.component.report.job.ReportJobListener;

/**
 * 定时任务报表页面数据交互服务类
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportJobManagerPR extends ReportManagerPR{
	
	public static final String JOB_REPORT_CATEGORY_ID="JOB_REPORT";
	
	/**
	 * 加载可选的定时任务生成报表后续处理的监听器配置
	 * 
	 * @return 返回监听器配置集合
	 * @throws Exception
	 */
	@DataProvider
	public Collection<Map<String,String>> loadConfigs()throws Exception{
		ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(Map<String,String> map : this.buildConfigs().values()){
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 获取系统配置好的监听器类型
	 * 
	 * @return 返回监听器类型集合
	 * @throws Exception
	 */
	private Map<String,Map<String,String>> buildConfigs()throws Exception{
		Map<String,Map<String,String>> listeners = new HashMap<String, Map<String,String>>();
		Collection<ReportJobListener> configs = this.getReportService().findReportJobListeners();
		Map<String, String> map = null;
		String id = null;
		for(ReportJobListener config : configs){
			id= "";//ClassUtils.getClassId(config.getClass());			
			map = new HashMap<String, String>();
			map.put("url",config.getUrl());
			map.put("desc", config.getDesc());
			map.put("id",id);
			listeners.put(id, map);
		}
		return listeners;
	}
	
	/**
	 * 加载用于执行定时报表的定时任务.
	 * @param parameters
	 * @return 返回Job集合
	 * @throws Exception
	 */
	@DataProvider
	public Collection<Job> findJobs(Map<String, Object> parameters)throws Exception{
		//JobQueryService service = this.jobService.createJobQueryService().group(new String[]{JOB_REPORT_CATEGORY_ID});
		return null;//service.list();
	}
	
	/**
	 * 根据定时任务ID查询与之相关的所有报表关系
	 * 
	 * @param jobId
	 * @return 返回ReportJob集合
	 * @throws Exception
	 */
	@DataProvider
	public Collection<ReportJob> findReportJobByJobId(String jobId)throws Exception{
		return this.getReportService().findJobReports(jobId);
	}
	
	/**
	 * 根据定时任务ID查询与之相关的所有定时任务配置关系
	 * 
	 * @param jobId
	 * @return 返回ReportJobConfig集合
	 * @throws Exception
	 */
	@DataProvider
	public Collection<ReportJobConfig> findReportJobConfigs(String jobId)throws Exception{
		Collection<ReportJobConfig> reportJobConfigs = this.getReportService().findReportJobConfigByJobId(jobId);
		Map<String,Map<String,String>> listeners = this.buildConfigs();
		for(ReportJobConfig config : reportJobConfigs){
			Map<String, String> listener = listeners.get(config.getConfigId());
			if(listener != null){
				config.setUrl(listener.get("url"));
				config.setDesc(listener.get("desc"));
			}
		}
		return reportJobConfigs;
	}
	
	/**
	 * 保存新增和删除的定时任务报表关系
	 * 
	 * @param jobId
	 * @param reportIds
	 * @param operate
	 * @throws Exception
	 */
	@Expose
	public void saveReportJobs(String jobId,List<String> reportIds,String operate)throws Exception{
		if(operate.equals("add")){
			ReportJob reportJob = null;
			for(String reportId: reportIds){
				reportJob = new ReportJob();
				reportJob.setJobId(jobId);
				reportJob.setReport(new Report(reportId));
				this.getReportService().saveReportJob(reportJob);
			}
		}else if(operate.equals("delete")){
			ReportJob reportJob = null;
			for(String reportId: reportIds){
				reportJob = new ReportJob();
				reportJob.setJobId(jobId);
				reportJob.setReport(new Report(reportId));
				this.getReportService().deleteReportJob(reportJob);
			}
		}
	}
	
	/**
	 * 更新定时任务报表关系
	 * 
	 * @param reportJob
	 * @throws Exception
	 */
	@DataResolver
	public void updateReportJobs(ReportJob reportJob)throws Exception{
		this.getReportService().updateReportJob(reportJob);
	}
	
	/**
	 * 保存定时任务报表配置关系
	 * 
	 * @param jobId
	 * @param configId
	 */
	@Expose
	public void saveReportJobConfig(String jobId,String configId){
		ReportJobConfig rjc = new ReportJobConfig();
		rjc.setJobId(jobId);
		rjc.setConfigId(configId);
		this.getReportService().saveReportJobConfig(rjc);
	}
	
	/**
	 * 删除定时任务报表配置关系
	 * 
	 * @param jobConfigIds
	 */
	@Expose
	public void deleteReportJobConfigs(List<String> jobConfigIds){
		ReportJobConfig rjc = null;
		for(String id : jobConfigIds){
			rjc = new ReportJobConfig();
			rjc.setId(id);
			this.getReportService().deleteReportJobConfig(rjc);
		}
	}
	
	//private JobService jobService;
//	public void setJobService(JobService jobService) {
//		this.jobService = jobService;
//	}

}
