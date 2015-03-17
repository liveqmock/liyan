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
@Table(name = "SYS_AUTHORIZE")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysAuthorizeType")
@DiscriminatorValue("sysAuthorize")
public class SysAuthorize extends BasePojo {
    /**
     * 资源ID
     */
    private String resourceId;

    /**
     * 岗位ID,业务主键
     */
    private String roleId;


    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源类型:1-菜单 0-按钮
     */
    private String resourceType;

    @Column(name = "RESOURCE_ID")
    public String getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Column(name = "ROLE_ID")
    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "AUTHORIZE_ID")
    public String getId() {
        return super.getId();
    }

    @Column(name = "RESOURCE_NAME")
    public String getResourceName() {
        return this.resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Column(name = "RESOURCE_TYPE")
    public String getResourceType() {
        return this.resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

}