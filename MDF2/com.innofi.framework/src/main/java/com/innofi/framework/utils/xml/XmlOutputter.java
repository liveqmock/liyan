package com.innofi.framework.utils.xml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Xml文档输出处理器
 *
 * @author liumy
 * @date 2010-3-14下午09:07:47
 */
public abstract interface XmlOutputter {

    /**
     * 以String格式输出一个XML节点
     *
     * @param xmlNode 节点对象
     * @return 输出的文档内容
     */
    public abstract String outputString(XmlNode xmlNode);

    /**
     * 将节点输出到文档
     *
     * @param xmlNode 节点对象
     * @param stream  输出流
     * @throws java.io.IOException
     */
    public abstract void output(XmlNode xmlNode, OutputStream stream) throws IOException;

    /**
     * 将节点输出到文档
     *
     * @param xmlNode 节点对象
     * @param writer  文件输出流
     * @throws java.io.IOException
     */
    public abstract void output(XmlNode xmlNode, Writer writer) throws IOException;

    /**
     * 将节点输出到文档
     *
     * @param xmlNode 节点对象
     * @param file    输出文件对象
     * @throws java.io.IOException
     */
    public abstract void output(XmlNode xmlNode, File file) throws IOException;

    /**
     * 将节点输出到文档
     *
     * @param xmlNode  节点对象
     * @param filePath 文件路径
     * @throws java.io.IOException
     */
    public abstract void output(XmlNode xmlNode, String filePath) throws IOException;

    /**
     * 以String格式输出整个XML
     *
     * @param xmlDocument xml文档对象
     * @return 输出的String类型xml内容
     */
    public abstract String outputString(XmlDocument xmlDocument);


    /**
     * 以String格式输出整个XML
     *
     * @param xmlDocument       xml文档对象
     * @param paramOutputStream 参数输出流
     */
    public abstract void output(XmlDocument xmlDocument, OutputStream paramOutputStream) throws IOException;

    /**
     * 以String格式输出整个XML
     *
     * @param xmlDocument xml文档对象
     * @param paramWriter 参数输出流
     */
    public abstract void output(XmlDocument xmlDocument, Writer paramWriter) throws IOException;

    /**
     * 以String格式输出整个XML
     *
     * @param xmlDocument xml文档对象
     * @param paramFile   输出文件
     */
    public abstract void output(XmlDocument xmlDocument, File paramFile) throws IOException;

    /**
     * 以String格式输出整个XML
     *
     * @param xmlDocument     xml文档对象
     * @param paramString文件路径
     */
    public abstract void output(XmlDocument xmlDocument, String paramString) throws IOException;

}