/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.job.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.StringUtils;

import com.innofi.component.report.domain.ReportJob;
import com.innofi.component.report.domain.ReportJobConfig;
import com.innofi.component.report.jasper.JasperReportBuilder;
import com.innofi.component.report.job.ReportJobListener;
import com.innofi.component.report.service.IReportService;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 定时报表执行服务类,该类继承自IBusinessJob接口,用于执行定时报表任务.
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class DefaultReportJobService {//implements IBusinessJob {

	/**
	 * 定时器调用方法，用于生成报表
	 * 
	 * @param parameters
	 * @throws Exception
	 */
	public void generateReport(Map<String, Object> parameters) throws Exception {
		String jobId = (String) parameters.get("jobId");
		Collection<ReportJob> reportJobs = this.reportService.findJobReports(jobId);
		Collection<ReportJobConfig> reportJobConfigs = this.reportService.findReportJobConfigByJobId(jobId);
		String targetType = null;
		File reportFile = null;
		ArrayList<File> reportFiles = new ArrayList<File>();
		for (ReportJob reportJob : reportJobs) {
			targetType = reportJob.getType();
			if (targetType == null)
				targetType = "pdf";
			reportFile = this.getFile(targetType, reportJob.getFileName());
			reportFiles.add(reportFile);
			reportService.buildFile(reportJob.getReport(), targetType, null, parameters, new FileOutputStream(
					reportFile), null);
		}

		afterGenerateReportListener(reportJobs, reportJobConfigs, reportFiles);
	}

	/**
	 * 报表生成后，调用相关监听器触发监听操作
	 * 
	 * @param reportJobs
	 * @param reportJobConfigs
	 * @param reportFiles
	 */
	private void afterGenerateReportListener(Collection<ReportJob> reportJobs,
			Collection<ReportJobConfig> reportJobConfigs, ArrayList<File> reportFiles) {
		Collection<ReportJobListener> jobListeners = this.getReportJobListener();
		String listenerId = null;
		for (ReportJobListener listener : jobListeners) {
			listenerId = listener.getClass().getName().replace(".", "_");
			for (ReportJobConfig config : reportJobConfigs) {
				if (config.getConfigId().equals(listenerId)) {
					listener.afterGenerateReport(config, reportJobs, reportFiles);
				}
			}

		}
	}

	/**
	 * 获取系统定义的所有报表生成监听器
	 * 
	 * @return 返回ReportJobListener集合
	 */
	private Collection<ReportJobListener> getReportJobListener() {
		return ContextHolder.getSpringBeanFactory().getBeansOfType(ReportJobListener.class).values();
	}

	/**
	 * 创建临时报表文件
	 * 
	 * @param targetType
	 *            目标文件类型
	 * 
	 * @return 返回File对象
	 * @throws Exception
	 */
	private File getFile(String targetType, String fileName) throws Exception {
		if (!StringUtils.hasText(fileName)) {
			fileName = UUID.randomUUID().toString();
		}
		String tempFileName = fileName + "." + this.getExportFileSuffix(targetType);
		return new File(ContextHolder.getIdfTempFileStorePath() + File.separator + tempFileName);
	}

	/**
	 * 获取报表文件后缀
	 * 
	 * @param targetType
	 *            目标文件类型
	 * @return 返回文件后缀
	 */
	private String getExportFileSuffix(String targetType) {
		targetType = targetType.toUpperCase();
		if (targetType.equals(JasperReportBuilder.PRINT_TYPE_HTML)) {
			return "html";
		} else if (targetType.equals(JasperReportBuilder.PRINT_TYPE_PDF)) {
			return "pdf";
		} else if (targetType.equals(JasperReportBuilder.PRINT_TYPE_RTF)) {
			return "rtf";
		} else if (targetType.equals(JasperReportBuilder.PRINT_TYPE_EXCEL)) {
			return "xls";
		} else {
			throw new RuntimeException("尚不支持此种报表导出类型[" + targetType + "]");
		}
	}

	private IReportService reportService;

	public void setReportService(IReportService reportService) {
		this.reportService = reportService;
	}
}
