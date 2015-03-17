package com.innofi.component.report.pdf.service;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import com.innofi.component.report.model.ReportFormModel;
import com.innofi.component.report.model.ReportTitleModel;
import com.innofi.component.report.pdf.model.ReportDataModel;

/**
 * PDF报表文件生成,报表的内容是Form和Grid的混合内容,<br>
 * 支持多个AutoForm和Grid的同时生成在一个报表中
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public interface PdfFormGridReportService {
	
	/**
	 * Spring Bean ID
	 */
	public static final String SERVICE_ID="bdf.pdfFormGridReportService";

	/**
	 * 
	 * @param reportTitle 报表标题
	 * @param reportFormModels Form数据模型
	 * @param gridDataModels Grid数据模型
	 * @param out 输出流
	 * @throws Exception
	 */
	public void generateFormGridReport(ReportTitleModel reportTitle,
			Collection<ReportFormModel> reportFormModels,
			List<ReportDataModel> gridDataModels, OutputStream out)
			throws Exception;
}
