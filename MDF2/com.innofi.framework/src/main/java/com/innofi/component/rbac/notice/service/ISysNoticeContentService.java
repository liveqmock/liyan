
package com.innofi.component.rbac.notice.service;

import com.innofi.component.rbac.notice.pojo.SysNoticeContent;
import com.innofi.framework.service.IBaseService;
import com.innofi.framework.dao.pagination.Page;

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
public interface ISysNoticeContentService  extends IBaseService<SysNoticeContent,String> {

	/**
	 * 分页查询当前用户角色下的公告
	 * @param innofiPage
	 */
	void findSysNoticeContentsByUserRole(Page innofiPage);
	
}