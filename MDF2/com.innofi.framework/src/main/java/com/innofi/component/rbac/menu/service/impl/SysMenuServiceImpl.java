
package com.innofi.component.rbac.menu.service.impl;


import com.innofi.component.rbac.RBACConstants;
import com.innofi.component.rbac.menu.service.ISysMenuService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.component.rbac.menu.pojo.SysMenu;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.spring.context.ContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value = "sysMenuService")
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu, String> implements ISysMenuService {

    @Resource(name = "sysMenuDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport() {
        return daoSupport;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.menu.service.ISysMenuService#findApps()
     */
    public List<SysMenu> findApps() {
        List<SysMenu> rsMenus = new ArrayList<SysMenu>();
        if (ContextHolder.getSystemProperties().getBoolean("mdf.system.permission.switch")) { //是否进行权限控制
            List<SysMenu> userMenus = (List<SysMenu>) ContextHolder.getRequest().getSession().getAttribute(FrameworkConstants.SESSION_USER_MENU);
            for (SysMenu menu : userMenus) {
                if ("0".equals(menu.getParentMenuId())) {
                    rsMenus.add(menu);
                }
            }
        } else {
            List<PropertyFilter> filters = buildPropertyFilter("parentMenuId", "0", QueryConstants.EQUAL);
            List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
            PropertyOrder order = new PropertyOrder("menuSeq", QueryConstants.ORDER_ASC);
            orders.add(order);
            rsMenus = findByPropertyFilters(filters, orders);
        }
        return rsMenus;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.menu.service.ISysMenuService#findModules(String appId)
     */
    public List<SysMenu> findModules(String appId) {
        List<SysMenu> rsMenus = new ArrayList<SysMenu>();
        if (ContextHolder.getSystemProperties().getBoolean("mdf.system.permission.switch")) { //是否进行权限控制
            try {
                List<SysMenu> userMenus = (List<SysMenu>) ContextHolder.getRequest().getSession().getAttribute(FrameworkConstants.SESSION_USER_MENU);
                for (SysMenu menu : userMenus) {
                    if (appId.equals(menu.getParentMenuId())) {
                        rsMenus.add(menu);
                    }
                }
            } catch (Exception e) {

            }
        } else {
            List<PropertyFilter> filters = buildPropertyFilter("parentMenuId", appId, QueryConstants.EQUAL);
            List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
            PropertyOrder order = new PropertyOrder("menuSeq", QueryConstants.ORDER_ASC);
            orders.add(order);
            rsMenus = findByPropertyFilters(filters, orders);
        }
        return rsMenus;
    }


    public List<SysMenu> findMenusByUserRoles(List<SysRole> userRoles) {
        if (userRoles != null && userRoles.size() > 0) {
            List<String> roleIds = new ArrayList<String>();
            Map<String, List> paramMap = new HashMap<String, List>();
            for (SysRole role : userRoles) {
                roleIds.add(role.getId());
            }
            paramMap.put("roleIds", roleIds);
            String hql = "select distinct f from SysAuthorize a,SysMenu f where a.resourceId=f.id and f.isStart = '1' and a.resourceType='" + RBACConstants.RESOURCE_TYPE_MENU + "' and a.roleId in (:roleIds) order by f.parentMenuId,f.menuSeq";
            return this.findByNamedHql(hql, paramMap);
        } else {
            return new ArrayList();
        }
    }
    
}

