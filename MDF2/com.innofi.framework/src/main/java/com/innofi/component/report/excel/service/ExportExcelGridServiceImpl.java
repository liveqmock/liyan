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
import com.innofi.component.report.model.ReportGridHeaderModel;
import com.innofi.component.report.model.ReportGridModel;
import com.innofi.component.report.model.ReportTitleModel;

/**
 * Excel导出Grid实现类，
 * @author matt.yao@bstek.com
 * @since 1.0
 */
public class ExportExcelGridServiceImpl implements ExportExcelGridService {

	/*
	 * (non-Javadoc)
	 * @see com.bstek.bdf.report.excel.service.ExportExcelGridService#execute(com.bstek.bdf.report.online.model.ReportTitle, com.bstek.bdf.report.excel.domain.ExportDataWrapper, java.lang.String, java.lang.String)
	 */
	public void execute(ReportTitleModel reportTitle, ExportDataWrapper exportDataWrapper,String fileName,String sheetName)
			throws Exception {
		Assert.notNull(fileName, "文件名不能为空");
		Assert.notNull(sheetName, "sheet name 不能为空");
		
		ExcelBuilder excelBuilder = new ExcelBuilder();
		
		Workbook workbook = excelBuilder.createWorkBook(fileName) ;
		Sheet sheet = excelBuilder.createSheet(workbook, sheetName);
		
		int nextRowNo = excelBuilder.addExcelModelTitleToSheet(reportTitle, workbook, sheet, 0,"1");
		nextRowNo = excelBuilder.addSimpleGridToSheet(exportDataWrapper, workbook, sheet, nextRowNo);
		
		outputFile(fileName, workbook);

	}

	/*
	 * (non-Javadoc)
	 * @see com.bstek.bdf.report.excel.service.ExportExcelGridService#execute(com.bstek.bdf.report.online.model.ReportTitle, com.bstek.bdf.report.excel.domain.grid.GridExcelModel, java.lang.String, java.lang.String)
	 */
	public void execute(ReportTitleModel reportTitle,List<ReportGridModel> reportGridModels, String fileName,
			String sheetName) throws Exception {
		ExcelBuilder excelBuilder = new ExcelBuilder();
		
		Workbook workbook = excelBuilder.createWorkBook(fileName) ;
		Sheet sheet = excelBuilder.createSheet(workbook, sheetName);
		
		int nextRowNo = excelBuilder.addExcelModelTitleToSheet(reportTitle, workbook, sheet, getGridCount(reportGridModels),"1");
		nextRowNo = excelBuilder.addComplexGridToSheet(reportGridModels, workbook, sheet, nextRowNo);
		
		this.outputFile(fileName, workbook);
	}
	
	/**
	 * 获取多个form列最大值
	 * 
	 * @param reportFormModels
	 * @return
	 */
	private int getGridCount(List<ReportGridModel> reportGridModels) {
		int count = 1;
		if (null != reportGridModels && reportGridModels.size() > 0) {
			for (ReportGridModel reportGridModel : reportGridModels) {
				List<ReportGridHeaderModel> gridList = reportGridModel.getGridHeaderModelList();
				if (null != gridList && gridList.size() > 0) {
					int columnCount = gridList.size();
					if (count < columnCount) {
						count = columnCount;
					}
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
