package com.innofi.component.rbac.privilage.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.innofi.component.rbac.privilage.pojo.SysUserRole;
import com.innofi.component.rbac.privilage.service.ISysUserRoleService;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.component.rbac.role.service.ISysRoleService;
import com.innofi.component.rbac.user.pojo.SysUser;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.service.impl.BaseServiceImpl;


/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Component(value = "sysUserRoleService")
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRole, String> implements ISysUserRoleService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    public List<SysRole> findRoleListByUserId(String userId) {
        ISysRoleService sysRoleService = getSpringBean("sysRoleService");
        List<SysRole> roleList = sysRoleService.findByPropertyFilters(null, null);
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(propertyFilters, "userId", userId, QueryConstants.EQUAL, true);
        List<SysUserRole> userRoleList = this.findByPropertyFilters(propertyFilters, null);
        for (SysRole role : roleList) {
            for (SysUserRole userRole : userRoleList) {
                if (role.getId().equals(userRole.getRoleId())) {
                    role.setSelectFlag(true);
                    break;
                }
            }
        }
        return roleList;
    }

    public List<SysUserRole> findSysUserRoleByUserId(String userId) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "userId", userId, QueryConstants.EQUAL, true);
        
		List<SysUserRole> userRoles=this.findByPropertyFilters(filters, null);
		ISysRoleService sysRoleService = getSpringBean("sysRoleService");
		Map<String,SysRole> roleMap=sysRoleService.findAllRolesMap();
		for(SysUserRole userRole:userRoles){
			userRole.setRoleName(roleMap.get(userRole.getRoleId()).getRoleName());
			userRole.setRoleType(roleMap.get(userRole.getRoleId()).getRoleType());//add by 毛小兵 增加角色类型
			userRole.setStatus(roleMap.get(userRole.getRoleId()).getStatus());
		}

        return userRoles;
    }

    public void saveSysUserRole(String roleId, List<SysUser> adds, List<SysUser> removes) {
        for (SysUser user : adds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(user.getId());
            userRole.setUserCode(user.getUserCode());
            userRole.setRoleId(roleId);
            this.save(userRole);
        }
        for (SysUser user : removes) {
            List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
            this.addPropertyFilter(filters, "roleId", roleId, QueryConstants.EQUAL, true);
            this.addPropertyFilter(filters, "userId", user.getId(), QueryConstants.EQUAL, true);
            this.deleteByPropertyFilters(filters);
        }
    }
    
    
    
    /**
	 * 通过岗位找到匹配的角色，并勾选在PAGE页中
	 */
	public void findSelectedSysRoleByUserId(String userId, List<PropertyFilter> filters,
			Page page) {
		ISysRoleService sysRoleService = getSpringBean("sysRoleService");
	  	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
 	    PropertyOrder order = new PropertyOrder("roleType", QueryConstants.ORDER_ASC);
    	PropertyOrder order2 = new PropertyOrder("roleName", QueryConstants.ORDER_ASC);
 	    orders.add(order);
 	    orders.add(order2);
		sysRoleService.findByPage_Filters(filters, orders, page);
		Map<String,SysUserRole> postMap=findSysUserRoleMapByUserId(userId);
		List<SysRole> roles=page.getEntities();
		for(SysRole role:roles){
			if(postMap.containsKey(role.getId())){
				role.setSelectFlag(true);
			}
		}
	}

	private Map<String,SysUserRole> findSysUserRoleMapByUserId(String userId){
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "userId", userId, QueryConstants.EQUAL, true);
		List<SysUserRole> userRoles=this.findByPropertyFilters(filters, null);
		Map<String,SysUserRole> userRoleMap=new HashMap<String,SysUserRole>();
		for(SysUserRole userRole:userRoles){
			userRoleMap.put(userRole.getRoleId(), userRole);
		}
		return userRoleMap;
	}
}

