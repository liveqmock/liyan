package com.innofi.component.rbac.privilage.action;
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
import com.innofi.component.rbac.org.service.ISysOrgInfoService;
import com.innofi.component.rbac.post.service.ISysPostService;
import com.innofi.component.rbac.privilage.service.ISysPostRoleService;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.component.rbac.org.pojo.SysOrgInfo;
import com.innofi.component.rbac.post.pojo.SysPost;
import com.innofi.component.rbac.privilage.pojo.SysPostRole;
import com.innofi.framework.spring.context.ContextHolder;


/**
 * 为dorado7界面维护操作的提供支持，实现SysPostRole对象信息的加载与保存操作
 */

@Component
public class SysPostRoleAction extends BaseActionImpl {	
    @Resource
	private   ISysPostRoleService sysPostRoleService;
    @Resource
    private   ISysOrgInfoService sysOrgInfoService;

    @DataProvider
    public void findSysPostRoles(Page page, Map<String, Object> parameter) throws Exception {

    List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
	 
    	if(parameter != null){
    																					}
	 
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        sysPostRoleService.findByPage_Filters(propertyFilters, null, innofiPage);

    }
    
    @DataProvider
    public List<SysPost> findSysPostNoSetByRoleId(Map<String, Object> parameter) throws Exception {
    	String roleId=(String) parameter.get("roleId");
    	ISysPostService  sysPostService= (ISysPostService)ContextHolder.getSpringBean("sysPostService");
    	return sysPostService.findSysPostNoSetByRoleId(roleId);
    	
    }

    @DataProvider
    public List<SysPost> findSysPostSetByRoleId(Map<String, Object> parameter) throws Exception {
    	String roleId=(String) parameter.get("roleId");
    	ISysPostService  sysPostService= (ISysPostService)ContextHolder.getSpringBean("sysPostService");
        return sysPostService.findSysPostSetByRoleId(roleId);
    }
    
    
    @DataProvider
    public List<SysPostRole> findSysPostRoleByPostId(String postId) throws Exception {
        return sysPostRoleService.findSysPostRoleByPostId(postId);
    }
    
    @DataProvider

    public void findSysRoleByPostId(Page page, Map<String, Object> parameter) throws Exception {
    	String postId=(String) parameter.get("postId");
    	String orgCode=(String) parameter.get("orgCode");
    	
    	List<PropertyFilter> filter=this.buildEqPropertyFilters(parameter);
    	this.removePropertyFilter(filter, "postId");
    	this.removePropertyFilter(filter, "orgCode");
    	SysOrgInfo org = sysOrgInfoService.findUniqueByProperty("orgCode",orgCode, QueryConstants.EQUAL);
    	List<String> levelList = new ArrayList<String>();
    	levelList.add("0");//全行的角色
    	levelList.add(org.getOrgLevel());

        addPropertyFilter(filter, "roleLevel", levelList, QueryConstants.IN, true);
    	
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	sysPostRoleService.findSelectedSysRoleByPostId(postId,filter,innofiPage);
    }
    
    
    
    @DataResolver
    public void saveSysPostAddRoles(Collection<SysPostRole> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			SysPostRole sysPostRole = (SysPostRole) iterator.next();
			EntityState state = EntityUtils.getState(sysPostRole);
			if (EntityState.DELETED.equals(state)) {
				sysPostRoleService.delete(sysPostRole);
			} else if (EntityState.MODIFIED.equals(state)
					|| EntityState.MOVED.equals(state)) {
				sysPostRoleService.update(sysPostRole);
			} else if (EntityState.NEW.equals(state)) {
				sysPostRoleService.save(sysPostRole);
			}
		}
    }
    
    @Expose
    public void saveSysPostRoles(Map<String, Object> parameter) {
    	String roleId=(String) parameter.get("roleId");
    	List<SysPost> added=(List<SysPost>) parameter.get("added");
    	List<SysPost> removed=(List<SysPost>) parameter.get("removed");
    	sysPostRoleService.saveSysPostRole(roleId,added,removed);
    }
}
