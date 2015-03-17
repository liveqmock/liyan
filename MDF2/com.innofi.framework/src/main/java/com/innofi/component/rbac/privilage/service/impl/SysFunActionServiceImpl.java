
package com.innofi.component.rbac.privilage.service.impl;


import com.innofi.component.rbac.privilage.pojo.SysFunAction;
import com.innofi.component.rbac.RBACConstants;
import com.innofi.component.rbac.privilage.service.ISysFunActionService;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.framework.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Component(value="sysFunActionService")
public class SysFunActionServiceImpl  extends BaseServiceImpl<SysFunAction,String> implements ISysFunActionService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

	public List<SysFunAction> findActionsByUserRoles(List<SysRole> userRoles) {
		if(userRoles!=null && userRoles.size()>0){
			List<String> roleIds=new ArrayList<String>();
			Set<String> rsSet=new HashSet<String>();
			Map<String, List> paramMap=new HashMap<String,List>();
			for(SysRole role:userRoles){
				roleIds.add(role.getId());
			}
			paramMap.put("roleIds", roleIds);
			String hql="select distinct f from SysAuthorize a,SysFunAction f where a.resourceId=f.id and a.resourceType='"+ RBACConstants.RESOURCE_TYPE_ACTION+"' and a.roleId in (:roleIds)";
			return this.findByNamedHql(hql, paramMap);
		}else{
			return new ArrayList();
		}
	}

}

