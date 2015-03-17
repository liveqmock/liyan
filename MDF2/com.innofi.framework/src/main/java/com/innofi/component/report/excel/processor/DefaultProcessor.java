/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.processor;

import java.rmi.dgc.VMID;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import com.innofi.framework.dao.DaoSupport;

import com.innofi.component.report.excel.ExcelParser;
import com.innofi.component.report.excel.domain.CellWrapper;
import com.innofi.component.report.excel.domain.ExcelDataWrapper;
import com.innofi.component.report.excel.domain.ExcelModel;
import com.innofi.component.report.excel.domain.ExcelModelDetail;
import com.innofi.component.report.excel.domain.RowWrapper;
import com.innofi.component.report.excel.manager.ExcelModelManager;

/**
 * @author matt.yao@bstek.com
 * @since 1.0
 */
public class DefaultProcessor extends DaoSupport implements Processor {
    public static final String ORACLEDBTYPE = "Oracle";
    public static final String DB2DBTYPE = "DB2";
    public final Logger logger = LoggerFactory.getLogger(DefaultProcessor.class);
    public ExcelModelManager excelModelManager;

    /* (non-Javadoc)
      * @see com.bstek.bdf.excel.processor.Processor#loadingDynamicStatement()
      */
    public void init() throws Exception {

    }

    /* (non-Javadoc)
      * @see com.bstek.bdf.excel.processor.Processor#execute(com.bstek.bdf.excel.domain.ExcelDataWrapper)
      */
    public void execute(ExcelDataWrapper excelDataWrapper) throws Exception {
        //ExcelModel  excelModel=excelModelManager.findExcelModelById(excelDataWrapper.getExcelModelId());
        ExcelModel excelModel = excelDataWrapper.getExcelModel();
        ExcelModelDetail excelModelDetail = null;
        if (StringUtils.hasText(excelModel.getTablePrimaryKey())) {
            excelModelDetail = excelModelManager.findExcelModelDetailByModelIdAndPrimaryKey(excelModel.getId(), excelModel.getTablePrimaryKey());
        }
        Collection<RowWrapper> rowWrappers = excelDataWrapper.getRowWrappers();
        int count = 0;
        for (RowWrapper rowWrapper : rowWrappers) {
            String tableName = rowWrapper.getTableName();
            Collection<CellWrapper> cellWrappers = rowWrapper.getCellWrappers();
            //系统定义主键策略
            if (excelModelDetail == null && cellWrappers.size() > 0) {
                if (StringUtils.hasText(excelModel.getTablePrimaryKey())) {
                    if (excelModel.getPrimaryKeyType().equals(ExcelParser.VMIDTYPE)) {
                        CellWrapper cellWrapper = new CellWrapper();
                        cellWrapper.setIsPrimaryKey(true);
                        cellWrapper.setColumnName(excelModel.getTablePrimaryKey());
                        cellWrapper.setValue(new VMID().toString());
                        cellWrappers.add(cellWrapper);
                    } else if (excelModel.getPrimaryKeyType().equals(ExcelParser.UUIDTYPE)) {
                        CellWrapper cellWrapper = new CellWrapper();
                        cellWrapper.setIsPrimaryKey(true);
                        cellWrapper.setColumnName(excelModel.getTablePrimaryKey());
                        cellWrapper.setValue(UUID.randomUUID().toString());
                        cellWrappers.add(cellWrapper);
                    }
                }
            }
            List<String> columnNameList = new ArrayList<String>();
            List<Object> columnValueList = new ArrayList<Object>();
            for (CellWrapper cellWrapper : cellWrappers) {
                String columnName = cellWrapper.getColumnName();
                if (StringUtils.hasText(columnName)) {
                    Object columnValue = cellWrapper.getValue();
                    columnNameList.add(columnName);
                    columnValueList.add(columnValue);
                    if (cellWrapper.getIsPrimaryKey() && excelModel.getPrimaryKeyType().equals(ExcelParser.ASSIGNEDTYPE)) {
                        //判断主键是否在数据库重复
                        String sql = "select count(*) from " + tableName + " where " + columnName + "=?";
                        int sum = this.getJdbcDao().queryForInt(sql, new Object[]{columnValue});
                        if (sum > 0) {
                            throw new RuntimeException("数据库表[" + tableName + "]的字段[" + columnName + "]为主键，键值[" + columnValue + "]重复！");
                        }
                    }
                }

            }
            int n = this.insertRowWrapper2Table(excelModel, columnNameList, columnValueList);
            if (n == 1) {
                count++;
            }
        }
        logger.info("解析excel入库成功，导入[" + count + "]条数据！");

    }

    public int insertRowWrapper2Table(ExcelModel excelModel, List<String> columnNameList, List<Object> columnValueList) throws Exception {
        String tableName = excelModel.getTableName();
        StringBuffer sb = new StringBuffer("insert into ");
        sb.append(tableName + "( ");
        StringBuffer sbValues = new StringBuffer(" values(");
        int j = 1;
        if (StringUtils.hasText(excelModel.getTablePrimaryKey())) {
            if (excelModel.getPrimaryKeyType().equals(ExcelParser.SEQUENCETYPE)) {
                if (excelModel.getDbType().equals(DefaultProcessor.ORACLEDBTYPE)) {
                    sb.append(excelModel.getTablePrimaryKey());
                    sbValues.append(this.getOracleNextval(excelModel.getSequenceName()));
                } else if (excelModel.getDbType().equals(DefaultProcessor.DB2DBTYPE)) {
                    sb.append(excelModel.getTablePrimaryKey());
                    sbValues.append(this.getDB2Nextval(excelModel.getSequenceName()));
                }
                if (columnNameList.size() > 0 && columnValueList.size() > 0) {
                    sb.append(",");
                    sbValues.append(",");
                }
            }
        }
        for (String s : columnNameList) {
            if (columnNameList.size() == j) {
                sb.append(s);
                sbValues.append("?");
            } else {
                sb.append(s + ",");
                sbValues.append("?,");
            }
            j++;

        }
        sb.append(" )");
        sbValues.append(" )");
        String sql = sb.append(sbValues).toString();
        logger.debug("insert into :" + sql);
        //this.getJdbcTemplate(excelModel.getDatasourceName()).batchUpdate(sql, pss)
        return this.getJdbcDao().update(sql, columnValueList.toArray());


    }

    /**
     * 批处理更新
     *
     * @param jdbcTemplate
     * @param sql
     * @param columnValueList
     */
    public void saveBatchUpdate(JdbcTemplate jdbcTemplate, String sql, final List<Object> columnValueList) {
        final int count = columnValueList.size();
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                int columnIndex = 0;
                for (Object o : columnValueList) {
                    ps.setObject(++columnIndex, o);
                }
            }

            public int getBatchSize() {
                return count;
            }
        });
    }

    public String getOracleNextval(String sequenceName) {
        return sequenceName + ".nextval";
    }

    public String getDB2Nextval(String sequenceName) {
        return "NEXTVAL FOR " + sequenceName;
    }

    public ExcelModelManager getExcelModelManager() {
        return excelModelManager;
    }

    public void setExcelModelManager(ExcelModelManager excelModelManager) {
        this.excelModelManager = excelModelManager;
    }

}
	


