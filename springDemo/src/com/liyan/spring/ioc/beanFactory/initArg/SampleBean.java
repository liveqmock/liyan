package com.liyan.spring.ioc.beanFactory.initArg;

public class SampleBean {
	private String name ;
	private int age ;
	
	public void init(){
		System.out.println("初始化参数");
		if(this.name == null){
			System.out.print("name 必须有值，初始化默认值");
			this.name="liyan" ;
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "SampleBean [name=" + name + ", age=" + age + "]";
	}
	
	
}
