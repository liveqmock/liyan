
package com.innofi.component.rbac.privilage.service;

import java.util.List;

import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.component.rbac.privilage.pojo.SysUserRole;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.framework.service.IBaseService;

/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface ISysUserRoleService  extends IBaseService<SysUserRole,String> {
	
	/**
	 * 根据用户ID查询角色列表，已分配的selectFlag为true
	 * @param userId
	 * @return
	 */
	List<SysRole> findRoleListByUserId(String userId);

	/**
	 * 根据用户ID查询已分配的角色
	 * @param userId
	 * @return
	 */
	List<SysUserRole> findSysUserRoleByUserId(String userId);


	/**
	 * 保存角色用户设置
	 * @param roleId
	 * @param added
	 * @param removed
	 */
	void saveSysUserRole(String roleId, List<SysUser> added,
			List<SysUser> removed);
	
	/**
	 * 
	 */
	void findSelectedSysRoleByUserId(String userId, List<PropertyFilter> filters,Page page) ;
}