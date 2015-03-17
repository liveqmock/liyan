package com.innofi.framework.action.impl;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.entity.FilterType;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.web.DoradoContext;
import com.innofi.component.rbac.org.service.ISysOrgInfoService;
import com.innofi.component.rbac.userview.pojo.SysUserViewCriteria;
import com.innofi.component.rbac.userview.service.ISysUserViewCriteriaService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.action.IBaseAction;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.dao.hibernate.PropertyFilterUtil;
import com.innofi.component.rbac.org.pojo.SysOrgInfo;
import com.innofi.framework.service.IBaseService;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.spring.context.FrameworkContext;
import com.innofi.framework.utils.validate.Validator;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          Action接口实现类，提供了与Service交互常用的参数组织工具方法
 * @found date: 2010-11-23
 * @found time: 20:40:56
 */
public abstract class BaseActionImpl implements IBaseAction {

    public IBaseService getBusinessService() {
        return null;
    }

    public ISysUserViewCriteriaService getSysUserViewCriteriaService() {
        return ContextHolder.getSpringBean("sysUserViewCriteriaService");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#getDoradoContext()
     */
    public DoradoContext getDoradoContext() {
        return getContext().getDoradoContext();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#buildPropertyFilter(String, Object, String)
     */
    public List<PropertyFilter> buildPropertyFilter(String propertyName, Object propertyValue, String matchType) {
        return PropertyFilterUtil.buildPropertyFilter(propertyName, propertyValue, matchType);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#buildEqPropertyFilters(Map<String, Object>)
     */
    public List<PropertyFilter> buildEqPropertyFilters(Map<String, Object> parameters) {
        return PropertyFilterUtil.buildEqPropertyFilters(parameters);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#addPropertyFilter(java.util.List, String, Object, String, Boolean)
     */
    public void addPropertyFilter(List<PropertyFilter> propertyFilters, String propertyName, Object propertyValue, String matchType, Boolean overwrite) {
        PropertyFilterUtil.addPropertyFilter(propertyFilters, QueryConstants.RESTRICTION_TYPE_AND, propertyName, propertyValue, matchType, overwrite);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#buildSqlOrHqlFilterOfOrgIn(StringBuffer, String, String)
     */
    public void buildSqlOrHqlFilterOfOrgIn(StringBuffer filterBuffer, String fieldName, String orgCode) {
        ISysOrgInfoService sysOrgInfoService = ContextHolder.getSpringBean("sysOrgInfoService");
        List<SysOrgInfo> sysOrgInfos = sysOrgInfoService.getDownLevelOrgsByOrgCode(orgCode);
        List<String> orgCodes = new ArrayList<String>();
        for (SysOrgInfo sysOrgInfo : sysOrgInfos) {
            orgCodes.add(sysOrgInfo.getOrgCode());
        }

        StringBuffer fieldArgs = new StringBuffer();

        int index = 1;

        filterBuffer.append("(");
        for (String curOrgCode : orgCodes) {

            if (Validator.isEmpty(fieldArgs.toString())) {
                fieldArgs.append("'" + curOrgCode + "'");
            } else {
                fieldArgs.append(",'" + curOrgCode + "'");
            }

            if (index % 500 == 0) {
                if (index == 500) {
                    filterBuffer.append(" " + fieldName + " IN (" + fieldArgs + ")");
                } else {
                    filterBuffer.append(" OR " + fieldName + " IN (" + fieldArgs + ")");
                }
                fieldArgs.delete(0, fieldArgs.length());
            }

            index++;
        }

        if (orgCodes.size() < 500) {
            filterBuffer.append(" " + fieldName + " IN (" + fieldArgs + ")");
        }

        filterBuffer.append(")");

    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#addPropertyFilterOfOrgIn(java.util.List, String, Object)
     */
    public void addPropertyFilterOfOrgIn(List<PropertyFilter> propertyFilters, String propertyName, Object propertyValue) {
        ISysOrgInfoService sysOrgInfoService = ContextHolder.getSpringBean("sysOrgInfoService");
        List<SysOrgInfo> sysOrgInfos = sysOrgInfoService.getDownLevelOrgsByOrgCode((String) propertyValue);
        List<String> orgCodes = new ArrayList<String>();
        for (SysOrgInfo sysOrgInfo : sysOrgInfos) {
            orgCodes.add(sysOrgInfo.getOrgCode());
        }
        PropertyFilterUtil.addPropertyFilter(propertyFilters, QueryConstants.RESTRICTION_TYPE_AND, propertyName, orgCodes, QueryConstants.IN, true);
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#addPropertyFilter(java.util.List, String, Object, String, Boolean)
     */
    public void addOrPropertyFilter(List<PropertyFilter> propertyFilters, String propertyName, Object propertyValue, String matchType, Boolean overwrite) {
        PropertyFilterUtil.addPropertyFilter(propertyFilters, QueryConstants.RESTRICTION_TYPE_OR, propertyName, propertyValue, matchType, overwrite);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#addDateRangePropertyFilter(java.util.List, String, java.util.Map)
     */
    public void addDateRangePropertyFilter(List<PropertyFilter> propertyFilters, String propertyName, Map<String, Object> parameters) {
        PropertyFilterUtil.addDateRangePropertyFilter(propertyFilters, propertyName, parameters);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#removePropertyFilter(List, String)
     */
    public void removePropertyFilter(List<PropertyFilter> propertyFilters, String propertyName) {
        PropertyFilterUtil.removePropertyFilter(propertyFilters, propertyName);
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#translateDoradoPageToInnofiPage(Page page)
     */
    public com.innofi.framework.dao.pagination.Page translateDoradoPageToInnofiPage(Page doradoPage) {
        return new com.innofi.framework.dao.pagination.Page(doradoPage);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#getSpringBean(String)
     */
    public <T> T getSpringBean(String beanId) {
        return (T) ContextHolder.getSpringBean(beanId);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#getSpringBeanFactory()
     */
    public ApplicationContext getSpringBeanFactory() {
        return ContextHolder.getSpringBeanFactory();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#getRequest()
     */
    public HttpServletRequest getRequest() {
        return ContextHolder.getRequest();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#getContext()
     */
    public FrameworkContext getContext() {
        return ContextHolder.getContext();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#getResponse()
     */
    public HttpServletResponse getResponse() {
        return ContextHolder.getResponse();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#setRequestAttribute(String, Object)
     */
    public void setRequestAttribute(String key, Object obj) {
        ContextHolder.setRequestAttribute(key, obj);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#getRequestAttribute(String)
     */
    public Object getRequestAttribute(String key) {
        return ContextHolder.getRequestAttribute(key);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#getRequestParameter(String)
     */
    public String getRequestParameter(String key) {
        return ContextHolder.getRequestParameter(key);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.action.IBaseAction#checkUpdDelPermission(String, List, String)
     */
    @Expose
    public List<String> checkUpdDelPermission(String entityClass, List<String> idValues, String operationType) {
        IBaseService baseService = getBusinessService();
        List<String> noPermissionDataList = baseService.checkUpdDelPermission(entityClass, idValues, operationType);
        return noPermissionDataList;
    }


    @SuppressWarnings("unchecked")
    private Map<String, String> doUserQuerySchemas(Map parameters) {
        String viewPath = (String) parameters.get("viewPath");
        Assert.notNull(viewPath, "视图路径参数不能为空!");
        DoradoContext context = DoradoContext.getCurrent();
        Map<String, String> querySchemas = (Map<String, String>) context.getAttribute(viewPath);
        if (querySchemas == null) {
            String userId = ContextHolder.getContext().getLoginUser().getId();
            List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
            addPropertyFilter(propertyFilters, "viewPath", viewPath, QueryConstants.EQUAL, false);
            addPropertyFilter(propertyFilters, "userId", userId, QueryConstants.EQUAL, false);
            addPropertyFilter(propertyFilters, "cfgType", "2", QueryConstants.EQUAL, false);
            List<SysUserViewCriteria> sysUserViewCriterias = getSysUserViewCriteriaService().findByPropertyFilters(propertyFilters, null);
            querySchemas = new LinkedHashMap<String, String>();
            for (SysUserViewCriteria sysUserViewCriteria : sysUserViewCriterias) {
                querySchemas.put(sysUserViewCriteria.getSchemaName(), sysUserViewCriteria.getCfgContent());
            }
            context.setAttribute(viewPath, querySchemas);
        }
        return querySchemas;
    }

    @DataProvider
    public List<Record> getQuerySchemas(Map parameters) throws Exception {
        Map<String, String> querySchemaMap = doUserQuerySchemas(parameters);
        List<Record> querySchemas = new ArrayList<Record>();
        querySchemas.add(new Record());
        for (Map.Entry<String, String> entry : querySchemaMap.entrySet()) {
            Record record = new Record();
            record.put("name", entry.getKey());
            record.put("criteria", entry.getValue());
            querySchemas.add(record);
        }
        return querySchemas;
    }

    @Expose
    public void saveDataGridDisplayColumn(Map<String, String> parameters) {
        String viewPath = parameters.get("viewPath");
        Assert.notNull(viewPath, "视图路径参数不能为空!");
        String dataGridId = parameters.get("dataGridId");
        Assert.notNull(dataGridId, "数据表ID参数不能为空!");
        String tableColumns = parameters.get("tableColumns");
        Assert.notNull(tableColumns, "表格显示列参数不能为空!");
        String userId = ContextHolder.getContext().getLoginUser().getId();
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        addPropertyFilter(propertyFilters, "viewPath", viewPath, QueryConstants.EQUAL, false);
        addPropertyFilter(propertyFilters, "schemaName", dataGridId, QueryConstants.EQUAL, false);
        addPropertyFilter(propertyFilters, "userId", userId, QueryConstants.EQUAL, false);
        addPropertyFilter(propertyFilters, "cfgType", FrameworkConstants.USER_CFG_TYPE_VIEW, QueryConstants.EQUAL, false);
        getSysUserViewCriteriaService().deleteByPropertyFilters(propertyFilters);

        SysUserViewCriteria sysUserViewCriteria = new SysUserViewCriteria();
        sysUserViewCriteria.setUserId(userId);
        sysUserViewCriteria.setCfgType(FrameworkConstants.USER_CFG_TYPE_VIEW);
        sysUserViewCriteria.setViewPath(viewPath);
        sysUserViewCriteria.setSchemaName(dataGridId);
        sysUserViewCriteria.setCfgContent(tableColumns);
        getSysUserViewCriteriaService().save(sysUserViewCriteria);
    }

    @DataResolver
    public void saveQuerySchemas(List<Record> querySchemas, Map<String, String> parameters) {
        String viewPath = parameters.get("viewPath");
        Assert.notNull(viewPath, "视图路径参数不能为空!");
        String userId = ContextHolder.getContext().getLoginUser().getId();
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        addPropertyFilter(propertyFilters, "viewPath", viewPath, QueryConstants.EQUAL, false);
        addPropertyFilter(propertyFilters, "userId", userId, QueryConstants.EQUAL, false);
        addPropertyFilter(propertyFilters, "cfgType", FrameworkConstants.USER_CFG_TYPE_CRITERIA, QueryConstants.EQUAL, false);


        for (Record querySchema : EntityUtils.getIterable(querySchemas, FilterType.DELETED, Record.class)) {
            String schemaName = querySchema.getString("name");
            addPropertyFilter(propertyFilters, "schemaName", schemaName, QueryConstants.EQUAL, true);
            getSysUserViewCriteriaService().deleteByPropertyFilters(propertyFilters);
        }
        for (Record querySchema : EntityUtils.getIterable(querySchemas, FilterType.MODIFIED, Record.class)) {
            String schemaName = querySchema.getString("name");
            addPropertyFilter(propertyFilters, "schemaName", schemaName, QueryConstants.EQUAL, true);
            List<SysUserViewCriteria> sysUserViewCriterias = getSysUserViewCriteriaService().findByPropertyFilters(propertyFilters, null);
            Assert.notEmpty(sysUserViewCriterias, "未找到名称为[" + schemaName + "]的查询方案!");
            SysUserViewCriteria sysUserViewCriteria = sysUserViewCriterias.get(0);
            sysUserViewCriteria.setCfgContent(querySchema.getString("criteria"));
            getSysUserViewCriteriaService().update(sysUserViewCriteria);
        }
        for (Record querySchema : EntityUtils.getIterable(querySchemas, FilterType.NEW, Record.class)) {
            String schemaName = querySchema.getString("name");
            SysUserViewCriteria sysUserViewCriteria = new SysUserViewCriteria();
            sysUserViewCriteria.setUserId(userId);
            sysUserViewCriteria.setCfgType(FrameworkConstants.USER_CFG_TYPE_CRITERIA);
            sysUserViewCriteria.setViewPath(viewPath);
            sysUserViewCriteria.setSchemaName(schemaName);
            sysUserViewCriteria.setCfgContent(querySchema.getString("criteria"));
            getSysUserViewCriteriaService().save(sysUserViewCriteria);
        }
    }

}
