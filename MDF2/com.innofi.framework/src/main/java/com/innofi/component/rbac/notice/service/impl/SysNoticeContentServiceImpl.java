
package com.innofi.component.rbac.notice.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.innofi.component.rbac.notice.pojo.SysNoticeContent;
import com.innofi.component.rbac.notice.service.ISysNoticeContentService;
import javax.annotation.Resource;

import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.FrameworkConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.framework.spring.context.ContextHolder;


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
@Service(value="sysNoticeContentService")
public class SysNoticeContentServiceImpl  extends BaseServiceImpl<SysNoticeContent,String> implements ISysNoticeContentService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

	public void findSysNoticeContentsByUserRole(Page innofiPage) {
		SysUser user = (SysUser) ContextHolder.getContext().getLoginUser();
		List<SysRole> roles=user.getFunctionRoles();
		if(roles.size()>0){
			List<String> roleIds=new ArrayList<String>();
			for(SysRole role:roles){
				roleIds.add(role.getId());
			}
			String hql="select c from SysNoticeContent c where c.status=:status and ( exists (select 1 from SysNoticeContentRole r where c.id=r.noticeContentId and r.roleId in (:roles)) or not exists (select 1 from SysNoticeContentRole s where c.id=s.noticeContentId)) order by c.crtDate desc";
			HashMap<String,Object> paramMap=new HashMap<String, Object>();
			paramMap.put("roles", roleIds);
			paramMap.put("status", FrameworkConstants.STATUS_EFFECTIVE);
			this.findByNamedHql_Page(hql, innofiPage, paramMap);
		}
	}
}

