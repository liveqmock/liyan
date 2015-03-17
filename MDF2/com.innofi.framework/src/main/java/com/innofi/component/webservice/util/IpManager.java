package com.innofi.component.webservice.util;

import java.net.InetAddress;
import java.util.Map;

/**
 * 获取本机的ip和用户名
 * @author liuhuaiyang
 *
 */
public class IpManager {
	
	/**
	 * 获取当前机器IP地址
	 * @return
	 */
	public static String getLocalHostIP() {
		String ip;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress();
		} catch (Exception ex) {
			ip = "";
		}
		return ip;
	}
	
	/**
	 * 获取当前机器名
	 * @return
	 */
	public static String getLocalHostName() {
		String hostName;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (Exception ex) {
			hostName = "";
		}
		return hostName;
	}
	
	/**
	 * 获取当前正在使用系统的用户名 例如 root用户
	 * @return
	 */
	public static String getLocalUserName() {
		Map<String, String> map = System.getenv();
		String userName = map.get("USERNAME");// 获取用户名
		return userName;
	}
	
	/**
	 * 获取计算机域名 例如 USER-PC
	 * @return
	 */
	public static String getLocalComputerName() {
		Map<String, String> map = System.getenv();
		String userName = map.get("COMPUTERNAME");// 获取计算机域名
		return userName;
	}
	
	/**
	 * 获取计算机所在域名 例如属于\\USER-PC
	 * @return
	 */
	public static String getLocalUserDomain() {
		Map<String, String> map = System.getenv();
		String userName = map.get("USERDOMAIN");// 获取计算机所在域名
		return userName;
	}
}
