package com.innofi.framework.exception;

import org.springframework.security.web.context.HttpRequestResponseHolder;

/**
 * 一个专门用于处理系统异常的ExceptionInterceptor接口类，用于通过实现该接口，从而实现对具体异常的特定处理
 *
 * @author liumy2009@126.com
 * @version  1.0-SNAPSHOT
 */
public interface IExceptionInterceptor {

    /**
     * 用户要重写的异常处理类
     *
     * @param holder
     * @param ex
     */
    void handleException(HttpRequestResponseHolder holder, Throwable ex);

    /**
     * 根据给定的异常实例，决定当前Interceptor要不要处理这个异常
     *
     * @param ex
     * @return 返回是否为当前支持的异常类型
     */
    boolean support(Throwable ex);

}
