/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.d7;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.innofi.component.report.upload.TempFilePersister;

public class ReportController {
	private TempFilePersister tempFilePersister;
	/**
	 * 从临时缓存中读取用于生成PDF/Excel报表信息的数据，生成报表并将报表写到response当中供用户下载
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void doDownloadReport(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String fileName=request.getParameter("fileName");
		String type=request.getParameter("type");
		response.setHeader("Server", "http://www.bstek.com");
		response.setContentType("application/octet-stream");
		response.setHeader("Connection", "close");// 表示不能用浏览器直接打开
		response.setHeader("Accept-Ranges", "bytes");// 告诉客户端允许断点续传多线程连接下载
		response.setHeader("Content-Disposition", "attachment;filename=\"report."+type+"\"");
		InputStream input=tempFilePersister.get(fileName);
		OutputStream out=response.getOutputStream();
		IOUtils.copy(input,out);
		input.close();
		out.flush();
		out.close();
	}
	public TempFilePersister getTempFilePersister() {
		return tempFilePersister;
	}
	public void setTempFilePersister(TempFilePersister tempFilePersister) {
		this.tempFilePersister = tempFilePersister;
	}
}
