/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.exception;

/**
 * 报表模块异常定义，报表模块所有业务异常通过该异常抛出，便于框架对异常统一处理
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2896520314081050945L;

	public ReportException(String message){
		super(message);
	}
}
