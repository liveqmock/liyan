package com.innofi.component.cache.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.cache.pojo.SysCacheConfig;
import com.innofi.component.cache.service.ISysCacheConfigService;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.service.IBaseService;

/**
 * 功能/ 模块：缓存维护action
 * 
 * @author liuhuaiyang
 * @version 2.0.0 13-5-10
 *          修订历史： 日期 作者 参考 描述 北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysCacheConfigAction extends BaseActionImpl {
	@Resource
	private ISysCacheConfigService sysCacheConfigSevice;

	public IBaseService getBusinessService() {
		return sysCacheConfigSevice;
	}

	/**
	 * 查询缓存配置
	 * @param page
	 * @param parameter
	 * @throws Exception
	 */
	@DataProvider
	public void findSysCacheConfigs(Page page, Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
		if (parameter != null) {
			addPropertyFilter(propertyFilters, "cacheDesc", parameter.get("cacheDesc"), (String) parameter.get("qMcacheDesc"), true);
			addPropertyFilter(propertyFilters, "id", parameter.get("id"), (String) parameter.get("qMid"), true);
			addPropertyFilter(propertyFilters, "cacheName", parameter.get("cacheName"), (String) parameter.get("qMcacheName"), true);
			addPropertyFilter(propertyFilters, "cachePolicy", parameter.get("cachePolicy"), (String) parameter.get("qMcachePolicy"), true);
			addPropertyFilter(propertyFilters, "cacheType", parameter.get("cacheType"), (String) parameter.get("qMcacheType"), true);
			addDateRangePropertyFilter(propertyFilters, "crtDate", parameter);
			addPropertyFilter(propertyFilters, "crtOrgCode", parameter.get("crtOrgCode"), (String) parameter.get("qMcrtOrgCode"), true);
			addPropertyFilter(propertyFilters, "crtUserCode", parameter.get("crtUserCode"), (String) parameter.get("qMcrtUserCode"), true);
			addPropertyFilter(propertyFilters, "objId", parameter.get("objId"), (String) parameter.get("qMobjId"), true);
			addPropertyFilter(propertyFilters, "objName", parameter.get("objName"), (String) parameter.get("qMobjName"), true);
			addPropertyFilter(propertyFilters, "refreshFreq", parameter.get("refreshFreq"), (String) parameter.get("qMrefreshFreq"), true);
			addPropertyFilter(propertyFilters, "status", parameter.get("status"), (String) parameter.get("qMstatus"), true);
			addPropertyFilter(propertyFilters, "tableId", parameter.get("tableId"), (String) parameter.get("qMtableId"), true);
			addPropertyFilter(propertyFilters, "tableName", parameter.get("tableName"), (String) parameter.get("qMtableName"), true);
			addDateRangePropertyFilter(propertyFilters, "updDate", parameter);
			addPropertyFilter(propertyFilters, "updOrgCode", parameter.get("updOrgCode"), (String) parameter.get("qMupdOrgCode"), true);
			addPropertyFilter(propertyFilters, "updUserCode", parameter.get("updUserCode"), (String) parameter.get("qMupdUserCode"), true);
		}
		com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
		sysCacheConfigSevice.findByPage_Filters(propertyFilters, null, innofiPage);
	}

	/**
	 * 保存缓存配置
	 * @param objs
	 */
	@DataResolver
	public void saveSysCacheConfigs(Collection<SysCacheConfig> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
			SysCacheConfig sysCacheConfig = (SysCacheConfig) iterator.next();
            EntityState state = EntityUtils.getState(sysCacheConfig);
			String cacheType = sysCacheConfig.getCacheType();
			String cacheServiceName = sysCacheConfig.getObjName();
            if (EntityState.DELETED.equals(state)) {
            	sysCacheConfigSevice.delete(sysCacheConfig);
        		if(FrameworkConstants.CACHE_TYPE_TEMPORARY.equals(cacheType)) {
        			sysCacheConfigSevice.removeTemporaryCacheObject(cacheServiceName);
        		} else {
        			sysCacheConfigSevice.removeCacheObject(cacheServiceName);
        		}
            } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
            	sysCacheConfigSevice.update(sysCacheConfig);
            	sysCacheConfigSevice.reloadCacheByCacheServiceBeanName(cacheServiceName);
            } else if (EntityState.NEW.equals(state)) {
            	sysCacheConfigSevice.reloadCacheByCacheServiceBeanName(cacheServiceName);
            	sysCacheConfigSevice.save(sysCacheConfig);
            }
		}
		
		
		//加载缓存类型和缓存策略
		this.setCacheTypeAndPolicy(objs);
	} 
	
	/**
	 * 刷新缓存配置的缓存类型和缓存策略
	 * @param objs
	 */
	private void setCacheTypeAndPolicy(Collection<SysCacheConfig> objs) {
		ListOrderedMap cacheTypeMap = (ListOrderedMap) sysCacheConfigSevice.getCacheObject(FrameworkConstants.CACHE_TYPE_KEY);
		ListOrderedMap cachePolicyMap = (ListOrderedMap) sysCacheConfigSevice.getCacheObject(FrameworkConstants.CACHE_POLICY_KEY);
		if (null != cacheTypeMap && null != cachePolicyMap) {
			for (SysCacheConfig entity : objs) {
				EntityState state = EntityUtils.getState(entity);
				String cacheType = entity.getCacheType();
				String cacheServiceName = entity.getObjName();
				String cachePolicy = entity.getCachePolicy();
				if (EntityState.DELETED.equals(state)) {
					cacheTypeMap.remove(cacheServiceName);
					cachePolicyMap.remove(cacheServiceName);
				} else {
					cacheTypeMap.put(cacheServiceName, cacheType);
					cachePolicyMap.put(cacheServiceName, cachePolicy);
				}
			}
			sysCacheConfigSevice.putCacheObject(FrameworkConstants.CACHE_TYPE_KEY, cacheTypeMap);
			sysCacheConfigSevice.putCacheObject(FrameworkConstants.CACHE_POLICY_KEY, cachePolicyMap);
		}
	}
	
	/**
	 * 删除缓存
	 * @param cacheType
	 * @param cacheServiceName
	 */
	private void deleteCache(String cacheType, String cacheServiceName) {
		if(FrameworkConstants.CACHE_TYPE_TEMPORARY.equals(cacheType)) {
			sysCacheConfigSevice.removeTemporaryCacheObject(cacheServiceName);
		} else {
			sysCacheConfigSevice.removeCacheObject(cacheServiceName);
		}
	}
	
	/**
	 * 手动刷新缓存
	 * 
	 * @param cacheServiceBeanNames
	 * @return
	 */
	@Expose
	public void refressCache(Collection<String> cacheServiceBeanNames) {
		if (null != cacheServiceBeanNames) {
			Set<String> serviceSet = new HashSet<String>();
			for (String cacheServiceBeanName : cacheServiceBeanNames) {
				serviceSet.add(cacheServiceBeanName);
			}
			sysCacheConfigSevice.refreshCache(serviceSet);
		}
	}
}
