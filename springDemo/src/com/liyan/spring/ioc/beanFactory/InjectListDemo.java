package com.liyan.spring.ioc.beanFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author liyan
 * Spring ×¢Èë¼¯ºÏdemo
 */
public class InjectListDemo {
	public static void main(String args[]){
		BeanFactory factory = new XmlBeanFactory(new ClassPathResource("/com/liyan/spring/ioc/beanFactory/spring_inject_list.xml"));
		InjectListBean listBean = (InjectListBean)factory.getBean("listBean");
		List testList = listBean.getTestList();
		Iterator it = testList.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
		Map testMap = listBean.getTestMap();
		Iterator it2 = testMap.keySet().iterator();
		while(it2.hasNext()){
			System.out.println(testMap.get(it2.next()));
		}
	}
}
