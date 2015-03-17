
package com.innofi.component.webservice.service;

import com.innofi.framework.service.IBaseService;
import com.innofi.component.webservice.pojo.SysServiceBeanmethodConfig;

/**
 * 功能/ 模块：webservice
 *
 * @author  liuhuaiyang  huaiyang.liu@innofi.com.cn
 * @version 2.0.0 13-5-10
 *          webservice接口方法处理类
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface ISysServiceBeanmethodConfigService  extends IBaseService<SysServiceBeanmethodConfig,String> {
	/**
	 * 根据serviceId 删除方法
	 * @param serviceId
	 */
	public void deleteMethodConfigsByServiceId(String serviceId);
}