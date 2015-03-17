package com.innofi.component.webservice.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.component.webservice.pojo.SysServiceBeanmethodConfig;
import com.innofi.component.webservice.service.ISysServiceBeanmethodConfigService;

/**
 * 功能/ 模块：todo 模块中文名称
 * 
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期 todo 类描述 修订历史： 日期 作者 参考 描述 北京名晟信息技术有限公司版权所有.
 */
@Component(value = "sysServiceBeanmethodConfigService")
public class SysServiceBeanmethodConfigServiceImpl extends BaseServiceImpl<SysServiceBeanmethodConfig, String> implements ISysServiceBeanmethodConfigService {

	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

	public void deleteMethodConfigsByServiceId(String serviceId) {
		getDaoSupport().getJdbcDao().update("delete from SYS_SERVICE_BEANMETHOD_CONFIG m where m.service_id=?", new Object[] { serviceId });
	}
}
