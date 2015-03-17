package com.innofi.component.rbac.product.action;

import com.bstek.dorado.data.entity.EntityState;
import com.innofi.component.rbac.product.pojo.SysProduct;
import com.innofi.component.rbac.product.service.ISysProductService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 为dorado7界面维护操作的提供支持，实现SysProduct对象信息的加载与保存操作
 */

@Component
public class SysProductAction extends BaseActionImpl {
    @Resource
    private ISysProductService sysProductService;

    /**
     * 产品管理列表
     *
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findSysProducts(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        if (parameter != null) {
            addPropertyFilter(propertyFilters, "parentId", parameter.get("parentId"), QueryConstants.EQUAL, true);
            addPropertyFilter(propertyFilters, "productCode", parameter.get("productCode"), QueryConstants.EQUAL, true);
            addPropertyFilter(propertyFilters, "productName", parameter.get("productName"), QueryConstants.EQUAL, true);
            addPropertyFilter(propertyFilters, "manageOrgCode", parameter.get("manageOrgCode"), QueryConstants.EQUAL, true);
            addPropertyFilter(propertyFilters, "busilineId", parameter.get("busilineId"), QueryConstants.EQUAL, true);
        }
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysProductService.findByPage_Filters(propertyFilters, null, innofiPage);
    }

    /**
     * 产品管理树子节点
     *
     * @param parentId
     * @return
     * @throws Exception
     */
    @DataProvider
    public List<SysProduct> findSysProductByParentId(String parentId) throws Exception {
        return sysProductService.findByProperty("parentId", parentId, QueryConstants.EQUAL);
    }

    /**
     * 产品下拉框树子节点
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    @DataProvider
    public List<SysProduct> findSysProducts(Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> filters = this.buildEqPropertyFilters(parameter);
        return sysProductService.findByPropertyFilters(filters, null);
    }

    /**
     * 产品下拉框列表
     *
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findSysProductList(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> propertyFilters = super.buildEqPropertyFilters(parameter);
        this.removePropertyFilter(propertyFilters, "searchType");
        addPropertyFilter(propertyFilters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);//状态有效
        addPropertyFilter(propertyFilters, "productName", parameter.get("productName"), QueryConstants.LIKE, true);
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysProductService.findSysProductList((String) parameter.get("searchType"), (String) parameter.get("parentId"), propertyFilters, innofiPage);
    }

    @DataResolver
    public void saveSysProducts(Collection<SysProduct> objs) {
        for (SysProduct line : objs) {
            Collection<SysProduct> children = EntityUtils.getValue(line, "children");
            if (children != null && children.size() > 0) {
                saveSysProducts(children);
            }
            EntityState state = EntityUtils.getState(line);
            if (EntityState.DELETED.equals(state)) {
                sysProductService.delete(line);
            } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
                sysProductService.update(line);
            } else if (EntityState.NEW.equals(state)) {
                sysProductService.save(line);
            }
        }
    }

    /**
     * 检查编号
     */
    @Expose
    public String checkProductCode(String code) {
        return sysProductService.checkEntityExist("productCode", code) + "";
    }

    /**
     * 产品停用
     */
    @Expose
    public String invalidProduct(String code) {
        return sysProductService.invalidProduct(code);
    }
}
