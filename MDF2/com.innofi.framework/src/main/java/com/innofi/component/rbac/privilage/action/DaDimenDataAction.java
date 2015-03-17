package com.innofi.component.rbac.privilage.action;

import com.innofi.framework.pojo.metadata.MdDimenField;
import com.innofi.component.rbac.privilage.pojo.DaDimenData;
import com.innofi.component.rbac.privilage.service.IDaDimenDataService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 功能/ 模块：todo
 *
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 为dorado7界面维护操作的提供支持，实现DaDimenData对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class DaDimenDataAction extends BaseActionImpl {
    @Resource
    private IDaDimenDataService daDimenDataSevice;

    @DataProvider
    public void findDaDimenDatas(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        if (parameter != null) {
            addPropertyFilter(propertyFilters, "dimenAuthType", parameter.get("dimenAuthType"), parameter.get("qMdimenAuthType").toString(), true);
            addPropertyFilter(propertyFilters, "fieldName", parameter.get("fieldName"), parameter.get("qMfieldName").toString(), true);
        }
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        daDimenDataSevice.findByPage_Filters(propertyFilters, null, innofiPage);
    }

    @DataProvider
    public List<DaDimenData> findDimenDatasForDimenControl(Map<String, Object> parameter) {
        String dimenControlId = (String) parameter.get("dimenControlId");
        String dimenFieldId = (String) parameter.get("dimenFieldId");
        if (StringUtils.isBlank(dimenControlId)) {
            return new ArrayList();
        }
        List<PropertyFilter> propertyFilters = this.buildEqPropertyFilters(parameter);
        return daDimenDataSevice.findDimenDatasForDimenControl(dimenControlId, dimenFieldId);
    }


    @DataProvider
    public List<DaDimenData> findDimenDatasForDataRestrict(String restrictId) {
        if (StringUtils.isBlank(restrictId)) {
            return new ArrayList();
        }
        return daDimenDataSevice.findDimenDatasForRestrict(restrictId);
    }


    @DataProvider
    public List<MdDimenField> findDimenFieldForDimenControl(Map<String, String> parameter) {
        String dimenControlId = parameter.get("dimenControlId");
        String dimenId = parameter.get("dimenId");
        return daDimenDataSevice.findDimenFieldForDimenControl(dimenControlId, dimenId);
    }

    @DataResolver
    public void saveDaDimenDatas(Collection<DaDimenData> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			DaDimenData daDimenData = (DaDimenData) iterator.next();
			EntityState state = EntityUtils.getState(daDimenData);
			if (EntityState.DELETED.equals(state)) {
				daDimenDataSevice.delete(daDimenData);
			} else if (EntityState.MODIFIED.equals(state)
					|| EntityState.MOVED.equals(state)) {
				daDimenDataSevice.update(daDimenData);
			} else if (EntityState.NEW.equals(state)) {
				daDimenDataSevice.save(daDimenData);
			}
		}
    }
}
