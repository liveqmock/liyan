/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.d7.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.hibernate.management.impl.BeanUtils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.core.resource.ResourceManager;
import com.bstek.dorado.core.resource.ResourceManagerUtils;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.ParameterWrapper;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.variant.MetaData;
import com.innofi.component.report.d7.online.GenerateModels;
import com.innofi.component.report.domain.UploadInfo;
import com.innofi.component.report.manager.ViewManagerHelper;
import com.innofi.component.report.model.ReportFormModel;
import com.innofi.component.report.model.ReportTitleModel;
import com.innofi.component.report.pdf.ColumnHeader;
import com.innofi.component.report.pdf.PdfToSwfConverter;
import com.innofi.component.report.pdf.ReportData;
import com.innofi.component.report.pdf.TextChunk;
import com.innofi.component.report.pdf.model.ReportDataModel;
import com.innofi.component.report.pdf.service.PdfFormGridReportService;
import com.innofi.component.report.pdf.service.PdfFormReportService;
import com.innofi.component.report.pdf.service.PdfGridReportService;
import com.innofi.component.report.upload.TempFilePersister;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 负责构建PDF格式报表数据信息，并将其存储到cache当中，<br>
 * 在controller当中，取出cache中缓存pdf报表数据，最终生成PDF格式报表
 * 
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public class PdfReportController implements ApplicationContextAware, InitializingBean {
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private PdfToSwfConverter pdfToSwfConverter;
	private TempFilePersister tempFilePersister;
	private SwfFileHandler swfFileHandler;

	private PdfFormReportService pdfFormReportService;
	private PdfGridReportService pdfGridReportService;
	private PdfFormGridReportService pdfFormGridReportService;
	private ViewManagerHelper viewManagerHelper;
	private GenerateModels generator;

	private static ResourceManager res;

	/**
	 * 将给定的pdf文件转换成swf，并将生成的swf文件写入到HttpServletResponse当中供用户下载使用
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void doGenerateSwf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = request.getParameter("sourcePdf");
		InputStream in = tempFilePersister.get(fileName);
		String pdfName = UUID.randomUUID().toString() + ".pdf";
		String sourcePdf = ContextHolder.getIdfTempFileStorePath() + pdfName;
		OutputStream out = new FileOutputStream(sourcePdf);
		IOUtils.copy(in, out);
		IOUtils.closeQuietly(in);
		IOUtils.closeQuietly(out);
		String swf = pdfToSwfConverter.convert(sourcePdf, null);
		response.setHeader("Server", "http://www.bstek.com");
		response.setContentType("application/x-shockwave-flash");
		response.setHeader("Content-Disposition", "attachment;filename=\"report.swf\"");
		OutputStream output = response.getOutputStream();
		InputStream input = new FileInputStream(swf);
		IOUtils.copy(input, output);
		output.flush();
		IOUtils.closeQuietly(input);
		IOUtils.closeQuietly(output);
	}

	/**
	 * 请求服务端某个SWF文件，以实现客户端在线显示功能，并将生成的swf文件写入到HttpServletResponse当中供用户下载使用
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void doRetriveSwf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (swfFileHandler == null) {

			throw new RuntimeException(res.getString("ReportClazz/pdfReportController.contextValue"));
		}
		File file = swfFileHandler.retriveSwf(request);
		if (file == null || !file.exists()) {

			throw new RuntimeException(swfFileHandler.getClass().getName() + res.getString("ReportClazz/pdfReportController.fileExist"));
		}
		response.setHeader("Server", "http://www.bstek.com");
		response.setContentType("application/x-shockwave-flash");
		response.setHeader("Content-Disposition", "attachment;filename=\"report.swf\"");
		OutputStream output = response.getOutputStream();
		InputStream input = new FileInputStream(file);
		IOUtils.copy(input, output);
		output.flush();
		IOUtils.closeQuietly(input);
		IOUtils.closeQuietly(output);
	}

	/**
	 * 构建客户端通过ajax方式提交上来的用于生成pdf格式的的表单数据,<br>
	 * 将这些数据进行装配，装配完成后放到临时缓存当中,以备controller调用
	 * 
	 * @param map
	 * @throws Exception
	 */
	@Expose
	public String buildPdfFormReportData(Map<String, Object> map) throws Exception {
		ReportTitleModel reportTitle = generator.generateReportTitle(map);
		List<ReportFormModel> reportFormModels = generator.createReportFormModels(map);
		String fileName = UUID.randomUUID().toString() + ".pdf";
		String path = ContextHolder.getIdfTempFileStorePath() + fileName;
		FileOutputStream out = new FileOutputStream(path);
		this.pdfFormReportService.generateFormReport(reportTitle, reportFormModels, out);
		out.close();

		FileInputStream in = new FileInputStream(path);
		UploadInfo info = new UploadInfo();
		info.setFileName(fileName);
		fileName = tempFilePersister.persist(in, info);
		if (in != null)
			in.close();
		return fileName;
	}

	/**
	 * 供AjaxAction调用，用于构建客户端提交上来的要导出PDF的数据及列信息，<br>
	 * 并将构建好的数据信息放在临时缓存当中，以备后续controller中生成下载PDF报表文件使用
	 * 
	 * @param parameter
	 * @throws Exception
	 */
	@Expose
	public String buildPdfGridReportData(Map<String, Object> parameter) throws Exception {
		ReportTitleModel reportTitle = generator.generateReportTitle(parameter);

		List<ReportDataModel> dataModels = this.createReportGridDataModels(parameter);

		String fileName = UUID.randomUUID().toString() + ".pdf";
		String path = ContextHolder.getIdfTempFileStorePath() + fileName;
		FileOutputStream out = new FileOutputStream(path);

		this.pdfGridReportService.generateGridReport(reportTitle, dataModels, out);

		out.close();
		FileInputStream in = new FileInputStream(path);
		UploadInfo info = new UploadInfo();
		info.setFileName(fileName);
		fileName = tempFilePersister.persist(in, info);
		if (in != null)
			in.close();
		return fileName;
	}

	/**
	 * 客户端ajaxAction调用生成由Form与Grid混合的报表文件，存放在系统临时目录
	 * 
	 * @param map
	 *            客户端参数
	 * @return 返回生成的文件名称
	 * @throws Exception
	 */
	@Expose
	public String buildFormGridReportData(Map<String, Object> map) throws Exception {
		ReportTitleModel reportTitle = generator.generateReportTitle(map);
		List<ReportFormModel> reportFormModels = generator.createReportFormModels(map);

		List<ReportDataModel> gridDataModels = this.createReportGridDataModels(map);

		String fileName = UUID.randomUUID().toString() + ".pdf";
		String path = ContextHolder.getIdfTempFileStorePath() + fileName;
		FileOutputStream out = new FileOutputStream(path);

		this.pdfFormGridReportService.generateFormGridReport(reportTitle, reportFormModels, gridDataModels, out);

		out.close();
		FileInputStream in = new FileInputStream(path);
		UploadInfo info = new UploadInfo();
		info.setFileName(fileName);
		fileName = tempFilePersister.persist(in, info);
		if (in != null)
			in.close();
		return fileName;
	}

	@SuppressWarnings("unchecked")
	private List<ReportDataModel> createReportGridDataModels(Map<String, Object> map) throws Exception {
		List<ReportDataModel> dataModels = new ArrayList<ReportDataModel>();

		Map<String, Object> grid = (Map<String, Object>) map.get("grid");

		List<Map<String, Object>> gridInfos = (List<Map<String, Object>>) grid.get("gridInfos");

		if (gridInfos != null && gridInfos.size() > 0) {
			for (Map<String, Object> gridInfo : gridInfos) {
				dataModels.add(this.createReportGridDataModel(gridInfo));
			}
		}

		return dataModels;
	}

	@SuppressWarnings("unchecked")
	private ReportDataModel createReportGridDataModel(Map<String, Object> map) throws Exception {

		List<Map<String, Object>> columnsInfo = (List<Map<String, Object>>) map.get("columnInfos");
		List<ColumnHeader> columnInfoDatas = new ArrayList<ColumnHeader>();
		Map<String, Object> colMappingMap = new HashMap<String, Object>();
		buildColumnHeader(columnsInfo, columnInfoDatas, colMappingMap, null);
		String dataScope = (String) map.get("dataScope");
		List<ReportData> dataList = null;
		if (StringUtils.isNotEmpty(dataScope) && dataScope.equals("serverAll")) {
			Object dataProviderParameter = map.get("dataProviderParameter");
			String dataProviderId = (String) map.get("dataProviderId");
			int pageSize = Integer.valueOf(map.get("pageSize").toString());
			int maxSize = 65535;
			if (map.get("maxSize") != null)
				maxSize = Integer.valueOf(map.get("maxSize").toString());
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
			Collection<Object> list = null;
			DataType resultDataType = null;
			String resultDataTypeName = (String) map.get("resultDataType");
			if (StringUtils.isNotEmpty(resultDataTypeName)) {
				resultDataType = viewManagerHelper.getDataType(resultDataTypeName);
			}
			if (pageSize > 0) {
				Page<Object> page = new Page<Object>(maxSize, 1);
				dataProvider.getPagingResult(dataProviderParameter, page, resultDataType);
				list = page.getEntities();
			} else {
				list = (Collection<Object>) dataProvider.getResult(dataProviderParameter, resultDataType);
			}
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			if (null != list) {
				for (Object obj : list) {
					if (obj instanceof Map) {
						mapList.add((Map<String, Object>) obj);
					} else {
						mapList.add(PropertyUtils.describe(obj));
					}
				}
			}
			dataList = buildColumnData(mapList, colMappingMap, columnInfoDatas, (Map<String, Object>) map.get("contentDataStyle"));
		} else {
			List<Map<String, Object>> dataListMap = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> clientData = (List<Map<String, Object>>) map.get("data");
			List<Map<String, Object>> subDataList;
			String treeColumn = (String) map.get("treeColumn");
			for (Map<String, Object> tempMap : clientData) {
				if (tempMap.get(treeColumn) != null) {
					map.put(treeColumn, tempMap.get(treeColumn));
				}
				dataListMap.add(tempMap);
				if (tempMap.get("children") != null) {
					subDataList = (List<Map<String, Object>>) tempMap.get("children");
					this.buildChildData(dataListMap, subDataList, "", treeColumn);
				}
			}
			dataList = buildColumnData(dataListMap, colMappingMap, columnInfoDatas, (Map<String, Object>) map.get("contentDataStyle"));
		}
		ReportDataModel dataModel = new ReportDataModel(columnInfoDatas, dataList);
		return dataModel;
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
	private void buildDataColumnNames(List<ColumnHeader> columnInfoDatas, List<String> result) {
		for (ColumnHeader header : columnInfoDatas) {
			if (header.getColumnHeaders().size() == 0) {
				result.add(header.getName());
			} else {
				buildDataColumnNames(header.getColumnHeaders(), result);
			}
		}
	}
	private List<ReportData> buildColumnData(List<Map<String, Object>> dataList, Map<String, Object> colMappingMap, List<ColumnHeader> columnInfoDatas, Map<String, Object> style) {
		List<ReportData> result = new ArrayList<ReportData>();
		List<String> dataColumnNameList = new ArrayList<String>();
		buildDataColumnNames(columnInfoDatas, dataColumnNameList);
		for (Map<String, Object> dataMap : dataList) {
			for (String name : dataColumnNameList) {
				Object data = dataMap.get(name);
				String mappingName = name + "_mapping";
				if (colMappingMap.containsKey(mappingName)) {
					@SuppressWarnings("unchecked")
					List<Map<Object, Object>> mappingList = (List<Map<Object, Object>>) colMappingMap.get(mappingName);
					for (Map<Object, Object> mapping : mappingList) {
						Object mappingKey = mapping.get("key");
						if (mappingKey != null)
							mappingKey = mappingKey.toString();
						Object value = data;
						if (value != null)
							value = value.toString();
						if (mappingKey.equals(value)) {
							data = mapping.get("value");
							break;
						}
					}
				}

				String displayName = name + "_displayProperty";
				if (colMappingMap.containsKey(displayName)) {
					String propertyName = (String) colMappingMap.get(displayName);
					if (StringUtils.isNotEmpty(propertyName)) {
						data = BeanUtils.getBeanProperty(data, propertyName);
					}
				}

				TextChunk textChunk = new TextChunk();
				String value = "";
				if (data != null && data instanceof Date) {
					value = simpleDateFormat.format((Date) data);
					if (value.endsWith("00:00:00")) {
						value = value.substring(0, 11);
					}
				} else {
					if (data != null) {
						value = data.toString();
					}
				}
				if (style != null) {
					textChunk.setFontSize((Integer) style.get("contentFontSize"));
					String[] color = ((String) style.get("contentFontColor")).split(",");
					int rgb[] = {Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2])};
					textChunk.setFontColor(rgb);
					textChunk.setAlign((Integer) style.get("contentFontAlign"));

				}
				textChunk.setText(value);
				ReportData columnData = new ReportData(textChunk);
				result.add(columnData);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private void buildColumnHeader(List<Map<String, Object>> columnsInfo, List<ColumnHeader> result, Map<String, Object> colMappingMap, ColumnHeader parentHeader) throws Exception {
		for (Map<String, Object> column : columnsInfo) {
			String columnName = (String) column.get("columnName");
			int level = (Integer) column.get("level");
			String label = (String) column.get("label");
			int width = (Integer) column.get("width");
			String bgColor = (String) column.get("bgColor");
			String fontColor = (String) column.get("fontColor");
			int align = 1;
			int fontSize = 9;
			if (column.get("fontSize") != null) {
				fontSize = (Integer) column.get("fontSize");
			}
			if (column.get("align") != null) {
				align = (Integer) column.get("align");
			}
			ColumnHeader header = new ColumnHeader(level);
			header.setAlign(align);
			if (StringUtils.isNotEmpty(bgColor)) {
				String[] bgcolors = bgColor.split(",");
				if (bgcolors.length != 3) {

					throw new IllegalArgumentException(columnName + res.getString("ReportClazz/pdfReportController.valueWrong"));
				}
				header.setBgColor(new int[]{Integer.parseInt(bgcolors[0]), Integer.parseInt(bgcolors[1]), Integer.parseInt(bgcolors[2])});
			}
			if (StringUtils.isNotEmpty(fontColor)) {
				String[] colors = fontColor.split(",");
				if (colors.length != 3) {

					throw new IllegalArgumentException(columnName + res.getString("ReportClazz/pdfReportController.valueWrong"));
				}
				header.setFontColor(new int[]{Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2])});
			}
			header.setName(columnName);
			header.setText(label);
			header.setFontSize(fontSize);
			header.setWidth(width);
			if (parentHeader != null) {
				parentHeader.addColumnHeader(header);
			} else {
				result.add(header);
			}
			String mappingName = columnName + "_mapping";
			if (column.containsKey(mappingName)) {
				colMappingMap.put(mappingName, column.get(mappingName));
			}
			String displayName = columnName + "_displayProperty";
			if (column.containsKey(displayName)) {
				colMappingMap.put(displayName, column.get(displayName));
			}
			List<Map<String, Object>> children = (List<Map<String, Object>>) column.get("children");
			if (children != null) {
				buildColumnHeader(children, result, colMappingMap, header);
			}
		}
	}
	public PdfToSwfConverter getPdfToSwfConverter() {
		return pdfToSwfConverter;
	}
	public void setPdfToSwfConverter(PdfToSwfConverter pdfToSwfConverter) {
		this.pdfToSwfConverter = pdfToSwfConverter;
	}
	public TempFilePersister getTempFilePersister() {
		return tempFilePersister;
	}
	public void setTempFilePersister(TempFilePersister tempFilePersister) {
		this.tempFilePersister = tempFilePersister;
	}

	public void setPdfFormReportService(PdfFormReportService pdfFormReportService) {
		this.pdfFormReportService = pdfFormReportService;
	}

	public void setPdfGridReportService(PdfGridReportService pdfGridReportService) {
		this.pdfGridReportService = pdfGridReportService;
	}

	public ViewManagerHelper getViewManagerHelper() {
		return viewManagerHelper;
	}

	public void setViewManagerHelper(ViewManagerHelper viewManagerHelper) {
		this.viewManagerHelper = viewManagerHelper;
	}

	public GenerateModels getGenerator() {
		return generator;
	}

	public void setGenerator(GenerateModels generator) {
		this.generator = generator;
	}

	public void setPdfFormGridReportService(PdfFormGridReportService pdfFormGridReportService) {
		this.pdfFormGridReportService = pdfFormGridReportService;
	}
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		Map<String, SwfFileHandler> map = context.getBeansOfType(SwfFileHandler.class);
		if (map.size() > 0) {
			swfFileHandler = map.values().iterator().next();
		}
	}

	public void afterPropertiesSet() throws Exception {
		res = ResourceManagerUtils.get(PdfReportController.class);

	}
}
