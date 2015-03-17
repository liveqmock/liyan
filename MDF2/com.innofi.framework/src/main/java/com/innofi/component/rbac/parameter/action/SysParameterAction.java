package com.innofi.component.rbac.parameter.action;

import com.innofi.component.rbac.notice.pojo.SysNoticeContentRole;
import com.innofi.component.rbac.parameter.pojo.SysParameter;
import com.innofi.component.rbac.parameter.service.ISysParameterService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 为dorado7界面维护操作的提供支持，实现SysParameter对象信息的加载与保存操作
 */

@Component
public class SysParameterAction extends BaseActionImpl {

    @Resource
    private ISysParameterService sysParameterService;

    @DataProvider
    public void findSysParameters(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> propertyFilters = this.buildEqPropertyFilters(parameter);
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysParameterService.findByPage_Filters(propertyFilters, null, innofiPage);
    }

    @DataResolver
    public void saveSysParameters(Collection<SysParameter> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			SysParameter sysParameter = (SysParameter) iterator.next();
			EntityState state = EntityUtils.getState(sysParameter);
			if (EntityState.DELETED.equals(state)) {
				sysParameterService.delete(sysParameter);
			} else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
				sysParameterService.update(sysParameter);
			} else if (EntityState.NEW.equals(state)) {
				sysParameterService.save(sysParameter);
			}
		}
    }

    @Expose
    public String checkCode(Map<String, String> parameter) {
        String paraCode = parameter.get("paraCode");
        return sysParameterService.checkCode(paraCode) + "";
    }

    @Expose
    public String updateSysParameterByParaCode(Map<String, String> parameter) {
        return sysParameterService.updateParamValue(parameter);
    }

    @Expose
    public void loadNewParameter() {
        sysParameterService.loadNewParameter();
    }

}
