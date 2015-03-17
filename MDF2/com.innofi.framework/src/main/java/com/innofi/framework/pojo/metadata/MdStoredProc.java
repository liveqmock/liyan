package com.innofi.framework.pojo.metadata;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.GenericGenerator;

/**
 * 功能/ 模块：todo 模块中文名称
 * 
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期 todo 类描述 修订历史： 日期 作者 参考 描述 北京名晟信息技术有限公司版权所有.
 */
@Entity
@Table(name = "MD_STORED_PROC")

public class MdStoredProc extends BasePojo {

	/**
	 * 存储过程名
	 */
	private String procName;

	/**
	 * 存付过程描述
	 */
	private String procDesc;

	/**
	 * 参数描述
	 */
	private String para;

	/**
	 * 有效状态:1-有效2-无效
	 */
	private String status;

	/**
	 * 创建人
	 */
	private String crtUserCode;

	/**
	 * 创建机构
	 */
	private String crtOrgCode;

	/**
	 * 创建日期
	 */
	private Date crtDate;

	/**
	 * 修改人
	 */
	private String updUserCode;

	/**
	 * 修改机构
	 */
	private String updOrgCode;

	/**
	 * 修改日期
	 */
	private Date updDate;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "PROC_ID")
	public String getId() {
		return super.getId();
	}

	@Column(name = "PROC_NAME")
	public String getProcName() {
		return this.procName;
	}

	public void setProcName(String procName) {
		this.procName = procName;
	}

	@Column(name = "PROC_DESC")
	public String getProcDesc() {
		return this.procDesc;
	}

	public void setProcDesc(String procDesc) {
		this.procDesc = procDesc;
	}

	@Column(name = "PARA")
	public String getPara() {
		return this.para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "CRT_USER_CODE")
	public String getCrtUserCode() {
		return this.crtUserCode;
	}

	public void setCrtUserCode(String crtUserCode) {
		this.crtUserCode = crtUserCode;
	}

	@Column(name = "CRT_ORG_CODE")
	public String getCrtOrgCode() {
		return this.crtOrgCode;
	}

	public void setCrtOrgCode(String crtOrgCode) {
		this.crtOrgCode = crtOrgCode;
	}

	@Column(name = "CRT_DATE")
	public Date getCrtDate() {
		return this.crtDate;
	}

	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}

	@Column(name = "UPD_USER_CODE")
	public String getUpdUserCode() {
		return this.updUserCode;
	}

	public void setUpdUserCode(String updUserCode) {
		this.updUserCode = updUserCode;
	}

	@Column(name = "UPD_ORG_CODE")
	public String getUpdOrgCode() {
		return this.updOrgCode;
	}

	public void setUpdOrgCode(String updOrgCode) {
		this.updOrgCode = updOrgCode;
	}

	@Column(name = "UPD_DATE")
	public Date getUpdDate() {
		return this.updDate;
	}

	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)	return true;
		if (o == null || getClass() != o.getClass()) return false;
		MdStoredProc mdStoredProc = (MdStoredProc) o;

		if (procName != null ? !procName.equals(mdStoredProc.procName): mdStoredProc.procName != null) return false;
		if (procDesc != null ? !procDesc.equals(mdStoredProc.procDesc): mdStoredProc.procDesc != null) return false;
		if (para != null ? !para.equals(mdStoredProc.para): mdStoredProc.para != null) return false;

		return true;
	}
}