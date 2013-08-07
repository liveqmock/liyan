package com.liyan.spring.ioc.beanFactory.destory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author liyan
 * 使用线程销毁Bean  
 * 注册关闭钩子 
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
					//InputStreamReader的第二参数为指定解码字符集名；
			while((len=bufferedReader.readLine())!=null){//.readLine()一次读取源文件一行；返回是一个string
				System.out.println(len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//注册关闭线程
		//ShutDownHook 是一个已经初始化 但尚未启动的线程，addShutdownHook将该线程注册入线程中执行
		Runtime.getRuntime().addShutdownHook(new Thread(new ShutDownHook(factory)));
		
	}
}
