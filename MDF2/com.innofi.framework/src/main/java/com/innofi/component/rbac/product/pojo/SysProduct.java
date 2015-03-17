package com.innofi.component.rbac.product.pojo;
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
@Table(name="SYS_PRODUCT")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysProductType")
@DiscriminatorValue("SysProduct")
public class SysProduct extends BasePojo{
		/**
	* 业务条线ID
	*/
	private String busilineId;
	private String busilineName;
		
		/**
	* 产品名称
	*/
	private String productName;
		
		/**
	* 产品描述
	*/
	private String productDesc;
		
		/**
	* 产品管理部门
	*/
	private String manageOrgCode;
	private String manageOrgName;
		/**
	* 产品编号
	*/
	private String productCode;
		
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
	* 显示序号
	*/
	private BigDecimal seq;
		
		/**
	* 树形层级
	*/
	private BigDecimal treeLevel;
		
		/**
	* 父产品ID
	*/
	private String parentId;
		
		/**
	* 状态
	*/
	private String status;
		
	private String treeSeq;
	
		@Column(name="BUSILINE_ID")
	public String getBusilineId(){
		return this.busilineId;
	}
	public void setBusilineId(String busilineId){
		this.busilineId = busilineId;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="PRODUCT_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="PRODUCT_NAME")
	public String getProductName(){
		return this.productName;
	}
	public void setProductName(String productName){
		this.productName = productName;
	} 
		
			@Column(name="PRODUCT_DESC")
	public String getProductDesc(){
		return this.productDesc;
	}
	public void setProductDesc(String productDesc){
		this.productDesc = productDesc;
	} 
		
			@Column(name="MANAGE_ORG_CODE")
	public String getManageOrgCode(){
		return this.manageOrgCode;
	}
	public void setManageOrgCode(String manageOrgCode){
		this.manageOrgCode = manageOrgCode;
	} 
		
			@Column(name="PRODUCT_CODE")
	public String getProductCode(){
		return this.productCode;
	}
	public void setProductCode(String productCode){
		this.productCode = productCode;
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
		
			@Column(name="PARENT_ID")
	public String getParentId(){
		return this.parentId;
	}
	public void setParentId(String parentId){
		this.parentId = parentId;
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
	public String getBusilineName() {
		return busilineName;
	}
	public void setBusilineName(String busilineName) {
		this.busilineName = busilineName;
	}
	@Transient
	public String getManageOrgName() {
		return manageOrgName;
	}
	public void setManageOrgName(String manageOrgName) {
		this.manageOrgName = manageOrgName;
	} 
	
	}