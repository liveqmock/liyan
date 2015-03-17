/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.pojo.metadata;

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
@Table(name = "MD_DIMENSION")
public class MdDimension extends BasePojo {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "DIMEN_ID")

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
     * 表结构ID
     */
    private String tableId;
    /**
     * 表结构
     */
    private String tableName;
    /**
     * 表中文名称
     */
    private String tableCnName;
    /**
     * 维度名称
     */
    private String dimenName;
    /**
     * 维度描述
     */
    private String dimenDesc;
    /**
     * 维度类型
     */
    private String dimenType;
    /**
     * 是否默认维度
     */
    private String isDimenDefault;
    /**
     * 有效状态
     */
    private String status;

    //是否选中标志
    private boolean selectFlag;
    //数据权限使用
    private String dimenControlId;

    @Column(name = "TABLE_ID")
    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    @Column(name = "TABLE_NAME")
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Transient
    public String getTableCnName() {
        return tableCnName;
    }

    public void setTableCnName(String tableCnName) {
        this.tableCnName = tableCnName;
    }

    @Column(name = "DIMEN_NAME")
    public String getDimenName() {
        return dimenName;
    }

    public void setDimenName(String dimenName) {
        this.dimenName = dimenName;
    }

    @Column(name = "DIMEN_DESC")
    public String getDimenDesc() {
        return dimenDesc;
    }

    public void setDimenDesc(String dimenDesc) {
        this.dimenDesc = dimenDesc;
    }

    @Column(name = "DIMEN_TYPE")
    public String getDimenType() {
        return dimenType;
    }

    public void setDimenType(String dimenType) {
        this.dimenType = dimenType;
    }

    @Column(name = "IS_DIMEN_DEFAULT")
    public String getIsDimenDefault() {
        return isDimenDefault;
    }

    public void setIsDimenDefault(String isDimenDefault) {
        this.isDimenDefault = isDimenDefault;
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
}
