/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.pojo.metadata;

import java.math.BigDecimal;
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
@Table(name="MD_TABLE_SPACE")
public class MdTableSpace extends BasePojo {
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="TBS_ID")
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
	* 表空间名
	*/
	private String tbsName;
	/**
	* 表空间类型
	*/
	private String tbsType;
	/**
	* 管理方式
	*/
	private String tbsManageMethod;
	/**
	* 页大小
	*/
	private BigDecimal tbsPagesize;
	/**
	 * 表空间大小
	 */
	private BigDecimal tbsSize;
	/**
	 * 已使用率
	 */
	private BigDecimal usedPercent;
	/**
	* 存储状态
	*/
	private String status;

	@Column(name="TBS_NAME")
	public String getTbsName() {
		return tbsName;
	}
	public void setTbsName(String tbsName) {
		this.tbsName = tbsName;
	}
	@Column(name="TBS_TYPE")
	public String getTbsType() {
		return tbsType;
	}
	public void setTbsType(String tbsType) {
		this.tbsType = tbsType;
	}
	@Column(name="TBS_MANAGE_METHOD")
	public String getTbsManageMethod() {
		return tbsManageMethod;
	}
	public void setTbsManageMethod(String tbsManageMethod) {
		this.tbsManageMethod = tbsManageMethod;
	}
	@Column(name="TBS_PAGESIZE")
	public BigDecimal getTbsPagesize() {
		return tbsPagesize;
	}
	public void setTbsPagesize(BigDecimal tbsPagesize) {
		this.tbsPagesize = tbsPagesize;
	}
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name="TBS_SIZE")
	public BigDecimal getTbsSize() {
		return tbsSize;
	}
	public void setTbsSize(BigDecimal tbsSize) {
		this.tbsSize = tbsSize;
	}
	@Column(name="USED_PERCENT")
	public BigDecimal getUsedPercent() {
		return usedPercent;
	}
	public void setUsedPercent(BigDecimal usedPercent) {
		this.usedPercent = usedPercent;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MdTableSpace mdTableSpacePojo = (MdTableSpace) o;

        if (tbsName != null ? !tbsName.equals(mdTableSpacePojo.tbsName) : mdTableSpacePojo.tbsName != null) return false;
        if (tbsManageMethod != null ? !tbsManageMethod.equals(mdTableSpacePojo.tbsManageMethod) : mdTableSpacePojo.tbsManageMethod != null) return false;
        if (tbsPagesize != null ? !tbsPagesize.equals(mdTableSpacePojo.tbsPagesize) : mdTableSpacePojo.tbsPagesize != null) return false;
        if (tbsSize != null ? !tbsSize.equals(mdTableSpacePojo.tbsSize) : mdTableSpacePojo.tbsSize != null) return false;
        if (tbsType != null ? !tbsType.equals(mdTableSpacePojo.tbsType) : mdTableSpacePojo.tbsType != null) return false;
        if (usedPercent != null ? !usedPercent.equals(mdTableSpacePojo.usedPercent) : mdTableSpacePojo.usedPercent != null) return false;
        
        return true;
    }
}
