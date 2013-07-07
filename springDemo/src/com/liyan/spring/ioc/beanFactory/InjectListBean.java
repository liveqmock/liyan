package com.liyan.spring.ioc.beanFactory;

import java.util.List;
import java.util.Map;

/**
 * @author liyan
 *	
 */
public class InjectListBean {
	private List testList ;
	private Map testMap ;
	public List getTestList() {
		return testList;
	}
	public void setTestList(List testList) {
		this.testList = testList;
	}
	public Map getTestMap() {
		return testMap;
	}
	public void setTestMap(Map testMap) {
		this.testMap = testMap;
	}
	
}
