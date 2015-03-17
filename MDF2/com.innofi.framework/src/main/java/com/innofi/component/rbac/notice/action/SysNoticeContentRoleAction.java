package com.innofi.component.rbac.notice.action;

import com.innofi.component.rbac.notice.pojo.SysNoticeContentRole;
import com.innofi.component.rbac.notice.service.ISysNoticeContentRoleService;
import com.innofi.component.rbac.workspace.infobar.pojo.SysUserInfBar;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;

/**
 * 功能/ 模块：todo
 * 
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期 todo
 *          为dorado7界面维护操作的提供支持，实现SysNoticeContentRole对象信息的加载与保存操作 修订历史： 日期 作者
 *          参考 描述 北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysNoticeContentRoleAction extends BaseActionImpl {
	@Resource
	private ISysNoticeContentRoleService sysNoticeContentRoleService;

	@DataProvider
	public void findSysNoticeContentRoles(Page page,
			Map<String, Object> parameter) throws Exception {
		List<PropertyFilter> propertyFilters = this
				.buildEqPropertyFilters(parameter);
		com.innofi.framework.dao.pagination.Page innofiPage = super
				.translateDoradoPageToInnofiPage(page);
		sysNoticeContentRoleService.findByPage_Filters(propertyFilters, null,
				innofiPage);

	}

	@DataProvider
	public List<SysNoticeContentRole> findNoticeRolesByNoticeId(String noticeId)
			throws Exception {
		List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
		this.addPropertyFilter(propertyFilters, "noticeContentId", noticeId,
				QueryConstants.EQUAL, true);
		return sysNoticeContentRoleService
				.findNoticeRolesByNoticeId(propertyFilters);

	}

	@DataProvider
	public void findSysRolesByNoticeId(Page page, Map<String, Object> parameter)
			throws Exception {
		List<PropertyFilter> propertyFilters = this
				.buildEqPropertyFilters(parameter);
		String noticeContentId = (String) parameter.get("noticeContentId");
		this.removePropertyFilter(propertyFilters, "noticeContentId");
		com.innofi.framework.dao.pagination.Page innofiPage = super
				.translateDoradoPageToInnofiPage(page);
		sysNoticeContentRoleService.findSysRolesByNoticeId(noticeContentId,
				propertyFilters, innofiPage);

	}

	@DataResolver
	public void saveSysNoticeContentRoles(Collection<SysNoticeContentRole> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			SysNoticeContentRole sysNoticeContentRole = (SysNoticeContentRole) iterator
					.next();
			EntityState state = EntityUtils.getState(sysNoticeContentRole);
			if (EntityState.DELETED.equals(state)) {
				sysNoticeContentRoleService.delete(sysNoticeContentRole);
			} else if (EntityState.MODIFIED.equals(state)
					|| EntityState.MOVED.equals(state)) {
				sysNoticeContentRoleService.update(sysNoticeContentRole);
			} else if (EntityState.NEW.equals(state)) {
				sysNoticeContentRoleService.save(sysNoticeContentRole);
			}
		}
	}
}
