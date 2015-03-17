/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.builder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import com.innofi.component.report.excel.domain.ExportDataWrapper;
import com.innofi.component.report.model.ReportFormDataModel;
import com.innofi.component.report.model.ReportFormModel;
import com.innofi.component.report.model.ReportGridDataModel;
import com.innofi.component.report.model.ReportGridHeaderModel;
import com.innofi.component.report.model.ReportGridModel;
import com.innofi.component.report.model.ReportTitleModel;
import com.innofi.component.report.model.TitleStyle;

/**
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ExcelBuilder {
	
	public static final String DefaultSheetName = "Sheet1";
	
	/**
	 * 创建Excel文件
	 * @param fileName
	 * @return 返回打开的Workbook对象
	 */
	public Workbook createWorkBook(String fileName){
		if(fileName.endsWith(".xlsx")){
			return new XSSFWorkbook();
		}else if(fileName.endsWith(".xls")){
			return new HSSFWorkbook();
		}else {
			throw new IllegalArgumentException("文件名称错误");
		}
	}
	
	/**
	 * 在指定的Workbook中添加一个Sheet页，若指定Sheet页名称即sheetName为null，<br>
	 * 则默认名称为Sheet1
	 * @param workbook
	 * @param sheetName
	 * @return 返回指定的Sheet对象
	 */
	public Sheet createSheet(Workbook workbook,String sheetName){
		if (StringUtils.hasText(sheetName)) {
			return workbook.createSheet(sheetName);
		} else {
			return workbook.createSheet("Sheet1");
		}
	}
	
	/**
	 * 添加Excel报表的标题信息
	 * @param reportTitle
	 * @param workbook
	 * @param sheet
	 * @param columnCount
	 * @return 返回1
	 */
	public Integer addExcelModelTitleToSheet(ReportTitleModel reportTitle,Workbook workbook,Sheet sheet,Integer columnCount,String flag){
		if(!reportTitle.isShowTitle())
			return 0;
		Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleRow.setHeightInPoints(40);
        titleCell.setCellValue(reportTitle.getTitle());
        
        CellStyle titleStyle = this.createExcelModelTitleStyle(reportTitle, workbook);
        titleCell.setCellStyle(titleStyle);
        
        //grid
        if("1".equals(flag)) {
        	sheet.addMergedRegion(new CellRangeAddress(0,0, 0,columnCount-1));
        } else {
        	sheet.addMergedRegion(new CellRangeAddress(0,0, 0,columnCount*2-1));
        }
		
		return 1;
	}
	
	/**
	 * 添加Form到指定的Sheet页中
	 * @param reportFormModels
	 * @param workbook
	 * @param sheet
	 * @param rowNo
	 * @return 返回row no
	 * @throws Exception
	 */
	public Integer addFormToSheet(List<ReportFormModel> reportFormModels,Workbook workbook,Sheet sheet,int rowNo) throws Exception {
		for(ReportFormModel reportFormModel : reportFormModels){
			rowNo = createCellWrapperData(rowNo,workbook,sheet,reportFormModel);
			rowNo += 2;
		}
		return rowNo;
	}
	
	/**
	 * 添加Grid到指定的Sheet页中
	 * @param reportGridModels
	 * @param workbook
	 * @param sheet
	 * @param initRowNo
	 * @return 返回row no
	 */
	public Integer addComplexGridToSheet(List<ReportGridModel> reportGridModels,Workbook workbook,Sheet sheet,int initRowNo){
		int rowNo = initRowNo;
		for(ReportGridModel gridModel : reportGridModels){
			List<ReportGridHeaderModel> tempGridExcelHeader= new ArrayList<ReportGridHeaderModel>();
			
			this.calculateMaxHeaderLevel(gridModel,gridModel.getGridHeaderModelList());
			this.calculateColumnCount(tempGridExcelHeader, gridModel.getGridHeaderModelList());
			
			gridModel.setColumnCount(tempGridExcelHeader.size());
			List<Map<String, Object>> excelDatas = gridModel.getGridDataModel().getDatas();
			
			Map<String, CellStyle> styles = createStyles(workbook,gridModel);
			
			int rowSize = excelDatas.size();
			int starHeaderRow = rowNo;
			int starDataRow = gridModel.getMaxHeaderLevel() + rowNo;
			
			this.createGridExcelHeader(sheet, gridModel, starHeaderRow, gridModel.getMaxHeaderLevel(),tempGridExcelHeader, styles.get("header"));
			this.createGridExcelData(sheet, gridModel, starDataRow, rowSize,styles.get("data"),styles.get("date"));
			
			rowNo = rowNo + rowSize + 2;
		}
		return rowNo;
	}
	
	/**
	 * 添加简单Grid到指定的Sheet页中
	 * @param exportDataWrapper
	 * @param workbook
	 * @param sheet
	 * @param startRowNo
	 * @return 返回数量
	 */
	public Integer addSimpleGridToSheet(ExportDataWrapper exportDataWrapper,Workbook workbook,Sheet sheet,int startRowNo){
		List<String> excelTitle = exportDataWrapper.getExcelTitle();
		List<Map<String, Object>> excelDatas = exportDataWrapper.getExcelData();
		
		CreationHelper helper = workbook.getCreationHelper();
		CellStyle titleStyle = workbook.createCellStyle();
		CellStyle dateStyle = workbook.createCellStyle();
		Font titleFont = workbook.createFont();
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		titleStyle.setFont(titleFont);
		dateStyle.setDataFormat(helper.createDataFormat().getFormat(
				"m/d/yy h:mm"));
		dateStyle.setAlignment(CellStyle.ALIGN_CENTER);
		
		int rowSize = excelDatas.size()+startRowNo;
		Row row;
		Cell cell;
		for (int rowNum = startRowNo; rowNum <= rowSize; rowNum++) {
			row = sheet.createRow(rowNum);
			if (rowNum == 0) {
				int i = 0;
				for (String title : excelTitle) {
					cell = row.createCell(i);
					cell.setCellStyle(titleStyle);
					cell.setCellValue(helper.createRichTextString(title));
					sheet.setColumnWidth(cell.getColumnIndex(), 20 * 256);
					i++;
				}
			} else {
				Map<String, Object> map = excelDatas.get(rowNum - 1);
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					Object value = entry.getValue();
					if (excelTitle.contains(entry.getKey())) {
						cell = row.createCell(excelTitle.indexOf(entry.getKey()));
						cell.setCellStyle(dateStyle);
						if (value != null) {
							if (value instanceof Date) {
								cell.setCellValue((Date) value);
							} else {
								cell.setCellValue(value.toString());
							}
						} else {
							cell.setCellValue("");
						}
						sheet.setColumnWidth(cell.getColumnIndex(), 20 * 256);
					}
				}
			}
		}
		return rowSize + 1;
	}
	
	private Integer createCellWrapperData(int dataRow,Workbook workbook,Sheet sheet,ReportFormModel reportFormModel)throws Exception{
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		List<ReportFormDataModel> formExcelDataList = reportFormModel.getListReportFormDataModel(); 
		int columnCount=reportFormModel.getColumnCount();
		int currentRow=dataRow;
		int currentColumn=0;
		int i=1;
		CellStyle labelCellStyle= createFormMapStyle(reportFormModel, workbook).get("label");
		CellStyle dataCellStyle= createFormMapStyle(reportFormModel, workbook).get("data");
		Row row=sheet.createRow(currentRow);;
		for(ReportFormDataModel reportFormDataModel:formExcelDataList){
			String label=reportFormDataModel.getLabel();
			Object data=reportFormDataModel.getData();
			if(data instanceof Date){
				String result=sdf.format(data);
				if(result.endsWith("00:00:00")){
					data=result.substring(0,11);
				}
			}
			int colSpan=reportFormDataModel.getColSpan();
			if(data==null)data="";
			if(i>columnCount){
				currentRow=currentRow+1;
				i=1;
				currentColumn=0;
				row=sheet.createRow(currentRow);
				Cell cellLabel=row.createCell(currentColumn);
				cellLabel.setCellValue(label);
				cellLabel.setCellStyle(labelCellStyle);
				sheet.setColumnWidth(cellLabel.getColumnIndex(), 30 * 256);
				currentColumn++;
				Cell cellData=row.createCell(currentColumn);
				cellData.setCellStyle(dataCellStyle);
				cellData.setCellValue(data.toString());
				sheet.setColumnWidth(cellData.getColumnIndex(), 30 * 256);
				if(colSpan>1){
					CellRangeAddress ca = new CellRangeAddress(currentRow,currentRow,currentColumn,colSpan*2-1);
					sheet.addMergedRegion(ca);
					this.setFormRegionStyle(sheet, ca, dataCellStyle);
					i++;
				}
				currentColumn++;				
			}else{
				Cell cellLabel=row.createCell(currentColumn);
				cellLabel.setCellValue(label);
				cellLabel.setCellStyle(labelCellStyle);
				sheet.setColumnWidth(cellLabel.getColumnIndex(), 30 * 256);
				currentColumn++;
				Cell cellData=row.createCell(currentColumn);
				cellData.setCellStyle(dataCellStyle);
				cellData.setCellValue(data.toString());
				sheet.setColumnWidth(cellData.getColumnIndex(), 30 * 256);
				if(colSpan>1){
					CellRangeAddress ca = new CellRangeAddress(currentRow,currentRow,currentColumn,colSpan*2-1);
					sheet.addMergedRegion(ca);
					this.setFormRegionStyle(sheet, ca, dataCellStyle);
					i++;
				}
				currentColumn++;
			}
			i++;	
		}
		return currentRow;
	}
	
	private HSSFCellStyle createExcelModelTitleStyle(ReportTitleModel reportTitle,Workbook wb){
		
		if(reportTitle.getStyle() == null)
			return null;
		TitleStyle  style = reportTitle.getStyle();
		
		int[] bgColor = style.getBgColor();
		int[] fontColor = style.getFontColor();
		int fontSize = style.getFontSize();
		if (wb instanceof HSSFWorkbook) {
			HSSFWorkbook workbook = (HSSFWorkbook) wb;
			HSSFPalette palette = workbook.getCustomPalette();
			palette.setColorAtIndex((short) 9, (byte) fontColor[0],
					(byte) fontColor[1], (byte) fontColor[2]);
			palette.setColorAtIndex((short) 10, (byte) bgColor[0],
					(byte) bgColor[1], (byte) bgColor[2]);
			HSSFFont titleFont = workbook.createFont();
			titleFont.setColor((short) 9);
			titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			titleFont.setFontHeightInPoints((short) fontSize);
			HSSFCellStyle titleStyle = createBorderedStyle(workbook,false);
			titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			titleStyle.setFillForegroundColor((short) 10);
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			return titleStyle;
		}
		return null;
	}
	
	private HSSFCellStyle createBorderedStyle(HSSFWorkbook wb,boolean showBorder) {
		HSSFCellStyle style = wb.createCellStyle();
		if(showBorder){
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		}
		return style;
	}
	
	private Map<String,CellStyle> createFormMapStyle(ReportFormModel reportFormModel,Workbook wb){
		Map<String,CellStyle> styles = new HashMap<String,CellStyle>();
		 List<ReportFormDataModel> list = reportFormModel.getListReportFormDataModel();
		 ReportFormDataModel reportFormDataModel;
		 if(list.size()>0){
			 reportFormDataModel=list.get(0); 
			int labelAlign=reportFormDataModel.getLabelAlign();
			int dataAlign=reportFormDataModel.getDataAlign();
			int dataStyle=reportFormDataModel.getDataStyle();		
			 if (wb instanceof HSSFWorkbook) {
					HSSFWorkbook workbook = (HSSFWorkbook) wb;
					HSSFCellStyle labelStyle = createBorderedStyle(workbook,reportFormModel.isShowBorder());
					this.setAligment(labelStyle, labelAlign);
					labelStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					styles.put("label", labelStyle);
					HSSFCellStyle valueStyle = createBorderedStyle(workbook,reportFormModel.isShowBorder());
					this.setAligment(valueStyle, dataAlign);
					valueStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					this.setFormDataStyle(workbook,valueStyle,dataStyle);
					styles.put("data", valueStyle);
				}
		 }
		
		return styles;
	}
	
	private void setFormDataStyle(HSSFWorkbook workbook ,HSSFCellStyle style,int i){
		HSSFFont font = workbook.createFont();
		 if(i==0){
			 //正常
		 }else if(i==4){
			 //下划线
			 font.setUnderline(HSSFFont.U_SINGLE);
			 style.setFont(font);
		 }else if(i==2){
			 //倾斜
			 font.setItalic(true);
			 style.setFont(font);
		 }else if(i==1){
			 //加粗
			 font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			 style.setFont(font);
		 }
	}
	
	private void setFormRegionStyle(Sheet sheet, CellRangeAddress ca, CellStyle cs) {
		for (int i = ca.getFirstRow(); i <= ca.getLastRow(); i++) {
			Row row = CellUtil.getRow(i, sheet);
			for (int j = ca.getFirstColumn(); j <= ca.getLastColumn(); j++) {
				Cell cell = CellUtil.getCell(row, j);
				cell.setCellStyle(cs);
			}
		}
	}
	private void setAligment(HSSFCellStyle style,int i){
		 if(i==0){
			 style.setAlignment(CellStyle.ALIGN_LEFT);
		 }else if(i==1){
			 style.setAlignment(CellStyle.ALIGN_CENTER);
		 }else if(i==2){
			 style.setAlignment(CellStyle.ALIGN_RIGHT);
		 }
	}
	
	private  Map<String, CellStyle> createStyles(Workbook wb,ReportGridModel gridModel){
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		//ExcelModelTitle title = gridExcelModel.getExcelModelTitle();
		//int[] bgColor = title.getBgColor();
		//if(bgColor==null)bgColor=new int[]{255,255,255};
		//int[] fontColor = title.getFontColor();
		//if(fontColor==null)fontColor=new int[]{0,0,0};
		//int fontSize = title.getFontSize();
		//if(fontSize<1)fontSize=10;
		ReportGridDataModel gridData = gridModel.getGridDataModel();
		int[] contextBgColor = gridData.getContentBgColor();
		if(contextBgColor==null)contextBgColor=new int[]{255,255,255};
		int[] contextFontColor = gridData.getContentFontColor();
		if(contextFontColor==null)contextFontColor=new int[]{0,0,0};
		int contextFontAlign = gridData.getContentFontAlign();
		int contextFontSize = gridData.getContentFontSize();
		if(contextFontSize<1)contextFontSize=10;
		List<ReportGridHeaderModel> headerList = gridModel.getGridHeaderModelList();
		ReportGridHeaderModel header=headerList.get(0);
		int headerAlign=header.getAlign();
		int[] headerBgColor=header.getBgColor();
		if(headerBgColor==null)headerBgColor=new int[]{255,255,255};
		int[] headerFontColor=header.getFontColor();
		if(headerFontColor==null)headerFontColor=new int[]{0,0,0};
		int headerFontSize=header.getFontSize();
		if (wb instanceof HSSFWorkbook) {
			HSSFWorkbook workbook = (HSSFWorkbook) wb;
			HSSFPalette palette = workbook.getCustomPalette();
//			palette.setColorAtIndex((short) 9, (byte) fontColor[0],
//					(byte) fontColor[1], (byte) fontColor[2]);
//			palette.setColorAtIndex((short) 10, (byte) bgColor[0],
//					(byte) bgColor[1], (byte) bgColor[2]);
			palette.setColorAtIndex((short) 11, (byte) contextBgColor[0],
					(byte) contextBgColor[1], (byte) contextBgColor[2]);
			palette.setColorAtIndex((short) 12, (byte) contextFontColor[0],
					(byte) contextFontColor[1], (byte) contextFontColor[2]);
			palette.setColorAtIndex((short) 13, (byte) headerBgColor[0],
					(byte) headerBgColor[1], (byte) headerBgColor[2]);
			palette.setColorAtIndex((short) 14, (byte) headerFontColor[0],
					(byte) headerFontColor[1], (byte) headerFontColor[2]);
			
			HSSFFont titleFont = workbook.createFont();
			titleFont.setColor((short) 9);
			titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			//titleFont.setFontHeightInPoints((short) fontSize);
			HSSFCellStyle titleStyle = this.createBorderedStyle(workbook,true);
			titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			titleStyle.setFillForegroundColor((short) 10);
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			styles.put("title", titleStyle);
			
			HSSFFont headerFont = workbook.createFont();
			headerFont.setColor((short) 14);
			headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			headerFont.setFontHeightInPoints((short) headerFontSize);
			HSSFCellStyle headerStyle = this.createBorderedStyle(workbook,true);
			headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerStyle.setFillForegroundColor((short) 13);
			headerStyle.setFont(headerFont);
			this.setAligment(headerStyle,headerAlign);
			headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			styles.put("header", headerStyle);
			
			HSSFFont dataFont = workbook.createFont();
			dataFont.setColor((short) 12);
			dataFont.setFontHeightInPoints((short)contextFontSize);
			HSSFCellStyle dataStyle = this.createBorderedStyle(workbook,true);
			dataStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			dataStyle.setFillForegroundColor((short) 11);
			dataStyle.setFont(dataFont);
			dataStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			dataStyle.setWrapText(true);
			this.setAligment(dataStyle,contextFontAlign);
			styles.put("data", dataStyle);
			
			HSSFFont dateFont = workbook.createFont();
			dateFont.setColor((short) 12);
			dateFont.setFontHeightInPoints((short)contextFontSize);
			HSSFCellStyle dateStyle = this.createBorderedStyle(workbook,true);
			CreationHelper helper = workbook.getCreationHelper();
			dateStyle.setDataFormat(helper.createDataFormat().getFormat(
					"m/d/yy h:mm"));
			dateStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			dateStyle.setFillForegroundColor((short) 11);
			dateStyle.setFont(dateFont);
			dateStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			this.setAligment(dateStyle,contextFontAlign);
			styles.put("date", dateStyle);

		}else{
			throw new RuntimeException(wb+"暂不支持此种格式");
		}
		return styles;
	}
	
	private void calculateMaxHeaderLevel(ReportGridModel gridModel,
			List<ReportGridHeaderModel> gridHeaders) {
		int maxLevel = 0;
		for (ReportGridHeaderModel header : gridHeaders) {
			if (header.getLevel() > maxLevel) {
				maxLevel = header.getLevel();
				gridModel.setMaxHeaderLevel(maxLevel);
			}
			if (header.getHeaders().size() > 0) {
				this.calculateMaxHeaderLevel(gridModel,
						header.getHeaders());
			}
		}
	}

	private void calculateColumnCount(List<ReportGridHeaderModel> tempGridHeader,
			List<ReportGridHeaderModel> gridHeader) {
		for (ReportGridHeaderModel header : gridHeader) {
			if (header.getHeaders().size() > 0) {
				this.calculateColumnCount(tempGridHeader, header.getHeaders());
			}else{
				tempGridHeader.add(header);
			}
		}
	}
	
	private void createGridExcelHeader(Sheet sheet,ReportGridModel gridModel,int starHeaderRow,int maxHeaderLevel,List<ReportGridHeaderModel> bottomHeaderList,CellStyle headerStyle){
		String[] header = this.getHeader(sheet, gridModel,bottomHeaderList);
		Row row;
		Cell cell;
		Map<String,Object> map = new HashMap<String,Object>();
		int rows_max =maxHeaderLevel;
		for (int k = 0; k < rows_max; k++) {
			row = sheet.createRow((short) k+starHeaderRow);
			for (int i = 0; i < header.length; i++) {
				String headerTemp = header[i];
				String[] s = headerTemp.split("_");
				String sk = "";
				int num = i;
				if (s.length == 1) {
					cell = row.createCell((short) (num));
					cell.setCellStyle(headerStyle);
					sheet.addMergedRegion(new CellRangeAddress(starHeaderRow,starHeaderRow+rows_max-1,num,num));
					sk = headerTemp;
					cell.setCellValue(sk);
				} else {
					cell = row.createCell((short) (num));
					cell.setCellStyle(headerStyle);
					int cols = 0;
					if (map.containsKey(headerTemp)) {
						continue;
					}
					for (int d = 0; d <= k; d++) {
						if (d != k) {
							sk += s[d] + "_";
						} else {
							sk += s[d];
						}
					}
					if (map.containsKey(sk)) {
						continue;
					}
					for (int j = 0; j < header.length; j++) {
						if (header[j].startsWith(sk)) {
							cols++;
						}
					}
					cell.setCellStyle(headerStyle);
					cell.setCellValue(s[k]);
					sheet.addMergedRegion(new CellRangeAddress(k + starHeaderRow,k + starHeaderRow,num,num + cols - 1));
					if (sk.equals(headerTemp)) {
						sheet.addMergedRegion(new CellRangeAddress(k + starHeaderRow,k + starHeaderRow + rows_max - s.length,num,num));
					}
				}
				if (s.length > k) {
					if (!map.containsKey(sk)) {
						String key = "";
						if (k > 0) {
							key = sk;
						} else {
							key = s[k];
						}
						map.put(key, null);
					}
				}
			}
		}
	}
	private void createGridExcelData(Sheet sheet,ReportGridModel gridModel,int starDataRow,int rowSize,CellStyle dataStyle,CellStyle dateStyle){
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		List<ReportGridHeaderModel> tempGridExcelHeader = new ArrayList<ReportGridHeaderModel>();
		this.calculateColumnCount(tempGridExcelHeader, gridModel.getGridHeaderModelList());
		List<Map<String, Object>> excelDatas = gridModel.getGridDataModel().getDatas();
		String treeColumn = gridModel.getGridDataModel().getTreeColumn();
		int mapDataIndex=0;
		Cell cell=null;
		Row row=null;
		for (int rowNum =starDataRow ; rowNum <= starDataRow+rowSize-1; rowNum++) {
			 row = sheet.createRow(rowNum);
			Map<String, Object> map = excelDatas.get(mapDataIndex);
			int j = 0;
			for(ReportGridHeaderModel header:tempGridExcelHeader){
				Object value=map.get(header.getColumnName());
				cell = row.createCell(j);
				cell.setCellStyle(dataStyle);
				if (value != null) {
					if (value instanceof Date) {
						String result=sdf.format(value);
						if(result.endsWith("00:00:00")){
							result=result.substring(0,11);
						}
						cell.setCellValue(result);
					} else {
						if (header.getColumnName().equalsIgnoreCase(treeColumn)) {
							int level = this.calculateIndentationCount(value.toString());
							cell.setCellStyle(this.createIndentationStyle(sheet.getWorkbook(), level == 0 ? 0 : level * 2));
						}else{
							cell.setCellStyle(dataStyle);
						}
						cell.setCellValue(value.toString());
						
					}
				} else {
					cell.setCellValue("");
				}
				sheet.setColumnWidth(cell.getColumnIndex(), header.getWidth()/6>255?254*256:header.getWidth()/6*256);
				j++;
			}
			mapDataIndex++;
		}
		
	}
	private String[] getHeader(Sheet sheet,ReportGridModel gridModel,List<ReportGridHeaderModel> bottomHeaderList){
		List<String>  tempHeaderList=new ArrayList<String>();
		for(ReportGridHeaderModel header:bottomHeaderList){
			if(header.getParent()!=null){
				StringBuffer  sb=new StringBuffer();
				Stack<String> s=new Stack<String>();
				s.push(header.getLabel());
				this.createHeaderLabel(header, s);
				int size=s.size();
				for(int i=1;i<=size;i++){
					sb.append(s.pop());
				}
				tempHeaderList.add(sb.toString());
			}else{
				tempHeaderList.add(header.getLabel());
			}
		}
		return tempHeaderList.toArray(new String[]{});
	}
	private void createHeaderLabel(ReportGridHeaderModel header,Stack<String> s){
		if(header.getParent()!=null){
			s.push("_");
			ReportGridHeaderModel parent=header.getParent();
			s.push(parent.getLabel());
			this.createHeaderLabel(parent,s);
		}
	}

	private CellStyle createIndentationStyle(Workbook workbook, int s) {
		CellStyle dataStyle1 = this.createBorderedStyle((HSSFWorkbook) workbook, true);
		HSSFFont dataFont = (HSSFFont) workbook.createFont();
		dataFont.setColor((short) 12);
		dataFont.setFontHeightInPoints((short) 10);
		dataStyle1.setFillPattern(CellStyle.SOLID_FOREGROUND);
		dataStyle1.setFillForegroundColor((short) 11);
		dataStyle1.setFont(dataFont);
		dataStyle1.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		dataStyle1.setAlignment(CellStyle.ALIGN_LEFT);
		dataStyle1.setIndention(Short.valueOf(String.valueOf((s))));
		return dataStyle1;
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
