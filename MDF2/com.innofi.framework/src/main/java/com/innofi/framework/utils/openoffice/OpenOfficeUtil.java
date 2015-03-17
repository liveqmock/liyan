/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.utils.openoffice;

import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          Openoffice工具类
 * @found date: 13-11-28
 * @found time: 下午12:18
 */
public class OpenOfficeUtil {

    public static boolean started = false; //openoffice是否已启动标识位

    public static Process pro = null;

    /**
     * 启动openoffice服务
     */
    public static synchronized void startOpenOffice() {
        String connPort = ContextHolder.getSystemProperties().getString("mdf.openoffice.listener.port");
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(Integer.parseInt(connPort));
        try {
            connection.connect();
            connection.disconnect();
            started = true;
        } catch (java.net.ConnectException e) {
            started = false;
        }
        if (!started) {
            String openOfficeInstallPath = ContextHolder.getSystemProperties().getString("mdf.openoffice.path");
            String listenerHost = ContextHolder.getSystemProperties().getString("mdf.openoffice.listener.host");
            String listenerPort = ContextHolder.getSystemProperties().getString("mdf.openoffice.listener.port");
            ConsoleUtil.info("openOffice安装路径:" + openOfficeInstallPath);
            String listenerCmd = "socket,host=" + listenerHost + ",port=" + listenerPort + ";urp;";
            String executeCmd = openOfficeInstallPath + "/soffice -headless -accept=" + listenerCmd + " -nofirststartwizard";
            try {
                pro = Runtime.getRuntime().exec(executeCmd);
                ConsoleUtil.info("openOffice服务已启动,已执行命令:" + openOfficeInstallPath);
                started = true;
            } catch (Exception e) {
                e.printStackTrace();
                ConsoleUtil.info("openOffice未安装,或者安装路径配置错误，执行启动命令失败:" + executeCmd);
            }
        } else {
            ConsoleUtil.info("openOffice在端口:" + connPort + "的服务已启动...");
        }
    }

    public synchronized static void stopOpenOffice(){
        if(pro!=null){
            pro.destroy();
        }
    }


}
