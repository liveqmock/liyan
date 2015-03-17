/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.cache.aop;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.springframework.aop.AfterReturningAdvice;

import com.innofi.framework.FrameworkConstants;
import com.innofi.component.cache.service.ISysCacheConfigService;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 功能/ 模块：框架缓存模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          缓存后置刷新
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class CacheAutoFlushAfterAdvice implements AfterReturningAdvice {

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] arguments, Object target) throws Throwable {
        ISysCacheConfigService sysCacheConfigService = ContextHolder.getSpringBean("sysCacheConfigService");
        Set<String> cacheServiceBeanNames = sysCacheConfigService.getCacheServiceBeanNames();
        String targetName = target.getClass().getSimpleName().toUpperCase();
        for (String beanMappedName : cacheServiceBeanNames) {
            if (FrameworkConstants.CACHE_POLICY_CURRENT.equals(sysCacheConfigService.getSysCachePolicy(beanMappedName))) {
                if (targetName.startsWith(beanMappedName.toUpperCase())) {
                    IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
                    String invocationMethodName = method.getName();
                    if (arguments != null && arguments.length == 1) {
                        if (invocationMethodName.startsWith("remove") ||
                                invocationMethodName.startsWith("delete") ||
                                invocationMethodName.startsWith("del") ||
                                invocationMethodName.startsWith("persist")) {
                            if (arguments[0] instanceof BasePojo) {
                                codeTransfer.removeCacheObject(beanMappedName, (BasePojo) arguments[0]);
                            } else {
                            	sysCacheConfigService.reloadCacheByCacheServiceBeanName(beanMappedName);
                            }
                        } else {
                            if (arguments[0] instanceof List) {
                                List<BasePojo> pojoList = (List) arguments[0];
                                for (BasePojo basePojo : pojoList) {
                                    codeTransfer.saveOrUpdateCache(beanMappedName, basePojo);
                                }
                            } else {
                                codeTransfer.saveOrUpdateCache(beanMappedName, (BasePojo) arguments[0]);
                            }
                        }
                    } else if (arguments == null || arguments.length > 0) {
                    	sysCacheConfigService.reloadCacheByCacheServiceBeanName(beanMappedName);
                    }
                    break;
                }
            }
        }
    }

}
