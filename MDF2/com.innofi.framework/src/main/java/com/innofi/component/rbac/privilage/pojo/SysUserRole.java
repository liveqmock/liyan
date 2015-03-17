package com.innofi.component.rbac.privilage.pojo;
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
@Table(name="SYS_USER_ROLE")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysUserRoleType")
@DiscriminatorValue("sysUserRole")
public class SysUserRole extends BasePojo{
		
		/**
	* 角色ID
	*/
	private String roleId;
		
		/**
	* 用户ID
	*/
	private String userId;
		
		/**
	* 用户编码
	*/
	private String userCode;
	
    /**
     * 角色名称
     */
	private String roleName;
	/**
	 * 角色类型
	 */
	private String roleType;
	
	/**
	 * 角色状态
	 */
	private String status;
		

		@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="USER_ROLE_MP_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="ROLE_ID")
	public String getRoleId(){
		return this.roleId;
	}
	public void setRoleId(String roleId){
		this.roleId = roleId;
	} 
		
			@Column(name="USER_ID")
	public String getUserId(){
		return this.userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	} 
		
			@Column(name="USER_CODE")
	public String getUserCode(){
		return this.userCode;
	}
	public void setUserCode(String userCode){
		this.userCode = userCode;
	} 
	
	  @Transient
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	  @Transient
	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	
	  @Transient
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
		
	}