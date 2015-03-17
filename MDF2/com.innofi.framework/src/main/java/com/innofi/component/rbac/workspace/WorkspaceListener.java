/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.rbac.workspace;

import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.widget.base.IFrame;
import com.bstek.dorado.view.widget.base.toolbar.ToolBar;
import com.bstek.dorado.view.widget.portal.Portal;
import com.bstek.dorado.view.widget.portal.Portlet;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.component.rbac.workspace.infobar.pojo.SysInfBar;
import com.innofi.component.rbac.workspace.infobar.pojo.SysUserInfBar;
import com.innofi.component.rbac.workspace.infobar.service.ISysInfBarService;
import com.innofi.component.rbac.workspace.infobar.service.ISysUserInfBarService;
import com.innofi.component.rbac.workspace.theme.pojo.SysUserTheme;
import com.innofi.component.rbac.workspace.theme.service.ISysUserThemeService;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.spring.context.ContextHolder;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 功能/ 模块：工作台展现监听器
 *
 * @author 蒲玄国 steve.pu@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class WorkspaceListener extends GenericObjectListener<ViewConfig> {


    @Resource
    private ISysInfBarService sysInfBarsevice;
    @Resource
    private ISysUserThemeService sysUserThemeSevice;
    @Resource
    private ISysUserInfBarService sysUserInfBarsevice;


    @Override
    public boolean beforeInit(ViewConfig arg0) throws Exception {
        return true;
    }


    public void onInit(ViewConfig viewConfig) throws Exception {
        View view = viewConfig.getView();
        SysUser user = ContextHolder.getContext().getLoginUser();
        String userCode = user.getUserCode();
        ToolBar toolBar = (ToolBar) view.getViewElement("toolBarControl");
        List<SysUserInfBar> sysUserInfBars = new ArrayList<SysUserInfBar>();
        SysUserTheme sysUserTheme = sysUserThemeSevice.getCurrentUserTheme();
        if (ContextHolder.getSystemProperties().getBoolean("mdf.workspace.inofibar.use.default", false)) {
            if ("admin".equals(userCode)) {
                toolBar.setIgnored(false);
            } else {
                toolBar.setIgnored(true);
            }
        } else {
            toolBar.setIgnored(false);
        }

        sysUserInfBars = sysUserInfBarsevice.findByProperty("userThemeId", sysUserTheme.getId(), QueryConstants.EQUAL);
        int index = 0;
        Portal portal = (Portal) view.getViewElement("portal");
        for (SysUserInfBar sysUserInfBar : sysUserInfBars) {
            SysInfBar sysInfBar = sysInfBarsevice.get(sysUserInfBar.getInfBarId());
            Portlet portlet = new Portlet();
            portlet.setCaption(sysInfBar.getInfBarTitle());
            portlet.setIcon(sysInfBar.getInfBarPict());
            portlet.setId("port" + index);
            portlet.setColumn(sysUserInfBar.getSeq());
            portlet.setTags(sysInfBar.getId());
            portlet.setCloseable(false);
            
            IFrame iframe = new IFrame();
            iframe.setId("sub" + index);
            iframe.setPath(sysInfBar.getInfBarUrl());
            portlet.addChild(iframe);
            portal.addPortlet(portlet);
            index++;
        }
        portal.addClientEventListener(
                "onReady",
                new DefaultClientEvent(
                        "var index = " + sysUserTheme.getInfBarFrame() + ", setting;" +
                                "switch (index) { " +
                                "case 0:setting = [{ width: '25%'}, {}, { width: '30%'}];break;" +
                                "case 1:setting = [{}, {}, {}];break;" +
                                "case 2:setting = [{}, { width: '30%' }];break;" +
                                "case 3:setting = [{ width: '30%' }, { }];break;" +
                                "case 4:setting = [{ }, { }];break;}" +
                                "self.set('columns', setting);"

                ));
    }

}
