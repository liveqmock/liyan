package com.innofi.component.rbac.post.pojo;
import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.GenericGenerator;

/**
* ${model.modelCnName}
*/

@Entity
@Table(name="SYS_POST")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysPostType")
@DiscriminatorValue("sysPost")
public class SysPost extends BasePojo{
		/**
	* 职责描述
	*/
	private String postDesc;
		
		/**
	* 状态
	*/
	private String status;
		
		/**
	* 所属机构
	*/
	private String orgCode;
		
		/**
	* 岗位编码
	*/
	private String postCode;
		
		/**
	* 岗位级别
	*/
	private String postLevel;
		
		/**
	* 岗位类别
	*/
	private String postCate;
		
		/**
	* 岗位名称
	*/
	private String postName;
		
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 创建时间
	*/
	private Date crtDate;
		
		/**
	* 创建人
	*/
	private String crtUserCode;
		
		/**
	* 修改机构
	*/
	private String updOrgCode;
		
		/**
	* 修改日期
	*/
	private Date updDate;
		
		/**
	* 修改人
	*/
	private String updUserCode;
	//是否选中标志
	private boolean selectFlag;
	//所属部门名称
	private String orgName;
	
		@Column(name="POST_DESC")
	public String getPostDesc(){
		return this.postDesc;
	}
	public void setPostDesc(String postDesc){
		this.postDesc = postDesc;
	} 
		
			@Column(name="STATUS")
	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	} 
		
			@Column(name="ORG_CODE")
	public String getOrgCode(){
		return this.orgCode;
	}
	public void setOrgCode(String orgCode){
		this.orgCode = orgCode;
	} 
		
			@Column(name="POST_CODE")
	public String getPostCode(){
		return this.postCode;
	}
	public void setPostCode(String postCode){
		this.postCode = postCode;
	} 
		
			@Column(name="POST_LEVEL")
	public String getPostLevel(){
		return this.postLevel;
	}
	public void setPostLevel(String postLevel){
		this.postLevel = postLevel;
	} 
		
			@Column(name="POST_CATE")
	public String getPostCate(){
		return this.postCate;
	}
	public void setPostCate(String postCate){
		this.postCate = postCate;
	} 
		
			@Column(name="POST_NAME")
	public String getPostName(){
		return this.postName;
	}
	public void setPostName(String postName){
		this.postName = postName;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="POST_ID")
	public String getId(){
		return super.getId();
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
		
			@Column(name="CRT_USER_CODE")
	public String getCrtUserCode(){
		return this.crtUserCode;
	}
	public void setCrtUserCode(String crtUserCode){
		this.crtUserCode = crtUserCode;
	} 
		
			@Column(name="UPD_ORG_CODE")
	public String getUpdOrgCode(){
		return this.updOrgCode;
	}
	public void setUpdOrgCode(String updOrgCode){
		this.updOrgCode = updOrgCode;
	} 
		
			@Column(name="UPD_DATE")
	public Date getUpdDate(){
		return this.updDate;
	}
	public void setUpdDate(Date updDate){
		this.updDate = updDate;
	} 
		
			@Column(name="UPD_USER_CODE")
	public String getUpdUserCode(){
		return this.updUserCode;
	}
	public void setUpdUserCode(String updUserCode){
		this.updUserCode = updUserCode;
	}
	@Transient
	public boolean getSelectFlag() {
		return selectFlag;
	}
	public void setSelectFlag(boolean selectFlag) {
		this.selectFlag = selectFlag;
	}
	@Transient
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	} 
	
	}