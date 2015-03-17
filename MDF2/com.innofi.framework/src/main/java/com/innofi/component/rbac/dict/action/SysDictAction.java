package com.innofi.component.rbac.dict.action;

import com.innofi.component.rbac.dict.pojo.SysDict;
import com.innofi.component.rbac.dict.service.ISysDictService;

import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.utils.string.StringUtil;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 为dorado7界面维护操作的提供支持，实现SysDict对象信息的加载与保存操作
 */

@Component
public class SysDictAction extends BaseActionImpl {	
    @Resource
	private   ISysDictService sysDictService;

    /**
     * 字典维护列表
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findSysDicts(Page page, Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
    	if(parameter != null){
    		addPropertyFilter(propertyFilters,"dictCode",parameter.get("dictCode"),parameter.get("qMdictCode").toString(),true);
			addPropertyFilter(propertyFilters,"dictName",parameter.get("dictName"),parameter.get("qMdictName").toString(),true);  
    	}
        addPropertyFilter(propertyFilters,"dictType","RISK_DICT",QueryConstants.NOT_EQUAL,true);
    	addPropertyFilter(propertyFilters,"parentDictId","0",QueryConstants.EQUAL,true);
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("viewSeq",QueryConstants.ORDER_ASC));

        sysDictService.findByPage_Filters(propertyFilters, orders, innofiPage);
    }

    /**
     * 参数管理左侧树（ID参数）、 字典下拉框树型结构查询（状态为有效）
     * @param parameter
     * @return
     * @throws Exception
     */
    @DataProvider
    public List<SysDict> findSysDicts(Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters =this.buildEqPropertyFilters(parameter);
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("viewSeq",QueryConstants.ORDER_ASC));
        return sysDictService.findByPropertyFilters(propertyFilters, orders);
    }
    
    /**
     * 根据父ID查询下级字典数据（字典维护页面）
     * @param parentDictId
     * @return
     * @throws Exception
     */
    @DataProvider
    public List<SysDict> findSysDictsByParentId(String parentDictId) throws Exception {
    	return sysDictService.findByProperty("parentDictId", parentDictId, QueryConstants.EQUAL);
    }
    
    /**
     * 字典下拉框-列表
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findSysDictsList(Page page,Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters =super.buildEqPropertyFilters(parameter);
    	addPropertyFilter(propertyFilters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);//状态有效
    	addPropertyFilter(propertyFilters,"dictName",parameter.get("dictName"),QueryConstants.LIKE,true);
    	removePropertyFilter(propertyFilters, "searchType");
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	sysDictService.findSysDictsList((String) parameter.get("searchType"), (String) parameter.get("parentDictId"), propertyFilters,innofiPage);
    }
    
    @Expose
    public String checkDictCode(Map<String,String> parameter){
    	String dictCode=parameter.get("dictCode");
    	String dictType=parameter.get("dictType");
    	return sysDictService.checkDictCode(dictCode,dictType)+"";
    }
    
    @DataResolver
    public void saveSysDicts(Collection<SysDict> objs) {
    	for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
    		SysDict sysDict = (SysDict) iterator.next();
            Collection<SysDict> children = EntityUtils.getValue(sysDict, "children");
            if (children != null && children.size() > 0) {
            	saveSysDicts(children);
            }
            EntityState state = EntityUtils.getState(sysDict);
            if (EntityState.NEW.equals(state)) {
            	sysDictService.save(sysDict);
            } else if (EntityState.MODIFIED.equals(state)) {
            	sysDictService.update(sysDict);
            } else if (EntityState.DELETED.equals(state)) {
            	sysDictService.delete(sysDict);
            }
        }
    }
    
	/**
	 * 根据树类型 节点编号和节点名称，查询树序列，用于前台查找树节点
	 * 
	 * @param parameter
	 * @return
	 */
	@Expose
	public Collection<String> findSysDictByNameAndCode(
			Map<String, Object> parameter) { 
		Collection<String> listCollection = new ArrayList<String>(); 
		String treeSeqQuery = "'%"+parameter.get("treeSeq")+"%'";
		String hql = "from SysDict where treeSeq like "+treeSeqQuery+" and (dictName =? or dictCode=?)";
		List<SysDict> retDicts = sysDictService.findByHql(hql, new Object[] {parameter.get("dictName"), parameter.get("dictCode") });
		if (null != retDicts && retDicts.size() > 0) {
			SysDict sysDict = retDicts.get(0);
			String treeSeq = sysDict.getTreeSeq();
			if (StringUtil.hasText(treeSeq)) {
				String[] strings = treeSeq.split("\\.");
				for (int i = 0; i < strings.length; i++) {
					if (StringUtil.hasText(strings[i])) {
						listCollection.add(strings[i]);
					}
				}
			}
		}
		return listCollection;
	}
}


