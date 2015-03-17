package com.innofi.component.cache.aop;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.innofi.framework.FrameworkConstants;
import com.innofi.component.cache.pojo.SysCacheConfig;
import com.innofi.component.cache.service.ISysCacheConfigService;
import com.innofi.framework.spring.context.ContextHolder;
/**
 * 功能/ 模块：定时刷新缓存任务
 * 
 * @author liuhuaiyang
 * @version 2.0.0 13-5-10
 *          修订历史： 日期 作者 参考 描述 北京名晟信息技术有限公司版权所有.
 */
public class RefresCacheJob {
	private Logger logger = LoggerFactory.getLogger(RefresCacheJob.class);
	public void refressCache(Map<String, String> cacheServiceMap) {
		ISysCacheConfigService sysCacheConfigService = ContextHolder.getSpringBean("sysCacheConfigService");
		if (null != cacheServiceMap) {
			String cacheServiceName = cacheServiceMap.get("cacheServiceName");
			try{
				ContextHolder.getSpringBean(cacheServiceName);
				sysCacheConfigService.reloadCacheByCacheServiceBeanName(cacheServiceName);
			}catch(Exception e) {
				logger.info("刷新缓存参数错误，正确的map参数为：{cacheServiceName：缓存服务类beanId} >>>>" + e.getMessage());
			}
		}
	}
	
	/**
	 * 刷新所有定时刷新的缓存
	 */
	public void refressAllDefindCache() {
		ISysCacheConfigService sysCacheConfigService = ContextHolder.getSpringBean("sysCacheConfigService");
		Collection<SysCacheConfig> cacheList = sysCacheConfigService.getSysCacheCofingListByPolicy(FrameworkConstants.CACHE_POLICY_DEFIND);
		Set<String> cacheServiceBeanNames = new HashSet<String>();
		if (null != cacheList && cacheList.size() > 0) {
			for (SysCacheConfig sysCacheConfig : cacheList) {
				String beanId = sysCacheConfig.getObjName();
				cacheServiceBeanNames.add(beanId);
			}
			sysCacheConfigService.refreshCache(cacheServiceBeanNames);
		}
	}
}
