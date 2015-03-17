package com.innofi.framework.exception;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import com.innofi.framework.FrameworkConstants;
import com.innofi.component.cache.service.ISysCacheConfigService;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 通过资源key、语言环境，从资源文件中读取囝际化资源信息
 *
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司 <p/>
 * all rights reserved.
 * @author 鄢利  robert.yan@innofi.com.cn
 * @version 2.0 2013-5-08  上午11:37:06
 * @since JDK1.6
 */
public class MessageUtil {
	public static String getMessage(String name) {
		return getMessage(name, null, ContextHolder.getLocale());
	}

	public static String getMessage(String name, Object[] arguments) {
		return getMessage(name, arguments, ContextHolder.getLocale());
	}

	public static String getMessage(String name, Object[] arguments,
			Locale locale) {
		return getMessageByResource(name, arguments, (locale != null) ? locale
				: Locale.getDefault());
	}

	protected static String getMessageByResource(String name,
			Object[] arguments, Locale locale) {
		String value = getMessageByResource(name, locale);
		if (value == null) {
			return "";
		}
		if (value.trim().length() == 0) {
			return value;
		}

		if ((arguments != null) && (arguments.length > 0)) {
			value = MessageFormat.format(value, arguments);
		}
		return value;
	}

	protected static String getMessageByResource(String name, Locale locale) {
		String value = "";

		if ((name == null) || (name.trim().length() == 0)) {
			return "";
		}

		// 从缓存中读取资源文件,如果读取不到，则加载资源文件，然后刷新缓存
		ISysCacheConfigService sysCacheConfigService = ContextHolder.getSpringBean("sysCacheConfigService");
		HashMap bundleMap = (HashMap) sysCacheConfigService.getCacheObject(FrameworkConstants.RESOURCE_BUNDLE_CACHE);
		ArrayList<ResourceBundle> bundleList = (ArrayList) bundleMap.get(locale.toString());

		if (bundleList == null || bundleList.size() <= 0) {
			//加载资源文件，然后放到缓存中
			bundleList = sysCacheConfigService.loadResourceBundles(locale);
		}
		for (ResourceBundle bundle : bundleList) {
			if (bundle.containsKey(name)){
				value = bundle.getString(name);
			}					
		}

		return value;
	}
}