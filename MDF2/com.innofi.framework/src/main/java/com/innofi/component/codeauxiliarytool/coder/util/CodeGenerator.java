package com.innofi.component.codeauxiliarytool.coder.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.innofi.framework.spring.context.ContextHolder;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class CodeGenerator {

    public static void generate(Map map, Model model) {
        String xml = config(map);
        Document document = null;
        SAXReader reader = null;
        try {
            reader = new SAXReader();
            document = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        for (Iterator iterator = root.elementIterator(); iterator.hasNext(); ) {
            Element element = (Element) iterator.next();
            String packageName = element.element("packageName").getText();
            String className = element.element("className").getText();
            String template = element.element("template").getText();

            String discPathName = model.getDiscPathName();
            if (StringUtils.isBlank(discPathName)) {
                discPathName = ContextHolder.getSystemProperties().getString("mdf.coder.path", "D:/coder");
            }

            packageName = discPathName + "/" + model.getPackageName().replace('.', '/') + "/" + packageName.replace('.', '/');
            File dir = new File(packageName);
            if (!dir.exists())
                dir.mkdirs();
            File outFile = new File(packageName + "/" + className);
            WriteUtil.writeFile(template, outFile.getAbsolutePath(), map);
        }
    }

    private static String config(Map map) {
        InputStreamReader reader = null;
        OutputStreamWriter writer = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        String xml = "";
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            reader = new InputStreamReader(WriteUtil.class.getResourceAsStream("/com/innofi/component/codeauxiliarytool/template/coder-config.xml"), "UTF-8");
            writer = new OutputStreamWriter(byteArrayOutputStream);
            Properties properties = new Properties();
            properties.put("input.encoding", "UTF-8");
            properties.put("output.encoding", "UTF-8");

            Velocity.init(properties);
            VelocityContext context = new VelocityContext(map);
            Velocity.evaluate(context, writer, "", reader);

            writer.flush();
            xml = byteArrayOutputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return xml;
    }
}
