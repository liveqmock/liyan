/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.objectmethod.service.impl;

import java.lang.reflect.Method;
import java.util.List;

import com.innofi.component.metadata.commons.MetadataConstants;
import com.innofi.component.metadata.entityobject.service.IMdEntityObjectService;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.metadata.MdEntityObject;
import com.innofi.framework.pojo.metadata.MdObjectMethod;
import com.innofi.component.metadata.objectmethod.service.IMdObjectMethodService;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.service.impl.BaseServiceImpl;

import javax.annotation.Resource;

import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.validate.Validator;

import org.springframework.stereotype.Component;

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
@Component(value = "mdObjectMethodService")
public class MdObjectMethodServiceImpl extends BaseServiceImpl<MdObjectMethod, String> implements IMdObjectMethodService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.objectmethod.service.IMdObjectMethodService#refreshObjecMethod(String, String)
     */
    public void refreshObjecMethod(String objectId, String pojo){
    	if(Validator.isNotEmpty(pojo)){
	        Class clazz;
			try {
				clazz = Class.forName(pojo);
			} catch (ClassNotFoundException e) {
				return;
			}
	        Method[] methods = clazz.getDeclaredMethods();
	        for (Method method : methods) {
	            MdObjectMethod mdObjectMethod = new MdObjectMethod();
	            mdObjectMethod.setObjId(objectId);
	            mdObjectMethod.setMethodName(method.getName());
	            if (method.getParameterTypes() == null) {
	                mdObjectMethod.setMethodParameter("");
	            } else {
	                String methodParameter = "";
	                for (int i = 0; i < method.getParameterTypes().length; i++) {
	                    if (i == method.getParameterTypes().length - 1) {
	                        methodParameter = methodParameter + method.getParameterTypes()[i].getName();
	                    } else {
	                        methodParameter = methodParameter + method.getParameterTypes()[i].getName() + ",";
	                    }
	                }
	                mdObjectMethod.setMethodParameter(methodParameter);
	            }
	            mdObjectMethod.setMethodReturn(method.getReturnType().getName());
	            if (method.getName().toLowerCase().indexOf("add") > -1) {
	                mdObjectMethod.setMethodOprType(MetadataConstants.METHOD_ADD);
	            } else if (method.getName().toLowerCase().indexOf("update") > -1) {
	                mdObjectMethod.setMethodOprType(MetadataConstants.METHOD_UPDATE);
	            } else if (method.getName().toLowerCase().indexOf("delete") > -1) {
	                mdObjectMethod.setMethodOprType(MetadataConstants.METHOD_DEL);
	            } else if (method.getName().toLowerCase().indexOf("find") > -1 || method.getName().toLowerCase().indexOf("query") > -1) {
	                mdObjectMethod.setMethodOprType(MetadataConstants.METHOD_LOOK);
	            }
	            save(mdObjectMethod);
        	}
        }
    }

    @Override
    public String getCnFieldName() {
        return "objName";
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.objectmethod.service.IMdObjectMethodService#refreshAllObjecMethod()
     */
    public void refreshAllObjecMethod() throws ClassNotFoundException {
        IMdEntityObjectService mdEntityObjectServcie = ContextHolder.getSpringBean("mdEntityObjectService");
        List<MdEntityObject> objects = mdEntityObjectServcie.findByProperty("objType", MetadataConstants.OBJECT_TYPE_INTERFACE, QueryConstants.EQUAL);
        for (int i = 0; i < objects.size(); i++) {
            refreshObjecMethod(objects.get(i).getId(), objects.get(i).getObjPath());
        }
    }
}
