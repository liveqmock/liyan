/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.dimension.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.framework.pojo.metadata.MdDimenField;
import com.innofi.framework.pojo.metadata.MdDimension;
import com.innofi.component.metadata.dimenfield.service.IMdDimenFieldService;
import com.innofi.component.metadata.dimension.service.IMdDimensionService;
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
public class MdDimensionAction extends BaseActionImpl{
	@Resource
    private IMdDimensionService mdDimensionServcie;
	@Resource
    private IMdDimenFieldService mdDimenFieldServcie;
	
	/**
	 * @param page
	 * @param parameter
	 * @throws ParseException
	 * @throws Exception
	 */
	@DataProvider
	public void findMdDimensions(Page page, Map<String, Object> parameter) throws Exception{
    	
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	PropertyOrder propertyOrder = new PropertyOrder("tableName","desc");
    	orders.add(propertyOrder);
    	List<String> dmdIdsList = new ArrayList<String>();
    	if(parameter != null){
    		if(parameter.get("dmdIds")!=null){
    			dmdIdsList = java.util.Arrays.asList(parameter.get("dmdIds").toString().split(","));
    			if(dmdIdsList.size()> 0){
    	           addPropertyFilter(filters,"id",dmdIdsList,QueryConstants.NOT_IN,true);
    	    	}
    		}
    		addPropertyFilter(filters,"tableId",parameter.get("tableId"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"dimenName",parameter.get("dimenName"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"dimenType",parameter.get("dimenType"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"isDimenDefault",parameter.get("isDimenDefault"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"status",parameter.get("status"),QueryConstants.EQUAL,true);
    	}
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	mdDimensionServcie.findByPage_Filters(filters,orders,innofiPage);

	}
	@DataResolver
	public void saveMdDimensions(Collection<MdDimension> objs){
		
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			MdDimension mdDimension = (MdDimension) iterator.next();
			EntityState state = EntityUtils.getState(mdDimension);
			if(EntityState.NEW.equals(state)){
				mdDimensionServcie.save(mdDimension);
			}else if(EntityState.MODIFIED.equals(state)){
				mdDimensionServcie.update(mdDimension);
			}else if(EntityState.DELETED.equals(state)){
		    	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
				List<MdDimenField> list = mdDimenFieldServcie.findByProperty("dimenId", mdDimension.getId(),QueryConstants.EQUAL, orders);
				for(MdDimenField mdDimenField:list){
						mdDimenFieldServcie.delete(mdDimenField.getId());
				}
				mdDimensionServcie.delete(mdDimension);
			}
		}
	}
	/**
	 * 传入tableId和dimenType获取dimenId，在查找dimenId对应dimenField表中的已经匹配了的字段信息。
	 * @param parameter
	 * @return
	 */
	@Expose
	public String findDimenFieldIds(Map<String, Object> parameter){
		String tableId = parameter.get("tableId").toString();
		//String dimenType = parameter.get("dimenType").toString();
		String dimenIdString = mdDimensionServcie.findDimenFieldIdsByDimenId(mdDimensionServcie.findIdByTableIdAndDimenType(tableId));
		return dimenIdString;
	}
}
