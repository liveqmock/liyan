package com.innofi.component.rbac.post.pojo;
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
@Table(name="SYS_USER_POST")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysUserPostType")
@DiscriminatorValue("sysUserPost")
public class SysUserPost extends BasePojo{
		/**
	* 岗位ID,业务主键
	*/
	private String postId;
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 创建时间
	*/
	private Date crtDate;
		
		/**
	* 修改人
	*/
	private String updUserCode;
		
		/**
	* 修改日期
	*/
	private Date updDate;
		
		
		/**
	* 用户ID
	*/
	private String userId;
		
		/**
	* 岗位类型：1-主岗位 2-兼岗位
	*/
	private String postType;
		
		/**
	* 创建人
	*/
	private String crtUserCode;
		
		/**
	* 修改机构
	*/
	private String updOrgCode;
	private String userName;
	private String postName;
	private String orgName;
	
		@Column(name="POST_ID")
	public String getPostId(){
		return this.postId;
	}
	public void setPostId(String postId){
		this.postId = postId;
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
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="USER_POST_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="USER_ID")
	public String getUserId(){
		return this.userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	} 
		
			@Column(name="POST_TYPE")
	public String getPostType(){
		return this.postType;
	}
	public void setPostType(String postType){
		this.postType = postType;
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
	@Transient
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	@Transient
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	@Transient
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	} 
	}