/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.d7.excel;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.bstek.dorado.annotation.Expose;
import com.innofi.component.report.d7.online.GenerateModels;
import com.innofi.component.report.domain.UploadInfo;
import com.innofi.component.report.excel.service.ExportExcelFormGridService;
import com.innofi.component.report.excel.service.ExportExcelFormService;
import com.innofi.component.report.excel.service.ExportExcelGridService;
import com.innofi.component.report.model.ReportFormModel;
import com.innofi.component.report.model.ReportGridModel;
import com.innofi.component.report.model.ReportTitleModel;
import com.innofi.component.report.upload.TempFilePersister;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 生成Excel报表的controller类
 * @author matt.yao@bstek.com
 * @since 1.0
 *
 */
public class ExcelReportController {
	private ExportExcelGridService exportExcelGridService;
	private ExportExcelFormService exportExcelFormService;
	private ExportExcelFormGridService exportExcelFormGridService;
	private TempFilePersister tempFilePersister;
	private GenerateModels generator;
	/**
	 * 客户端ajaxAction调用生成Excel Grid报表文件，存放在系统临时目录
	 * @param parameter 客户端参数
	 * @return 返回生成的文件名称
	 * @throws Exception
	 */
	@Expose
	public String buildExcelGridReportData(Map<String, Object> parameter) throws Exception{
		ReportTitleModel reportTitle = generator.generateReportTitle(parameter);
		List<ReportGridModel> reportGridModels = generator.createReportGridModels(parameter);
		String fileName=UUID.randomUUID().toString()+".xls";
		String path=ContextHolder.getIdfTempFileStorePath()+fileName;
		exportExcelGridService.execute(reportTitle,reportGridModels, path, "sheet");
		return this.persistExcelFile(path, fileName);
	}
	
	/**
	 * 客户端ajaxAction调用生成Excel Form报表文件，存放在系统临时目录,<br>
	 * 支持多AutoForm表单同时导出到同一个Sheet页中，显示顺序从上之下，按照客户端调用指定的先后顺序.
	 * @param map 客户端参数
	 * @return 文件名称
	 * @throws Exception
	 */
	@Expose
	public String buildExcelFormReportData(Map<String,Object> map) throws Exception{
		ReportTitleModel reportTitle = generator.generateReportTitle(map);
		List<ReportFormModel> reportFormModels = generator.createReportFormModels(map);
		String fileName=UUID.randomUUID().toString()+".xls";
		String path=ContextHolder.getIdfTempFileStorePath()+fileName;
		exportExcelFormService.execute(reportTitle,reportFormModels, path, "sheet");
		return this.persistExcelFile(path, fileName);
	}
	
	/**
	 * 客户端ajaxAction调用生成Form与Grid混合的报表文件，存放在系统临时目录
	 * @param map 客户端参数
	 * @return 返回生成的文件名称
	 * @throws Exception
	 */
	@Expose
	public String buildFormGridReportData(Map<String, Object> map) throws Exception{
		ReportTitleModel reportTitle = generator.generateReportTitle(map);
		List<ReportFormModel> reportFormModels = generator.createReportFormModels(map);
		List<ReportGridModel> reportGridModels = generator.createReportGridModels(map);
		String fileName=UUID.randomUUID().toString()+".xls";
		String path=ContextHolder.getIdfTempFileStorePath()+fileName;
		exportExcelFormGridService.execute(reportTitle,reportFormModels, reportGridModels, path, "sheet1");
		return this.persistExcelFile(path, fileName);
	}
	
	private String persistExcelFile(String path,String fileName)throws Exception{
		FileInputStream in=new FileInputStream(path);
		UploadInfo info=new UploadInfo();
		info.setFileName(fileName);
		fileName=tempFilePersister.persist(in, info);
		if(in!=null)
			in.close();
		return fileName;
	}
	
	public ExportExcelGridService getExportExcelGridService() {
		return exportExcelGridService;
	}
	public void setExportExcelGridService(
			ExportExcelGridService exportExcelGridService) {
		this.exportExcelGridService = exportExcelGridService;
	}
	public ExportExcelFormService getExportExcelFormService() {
		return exportExcelFormService;
	}
	public void setExportExcelFormService(
			ExportExcelFormService exportExcelFormService) {
		this.exportExcelFormService = exportExcelFormService;
	}
	public ExportExcelFormGridService getExportExcelFormGridService() {
		return exportExcelFormGridService;
	}
	public void setExportExcelFormGridService(
			ExportExcelFormGridService exportExcelFormGridService) {
		this.exportExcelFormGridService = exportExcelFormGridService;
	}
	public TempFilePersister getTempFilePersister() {
		return tempFilePersister;
	}
	public void setTempFilePersister(TempFilePersister tempFilePersister) {
		this.tempFilePersister = tempFilePersister;
	}

	public GenerateModels getGenerator() {
		return generator;
	}

	public void setGenerator(GenerateModels generator) {
		this.generator = generator;
	}
}
