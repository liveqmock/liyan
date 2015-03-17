
package com.innofi.component.rbac.privilage.service;

import java.util.List;

import com.innofi.component.rbac.org.pojo.SysOrgInfo;
import com.innofi.component.rbac.privilage.pojo.SysOrgRole;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.pagination.Page;
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
public interface ISysOrgRoleService  extends IBaseService<SysOrgRole,String> {

	/**
	 * 根据orgCode查询角色
	 * @param orgCode
	 * @return
	 */
	List<SysOrgRole> findSysOrgRoleByOrgCode(String orgCode);

	/**
	 * 保存角色部门设置
	 * @param roleId
	 * @param added
	 * @param removed
	 */
	void saveSysOrgRole(String roleId, List<SysOrgInfo> added,
			List<SysOrgInfo> removed);
	

	List<SysOrgRole> findSysOrgRoleByOrgId(String orgId);

	void findSysRoleByOrgId(String orgId, List<PropertyFilter> filter, Page page);
	
}