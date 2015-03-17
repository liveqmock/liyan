/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.processor;

import com.innofi.component.report.excel.domain.ExcelDataWrapper;
/**
 * 对要导入的Excel内容的处理类
 * @author matt.yao@bstek.com
 * @since 1.0
 */
public interface Processor {
	/**
	 * 初始化方法，其中的init在每次处理Excel内容过程中只会执行一次，<br>
	 * 可以将一些需要初始化的变量放在这个方法中
	 * @throws Exception
	 */
	void init()throws Exception;
	
	/**
	 * 执行处理Excel内容的方法
	 * @param excelDataWrapper 一个包装了Excel信息的集合
	 * @throws Exception
	 */
	void execute(ExcelDataWrapper excelDataWrapper)throws Exception;
}
