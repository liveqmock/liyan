package com.innofi.framework.utils.xml.dom4j;

import com.innofi.framework.utils.xml.DynamicStatementDTDEntityResolver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;


public class Dom4jUtil {
    /**
     * 获得Dom4jDocumen文档对象
     *
     * @param filePath 文件路径
     * @param resolver org.xml.sax.EntityResolver对象，dtd提供者
     * @return
     */
    public static Document getXmlDocument(String filePath, DynamicStatementDTDEntityResolver resolver) throws DocumentException {
        SAXReader builder = new SAXReader();
        builder.setEntityResolver(resolver);
        return builder.read(filePath);
    }

    /**
     * 写xml方法
     *
     * @param filePath xml文件存放地址
     * @param document Dom4jDocumen文档对象
     * @throws java.io.IOException
     */
    public static void writeXml(String filePath, Document document) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter output = new XMLWriter(new FileWriter(filePath), format);
        output.write(document);
        output.close();
    }

}
