package com.innofi.component.rbac.workspace.infobar.pojo;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.GenericGenerator;


/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Entity
@Table(name="SYS_INF_BAR")

public class SysInfBar extends BasePojo{
		/**
	* 信息栏名称
	*/
	private String infBarName;
		
		/**
	* 信息栏描述
	*/
	private String infBarDesc;
		
		/**
	* 信息栏图标
	*/
	private String infBarPict;
		
		/**
	* 创建人
	*/
	private String crtUserCode;
		
		/**
	* 修改人
	*/
	private String updUserCode;
		
		/**
	* 修改日期
	*/
	private Date updDate;
		
		
		/**
	* 信息栏标题
	*/
	private String infBarTitle;
		
		/**
	* 信息栏URL
	*/
	private String infBarUrl;
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 创建时间
	*/
	private Date crtDate;
		
		/**
	* 修改机构
	*/
	private String updOrgCode;
		
	private String infBarCol;
	@Transient
		public String getInfBarCol() {
		return infBarCol;
	}
	public void setInfBarCol(String infBarCol) {
		this.infBarCol = infBarCol;
	}
		@Column(name="INF_BAR_NAME")
	public String getInfBarName(){
		return this.infBarName;
	}
	public void setInfBarName(String infBarName){
		this.infBarName = infBarName;
	} 
		
			@Column(name="INF_BAR_DESC")
	public String getInfBarDesc(){
		return this.infBarDesc;
	}
	public void setInfBarDesc(String infBarDesc){
		this.infBarDesc = infBarDesc;
	} 
		
			@Column(name="INF_BAR_PICT")
	public String getInfBarPict(){
		return this.infBarPict;
	}
	public void setInfBarPict(String infBarPict){
		this.infBarPict = infBarPict;
	} 
		
			@Column(name="CRT_USER_CODE")
	public String getCrtUserCode(){
		return this.crtUserCode;
	}
	public void setCrtUserCode(String crtUserCode){
		this.crtUserCode = crtUserCode;
	} 
		
			@Column(name="UPD_USER_CODE")
	public String getUpdUserCode(){
		return this.updUserCode;
	}
	public void setUpdUserCode(String updUserCode){
		this.updUserCode = updUserCode;
	} 
		
			@Column(name="UPD_DATE")
	public Date getUpdDate(){
		return this.updDate;
	}
	public void setUpdDate(Date updDate){
		this.updDate = updDate;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="INF_BAR_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="INF_BAR_TITLE")
	public String getInfBarTitle(){
		return this.infBarTitle;
	}
	public void setInfBarTitle(String infBarTitle){
		this.infBarTitle = infBarTitle;
	} 
		
			@Column(name="INF_BAR_URL")
	public String getInfBarUrl(){
		return this.infBarUrl;
	}
	public void setInfBarUrl(String infBarUrl){
		this.infBarUrl = infBarUrl;
	} 
		
			@Column(name="CRT_ORG_CODE")
	public String getCrtOrgCode(){
		return this.crtOrgCode;
	}
	public void setCrtOrgCode(String crtOrgCode){
		this.crtOrgCode = crtOrgCode;
	} 
		
			@Column(name="CRT_DATE")
	public Date getCrtDate(){
		return this.crtDate;
	}
	public void setCrtDate(Date crtDate){
		this.crtDate = crtDate;
	} 
		
			@Column(name="UPD_ORG_CODE")
	public String getUpdOrgCode(){
		return this.updOrgCode;
	}
	public void setUpdOrgCode(String updOrgCode){
		this.updOrgCode = updOrgCode;
	} 
		
	}