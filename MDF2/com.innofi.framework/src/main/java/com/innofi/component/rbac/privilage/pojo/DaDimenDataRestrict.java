package com.innofi.component.rbac.privilage.pojo;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.innofi.framework.pojo.BasePojo;

/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期 todo 类描述 修订历史： 日期 作者 参考 描述 北京名晟信息技术有限公司版权所有.
 */
@Entity
@Table(name = "DA_DIMEN_DATA_RESTRICT")

public class DaDimenDataRestrict extends BasePojo {


    /**
     * 父约束关系：当约束关系类型为and 或 or 时，父约束关系为空
     */
    private String parentRestrictId;

    /**
     * 约束关系类型：1-and 2-or 3-维度数据，描述约束关系的属性
     */
    private String restrictType;

    /**
     * 约束关系描述
     */
    private String restrictDesc;

    /**
     * 表权限控制策略ID
     */
    private String tableAuthId;

    /**
     * 节点显示图标
     */
    private String restrictPic;

    /**
     * 维度ID
     */
    private String dimenId;

    /**
     * 维度名称
     */
    private String dimenCnName;

    /**
     * 维度类型
     */
    private String dimenType;

    /**
     * 字段ID
     */
    private String dimenFieldId;

    /**
     * 字段中文名称
     */
    private String fieldCnName;

    /**
     * 维度控制Id
     */
    private String dimenContorlId;


    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "assigned")
    @Column(name = "RESTRICT_ID")
    public String getId() {
        return super.getId();
    }

    @Column(name = "RESTRICT_DESC")
    public String getRestrictDesc() {
        return restrictDesc;
    }

    public void setRestrictDesc(String restrictDesc) {
        this.restrictDesc = restrictDesc;
    }

    @Column(name = "PARENT_RESTRICT_ID")
    public String getParentRestrictId() {
        return this.parentRestrictId;
    }

    public void setParentRestrictId(String parentRestrictId) {
        this.parentRestrictId = parentRestrictId;
    }

    @Column(name = "RESTRICT_TYPE")
    public String getRestrictType() {
        return this.restrictType;
    }

    public void setRestrictType(String restrictType) {
        this.restrictType = restrictType;
    }

    @Column(name = "TABLE_AUTH_ID")
    public String getTableAuthId() {
        return this.tableAuthId;
    }

    public void setTableAuthId(String tableAuthId) {
        this.tableAuthId = tableAuthId;
    }

    @Column(name = "RESTRICT_PIC")
    public String getRestrictPic() {
        return restrictPic;
    }

    public void setRestrictPic(String restrictPic) {
        this.restrictPic = restrictPic;
    }

    @Transient
    public String getDimenId() {
        return dimenId;
    }

    public void setDimenId(String dimenId) {
        this.dimenId = dimenId;
    }

    @Transient
    public String getDimenCnName() {
        return dimenCnName;
    }

    public void setDimenCnName(String dimenCnName) {
        this.dimenCnName = dimenCnName;
    }

    @Transient
    public String getDimenFieldId() {
        return dimenFieldId;
    }

    public void setDimenFieldId(String dimenFieldId) {
        this.dimenFieldId = dimenFieldId;
    }

    @Transient
    public String getFieldCnName() {
        return fieldCnName;
    }

    public void setFieldCnName(String fieldCnName) {
        this.fieldCnName = fieldCnName;
    }

    @Transient
    public String getDimenType() {
        return dimenType;
    }

    public void setDimenType(String dimenType) {
        this.dimenType = dimenType;
    }

    @Transient
    public String getDimenContorlId() {
        return dimenContorlId;
    }

    public void setDimenContorlId(String dimenContorlId) {
        this.dimenContorlId = dimenContorlId;
    }

}