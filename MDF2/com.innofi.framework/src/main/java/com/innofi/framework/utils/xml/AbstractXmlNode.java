package com.innofi.framework.utils.xml;

import com.innofi.framework.utils.variable.VariableHelper;
import org.apache.commons.lang.BooleanUtils;

/**
 * 抽象的Xml文档节点类
 * 该类实现了一些基础的方法，逐如 getContentString() 、getContentInt()、等方法
 *
 * @author liumy
 * @date 2010-3-11下午05:14:08
 */
public abstract class AbstractXmlNode implements XmlNode {
    /**
     * 获得当前节点的文本内容,以String类型返回
     *
     * @return String类型的文本内容, 如果该节点没有文本内容返回""
     */
    public final String getContentString() {
        String v = getContent();
        if (v == null) {
            return "";
        }

        return v;
    }

    /**
     * 获得当前节点的文本内容,以String类型返回
     *
     * @param defaultValue 默认值
     * @return String类型的文本内容, 如果该节点没有文本内容返回defaultValue
     */
    public final String getContentString(String defaultValue) {
        String v = getContent();
        if (v == null) {
            return defaultValue;
        }

        return v;
    }

    /**
     * 获得当前节点的文本内容,int类型返回
     *
     * @return int类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0,当文本内容为false返回0,当文本内容为true返回1
     */
    public final int getContentInt() {
        return VariableHelper.parseInt(getContent());
    }

    /**
     * 获得当前节点的文本内容,int类型返回
     *
     * @param defaultValue 默认值
     * @return int类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public final int getContentInt(int defaultValue) {
        String v = getContent();
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseInt(v);
    }

    /**
     * 设置当前节点的文本内容,以int类型设置
     *
     * @param value int类型的文本内容
     */
    public final void setContentInt(int value) {
        setContent(Integer.toString(value));
    }

    /**
     * 获得当前节点的文本内容,long类型返回
     *
     * @return long类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0L,当文本内容为false返回0L,当文本内容为true返回1L
     */
    public final long getContentLong() {
        return VariableHelper.parseLong(getContent());
    }

    /**
     * 获得当前节点的文本内容,int类型返回
     *
     * @param defaultValue 默认值
     * @return int类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public final long getContentLong(long defaultValue) {
        String v = getContent();
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseLong(v);
    }

    /**
     * 设置当前节点的文本内容,以long类型设置
     *
     * @param value long类型的文本内容
     */
    public final void setContentLong(long value) {
        setContent(Long.toString(value));
    }

    /**
     * 获得当前节点的文本内容,float类型返回
     *
     * @return float类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0.0F,当文本内容为false返回0.0F,当文本内容为true返回1.0F
     */
    public final float getContentFloat() {
        return VariableHelper.parseFloat(getContent());
    }

    /**
     * 获得当前节点的文本内容,float类型返回
     *
     * @param defaultValue 默认值
     * @return float类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public final float getContentFloat(float defaultValue) {
        String v = getContent();
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseFloat(v);
    }

    /**
     * 设置当前节点的文本内容,以float类型设置
     *
     * @param value float类型的文本内容
     */
    public final void setContentFloat(float value) {
        setContent(Float.toString(value));
    }

    /**
     * 获得当前节点的文本内容,double类型返回
     *
     * @return double类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0.0D,当文本内容为false返回0.0D,当文本内容为true返回1.0D
     */
    public final double getContentDouble() {
        return VariableHelper.parseDouble(getContent());
    }

    /**
     * 获得当前节点的文本内容,double类型返回
     *
     * @param defaultValue 默认值
     * @return double类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public final double getContentDouble(double defaultValue) {
        String v = getContent();
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseDouble(v);
    }

    /**
     * 设置当前节点的文本内容,以double类型设置
     *
     * @param value double类型的文本内容
     */
    public final void setContentDouble(double value) {
        setContent(Double.toString(value));
    }

    /**
     * 获得当前节点的文本内容,boolean类型返回
     *
     * @return boolean类型的文本内容，如果该节点没有文本内容或者内容不为true,1,-1,T,t,Y,y时返回false，否则返回true
     */
    public final boolean getContentBoolean() {
        return VariableHelper.parseBoolean(getContent());
    }

    /**
     * 获得当前节点的文本内容,boolean类型返回
     *
     * @param defaultValue 默认值
     * @return boolean类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public final boolean getContentBoolean(boolean defaultValue) {
        String v = getContent();
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseBoolean(v);
    }

    /**
     * 设置当前节点的文本内容,以boolean类型设置
     *
     * @param value boolean类型的文本内容
     */
    public final void setContentBoolean(boolean value) {
        setContent(booleanToString(value));
    }

    /**
     * 获得当前节点指定名称的属性值,以String类型返回
     *
     * @param name String类型的属性名称
     * @return String类型的属性值, 如果该节点的指定名称属性的值为空返回""
     */
    public String getAttributeString(String name) {
        String v = getAttribute(name);
        if (v == null) {
            return "";
        }

        return v;
    }

    /**
     * 获得当前节点指定名称的属性值,以String类型返回
     *
     * @param name         String类型的属性名称
     * @param defaultValue 默认值
     * @return String类型的属性值, 如果该节点的指定名称属性的值为空返回defaultValue
     */
    public String getAttributeString(String name, String defaultValue) {
        String v = getAttribute(name);
        if (v == null) {
            return defaultValue;
        }

        return v;
    }

    /**
     * 获得当前节点指定名称的属性值,以int类型返回
     *
     * @param name String类型的属性名称
     * @return int类型的属性值, 如果当前节点没有该属性或者该属性值为""返回0,当属性值为false返回0,当属性值为true返回1
     */
    public final int getAttributeInt(String name) {
        return VariableHelper.parseInt(getAttribute(name));
    }

    /**
     * 获得当前节点指定名称的属性值,以int类型返回
     *
     * @param name         String类型的属性名称
     * @param defaultValue 默认值
     * @return int类型的属性值, 如果该节点的指定名称属性的值为空返回defaultValue
     */
    public int getAttributeInt(String name, int defaultValue) {
        String v = getAttribute(name);
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseInt(v);
    }

    /**
     * 设置当前节点指定名称属性的值,以int类型设置
     *
     * @param name  String类型的属性名称
     * @param value int类型的属性值
     */
    public final void setAttributeInt(String name, int value) {
        setAttribute(name, Integer.toString(value));
    }

    /**
     * 获得当前节点指定名称的属性值,以long类型返回
     *
     * @param name String类型的属性名称
     * @return long类型的属性值, 如果当前节点没有该属性或者该属性值为""返回0L,当属性值为false返回0L,当属性值为true返回1L
     */
    public final long getAttributeLong(String name) {
        return VariableHelper.parseLong(getAttribute(name));
    }

    /**
     * 获得当前节点指定名称的属性值,以long类型返回
     *
     * @param name         String类型的属性名称
     * @param defaultValue 默认值
     * @return long类型的属性值, 如果该节点的指定名称属性的值为空返回defaultValue
     */
    public long getAttributeLong(String name, long defaultValue) {
        String v = getAttribute(name);
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseLong(v);
    }

    /**
     * 设置当前节点指定名称属性的值,以long类型设置
     *
     * @param name  String类型的属性名称
     * @param value long类型的属性值
     */
    public final void setAttributeLong(String name, long value) {
        setAttribute(name, Long.toString(value));
    }

    /**
     * 获得当前节点指定名称的属性值,以float类型返回
     *
     * @param name String类型的属性名称
     * @return float类型的属性值, 如果当前节点没有该属性或者该属性值为""返回0.0F,当属性值为false返回0.0F,当属性值为true返回1.0F
     */
    public final float getAttributeFloat(String name) {
        return VariableHelper.parseFloat(getAttribute(name));
    }

    /**
     * 获得当前节点指定名称的属性值,以long类型返回
     *
     * @param name         String类型的属性名称
     * @param defaultValue 默认值
     * @return long类型的属性值, 如果该节点的指定名称属性的值为空返回defaultValue
     */
    public float getAttributeFloat(String name, float defaultValue) {
        String v = getAttribute(name);
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseFloat(v);
    }

    /**
     * 设置当前节点指定名称属性的值,以float类型设置
     *
     * @param name  String类型的属性名称
     * @param value float类型的属性值
     */
    public final void setAttributeFloat(String name, float value) {
        setAttribute(name, Float.toString(value));
    }

    /**
     * 获得当前节点指定名称的属性值,以double类型返回
     *
     * @param name String类型的属性名称
     * @return double类型的属性值, 如果当前节点没有该属性或者该属性值为""返回0.0D,当属性值为false返回0.0D,当属性值为true返回1.0D
     */
    public final double getAttributeDouble(String name) {
        return VariableHelper.parseDouble(getAttribute(name));
    }

    /**
     * 获得当前节点指定名称的属性值,以double类型返回
     *
     * @param name         String类型的属性名称
     * @param defaultValue 默认值
     * @return double类型的属性值, 如果该节点的指定名称属性的值为空返回defaultValue
     */
    public double getAttributeDouble(String name, double defaultValue) {
        String v = getAttribute(name);
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseDouble(v);
    }

    /**
     * 设置当前节点指定名称属性的值,以double类型设置
     *
     * @param name  String类型的属性名称
     * @param value double类型的属性值
     */
    public final void setAttributeDouble(String name, double value) {
        setAttribute(name, Double.toString(value));
    }

    /**
     * 获得当前节点的文本内容,boolean类型返回
     *
     * @param name String类型的属性名称
     * @return boolean类型的属性值，如果该节点没有属性值或者内容不为true,1,-1,T,t,Y,y时返回false，否则返回true
     */
    public final boolean getAttributeBoolean(String name) {
        return VariableHelper.parseBoolean(getAttribute(name));
    }

    /**
     * 获得当前节点指定名称属性的值,boolean类型返回
     *
     * @param name         String类型的属性名称
     * @param defaultValue 默认值
     * @return boolean类型的属性值, 如果该节点没有属性值返回defaultValue
     */
    public boolean getAttributeBoolean(String name, boolean defaultValue) {
        String v = getAttribute(name);
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseBoolean(v);
    }

    /**
     * 设置当前节点指定名称属性的值,以boolean类型设置
     *
     * @param name  String类型的属性名称
     * @param value boolean类型的属性值
     */
    public final void setAttributeBoolean(String name, boolean value) {
        setAttribute(name, booleanToString(value));
    }

    /**
     * 获得当前节点的子节点的文本内容,如果该节点没有文本内容返回null
     *
     * @param name String类型子节点名称
     * @return String类型的文本内容
     */
    public String getChildContent(String name) {
        XmlNode node = getChild(name);
        if (node != null) {
            return node.getContent();
        }

        return null;
    }

    /**
     * 设置当前节点子节点的文本内容，以String类型设置
     *
     * @param name  String类型的属性名称
     * @param value String 类型的属性值
     */
    public void setChildContent(String name, String value) {
        XmlNode node = getChild(name);
        if (node == null) {
            node = addChild(name);
        }
        node.setContent(value);
    }

    /**
     * 获得当前节点的子节点的文本内容,以String类型返回
     *
     * @param name String类型子节点名称
     * @return String类型的文本内容, 如果该节点没有文本内容返回""
     */
    public String getChildContentString(String name) {
        String v = getChildContent(name);
        if (v == null) {
            return "";
        }

        return v;
    }

    /**
     * 获得当前节点的子节点的文本内容,以String类型返回
     *
     * @param name         String类型子节点名称
     * @param defaultValue 默认值
     * @return String类型的文本内容, 如果该节点没有文本内容返回defaultValue
     */
    public String getChildContentString(String name, String defaultValue) {
        String v = getChildContent(name);
        if (v == null) {
            return defaultValue;
        }

        return v;
    }

    /**
     * 获得当前节点的子节点的文本内容,以int类型返回
     *
     * @param name String类型子节点名称
     * @return int类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0,当文本内容为false返回0,当文本内容为true返回1
     */
    public int getChildContentInt(String name) {
        return VariableHelper.parseInt(getChildContent(name));
    }

    /**
     * 获得当前节点的子节点的文本内容,以int类型返回
     *
     * @param name         String类型子节点名称
     * @param defaultValue 默认值
     * @return int类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public int getChildContentInt(String name, int defaultValue) {
        String v = getChildContent(name);
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseInt(v);
    }

    /**
     * 设置当前节点子节点的文本内容，以int类型设置
     *
     * @param name  int类型的属性名称
     * @param value int 类型的属性值
     */
    public void setChildContentInt(String name, int value) {
        setChildContent(name, Integer.toString(value));
    }

    /**
     * 获得当前节点的子节点的文本内容,以long类型返回
     *
     * @param name String类型子节点名称
     * @return long类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0L,当文本内容为false返回0L,当文本内容为true返回1L
     */
    public long getChildContentLong(String name) {
        return VariableHelper.parseLong(getChildContent(name));
    }

    /**
     * 获得当前节点的子节点的文本内容,以long类型返回
     *
     * @param name         String类型子节点名称
     * @param defaultValue 默认值
     * @return long类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public long getChildContentLong(String name, long defaultValue) {
        String v = getChildContent(name);
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseLong(v);
    }

    /**
     * 设置当前节点子节点的文本内容，以long类型设置
     *
     * @param name  long类型的属性名称
     * @param value long 类型的属性值
     */
    public void setChildContentLong(String name, long value) {
        setChildContent(name, Long.toString(value));
    }

    /**
     * 获得当前节点的子节点的文本内容,以float类型返回
     *
     * @param name String类型子节点名称
     * @return float类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0.0F,当文本内容为false返回0.0F,当文本内容为true返回1.0F
     */
    public float getChildContentFloat(String name) {
        return VariableHelper.parseFloat(getChildContent(name));
    }

    /**
     * 获得当前节点的子节点的文本内容,以float类型返回
     *
     * @param name         String类型子节点名称
     * @param defaultValue 默认值
     * @return float类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public float getChildContentFloat(String name, float defaultValue) {
        String v = getChildContent(name);
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseFloat(v);
    }

    /**
     * 设置当前节点子节点的文本内容，以float类型设置
     *
     * @param name  float类型的属性名称
     * @param value float 类型的属性值
     */
    public void setChildContentFloat(String name, float value) {
        setChildContent(name, Float.toString(value));
    }

    /**
     * 获得当前节点的子节点的文本内容,以double类型返回
     *
     * @param name String类型子节点名称
     * @return double类型的文本内容，如果该节点没有文本内容或者文本内容为""返回0.0D,当文本内容为false返回0.0D,当文本内容为true返回1.0D
     */
    public double getChildContentDouble(String name) {
        return VariableHelper.parseDouble(getChildContent(name));
    }

    /**
     * 获得当前节点的子节点的文本内容,以double类型返回
     *
     * @param name         String类型子节点名称
     * @param defaultValue 默认值
     * @return double类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public double getChildContentDouble(String name, double defaultValue) {
        String v = getChildContent(name);
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseDouble(v);
    }

    /**
     * 设置当前节点子节点的文本内容，以double类型设置
     *
     * @param name  double类型的属性名称
     * @param value double 类型的属性值
     */
    public void setChildContentDouble(String name, double value) {
        setChildContent(name, Double.toString(value));
    }

    /**
     * 获得当前节点子节点的文本内容,boolean类型返回
     *
     * @param name String类型的属性名称
     * @return boolean类型的属性值，如果该节点没有属性值或者内容不为true,1,-1,T,t,Y,y时返回false，否则返回true
     */
    public boolean getChildContentBoolean(String name) {
        return VariableHelper.parseBoolean(getChildContent(name));
    }

    /**
     * 获得当前节点的子节点的文本内容,以boolean类型返回
     *
     * @param name         String类型子节点名称
     * @param defaultValue 默认值
     * @return boolean类型的文本内容，如果该节点没有文本内容返回defaultValue
     */
    public boolean getChildContentBoolean(String name, boolean defaultValue) {
        String v = getChildContent(name);
        if (v == null) {
            return defaultValue;
        }

        return VariableHelper.parseBoolean(v);
    }

    /**
     * 设置当前节点子节点的文本内容，以boolean类型设置
     *
     * @param name  boolean类型的属性名称
     * @param value boolean 类型的属性值
     */
    public void setChildContentBoolean(String name, boolean value) {
        setChildContent(name, booleanToString(value));
    }

    private String booleanToString(boolean value) {
        return BooleanUtils.toStringTrueFalse(value);
    }

}