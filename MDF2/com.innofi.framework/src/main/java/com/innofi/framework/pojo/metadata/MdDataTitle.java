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
@Table(name="MD_DATA_TITLE")
public class MdDataTitle extends BasePojo {
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="DATA_TITLE_ID")
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
	* 数据主题编码
	*/
	private String dataTitleCode;
	/**
	* 数据主题名称
	*/
	private String dataTitleName;
	/**
	* 数据主题描述
	*/
	private String dataTitleDesc;
	/**
	* 父数据主题编码
	*/
	private String parentId;
	/**
	* 有效状态
	*/
	private String status;

	
	@Column(name="DATA_TITLE_CODE")
	public String getDataTitleCode() {
		return dataTitleCode;
	}
	public void setDataTitleCode(String dataTitleCode) {
		this.dataTitleCode = dataTitleCode;
	}
	@Column(name="DATA_TITLE_NAME")
	public String getDataTitleName() {
		return dataTitleName;
	}
	public void setDataTitleName(String dataTitleName) {
		this.dataTitleName = dataTitleName;
	}
	@Column(name="DATA_TITLE_DESC")
	public String getDataTitleDesc() {
		return dataTitleDesc;
	}
	public void setDataTitleDesc(String dataTitleDesc) {
		this.dataTitleDesc = dataTitleDesc;
	}
	@Column(name="PARENT_ID")
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
