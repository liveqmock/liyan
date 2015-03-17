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
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.core.resource.ResourceManager;
import com.bstek.dorado.core.resource.ResourceManagerUtils;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;
import com.innofi.framework.datasource.DataSourceRegister;
//import com.innofi.framework.pojo.Variable;
import com.innofi.component.report.domain.Report;
import com.innofi.component.report.service.IReportService;
//import com.innofi.framework.service.VariableService;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 报表中心及报表权限维护View数据交互类
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportManagerPR implements InitializingBean{
	
   private static  ResourceManager res;
	
	
	
	/**
	 * 加载报表分组类型
	 * 
	 * @return 返回EnumObject集合
	 * @throws Exception
	 */
	@DataProvider
	public Collection findGroups()throws Exception{
//		return enumService.findEnumObjectByCategoryId(Report.REPORT_GROUP_CATEGORY);
		return null;
	}
	
	/**
	 * 加载当前应用中可使用的的数据源
	 * 
	 * @return 返回数据源的集合
	 * @throws Exception
	 */
	@DataProvider
	public Collection<Map<String,String>> findDataSources()throws Exception{
		Map<String,DataSourceRegister> maps = ContextHolder.getSpringBeanFactory().getBeansOfType(DataSourceRegister.class);
		ArrayList<Map<String,String>> dataSources = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
		for(DataSourceRegister r : maps.values()){
			map = new HashMap<String, String>();
			map.put("dataSourceName", r.getName());
			dataSources.add(map);
		}
		return dataSources;
	}
	
	/**
	 * 根据当前用户所在公司加载该公司下的素有报表信息
	 * 
	 * @param page
	 * @param criteria
	 * @throws Exception
	 */
	@DataProvider
	public void findReports(Page<Report> page, Criteria criteria)
			throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		if (criteria != null) {
			for(Criterion c: criteria.getCriterions()){
				if (c instanceof SingleValueFilterCriterion){
					SingleValueFilterCriterion fc = (SingleValueFilterCriterion)c;
					if(StringUtils.hasText(fc.getProperty()))
						parameters.put(fc.getProperty(), fc.getValue());
				}
			};
		}
		parameters.put("companyId", ContextHolder.getContext().getLoginUser().getCrtOrgCode());
		Page<Report> pg = this.reportService.findReportsByCondition(parameters, page.getPageNo(), page.getPageSize());
		page.setEntities(pg.getEntities());
		page.setEntityCount(pg.getEntityCount());
	}
	
	/**
	 * 保存报表信息，及相关报表参数信息
	 * 
	 * @param reports
	 * @throws Exception
	 */
	@DataResolver
	public void saveReports(Collection<Report> reports)throws Exception{
		for(Report report : reports){
			EntityState state = EntityUtils.getState(report);
			if(state.equals(EntityState.NEW)){
				this.reportService.saveReport(report);
				/*if(report.getVariables() != null){
					this.saveReportVariables(report.getVariables(), report.getId());
				}*/
			}else if(state.equals(EntityState.MODIFIED)){
				this.reportService.updateReport(report);
/*				if(report.getVariables() != null){
					this.saveReportVariables(report.getVariables(), report.getId());
				}*/
			}else if(state.equals(EntityState.DELETED)){
/*				Collection<Variable> variables = this.variableService.findVariable(report.getId(), Variable.MODULE_REPORT, null, null, null);
				for(Variable variable : variables){
					this.variableService.deleteVariable(variable);
				}*/
				this.reportService.deleteReport(report);
			}else{
/*				if(report.getVariables() != null){
					this.saveReportVariables(report.getVariables(), report.getId());
				}*/
			}
			
		}
	}
	
	/**
	 * 检查报表名称唯一性
	 * 
	 * @param parameters
	 * @return 若唯一返回null,否则返回提示信息
	 */
	@Expose
	public String checkReportUnique(Map<String,Object> parameters) throws Exception{
		String ename = (String) parameters.get("data");
		String id = (String) parameters.get("id");
		
		Report report = this.reportService.findReportByEname(ename);
		if(report == null)
			return null;
		if(id != null && id.equals(report.getId()))
			return null;
	
		return res.getString("SecurityClazz/reportManagerPR.nameExist");
	}
	
	/**
	 * 根据报表编号加载报表参数信息
	 * 
	 * @param reportId
	 * @return 返回Variable集合
	 * @throws Exception
	 */
	//@DataProvider
	//public Collection<Variable> findVariables(String reportId)throws Exception{
		//return null;//this.variableService.findVariable(reportId, Variable.MODULE_REPORT, null,null,null);
	//}
	
	/**
	 * 保存报表参数
	 * @param reportVariables
	 * @param reportId
	 * @throws Exception
	 */
/*	private void saveReportVariables(Collection<Variable> variables,String reportId)throws Exception{
		for(Variable var : variables){
			EntityState state = EntityUtils.getState(var);
			if(state.equals(EntityState.NEW)){
				var.setMasterId(reportId);
				var.setModule(Variable.MODULE_REPORT);
				this.variableService.saveVariable(var);
			}else if(state.equals(EntityState.MODIFIED)){
				this.variableService.updateVariable(var);
			}else if(state.equals(EntityState.DELETED)){
				this.variableService.deleteVariable(var);
			}
		}
	}*/
	
	private IReportService reportService;
	//private EnumService enumService;
	//private VariableService variableService;
	
	public IReportService getReportService() {
		return reportService;
	}
//	public EnumService getEnumService() {
//		return enumService;
//	}
	/*public VariableService getVariableService() {
		return variableService;
	}*/
	public void setReportService(IReportService reportService) {
		this.reportService = reportService;
	}
//	public void setEnumService(EnumService enumService) {
//		this.enumService = enumService;
//	}
/*	public void setVariableService(VariableService variableService) {
		this.variableService = variableService;
	}*/

	public void afterPropertiesSet() throws Exception {
		res = ResourceManagerUtils.get(ReportManagerPR.class);
		
	}
}
