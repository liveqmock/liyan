package com.innofi.component.webservice.webservice;

import com.innofi.component.webservice.pojo.UserPojo;

public interface IDemoService {

	/**
	 * 入参和出参都是简单的String类型
	 * @param xml
	 * @return String
	 */
	public String demo(String xml);

	/**
	 * 返回参数是pojo类型
	 * @param userId
	 * @return UserPojo
	 */
	public UserPojo getUsr(String userId);

	/**
	 * 入参是pojo类型
	 * @param userPojo
	 * @return int
	 */
	public int countUser(UserPojo userPojo);
	
	/**
	 * 没有返回值的类型
	 * @param userId
	 */
	public void setUser(String userId);
	
	/**
	 * 返回值是数组类型,axis2不支持返回参数为集合类型,因此需要把集合类型转换成数组
	 * @return UserPojo[]
	 */
	public UserPojo[] getAllUser();
	
	public String demo1();
	
	public String demo2();
	
	public String demo3();
}
