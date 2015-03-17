/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.tablerelate.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.component.metadata.metamodel.service.IMdMetaModelService;
import com.innofi.framework.pojo.metadata.MdTableRelate;
import com.innofi.component.metadata.tablerelate.service.IMdTableRelateService;
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
public class MdTableRelateAction extends BaseActionImpl{
	@Resource
    private IMdTableRelateService mdTableRelateServcie;
	    
	/**
	 * @param page
	 * @param parameter
	 * @throws ParseException
	 * @throws Exception
	 */
	@DataProvider
	public void findMdTableRelates(Page page, Map<String, Object> parameter) throws Exception{
    	
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
    		addPropertyFilter(filters,"tableRelateId",parameter.get("tableRelateId"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"mainTableId",parameter.get("mainTableId"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"mainTableName",parameter.get("mainTableName"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"mainField",parameter.get("mainField"),QueryConstants.LIKE,true);
    		addPropertyFilter(filters,"foreignTableName",parameter.get("foreignTableName"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"foreignTableId",parameter.get("foreignTableId"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"foreignField",parameter.get("foreignField"),QueryConstants.LIKE,true);
    		addPropertyFilter(filters,"relateType",parameter.get("relateType"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"corrType",parameter.get("corrType"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"isCrt",parameter.get("isCrt"),QueryConstants.EQUAL,true);
    	}
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	mdTableRelateServcie.findByPage_Filters(filters,orders,innofiPage);

	}
	@DataResolver
	public void saveMdTableRelates(Collection<MdTableRelate> objs){
		
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			MdTableRelate mdTableRelate = (MdTableRelate) iterator.next();
			EntityState state = EntityUtils.getState(mdTableRelate);
			if(EntityState.NEW.equals(state)){
				mdTableRelateServcie.save(mdTableRelate);
			}else if(EntityState.MODIFIED.equals(state)){
				mdTableRelateServcie.update(mdTableRelate);
			}else if(EntityState.DELETED.equals(state)){
				mdTableRelateServcie.delete(mdTableRelate);
			}
		}
	}

	/**
	 * 通过表名查找跟本表有主外键关系的表.
	 * @param parameter
	 * @return
	 */
	@Expose
	public String findTableRelateByName(Map<String, Object> parameter){
		String savePathString = ""; 
		String mainTableName = parameter.get("savePath").toString();
		List<MdTableRelate> list = mdTableRelateServcie.findByProperty("mainTableName", mainTableName, QueryConstants.EQUAL);
		if(list == null||list.size()==0){
			return "null";
		}else{
			for(int i=0;i<list.size();i++){
				if(i==list.size()-1){
					savePathString += list.get(i).getForeignTableName()+",";
					savePathString = savePathString.substring(0, savePathString.length()-1);
				}else{
					savePathString += list.get(i).getForeignTableName()+",";
				}
			}
	    	return savePathString;
		}
	}
	
	/**
	 * 
	 * @param parameter
	 * @return
	 */
	@Expose
	public String checkMetaModelRelate(Map<String, Object> parameter){
		IMdMetaModelService mdMetaModelService = getSpringBean("mdMetaModelService");
		String mainTableName = mdMetaModelService.findByProperty("id", parameter.get("mainTableNameId"), QueryConstants.EQUAL).get(0).getSavePath();
		String foreignTableName = parameter.get("foreignTableName").toString();
		return mdTableRelateServcie.checkMetaModelRelate(mainTableName, foreignTableName);
	}
}
