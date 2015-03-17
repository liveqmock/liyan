package com.innofi.component.report.pdf.service;

import java.io.OutputStream;
import java.util.Collection;

import com.innofi.component.report.model.ReportFormModel;
import com.innofi.component.report.model.ReportTitleModel;

/**
 * 生成PDF报表,报表内容是Form表单中的数据,报表的展现形式由Form表单决定
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public interface PdfFormReportService {
	
	/**
	 * Spring Bean ID
	 */
	public static final String SERVICE_ID="bdf.pdfFormReportService";
	
	/**
	 * @param reportTitle 报表标题
	 * @param reportFormModels Form表单数据模型
	 * @param out 生成文件输出目的地
	 * @throws Exception
	 */
	public void generateFormReport(ReportTitleModel reportTitle,Collection<ReportFormModel> reportFormModels, OutputStream out)throws Exception;

}
