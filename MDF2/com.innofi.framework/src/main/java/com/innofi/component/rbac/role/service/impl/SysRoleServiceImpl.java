package com.innofi.component.rbac.role.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.innofi.component.rbac.privilage.service.ISysOrgRoleService;
import com.innofi.component.rbac.privilage.service.ISysPostRoleService;
import com.innofi.component.rbac.privilage.service.ISysUserRoleService;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.component.rbac.role.service.ISysRoleService;
import com.innofi.component.rbac.user.service.ISysUserService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.component.rbac.privilage.pojo.SysOrgRole;
import com.innofi.component.rbac.privilage.pojo.SysPostRole;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.component.rbac.privilage.pojo.SysUserRole;



@Component(value = "sysRoleService")
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole, String> implements ISysRoleService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    public Map<String, SysRole> findAllRolesMap() {
        Map<String, SysRole> roleMap = new HashMap<String, SysRole>();
        List<SysRole> roleList = getDaoSupport().getHibernateDao().getAll(getEntityType());
        for (SysRole role : roleList) {
            roleMap.put(role.getId(), role);
        }
        return roleMap;
    }

    public List<SysRole> findAllRolesByUserId(String userId) {
        ISysUserService sysUserService = getSpringBean("sysUserService");
        ISysOrgRoleService sysOrgRoleService = getSpringBean("sysOrgRoleService");
        ISysPostRoleService sysPostRoleService = getSpringBean("sysPostRoleService");
        ISysUserRoleService sysUserRoleService = getSpringBean("sysUserRoleService");

        Map<String, SysRole> roleMap = new HashMap<String, SysRole>();
        //查询所有角色列表
        Map<String, SysRole> allRoleMap = this.findAllRolesMap();
        //根据用户ID查询所属部门，根据部门查询所拥有的角色
        SysUser user = sysUserService.get(userId);
        List<SysOrgRole> orgRoles = sysOrgRoleService.findSysOrgRoleByOrgCode(user.getOrgCode());
        for (SysOrgRole orgRole : orgRoles) {
            roleMap.put(orgRole.getRoleId(), allRoleMap.get(orgRole.getRoleId()));
        }
        //根据用户ID查询所拥有岗位，根据岗位查询所拥有的角色
        List<SysPostRole> postRoles = sysPostRoleService.findSysPostRoleByUserId(userId);
        for (SysPostRole postRole : postRoles) {
            roleMap.put(postRole.getRoleId(), allRoleMap.get(postRole.getRoleId()));
        }
        //查询用户拥有的角色
        List<SysUserRole> userRoles = sysUserRoleService.findSysUserRoleByUserId(userId);
        for (SysUserRole userRole : userRoles) {
            roleMap.put(userRole.getRoleId(), allRoleMap.get(userRole.getRoleId()));
        }
        //将Map转为List
        List<SysRole> roles = new ArrayList<SysRole>();
        for (Iterator<Entry<String, SysRole>> itor = roleMap.entrySet().iterator(); itor.hasNext(); ) {
        	//判断角色状态 排除已作废的
        	SysRole role=itor.next().getValue();
        	if(FrameworkConstants.STATUS_EFFECTIVE.equals(role.getStatus())){
        		roles.add(role);
        	}
        }
        return roles;
    }

	@Override
	public String getCnFieldName() {
		return "roleName";
	}



}

