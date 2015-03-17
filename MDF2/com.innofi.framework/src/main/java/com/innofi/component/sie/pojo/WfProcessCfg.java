package com.innofi.component.sie.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "WF_PROCESS_CFG")

public class WfProcessCfg extends BasePojo implements Serializable {

    /**
     * 上一步节点名称
     */
    private String prevNodeId;

    /**
     * 上一步业务处理节点名称
     */
    private String prevNodeIdName;

    /**
     * 适用机构
     */
    private String orgLevel;

    /**
     * 节点类型：1-人工节点 2-判断节点 3-自动节点
     */
    private String nodeType;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 判断表达式 EL表达式：${param_name}>5
     */
    private String condExpre;

    /**
     * 模块编码
     */
    private String moduleCode;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 是否当前用户部门
     */
    private String isUserOrg;

    /**
     * 是否当前用户
     */
    private String isUser;

    /**
     * 操作机构
     */
    private String operOrgCode;

    /**
     * 操作机构名称
     */
    private String operOrgCodeName;

    /**
     * 操作岗位
     */
    private String operPostCode;

    /**
     * 操作岗位名称
     */
    private String operPostCodeName;

    /**
     * 操作角色
     */
    private String operRoleCode;

    /**
     * 操作角色名称
     */
    private String operRoleCodeName;

    /**
     * 操作人
     */
    private String operUserCode;

    /**
     * 操作人名称
     */
    private String operUserCodeName;

    /**
     * 业务处理是否结束：1 已结束 0 未结束
     */
    private String isEnd;

    /**
     * 业务状态：对该状态的业务处理配置信息，1 编辑中（草稿） 2 待审核（已提交） 3 待审批（已审核）  4 已审批（待归档） 5 已归档 6 审核退回 7 审批退回
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


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "NODE_ID")
    public String getId() {
        return super.getId();
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

    @Column(name = "IS_END")
    public String getIsEnd() {
        return this.isEnd;
    }

    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }

    @Column(name = "IS_USER_ORG")
    public String getIsUserOrg() {
        return this.isUserOrg;
    }

    public void setIsUserOrg(String isUserOrg) {
        this.isUserOrg = isUserOrg;
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

    @Column(name = "OPER_USER_CODE")
    public String getOperUserCode() {
        return this.operUserCode;
    }

    public void setOperUserCode(String operUserCode) {
        this.operUserCode = operUserCode;
    }

    @Column(name = "PREV_NODE_ID")
    public String getPrevNodeId() {
        return this.prevNodeId;
    }

    public void setPrevNodeId(String prevNodeId) {
        this.prevNodeId = prevNodeId;
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

    @Column(name = "ORG_LEVEL")
    public String getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    @Column(name = "NODE_TYPE")
    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    @Column(name = "COND_EXPRE")
    public String getCondExpre() {
        return condExpre;
    }

    public void setCondExpre(String condExpre) {
        this.condExpre = condExpre;
    }

    @Column(name = "IS_USER")
    public String getIsUser() {
        return isUser;
    }

    @Column(name = "NODE_NAME")
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setIsUser(String user) {
        isUser = user;
    }

    @Transient
    public String getOperOrgCodeName() {
        return operOrgCodeName;
    }

    public void setOperOrgCodeName(String operOrgCodeName) {
        this.operOrgCodeName = operOrgCodeName;
    }

    @Transient
    public String getOperPostCodeName() {
        return operPostCodeName;
    }

    public void setOperPostCodeName(String operPostCodeName) {
        this.operPostCodeName = operPostCodeName;
    }

    @Transient
    public String getOperRoleCodeName() {
        return operRoleCodeName;
    }

    public void setOperRoleCodeName(String operRoleCodeName) {
        this.operRoleCodeName = operRoleCodeName;
    }

    @Transient
    public String getOperUserCodeName() {
        return operUserCodeName;
    }

    public void setOperUserCodeName(String operUserCodeName) {
        this.operUserCodeName = operUserCodeName;
    }

    @Transient
    public String getPrevNodeIdName() {
        return prevNodeIdName;
    }

    public void setPrevNodeIdName(String prevNodeIdName) {
        this.prevNodeIdName = prevNodeIdName;
    }
}