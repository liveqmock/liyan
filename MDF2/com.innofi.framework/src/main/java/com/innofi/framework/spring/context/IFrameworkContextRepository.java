package com.innofi.framework.spring.context;

import org.springframework.security.web.context.HttpRequestResponseHolder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 用来决定当前会话信息存储位置的接口，默认有HttpSessionFrameworkContextRespository实现类，它是将所有会话信息放在HttpSession当中。<br>
 * 实际应用当中如果不使用HttpSession存储会话信息，可以重实现该接口以及SecurityContextRepository接口即可，实现方式可参照他们的HttpSession的默认实现类
 */
public interface IFrameworkContextRepository {

    /**
     * 放在ContextPersistenceFilter当中使用，这是所有filter链中第一个filter。<br>
     * 从指定的会话存储介质(比如默认的HttpSessionFrameworkContextRespository中使用的是HttpSession)中取FrameworkContext对象,<br>
     * 如果有FrameworkContext就取出来，否则就创建一个新的FrameworkContext实例
     *
     * @param requestResponseHolder HttpRequestResponseHolder对象，其中有当前线程的Request与Response
     * @return 返回当前请求线程的FrameworkContext对象
     */
    FrameworkContext loadFrameworkContext(HttpRequestResponseHolder requestResponseHolder);

    /**
     * 这个方法在ContextPersistenceFilter之后所有filter处理完成之后调用,<br>
     * 用于判断当前的FrameworkContext对象，是否是一个空的FrameworkContext对象，如果是就不向会话存储介质（比如HttpSession）中存储，<br>
     * 否则，如果在存储介质中不存在这个FrameworkContext对象，就将这个对象存储到特定的存储介质当中（比如HttpSession）。
     *
     * @param context  上下文对象
     * @param request  HttpRequest对象
     * @param response HttpResponse对象
     */
    void saveFrameworkContext(FrameworkContext context, HttpServletRequest request, HttpServletResponse response);

    /**
     * 设置当前应用的ServletContext为FrameworkContextRepository使用
     *
     * @param servletContext ServletContext对象
     */
    void setServletContext(ServletContext servletContext);

}
