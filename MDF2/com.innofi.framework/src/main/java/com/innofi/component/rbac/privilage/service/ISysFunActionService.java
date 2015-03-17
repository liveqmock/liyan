
package com.innofi.component.rbac.privilage.service;

import java.util.List;

import com.innofi.component.rbac.privilage.pojo.SysFunAction;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.framework.service.IBaseService;

public interface ISysFunActionService  extends IBaseService<SysFunAction,String> {

	/**
	 * 根据角色列表查询按钮权限
	 * @param userRoles
	 * @return
	 */
	List<SysFunAction> findActionsByUserRoles(List<SysRole> userRoles);
	
}