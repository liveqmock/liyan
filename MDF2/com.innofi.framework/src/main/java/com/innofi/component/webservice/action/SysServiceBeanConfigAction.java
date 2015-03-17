package com.innofi.component.webservice.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.metadata.MdEntityObject;
import com.innofi.framework.pojo.metadata.MdObjectMethod;
import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.component.webservice.pojo.GenerateServicePojo;
import com.innofi.component.webservice.pojo.SysServiceBeanConfig;
import com.innofi.component.webservice.pojo.SysServiceBeanmethodConfig;
import com.innofi.component.webservice.service.IManageWebservice;
import com.innofi.component.webservice.service.IMdSysEntityObjectService;
import com.innofi.component.webservice.service.IMdSysObjectMethodService;
import com.innofi.component.webservice.service.IMdSysTableService;
import com.innofi.component.webservice.service.ISysServiceBeanConfigService;
import com.innofi.component.webservice.service.ISysServiceBeanmethodConfigService;
import com.innofi.component.webservice.util.CommonUtil;
import com.innofi.component.webservice.util.GenerateXml;

/**
 * 功能/ 模块：webservice
 *
 * @author liuhuaiyang
 * @version 2.0.0 13-5-10
 *          为dorado7界面维护操作的提供支持，实现SysServiceBeanConfig对象信息的加载与保存操作 修订历史： 日期 作者
 *          参考 描述 北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysServiceBeanConfigAction extends BaseActionImpl {

    // 处理bean service 服务类
    @Resource
    private ISysServiceBeanConfigService sysServiceBeanConfigSevice;

    // 处理 bean method 服务类
    @Resource
    ISysServiceBeanmethodConfigService sysServiceBeanmethodConfigService;

    // 处理webservice接口发布类
    @Resource
    IManageWebservice managerWebServiceService;

    // 获取元数据配置服务类
    @Resource
    IMdSysEntityObjectService mdSysEntityObjectService;

    // 获取元数据方法服务类
    @Resource
    private IMdSysObjectMethodService mdSysObjectMethodService;

    //获取元数据表服务类
    @Resource
    private IMdSysTableService mdSysTableService;

    public ISysServiceBeanConfigService getBusinessService() {
        return sysServiceBeanConfigSevice;
    }

    /**
     * 查询bean的配置
     *
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findSysServiceBeanConfigs(Page page,
                                          Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

        if (parameter != null) {
            addPropertyFilter(propertyFilters, "beanId",
                    parameter.get("qBeanId"), null, true);
            addPropertyFilter(propertyFilters, "beanPath",
                    parameter.get("qBeanPath"), null, true);
            addPropertyFilter(propertyFilters, "intfPath",
                    parameter.get("qIntfPath"), null, true);
            addPropertyFilter(propertyFilters, "id",
                    parameter.get("qServiceId"), null, true);
            addPropertyFilter(propertyFilters, "serviceName",
                    parameter.get("qServiceName"), null, true);
        }

        com.innofi.framework.dao.pagination.Page innofiPage = super
                .translateDoradoPageToInnofiPage(page);
        sysServiceBeanConfigSevice.findByPage_Filters(propertyFilters, null,
                innofiPage);
    }

    /**
     * 持久化bean methods
     *
     * @param objs
     */
    @DataResolver
    public void saveSysServiceBeanConfigs(Collection<SysServiceBeanConfig> objs) {

		for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
			SysServiceBeanConfig sysServiceBeanConfig = (SysServiceBeanConfig) iterator.next();
            EntityState state = EntityUtils.getState(sysServiceBeanConfig);
            if (EntityState.DELETED.equals(state)) {
            	sysServiceBeanConfigSevice.delete(sysServiceBeanConfig);
            } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
            	sysServiceBeanConfigSevice.update(sysServiceBeanConfig);
            } else if (EntityState.NEW.equals(state)) {
            	sysServiceBeanConfigSevice.save(sysServiceBeanConfig);
            }
		}
    	
        // 如果是删除bean的配置，则删除其相应的方法
        for (SysServiceBeanConfig entity : objs) {
            EntityState state = EntityUtils.getState(entity);
            if (EntityState.DELETED.equals(state)) {
                sysServiceBeanmethodConfigService.deleteMethodConfigsByServiceId(entity.getId());
            }
        }
    }

    /**
     * 获取元数据配置的服务bean
     *
     * @return
     */
    @DataProvider
    public void findEntity(Page page, Map<String, String> parameter) {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        addPropertyFilter(propertyFilters, "objType", "2", null, true);
        if (parameter != null) {
            addPropertyFilter(propertyFilters, "objName", parameter.get("beanName"), QueryConstants.LIKE, true);
        }
        com.innofi.framework.dao.pagination.Page innofiPage = super
                .translateDoradoPageToInnofiPage(page);
        mdSysEntityObjectService.findByPage_Filters(propertyFilters, null,
                innofiPage);
    }

    /**
     * 根据bean的路径获取其元数据方法
     *
     * @param parameter
     * @return
     */
    @DataProvider
    public Collection<MdObjectMethod> findEntityMethod(
            Map<String, Object> parameter) {
        if (parameter != null) {
            String beanPath = (String) parameter.get("beanPath");
            if (null != beanPath && !"".equals(beanPath)) {
                List<MdObjectMethod> list = mdSysObjectMethodService
                        .queryBeanForList(
                                "select tt.METHOD_NAME from md_entity_object t, md_object_method tt where t.obj_id = tt.obj_id and t.obj_path = ? and t.obj_type = ?",
                                new Object[]{beanPath, "2"});

                return list;
            }
        }
        return null;
    }

    /**
     * 发布所有webservice接口
     */
    @Expose
    public void savePublishWebservice() {
        String xml = this.getWebserviceXml();
        if (null == xml || "".equals(xml)) {
            xml = GenerateXml.buildNullXmlDoc();
        }
        managerWebServiceService.deleteWebserviceXml();
        managerWebServiceService.saveWebserviceXml(xml);
    }

    /**
     * 获得services.xml的文件内容
     *
     * @return
     */
    private String getWebserviceXml() {
        String xml = "";
        List<SysServiceBeanConfig> beanList = sysServiceBeanConfigSevice
                .findByHql("from SysServiceBeanConfig sys", new Object[]{});

        if (beanList == null || beanList.size() == 0) {
            return xml;
        }

        // 需要发布接口的集合
        List<GenerateServicePojo> serviceList = new ArrayList<GenerateServicePojo>();
        for (SysServiceBeanConfig beans : beanList) {
            String serviceId = beans.getId();
            List<SysServiceBeanmethodConfig> methods = sysServiceBeanmethodConfigService
                    .findByHql(
                            "from SysServiceBeanmethodConfig method where SERVICE_ID=?",
                            new Object[]{serviceId});
            // 如果类中有方法，则发布此类为webservice，否则不发布此类为webservice
            if (null != methods && methods.size() > 0) {
                GenerateServicePojo servicePojo = new GenerateServicePojo();
                String[] methodListTmp = new String[methods.size()];
                for (int i = 0; i < methods.size(); i++) {
                    SysServiceBeanmethodConfig me = methods.get(i);
                    String methodName = me.getMethodName();
                    methodListTmp[i] = methodName;
                }
                servicePojo.setClassPath(beans.getBeanPath());
                servicePojo.setMethods(methodListTmp);
                servicePojo.setServiceId(serviceId);
                servicePojo.setServiceClass(beans.getIntfPath());
                servicePojo.setServiceName(beans.getServiceName());
                servicePojo.setSpringBeanName(beans.getBeanId());
                serviceList.add(servicePojo);
            }
        }
        xml = GenerateXml.createOperationServiceXml(serviceList);
        return xml;
    }

    /**
     * 根据类路径获取bean的接口和bean的springId
     *
     * @param beanPath
     * @return
     */
    @Expose
    public Map<String, String> getIntfPathAndBeanId(String beanPath) {
        Map<String, String> ret = null;
        if (CommonUtil.isNotEmptyString(beanPath)) {
            String beanId = CommonUtil.getBeanId(beanPath,
                    getSpringBeanFactory());
            String intfPath = CommonUtil.getintfPath(beanPath);
            if (CommonUtil.isNotEmptyString(beanId)
                    && CommonUtil.isNotEmptyString(intfPath)) {
                ret = new HashMap<String, String>();
                ret.put("beanId", beanId);
                ret.put("intfPath", intfPath);
            }

        }
        return ret;
    }

    @Expose
    public Map<String, String> getTableInfoAndObjId(String beanPath) {
        Map<String, String> ret = null;
        if (CommonUtil.isNotEmptyString(beanPath)) {
            String beanId = CommonUtil.getBeanId(beanPath, getSpringBeanFactory());
            String intfPath = CommonUtil.getintfPath(beanPath);
            List<MdTable> mdTables = this.mdSysTableService.findByHql("from MdTable where serviceName=?", new Object[]{beanPath});
            List<MdEntityObject> mdEntitys = this.mdSysEntityObjectService.findByHql("from MdEntityObject where objPath=?", new Object[]{beanPath});
            if (CommonUtil.isNotEmptyString(beanId)
                    && CommonUtil.isNotEmptyString(intfPath)
                    && CommonUtil.isNotEmptyCollection(mdTables)
                    && CommonUtil.isNotEmptyCollection(mdEntitys)) {
                ret = new HashMap<String, String>();
                MdTable mdtable = mdTables.get(0);
                String tableId = mdtable.getId();
                String tableName = mdtable.getTableName();
                ret.put("tableId", tableId);
                ret.put("tableName", tableName);
                ret.put("objId", mdEntitys.get(0).getId());
                ret.put("beanId", beanId);
                ret.put("intfPath", intfPath);
            }
        }
        return ret;
    }
}
