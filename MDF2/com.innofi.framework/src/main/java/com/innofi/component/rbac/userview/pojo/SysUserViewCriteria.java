package com.innofi.component.rbac.userview.pojo;
import java.math.BigDecimal;
import java.io.Serializable;
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
* SysUserViewCriteria实体对象
* @author  liumy2009@126.com
* @version V1.0.0
* 修订历史：
* 日期  作者  参考  描述
* 北京名晟信息技术有限公司版权所有.
*/
@Entity
@Table(name="SYS_USER_VIEW_CRITERIA")
public class SysUserViewCriteria extends BasePojo implements Serializable {
        /**
    * 配置内容
    */
    private String cfgContent;
    
        /**
    * 1-列表显示列 2-查询条件
    */
    private String cfgType;
    
        /**
    * 创建时间
    */
    private Date crtDate;
    
        /**
    * 创建机构
    */
    private String crtOrgCode;
    
        /**
    * 创建机构名称
    */
    private String crtOrgName;
    
        /**
    * 创建人
    */
    private String crtUserCode;
    
        /**
    * 创建人姓名
    */
    private String crtUserName;
    
    
        /**
    * 查询方案名称
    */
    private String schemaName;
    
        /**
    * 修改日期
    */
    private Date updDate;
    
        /**
    * 修改机构
    */
    private String updOrgCode;
    
        /**
    * 修改机构名称
    */
    private String updOrgName;
    
        /**
    * 修改人
    */
    private String updUserCode;
    
        /**
    * 修改人姓名
    */
    private String updUserName;
    
        /**
    * 用户ID
    */
    private String userId;
    
        /**
    * 视图路径
    */
    private String viewPath;
    
        @Column(name="CFG_CONTENT")
    public String getCfgContent(){
    return this.cfgContent;
    }
    public void setCfgContent(String cfgContent){
    this.cfgContent = cfgContent;
    }
    
        @Column(name="CFG_TYPE")
    public String getCfgType(){
    return this.cfgType;
    }
    public void setCfgType(String cfgType){
    this.cfgType = cfgType;
    }
    
        @Column(name="CRT_DATE")
    public Date getCrtDate(){
    return this.crtDate;
    }
    public void setCrtDate(Date crtDate){
    this.crtDate = crtDate;
    }
    
        @Column(name="CRT_ORG_CODE")
    public String getCrtOrgCode(){
    return this.crtOrgCode;
    }
    public void setCrtOrgCode(String crtOrgCode){
    this.crtOrgCode = crtOrgCode;
    }
    
        @Column(name="CRT_ORG_NAME")
    public String getCrtOrgName(){
    return this.crtOrgName;
    }
    public void setCrtOrgName(String crtOrgName){
    this.crtOrgName = crtOrgName;
    }
    
        @Column(name="CRT_USER_CODE")
    public String getCrtUserCode(){
    return this.crtUserCode;
    }
    public void setCrtUserCode(String crtUserCode){
    this.crtUserCode = crtUserCode;
    }
    
        @Column(name="CRT_USER_NAME")
    public String getCrtUserName(){
    return this.crtUserName;
    }
    public void setCrtUserName(String crtUserName){
    this.crtUserName = crtUserName;
    }
    
        @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
    @Column(name="ID")
    public String getId(){
    return super.getId();
    }
    
        @Column(name="SCHEMA_NAME")
    public String getSchemaName(){
    return this.schemaName;
    }
    public void setSchemaName(String schemaName){
    this.schemaName = schemaName;
    }
    
        @Column(name="UPD_DATE")
    public Date getUpdDate(){
    return this.updDate;
    }
    public void setUpdDate(Date updDate){
    this.updDate = updDate;
    }
    
        @Column(name="UPD_ORG_CODE")
    public String getUpdOrgCode(){
    return this.updOrgCode;
    }
    public void setUpdOrgCode(String updOrgCode){
    this.updOrgCode = updOrgCode;
    }
    
        @Column(name="UPD_ORG_NAME")
    public String getUpdOrgName(){
    return this.updOrgName;
    }
    public void setUpdOrgName(String updOrgName){
    this.updOrgName = updOrgName;
    }
    
        @Column(name="UPD_USER_CODE")
    public String getUpdUserCode(){
    return this.updUserCode;
    }
    public void setUpdUserCode(String updUserCode){
    this.updUserCode = updUserCode;
    }
    
        @Column(name="UPD_USER_NAME")
    public String getUpdUserName(){
    return this.updUserName;
    }
    public void setUpdUserName(String updUserName){
    this.updUserName = updUserName;
    }
    
        @Column(name="USER_ID")
    public String getUserId(){
    return this.userId;
    }
    public void setUserId(String userId){
    this.userId = userId;
    }
    
        @Column(name="VIEW_PATH")
    public String getViewPath(){
    return this.viewPath;
    }
    public void setViewPath(String viewPath){
    this.viewPath = viewPath;
    }
    
}