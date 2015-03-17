/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.objectmethod.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.framework.pojo.metadata.MdEntityObject;
import com.innofi.component.metadata.entityobject.service.IMdEntityObjectService;
import com.innofi.framework.pojo.metadata.MdObjectMethod;
import com.innofi.component.metadata.objectmethod.service.IMdObjectMethodService;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.spring.context.ContextHolder;

import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
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
public class MdObjectMethodAction extends BaseActionImpl{
	@Resource
    private IMdObjectMethodService mdObjectMethodServcie;
	@Resource
    private IMdEntityObjectService mdEntityObjectServcie;
	private   ListOrderedMap allObjectMethods;
    private   IdfCodeTransfer idfCodeTransfer;
	    
	/**
	 * @param page
	 * @param parameter
	 * @throws ParseException
	 * @throws Exception
	 */
	@DataProvider
	public void findMdObjectMethods(Page page, Map<String, Object> parameter) throws Exception{
    	
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	List<String> dmdIdsList = new ArrayList<String>();
    	if(parameter != null){
    		if(parameter.get("objName")!=null){
    			List<String> idList = new ArrayList<String>();
    			List<MdEntityObject> mdEntityObjectList = mdEntityObjectServcie.findByProperty("objName", parameter.get("objName"), QueryConstants.LIKE, orders);
    			if(mdEntityObjectList.size()>0){
	    			for(MdEntityObject entity : mdEntityObjectList){
	    				idList.add(entity.getId());
	    			}
	    			addPropertyFilter(filters,"objId",idList,QueryConstants.IN,true);
    			}
    		}else{
				addPropertyFilter(filters,"objId",parameter.get("objId"),QueryConstants.IN,true);
			}
    		if(parameter.get("dmdIds")!=null){
    			dmdIdsList = java.util.Arrays.asList(parameter.get("dmdIds").toString().split(","));
    			if(dmdIdsList.size()> 0){
    	           addPropertyFilter(filters,"id",dmdIdsList,QueryConstants.NOT_IN,true);
    	    	}
    		}
    		
    		addPropertyFilter(filters,"methodCode",parameter.get("methodCode"),QueryConstants.LIKE,true);
    		addPropertyFilter(filters,"methodParameter",parameter.get("methodParameter"),QueryConstants.LIKE,true);
    		addPropertyFilter(filters,"methodOprType",parameter.get("methodOprType"),QueryConstants.EQUAL,true);
    	}
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	mdObjectMethodServcie.findByPage_Filters(filters,orders,innofiPage);
    	transferCn(innofiPage);

	}
	@DataProvider
	public void lookServiceImplMethod(Page page, Map<String, Object> parameter)
	{
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    	if(parameter != null){
    		
    		addPropertyFilter(filters,"methodName",parameter.get("methodName"),QueryConstants.LIKE,true);
        	addPropertyFilter(filters,"objId",parameter.get("objId"),QueryConstants.EQUAL,true);

    	}
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	mdObjectMethodServcie.findByPage_Filters(filters,null,innofiPage);
	}
	
	@DataProvider
	public void lookBaseServiceImplMethod(Page page, Map<String, Object> parameter)
	{
		String currServiceImpl = parameter.get("objName").toString();
		List ids = new ArrayList<String>();
		List<MdEntityObject> currServicelist =mdEntityObjectServcie.findByProperty("objName", currServiceImpl,QueryConstants.EQUAL);
		List<MdEntityObject> baseServicelist = mdEntityObjectServcie.findByProperty("objName","BaseServiceImpl", QueryConstants.EQUAL);
		
		idfCodeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
	    allObjectMethods = idfCodeTransfer.getCacheObjects("mdObjectMethodService");
	    ListOrderedMap currServiceMethods = idfCodeTransfer.listDistinctFilter(allObjectMethods, "objId", currServicelist.get(0).getId(), null);
	    ListOrderedMap baseServiceMethods = idfCodeTransfer.listDistinctFilter(allObjectMethods, "objId", baseServicelist.get(0).getId(), null);
	   // int size = currServiceMethods.size()-baseServiceMethods.size()>=0?currServiceMethods.size():baseServiceMethods.size();

	    for (Iterator it = currServiceMethods.keySet().iterator(); it.hasNext(); ) {
	    	MdObjectMethod currMdObjectMethod = (MdObjectMethod) currServiceMethods.get(it.next());
	    	
	    	StringBuffer newMethodParameter = new StringBuffer();
	    	String methodTypeString="";
	    	if(currMdObjectMethod.getMethodParameter()!=null&&currMdObjectMethod.getMethodParameter().length()!=0)
	    	{
	    		String[] currStr = currMdObjectMethod.getMethodParameter().split(",");
		    	for(int i =0;i<currStr.length;i++)
		    	{
		    		if("com.innofi.framework.pojo.BasePojo".equals(currStr[i])){
		    			//removeMehodList.add(currMdObjectMethod.getId());
		    			mdObjectMethodServcie.delete(currMdObjectMethod.getId());
		    			
		    		}else {
		    			try {
							 Object object =  Class.forName(currStr[i]).newInstance();
							 if(object instanceof BasePojo){
								 newMethodParameter.append("com.innofi.framework.pojo.BasePojo"+",");
							 }else{
								 newMethodParameter.append(currStr[i]+",");
							 }
							 
						} catch (InstantiationException e) {
							newMethodParameter.append(currStr[i]+",");
						} catch (IllegalAccessException e) {
							newMethodParameter.append(currStr[i]+",");
						} catch (ClassNotFoundException e) {
							newMethodParameter.append(currStr[i]+",");
							//e.printStackTrace();
						}
		    		}
		    	}
		    	if(newMethodParameter.length()!=0)
		    	{
		    	methodTypeString = newMethodParameter.substring(0, newMethodParameter.length()-1);
		    	}
	    	}
	    	
	    	 for(Iterator baseit = baseServiceMethods.keySet().iterator(); baseit.hasNext();)
	    	 {
	    		 MdObjectMethod baseMdObjectMethod = (MdObjectMethod) baseServiceMethods.get(baseit.next());
	    		 if(currMdObjectMethod.getMethodName().equals(baseMdObjectMethod.getMethodName()))
	    		 {
	    			 String baseType =baseMdObjectMethod.getMethodParameter()==null?"":baseMdObjectMethod.getMethodParameter();
	    			
    				 if(methodTypeString.length()==baseType.length()&&methodTypeString.equals(baseType))
					{
    					 System.out.println("   id "+baseMdObjectMethod.getId());
    					 ids.add(baseMdObjectMethod.getId());
					}
	    		 }
	    	 }
	    	
	    }
	    
	    
	    
	    	List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
	    	addPropertyFilter(filters,"id",ids,QueryConstants.NOT_IN,true);
        	addPropertyFilter(filters,"objId",baseServicelist.get(0).getId(),QueryConstants.EQUAL,true);

    	
        	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        	mdObjectMethodServcie.findByPage_Filters(filters,null,innofiPage);
	}
	@DataResolver
	public void saveMdObjectMethods(Collection<MdObjectMethod> objs){
		
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			MdObjectMethod mdObjectMethod = (MdObjectMethod) iterator.next();
			EntityState state = EntityUtils.getState(mdObjectMethod);
			if(EntityState.NEW.equals(state)){
				mdObjectMethodServcie.save(mdObjectMethod);
			}else if(EntityState.MODIFIED.equals(state)){
				mdObjectMethodServcie.update(mdObjectMethod);
			}else if(EntityState.DELETED.equals(state)){
				mdObjectMethodServcie.delete(mdObjectMethod);
			}
		}
	}
	/**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.objectmethod.service.IMdObjectMethodService#refreshObjecMethod(String,String)
     */
	@Expose
	public String refreshMethod(Map<String, Object> parameter){
		if(parameter != null){
			List<String> objectIds = java.util.Arrays.asList(parameter.get("objectIds").toString().split(","));
			List<String> pojos = java.util.Arrays.asList(parameter.get("pojos").toString().split(","));
			for(int i = 0; i < objectIds.size(); i++){
				try {
					mdObjectMethodServcie.refreshObjecMethod(objectIds.get(i), pojos.get(i));
					
				} catch (ClassNotFoundException e) {
					MessageFormat.format("找不到对应的类:",pojos.get(i));
					return "0";
				}
			}
			return "1";
		}else{
			try {
				mdObjectMethodServcie.refreshAllObjecMethod();
			} catch (ClassNotFoundException e) {
				return "0";
			}

			return "1";
		}
	}
	/**
	 * 
	 * @param innofiPage
	 */
	private void transferCn(com.innofi.framework.dao.pagination.Page innofiPage){
		Map map=new HashMap();
		map.put("objId", "objName");
		IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
		codeTransfer.transferResult(innofiPage.getEntities(), "mdEntityObjectService", map);
	}
}
