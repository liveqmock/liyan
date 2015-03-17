/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.model;

/**
 * 自定义导出报表时的报表标题的样式
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class TitleStyle {

	private int[] bgColor;
	private int[] fontColor;
	private int fontSize;
	public int[] getBgColor() {
		return bgColor;
	}
	public int[] getFontColor() {
		return fontColor;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setBgColor(int[] bgColor) {
		this.bgColor = bgColor;
	}
	public void setFontColor(int[] fontColor) {
		this.fontColor = fontColor;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
}
