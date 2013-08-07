package com.liyan.spring.ioc.beanFactory.destory;

import org.springframework.beans.factory.xml.XmlBeanFactory;

/**
 * @author liyan
 *  销毁Bean线程对象
 */
public class ShutDownHook implements Runnable{

	private XmlBeanFactory factory ;
	
	public ShutDownHook(XmlBeanFactory factory) {
		this.factory = factory;
	}

	@Override
	public void run() {
		this.factory.destroySingletons();
	}

}
