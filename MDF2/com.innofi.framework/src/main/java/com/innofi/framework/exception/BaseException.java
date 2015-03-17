package com.innofi.framework.exception;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

public class BaseException extends RuntimeException implements Serializable {

	protected Throwable cause;
	public String stackTraceString;
	protected SysMessage msgObj;

	public BaseException() {
		super();
	}

	public BaseException(SysMessage msgObj) {
		setMsgObj(msgObj);
	}

	public BaseException(String msg) {
		super(msg);
	}

	public BaseException(String msg, SysMessage msgObj) {
		super(msg);
		setMsgObj(msgObj);
	}

	public BaseException(Throwable ex) {
		if ((ex instanceof BaseException)
				&& (((BaseException) ex).stackTraceString != null)) {
			this.cause = ex;
			this.stackTraceString = ((BaseException) this.cause).stackTraceString;
			setMsgObj(((BaseException) ex).getMsgObj());
		} else {
			this.cause = ex;
			this.stackTraceString = extractStackTraceString(this.cause);
			setMsgObj(new SysMessage(""));
		}
	}

	public BaseException(SysMessage msgObj, Throwable ex) {
		if ((ex instanceof BaseException)
				&& (((BaseException) ex).stackTraceString != null)) {
			this.cause = ex;
			this.stackTraceString = ((BaseException) this.cause).stackTraceString;
			setMsgObj(msgObj);
		} else {
			this.cause = ex;
			this.stackTraceString = extractStackTraceString(this.cause);
			setMsgObj(msgObj);
		}
	}

	public BaseException(String msg, Throwable ex) {
		super(msg);
		this.cause = ex;
		this.stackTraceString = extractStackTraceString(this.cause);
	}

	public BaseException(String msg, SysMessage msgObj, Throwable ex) {
		super(msg);
		this.cause = ex;
		this.stackTraceString = extractStackTraceString(this.cause);
		setMsgObj(msgObj);
	}

	public SysMessage getMsgObj() {
		return this.msgObj;
	}

	public void setMsgObj(SysMessage msgObj) {
		this.msgObj = msgObj;
	}

	public String getErrorType() {
		return this.msgObj.getErrorType();
	}

	public Throwable getCause() {
		return cause;
	}

	public String extractStackTraceString(Throwable cause) {
		StringWriter s = new StringWriter();
		cause.printStackTrace(new PrintWriter(s));
		return s.toString();
	}

	public String getStackTraceString() {
		if (this.cause == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(this.stackTraceString);
		return sb.toString();
	}

	public String getMessage() {
		String superMsg = super.getMessage();
		String objMsg = "";
		String nestedMsg = "";

		if (getMsgObj() != null) {
			objMsg = new StringBuffer(getMsgObj().getMsgInfo()).toString();
		}
		if (getCause() != null) {
			nestedMsg = getCause().getMessage();
		}
		StringBuffer sb = new StringBuffer();
		if (superMsg != null) {
			sb.append(superMsg).append("\t ").append(objMsg).append("\t ")
					.append(nestedMsg);
		} else {
			sb.append(objMsg).append("\t ").append(nestedMsg);
		}
		return sb.toString();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		if (getCause() != null) {
			sb.append("\n\t ").append(getCause());
		}
		return sb.toString();
	}
}
