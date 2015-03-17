package com.innofi.component.metadata.table.action;

import com.innofi.component.metadata.adapter.IMdMetaAdapter;
import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.component.metadata.table.service.IMdTableService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import com.innofi.framework.spring.context.ContextHolder;
import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;

/**
 * 为dorado7界面维护操作的提供支持，实现${model.modelCnName}对象信息的加载与保存操作
 */

@Component
public class MdTableAction extends BaseActionImpl {

    @Resource
    private IMdTableService mdTableServcie;

    @DataProvider
    public void findMdTables(Page page, Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        PropertyOrder propertyOrder = new PropertyOrder("verNo", "desc");
    	PropertyOrder propertyOrder2 = new PropertyOrder("tableName","asc");
        orders.add(propertyOrder);
    	orders.add(propertyOrder2);
        List<String> dmdIdsList = new ArrayList<String>();
        if (parameter != null) {
            addPropertyFilter(filters, "id", parameter.get("id"), QueryConstants.EQUAL, true);
            addPropertyFilter(filters, "systemId", parameter.get("systemId"), QueryConstants.LIKE, true);
            addPropertyFilter(filters, "tableCnName", parameter.get("tableCnName"), QueryConstants.LIKE, true);
            addPropertyFilter(filters, "tableType", parameter.get("tableType"), QueryConstants.EQUAL, true);
            addPropertyFilter(filters, "dataStatus", parameter.get("dataStatus"), QueryConstants.EQUAL, true);
            addPropertyFilter(filters, "tbsName", parameter.get("tbsName"), QueryConstants.EQUAL, true);
            addPropertyFilter(filters, "tableName", parameter.get("tableName") == null?null:((String)parameter.get("tableName")).toUpperCase(), QueryConstants.LIKE, true);
    		addPropertyFilter(filters,"tableDesc",parameter.get("tableDesc"),QueryConstants.LIKE,true);//业务模块查询
    		addPropertyFilter(filters,"isDaControl",parameter.get("isDaControl"),QueryConstants.EQUAL,true);
    		addPropertyFilter(filters,"isMaintain",parameter.get("isMaintain"),QueryConstants.EQUAL,true);

            if (parameter.get("dmdIds") != null) {
                dmdIdsList = java.util.Arrays.asList(parameter.get("dmdIds").toString().split(","));
                if (dmdIdsList.size() > 0) {
                    addPropertyFilter(filters, "id", dmdIdsList, QueryConstants.NOT_IN, true);
                }
            }

            //当查看方式为look.表空间名称为equal查询.
            if ((parameter.get("lookStyle") != null && "look".equals(parameter.get("lookStyle"))) || (parameter.get("sign") != null && "EQUAL".equals(parameter.get("sign")))) {
                addPropertyFilter(filters, "tbsName", parameter.get("tbsName"), QueryConstants.EQUAL, true);
            }
            //表在做查询的时候.如果标记为equal为等于查询的时候.
            if (parameter.get("sign") != null && "EQUAL".equals(parameter.get("sign"))) {
                addPropertyFilter(filters, "tableName", parameter.get("tableName"), QueryConstants.EQUAL, true);
                addPropertyFilter(filters, "status", parameter.get("status"), QueryConstants.EQUAL, true);
            } else if (parameter.get("status") != null) {
                addPropertyFilter(filters, "status", parameter.get("status"), QueryConstants.EQUAL, true);
            } else {
                addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
            }
            //用于版本管理的条件。传入两个tableId
            if (parameter.get("tableIds") != null) {
                List<String> tableIdList = new ArrayList<String>();
//                List<String> statusList = new ArrayList<String>();
//                statusList.add("1");
//                statusList.add("0");
                tableIdList = java.util.Arrays.asList(parameter.get("tableIds").toString().split(","));
                addPropertyFilter(filters, "id", tableIdList, QueryConstants.IN, true);
//                addPropertyFilter(filters, "status", statusList, QueryConstants.IN, true);
            }
        }
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        mdTableServcie.findByPage_Filters(filters, orders, innofiPage);

    }

    @DataResolver
    public void saveMdTables(Collection<MdTable> objs) {
        for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
            MdTable mdTable = (MdTable) iterator.next();
            EntityState state = EntityUtils.getState(mdTable);
            if (EntityState.NEW.equals(state)) {
                mdTableServcie.save(mdTable);
            } else if (EntityState.MODIFIED.equals(state)) {
                mdTableServcie.updateForNewVersion(mdTable);
            } else if (EntityState.DELETED.equals(state)) {
                mdTableServcie.delete(mdTable);
            }
        }
    }

    /**
     * 通过uiName查找SavePath
     *
     * @param parameter
     * @return
     */
    @Expose
    public String findTableNameByUiName(Map<String, Object> parameter) {
        String savePath = parameter.get("uiName").toString();
        return mdTableServcie.findLastVersionTableNameByUiName(savePath);
    }

    /**
     * 刷新表元数据，目前只针对数据库进行了实现
     *
     * @param parameter
     * @return
     */
    @Expose
    public String refreshMetaData(Map<String, Object> parameter) {
        IMdMetaAdapter adapter = ContextHolder.getSpringBean("mdTableAdapter");
        return adapter.refreshMetaData(parameter);
    }





}
