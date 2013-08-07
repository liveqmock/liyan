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
	 * ʵ��InitializingBean�ӿ� ������ʼ������  �����൱�� init-method
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("��ʼ������");
		if(this.name == null){
			System.out.print("name ������ֵ����ʼ��Ĭ��ֵ");
			this.name="liyan" ;
		}
	} 
	
}
