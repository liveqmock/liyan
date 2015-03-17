package com.innofi.component.rbac.dict.pojo;

import java.math.BigDecimal;
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
@Table(name = "SYS_DICT")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysDictType")
@DiscriminatorValue("sysDict")
public class SysDict extends BasePojo {

    /**
     * 父代码字典ID
     */
    private String parentDictId;

    /**
     * 代码字典编号
     */
    private String dictCode;

    /**
     * 代码字典名称
     */
    private String dictName;

    /**
     * 代码字典描述
     */
    private String dictDesc;

    /**
     * 树形层级:00--根节点,非根节点1,2,3
     */
    private BigDecimal treeLevel;

    /**
     * 代码字典序列
     */
    private String treeSeq;

    /**
     * 显示序号
     */
    private BigDecimal viewSeq;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态：1-启用 2-停用
     */
    private String status;

    /**
     * 创建机构
     */
    private String crtOrgCode;

    /**
     * 创建人
     */
    private String crtUserCode;

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
     * 创建时间
     */
    private Date crtDate;

    /**
     * 备注
     */
    private String remark;
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "DICT_ID")
    public String getId() {
        return super.getId();
    }

    @Column(name = "PARENT_DICT_ID")
    public String getParentDictId() {
        return this.parentDictId;
    }

    public void setParentDictId(String parentDictId) {
        this.parentDictId = parentDictId;
    }

    @Column(name = "DICT_CODE")
    public String getDictCode() {
        return this.dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    @Column(name = "DICT_NAME")
    public String getDictName() {
        return this.dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    @Column(name = "DICT_DESC")
    public String getDictDesc() {
        return this.dictDesc;
    }

    public void setDictDesc(String dictDesc) {
        this.dictDesc = dictDesc;
    }

    @Column(name = "TREE_LEVEL")
    public BigDecimal getTreeLevel() {
        return this.treeLevel;
    }

    public void setTreeLevel(BigDecimal treeLevel) {
        this.treeLevel = treeLevel;
    }

    @Column(name = "TREE_SEQ")
    public String getTreeSeq() {
        return this.treeSeq;
    }

    public void setTreeSeq(String treeSeq) {
        this.treeSeq = treeSeq;
    }

    @Column(name = "VIEW_SEQ")
    public BigDecimal getViewSeq() {
        return this.viewSeq;
    }

    public void setViewSeq(BigDecimal viewSeq) {
        this.viewSeq = viewSeq;
    }

    @Column(name = "DICT_TYPE")
    public String getDictType() {
        return this.dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
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

    @Column(name = "CRT_DATE")
    public Date getCrtDate() {
        return this.crtDate;
    }

    public void setCrtDate(Date crtDate) {
        this.crtDate = crtDate;
    }
    
    @Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}