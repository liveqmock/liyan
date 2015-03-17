/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.dimenfield.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.framework.pojo.metadata.MdDimenField;
import com.innofi.component.metadata.dimenfield.service.IMdDimenFieldService;
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
public class MdDimenFieldAction extends BaseActionImpl{
	@Resource
    private IMdDimenFieldService mdDimenFieldServcie;
	    
	/**
	 * @param page
	 * @param parameter
	 * @throws ParseException
	 * @throws Exception
	 */
	@DataProvider
	public void findMdDimenFields(Page page, Map<String, Object> parameter) throws Exception{
    	
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
    		addPropertyFilter(filters,"dimenId",parameter.get("dimenId"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"fieldId",parameter.get("fieldId"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"fieldName",parameter.get("fieldName"),QueryConstants.LIKE,true);
    	}
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	mdDimenFieldServcie.findByPage_Filters(filters,orders,innofiPage);

	}
	@DataResolver
	public void saveMdDimenFields(Collection<MdDimenField> objs){
		
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			MdDimenField mdDimenField = (MdDimenField) iterator.next();
			EntityState state = EntityUtils.getState(mdDimenField);
			if(EntityState.NEW.equals(state)){
				mdDimenFieldServcie.save(mdDimenField);
			}else if(EntityState.MODIFIED.equals(state)){
				mdDimenFieldServcie.update(mdDimenField);
			}else if(EntityState.DELETED.equals(state)){
				mdDimenFieldServcie.delete(mdDimenField);
			}
		}
	}



}
