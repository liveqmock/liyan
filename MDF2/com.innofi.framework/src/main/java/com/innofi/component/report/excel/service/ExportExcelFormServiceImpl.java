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
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.innofi.component.report.excel.builder.ExcelBuilder;
import com.innofi.component.report.model.ReportFormModel;
import com.innofi.component.report.model.ReportTitleModel;

/**
 * Excel导出form表单实现类
 * @author matt.yao@bstek.com
 * @since 1.0
 *
 */
public class ExportExcelFormServiceImpl implements ExportExcelFormService {

	/*
	 * (non-Javadoc)
	 * @see com.bstek.bdf.report.excel.service.ExportExcelFormService#execute(com.bstek.bdf.report.online.model.ReportTitle, com.bstek.bdf.report.excel.domain.form.FormExcelModel, java.lang.String, java.lang.String)
	 */
	public void execute(ReportTitleModel reportTitle, ReportFormModel reportFormModel, String fileName,
			String sheetName) throws Exception {
		
		List<ReportFormModel> reportFormModels = new ArrayList<ReportFormModel>();
		reportFormModels.add(reportFormModel);
		
		ExcelBuilder excelBuilder = new ExcelBuilder();
		
		Workbook workbook = excelBuilder.createWorkBook(fileName) ;
		Sheet sheet = excelBuilder.createSheet(workbook, sheetName);
		
		int nextRowNo = excelBuilder.addExcelModelTitleToSheet(reportTitle, workbook, sheet, reportFormModel.getColumnCount(),"2");
		nextRowNo = excelBuilder.addFormToSheet(reportFormModels, workbook, sheet, nextRowNo);
		
		outputFile(fileName, workbook);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bstek.bdf.report.excel.service.ExportExcelFormService#execute(com.bstek.bdf.report.online.model.ReportTitle, java.util.List, java.lang.String, java.lang.String)
	 */
	public void execute(ReportTitleModel reportTitle, List<ReportFormModel> reportFormModels, String fileName,
			String sheetName) throws Exception {
		ExcelBuilder excelBuilder = new ExcelBuilder();
		
		Workbook workbook = excelBuilder.createWorkBook(fileName) ;
		Sheet sheet = excelBuilder.createSheet(workbook, sheetName);
		
		int nextRowNo = excelBuilder.addExcelModelTitleToSheet(reportTitle, workbook, sheet, getFormCount(reportFormModels),"2");
		nextRowNo = excelBuilder.addFormToSheet(reportFormModels, workbook, sheet, nextRowNo);
		
		outputFile(fileName, workbook);
	}
	
	/**
	 * 获取多个form列最大值
	 * @param reportFormModels
	 * @return
	 */
	private int getFormCount(List<ReportFormModel> reportFormModels) {
		int count = 1;
		if (null != reportFormModels && reportFormModels.size() > 0) {
			for (ReportFormModel reportFormModel : reportFormModels) {
				int columnCount = reportFormModel.getColumnCount();
				if (count < columnCount) {
					count = columnCount;
				}
			}
		}
		return count;
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
