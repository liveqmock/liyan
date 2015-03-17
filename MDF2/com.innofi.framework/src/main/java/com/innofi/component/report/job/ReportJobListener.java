/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.job;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.innofi.component.report.domain.ReportJob;
import com.innofi.component.report.domain.ReportJobConfig;

/**
 * 报表生成监听器，支持自定义配置监听器，实现了该接口的Bean会在定时报表处理方式中出现,<br>
 * 报表生成后会调用afterGenerateReport方法用于执行具体的处理操作
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public abstract class ReportJobListener {
	
	private Boolean use = true;
	/**
	 * 返回具体配置页面的URL，如果返回null，则认为该Listener没有配置页面
	 * 
	 * @return 返回页面URL
	 */
	abstract public String getUrl();
	
	/**
	 * 返回该Listener的中文描述，用于在配置页面显示
	 * @return 返回该Listener的中文描述
	 */
	abstract public String getDesc();
	
	/**
	 * 生成报表后具体的执行方法，方法的参数是由调度器传入，可以直接使用
	 * 
	 * @param reportJobConfig 定时报表配置
	 * @param reportJobs 当前定时器所执行的所有报表配置
	 * @param reportFiles 当前执行器生成的所有报表文件，与报表配置一一对应
	 */
	abstract public void afterGenerateReport(ReportJobConfig reportJobConfig,Collection<ReportJob> reportJobs,ArrayList<File> reportFiles);
	
	/**
	 * 在删除该配置时,调用的后续操作
	 * 
	 * @param reportJobConfig
	 */
	abstract public void afterDeleteConfig(ReportJobConfig reportJobConfig);
	
	public void setUse(Boolean use){
		this.use = use;
	}
	
	public Boolean isUse(){
		return this.use;
	}
}
