/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.domain;

import java.util.List;

import com.innofi.framework.pojo.BasePojo;

public class ExcelModel extends BasePojo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String sheet;
	private String tableName;
	private String tablePrimaryKey;
	private String primaryKeyType;
	private String sequenceName;
	private String dbType;
	private int startRow;
	private int endRow;
	private int startColumn;
	private int endColumn;
	private String processor;
	private String helpDoc;
	private String cmnt;
	private List<ExcelModelDetail> listExcelModelDetail;
	private String companyId;
	private String datasourceName;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSheet() {
		return sheet;
	}
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	public int getStartColumn() {
		return startColumn;
	}
	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}
	public int getEndColumn() {
		return endColumn;
	}
	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}
	public String getProcessor() {
		return processor;
	}
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	public String getHelpDoc() {
		return helpDoc;
	}
	public void setHelpDoc(String helpDoc) {
		this.helpDoc = helpDoc;
	}
	public String getCmnt() {
		return cmnt;
	}
	public String getPrimaryKeyType() {
		return primaryKeyType;
	}
	public String getSequenceName() {
		return sequenceName;
	}
	public String getDbType() {
		return dbType;
	}
	public void setPrimaryKeyType(String primaryKeyType) {
		this.primaryKeyType = primaryKeyType;
	}
	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public void setCmnt(String cmnt) {
		this.cmnt = cmnt;
	}
	public String getTablePrimaryKey() {
		return tablePrimaryKey;
	}
	public void setTablePrimaryKey(String tablePrimaryKey) {
		this.tablePrimaryKey = tablePrimaryKey;
	}
	public String getDatasourceName() {
		return datasourceName;
	}
	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}
	public void setListExcelModelDetail(List<ExcelModelDetail> listExcelModelDetail) {
		this.listExcelModelDetail = listExcelModelDetail;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public List<ExcelModelDetail> getListExcelModelDetail() {
		return listExcelModelDetail;
	}
}
