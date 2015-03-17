/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.domain;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * 文件上传后的信息.
 * <p>
 * 此类代表一个上传完毕后的文件,其包含如原如文件名,文件大小,类型,保存的位置等信息.
 * </p>
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public class UploadInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	public UploadInfo(){}
	private String id = null;
	private String fileName = null;
	private String location = null;
	private int size = 0;
	private String contentType = null;
	private Date uploadTime = null;
	private String uploadUser = null;
	private InputStream content = null;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getUploadUser() {
		return uploadUser;
	}
	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}
	public InputStream getContent() {
		return content;
	}
	public void setContent(InputStream content) {
		this.content = content;
	}
}
