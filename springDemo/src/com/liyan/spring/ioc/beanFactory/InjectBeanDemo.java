package com.liyan.spring.ioc.beanFactory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author liyan
 *  ��һ��Bean ע�뵽��һ��Bean��
 */
public class InjectBeanDemo {
	public static void main(String args[]){
		BeanFactory factory = new XmlBeanFactory(new ClassPathResource("/com/liyan/spring/ioc/beanFactory/spring_inject_bean.xml"));
		InjectBean injectBean = (InjectBean)factory.getBean("injectBean");
		InjectBean2 injectBean2 = injectBean.getInjectBean2();
		System.out.println(injectBean2.getName());
		
		InjectBean injectBeanp = (InjectBean)factory.getBean("injectBeanp");
		InjectBean2 injectBeanp2 = injectBean.getInjectBean2();
		System.out.println(injectBeanp2.getName());
	}
}
