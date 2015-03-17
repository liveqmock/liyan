package com.innofi.component.message.pojo;

import java.util.List;
import java.util.Map;

public class EmailPojo {

	//邮件的标题,不能为空
	private String emailTitle;

	//邮件的内容，如果是html邮件，可以传入html脚本
	private String emailContent;

	//接收邮件的地址，不能为空
	private String[] setTo;

	//发送邮件的类型，不能为空 1：普通文本邮件 2：html邮件 3：模板邮件  如果是模板邮件，需要传入emailValue
	private String emailType;
	
	//附件列表，需要传入上传附件文件的路径
	private List<String> fileIdList;
	
	//邮件嵌入图片列表，提供图片的路径
	private List<String> ImageFileIdList;

	//如果是模板邮件，此map传入模板里面需要显示的信息
	private Map<String, String> emailValue;
	
	//如果是模板邮件，需要传入模板文件名，模板统一放在ipdp\src\main\resources\META-INF\template
	private String templateFileName;
	
	//密送人邮件地址
	private String[] setToBcc;
	
	//抄送人邮件地址
	private String[] setToCc;
	
	public String getEmailTitle() {
		return emailTitle;
	}

	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public String[] getSetTo() {
		return setTo;
	}

	public void setSetTo(String[] setTo) {
		this.setTo = setTo;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public List<String> getFileIdList() {
		return fileIdList;
	}

	public void setFileIdList(List<String> fileIdList) {
		this.fileIdList = fileIdList;
	}

	public List<String> getImageFileIdList() {
		return ImageFileIdList;
	}

	public void setImageFileIdList(List<String> imageFileIdList) {
		ImageFileIdList = imageFileIdList;
	}

	public Map<String, String> getEmailValue() {
		return emailValue;
	}

	public void setEmailValue(Map<String, String> emailValue) {
		this.emailValue = emailValue;
	}

	public String getTemplateFileName() {
		return templateFileName;
	}

	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}

	public String[] getSetToBcc() {
		return setToBcc;
	}

	public void setSetToBcc(String[] setToBcc) {
		this.setToBcc = setToBcc;
	}

	public String[] getSetToCc() {
		return setToCc;
	}

	public void setSetToCc(String[] setToCc) {
		this.setToCc = setToCc;
	}
}
