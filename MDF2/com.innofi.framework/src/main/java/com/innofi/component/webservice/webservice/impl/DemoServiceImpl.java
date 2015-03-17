package com.innofi.component.webservice.webservice.impl;

import org.springframework.stereotype.Component;

import com.innofi.component.webservice.pojo.UserPojo;
import com.innofi.component.webservice.webservice.IDemoService;
import com.innofi.component.webservice.webservice.IDemoServiceFather;

@Component(value = "demoService")
public class DemoServiceImpl extends IDemoServiceFather implements IDemoService {

	public String demo(String xml) {
		return "demo";
	}

	public UserPojo getUsr(String userId) {
		UserPojo user = new UserPojo();
		user.setSex(true);
		user.setUserId(Integer.parseInt(userId));
		user.setUserName("demoUserName");
		return user;
	}

	public int countUser(UserPojo userPojo) {
		return 0;
	}

	public void setUser(String userId) {
		System.out.println("没有返回值的服务接口");
	}

	public UserPojo[] getAllUser() {
		UserPojo[] userList = new UserPojo[3];
		for (int i = 0; i < 3; i++) {
			UserPojo user = new UserPojo();
			user.setSex(true);
			user.setUserId(22);
			user.setUserName("demoUserName");
			userList[i] = user;
		}
		return userList;
	}

	@Override
	public String demo1() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String demo2() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String demo3() {
		// TODO Auto-generated method stub
		return null;
	}
}
