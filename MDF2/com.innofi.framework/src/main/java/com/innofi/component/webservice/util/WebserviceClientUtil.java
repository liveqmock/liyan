package com.innofi.component.webservice.util;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import com.innofi.component.webservice.pojo.UserPojo;

/**
 * 模块：webservice
 * webservice调用客户端工具类
 * @author liuhuaiyang
 * @version 2.0.0 13-5-10
 *
 */
public class WebserviceClientUtil {
	public static void main(String[] args) throws AxisFault {
		
		String url = "http://localhost:8080/ipdp/services/demoService?wsdl";
		String targertNameSpace = "http://com.innofi.component.webservice.webservice.impl.DemoServiceImpl";

		// 调用有返回值的接口，请求和返回都为字符串
		String xml = WebserviceClientUtil.sendRecevieXmlStringMsg(url, targertNameSpace, "1", "demo");
		System.out.println("默认提供有返回值，请求和返回都为String类型的接口："+ xml);

		RPCServiceClient serviceClient = WebserviceClientUtil.getRpcClient(url);
		// 用此方法调用有返回值的服务接口
		// invokeBlocking第一个参数表示要调用哪个方法,第二个参数表示调用方法的参数,第三个参数表示webservice返回对象的类型
		// 当方法没有参数时，invokeBlocking方法的第二个参数值不能是null，而要使用new Object[]{}
		String ret = (String) serviceClient.invokeBlocking(new QName(targertNameSpace,
				"demo"), new Object[] { "1" }, new Class[] { String.class })[0];
		System.out.println("有返回值的webservice接口,参数是 >>>>>" + ret);

		// 用此方法调用没有返回值的服务接口
		serviceClient.invokeRobust(new QName(targertNameSpace, "setUser"),
				new Object[] { "1" });
		System.out.println("没有返回值的接口>>>>>>");

		 //请求参数为对象
		Integer count = (Integer) serviceClient.invokeBlocking(new QName(targertNameSpace,
				"countUser"), new Object[] { getUser() },
				new Class[] { Integer.class })[0];
		System.out.println("请求参数是对象的webservice,返回参数是>>>>>" + count);

		// 请求参数为空的服务接口,返回值为数组类型
		UserPojo[] userList = (UserPojo[]) serviceClient.invokeBlocking(
				new QName(targertNameSpace, "getAllUser"), new Object[] {},
				new Class[] { UserPojo[].class })[0];
		if (userList != null && userList.length > 0) {
			for (int i = 0; i < userList.length; i++) {
				System.out.println(userList[i].getUserId());
			}
		}
	}

	/**
	 * 模拟请求参数
	 * 
	 * @return
	 */
	private static UserPojo getUser() {
		UserPojo userPojo = new UserPojo();
		userPojo.setSex(true);
		userPojo.setUserId(111);
		userPojo.setUserName("liuhuaiyang");
		return userPojo;
	}
	
	/**
	 * 此方法用于调用服务端有返回值的接口，并且接口的入参数和返回参数都为String类型
	 * 
	 * @param url  接口地址
	 * @param targetNameSpace  接口的命名空间
	 * @param parameter 接口参数
	 * @param methodName  服务方法名称
	 * @return String
	 */
	public static String sendRecevieXmlStringMsg(String url, String targetNameSpace, String parameter, String methodName) {
		String xml = "";
		try {
			RPCServiceClient serviceClient = getRpcClient(url);
			xml = (String) serviceClient.invokeBlocking(new QName(
					targetNameSpace, methodName), new Object[] { parameter },
					new Class[] { String.class })[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml;
	}
	
	/**
	 * 此方法用于调用服务端没有返回值的接口，并且接口的入参为String类型
	 * @param url
	 * @param targetNameSpace
	 * @param parameter
	 * @param methodName
	 */
	public static void sendNoReturnXmlStringMsg(String url,
			String targetNameSpace, String parameter, String methodName) {
		try {
			RPCServiceClient serviceClient = getRpcClient(url);
			serviceClient.invokeRobust(new QName(targetNameSpace, methodName),
					new Object[] { parameter });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得客户端
	 * 
	 * @param url
	 * @return
	 */
	public static RPCServiceClient getRpcClient(String url) {
		RPCServiceClient serviceClient = null;
		try {
			serviceClient = new RPCServiceClient();
			Options options = serviceClient.getOptions();
			// 指定调用WebService的URL
			EndpointReference targetEPR = new EndpointReference(url);
			options.setTo(targetEPR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceClient;
	}
}
