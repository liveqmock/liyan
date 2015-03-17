
package com.innofi.component.webservice.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.component.webservice.pojo.SysServiceBeanConfig;
import com.innofi.component.webservice.service.ISysServiceBeanConfigService;


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
@Component(value="sysServiceBeanConfigService")
public class SysServiceBeanConfigServiceImpl  extends BaseServiceImpl<SysServiceBeanConfig,String> implements ISysServiceBeanConfigService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

}

