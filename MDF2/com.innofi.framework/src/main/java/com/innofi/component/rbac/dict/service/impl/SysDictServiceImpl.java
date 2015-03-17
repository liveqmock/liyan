
package com.innofi.component.rbac.dict.service.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.innofi.framework.dao.hibernate.PropertyOrder;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.innofi.component.rbac.dict.pojo.SysDict;
import com.innofi.component.rbac.dict.service.ISysDictService;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value = "sysDictService")
public class SysDictServiceImpl extends BaseServiceImpl<SysDict, String> implements ISysDictService {

    @Resource(name = "sysDictDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport() {
        return daoSupport;
    }

    public void findSysDictsList(String searchType,
                                 String parentDictId, List<PropertyFilter> filters, Page innofiPage) {
        if ("tree".equals(searchType) && !StringUtils.isBlank(parentDictId)) {
            SysDict dict = this.get(parentDictId);
            this.removePropertyFilter(filters, "parentDictId");
            this.addPropertyFilter(filters, "dictType", dict.getDictType(), QueryConstants.EQUAL, true);
            this.addPropertyFilter(filters, "id", parentDictId, QueryConstants.NOT_EQUAL, true);
            this.addPropertyFilter(filters, "treeSeq", "." + dict.getDictCode() + ".", QueryConstants.LIKE, true);
            findByPage_Filters(filters, null, innofiPage);
        } else {
            findByPage_Filters(filters, null, innofiPage);
        }
    }

    public boolean checkDictCode(String dictCode, String dictType) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "dictCode", dictCode, QueryConstants.EQUAL, true);
        this.addPropertyFilter(filters, "dictType", dictType, QueryConstants.EQUAL, true);
        List<SysDict> list = this.findByPropertyFilters(filters, null);
        if (list.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<SysDict> loadCacheObjects() {
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("viewSeq", QueryConstants.ORDER_ASC));
        return this.getAll(orders);
    }

    public boolean checkDictName(String dictName, String dictType) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "dictName", dictName, QueryConstants.EQUAL, true);
        this.addPropertyFilter(filters, "dictType", dictType, QueryConstants.EQUAL, true);
        List<SysDict> list = this.findByPropertyFilters(filters, null);
        if (list.size() > 0) {
            return false;
        }
        return true;
    }


    public String getCnFieldName() {
        return "dictName";
    }

}

