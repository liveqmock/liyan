/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.upload.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.innofi.component.report.domain.UploadInfo;
import com.innofi.component.report.upload.TempFilePersister;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 默认的临时文件存储实现类，默认实现类是将文件存储到当前系统的临时目录/bdftemp目录下
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public class DefaultTempFilePersister implements TempFilePersister{
	public InputStream get(String id) throws Exception {
		String path=ContextHolder.getIdfTempFileStorePath()+id;
		return new FileInputStream(path);
	}

	public String persist(InputStream in, UploadInfo uploadInfo)
			throws Exception {
		return uploadInfo.getFileName();
	}

	public void delete(String id) throws Exception {
		String path=ContextHolder.getIdfTempFileStorePath()+id;
		File f=new File(path);
		if(f.exists()){
			f.delete();
		}
	}
}
