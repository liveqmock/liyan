/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.rbac.index.listener;

import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.desktop.*;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.widget.base.IFrame;
import com.bstek.dorado.view.widget.base.accordion.Accordion;
import com.bstek.dorado.view.widget.base.accordion.Section;
import com.bstek.dorado.view.widget.base.menu.Menu;
import com.bstek.dorado.view.widget.base.menu.MenuItem;
import com.bstek.dorado.view.widget.base.toolbar.Button;
import com.bstek.dorado.view.widget.base.toolbar.Fill;
import com.bstek.dorado.view.widget.base.toolbar.Separator;
import com.bstek.dorado.view.widget.base.toolbar.ToolBar;
import com.bstek.dorado.view.widget.blockview.BlockView;
import com.bstek.dorado.web.DoradoContext;
import com.innofi.framework.exception.FrameworkRuntimeException;
import com.innofi.component.rbac.menu.pojo.SysMenu;
import com.innofi.component.rbac.menu.service.ISysMenuService;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;
import com.innofi.framework.utils.variable.VariableHelper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能/ 模块：桌面首页监听器
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          首页视图监听器
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component("desktopViewListener")
public class DesktopViewListener {

    public static final String MENU_PAGE_URL = "com.innofi.component.rbac.index.defaultweb.Menu.d";

    private boolean showMaxAble = false; //是否显示最大化选项
    private boolean showMinAble = true; //是否显示最小化选项
    private boolean showCenter = true;  //是否居中显示
    private boolean maximized = false;  //是否最大化显示
    private int appWidth = 500;       //桌面模式默认窗口宽度
    private int appHeight = 300;      //桌面模式默认窗口高度
    

    
    public void onViewInit(View view){
        String clientWidth = ContextHolder.getRequest().getParameter("clientWidth");                                    //客户端宽度
        String clientHeight = ContextHolder.getRequest().getParameter("clientHeight");                                  //客户端高度
        if (Validator.isNotEmpty(clientWidth)) {
            appWidth = (int) (VariableHelper.parseInt(clientWidth) * 0.66);}                                             //设置app宽度
        if (Validator.isNotEmpty(clientHeight)) {                                                                        //设置app高度
            appHeight = (int) (VariableHelper.parseInt(clientHeight) * 0.73);}
        ISysMenuService menuService = ContextHolder.getSpringBean("sysMenuService");                                    //获取菜单服务
        List<SysMenu> desktopApps = menuService.findApps();                                                             //查询app菜单
        Menu rightMenu = (Menu) view.getViewElement("rightMenu");                                                       //菜单
        List<App> apps = new ArrayList<App>();                                                                          //保存shell中所有应用
        
        Shell mainShell = (Shell) view.getViewElement("shell");                                                         //获取Shell对象
        DesktopCarousel desCar = (DesktopCarousel) view.getViewElement("desCar");                                       //获取DesktopCarousel对象
        List<Desktop> desktops = new ArrayList<Desktop>();                                                              //获取桌面对象列表
        for(SysMenu desktopApp : desktopApps){
        	StringBuffer script = new StringBuffer("var desktopCar = view.get('#desCar'); var desktop = desktopCar.getControl('"+desktopApp.getMenuCode()+"'); desktopCar.set('currentControl',desktop);");//单击事件脚本
        	Desktop desktop = new Desktop();
            desktop.setId(desktopApp.getMenuCode());
            desktop.setNavtip(desktopApp.getMenuLabel());
            desktop.setIconSize(ShortcutIconSize.small);
            desktops.add(desktop);
            List<SysMenu> modules = menuService.findModules(desktopApp.getId());                                        //查询功能模块
            List<Shortcut> shortcuts = new ArrayList<Shortcut>();														//保存桌面快捷方式
            MenuItem item = new MenuItem();
            item.setCaption(desktopApp.getMenuLabel());
            item.setName(desktopApp.getMenuCode());																		
            setIcon16(desktopApp.getMenuPict(), item);																	//设置图标
            item.addClientEventListener("onClick", new DefaultClientEvent(script.toString()));							//添加事件
            rightMenu.addItem(item);                                                                                    //设置开始菜单

            
            for (SysMenu module : modules) {
                Shortcut shortcut = new Shortcut();
                shortcut.setAppId(module.getMenuCode());                                                                //创建桌面快捷方式
                if(Validator.isNotNull(module.getMenuTip())){
                    String position[] = StringUtil.split(module.getMenuTip(), ",");
                	shortcut.setRow(VariableHelper.parseInt(position[0]));
                    shortcut.setColumn(VariableHelper.parseInt(position[1]));
                }
                shortcuts.add(shortcut);
                String pict = module.getMenuPict();

                IFrameApp app = new IFrameApp();
                if (Validator.isNull(module.getMenuCode())) {
                    throw new FrameworkRuntimeException("菜单" + module.getMenuName() + "menuCode为空，请检查!");
                }

                app.setId(module.getMenuCode());
                app.setCaption(module.getMenuLabel());
                app.setIcon32(module.getMenuPict());
                setIcon16(pict, app);
                app.setPath(module.getMenuUrl());
                app.setMaximizeable(showMaxAble);
                app.setMinimizeable(showMinAble);
                app.setWidth(appWidth);
                app.setHeight(appHeight);
                app.setCenter(showCenter);
                app.setMaximized(maximized);
                app.setShowTaskButton(true);
                app.setTop(30);
                app.setLeft(240);
                apps.add(app);

/*                if (sysMenu.getMenuUrl() != null && sysMenu.getMenuUrl().startsWith("com")) {
                    ConsoleUtil.info(" shell open menu url is [" + sysMenu.getMenuUrl() + "]");
                    script = new StringBuilder();
                    script.append("var shell = view.get('#shell');");
                    script.append("shell.launchApp(self.get('name'));");
                } else {
                    script = new StringBuilder();
                    ConsoleUtil.info(" window open menu url is [" + sysMenu.getMenuUrl() + "]");
                    script.append("window.open('" + sysMenu.getMenuUrl() + "','_blank');");
                }
                item.addClientEventListener("onClick", new DefaultClientEvent(script.toString()));*/

            }
            desktop.setItems(shortcuts);
        }
        List<App> viewDefApp = mainShell.getApps();
        for(App app : viewDefApp){
        	if(app instanceof IFrameApp){
        		IFrameApp frameApp = (IFrameApp) app;
        		frameApp.setWidth(appWidth);
        		frameApp.setHeight(appHeight);
        	}
        }
        
        mainShell.getApps().addAll(apps);
        desCar.setControls(desktops);

        
        StringBuilder script = new StringBuilder();










       /* Desktop desktop = desktops.get(0);//桌面
        Menu rightMenu = (Menu) view.getViewElement("rightMenu");//菜单


        List<Shortcut> shortcuts = new ArrayList<Shortcut>();
        List<App> apps = new ArrayList<App>();
        for (SysMenu sysMenu : apps) {
            Shortcut shortcut = new Shortcut();
            shortcut.setAppId(sysMenu.getMenuCode());
            shortcuts.add(shortcut);
            String pict = sysMenu.getMenuPict();

            IFrameApp app = new IFrameApp();
            if (Validator.isNull(sysMenu.getMenuCode())) {
                throw new FrameworkRuntimeException("菜单" + sysMenu.getMenuName() + "menuCode为空，请检查!");
            }

            app.setId(sysMenu.getMenuCode());
            app.setCaption(sysMenu.getMenuLabel());
            app.setIcon32(sysMenu.getMenuPict());
            setIcon16(pict, app);
            app.setPath(sysMenu.getMenuUrl());
            app.setMaximizeable(showMaxAble);
            app.setMinimizeable(showMinAble);
            app.setWidth(200);
            app.setHeight(200);
            app.setCenter(showCenter);
            app.setMaximized(maximized);


            apps.add(app);

            MenuItem item = new MenuItem();
            item.setCaption(sysMenu.getMenuLabel());
            item.setName(sysMenu.getMenuCode());
            setIcon16(pict, item);

            if (sysMenu.getMenuUrl() != null && sysMenu.getMenuUrl().startsWith("com")) {
                ConsoleUtil.info(" shell open menu url is [" + sysMenu.getMenuUrl() + "]");
                script = new StringBuilder();
                script.append("var shell = view.get('#shell');");
                script.append("shell.launchApp(self.get('name'));");
            } else {
                script = new StringBuilder();
                ConsoleUtil.info(" window open menu url is [" + sysMenu.getMenuUrl() + "]");
                script.append("window.open('" + sysMenu.getMenuUrl() + "','_blank');");
            }
            item.addClientEventListener("onClick", new DefaultClientEvent(script.toString()));
            rightMenu.addItem(item);

        }
        desktop.getItems().addAll(0, shortcuts);
        mainShell.getApps().addAll(apps);*/
    }

    public void onBlockViewInit(BlockView blockView){


    }

    /**
     * 初始化桌面Shell对象
     *
     * @param shell shell对象
     * @throws Exception
     */
    public void onShellInit(Shell mainShell) {

    }


    /**
     * 主界面初始化左侧二级菜单
     *
     * @param secondMenuAccordion
     */
    public void onAccordionInit(Accordion secondMenuAccordion) {
        ISysMenuService menuService = ContextHolder.getSpringBean("sysMenuService");
        String appId = (String) ContextHolder.getContext().getDoradoContext().getAttribute("appId");

        HttpServletRequest request = DoradoContext.getCurrent().getRequest();

        if (Validator.isNotNull(appId)) {
            List<SysMenu> sysMenus = menuService.findModules(appId);
            for (SysMenu sysMenu : sysMenus) {

                Section section = new Section();
                section.setTags(sysMenu.getId());
                section.setCaption(sysMenu.getMenuLabel());
                section.setName(sysMenu.getMenuCode());
                String pict = sysMenu.getMenuPict();
                setIcon16(pict, section);

                StringBuilder script = new StringBuilder();

                if (Validator.isNull(sysMenu.getMenuUrl())) {
                    IFrame iFrame = new IFrame();
                    iFrame.setId(sysMenu.getMenuCode());
                    iFrame.setPath(MENU_PAGE_URL + "?parentMenuId=" + sysMenu.getId() + "&time=" + (int) (Math.random() * 1000));
                    section.setControl(iFrame);
                } else if (!sysMenu.getMenuUrl().startsWith("com")) {
                    script = new StringBuilder();
                    script.append("window.open('" + sysMenu.getMenuUrl() + "','_blank');");
                    section.addClientEventListener("onCaptionClick", new DefaultClientEvent(script.toString()));
                } else {
                    script.append("var tabset = top.getTabSet();\n" +
                            "\t\tvar frameTab = tabset.getTab('menuId" + sysMenu.getId() + "');\n" +
                            "\t\tif(!frameTab){\n" +
                            "\t\t\ttabset.addTab({\n" +
                            "\t\t\t\t$type : 'IFrame',\n" +
                            "\t\t\t\tcaption : '" + sysMenu.getMenuName() + "',\n" +
                            "\t\t\t\tname : 'menuId" + sysMenu.getId() + "',\n" +
                            "\t\t\t\tcloseable : true,\n" +
                            "\t\t\t\tpath : '" + request.getContextPath() + "/" + sysMenu.getMenuUrl() + "',\n" +
                            "\t\t\t\ticon:'" + sysMenu.getMenuPict() + "'\n" +
                            "\t\t\t},tabset.get(\"tabs\").size,true);\n" +
                            "\t\t}\n" +
                            "\t\tvar index = tabset.get(\"currentIndex\");\n" +
                            "\t\tif(index > 3){\n" +
                            "\t\t\tvar tab = tabset.getTab(0);\n" +
                            "\t\t\ttab.close();\n" +
                            "\t\t}");
                    section.addClientEventListener("onCaptionClick", new DefaultClientEvent(script.toString()));
                }


                secondMenuAccordion.addSection(section);
            }
        }
    }


    /**
     * 初始化Index View appToolBar
     *
     * @param appToolBar 应用toolBar
     * @throws Exception
     */
    public void onAppToolBarInit(ToolBar appToolBar) {
        ISysMenuService menuService = ContextHolder.getSpringBean("sysMenuService");
        List<SysMenu> sysMenus = menuService.findApps();
        addMenuIdParam(sysMenus);

        StringBuilder script = null;

        Fill fill = new Fill();
        appToolBar.addItem(fill);

        for (SysMenu sysMenu : sysMenus) {
            Button button = new Button();
            button.setId(sysMenu.getMenuCode());
            button.setCaption(sysMenu.getMenuLabel());
            String pict = sysMenu.getMenuPict();
            setIcon16(pict, button);
            script = new StringBuilder();
            script.append("view.get('#mainWindow').set('caption', '" + sysMenu.getMenuLabel() + "');");
            script.append("view.get('#mainFrame').set('path', '" + sysMenu.getMenuUrl() + "');");
            script.append("view.get('#mainWindow').set('icon', '" + button.getIcon() + "');");
            button.addClientEventListener("onClick", new DefaultClientEvent(script.toString()));
            appToolBar.addItem(button);
        }

        //分隔符
        Separator separator = new Separator();
        appToolBar.addItem(separator);

        //设置
        script = new StringBuilder();
        script.append("var settingDialog = view.get('#settingDialog');");
        script.append("settingDialog.show();");
        Button button = new Button();
        button.setId("setting");
        button.setCaption("设置");
        button.setIcon(">icons/gears.png");
        button.addClientEventListener("onClick", new DefaultClientEvent(script.toString()));
        appToolBar.addItem(button);

        //签退
        script = new StringBuilder();
        script.append("window.location.href='loginAction.logout.c?logOut=true';");
        button = new Button();
        button.setId("logout");
        button.setCaption("签退");
        button.setIcon(">icons/logout.png");
        button.addClientEventListener("onClick", new DefaultClientEvent(script.toString()));
        appToolBar.addItem(button);
    }

    /**
     * 初始化ModuleIndex View moduleToolBar
     *
     * @param moduleToolBar 模块toolBar
     * @throws Exception
     */
    public void onModuleToolBarInit(ToolBar moduleToolBar) {
        ISysMenuService menuService = ContextHolder.getSpringBean("sysMenuService");
        String appId = (String) ContextHolder.getContext().getDoradoContext().getAttribute("appId");

        if (Validator.isNotNull(appId)) {
            List<SysMenu> sysMenus = menuService.findModules(appId);
            for (SysMenu sysMenu : sysMenus) {
                Button button = new Button();
                button.setId(sysMenu.getMenuCode());
                button.setTags(sysMenu.getId());
                button.setCaption(sysMenu.getMenuLabel());
                String pict = sysMenu.getMenuPict();
                setIcon16(pict, button);
                StringBuilder script = new StringBuilder();
                SysMenu parentSysMenu = null;
                if (sysMenu.getParentMenuId() != null) {
                    parentSysMenu = menuService.get(sysMenu.getParentMenuId());
                }
                if (Validator.isNull(sysMenu.getMenuUrl())) {
                    script.append("var tree = view.get('#treeMenu');");
                    script.append("view.get('#dataSetMenu').set('parameter', {parentMenuId:self.get('tags')}).flushAsync(function(result){");
                    script.append("tree.refresh(true);");
                    script.append("});");
                    script.append("view.get('#panelSubMenuTree').set('caption',self.get('caption'));");
                    script.append("view.get('#panelSubMenuTree').set('icon',self.get('icon'));");
                    script.append("view.get('#panelSubMenuTree').set('icon',self.get('icon'));");
                    script.append("parent.setMainWindowCation(self.get('caption'),'" + parentSysMenu.getMenuCode() + "')");
                } else if (!sysMenu.getMenuUrl().startsWith("com")) {
                    script = new StringBuilder();
                    script.append("window.open('" + sysMenu.getMenuUrl() + "','_blank');");
                } else {
                    script.append("view.get('#mainSplitPanel').set('collapsed',true);");
                    script.append("var tabset = view.get('#tabControlWorkspace');");
                    script.append("var frameTab = tabset.getTab('menuId" + sysMenu.getMenuCode() + "');");
                    script.append("if(frameTab == null){");
                    script.append(" var tab = {");
                    script.append(" name:'menuId" + sysMenu.getMenuCode() + "',");
                    script.append(" caption:'" + sysMenu.getMenuLabel() + "',");
                    script.append(" path:'" + sysMenu.getMenuUrl() + "',");
                    script.append(" closeable:true,");
                    script.append(" icon:'" + sysMenu.getMenuPict() + "'");
                    script.append(" };");
                    script.append(" frameTab = new dorado.widget.tab.IFrameTab(tab);");
                    script.append(" tabset.addTab(frameTab);");
                    script.append(" }");
                    script.append(" tabset.set('currentTab',frameTab);");
                    script.append(" var index = tabset.get('currentIndex');");
                    script.append(" if(index > 5){");
                    script.append(" var tab = tabset.getTab(0);");
                    script.append(" tab.close();");
                    script.append(" }");
                    script.append("parent.setMainWindowCation('" + sysMenu.getMenuLabel() + "','" + parentSysMenu.getMenuCode() + "');");
                }
                button.addClientEventListener("onClick", new DefaultClientEvent(script.toString()));
                moduleToolBar.addItem(button);
            }
        }
    }

    /*
     * 添加menuId参数
     */
    private void addMenuIdParam(List<SysMenu> menus) {
        for (SysMenu menu : menus) {
            String uri = menu.getMenuUrl();
            if (Validator.isNotNull(uri) && uri.indexOf("appId") == -1) {
                if (uri.indexOf("?") > -1) {
                    uri += "&appId=" + menu.getId();
                } else {
                    uri += "?appId=" + menu.getId();
                }
            }
            menu.setMenuUrl(uri);
        }
    }

    /*
    * 获取16*16图标
    */
    private String getIcon16(String pict) {
    	try{
        if (Validator.isNotNull(pict) && pict.indexOf("32") > -1) {
            int start = pict.indexOf("32");
            int end = start + 3;
            String s = pict.substring(0, start);
            String e = pict.substring(end, pict.length());
            return s + e;
        }
    	}catch (Exception e) {
    		e.printStackTrace();
		}
        return pict;
    }

    /*
     * 设置16*16图标
     */
    private void setIcon16(String pict, MenuItem item) {
        pict = getIcon16(pict);
        item.setIcon(pict);
    }

    /*
    * 设置16*16图标
    */
    private void setIcon16(String pict, Section section) {
        pict = getIcon16(pict);
        section.setIcon(pict);
    }

    /*
    * 设置16*16图标
    */
    private void setIcon16(String pict, IFrameApp app) {
        pict = getIcon16(pict);
        app.setIcon16(pict);
    }

    /*
    * 设置16*16图标
    */
    private void setIcon16(String pict, Button button) {
        pict = getIcon16(pict);
        button.setIcon(pict);
    }

    /**
     *
     *
     BlockView blockView = (BlockView) view.getViewElement("leftMenuBlockView");
        addMenuIdParam(sysMenus);
        List appItems = new ArrayList();
        for(SysMenu sysMenu : sysMenus){
            Map app = new HashMap();
            app.put("name", sysMenu.getMenuLabel());
            app.put("image", sysMenu.getMenuPict());
            app.put("id", sysMenu.getMenuCode());
            appItems.add(app);
        }
        Map setting = new HashMap();
        setting.put("name","设置");
        setting.put("image",">icons/32/wrench.png");
        setting.put("id","setting");
        appItems.add(setting);
        Map logout = new HashMap();
        logout.put("name","退出");
        logout.put("image",">icons/c7.png");
        logout.put("id","logout");
        appItems.add(logout);
        blockView.setItems(appItems);
     *
     *
     *
     */

}
