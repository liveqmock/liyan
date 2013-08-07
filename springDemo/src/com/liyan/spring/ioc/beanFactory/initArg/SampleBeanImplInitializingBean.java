package com.liyan.spring.ioc.beanFactory.initArg;

import org.springframework.beans.factory.InitializingBean;

public class SampleBeanImplInitializingBean implements InitializingBean {
	private String name  ;
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
		return "SampleBeanImplInitializingBean [name=" + name + ", age=" + age
				+ "]";
	}
	
	/* 
	 * 实现InitializingBean接口 用来初始化参数  功能相当于 init-method
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("初始化参数");
		if(this.name == null){
			System.out.print("name 必须有值，初始化默认值");
			this.name="liyan" ;
		}
	} 
	
}
