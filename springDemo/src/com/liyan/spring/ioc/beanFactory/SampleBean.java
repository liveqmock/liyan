package com.liyan.spring.ioc.beanFactory;

import java.util.Map;

/**
 * @author liyan
 * 构造注入样例Bean
 */
public class SampleBean {
	
	private Map<String ,Long> entries ;
	private String num ; 
	
	public SampleBean(Map<String, Long> entries) {

		this.entries = entries;

	}
	
	public SampleBean(String num) {
		this.num = num;
	}
	
	public SampleBean(int num) {
		this.num ="Number:"+Integer.toString(num);
	}
	public Long findLong(String entry){
		return this.entries.get(entry);
	}
	
	public String getNum(){
		return this.num;
	}
	
}
