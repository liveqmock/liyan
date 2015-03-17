package com.innofi.component.rbac.job.pojo;
import java.util.Date;
import javax.persistence.*;

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
@Table(name="SYS_JOB_TODO")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysJobTodoType")
@DiscriminatorValue("sysJobTodo")
public class SysJobTodo extends BasePojo{
		/**
	* 统计查询SQL
	*/
	private String seleCountSql;
		
		/**
	* 状态
	*/
	private String status;
		
		/**
	* 按钮ID
	*/
	private String actionId;
		
		/**
	* 待办列表URL
	*/
	private String gotoUrl;
		
		/**
	* 待办事项说明
	*/
	private String jobTodoDesc;
		
		/**
	* 待办事项编码(类型)
	*/
	private String jobTodoNo;
		
		/**
	* 待办事项名称
	*/
	private String jobTodoName;
		
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 创建日期
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
	
	private String jobCount;
		
		@Column(name="SELE_COUNT_SQL")
	public String getSeleCountSql(){
		return this.seleCountSql;
	}
	public void setSeleCountSql(String seleCountSql){
		this.seleCountSql = seleCountSql;
	} 
		
			@Column(name="STATUS")
	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	} 
		
			@Column(name="ACTION_ID")
	public String getActionId(){
		return this.actionId;
	}
	public void setActionId(String actionId){
		this.actionId = actionId;
	} 
		
			@Column(name="GOTO_URL")
	public String getGotoUrl(){
		return this.gotoUrl;
	}
	public void setGotoUrl(String gotoUrl){
		this.gotoUrl = gotoUrl;
	} 
		
			@Column(name="JOB_TODO_DESC")
	public String getJobTodoDesc(){
		return this.jobTodoDesc;
	}
	public void setJobTodoDesc(String jobTodoDesc){
		this.jobTodoDesc = jobTodoDesc;
	} 
		
			@Column(name="JOB_TODO_NO")
	public String getJobTodoNo(){
		return this.jobTodoNo;
	}
	public void setJobTodoNo(String jobTodoNo){
		this.jobTodoNo = jobTodoNo;
	} 
		
			@Column(name="JOB_TODO_NAME")
	public String getJobTodoName(){
		return this.jobTodoName;
	}
	public void setJobTodoName(String jobTodoName){
		this.jobTodoName = jobTodoName;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="JOB_TODO_ID")
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
	public String getJobCount() {
		return jobCount;
	}
	public void setJobCount(String jobCount) {
		this.jobCount = jobCount;
	} 
	}