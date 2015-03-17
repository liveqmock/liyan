/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.d7.domain;


/**
 * @author matt.yao@bstek.com
 * @since 1.0
 */
public class TableInfo{
	String tableName;
	String tablePrimaryKey;
	String tableColumn;
	String primaryKeyType;
	public String getPrimaryKeyType() {
		return primaryKeyType;
	}
	public void setPrimaryKeyType(String primaryKeyType) {
		this.primaryKeyType = primaryKeyType;
	}
	public String getTableName() {
		return tableName;
	}
	public String getTablePrimaryKey() {
		return tablePrimaryKey;
	}
	public String getTableColumn() {
		return tableColumn;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public void setTablePrimaryKey(String tablePrimaryKey) {
		this.tablePrimaryKey = tablePrimaryKey;
	}
	public void setTableColumn(String tableColumn) {
		this.tableColumn = tableColumn;
	}

}
