package com.innofi.component.rbac.parameter.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.GenericGenerator;

/**
 * ${model.modelCnName}
 */

@Entity
@Table(name = "SYS_PARAMETER")

public class SysParameter extends BasePojo {

    /**
     * 参数编码
     */
    private String paraCode;

    /**
     * 参数名称
     */
    private String paraName;

    /**
     * 父参数
     */
    private String parentParaId;

    /**
     * 参数类型
     */
    private String paraType;

    /**
     * 参数值
     */
    private String paraValue;

    /**
     * 备注
     */
    private String paraDesc;

    /**
     * 创建机构
     */
    private String crtOrgCode;

    /**
     * 创建人
     */
    private String crtUserCode;

    /**
     * 创建时间
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

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "PARA_ID")
    public String getId() {
        return super.getId();
    }

    @Column(name = "PARA_CODE")
    public String getParaCode() {
        return this.paraCode;
    }

    public void setParaCode(String paraCode) {
        this.paraCode = paraCode;
    }

    @Column(name = "PARA_NAME")
    public String getParaName() {
        return this.paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName;
    }

    @Column(name = "PARENT_PARA_ID")
    public String getParentParaId() {
        return this.parentParaId;
    }

    public void setParentParaId(String parentParaId) {
        this.parentParaId = parentParaId;
    }

    @Column(name = "PARA_TYPE")
    public String getParaType() {
        return this.paraType;
    }

    public void setParaType(String paraType) {
        this.paraType = paraType;
    }

    @Column(name = "PARA_VALUE")
    public String getParaValue() {
        return this.paraValue;
    }

    public void setParaValue(String paraValue) {
        this.paraValue = paraValue;
    }

    @Column(name = "PARA_DESC")
    public String getParaDesc() {
        return this.paraDesc;
    }

    public void setParaDesc(String paraDesc) {
        this.paraDesc = paraDesc;
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

}