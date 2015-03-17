package com.innofi.component.rbac.workspace.infobar.pojo;
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
@Table(name="SYS_USER_INF_BAR")

public class SysUserInfBar extends BasePojo{
		/**
	* 用户主题ID
	*/
	private String userThemeId;
		
		
		/**
	* 信息栏ID
	*/
	private String infBarId;
		
		/**
	* 显示顺序（按从左到右，从上到下顺序）
	*/
	private int seq;
	
	@Column(name="USER_THEME_ID")
	public String getUserThemeId(){
		return this.userThemeId;
	}
	public void setUserThemeId(String userThemeId){
		this.userThemeId = userThemeId;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="USER_INF_BAR_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="INF_BAR_ID")
	public String getInfBarId(){
		return this.infBarId;
	}
	public void setInfBarId(String infBarId){
		this.infBarId = infBarId;
	} 
		
			@Column(name="SEQ")
	public int getSeq(){
		return this.seq;
	}
	public void setSeq(int seq){
		this.seq = seq;
	} 


		
	}