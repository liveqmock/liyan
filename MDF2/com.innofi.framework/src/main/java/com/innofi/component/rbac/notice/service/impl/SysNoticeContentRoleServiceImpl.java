
package com.innofi.component.rbac.notice.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.innofi.component.rbac.notice.pojo.SysNoticeContentRole;
import com.innofi.component.rbac.notice.service.ISysNoticeContentRoleService;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.component.rbac.role.service.ISysRoleService;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
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
@Component(value="sysNoticeContentRoleService")
public class SysNoticeContentRoleServiceImpl  extends BaseServiceImpl<SysNoticeContentRole,String> implements ISysNoticeContentRoleService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

	public List<SysNoticeContentRole> findNoticeRolesByNoticeId(
			List<PropertyFilter> propertyFilters) {
		List<SysNoticeContentRole> noticeRoles=this.findByPropertyFilters(propertyFilters, null);
		ISysRoleService sysRoleService = getSpringBean("sysRoleService");
		Map<String,SysRole> roleMap=sysRoleService.findAllRolesMap();
		for(SysNoticeContentRole role:noticeRoles){
			role.setRoleName(roleMap.get(role.getRoleId()).getRoleName());
		}
		return noticeRoles;
	}
	
	public Map<String,SysNoticeContentRole> findNoticeRoleMapsByNoticeId(String noticeContentId){
		List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
    	this.addPropertyFilter(propertyFilters, "noticeContentId", noticeContentId, QueryConstants.EQUAL, true);
    	List<SysNoticeContentRole> roles=this.findByPropertyFilters(propertyFilters, null);
    	Map<String,SysNoticeContentRole> roleMap=new HashMap<String,SysNoticeContentRole>();
    	for(SysNoticeContentRole role:roles){
    		roleMap.put(role.getRoleId(), role);
    	}
    	return roleMap;
	}

	public void findSysRolesByNoticeId(String noticeContentId,
			List<PropertyFilter> propertyFilters, Page innofiPage) {
		ISysRoleService sysRoleService = getSpringBean("sysRoleService");
		sysRoleService.findByPage_Filters(propertyFilters, null, innofiPage);
		if(noticeContentId!=null){
			List<SysRole> roles=innofiPage.getEntities();
			Map<String,SysNoticeContentRole> roleMap=findNoticeRoleMapsByNoticeId(noticeContentId);
			for(SysRole role:roles){
				if(roleMap.containsKey(role.getId())){
					role.setSelectFlag(true);
				}
			}
		}
	}
}

