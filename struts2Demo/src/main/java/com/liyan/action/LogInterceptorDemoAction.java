package com.liyan.action;

import com.liyan.common.exception.LogException;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

public class LogInterceptorDemoAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4964731535024349356L;
	
	public String test() throws Exception{
		System.out.println("123123123123");
		System.out.println(2/0);
		return Action.SUCCESS ;
	}
	public String test2(){
		
		return Action.SUCCESS;
	}
}
