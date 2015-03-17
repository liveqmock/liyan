package com.innofi.framework.utils.xml;

/**
 * xml文档对象
 *
 * @author liumy
 * @date 2010-3-14下午09:36:01
 */
public abstract interface IXmlDocument {

    /**
     * 返回xml文档的根节点
     *
     * @return xml节点对象
     */
    public abstract IXmlNode getRootNode();

    /**
     * 设置xml的root节点
     *
     * @param xmlNode
     */
    public abstract void setRootNode(IXmlNode xmlNode);

    /**
     * 创建xml文档的root节点
     *
     * @param nodeName 节点名称
     * @return 返回创建后的xml节点
     */
    public abstract IXmlNode createRootNode(String nodeName);

    /**
     * 根据节点对象id获得对应的节点
     *
     * @param nodeId 节点Id
     * @return 返回id对应的节点对象
     */
    public abstract IXmlNode getNodeById(String nodeId);

    /**
     * 根据xPath获得xml节点对象
     *
     * @param xPath xPath路径
     * @return 与之对应的xml文档节点
     */
    public abstract IXmlNode getNodeByXPath(String xPath);

    /**
     * 根据xPath获得xml节点对象
     *
     * @param paramString xPath路径
     * @return 与之对应的xml文档节点数组
     */
    public abstract IXmlNode[] getNodesByXPath(String paramString);

}