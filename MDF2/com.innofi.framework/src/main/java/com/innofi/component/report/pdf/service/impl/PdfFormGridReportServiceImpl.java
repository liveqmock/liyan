package com.innofi.component.report.pdf.service.impl;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import com.innofi.component.report.model.ReportFormModel;
import com.innofi.component.report.model.ReportTitleModel;
import com.innofi.component.report.pdf.AbstractReportFactory;
import com.innofi.component.report.pdf.PdfReportPageNumber;
import com.innofi.component.report.pdf.model.ReportDataModel;
import com.innofi.component.report.pdf.service.PdfFormGridReportService;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class PdfFormGridReportServiceImpl extends AbstractReportFactory implements
		PdfFormGridReportService {

	public PdfFormGridReportServiceImpl() throws Exception {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.bstek.bdf.report.pdf.service.PdfFormGridReportService#generateFormGridReport(com.bstek.bdf.report.model.ReportTitleModel, java.util.Collection, java.util.List, java.io.OutputStream)
	 */
	public void generateFormGridReport(ReportTitleModel reportTitle,
			Collection<ReportFormModel> reportFormModels,
			List<ReportDataModel> gridDataModels, OutputStream out)
			throws Exception {
		Document doc=new Document();
		PdfWriter writer=PdfWriter.getInstance(doc,out);
		if(reportTitle.isShowPageNo()){
			PdfReportPageNumber event=new PdfReportPageNumber(chineseFont);
			writer.setPageEvent(event);			
		}
		doc.open();
		doc.add(this.createReportTitle(reportTitle));
		
		for(ReportFormModel dataModel : reportFormModels){
			doc.add(this.createFormContentTable(dataModel.getListReportFormDataModel(), dataModel.getColumnCount(),reportTitle.isShowBorder()));
			doc.add(Chunk.NEWLINE);
		}
		
		for(ReportDataModel dataModel : gridDataModels){
			doc.add(createGridTable(dataModel,reportTitle.isRepeatHeader()));
			doc.add(Chunk.NEWLINE);
		}
		doc.close();

	}

}
