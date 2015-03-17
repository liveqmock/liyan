/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.pojo.metadata;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.innofi.framework.spring.context.ContextHolder;
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
@Table(name="MD_TABLE_RELATE")
public class MdTableRelate extends BasePojo {

    public MdTableRelate(){
    	String adminString = ContextHolder.getSystemProperties().getString("mtp.system.admin.account","admin");
    	if(adminString.indexOf(",")!=-1){
    		crtUserCode = adminString.split(",")[0];
    	}else{
            crtUserCode = adminString;
    	}
    }

	
  @Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="TABLE_RELATE_ID")
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
	* 依赖关系ID
	*/
	private String dependId;
	/**
	* 主表ID
	*/
	private String mainTableId;
    /**
     * 主表名称
     */
    private String mainTableName;
	/**
	* 主表字段名
	*/
	private String mainField;
	/**
	* 从表ID
	*/
	private String foreignTableId;
    /**
     * 从表名称
     */
    private String foreignTableName;
	/**
	* 外键名字段
	*/
	private String foreignField;
	/**
	* 关系名称
	*/
	private String relateName;
	/**
	* 关系描述
	*/
	private String relateDesc;
	/**
	* 关系类型
	*/
	private String relateType;
	/**
	* 对多关系类型
	*/
	private String corrType;
	/**
	* 是否创建
	*/
	private String isCrt;

	@Column(name="DEPEND_ID")
	public String getDependId() {
		return dependId;
	}
	public void setDependId(String dependId) {
		this.dependId = dependId;
	}
	@Column(name="MAIN_TABLE_NAME")
	public String getMainTableName() {
		return mainTableName;
	}
	public void setMainTableName(String mainTableName) {
		this.mainTableName = mainTableName;
	}
	@Column(name="FOREIGN_TABLE_NAME")
	public String getForeignTableName() {
		return foreignTableName;
	}
	public void setForeignTableName(String foreignTableName) {
		this.foreignTableName = foreignTableName;
	}
	@Column(name="MAIN_TABLE_ID")
	public String getMainTableId() {
		return mainTableId;
	}
	public void setMainTableId(String mainTableId) {
		this.mainTableId = mainTableId;
	}
	@Column(name="MAIN_FIELD")
	public String getMainField() {
		return mainField;
	}
	public void setMainField(String mainField) {
		this.mainField = mainField;
	}
	@Column(name="FOREIGN_TABLE_ID")
	public String getForeignTableId() {
		return foreignTableId;
	}
	public void setForeignTableId(String foreignTableId) {
		this.foreignTableId = foreignTableId;
	}
	@Column(name="FOREIGN_FIELD")
	public String getForeignField() {
		return foreignField;
	}
	public void setForeignField(String foreignField) {
		this.foreignField = foreignField;
	}
	@Column(name="RELATE_NAME")
	public String getRelateName() {
		return relateName;
	}
	public void setRelateName(String relateName) {
		this.relateName = relateName;
	}
	@Column(name="RELATE_DESC")
	public String getRelateDesc() {
		return relateDesc;
	}
	public void setRelateDesc(String relateDesc) {
		this.relateDesc = relateDesc;
	}
	@Column(name="RELATE_TYPE")
	public String getRelateType() {
		return relateType;
	}
	public void setRelateType(String relateType) {
		this.relateType = relateType;
	}
	@Column(name="CORR_TYPE")
	public String getCorrType() {
		return corrType;
	}
	public void setCorrType(String corrType) {
		this.corrType = corrType;
	}
	@Column(name="IS_CRT")
	public String getIsCrt() {
		return isCrt;
	}
	public void setIsCrt(String isCrt) {
		this.isCrt = isCrt;
	}

}
