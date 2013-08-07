package com.liyan.spring.ioc.beanFactory.destory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class SampleBeanImplDisposable implements DisposableBean,InitializingBean {
	
	private FileInputStream is ;
	/* 
	 * 实现DisposableBean 接口 destory方法 
	 */
	@Override
	public void destroy() throws Exception {
		try {
			System.out.println("调用销毁函数");
			this.getIs().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void afterPropertiesSet() throws Exception {
		File file = new File("D://test.txt");
		this.setIs(new FileInputStream(file));
		
	}

	public FileInputStream getIs() {
		return is;
	}

	public void setIs(FileInputStream is) {
		this.is = is;
	}

}
