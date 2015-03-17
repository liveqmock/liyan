package com.innofi.component.webservice.pojo;

/**
 * 功能/ 模块： 模块中文名称
 * 
 * @author  liuhuaiyang huaiyang.liu@innofi.com.cn
 * @version 2.0.0 13-5-10
 *          webservice配置对象
 *          修订历史： 日期 作者 参考 描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class GenerateServicePojo {
	private String serviceId;
	private String serviceName;
	private String springBeanName;
	private String serviceClass;
	private String classPath;
	private String[] methods;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSpringBeanName() {
		return springBeanName;
	}

	public void setSpringBeanName(String springBeanName) {
		this.springBeanName = springBeanName;
	}

	public String getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String[] getMethods() {
		return methods;
	}

	public void setMethods(String[] methods) {
		this.methods = methods;
	}
}
