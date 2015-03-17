/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.pdf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.innofi.component.report.model.ReportFormDataModel;
import com.innofi.component.report.model.ReportTitleModel;
import com.innofi.component.report.pdf.model.ReportDataModel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public abstract class AbstractReportFactory{
	private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected BaseFont chineseFont;
	protected AbstractReportFactory() throws Exception{			
		chineseFont=BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
	}
	protected Chunk createChunk(TextChunk textChunk){
		Chunk chunk=new Chunk(textChunk.getText());
		chunk.setFont(createFont(textChunk));
		return chunk;
	}
	protected Font createFont(TextChunk textChunk) {
		Font font=new Font(chineseFont);
		if(textChunk.getFontColor()!=null){
			int[] colors=textChunk.getFontColor();
			font.setColor(new BaseColor(colors[0],colors[1],colors[2]));
		}
		if(textChunk.getFontSize()>0){
			font.setSize(textChunk.getFontSize());
		}
		font.setStyle(textChunk.getFontStyle());
		return font;
	}
	protected Paragraph createParagraph(TextChunk textChunk){
		Paragraph paragraph=new Paragraph(textChunk.getText(),createFont(textChunk));
		paragraph.setAlignment(textChunk.getAlign());
		return paragraph;
	}
	
	protected Paragraph createReportTitle(ReportTitleModel reportTitle){
		Paragraph paragraph=new Paragraph();
		paragraph.setAlignment(Element.ALIGN_CENTER);
		if(reportTitle!=null && reportTitle.isShowTitle()){
			TextChunk titleChunk=new TextChunk();
			titleChunk.setText(reportTitle.getTitle());
			titleChunk.setFontSize(reportTitle.getStyle().getFontSize());
			titleChunk.setFontColor(reportTitle.getStyle().getFontColor());
			paragraph.add(createChunk(titleChunk));
			
			paragraph.add(Chunk.NEWLINE);
			paragraph.add(Chunk.NEWLINE);
		}
		return paragraph;
	}
	
	protected PdfPTable createFormContentTable(Collection<ReportFormDataModel> datas,int columnCount,boolean showBorder) throws Exception{
		PdfPTable table=new PdfPTable(columnCount*2);
		table.setWidthPercentage(100);
		for(ReportFormDataModel data:datas){
			
			Object value = data.getData();
			String result="";
			if(value instanceof Date){
				result=simpleDateFormat.format((Date)value);
				if(result.endsWith("00:00:00")){
					result=result.substring(0,11);
				}
			}else if(value!=null){
				result=value.toString();
			}
			TextChunk text=new TextChunk();
			text.setText(result);
			text.setAlign(data.getDataAlign());
			text.setFontStyle(data.getDataStyle());
			
			LabelData labelData=new LabelData();
			labelData.setAlign(data.getLabelAlign());
			labelData.setText(data.getLabel());
			
			PdfPCell labelCell=new PdfPCell();
			labelCell.addElement(this.createParagraph(labelData));
			labelCell.setHorizontalAlignment(labelData.getAlign());
			labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			
			if(!showBorder){
				labelCell.setBorderWidth(0);
			}
			table.addCell(labelCell);
			int colSpan=data.getColSpan()*2;
			PdfPCell valueCell=new PdfPCell();
			valueCell.setColspan(colSpan-1);
			valueCell.setHorizontalAlignment(data.getDataAlign());
			valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			valueCell.addElement(this.createParagraph(text));
			if(!showBorder){
				valueCell.setBorderWidth(0);
			}
			table.addCell(valueCell);
		}
		return table;
	}
	
	protected PdfPTable createGridTable(ReportDataModel dataModel,boolean isRepeatHeader) throws Exception{
		PdfPTable table=new PdfPTable(calculateGridColumnCount(dataModel.getTopColumnHeaders()));
		table.setWidthPercentage(100);
		Collection<ColumnHeader> topHeaders=dataModel.getTopColumnHeaders();
		List<Integer> widths=new ArrayList<Integer>();
		generateGridColumnWidths(topHeaders,widths);
		int[] values=new int[widths.size()];
		for(int i=0;i<widths.size();i++){
			values[i]=widths.get(i);
		}
		table.setWidths(values);
		int maxHeaderLevel=getGridMaxColumngroup(topHeaders);
		createGridColumnHeader(table,topHeaders,maxHeaderLevel);
		createGridTableDatas(table,dataModel.getReportData());
		if(isRepeatHeader){
			table.setHeaderRows(maxHeaderLevel);
		}
		return table;
	}
	
	private void createGridColumnHeader(PdfPTable table,Collection<ColumnHeader> topHeaders,int maxHeaderLevel) throws Exception{
		for(int i=1;i<50;i++){
			List<ColumnHeader> result=new ArrayList<ColumnHeader>();
			generateGridHeadersByLevel(topHeaders,i,result);
			for(ColumnHeader header:result){
				PdfPCell cell=new PdfPCell(createParagraph(header));
				if(header.getBgColor()!=null){
					int[] colors=header.getBgColor();
					cell.setBackgroundColor(new BaseColor(colors[0],colors[1],colors[2]));
				}
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(header.getAlign());
				cell.setColspan(header.getColspan());
				if(header.getColumnHeaders().size()==0){
					int rowspan=maxHeaderLevel-(header.getLevel()-1);
					if(rowspan>0){
						cell.setRowspan(rowspan);				
					}				
				}
				table.addCell(cell);
			}
		}
	}
	
	private void generateGridHeadersByLevel(Collection<ColumnHeader> topHeaders,int level,List<ColumnHeader> result){
		for(ColumnHeader header:topHeaders){
			if(header.getLevel()==level){
				result.add(header);
			}
			generateGridHeadersByLevel(header.getColumnHeaders(),level,result);
		}
	}
	
	
	private void generateGridColumnWidths(Collection<ColumnHeader> topHeaders,List<Integer> widths){
		for(ColumnHeader header:topHeaders){
			Collection<ColumnHeader> children=header.getColumnHeaders();
			if(children.size()==0){
				widths.add(header.getWidth());				
			}
			generateGridColumnWidths(children,widths);
		}
	}
	
	private void createGridTableDatas(PdfPTable table,Collection<ReportData> datas){
		for(ReportData data:datas){
			PdfPCell cell=new PdfPCell(createParagraph(data.getTextChunk()));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			int level = this.calculateIndentationCount(data.getTextChunk().getText());
			if(level==0){
				cell.setHorizontalAlignment(data.getAlign());
			}else{
				cell.setIndent(20*level);
			}
			table.addCell(cell);
		}
	}
	
	
	private int getGridMaxColumngroup(Collection<ColumnHeader> topHeaders){
		int max=1;
		for(int i=1;i<50;i++){
			List<ColumnHeader> result=new ArrayList<ColumnHeader>();
			generateGridHeadersByLevel(topHeaders,i,result);
			if(result.size()==0){
				max=i-1;
				break;
			}
		}
		return max;
	}
	
	private int calculateGridColumnCount(Collection<ColumnHeader> topHeaders){
		int i=0;
		for(ColumnHeader header:topHeaders){
			i++;
			i+=(header.getColspan()-1);
		}
		return i;
	}
	private int calculateIndentationCount(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			char temp = s.charAt(i);
			if (temp == '\u0009') {
				count++;
			}
		}
		return count;
	}
	
}
