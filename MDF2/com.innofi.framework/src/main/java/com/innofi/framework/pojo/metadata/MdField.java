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

import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.spring.context.ContextHolder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
@Table(name = "MD_FIELD")
public class MdField extends BasePojo {

    public MdField() {
        updateFlg = 0;
        status = FrameworkConstants.STATUS_EFFECTIVE;
    	String adminString = ContextHolder.getSystemProperties().getString("mtp.system.admin.account","admin");
    	if(adminString.indexOf(",")!=-1){
    		crtUserCode = adminString.split(",")[0];
    	}else{
            crtUserCode = adminString;
    	}
    }

    /**
     * 表结构ID
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "FIELD_ID")
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
     * 表结构id
     */
    private String tableId;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 字段中文名称
     */
    private String fieldCnName;
    /**
     * 字段描述
     */
    private String fieldDesc;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 字段长度
     */
    private BigDecimal fieldLen;
    /**
     * 小数位数
     */
    private BigDecimal precision;
    /**
     * 显示顺序
     */
    private BigDecimal fieldSeq;
    /**
     * 数据格式
     */
    private String dataFmt;
    /**
     * 代码字典
     */
    private String fieldDict;
    /**
     * 实体类属性名
     */
    private String entityAttriName;
    /**
     * 约束条件
     */
    private String consCond;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 是否可以为空
     */
    private String isNull;
    /**
     * 是否主键PK
     */
    private String isPk;
    /**
     * 是否唯一索引
     */
    private String isPkIndex;
    /**
     * 是否统计字段
     */
    private String isStati;
    /**
     * 是否冗余字段
     */
    private String isRedundant;
    /**
     * 冗余源字段
     */
    private String srcReduField;
    /**
     * 是否机构权限控制项
     */
    private String isOrgAuth;
    /**
     * 是否数据控制项
     */
    private String isDataAuth;
    /**
     * 是否父ID字段
     */
    private String isParentid;
    /**
     * blank Text 输入框默认值
     */
    private String bankTextInf;
    /**
     * tip 输入框提示信息
     */
    private String tipInf;
    /**
     * label 文本
     */
    private String labelInf;
    /**
     * 查询方式
     */
    private String queryMethod;
    /**
     * 列表是否显示
     */
    private String isTableView;
    /**
     * 新增是否只读
     */
    private String isAddRead;
    /**
     * 修改是否只读
     */
    private String isUpdRead;
    /**
     * 正则表达式
     */
    private String regExp;
    /**
     * 控件类型
     */
    private String eleType;
    /**
     * 新增是否显示
     */
    private String isAddView;
    /**
     * 修改是否显示
     */
    private String isUpdView;
    /**
     * 是否查询条件
     */
    private String isQueryCond;
    /**
     * 是否扩展字段
     */
    private String isExtend;
    /**
     * 是否有效状态
     */
    private String status;

    /**
     * 下拉框选择类型:单选树、多选树、单选列表、多选列表
     */
    private String selectType;

    /**
     * 选择类型:机构、部门 只对机构有效
     */
    private String orgCategory;

    /**
     * 树展开层级限制，该参数值需大于1，列表无效。若传递2，则树只能展开2级，第3级节点则不能展开。
     */
    private String expandLevel;

    /**
     * 取该节点的下级节点
     */
    private String rootCode;


    /**
     * 1、新增 2、修改 3、删除
     */
    private int updateFlg;

    /**
     * 使用Hibernate缓存
     *
     * @return true
     */
    @Transient
    public boolean getCacheAble() {
        return true;
    }

    @Transient
    public String getRootCode() {
        return rootCode;
    }

    public void setRootCode(String rootCode) {
        this.rootCode = rootCode;
    }

    @Transient
    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    @Transient
    public String getOrgCategory() {
        return orgCategory;
    }

    public void setOrgCategory(String orgCategory) {
        this.orgCategory = orgCategory;
    }

    @Transient
    public String getExpandLevel() {
        return expandLevel;
    }

    public void setExpandLevel(String expandLevel) {
        this.expandLevel = expandLevel;
    }

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

    @Column(name = "FIELD_NAME")
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Column(name = "FIELD_CN_NAME")
    public String getFieldCnName() {
        return fieldCnName;
    }

    public void setFieldCnName(String fieldCnName) {
        this.fieldCnName = fieldCnName;
    }

    @Column(name = "FIELD_DESC")
    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    @Column(name = "FIELD_TYPE")
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @Column(name = "FIELD_LEN")
    public BigDecimal getFieldLen() {
        return fieldLen;
    }

    public void setFieldLen(BigDecimal fieldLen) {
        this.fieldLen = fieldLen;
    }

    @Column(name = "PRECISION")
    public BigDecimal getPrecision() {
        return precision;
    }

    public void setPrecision(BigDecimal precision) {
        this.precision = precision;
    }

    @Column(name = "FIELD_SEQ")
    public BigDecimal getFieldSeq() {
        return fieldSeq;
    }

    public void setFieldSeq(BigDecimal fieldSeq) {
        this.fieldSeq = fieldSeq;
    }

    @Column(name = "DATA_FMT")
    public String getDataFmt() {
        return dataFmt;
    }

    public void setDataFmt(String dataFmt) {
        this.dataFmt = dataFmt;
    }

    @Column(name = "FIELD_DICT")
    public String getFieldDict() {
        return fieldDict;
    }

    public void setFieldDict(String fieldDict) {
        this.fieldDict = fieldDict;
    }

    @Column(name = "ENTITY_ATTRI_NAME")
    public String getEntityAttriName() {
        return entityAttriName;
    }

    public void setEntityAttriName(String entityAttriName) {
        this.entityAttriName = entityAttriName;
    }

    @Column(name = "CONS_COND")
    public String getConsCond() {
        return consCond;
    }

    public void setConsCond(String consCond) {
        this.consCond = consCond;
    }

    @Column(name = "DEFAULT_VALUE")
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Column(name = "IS_NULL")
    public String getIsNull() {
        return isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }

    @Column(name = "IS_PK")
    public String getIsPk() {
        return isPk;
    }

    public void setIsPk(String isPk) {
        this.isPk = isPk;
    }

    @Column(name = "IS_PK_INDEX")
    public String getIsPkIndex() {
        return isPkIndex;
    }

    public void setIsPkIndex(String isPkIndex) {
        this.isPkIndex = isPkIndex;
    }

    @Column(name = "IS_STATI")
    public String getIsStati() {
        return isStati;
    }

    public void setIsStati(String isStati) {
        this.isStati = isStati;
    }

    @Column(name = "IS_REDUNDANT")
    public String getIsRedundant() {
        return isRedundant;
    }

    public void setIsRedundant(String isRedundant) {
        this.isRedundant = isRedundant;
    }

    @Column(name = "SRC_REDU_FIELD")
    public String getSrcReduField() {
        return srcReduField;
    }

    public void setSrcReduField(String srcReduField) {
        this.srcReduField = srcReduField;
    }

    @Column(name = "IS_ORG_AUTH")
    public String getIsOrgAuth() {
        return isOrgAuth;
    }

    public void setIsOrgAuth(String isOrgAuth) {
        this.isOrgAuth = isOrgAuth;
    }

    @Column(name = "IS_DATA_AUTH")
    public String getIsDataAuth() {
        return isDataAuth;
    }

    public void setIsDataAuth(String isDataAuth) {
        this.isDataAuth = isDataAuth;
    }

    @Column(name = "IS_PARENTID")
    public String getIsParentid() {
        return isParentid;
    }

    public void setIsParentid(String isParentid) {
        this.isParentid = isParentid;
    }

    @Column(name = "BANK_TEXT_INF")
    public String getBankTextInf() {
        return bankTextInf;
    }

    public void setBankTextInf(String bankTextInf) {
        this.bankTextInf = bankTextInf;
    }

    @Column(name = "TIP_INF")
    public String getTipInf() {
        return tipInf;
    }

    public void setTipInf(String tipInf) {
        this.tipInf = tipInf;
    }

    @Column(name = "LABEL_INF")
    public String getLabelInf() {
        return labelInf;
    }

    public void setLabelInf(String labelInf) {
        this.labelInf = labelInf;
    }

    @Column(name = "QUERY_METHOD")
    public String getQueryMethod() {
        return queryMethod;
    }

    public void setQueryMethod(String queryMethod) {
        this.queryMethod = queryMethod;
    }

    @Column(name = "IS_TABLE_VIEW")
    public String getIsTableView() {
        return isTableView;
    }

    public void setIsTableView(String isTableView) {
        this.isTableView = isTableView;
    }

    @Column(name = "IS_ADD_READ")
    public String getIsAddRead() {
        return isAddRead;
    }

    public void setIsAddRead(String isAddRead) {
        this.isAddRead = isAddRead;
    }

    @Column(name = "IS_UPD_READ")
    public String getIsUpdRead() {
        return isUpdRead;
    }

    public void setIsUpdRead(String isUpdRead) {
        this.isUpdRead = isUpdRead;
    }

    @Column(name = "REG_EXP")
    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }

    @Column(name = "ELE_TYPE")
    public String getEleType() {
        return eleType;
    }

    public void setEleType(String eleType) {
        this.eleType = eleType;
    }

    @Column(name = "IS_ADD_VIEW")
    public String getIsAddView() {
        return isAddView;
    }

    public void setIsAddView(String isAddView) {
        this.isAddView = isAddView;
    }

    @Column(name = "IS_UPD_VIEW")
    public String getIsUpdView() {
        return isUpdView;
    }

    public void setIsUpdView(String isUpdView) {
        this.isUpdView = isUpdView;
    }

    @Column(name = "IS_QUERY_COND")
    public String getIsQueryCond() {
        return isQueryCond;
    }

    public void setIsQueryCond(String isQueryCond) {
        this.isQueryCond = isQueryCond;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    @Column(name = "IS_EXTEND")
    public String getIsExtend() {
        return isExtend;
    }

    public void setIsExtend(String extend) {
        isExtend = extend;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Transient
    public int getUpdateFlg() {
        return updateFlg;
    }

    public void setUpdateFlg(int updateFlg) {
        this.updateFlg = updateFlg;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MdField fieldPojo = (MdField) o;

        //if (bankTextInf != null ? !bankTextInf.equals(fieldPojo.bankTextInf) : fieldPojo.bankTextInf != null) return false;
        if (consCond != null ? !consCond.equals(fieldPojo.consCond) : fieldPojo.consCond != null) return false;
        if (dataFmt != null ? !dataFmt.equals(fieldPojo.dataFmt) : fieldPojo.dataFmt != null) return false;
        if (defaultValue != null ? !defaultValue.equals(fieldPojo.defaultValue) : fieldPojo.defaultValue != null)
            return false;
        if (eleType != null ? !eleType.equals(fieldPojo.eleType) : fieldPojo.eleType != null) return false;
        if (entityAttriName != null ? !entityAttriName.equals(fieldPojo.entityAttriName) : fieldPojo.entityAttriName != null)
            return false;
        if (expandLevel != null ? !expandLevel.equals(fieldPojo.expandLevel) : fieldPojo.expandLevel != null)
            return false;
        if (fieldCnName != null ? !fieldCnName.equals(fieldPojo.fieldCnName) : fieldPojo.fieldCnName != null)
            return false;
        if (fieldDesc != null ? !fieldDesc.equals(fieldPojo.fieldDesc) : fieldPojo.fieldDesc != null) return false;
        if (fieldDict != null ? !fieldDict.equals(fieldPojo.fieldDict) : fieldPojo.fieldDict != null) return false;
        if (fieldLen != null ? !fieldLen.equals(fieldPojo.fieldLen) : fieldPojo.fieldLen != null) return false;
        if (fieldName != null ? !fieldName.equals(fieldPojo.fieldName) : fieldPojo.fieldName != null) return false;
        if (fieldSeq != null ? !fieldSeq.equals(fieldPojo.fieldSeq) : fieldPojo.fieldSeq != null) return false;
        if (fieldType != null ? !fieldType.equals(fieldPojo.fieldType) : fieldPojo.fieldType != null) return false;
        if (isAddRead != null ? !isAddRead.equals(fieldPojo.isAddRead) : fieldPojo.isAddRead != null) return false;
        if (isAddView != null ? !isAddView.equals(fieldPojo.isAddView) : fieldPojo.isAddView != null) return false;
        if (isDataAuth != null ? !isDataAuth.equals(fieldPojo.isDataAuth) : fieldPojo.isDataAuth != null) return false;
        if (isNull != null ? !isNull.equals(fieldPojo.isNull) : fieldPojo.isNull != null) return false;
        if (isOrgAuth != null ? !isOrgAuth.equals(fieldPojo.isOrgAuth) : fieldPojo.isOrgAuth != null) return false;
        if (isParentid != null ? !isParentid.equals(fieldPojo.isParentid) : fieldPojo.isParentid != null) return false;
        if (isPk != null ? !isPk.equals(fieldPojo.isPk) : fieldPojo.isPk != null) return false;
        if (isPkIndex != null ? !isPkIndex.equals(fieldPojo.isPkIndex) : fieldPojo.isPkIndex != null) return false;
        if (isQueryCond != null ? !isQueryCond.equals(fieldPojo.isQueryCond) : fieldPojo.isQueryCond != null)
            return false;
        if (isRedundant != null ? !isRedundant.equals(fieldPojo.isRedundant) : fieldPojo.isRedundant != null)
            return false;
        if (isStati != null ? !isStati.equals(fieldPojo.isStati) : fieldPojo.isStati != null) return false;
        if (isTableView != null ? !isTableView.equals(fieldPojo.isTableView) : fieldPojo.isTableView != null)
            return false;
        if (isUpdRead != null ? !isUpdRead.equals(fieldPojo.isUpdRead) : fieldPojo.isUpdRead != null) return false;
        if (isUpdView != null ? !isUpdView.equals(fieldPojo.isUpdView) : fieldPojo.isUpdView != null) return false;
        //if (labelInf != null ? !labelInf.equals(fieldPojo.labelInf) : fieldPojo.labelInf != null) return false;
        if (orgCategory != null ? !orgCategory.equals(fieldPojo.orgCategory) : fieldPojo.orgCategory != null)
            return false;
        if (precision != null ? !precision.equals(fieldPojo.precision) : fieldPojo.precision != null) return false;
        if (queryMethod != null ? !queryMethod.equals(fieldPojo.queryMethod) : fieldPojo.queryMethod != null)
            return false;
        if (regExp != null ? !regExp.equals(fieldPojo.regExp) : fieldPojo.regExp != null) return false;
        if (rootCode != null ? !rootCode.equals(fieldPojo.rootCode) : fieldPojo.rootCode != null) return false;
        if (selectType != null ? !selectType.equals(fieldPojo.selectType) : fieldPojo.selectType != null) return false;
        if (srcReduField != null ? !srcReduField.equals(fieldPojo.srcReduField) : fieldPojo.srcReduField != null)
            return false;
        //if (tipInf != null ? !tipInf.equals(fieldPojo.tipInf) : fieldPojo.tipInf != null) return false;
        return true;
    }


}
