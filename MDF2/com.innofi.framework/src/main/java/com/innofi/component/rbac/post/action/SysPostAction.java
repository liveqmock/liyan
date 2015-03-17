package com.innofi.component.rbac.post.action;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.rbac.post.pojo.SysPost;
import com.innofi.component.rbac.org.service.ISysOrgInfoService;
import com.innofi.component.rbac.post.service.ISysPostService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;



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
 * 为dorado7界面维护操作的提供支持，实现SysPost对象信息的加载与保存操作
 */

@Component
public class SysPostAction extends BaseActionImpl {	
    @Resource(name="sysPostService")
	private ISysPostService sysPostSevice;

    @DataProvider
    public void findSysPosts(Page page, Map<String, Object> parameter) throws Exception {
	 
    	List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
    	if(parameter != null){
    		addPropertyFilter(propertyFilters,"postCode",parameter.get("postCode"),parameter.get("qMpostCode").toString(),true);  
			addPropertyFilter(propertyFilters,"postName",parameter.get("postName"),parameter.get("qMpostName").toString(),true);  
			ISysOrgInfoService sysOrgInfoService = getSpringBean("sysOrgInfoService");
			if(null!=parameter.get("orgCode")){
                List<Object> orgCodes = sysOrgInfoService.getPropertyValueList(sysOrgInfoService.getDownLevelOrgsByOrgCode((String) parameter.get("orgCode")), "orgCode");
                addPropertyFilter(propertyFilters, "orgCode", orgCodes, QueryConstants.IN, true);
            }
		}
	 
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysPostSevice.findByPage_Filters(propertyFilters, null, innofiPage);
    }
    
    @DataProvider
    public void findSysPostList(Page page, Map<String, Object> parameter) throws Exception {
	 
    	List<PropertyFilter> propertyFilters = super.buildEqPropertyFilters(parameter);
    	addPropertyFilter(propertyFilters,"status",FrameworkConstants.STATUS_EFFECTIVE,QueryConstants.EQUAL,true);//查询有效
    	if(parameter != null){
			addPropertyFilter(propertyFilters,"postName",parameter.get("postName"),QueryConstants.LIKE,true);  
    	}
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysPostSevice.findByPage_Filters(propertyFilters, null, innofiPage);

    }

    @DataProvider
    public void findSysPostForUserSecondaryPost(Page page, Map<String, Object> parameter) throws Exception {
    	String userId=(String) parameter.get("userId");
    	List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
    	addPropertyFilter(propertyFilters,"status", FrameworkConstants.STATUS_EFFECTIVE,QueryConstants.EQUAL,true);//查询有效
    	if(parameter != null){
			addPropertyFilter(propertyFilters,"postName",parameter.get("postName"),QueryConstants.LIKE,true);  
		}
    	this.removePropertyFilter(propertyFilters, "userId");
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysPostSevice.findSysPostForUserSecondaryPost(userId,propertyFilters, innofiPage);
    }
    
    @DataResolver
    public void saveSysPosts(Collection<SysPost> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			SysPost sysPost = (SysPost) iterator.next();
			EntityState state = EntityUtils.getState(sysPost);
			if (EntityState.DELETED.equals(state)) {
				sysPostSevice.delete(sysPost);
			} else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
				sysPostSevice.update(sysPost);
			} else if (EntityState.NEW.equals(state)) {
				sysPostSevice.save(sysPost);
			}
		}
    }
    
    /**
     * 验证编号
     * @param postCode
     * @return
     */
    @Expose
    public String checkCode(String postCode){
    	return sysPostSevice.checkEntityExist("postCode", postCode)+"";
    }
    
    /**
     * 岗位停用校验
     */
    @Expose
    public String invalidPost(String postId) {
        return sysPostSevice.invalidPost(postId);
    }
}
