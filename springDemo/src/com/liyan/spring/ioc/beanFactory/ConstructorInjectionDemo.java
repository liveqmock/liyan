package com.liyan.spring.ioc.beanFactory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author liyan
 * Spring����ע�뷽�� 
 */
public class ConstructorInjectionDemo {
	public static void main(String args[]){
		BeanFactory factory = new XmlBeanFactory(new ClassPathResource("/com/liyan/spring/ioc/beanFactory/spring_constructor_inject.xml"));
		SampleBean bean = (SampleBean)factory.getBean("sampleBean");
		System.out.println(bean.findLong("test1"));
		System.out.println(bean.findLong("test2"));
		//ָ�����췽���������Ͳ���
		SampleBean bean2 = (SampleBean)factory.getBean("sampleBean1");
		System.out.println(bean2.getNum());
	}
}
