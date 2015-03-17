package com.innofi.component.rbac.privilage.service;

import java.util.List;

import com.innofi.component.rbac.privilage.pojo.DaTablePolicy;
import com.innofi.component.rbac.privilage.pojo.SysFunAction;
import com.innofi.component.rbac.menu.pojo.SysMenu;
import com.innofi.component.rbac.privilage.pojo.SysAuthorize;
import com.innofi.framework.service.IBaseService;

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
public interface ISysAuthorizeService extends IBaseService<SysAuthorize, String> {

    /**
     * 查询下级菜单，根据角色资源表添加菜单选中状态
     *
     * @param parentMenuId
     * @param roleId
     */
    List<SysMenu> findRoleMenuByParentId(String parentMenuId, String roleId);

    /**
     * 查询菜单按钮列表，根据角色资源表添加按钮选中状态
     *
     * @param menuId
     * @param roleId
     * @return
     */
    List<SysFunAction> findMenuActionByMenuId(String menuId, String roleId);

    /**
     * 添加角色菜单资源
     *
     * @param roleId
     * @param sysMenu
     */
    void addMenuAuthorize(String roleId, SysMenu sysMenu);

    /**
     * 删除已添加角色菜单资源 同时删除该菜单下已添加按钮
     *
     * @param roleId
     * @param sysMenu
     */
    void deleteMenuAuthorize(String roleId, SysMenu sysMenu);

    /**
     * 添加角色按钮资源
     *
     * @param roleId
     * @param action
     */
    void addActionAuthorize(String roleId, SysFunAction action);

    /**
     * 删除已添加角色按钮资源
     *
     * @param roleId
     * @param action
     */
    void deleteActionAuthorize(String roleId, SysFunAction action);

    void assignFunctionPermissionToRole(String roleId , List<SysMenu> menus , List<SysFunAction> actions);

    /**
     * 为角色分配菜单
     * @param menus  菜单列表
     * @param roleId 角色编号
     */
    void assignMenuToRole(List<SysMenu> menus, String roleId);

    /**
     * 为角色分配按钮
     * @param actions 按钮列表
     * @param roleId  角色编号
     */
    void assignActionToRole(List<SysFunAction> actions, String roleId);

    /**
     * 保存角色数据权限
     * @param roleId
     * @param policys
     */
	void saveRoleDataPrivilege(String roleId, List<DaTablePolicy> policys);

}