package com.innofi.framework.utils.xml.dom4j;

import com.innofi.framework.utils.xml.XmlDocument;
import com.innofi.framework.utils.xml.XmlNode;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;

import java.util.List;

public class Dom4jDocument implements XmlDocument {


    private Document document;

    public Dom4jDocument() {
        this.document = new DOMDocument();
    }

    public Dom4jDocument(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return this.document;
    }

    public XmlNode getRootNode() {
        Element root = this.document.getRootElement();
        if (root != null) {
            return new Dom4jNode(root);
        }
        return null;
    }

    public void setRootNode(XmlNode rootNode) {
        Element root = this.document.getRootElement();
        if (root != null) {
            root.detach();
        }
        this.document.setRootElement(((Dom4jNode) rootNode).getElement());
    }

    public XmlNode createRootNode(String name) {
        XmlNode rootNode = new Dom4jNode(name);
        setRootNode(rootNode);
        return rootNode;
    }

    public XmlNode getNodeById(String id) {
        try {
            XPath path = new Dom4jXPath("/=\"" + id + "\"]");
            Element el = (Element) path.selectSingleNode(this.document.getRootElement());
            if (el != null) {
                return new Dom4jNode(el);
            }

            return null;
        } catch (JaxenException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public XmlNode getNodeByXPath(String xpath) {
        try {
            XPath path = new Dom4jXPath(xpath);
            Element el = (Element) path.selectSingleNode(this.document.getRootElement());
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
            List list = path.selectNodes(this.document.getRootElement());
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

}