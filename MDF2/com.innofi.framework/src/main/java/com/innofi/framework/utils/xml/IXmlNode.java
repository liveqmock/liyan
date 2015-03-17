package com.innofi.framework.utils.xml;

/**
 * Xml文档节点对象接口
 *
 * @author liumy
 * @date 2010-3-11下午03:31:39
 */
public abstract interface IXmlNode {

    /**
     * 获得Xml节点名称
     *
     * @return String类型节点名称
     */
    public abstract String getName();

    /**
     * 添加XmlNode子节点
     *
     * @param nodeName String类型的节点名称
     * @return 添加的XmlNode对象
     */
    public abstract IXmlNode addChild(String nodeName);

    /**
     * 添加XmlNode子节点
     *
     * @param paramXmlNode XmlNode类型的节点对象
     * @return 添加的XmlNode对象
     */
    public abstract void addChild(IXmlNode paramXmlNode);

    /**
     * 从父节点中删除当前节点
     */
    public abstract void removeFromParent();

    /**
     * 删除当前节点指定子节点
     *
     * @param nodeName 节点名称
     * @return true删除成功，false删除失败
     */
    public abstract boolean removeChild(String nodeName);

    /**
     * 删除当前节点所有子节点
     */
    public abstract void removeChildren();

    /**
     * 获得当前节点的父节点对象
     *
     * @return 父XmlNode对象
     */
    public abstract IXmlNode getParent();

    /**
     * 获得当前节点指定名称的子节点
     *
     * @param nodeName String类型的节点名称
     * @return 子XmlNode对象
     */
    public abstract IXmlNode getChild(String nodeName);

    /**
     * 获得当前节点的所有子节点
     *
     * @return 子XmlNode数组
     */
    public abstract IXmlNode[] getChildren();

    /**
     * 根据XPath获得XmlNode节点
     *
     * @param xp Stirng类型的XPath
     * @return XPath对应的XmlNode节点
     */
    public abstract IXmlNode getNodeByXPath(String xp);

    /**
     * 根据XPath获得XmlNode节点
     *
     * @param xp Stirng类型的XPath
     * @return XPath对应的XmlNode节点数组
     */
    public abstract IXmlNode[] getNodesByXPath(String xp);

    /**
     * 获得当前节点的文本内容,如果该节点没有文本内容返回null
     *
     * @return String类型的文本内容
     */
    public abstract String getContent();

    /**
     * 设置当前节点的文本内容,以String类型设置
     *
     * @param value String类型的文本内容
     */
    public abstract void setContent(String value);

    /**
     * 获得当前节点的文本内容,以String类型返回
     *
     * @return String类型的文本内容, 如果该节点没有文本内容返回""
     */
    public abstract String getContentString();

    /**
     * 获得当前节点的文本内容,以String类型返回
     *
     * @param defaultValue 默认值
     * @return String类型的文本内容, 如果该节点没有文本内容返回defaultValue
     */
    public abstract String getContentString(String defaultValue);

    /**
     * 获得当前节点的文本内容,int类型返回
     *
     * @return int类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0,当文本内容为false返回0,当文本内容为true返回1
     */
    public abstract int getContentInt();

    /**
     * 获得当前节点的文本内容,int类型返回
     *
     * @param defaultValue 默认值
     * @return int类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public abstract int getContentInt(int defaultValue);

    /**
     * 设置当前节点的文本内容,以int类型设置
     *
     * @param value int类型的文本内容
     */
    public abstract void setContentInt(int value);

    /**
     * 获得当前节点的文本内容,long类型返回
     *
     * @return long类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0L,当文本内容为false返回0L,当文本内容为true返回1L
     */
    public abstract long getContentLong();

    /**
     * 获得当前节点的文本内容,int类型返回
     *
     * @param defaultValue 默认值
     * @return int类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public abstract long getContentLong(long defaultValue);

    /**
     * 设置当前节点的文本内容,以long类型设置
     *
     * @param paramLong long类型的文本内容
     */
    public abstract void setContentLong(long paramLong);

    /**
     * 获得当前节点的文本内容,float类型返回
     *
     * @return float类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0.0F,当文本内容为false返回0.0F,当文本内容为true返回1.0F
     */
    public abstract float getContentFloat();

    /**
     * 获得当前节点的文本内容,float类型返回
     *
     * @param defaultValue 默认值
     * @return float类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public abstract float getContentFloat(float defaultValue);

    /**
     * 设置当前节点的文本内容,以float类型设置
     *
     * @param paramFloat float类型的文本内容
     */
    public abstract void setContentFloat(float paramFloat);

    /**
     * 获得当前节点的文本内容,double类型返回
     *
     * @return double类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0.0D,当文本内容为false返回0.0D,当文本内容为true返回1.0D
     */
    public abstract double getContentDouble();

    /**
     * 获得当前节点的文本内容,double类型返回
     *
     * @param defaultValue 默认值
     * @return double类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public abstract double getContentDouble(double defaultValue);

    /**
     * 设置当前节点的文本内容,以double类型设置
     *
     * @param paramDouble double类型的文本内容
     */
    public abstract void setContentDouble(double paramDouble);

    /**
     * 获得当前节点的文本内容,boolean类型返回
     *
     * @return boolean类型的文本内容，如果该节点没有文本内容或者内容不为true,1,-1,T,t,Y,y时返回false，否则返回true
     */
    public abstract boolean getContentBoolean();

    /**
     * 获得当前节点的文本内容,boolean类型返回
     *
     * @return boolean类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public abstract boolean getContentBoolean(boolean paramBoolean);

    /**
     * 设置当前节点的文本内容,以boolean类型设置
     *
     * @param paramBoolean boolean类型的文本内容
     */
    public abstract void setContentBoolean(boolean paramBoolean);

    /**
     * 为当前节点添加属性
     *
     * @param attriName String类型的属性名称
     */
    public abstract void addAttribute(String attriName);

    /**
     * 获得当前节点所有属性的名称
     *
     * @return String[]类型的属性名称数组
     */
    public abstract String[] getAttributeNames();

    /**
     * 获得当前节点指定名称的属性值,如果没有该属性，返回null
     *
     * @param attriName String类型的属性名称
     * @return String类型的属性值
     */
    public abstract String getAttribute(String attriName);

    /**
     * 设置当前节点指定名称属性的值,以String类型设置
     *
     * @param attriName String类型的属性名称
     * @param value     String类型的属性值
     */
    public abstract void setAttribute(String attriName, String value);

    /**
     * 获得当前节点指定名称的属性值,以String类型返回
     *
     * @param attriName String类型的属性名称
     * @return String类型的属性值, 如果该节点的指定名称属性的值为空返回""
     */
    public abstract String getAttributeString(String attriName);

    /**
     * 获得当前节点指定名称的属性值,以String类型返回
     *
     * @param attriName    String类型的属性名称
     * @param defaultValue 默认值
     * @return String类型的属性值, 如果该节点的指定名称属性的值为空返回defaultValue
     */
    public abstract String getAttributeString(String attriName, String defaultValue);

    /**
     * 获得当前节点指定名称的属性值,以int类型返回
     *
     * @param attriName String类型的属性名称
     * @return int类型的属性值, 如果当前节点没有该属性或者该属性值为""返回0,当属性值为false返回0,当属性值为true返回1
     */
    public abstract int getAttributeInt(String attriName);

    /**
     * 获得当前节点指定名称的属性值,以int类型返回
     *
     * @param attriName    String类型的属性名称
     * @param defaultValue 默认值
     * @return int类型的属性值, 如果该节点的指定名称属性的值为空返回defaultValue
     */
    public abstract int getAttributeInt(String attriName, int defaultValue);

    /**
     * 设置当前节点指定名称属性的值,以int类型设置
     *
     * @param attriName String类型的属性名称
     * @param value     int类型的属性值
     */
    public abstract void setAttributeInt(String attriName, int value);

    /**
     * 获得当前节点指定名称的属性值,以long类型返回
     *
     * @param attriName String类型的属性名称
     * @return long类型的属性值, 如果当前节点没有该属性或者该属性值为""返回0L,当属性值为false返回0L,当属性值为true返回1L
     */
    public abstract long getAttributeLong(String attriName);

    /**
     * 获得当前节点指定名称的属性值,以long类型返回
     *
     * @param attriName    String类型的属性名称
     * @param defaultValue 默认值
     * @return long类型的属性值, 如果该节点的指定名称属性的值为空返回defaultValue
     */
    public abstract long getAttributeLong(String attriName, long defaultValue);

    /**
     * 设置当前节点指定名称属性的值,以long类型设置
     *
     * @param attriName String类型的属性名称
     * @param value     long类型的属性值
     */
    public abstract void setAttributeLong(String attriName, long value);

    /**
     * 获得当前节点指定名称的属性值,以float类型返回
     *
     * @param attriName String类型的属性名称
     * @return float类型的属性值, 如果当前节点没有该属性或者该属性值为""返回0.0F,当属性值为false返回0.0F,当属性值为true返回1.0F
     */
    public abstract float getAttributeFloat(String attriName);

    /**
     * 获得当前节点指定名称的属性值,以long类型返回
     *
     * @param attriName    String类型的属性名称
     * @param defaultValue 默认值
     * @return long类型的属性值, 如果该节点的指定名称属性的值为空返回defaultValue
     */
    public abstract float getAttributeFloat(String attriName, float defaultValue);

    /**
     * 设置当前节点指定名称属性的值,以float类型设置
     *
     * @param attriName String类型的属性名称
     * @param value     float类型的属性值
     */
    public abstract void setAttributeFloat(String attriName, float value);

    /**
     * 获得当前节点指定名称的属性值,以double类型返回
     *
     * @param attriName String类型的属性名称
     * @return double类型的属性值, 如果当前节点没有该属性或者该属性值为""返回0.0D,当属性值为false返回0.0D,当属性值为true返回1.0D
     */
    public abstract double getAttributeDouble(String attriName);

    /**
     * 获得当前节点指定名称的属性值,以double类型返回
     *
     * @param attriName    String类型的属性名称
     * @param defaultValue 默认值
     * @return double类型的属性值, 如果该节点的指定名称属性的值为空返回defaultValue
     */
    public abstract double getAttributeDouble(String attriName, double defaultValue);

    /**
     * 设置当前节点指定名称属性的值,以double类型设置
     *
     * @param attriName String类型的属性名称
     */
    public abstract void setAttributeDouble(String attriName, double paramDouble);

    /**
     * 获得当前节点的文本内容,boolean类型返回
     *
     * @param attriName String类型的属性名称
     * @return boolean类型的属性值，如果该节点没有属性值或者内容不为true,1,-1,T,t,Y,y时返回false，否则返回true
     */
    public abstract boolean getAttributeBoolean(String attriName);

    /**
     * 获得当前节点指定名称属性的值,boolean类型返回
     *
     * @param attriName    String类型的属性名称
     * @param defaultValue 默认值
     * @return boolean类型的属性值, 如果该节点没有属性值返回defaultValue
     */
    public abstract boolean getAttributeBoolean(String attriName, boolean defaultValue);

    /**
     * 设置当前节点指定名称属性的值,以boolean类型设置
     *
     * @param attriName String类型的属性名称
     * @param value     boolean类型的属性值
     */
    public abstract void setAttributeBoolean(String attriName, boolean value);

    /**
     * 删除当前节点指定名称的属性
     *
     * @param attriName String类型的属性名称
     */
    public abstract void removeAttribute(String attriName);

    /**
     * 获得当前节点的子节点的文本内容,如果该节点没有文本内容返回null
     *
     * @param childName String类型子节点名称
     * @return String类型的文本内容
     */
    public abstract String getChildContent(String childName);

    /**
     * 设置当前节点子节点的文本内容，以String类型设置
     *
     * @param value String 类型的属性值
     */
    public abstract void setChildContent(String childName, String value);

    /**
     * 获得当前节点的子节点的文本内容,以String类型返回
     *
     * @param childName String类型子节点名称
     * @return String类型的文本内容, 如果该节点没有文本内容返回""
     */
    public abstract String getChildContentString(String childName);

    /**
     * 获得当前节点的子节点的文本内容,以String类型返回
     *
     * @param childName    String类型子节点名称
     * @param defaultValue 默认值
     * @return String类型的文本内容, 如果该节点没有文本内容返回defaultValue
     */
    public abstract String getChildContentString(String childName, String defaultValue);

    /**
     * 获得当前节点的子节点的文本内容,以int类型返回
     *
     * @param childName String类型子节点名称
     * @return int类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0,当文本内容为false返回0,当文本内容为true返回1
     */
    public abstract int getChildContentInt(String childName);

    /**
     * 获得当前节点的子节点的文本内容,以int类型返回
     *
     * @param childName    String类型子节点名称
     * @param defaultValue 默认值
     * @return int类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public abstract int getChildContentInt(String childName, int defaultValue);

    /**
     * 设置当前节点子节点的文本内容，以int类型设置
     *
     * @param attriName int类型的属性名称
     * @param value     int 类型的属性值
     */
    public abstract void setChildContentInt(String attriName, int value);

    /**
     * 获得当前节点的子节点的文本内容,以long类型返回
     *
     * @param childName String类型子节点名称
     * @return long类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0L,当文本内容为false返回0L,当文本内容为true返回1L
     */
    public abstract long getChildContentLong(String childName);

    /**
     * 获得当前节点的子节点的文本内容,以long类型返回
     *
     * @param childName    String类型子节点名称
     * @param defaultValue 默认值
     * @return long类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public abstract long getChildContentLong(String childName, long defaultValue);

    /**
     * 设置当前节点子节点的文本内容，以long类型设置
     *
     * @param attriName long类型的属性名称
     * @param value     long 类型的属性值
     */
    public abstract void setChildContentLong(String attriName, long value);

    /**
     * 获得当前节点的子节点的文本内容,以float类型返回
     *
     * @param childName String类型子节点名称
     * @return float类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0.0F,当文本内容为false返回0.0F,当文本内容为true返回1.0F
     */
    public abstract float getChildContentFloat(String childName);

    /**
     * 获得当前节点的子节点的文本内容,以float类型返回
     *
     * @param childName    String类型子节点名称
     * @param defaultValue 默认值
     * @return float类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public abstract float getChildContentFloat(String childName, float defaultValue);

    /**
     * 设置当前节点子节点的文本内容，以float类型设置
     *
     * @param attriName float类型的属性名称
     * @param value     float 类型的属性值
     */
    public abstract void setChildContentFloat(String attriName, float value);

    /**
     * 获得当前节点的子节点的文本内容,以double类型返回
     *
     * @param childName String类型子节点名称
     * @return double类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0.0D,当文本内容为false返回0.0D,当文本内容为true返回1.0D
     */
    public abstract double getChildContentDouble(String childName);

    /**
     * 获得当前节点的子节点的文本内容,以double类型返回
     *
     * @param childName    String类型子节点名称
     * @param defaultValue 默认值
     * @return double类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public abstract double getChildContentDouble(String childName, double defaultValue);

    /**
     * 设置当前节点子节点的文本内容，以double类型设置
     *
     * @param attriName double类型的属性名称
     * @param value     double 类型的属性值
     */
    public abstract void setChildContentDouble(String attriName, double value);

    /**
     * 获得当前节点子节点的文本内容,boolean类型返回
     *
     * @param attriName String类型的属性名称
     * @return boolean类型的属性值，如果该节点没有属性值或者内容不为true,1,-1,T,t,Y,y时返回false，否则返回true
     */
    public abstract boolean getChildContentBoolean(String attriName);

    /**
     * 获得当前节点的子节点的文本内容,以boolean类型返回
     *
     * @param defaultValue 默认值
     * @return boolean类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public abstract boolean getChildContentBoolean(String attriName, boolean defaultValue);

    /**
     * 设置当前节点子节点的文本内容，以boolean类型设置
     *
     * @param attriName boolean类型的属性名称
     * @param value     boolean 类型的属性值
     */
    public abstract void setChildContentBoolean(String attriName, boolean value);

    /**
     * 复制当前节点
     *
     * @return XmlNode类型的当前节点
     * @throws CloneNotSupportedException
     */
    public abstract IXmlNode cloneNode() throws CloneNotSupportedException;

}


