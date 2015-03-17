package com.innofi.component.report.pdf.service.impl;

import java.io.OutputStream;
import java.util.Collection;

import com.innofi.component.report.model.ReportTitleModel;
import com.innofi.component.report.pdf.AbstractReportFactory;
import com.innofi.component.report.pdf.PdfReportPageNumber;
import com.innofi.component.report.pdf.model.ReportDataModel;
import com.innofi.component.report.pdf.service.PdfGridReportService;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class PdfGridReportServiceImpl extends AbstractReportFactory implements
		PdfGridReportService {

	public PdfGridReportServiceImpl() throws Exception {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.bstek.bdf.report.pdf.service.PdfGridReportService#generateGridReport(com.bstek.bdf.report.model.ReportTitleModel, java.util.Collection, java.io.OutputStream)
	 */
	public void generateGridReport(ReportTitleModel reportTitle,
			Collection<ReportDataModel> reportDataModels, OutputStream out)
			throws Exception {
		Document doc=new Document();
		PdfWriter writer=PdfWriter.getInstance(doc,out);
		if(reportTitle.isShowPageNo()){
			PdfReportPageNumber event=new PdfReportPageNumber(chineseFont);
			writer.setPageEvent(event);			
		}
		doc.open();
		Paragraph paragraph = this.createReportTitle(reportTitle);
		
		for(ReportDataModel dataModel : reportDataModels){
			paragraph.add(createGridTable(dataModel,reportTitle.isRepeatHeader()));
			paragraph.add(Chunk.NEWLINE);
		}
		doc.add(paragraph);
		doc.close();
	}

}
