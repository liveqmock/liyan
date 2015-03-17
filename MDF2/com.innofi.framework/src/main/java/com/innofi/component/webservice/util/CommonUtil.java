package com.innofi.component.webservice.util;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 一般通用类
 *
 * @author lh 2011-11-17
 */
public class CommonUtil {

    /**
     * 根据bean的路径获取其直接上属接口，此处需要约定
     * 格式 例如com.innofi.frameWork.IDemoService
     * com.innofi.frameWork.impl.DemoServcieImpl
     *
     * @param beanPath
     * @return
     */
    public static String getintfPath(String beanPath) {
        String[] a = beanPath.split("\\.");
        String bean = a[a.length - 1];
        String intfName = "I" + bean.substring(0, bean.lastIndexOf("Impl"));
        String intfPath = beanPath.substring(0, beanPath.lastIndexOf("impl")) + intfName;
        return intfPath;
    }

    /**
     * 根据bean的路径获取beanId值
     *
     * @param beanPath
     * @return
     * @throws ClassNotFoundException
     */
    public static String getBeanId(String beanPath, ApplicationContext context) {
        Component componet = null;
        String value = null;
        if (!CommonUtil.isNotEmptyString(beanPath)) {
            return value;
        }
        try {
            componet = Class.forName(beanPath.trim()).getAnnotation(Component.class);
            if (componet != null) {
                value = componet.value();
            }
        } catch (Exception e) {
            value = null;
        }
        if (value == null) {
            try {
                String[] beanIds = context.getBeanNamesForType(Class.forName(beanPath.trim()));
                if (null != beanIds && beanIds.length > 0) {
                    value = beanIds[0];
                }
            } catch (Exception e) {
                value = null;
            }
        }
        return value;
    }


    public static List<String> getMethods(String classPath) throws ClassNotFoundException {
        if (!CommonUtil.isNotEmptyString(classPath)) {
            return null;
        }
        Class<?> clazz = Class.forName(classPath.trim());
        Method[] methods = clazz.getMethods();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String methodName = method.getName();
            System.out.println(methodName);
            list.add(methodName);
        }
        return list;
    }

    /**
     * 判断传入对象是否为空或值为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        // 判断是否为空
        if (obj == null)
            return true;
        // ----------------根据各种对象类型判断是否值为空--------------
        if (obj instanceof String)
            return ((String) obj).trim().equals("");
        if (obj instanceof Collection) {
            Collection coll = (Collection) obj;
            return coll.size() == 0;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return map.size() == 0;
        }
        if (obj.getClass().isArray())
            return Array.getLength(obj) == 0;
        else
            return false;
    }

    /**
     * 产生时间形式的uuid方法+后4位随机数
     *
     * @return
     */
    public static String getUUID() {
        // 定义uuid
        String uuId = "0";
        // 定义时间格式
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String tempId = sf.format(new Date());
        // 构造uuid
        if (Long.parseLong(uuId) >= Long.parseLong(tempId))
            uuId = (new StringBuilder(String.valueOf(Long.parseLong(uuId) + 1L)))
                    .toString();
        else
            uuId = tempId;

        // 返回
        return uuId + get4RandomNum();
    }

    /**
     * 产生时间形式(MMdd)的uuid方法+后4位随机数
     *
     * @return
     */
    public static String getBulletinNo() {
        // 定义uuid
        String uuId = "0";
        // 定义时间格式
        SimpleDateFormat sf = new SimpleDateFormat("yyMM");
        String tempId = sf.format(new Date());
        // 构造uuid
        if (Long.parseLong(uuId) >= Long.parseLong(tempId))
            uuId = (new StringBuilder(String.valueOf(Long.parseLong(uuId) + 1L)))
                    .toString();
        else
            uuId = tempId;

        // 返回
        return uuId + get4RandomNum();
    }

    /**
     * 检查传入值是否为null并处理方法，null则转为空值，否则为其身
     *
     * @param s 为字符串类
     * @return
     */
    public static String jugeAndFixNull(String s) {
        // 判断s是否为null，是则返回空串
        if (s == null) {
            return "";
        } else {// 否则返回其自身
            return s;
        }
    }

    /**
     * 将一个字符串数组转换为long数组，by weiqiang.yang
     *
     * @param strArr
     * @return
     */
    public static Long[] strArrToLongArr(String[] strArr) {
        Long[] retArr = new Long[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            retArr[i] = Long.parseLong(strArr[i]);
        }
        return retArr;
    }

    /**
     * 生成4位随机数
     *
     * @return
     */
    public static String get4RandomNum() {
        Random random = new Random();
        String randomNum = "" + random.nextInt(9) + random.nextInt(9)
                + random.nextInt(9) + random.nextInt(9);
        return randomNum;
    }

    /**
     * 方法功能描述（*）
     *
     * @param dataXml
     * @return
     */
    public static String getLoadResults(String dataXml) {
        String results = "";
        Document doc_new = DocumentHelper.createDocument();
        try {
            Document doc = DocumentHelper.parseText(dataXml);
            Element root = doc.getRootElement();
            String flagText = root.elementText("resultFlag");
            if (CommonUtil.isEmpty(flagText))
                return dataXml;
            if ("0".equals(flagText) || "true".equals(flagText)) {
                List<Element> list = root.element("bodyMsg").elements();
                if (!CommonUtil.isEmpty(list)) {
                    doc_new.add((Element) (list.get(0)).clone());
                    results = doc_new.asXML();
                } else {
                    results = root.element("bodyMsg").getText();
                    if (CommonUtil.isEmpty(results) || "null".equals(results)) {
                        results = "0";
                    }
                }
            } else if (!"0".equals(flagText) || "false".equals(flagText)) {
                results = root.element("errorMsg").getText();
            }
        } catch (DocumentException e) {
            // e.printStackTrace();
            return dataXml;
        }
        return results;
    }

    public static String revertXMLString(String xml) {
        xml = xml.replace("%2B", "+");
        xml = xml.replace("%25", "%");
        xml = xml.replace("%26", "&");
        xml = xml.replace("!#92;", "\\");
        xml = xml.replace("!#60;", "<");
        xml = xml.replace("!#62;", ">");
        xml = xml.replace("!#34;", "\"");
        xml = xml.replace("!#35;", "'");
        return xml;
    }

    /**
     * 判断字符串是否为空，如果为空返回true，否则返回false
     *
     * @param str
     * @return
     */
    public static boolean isEmptyString(String str) {
        if (null == str || "".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 字符串非空判断：如果为空，则返回false，如果不为空返回true
     *
     * @param str
     * @return
     */
    public static boolean isNotEmptyString(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        return true;
    }

    /**
     * 判断List是否为空，如果为空返回true，如果不为空返回false
     *
     * @param list
     * @return
     */
    public static boolean isEmptyCollection(List<?> list) {
        if (null != list && list.size() > 0) {
            return false;
        }
        return true;
    }

    /**
     * List 非空判断，如果为空返回false，如果不为空返回true
     *
     * @param list
     * @return
     */
    public static boolean isNotEmptyCollection(List<?> list) {
        if (null != list && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断map是否为空，如果为空返回true，如果不为空返回false
     *
     * @param map
     * @return
     */
    public static boolean isEmptyMap(Map<?, ?> map) {
        if (null != map && map.size() > 0) {
            return false;
        }
        return true;
    }

    /**
     * Map非空判断，如果map为空返回false，如果map不为空，返回true
     *
     * @param map
     * @return boolean
     */
    public static boolean isNotEmptyMap(Map<?, ?> map) {
        if (null != map && map.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断对象是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmptyObject(Object obj) {
        if (null != obj) {
            return true;
        }
        return false;
    }

    /**
     * 返回一个UUID
     *
     * @return
     */
    public static String randomUUID() {
        String uuid = java.util.UUID.randomUUID().toString()
                .replaceAll("-", "");
        return uuid;
    }

    /**
     * 获取一个字母加数字的随机字符串：
     *
     * @param length
     * @return
     */
    public static String getCharacterAndNumber(int length) {
        String password = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) { // 字符串
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
                password += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                password += String.valueOf(random.nextInt(10));
            }
        }
        password = checkPassword(length, password);
        return password;
    }

    /**
     * 检查是否全是数字或者全是字母<br>
     *
     * @param length
     * @param password password
     */
    public static String checkPassword(int length, String password) {
        Pattern pattern = Pattern
                .compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
        Matcher isNum = pattern.matcher(password);
        if (!isNum.matches()) {
            return getCharacterAndNumber(length);
        }
        return password;
    }

    /**
     * 校验是不是数字
     *
     * @param str
     * @return
     * @desc <br>
     * @create Jul 5, 2012 11:38:36 AM by fangqi<br>
     */
    public static boolean isNumeric(String str) {
        if (str.matches("\\d*")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串非空判断<br>
     * 如果为空，空字符，空格则返回true<br>
     * 否则返回false
     *
     * @param str
     * @return
     */
    public static boolean isEmptyStringOrSpace(String str) {
        if (null == str || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 按设置的长度用空格补齐字符串
     *
     * @param targetStr
     * @param strLength
     * @return
     */
    public static String FormatString(String targetStr, int strLength) {
        int curLength = targetStr.getBytes().length;
        if (targetStr != null && curLength > strLength) {
            targetStr = targetStr.substring(0, strLength);
        }
        String newString = "";
        int cutLength = strLength - targetStr.getBytes().length;
        for (int i = 0; i < cutLength; i++)
            newString += " ";
        return targetStr + newString;
    }

    /**
     * 按设置长度用0补齐数字
     *
     * @param seqs
     * @param length
     * @return
     */
    public static String getSeq(String seqs, int length) {
        String formatString = "%0" + length + "d";
        if (seqs.length() > length) {
            seqs = seqs.substring(seqs.length() - length, seqs.length());
        }
        String str = String.format(formatString, Long.parseLong(seqs));
        return str;
    }

    public static String getYYYYMMddHmmss() {
        String fmt = "yyyyMMddHmmss";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.format(date);
    }
}