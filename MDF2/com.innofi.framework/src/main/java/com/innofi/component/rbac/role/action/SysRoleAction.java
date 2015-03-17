package com.innofi.component.rbac.role.action;

import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.component.rbac.role.service.ISysRoleService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
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
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 为dorado7界面维护操作的提供支持，实现SysRole对象信息的加载与保存操作
 */

@Component
public class SysRoleAction extends BaseActionImpl {
    @Resource
    private ISysRoleService sysRoleService;

    @DataProvider
    public void findSysRoles(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        if (parameter != null) {
            addPropertyFilter(propertyFilters, "roleCode", parameter.get("roleCode"), (String) parameter.get("qMroleCode"), true);
            addPropertyFilter(propertyFilters, "roleName", parameter.get("roleName"), (String) parameter.get("qMroleName"), true);
            addPropertyFilter(propertyFilters, "roleLevel", parameter.get("roleLevel"), QueryConstants.EQUAL, true);
            if (parameter.get("qMstatus") != null) {
                addPropertyFilter(propertyFilters, "status", parameter.get("status"), parameter.get("qMstatus").toString(), true);
            } else {
                addPropertyFilter(propertyFilters, "status", parameter.get("status"), QueryConstants.EQUAL, true);
            }
            if (parameter.get("qMroleType") != null) {
                addPropertyFilter(propertyFilters, "roleType", parameter.get("roleType"), (String) parameter.get("qMroleType"), true);
            } else {
                addPropertyFilter(propertyFilters, "roleType", parameter.get("roleType"), QueryConstants.EQUAL, true);
            }
        }
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        PropertyOrder order = new PropertyOrder("roleType", QueryConstants.ORDER_ASC);
        PropertyOrder order2 = new PropertyOrder("roleName", QueryConstants.ORDER_ASC);
        orders.add(order);
        orders.add(order2);
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysRoleService.findByPage_Filters(propertyFilters, orders, innofiPage);
    }

    @DataProvider
    public void findSysRolesForWf(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        if (parameter != null) {
            addPropertyFilter(propertyFilters, "roleCode", parameter.get("roleCode"), (String) parameter.get("qMroleCode"), true);
            addPropertyFilter(propertyFilters, "roleName", parameter.get("roleName"), (String) parameter.get("qMroleName"), true);
            if (parameter.get("roleLevel") != null && !parameter.get("roleLevel").equals("0"))
                addPropertyFilter(propertyFilters, "roleLevel", parameter.get("roleLevel"), QueryConstants.EQUAL, true);
            if (parameter.get("qMstatus") != null) {
                addPropertyFilter(propertyFilters, "status", parameter.get("status"), parameter.get("qMstatus").toString(), true);
            } else {
                addPropertyFilter(propertyFilters, "status", parameter.get("status"), QueryConstants.EQUAL, true);
            }
            if (parameter.get("qMroleType") != null) {
                addPropertyFilter(propertyFilters, "roleType", parameter.get("roleType"), (String) parameter.get("qMroleType"), true);
            } else {
                addPropertyFilter(propertyFilters, "roleType", parameter.get("roleType"), QueryConstants.EQUAL, true);
            }
        }
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        PropertyOrder order = new PropertyOrder("roleType", QueryConstants.ORDER_ASC);
        PropertyOrder order2 = new PropertyOrder("roleName", QueryConstants.ORDER_ASC);
        orders.add(order);
        orders.add(order2);
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysRoleService.findByPage_Filters(propertyFilters, orders, innofiPage);
    }


    @DataResolver
    public void saveSysRoles(Collection<SysRole> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			SysRole sysRole = (SysRole) iterator.next();
			EntityState state = EntityUtils.getState(sysRole);
			if (EntityState.DELETED.equals(state)) {
				sysRoleService.delete(sysRole);
			} else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
				sysRoleService.update(sysRole);
			} else if (EntityState.NEW.equals(state)) {
				sysRoleService.save(sysRole);
			}
		}
    }
}
