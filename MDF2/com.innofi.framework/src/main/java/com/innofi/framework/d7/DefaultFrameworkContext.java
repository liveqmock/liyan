package com.innofi.framework.d7;

import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.innofi.component.rbac.login.action.LoginAction;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.spring.context.FrameworkContext;
import org.springframework.beans.factory.InitializingBean;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 一个FrameworkContext默认实现类.
 */
public class DefaultFrameworkContext extends FrameworkContext implements InitializingBean {

    private static final long serialVersionUID = 5680026674620568798L;
    private String loginFormUrl = LoginAction.LOGIN_VIEW_URL;
    private String script;

    /**
     * (non-Javadoc)
     *
     * @see FrameworkContext#getLoginUser()
     */
    public SysUser getLoginUser() {
        if (loginUser == null) {
            if (ContextHolder.getRequest().getRequestURL().toString().indexOf("?") == -1) {
                throw new ClientRunnableException(script);
            }
        }
        return loginUser;
    }

    /**
     * (non-Javadoc)
     *
     * @see FrameworkContext#getLoginUsername()
     */
    public String getLoginUsername() {
        if (loginUsername == null) {
            throw new ClientRunnableException(script);
        }
        return loginUsername;
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("dorado.MessageBox.confirm('您未登录或登录会话已过期！")
                .append("请重新登录！', function() {")
                .append("window.top.open($url('>" + loginFormUrl + "?timeout=true" + "'), '_self');")
                .append("});");
        this.script = sb.toString();
    }

}
