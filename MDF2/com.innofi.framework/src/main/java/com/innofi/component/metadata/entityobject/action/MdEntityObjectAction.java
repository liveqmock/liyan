/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.entityobject.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.framework.pojo.metadata.MdEntityObject;
import com.innofi.component.metadata.entityobject.service.IMdEntityObjectService;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.spring.context.ContextHolder;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 功能/ 模块：测试模块
 *
 * @author 张磊  Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          测试Action实现类
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class MdEntityObjectAction extends BaseActionImpl{
	@Resource
    private IMdEntityObjectService mdEntityObjectServcie;
	    
	/**
	 * @param page
	 * @param parameter
	 * @throws ParseException
	 * @throws Exception
	 */
	@DataProvider
	public void findMdEntityObjects(Page page, Map<String, Object> parameter) throws Exception{
    	
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	List<String> dmdIdsList = new ArrayList<String>();
    	if(parameter != null){
    		if(parameter.get("dmdIds")!=null){
    			dmdIdsList = java.util.Arrays.asList(parameter.get("dmdIds").toString().split(","));
    			if(dmdIdsList.size()> 0){
    	           addPropertyFilter(filters,"id",dmdIdsList,QueryConstants.NOT_IN,true);
    	    	}
    		}
    		addPropertyFilter(filters,"id",parameter.get("id"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"objName",parameter.get("objName"),QueryConstants.LIKE,true);
    		addPropertyFilter(filters,"objType",parameter.get("objType"),QueryConstants.EQUAL,true);
    	}
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	mdEntityObjectServcie.findByPage_Filters(filters,orders,innofiPage);
    	transferCn(innofiPage);
	}
	
	@DataProvider
	public void findMdEntityService(Page page, Map<String, Object> parameter) throws Exception{
    	
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	List<String> dmdIdsList = new ArrayList<String>();
    	if(parameter != null){
    		addPropertyFilter(filters,"objName",parameter.get("objName"),QueryConstants.LIKE,true);
    	}else{
        	addPropertyFilter(filters,"objName","baseService",QueryConstants.NOT_EQUAL,true);
    	}
    	addPropertyFilter(filters,"objType","5",QueryConstants.EQUAL,true);
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	mdEntityObjectServcie.findByPage_Filters(filters,orders,innofiPage);

	}
	@DataResolver
	public void saveMdEntityObjects(Collection<MdEntityObject> objs){
		
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			MdEntityObject mdEntityObject = (MdEntityObject) iterator.next();
			EntityState state = EntityUtils.getState(mdEntityObject);
			if(EntityState.NEW.equals(state)){
				mdEntityObjectServcie.save(mdEntityObject);
			}else if(EntityState.MODIFIED.equals(state)){
				mdEntityObjectServcie.update(mdEntityObject);
			}else if(EntityState.DELETED.equals(state)){
				mdEntityObjectServcie.delete(mdEntityObject);
			}
		}
	}
	/**
	 * 
	 * @param innofiPage
	 */
	private void transferCn(com.innofi.framework.dao.pagination.Page innofiPage){
		Map map=new HashMap();
		map.put("interFace", "interFaceName");
		IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
		codeTransfer.transferResult(innofiPage.getEntities(), "mdEntityObjectService", map);
	}


}
