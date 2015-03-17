package com.innofi.component.rbac.privilage.pojo;

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
@Table(name = "DA_DIMEN_DATA")

public class DaDimenData extends BasePojo {
    /**
     * 维度权限类型：例如机构维度，U：当前机构所有上级节点 D：当前机构所有下级节点 V：当前机构垂直所有节点 H: 当前机构水平所有节点 O: 其它实际值
     */
    private String dimenAuthType;


    /**
     * 维度控制ID
     */
    private String dimenControlId;

    /**
     * 维度字段ID
     */
    private String dimenFieldId;

    /**
     * 操作符:1-包含（in），2-等于，3-比对,4-like
     */
    private String operator;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 创建机构
     */
    private String crtOrgCode;

    /**
     * 创建日期
     */
    private Date crtDate;

    /**
     * 创建人
     */
    private String crtUserCode;

    /**
     * 值:可能是具体数值，也可能是变量、表达式。
     */
    private String dimenValue;

    /**
     * 值：中文名称
     */
    private String dimenValueName;

    /**
     * 修改机构
     */
    private String updOrgCode;

    /**
     * 修改日期
     */
    private Date updDate;

    /**
     * 修改人
     */
    private String updUserCode;

    private String dispName;

    private Date startDate;
    private Date endDate;
    private String dimenType;
    private String category;

    @Column(name = "DIMEN_AUTH_TYPE")
    public String getDimenAuthType() {
        return this.dimenAuthType;
    }

    public void setDimenAuthType(String dimenAuthType) {
        this.dimenAuthType = dimenAuthType;
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "DIMEN_DATA_ID")
    public String getId() {
        return super.getId();
    }

    @Column(name = "DIMEN_CONTROL_ID")
    public String getDimenControlId() {
        return this.dimenControlId;
    }

    public void setDimenControlId(String dimenControlId) {
        this.dimenControlId = dimenControlId;
    }

    @Column(name = "DIMEN_FIELD_ID")
    public String getDimenFieldId() {
        return this.dimenFieldId;
    }

    public void setDimenFieldId(String dimenFieldId) {
        this.dimenFieldId = dimenFieldId;
    }

    @Column(name = "OPERATOR")
    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "FIELD_NAME")
    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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

    @Column(name = "CRT_USER_CODE")
    public String getCrtUserCode() {
        return this.crtUserCode;
    }

    public void setCrtUserCode(String crtUserCode) {
        this.crtUserCode = crtUserCode;
    }

    @Column(name = "DIMEN_VALUE")
    public String getDimenValue() {
        return this.dimenValue;
    }

    public void setDimenValue(String dimenValue) {
        this.dimenValue = dimenValue;
    }

    @Column(name = "DIMEN_VALUE_NAME")
    public String getDimenValueName() {
        return dimenValueName;
    }

    public void setDimenValueName(String dimenValueName) {
        this.dimenValueName = dimenValueName;
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

    @Column(name = "UPD_USER_CODE")
    public String getUpdUserCode() {
        return this.updUserCode;
    }

    public void setUpdUserCode(String updUserCode) {
        this.updUserCode = updUserCode;
    }

    @Transient
    public String getDispName() {
        return dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    @Transient
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Transient
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Transient
    public String getDimenType() {
        return dimenType;
    }

    public void setDimenType(String dimenType) {
        this.dimenType = dimenType;
    }

    @Transient
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}