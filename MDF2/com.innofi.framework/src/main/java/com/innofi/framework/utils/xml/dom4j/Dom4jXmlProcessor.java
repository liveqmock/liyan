package com.innofi.framework.utils.xml.dom4j;

import com.innofi.framework.utils.xml.XmlBuilder;
import com.innofi.framework.utils.xml.XmlOutputter;
import com.innofi.framework.utils.xml.XmlProcessor;

/**
 * Xml处理器Dom4j实现
 *
 * @author liumy
 * @date 2010-3-14下午06:27:25
 */
public class Dom4jXmlProcessor implements XmlProcessor {

    private Dom4jXmlBuilder builder;

    /**
     * 获得处理器名称
     *
     * @return 处理器对应名称
     */
    public String getName() {
        return "DOM4J";
    }

    /**
     * 获得Xml建造器
     *
     * @return xml建造器
     */
    public XmlBuilder createBuilder() {
        if (this.builder == null) {
            this.builder = new Dom4jXmlBuilder();
        }
        return this.builder;
    }

    /**
     * 获得Xml输出器
     *
     * @return xml输出器
     */
    public XmlOutputter createOutputter() {
        return new Dom4jXmlOutputter();
    }

}