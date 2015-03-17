/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.servlet;

import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.openoffice.OpenOfficeUtil;
import com.innofi.component.webservice.service.IManageWebservice;
import com.innofi.component.webservice.util.GenerateXml;

import javax.servlet.http.HttpServlet;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          IDF框架初始化Servlet
 * @found date: 13-11-28
 * @found time: 下午12:27
 */
public class IdfInitServlet extends HttpServlet {

    /**
     * Webservice发布目录
     */
    public static final String SERVICESDIR = "WEB-INF/services/innofiService/META-INF/";

    @Override
    public void init() {
        initWebservice();//1.Webservice
        initOpenOffice();//2.openOffice
    }

    @Override 
    public void destroy() {
        OpenOfficeUtil.stopOpenOffice();//关闭openOffice服务
    }

    /**
     * 初始化Webservice
     */
    private void initWebservice() {
        ConsoleUtil.info("初始化webservice配置文件开始...");
        IManageWebservice webservice = ContextHolder.getSpringBean("managerWebServiceService"); // 获取数据库配置好的service.xml内容，并自动生成文件，放到servcies目录下
        String xml = webservice.getWebserviceXml();
        String webRootPath = ContextHolder.getWebRootPath();
        if (null != xml && !"".equals(xml)) {                                                   // 如果数据库有记录，则生成services.xml文件
            GenerateXml.buildXmlFile(xml, webRootPath + SERVICESDIR);
        } else {
            GenerateXml.buildXmlFile(GenerateXml.buildNullXmlDoc(), webRootPath + SERVICESDIR);
        }
        ConsoleUtil.info("生成文件的路径为:" + webRootPath + SERVICESDIR);
        ConsoleUtil.info("生成webservice配置文件成功!");
    }

    /**
     * 初始化openOffice
     */
    private void initOpenOffice() {
        ConsoleUtil.info("启动openOffice服务开始...");
        OpenOfficeUtil.startOpenOffice();
        ConsoleUtil.info("启动openOffice服务结束...");
    }

}
