/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.d7.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.innofi.component.report.domain.ReportJobConfig;
import com.innofi.component.report.domain.ReportMember;
import com.innofi.component.report.job.ReportMessageVariableRegister;
import com.innofi.component.report.service.IReportService;
import com.saxonica.bytecode.util.MessageTemplate;

/**
 * 定时任务报表生成后发送邮件配置，<br>
 * 配置邮件接收者页面数据交互服务类
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class SendReportMailUserConfigPR {
	
	/**
	 * 根据定时任务ID和监听器ID查询定时任务报表配置
	 * 
	 * @param jobId
	 * @param configId
	 * @return 返回ReportJobUserConfig集合
	 * @throws Exception
	 */
	@DataProvider
	public Collection<ReportJobUserConfig> findReportJobById(String jobId,String configId)throws Exception{
		ArrayList<ReportJobUserConfig> rjcList = new ArrayList<ReportJobUserConfig>();
		ReportJobConfig rjc =  this.reportService.findReportJobConfig(jobId,configId);
		if(rjc != null){
			ReportJobUserConfig rjuc = new ReportJobUserConfig();
			rjuc.setId(rjc.getId());
			rjuc.setJobId(rjc.getJobId());
			rjc.setConfigId(rjc.getConfigId());
//			MessageTemplateUse templateUse = this.findMessageTemplateUse(rjc.getId());
//			if(templateUse == null)
//				templateUse = new MessageTemplateUse();
//			rjuc.setMessageTemplateUse(templateUse);
//			rjcList.add(rjuc);
		}
		return rjcList;
	}
	
	/**
	 * 根据定时任务报表配置ID和成员类型加载符合条件的说有成员
	 * 
	 * @param id
	 * @param type
	 * @return 返回ReportMember集合
	 * @throws Exception
	 */
	@DataProvider
	public Collection<ReportMember> findReportMembers(String id,String type)throws Exception{
		return this.reportService.findReportMembers(id,type);
	}
	
	/**
	 * 保存定时报表配置
	 * 
	 * @param reportJobConfigs
	 * @throws Exception
	 */
	@DataResolver
	public void saveReportJobConfig(Collection<ReportJobConfig> reportJobConfigs)throws Exception{
		for(ReportJobConfig reportJobConfig : reportJobConfigs){
			EntityState state = EntityUtils.getState(reportJobConfig);
			if(state.equals(EntityState.NEW)){
				this.reportService.saveReportJobConfig(reportJobConfig);
			}else if(state.equals(EntityState.MODIFIED)){
			
			}else if(state.equals(EntityState.DELETED)){
				this.reportService.deleteReportJobConfig(reportJobConfig);
			}
			if(reportJobConfig.getUserMembers()!=null){
				this.saveJobConfigMembers(reportJobConfig.getUserMembers(),reportJobConfig.getId());
			}
			if(reportJobConfig.getDeptMembers()!=null){
				this.saveJobConfigMembers(reportJobConfig.getDeptMembers(),reportJobConfig.getId());
			}
			if(reportJobConfig.getGroupMembers()!=null){
				this.saveJobConfigMembers(reportJobConfig.getGroupMembers(),reportJobConfig.getId());
			}
		}
	}
	
	/**
	 * 保存定时任务报表配置成员信息
	 * 
	 * @param reportMembers
	 * @param jobConfigId
	 */
	private void saveJobConfigMembers(Collection<ReportMember> reportMembers,String jobConfigId){
		EntityState state = null;
		for(ReportMember rm : reportMembers){
			state = EntityUtils.getState(rm);
			if(state.equals(EntityState.NEW)){
				rm.setMasterId(jobConfigId);
				this.reportService.saveReportMember(rm);
			}else if(state.equals(EntityState.MODIFIED)){
			
			}else if(state.equals(EntityState.DELETED)){
				this.reportService.deleteReportSecurityMember(rm);
			}
		}
	}
	
	/**
	 * 删除定时任务报表成员信息
	 * 
	 * @param ids
	 */
	@Expose
	public void deleteReportMembers(List<String> ids){
//		this.reportService.deleteReportJobMember(ids);
	}
	
	/**
	 * 获取隶属与报表模块的所有消息模版
	 * 
	 * @return 返回MessageTemplate集合
	 * @throws Exception
	 */
	@DataProvider
	public Collection<MessageTemplate> findMessageTemplates()throws Exception{
		String type = ReportMessageVariableRegister.class.getName().replace(".", "_");
//		return this.messageTemplateService.findMessageTemplate(null, null, null, type);
		return null;
	}
	
	/**
	 * 查询当前配置使用的消息模版
	 * 
	 * @param jobConfigId
	 * @return 返回MessageTemplateUse
	 * @throws Exception
	 */
//	private MessageTemplateUse findMessageTemplateUse(String jobConfigId)throws Exception{
//		String module = ReportMessageVariableRegister.class.getName().replace(".", "_");
//		Collection<MessageTemplateUse> uses = this.messageTemplateService.findMessageTemplateUse(jobConfigId, null, module, null, null, null, null);
//		if(uses.size() >0)
//			return uses.iterator().next();
//		return null;
//	}
	
	/**
	 * 保存消息模版关系
	 * 
	 * @param messageTemplateUse
	 * @throws Exception
	 */
//	@Expose
//	public MessageTemplateUse saveMessageTemplateUse(MessageTemplateUse messageTemplateUse)throws Exception{
//		if(StringUtils.hasText(messageTemplateUse.getId())){
//			this.messageTemplateService.updateMessageTemplateUse(messageTemplateUse);
//		}else{
//			String module = ReportMessageVariableRegister.class.getName().replace(".", "_");
//			messageTemplateUse.setModule(module);
//			this.messageTemplateService.saveMessageTemplateUse(messageTemplateUse);
//		}
//		return messageTemplateUse;
//	}
	private IReportService reportService;
//	private MessageTemplateService messageTemplateService;
	public void setReportService(IReportService reportService) {
		this.reportService = reportService;
	}
//	public void setMessageTemplateService(
//			MessageTemplateService messageTemplateService) {
//		this.messageTemplateService = messageTemplateService;
//	}
}
