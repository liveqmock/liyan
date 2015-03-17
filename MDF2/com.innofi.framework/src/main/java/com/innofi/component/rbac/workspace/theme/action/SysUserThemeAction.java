package com.innofi.component.rbac.workspace.theme.action;
import com.innofi.component.rbac.workspace.theme.pojo.SysUserTheme;
import com.innofi.component.rbac.workspace.theme.service.ISysUserThemeService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.spring.context.ContextHolder;


import java.util.ArrayList;
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
 * 功能/ 模块：todo
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 为dorado7界面维护操作的提供支持，实现SysUserTheme对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysUserThemeAction extends BaseActionImpl {
    @Resource
	private   ISysUserThemeService sysUserThemesevice;

    @DataProvider
    public void findSysUserThemes(Page page, Map<String, Object> parameter) throws Exception {

    List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

    	if(parameter != null){
    			    	addPropertyFilter(propertyFilters,"userCode",parameter.get("userCode"),parameter.get("qMuserCode").toString(),true);
								    	addPropertyFilter(propertyFilters,"infBarFrame",parameter.get("infBarFrame"),parameter.get("qMinfBarFrame").toString(),true);
								    	addPropertyFilter(propertyFilters,"className",parameter.get("className"),parameter.get("qMclassName").toString(),true);
								    	addPropertyFilter(propertyFilters,"isSysDefault",parameter.get("isSysDefault"),parameter.get("qMisSysDefault").toString(),true);
								    	addPropertyFilter(propertyFilters,"crtOrgCode",parameter.get("crtOrgCode"),parameter.get("qMcrtOrgCode").toString(),true);
												addDateRangePropertyFilter(propertyFilters, "crtDate", parameter);
						    	addPropertyFilter(propertyFilters,"updOrgCode",parameter.get("updOrgCode"),parameter.get("qMupdOrgCode").toString(),true);
								    	addPropertyFilter(propertyFilters,"id",parameter.get("id"),parameter.get("qMid").toString(),true);
								    	addPropertyFilter(propertyFilters,"userThemeName",parameter.get("userThemeName"),parameter.get("qMuserThemeName").toString(),true);
								    	addPropertyFilter(propertyFilters,"isFullScreen",parameter.get("isFullScreen"),parameter.get("qMisFullScreen").toString(),true);
								    	addPropertyFilter(propertyFilters,"systemCode",parameter.get("systemCode"),parameter.get("qMsystemCode").toString(),true);
								    	addPropertyFilter(propertyFilters,"status",parameter.get("status"),parameter.get("qMstatus").toString(),true);
								    	addPropertyFilter(propertyFilters,"crtUserCode",parameter.get("crtUserCode"),parameter.get("qMcrtUserCode").toString(),true);
								    	addPropertyFilter(propertyFilters,"updUserCode",parameter.get("updUserCode"),parameter.get("qMupdUserCode").toString(),true);
												addDateRangePropertyFilter(propertyFilters, "updDate", parameter);
						}

        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        sysUserThemesevice.findByPage_Filters(propertyFilters, null, innofiPage);

    }

    @DataResolver
    public void saveSysUserThemes(Collection<SysUserTheme> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
			SysUserTheme sysUserTheme = (SysUserTheme) iterator.next();
            EntityState state = EntityUtils.getState(sysUserTheme);
            if (EntityState.DELETED.equals(state)) {
            	sysUserThemesevice.delete(sysUserTheme);
            } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
            	sysUserThemesevice.update(sysUserTheme);
            } else if (EntityState.NEW.equals(state)) {
            	sysUserThemesevice.save(sysUserTheme);
            }
		}
    }
    
    @Expose
    public void saveMainTheme(Map parameter) {
        String mainTHeme = (String) parameter.get("mainTHeme");
        String isFullScreen = (String) parameter.get("isFullScreen");
    	sysUserThemesevice.saveMainTheme(mainTHeme,isFullScreen);
    }
    
    @DataProvider
    public SysUserTheme findUserTheme() {
    	SysUserTheme userTheme = ContextHolder.getContext().getLoginUser().getSysUserTheme();
        return userTheme;
    }
    
}
