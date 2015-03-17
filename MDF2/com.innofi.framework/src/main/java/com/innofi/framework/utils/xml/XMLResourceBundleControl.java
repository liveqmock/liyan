package com.innofi.framework.utils.xml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

/**
 * 获取应用语言环境，通过语言环境，读取XML国際化资源文件类
 *
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司 <p/>
 * all rights reserved.
 * @author 鄢利  robert.yan@innofi.com.cn
 * @version 2.0 2013-5-08  上午11:37:06
 * @since JDK1.6
 */
public class XMLResourceBundleControl extends ResourceBundle.Control {
	private static String XML = "xml";

	public List<String> getFormats(String baseName) {
		return Collections.singletonList(XML);
	}

	public ResourceBundle newBundle(String baseName, Locale locale,
			String format, ClassLoader loader, boolean reload)
			throws IllegalAccessException, InstantiationException, IOException {

		if ((baseName == null) || (locale == null) || (format == null) || (loader == null)) {
			throw new NullPointerException();
		}
		ResourceBundle bundle = null;
		if (!format.equals(XML)) {
			return null;
		}

		String bundleName = toBundleName(baseName, locale);
		String resourceName = toResourceName(bundleName, format);
		
		URL url = loader.getResource(resourceName);
		if (url == null) {
			return null;
		}
		URLConnection connection = url.openConnection();
		if (connection == null) {
			return null;
		}
		if (reload) {
			connection.setUseCaches(false);
		}
		InputStream stream = connection.getInputStream();
		if (stream == null) {
			return null;
		}
		
		BufferedInputStream bis = new BufferedInputStream(stream);
		bundle = new XMLResourceBundle(bis);
		bis.close();

		return bundle;
	}
	
	/**
	 * 本地测试使用，获得模块需加载的资源文件
	 * 
	 * @return
	 */
	public static ArrayList<ResourceBundle> getResourceBundles(Locale locale) {
		ArrayList<ResourceBundle> bundleList = new ArrayList();

		try {
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resourceResolver.getResources("classpath*:META-INF/resource*.xml");

			for (Resource resource : resources) {
				String  fileURL = resource.getURL().toString();
				String baseName = fileURL.substring(fileURL.indexOf("META-INF"),fileURL.indexOf(".xml"));
				
				ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale,
						ClassUtils.getDefaultClassLoader(),new XMLResourceBundleControl());
				bundleList.add(bundle);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}

		return bundleList;
	}
}

class XMLResourceBundle extends ResourceBundle implements Serializable{
	private Properties props;

	XMLResourceBundle(InputStream stream) throws IOException {
		props = new Properties();
		props.loadFromXML(stream);
	}

	protected Object handleGetObject(String key) {
		return props.getProperty(key);
	}

	public Enumeration<String> getKeys() {
		Set<String> handleKeys = props.stringPropertyNames();
		return Collections.enumeration(handleKeys);
	}
}