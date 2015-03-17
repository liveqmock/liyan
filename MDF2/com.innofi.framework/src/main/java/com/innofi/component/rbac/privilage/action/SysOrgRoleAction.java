package com.innofi.component.rbac.privilage.action;
import com.innofi.component.rbac.org.pojo.SysOrgInfo;
import com.innofi.component.rbac.privilage.pojo.SysOrgRole;
import com.innofi.component.rbac.org.service.ISysOrgInfoService;
import com.innofi.component.rbac.privilage.service.ISysOrgRoleService;

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
 * 为dorado7界面维护操作的提供支持，实现SysOrgRole对象信息的加载与保存操作
 */

@Component
public class SysOrgRoleAction extends BaseActionImpl {	
    @Resource
	private   ISysOrgRoleService sysOrgRoleService;

    @DataProvider
    public void findSysOrgRoles(Page page, Map<String, Object> parameter) throws Exception {

    List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
	 
    	if(parameter != null){
    																											}
	 
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        sysOrgRoleService.findByPage_Filters(propertyFilters, null, innofiPage);

    }
    
    @DataProvider
    public List<SysOrgInfo> findSysOrgInfoNoSetByRoleId(Map<String, Object> parameter) throws Exception {
    	String roleId=(String) parameter.get("roleId");
    	ISysOrgInfoService  sysOrgInfoService= (ISysOrgInfoService)ContextHolder.getSpringBean("sysOrgInfoService");
    	return sysOrgInfoService.findSysOrgInfoNoSetByRoleId(roleId);
    }

    @DataProvider
    public List<SysOrgInfo> findSysOrgInfoSetByRoleId(Map<String, Object> parameter) throws Exception {
    	String roleId=(String) parameter.get("roleId");
    	ISysOrgInfoService  sysOrgInfoService= (ISysOrgInfoService)ContextHolder.getSpringBean("sysOrgInfoService");
        return sysOrgInfoService.findSysOrgInfoSetByRoleId(roleId);
    }
    
    @DataProvider
    public List<SysOrgRole> findSysOrgRoleByOrgId(String orgId) throws Exception {
        return sysOrgRoleService.findSysOrgRoleByOrgId(orgId);
    }

    @DataProvider
    public void findSysRoleByOrgId(Page page, Map<String, Object> parameter) throws Exception {
    	String orgId=(String) parameter.get("orgId");
    	String orgLevel = (String)parameter.get("orgLevel");
    	List<PropertyFilter> filter=this.buildEqPropertyFilters(parameter);
    	this.removePropertyFilter(filter, "orgId");
    	this.removePropertyFilter(filter, "orgLevel");
    	
    	List<String> levelList = new ArrayList<String>();
    	levelList.add("0");//全行的角色
    	levelList.add(orgLevel);
        addPropertyFilter(filter, "roleLevel", levelList, QueryConstants.IN, true);

    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	sysOrgRoleService.findSysRoleByOrgId(orgId,filter,innofiPage);
    }
    
    @DataResolver
    public void saveSysOrgRoles(Collection<SysOrgRole> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			SysOrgRole sysOrgRole = (SysOrgRole) iterator.next();
			EntityState state = EntityUtils.getState(sysOrgRole);
			if (EntityState.DELETED.equals(state)) {
				sysOrgRoleService.delete(sysOrgRole);
			} else if (EntityState.MODIFIED.equals(state)
					|| EntityState.MOVED.equals(state)) {
				sysOrgRoleService.update(sysOrgRole);
			} else if (EntityState.NEW.equals(state)) {
				sysOrgRoleService.save(sysOrgRole);
			}
		}
    }
    
    @Expose
    public void saveSysOrgRole(Map<String, Object> parameter) {
    	String roleId=(String) parameter.get("roleId");
    	List<SysOrgInfo> added=(List<SysOrgInfo>) parameter.get("added");
    	List<SysOrgInfo> removed=(List<SysOrgInfo>) parameter.get("removed");
    	sysOrgRoleService.saveSysOrgRole(roleId,added,removed);
    } 
    //add by 毛小兵 增加一个通过机构 取角色 
    
    
}