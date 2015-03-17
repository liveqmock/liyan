package com.innofi.framework.utils.xml;

import net.sf.cglib.beans.BeanMap;
import org.apache.commons.beanutils.ConvertUtils;

import java.util.Properties;

/**
 * Xml配置工具类
 *
 * @author liumy
 * @date 2010-3-14下午09:43:47
 */
public final class XmlConfigUtil {

    /**
     * 根据名称自动读取一个XML节点下的所有attribute中的值到对象的属性中
     *
     * @param node
     * @param o
     * @throws com.innofi.framework.utils.xml.LoadXmlPropertyException
     */
    public static void loadAttributes(XmlNode node, Object o) throws LoadXmlPropertyException {
        doLoadAttributesToObject(node, o, null);
    }

    /**
     * 根据名称自动读取一个XML节点下的所有attribute中的值到对象的属性中
     *
     * @param node
     * @param o
     * @param except
     * @throws com.innofi.framework.utils.xml.LoadXmlPropertyException
     */
    public static void loadAttributes(XmlNode node, Object o, String except) throws LoadXmlPropertyException {
        String[] excepts = {except};
        doLoadAttributesToObject(node, o, excepts);
    }

    /**
     * 根据名称自动读取一个XML节点下的所有attribute中的值到对象的属性中
     *
     * @param node
     * @param o
     * @param excepts
     * @throws com.innofi.framework.utils.xml.LoadXmlPropertyException
     */
    public static void loadAttributes(XmlNode node, Object o, String[] excepts) throws LoadXmlPropertyException {
        doLoadAttributesToObject(node, o, excepts);
    }

    /**
     * 根据名称自动读取一个XML节点下的所有子节点中的值到对象的属性中
     *
     * @param node
     * @param o
     * @throws com.innofi.framework.utils.xml.LoadXmlPropertyException
     */
    public static void loadChildNodes(XmlNode node, Object o) throws LoadXmlPropertyException {
        doLoadChildNodesToObject(node, o, null);
    }

    /**
     * 根据名称自动读取一个XML节点下的所有子节点中的值到对象的属性中
     *
     * @param node
     * @param o
     * @param except
     * @throws com.innofi.framework.utils.xml.LoadXmlPropertyException
     */
    public static void loadChildNodes(XmlNode node, Object o, String except) throws LoadXmlPropertyException {
        String[] excepts = {except};
        doLoadChildNodesToObject(node, o, excepts);
    }

    /**
     * 根据名称自动读取一个XML节点下的所有子节点中的值到对象的属性中
     *
     * @param node
     * @param o
     * @param excepts
     * @throws com.innofi.framework.utils.xml.LoadXmlPropertyException
     */
    public static void loadChildNodes(XmlNode node, Object o, String[] excepts) throws LoadXmlPropertyException {
        doLoadChildNodesToObject(node, o, excepts);
    }

    /**
     * 根据名称自动读取一个XML节点下的所有子节点中的值到对象的属性中
     *
     * @param node
     * @param o
     * @throws com.innofi.framework.utils.xml.LoadXmlPropertyException
     */
    public static void loadChildProperties(XmlNode node, Object o) throws LoadXmlPropertyException {
        XmlNode[] children = node.getChildren();
        if (children.length == 0) {
            return;
        }

        BeanMap beanMap = BeanMap.create(o);
        for (int i = 0; i < children.length; ++i) {
            XmlNode child = children[i];
            if ("property".equals(child.getName())) {
                String name = child.getAttribute("name");
                try {
                    Object value = child.getAttribute("value");
                    if (value != null) {
                        Class type = beanMap.getPropertyType(name);
                        if ((type != null) && (!(type.equals(String.class)))) {
                            value = ConvertUtils.convert((String) value, type);
                        }
                        beanMap.put(name, value);
                    }
                } catch (Exception ex) {
                    throw new LoadXmlPropertyException("Can not load XML node \"" + name + "\" automatically!");
                }
            }
        }
    }

    /**
     * 将所有名称为property的节点中的名称和值加载到给定的properties中
     *
     * @param document   xml文档对象
     * @param properties 存储属性的对象
     * @throws com.innofi.framework.utils.xml.LoadXmlPropertyException
     */
    public static void loadProperties(XmlDocument document, Properties properties) throws LoadXmlPropertyException {
        XmlNode[] propertyNodes = document.getNodesByXPath("/properties/property");
        for (int i = 0; i < propertyNodes.length; ++i) {
            XmlNode propertyNode = propertyNodes[i];
            String name = propertyNode.getAttribute("name");
            try {
                properties.setProperty(name, propertyNode.getAttribute("value"));
            } catch (Exception ex) {
                throw new LoadXmlPropertyException("Can not load XML node \"" + name + "\" automatically!");
            }
        }
    }


    /**
     * 将当前节点的下级名称为"property"的子节点中名称和值加载到给定的properties中
     *
     * @param node       xml节点对象
     * @param properties 存储属性的对象
     * @throws com.innofi.framework.utils.xml.LoadXmlPropertyException
     */
    public static void loadChildProperties(XmlNode node, Properties properties) throws LoadXmlPropertyException {
        XmlNode[] children = node.getChildren();
        for (int i = 0; i < children.length; ++i) {
            XmlNode child = children[i];
            if ("property".equals(child.getName())) {
                String name = child.getAttribute("name");
                try {
                    properties.setProperty(name, child.getAttribute("value"));
                } catch (Exception ex) {
                    throw new LoadXmlPropertyException("Can not load XML node \"" + name + "\" automatically!");
                }
            }
        }
    }

    /**
     * 是否需要排除
     *
     * @param excepts
     * @param name
     * @return
     */
    private static boolean isExcepted(String[] excepts, String name) {

        if (excepts == null) {
            return false;
        }

        if (name == null) {
            return true;
        }

        boolean b = false;

        for (int i = 0; i < excepts.length; ++i) {
            if (name.equals(excepts[i])) {
                b = true;
                break;
            }
        }

        return b;
    }

    /**
     * 根据名称自动读取一个XML节点下的所有attribute中的值到对象的属性中。
     *
     * @param node    xml节点对象
     * @param o
     * @param excepts
     * @throws com.innofi.framework.utils.xml.LoadXmlPropertyException
     */
    private static void doLoadAttributesToObject(XmlNode node, Object o, String[] excepts) throws LoadXmlPropertyException {
        String[] names = node.getAttributeNames();
        if (names.length == 0) {
            return;
        }
        BeanMap beanMap = BeanMap.create(o);
        for (int i = 0; i < names.length; ++i) {
            String name = names[i];
            if (isExcepted(excepts, name)) {
                continue;
            }
            try {
                Object value = node.getAttribute(name);
                if (value != null) {
                    Class type = beanMap.getPropertyType(name);
                    if ((type != null) && (!(type.equals(String.class)))) {
                        value = ConvertUtils.convert((String) value, type);
                    }
                    beanMap.put(name, value);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new LoadXmlPropertyException("Can not load XML node \"" + name + "\" automatically!");
            }
        }
    }

    /**
     * 根据名称自动读取一个XML节点下的所有子节点中的值到对象的属性中。
     *
     * @param node
     * @param o
     * @param excepts
     * @throws com.innofi.framework.utils.xml.LoadXmlPropertyException
     */
    private static void doLoadChildNodesToObject(XmlNode node, Object o, String[] excepts)
            throws LoadXmlPropertyException {
        XmlNode[] children = node.getChildren();
        if (children.length == 0) {
            return;
        }
        BeanMap beanMap = BeanMap.create(o);
        for (int i = 0; i < children.length; ++i) {
            XmlNode child = children[i];
            String name = child.getName();
            if (isExcepted(excepts, name))
                continue;
            try {
                Object value = child.getContent();
                if (value != null) {
                    Class type = beanMap.getPropertyType(name);
                    if ((type != null) && (!(type.equals(String.class)))) {
                        value = ConvertUtils.convert((String) value, type);
                    }
                    beanMap.put(name, value);
                }
            } catch (Exception ex) {
                throw new LoadXmlPropertyException("Can not load XML node \"" + name + "\" automatically!");
            }
        }
    }
}