package com.innofi.component.rbac.busiline.pojo;
import java.math.BigDecimal;
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
@Table(name="SYS_BUSINESS_LINE")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysBusinessLineType")
@DiscriminatorValue("sysBusinessLine")
public class SysBusinessLine extends BasePojo{
		/**
	* BASEL业务条线ID
	*/
	private String busilineBaselId;
		
		
		/**
	* 业务条线名称
	*/
	private String busilineName;
		
		/**
	* 业务条线描述
	*/
	private String busilineDesc;
		
		/**
	* 业务条线编号
	*/
	private String busilineCode;
		
		/**
	* 修改人
	*/
	private String updUserCode;
		
		/**
	* 修改日期
	*/
	private Date updDate;
		
		/**
	* 修改机构
	*/
	private String updOrgCode;
		
		/**
	* 创建人
	*/
	private String crtUserCode;
		
		/**
	* 创建时间
	*/
	private Date crtDate;
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 所属机构
	*/
	private String belongOrgCode;
		
		/**
	* 显示序号
	*/
	private BigDecimal seq;
		
		/**
	* 树形层级
	*/
	private BigDecimal treeLevel;
		
		/**
	* 父业务条线ID
	*/
	private String parentBusilineId;
		
		/**
	* 状态
	*/
	private String status;
	
	private String treeSeq;
	
	private String busilineBaselName;
	
	private String belongOrgName;
		
		@Column(name="BUSILINE_BASEL_ID")
	public String getBusilineBaselId(){
		return this.busilineBaselId;
	}
	public void setBusilineBaselId(String busilineBaselId){
		this.busilineBaselId = busilineBaselId;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="BUSILINE_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="BUSILINE_NAME")
	public String getBusilineName(){
		return this.busilineName;
	}
	public void setBusilineName(String busilineName){
		this.busilineName = busilineName;
	} 
		
			@Column(name="BUSILINE_DESC")
	public String getBusilineDesc(){
		return this.busilineDesc;
	}
	public void setBusilineDesc(String busilineDesc){
		this.busilineDesc = busilineDesc;
	} 
		
			@Column(name="BUSILINE_CODE")
	public String getBusilineCode(){
		return this.busilineCode;
	}
	public void setBusilineCode(String busilineCode){
		this.busilineCode = busilineCode;
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
		
			@Column(name="UPD_ORG_CODE")
	public String getUpdOrgCode(){
		return this.updOrgCode;
	}
	public void setUpdOrgCode(String updOrgCode){
		this.updOrgCode = updOrgCode;
	} 
		
			@Column(name="CRT_USER_CODE")
	public String getCrtUserCode(){
		return this.crtUserCode;
	}
	public void setCrtUserCode(String crtUserCode){
		this.crtUserCode = crtUserCode;
	} 
		
			@Column(name="CRT_DATE")
	public Date getCrtDate(){
		return this.crtDate;
	}
	public void setCrtDate(Date crtDate){
		this.crtDate = crtDate;
	} 
		
			@Column(name="CRT_ORG_CODE")
	public String getCrtOrgCode(){
		return this.crtOrgCode;
	}
	public void setCrtOrgCode(String crtOrgCode){
		this.crtOrgCode = crtOrgCode;
	} 
		
			@Column(name="BELONG_ORG_CODE")
	public String getBelongOrgCode(){
		return this.belongOrgCode;
	}
	public void setBelongOrgCode(String belongOrgCode){
		this.belongOrgCode = belongOrgCode;
	} 
		
			@Column(name="SEQ")
	public BigDecimal getSeq(){
		return this.seq;
	}
	public void setSeq(BigDecimal seq){
		this.seq = seq;
	} 
		
			@Column(name="TREE_LEVEL")
	public BigDecimal getTreeLevel(){
		return this.treeLevel;
	}
	public void setTreeLevel(BigDecimal treeLevel){
		this.treeLevel = treeLevel;
	} 
		
			@Column(name="PARENT_BUSILINE_ID")
	public String getParentBusilineId(){
		return this.parentBusilineId;
	}
	public void setParentBusilineId(String parentBusilineId){
		this.parentBusilineId = parentBusilineId;
	} 
		
			@Column(name="STATUS")
	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	} 
	@Column(name="TREE_SEQ")
	public String getTreeSeq() {
		return treeSeq;
	}

	public void setTreeSeq(String treeSeq) {
		this.treeSeq = treeSeq;
	}
	@Transient
	public String getBusilineBaselName() {
		return busilineBaselName;
	}
	public void setBusilineBaselName(String busilineBaselName) {
		this.busilineBaselName = busilineBaselName;
	}
	@Transient
	public String getBelongOrgName() {
		return belongOrgName;
	}
	public void setBelongOrgName(String belongOrgName) {
		this.belongOrgName = belongOrgName;
	} 
	}