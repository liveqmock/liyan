/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.pdf;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import com.innofi.component.report.model.ReportFormModel;
import com.innofi.component.report.model.ReportTitleModel;
import com.innofi.component.report.pdf.model.ReportDataModel;

/**
 * 用于根据dorado7页面组件信息生成报表的Factory接口类
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public interface ReportFactory {
	/**
	 * 根据给定的报表数据以及输出流生成对应的报表
	 * @param dataModel ReportDataModel对象实例
	 * @param out 生成报表文件的输出流对象
	 * @throws Exception
	 */
	public abstract void generateReport(ReportDataModel dataModel,
			OutputStream out) throws Exception;
	
	public void generateFormReport(ReportTitleModel reportTitle,Collection<ReportFormModel> reportFormModels, OutputStream out)throws Exception;
	
	public void generateGridReport(ReportTitleModel reportTitle,Collection<ReportDataModel> reportFormModels, OutputStream out)throws Exception;
	
	public void generateFormGridReport(ReportTitleModel reportTitle,Collection<ReportFormModel> reportFormModels,List<ReportDataModel> gridDataModels, OutputStream out)throws Exception;

}