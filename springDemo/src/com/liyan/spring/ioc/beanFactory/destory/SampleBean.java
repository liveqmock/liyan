package com.liyan.spring.ioc.beanFactory.destory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.InitializingBean;

public class SampleBean  implements InitializingBean{
	private String name ;
	private String age ;
	private FileInputStream is ;
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
	/* 
	 *  ��ʼ���ص�����
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.name ="liyan";
		this.age = "24" ;
		File file = new File("D://test.txt");
		this.setIs(new FileInputStream(file));
	}
	/**
	 *  ���ٷ����ص�
	 */
	public void destory(){
		
		try {
			System.out.println("�������ٺ���");
			this.getIs().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public FileInputStream getIs() {
		return is;
	}
	public void setIs(FileInputStream is) {
		this.is = is;
	}
	
}
