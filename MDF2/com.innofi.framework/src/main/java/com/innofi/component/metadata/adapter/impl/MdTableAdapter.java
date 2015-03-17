package com.innofi.component.metadata.adapter.impl;

import com.innofi.component.metadata.adapter.IMdMetaAdapter;
import com.innofi.component.metadata.table.service.IMdTableService;
import com.innofi.component.metadata.tablerelate.service.IMdTableRelateService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.cache.service.ISysCacheConfigService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.framework.pojo.metadata.MdTableRelate;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;

import org.springframework.stereotype.Service;

/**
 * 功能/ 模块：元数据
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30 表元数据适配器 修订历史： 日期:2012-12-24 作者:张磊
 *          Alex.zhang@innofi.com.cn 参考: 描述:获取表信息 北京名晟信息技术有限公司版权所有.
 */
@Service("mdTableAdapter")
public class MdTableAdapter implements IMdMetaAdapter {

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.adapter.IMdMetaAdapter#refreshMetaData(java.util.Map)
     */
    public String refreshMetaData(Map<String, Object> parameter) {
        IMdTableService mdTableService = ContextHolder.getSpringBean("mdTableService");

        //IMdTableRelateService mdTableRelateService = ContextHolder.getSpringBean("mdTableRelateService");
        List<MdTable> allTables = mdTableService.dynamicQueryBeanForList("metadata-dynamic-statement", "findTableInfo"); // 1.获取数据库中最新的数据。目标数据.
        List<String> tableNames = new ArrayList<String>();


        //mdTableService.refreshTableMetaData("CSS_MER_LOCK",allTables);

        //Map<String, MdTable> dbTablesMapping = new HashMap<String, MdTable>(); // 保存数据库中最新数据表的Map结构减少后续遍历
        //Map<String, MdTable> mdTablesMapping = new HashMap<String, MdTable>(); // 保存当前版本数据表的Map结构减少后续遍历

        //for (MdTable dbTable : allTables) {
        //    dbTablesMapping.put(dbTable.getTableName(), dbTable);
        //}

        char result = '1';
        synchronized (this) {

            if (parameter != null) {// 刷新数据表
                tableNames = java.util.Arrays.asList(parameter.get("tableNames").toString().split(","));
                for (int i = 0; i < tableNames.size(); i++) {
                	ConsoleUtil.info("table metadata refresh current table [" + tableNames.get(i) + "]");
                    if (mdTableService.refreshTableMetaData(tableNames.get(i), allTables) == '0')result = '0';
                    //mdTablesMapping.put(tableNames.get(i), mdTableService.findLastVersionMdTableByTableName(tableNames.get(i)));
                }
            } else {
                for (MdTable mdTable : allTables) {
                    ConsoleUtil.info("table relate metadata refresh current table [" + mdTable.getTableName() + "]");
                    if (mdTableService.refreshTableMetaData(mdTable.getTableName(), allTables) == '0') result = '0';
                    //mdTablesMapping.put(mdTable.getTableName(), mdTableService.findLastVersionMdTableByTableName(mdTable.getTableName()));
                }
            }

            //List<MdTable> mdTables = mdTableService.findMdTableByStatus(FrameworkConstants.STATUS_EFFECTIVE); // 所有有效表


            /*for (MdTable mdTable : mdTables) {
                mdTableRelateService.removeRelatePMdMeta(mdTable.getId());
                mdTablesMapping.put(mdTable.getTableName(), mdTable);
            }*/

            //List<MdTableRelate> dbAllMdTableRelates = mdTableRelateService.findAllMdTableRelateFormDb(); // 数据库当前版本关联关系

            /*for (MdTableRelate dbMdTableRelate : dbAllMdTableRelates) { // 保存物理关联关系
                String mainTableName = dbMdTableRelate.getMainTableName();
                MdTable mainMdTable = mdTablesMapping.get(mainTableName);
                String foreignTableName = dbMdTableRelate.getForeignTableName();
                MdTable foreignMdTable = mdTablesMapping.get(foreignTableName);

                if (!mdTablesMapping.containsKey(mainTableName)) {
                    mainMdTable = dbTablesMapping.get(mainTableName);
                    mainMdTable.setVerNo(new BigDecimal(1));
                    mdTableService.save(mainMdTable);
                    mdTablesMapping
                            .put(mainMdTable.getTableName(), mainMdTable); // 保存后将MdTable添加到映射中
                }

                if (!mdTablesMapping.containsKey(foreignTableName)) {// 表在MD_TABLE中不存在
                    foreignMdTable = dbTablesMapping.get(foreignTableName);
                    foreignMdTable.setVerNo(new BigDecimal(1));
                    mdTableService.save(foreignMdTable);
                    mdTablesMapping.put(foreignMdTable.getTableName(), foreignMdTable); // 保存后将MdTable添加到映射中
                }

                dbMdTableRelate.setMainTableId(mainMdTable.getId());
                dbMdTableRelate.setForeignTableId(foreignMdTable.getId());
                mdTableRelateService.save(dbMdTableRelate);
            }*/

            // 刷新表关系
            /*if (parameter != null) {
                tableNames = java.util.Arrays.asList(parameter
                        .get("tableNames").toString().split(","));
                for (int i = 0; i < tableNames.size(); i++) {
                    String tableName = tableNames.get(i).toUpperCase();
                    List<MdTableRelate> dbMdTableRelates = new ArrayList<MdTableRelate>();
                    List<MdTableRelate> preVerRelates = mdTableRelateService
                            .findLastVersionMdTableRelatePojosByTableName(tableName); // 数据库当前版本关联关系
                    for (MdTableRelate dbTableRelate : dbAllMdTableRelates) {
                        if (dbTableRelate.getMainTableName().equals(tableName)
                                || dbTableRelate.getForeignTableName().equals(
                                tableName)) {
                            dbMdTableRelates.add(dbTableRelate);
                        }
                    }
                    mdTableRelateService.refreshTableRelateMdMeta(
                            mdTablesMapping.get(tableName).getId(), tableName,
                            preVerRelates);
                }
            } else {
                for (MdTable dbTable : allTables) {
                    String tableName = dbTable.getTableName().toUpperCase();
                    List<MdTableRelate> dbMdTableRelates = new ArrayList<MdTableRelate>();
                    List<MdTableRelate> preVerRelates = mdTableRelateService
                            .findLastVersionMdTableRelatePojosByTableName(tableName); // 数据库当前版本关联关系
                    for (MdTableRelate dbTableRelate : dbAllMdTableRelates) {
                        if (dbTableRelate.getMainTableName().equals(tableName)
                                || dbTableRelate.getForeignTableName().equals(
                                tableName)) {
                            dbMdTableRelates.add(dbTableRelate);
                        }
                    }
                    mdTableRelateService
                            .refreshTableRelateMdMeta(
                                    mdTablesMapping.get(dbTable.getTableName())
                                            .getId(), dbTable.getTableName(),
                                    preVerRelates);
                }

            }*/
            ISysCacheConfigService sysCacheConfigService = ContextHolder.getSpringBean("sysCacheConfigService");
            sysCacheConfigService.reloadCacheByCacheServiceBeanName("mdTableService");
            sysCacheConfigService.reloadCacheByCacheServiceBeanName("mdFieldService");

            return result + "";

        }
    }

}
