/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.pojo.metadata;

import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

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
@Table(name = "MD_ENTITY_OBJECT")
public class MdEntityObject extends BasePojo {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "OBJ_ID")
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
     * 实体对象名
     */
    private String objName;
    /**
     * 实体对象描述
     */
    private String objDesc;
    /**
     * 路径
     */
    private String objPath;
    /**
     * 接口
     */
    private String interFace;
    /**
     * 接口名称
     */
    private String interFaceName;
    /**
     * 对象类型
     */
    private String objType;

    @Column(name = "OBJ_NAME")
    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    @Column(name = "OBJ_DESC")
    public String getObjDesc() {
        return objDesc;
    }

    public void setObjDesc(String objDesc) {
        this.objDesc = objDesc;
    }

    @Column(name = "OBJ_PATH")
    public String getObjPath() {
        return objPath;
    }

    public void setObjPath(String objPath) {
        this.objPath = objPath;
    }

    @Column(name = "INTERFACE")
    public String getInterFace() {
        return interFace;
    }

    public void setInterFace(String interFace) {
        this.interFace = interFace;
    }

    @Transient
    public String getInterFaceName() {
        return interFaceName;
    }

    public void setInterFaceName(String interFaceName) {
        this.interFaceName = interFaceName;
    }

    @Column(name = "OBJ_TYPE")
    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

}
