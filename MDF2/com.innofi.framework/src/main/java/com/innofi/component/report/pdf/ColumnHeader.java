/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.pdf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public class ColumnHeader extends TextChunk implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4545085711049920810L;
	private String name;
	private int width;
	private int[] bgColor;
	private int level;
	private List<ColumnHeader> columnHeaders=new ArrayList<ColumnHeader>();
	public ColumnHeader(int level){
		this.level=level;
	}
	public String getName() {
		return name;
	}
	
	public int getColspan(){
		if(columnHeaders.size()==0){
			return 1;
		}
		return calculateColspan(0,this.columnHeaders);
	}
	private int calculateColspan(int start,List<ColumnHeader> columnHeaders){
		if(start==0)start+=columnHeaders.size();
		for(ColumnHeader header:columnHeaders){
			if(header.getColumnHeaders().size()>0){
				start+=(header.getColumnHeaders().size()-1);
				start=calculateColspan(start,header.getColumnHeaders());
			}
		}
		return start;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}

	public List<ColumnHeader> getColumnHeaders() {
		return columnHeaders;
	}
	
	public void addColumnHeader(ColumnHeader columnHeader){
		columnHeaders.add(columnHeader);
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int[] getBgColor() {
		return bgColor;
	}
	public void setBgColor(int[] bgColor) {
		if(bgColor.length!=3){
			throw new IllegalArgumentException("设置颜色的RBG值不合法!");
		}
		this.bgColor = bgColor;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
