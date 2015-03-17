package com.innofi.framework.utils.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class AbstractXmlBuilder implements XmlBuilder {
    /**
     * 根据文件对象构建一个xml文档对象
     *
     * @param file 文件对象
     * @return 返回一个xml文档对象
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws XmlParseException
     */
    public final XmlDocument buildDocument(File file) throws IOException, FileNotFoundException, XmlParseException {
        FileInputStream in = new FileInputStream(file);
        try {
            XmlDocument localXmlDocument = buildDocument(in);
            return localXmlDocument;
        } finally {
            in.close();
        }
    }
}