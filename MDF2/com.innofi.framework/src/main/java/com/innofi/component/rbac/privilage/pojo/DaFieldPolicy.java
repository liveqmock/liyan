package com.innofi.component.rbac.privilage.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.innofi.framework.pojo.BasePojo;


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
@Table(name="DA_FIELD_POLICY")

public class DaFieldPolicy extends BasePojo{
		/**
	* 创建日期
	*/
	private Date crtDate;
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 创建人
	*/
	private String crtUserCode;
		
		
		/**
	* 字段名
	*/
	private String fieldName;
		
		/**
	* 数据权限类型:1-浏览 2-修改 (3-删除)，冗余字段 
	*/
	private String operType;
		
		/**
	* 表权限控制策略ID
	*/
	private String tableAuthId;
		
		/**
	* 表名
	*/
	private String tableName;
		
		/**
	* 修改日期
	*/
	private Date updDate;
		
		/**
	* 修改机构
	*/
	private String updOrgCode;
		
		/**
	* 修改人
	*/
	private String updUserCode;
		
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
		
			@Column(name="CRT_USER_CODE")
	public String getCrtUserCode(){
		return this.crtUserCode;
	}
	public void setCrtUserCode(String crtUserCode){
		this.crtUserCode = crtUserCode;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="FIELD_AUTH")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="FIELD_NAME")
	public String getFieldName(){
		return this.fieldName;
	}
	public void setFieldName(String fieldName){
		this.fieldName = fieldName;
	} 
		
			@Column(name="OPER_TYPE")
	public String getOperType(){
		return this.operType;
	}
	public void setOperType(String operType){
		this.operType = operType;
	} 
		
			@Column(name="TABLE_AUTH_ID")
	public String getTableAuthId(){
		return this.tableAuthId;
	}
	public void setTableAuthId(String tableAuthId){
		this.tableAuthId = tableAuthId;
	} 
		
			@Column(name="TABLE_NAME")
	public String getTableName(){
		return this.tableName;
	}
	public void setTableName(String tableName){
		this.tableName = tableName;
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
		
			@Column(name="UPD_USER_CODE")
	public String getUpdUserCode(){
		return this.updUserCode;
	}
	public void setUpdUserCode(String updUserCode){
		this.updUserCode = updUserCode;
	} 
		
	}