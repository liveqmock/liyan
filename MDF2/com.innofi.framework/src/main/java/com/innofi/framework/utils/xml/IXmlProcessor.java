package com.innofi.framework.utils.xml;

/**
 * Xml处理器
 *
 * @author liumy
 * @date 2010-3-14下午06:25:12
 */
public abstract interface IXmlProcessor {

    /**
     * 获得处理器名称
     *
     * @return 处理器对应名称
     */
    public abstract String getName();

    /**
     * 获得Xml建造器
     *
     * @return xml建造器
     */
    public abstract IXmlBuilder createBuilder();

    /**
     * 获得Xml输出器
     *
     * @return xml输出器
     */
    public abstract IXmlOutputter createOutputter();

}