/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.innofi.component.cache.service.ISysCacheConfigService;
import com.innofi.component.report.excel.domain.CellWrapper;
import com.innofi.component.report.excel.domain.ExcelDataWrapper;
import com.innofi.component.report.excel.domain.ExcelModel;
import com.innofi.component.report.excel.domain.ExcelModelDetail;
import com.innofi.component.report.excel.domain.RowWrapper;
import com.innofi.component.report.excel.interceptor.InterceptorException;
import com.innofi.component.report.excel.manager.ExcelModelManager;
import com.innofi.component.report.excel.processor.Processor;
import com.innofi.framework.spring.context.ContextHolder;


/**
 * Excel解析处理
 *
 * @author matt.yao@bstek.com
 * @since 1.0
 */
public class ExcelParser {
    public final Logger logger = LoggerFactory.getLogger(ExcelParser.class);
    public static final String INTERCEPTER_SEPERATOR = ":";
    public static final String CLASS_METHOD_SEPERATOR = "#";
    public static final String CLASS_INTERCEPTER_PREFIX = "class";
    public static final String SPRING_INTERCEPTER_PREFIX = "spring";
    public static final String DEFAULT_SPRING_EXCEL_PROCESSOR = "bdf.defaultExcelProcessor";
    public static final String EXCEL_DATA_CACHE_KEY = "excel_cache";
    public static final String INCREMENTTYPE = "INCREMENT";
    public static final String SEQUENCETYPE = "SEQUENCE";
    public static final String VMIDTYPE = "VMID";
    public static final String UUIDTYPE = "UUID";
    public static final String ASSIGNEDTYPE = "ASSIGNED";

    /**
     * excel支持的最大行数65535行
     */
    public static final int MAX_EXCEL_ROW = 65535;
    /**
     * excel支持的最大列数255列
     */
    public static final int MAX_EXCEL_COLUMN = 255;
    public ExcelModelManager excelModelManager;
    public ISysCacheConfigService sysCacheConfigService;

    public ExcelDataWrapper parser(String excelModelId, InputStream in) throws Exception {
        ExcelDataWrapper excelDataWrapper = new ExcelDataWrapper();
        excelDataWrapper.setExcelModelId(excelModelId);
        ExcelModel excelModel = excelModelManager.findExcelModelById(excelModelId);
        List<ExcelModelDetail> excelModelDetailsList = excelModelManager.findExcelModelDetailByModelId(excelModelId);
        String primarykey = excelModel.getTablePrimaryKey();
        Workbook workbook = this.createWorkbook(in);
        Sheet sheet = null;
        if (!StringUtils.hasText(excelModel.getSheet())) {
            sheet = workbook.getSheetAt(0);
        } else {
            sheet = workbook.getSheet(excelModel.getSheet());
        }
        if (sheet == null) {
            throw new RuntimeException("上传的excel没有解析到合法的sheet值！");
        }
        int startRow = excelModel.getStartRow();
        int endRow = excelModel.getEndRow();
        int startColumn = excelModel.getStartColumn();
        int endColumn = excelModel.getEndColumn();
        if (startRow == 0 || startRow < sheet.getFirstRowNum() + 1) {
            startRow = sheet.getFirstRowNum() + 1;
        }
        if (endRow == 0 || endRow > sheet.getLastRowNum() + 1) {
            endRow = sheet.getLastRowNum() + 1;
        }
        if (endRow > ExcelParser.MAX_EXCEL_ROW) {
            endRow = ExcelParser.MAX_EXCEL_ROW;
        }
        if (endColumn > ExcelParser.MAX_EXCEL_COLUMN) {
            endColumn = ExcelParser.MAX_EXCEL_COLUMN;
        }
        Collection<RowWrapper> rowWrapperCollection = new ArrayList<RowWrapper>();
        //遍历excel行数据
        for (int i = startRow - 1; i <= endRow - 1; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            if (startColumn == 0) {
                startColumn = row.getFirstCellNum() + 1;
            }
            if (endColumn == 0) {
                endColumn = row.getLastCellNum();
            }
            RowWrapper rowWrapper = new RowWrapper();
            //设置表名称
            rowWrapper.setTableName(excelModel.getTableName());
            //设置行号
            rowWrapper.setRow(row.getRowNum() + 1);
            List<CellWrapper> cellWrapperList = new ArrayList<CellWrapper>();
            CellWrapper cellWrapper = null;
            for (ExcelModelDetail excelModelDetail : excelModelDetailsList) {
                int excelColumn = excelModelDetail.getExcelColumn();
                if (excelColumn == 0) {
                    throw new RuntimeException("上传的excel工作表" + sheet.getSheetName() + "第" + excelColumn + "列不存在！");
                }
                String excelColumnTableName = excelModelDetail.getTableColumn();
                String interceptor = excelModelDetail.getInterceptor();
                Cell cell = row.getCell(excelColumn - 1);
                cellWrapper = new CellWrapper();
                if (cell == null) {
                    cellWrapper.setColumn(excelColumn);
                } else {
                    cellWrapper.setColumn(cell.getColumnIndex() + 1);
                }
                cellWrapper.setColumnName(excelModelDetail.getTableColumn());
                cellWrapper.setValid(true);
                this.getIntercepterCellValue(cell, cellWrapper, interceptor);
                if (StringUtils.hasText(primarykey)) {
                    if (primarykey.toLowerCase().equals(
                            excelColumnTableName.toLowerCase()) && excelModel.getPrimaryKeyType().equals(ExcelParser.ASSIGNEDTYPE)) {
                        cellWrapper.setIsPrimaryKey(true);
                        Object obj = cellWrapper.getValue();
                        if (obj == null) {
                            cellWrapper.setValue("<font color=\"red\">用户自定义主键不能为空!</font>");
                            cellWrapper.setValid(false);
                        }
                    }
                } else {
                    cellWrapper.setIsPrimaryKey(false);
                }
                cellWrapperList.add(cellWrapper);
            }
            rowWrapper.setCellWrappers(cellWrapperList);
            rowWrapper.setValid(true);
            for (CellWrapper cw : cellWrapperList) {
                Boolean flag = cw.isValid();
                if (!flag) {
                    rowWrapper.setValid(false);
                    break;
                }
            }
            rowWrapperCollection.add(rowWrapper);
        }
        if (!StringUtils.hasText(excelModel.getProcessor())) {
            excelDataWrapper.setProcessor(this.getDefaultProcessor());
        } else {
            excelDataWrapper.setProcessor(excelModel.getProcessor());
        }
        excelDataWrapper.setExcelModel(excelModel);
        excelDataWrapper.setRowWrappers(rowWrapperCollection);
        excelDataWrapper.setValidate(true);
        for (RowWrapper rowWrapper : rowWrapperCollection) {
            if (rowWrapper.getCellWrappers().size() == 0) {
                excelDataWrapper.setValidate(false);
            } else {
                Boolean flag = rowWrapper.isValid();
                if (!flag) {
                    excelDataWrapper.setValidate(false);
                    break;
                }
            }
        }
        if (excelDataWrapper.isValidate()) {
            sysCacheConfigService.putTemporaryCacheObject(getExcelCacheKey(), excelDataWrapper);
        } else {
            sysCacheConfigService.removeTemporaryCacheObject(getExcelCacheKey());
        }
        return excelDataWrapper;
    }

    /**
     * 创建工作薄,支持xls和xlss格式的excel文件
     *
     * @param in 文件输入流
     * @return 工作薄 Workbook
     * @throws IOException
     * @throws InvalidFormatException
     */
    public Workbook createWorkbook(InputStream in) throws IOException, InvalidFormatException {
        Assert.notNull(in, "参数 InputStream 不能为空！");
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        //xls格式
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            return new HSSFWorkbook(in);
        }
        //xlsx格式
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            //new SXSSFWorkbook();
            return new XSSFWorkbook(OPCPackage.open(in));
        }
        throw new RuntimeException("上传的不是正确的Excel格式文件，不能创建工作薄");
    }

    /**
     * @param cell        poi单元格cell对象
     * @param cellWrapper CellWrapper对象
     * @param interceptor 拦截类字符串  如class:com.comp.excel.format.UserFormatter#formatSex或者spring:SpringBeanId#method，
     * @throws Exception
     */
    public void getIntercepterCellValue(Cell cell, CellWrapper cellWrapper, String interceptor) throws Exception {
        if (cell == null) {
            cellWrapper.setValue(null);
        } else {
            int cellType = cell.getCellType();
            switch (cellType) {
                case Cell.CELL_TYPE_BLANK:
                    cellWrapper.setValue(null);
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    cellWrapper
                            .setValue(Boolean.valueOf(cell.getBooleanCellValue()));
                    break;
                case Cell.CELL_TYPE_ERROR:
                    cellWrapper.setValue(cell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    cellWrapper.setValue(cell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        cellWrapper.setValue(date);
                    } else {
                        cellWrapper.setValue(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    cellWrapper.setValue(cell.getStringCellValue());
                    break;
            }
        }
        logger.debug("before interceptor column number["
                + cellWrapper.getColumn() + "] cell value["
                + cellWrapper.getValue() + "] cell intercepter[" + interceptor
                + "]");
        if (StringUtils.hasText(interceptor)) {
            Object obj = null;
            try {
                obj = this.activateInterceptor(interceptor,
                        cellWrapper.getValue());
            } catch (InterceptorException ex) {
                obj = ex.getMessage();
                cellWrapper.setValid(false);
            }
            if (cellWrapper.isValid()) {
                cellWrapper.setValue(obj);
            } else {
                cellWrapper.setValue(obj);
            }
        }
        logger.debug("after interceptor  cell value[" + cellWrapper.getValue()
                + "]");

    }

    /**
     * 处理用户定义的拦截器
     *
     * @param interceptor 拦截类字符串  如class:com.comp.excel.format.UserFormatter#formatSex或者spring:SpringBeanId#method，
     * @param value       方法参数值
     * @return 处理结果值
     * @throws Exception
     */
    private Object activateInterceptor(String interceptor, Object value) throws Exception {
        String typeInterceptor[] = StringUtils.split(interceptor, ExcelParser.INTERCEPTER_SEPERATOR);
        if (typeInterceptor != null && typeInterceptor.length == 2) {
            String typeClassMethod[] = StringUtils.split(typeInterceptor[1], ExcelParser.CLASS_METHOD_SEPERATOR);
            if (typeClassMethod != null && typeClassMethod.length == 2) {
                Object obj = null;
                Class<?> clazz = null;
                String objClass = typeClassMethod[0];
                String methodName = typeClassMethod[1];
                if (typeInterceptor[0].toLowerCase().equals(ExcelParser.CLASS_INTERCEPTER_PREFIX)) {
                    clazz = ClassUtils.getClass(objClass);
                    obj = clazz.newInstance();
                }
                if (typeInterceptor[0].toLowerCase().equals(ExcelParser.SPRING_INTERCEPTER_PREFIX)) {
                    obj = ContextHolder.getSpringBean(objClass);
                    //obj= SpringHolder.getSpringBean(objClass);
                    clazz = obj.getClass();
                }
                if (clazz != null) {
                    //反射processor定义的方法
                    if (value instanceof ExcelDataWrapper) {
                        ExcelDataWrapper edw = (ExcelDataWrapper) value;
                        Method method = clazz.getDeclaredMethod(methodName, new Class[]{ExcelDataWrapper.class});
                        return method.invoke(obj, new Object[]{edw});
                    } else {
                        //反射cell定义的拦截方法 获取cell自定义异常
                        Method method = clazz.getDeclaredMethod(methodName, new Class[]{Object.class});
                        try {
                            return method.invoke(obj, new Object[]{value});
                        } catch (InvocationTargetException it) {
                            Throwable e = it.getTargetException();
                            if (e instanceof InterceptorException) {
                                throw new InterceptorException(e.getMessage());
                            } else {
                                throw new RuntimeException(clazz + "方法【" + method + "】调用失败！");
                            }

                        }
                    }

                }

            }
        }
        return value;
    }

    /**
     * 保存缓存中存放的excel数据到数据库
     *
     * @throws Exception
     */
    public void saveParserExcelData() throws Exception {
        ExcelDataWrapper excelDataWrapper = (ExcelDataWrapper) sysCacheConfigService.getTemporaryCacheObject(getExcelCacheKey());
        if (excelDataWrapper != null) {
            String processor = excelDataWrapper.getProcessor();
            if (StringUtils.hasText(processor) && excelDataWrapper.getRowWrappers().size() > 0 && excelDataWrapper.validate) {
                Processor p = (Processor) ContextHolder.getSpringBean(processor);
                p.execute(excelDataWrapper);
            }
            sysCacheConfigService.removeTemporaryCacheObject(getExcelCacheKey());
        }
    }

    /**
     * 得到excel数据缓存key参数
     *
     * @return 返回key值
     */
    public static String getExcelCacheKey() {
        return ContextHolder.getContext().getLoginUsername() + ExcelParser.EXCEL_DATA_CACHE_KEY;
    }

    /**
     * 获取当前存放在Cache中的Excel数据
     *
     * @return 返回ExcelDataWrapper对象
     */
    public ExcelDataWrapper getCurrentExcelData() {
        return (ExcelDataWrapper) sysCacheConfigService.getTemporaryCacheObject(getExcelCacheKey());
    }

    /**
     * 清除当前存放到Cache中的Excel数据
     */
    public void clearCurrentExcelData() {
        sysCacheConfigService.removeTemporaryCacheObject(getExcelCacheKey());
    }

    /**
     * 得到默认excel数据处理器
     *
     * @return 返回默认excel数据处理器值
     */
    public String getDefaultProcessor() {
        return ExcelParser.DEFAULT_SPRING_EXCEL_PROCESSOR;
    }

    /**
     * 解析excel文件的开始行和结束行，存放在map对象中,参数分别是startRow和endRow
     *
     * @param excelModelId excel表定义id
     * @param in           excel文件输入流
     * @return 开始行和结束行
     * @throws Exception
     */
    public Map<String, Integer> parseExcelRow(String excelModelId, InputStream in) throws Exception {
        Workbook wb = this.createWorkbook(in);
        Sheet sheet = null;
        ExcelModel excelModel = excelModelManager.findExcelModelById(excelModelId);
        if (!StringUtils.hasText(excelModel.getSheet())) {
            sheet = wb.getSheetAt(0);
        } else {
            sheet = wb.getSheet(excelModel.getSheet());
        }
        if (sheet == null) {
            throw new RuntimeException("上传的excel没有解析到合法的sheet值！");
        }
        int startRow = sheet.getFirstRowNum() + 1;
        int endRow = sheet.getLastRowNum() + 1;
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("startRow", startRow);
        map.put("endRow", endRow);
        return map;

    }

    public Collection<String> getPrimaryTypes() {
        List<String> list = new ArrayList<String>();
        list.add(ExcelParser.INCREMENTTYPE);
        list.add(ExcelParser.SEQUENCETYPE);
        list.add(ExcelParser.UUIDTYPE);
        list.add(ExcelParser.VMIDTYPE);
        list.add(ExcelParser.ASSIGNEDTYPE);
        return list;
    }

    public ExcelModelManager getExcelModelManager() {
        return excelModelManager;
    }

    public void setExcelModelManager(ExcelModelManager excelModelManager) {
        this.excelModelManager = excelModelManager;
    }

    public ISysCacheConfigService getSysCacheConfigService() {
        return sysCacheConfigService;
    }

    public void setSysCacheConfigService(ISysCacheConfigService sysCacheConfigService) {
        this.sysCacheConfigService = sysCacheConfigService;
    }
}
