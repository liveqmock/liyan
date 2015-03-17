package com.innofi.component.rbac.user.pojo;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.innofi.component.rbac.org.pojo.SysOrgInfo;
import com.innofi.component.rbac.post.pojo.SysPost;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.component.rbac.workspace.theme.pojo.SysUserTheme;
import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYS_USER")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysUserType")
@DiscriminatorValue("sysUser")
public class SysUser extends BasePojo {

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 所属部门
     */
    private String orgCode;

    /**
     * 当前用户所属机构编码
     */
    private String owerOrgCode;

    /**
     * 联系手机
     */
    private String mobile;

    /**
     * 联系邮箱
     */
    private String email;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 持有证书
     */
    private String certificate;

    /**
     * 考核评价
     */
    private String assessment;

    /**
     * 创建人
     */
    private String crtUserCode;

    /**
     * 修改人
     */
    private String updUserCode;

    /**
     * 性别
     */
    private String sex;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 培训记录
     */
    private String tranHis;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 在线状态
     */
    private String onlineStatus;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建机构
     */
    private String crtOrgCode;

    /**
     * 创建时间
     */
    private Date crtDate;

    /**
     * 修改机构
     */
    private String updOrgCode;


    /**
     * 修改日期
     */
    private Date updDate;


    private boolean selectFlag;
    private String orgName;

    /**
     * 用户所属机构对象
     */
    private SysOrgInfo sysOrgInfo;

    /**
     * 用户岗位列表 第0个元素为主岗位，其它为兼职岗位
     */
    private List<SysPost> posts;

    /**
     * 用户功能角色列表,部门+岗位+用户
     */
    private List<SysRole> functionRoles;

    /**
     * 用户数据角色列表,部门+岗位+用户
     */
    private List<SysRole> dataRoles;

    /**
     * 用户主题
     */
    private SysUserTheme sysUserTheme;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 当前用户拥有功能角色名称
     */
    private String functionRoleNames;

    /**
     * 当前用户拥有数据角色名称
     */
    private String dataRoleNames;

    /**
     * 当前用户拥有功能角色ID
     */
    private String functionRoleIds;

    /**
     * 当前用户拥有数据角色ID
     */
    private String dataRoleIds;

    /**
     * 当前用户拥有主岗位名称
     */
    private String mainPostNames;

    /**
     * 当前用户拥有兼职岗位名称
     */
    private String partPostNames;


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "USER_ID")
    public String getId() {
        return super.getId();
    }

    @Column(name = "USER_CODE")
    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Column(name = "USER_NAME")
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "ORG_CODE")
    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Transient
    public String getOwerOrgCode() {
        return owerOrgCode;
    }

    public void setOwerOrgCode(String owerOrgCode) {
        this.owerOrgCode = owerOrgCode;
    }

    @Column(name = "MOBILE")
    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(name = "EMAIL")
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "ADDRESS")
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "CERTIFICATE")
    public String getCertificate() {
        return this.certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @Column(name = "ASSESSMENT")
    public String getAssessment() {
        return this.assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    @Column(name = "CRT_USER_CODE")
    public String getCrtUserCode() {
        return this.crtUserCode;
    }

    public void setCrtUserCode(String crtUserCode) {
        this.crtUserCode = crtUserCode;
    }

    @Column(name = "UPD_USER_CODE")
    public String getUpdUserCode() {
        return this.updUserCode;
    }

    public void setUpdUserCode(String updUserCode) {
        this.updUserCode = updUserCode;
    }

    @Column(name = "SEX")
    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Column(name = "TEL")
    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Column(name = "TRAN_HIS")
    public String getTranHis() {
        return this.tranHis;
    }

    public void setTranHis(String tranHis) {
        this.tranHis = tranHis;
    }

    @Column(name = "PASSWORD")
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "ONLINE_STATUS")
    public String getOnlineStatus() {
        return this.onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "CRT_ORG_CODE")
    public String getCrtOrgCode() {
        return this.crtOrgCode;
    }

    public void setCrtOrgCode(String crtOrgCode) {
        this.crtOrgCode = crtOrgCode;
    }

    @Column(name = "CRT_DATE")
    public Date getCrtDate() {
        return this.crtDate;
    }

    public void setCrtDate(Date crtDate) {
        this.crtDate = crtDate;
    }

    @Column(name = "UPD_ORG_CODE")
    public String getUpdOrgCode() {
        return this.updOrgCode;
    }

    public void setUpdOrgCode(String updOrgCode) {
        this.updOrgCode = updOrgCode;
    }

    @Column(name = "UPD_DATE")
    public Date getUpdDate() {
        return this.updDate;
    }

    public void setUpdDate(Date updDate) {
        this.updDate = updDate;
    }


    @Transient
    public boolean getSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }

    @Transient
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Transient
    public SysOrgInfo getSysOrgInfo() {
        return sysOrgInfo;
    }

    public void setSysOrgInfo(SysOrgInfo sysOrgInfo) {
        this.sysOrgInfo = sysOrgInfo;
    }

    @Transient
    public List<SysPost> getPosts() {
        return posts;
    }

    public void setPosts(List<SysPost> posts) {
        this.posts = posts;
    }

    @Transient
    public List<SysRole> getFunctionRoles() {
        return functionRoles;
    }

    public void setFunctionRoles(List<SysRole> functionRoles) {
        this.functionRoles = functionRoles;
    }

    @Transient
    public List<SysRole> getDataRoles() {
        return dataRoles;
    }

    public void setDataRoles(List<SysRole> dataRoles) {
        this.dataRoles = dataRoles;
    }

    @Transient
    public SysUserTheme getSysUserTheme() {
        return sysUserTheme;
    }

    public void setSysUserTheme(SysUserTheme sysUserTheme) {
        this.sysUserTheme = sysUserTheme;
    }

    @Transient
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Transient
    public String getFunctionRoleNames() {
        return functionRoleNames;
    }

    public void setFunctionRoleNames(String functionRoleNames) {
        this.functionRoleNames = functionRoleNames;
    }

    @Transient
    public String getDataRoleNames() {
        return dataRoleNames;
    }

    public void setDataRoleNames(String dataRoleNames) {
        this.dataRoleNames = dataRoleNames;
    }

    @Transient
    public String getMainPostNames() {
        return mainPostNames;
    }

    public void setMainPostNames(String mainPostNames) {
        this.mainPostNames = mainPostNames;
    }

    @Transient
    public String getPartPostNames() {
        return partPostNames;
    }

    public void setPartPostNames(String partPostNames) {
        this.partPostNames = partPostNames;
    }

    @Transient
    public String getFunctionRoleIds() {
        return functionRoleIds;
    }

    public void setFunctionRoleIds(String functionRoleIds) {
        this.functionRoleIds = functionRoleIds;
    }

    @Transient
    public String getDataRoleIds() {
        return dataRoleIds;
    }

    public void setDataRoleIds(String dataRoleIds) {
        this.dataRoleIds = dataRoleIds;
    }
}