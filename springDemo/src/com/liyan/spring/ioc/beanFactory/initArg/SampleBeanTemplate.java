package com.liyan.spring.ioc.beanFactory.initArg;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author liyan
 *	 实现InitializingBean 初始化接口 创建模板方法模式， 除继承方法外 其余方法均为final 不可被继承
 */
public abstract class SampleBeanTemplate implements InitializingBean{
	
	@Override
	public  final void afterPropertiesSet() throws Exception {
		init();
	}
	/**
	 * 使用模板方式创建初始化模板  其子类实现 init 方法
	 * */
	protected void init(){
		
	}
}

