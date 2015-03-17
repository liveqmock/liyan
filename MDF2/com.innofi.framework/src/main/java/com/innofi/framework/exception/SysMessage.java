package com.innofi.framework.exception;

import java.io.Serializable;

/**
 * 系统信息类
 *
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司 <p/>
 * all rights reserved.
 * @author 鄢利  robert.yan@innofi.com.cn
 * @version 2.0 2013-5-08  上午11:37:06
 * @since JDK1.6
 */
public class SysMessage implements Serializable {
	protected String key;
	protected Object[] params;

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object[] getParams() {
		return this.params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public SysMessage(String key, Object[] params) {
		this.key = key;
		this.params = params;
	}

	public SysMessage(String key) {
		this.key = key;
	}

	public String getErrorType() {
		return this.key.substring(0, 1);
	}

	public String getMsgInfo() {
		if (getParams() != null) {
			return MessageUtil.getMessage(getKey(), getParams());
		}
		return MessageUtil.getMessage(getKey());
	}
}