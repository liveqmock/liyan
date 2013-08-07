package com.liyan.spring.ioc.beanFactory.destory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Spring bean销毁方式Demo
 * */
public class DestoryDemo  {
	
	public static void main(String args[]){
		XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("com/liyan/spring/ioc/beanFactory/destory/destoryDemo_context.xml"));
		/*SampleBean bean = (SampleBean)factory.getBean("sampleBean");*/
		SampleBeanImplDisposable bean = (SampleBeanImplDisposable)factory.getBean("sampleBeanImplDisposable");
		FileInputStream is = bean.getIs();
		
		String len;
		try {
			BufferedReader bufferedReader = 
					new BufferedReader(new InputStreamReader(is,"gb2312"));
					//InputStreamReader的第二参数为指定解码字符集名；
			while((len=bufferedReader.readLine())!=null){//.readLine()一次读取源文件一行；返回是一个string
				System.out.println(len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//factory.destroySingleton(beanName); //销毁单独一个Bean
		factory.destroySingletons(); //销毁所有
		
	}
}
