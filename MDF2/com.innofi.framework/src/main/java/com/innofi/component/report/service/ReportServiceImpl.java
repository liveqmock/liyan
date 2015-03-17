package com.innofi.component.report.service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.innofi.framework.dao.pagination.Page;
import com.innofi.component.report.domain.Report;
import com.innofi.component.report.domain.ReportJob;
import com.innofi.component.report.domain.ReportJobConfig;
import com.innofi.component.report.domain.ReportMember;
import com.innofi.component.report.exception.ReportException;
import com.innofi.component.report.jasper.JasperReportBuilder;
import com.innofi.component.report.job.ReportJobListener;
import com.innofi.component.report.manager.ReportJobConfigManager;
import com.innofi.component.report.manager.ReportJobManager;
import com.innofi.component.report.manager.ReportManager;
import com.innofi.component.report.manager.ReportMemberManager;
import com.innofi.component.report.security.ReportSecurityCenter;
import com.innofi.framework.spring.context.ContextHolder;

public class ReportServiceImpl implements IReportService{
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
	public void buildFile(Report report,String targetType,String dataProviderName,Map<String,Object> parameters,OutputStream outputStream,Object datas)throws Exception{
		if(parameters == null)
			parameters = new HashMap<String, Object>();
		//parameters.putAll(this.buildDefaultReportParameters(report,parameters));
		if(StringUtils.hasText(dataProviderName)){
			jasperReportBuilder.buildReportFile(report, targetType, outputStream, parameters, datas);
		}else{
			jasperReportBuilder.buildReportFile(report, targetType, outputStream, parameters);
		}
	}
	
	/**
	 * 构建默认配置的报表参数
	 * 
	 * @param report
	 * @return 返回报表参数的map
	 */
	/*private Map<String,Object> buildDefaultReportParameters(Report report,Map<String, Object> parameters){
		Collection<Variable> variables = this.variableService.findVariable(report.getId(), Variable.MODULE_REPORT, null, null, null);
		Map<String,Object> reportParameters =  new HashMap<String, Object>();
		for(Variable var : variables){
			if(!parameters.containsKey(var.getName())){
				VariableService variableService = new VariableServiceImpl();
				reportParameters.put(var.getName(), variableService.parseDataValue(var.getDataType(), var.getValue()));				
			}
		}
		return reportParameters;
	}*/
	
	/**
	 * 检查当前登录用户是否由制定报表配置的访问权限
	 * 
	 * @param report
	 */
	public void checkAuth(Report report){
		if(!reportSecurityCenter.checkAuth(report.getId())){
			throw new ReportException("您无权限访问当前报表[" + report.getEname() + "]");
		}
	}
	
	/**
	 * 刷新报表权限缓存
	 */
	public void initReportSecurityData(){
		this.reportSecurityCenter.initReportSecurityData();
	}
	
	/**
	 * 新增报表上数据
	 * 
	 * @param report
	 */
	public void saveReport(Report report) {
		this.reportManager.insertReport(report);
	}

	/**
	 * 更新报表数据
	 * 
	 * @param report
	 */
	public void updateReport(Report report) {
		this.reportManager.updateReport(report);
	}

	/**
	 * 删除报表数据
	 * 
	 * @param report
	 */
	public void deleteReport(Report report) {
		this.reportJobManager.deleteReportJobByReportId(report.getId());
		this.reportMemberManager.deleteReportMemberByMasterId(report.getId());
		this.reportManager.deleteReport(report);
	}
	
	/**
	 * 分页查询报表数据
	 * 
	 * @param parameters
	 * @param pageIndex
	 * @param pageSize
	 * @return 返回存有Report的分页对象
	 */
	public Page<Report> findReportsByCondition(Map<String, Object> parameters,int pageIndex,int pageSize){
		return this.reportManager.findReportsByCondition(parameters, pageIndex, pageSize);
	}
	
	/**
	 * 根据报表ID查询报表数据
	 * 
	 * @param id
	 * @return 返回Report对象
	 */
	public Report findReportById(String id){
		return this.reportManager.findReportById(id);
	}
	
	/**
	 * 根据报表英文名称查询报表数据
	 * 
	 * @param ename
	 * @return 返回Report对象
	 */
	public Report findReportByEname(String ename){
		return this.reportManager.findReportByEname(ename);
	}
	
	/**
	 * 保存报表成员
	 * 
	 * @param reportMember
	 */
	public void saveReportMember(ReportMember reportMember){
		this.reportMemberManager.insertReportMember(reportMember);
	}
	
	/**
	 * 保存定时任务发送邮件接收者成员
	 * 
	 * @param memberIds 成员ID
	 * @param type 成员类型
	 * @param jobId 定时任务ID
	 * @param configId 执行类ID
	 * @param id 
	 */
	public void saveReportJobMember(List<String> memberIds,String type,String jobId,String configId,String id){
		if(!StringUtils.hasText(id)){
			ReportJobConfig rjc = new ReportJobConfig();
			rjc.setConfigId(configId);
			rjc.setJobId(jobId);
			this.reportJobConfigManager.insertReportJobConfig(rjc);
			id = rjc.getId();
		}
		ReportMember rm = null;
		for(String memberId : memberIds){
			rm = new ReportMember();
			rm.setMemberId(memberId);
			rm.setType(type);
			rm.setMasterId(id);
			this.reportMemberManager.insertReportMember(rm);
		}
	}
	
	/**
	 * 根据报表成员删除报表成员信息
	 * 
	 * @param reportMember
	 */
	public void deleteReportSecurityMember(ReportMember reportMember){
		this.reportMemberManager.deleteReportMember(reportMember);
	}
	
	/**
	 * 删除定时任务配置的邮件接收者成员
	 * @param ids
	 */
	public void deleteReportJobMember(List<String> ids){
		ReportMember rm = null;
		for(String id : ids){
			rm = new ReportMember();
			rm.setId(id);
			this.reportMemberManager.deleteReportMember(rm);
		}
	}
	/**
	 * 根据条件查选报表成员信息
	 * 
	 * @param masterId
	 * @param type 
	 * @return 返回报表成员信息的集合
	 */
	public Collection<ReportMember> findReportMembers(String masterId,String type){
		return this.reportMemberManager.findReportMembers(masterId,type);
	}
	
	/**
	 * 保存定时报表配置
	 * @param reportJob
	 */
	public void saveReportJob(ReportJob reportJob){
		this.reportJobManager.insertReportJob(reportJob);
	}
	
	/**
	 * 更新定时报表配置
	 * 
	 * @param reportJob
	 */
	public void updateReportJob(ReportJob reportJob){
		this.reportJobManager.updateReportJob(reportJob);
	}
	
	/**
	 * 删除定时报表配置
	 * @param reportJob
	 */
	public void deleteReportJob(ReportJob reportJob){
		// Todo
		Collection<ReportJobConfig> rjcs = this.findReportJobConfigByJobId(reportJob.getId());
		for(ReportJobConfig rjc : rjcs){
			this.deleteReportJobConfig(rjc);
		}
		this.reportJobManager.deleteReportJob(reportJob);
	}
	
	/**
	 * 根据定时任务查找与之关联的所有报表关系
	 * 
	 * @param jobId
	 * @return 返回ReportJob的集合
	 */
	public Collection<ReportJob> findJobReports(String jobId){
		return this.reportJobManager.findJobReports(jobId);
	}
	
	/**
	 * 根据定时任务ID和报表ID查询定时报表明细
	 * 
	 * @param jobId
	 * @param reportId
	 * @return 返回ReportJob
	 */
	public ReportJob findJobReport(String jobId,String reportId){
		return this.reportJobManager.findJobReport(jobId, reportId);
	}
	
	/**
	 * 保存定时报表配置
	 * 
	 * @param reportJobConfig
	 */
	public void saveReportJobConfig(ReportJobConfig reportJobConfig){
		this.reportJobConfigManager.insertReportJobConfig(reportJobConfig);
	}
	
	/**
	 * 删除定时报表配置
	 * @param reportJobConfig
	 */
	public void deleteReportJobConfig(ReportJobConfig reportJobConfig){
		this.reportJobConfigManager.deleteReportJobConfig(reportJobConfig);
		Collection<ReportJobListener> jobListeners = this.findReportJobListeners();
		for(ReportJobListener listener : jobListeners){
			listener.afterDeleteConfig(reportJobConfig);
		}
	}

	/**
	 * 根据定时任务ID查询定时报表配置
	 * 
	 * @param jobId
	 * @return 返回ReportJobConfig的集合
	 */
	public Collection<ReportJobConfig> findReportJobConfigByJobId(String jobId){
		return this.reportJobConfigManager.findReportJobConfigByJobId(jobId);
	}
	
	/**
	 * 根据定时任务ID和监听器ID查询定时任务报表配置
	 * 
	 * @param jobId
	 * @param configId
	 * @return 返回ReportJobConfig的对象
	 */
	public ReportJobConfig findReportJobConfig(String jobId,String configId){
		return this.reportJobConfigManager.findReportJobConfig(jobId,configId);
	}
	
	/**
	 * 获取系统定义的定时报表监听器
	 * @return 返回ReportJobListener的集合
	 */
	public Collection<ReportJobListener> findReportJobListeners(){
		 Collection<ReportJobListener> configs = ContextHolder.getSpringBeanFactory().getBeansOfType(ReportJobListener.class).values();
		ArrayList<ReportJobListener> listeners  = new ArrayList<ReportJobListener>();
		for(ReportJobListener listener : configs){
			if(listener.isUse())
				listeners.add(listener);
		}
		return listeners;
	}
	
	private ReportManager reportManager;
	private ReportMemberManager reportMemberManager;
	private ReportJobManager reportJobManager;
	private ReportJobConfigManager reportJobConfigManager;
	private ReportSecurityCenter reportSecurityCenter;
	private JasperReportBuilder jasperReportBuilder;

	public void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}

	public void setReportMemberManager(ReportMemberManager reportMemberManager) {
		this.reportMemberManager = reportMemberManager;
	}

	public void setReportJobManager(ReportJobManager reportJobManager) {
		this.reportJobManager = reportJobManager;
	}

	public void setReportJobConfigManager(ReportJobConfigManager reportJobConfigManager) {
		this.reportJobConfigManager = reportJobConfigManager;
	}

	public void setReportSecurityCenter(ReportSecurityCenter reportSecurityCenter) {
		this.reportSecurityCenter = reportSecurityCenter;
	}

	public void setJasperReportBuilder(JasperReportBuilder jasperReportBuilder) {
		this.jasperReportBuilder = jasperReportBuilder;
	}

}
