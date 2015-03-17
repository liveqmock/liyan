package com.innofi.framework.utils.xml;

import com.innofi.framework.utils.xml.dom4j.Dom4jXmlProcessor;

/**
 * xml工厂类
 *
 * @author liumy
 * @date 2010-3-14下午09:28:42
 */
public final class XmlFactory {

    private static XmlProcessor xmlProcessor;

    static {
        xmlProcessor = new Dom4jXmlProcessor();
    }

    public static void setXmlProcessor(XmlProcessor processor) {
        xmlProcessor = processor;
    }

    /**
     * 获得处理器名称
     *
     * @return xml处理器名称
     */
    public static String getProcessorName() {
        return xmlProcessor.getName();
    }

    /**
     * 创建xml构建器
     *
     * @return 返回xml构建器
     */
    public static XmlBuilder createXmlBuilder() {
        return xmlProcessor.createBuilder();
    }

    /**
     * 创建xml输出器
     *
     * @return 返回xml输出器
     */
    public static XmlOutputter createXmlOutputter() {
        return xmlProcessor.createOutputter();
    }

}