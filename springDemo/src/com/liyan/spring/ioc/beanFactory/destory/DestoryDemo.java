package com.liyan.spring.ioc.beanFactory.destory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Spring bean���ٷ�ʽDemo
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
					//InputStreamReader�ĵڶ�����Ϊָ�������ַ�������
			while((len=bufferedReader.readLine())!=null){//.readLine()һ�ζ�ȡԴ�ļ�һ�У�������һ��string
				System.out.println(len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//factory.destroySingleton(beanName); //���ٵ���һ��Bean
		factory.destroySingletons(); //��������
		
	}
}
