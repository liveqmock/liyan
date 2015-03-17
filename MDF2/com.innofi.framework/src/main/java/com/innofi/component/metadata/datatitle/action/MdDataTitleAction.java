/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.datatitle.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.component.metadata.commons.MetadataConstants;
import com.innofi.framework.pojo.metadata.MdDataTitle;
import com.innofi.component.metadata.datatitle.service.IMdDataTitleService;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;

import org.apache.commons.lang.StringUtils;
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
public class MdDataTitleAction extends BaseActionImpl {

    @Resource
    public IMdDataTitleService mdDataTitleServcie;

    /**
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findMdDataTitles(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        List<String> dmdIdsList = new ArrayList<String>();
        String parentId = null;
        if (parameter != null) {
            if (parameter.get("dmdIds") != null) {
                dmdIdsList = java.util.Arrays.asList(parameter.get("dmdIds").toString().split(","));
                if (dmdIdsList.size() > 0) {
                    addPropertyFilter(filters, "id", dmdIdsList, QueryConstants.NOT_IN, true);
                }
            }
            addPropertyFilter(filters, "dataTitleId", parameter.get("dataTitleId"), QueryConstants.EQUAL, true);
            addPropertyFilter(filters, "dataTitleCode", parameter.get("dataTitleCode"), QueryConstants.LIKE, true);
            addPropertyFilter(filters, "dataTitleName", parameter.get("dataTitleName"), QueryConstants.LIKE, true);
            addPropertyFilter(filters, "status", parameter.get("status"), QueryConstants.EQUAL, true);
            parentId = (String) parameter.get("parentId");
            if (StringUtils.isBlank(parentId)) {
                parentId = MetadataConstants.TREE_ROOT;
            }
            addPropertyFilter(filters, "parentId", parentId, QueryConstants.EQUAL, true);
            
        }else{
            parentId = MetadataConstants.TREE_ROOT;                
            addPropertyFilter(filters, "parentId", parentId, QueryConstants.EQUAL, true);
        }
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        mdDataTitleServcie.findByPage_Filters(filters, orders, innofiPage);        
    }

    /**
     * 通过父ID查询数据信息
     *
     * @param parentId
     * @return
     * @throws Exception
     */
    @DataProvider
    public List<MdDataTitle> findMdDataTitlesByParentId(String parentId) throws Exception {
        if (StringUtils.isBlank(parentId)) {
            parentId = MetadataConstants.TREE_ROOT;
        }
        List<MdDataTitle> mdDataTitle = mdDataTitleServcie.findMdDataTitlesByParentId(parentId);
        return mdDataTitle;
    }

    @DataResolver
    public void saveMdDataTitles(Collection<MdDataTitle> objs) {

        for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
            MdDataTitle mdDataTitle = (MdDataTitle) iterator.next();
            EntityState state = EntityUtils.getState(mdDataTitle);
            Collection<MdDataTitle> children = EntityUtils.getValue(mdDataTitle, "children");
            if (children != null && children.size() > 0) {
                saveMdDataTitles(children);
            }
            if (EntityState.NEW.equals(state)) {
                mdDataTitleServcie.save(mdDataTitle);
            } else if (EntityState.MODIFIED.equals(state)) {
                mdDataTitleServcie.update(mdDataTitle);
            } else if (EntityState.DELETED.equals(state)) {
                mdDataTitleServcie.deleteDataTitle(mdDataTitle);
            }
        }
    }
}