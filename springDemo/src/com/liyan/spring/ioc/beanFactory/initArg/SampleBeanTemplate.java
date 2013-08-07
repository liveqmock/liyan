package com.liyan.spring.ioc.beanFactory.initArg;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author liyan
 *	 ʵ��InitializingBean ��ʼ���ӿ� ����ģ�巽��ģʽ�� ���̳з����� ���෽����Ϊfinal ���ɱ��̳�
 */
public abstract class SampleBeanTemplate implements InitializingBean{
	
	@Override
	public  final void afterPropertiesSet() throws Exception {
		init();
	}
	/**
	 * ʹ��ģ�巽ʽ������ʼ��ģ��  ������ʵ�� init ����
	 * */
	protected void init(){
		
	}
}

