/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.service;

import java.util.List;

import com.innofi.component.report.excel.domain.ExportDataWrapper;
import com.innofi.component.report.model.ReportFormModel;
import com.innofi.component.report.model.ReportGridModel;
import com.innofi.component.report.model.ReportTitleModel;

/**
 * Excel导出form,grid
 * @author tode.yu@bstek.com
 * @since 1.0
 */
public interface ExportExcelFormGridService {
	
	public static final String SPRING_ID = "bdf.exportExcelFormGridService";

	/**
	 * excel grid 导出操作简单模式
	 * @param reportTitle
	 * @param reportFormModels
	 * @param exportDataWrapper
	 * @param fileName
	 * @param excelSheetName
	 * @throws Exception
	 */
	public  void execute(ReportTitleModel reportTitle,List<ReportFormModel> reportFormModels,ExportDataWrapper exportDataWrapper,String fileName,String excelSheetName)throws Exception;
   
	/**
	 * Excel grid 导出操作复杂模式
	 * @param reportTitle
	 * @param reportFormModels
	 * @param reportGridModels
	 * @param fileName
	 * @param excelSheetName
	 * @throws Exception
	 */
    public void  execute(ReportTitleModel reportTitle,List<ReportFormModel> reportFormModels,List<ReportGridModel> reportGridModels,String fileName,String excelSheetName) throws Exception;
  
}
