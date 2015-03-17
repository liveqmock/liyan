package com.innofi.component.rbac.privilage.pojo;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;


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
@Table(name = "SYS_OBJ_BUSINESS")

public class SysObjBusiness extends BasePojo implements Serializable {

    /**
     * 业务条线ID
     */
    private String busilineId;

    /**
     * 业务条线名称
     */
    private String busilineName;


    /**
     * 对象类型：1 部门 2 岗位 3 用户
     */
    private String objBusilineType;

    /**
     * 对象编码
     */
    private String objCode;

    /**
     * 对象ID
     */
    private String objId;

    @Column(name = "BUSILINE_ID")
    public String getBusilineId() {
        return this.busilineId;
    }

    public void setBusilineId(String busilineId) {
        this.busilineId = busilineId;
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "OBJ_BUSILINE_ID")
    public String getId() {
        return super.getId();
    }

    @Column(name = "OBJ_BUSILINE_TYPE")
    public String getObjBusilineType() {
        return this.objBusilineType;
    }

    public void setObjBusilineType(String objBusilineType) {
        this.objBusilineType = objBusilineType;
    }

    @Column(name = "OBJ_CODE")
    public String getObjCode() {
        return this.objCode;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }

    @Column(name = "OBJ_ID")
    public String getObjId() {
        return this.objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    @Transient
    public String getBusilineName() {
        return busilineName;
    }

    public void setBusilineName(String busilineName) {
        this.busilineName = busilineName;
    }

}