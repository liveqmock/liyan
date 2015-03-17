/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.rbac.login.action;

import com.innofi.component.rbac.RBACConstants;
import com.innofi.component.rbac.user.service.ISysUserService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.component.rbac.workspace.theme.pojo.SysUserTheme;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.validate.Validator;
import com.innofi.framework.utils.variable.VariableHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 功能/ 模块：登录模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0 12-12-29
 *          平台登录处理
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component("loginAction")
public class LoginAction extends BaseActionImpl {

    private static Logger _log = LoggerFactory.getLogger(LoginAction.class);


    public static final String LOGIN_VIEW_URL = ContextHolder.getIdfProperty("mdf.security.loginFormUrl");

    /**
     * 登录验证方法
     *
     * @param request
     * @param response
     */
    public void doLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = ContextHolder.getIdfProperty("mdf.security.loginFormUrl");        //下一跳转页面
        ISysUserService sysUserService = ContextHolder.getSpringBean("sysUserService"); //注入用户服务
        _log.debug(">>>>>>>>>>>>>>>>>>>Start login!<<<<<<<<<<<<<<<<<<<<<<<<");
        String userCode = request.getParameter("userName"); //用户名
        String password = request.getParameter("password"); //密码
        String clientWidth = request.getParameter("clientWidth"); //客户端宽度
        String clientHeight = request.getParameter("clientHeight"); //客户端高度
        String ip = getIpAddressByRequest(request);         //ip地址
        String mac = ""; //MacUtil.getMac(ip);              //mac地址

        if (Validator.isEmpty(userCode)) {
            request.setAttribute("message", "请输入用户名!");
            request.getRequestDispatcher(path).forward(request, response);
            return;
        }

        if (Validator.isEmpty(password)) {
            request.setAttribute("message", "请输入密码!");
            request.getRequestDispatcher(path).forward(request, response);
            return;
        }

        char loginRs = sysUserService.login(userCode, password, ip, mac);//登录验证

        switch (loginRs) {
            case '0':
                SysUser currentUser = (SysUser) ContextHolder.getRequest().getSession().getAttribute(FrameworkConstants.SESSION_USER);//取出用户
                currentUser.setIpAddress(ip);
                SysUserTheme userTheme = currentUser.getSysUserTheme();
                String pageIndex = "";

                if(Validator.isNotNull(userTheme.getMainFrame())){
                    pageIndex = ContextHolder.getSystemProperties().getString(userTheme.getMainFrame(),"classURL");
                }else{
                    pageIndex = ContextHolder.getSystemProperties().getString("classURL","/com.innofi.component.rbac.index.desktop.Desktop.d");
                }

                if(userTheme.getIsFullScreen()==null){
                	userTheme.setIsFullScreen("0");
                }
				if(pageIndex.indexOf("?")==-1){
					pageIndex += "?";
				}else{
					pageIndex += "&";
			 	}
				if (VariableHelper.parseBoolean(userTheme.getIsFullScreen())) { //是否全屏显示
					 pageIndex += "isFullScreen=true&clientWidth=" + clientWidth + "&clientHeight=" + clientHeight;//添加全屏显示参数,添加客户端高度宽度参数;
				} else {
					 pageIndex += "isFullScreen=false";
				}
                currentUser.setOnlineStatus(RBACConstants.ONLINE_TRUE);                            //更新在线状态

                path = pageIndex;
                break;
            case '1':
                request.setAttribute("message", "登录失败,帐号不存在!");
                path = LOGIN_VIEW_URL;
                break;
            case '2':
                request.setAttribute("message", "登录失败,密码错误!");
                request.setAttribute("userName", userCode);
                path = LOGIN_VIEW_URL;
                break;
            case '3':
                request.setAttribute("message", "登录失败,此用户已停用!");
                path = LOGIN_VIEW_URL;
                break;
            case '4':
                request.setAttribute("message", "登录失败,用户未分配角色!");
                path = LOGIN_VIEW_URL;
                break;
/*            case '5':
                request.setAttribute("message", "登录失败,用户未分配岗位!");
                path = ContextHolder.getIdfProperty("mdf.security.loginFormUrl");
                break;*/
        }
        Cookie cookie = new Cookie(FrameworkConstants.COOKIE_USER, userCode);       		   //向cookie设置登录用户帐号
        cookie.setMaxAge(ContextHolder.getSystemProperties().getInteger("mdf.cookie.timeout"));//设置cookie超时时间
        ContextHolder.getResponse().addCookie(cookie);                              		   //将用户名添加到cookie中
        request.getRequestDispatcher(path).forward(request, response);
    }

    /**
     * 签退方法
     *
     * @param request
     * @param response
     */
    public void doLogout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ISysUserService sysUserService = ContextHolder.getSpringBean("sysUserService"); //注入用户服务
        SysUser currentUser = (SysUser) ContextHolder.getRequest().getSession().getAttribute(FrameworkConstants.SESSION_USER);//取出用户
        String loginPage = ContextHolder.getIdfProperty("mdf.security.logoutSuccessUrl");
        if (currentUser != null) {
        	 SysUserTheme userTheme = currentUser.getSysUserTheme();
             if(userTheme.getIsFullScreen()==null){
             	userTheme.setIsFullScreen("0");
             }
         	 if(loginPage.indexOf("?")==-1){
         		loginPage += "?";
			 }else{
				loginPage += "&";
			 }
			 if (VariableHelper.parseBoolean(userTheme.getIsFullScreen())) { //是否全屏显示
				 loginPage += "resetFullScreen=true";
			 } else {
				 loginPage += "resetFullScreen=false";
			 }
            sysUserService.logout();
        }
        response.sendRedirect(loginPage);
    }

    /*
     *获取登录用户ip地址
     */
    private String getIpAddressByRequest(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("Proxy-Client-IP");

        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("WL-Proxy-Client-IP");

        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getRemoteAddr();
        }

        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        return ip;
    }
}
