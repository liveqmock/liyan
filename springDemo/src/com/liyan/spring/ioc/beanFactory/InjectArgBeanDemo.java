package com.liyan.spring.ioc.beanFactory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author liyan
 *	sprig注入简单参数的方式
 */
public class InjectArgBeanDemo {
	private int age ;
	private String name ; 
	private boolean flag ; 
	private double height ;
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	@Override
	public String toString() {
		return "InjectArgBeanDemo [age=" + age + ", name=" + name + ", flag="
				+ flag + ", height=" + height + "]";
	} 
	public static void main(String args[]){
		BeanFactory factory = new XmlBeanFactory(new ClassPathResource("/com/liyan/spring/ioc/beanFactory/spring_inject_arg.xml"));
		InjectArgBeanDemo iabd  = (InjectArgBeanDemo)factory.getBean("injectBean");
		System.out.println(iabd);
		InjectArgBeanDemo iabd2  = (InjectArgBeanDemo)factory.getBean("injectBean2");
		System.out.println(iabd2);
	}
}
