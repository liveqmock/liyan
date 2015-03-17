/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.filterinterceptor;

import com.innofi.component.rbac.login.action.LoginAction;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.spring.context.FilterInterceptor;
import com.innofi.framework.utils.validate.Validator;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能/ 模块：框架基础模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          过滤
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
//@Component("timeOutFilterInterceptor")
public class TimeOutFilterInterceptor implements FilterInterceptor{

    String path = LoginAction.LOGIN_VIEW_URL+"?timeout=true";

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.spring.context.FilterInterceptor#before(ServletRequest req, ServletResponse res)
     */
    public void before(ServletRequest req, ServletResponse res) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
    	String url = request.getRequestURL().toString();
    	String userName = request.getParameter("userName");
    	String logOut = request.getParameter("logOut");
        if(Validator.isEmpty(logOut)&&url.indexOf("page")>-1||url.indexOf("com.innofi")>-1||url.indexOf("com/innofi")>-1||(url.toLowerCase().indexOf("action.")>-1&&url.indexOf("loginAction.login")==-1)||(url.indexOf("loginAction.login")>-1&&Validator.isEmpty(userName))){
            if(url.indexOf(ContextHolder.getSystemProperties().getString("mdf.security.loginFormUrl"))==-1&&url.indexOf("com.innofi.component.rbac.index.defaultweb.SecondMenu.d")==-1){
                Boolean checkewitch = ContextHolder.getSystemProperties().getBoolean("mdf.check.session.timeout");
                if(checkewitch){
                    try{
                	    ContextHolder.getContext().getLoginUser();
                    }catch(Exception e){
                    	request.getRequestDispatcher(path).forward(request, response);
                    }
                }
            }
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.spring.context.FilterInterceptor#after(ServletRequest req, ServletResponse res)
     */
    public void after(ServletRequest req, ServletResponse res) throws IOException, ServletException {
    }

}
