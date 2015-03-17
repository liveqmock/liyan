package com.innofi.framework.utils.properties;

import com.innofi.framework.utils.codec.Unicode;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Properties工具类
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public class PropertiesUtil {

    /**
     * 将一个属性集合的内容复制到另一个属性集合当中
     *
     * @param from 源属性集合
     * @param to   目标属性集合
     */
    public static void copyProperties(Properties from, Properties to) {
        Iterator itr = from.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();
            to.setProperty((String) entry.getKey(), (String) entry.getValue());
        }
    }

    /**
     * 将Map当中的键值对以Properties的形式返回
     *
     * @param map 源Map
     * @return 包含map当中内容的Properties对象
     */
    public static Properties fromMap(Map map) {
        Properties p = new Properties();

        Iterator itr = map.entrySet().iterator();

        while (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();

            p.setProperty((String) entry.getKey(), (String) entry.getValue());
        }

        return p;
    }

    /**
     * 将Properties当中的属性以Map的键值对形式返回
     *
     * @param p   源属性对象
     * @param map 存储Properites当中内容的Map
     * @return 包含Properties当中内容的Map
     */
    public static void fromProperties(Properties p, Map map) {
        map.clear();
        Iterator itr = p.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();
            map.put(entry.getKey(), entry.getValue());
        }
    }

    public static Properties load(String s) throws IOException {
        Properties p = new Properties();
        load(p, s);
        return p;
    }

    /**
     * 将字符串信息查分为Properites对象当中的信息
     *
     * @param p 存储拆分字符串后的属性信息的对象
     * @param s 属性信息字符串
     * @throws java.io.IOException
     */
    public static void load(Properties p, String s) throws IOException {
        if (Validator.isNotNull(s)) {
            s = Unicode.toUnicode(s, false);
            s = StringUtil.replace(s, "\\u003d", "=");
            s = StringUtil.replace(s, "\\u000a", "\n");

            p.load(new ByteArrayInputStream(s.getBytes()));

            Enumeration enu = p.propertyNames();

            while (enu.hasMoreElements()) {
                String key = (String) enu.nextElement();
                String value = p.getProperty(key);
                if (value != null) {
                    value = value.trim();
                    p.setProperty(key, value);
                }
            }
        }
    }

    /**
     * 将两个属性对象当中的信息进行整合,p2中的信息复制到p1当中
     *
     * @param p1 复制后的属性对象
     * @param p2 被复制的属性对象
     */
    public static void merge(Properties p1, Properties p2) {
        Enumeration enu = p2.propertyNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            String value = p2.getProperty(key);
            p1.setProperty(key, value);
        }
    }

    /**
     * 将一个Properites属性性对象转换为字符串
     *
     * @param p 属性对象
     * @return 返回重新组装的字符串
     */
    public static String toString(Properties p) {
        StringBuffer sb = new StringBuffer();
        Enumeration enu = p.propertyNames();

        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            sb.append(key);
            sb.append("=");
            sb.append(p.getProperty(key));
            sb.append("\n");
        }

        return sb.toString();
    }


    /**
     * 清除属性对象key中的控制或者\r字符方法
     *
     * @param p 属性对象
     */
    public static void trimKeys(Properties p) {
        Enumeration enu = p.propertyNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            String value = p.getProperty(key);
            String trimmedKey = key.trim();
            if (!(key.equals(trimmedKey))) {
                p.remove(key);
                p.setProperty(trimmedKey, value);
            }
        }
    }

}