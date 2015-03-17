
package com.innofi.component.rbac.role.service;

import java.util.List;
import java.util.Map;

import com.innofi.framework.service.IBaseService;
import com.innofi.component.rbac.role.pojo.SysRole;

public interface ISysRoleService  extends IBaseService<SysRole,String> {

	Map<String, SysRole> findAllRolesMap();

	/**
	 * 根据用户Code查询所拥有的角色
	 * @param userCode
	 * @return
	 */
    List<SysRole> findAllRolesByUserId(String userCode);

	
}