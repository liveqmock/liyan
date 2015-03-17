package com.innofi.component.rbac.workspace.infobar.action;

import com.innofi.component.rbac.workspace.infobar.pojo.SysInfBar;
import com.innofi.component.rbac.workspace.infobar.service.ISysInfBarService;
import com.innofi.component.rbac.workspace.infobar.service.ISysUserInfBarService;
import com.innofi.component.rbac.workspace.theme.pojo.SysUserTheme;
import com.innofi.component.rbac.workspace.theme.service.ISysUserThemeService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;


/**
 * 功能/ 模块：todo
 *
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 为dorado7界面维护操作的提供支持，实现SysInfBar对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysInfBarAction extends BaseActionImpl {
    @Resource
    private ISysInfBarService sysInfBarService;
    @Resource
    private ISysUserThemeService sysUserThemeService;
    @Resource
    private ISysUserInfBarService sysUserInfBarService;

    @DataProvider
    public void findSysInfBars(Page page, Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

        if (parameter != null) {
            addPropertyFilter(propertyFilters, "infBarName", parameter.get("infBarName"), parameter.get("qMinfBarName").toString(), true);
            addPropertyFilter(propertyFilters, "infBarTitle", parameter.get("infBarTitle"), parameter.get("qMinfBarTitle").toString(), true);
        }

        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        sysInfBarService.findByPage_Filters(propertyFilters, null, innofiPage);

    }

    @DataProvider
    public void findUserInfBars(Page page, Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        SysUserTheme sysUserTheme = sysUserThemeService.getCurrentUserTheme();
        String userThemeId = sysUserTheme.getId();

        List<SysInfBar> infBars = sysInfBarService.findByHql(" from SysInfBar where id not in(select infBarId from SysUserInfBar where userThemeId=?)", userThemeId);
        if (parameter != null) {
            addPropertyFilter(propertyFilters, "infBarName", parameter.get("infBarName"), parameter.get("qMinfBarName").toString(), true);
            addPropertyFilter(propertyFilters, "infBarTitle", parameter.get("infBarTitle"), parameter.get("qMinfBarTitle").toString(), true);
        }
        List<Object> infBarIds = sysInfBarService.getPropertyValueList(infBars, sysInfBarService.getDaoSupport().getHibernateDao().getIdPropertyName(sysInfBarService.getEntityType()));
        addPropertyFilter(propertyFilters, "id",infBarIds , "in", true);
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysInfBarService.findByPage_Filters(propertyFilters, null, innofiPage);
    }

    /*
     * 添加信息栏
     */
    @DataResolver
    public void saveSysInfBars(Collection<SysInfBar> objs) {
        sysInfBarService.saveSysInfBars(objs);
    }

    /**
     * 保存主题版式
     */
    @Expose
    public void saveTheme(Map parameter) throws Exception {
        sysInfBarService.saveTheme(parameter);
    }

    /**
     * 保存主题版式
     */
    @Expose
    public void saveUserInfbar(String parameter) throws Exception {
        sysInfBarService.saveUserInfbar(parameter);
    }

    /**
     * 移除portal
     */
    @Expose
    public void removePortal(String parameter) throws Exception {

        sysInfBarService.removePortal(parameter);
    }
}
