/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.tablerelate.service.impl;

import com.innofi.component.metadata.commons.MetadataConstants;
import com.innofi.component.metadata.tablerelate.service.IMdTableRelateService;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.metadata.MdTableRelate;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.utils.console.ConsoleUtil;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能/ 模块：测试模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Component(value = "mdTableRelateService")
public class MdTableRelateServiceImpl extends BaseServiceImpl<MdTableRelate, String> implements IMdTableRelateService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablerelate.service.IMdTableRelateService#findLastVersionMdTableRelatePojosByTableName(String)
     */
    public List<MdTableRelate> findLastVersionMdTableRelatePojosByTableName(String tableName) {
        List<MdTableRelate> relateList = new ArrayList<MdTableRelate>();
        IdfCodeTransfer idfCodeTransfer = getIdfCodeTransfer();
        ListOrderedMap mdTableRelateOrderedMap = idfCodeTransfer.getCacheObjects("mdTableRelateService");
        ListOrderedMap mdTableFRelateOrderedMap = idfCodeTransfer.getCacheObjects("mdTableRelateService");
        relateList.addAll(IdfCodeTransfer.listDistinctFilter(mdTableRelateOrderedMap, "mainTableName", tableName, null).valueList());
        relateList.addAll(IdfCodeTransfer.listDistinctFilter(mdTableFRelateOrderedMap, "foreignTableName", tableName, null).valueList());
        return relateList;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablerelate.service.IMdTableRelateService#findMdTableRelateByStatus(String)
     */
    public List<MdTableRelate> findMdTableRelateByStatus(String status) {
        return findByProperty("isCrt", status, QueryConstants.EQUAL);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablerelate.service.IMdTableRelateService#findMdTableRelateByMainTable(String)
     */
    public List<MdTableRelate> findMdTableRelateByMainTable(String mainTableName) {
        IdfCodeTransfer idfCodeTransfer = getIdfCodeTransfer();
        ListOrderedMap mdTableRelateOrderedMap = idfCodeTransfer.getCacheObjects("mdTableRelateService");
        return IdfCodeTransfer.listDistinctFilter(mdTableRelateOrderedMap, "mainTableName", mainTableName, null).valueList();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablerelate.service.IMdTableRelateService#findMdTableRelateByTableNameFormDb(String)
     */
    public List<MdTableRelate> findMdTableRelateByTableNameFormDb(String tableName) {
        String lowerName = tableName.toLowerCase();
        String upperName = tableName.toUpperCase();
        Map<String, String> params = new HashMap<String, String>();
        params.put("lowerName", lowerName);
        params.put("upperName", upperName);
        params.put("tableName", tableName);
        return dynamicQueryBeanForList("metadata-dynamic-statement", "findIdxInfo", params); //获取数据库中最新的表关系数据!
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablerelate.service.IMdTableRelateService#removeRelatePMdMeta(String) {
     */
    public void removeRelatePMdMeta(String tableId) {
    	getDaoSupport().getJdbcDao().execute("delete from MD_TABLE_RELATE where (main_table_id='" + tableId + "' or foreign_table_id='" + tableId + "') and is_crt='" + MetadataConstants.IS_CRT_P + "'");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablerelate.service.IMdTableRelateService#refreshTableRelateMdMeta(String, String, List) {
     */
    public void refreshTableRelateMdMeta(String tableId, String tableName, List<MdTableRelate> preVerMdTableRelates) {
        ConsoleUtil.info("current refresh tablerelate [" + tableName + "]");
        if (preVerMdTableRelates.size() != 0) {
            String hql = "from MdTableRelate relate where relate.mainTableName = ? and relate.isCrt = ? ";

            List<MdTableRelate> mainTableRelates = findByHql(hql, tableName, MetadataConstants.IS_CRT_L);//作为主表查找逻辑外键
            for (MdTableRelate mdTableRelate : mainTableRelates) {
                mdTableRelate.setMainTableId(tableId);
                update(mdTableRelate);
            }

            hql = "from MdTableRelate relate where relate.foreignTableName = ? and relate.isCrt = ? ";//作为从表查找逻辑外键
            List<MdTableRelate> foreignTableRelates = findByHql(hql, tableName, MetadataConstants.IS_CRT_L);

            for (MdTableRelate mdTableRelate : foreignTableRelates) {
                mdTableRelate.setForeignField(tableId);
                update(mdTableRelate);
            }
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablerelate.service.IMdTableRelateService#findMdTableRelatePojosByIsCrt(String)
     */
    public List<MdTableRelate> findMdTableRelatePojosByIsCrt(String isCrt) {
        return findByProperty("isCrt", isCrt, QueryConstants.EQUAL);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablerelate.service.IMdTableRelateService#checkMetaModelRelate(String mainTableName, String foreignTableName)
     */
    public String checkMetaModelRelate(String mainTableName, String foreignTableName) {
        int countNum = getDaoSupport().getJdbcDao().queryForInt("select count(*) from md_table_relate where main_table_name = '" + mainTableName + "' and FOREIGN_TABLE_NAME = '" + foreignTableName + "'");
        if (countNum > 0) {
            return MetadataConstants.SUCCESS;
        } else {
            return MetadataConstants.FAILED;
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablerelate.service.IMdTableRelateService#findAllMdTableRelateFormDb()
     */
    public List<MdTableRelate> findAllMdTableRelateFormDb() {
        Map<String,String> params = new HashMap<String,String>();
        return new ArrayList();//dynamicQueryBeanForList("metadata-dynamic-statement", "findAllIdxInfo", params); //获取数据库中最新的表关系数据!;
    }

}
