package com.innofi.component.rbac.notice.pojo;
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
@Table(name="SYS_NOTICE_CONTENT")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysNoticeContentType")
@DiscriminatorValue("sysNoticeContent")
public class SysNoticeContent extends BasePojo{
		/**
	* 通知通告编号
	*/
	private String noticeNo;
		
		/**
	* 通知通告标题
	*/
	private String title;
		
		/**
	* 通知通告指令
	*/
	private String noticeType;
		
		/**
	* 通知通告内容
	*/
	private String content;
		
		
		/**
	* 结束有效日期后自动收回，处于不公开状态
	*/
	private Date completeDate;
		
		/**
	* 状态：1 提交 2 待审批3 已发布 4 审批退回
	*/
	private String busiStatus;
		
		/**
	* 生效状态:1-启用2-停用
	*/
	private String status;
		
		/**
	* 开始有效日期
	*/
	private Date releaseDate;
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 创建日期
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
		
		@Column(name="NOTICE_NO")
	public String getNoticeNo(){
		return this.noticeNo;
	}
	public void setNoticeNo(String noticeNo){
		this.noticeNo = noticeNo;
	} 
		
			@Column(name="TITLE")
	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	} 
		
			@Column(name="NOTICE_TYPE")
	public String getNoticeType(){
		return this.noticeType;
	}
	public void setNoticeType(String noticeType){
		this.noticeType = noticeType;
	} 
		
			@Column(name="CONTENT")
	public String getContent(){
		return this.content;
	}
	public void setContent(String content){
		this.content = content;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="NOTICE_CONTENT_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="COMPLETE_DATE")
	public Date getCompleteDate(){
		return this.completeDate;
	}
	public void setCompleteDate(Date completeDate){
		this.completeDate = completeDate;
	} 
		
			@Column(name="BUSI_STATUS")
	public String getBusiStatus() {
		return busiStatus;
	}
	public void setBusiStatus(String busiStatus) {
		this.busiStatus = busiStatus;
	}

			@Column(name="STATUS")
	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	} 
		
			@Column(name="RELEASE_DATE")
	public Date getReleaseDate(){
		return this.releaseDate;
	}
	public void setReleaseDate(Date releaseDate){
		this.releaseDate = releaseDate;
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
		
	}