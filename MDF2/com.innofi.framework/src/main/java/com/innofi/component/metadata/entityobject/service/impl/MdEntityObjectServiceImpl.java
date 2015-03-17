/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.entityobject.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.innofi.component.metadata.objectmethod.service.IMdObjectMethodService;
import com.innofi.framework.utils.validate.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innofi.component.metadata.commons.MetadataConstants;
import com.innofi.component.metadata.entityobject.service.IMdEntityObjectService;
import com.innofi.component.metadata.table.service.IMdTableService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.cache.service.ISysCacheConfigService;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.pojo.metadata.MdEntityObject;
import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.framework.service.IBaseService;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.file.FileUtil;
import com.innofi.framework.utils.string.StringUtil;

/**
 * 功能/ 模块：测试模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value = "mdEntityObjectService")
public class MdEntityObjectServiceImpl extends BaseServiceImpl<MdEntityObject, String> implements IMdEntityObjectService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    @Override
    public String getCnFieldName() {
        return "objName";
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.entityobject.service.IMdEntityObjectService#refreshProgramMetadata(String)
     */
    public String refreshProgramMetadata(String codePath) {
        Map<String, String> classNameMapping = new HashMap<String, String>();
        List<File> files = null;
        try {
            files = FileUtil.recursionLoadingFiles(new File(codePath), "java,xml", true);
        } catch (IOException e) {
            return "解析文件路径出错！";
        }
        StringBuffer fileListLog = new StringBuffer();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            String absolutePath = StringUtil.replace(file.getAbsolutePath(), "\\", "/");
            if (absolutePath.indexOf("com/innofi") == -1) continue;
            String className = "";
            String classSimpleName = "";
            if (absolutePath.indexOf(".view.xml") == -1) {
                className = StringUtil.replace(absolutePath.substring(absolutePath.indexOf("com/innofi"), absolutePath.lastIndexOf(".")), "/", ".");
                classSimpleName = className.substring((className.lastIndexOf(".") + 1));
            } else {
                className = StringUtil.replace(absolutePath.substring(absolutePath.indexOf("com/innofi"), absolutePath.lastIndexOf(".view")), "/", ".") + ".d";
                String tempName = className.substring(0, className.lastIndexOf(".d"));
                classSimpleName = tempName.substring((tempName.lastIndexOf(".") + 1));
            }

            classNameMapping.put(classSimpleName.toLowerCase(), className);
            fileListLog.append((i + 1) + "  文件绝对路径[" + file.getAbsolutePath() + "]  类名[" + className + "]\r\n");
        }

        try {
            FileUtil.write("c:/fileList.log", fileListLog.toString());
        } catch (IOException e) {
            e.printStackTrace(); //输出文件日志
        }

        IMdTableService mdTableService = getSpringBean("mdTableService");
        List<MdTable> mdTables = mdTableService.findMdTableByStatus(FrameworkConstants.STATUS_EFFECTIVE);

        if (mdTables.size() > 0) {
            int index = 0;
            for (MdTable mdTable : mdTables) {
                String tableName = mdTable.getTableName();
                String pojoName = StringUtil.convertPropertyName(tableName);
                String daoName = (StringUtil.convertPropertyName(tableName) + "DaoSupport");
                String serviceName = ("I" + StringUtil.upperFirst(StringUtil.convertPropertyName(tableName)) + "Service");
                String serviceImplName = (StringUtil.convertPropertyName(tableName) + "ServiceImpl");
                String actionName = (StringUtil.convertPropertyName(tableName) + "Action");
                String viewName = (StringUtil.convertPropertyName(tableName) + "Manage");

                String pojoPath = classNameMapping.get(pojoName.toLowerCase());
                String daoPath = classNameMapping.get(daoName.toLowerCase());
                String servicePath = classNameMapping.get(serviceName.toLowerCase());
                String serviceImplPath = classNameMapping.get(serviceImplName.toLowerCase());
                String actionPath = classNameMapping.get(actionName.toLowerCase());
                String viewPath = classNameMapping.get(viewName.toLowerCase());

                if (pojoPath == null) continue;
                if (pojoPath.endsWith(".d")) {
                    pojoPath = StringUtil.replace(pojoPath.substring(0, pojoPath.indexOf(".d")), "view", "pojo");
                }

                //创建实体对象元数据
                saveOrUpdateObjectMetaData(pojoPath, pojoName, MetadataConstants.OBJECT_TYPE_ENTITY, BasePojo.class.getName());
                //创建DaoSuport元数据信息
                saveOrUpdateObjectMetaData(daoPath, daoName, MetadataConstants.OBJECT_TYPE_DAO, DaoSupport.class.getName());
                //创建Service接口对象信息元数据信息
                saveOrUpdateObjectMetaData(servicePath, serviceName, MetadataConstants.OBJECT_TYPE_INTERFACE, IBaseService.class.getName());
                //创建Service实现对象信息元数据信息
                saveOrUpdateObjectMetaData(serviceImplPath, serviceImplName, MetadataConstants.OBJECT_TYPE_SERVICE, BaseServiceImpl.class.getName());
                //创建Action对象信息元数据信息
                saveOrUpdateObjectMetaData(actionPath, actionName, MetadataConstants.OBJECT_TYPE_ACTION, BaseActionImpl.class.getName());
                //创建UI对象信息元数据信息
                saveOrUpdateObjectMetaData(viewPath, viewName, MetadataConstants.OBJECT_TYPE_UI, null);

                mdTable.setEntityName(pojoPath);
                mdTable.setDaoName(daoPath);
                mdTable.setServiceName(serviceImplPath);
                mdTable.setActionName(actionPath);
                mdTable.setUiName(viewPath);

                ConsoleUtil.info((++index) + ".pojo name is [" + pojoName + "] path is [" + pojoPath + "]");
                ConsoleUtil.info((index) + ".dao name is [" + daoName + "] path is [" + daoPath + "]");
                ConsoleUtil.info((index) + ".service name is [" + serviceName + "] path is [" + servicePath + "]");
                ConsoleUtil.info((index) + ".service impl name is [" + serviceImplName + "] path is [" + serviceImplPath + "]");
                ConsoleUtil.info((index) + ".action name is [" + actionName + "] path is [" + actionPath + "]");
                ConsoleUtil.info((index) + ".view name is [" + viewName + "] path is [" + viewPath + "]");
            }

            mdTableService.updateForBranch(mdTables);
            ISysCacheConfigService sysCacheConfigService = ContextHolder.getSpringBean("sysCacheConfigService");
            sysCacheConfigService.reloadCacheByCacheServiceBeanName("mdTableService");
            sysCacheConfigService.reloadCacheByCacheServiceBeanName("mdEntityObjectService");
        }


        return "刷新成功!";
    }


    /*
    * 获取视图名包含类路径
    */
    private String getViewPath(String packageName, String modelName) {
        return packageName + ".view." + modelName + "Manage.d";
    }

    /*
    * 获取Action类名包含类路径
    */
    private String getActionPath(String packageName, String modelName) {
        return packageName + ".action." + modelName + "Action";
    }

    /*
    * 获取业务服务实现类名包含类路径
    */
    private String getServiceImplPath(String packageName, String modelName) {
        return packageName + ".service.impl." + modelName + "ServiceImpl";
    }

    /*
    * 获取业务服务类名包含类路径
    */
    private String getServicePath(String packageName, String modelName) {
        return packageName + ".service.I" + modelName + "Service";
    }

    /*
    * 获取DAO对象类名包含类路径
    */
    private String getDaoPath(String packageName, String modelName) {
        return packageName + ".dao." + modelName + "DaoSupport";
    }

    /*
     * 获取实体对象类名包含类路径
     */
    private String getEntityPath(String packageName, String modelName) {
        return packageName + ".pojo." + modelName;
    }


    /*
     * 保存或更新对象元数据
     */
    private void saveOrUpdateObjectMetaData(String objectPath, String objectName, String objType, String parentClassName) {
    	if(objectPath!=null){
	        MdEntityObject mdEntity = findUniqueByProperty("objName", objectName, QueryConstants.EQUAL);
	        IMdObjectMethodService mdObjectMethodService = getSpringBean("mdObjectMethodService");
	        if (mdEntity == null) {
	            mdEntity = new MdEntityObject();
	            mdEntity.setObjName(objectName);
	            mdEntity.setObjPath(objectPath);
	            mdEntity.setObjType(objType);
	            if(Validator.isNotNull(parentClassName)){
	                String basePojoId = findUniqueByProperty("objPath", parentClassName, QueryConstants.EQUAL).getId();
	                mdEntity.setInterFace(basePojoId);
	            }
	            save(mdEntity);
	        } else {
	            mdEntity.setObjName(objectName);
	            mdEntity.setObjPath(objectPath);
	            mdEntity.setObjType(objType);
	            if(Validator.isNotNull(parentClassName)){
	                String basePojoId = findUniqueByProperty("objPath", parentClassName, QueryConstants.EQUAL).getId();
	                mdEntity.setInterFace(basePojoId);
	            }
	            update(mdEntity);
	        }
	        if(objType.equals(MetadataConstants.OBJECT_TYPE_SERVICE)){
	            try {
	            	System.out.println("id["+mdEntity.getId()+"]"+"path ["+mdEntity.getObjPath()+"]");
	                mdObjectMethodService.refreshObjecMethod(mdEntity.getId(),mdEntity.getObjPath());
	            } catch (ClassNotFoundException e) {
	                e.printStackTrace();
	            }
	        }
    	}
    }

}
