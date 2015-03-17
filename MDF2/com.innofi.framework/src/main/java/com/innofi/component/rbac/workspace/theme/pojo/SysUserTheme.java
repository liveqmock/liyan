package com.innofi.component.rbac.workspace.theme.pojo;
import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.GenericGenerator;


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
@Table(name="SYS_USER_THEME")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysUserThemeType")
@DiscriminatorValue("sysUserTheme")
public class SysUserTheme extends BasePojo{
		/**
	* 用户编码
	*/
	private String userCode;
		
		/**
	* 信息栏布局版式
	*/
	private String infBarFrame;
		
		/**
	* 样式表（皮肤）
	*/
	private String className;
		
		/**
	* 是否系统默认:默认用户主题
	*/
	private String isSysDefault;
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 创建时间
	*/
	private Date crtDate;
		
		/**
	* 修改机构
	*/
	private String updOrgCode;
		
		
		/**
	* 用户主题名称
	*/
	private String userThemeName;
		
		/**
	* 是否全屏
	*/
	private String isFullScreen;
		
		/**
	* 显示系统，多选，用逗号分号
	*/
	private String systemCode;
		
		/**
	* 状态
	*/
	private String status;
		
		/**
	* 创建人
	*/
	private String crtUserCode;
		
		/**
	* 修改人
	*/
	private String updUserCode;
		
		/**
	* 修改日期
	*/
	private Date updDate;
	
	/**
	* 主界面版式
	*/
	private String mainFrame;
		
		@Column(name="USER_CODE")
	public String getUserCode(){
		return this.userCode;
	}
	public void setUserCode(String userCode){
		this.userCode = userCode;
	} 
		
			@Column(name="INF_BAR_FRAME")
	public String getInfBarFrame(){
		return this.infBarFrame;
	}
	public void setInfBarFrame(String infBarFrame){
		this.infBarFrame = infBarFrame;
	} 
		
			@Column(name="CLASS_NAME")
	public String getClassName(){
		return this.className;
	}
	public void setClassName(String className){
		this.className = className;
	} 
		
			@Column(name="IS_SYS_DEFAULT")
	public String getIsSysDefault(){
		return this.isSysDefault;
	}
	public void setIsSysDefault(String isSysDefault){
		this.isSysDefault = isSysDefault;
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
		
			@Column(name="UPD_ORG_CODE")
	public String getUpdOrgCode(){
		return this.updOrgCode;
	}
	public void setUpdOrgCode(String updOrgCode){
		this.updOrgCode = updOrgCode;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="USER_THEME_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="USER_THEME_NAME")
	public String getUserThemeName(){
		return this.userThemeName;
	}
	public void setUserThemeName(String userThemeName){
		this.userThemeName = userThemeName;
	} 
		
			@Column(name="IS_FULL_SCREEN")
	public String getIsFullScreen(){
		return this.isFullScreen;
	}
	public void setIsFullScreen(String isFullScreen){
		this.isFullScreen = isFullScreen;
	} 
		
			@Column(name="SYSTEM_CODE")
	public String getSystemCode(){
		return this.systemCode;
	}
	public void setSystemCode(String systemCode){
		this.systemCode = systemCode;
	} 
		
			@Column(name="STATUS")
	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	} 
		
			@Column(name="CRT_USER_CODE")
	public String getCrtUserCode(){
		return this.crtUserCode;
	}
	public void setCrtUserCode(String crtUserCode){
		this.crtUserCode = crtUserCode;
	} 
		
			@Column(name="UPD_USER_CODE")
	public String getUpdUserCode(){
		return this.updUserCode;
	}
	public void setUpdUserCode(String updUserCode){
		this.updUserCode = updUserCode;
	} 
		
			@Column(name="UPD_DATE")
	public Date getUpdDate(){
		return this.updDate;
	}
	public void setUpdDate(Date updDate){
		this.updDate = updDate;
	}
	@Column(name="MAIN_FRAME")
	public String getMainFrame() {
		return mainFrame;
	}
	public void setMainFrame(String mainFrame) {
		this.mainFrame = mainFrame;
	} 
		
	}