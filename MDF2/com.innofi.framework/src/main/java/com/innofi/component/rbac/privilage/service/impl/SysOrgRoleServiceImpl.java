
package com.innofi.component.rbac.privilage.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.innofi.component.rbac.org.pojo.SysOrgInfo;
import com.innofi.component.rbac.org.service.ISysOrgInfoService;
import com.innofi.component.rbac.privilage.pojo.SysOrgRole;
import com.innofi.component.rbac.privilage.service.ISysOrgRoleService;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.component.rbac.role.service.ISysRoleService;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.service.impl.BaseServiceImpl;


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
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Component(value="sysOrgRoleService")
public class SysOrgRoleServiceImpl  extends BaseServiceImpl<SysOrgRole,String> implements ISysOrgRoleService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;
	@Resource
    private ISysOrgInfoService sysOrgInfoService;
	
	public DaoSupport getDaoSupport(){
		return daoSupport;
	}
	
	public void saveSysOrgRole(String roleId,List<SysOrgInfo> adds,List<SysOrgInfo> removes){

        for(SysOrgInfo orgInfo:adds){
			SysOrgRole orgRole=new SysOrgRole();
			orgRole.setOrgId(orgInfo.getId());
			orgRole.setOrgCode(orgInfo.getOrgCode());
			orgRole.setRoleId(roleId);
			this.save(orgRole);
		}
		for(SysOrgInfo orgInfo:removes){
			List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
			this.addPropertyFilter(filters, "roleId", roleId, QueryConstants.EQUAL, true);
			this.addPropertyFilter(filters, "orgId", orgInfo.getId(), QueryConstants.EQUAL, true);
			this.deleteByPropertyFilters(filters);
		}
	}
	
	public List<SysOrgRole> findSysOrgRoleByOrgCode(String orgCode){
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "orgCode", orgCode, QueryConstants.EQUAL, true);
		List<SysOrgRole> orgRoles=this.findByPropertyFilters(filters, null);
		return orgRoles;
	}

	public List<SysOrgRole> findSysOrgRoleByOrgId(String orgId) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "orgId", orgId, QueryConstants.EQUAL, true);
		List<SysOrgRole> orgRoles=this.findByPropertyFilters(filters, null);
		ISysRoleService sysRoleService = getSpringBean("sysRoleService");
		Map<String,SysRole> roleMap=sysRoleService.findAllRolesMap();
		for(SysOrgRole orgRole:orgRoles){
			orgRole.setRoleName(roleMap.get(orgRole.getRoleId()).getRoleName());
			orgRole.setRoleType(roleMap.get(orgRole.getRoleId()).getRoleType());//add by 毛小兵 增加角色类型
			orgRole.setStatus(roleMap.get(orgRole.getRoleId()).getStatus());
		}
		return orgRoles;
	}

	public void findSysRoleByOrgId(String orgId, List<PropertyFilter> filters,
			Page page) {
		ISysRoleService sysRoleService = getSpringBean("sysRoleService");
	  	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
 	    PropertyOrder order = new PropertyOrder("roleType", QueryConstants.ORDER_ASC);
    	PropertyOrder order2 = new PropertyOrder("roleName", QueryConstants.ORDER_ASC);
 	    orders.add(order);
 	    orders.add(order2);
		sysRoleService.findByPage_Filters(filters, orders, page);
		Map<String,SysOrgRole> orgMap=findSysOrgRoleMapByOrgId(orgId);
		List<SysRole> roles=page.getEntities();
		for(SysRole role:roles){
			if(orgMap.containsKey(role.getId())){
				role.setSelectFlag(true);
			}
		}
	}

	private Map<String,SysOrgRole> findSysOrgRoleMapByOrgId(String orgId){
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "orgId", orgId, QueryConstants.EQUAL, true);
		List<SysOrgRole> orgRoles=this.findByPropertyFilters(filters, null);
		Map<String,SysOrgRole> orgRoleMap=new HashMap<String,SysOrgRole>();
		for(SysOrgRole orgRole:orgRoles){
			orgRoleMap.put(orgRole.getRoleId(), orgRole);
		}
		return orgRoleMap;
	}
	
}

