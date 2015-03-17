/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.mdmdepend.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.framework.pojo.metadata.MdMdmDepend;
import com.innofi.component.metadata.mdmdepend.service.IMdMdmDependService;
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
public class MdMdmDependAction extends BaseActionImpl{
	@Resource
    private IMdMdmDependService mdMdmDependServcie;
	    
	/**
	 * @param page
	 * @param parameter
	 * @throws ParseException
	 * @throws Exception
	 */
	@DataProvider
	public void findMdMdmDepends(Page page, Map<String, Object> parameter) throws Exception{
    	
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	
    	if(parameter != null){
    		addPropertyFilter(filters,"mdmId",parameter.get("mdmId"),QueryConstants.EQUAL,true);
    	}
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	mdMdmDependServcie.findByPage_Filters(filters,orders,innofiPage);
    	transferCn(innofiPage);
	}
    
    @Expose
    public List findMdMdmDependByMdmId(String mdmId){
    	return mdMdmDependServcie.findMdMdmDependByMdmId(mdmId);
    }
    
	@DataResolver
	public void saveMdMdmDepends(Collection<MdMdmDepend> objs){
		
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			MdMdmDepend mdMdmDepend = (MdMdmDepend) iterator.next();
			EntityState state = EntityUtils.getState(mdMdmDepend);
			if(EntityState.NEW.equals(state)){
				mdMdmDependServcie.save(mdMdmDepend);
			}else if(EntityState.MODIFIED.equals(state)){
				mdMdmDependServcie.update(mdMdmDepend);
			}else if(EntityState.DELETED.equals(state)){
				mdMdmDependServcie.delete(mdMdmDepend);
			}
		}
	}
	/**
	 * 根据元模型ID,查找元模型依赖关系表中与条件元模型的关系匹配的数据.
	 * @param mdmId
	 * @return
	 */
	@Expose
    public String findMdMdmDependIdByMdmId(String mdmId){
		String mdmIdsString = "";
		List<MdMdmDepend> list = new ArrayList<MdMdmDepend>();
		list = mdMdmDependServcie.findMdMdmDependByMdmId(mdmId);
		if(list == null||list.size()==0){
			return "null";
		}else{
			for(int i=0;i<list.size();i++){
				if(i==list.size()-1){
					mdmIdsString += list.get(i).getdMdmId()+",";
					mdmIdsString = mdmIdsString.substring(0, mdmIdsString.length()-1);
				}else{
					mdmIdsString += list.get(i).getdMdmId()+",";
				}
			}
	    	return mdmIdsString;
		}
    }
	
	/**
	 * 
	 * @param innofiPage
	 */
	private void transferCn(com.innofi.framework.dao.pagination.Page innofiPage){
		Map map=new HashMap();
		map.put("mdmId", "mdmName");
		map.put("dMdmId", "dMdmName");
		IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
		codeTransfer.transferResult(innofiPage.getEntities(), "mdMetaModelService", map);
	}
	
}
