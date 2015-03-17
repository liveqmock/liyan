/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.d7.pdf;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

/**
 * 由客户实现，返回需要在线浏览显示的SWF文件信息
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public interface SwfFileHandler {
	/**
	 * 根据给出的参数，返回一个可以在线浏览的SWF文件对象，<br>
	 * 在使用时，如果需要将一个Pdf文件转换成一个SWF文件，可以利用BDF中提供的PdfToSwfConverter类实现,<br>
	 * PdfToSwfConverter类实例注册在Spring当中，其bean的id为<strong>mdf.pdfToSwfConverter</strong>
	 * @param request 当前线程的HttpServletRequest对象
	 * @return 返回一个包含要显示的SWF文件引用 的File对象
	 * @throws Exception
	 */
	public File retriveSwf(HttpServletRequest request) throws Exception;
}
