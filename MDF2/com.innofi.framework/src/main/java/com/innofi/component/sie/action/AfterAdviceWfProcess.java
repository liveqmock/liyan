package com.innofi.component.sie.action;

import org.aspectj.lang.JoinPoint;

import com.innofi.component.sie.service.IWfProcessService;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;
public class AfterAdviceWfProcess {
	
	public void doBefore() {
		//System.out.println("切入前置方法！");
	}
	/**
     * 创建流程
     *
     */
    public void doAfter(JoinPoint jp){
    	IWfProcessService iWfProcessService = ContextHolder.getSpringBean("wfProcessService");
        for (int i = 0; i < jp.getArgs().length; i++) {   
            Object arg = jp.getArgs()[i];  
            if(null != arg) {   
            	if(arg instanceof BasePojo){
            		Class clazz = arg.getClass();
            		String className = clazz.getName();
            		//dorado代理 className _$$_java
            		if(className.indexOf("_") != -1){
            			className = className.substring(0, className.indexOf("_"));
            		}
            		BasePojo basePojo = (BasePojo)arg;
            		try {
            			ConsoleUtil.info("--------开始启动流程 ["+className+"]--------");
            			iWfProcessService.saveWfProcess(className, basePojo.getId());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }   
        }   
    }
}
