package com.innofi.component.rbac.privilage.action;

import com.innofi.component.rbac.privilage.pojo.DaTablePolicy;
import com.innofi.component.rbac.privilage.pojo.SysFunAction;
import com.innofi.component.rbac.menu.pojo.SysMenu;
import com.innofi.component.rbac.privilage.pojo.SysAuthorize;
import com.innofi.component.rbac.privilage.service.ISysAuthorizeService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import com.innofi.framework.service.IBaseService;
import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 为dorado7界面维护操作的提供支持，实现SysAuthorize对象信息的加载与保存操作
 */

@Component
public class SysAuthorizeAction extends BaseActionImpl {



    @Resource
    private ISysAuthorizeService sysAuthorizeService;

    @DataProvider
    public void findSysAuthorizes(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysAuthorizeService.findByPage_Filters(propertyFilters, null, innofiPage);
    }

    @DataProvider
    public List<SysMenu> findRoleMenuByParentId(Map<String, Object> parameter) throws Exception {
    	String parentMenuId = (String) parameter.get("parentMenuId");
        String roleId = (String) parameter.get("roleId");
        return sysAuthorizeService.findRoleMenuByParentId(parentMenuId, roleId);
    }

    @DataProvider
    public List<SysFunAction> findMenuActionByMenuId(Map<String, Object> parameter) throws Exception {
        return sysAuthorizeService.findMenuActionByMenuId((String) parameter.get("menuId"), (String) parameter.get("roleId"));
    }

    /**
     * 分配菜单、按钮权限
     */
    @DataResolver
    public void saveRoleMenus(List<SysMenu> menus, List<SysFunAction> actions, Map parameter) {
        String roleId = (String) parameter.get("roleId");
        sysAuthorizeService.assignFunctionPermissionToRole(roleId, menus, actions);
    }

    @DataResolver
    public void saveRoleDataPrivilege(List<DaTablePolicy> policys,String roleId) {
        sysAuthorizeService.saveRoleDataPrivilege(roleId, policys);
    }
    
    @DataResolver
    public void saveSysAuthorizes(Collection<SysAuthorize> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			SysAuthorize sysAuthorize = (SysAuthorize) iterator.next();
			EntityState state = EntityUtils
					.getState(sysAuthorize);
			if (EntityState.DELETED.equals(state)) {
				sysAuthorizeService.delete(sysAuthorize);
			} else if (EntityState.MODIFIED.equals(state)
					|| EntityState.MOVED.equals(state)) {
				sysAuthorizeService.update(sysAuthorize);
			} else if (EntityState.NEW.equals(state)) {
				sysAuthorizeService.save(sysAuthorize);
			}
		}
    }

    @Override
    public IBaseService getBusinessService() {
        return sysAuthorizeService;
    }
}
