/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.pdf.model;

import java.util.Collection;

import com.innofi.component.report.pdf.ColumnHeader;
import com.innofi.component.report.pdf.ReportData;

/**
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public class ReportDataModel implements java.io.Serializable{
	private static final long serialVersionUID = -4269131426608999898L;
	private Collection<ColumnHeader> topColumnHeaders;
	private Collection<ReportData> reportData;
	private String fileName;
	private int columnCount;
	public ReportDataModel(Collection<ColumnHeader> topColumnHeaders,Collection<ReportData> reportData) {
		this.topColumnHeaders = topColumnHeaders;
		this.reportData=reportData;
	}
	public Collection<ColumnHeader> getTopColumnHeaders() {
		return topColumnHeaders;
	}
	public void setTopColumnHeaders(Collection<ColumnHeader> topColumnHeaders) {
		this.topColumnHeaders = topColumnHeaders;
	}

	public Collection<ReportData> getReportData() {
		return reportData;
	}
	public void setReportData(Collection<ReportData> reportData) {
		this.reportData = reportData;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getColumnCount() {
		return columnCount;
	}
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
}
