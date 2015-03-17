/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.pdf;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;

/**
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public class TextChunk implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -958134552108683L;
	private String text;
	private int[] fontColor={0,0,0};
	private int fontSize=9;
	private int fontStyle=Font.NORMAL;
	private int align=Element.ALIGN_CENTER;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int[] getFontColor() {
		return fontColor;
	}
	public void setFontColor(int[] fontColor) {
		if(fontColor.length!=3){
			throw new IllegalArgumentException("设置颜色的RBG值不合法!");
		}
		this.fontColor = fontColor;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public int getFontStyle() {
		return fontStyle;
	}
	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}
	public int getAlign() {
		return align;
	}
	public void setAlign(int align) {
		this.align = align;
	}
}
