package com.liyan.spring.ioc.beanFactory.initArg;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author liyan
 * ָ����ʼ������  ��ʼ������
 */
public class SpingInitArgDemo {
	public static void main(String args[]){
		BeanFactory factory = new XmlBeanFactory(new ClassPathResource("com/liyan/spring/ioc/beanFactory/initArg/spring_init_arg.xml"));
		SampleBean bean = (SampleBean)factory.getBean("sampleBean");
		System.out.println(bean);
		SampleBeanImplInitializingBean bean2 = (SampleBeanImplInitializingBean)factory.getBean("sampleBeanimplInitial");
		System.out.println(bean2);
		SampleBeanImplAndInit bean3 = (SampleBeanImplAndInit)factory.getBean("sampleBeanIpmlAndInit");
		System.out.println(bean3);
		SampleBeanEx bean4 = (SampleBeanEx)factory.getBean("sampleBeanEx");
		System.out.println(bean4);
	}
}
