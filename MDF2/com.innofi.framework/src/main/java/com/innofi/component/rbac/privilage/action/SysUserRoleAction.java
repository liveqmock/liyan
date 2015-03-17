package com.innofi.component.rbac.privilage.action;
import com.innofi.component.rbac.org.service.ISysOrgInfoService;
import com.innofi.component.rbac.privilage.service.ISysUserRoleService;
import com.innofi.component.rbac.user.service.ISysUserService;
import com.innofi.component.rbac.org.pojo.SysOrgInfo;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.component.rbac.privilage.pojo.SysUserRole;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.spring.context.ContextHolder;

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
 * 为dorado7界面维护操作的提供支持，实现SysUserRole对象信息的加载与保存操作
 */

@Component
public class SysUserRoleAction extends BaseActionImpl {	
    @Resource
	private   ISysUserRoleService sysUserRoleService;
    @Resource
    private   ISysOrgInfoService sysOrgInfoService;
    

    @DataProvider
    public void findSysUserRoles(Page page, Map<String, Object> parameter) throws Exception {

    List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysUserRoleService.findByPage_Filters(propertyFilters, null, innofiPage);
    }

    @DataProvider
    public List<SysRole> findUserRolesByUserId(String userId) throws Exception {
    	return sysUserRoleService.findRoleListByUserId(userId);
    }
    
    
    @DataProvider
    public List<SysUserRole> findSysUserRoleByUserId(String userId) throws Exception {
        return sysUserRoleService.findSysUserRoleByUserId(userId);
    }
    
    
    @DataProvider
    public List<SysUser> findSysUserNoSetByRoleId(Map<String, Object> parameter) throws Exception {
    	String roleId=(String) parameter.get("roleId");
    	ISysUserService sysUserService = (ISysUserService)ContextHolder.getSpringBean("sysUserService");
    	return sysUserService.findSysUserNoSetByRoleId(roleId);
    }

    @DataProvider
    public List<SysUser> findSysUserSetByRoleId(Map<String, Object> parameter) throws Exception {
    	String roleId=(String) parameter.get("roleId");
      	ISysUserService sysUserService = (ISysUserService)ContextHolder.getSpringBean("sysUserService");
        return sysUserService.findSysUserSetByRoleId(roleId);
    }
    
    @Expose
    public void saveSysUserRoles(Map<String, Object> parameter) {
    	String roleId=(String) parameter.get("roleId");
    	List<SysUser> added=(List<SysUser>) parameter.get("added");
    	List<SysUser> removed=(List<SysUser>) parameter.get("removed");
    	sysUserRoleService.saveSysUserRole(roleId,added,removed);
    }

    @DataProvider
    public void findSysRoleByUserId(Page page, Map<String, Object> parameter) throws Exception {
    	String userId=(String) parameter.get("userId");
    	String orgCode=(String) parameter.get("orgCode");
    	List<PropertyFilter> filter=this.buildEqPropertyFilters(parameter);
    	this.removePropertyFilter(filter, "userId");
    	this.removePropertyFilter(filter, "orgCode");
    	
    	SysOrgInfo org = sysOrgInfoService.findUniqueByProperty("orgCode",orgCode, QueryConstants.EQUAL);
    	List<String> levelList = new ArrayList<String>();
    	levelList.add("0");//全行的角色
    	levelList.add(org.getOrgLevel());

        addPropertyFilter(filter, "roleLevel", levelList, QueryConstants.IN, true);
    	
    	
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	sysUserRoleService.findSelectedSysRoleByUserId(userId,filter,innofiPage);
    }
    
    @DataResolver
    public void saveSysUserAddRoles(Collection<SysUserRole> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			SysUserRole sysUserRole = (SysUserRole) iterator.next();
			EntityState state = EntityUtils.getState(sysUserRole);
			if (EntityState.DELETED.equals(state)) {
				sysUserRoleService.delete(sysUserRole);
			} else if (EntityState.MODIFIED.equals(state)
					|| EntityState.MOVED.equals(state)) {
				sysUserRoleService.update(sysUserRole);
			} else if (EntityState.NEW.equals(state)) {
				sysUserRoleService.save(sysUserRole);
			}
		}
    }

}
