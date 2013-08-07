package com.liyan.spring.ioc.beanFactory.initArg;

import org.springframework.beans.factory.InitializingBean;

public class SampleBeanImplAndInit implements InitializingBean{
	private String name ; 
	private String age ;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "SampleBeanImplAndInit [name=" + name + ", age=" + age + "]";
	}
	
	/*
	 * 实现InitializingBean  然后调用初始化方法 2种方式组合  用来初始化特殊初始化
	 * */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.init("liyan","26");
	}
	
	public void init(String name, String age){
		System.out.println("初始化参数");
		if(this.name == null){
			System.out.print("name 必须有值，初始化默认值");
			this.name=name;
			this.age =age ; 
		}
	}
	
}
	
