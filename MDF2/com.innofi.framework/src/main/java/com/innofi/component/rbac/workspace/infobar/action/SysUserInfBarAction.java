package com.innofi.component.rbac.workspace.infobar.action;
import com.innofi.component.rbac.workspace.infobar.pojo.SysUserInfBar;
import com.innofi.component.rbac.workspace.infobar.service.ISysUserInfBarService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 功能/ 模块：todo
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 为dorado7界面维护操作的提供支持，实现SysUserInfBar对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysUserInfBarAction extends BaseActionImpl {
    @Resource
	private   ISysUserInfBarService sysUserInfBarsevice;

    @DataProvider
    public void findSysUserInfBars(Page page, Map<String, Object> parameter) throws Exception {

    List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

    	if(parameter != null){
    			    	addPropertyFilter(propertyFilters,"userThemeId",parameter.get("userThemeId"),parameter.get("qMuserThemeId").toString(),true);
								    	addPropertyFilter(propertyFilters,"id",parameter.get("id"),parameter.get("qMid").toString(),true);
								    	addPropertyFilter(propertyFilters,"infBarId",parameter.get("infBarId"),parameter.get("qMinfBarId").toString(),true);
								    	addPropertyFilter(propertyFilters,"seq",parameter.get("seq"),parameter.get("qMseq").toString(),true);
								}

        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        sysUserInfBarsevice.findByPage_Filters(propertyFilters, null, innofiPage);

    }

    @DataResolver
    public void saveSysUserInfBars(Collection<SysUserInfBar> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
			SysUserInfBar sysUserInfBar = (SysUserInfBar) iterator.next();
            EntityState state = EntityUtils.getState(sysUserInfBar);
            if (EntityState.DELETED.equals(state)) {
            	sysUserInfBarsevice.delete(sysUserInfBar);
            } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
            	sysUserInfBarsevice.update(sysUserInfBar);
            } else if (EntityState.NEW.equals(state)) {
            	sysUserInfBarsevice.save(sysUserInfBar);
            }
		}
    }
}
