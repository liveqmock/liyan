package com.innofi.component.webservice.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.component.webservice.pojo.SysServiceBeanmethodConfig;
import com.innofi.component.webservice.service.ISysServiceBeanmethodConfigService;

/**
 * 功能/ 模块：webservice
 * 
 * @author liuhuaiyang huaiyang.liu@innofi.com.cn
 * @version 2.0.0 13-5-10
 *          为dorado7界面维护操作的提供支持，实现SysServiceBeanmethodConfig对象信息的加载与保存操作 
 *          修订历史：  日期 作者 参考 描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysServiceBeanmethodConfigAction extends BaseActionImpl {
	@Resource
	private ISysServiceBeanmethodConfigService sysServiceBeanmethodConfigSevice;

	public ISysServiceBeanmethodConfigService getBusinessService() {
		return sysServiceBeanmethodConfigSevice;
	}

	/**
	 * 查询service bean的方法
	 * @param page
	 * @param parameter
	 * @throws Exception
	 */
	@DataProvider
	public void findSysServiceBeanmethodConfigs(Page page,
			Map<String, Object> parameter) throws Exception {

		List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

		if (parameter != null) {
			addDateRangePropertyFilter(propertyFilters, "crtDate", parameter);
			addPropertyFilter(propertyFilters, "crtOrgCode",
					parameter.get("crtOrgCode"),
					(String) parameter.get("qMcrtOrgCode"), true);
			addPropertyFilter(propertyFilters, "crtUserCode",
					parameter.get("crtUserCode"),
					(String) parameter.get("qMcrtUserCode"), true);
			addPropertyFilter(propertyFilters, "isauth",
					parameter.get("isauth"),
					(String) parameter.get("qMisauth"), true);
			addPropertyFilter(propertyFilters, "id", parameter.get("id"),
					(String) parameter.get("qMid"), true);
			addPropertyFilter(propertyFilters, "methodName",
					parameter.get("methodName"),
					(String) parameter.get("qMmethodName"), true);
			addPropertyFilter(propertyFilters, "serviceId",
					parameter.get("serviceId"),
					(String) parameter.get("qMserviceId"), true);
			addDateRangePropertyFilter(propertyFilters, "updDate", parameter);
			addPropertyFilter(propertyFilters, "updOrgCode",
					parameter.get("updOrgCode"),
					(String) parameter.get("qMupdOrgCode"), true);
			addPropertyFilter(propertyFilters, "updUserCode",
					parameter.get("updUserCode"),
					(String) parameter.get("qMupdUserCode"), true);
		}

		com.innofi.framework.dao.pagination.Page innofiPage = super
				.translateDoradoPageToInnofiPage(page);

		sysServiceBeanmethodConfigSevice.findByPage_Filters(propertyFilters,
				null, innofiPage);

	}

	/**
	 * 根据服务名称，查询方法列表
	 * @param serviceId
	 * @return
	 */
	@DataProvider
	public Collection<SysServiceBeanmethodConfig> findmethodConfigByServcieId(
			String serviceId) {
		if (StringUtils.hasText(serviceId)) {
			return sysServiceBeanmethodConfigSevice.findByProperty("serviceId",
					serviceId, QueryConstants.EQUAL);
		}
		return null;
	}

	/**
	 * 保存方法配置
	 * @param objs
	 */
	@DataResolver
	public void saveSysServiceBeanmethodConfigs(Collection<SysServiceBeanmethodConfig> objs) {

		for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
			SysServiceBeanmethodConfig sysServiceBeanmethodConfig = (SysServiceBeanmethodConfig) iterator.next();
            EntityState state = EntityUtils.getState(sysServiceBeanmethodConfig);
            if (EntityState.DELETED.equals(state)) {
            	sysServiceBeanmethodConfigSevice.delete(sysServiceBeanmethodConfig);
            } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
            	sysServiceBeanmethodConfigSevice.update(sysServiceBeanmethodConfig);
            } else if (EntityState.NEW.equals(state)) {
            	sysServiceBeanmethodConfigSevice.save(sysServiceBeanmethodConfig);
            }
		}
	}
	
	/**
	 * 根据服务id删除方法配置
	 * @param serviceId
	 */
	public void deleteMethodConfigsByServieId(String serviceId){
		if(StringUtils.hasText(serviceId)) {
			SysServiceBeanmethodConfig method = new SysServiceBeanmethodConfig();
			method.setServiceId(serviceId);
			 sysServiceBeanmethodConfigSevice.delete(method);
		}
	}
}
