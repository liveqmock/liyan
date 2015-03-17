/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.d7.online;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.hibernate.management.impl.BeanUtils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.InitializingBean;

import com.bstek.dorado.core.resource.ResourceManager;
import com.bstek.dorado.core.resource.ResourceManagerUtils;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.ParameterWrapper;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.variant.MetaData;
import com.innofi.component.report.manager.ViewManagerHelper;
import com.innofi.component.report.model.ReportFormDataModel;
import com.innofi.component.report.model.ReportFormModel;
import com.innofi.component.report.model.ReportGridDataModel;
import com.innofi.component.report.model.ReportGridHeaderModel;
import com.innofi.component.report.model.ReportGridModel;
import com.innofi.component.report.model.ReportTitleModel;
import com.innofi.component.report.model.TitleStyle;

/**
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class GenerateModels implements InitializingBean {
	private ViewManagerHelper viewManagerHelper;
	private static ResourceManager res;

	private GenerateModels() {
	}
	@SuppressWarnings("unchecked")
	public ReportTitleModel generateReportTitle(Map<String, Object> map) {
		ReportTitleModel reportTitle = new ReportTitleModel();
		if (map == null)
			return reportTitle;
		Map<String, Object> titleInfos = (Map<String, Object>) map.get("titleInfos");
		if (titleInfos == null)
			return reportTitle;
		Boolean showTitle = (Boolean) titleInfos.get("showTitle");
		if (showTitle != null && showTitle) {
			reportTitle.setShowTitle(true);
			reportTitle.setTitle((String) titleInfos.get("title"));
			reportTitle.setStyle(createTitleStyle(titleInfos));
		}
		if (titleInfos.get("showBorder") != null) {
			reportTitle.setShowBorder((Boolean) titleInfos.get("showBorder"));
		}
		if (titleInfos.get("showPageNo") != null) {
			reportTitle.setShowPageNo((Boolean) titleInfos.get("showPageNo"));
		}
		return reportTitle;
	}

	private TitleStyle createTitleStyle(Map<String, Object> map) {
		TitleStyle titleStyle = new TitleStyle();
		int fontSize = (Integer) map.get("fontSize");
		String fontColor = (String) map.get("fontColor");
		String bgColor = (String) map.get("bgColor");
		titleStyle.setBgColor(this.createRGB(bgColor));
		titleStyle.setFontSize(fontSize);
		titleStyle.setFontColor(this.createRGB(fontColor));
		return titleStyle;
	}

	private int[] createRGB(String s) {
		try {
			if (StringUtils.isNotEmpty(s)) {
				String pattern = "[0-9]{1,3},[0-9]{1,3},[0-9]{1,3}";
				boolean flag = s.matches(pattern);
				String[] colors = s.split(",");
				if (!flag || Integer.parseInt(colors[0]) > 255 || Integer.parseInt(colors[1]) > 255 || Integer.parseInt(colors[2]) > 255) {

					throw new IllegalArgumentException(s + res.getString("ReportClazz/generateModels.value"));
				}
				return new int[]{Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2])};
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ReportFormModel> createReportFormModels(Map<String, Object> map) {
		List<ReportFormModel> reportFormModels = new ArrayList<ReportFormModel>();
		Map<String, Object> form = (Map<String, Object>) map.get("form");

		List<Map<String, Object>> formInfos = (List<Map<String, Object>>) form.get("formInfos");
		Map<String, Object> formStyle = (Map<String, Object>) form.get("formStyle");
		if (formStyle == null)
			formStyle = new HashMap<String, Object>();
		if (formInfos != null && formInfos.size() > 0) {
			for (Map<String, Object> formInfo : formInfos) {
				reportFormModels.add(this.createReportFormModel(formStyle, formInfo));
			}
		}
		return reportFormModels;
	}

	@SuppressWarnings("unchecked")
	public ReportFormModel createReportFormModel(Map<String, Object> map, Map<String, Object> formInfo) {
		ReportFormModel reportFormModel = new ReportFormModel();
		List<ReportFormDataModel> listReportFormDatas = new ArrayList<ReportFormDataModel>();

		int columnCount = (Integer) formInfo.get("columnCount");
		boolean showBorder = true;
		if (map.get("showBorder") != null) {
			showBorder = (Boolean) map.get("showBorder");
		}
		reportFormModel.setColumnCount(columnCount);
		reportFormModel.setShowBorder(showBorder);
		List<Map<String, Object>> datas = (List<Map<String, Object>>) formInfo.get("datas");
		int labelAlign = 2;
		int dataAlign = 0;
		int dataStyle = 0;
		if (map.get("labelAlign") != null) {
			labelAlign = (Integer) map.get("labelAlign");
		}
		if (map.get("dataStyle") != null) {
			dataStyle = (Integer) map.get("dataStyle");
		}
		if (map.get("dataAlign") != null) {
			dataAlign = (Integer) map.get("dataAlign");
		}
		ReportFormDataModel reportFormDataModel = null;
		for (Map<String, Object> dataMap : datas) {
			reportFormDataModel = new ReportFormDataModel();
			Object data = dataMap.get("value");
			int colSpan = (Integer) dataMap.get("colSpan");
			int rowSpan = (Integer) dataMap.get("rowSpan");
			String label = (String) dataMap.get("label");
			reportFormDataModel.setLabel(label);
			reportFormDataModel.setLabelAlign(labelAlign);
			reportFormDataModel.setData(data);
			reportFormDataModel.setDataAlign(dataAlign);
			reportFormDataModel.setDataStyle(dataStyle);
			reportFormDataModel.setColSpan(colSpan);
			reportFormDataModel.setRowSpan(rowSpan);
			listReportFormDatas.add(reportFormDataModel);
		}
		reportFormModel.setListReportFormDataModel(listReportFormDatas);
		return reportFormModel;
	}

	@SuppressWarnings("unchecked")
	public List<ReportGridModel> createReportGridModels(Map<String, Object> map) throws Exception {
		List<ReportGridModel> reportGridModels = new ArrayList<ReportGridModel>();
		Map<String, Object> grid = (Map<String, Object>) map.get("grid");

		List<Map<String, Object>> gridInfos = (List<Map<String, Object>>) grid.get("gridInfos");
		if (gridInfos != null && gridInfos.size() > 0) {
			for (Map<String, Object> gridInfo : gridInfos) {
				reportGridModels.add(this.createReportGridModel(gridInfo));
			}
		}
		return reportGridModels;
	}

	@SuppressWarnings("unchecked")
	public ReportGridModel createReportGridModel(Map<String, Object> map) throws Exception {
		ReportGridModel gridModel = new ReportGridModel();
		List<Map<String, Object>> columnInfos = (List<Map<String, Object>>) map.get("columnInfos");
		List<ReportGridHeaderModel> gridHeaders = new ArrayList<ReportGridHeaderModel>();
		this.createGridColumnHeader(columnInfos, gridHeaders, null);
		gridModel.setGridHeaderModelList(gridHeaders);
		gridModel.setGridDataModel(this.createReportGridDataModel(map, columnInfos));
		return gridModel;
	}

	@SuppressWarnings("unchecked")
	private void createGridColumnHeader(List<Map<String, Object>> columnInfos, List<ReportGridHeaderModel> headerList, ReportGridHeaderModel parent) {
		if (columnInfos == null)
			return;
		for (Map<String, Object> column : columnInfos) {
			String columnName = (String) column.get("columnName");
			int level = (Integer) column.get("level");
			String label = (String) column.get("label");
			int width = (Integer) column.get("width");
			String bgColor = (String) column.get("bgColor");
			String fontColor = (String) column.get("fontColor");
			int fontSize = 10;
			if (column.get("fontSize") != null) {
				fontSize = (Integer) column.get("fontSize");
			}
			int align = 1;
			if (column.get("align") != null) {
				align = (Integer) column.get("align");
			}
			ReportGridHeaderModel header = new ReportGridHeaderModel();
			header.setLevel(level);
			header.setAlign(align);
			if (StringUtils.isNotEmpty(bgColor)) {
				header.setBgColor(this.createRGB(bgColor));
			}
			if (StringUtils.isNotEmpty(fontColor)) {
				header.setFontColor(this.createRGB(fontColor));
			}
			header.setColumnName(columnName);
			header.setLabel(label);
			header.setFontSize(fontSize);
			header.setWidth(width);
			if (parent != null) {
				parent.addGridHeader(header);
				header.setParent(parent);
			} else {
				headerList.add(header);
			}
			List<Map<String, Object>> children = (List<Map<String, Object>>) column.get("children");
			if (children != null) {
				this.createGridColumnHeader(children, headerList, header);
			}
		}
	}
	@SuppressWarnings("unchecked")
	private ReportGridDataModel createReportGridDataModel(Map<String, Object> map, List<Map<String, Object>> columnInfos) throws Exception {
		List<Map<String, Object>> dataList = null;
		String dataScope = (String) map.get("dataScope");
		String treeColumn = (String) map.get("treeColumn");
		if (StringUtils.isNotEmpty(dataScope) && dataScope.equals("serverAll")) {
			Object dataProviderParameter = map.get("dataProviderParameter");
			String dataProviderId = (String) map.get("dataProviderId");
			int pageSize = Integer.valueOf(map.get("pageSize").toString());
			int maxSize = 65535;
			if (map.get("maxSize") != null) {
				maxSize = Integer.valueOf(map.get("maxSize").toString());
			}
			DataProvider dataProvider = viewManagerHelper.getDataProvider(dataProviderId);
			if (map.get("sysParameter") != null) {
				ObjectMapper om = JsonUtils.getObjectMapper();
				String sp = om.writeValueAsString(map.get("sysParameter"));
				ObjectNode rudeSysParameter = (ObjectNode) om.readTree(sp);
				JsonNode rudeCriteria = null;
				if (rudeSysParameter != null) {
					rudeCriteria = rudeSysParameter.remove("criteria");
				}
				MetaData sysParameter = null;
				if (rudeSysParameter != null) {
					sysParameter = (MetaData) JsonUtils.toJavaObject(rudeSysParameter, null, null, false, null);
					if (rudeCriteria != null && rudeCriteria instanceof ObjectNode) {
						sysParameter.put("criteria", viewManagerHelper.getCriteria((ObjectNode) rudeCriteria));
					}
					if (sysParameter != null && !sysParameter.isEmpty()) {
						dataProviderParameter = new ParameterWrapper(dataProviderParameter, sysParameter);
					}
				}
			}
			DataType resultDataType = null;
			String resultDataTypeName = (String) map.get("resultDataType");
			if (StringUtils.isNotEmpty(resultDataTypeName)) {
				resultDataType = viewManagerHelper.getDataType(resultDataTypeName);
			}
			Collection<Object> list = null;
			if (pageSize > 0) {
				Page<Object> page = new Page<Object>(maxSize, 1);
				dataProvider.getPagingResult(dataProviderParameter, page, resultDataType);
				list = page.getEntities();
			} else {
				list = (Collection<Object>) dataProvider.getResult(dataProviderParameter, resultDataType);
			}
			dataList = new ArrayList<Map<String, Object>>();
			if (null != list) {
				for (Object obj : list) {
					if (obj instanceof Map) {
						dataList.add((Map<String, Object>) obj);
					} else {
						dataList.add(PropertyUtils.describe(obj));
					}
				}
			}
		} else {
			dataList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> clientData = (List<Map<String, Object>>) map.get("data");
			List<Map<String, Object>> subDataList;
			for (Map<String, Object> tempMap : clientData) {
				if (tempMap.get(treeColumn) != null) {
					map.put(treeColumn, tempMap.get(treeColumn));
				}
				dataList.add(tempMap);
				if (tempMap.get("children") != null) {
					subDataList = (List<Map<String, Object>>) tempMap.get("children");
					this.buildChildData(dataList, subDataList, "", treeColumn);
				}
			}
		}

		for (Map<String, Object> dataMap : dataList) {
			Set<String> nameSet = dataMap.keySet();
			for (String name : nameSet) {
				replaceValueWithMapping(dataMap, columnInfos, name);
			}
		}
		Map<String, Object> style = (Map<String, Object>) map.get("gridDataStyle");
		ReportGridDataModel gridData = new ReportGridDataModel();
		gridData.setDatas(dataList);
		gridData.setTreeColumn(treeColumn);
		if (style != null) {
			gridData.setContentFontSize((Integer) style.get("fontSize"));
			String contentFontColor = (String) style.get("fontColor");
			String contentBgColor = (String) style.get("bgColor");
			gridData.setContentFontColor(this.createRGB(contentFontColor));
			gridData.setContentFontAlign((Integer) style.get("fontAlign"));
			gridData.setContentBgColor(this.createRGB(contentBgColor));
		}
		return gridData;
	}

	@SuppressWarnings("unchecked")
	private void replaceValueWithMapping(Map<String, Object> dataMap, List<Map<String, Object>> columnInfos, String name) throws Exception {
		for (Map<String, Object> columnMap : columnInfos) {
			Object obj = columnMap.get(name + "_mapping");
			if (obj != null) {
				List<Map<Object, Object>> mappingList = (List<Map<Object, Object>>) obj;
				for (Map<Object, Object> mappingMap : mappingList) {
					Object mappingKey = mappingMap.get("key");
					if (mappingKey != null)
						mappingKey = mappingKey.toString();
					Object value = dataMap.get(name);
					if (value != null)
						value = value.toString();
					if (mappingKey.equals(value)) {
						dataMap.put(name, mappingMap.get("value"));
						break;
					}
				}
			} else {
				Object children = columnMap.get("children");
				if (children != null) {
					List<Map<String, Object>> childrenList = (List<Map<String, Object>>) children;
					replaceValueWithMapping(dataMap, childrenList, name);
				}
			}

			String displayName = name + "_displayProperty";
			if (columnMap.containsKey(displayName)) {
				String propertyName = (String) columnMap.get(displayName);
				if (StringUtils.isNotEmpty(propertyName)) {
					Object data = dataMap.get(name);
					if (data != null) {
						dataMap.put(name, BeanUtils.getBeanProperty(data, propertyName));
					}
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void buildChildData(List<Map<String, Object>> dataList, List<Map<String, Object>> sub, String header, String treeColumn) {
		for (Map<String, Object> tempMap : sub) {
			String tmp = header + "\t";
			if (tempMap.get(treeColumn) != null) {
				tempMap.put(treeColumn, tmp + tempMap.get(treeColumn));
			}
			dataList.add(tempMap);
			if (tempMap.get("children") != null) {
				this.buildChildData(dataList, (List<Map<String, Object>>) tempMap.get("children"), tmp, treeColumn);
			}
		}
	}

	public ViewManagerHelper getViewManagerHelper() {
		return viewManagerHelper;
	}
	public void setViewManagerHelper(ViewManagerHelper viewManagerHelper) {
		this.viewManagerHelper = viewManagerHelper;
	}

	public void afterPropertiesSet() throws Exception {
		res = ResourceManagerUtils.get(GenerateModels.class);

	}
}
