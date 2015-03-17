package com.innofi.component.rbac.busiline.pojo;

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
@Table(name = "SYS_BASEL_BUSINESS_LINE")

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "sysBaselBusinessLineType")
@DiscriminatorValue("sysBaselBusinessLine")
public class SysBaselBusinessLine extends BasePojo {

    /**
     * BASEL业务条线名称
     */
    private String busilineBaselName;

    /**
     * BASEL业务条线描述
     */
    private String busilineBaselDesc;

    /**
     * BASEL业务条线编号
     */
    private String busilineBaselCode;

    /**
     * 修改人
     */
    private String updUserCode;

    /**
     * 修改日期
     */
    private Date updDate;

    /**
     * 修改机构
     */
    private String updOrgCode;

    /**
     * 创建人
     */
    private String crtUserCode;

    /**
     * 创建时间
     */
    private Date crtDate;

    /**
     * 创建机构
     */
    private String crtOrgCode;

    /**
     * 显示序号
     */
    private BigDecimal seq;

    /**
     * 树形层级
     */
    private BigDecimal treeLevel;

    /**
     * 父BASEL业务条线
     */
    private String parentId;

    /**
     * 状态
     */
    private String status;

    private String treeSeq;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "BUSILINE_BASEL_ID")
    public String getId() {
        return super.getId();
    }

    @Column(name = "BUSILINE_BASEL_NAME")
    public String getBusilineBaselName() {
        return this.busilineBaselName;
    }

    public void setBusilineBaselName(String busilineBaselName) {
        this.busilineBaselName = busilineBaselName;
    }

    @Column(name = "BUSILINE_BASEL_DESC")
    public String getBusilineBaselDesc() {
        return this.busilineBaselDesc;
    }

    public void setBusilineBaselDesc(String busilineBaselDesc) {
        this.busilineBaselDesc = busilineBaselDesc;
    }

    @Column(name = "BUSILINE_BASEL_CODE")
    public String getBusilineBaselCode() {
        return this.busilineBaselCode;
    }

    public void setBusilineBaselCode(String busilineBaselCode) {
        this.busilineBaselCode = busilineBaselCode;
    }

    @Column(name = "UPD_USER_CODE")
    public String getUpdUserCode() {
        return this.updUserCode;
    }

    public void setUpdUserCode(String updUserCode) {
        this.updUserCode = updUserCode;
    }

    @Column(name = "UPD_DATE")
    public Date getUpdDate() {
        return this.updDate;
    }

    public void setUpdDate(Date updDate) {
        this.updDate = updDate;
    }

    @Column(name = "UPD_ORG_CODE")
    public String getUpdOrgCode() {
        return this.updOrgCode;
    }

    public void setUpdOrgCode(String updOrgCode) {
        this.updOrgCode = updOrgCode;
    }

    @Column(name = "CRT_USER_CODE")
    public String getCrtUserCode() {
        return this.crtUserCode;
    }

    public void setCrtUserCode(String crtUserCode) {
        this.crtUserCode = crtUserCode;
    }

    @Column(name = "CRT_DATE")
    public Date getCrtDate() {
        return this.crtDate;
    }

    public void setCrtDate(Date crtDate) {
        this.crtDate = crtDate;
    }

    @Column(name = "CRT_ORG_CODE")
    public String getCrtOrgCode() {
        return this.crtOrgCode;
    }

    public void setCrtOrgCode(String crtOrgCode) {
        this.crtOrgCode = crtOrgCode;
    }

    @Column(name = "SEQ")
    public BigDecimal getSeq() {
        return this.seq;
    }

    public void setSeq(BigDecimal seq) {
        this.seq = seq;
    }

    @Column(name = "TREE_LEVEL")
    public BigDecimal getTreeLevel() {
        return this.treeLevel;
    }

    public void setTreeLevel(BigDecimal treeLevel) {
        this.treeLevel = treeLevel;
    }

    @Column(name = "PARENT_ID")
    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "TREE_SEQ")
    public String getTreeSeq() {
        return treeSeq;
    }

    public void setTreeSeq(String treeSeq) {
        this.treeSeq = treeSeq;
    }
}