package com.innofi.component.message;

public class MessageConstants {

	/**
	 * 发送简单文本邮件
	 */
	public static final String EMAIL_TYPE_SIMPLE = "1";
	/**
	 * 发送html邮件
	 */
	public static final String EMAIL_TYPE_HTML = "2";
	/**
	 * 发送模板邮件
	 */
	public static final String EMAIL_TYPE_TEMPLATE = "3";

	/**
	 * 邮件编码
	 */
	public static final String EMAIL_ECODING = "GBK";

	/**
	 * 邮件服务器地址
	 */
	public static final String EMAIL_SMTP_HOST = "mail.smtp.host";
	/**
	 * 邮件服务器端口
	 */
	public static final String EMAIL_SMTP_PORT = "mail.smtp.port";
	/**
	 * 服务鉴权
	 */
	public static final String EMAIL_SMTP_AUTH = "mail.smtp.auth";
	/**
	 * 邮件服务器用户名
	 */
	public static final String EMAIL_SMTP_USER = "mail.smtp.user";
	/**
	 * 邮件服务器密码
	 */
	public static final String EMAIL_SMTP_PASSWORD = "mail.smtp.password";
	/**
	 * 邮件接收者
	 */
	public static final String EMAIL_SET_TO = "setTo";
	/**
	 * 邮件密件者
	 */
	public static final String EMAIL_SET_BC = "setToBc";
	/**
	 * 邮件抄送者
	 */
	public static final String EMAIL_SET_CC = "setToCc";

	/**
	 * email正则表达式
	 */
	public static final String EMAIL_ZH="^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
}
