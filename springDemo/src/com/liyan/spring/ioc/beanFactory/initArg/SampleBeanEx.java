package com.liyan.spring.ioc.beanFactory.initArg;

/**
 * @author liyan
 *  继承模板 实现init方法 初始化
 */
public class SampleBeanEx  extends SampleBeanTemplate{

	private String name ; 
	private String age ; 
	
	@Override
	protected void init() {
		System.out.println("初始化参数");
		if(this.name == null){
			System.out.print("name 必须有值，初始化默认值");
			this.name="111";
			this.age ="222" ; 
		}
	}

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
		return "SampleBeanEx [name=" + name + ", age=" + age + "]";
	}
	
	
}
