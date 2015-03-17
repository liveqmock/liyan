package com.innofi.component.metadata.storedproc.action;

import com.bstek.dorado.annotation.Expose;
import com.innofi.component.metadata.adapter.IMdMetaAdapter;
import com.innofi.framework.pojo.metadata.MdStoredProc;
import com.innofi.component.metadata.storedproc.service.IMdStoredProcService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;


import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 为dorado7界面维护操作的提供支持，实现MdStoredProc对象信息的加载与保存操作
 */

@Component
public class MdStoredProcAction extends BaseActionImpl {
    @Resource
    private IMdStoredProcService mdStoredProcSevice;

    @DataProvider
    public void findMdStoredProcs(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        List<String> dmdIdsList = new ArrayList<String>();
        if (parameter != null) {
            if (parameter.get("dmdIds") != null) {
                dmdIdsList = java.util.Arrays.asList(parameter.get("dmdIds").toString().split(","));
                if (dmdIdsList.size() > 0) {
                    addPropertyFilter(filters, "id", dmdIdsList, QueryConstants.NOT_IN, true);
                }
            }
            addPropertyFilter(propertyFilters, "id", parameter.get("id"), QueryConstants.EQUAL, true);
            addPropertyFilter(propertyFilters, "procName", parameter.get("procName"), QueryConstants.LIKE, true);
            addPropertyFilter(propertyFilters, "procDesc", parameter.get("procDesc"), QueryConstants.LIKE, true);
            addPropertyFilter(propertyFilters, "para", parameter.get("para"), QueryConstants.LIKE, true);
            addPropertyFilter(propertyFilters, "status", parameter.get("status"), QueryConstants.EQUAL, true);
        }

        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        mdStoredProcSevice.findByPage_Filters(propertyFilters, null, innofiPage);

    }

    @DataResolver
    public void saveMdStoredProcs(Collection<MdStoredProc> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
			MdStoredProc mdStoredProc = (MdStoredProc) iterator.next();
            EntityState state = EntityUtils.getState(mdStoredProc);
            if (EntityState.DELETED.equals(state)) {
            	mdStoredProcSevice.delete(mdStoredProc);
            } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
            	mdStoredProcSevice.update(mdStoredProc);
            } else if (EntityState.NEW.equals(state)) {
            	mdStoredProcSevice.save(mdStoredProc);
            }
		}
    }

    /**
     * 刷新存储过程元数据
     * @param parameter
     * @return
     */
    @Expose
    public String refreshMetaData(Map<String, Object> parameter) {
        IMdMetaAdapter mdMetaAdapter = getSpringBean("mdStoredProcAdapter");
        return mdMetaAdapter.refreshMetaData(parameter);
    }


}
