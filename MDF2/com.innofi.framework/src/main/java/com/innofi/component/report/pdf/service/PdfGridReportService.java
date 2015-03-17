package com.innofi.component.report.pdf.service;

import java.io.OutputStream;
import java.util.Collection;

import com.innofi.component.report.model.ReportTitleModel;
import com.innofi.component.report.pdf.model.ReportDataModel;

/**
 * 生成PDF报表,报表内容是Grid表格
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public interface PdfGridReportService {

	/**
	 * Spring Bean ID
	 */
	public static final String SERVICE_ID="bdf.pdfGridReportService";
	
	/**
	 * 
	 * @param reportTitle 报表标题
	 * @param reportDataModels 数据
	 * @param out 输出流
	 * @throws Exception
	 */
	public void generateGridReport(ReportTitleModel reportTitle,Collection<ReportDataModel> reportDataModels, OutputStream out)throws Exception;
}
