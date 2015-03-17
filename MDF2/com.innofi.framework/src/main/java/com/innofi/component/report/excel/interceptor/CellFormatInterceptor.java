/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.interceptor;


/**
 * Excel单元格拦截处理类
 * @author matt.yao@bstek.com
 * @since 1.0
 */
public class CellFormatInterceptor {
	public Object formatGender(Object cellValue) throws Exception {
		Object obj=this.requiredValidator(cellValue);
		if("男".equals(obj)){
			return "Y";
		}else{
			return "N";
		}
	}
	public Object formatBoolean(Object cellValue) throws Exception {
		Object obj=this.requiredValidator(cellValue);
		if(obj instanceof Boolean){
			return (Boolean)obj?"Y":"N";
		}
		return obj;
	}
	public Object requiredValidator(Object cellValue) throws Exception {
		if(cellValue==null){
			throw new InterceptorException("当前单元格内容不能为空！");
		}
		return cellValue;
	}
}
