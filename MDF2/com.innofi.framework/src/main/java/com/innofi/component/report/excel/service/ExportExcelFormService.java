/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.service;

import java.util.List;

import com.innofi.component.report.model.ReportFormModel;
import com.innofi.component.report.model.ReportTitleModel;

/**
 * Excel导出form表单
 * @author matt.yao@bstek.com
 * @since 1.0
 *
 */
public interface ExportExcelFormService {
	
	public static final String SPRING_ID = "bdf.exportExcelFormService";
	
	/**
	 * 导出excel表单
	 * @param reportTitle
	 * @param reportFormModel form表单数据模型
	 * @param fileName 文件名称
	 * @param excelSheetName sheet页名称
	 * @throws Exception
	 */
	public void execute(ReportTitleModel reportTitle, ReportFormModel reportFormModel,String fileName,String excelSheetName) throws Exception;
	
	/**导出excel表单
	 * @param reportTitle
	 * @param reportFormModels  form表单数据模型
	 * @param fileName   文件名称
	 * @param excelSheetName excel sheet页名称
	 * @throws Exception
	 */
	public void execute(ReportTitleModel reportTitle,List<ReportFormModel> reportFormModels,String fileName,String excelSheetName) throws Exception;
}
