package com.innofi.component.rbac.menu.service;

import com.innofi.component.rbac.menu.pojo.SysMenu;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.framework.service.IBaseService;

import java.util.List;

public interface ISysMenuService  extends IBaseService<SysMenu,String> {

    /**
     * 查找所有应用菜单
     * @return List<SysMenu>
     */
    public List<SysMenu> findApps();

    /**
     * 查找所有模块菜单
     * @return List<SysMenu>
     */
    public List<SysMenu> findModules(String appId);
    
	/**
	 * 根据用户ID查询权限菜单
	 * @param userRoles
	 * @return
	 */
	public List<SysMenu> findMenusByUserRoles(List<SysRole> userRoles);



}