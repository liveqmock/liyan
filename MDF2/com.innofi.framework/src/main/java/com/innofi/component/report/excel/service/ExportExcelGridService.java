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
import com.innofi.component.report.model.ReportGridModel;
import com.innofi.component.report.model.ReportTitleModel;

/**
 * Excel导出grid
 * @author matt.yao@bstek.com
 * @since 1.0
 */
public interface ExportExcelGridService {
	
	public static final String SPRING_ID = "bdf.exportExcelGridService";
	/**
	 * excel grid 导出操作简单模式
	 * @param reportTitle
	 * @param exportDataWrappers 结果集
	 * @param fileName 文件名
	 * @param excelSheetName  sheet页名称
	 * @throws Exception
	 */
	public  void execute(ReportTitleModel reportTitle,ExportDataWrapper exportDataWrappers,String fileName,String excelSheetName)throws Exception;
    /**
     * Excel grid 导出操作复杂模式
     * @param reportTitle
     * @param reportGridModels
     * @param fileName
     * @param excelSheetName
     * @throws Exception
     */
    public void  execute(ReportTitleModel reportTitle,List<ReportGridModel> reportGridModels,String fileName,String excelSheetName) throws Exception;
  
}
