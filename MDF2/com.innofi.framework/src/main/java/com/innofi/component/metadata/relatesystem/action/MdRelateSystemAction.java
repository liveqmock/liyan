/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.relatesystem.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.framework.pojo.metadata.MdRelateSystem;
import com.innofi.component.metadata.relatesystem.service.IMdRelateSystemService;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
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
public class MdRelateSystemAction extends BaseActionImpl{
	@Resource
    private IMdRelateSystemService mdRelateSystemServcie;
	    
	/**
	 * @param page
	 * @param parameter
	 * @throws ParseException
	 * @throws Exception
	 */
	@DataProvider
	public void findMdRelateSystems(Page page, Map<String, Object> parameter) throws Exception{
    	
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

			addPropertyFilter(filters, "id", parameter.get("id"),QueryConstants.EQUAL, true);
			addPropertyFilter(filters, "systemCode", parameter.get("systemCode"),QueryConstants.LIKE, true);
			addPropertyFilter(filters, "systemName", parameter.get("systemName"),QueryConstants.LIKE, true);
			addPropertyFilter(filters, "systemType", parameter.get("systemType"),QueryConstants.EQUAL, true);
			addPropertyFilter(filters, "status", parameter.get("status"),QueryConstants.EQUAL, true);
    	}
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	mdRelateSystemServcie.findByPage_Filters(filters,orders,innofiPage);

	}
	@DataResolver
	public void saveMdRelateSystems(Collection<MdRelateSystem> objs){
		
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			MdRelateSystem mdRelateSystem = (MdRelateSystem) iterator.next();
			EntityState state = EntityUtils.getState(mdRelateSystem);
			if(EntityState.NEW.equals(state)){
				mdRelateSystemServcie.save(mdRelateSystem);
			}else if(EntityState.MODIFIED.equals(state)){
				mdRelateSystemServcie.update(mdRelateSystem);
			}else if(EntityState.DELETED.equals(state)){
				mdRelateSystemServcie.delete(mdRelateSystem);
			}
		}
	}



}
