/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.pojo.metadata;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.innofi.framework.pojo.BasePojo;

/**
 * 功能/ 模块：测试模块
 *
 * @author 张磊 alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          测试实体对象
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Entity
@Table(name = "MD_DIMEN_FIELD")
public class MdDimenField extends BasePojo {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "DIMEN_FIELD_ID")
    public String getId() {
        return super.getId();
    }

    @Column(name = "CRT_ORG_CODE")
    public String getCrtOrgCode() {
        return super.getCrtOrgCode();
    }

    @Column(name = "UPD_ORG_CODE")
    public String getUpdOrgCode() {
        return super.getUpdOrgCode();
    }

    @Column(name = "CRT_USER_CODE")
    public String getCrtUserCode() {
        return super.getCrtUserCode();
    }

    @Column(name = "CRT_DATE")
    public Date getCrtDate() {
        return super.getCrtDate();
    }

    @Column(name = "UPD_DATE")
    public Date getUpdDate() {
        return super.getUpdDate();
    }

    @Column(name = "UPD_USER_CODE")
    public String getUpdUserCode() {
        return super.getUpdUserCode();
    }

    /**
     * 维度ID
     */
    private String dimenId;
    /**
     * 字段ID
     */
    private String fieldId;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 维度字段顺序号
     */
    private BigDecimal fieldSeq;
    /**
     * 有效状态
     */
    private String status;
    //选中状态
    private boolean selectFlag;
    //数据权限使用
    private String dimenControlId;
    //字段元数据中的中文名
    private String fieldCnName;

    @Column(name = "DIMEN_ID")
    public String getDimenId() {
        return dimenId;
    }

    public void setDimenId(String dimenId) {
        this.dimenId = dimenId;
    }

    @Column(name = "FIELD_ID")
    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    @Column(name = "FIELD_NAME")
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Column(name = "FIELD_SEQ")
    public BigDecimal getFieldSeq() {
        return fieldSeq;
    }

    public void setFieldSeq(BigDecimal fieldSeq) {
        this.fieldSeq = fieldSeq;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Transient
    public boolean getSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }

    @Transient
    public String getDimenControlId() {
        return dimenControlId;
    }

    public void setDimenControlId(String dimenControlId) {
        this.dimenControlId = dimenControlId;
    }

    @Transient
    public String getFieldCnName() {
        return fieldCnName;
    }

    public void setFieldCnName(String fieldCnName) {
        this.fieldCnName = fieldCnName;
    }
}
