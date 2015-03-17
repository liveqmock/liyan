package com.innofi.component.rbac.privilage.service.impl;


import com.bstek.dorado.data.entity.EntityUtils;
import com.innofi.component.rbac.RBACConstants;
import com.innofi.component.rbac.privilage.service.ISysFunActionService;
import com.innofi.component.rbac.menu.service.ISysMenuService;
import com.innofi.component.rbac.privilage.service.ISysAuthorizeService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.component.rbac.privilage.pojo.DaTablePolicy;
import com.innofi.component.rbac.privilage.pojo.SysAuthorize;
import com.innofi.component.rbac.privilage.pojo.SysFunAction;
import com.innofi.component.rbac.menu.pojo.SysMenu;
import com.innofi.framework.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          系统权限服务实现类
 * @found date: 2010-11-23
 * @found time: 20:40:56
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value = "sysAuthorizeService")
public class SysAuthorizeServiceImpl extends BaseServiceImpl<SysAuthorize, String> implements ISysAuthorizeService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;
    @Resource
    private ISysFunActionService sysFunActionService;
    @Resource
    private ISysMenuService sysMenuService;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    public List<SysMenu> findRoleMenuByParentId(String parentMenuId, String roleId) {
    	List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        addPropertyFilter(propertyFilters, "parentMenuId", parentMenuId, QueryConstants.EQUAL, true);
        addPropertyFilter(propertyFilters, "isStart", FrameworkConstants.COMM_Y, QueryConstants.EQUAL, true);
    	List<SysMenu> menuList = sysMenuService.findByPropertyFilters(propertyFilters, new ArrayList());
        propertyFilters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(propertyFilters, "roleId", roleId, QueryConstants.EQUAL, true);
        this.addPropertyFilter(propertyFilters, "resourceType", RBACConstants.RESOURCE_TYPE_MENU, QueryConstants.EQUAL, true);
        List<SysAuthorize> sysAuthorizeList = this.findByPropertyFilters(propertyFilters, null);
        for (SysMenu menu : menuList) {
            for (SysAuthorize authorize : sysAuthorizeList) {
                if (menu.getId().equals(authorize.getResourceId())) {
                    menu.setSelectFlag(true);
                    break;
                }
            }
        }
        return menuList;
    }

    public List<SysFunAction> findMenuActionByMenuId(String menuId,
                                                     String roleId) {
        List<SysFunAction> actionList = sysFunActionService.findByProperty("menuId", menuId, QueryConstants.EQUAL);
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(propertyFilters, "roleId", roleId, QueryConstants.EQUAL, true);
        this.addPropertyFilter(propertyFilters, "resourceType", RBACConstants.RESOURCE_TYPE_ACTION, QueryConstants.EQUAL, true);
        List<SysAuthorize> sysAuthorizeList = this.findByPropertyFilters(propertyFilters, null);
        
        for (SysFunAction action : actionList) {
            for (SysAuthorize authorize : sysAuthorizeList) {
                if (action.getId().equals(authorize.getResourceId())) {
                    action.setSelectFlag(true);
                    break;
                }
            }
        }
        return actionList;
    }

    public void addMenuAuthorize(String roleId, SysMenu sysMenu) {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(propertyFilters, "resourceId", sysMenu.getId(), QueryConstants.EQUAL, true);
        this.addPropertyFilter(propertyFilters, "roleId", roleId, QueryConstants.EQUAL, true);
        this.addPropertyFilter(propertyFilters, "resourceType", RBACConstants.RESOURCE_TYPE_MENU, QueryConstants.EQUAL, true);
        List<SysAuthorize> authorizes = this.findByPropertyFilters(propertyFilters, null);
        if (authorizes != null && authorizes.size() > 0) {
            return;
        } else {
            SysAuthorize authorize = new SysAuthorize();
            authorize.setResourceId(sysMenu.getId());
            authorize.setResourceName(sysMenu.getMenuName());
            authorize.setResourceType(RBACConstants.RESOURCE_TYPE_MENU);
            authorize.setRoleId(roleId);
            this.save(authorize);
        }
    }

    public void deleteMenuAuthorize(String roleId, SysMenu sysMenu) {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(propertyFilters, "resourceId", sysMenu.getId(), QueryConstants.EQUAL, true);
        this.addPropertyFilter(propertyFilters, "roleId", roleId, QueryConstants.EQUAL, true);
        this.addPropertyFilter(propertyFilters, "resourceType", RBACConstants.RESOURCE_TYPE_MENU, QueryConstants.EQUAL, true);
        List<SysAuthorize> authorizes = this.findByPropertyFilters(propertyFilters, null);

        List<PropertyFilter> propertyFilters1 = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(propertyFilters1, "roleId", roleId, QueryConstants.EQUAL, true);
        this.addPropertyFilter(propertyFilters1, "resourceType", RBACConstants.RESOURCE_TYPE_ACTION, QueryConstants.EQUAL, true);
        List<SysAuthorize> sysAuthorizeList = this.findByPropertyFilters(propertyFilters1, null);

        for (SysAuthorize authorize : authorizes) {
            List<SysFunAction> actionList = sysFunActionService.findByProperty("menuId", authorize.getResourceId(), QueryConstants.EQUAL);
            for (SysFunAction action : actionList) {
                for (SysAuthorize authorize1 : sysAuthorizeList) {
                    if (action.getId().equals(authorize1.getResourceId())) {
                        this.delete(authorize1);
                    }
                }
            }
            this.delete(authorize);
        }
    }


    public void addActionAuthorize(String roleId, SysFunAction action) {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(propertyFilters, "roleId", roleId, QueryConstants.EQUAL, true);
        this.addPropertyFilter(propertyFilters, "resourceId", action.getId(), QueryConstants.EQUAL, true);
        this.addPropertyFilter(propertyFilters, "resourceType", RBACConstants.RESOURCE_TYPE_ACTION, QueryConstants.EQUAL, true);
        List<SysAuthorize> authorizes = this.findByPropertyFilters(propertyFilters, null);
        if (authorizes != null && authorizes.size() > 0) {
            return;
        } else {
            SysAuthorize authorize = new SysAuthorize();
            authorize.setRoleId(roleId);
            authorize.setResourceId(action.getId());
            authorize.setResourceName(action.getActionName());
            authorize.setResourceType(RBACConstants.RESOURCE_TYPE_ACTION);
            this.save(authorize);
        }
    }

    public void deleteActionAuthorize(String roleId, SysFunAction action) {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(propertyFilters, "roleId", roleId, QueryConstants.EQUAL, true);
        this.addPropertyFilter(propertyFilters, "resourceId", action.getId(), QueryConstants.EQUAL, true);
        this.addPropertyFilter(propertyFilters, "resourceType", RBACConstants.RESOURCE_TYPE_ACTION, QueryConstants.EQUAL, true);
        List<SysAuthorize> authorizes = this.findByPropertyFilters(propertyFilters, null);
        for (SysAuthorize authorize : authorizes) {
            this.delete(authorize);
        }
    }

    @Override
    public void assignFunctionPermissionToRole(String roleId, List<SysMenu> menus, List<SysFunAction> actions) {
        assignMenuToRole(menus,roleId);
        assignActionToRole(actions,roleId);
    }

    @Override
    public void assignMenuToRole(List<SysMenu> menus, String roleId) {
        for (Iterator iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu menu = (SysMenu) iterator.next();
            List<SysMenu> children = EntityUtils.getValue(menu, "children");
            //System.out.println("需要调整的的!!!"+menu.getId()+menu.getName());
            if (children != null && children.size() > 0) {
                assignMenuToRole(children, roleId);
            }
            
            if (menu.getSelectFlag()) {
                addMenuAuthorize(roleId, menu);
            } else {
                deleteMenuAuthorize(roleId, menu);
            }
            
        }
    }

    @Override
    public void assignActionToRole(List<SysFunAction> actions, String roleId) {
        for (Iterator iterator = actions.iterator(); iterator.hasNext(); ) {
            SysFunAction action = (SysFunAction) iterator.next();
            if (action.getSelectFlag()) {
                addActionAuthorize(roleId, action);

            } else {
                deleteActionAuthorize(roleId, action);
            }
        }
    }

	public void saveRoleDataPrivilege(String roleId, List<DaTablePolicy> policys) {
		for(DaTablePolicy policy:policys){
			List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
	        this.addPropertyFilter(propertyFilters, "roleId", roleId, QueryConstants.EQUAL, true);
	        this.addPropertyFilter(propertyFilters, "resourceId", policy.getId(), QueryConstants.EQUAL, true);
	        this.addPropertyFilter(propertyFilters, "resourceType", RBACConstants.RESOURCE_TYPE_TABLE, QueryConstants.EQUAL, true);
	        List<SysAuthorize> authorizes = this.findByPropertyFilters(propertyFilters, null);
	        if(policy.getSelectFlag()){
	        	if (authorizes != null && authorizes.size() > 0) {
	                continue;
	            } else {
	                SysAuthorize authorize = new SysAuthorize();
	                authorize.setRoleId(roleId);
	                authorize.setResourceId(policy.getId());
	                authorize.setResourceName(policy.getTableName());
	                authorize.setResourceType(RBACConstants.RESOURCE_TYPE_TABLE);
	                this.save(authorize);
	            }
			}else{
				for (SysAuthorize authorize : authorizes) {
		            this.delete(authorize);
		        }
			}
		}
	}
}

