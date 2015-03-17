package com.innofi.component.sie.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.GenericGenerator;

/**
 * 功能/ 模块：状态引擎
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          业务处理过程实体
 * @found date: 2013-11-1
 * @found time: 20:40:56
 */
@Entity
@Table(name = "WF_PROCESS")

public class WfProcess extends BasePojo implements Serializable {
    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 处理意见
     */
    private String dealIdea;

    /**
     * 是否结束：1 已结束 0或为空 未结束
     */
    private String isEnd;

    /**
     * 是否最近
     */
    private String isNew;

    /**
     * 模块编码
     */
    private String moduleCode;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 业务处理ID
     */
    private String nodeId;

    /**
     * 操作日期
     */
    private Date operDate;

    /**
     * 操作名称
     */
    private String operDesc;

    /**
     * 操作机构
     */
    private String operOrgCode;

    /**
     * 操作岗位
     */
    private String operPostCode;

    /**
     * 操作角色
     */
    private String operRoleCode;

    /**
     * 操作用户
     */
    private String operUserCode;

    /**
     * 操作类型:1-新增 2-修改 3-删除  4-提交 5-审核 6-退回 7-浏览 8-审批 9-退回到编辑中10-撤消
     */
    private String operType;

    /**
     * 状态：1 编辑中（草稿） 2 待审核（已提交） 3 待审批（已审核） 4 已审批（待归档） 5 已归档 6 审核退回 7 审批退回
     */
    private String status;

    /**
     * 创建日期
     */
    private Date crtDate;

    /**
     * 创建机构
     */
    private String crtOrgCode;

    /**
     * 创建人
     */
    private String crtUserCode;

    /**
     * 修改日期
     */
    private Date updDate;

    /**
     * 修改机构
     */
    private String updOrgCode;

    /**
     * 修改人
     */
    private String updUserCode;

    /**
     * 创建机构名称
     */
    private String crtOrgCodeName;

    /**
     * 创建用户名称
     */
    private String crtUserCodeName;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 节点名称
     */
    private String nodeName;


    @Column(name = "BUSINESS_ID")
    public String getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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

    @Column(name = "CRT_USER_CODE")
    public String getCrtUserCode() {
        return this.crtUserCode;
    }

    public void setCrtUserCode(String crtUserCode) {
        this.crtUserCode = crtUserCode;
    }

    @Column(name = "DEAL_IDEA")
    public String getDealIdea() {
        return this.dealIdea;
    }

    public void setDealIdea(String dealIdea) {
        this.dealIdea = dealIdea;
    }

    @Column(name = "IS_END")
    public String getIsEnd() {
        return this.isEnd;
    }

    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }

    @Column(name = "IS_NEW")
    public String getIsNew() {
        return this.isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    @Column(name = "MODULE_CODE")
    public String getModuleCode() {
        return this.moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    @Column(name = "MODULE_NAME")
    public String getModuleName() {
        return this.moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Column(name = "NODE_ID")
    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @Column(name = "OPER_DATE")
    public Date getOperDate() {
        return this.operDate;
    }

    public void setOperDate(Date operDate) {
        this.operDate = operDate;
    }

    @Column(name = "OPER_DESC")
    public String getOperDesc() {
        return this.operDesc;
    }

    public void setOperDesc(String operDesc) {
        this.operDesc = operDesc;
    }

    @Column(name = "OPER_ORG_CODE")
    public String getOperOrgCode() {
        return this.operOrgCode;
    }

    public void setOperOrgCode(String operOrgCode) {
        this.operOrgCode = operOrgCode;
    }

    @Column(name = "OPER_POST_CODE")
    public String getOperPostCode() {
        return this.operPostCode;
    }

    public void setOperPostCode(String operPostCode) {
        this.operPostCode = operPostCode;
    }

    @Column(name = "OPER_ROLE_CODE")
    public String getOperRoleCode() {
        return this.operRoleCode;
    }

    public void setOperRoleCode(String operRoleCode) {
        this.operRoleCode = operRoleCode;
    }

    @Column(name = "OPER_TYPE")
    public String getOperType() {
        return this.operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    @Column(name = "OPER_USER_CODE")
    public String getOperUserCode() {
        return this.operUserCode;
    }

    public void setOperUserCode(String operUserCode) {
        this.operUserCode = operUserCode;
    }

    @Column(name = "NODE_TYPE")
    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    @Column(name = "NODE_NAME")
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "PROCESS_ID")
    public String getId() {
        return super.getId();
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Column(name = "UPD_USER_CODE")
    public String getUpdUserCode() {
        return this.updUserCode;
    }

    public void setUpdUserCode(String updUserCode) {
        this.updUserCode = updUserCode;
    }

    @Transient
    public String getCrtOrgCodeName() {
        return crtOrgCodeName;
    }

    public void setCrtOrgCodeName(String crtOrgCodeName) {
        this.crtOrgCodeName = crtOrgCodeName;
    }

    @Transient
    public String getCrtUserCodeName() {
        return crtUserCodeName;
    }

    public void setCrtUserCodeName(String crtUserCodeName) {
        this.crtUserCodeName = crtUserCodeName;
    }

}

