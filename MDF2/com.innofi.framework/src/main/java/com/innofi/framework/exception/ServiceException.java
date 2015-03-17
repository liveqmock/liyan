package com.innofi.framework.exception;

public class ServiceException extends BaseException {
	public ServiceException(SysMessage msg) {
		super(msg);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(SysMessage msg, Throwable cause) {
		super(msg, cause);
	}

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(String msg, Throwable ex) {
		super(msg,ex);
	}
	
	public ServiceException(String msg, SysMessage sysMsg, Throwable cause) {
		super(msg, sysMsg, cause);
	}
}
