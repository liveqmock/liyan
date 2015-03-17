/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.tablespace.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.component.metadata.adapter.IMdMetaAdapter;
import com.innofi.framework.pojo.metadata.MdTableSpace;
import com.innofi.component.metadata.tablespace.service.IMdTableSpaceService;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;

import org.springframework.stereotype.Component;

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
public class MdTableSpaceAction extends BaseActionImpl {

    @Resource
    private IMdTableSpaceService mdTableSpaceService;

    /**
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findMdTableSpaces(Page page, Map<String, Object> parameter) throws Exception {

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
            addPropertyFilter(filters, "id", parameter.get("id"), QueryConstants.EQUAL, true);
            addPropertyFilter(filters, "tbsId", parameter.get("tbsId"), QueryConstants.EQUAL, true);
            addPropertyFilter(filters, "tbsName", parameter.get("tbsName"), QueryConstants.LIKE, true);
            addPropertyFilter(filters, "tbsType", parameter.get("tbsType"), QueryConstants.EQUAL, true);
            addPropertyFilter(filters, "tbsManageMethod", parameter.get("tbsManageMethod"), QueryConstants.EQUAL, true);
            addPropertyFilter(filters, "tbsStatus", parameter.get("tbsStatus"), QueryConstants.EQUAL, true);
        }
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        mdTableSpaceService.findByPage_Filters(filters, orders, innofiPage);

    }

    @DataResolver
    public void saveMdTableSpaces(Collection<MdTableSpace> objs) {

        for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
            MdTableSpace mdTableSpace = (MdTableSpace) iterator.next();
            EntityState state = EntityUtils.getState(mdTableSpace);
            if (EntityState.NEW.equals(state)) {
                mdTableSpaceService.save(mdTableSpace);
            } else if (EntityState.MODIFIED.equals(state)) {
                mdTableSpaceService.update(mdTableSpace);
            } else if (EntityState.DELETED.equals(state)) {
                mdTableSpaceService.delete(mdTableSpace);
            }
        }
    }

    @Expose
    public String refreshMetaData(Map<String, Object> parameter) {
        IMdMetaAdapter mdMetaAdapter = getSpringBean("mdTableSpaceAdapter");
        return mdMetaAdapter.refreshMetaData(parameter);
    }

}
