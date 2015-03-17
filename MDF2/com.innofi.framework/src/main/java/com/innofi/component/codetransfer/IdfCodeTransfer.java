/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.codetransfer;

import java.util.*;

import javax.annotation.Resource;

import com.innofi.framework.utils.validate.Validator;
import org.apache.commons.collections.map.ListOrderedMap;

import com.innofi.framework.FrameworkConstants;
import com.innofi.component.cache.ICacheService;
import com.innofi.component.cache.service.ISysCacheConfigService;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.reflect.ReflectionUtil;

/**
 * 功能/ 模块：框架基础模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          代码翻译类
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class IdfCodeTransfer {

    @Resource(name = "sysCacheConfigService")
    ISysCacheConfigService sysCacheConfigService;
    
    private StringBuffer cacheLogBuffer = new StringBuffer();
    @Resource
    private ISysCacheConfigService sysCacheConfigSevice;

    /**
     * 根据缓存服务名称获取缓存对象列表
     *
     * @param cacheServiceBeanName 缓存服务名称
     * @return Map key为codeFiled字段的值，value为缓存对象
     */
    public ListOrderedMap getCacheObjects(String cacheServiceBeanName) {
        ICacheService cacheService = ContextHolder.getSpringBean(cacheServiceBeanName);
        ListOrderedMap cacheObjects = null;
        cacheObjects = getCacheObjectsByBeanName(cacheServiceBeanName, cacheService);
        if (cacheObjects == null) {
        	sysCacheConfigService.reloadCacheByCacheServiceBeanName(cacheServiceBeanName);
            cacheObjects = getCacheObjectsByBeanName(cacheServiceBeanName, cacheService);
        }
        return cacheObjects;
    }

    /**
     * 根据缓存服务名称、代码值获取缓存对象
     *
     * @param cacheServiceBeanName 缓存服务名称
     * @param codeValue            代码值
     * @return Map key为codeFiled字段的值，value为缓存对象
     */
    public BasePojo getCacheObject(String cacheServiceBeanName, String codeValue) {
        ListOrderedMap cacheObjects = getCacheObjects(cacheServiceBeanName);
        return (BasePojo) cacheObjects.get(codeValue);
    }

    /**
     * 根据缓存服务名称、代码值获取缓存对象中文描述
     *
     * @param cacheServiceBeanName 缓存服务名称
     * @param codeValue            代码值
     * @return String 中文描述
     */
    public String getCacheObjectCnValue(String cacheServiceBeanName, String codeValue) {
        ICacheService cacheService = ContextHolder.getSpringBean(cacheServiceBeanName);
        BasePojo basePojo = getCacheObject(cacheServiceBeanName, codeValue);
        return (String) ReflectionUtil.getFieldValue(basePojo, cacheService.getCnFieldName());
    }

    /**
     * 翻译List<BasePojo>查询结果集
     *
     * @param queryResult          对象列表
     * @param cacheServiceBeanName 缓存服务名称
     * @param transFieldMapping    翻译映射字段 ， key代码字段，value存储中文字段
     */
    public void transferResult(Collection<BasePojo> queryResult, String cacheServiceBeanName, Map<String, String> transFieldMapping) {
        for (BasePojo basePojo : queryResult) {
            transferSingleObject(basePojo, cacheServiceBeanName, transFieldMapping);
        }
    }

    /**
     * 翻译Map查询结果集
     *
     * @param queryResult          对象列表
     * @param cacheServiceBeanName 缓存服务名称
     * @param transFieldMapping    翻译映射字段 ， key代码字段，value存储中文字段
     */
    public void transferMapResult(Collection<Map<String, Object>> queryResult, String cacheServiceBeanName, Map<String, String> transFieldMapping) {
        for (Map<String, Object> record : queryResult) {
            transferSingleMap(record, cacheServiceBeanName, transFieldMapping);
        }
    }

    /**
     * 翻译单一对象
     *
     * @param basePojo             对象
     * @param cacheServiceBeanName 缓存服务名称
     * @param transFieldMapping    翻译映射字段 ， key代码字段，value存储中文字段
     */
    public void transferSingleObject(BasePojo basePojo, String cacheServiceBeanName, Map<String, String> transFieldMapping) {
        ICacheService cacheService = ContextHolder.getSpringBean(cacheServiceBeanName);
        ListOrderedMap cacheObjects = getCacheObjects(cacheServiceBeanName);
        if (cacheObjects != null) {
            for (Map.Entry<String, String> entry : transFieldMapping.entrySet()) {
                String codeField = entry.getKey();
                String cnField = entry.getValue();
                Object codeValue = ReflectionUtil.getFieldValue(basePojo, codeField);
                if(Validator.isNotEmpty((String) codeValue)&&codeValue.equals("${userCode}")){
                    ReflectionUtil.setFieldValue(basePojo, cnField, "上下文用户");
                }
                BasePojo cacheObject = (BasePojo) cacheObjects.get(codeValue);
                if (cacheObject != null) {
                    String cnValue = (String) ReflectionUtil.getFieldValue(cacheObject, cacheService.getCnFieldName());
                    ReflectionUtil.setFieldValue(basePojo, cnField, cnValue);
                }
            }
        }
    }

    /**
     * 翻译单一对象
     *
     * @param record               单条记录
     * @param cacheServiceBeanName 缓存服务名称
     * @param transFieldMapping    翻译映射字段 ， key代码字段，value存储中文字段
     */
    public void transferSingleMap(Map<String, Object> record, String cacheServiceBeanName, Map<String, String> transFieldMapping) {
        ICacheService cacheService = ContextHolder.getSpringBean(cacheServiceBeanName);
        ListOrderedMap cacheObjects = getCacheObjects(cacheServiceBeanName);
        if (cacheObjects != null) {
            for (Map.Entry<String, String> entry : transFieldMapping.entrySet()) {
                String codeField = entry.getKey();
                String cnField = entry.getValue();
                Object codeValue = record.get(codeField);
                BasePojo cacheObject = (BasePojo) cacheObjects.get(codeValue);
                if (cacheObject != null) {
                    String cnValue = (String) ReflectionUtil.getFieldValue(cacheObject, cacheService.getCnFieldName());
                    record.put(cnField, cnValue);
                }
            }
        }
    }


    /**
     * 刷新单个缓存对象方法
     *
     * @param cacheServiceBeanName 缓存服务名称
     * @param basePojo             要刷新的缓存对象
     */
    public synchronized void saveOrUpdateCache(String cacheServiceBeanName, BasePojo basePojo) {
        ICacheService cacheService = ContextHolder.getSpringBean(cacheServiceBeanName);
        ListOrderedMap cacheObjects = getCacheObjects(cacheServiceBeanName);
        Object key = ReflectionUtil.getFieldValue(basePojo, cacheService.getCodeFieldName());
        cacheObjects.put(key, basePojo);
    }

    /**
     * 删除缓存对象方法
     *
     * @param cacheServiceBeanName 缓存服务名称
     */
    public void removeCache(String cacheServiceBeanName) {
    	sysCacheConfigService.removeCacheObject(cacheServiceBeanName);
    }

    /**
     * 删除缓存对象方法
     *
     * @param cacheServiceBeanName 缓存服务名称
     * @param basePojo             要删除的缓存对象
     */
    public synchronized void removeCacheObject(String cacheServiceBeanName, BasePojo basePojo) {
        ICacheService cacheService = ContextHolder.getSpringBean(cacheServiceBeanName);
        ListOrderedMap cacheObjects = getCacheObjects(cacheServiceBeanName);
        Object key = ReflectionUtil.getFieldValue(basePojo, cacheService.getCodeFieldName());
        cacheObjects.remove(key);
    }

    /**
     * 从内存中操作一个map的list,类似拼成where和disticnt的sql语句过滤list中的纪录
     *
     * @param listOrderedMap：操作的数据集对象，每个BasePojo为一条记录
     *
     * @param whereField：等于的字段名
     * @param whereValue：等于的字段值
     * @param distinctField：排重的字段
     * @return list：过滤后的结果集
     */

    public static ListOrderedMap listDistinctFilter(ListOrderedMap listOrderedMap, String whereField, String whereValue, String distinctField) {
        ListOrderedMap filterResult = new ListOrderedMap();
        HashMap<Object, BasePojo> resultsMap = new LinkedHashMap<Object, BasePojo>();

        if (listOrderedMap != null && listOrderedMap.size() > 0) {
            if (distinctField == null) {//只过滤where条件
                //_log.debug("********************只过滤where条件！***********************");
                for (int i = 0; i < listOrderedMap.size(); i++) {
                    BasePojo basePojo = (BasePojo) listOrderedMap.getValue(i);
                    if (ReflectionUtil.getFieldValue(basePojo, whereField) == null) continue;
                    if (ReflectionUtil.getFieldValue(basePojo, whereField).toString().equals(whereValue)) {
                        filterResult.put(basePojo.getId(), basePojo);
                    }
                }
            } else {
                for (int i = 0; i < listOrderedMap.size(); i++) {
                    BasePojo basePojo = (BasePojo) listOrderedMap.getValue(i);
                    if (whereField == null && whereValue == null) {//只过滤distinct条件
                        resultsMap.put(ReflectionUtil.getFieldValue(basePojo, distinctField), basePojo);
                    } else { //同时过滤
                        if (ReflectionUtil.getFieldValue(basePojo, whereField) == null) continue;
                        if (ReflectionUtil.getFieldValue(basePojo, whereField).toString().equals(whereValue)) {
                            resultsMap.put(ReflectionUtil.getFieldValue(basePojo, distinctField), basePojo);
                        }
                    }
                }

                for (Iterator it = resultsMap.keySet().iterator(); it.hasNext(); ) {
                    BasePojo basePojo = resultsMap.get(it.next());
                    filterResult.put(basePojo.getId(), basePojo);
                }
            }

        }
        return filterResult;
    }
    
    
    /**
     * 从内存中操作一个map的list,类似拼成where和disticnt的sql语句过滤list中的纪录
     *
     * @param listOrderedMap：操作的数据集对象，每个BasePojo为一条记录
     *
     * @param whereField：等于的字段名
     * @param whereValue：等于的字段值
     * @param distinctField：排重的字段
     * @return list：过滤后的结果集
     */

    public static ListOrderedMap listDistinctFilter(ListOrderedMap listOrderedMap, String whereField, String whereValue, String distinctField , String operator) {
        ListOrderedMap filterResult = new ListOrderedMap();
        HashMap<Object, BasePojo> resultsMap = new LinkedHashMap<Object, BasePojo>();

        if (listOrderedMap != null && listOrderedMap.size() > 0) {
            if (distinctField == null) {//只过滤where条件
                //_log.debug("********************只过滤where条件！***********************");
                for (int i = 0; i < listOrderedMap.size(); i++) {
                    BasePojo basePojo = (BasePojo) listOrderedMap.getValue(i);
                    if (ReflectionUtil.getFieldValue(basePojo, whereField) == null) continue;
                    if(operator==null||operator.equals(QueryConstants.EQUAL)){
                        if (ReflectionUtil.getFieldValue(basePojo, whereField).toString().equals(whereValue)) {
                        	filterResult.put(basePojo.getId(), basePojo);
                        }
                    }else if(operator.equals(QueryConstants.NOT_EQUAL)){
                        if (ReflectionUtil.getFieldValue(basePojo, whereField) == null) continue;
                        if (!ReflectionUtil.getFieldValue(basePojo, whereField).toString().equals(whereValue)) {
                        	filterResult.put(basePojo.getId(), basePojo);
                        }
                    }
                }
            } else {
                for (int i = 0; i < listOrderedMap.size(); i++) {
                    BasePojo basePojo = (BasePojo) listOrderedMap.getValue(i);
                    if (whereField == null && whereValue == null) {//只过滤distinct条件
                        resultsMap.put(ReflectionUtil.getFieldValue(basePojo, distinctField), basePojo);
                    } else { //同时过滤
                        if (ReflectionUtil.getFieldValue(basePojo, whereField) == null) continue;
                        if(operator==null||operator.equals(QueryConstants.EQUAL)){
	                        if (ReflectionUtil.getFieldValue(basePojo, whereField).toString().equals(whereValue)) {
	                            resultsMap.put(ReflectionUtil.getFieldValue(basePojo, distinctField), basePojo);
	                        }
                        }else if(operator.equals(QueryConstants.NOT_EQUAL)){
                        	if (ReflectionUtil.getFieldValue(basePojo, whereField) == null) continue;
	                        if (!ReflectionUtil.getFieldValue(basePojo, whereField).toString().equals(whereValue)) {
	                            resultsMap.put(ReflectionUtil.getFieldValue(basePojo, distinctField), basePojo);
	                        }
                        }
                    }
                }

                for (Iterator it = resultsMap.keySet().iterator(); it.hasNext(); ) {
                    BasePojo basePojo = resultsMap.get(it.next());
                    filterResult.put(basePojo.getId(), basePojo);
                }
            }

        }
        return filterResult;
    }

    private ListOrderedMap getCacheObjectsByBeanName(String cacheServiceBeanName, ICacheService cacheService) {
        ListOrderedMap cacheObjects = new ListOrderedMap();
        String cacheType = sysCacheConfigSevice.getSysCacheType(cacheServiceBeanName);
        if (FrameworkConstants.CACHE_TYPE_TEMPORARY.equals(cacheType)) {
            cacheObjects = (ListOrderedMap) sysCacheConfigService.getTemporaryCacheObject(cacheServiceBeanName);
        } else {
            cacheObjects = (ListOrderedMap) sysCacheConfigService.getCacheObject(cacheServiceBeanName);
        }
        return cacheObjects;
    }

    public ISysCacheConfigService getSysCacheConfigSevice() {
        return sysCacheConfigSevice;
    }

    public void setSysCacheConfigSevice(ISysCacheConfigService sysCacheConfigSevice) {
        this.sysCacheConfigSevice = sysCacheConfigSevice;
    }
}
