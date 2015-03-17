package com.innofi.framework.utils.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 输出Xml文档对象
 *
 * @author liumy
 * @date 2010-3-14下午06:37:03
 */
public abstract class AbstractXmlOutputter implements XmlOutputter {

    public final void output(XmlNode xmlNode, File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        output(xmlNode, out);
        out.close();
    }

    public final void output(XmlNode xmlNode, String filepath) throws IOException {
        output(xmlNode, new File(filepath));
    }

    public final void output(XmlDocument xmlDocument, File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        output(xmlDocument, out);
        out.close();
    }

    public final void output(XmlDocument xmlDocument, String filepath) throws IOException {
        output(xmlDocument, new File(filepath));
    }
}