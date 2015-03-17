/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.domain;

public class CellWrapper implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 当前单元格在Excel中的列号
	 */
	private int column;
	
	/**
	 * 当前单元格具体值
	 */
	private Object value;
	/**
	 * 当前单元格对应的数据库表列表<br>
	 * 该值可选
	 */
	private String columnName;
	
	/**
	 * 是否通过验证
	 */
	private boolean valid;
	
	/**
	 * 是否是数据主键
	 */
	private boolean isPrimaryKey;
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public boolean getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
