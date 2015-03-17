package com.innofi.framework.utils.xml.dom4j;

import com.innofi.framework.utils.xml.AbstractXmlNode;
import com.innofi.framework.utils.xml.XmlNode;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.dom.DOMElement;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;

import java.util.List;


public class Dom4jNode extends AbstractXmlNode {
    private Element element;

    public Dom4jNode(String name) {
        this.element = new DOMElement(name);
    }

    public Dom4jNode(Element element) {
        this.element = element;
    }

    public String getName() {
        return this.element.getName();
    }

    public XmlNode addChild(String name) {
        Element el = this.element.addElement(name);
        return new Dom4jNode(el);
    }

    public void addChild(XmlNode node) {
        Element el = ((Dom4jNode) node).getElement();
        this.element.add(el);
    }

    public void removeFromParent() {
        this.element.detach();
    }

    public boolean removeChild(String name) {
        Element el = this.element.element(name);
        if (el != null) {
            return this.element.remove(el);
        }

        return false;
    }

    public void removeChildren() {
        this.element.elements().clear();
    }

    public XmlNode getParent() {
        return new Dom4jNode(this.element.getParent());
    }

    public XmlNode getChild(String name) {
        Element el = this.element.element(name);
        if (el == null) {
            return null;
        }

        return new Dom4jNode(el);
    }

    public XmlNode[] getChildren() {
        List list = this.element.elements();
        int size = list.size();
        XmlNode[] nodes = new XmlNode[size];
        for (int i = 0; i < size; ++i) {
            nodes[i] = new Dom4jNode((Element) list.get(i));
        }
        return nodes;
    }

    public XmlNode getNodeByXPath(String xpath) {
        try {
            XPath path = new Dom4jXPath(xpath);
            Element el = (Element) path.selectSingleNode(this.element);
            if (el != null) {
                return new Dom4jNode(el);
            }

            return null;
        } catch (JaxenException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public XmlNode[] getNodesByXPath(String xpath) {
        XmlNode[] nodes;
        try {
            XPath path = new Dom4jXPath(xpath);
            List list = path.selectNodes(this.element);
            int size = list.size();
            nodes = new XmlNode[size];
            for (int i = 0; i < size; ++i) {
                nodes[i] = new Dom4jNode((Element) list.get(i));
            }
            return nodes;
        } catch (JaxenException ex) {
            ex.printStackTrace();
            nodes = new XmlNode[0];
        }
        return nodes;
    }

    public String getContent() {
        return this.element.getText();
    }

    public void setContent(String value) {
        this.element.setText(value);
    }

    public void addAttribute(String name) {
        this.element.addAttribute(name, "");
    }

    public String[] getAttributeNames() {
        List attrlist = this.element.attributes();
        int size = attrlist.size();
        String[] attrs = new String[size];
        for (int i = 0; i < size; ++i) {
            attrs[i] = ((Attribute) attrlist.get(i)).getName();
        }
        return attrs;
    }

    public String getAttribute(String name) {
        return this.element.attributeValue(name);
    }

    public void setAttribute(String name, String value) {
        this.element.addAttribute(name, value);
    }

    public void removeAttribute(String name) {
        this.element.addAttribute(name, null);
    }

    public Element getElement() {
        return this.element;
    }

    public XmlNode cloneNode()
            throws CloneNotSupportedException {
        return ((XmlNode) clone());
    }

    public boolean equals(Object obj) {
        if (obj instanceof Dom4jNode) {
            Element el = ((Dom4jNode) obj).getElement();
            return (this.element == el);
        }

        return false;
    }

    public int hashCode() {
        return this.element.hashCode();
    }

    public Object clone()
            throws CloneNotSupportedException {
        return new Dom4jNode((Element) this.element.clone());
    }

    public String toString() {
        return this.element.toString();
    }
}