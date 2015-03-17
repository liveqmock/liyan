package com.innofi.component.rbac.org.pojo;

import java.util.Date;
import javax.persistence.*;

import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * ${model.modelCnName}
 */
@Entity
@Table(name = "SYS_ORG_INFO")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysOrgInfoType")
@DiscriminatorValue("sysOrgInfo")
public class SysOrgInfo extends BasePojo {
    /**
     * 机构ID
     */
    private String id;

    /**
     * 机构编码
     */
    private String orgCode;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构简称
     */
    private String orgBrief;

    /**
     * 父机构ID
     */
    private String parentOrgId;
    /**
     * 父机构编码
     */
    private String parentOrgCode;

    /**
     * 部门所属机构ID
     */
    private String parentDeptId;
    /**
     * 部门所属机构编码
     */
    private String parentDeptCode;


	/**
     * 机构类别:1-机构 0-部门
     */
    private String category;

    /**
     * 机构级别: 1-总行 1.1-总行部门 1.2-分行 1.2.1-分行部门 1.2.2-支行 1.2.2.1-支行部门
     */
    private String orgLevel;

    /**
     * 机构序列:用机构编号表示机构路径，如：.121.146.
     */
    private String orgSeq;
    
    /**
     * 部门序列：部门序列
     */
    private String deptSeq;

	/**
     * 中文路径：用‘/’隔开各层级路径名称
     */
    private String pathName;

    /**
     * 国家或地区:CN-中国 HK-中国香港 USA-美国 JP-日本
     */
    private String country;

    /**
     * 城市：BeiJing-北京 ShangHai-上海
     */
    private String citycode;

    /**
     * 联系邮箱
     */
    private String email;

    /**
     * 办公地址
     */
    private String officeAddr;

    /**
     * 邮政编码
     */
    private String post;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 是否操作风险部:1-是 2-否
     */
    private String isRiskDept;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
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
     * 创建日期
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
    
    
    private String parentOrgName;
    
    
    private String parentDeptName;
    /**
     * 权限控制标记，对于通过权限策略得到的数据对象标记为true
     */
    private boolean policyFlag;
    /**
     * 顺序号
     */
    private Integer seq;

	/**
     * 部门对应的所有角色
     */
    private String deptRoles;

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ORG_ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "ORG_CODE")
    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Column(name = "ORG_NAME")
    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Column(name = "ORG_BRIEF")
    public String getOrgBrief() {
        return this.orgBrief;
    }

    public void setOrgBrief(String orgBrief) {
        this.orgBrief = orgBrief;
    }

    @Column(name = "PARENT_ORG_ID")
    public String getParentOrgId() {
        return this.parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    @Column(name = "PARENT_ORG_CODE")
    public String getParentOrgCode() {
        return this.parentOrgCode;
    }

    public void setParentOrgCode(String parentOrgCode) {
        this.parentOrgCode = parentOrgCode;
    }
    
    @Column(name = "PARENT_DEPT_ID")
    public String getParentDeptId() {
		return parentDeptId;
	}

	public void setParentDeptId(String parentDeptId) {
		this.parentDeptId = parentDeptId;
	}
	
	@Column(name = "PARENT_DEPT_CODE")
	public String getParentDeptCode() {
		return parentDeptCode;
	}

	public void setParentDeptCode(String parentDeptCode) {
		this.parentDeptCode = parentDeptCode;
	}

    @Column(name = "CATEGORY")
    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "ORG_LEVEL")
    public String getOrgLevel() {
        return this.orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    @Column(name = "ORG_SEQ")
    public String getOrgSeq() {
        return this.orgSeq;
    }
    
    public void setOrgSeq(String orgSeq) {
        this.orgSeq = orgSeq;
    }
    
    @Column(name = "DEPT_SEQ")
    public String getDeptSeq() {
		return deptSeq;
	}

	public void setDeptSeq(String deptSeq) {
		this.deptSeq = deptSeq;
	}

    @Column(name = "PATH_NAME")
    public String getPathName() {
        return this.pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    @Column(name = "COUNTRY")
    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "CITYCODE")
    public String getCitycode() {
        return this.citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    @Column(name = "EMAIL")
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "OFFICE_ADDR")
    public String getOfficeAddr() {
        return this.officeAddr;
    }

    public void setOfficeAddr(String officeAddr) {
        this.officeAddr = officeAddr;
    }

    @Column(name = "POST")
    public String getPost() {
        return this.post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Column(name = "CONTACT")
    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Column(name = "TEL")
    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Column(name = "IS_RISK_DEPT")
    public String getIsRiskDept() {
        return this.isRiskDept;
    }

    public void setIsRiskDept(String isRiskDept) {
        this.isRiskDept = isRiskDept;
    }

    @Column(name = "REMARK")
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "CRT_USER_CODE")
    public String getCrtUserCode() {
        return this.crtUserCode;
    }

    public void setCrtUserCode(String crtUserCode) {
        this.crtUserCode = crtUserCode;
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

    @Column(name = "UPD_USER_CODE")
    public String getUpdUserCode() {
        return this.updUserCode;
    }

    public void setUpdUserCode(String updUserCode) {
        this.updUserCode = updUserCode;
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
    @Column(name = "SEQ")
	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

    @Transient
    public boolean getTreeStruct() {
        return true;
    }

    @Transient
    public String getTreeParentIdFieldName() {
        return "parentOrgCode";
    }

    @Transient
    public String getTreeIdFiledName() {
        return "orgCode";
    }

    @Transient
    public String getTreeSeqFieldName() {
        return "orgSeq";
    }

	@Transient
	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}
	@Transient
    public String getParentDeptName() {
		return parentDeptName;
	}

	public void setParentDeptName(String parentDeptName) {
		this.parentDeptName = parentDeptName;
	}

	@Transient
    public String getDeptRoles() {
		return deptRoles;
	}

	public void setDeptRoles(String deptRoles) {
		this.deptRoles = deptRoles;
	}
	
	@Transient
	public boolean getPolicyFlag() {
		return policyFlag;
	}
	public void setPolicyFlag(boolean policyFlag) {
		this.policyFlag = policyFlag;
	}

}