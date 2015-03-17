package com.innofi.component.webservice.service;

/**
 * 功能/ 模块： webservice
 *
 * @author  liuhuaiyang huaiyang.liu@innofi.com.cn
 * @version 2.0.0 13-5-10
 *          处理webservice服务发布的xml服务类
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface IManageWebservice {

	/**
	 * 查询xml配置
	 * @return
	 */
	public String getWebserviceXml();
	
	/**
	 * 保存xml配置
	 * @param xml
	 */
	public void saveWebserviceXml(String xml);
	
	/**
	 * 删除xml配置
	 */
	public void deleteWebserviceXml();
	
}
