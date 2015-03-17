/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.domain;

public class ExcelModelDetail implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String modelId;
	private ExcelModel excelModel;
	private int excelColumn;
	private String tableColumn;
	private String interceptor;
	private String interceptorType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public int getExcelColumn() {
		return excelColumn;
	}
	public void setExcelColumn(int excelColumn) {
		this.excelColumn = excelColumn;
	}
	public String getTableColumn() {
		return tableColumn;
	}
	public void setTableColumn(String tableColumn) {
		this.tableColumn = tableColumn;
	}
	public String getInterceptor() {
		return interceptor;
	}
	public void setInterceptor(String interceptor) {
		this.interceptor = interceptor;
	}
	public void setExcelModel(ExcelModel excelModel) {
		this.excelModel = excelModel;
	}
	public ExcelModel getExcelModel() {
		return excelModel;
	}
	public String getInterceptorType() {
		return interceptorType;
	}
	public void setInterceptorType(String interceptorType) {
		this.interceptorType = interceptorType;
	}
}
