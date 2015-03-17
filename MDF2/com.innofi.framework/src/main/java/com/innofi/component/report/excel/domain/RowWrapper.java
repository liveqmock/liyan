/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.domain;

import java.util.Collection;

public class RowWrapper implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 当前行在Excel中行号
	 */
	private int row;
	/**
	 * 要插入的目标数据库表名
	 */
	private String tableName;
	/**
	 * 当前行所有列对应的单元格值对象
	 */
	private Collection<CellWrapper> cellWrappers;
	/**
	 * 是否通过验证
	 */
	private boolean valid;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public Collection<CellWrapper> getCellWrappers() {
		return cellWrappers;
	}
	public void setCellWrappers(Collection<CellWrapper> cellWrappers) {
		this.cellWrappers = cellWrappers;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
