package com.innofi.component.message.service;

import com.innofi.component.message.pojo.EmailPojo;

public interface ITemplateEmailService {
	
	/**
	 * 发送邮件接口
	 * 
	 * @param emailPojo
	 * @return
	 */
	public boolean send(EmailPojo emailPojo);
}
