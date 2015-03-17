/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.domain;

import java.util.Collection;

public class ExcelDataWrapper implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * excel对应的模型信息
	 */
	public ExcelModel excelModel;
	public String excelModelId;
    /**
     * 解析的excel数据集合
     */
    public Collection<RowWrapper> rowWrappers;
    /**
     * 数据是否通过验证
     */
    public boolean validate;
    /**
     * 采用的数据处理器
     */
    public String processor;
	public ExcelModel getExcelModel() {
		return excelModel;
	}
	public Collection<RowWrapper> getRowWrappers() {
		return rowWrappers;
	}
	
	public void setExcelModel(ExcelModel excelModel) {
		this.excelModel = excelModel;
	}
	public void setRowWrappers(Collection<RowWrapper> rowWrappers) {
		this.rowWrappers = rowWrappers;
	}
	public boolean isValidate() {
		return validate;
	}
	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	public String getProcessor() {
		return processor;
	}
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	public String getExcelModelId() {
		return excelModelId;
	}
	public void setExcelModelId(String excelModelId) {
		this.excelModelId = excelModelId;
	}
}
