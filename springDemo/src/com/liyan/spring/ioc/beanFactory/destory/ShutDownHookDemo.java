package com.liyan.spring.ioc.beanFactory.destory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author liyan
 * ʹ���߳�����Bean  
 * ע��رչ��� 
 */
public class ShutDownHookDemo {
	
	public static void main(String args[]){
		XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("com/liyan/spring/ioc/beanFactory/destory/destoryDemo_context.xml"));
		SampleBean bean  = (SampleBean)factory.getBean("sampleBean");
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
		
		//ע��ر��߳�
		//ShutDownHook ��һ���Ѿ���ʼ�� ����δ�������̣߳�addShutdownHook�����߳�ע�����߳���ִ��
		Runtime.getRuntime().addShutdownHook(new Thread(new ShutDownHook(factory)));
		
	}
}
