/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.manager;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.bstek.dorado.data.Constants;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.config.DataTypeName;
import com.bstek.dorado.data.provider.And;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.Junction;
import com.bstek.dorado.data.provider.Or;
import com.bstek.dorado.data.provider.Order;
import com.bstek.dorado.data.provider.filter.FilterCriterionParser;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.view.config.xml.ViewXmlConstants;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.manager.ViewConfigManager;
import com.bstek.dorado.view.service.DataServiceProcessorSupport.ParsedDataObjectName;
import com.bstek.dorado.web.DoradoContext;

/**
 * Dorado7 ViewManager工具类，利用该类可快速 取到ViewConfig、DataProvider之类Dorado7服务端对象
 * 
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public class ViewManagerHelper {
	protected static final String PRIVATE_VIEW_OBJECT_PREFIX = ViewXmlConstants.PATH_VIEW_SHORT_NAME + Constants.PRIVATE_DATA_OBJECT_SUBFIX;
	private ViewConfigManager viewConfigManager;
	private DataProviderManager dataProviderManager;
	private DataTypeManager dataTypeManager;
	private FilterCriterionParser filterCriterionParser;

	public ViewConfig getViewConfig(DoradoContext context, String viewName) throws Exception {
		ViewConfig viewConfig = (ViewConfig) context.getAttribute(viewName);
		if (viewConfig == null) {
			viewConfig = viewConfigManager.getViewConfig(viewName);
			context.setAttribute(viewName, viewConfig);
		}
		return viewConfig;
	}

	public DataProvider getDataProvider(String dataProviderId) throws Exception {
		DataProvider dataProvider;
		// 判断是否View中的私有DataProvider
		if (dataProviderId.startsWith(PRIVATE_VIEW_OBJECT_PREFIX)) {
			ParsedDataObjectName parsedName = new ParsedDataObjectName(dataProviderId);
			ViewConfig viewConfig = getViewConfig(DoradoContext.getCurrent(), parsedName.getViewName());
			dataProvider = viewConfig.getDataProvider(parsedName.getDataObject());
		} else {
			dataProvider = dataProviderManager.getDataProvider(dataProviderId);
		}
		return dataProvider;
	}

	public DataType getDataType(String dataTypeName) throws Exception {
		DataType dataType;
		// 判断是否View中的私有DataType
		if (dataTypeName.startsWith(PRIVATE_VIEW_OBJECT_PREFIX)) {
			DoradoContext context = DoradoContext.getCurrent();
			dataType = (DataType) context.getAttribute(dataTypeName);
			if (dataType == null) {
				ParsedDataObjectName parsedName = new ParsedDataObjectName(dataTypeName);
				String viewName = parsedName.getViewName();
				ViewConfig viewConfig = getViewConfig(context, viewName);
				String name = parsedName.getDataObject();
				DataTypeName dtn = new DataTypeName(name);
				if (dtn.getSubDataTypes().length == 1) {
					String subName = dtn.getSubDataTypes()[0];
					if (subName.startsWith(PRIVATE_VIEW_OBJECT_PREFIX)) {
						parsedName = new ParsedDataObjectName(subName);
						name = new StringBuffer().append(dtn.getOriginDataType()).append('[').append(parsedName.getDataObject()).append(']').toString();
					}
				}
				dataType = viewConfig.getDataType(name);
				context.setAttribute(dataTypeName, dataType);
			}
		} else {
			dataType = dataTypeManager.getDataType(dataTypeName);
		}
		return dataType;
	}

	public Criteria getCriteria(ObjectNode rudeCriteria) throws Exception {
		Criteria criteria = new Criteria();
		if (rudeCriteria.has("criterions")) {
			ArrayNode criterions = (ArrayNode) rudeCriteria.get("criterions");
			if (criterions != null) {
				for (Iterator<JsonNode> it = criterions.iterator(); it.hasNext();) {
					criteria.addCriterion(parseCriterion((ObjectNode) it.next()));
				}
			}
		}

		if (rudeCriteria.has("orders")) {
			ArrayNode orders = (ArrayNode) rudeCriteria.get("orders");
			if (orders != null) {
				for (Iterator<JsonNode> it = orders.iterator(); it.hasNext();) {
					ObjectNode rudeCriterion = (ObjectNode) it.next();
					Order order = new Order(JsonUtils.getString(rudeCriterion, "property"), JsonUtils.getBoolean(rudeCriterion, "desc"));
					criteria.addOrder(order);
				}
			}
		}
		return criteria;
	}
	public Criterion parseCriterion(ObjectNode rudeCriterion) throws Exception {
		String junction = JsonUtils.getString(rudeCriterion, "junction");
		if (StringUtils.isNotEmpty(junction)) {
			Junction junctionCrition;
			if ("or".equals(junction)) {
				junctionCrition = new Or();
			} else {
				junctionCrition = new And();
			}
			ArrayNode criterions = (ArrayNode) rudeCriterion.get("criterions");
			if (criterions != null) {
				for (Iterator<JsonNode> it = criterions.iterator(); it.hasNext();) {
					junctionCrition.addCriterion(parseCriterion((ObjectNode) it.next()));
				}
			}
			return junctionCrition;
		} else {
			String property = JsonUtils.getString(rudeCriterion, "property");
			String expression = JsonUtils.getString(rudeCriterion, "expression");
			String dataTypeName = JsonUtils.getString(rudeCriterion, "dataType");
			DataType dataType = null;
			if (StringUtils.isNotEmpty(dataTypeName)) {
				dataType = this.getDataType(dataTypeName);
			}
			return this.getFilterCriterionParser().createFilterCriterion(property, dataType, expression);
		}
	}

	public ViewConfigManager getViewConfigManager() {
		return viewConfigManager;
	}

	public void setViewConfigManager(ViewConfigManager viewConfigManager) {
		this.viewConfigManager = viewConfigManager;
	}

	public DataProviderManager getDataProviderManager() {
		return dataProviderManager;
	}

	public void setDataProviderManager(DataProviderManager dataProviderManager) {
		this.dataProviderManager = dataProviderManager;
	}

	public DataTypeManager getDataTypeManager() {
		return dataTypeManager;
	}

	public void setDataTypeManager(DataTypeManager dataTypeManager) {
		this.dataTypeManager = dataTypeManager;
	}

	public FilterCriterionParser getFilterCriterionParser() {
		return filterCriterionParser;
	}

	public void setFilterCriterionParser(FilterCriterionParser filterCriterionParser) {
		this.filterCriterionParser = filterCriterionParser;
	}
}
