package com.liyan.struts.interceptor;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ExceptionInterceptor extends AbstractInterceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8772752450868803209L;

	private static final Logger log = Logger  
            .getLogger(ExceptionInterceptor.class);  
  
    @Override  
    public String intercept(ActionInvocation invocation) throws Exception {  
        String actionName = invocation.getInvocationContext().getName();  
        try {  
            String result = invocation.invoke();  
            return result;  
        } catch (Exception e) {  
            log.error(actionName, e);  
            return Action.SUCCESS;  
        }  
    }  
}
