package com.innofi.framework.spring.context;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 仿照Spring Security3当中的HttpSessionSecurityContextRepository类写法，<br>
 * 重写在HttpSession当中存储SecurityContext的SecurityContextRepository实现类。<br>
 * 该类将会被SecurityContextPersistenceFilter调用，用来决定SecurityContext的存放地，这里是将其存储在HttpSession中，也是一般通用做法
 *
 * @author 刘名寓 jack.liu@innofi.com
 * @version 2.0
 */
public class HttpSessionSecurityContextRepository implements SecurityContextRepository {
    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    private Object contextObject = SecurityContextHolder.createEmptyContext();
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    /**
     * SecurityContextRepository接口方法之一<br>
     * 从当前的HttpSession中得到一个SecurityContext，如果当前HttpSession为空，就返回空，<br>
     * 如果当前HttpSession不为空，且其中没有SecurityContext对象，那么就创建并返回一个新的空的SecurityContext对象
     *
     * @param requestResponseHolder 一个HttpRequestResponseHolder对象，该当象中有当前请求的Request以及Response对象
     * @return 返回从session中取到的SecurityContext对象，如果有的话
     */
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        HttpServletResponse response = requestResponseHolder.getResponse();
        HttpSession httpSession = request.getSession(false);
        SecurityContext securityContext = readSecurityContextFromSession(httpSession);
        if (securityContext == null) {
            securityContext = SecurityContextHolder.createEmptyContext();
        }
        requestResponseHolder.setResponse(new SaveToSessionResponseWrapper(response, request, httpSession != null, securityContext.hashCode()));
        return securityContext;
    }

    /**
     * SecurityContextRepository接口方法之一<br>
     * 在SecurityContextPersistenceFilter请求结束时调用，<br>
     * 用来处理经过一系列后续的filter后的SecurityContext对象(这些后续的filter可能会对SecurityContext进行一些修改)
     */
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        SaveToSessionResponseWrapper responseWrapper = (SaveToSessionResponseWrapper) response;
        if (!responseWrapper.isContextSaved()) {
            responseWrapper.saveContext(context);
        }
    }

    /**
     * SecurityContextRepository接口方法之一<br>
     * 判断当前HttpSession中是否包含SecurityContext对象
     */
    public boolean containsContext(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return false;
        }
        return httpSession.getAttribute(SPRING_SECURITY_CONTEXT_KEY) != null;
    }

    /**
     * 从当前的HttpSession中读取一个SecurityContext对象并返回
     *
     * @param httpSession
     * @return 返回SecurityContext对象
     */
    private SecurityContext readSecurityContextFromSession(HttpSession httpSession) {
        if (httpSession == null) return null;
        Object contextFromSession = httpSession.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        if (contextFromSession == null) return null;
        if (contextFromSession instanceof SecurityContext) {
            return (SecurityContext) contextFromSession;
        }
        return null;
    }

    //一个response包装内部类-----------------------------------------------------------------------------
    final class SaveToSessionResponseWrapper extends SaveContextOnUpdateOrErrorResponseWrapper {
        private HttpServletRequest request;
        private boolean httpSessionExistedAtStartOfRequest;
        private int contextHashBeforeChainExecution;

        public SaveToSessionResponseWrapper(HttpServletResponse response, HttpServletRequest request,
                                            boolean httpSessionExistedAtStartOfRequest, int contextHashBeforeChainExecution) {
            super(response, false);
            this.request = request;
            this.httpSessionExistedAtStartOfRequest = httpSessionExistedAtStartOfRequest;
            this.contextHashBeforeChainExecution = contextHashBeforeChainExecution;
        }

        /**
         * 该方法是在filter请求结束时调用，用于对经过SecurityContextPersistenceFilter后面一系列的filter链后SecurityContext进行处理,<br>
         * 1.如果这个SecurityContext中没有被认证信息填充或是被一个匿名的认证信息填充，就将其从HttpSession中移除。<br>
         * 2.如果这个SecurityContext不是一个空的SecurityContext实例，且在HttpSession中没有这个SecurityContext，那么就将其放到HttpSession当中
         */
        @Override
        protected void saveContext(SecurityContext context) {
            Authentication authentication = context.getAuthentication();
            HttpSession httpSession = this.request.getSession(false);
            if (authentication == null || authenticationTrustResolver.isAnonymous(authentication)) {
                /**
                 * 如果当前没有认证信息（用户还未登录）或者是一个匿名用户（框架对未登录用户赋予的一种类型账号）<br>
                 * 在当前HttpSession不为空的情况下将这种类型的SecurityContext从HttpSession中删除
                 * */
                if (httpSession != null) {
                    httpSession.removeAttribute(SPRING_SECURITY_CONTEXT_KEY);
                }
                return;
            }
            if (httpSession == null) {
                httpSession = createNewSession(context);
            }
            if (httpSession != null && (context.hashCode() != this.contextHashBeforeChainExecution || httpSession.getAttribute(SPRING_SECURITY_CONTEXT_KEY) == null)) {
                httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
            }
        }

        /**
         * 在当前请求中没有HttpSession以及当前的SecurityContext不是一个空的SecurityContext实例时,<br>
         * 就创建一个新的HttpSession对象
         *
         * @param securityContext 给定的SecurityContext对象
         * @return 返回HttpSession对象
         */
        private HttpSession createNewSession(SecurityContext securityContext) {
            if (this.httpSessionExistedAtStartOfRequest) {
                //如果在当前请求之前就存在HttpSession，这里不创建新的HttpSession，返回null
                return null;
            }
            if (contextObject.equals(securityContext)) {
                //表示当前的SecurityContext是一个空的SecurityContext实例
                return null;
            }
            return this.request.getSession(true);
        }
    }
}
