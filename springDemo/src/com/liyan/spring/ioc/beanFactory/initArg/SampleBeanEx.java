package com.liyan.spring.ioc.beanFactory.initArg;

/**
 * @author liyan
 *  �̳�ģ�� ʵ��init���� ��ʼ��
 */
public class SampleBeanEx  extends SampleBeanTemplate{

	private String name ; 
	private String age ; 
	
	@Override
	protected void init() {
		System.out.println("��ʼ������");
		if(this.name == null){
			System.out.print("name ������ֵ����ʼ��Ĭ��ֵ");
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
