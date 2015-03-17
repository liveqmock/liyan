package com.innofi.component.webservice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.innofi.component.webservice.pojo.GenerateServicePojo;

/**
 * 模块：webservice
 * @author liuhuaiyang
 * @version 2.0.0 13-5-10
 * webservice文件生成处理工具类
 *
 */
public class GenerateXml {

	public static final String SERVICES_FILE_NAME = "services.xml";

	public static final String FILE_ENCODING = "UTF-8";

	/**
	 * 判断xml文档对象的根节点下是否有子节点，如果有返回false，否则返回true
	 * 
	 * @param fileName
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static boolean xmlParse(String fileName) {
		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream file = new FileInputStream(fileName);
			Document document = builder.build(file);// 获得文档对象
			Element root = document.getRootElement();// 获得根节点
			List<Element> list = root.getChildren();
			if (list != null && list.size() > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 把xml内容生成到指定的文件当中
	 * 
	 * @param xml
	 * @param filePath
	 */
	public static void buildXmlFile(String xml, String filePath) {
		try {
			//FileUtils.forceMkdir(new File(filePath));
			FileUtils.writeStringToFile(new File(filePath
					+ GenerateXml.SERVICES_FILE_NAME), xml,
					GenerateXml.FILE_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 在xml文件中生成serviceGroup根节点
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * 
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static String buildNullXmlDoc() {
		// serviceGroup根节点
		Element root = new Element("serviceGroup");
		// 根节点添加到文档中
		Document doc = new Document(root);
		XMLOutputter xmlOut = new XMLOutputter();
		xmlOut.setFormat(Format.getPrettyFormat());
		String retStr = xmlOut.outputString(doc);
		return retStr;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param path
	 */
	public static void removeDir(String path) {
		try {
			FileUtils.deleteDirectory(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 */
	public static void makeDir(String path) {
		try {
			FileUtils.forceMkdir(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据对象列表生成对应的services.xml文件
	 * 
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static String createOperationServiceXml(
			List<GenerateServicePojo> serviceList) {

		if (null == serviceList || serviceList.size() == 0) {
			return "";
		}

		// serviceGroup根节点
		Element root = new Element("serviceGroup");
		// 根节点添加到文档中
		Document doc = new Document(root);

		for (GenerateServicePojo servicePojo : serviceList) {
			// 创建节点 service;

			Element elements = new Element("service");

			// 给 service 节点添加属性 name和targetNamespace;

			elements.setAttribute("name", servicePojo.getServiceName());
			elements.setAttribute("targetNamespace",
					"http://" + servicePojo.getClassPath());

			// 给service节点添加子节点parameter,此处固定写法
			Element parameter = new Element("parameter");
			parameter.setAttribute("name", "ServiceObjectSupplier");
			parameter
					.setText("org.apache.axis2.extensions.spring.receivers.SpringServletContextObjectSupplier");
			elements.addContent(parameter);

			// 给service节点添加子节点schema
			Element schema = new Element("schema");
			schema.setAttribute("schemaNamespace",
					"http://" + servicePojo.getClassPath());
			elements.addContent(schema);

			// 给service节点添加子节点parameter,用于配置spring bean id
			Element parameterBeanName = new Element("parameter");
			parameterBeanName.setAttribute("name", "SpringBeanName");
			// 动态获取text值
			parameterBeanName.setText(servicePojo.getSpringBeanName());
			elements.addContent(parameterBeanName);

			// 给service节点添加子节点parameter,用于配置接口路径
			Element parameterServiceClass = new Element("parameter");
			parameterServiceClass.setAttribute("name", "ServiceClass");
			// 动态获取接口路径
			parameterServiceClass.setText(servicePojo.getServiceClass());
			elements.addContent(parameterServiceClass);

			for (int i = 0; i < servicePojo.getMethods().length; i++) {
				Element operations = new Element("operation");
				operations.setAttribute("name", ""
						+ servicePojo.getMethods()[i]);

				Element messageReceiverInout = new Element("messageReceiver");
				messageReceiverInout.setAttribute("mep",
						"http://www.w3.org/2004/08/wsdl/in-out");
				messageReceiverInout.setAttribute("class",
						"org.apache.axis2.rpc.receivers.RPCMessageReceiver");
				operations.addContent(messageReceiverInout);
				elements.addContent(operations);
			}
			root.addContent(elements);
		}

		XMLOutputter xmlOut = new XMLOutputter();
		xmlOut.setFormat(Format.getPrettyFormat());
		String ret = xmlOut.outputString(doc);
		// makeDir(generatePath);
		// try {
		// xmlOut.output(doc, new FileOutputStream(generatePath +
		// GenerateXml.SERVICES_FILE_NAME));
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return ret;
	}
}
