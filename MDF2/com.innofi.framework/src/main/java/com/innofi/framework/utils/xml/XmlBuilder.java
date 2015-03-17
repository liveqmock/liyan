package com.innofi.framework.utils.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 处理构建Xml的对象
 *
 * @author liumy
 * @date 2010-3-14下午06:28:53
 */
public abstract interface XmlBuilder {

    /**
     * 创建一个Xml文档对象
     *
     * @return 返回创建的xml文档对象
     */
    public abstract XmlDocument newDocument();

    /**
     * 创建一个Xml文档节点对象
     *
     * @param nodeName 节点名称
     * @return 返回创建的节点对象
     */
    public abstract XmlNode newNode(String nodeName);

    /**
     * 根据指定路径构建一个xml文档对象
     *
     * @param xmlPath xml文档路径
     * @return xml文档对象
     * @throws java.io.IOException
     * @throws XmlParseException
     */
    public abstract XmlDocument buildDocument(String xmlPath)
            throws IOException, XmlParseException;

    /**
     * 根据输入流构建一个xml文档对象
     *
     * @param inputStream 输入流
     * @return xml文档对象
     * @throws java.io.IOException
     * @throws XmlParseException
     */
    public abstract XmlDocument buildDocument(InputStream inputStream) throws IOException, XmlParseException;

    /**
     * 根据文件对象构建一个xml文档对象
     *
     * @param file 文件对象
     * @return 返回一个xml文档对象
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws XmlParseException
     */
    public abstract XmlDocument buildDocument(File file) throws IOException, FileNotFoundException, XmlParseException;

}