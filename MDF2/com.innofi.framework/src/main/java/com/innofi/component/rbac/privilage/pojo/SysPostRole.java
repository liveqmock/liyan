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
@Table(name="SYS_POST_ROLE")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysPostRoleType")
@DiscriminatorValue("sysPostRole")
public class SysPostRole extends BasePojo{
		
		/**
	* 岗位ID,业务主键
	*/
	private String postId;
		
		/**
	* 角色ID
	*/
	private String roleId;
	
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
	@Column(name="POST_ROLE_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="POST_ID")
	public String getPostId(){
		return this.postId;
	}
	public void setPostId(String postId){
		this.postId = postId;
	} 
		
			@Column(name="ROLE_ID")
	public String getRoleId(){
		return this.roleId;
	}
	public void setRoleId(String roleId){
		this.roleId = roleId;
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