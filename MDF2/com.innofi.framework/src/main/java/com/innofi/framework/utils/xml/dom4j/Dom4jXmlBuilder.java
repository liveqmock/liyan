package com.innofi.framework.utils.xml.dom4j;

import com.innofi.framework.utils.xml.XmlBuilder;
import com.innofi.framework.utils.xml.XmlDocument;
import com.innofi.framework.utils.xml.XmlNode;
import com.innofi.framework.utils.xml.XmlParseException;
import org.dom4j.io.DOMReader;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Dom4jXmlBuilder implements XmlBuilder {
    private static DocumentBuilder builder;
    private static DOMReader domReader;

    private static void prepareDocumentBuilder() {
        if ((builder != null) && (domReader != null)) return;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            builder = dbf.newDocumentBuilder();
            domReader = new DOMReader();
            /*
             *加入word w命名空间
             */
            Map map = new HashMap();
            map.put("w", "http://schemas.openxmlformats.org/wordprocessingml/2006/main");
            domReader.getDocumentFactory().setXPathNamespaceURIs(map);
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (FactoryConfigurationError ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 根据指定字符串构建一个xml文档对象
     *
     * @param s xml文档路径
     * @return xml文档对象
     * @throws java.io.IOException
     * @throws XmlParseException
     */
    public synchronized XmlDocument buildDocument(String s) throws IOException, XmlParseException {
        try {
            prepareDocumentBuilder();
            if ((builder != null) && (domReader != null)) {
                Document document = builder.parse(new ByteArrayInputStream(s.getBytes()));
                return new Dom4jDocument(domReader.read(document));
            }
        } catch (SAXException ex) {
            throw new XmlParseException(ex);
        }
        return null;
    }

    /**
     * 根据文件对象构建一个xml文档对象
     *
     * @param file 文件对象
     * @return 返回一个xml文档对象
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws XmlParseException
     */
    public synchronized XmlDocument buildDocument(File file)
            throws IOException, FileNotFoundException, XmlParseException {
        try {
            prepareDocumentBuilder();
            if ((builder != null) && (domReader != null)) {
                Document document = builder.parse(file);
                return new Dom4jDocument(domReader.read(document));
            }
        } catch (SAXException ex) {
            throw new XmlParseException(ex);
        }
        return null;
    }

    /**
     * 根据输入流构建一个xml文档对象
     *
     * @param in 输入流
     * @return xml文档对象
     * @throws java.io.IOException
     * @throws XmlParseException
     */
    public synchronized XmlDocument buildDocument(InputStream in)
            throws IOException, XmlParseException {
        try {
            prepareDocumentBuilder();
            if ((builder != null) && (domReader != null)) {
                Document document = builder.parse(in);
                return new Dom4jDocument(domReader.read(document));
            }
        } catch (SAXException ex) {
            throw new XmlParseException(ex);
        }
        return null;
    }

    /**
     * 创建一个Xml文档对象
     *
     * @return 返回创建的xml文档对象
     */
    public synchronized XmlDocument newDocument() {
        return new Dom4jDocument();
    }

    /**
     * 创建一个Xml文档节点对象
     *
     * @param name 节点名称
     * @return 返回创建的节点对象
     */
    public synchronized XmlNode newNode(String name) {
        return new Dom4jNode(name);
    }


}