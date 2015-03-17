package com.innofi.framework.utils.xml.dom4j;

import com.innofi.framework.utils.xml.AbstractXmlOutputter;
import com.innofi.framework.utils.xml.XmlDocument;
import com.innofi.framework.utils.xml.XmlNode;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Dom4jXml输出处理器实现
 *
 * @author liumy
 * @date 2010-3-14下午09:20:46
 */
public class Dom4jXmlOutputter extends AbstractXmlOutputter {

    /**
     * 初始化xmlWriter方法
     *
     * @param xmlWriter
     */
    private void initXmlWriter(XMLWriter xmlWriter) {
        xmlWriter.setEscapeText(true);
    }

    public String outputString(XmlNode xmlNode) {
        try {
            StringWriter writer = new StringWriter();
            output(xmlNode, writer);
            return writer.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String outputString(XmlDocument xmlDocument) {
        try {
            StringWriter writer = new StringWriter();
            output(xmlDocument, writer);
            return writer.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void output(XmlNode xmlNode, OutputStream out) throws IOException {
        XMLWriter xmlWriter = new XMLWriter(out);
        initXmlWriter(xmlWriter);
        xmlWriter.write(((Dom4jNode) xmlNode).getElement());
        xmlWriter.flush();
        xmlWriter.close();
    }

    public void output(XmlNode xmlNode, Writer out) throws IOException {
        XMLWriter xmlWriter = new XMLWriter(out);
        initXmlWriter(xmlWriter);
        xmlWriter.write(((Dom4jNode) xmlNode).getElement());
        xmlWriter.flush();
        xmlWriter.close();
    }

    public void output(XmlDocument xmlDocument, OutputStream out) throws IOException {
        XMLWriter xmlWriter = new XMLWriter(out);
        initXmlWriter(xmlWriter);
        xmlWriter.write(((Dom4jDocument) xmlDocument).getDocument());
        xmlWriter.flush();
        xmlWriter.close();
    }

    public void output(XmlDocument xmlDocument, Writer out) throws IOException {
        XMLWriter xmlWriter = new XMLWriter(out);
        initXmlWriter(xmlWriter);
        xmlWriter.write(((Dom4jDocument) xmlDocument).getDocument());
        xmlWriter.flush();
        xmlWriter.close();
    }

}
