package com.innofi.component.rbac.privilage.pojo;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.GenericGenerator;


/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Entity
@Table(name = "SYS_ORG_ROLE")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysOrgRoleType")
@DiscriminatorValue("sysOrgRole")
public class SysOrgRole extends BasePojo {
    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 机构编码
     */
    private String orgCode;


    /**
     * 机构ID
     */
    private String orgId;

    private String roleName;
    
	/**
	 * 角色类型
	 */
	private String roleType;
	/**
	 * 角色有效标记
	 */
	private String status;

	@Column(name = "ROLE_ID")
    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }


	@Column(name = "ORG_CODE")
    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ORG_ROLE_ID")
    public String getId() {
        return super.getId();
    }

    @Column(name = "ORG_ID")
    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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