/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.domain;

import java.util.List;
import java.util.Map;

public class ExportDataWrapper {
	private List<String> excelTitle;
	private List<Map<String,Object>> excelData;
	public List<String> getExcelTitle() {
		return excelTitle;
	}
	public void setExcelTitle(List<String> excelTitle) {
		this.excelTitle = excelTitle;
	}
	public List<Map<String, Object>> getExcelData() {
		return excelData;
	}
	public void setExcelData(List<Map<String, Object>> excelData) {
		this.excelData = excelData;
	}

}
