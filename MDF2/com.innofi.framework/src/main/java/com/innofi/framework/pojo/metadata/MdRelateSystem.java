/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.pojo.metadata;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import com.innofi.framework.pojo.BasePojo;

/**
 * 功能/ 模块：测试模块
 *
 * @author 张磊 alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          测试实体对象
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Entity
@Table(name="MD_RELATE_SYSTEM")
public class MdRelateSystem extends BasePojo {
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="SYSTEM_ID")
	public String getId(){
		return super.getId();
	}
		@Column(name="CRT_ORG_CODE")
	public String getCrtOrgCode() {
		return super.getCrtOrgCode();
	}
	@Column(name="UPD_ORG_CODE")
	public String getUpdOrgCode() {
		return super.getUpdOrgCode();
	}
	@Column(name="CRT_USER_CODE")
	public String getCrtUserCode() {
		return super.getCrtUserCode();
	}
	@Column(name="CRT_DATE")
	public Date getCrtDate() {
		return super.getCrtDate();
	}
	@Column(name="UPD_DATE")
	public Date getUpdDate() {
		return super.getUpdDate();
	}
	@Column(name="UPD_USER_CODE")
	public String getUpdUserCode() {
		return super.getUpdUserCode();
	}
	/**
	* 系统编码
	*/
	private String systemCode;
	/**
	* 系统名称
	*/
	private String systemName;
	/**
	* 系统描述
	*/
	private String systemDesc;
	/**
	* 系统类型
	*/
	private String systemType;

	@Column(name="SYSTEM_CODE")
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	@Column(name="SYSTEM_NAME")
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	@Column(name="SYSTEM_DESC")
	public String getSystemDesc() {
		return systemDesc;
	}
	public void setSystemDesc(String systemDesc) {
		this.systemDesc = systemDesc;
	}
	@Column(name="SYSTEM_TYPE")
	public String getSystemType() {
		return systemType;
	}
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

}
