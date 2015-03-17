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
@Table(name="MD_OBJECT_METHOD")
public class MdObjectMethod extends BasePojo {
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="METHOD_ID")
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
	* 实体对象ID
	*/
	private String objId;
	/**
	* 实体对象名称
	*/
	private String objName;
	/**
	* 方法名
	*/
	private String methodName;
	/**
	* 方法描述
	*/
	private String methodDesc;
	/**
	* 参数
	*/
	private String methodParameter;
	/**
	* 方法返回类型
	*/
	private String methodReturn;
	/**
	* 方法操作类型
	*/
	private String methodOprType;

	@Column(name="OBJ_ID")
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	@Transient
	public String getObjName() {
		return objName;
	}
	public void setObjName(String objName) {
		this.objName = objName;
	}
	@Column(name="METHOD_NAME")
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	@Column(name="METHOD_DESC")
	public String getMethodDesc() {
		return methodDesc;
	}
	public void setMethodDesc(String methodDesc) {
		this.methodDesc = methodDesc;
	}
	@Column(name="METHOD_PARAMETER")
	public String getMethodParameter() {
		return methodParameter;
	}
	public void setMethodParameter(String methodParameter) {
		this.methodParameter = methodParameter;
	}
	@Column(name="METHOD_OPR_TYPE")
	public String getMethodOprType() {
		return methodOprType;
	}
	public void setMethodOprType(String methodOprType) {
		this.methodOprType = methodOprType;
	}
	@Column(name="METHOD_RETURN")
	public String getMethodReturn() {
		return methodReturn;
	}
	public void setMethodReturn(String methodReturn) {
		this.methodReturn = methodReturn;
	}

}
