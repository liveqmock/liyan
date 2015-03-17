package com.innofi.framework.service.impl;


import java.io.Serializable;
import java.util.*;

/*import com.innofi.component.sie.pojo.WfProcess;
import com.innofi.component.sie.service.IWfProcessService;*/
import com.innofi.framework.dao.impl.EntityOperation;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.*;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.transform.Transformers;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.web.DoradoContext;
import com.innofi.component.metadata.field.service.IMdFieldService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.cache.service.ISysCacheConfigService;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.dao.hibernate.PropertyFilterUtil;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.pojo.metadata.MdField;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.component.sie.pojo.WfProcess;
import com.innofi.framework.service.IBaseService;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.spring.context.FrameworkContext;
import com.innofi.framework.test.LocalInitSpringContextHelper;
import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.reflect.ReflectionUtil;
import com.innofi.framework.utils.validate.Validator;


/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          业务服务层基类实现
 * @found date: 2010-11-23
 * @found time: 20:40:56
 */
public abstract class BaseServiceImpl<T extends BasePojo, PK extends Serializable> implements IBaseService<T, PK> {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    
    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#changeDaoSupportDataSource(String)
     */
    public void changeDaoSupportDataSource(String dataSourceName) {
    	getDaoSupport().changeDataSource(dataSourceName);
    }
    
    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#checkEntityExist(String, String)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public boolean checkEntityExist(String propertyName, String propertyValue) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        addPropertyFilter(filters, propertyName, propertyValue, QueryConstants.EQUAL, true);
        long count = countByPropertyFilters(filters);
        if (count > 0) return true;
        return false;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#getPropertyValueString(java.util.Collection, String, String)
     */
    public String getPropertyValueString(Collection<T> entityList, String propertyName, String separator) {
        String sep = separator;
        if (Validator.isNull(sep)) sep = ",";

        if (entityList == null || entityList.size() == 0) return "";

        StringBuffer propertyValueString = new StringBuffer();
        for (BasePojo pojo : entityList) {
            String propertyValue = ReflectionUtil.getFieldValue(pojo, propertyName).toString();
            propertyValueString.append("'" + propertyValue + "'" + sep);
        }
        propertyValueString.setCharAt(propertyValueString.length() - 1, ' ');
        return propertyName.toString();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#getPropertyValueList(java.util.Collection, String)
     */
    public List<Object> getPropertyValueList(Collection<T> entityList, String propertyName) {
        List<Object> propertyValueList = new ArrayList<Object>();
        for (Object obj : entityList) {
            Object propertyValue = ReflectionUtil.getFieldValue(obj, propertyName);
            propertyValueList.add(propertyValue);
        }
        return propertyValueList;
    }

    /**
     * @see com.innofi.framework.service.IBaseService#save(T)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void save(T entity) {
        if (!LocalInitSpringContextHelper.isLocalRun()) {//判断是否本地运行，如果为本地运行则不进行crtDate、crtUserCode、crtOrgCode的设置
            setEntityCrtInfo(entity);
            getDaoSupport().getHibernateDao().save(entity);
            /* checkTableIsSaveClosed();                    //判断当前对象是否关闭了持久化操作， todo 此处逻辑修改至拦截器当中

           Object object = ReflectionUtil.getFieldValue(entity, getIdPropertyName()); todo 此段逻辑可能存在问题，如果开发人员手工设置对象Id值则会将其设置的值冲掉,系统默认使用UUID或者自动生成序列方式产生对象主键
            if (object != null && object instanceof String) {
                if (Validator.isEmpty((String) object)) {
                    ReflectionUtil.setFieldValue(entity, getIdPropertyName(), null);
                }
            }*/
        }
        if (entity.getTreeStruct()) {                   //判断实体对象是否树型结构，生成seq字段
            generatorSeq(entity);
            getDaoSupport().getHibernateDao().flush();
        }


        /*MdTable mdTable = getMdTable(getEntityType());                                 todo 元数据及工作流逻辑
        if (mdTable == null || null == mdTable.getIsMaintain() || mdTable.getIsMaintain().equals(FrameworkConstants.COMM_N)) {
            getDaoSupport().getHibernateDao().save(entity);
            getDaoSupport().getHibernateDao().flush();
        } else {
            getDaoSupport().saveDynamicPojo(getEntityType(), entity);
        }

        IWfProcessService wfProcessService = getSpringBean("wfProcessService");
        if (getEntityType().getName().indexOf("SysLog") == -1) {
            wfProcessService.saveWfProcess(getEntityType(), entity.getId());
        }*/
    }

    /**
     * @see com.innofi.framework.service.IBaseService#saveForBranch(List)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void saveForBranch(List<T> entityList) {
        for (T entity : entityList) {
            if (!LocalInitSpringContextHelper.isLocalRun()) {
                setEntityCrtInfo(entity);
                getDaoSupport().getHibernateDao().save(entity);
            }
            if (entity.getTreeStruct()) {
                generatorSeq(entity);
            }
        }
        getDaoSupport().getHibernateDao().flush();
    }

    /**
     * @see com.innofi.framework.service.IBaseService#update(T)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void update(T entity) {
        if (!LocalInitSpringContextHelper.isLocalRun()) {
            setUpdateInfo(entity);
        }
        getDaoSupport().getHibernateDao().saveOrUpdate(entity);
        getDaoSupport().getHibernateDao().flush();
        /*checkTableIsSaveClosed();
        判断当前对象是否关闭了持久化操作， todo  保存历史表  此处逻辑修改至拦截器当中



        if (entity.getTreeStruct()) {
            generatorSeq(entity);

        }
        T oldEntity = (T) getDaoSupport().getHibernateDao().get(getEntityType(), entity.getId());//保存历史表
        saveInfoToHistory("update", entity, oldEntity);
                MdTable mdTable = getMdTable(getEntityType());
        if (null == mdTable || null == mdTable.getIsMaintain() || mdTable.getIsMaintain().equals(FrameworkConstants.COMM_N)) {
            getDaoSupport().getHibernateDao().update(entity);
            getDaoSupport().getHibernateDao().flush();
        } else {
            getDaoSupport().updateDynamicPojo(getEntityType(), entity);
        }

         */

    }

    /**
     * @see com.innofi.framework.service.IBaseService#updateForBranch(List<T>)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void updateForBranch(List<T> entityList) {
        for (T entity : entityList) {
            getDaoSupport().getHibernateDao().saveOrUpdate(entity);
        }
        getDaoSupport().getHibernateDao().flush();

        /*if (!LocalInitSpringContextHelper.isLocalRun()) {
            SysUser user = ContextHolder.getContext().getLoginUser();
            checkTableIsSaveClosed();
            entity.setUpdDate(new Date());                                  //设置更新日期
            entity.setUpdUserCode(user.getUserCode());                      //设置更新用户
            entity.setUpdOrgCode(user.getOrgCode());                        //设置更新机构
            T oldEntity = (T) getDaoSupport().getHibernateDao().get(getEntityType(), entity.getId());//保存历史表
            saveInfoToHistory("update", entity, oldEntity);
        }

        if (null == mdTable.getIsMaintain() || mdTable.getIsMaintain().equals(FrameworkConstants.COMM_N)) {
            getDaoSupport().getHibernateDao().update(entity);
        } else {
            getDaoSupport().updateDynamicPojo(getEntityType(), entity);
        }

        if (entity.getTreeStruct()) {
            generatorSeq(entity);
        }
        if (null == mdTable.getIsMaintain() || mdTable.getIsMaintain().equals(FrameworkConstants.COMM_N)) {
            getDaoSupport().getHibernateDao().flush();
        }*/
    }

    /**
     * @see com.innofi.framework.service.IBaseService#delete(T)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void delete(T entity) {
        //checkTableIsSaveClosed();            todo
        //saveInfoToHistory("delete", entity, entity);   todo
/*        IWfProcessService wfProcessService = getSpringBean("wfProcessService");
        wfProcessService.deleteWfProcess(this.getEntityType().getName(), entity.getId());*/
        getDaoSupport().getHibernateDao().delete(entity);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#delete(PK)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void delete(PK id) {
        T entity = getDaoSupport().getHibernateDao().get(getEntityType(), id);
        delete(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public T get(PK id) {
        return getDaoSupport().getHibernateDao().get(getEntityType(), id);
    }


    /*
     * 设置记录的创建用户、创建时间、创建机构
     * @param entity 实体对象
     */
    private void setEntityCrtInfo(T entity) {
        SysUser user = ContextHolder.getContext().getLoginUser();
        entity.setCrtDate(new Date());                                  //设置创建日期
        entity.setCrtUserCode(user.getUserCode());                      //设置创建用户
        entity.setCrtUserName(user.getUserName());                      //设置创建用户姓名
        entity.setCrtOrgCode(user.getOrgCode());                        //设置创建机构
        entity.setCrtOrgName(user.getOrgName());                        //设置创建机构名称
    }

    /*
     * 设置记录的更新用户、更新时间、更新机构
     * @param entity 实体对象
     */
    private void setUpdateInfo(T entity) {
        SysUser user = ContextHolder.getContext().getLoginUser();
        entity.setUpdDate(new Date());                                  //设置更新日期
        entity.setUpdUserCode(user.getUserCode());                      //设置更新用户
        entity.setUpdUserName(user.getUserName());                      //设置更新用户姓名
        entity.setUpdOrgCode(user.getOrgCode());                        //设置更新机构
        entity.setUpdOrgName(user.getOrgName());                        //设置更新机构名称
    }

    /**
     * 保存简单的Class小写名称和PersistenceClass映射
     */
    protected final static Map<String, PersistentClass> entitySimpleNamePersistenceClassMapping = new HashMap<String, PersistentClass>();
    /**
     * 保存简单的大写表名名称和简单的CLASS小写名称
     */
    protected final static Map<String, String> tableNameEntityMapping = new HashMap<String, String>();

    /**
     * 业务实体状态字段名称
     */
    protected String statusFieldName = "status";

    /**
     * 业务状态字段名称
     */
    protected String nodeFieldName = "nodeName";


    private StringBuffer script = new StringBuffer();

    public BaseServiceImpl() {
        DaoSupport daoSupport = getDaoSupport();
        if (Validator.isNotNull(daoSupport) && entitySimpleNamePersistenceClassMapping.size() == 0) {
            Iterator<PersistentClass> persistentClassIterator = getDaoSupport().getHibernateDao().getConfiguration().getClassMappings();
            while (persistentClassIterator.hasNext()) {
                PersistentClass persistentClass = persistentClassIterator.next();
                if (persistentClass.getClassName() != null) {
                    entitySimpleNamePersistenceClassMapping.put(persistentClass.getClassName().substring(persistentClass.getClassName().lastIndexOf(".") + 1), persistentClass);
                    tableNameEntityMapping.put(persistentClass.getTable().getName().toUpperCase(), persistentClass.getClassName().substring(persistentClass.getClassName().lastIndexOf(".") + 1));
                } else {
                    ConsoleUtil.info("class name is null [" + persistentClass.getEntityName() + "]");
                }
            }
            script.append("dorado.MessageBox.alert('操作失败，系统已关闭此表的新增、修改、删除功能,如有疑问请与系统管理员联系！');");
        }
    }


    /**
     * SEQ分隔符
     */
    public static String SEQ_SEPARATOR = ".";


    public List<T> loadDynamicProperty(Collection<T> entityList) {

        List<T> result = new ArrayList();
        PersistentClass persistentClass = entitySimpleNamePersistenceClassMapping.get(getEntityType().getSimpleName());
        String tableName = persistentClass.getTable().getName();
        IMdFieldService mdFieldService = getSpringBean("mdFieldService");
        List<MdField> mdFields = mdFieldService.findLastVersionMdFieldsByTableName(tableName.toUpperCase());
        String ids = "";
        if (entityList.size() > 0) {
            ids = getPropertyValueString(entityList, getDaoSupport().getHibernateDao().getIdPropertyName(this.getEntityType()), ",");
        } else {
            return result;
        }
        Map<String, String> columnAttributeMapping = new HashMap<String, String>();

        StringBuffer querySql = new StringBuffer();
        querySql.append("SELECT ");

        StringBuffer whereSql = new StringBuffer();

        MdField pkField = null;

        for (MdField mdField : mdFields) {
            if (mdField.getIsExtend().equals(FrameworkConstants.COMM_Y)) {
                querySql.append(" " + mdField.getFieldName() + " " + mdField.getEntityAttriName() + ",");
                columnAttributeMapping.put(mdField.getFieldName().toUpperCase(), mdField.getEntityAttriName());
            }
            if (mdField.getIsPk() != null && mdField.getIsPk().equals(FrameworkConstants.COMM_Y)) {
                pkField = mdField;
                querySql.append(" " + mdField.getFieldName() + " " + mdField.getEntityAttriName() + ",");
                whereSql.append(" WHERE " + mdField.getFieldName() + " IN (" + ids + ")");
            }
        }
        querySql.setCharAt(querySql.length() - 1, ' ');
        querySql.append("FROM ");
        querySql.append(tableName);
        querySql.append(whereSql.toString());

        List<Map<String, Object>> records = getDaoSupport().getJdbcDao().queryForList(querySql.toString());

        Map<String, Map<String, Object>> idAndRecordsMapping = new HashMap<String, Map<String, Object>>();

        for (Map<String, Object> record : records) {
            idAndRecordsMapping.put((String) record.get(pkField.getEntityAttriName()), record);
        }

        for (T entity : entityList) {
            Map<String, Object> record = idAndRecordsMapping.get(entity.getId());
            try {
                entity = EntityUtils.toEntity(entity);
                for (Map.Entry<String, Object> entry : record.entrySet()) {
                    if (entry.getKey().equals(pkField.getFieldName().toUpperCase())) continue;
                    if (columnAttributeMapping.get(entry.getKey()) != null) {
                        EntityUtils.setValue(entity, columnAttributeMapping.get(entry.getKey()), entry.getValue());
                    }
                }
                result.add(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取当前DaoSupport对应实体的Class对象
     *
     * @return 当前DaoSupport对应的实体Class对象
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Class<T> getEntityType() {
        return ReflectionUtil.getSuperClassGenricType(getClass(), 0);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#queryHistoryRecord(Page, Map)
     */
    /*public void queryHistoryRecord(Page<Map<String, Object>> page, Map<String, String> objectIds) {
        MdTable mdTable = getMdTable(getEntityType());
        if (null != mdTable.getHisTabCode()) {
            String historyTable = mdTable.getHisTabCode();
            Map<String, PropertyMapping> propertyMappings = this.getDaoSupport().getHibernateDao().getEntityHbmMapping(getEntityType());

            StringBuffer querySql = new StringBuffer();
            querySql.append("SELECT * FROM " + historyTable + " where 1=1 ");

            int idFieldCount = 0;
            for (Map.Entry<String, PropertyMapping> propertyMappingEntry : propertyMappings.entrySet()) {
                PropertyMapping propertyMapping = propertyMappingEntry.getValue();
                if (propertyMapping.isIdentifierProperty()) {
                    querySql.append(" and " + propertyMapping.getColumnName() + "='" + objectIds.get(propertyMapping.getPropertyName()) + "'");
                    idFieldCount++;
                }
            }

            querySql.append(" ORDER BY UPD_DATE DESC ");

            if (idFieldCount != objectIds.size()) {
                throw new BusinessRuntimeException("参数传递错误，字段数[" + idFieldCount + "]，参数个数[" + objectIds.size() + "],无法进行查询！");
            }
            dynamicQueryMapForList(querySql.toString(), page);

            List<Map<String, Object>> result = page.getEntities();
            IdfCodeTransfer idfCodeTransfer = getIdfCodeTransfer();

            Map<String, String> transFieldMapping = new HashMap<String, String>();

            transFieldMapping.put("CRT_ORG_CODE", "CRT_ORG_CODE");
            transFieldMapping.put("UPD_ORG_CODE", "UPD_ORG_CODE");
            idfCodeTransfer.transferMapResult(result, "sysOrgInfoService", transFieldMapping);
            transFieldMapping.clear();
            transFieldMapping.put("CRT_USER_CODE", "CRT_USER_CODE");
            transFieldMapping.put("UPD_USER_CODE", "UPD_USER_CODE");
            idfCodeTransfer.transferMapResult(result, "sysUserService", transFieldMapping);
        }
    }
*/
    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#queryCompareRecordInfo(String versionIds)
     */
   /* public List<Map<String, Object>> queryCompareRecordInfo(String versionIds) {
        List<Map<String, Object>> compareInfos = new ArrayList<Map<String, Object>>();
        MdTable mdTable = getMdTable(getEntityType());
        if (null != mdTable.getDataStatus() && null != mdTable.getHisTabCode() && MetadataConstants.DATA_STATUS_HISTORY.equals(mdTable.getDataStatus())) {
            IdfCodeTransfer idfCodeTransfer = getIdfCodeTransfer();
            Map<String, String> transFieldMapping = new HashMap<String, String>();
            String historyTable = mdTable.getHisTabCode();
            StringBuffer querySql = new StringBuffer();
            querySql.append("SELECT * FROM " + historyTable + " where 1=1 AND VERSION_ID in(" + versionIds + ")");
            Page page = new Page<Map<String, Object>>(Page.BIG_PAGE_SIZE, 1);
            getDaoSupport().getJdbcDao().dynamicQueryMapForList(querySql.toString(), page);
            List<Map<String, Object>> records = page.getEntities();
            transFieldMapping.put("CRT_ORG_CODE", "CRT_ORG_CODE");
            transFieldMapping.put("UPD_ORG_CODE", "UPD_ORG_CODE");
            idfCodeTransfer.transferMapResult(page.getEntities(), "sysOrgInfoService", transFieldMapping);
            transFieldMapping.clear();
            transFieldMapping.put("CRT_USER_CODE", "CRT_USER_CODE");
            transFieldMapping.put("UPD_USER_CODE", "UPD_USER_CODE");
            idfCodeTransfer.transferMapResult(page.getEntities(), "sysUserService", transFieldMapping);
            IMdFieldService mdFieldService = ContextHolder.getSpringBean("mdFieldService");
            List<MdField> mdFields = mdFieldService.findMdFieldsByTableId(mdTable.getId());
            if (null != mdFields) {
                for (MdField mdField : mdFields) {
                    if (null == mdField.getIsTableView() || FrameworkConstants.COMM_Y.equals(mdField.getIsTableView())) {
                        Map<String, Object> compareInfo = new HashMap<String, Object>();
                        String propertyLabel = mdField.getLabelInf();
                        int index = 1;
                        for (Map<String, Object> record : records) {
                            compareInfo.put("propertyLabel", propertyLabel);
                            compareInfo.put("version" + (index++) + "Value", record.get(mdField.getFieldName().toUpperCase()));
                        }
                        compareInfos.add(compareInfo);
                    }
                }
            }
        }
        return compareInfos;
    }*/


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#buildPropertyFilter(String, Object, String)
     */
    public List<PropertyFilter> buildPropertyFilter(String propertyName, Object propertyValue, String matchType) {
        return PropertyFilterUtil.buildPropertyFilter(propertyName, propertyValue, matchType);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#buildEqPropertyFilters(Map<String, Object>)
     */
    public List<PropertyFilter> buildEqPropertyFilters(Map<String, Object> parameters) {
        return PropertyFilterUtil.buildEqPropertyFilters(parameters);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#addPropertyFilter(java.util.List, String, Object, String, Boolean)
     */
    public void addPropertyFilter(List<PropertyFilter> propertyFilters, String propertyName, Object propertyValue, String matchType, Boolean overwrite) {
        PropertyFilterUtil.addPropertyFilter(propertyFilters, QueryConstants.RESTRICTION_TYPE_AND, propertyName, propertyValue, matchType, overwrite);
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
     * @see com.innofi.framework.service.IBaseService#addDateRangePropertyFilter(java.util.List, String, java.util.Map)
     */
    public void addDateRangePropertyFilter(List<PropertyFilter> propertyFilters, String propertyName, Map<String, Object> parameters) {
        PropertyFilterUtil.addDateRangePropertyFilter(propertyFilters, propertyName, parameters);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#removePropertyFilter(List, String)
     */
    public void removePropertyFilter(List<PropertyFilter> propertyFilters, String propertyName) {
        PropertyFilterUtil.removePropertyFilter(propertyFilters, propertyName);
    }


    /**
     * @see com.innofi.framework.service.IBaseService#deleteByPropertyFilters(List<PropertyFilter>)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public int deleteByPropertyFilters(List<PropertyFilter> filters) {
        //checkTableIsSaveClosed();    todo
        List<T> queryResult = findByPropertyFilters(filters, null);
        for (T t : queryResult) {
            delete(t);
        }
        return queryResult.size();
    }


    ///////////////////////////////////Criteria///////////////////////////////////////////


    /**
     * @see com.innofi.framework.service.IBaseService#getAll(List)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> getAll(List<PropertyOrder> orders) {
        if (orders != null && orders.size() > 0) {
            Page page = new Page(Page.BIG_PAGE_SIZE, 1);
            findByPage_Filters(new ArrayList(), orders, page);
            return page.getEntities();
        }
        return getAll(getEntityType());
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#getAll(Class)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    private List<T> getAll(Class<T> clazz) {
        Assert.notNull(clazz, "clazz类型不能为空");
        return findByCriterions(clazz, null);
    }


    protected void saveDynamicPojo(T entity) {
        EntityOperation<?> op = new EntityOperation(entity.getClass());

    }

    /**
     * 设置分页参数到Query对象,辅助函数.
     */
    protected Query setPageParameter(final Query q, final Page<T> page) {
        // hibernate的firstResult的序号从0开始
        q.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
        q.setMaxResults(page.getPageSize());

        return q;
    }

    /**
     * 设置分页参数到Criteria对象,辅助函数.
     */
    protected void setPageParameter(final DetachedCriteria c, final List<PropertyOrder> orders, final Page<T> page) {
        if (orders != null && orders.size() > 0 && !page.isOrderBySetted()) {
            addOrders(c, orders);
        } else if (page.isOrderBySetted()) {
            addOrders(c, page);
        }
    }

    protected void addOrders(DetachedCriteria c, Page<T> page) {
        String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
        String[] orderArray = StringUtils.split(page.getOrder(), ',');

        Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");

        for (int i = 0; i < orderByArray.length; i++) {
            if (QueryConstants.ORDER_ASC.equals(orderArray[i])) {
                c.addOrder(Order.asc(orderByArray[i]));
            } else {
                c.addOrder(Order.desc(orderByArray[i]));
            }
        }
    }

    /**
     * 添加排序方法
     *
     * @param orders PropertyOrder 对象
     * @param ca     Hibernate Criteria对象
     */
    protected void addOrders(DetachedCriteria ca, List<PropertyOrder> orders) {
        for (PropertyOrder order : orders) {
            String propertyName = order.getPropertyName();
            String orderRule = order.getOrderRule();
            if (orderRule.equals(QueryConstants.ORDER_ASC)) {
                ca.addOrder(Order.asc(propertyName));
            }
            if (orderRule.equals(QueryConstants.ORDER_DESC)) {
                ca.addOrder(Order.desc(propertyName));
            }
        }
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByIds(Class, List<PK>)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> findByIds(Class<T> clazz, List<PK> ids) {
        Assert.notNull(clazz, "clazz类型不能为空");
        Assert.notEmpty(ids, "id列表不能为空");
        return findByCriterions(clazz, null, Restrictions.in(getIdPropertyName(clazz), ids));
    }

    /**
     * 创建实体对象Criteria查询对象
     *
     * @return criteria对象实例
     */
    protected DetachedCriteria createDetachedCriteria(Class<T> clazz) {
        DetachedCriteria c = DetachedCriteria.forClass(clazz);

        BasePojo basePojo = null;
        try {
            basePojo = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        List<String> projectionFields = basePojo.getProjectionFields();
        if (projectionFields.size() > 0) {
            ProjectionList projections = Projections.projectionList();
            for (String fieldName : projectionFields) {
                projections.add(Projections.property(fieldName).as(fieldName));
            }
            c.setProjection(projections);
            c.setResultTransformer(Transformers.aliasToBean(clazz));
        }
        return c;
    }

    /**
     * 获取当前对象是否缓存
     *
     * @param calzz 当前对象
     * @return 返回是否缓存
     */
    protected boolean getCacheAble(Class<T> calzz) {
        try {
            BasePojo pojo = (BasePojo) calzz.newInstance();
            return pojo.getCacheAble();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#getIdPropertyName(Class<T>)
     */
    public String getIdPropertyName(Class<T> clazz) {
        return getDaoSupport().getHibernateDao().getIdPropertyName(clazz);
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByCriterions
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> findByCriterions(Class<T> clazz, List<PropertyOrder> orders, Criterion... criterions) {
        Page page = new Page(Page.BIG_PAGE_SIZE, 1);
        findPage_Criterion(clazz, orders, page, null, criterions);
        return page.getEntities();
    }

    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findPage_Criterion(Class, List, Page, Criterion...) {
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    @SuppressWarnings("unchecked")
    public void findPage_Criterion(Class<T> clazz, List<PropertyOrder> orders, final Page<T> page, List<PropertyFilter> filters, Criterion... criterions) {
        Assert.notNull(clazz, "clazz类型不能为空");
        Assert.notNull(page, "page不能为空");
        DetachedCriteria c = createDetachedCriteria(clazz, filters, criterions);

        if (page.isAutoCount()) {
            long totalCount = getDaoSupport().getHibernateDao().countCriteriaResult(c);
            page.setEntityCount((int) totalCount);
        }
        setPageParameter(c, orders, page);
        List result = getDaoSupport().getHibernateDao().findByCriteria(c, (page.getPageNo() - 1) * page.getPageSize(), page.getPageSize());
        page.setEntities(result);
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#createCriteria(Class, Criterion...)
     */
    public DetachedCriteria createDetachedCriteria(Class<T> clazz, List<PropertyFilter> filters, Criterion... criterions) {
        DetachedCriteria criteria = createDetachedCriteria(clazz);

        List<Criterion> orCriterions = new ArrayList();

        if (filters != null) {
            for (PropertyFilter filter : filters) {
                Criterion criterion = buildPropertyFilterCriterion(filter.getPropertyName(), filter.getPropertyValue(), filter.getMatchType());
                if (criterion != null)
                    if (QueryConstants.RESTRICTION_TYPE_AND.equals(filter.getRestrictionType())) {
                        if (orCriterions.size() > 0) {
                            Disjunction disjunction = Restrictions.disjunction();
                            for (Criterion orCriterion : orCriterions) {
                                disjunction.add(orCriterion);
                            }
                            criteria.add(disjunction);
                            orCriterions.clear();
                        }
                        criteria.add(criterion);
                    } else {
                        orCriterions.add(criterion);
                    }
            }

            if (orCriterions.size() > 0) {
                Disjunction disjunction = Restrictions.disjunction();
                for (Criterion orCriterion : orCriterions) {
                    disjunction.add(orCriterion);
                }
                criteria.add(disjunction);
                orCriterions.clear();
            }

        } else if (criterions != null) {
            for (Criterion c : criterions) {
                if (c != null) {
                    criteria.add(c);
                }
            }
        }
        return criteria;
    }

    /**
     * @see com.innofi.framework.service.IBaseService#findByProperty(String, Object, String)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> findByProperty(final String propertyName, final Object value, final String matchType) {
        if (Validator.isNull(matchType)) {
            return findByProperty(getEntityType(), propertyName, value, QueryConstants.EQUAL);
        }
        return findByProperty(getEntityType(), propertyName, value, matchType);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#findByProperty(String, Object, String, List)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> findByProperty(final String propertyName, final Object value, final String matchType, List<PropertyOrder> orders) {
        if (Validator.isNull(matchType)) {
            return findByProperty(getEntityType(), propertyName, value, QueryConstants.EQUAL, orders);
        }
        return findByProperty(getEntityType(), propertyName, value, matchType, orders);
    }

    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByProperty(Class, String, Object, String) {
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> findByProperty(Class<T> clazz, final String propertyName, final Object value, final String matchType) {
        Criterion criterion = buildPropertyFilterCriterion(propertyName, value, matchType);
        return findByCriterions(clazz, null, criterion);
    }

    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByProperty(Class, String, Object, String, List) {
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> findByProperty(Class<T> clazz, String propertyName, Object value, String matchType, List<PropertyOrder> orders) {
        Criterion criterion = buildPropertyFilterCriterion(propertyName, value, matchType);
        return findByCriterions(clazz, orders, criterion);
    }

    /**
     * 按属性条件参数创建Criterion,辅助函数. 比较符号说明 Restrictions.eq SQL 里的 = 操作
     * Restrictions.ne SQL 里的 ！= 操作 Restrictions.allEq 全等于操作 Restrictions.gt SQL
     * 里的 > 操作 Restrictions.ge SQL 里的 >= 操作 Restrictions.lt SQL里的< 操作
     * Restrictions.le SQL里的<= 操作 Restrictions.between SQL里的 BETWEEN 操作
     * Restrictions.like SQL里的 LIKE 操作 Restrictions.in SQL里的 IN 操作
     * Restrictions.isNull SQL里的 ISNull 操作 Restrictions.isNotNull SQL里的
     * ISNotNull 操作 Restrictions.isEmpty SQL里的 ISEmpty 操作
     * Restrictions.isNotEmpty SQL里的 ISNotEmpty 操作
     *
     * @param propertyName  属性名称
     * @param propertyValue String类型对象字段常量
     * @param matchType     value String类型比较方式
     * @return 添加匹配条件后的 Criterion 对象
     */
    protected Criterion buildPropertyFilterCriterion(final String propertyName, final Object propertyValue, final String matchType) {
        Criterion criterion = null;
        if (Validator.isNotNull(propertyValue)) {
            try {
                // 根据MatchType构造criterion
                if (QueryConstants.EQUAL.equals(matchType) || matchType == null) {
                    criterion = Restrictions.eq(propertyName, propertyValue);
                } else if (QueryConstants.LIKE.equals(matchType)) {
                    criterion = Restrictions.like(propertyName,
                            (String) propertyValue, MatchMode.ANYWHERE);
                } else if (QueryConstants.LESS_OR_EQUAL.equals(matchType)) {
                    criterion = Restrictions.le(propertyName, propertyValue);
                } else if (QueryConstants.LESS_THAN.equals(matchType)) {
                    criterion = Restrictions.lt(propertyName, propertyValue);
                } else if (QueryConstants.GREATER_OR_EQUAL.equals(matchType)) {
                    criterion = Restrictions.ge(propertyName, propertyValue);
                } else if (QueryConstants.GREATER_THAN.equals(matchType)) {
                    criterion = Restrictions.gt(propertyName, propertyValue);
                } else if (QueryConstants.NOT_EQUAL.equals(matchType)) {
                    criterion = Restrictions.ne(propertyName, propertyValue);
                } else if (QueryConstants.IN.equals(matchType)) {
                    if (propertyValue instanceof Object[] && ((Object[]) propertyValue).length > 0) {
                        if (((Object[]) propertyValue).length <= 500) {
                            criterion = Restrictions.in(propertyName, ((Object[]) propertyValue));
                        } else {
                            Disjunction disjunction = Restrictions.disjunction();
                            Object[] array = (Object[]) propertyValue;
                            List<Object> valueList = Arrays.asList((Object[]) propertyValue);
                            for (int i = 0; i <= array.length; i++) {
                                if (i == 0) {
                                    disjunction.add(Restrictions.in(propertyName, valueList.subList(i, i + 499)));
                                } else if (i > 0 && i % 500 == 0) {
                                    if ((i + 500) > array.length) {
                                        disjunction.add(Restrictions.in(propertyName, valueList.subList(i, array.length)));
                                        break;
                                    } else {
                                        disjunction.add(Restrictions.in(propertyName, valueList.subList(i, i + 499)));
                                    }
                                }
                            }
                            criterion = disjunction;
                        }
                    } else if (propertyValue instanceof Collection
                            && ((Collection) propertyValue).size() > 0) {
                        if (((Collection) propertyValue).size() <= 500) {
                            criterion = Restrictions.in(propertyName, (Collection) propertyValue);
                        } else {
                            Disjunction disjunction = Restrictions.disjunction();
                            List<Object> valueList = new ArrayList();
                            valueList.addAll((Collection) propertyValue);
                            for (int i = 0; i <= valueList.size(); i++) {
                                if (i == 0) {
                                    disjunction.add(Restrictions.in(propertyName, valueList.subList(i, i + 499)));
                                } else if (i > 0 && i % 500 == 0) {
                                    if ((i + 500) > valueList.size()) {
                                        disjunction.add(Restrictions.in(propertyName, valueList.subList(i, valueList.size())));
                                        break;
                                    } else {
                                        disjunction.add(Restrictions.in(propertyName, valueList.subList(i, i + 499)));
                                    }
                                }
                            }
                            criterion = disjunction;
                        }
                    }
                } else if (QueryConstants.NOT_IN.equals(matchType)) {
                    if (propertyValue instanceof Object[] && ((Object[]) propertyValue).length > 0) {
                        criterion = Restrictions.not(Restrictions.in(propertyName, (Object[]) propertyValue));
                    } else if (propertyValue instanceof Collection
                            && ((Collection) propertyValue).size() > 0) {
                        criterion = Restrictions.not(Restrictions.in(propertyName, (Collection) propertyValue));
                    }
                } else if (QueryConstants.BETWEEN.equals(matchType)) {
                    Object object1 = null;
                    Object object2 = null;
                    if (propertyValue instanceof Object[]) {
                        object1 = ((Object[]) propertyValue)[0];
                        object2 = ((Object[]) propertyValue)[1];
                    } else if (propertyValue instanceof Collection) {
                        Collection c = (Collection) propertyValue;
                        int index = 0;
                        for (Iterator it = c.iterator(); it.hasNext() && index <= 1; ) {
                            if (index == 0) {
                                object1 = it.next();
                                index++;
                            } else {
                                object2 = it.next();
                            }
                        }
                    }
                    if (object1 != null && object2 != null) {
                        criterion = Restrictions.between(propertyName, object1,
                                object2);
                    } else if (object1 != null) {
                        criterion = Restrictions.ge(propertyName, object1);
                    } else if (object2 != null) {
                        criterion = Restrictions.le(propertyName, object1);
                    }
                } else if (QueryConstants.ALL_EQUAL.equals(matchType)
                        && propertyValue instanceof Map) {
                    criterion = Restrictions.allEq((Map) propertyValue);
                }
            } catch (Exception e) {
                throw ReflectionUtil.convertReflectionExceptionToUnchecked(e);
            }
        } else {
            if (QueryConstants.IS_NULL.equals(matchType)) {
                criterion = Restrictions.isNull(propertyName);
            } else if (QueryConstants.IS_NOT_NULL.equals(matchType)) {
                criterion = Restrictions.isNotNull(propertyName);
            } else if (QueryConstants.IS_EMPTY.equals(matchType)) {
                criterion = Restrictions.isEmpty(propertyName);
            } else if (QueryConstants.IS_NOT_EMPTY.equals(matchType)) {
                criterion = Restrictions.isNotEmpty(propertyName);
            }
        }

        return criterion;
    }


    /**
     * @see com.innofi.framework.service.IBaseService#findUniqueByProperty(String, Object, String)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public T findUniqueByProperty(final String propertyName, final Object value, final String matchType) {
        List<T> beanList = findByProperty(propertyName, value, matchType);
        if (beanList.size() > 1) {
            throw new RuntimeException("查询结果不唯一，请检查条件是否正确！");
        } else if (beanList.size() == 0) {
            return null;
        }
        return beanList.get(0);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#findByIds(List<PK>)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> findByIds(List<PK> ids) {
        return findByIds(getEntityType(), ids);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#findByPropertyFilters(List, List)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> findByPropertyFilters(List<PropertyFilter> filters, List<PropertyOrder> orders) {
        Page page = new Page(Page.BIG_PAGE_SIZE, 1);
        findByPage_Filters(filters, orders, page);
        return page.getEntities();
    }

    /**
     * @see com.innofi.framework.service.IBaseService#findByPage_Filters(List, List, Page)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void findByPage_Filters(List<PropertyFilter> filters, List<PropertyOrder> orders, Page page) {
        findByPage_Filters(getEntityType(), filters, orders, page);
    }

    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByPage_Filters(Class, java.util.List, java.util.List, com.innofi.framework.dao.pagination.Page)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void findByPage_Filters(Class<T> clazz, final List<PropertyFilter> filters, List<PropertyOrder> orders, final Page page) {
        findPage_Criterion(clazz, orders, page, filters);
    }

///////////////////////////Criteria/////////////////////////////////////////////////       

    /**
     * @see com.innofi.framework.service.IBaseService#findByHql(String hql, Object... values)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> findByHql(String hql, Object... values) {
        return getDaoSupport().getHibernateDao().findByHql(getEntityType(), hql, values);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#findByHql<String, Object>) {
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> findByNamedHql(String hql, Map<String, ?> values) {
        return getDaoSupport().getHibernateDao().findByNamedHql(getEntityType(), hql, values);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#findByHql_Page(String hql, Page page, Object... values)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void findByHql_Page(String hql, Page page, Object... values) {
        getDaoSupport().getHibernateDao().findByHql_Page(getEntityType(), hql, page, values);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#findByHql_Page(String hql, Page page, Object... values)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void findByNamedHql_Page(String hql, Page page, Map<String, ?> values) {
        getDaoSupport().getHibernateDao().findByNamedHql_Page(getEntityType(), hql, page, values);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#findUniqueByHql(String hql, Object... values)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public <X> X findUniqueByHql(String hql, Object... values) {
        return (X) getDaoSupport().getHibernateDao().findUnique(getEntityType(), hql, values);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#findUniqueByNamedHql(String sql, Map)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    @SuppressWarnings("unchecked")
    public <X> X findUniqueByNamedHql(String hql, Map<String, ?> values) {
        return (X) getDaoSupport().getHibernateDao().findUnique(getEntityType(), hql, values);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#queryBeanForUnique(String sql, Object... objects)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public <X> X queryBeanForUnique(String sql, Object... objects) {
        List<T> beanList = queryBeanForList(sql, objects);
        if (beanList.size() > 1) {
            throw new RuntimeException("查询结果不唯一，请检查SQL的条件是否正确！");
        } else if (beanList.size() == 0) {
            return null;
        }
        return (X) beanList.get(0);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#queryBeanForList(String sql, Object[] objects)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> queryBeanForList(String sql, Object... objects) {
        Page page = new Page(Page.BIG_PAGE_SIZE, 1);
        queryBeanForPage(sql, page, objects);
        return page.getEntities();
    }

    /**
     * @see com.innofi.framework.service.IBaseService#dynamicQueryBeanForUnique(String, String, Map)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public <X> X dynamicQueryBeanForUnique(String moduleId, String sqlId, Map<String, String> params) {
        List<T> beanList = dynamicQueryBeanForList(moduleId, sqlId, params);
        if (beanList.size() > 1) {
            throw new RuntimeException("查询结果不唯一，请检查SQL的条件是否正确！");
        } else if (beanList.size() == 0) {
            return null;
        }
        return (X) beanList.get(0);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#dynamicQueryBeanForList(String, String)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> dynamicQueryBeanForList(String moduleId, String sqlId) {
        return dynamicQueryBeanForList(moduleId, sqlId, new HashMap<String, String>());
    }

    /**
     * @see com.innofi.framework.service.IBaseService#dynamicQueryBeanForList(String, String, Map)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> dynamicQueryBeanForList(String moduleId, String sqlId, Map<String, String> params) {
        Page page = new Page(Page.BIG_PAGE_SIZE, 1);
        dynamicQueryBeanForPage(moduleId, sqlId, params, page);
        return page.getEntities();
    }

    /**
     * @see com.innofi.framework.service.IBaseService#queryBeanForPage(String, Page, Object...)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void queryBeanForPage(String sql, Page<T> page, Object... objects) {
        getDaoSupport().queryBeanForPage(getEntityType(), sql, page, objects);
    }


    /**
     * @see com.innofi.framework.service.IBaseService#dynamicQueryBeanForPage(String, String, Map, Page)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void dynamicQueryBeanForPage(String moduleId, String sqlId, Map<String, String> params, Page<T> page) {
        getDaoSupport().queryBeanForPage(getEntityType(), moduleId, sqlId, params, page);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#getIdPropertyName()
     */
    public String getIdPropertyName() {
        return getDaoSupport().getHibernateDao().getIdPropertyName(getEntityType());
    }

    /**
     * @see com.innofi.framework.service.IBaseService#getDoradoContext()
     */
    public DoradoContext getDoradoContext() {
        return getContext().getDoradoContext();
    }


    /**
     * @see com.innofi.framework.service.IBaseService#getSpringBean(String)
     */
    public <T> T getSpringBean(String beanId) {
        return (T) ContextHolder.getSpringBean(beanId);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#getSpringBeanFactory()
     */
    public ApplicationContext getSpringBeanFactory() {
        return ContextHolder.getSpringBeanFactory();
    }

    /**
     * @see com.innofi.framework.service.IBaseService#getContext()
     */
    public FrameworkContext getContext() {
        return ContextHolder.getContext();
    }

    /**
     * @see com.innofi.framework.service.IBaseService#setRequestAttribute(String, Object)
     */
    public void setRequestAttribute(String key, Object obj) {
        ContextHolder.setRequestAttribute(key, obj);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#getRequestAttribute(String)
     */
    public Object getRequestAttribute(String key) {
        return ContextHolder.getRequestAttribute(key);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#putCacheObject(String, Object)
     */
    public void putCacheObject(String key, Object obj) {
        ISysCacheConfigService applicationCache = getApplicationCache();
        applicationCache.putCacheObject(key, obj);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#putTemporaryCacheObject(String, Object)
     */
    public void putTemporaryCacheObject(String key, Object obj) {
        ISysCacheConfigService applicationCache = getApplicationCache();
        applicationCache.putTemporaryCacheObject(key, obj);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#getCacheObject(String)
     */
    public Object getCacheObject(String key) {
        ISysCacheConfigService applicationCache = getApplicationCache();
        return applicationCache.getCacheObject(key);
    }

    @Override
    public Object getTemporaryCacheObject(String key) {
        ISysCacheConfigService applicationCache = getApplicationCache();
        return applicationCache.getTemporaryCacheObject(key);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#removeCacheObject(String)
     */
    public void removeCacheObject(String key) {
        ISysCacheConfigService applicationCache = getApplicationCache();
        applicationCache.removeCacheObject(key);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#removeCacheObject(String)
     */
    public void removeTemporaryCacheObject(String key) {
        ISysCacheConfigService applicationCache = getApplicationCache();
        applicationCache.removeTemporaryCacheObject(key);
    }

    /**
     * @see com.innofi.framework.service.IBaseService#countByPropertyFilters(List)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public long countByPropertyFilters(List<PropertyFilter> filters) {
        return getDaoSupport().getHibernateDao().countByPropertyFilters(getEntityType(), filters);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#getPropertyValueString(java.util.Collection, String, String)
     */
    public List<String> getPropertyValueList(Collection<T> entityList, String propertyName, String separator) {
        List<String> idList = new ArrayList<String>();
        for (BasePojo pojo : entityList) {
            String id = pojo.getId();
            idList.add(id);
        }
        return idList;
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.cache.ICacheService#loadCacheObjects()
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public List<T> loadCacheObjects() {
        return getAll(getEntityType());
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.cache.ICacheService#getCodeFieldName()
     */
    public String getCodeFieldName() {
        return "id";
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.cache.ICacheService#getCnFieldName()
     */
    public String getCnFieldName() {
        return "name";
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.cache.ICacheService#getCnFieldName()
     */
    public String getCacheType() {
        return "temporary";
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#getIdfCodeTransfer()
     */
    public IdfCodeTransfer getIdfCodeTransfer() {
        return ContextHolder.getSpringBean("mdf.codeTransfer");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#checkUpdDelPermission(String, List, String)
     */
    public List<String> checkUpdDelPermission(String entityClass, List<String> idValues, String operationType) {
        return new ArrayList<String>();
    }


    private ISysCacheConfigService getApplicationCache() {
        return getSpringBeanFactory().getBean(ISysCacheConfigService.class);
    }

    private String getParentSeq(String idFieldName, Object value, String seqFieldName) {
        T entity = findUniqueByProperty(idFieldName, value, QueryConstants.EQUAL);
        if (entity == null) {
            return SEQ_SEPARATOR;
        }
        return (String) ReflectionUtil.getFieldValue(entity, seqFieldName);
    }

    private void generatorSeq(T entity) {
        if (entity.getTreeStruct()) {
            Object parentId = ReflectionUtil.getFieldValue(entity, entity.getTreeParentIdFieldName());
            String treeSeqFieldName = entity.getTreeSeqFieldName();
            String parentOrgSeq = getParentSeq(entity.getTreeIdFiledName(), parentId, treeSeqFieldName);//上级路径序列
            String treeSeq = parentOrgSeq + ReflectionUtil.getFieldValue(entity, entity.getTreeIdFiledName()) + SEQ_SEPARATOR;//tree路径=上级路径序列+本级节点编号
            ReflectionUtil.setFieldValue(entity, entity.getTreeSeqFieldName(), treeSeq);
            getDaoSupport().getHibernateDao().saveOrUpdate(entity);
        }
    }

    /*private void saveInfoToHistory(String method, T entity, T oldEntity) {
        MdTable mdTable = getMdTable(this.getEntityType());                  //保存历史信息
        if (null != mdTable && null != mdTable.getDataStatus() && null != mdTable.getHisTabCode() && MetadataConstants.DATA_STATUS_HISTORY.equals(mdTable.getDataStatus())) {
            if (method.startsWith("del") || !oldEntity.equals(entity)) {
                StringBuffer insertSql = new StringBuffer();
                StringBuffer saveSqlBuffer = new StringBuffer(" INSERT INTO " + mdTable.getHisTabCode() + " ( VERSION_ID , ");
                StringBuffer valuesSqlBuffer = new StringBuffer(" VALUES ( '" + UUID.randomUUID().toString() + "',");
                Map<String, PropertyMapping> classPropertyMapping = getDaoSupport().getEntityHbmMapping(this.getEntityType());
                for (Map.Entry<String, PropertyMapping> entry : classPropertyMapping.entrySet()) {
                    PropertyMapping propertyMapping = entry.getValue();
                    Field field = ReflectionUtil.getDeclaredField(oldEntity, propertyMapping.getPropertyName());
                    String fieldType = field.getType().toString();
                    fieldType = fieldType.substring(fieldType.lastIndexOf(".") + 1);
                    saveSqlBuffer.append(propertyMapping.getColumnName().toUpperCase() + ",");
                    if (DataType.isNumberType(DataType.nameToType(fieldType))) {
                        if (null == ReflectionUtil.getFieldValue(oldEntity, propertyMapping.getPropertyName())) {
                            valuesSqlBuffer.append("null,");
                        } else {
                            valuesSqlBuffer.append("'" + ReflectionUtil.getFieldValue(oldEntity, propertyMapping.getPropertyName()) + "',");
                        }
                    } else if (DataType.isDateType(DataType.nameToType(fieldType))) {
                        if (null == ReflectionUtil.getFieldValue(oldEntity, propertyMapping.getPropertyName())) {
                            valuesSqlBuffer.append("null,");
                        } else {
                            String dateValue = DateUtil.formatDateTime((Date) ReflectionUtil.getFieldValue(oldEntity, propertyMapping.getPropertyName()));
                            if (getDaoSupport().getJdbcDao().getCurrentDbType().startsWith("oracle")) {
                                valuesSqlBuffer.append("to_date('" + dateValue + "','yyyy-MM-dd HH24:mi:ss'),");
                            } else if (getDaoSupport().getJdbcDao().getCurrentDbType().startsWith("db2")) {
                                valuesSqlBuffer.append("date('" + dateValue + "','yyyy-MM-dd HH24:mi:ss'),");
                            }
                        }
                    } else {
                        if (null == ReflectionUtil.getFieldValue(oldEntity, propertyMapping.getPropertyName())) {
                            valuesSqlBuffer.append("null,");
                        } else {
                            valuesSqlBuffer.append("'" + ReflectionUtil.getFieldValue(oldEntity, propertyMapping.getPropertyName()) + "',");
                        }
                    }
                }
                saveSqlBuffer.replace(saveSqlBuffer.lastIndexOf(","), saveSqlBuffer.length(), " )");
                valuesSqlBuffer.replace(valuesSqlBuffer.lastIndexOf(","), valuesSqlBuffer.length(), " )");
                insertSql.append(saveSqlBuffer + " " + valuesSqlBuffer);
                getDaoSupport().getJdbcDao().execute(insertSql.toString());
            }
        }
    }
*/

    /**
     * 检查数据表是否已关闭保存操作
     */
    /*private void checkTableIsSaveClosed() {
        MdTable mdTable = getMdTable(this.getEntityType());
        if (mdTable != null) {
            if (null != mdTable.getIsCloseSaved() && FrameworkConstants.COMM_Y.equals(mdTable.getIsCloseSaved())) {
                throw new ClientRunnableException(script.toString());
            }
        }
    }*/

    ///////////////////////////MAP/////////////////////////////////

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#dynamicQueryMapForUnique(String, String, Map)
     */
    public Map<String, Object> dynamicQueryMapForUnique(String moduleId, String sqlId, Map<String, String> params) {
        return getDaoSupport().getJdbcDao().queryMapForUnique(moduleId, sqlId, params);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#dynamicQueryMapForList(String, String)
     */
    public List<Map<String, Object>> dynamicQueryMapForList(String moduleId, String sqlId) {
        return getDaoSupport().getJdbcDao().queryMapForList(moduleId, sqlId);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#dynamicQueryMapForList(String, String, Map)
     */
    public List<Map<String, Object>> dynamicQueryMapForList(String moduleId, String sqlId, Map<String, String> params) {
        return getDaoSupport().getJdbcDao().queryMapForList(moduleId, sqlId, params);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#queryMapForList(String, Page page)
     */
    public void queryMapForList(String querySql, Page<Map<String, Object>> page) {
        getDaoSupport().getJdbcDao().queryMapForList(querySql, page);
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#dynamicQueryMapForList(String, String, Map, com.innofi.framework.dao.pagination.Page)
     */
    public void dynamicQueryMapForList(String moduleId, String sqlId, Map<String, String> params, Page<Map<String, Object>> page) {
        getDaoSupport().getJdbcDao().queryMapForList(moduleId, sqlId, params, page);
    }
//------------------------------------------------------状态引擎-------------------------------------------------------------

    public boolean support(String moduleCode) {
        boolean bRet = false;
        String entityName = getEntityName(moduleCode);

        if (entityName.equals(this.getEntityType().getName())) {
            bRet = true;
        } else {
            bRet = false;
        }
        return bRet;
    }

/*    public void before(WfProcess wfProcess, BasePojo businessObject) {

    }

    public void after(WfProcess wfProcess, BasePojo businessObject) {
        // 当没有修改的时候，只更新状态
        if (businessObject == null) {
            businessObject = (BasePojo) getDaoSupport().getHibernateDao().get(this.getEntityType(), wfProcess.getBusinessId());
        } else {

        }
        // 当前业务状态
        String status = wfProcess.getStatus();
        String nodeName = wfProcess.getNodeName();
        // 状态字段名
        ReflectionUtil.setFieldValue(businessObject, getStatusFieldName(), status);
        ReflectionUtil.setFieldValue(businessObject, getNodeFieldName(), nodeName);

        getDaoSupport().getHibernateDao().update(businessObject);
    }*/

    /**
     * 状态字段名称当不为status时，需要重写该方法，返回实际状态字段名，如：busiStatus
     *
     * @return
     */
    public String getStatusFieldName() {
        return statusFieldName;
    }

    /**
     * 状态字段名称当不为nodeName时，需要重写该方法，返回实际状态字段名，如：myNodeName
     *
     * @return
     */
    public String getNodeFieldName() {
        return nodeFieldName;
    }

    /*
     * 通过菜单编码获得实体名
     */
    private String getEntityName(String moduleCode) {
        String entityName = "";

        String sql = "SELECT a.ENTITY_NAME AS ENTITYNAME from MD_TABLE a ,SYS_MENU b where b.MENU_URL = a.UI_NAME and b.MENU_CODE = ?";
        List<Map<String, Object>> moduleList = getDaoSupport().getJdbcDao().queryForList(sql, moduleCode);

        if (moduleList != null && moduleList.size() > 0) {
            entityName = (String) ((Map) moduleList.get(0)).get("ENTITYNAME");
        }

        return entityName;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void findByPageAndCriteria(Page page, DetachedCriteria criteria) {
        Assert.notNull(page, "page不能为空");

        if (page.isAutoCount()) {
            long totalCount = getDaoSupport().getHibernateDao().countCriteriaResult(criteria);
            page.setEntityCount((int) totalCount);
        }
        setPageParameter(criteria, null, page);
        List result = getDaoSupport().getHibernateDao().findByCriteria(criteria, (page.getPageNo() - 1) * page.getPageSize(), page.getPageSize());
        page.setEntities(result);

    }

	@Override
	public void before(WfProcess wfProcess, BasePojo businessObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void after(WfProcess wfProcess, BasePojo businessObject) {
		// TODO Auto-generated method stub
		
	}
    
    


/*    *//**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#getMdTable(Class)
     *//*
    public MdTable getMdTable(Class clazz) {
        String classSimpleName = clazz.getSimpleName();
        PersistentClass persistentClass = entitySimpleNamePersistenceClassMapping.get(classSimpleName);
        String tableName = persistentClass.getTable().getName().toUpperCase();
        IMdTableService mdTableService = getSpringBean("mdTableService");
        return mdTableService.findLastVersionMdTableByTableName(tableName);
    }*/

}
