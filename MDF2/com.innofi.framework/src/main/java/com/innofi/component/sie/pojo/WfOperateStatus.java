package com.innofi.component.sie.pojo;

import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;


/**
* 功能/ 模块：todo 模块中文名称
*
* @author  todo 开发人员姓名 邮箱
* @version todo 当前版本 日期
*          todo 类描述
*          修订历史：
*          日期  作者  参考  描述
*          北京名晟信息技术有限公司版权所有.
*/
@Entity
@Table(name="WF_OPERATE_STATUS")

public class WfOperateStatus extends BasePojo implements Serializable {
        /**
    * 操作描述
    */
    private String operDesc;
        /**
    * 操作状态
    */
    private String operStatus;
        /**
    * 操作类型：该状态对应的操作，1 提交 2 审核 3 审批 4 归档 5 撤消
    */
    private String operType;
    
        @Column(name="OPER_DESC")
    public String getOperDesc(){
    return this.operDesc;
    }

    public void setOperDesc(String operDesc){
    this.operDesc = operDesc;
    }
    
        @Column(name="OPER_STATUS")
    public String getOperStatus(){
    return this.operStatus;
    }

    public void setOperStatus(String operStatus){
    this.operStatus = operStatus;
    }
    
        @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
    @Column(name="OPER_STATUS_ID")
    public String getId(){
    return super.getId();
    }
    
        @Column(name="OPER_TYPE")
    public String getOperType(){
    return this.operType;
    }

    public void setOperType(String operType){
    this.operType = operType;
    }
}