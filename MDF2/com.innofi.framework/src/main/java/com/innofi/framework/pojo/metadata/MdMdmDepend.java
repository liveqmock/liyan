/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.pojo.metadata;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name="MD_MDM_DEPEND")
public class MdMdmDepend extends BasePojo {
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="DEPEND_ID")
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
	* 元模型ID
	*/
	private String mdmId;
	/**
	* 元模型名称
	*/
	private String mdmName;
	/**
	* 依赖元模型ID
	*/
	private String dMdmId;
	/**
	* 依赖元模型名称
	*/
	private String dMdmName;
	/**
	* 依赖关系类型
	*/
	private String dependType;
	/**
	* 依赖关系描述
	*/
	private String dependDesc;


	@Column(name="MDM_ID")
	public String getMdmId() {
		return mdmId;
	}
	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
	}
	@Column(name="D_MDM_ID")
	public String getdMdmId() {
		return dMdmId;
	}
	public void setdMdmId(String dMdmId) {
		this.dMdmId = dMdmId;
	}
	@Column(name="DEPEND_TYPE")
	public String getDependType() {
		return dependType;
	}
	public void setDependType(String dependType) {
		this.dependType = dependType;
	}
	@Column(name="DEPEND_DESC")
	public String getDependDesc() {
		return dependDesc;
	}
	public void setDependDesc(String dependDesc) {
		this.dependDesc = dependDesc;
	}
	@Transient
	public String getMdmName() {
		return mdmName;
	}
	public void setMdmName(String mdmName) {
		this.mdmName = mdmName;
	}
	@Transient
	public String getdMdmName() {
		return dMdmName;
	}
	public void setdMdmName(String dMdmName) {
		this.dMdmName = dMdmName;
	}

}
