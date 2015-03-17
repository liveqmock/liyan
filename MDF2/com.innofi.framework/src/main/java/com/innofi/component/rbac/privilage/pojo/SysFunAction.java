package com.innofi.component.rbac.privilage.pojo;
import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.GenericGenerator;

/**
* ${model.modelCnName}
*/

@Entity
@Table(name="SYS_FUN_ACTION")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysFunActionType")
@DiscriminatorValue("sysFunAction")
public class SysFunAction extends BasePojo{
		/**
	* 菜单ID
	*/
	private String menuId;
		
		/**
	* 按钮描述
	*/
	private String actionDesc;
		
		/**
	* 按钮图片路径
	*/
	private String actPicPath;
		
		/**
	* 按钮名称
	*/
	private String actionName;
		
		
		/**
	* 功能编号
	*/
	private String actionCode;
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 创建时间
	*/
	private Date crtDate;
		
		/**
	* 创建人
	*/
	private String crtUserCode;
		
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
		
	private boolean selectFlag;
	
		@Column(name="MENU_ID")
	public String getMenuId(){
		return this.menuId;
	}
	public void setMenuId(String menuId){
		this.menuId = menuId;
	} 
		
			@Column(name="ACTION_DESC")
	public String getActionDesc(){
		return this.actionDesc;
	}
	public void setActionDesc(String actionDesc){
		this.actionDesc = actionDesc;
	} 
		
			@Column(name="ACT_PIC_PATH")
	public String getActPicPath(){
		return this.actPicPath;
	}
	public void setActPicPath(String actPicPath){
		this.actPicPath = actPicPath;
	} 
		
			@Column(name="ACTION_NAME")
	public String getActionName(){
		return this.actionName;
	}
	public void setActionName(String actionName){
		this.actionName = actionName;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="ACTION_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="ACTION_CODE")
	public String getActionCode(){
		return this.actionCode;
	}
	public void setActionCode(String actionCode){
		this.actionCode = actionCode;
	} 
		
			@Column(name="CRT_ORG_CODE")
	public String getCrtOrgCode(){
		return this.crtOrgCode;
	}
	public void setCrtOrgCode(String crtOrgCode){
		this.crtOrgCode = crtOrgCode;
	} 
		
			@Column(name="CRT_DATE")
	public Date getCrtDate(){
		return this.crtDate;
	}
	public void setCrtDate(Date crtDate){
		this.crtDate = crtDate;
	} 
		
			@Column(name="CRT_USER_CODE")
	public String getCrtUserCode(){
		return this.crtUserCode;
	}
	public void setCrtUserCode(String crtUserCode){
		this.crtUserCode = crtUserCode;
	} 
		
			@Column(name="UPD_ORG_CODE")
	public String getUpdOrgCode(){
		return this.updOrgCode;
	}
	public void setUpdOrgCode(String updOrgCode){
		this.updOrgCode = updOrgCode;
	} 
		
			@Column(name="UPD_DATE")
	public Date getUpdDate(){
		return this.updDate;
	}
	public void setUpdDate(Date updDate){
		this.updDate = updDate;
	} 
		
			@Column(name="UPD_USER_CODE")
	public String getUpdUserCode(){
		return this.updUserCode;
	}
	public void setUpdUserCode(String updUserCode){
		this.updUserCode = updUserCode;
	}
	@Transient
	public boolean getSelectFlag() {
		return selectFlag;
	}
	public void setSelectFlag(boolean selectFlag) {
		this.selectFlag = selectFlag;
	} 
	}