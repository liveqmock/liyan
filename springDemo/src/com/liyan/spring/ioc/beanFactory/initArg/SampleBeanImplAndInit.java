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
	 * ʵ��InitializingBean  Ȼ����ó�ʼ������ 2�ַ�ʽ���  ������ʼ�������ʼ��
	 * */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.init("liyan","26");
	}
	
	public void init(String name, String age){
		System.out.println("��ʼ������");
		if(this.name == null){
			System.out.print("name ������ֵ����ʼ��Ĭ��ֵ");
			this.name=name;
			this.age =age ; 
		}
	}
	
}
	
