/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.upload;

import java.io.InputStream;

import com.innofi.component.report.domain.UploadInfo;


/**
 * 系统临时文件存储接口，用于定义临时文件的存储、删除、获取等.<br>
 * 这里指的临时文件诸如生成临时供下载的报表文件（表现层导出的excel、pdf，或报表模版产生的excel,pdf或在线预览产生的swf等）
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public interface TempFilePersister {
	/** 
	 * 根据给定的id，得到指定文件的流信息
	 * @param id 文件的id，可能是文件名等
	 * @return 文件流信息
	 * @throws Exception
	 */
	InputStream get(String id) throws Exception;
	/**
	 * 持久化指定的文件信息，持久化成功之后要返回可以唯一定位该文件的id字符串
	 * @param in 要持久化的文件流
	 * @param uploadInfo 文件的其它信息
	 * @return 返回可以唯一定位该文件的id字符串
	 * @throws Exception
	 */
	String persist(InputStream in,UploadInfo uploadInfo)throws Exception;
	/**
	 * 删除指定ID的文件信息
	 * @param id
	 * @throws Exception
	 */
	void delete(String id)throws Exception;
}
