package com.innofi.component.webservice.webservice.impl;

import org.springframework.stereotype.Component;

import com.innofi.component.webservice.webservice.IJavaWebservice;

@Component(value="javaWebservice")
public class JavaWebserviceImpl implements IJavaWebservice{

	public String javaWebservice(String xml) {
		System.out.println("接到客户端参数>>>>" + xml);
		return "java webservice";
	}
}
