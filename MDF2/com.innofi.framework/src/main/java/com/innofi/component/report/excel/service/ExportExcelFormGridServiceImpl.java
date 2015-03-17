/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.Assert;

import com.innofi.component.report.excel.builder.ExcelBuilder;
import com.innofi.component.report.excel.domain.ExportDataWrapper;
import com.innofi.component.report.model.ReportFormModel;
import com.innofi.component.report.model.ReportGridModel;
import com.innofi.component.report.model.ReportTitleModel;

/**
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ExportExcelFormGridServiceImpl implements
		ExportExcelFormGridService {

	/*
	 * (non-Javadoc)
	 * @see com.bstek.bdf.report.excel.service.ExportExcelFormGridService#execute(com.bstek.bdf.report.online.model.ReportTitle, java.util.List, com.bstek.bdf.report.excel.domain.ExportDataWrapper, java.lang.String, java.lang.String)
	 */
	public void execute(ReportTitleModel reportTitle,List<ReportFormModel> reportFormModels,ExportDataWrapper exportDataWrapper, String fileName,String sheetName) throws Exception {
		Assert.notNull(exportDataWrapper, "exportDataWrapper 对象不能为空");
		Assert.notNull(fileName, "文件名不能为空");
		Assert.notNull(sheetName, "sheet name 不能为空");
		
		ExcelBuilder excelBuilder = new ExcelBuilder();
		
		Workbook workbook = excelBuilder.createWorkBook(fileName) ;
		Sheet sheet = excelBuilder.createSheet(workbook, sheetName);
		
		int nextRowNo = excelBuilder.addExcelModelTitleToSheet(reportTitle, workbook, sheet, 0,"3");
		nextRowNo = excelBuilder.addFormToSheet(reportFormModels, workbook, sheet, nextRowNo);
		nextRowNo = excelBuilder.addSimpleGridToSheet(exportDataWrapper, workbook, sheet, nextRowNo);
		
		outputFile(fileName, workbook);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bstek.bdf.report.excel.service.ExportExcelFormGridService#execute(com.bstek.bdf.report.online.model.ReportTitle, java.util.List, java.util.List, java.lang.String, java.lang.String)
	 */
	public void execute(ReportTitleModel reportTitle,List<ReportFormModel> reportFormModels,
			List<ReportGridModel> reportGridModels, String fileName, String sheetName)
			throws Exception {
		ExcelBuilder excelBuilder = new ExcelBuilder();
		
		Workbook workbook = excelBuilder.createWorkBook(fileName) ;
		Sheet sheet = excelBuilder.createSheet(workbook, sheetName);
		
		int nextRowNo = excelBuilder.addExcelModelTitleToSheet(reportTitle, workbook, sheet, 1,"3");
		nextRowNo = excelBuilder.addFormToSheet(reportFormModels, workbook, sheet, nextRowNo);
		nextRowNo = excelBuilder.addComplexGridToSheet(reportGridModels, workbook, sheet, nextRowNo);
		
		outputFile(fileName, workbook);
	}
	
	/**
	 * 输出文件
	 * @param fileName
	 * @param workbook
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void outputFile(String fileName, Workbook workbook)
			throws FileNotFoundException, IOException {
		File file = new File(fileName);
		FileOutputStream out = new FileOutputStream(file);
		workbook.write(out);
		out.close();
	}
}
