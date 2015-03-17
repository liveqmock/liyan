
package com.innofi.component.rbac.userview.service.impl;


import com.innofi.component.rbac.userview.pojo.SysUserViewCriteria;
import com.innofi.component.rbac.userview.service.ISysUserViewCriteriaService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.service.impl.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;


/**
* SysUserViewCriteria服务实现类
* @author  liumy2009@126.com
* @version V1.0.0
* 修订历史：
* 日期  作者  参考  描述
* 北京名晟信息技术有限公司版权所有.
*/
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value="sysUserViewCriteriaService")
public class SysUserViewCriteriaServiceImpl  extends BaseServiceImpl<SysUserViewCriteria,String> implements ISysUserViewCriteriaService {
	
	@Resource(name="sysUserViewCriteriaDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

}

