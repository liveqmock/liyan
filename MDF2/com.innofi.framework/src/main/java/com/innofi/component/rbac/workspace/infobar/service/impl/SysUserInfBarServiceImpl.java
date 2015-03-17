
package com.innofi.component.rbac.workspace.infobar.service.impl;


import com.innofi.component.rbac.workspace.infobar.pojo.SysUserInfBar;
import com.innofi.component.rbac.workspace.infobar.service.ISysUserInfBarService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innofi.framework.dao.DaoSupport;
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
@Service(value="sysUserInfBarService")
public class SysUserInfBarServiceImpl  extends BaseServiceImpl<SysUserInfBar,String> implements ISysUserInfBarService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}
}

