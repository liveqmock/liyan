
package com.innofi.component.rbac.notice.service;

import java.util.List;

import com.innofi.component.rbac.notice.pojo.SysNoticeContentRole;
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
public interface ISysNoticeContentRoleService  extends IBaseService<SysNoticeContentRole,String> {

	List<SysNoticeContentRole> findNoticeRolesByNoticeId(List<PropertyFilter> propertyFilters);

	void findSysRolesByNoticeId(String noticeContentId,List<PropertyFilter> propertyFilters,Page innofiPage);
	
}