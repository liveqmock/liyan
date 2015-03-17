package com.innofi.component.webservice.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.innofi.framework.pojo.BasePojo;

/**
 * 功能/ 模块： 模块中文名称
 * 
 * @author  liuhuaiyang huaiyang.liu@innofi.com.cn
 * @version 2.0.0 13-5-10
 *          查询webservice配置bean对象 
 *          修订历史： 日期 作者 参考 描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Entity
@Table(name = "SYS_SERVICE_BEAN_CONFIG")

public class SysServiceBeanConfig extends BasePojo {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 发布的接口类所对应Spring的bean id
	 */
	private String beanId;

	/**
	 * 发布接口类的路径
	 */
	private String beanPath;

	/**
	 * 创建日期
	 */
	private Date crtDate;

	/**
	 * 创建组织
	 */
	private String crtOrgCode;

	/**
	 * 创建用户
	 */
	private String crtUserCode;

	/**
	 * 接口路径
	 */
	private String intfPath;

	/**
	 * 服务描述
	 */
	private String serviceDesc;

	/**
	 * 服务名称
	 */
	private String serviceName;

	/**
	 * 修改日期
	 */
	private Date updDate;

	/**
	 * 修改组织
	 */
	private String updOrgCode;

	/**
	 * 修改人
	 */
	private String updUserCode;

	@Column(name = "BEAN_ID")
	public String getBeanId() {
		return this.beanId;
	}

	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}

	@Column(name = "BEAN_PATH")
	public String getBeanPath() {
		return this.beanPath;
	}

	public void setBeanPath(String beanPath) {
		this.beanPath = beanPath;
	}

	@Column(name = "CRT_DATE")
	public Date getCrtDate() {
		return this.crtDate;
	}

	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}

	@Column(name = "CRT_ORG_CODE")
	public String getCrtOrgCode() {
		return this.crtOrgCode;
	}

	public void setCrtOrgCode(String crtOrgCode) {
		this.crtOrgCode = crtOrgCode;
	}

	@Column(name = "CRT_USER_CODE")
	public String getCrtUserCode() {
		return this.crtUserCode;
	}

	public void setCrtUserCode(String crtUserCode) {
		this.crtUserCode = crtUserCode;
	}

	@Column(name = "INTF_PATH")
	public String getIntfPath() {
		return this.intfPath;
	}

	public void setIntfPath(String intfPath) {
		this.intfPath = intfPath;
	}

	@Column(name = "SERVICE_DESC")
	public String getServiceDesc() {
		return this.serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "SERVICE_ID")
	public String getId() {
		return super.getId();
	}

	@Column(name = "SERVICE_NAME")
	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Column(name = "UPD_DATE")
	public Date getUpdDate() {
		return this.updDate;
	}

	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}

	@Column(name = "UPD_ORG_CODE")
	public String getUpdOrgCode() {
		return this.updOrgCode;
	}

	public void setUpdOrgCode(String updOrgCode) {
		this.updOrgCode = updOrgCode;
	}

	@Column(name = "UPD_USER_CODE")
	public String getUpdUserCode() {
		return this.updUserCode;
	}

	public void setUpdUserCode(String updUserCode) {
		this.updUserCode = updUserCode;
	}

}