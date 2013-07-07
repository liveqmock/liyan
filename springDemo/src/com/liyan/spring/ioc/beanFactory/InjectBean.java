package com.liyan.spring.ioc.beanFactory;

/**
 * @author liyan
 * Spring ×¢ÈëBean
 */
public class InjectBean {
	private InjectBean2 injectBean2;

	public InjectBean2 getInjectBean2() {
		return injectBean2;
	}

	public void setInjectBean2(InjectBean2 injectBean2) {
		this.injectBean2 = injectBean2;
	}
	
}
