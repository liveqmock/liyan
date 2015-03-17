package com.innofi.framework.spring.context;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 一个应用Filter的拦截类，用户如果需要在应用Filter里添加业务逻辑，不需要写一个真正的Filter<br>
 * 可以简单的写一个FilterInterceptor接口的实现类，然后将其配置在spring中即可，IDF在启动时会自动加载所有实现了该接口的类，<br>
 * 同时在系统全局的ContextPersistenceFilter中进行调用
 *
 * @author liumy2009@126.com
 * @version  1.0-SNAPSHOT
 */
public interface FilterInterceptor {

    /**
     * 在系统中所有Filter调用之前调用
     *
     * @param req
     * @param res
     * @throws java.io.IOException
     * @throws ServletException
     */
    public void before(ServletRequest req, ServletResponse res) throws IOException, ServletException;

    /**
     * 在系统中所有Filter调用之后调用
     *
     * @param req
     * @param res
     * @throws java.io.IOException
     * @throws ServletException
     */
    public void after(ServletRequest req, ServletResponse res) throws IOException, ServletException;

}
