/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.model;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义导出报表时的Form表单模型
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportFormModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int columnCount;
	private boolean showBorder;
	private List<ReportFormDataModel> listReportFormData;
	public int getColumnCount() {
		return columnCount;
	}
	public boolean isShowBorder() {
		return showBorder;
	}
	public List<ReportFormDataModel> getListReportFormDataModel() {
		return listReportFormData;
	}
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
	public void setShowBorder(boolean showBorder) {
		this.showBorder = showBorder;
	}
	public void setListReportFormDataModel(List<ReportFormDataModel> listReportFormData) {
		this.listReportFormData = listReportFormData;
	}
	
}
