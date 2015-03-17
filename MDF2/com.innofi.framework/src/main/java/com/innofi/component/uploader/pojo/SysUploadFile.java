package com.innofi.component.uploader.pojo;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.innofi.framework.pojo.BasePojo;


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
@Table(name="SYS_UPLOAD_FILE")
//
public class SysUploadFile extends BasePojo{
		/**
	* 创建时间
	*/
	private Date crtDate;
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
   /**
	* 创建人
	*/
	private String crtUserCode;
		
   /**
	* 文件内容
	*/
	private Blob fileContentB;
		
   /**
	* 文件内容
	*/
	private String fileContentC;
		
		
		/**
	* 文件名称
	*/
	private String fileName;
		
		/**
	* 文件路径
	*/
	private String filePath;
		
		/**
	* 文件大小
	*/
	private Long fileSize;
		
		/**
	* 文件类型:文件扩展名
	*/
	private String fileType;
	
	/**
	 * SWF文件转换
	 */
	private Blob fileSwfB;

	private String fileSwfName;
	
	private String fileSwfPath;
		
		/**
	* 修改日期
	*/
	private Date updDate;
		
		/**
	* 修改机构
	*/
	private String updOrgCode;
		
		/**
	* 修改人
	*/
	private String updUserCode;
	
	/**
	 * 绑定标记
	 */
	private String fileBindFlg;
	/**
	 * 
	 */
    private String fileSaveType;
		
	@Column(name="CRT_DATE")
	public Date getCrtDate(){
		return this.crtDate;
	}

	public String getFileBindFlg() {
		return fileBindFlg;
	}
	@Column(name="FILE_BIND_FLG")
	public void setFileBindFlg(String fileBindFlg) {
		this.fileBindFlg = fileBindFlg;
	}
	
	
	@Column(name="FILE_SAVE_TYPE")
	public String getFileSaveType() {
		return fileSaveType;
	}
	public void setFileSaveType(String fileSaveType) {
		this.fileSaveType = fileSaveType;
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
		
	@Column(name="CRT_USER_CODE")
	public String getCrtUserCode(){
		return this.crtUserCode;
	}
	public void setCrtUserCode(String crtUserCode){
		this.crtUserCode = crtUserCode;
	} 
		
	@Column(name="FILE_CONTENT_B")
	public Blob getFileContentB(){
		return this.fileContentB;
	}
	public void setFileContentB(Blob fileContentB){
		this.fileContentB = fileContentB;
	} 
		
	@Column(name="FILE_CONTENT_C")
	public String getFileContentC(){
		return this.fileContentC;
	}
	public void setFileContentC(String fileContentC){
		this.fileContentC = fileContentC;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="FILE_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="FILE_NAME")
	public String getFileName(){
		return this.fileName;
	}
	public void setFileName(String fileName){
		this.fileName = fileName;
	} 
		
			@Column(name="FILE_PATH")
	public String getFilePath(){
		return this.filePath;
	}
	public void setFilePath(String filePath){
		this.filePath = filePath;
	} 
		
			@Column(name="FILE_SIZE")
	public Long getFileSize(){
		return this.fileSize;
	}
	public void setFileSize(Long fileSize){
		this.fileSize = fileSize;
	} 
		
			@Column(name="FILE_TYPE")
	public String getFileType(){
		return this.fileType;
	}
	public void setFileType(String fileType){
		this.fileType = fileType;
	} 
	
	@Column(name="FILE_SWF_B")
	public Blob getFileSwfB() {
		return fileSwfB;
	}
	
	public void setFileSwfB(Blob fileSwfB) {
		this.fileSwfB = fileSwfB;
	}
	
	@Column(name="FILE_SWF_NAME")
	public String getFileSwfName() {
		return fileSwfName;
	}


	public void setFileSwfName(String fileSwfName) {
		this.fileSwfName = fileSwfName;
	}
	@Column(name="FILE_SWF_PATH")
	public String getFileSwfPath() {
		return fileSwfPath;
	}

	public void setFileSwfPath(String fileSwfPath) {
		this.fileSwfPath = fileSwfPath;
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
		
			@Column(name="UPD_USER_CODE")
	public String getUpdUserCode(){
		return this.updUserCode;
	}
	public void setUpdUserCode(String updUserCode){
		this.updUserCode = updUserCode;
	} 
		
	}