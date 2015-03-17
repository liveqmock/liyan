package com.innofi.component.rbac.log.pojo;
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
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Entity
@Table(name="SYS_LOG_CFG")

public class SysLogCfg extends BasePojo{
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
	* 日志内容规则：通过定义日志内容规则，记录日志内容，
eg: 1、表的唯一索引/主键值，新增成功/失败。
      2、表的唯一索引，修改前主键ID值，修改成功/失败。
	*/
	private String logRule;
		
		/**
	* 方法ID
	*/
	private String methodId;
		
		/**
	* 方法名
	*/
	private String methodName;
		
		/**
	* 实体对象名
	*/
	private String objName;
	/**
	* 实体对象id
	*/
	private String objId;
		
		
	
		/**
	* 操作类型:1-新增 2-修改 3-删除 4-浏览
	*/
	private String operType;
		
		/**
	* 业务模块编码:对应菜单编码
	*/
	private String resourceCode;
		
		/**
	* 业务模块名称
	*/
	private String resourceName;
		
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
	
	private String status;
	@Column(name="status")
		public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	@Column(name="LOG_CFG_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="LOG_RULE")
	public String getLogRule(){
		return this.logRule;
	}
	public void setLogRule(String logRule){
		this.logRule = logRule;
	} 
		
			@Column(name="METHOD_ID")
	public String getMethodId(){
		return this.methodId;
	}
	public void setMethodId(String methodId){
		this.methodId = methodId;
	} 
		
			@Column(name="METHOD_NAME")
	public String getMethodName(){
		return this.methodName;
	}
	public void setMethodName(String methodName){
		this.methodName = methodName;
	} 
		
			@Column(name="OBJ_NAME")
	public String getObjName(){
		return this.objName;
	}
	public void setObjName(String objName){
		this.objName = objName;
	} 
	@Column(name="OBJ_ID")
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
			@Column(name="OPER_TYPE")
	public String getOperType(){
		return this.operType;
	}
	public void setOperType(String operType){
		this.operType = operType;
	} 
		
			@Column(name="RESOURCE_CODE")
	public String getResourceCode(){
		return this.resourceCode;
	}
	public void setResourceCode(String resourceCode){
		this.resourceCode = resourceCode;
	} 
		
			@Column(name="RESOURCE_NAME")
	public String getResourceName(){
		return this.resourceName;
	}
	public void setResourceName(String resourceName){
		this.resourceName = resourceName;
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