/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.mddepend.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.framework.pojo.metadata.MdMdDepend;
import com.innofi.component.metadata.mddepend.service.IMdMdDependService;
import com.innofi.component.metadata.mdmdepend.service.IMdMdmDependService;
import com.innofi.framework.pojo.metadata.MdMetaModel;
import com.innofi.component.metadata.metamodel.service.IMdMetaModelService;
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
public class MdMdDependAction extends BaseActionImpl{
	@Resource
    private IMdMdDependService mdMdDependServcie;
	@Resource
	private IMdMetaModelService mdMetaModelServcie;
	@Resource
	private IMdMdmDependService mdMdmDependService;
	    
	/**
	 * @param page
	 * @param parameter
	 * @throws Exception
	 */
	@DataProvider
	public void findMdMdDepends(Page page, Map<String, Object> parameter) throws Exception{
    	
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	
    	if(parameter != null){
    		addPropertyFilter(filters,"mdId",parameter.get("mdId"),QueryConstants.EQUAL,true);
    	}
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	mdMdDependServcie.findByPage_Filters(filters,orders,innofiPage);
    	transferCn(innofiPage);

	}
	/**
	 * 
	* @Title: findMetaModelByModelId 
	* @Description: 通过元数据ID查找元模型的依赖关系，通过依赖关系中的元模型ID查找元模型。返回元模型对象 
	* @param @param mdId
	* @param @return MdMetaModelPojo对象
	* @return List<MdMetaModel>    返回类型
	* @throws
	 */
	@Expose
	public List<MdMetaModel> findMetaModelByModelId(String mdmId){
		List idList = mdMdmDependService.findMdMdmDependByMdmId(mdmId);
		return mdMetaModelServcie.findByIds(idList);
	}
	@Expose
	public int countMdmDepend(Map<String, Object> parameter){
		String mdmId = parameter.get("mdmId").toString();
		return mdMdmDependService.findMdMdmDependByMdmId(mdmId).size();
	}
	@DataResolver
	public void saveMdMdDepends(Collection<MdMdDepend> objs){
		
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			MdMdDepend mdMdDepend = (MdMdDepend) iterator.next();
			EntityState state = EntityUtils.getState(mdMdDepend);
			if(EntityState.NEW.equals(state)){
				mdMdDependServcie.save(mdMdDepend);
			}else if(EntityState.MODIFIED.equals(state)){
				mdMdDependServcie.update(mdMdDepend);
			}else if(EntityState.DELETED.equals(state)){
				mdMdDependServcie.delete(mdMdDepend);
			}
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
