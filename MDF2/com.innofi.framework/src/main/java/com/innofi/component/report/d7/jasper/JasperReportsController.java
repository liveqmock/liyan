/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.d7.jasper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.innofi.component.report.service.IReportService;
import com.innofi.component.report.upload.TempFilePersister;
//import com.bstek.bdf.upload.TempFilePersister;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.core.resource.ResourceManager;
import com.bstek.dorado.core.resource.ResourceManagerUtils;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.innofi.component.report.domain.Report;
import com.innofi.component.report.domain.UploadInfo;
import com.innofi.component.report.jasper.JasperReportBuilder;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * JapserReport报表生成服务请求控制类
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class JasperReportsController implements InitializingBean{
	private TempFilePersister tempFilePersister;
	private static Set<String> keyWords;
	static {
		keyWords = new HashSet<String>();
		keyWords.add("dataProvider");
		keyWords.add("type");
		keyWords.add("jasperFile");
		keyWords.add("fileName");
	}
	
	private static ResourceManager res ;
		

	/**
	 * 创建指定报表类型的报表
	 * 
	 * @param parameters
	 * @return 返回临时文件名
	 * @throws Exception
	 */
	@Expose
	public String buildReport(Map<String, Object> parameters) throws Exception {
		String dataProviderName = (String) parameters.get("dataProvider");
		String targetType = (String) parameters.get("type");
		String reportName = (String) parameters.get("jasperFile");
		if (!StringUtils.hasText(targetType)) {
			targetType = "pdf";
		}
		Report report = this.reportService.findReportByEname(reportName);

		this.reportService.checkAuth(report);

		targetType = targetType.toUpperCase();
		String tempFileName = UUID.randomUUID().toString() + "." + this.getExportFileSuffix(targetType);
		String path = ContextHolder.getIdfTempFileStorePath() + File.separator + tempFileName;
		FileOutputStream fos = new FileOutputStream(path);
		Map<String, Object> reportParameter = new HashMap<String, Object>();
		for (Entry<String, Object> entry : parameters.entrySet()) {
			if (!keyWords.contains(entry.getKey())) {
				reportParameter.put(entry.getKey(), entry.getValue());
			}
		}
		this.buildFile(report, targetType, dataProviderName, parameters, fos);

		FileInputStream in = new FileInputStream(path);
		UploadInfo info = new UploadInfo();
		info.setFileName(tempFileName);
		tempFileName = tempFilePersister.persist(in, info);
		if (in != null)
			in.close();

		return tempFileName;
	}

	public void doDownloadTempReport(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws Exception {
		String ID_KEY = "id";

		String id = httpServletRequest.getParameter(ID_KEY);
		if (!StringUtils.hasText(id)) {
			id = (String) httpServletRequest.getAttribute(ID_KEY);
		}
		String type = httpServletRequest.getParameter("type");
		if (!StringUtils.hasText(type)) {
			type = "pdf";
		}
		String fileName = "report." + type;
		httpServletResponse.setContentType("application/octet-stream");
		httpServletResponse.setHeader("Connection", "close");// 表示不能用浏览器直接打开
		httpServletResponse.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		InputStream in = tempFilePersister.get(id);
		BufferedInputStream bin = new BufferedInputStream(in);
		ServletOutputStream out = httpServletResponse.getOutputStream();
		try {
			IOUtils.copy(bin, out);
		} finally {
			IOUtils.closeQuietly(in);
		}
		out.flush();
		out.close();

	}

	/**
	 * Japser报表输出或下载请求响应
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void doDownloadReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String exportFileName = request.getParameter("exportFileName");
		String jasperFile = request.getParameter("jasperFile");
		if (StringUtils.hasText(jasperFile)) {
			Map<String, Object> params = new HashMap<String, Object>();
			if (request.getParameter("dataProvider") != null) {
				params.put("dataProvider", request.getParameter("dataProvider"));
			}
			if (request.getParameter("type") != null) {
				params.put("type", request.getParameter("type"));
			}
			if (request.getParameter("jasperFile") != null) {
				params.put("jasperFile", request.getParameter("jasperFile"));
			}
			String p = null;
			for (Object key : request.getParameterMap().keySet()) {
				if (!keyWords.contains(key)) {
					p = request.getParameter(key.toString());
					params.put(key.toString(), p);
				}
			}
			exportFileName = this.buildReport(params);
		}
		String targetType = request.getParameter("type");

		String fileName = request.getParameter("fileName");

		if (!StringUtils.hasText(fileName)) {
			fileName = "report";
		}
		if (!StringUtils.hasText(targetType)) {
			targetType = "pdf";
		}
		targetType = targetType.toUpperCase();
		this.buildResponse(fileName, targetType, response);
		InputStream fis = tempFilePersister.get(exportFileName);
		OutputStream out = response.getOutputStream();
		IOUtils.copy(fis, out);
		out.flush();
		fis.close();
		out.close();
	}

	/**
	 * 报表文件构建
	 * 
	 * @param reportName
	 * @param targetType
	 * @param dataProviderName
	 * @param parameters
	 * @param outputStream
	 * @throws Exception
	 */
	private void buildFile(Report report, String targetType, String dataProviderName, Map<String, Object> parameters,
			OutputStream outputStream) throws Exception {
		if (StringUtils.hasText(dataProviderName)) {
			reportService.buildFile(report, targetType, dataProviderName, parameters, outputStream,
					this.getDataProviderData(dataProviderName, parameters));
		} else {
			reportService.buildFile(report, targetType, dataProviderName, parameters, outputStream, null);
		}
	}

	/**
	 * 构建HttpResponse对象输出头内容
	 * 
	 * @param fileName
	 * @param targetType
	 * @param response
	 * @throws Exception
	 */
	private void buildResponse(String fileName, String targetType, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(this.getContentType(targetType));
		if (!targetType.equals(JasperReportBuilder.PRINT_TYPE_HTML))
			response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8")
					+ "." + this.getExportFileSuffix(targetType));
	}

	/**
	 * 判断导出报表输出的格式
	 * 
	 * @param targetType
	 * @return 返回输出格式
	 */
	private String getContentType(String targetType)  throws Exception{
		if (targetType.equals(JasperReportBuilder.PRINT_TYPE_HTML)) {
			return "text/html";
		} else if (targetType.equals(JasperReportBuilder.PRINT_TYPE_PDF)) {
			return "application/pdf";
		} else if (targetType.equals(JasperReportBuilder.PRINT_TYPE_RTF)) {
			return "application/rtf";
		} else if (targetType.equals(JasperReportBuilder.PRINT_TYPE_EXCEL)) {
			return "application/msexcel";
		} else {
		
			throw new RuntimeException(res.getString("ReportClazz/jasperReportsController.UnSupportWay"));
		}
	}

	/**
	 * 获取导出文件的后缀名
	 * 
	 * @param targetType
	 * @return 返回文件后缀名
	 */
	private String getExportFileSuffix(String targetType)  throws Exception{
		if (targetType.equals(JasperReportBuilder.PRINT_TYPE_HTML)) {
			return "html";
		} else if (targetType.equals(JasperReportBuilder.PRINT_TYPE_PDF)) {
			return "pdf";
		} else if (targetType.equals(JasperReportBuilder.PRINT_TYPE_RTF)) {
			return "rtf";
		} else if (targetType.equals(JasperReportBuilder.PRINT_TYPE_EXCEL)) {
			return "xls";
		} else {
			
			throw new RuntimeException(res.getString("ReportClazz/jasperReportsController.UnSupportWay"));
		}
	}

	/**
	 * 通过dorado的DataProvider加载报表所需要的数据
	 * 
	 * @param dataProviderName
	 * @param parameters
	 * @return 返回相关数据
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	private Object getDataProviderData(String dataProviderName, Map<String, Object> parameters) throws Exception {
		DataProvider dataProvider = dataProviderManager.getDataProvider(dataProviderName);
		if (parameters == null) {
			return dataProvider.getResult();
		}
		String pageNo = (String) parameters.get("pageIndex");
		String pageSize = (String) parameters.get("pageSize");
		if (StringUtils.hasText(pageSize) && StringUtils.hasText(pageNo)) {
			Page page = new Page(new Integer(pageSize), new Integer(pageNo));
			dataProvider.getPagingResult(parameters, page);
			return page.getEntities();
		} else {
			return dataProvider.getResult(parameters);
		}
	}

	private IReportService reportService;
	private DataProviderManager dataProviderManager;

	public void setReportService(IReportService reportService) {
		this.reportService = reportService;
	}

	public void setDataProviderManager(DataProviderManager dataProviderManager) {
		this.dataProviderManager = dataProviderManager;
	}

	public TempFilePersister getTempFilePersister() {
		return tempFilePersister;
	}

	public void setTempFilePersister(TempFilePersister tempFilePersister) {
		this.tempFilePersister = tempFilePersister;
	}

	public void afterPropertiesSet() throws Exception {
		res = ResourceManagerUtils.get(JasperReportsController.class);
		
	}
}
