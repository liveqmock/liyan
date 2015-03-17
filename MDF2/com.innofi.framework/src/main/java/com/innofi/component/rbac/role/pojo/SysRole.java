package com.innofi.component.rbac.role.pojo;
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
@Table(name="SYS_ROLE")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysRoleType")
@DiscriminatorValue("sysRole")
public class SysRole extends BasePojo{
		
		/**
	* 角色编码
	*/
	private String roleCode;
		
		/**
	* 角色名称
	*/
	private String roleName;
		
		/**
	* 角色描述
	*/
	private String remark;
		
		/**
	* 角色级别:1-总行 1.1-总行部门 1.2-分行 1.2.1-分行部门 1.2.2-支行 1.2.2.1-支行部门
	*/
	private String roleLevel;
		
		/**
	* 状态:1-启用 2-停用
	*/
	private String status;
		
		/**
	* 创建人
	*/
	private String crtUserCode;
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 创建时间
	*/
	private Date crtDate;
		
		/**
	* 修改人
	*/
	private String updUserCode;
		
		/**
	* 修改机构
	*/
	private String updOrgCode;
		
		/**
	* 修改日期
	*/
	private Date updDate;
		
	/**
	 * 角色来源 用户，岗位，部门
	 */
	private String roleSource;
	
	/**
	 * 角色类型
	 */
	private String roleType;
	
	private boolean selectFlag;
		@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="ROLE_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="ROLE_CODE")
	public String getRoleCode(){
		return this.roleCode;
	}
	public void setRoleCode(String roleCode){
		this.roleCode = roleCode;
	} 
		
			@Column(name="ROLE_NAME")
	public String getRoleName(){
		return this.roleName;
	}
	public void setRoleName(String roleName){
		this.roleName = roleName;
	} 
		
			@Column(name="REMARK")
	public String getRemark(){
		return this.remark;
	}
	public void setRemark(String remark){
		this.remark = remark;
	} 
		
			@Column(name="ROLE_LEVEL")
	public String getRoleLevel(){
		return this.roleLevel;
	}
	public void setRoleLevel(String roleLevel){
		this.roleLevel = roleLevel;
	} 
		
			@Column(name="STATUS")
	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	} 
		
			@Column(name="CRT_USER_CODE")
	public String getCrtUserCode(){
		return this.crtUserCode;
	}
	public void setCrtUserCode(String crtUserCode){
		this.crtUserCode = crtUserCode;
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
		
			@Column(name="UPD_USER_CODE")
	public String getUpdUserCode(){
		return this.updUserCode;
	}
	public void setUpdUserCode(String updUserCode){
		this.updUserCode = updUserCode;
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
	@Column(name="ROLE_TYPE")
	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	@Transient
	public boolean getSelectFlag() {
		return selectFlag;
	}

	public void setSelectFlag(boolean selectFlag) {
		this.selectFlag = selectFlag;
	} 
	@Transient
	public String getRoleSource() {
		return roleSource;
	}

	public void setRoleSource(String roleSource) {
		this.roleSource = roleSource;
	}
	}