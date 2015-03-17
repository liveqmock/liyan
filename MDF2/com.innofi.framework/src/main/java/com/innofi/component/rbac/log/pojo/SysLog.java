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
@Table(name="SYS_LOG")

public class SysLog extends BasePojo{
		/**
	* 用户机构名称
	*/
	private String orgName;
		
		/**
	* 用户机构
	*/
	private String orgCode;
		
		/**
	* 日志类型:1--操作日志,2--异常日志
	*/
	private String logType;
		
		
		/**
	* 操作类型:1-新增 2-修改 3-删除 4-提交 5-审批 等
	*/
	private String operType;
		
		/**
	* 操作用户姓名
	*/
	private String userName;
		
		/**
	* 操作用户
	*/
	private String userCode;
		
		/**
	* 操作时间
	*/
	private Date operateDate;
		
		/**
	* 操作内容
	*/
	private String logContent;
		
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
		
		/**
	* 业务模块编码
	*/
	private String resourceCode;
		
		/**
	* 业务模块名称
	*/
	private String resourceName;
		
		/**
	* IP地址
	*/
	private String ipAddr;
	
	private String pkValue;
	@Column(name="PK_VALUE")
		public String getPkValue() {
		return pkValue;
	}
	public void setPkValue(String pkValue) {
		this.pkValue = pkValue;
	}
		@Column(name="ORG_NAME")
	public String getOrgName(){
		return this.orgName;
	}
	public void setOrgName(String orgName){
		this.orgName = orgName;
	} 
		
			@Column(name="ORG_CODE")
	public String getOrgCode(){
		return this.orgCode;
	}
	public void setOrgCode(String orgCode){
		this.orgCode = orgCode;
	} 
		
			@Column(name="LOG_TYPE")
	public String getLogType(){
		return this.logType;
	}
	public void setLogType(String logType){
		this.logType = logType;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="LOG_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="OPER_TYPE")
	public String getOperType(){
		return this.operType;
	}
	public void setOperType(String operType){
		this.operType = operType;
	} 
		
			@Column(name="USER_NAME")
	public String getUserName(){
		return this.userName;
	}
	public void setUserName(String userName){
		this.userName = userName;
	} 
		
			@Column(name="USER_CODE")
	public String getUserCode(){
		return this.userCode;
	}
	public void setUserCode(String userCode){
		this.userCode = userCode;
	} 
		
			@Column(name="OPERATE_DATE")
	public Date getOperateDate(){
		return this.operateDate;
	}
	public void setOperateDate(Date operateDate){
		this.operateDate = operateDate;
	} 
		
			@Column(name="LOG_CONTENT")
	public String getLogContent(){
		return this.logContent;
	}
	public void setLogContent(String logContent){
		this.logContent = logContent;
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
		
			@Column(name="IP_ADDR")
	public String getIpAddr(){
		return this.ipAddr;
	}
	public void setIpAddr(String ipAddr){
		this.ipAddr = ipAddr;
	} 
		
	}