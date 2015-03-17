/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.d7.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpringBeanTest {
	
	public Collection<Map<String, Object>> loadDatas(){
		ArrayList<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "于定波");
		map.put("age", 24);
		map.put("desc", "在上海工作");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("name", "于定波");
		map.put("age", 24);
		map.put("desc", "在上海工作");
		list.add(map);
		return list;
	}

}
