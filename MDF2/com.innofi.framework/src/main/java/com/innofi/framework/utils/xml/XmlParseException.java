package com.innofi.framework.utils.xml;

/**
 * Xml解析异常
 *
 * @author liumy
 * @date 2010-3-25 下午02:34:18
 */
public class XmlParseException extends Exception {
    public XmlParseException() {
    }

    public XmlParseException(String message) {
        super(message);
    }

    public XmlParseException(Throwable t) {
        super(t);
    }
}