/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.d7.listener;

import java.util.Map;

import com.bstek.dorado.data.entity.EntityList;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.widget.SubViewHolder;
import com.bstek.dorado.view.widget.base.CardBook;
import com.bstek.dorado.view.widget.base.Panel;
import com.bstek.dorado.view.widget.data.DataSet;

/**
 * 定时报表处理配置监听器，<br>
 * 用于加载自定配置的配置方案.
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportJobConfigListener {
	
	public void onInit(View view)throws Exception{
		DataSet dataSet = (DataSet)view.getComponent("dataSetConfig");		
		CardBook cardBook = (CardBook)view.getComponent("cardBookConfig");
		this.initCarBook(cardBook, dataSet);
	}
	
	/**
	 * 根据用户的配置动态的构建可选的配置处理方案项
	 * 
	 * @param cardBook
	 * @param dataSet
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void initCarBook(CardBook cardBook,DataSet dataSet)throws Exception{
		EntityList<Map<String,String>> datas = (EntityList<Map<String,String>>)dataSet.getData();
		SubViewHolder subViewHolder = null;
		Panel panel = null;
		for(Map<String,String> map : datas){
			subViewHolder = new SubViewHolder();
			subViewHolder.setSubView(map.get("url"));
			subViewHolder.setId("subViewHolder_"+map.get("id"));
			
			panel = new Panel();
			panel.addChild(subViewHolder);
			panel.setId("panel_"+map.get("id"));
			cardBook.addControl(panel);
		}
	}
}
