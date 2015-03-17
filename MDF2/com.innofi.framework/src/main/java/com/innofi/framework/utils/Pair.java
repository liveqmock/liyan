/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.framework.utils;

import java.io.Serializable;

/**
 * 包含且仅包含两个对象的容器.
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public class Pair<L, R> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7838072938875014319L;

	public Pair(L leftObject, R rightObject) {
		this.setLeftObject(leftObject);
		this.setRightObject(rightObject);
	}

	private L leftObject = null;
	private R rightObject = null;

	/**
	 * 取得左侧的对象.
	 * 
	 * @return the leftObject
	 */
	public L getLeftObject() {
		return leftObject;
	}

	/**
	 * 设置左侧的对象.
	 * 
	 * @param leftObject
	 *            the leftObject to set
	 */
	public void setLeftObject(L leftObject) {
		this.leftObject = leftObject;
	}

	/**
	 * 取得左侧的对象.
	 * 
	 * @return the rightObject
	 */
	public R getRightObject() {
		return rightObject;
	}

	/**
	 * 设置右侧的对象.
	 * 
	 * @param rightObject
	 *            the rightObject to set
	 */
	public void setRightObject(R rightObject) {
		this.rightObject = rightObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object anotherObject) {
		if (this == anotherObject) {
			return true;
		}
		if (!this.getClass().isAssignableFrom(anotherObject.getClass())) {
			return false;
		}
		Pair<L, R> anotherPair = (Pair<L, R>) anotherObject;
		if (null == this.leftObject && null == this.rightObject) {
			return null == anotherPair.leftObject
					&& null == anotherPair.rightObject;
		}
		if (null == this.leftObject) {
			return this.rightObject.equals(anotherPair.leftObject);
		}
		if (null == this.rightObject) {
			return this.rightObject.equals(anotherPair.rightObject);
		}
		return this.leftObject.equals(anotherPair.leftObject)
				&& this.rightObject.equals(anotherPair.rightObject);
	}

	@Override
	public int hashCode() {
		if (null != this.leftObject && null != this.rightObject) {
			return this.leftObject.hashCode() ^ this.rightObject.hashCode();
		}
		if (null != this.leftObject) {
			return 17 ^ this.leftObject.hashCode();
		}
		if (null != this.rightObject) {
			return 17 ^ this.rightObject.hashCode();
		}
		return super.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Pair:"
				+ this.getClass()
				+ "<<"
				+ (null == this.leftObject ? "EMPTY" : this.leftObject
						.toString())
				+ ">,<"
				+ (null == this.rightObject ? "EMPTY" : this.rightObject
						.toString()) + ">>";
	}
}
