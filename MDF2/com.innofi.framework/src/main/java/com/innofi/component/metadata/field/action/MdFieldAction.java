/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.field.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.component.metadata.entityobject.service.IMdEntityObjectService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.pojo.metadata.MdField;
import com.innofi.component.metadata.field.service.IMdFieldService;
import com.innofi.component.metadata.table.service.IMdTableService;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.xml.XmlParseException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 功能/ 模块：测试模块
 *
 * @author 张磊  Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          测试Action实现类
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class MdFieldAction extends BaseActionImpl {

    @Resource
    private IMdFieldService mdFieldServcie;

    /**
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findMdFields(Page page, Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();

        List<String> dmdIdsList = new ArrayList<String>();
        if (parameter != null) {
            if (parameter.get("dmdIds") != null) {
                dmdIdsList = java.util.Arrays.asList(parameter.get("dmdIds").toString().split(","));
                if (dmdIdsList.size() > 0) {
                    addPropertyFilter(filters, "id", dmdIdsList, QueryConstants.NOT_IN, true);
                }
            }
            if (parameter.get("dimenFieldIds") != null && !"".equals(parameter.get("dimenFieldIds"))) {
                dmdIdsList = java.util.Arrays.asList(parameter.get("dimenFieldIds").toString().split(","));
                if (dmdIdsList.size() > 0) {
                    addPropertyFilter(filters, "id", dmdIdsList, QueryConstants.NOT_IN, true);
                }
            }
            addPropertyFilter(filters, "tableId", parameter.get("tableId"), QueryConstants.LIKE, true);
            addPropertyFilter(filters, "tableName", parameter.get("tableName"), QueryConstants.LIKE, true);
            addPropertyFilter(filters, "fieldName", parameter.get("fieldName") == null ? null : ((String) parameter.get("fieldName")).toUpperCase(), QueryConstants.LIKE, true);
            addPropertyFilter(filters, "fieldCnName", parameter.get("fieldCnName"), QueryConstants.LIKE, true);
            addPropertyFilter(filters, "fieldDict", parameter.get("fieldDict"), QueryConstants.LIKE, true);
            addPropertyFilter(filters, "status", parameter.get("status"), QueryConstants.EQUAL, true);
        }
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        orders.add(new PropertyOrder("tableName", QueryConstants.ORDER_ASC));
        orders.add(new PropertyOrder("fieldSeq", QueryConstants.ORDER_ASC));
        mdFieldServcie.findByPage_Filters(filters, orders, innofiPage);

    }

    @DataResolver
    public void saveMdFields(Collection<MdField> objs) {
        IMdTableService mdTableService = ContextHolder.getSpringBean("mdTableService");
        MdField mdField = null;
        for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
            mdField = (MdField) iterator.next();
            EntityState state = EntityUtils.getState(mdField);
            if (EntityState.NEW.equals(state)) {
                mdFieldServcie.save(mdField);
                mdTableService.refreshTableMetaData(mdField.getTableName(), null);
            } else if (EntityState.MODIFIED.equals(state)) {
                mdFieldServcie.update(mdField);
            } else if (EntityState.DELETED.equals(state)) {
                mdFieldServcie.delete(mdField);
                mdTableService.refreshTableMetaData(mdField.getTableName(), null);
            }
        }
    }

    @DataResolver
    public void saveMdFieldsforDynamictPojoManage(Collection<MdField> objs, Map parameter) {
        MdField mdField = null;
        for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
            mdField = (MdField) iterator.next();
            EntityState state = EntityUtils.getState(mdField);
            if (EntityState.NEW.equals(state)) {
                mdFieldServcie.saveMdFieldsforDynamictPojoManage(mdField);
            } else if (EntityState.MODIFIED.equals(state)) {
                if (FrameworkConstants.COMM_Y.equals(mdField.getIsExtend())) {
                    if (parameter == null) {
                        mdFieldServcie.updateMdFieldsforDynamictPojoManage(mdField, null);
                    } else {
                        mdFieldServcie.updateMdFieldsforDynamictPojoManage(mdField, (Boolean) parameter.get("active"));
                    }
                } else {
                    mdFieldServcie.update(mdField);
                }
            } else if (EntityState.DELETED.equals(state)) {
                mdFieldServcie.delete(mdField);
            }
        }
    }


    @Expose
    public boolean getColumnInfos(Map<String, Object> parameter) throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Collection<MdField> entityList = (Collection<MdField>) parameter.get("ds");
        String tableCnName = parameter.get("tableCnName").toString();
        String tableName = "";
        String[] headers = new String[entityList.size()];
        int i = 0;
        for (MdField field : entityList) {
            headers[i] = field.getFieldCnName() + "(" + field.getFieldName() + ")";
            i++;
            tableName = field.getTableName();
        }
        String filedir = ContextHolder.getSystemProperties().getString("metadata.template.path", "c:/metadataTemplate");
        File dir = new File(filedir);
        if (!dir.exists())
            dir.mkdirs();
        String filePath = filedir + "/" + tableCnName + "(" + tableName + ")" + ".xls";
        OutputStream out = new FileOutputStream(filePath, false);
        boolean flag = this.exportExcel(tableCnName + "(" + tableName + ")", headers, out);
        return flag;
    }

    @Expose
    public String refreshMdFieldLabel() throws IOException, XmlParseException, ClassNotFoundException {
        IMdEntityObjectService mdEntityObjectService = getSpringBean("mdEntityObjectService");
        return mdEntityObjectService.refreshProgramMetadata(ContextHolder.getSystemProperties().getString("mdf.coder.path", "D:/coder"));
        //mdFieldServcie.refreshMdFieldLabel();
    }

    /**
     * @param title   表格标题名
     * @param headers 表格属性列名数组
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     */

    @SuppressWarnings("unchecked")

    public boolean exportExcel(String title, String[] headers, OutputStream out) {

        // 声明一个工作薄

        HSSFWorkbook workbook = new HSSFWorkbook();

        // 生成一个表格

        HSSFSheet sheet = workbook.createSheet(title);

        // 设置表格默认列宽度为15个字节

        sheet.setDefaultColumnWidth(35);

        // 生成一个样式

        HSSFCellStyle style = workbook.createCellStyle();

        // 设置这些样式

        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);

        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);

        style.setBorderRight(HSSFCellStyle.BORDER_THIN);

        style.setBorderTop(HSSFCellStyle.BORDER_THIN);

        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 生成一个字体

        HSSFFont font = workbook.createFont();

        font.setColor(HSSFColor.VIOLET.index);

        font.setFontHeightInPoints((short) 12);

        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        // 把字体应用到当前的样式

        style.setFont(font);


        //产生表格标题行

        HSSFRow row = sheet.createRow(0);

        for (short i = 0; i < headers.length; i++) {

            HSSFCell cell = row.createCell(i);

            cell.setCellStyle(style);

            HSSFRichTextString text = new HSSFRichTextString(headers[i]);

            cell.setCellValue(text);

        }

        try {

            workbook.write(out);
            return true;

        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();
            return false;

        }
    }

    @DataProvider
    public Collection<MdField> compareFieldsVerNo1(Map<String, Object> parameter) {
        String tableName = parameter.get("tableName").toString();
        String ver1 = parameter.get("verNo1").toString();
        String ver2 = parameter.get("verNo2").toString();
        BigDecimal verNo1 = new BigDecimal(Long.valueOf(ver1).longValue());
        BigDecimal verNo2 = new BigDecimal(Long.valueOf(ver2).longValue());
        Map<BigDecimal, List<MdField>> listMap = mdFieldServcie.compareFields(tableName, verNo1, verNo2);
        return listMap.get(verNo1);
    }

    @DataProvider
    public Collection<MdField> compareFieldsVerNo2(Map<String, Object> parameter) {
        String tableName = parameter.get("tableName").toString();
        String ver1 = parameter.get("verNo1").toString();
        String ver2 = parameter.get("verNo2").toString();
        BigDecimal verNo1 = new BigDecimal(Long.valueOf(ver1).longValue());
        BigDecimal verNo2 = new BigDecimal(Long.valueOf(ver2).longValue());
        Map<BigDecimal, List<MdField>> listMap = mdFieldServcie.compareFields(tableName, verNo1, verNo2);
        return listMap.get(verNo2);
    }
}
