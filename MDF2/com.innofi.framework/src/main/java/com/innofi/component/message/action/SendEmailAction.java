package com.innofi.component.message.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.annotation.Expose;
import com.innofi.component.message.pojo.EmailPojo;
import com.innofi.component.message.service.ITemplateEmailService;

public class SendEmailAction {

	private ITemplateEmailService senderEmailService;

	@Expose
	public boolean sendEmail(String type) {
		if (type.equals("1")) {
			return this.sendSimpleEmail();
		} else if (type.equals("2")) {
			return this.sendHtmlEmail();

		} else if (type.equals("3")) {
			return this.sendTemplateEmail();
		}
		return false;
	}

	private boolean sendSimpleEmail() {
		EmailPojo emailPojo = new EmailPojo();
		emailPojo.setEmailContent("邮件内容测试, sfdsfsf,fdsfdsf,fdsfsf sfdsfsfsfsfsfsfsfsdfsfsfsffsfsdfsfsfsdfdfdfdfdfdfdfdffdfd");
		emailPojo.setEmailTitle("我的第一个邮件");
		emailPojo.setEmailType("1");
		emailPojo.setSetTo(new String[]{"huaiyang.liu@innofi.com.cn"});
		//emailPojo.setSetTo(new String[]{"422981784@qq.com","627435924f@qq.com"});
		//emailPojo.setSetToBcc(new String[]{"huaiyang.liu@innofi.com.cn"});
		//emailPojo.setSetToCc(new String[]{"10000liuhuaiyang@163.com"});
		boolean ret = senderEmailService.send(emailPojo);
		return ret;
	}

	private boolean sendHtmlEmail() {
		EmailPojo emailPojo = new EmailPojo();
		emailPojo.setEmailContent("<html><head></head><body><div><font size='10' color='red'>邮件内容测试<br>html格式的邮件，可以自定义格式，传入html格式字符串</font></div></body>");
		emailPojo.setEmailTitle("我的第一个HTML邮件");
		emailPojo.setEmailType("2");
		emailPojo.setSetTo(new String[]{"huaiyang.liu@innofi.com.cn"});
		//emailPojo.setSetToBcc(new String[]{"10000liuhuaiy@163.com"});
		//emailPojo.setSetToCc(new String[]{"huaiyang.liu@innofi.com.cn"});
		List<String> fileIdList = new ArrayList<String>();
		fileIdList.add("D:/freemarker-2.3.3.jar");
		fileIdList.add("D:/jquery.json-2.4.min.js");
		fileIdList.add("D:/新建 Microsoft Office Excel 工作表.xlsx");
		emailPojo.setFileIdList(fileIdList);
		return senderEmailService.send(emailPojo);
	}

	private boolean sendTemplateEmail() {
		EmailPojo emailPojo = new EmailPojo();
		emailPojo.setEmailContent("邮件内容测试");
		emailPojo.setEmailTitle("我的第一个邮件");
		emailPojo.setEmailType("3");
		emailPojo.setTemplateFileName("demo.ftl");
		emailPojo.setSetTo(new String[]{"huaiyang.liu@innofi.com.cn"});
		//emailPojo.setSetToBcc(new String[]{"10000liuhuaiyang@163.com"});
		//emailPojo.setSetToCc(new String[]{"huaiyang.liu@innofi.com.cn"});
		//添加显示信息
		Map<String,String> emailValue =new HashMap<String,String>();
		emailValue.put("userName","<font color='red'>添加用户名,<br>模板邮件也可以传入定义好的html格式串</font>");
		emailPojo.setEmailValue(emailValue);
		
		//添加附件
		List<String> fileIdList = new ArrayList<String>();
		fileIdList.add("D:/freemarker-2.3.3.jar");
		fileIdList.add("D:/jquery.json-2.4.min.js");
		fileIdList.add("D:/新建 Microsoft Office Excel 工作表.xlsx");
		
		
		//添加image
		List<String> imageIdList = new ArrayList<String>();
		imageIdList.add("D:/about.png");
		imageIdList.add("D:/20130608170856.png");
		
		emailPojo.setFileIdList(fileIdList);
		emailPojo.setImageFileIdList(imageIdList);
		
		
		boolean ret = senderEmailService.send(emailPojo);
		return ret;
	}

	public ITemplateEmailService getSenderEmailService() {
		return senderEmailService;
	}

	public void setSenderEmailService(ITemplateEmailService senderEmailService) {
		this.senderEmailService = senderEmailService;
	}
}
