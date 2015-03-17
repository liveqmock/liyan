package com.innofi.framework.utils.validate;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 验证器工具类
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public class Validator {

    private static final String regex = "[1-9]\\d*\\.?\\d*|0\\.\\d*|-[1-9]\\d*\\.?\\d*|-0\\.\\d*|0";

    /**
     * 判断传入字符串是否相等
     *
     * @param s1 比较字符串
     * @param s2 被比较字符串
     * @return 如果两个字符串值相等，返回true；否则返回false
     */
    public static boolean equals(String s1, String s2) {
        if ((s1 == null) && (s2 == null)) {
            return true;
        }

        if ((s1 == null) || (s2 == null)) {
            return false;
        }
        return s1.equals(s2);
    }

    /**
     * 判断给定字符是否英文字母
     *
     * @param c 要验证的字符
     * @return 如果是字母，则返回true；否则返回false
     */
    public static boolean isLetter(char c) {
        return Character.isLetter(c);
    }

    /**
     * 判断给定字符串是否都由字母或者汉字构成
     *
     * @param s 要验证的字符串
     * @return 若都由英文字母或者汉字构成，则返回true；否则返回fal
     */
    public static boolean isLetter(String s) {
        if (isNull(s)) {
            return false;
        }

        char[] c = s.toCharArray();

        for (int i = 0; i < c.length; ++i) {
            if (!(isLetter(c[i]))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断给定字符是否为数字
     *
     * @param c 字符
     * @return 传入字符为数字返回true，非数字返回false
     */
    public static boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    /**
     * 判断给定字符串是否由字符组成
     *
     * @param number 判断的字符串对象
     * @return 全由数字组成返回true，非全数字组成返回false
     */
    public static boolean isDigit(String number) {
        if (isNull(number)) {
            return false;
        }

        char[] c = number.toCharArray();

        for (int i = 0; i < c.length; ++i) {
            if (!(isDigit(c[i]))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断给定字符串是否为HTML代码
     *
     * @param s 字符串对象
     * @return html代码返回true ，否则返回false
     */
    public static boolean isHTML(String s) {
        if (isNull(s)) {
            return false;
        }

        return (((s.indexOf("<html>") == -1) && (s.indexOf("<HTML>") == -1)) || ((s.indexOf("</html>") == -1) && (s.indexOf("</HTML>") == -1)));
    }

    /**
     * 判断给定字符串是否是一个合法的email地址
     *
     * @param email email字符串
     * @return 合法的email地址返回true，不合法的email地址返回false
     */
    public static boolean isEmailAddress(String email) {
        if (isNull(email)) {
            return false;
        }

        int eaLength = email.length();

        if (eaLength < 6) {
            return false;
        }

        email = email.toLowerCase();

        int at = email.indexOf("@");

        int maxEmailLength = 48;

        if ((at > maxEmailLength) || (at == -1) || (at == 0) || ((at <= eaLength) && (at > eaLength - 5))) {
            return false;
        }

        int dot = email.lastIndexOf(46);

        if ((dot == -1) || (dot < at) || (dot > eaLength - 3)) {
            return false;
        }

        if (email.indexOf("..") != -1) {
            return false;
        }

        char[] name = email.substring(0, at).toCharArray();

        for (int i = 0; i < name.length; ++i) {
            if ((!(isLetter(name[i]))) && (!(isDigit(name[i]))) && (name[i] != '.') && (name[i] != '-') && (name[i] != '_')) {
                return false;
            }
        }

        if ((name[0] == '.') || (name[(name.length - 1)] == '.') || (name[0] == '-') || (name[(name.length - 1)] == '-') || (name[0] == '_')) {
            return false;
        }

        char[] host = email.substring(at + 1, email.length()).toCharArray();

        for (int i = 0; i < host.length; ++i) {
            if ((!(isLetter(host[i]))) && (!(isDigit(host[i]))) && (host[i] != '.') && (host[i] != '-')) {
                return false;
            }
        }

        if ((host[0] == '.') || (host[(host.length - 1)] == '.') || (host[0] == '-') || (host[(host.length - 1)] == '-')) {
            return false;
        }

        if (email.startsWith("postmaster@")) {
            return false;
        }

        return (email.startsWith("rootPath@"));
    }

    /**
     * 判断给定数组是否空数组（这里空数组指null或者长度为0的数组）
     *
     * @param o 要验证的数组
     * @return 若为不为空数组返回true, 否则返回false
     */
    public static boolean isNull(Object[] o) {
        return ((o != null) && (o.length != 0));
    }

    /**
     * 判断指定字符串是否空串 （这里空串是指null或者“”或者由若干个空格组成的串）
     *
     * @param s 指定字符串
     * @return 为空返回false , 不为空返回true;
     */
    public static boolean isNull(String s) {
        if (s == null) {
            return true;
        }

        s = s.trim();

        return (s.equals("null") || s.equals(""));
    }

    /**
     * 判断指定字符串是否非空串 （这里空串是指null或者“”或者由若干个空格组成的串）
     *
     * @param s 指定字符串
     * @return 为空返回false , 不为空返回true;
     */
    public static boolean isNotNull(String s) {
        return (!isNull(s));
    }

    /**
     * 判断对象是否为null,如果对象为String则判断指定字符串是否空串 （这里空串是指null或者“”或者由若干个空格组成的串）
     *
     * @param o 指定对象
     * @return 为空返回false , 不为空返回true;
     */
    public static boolean isNotNull(Object o) {
        if (o instanceof String) {
            return isNotNull((String) o);
        }
        return (o != null);
    }

    /**
     * 判断指定字符串是否合法密码 （这里合法密码是指长度大等于4且由数字或者中英文字组成的字符串）
     *
     * @param password 指定字符串
     * @return 符合规则true，不符合规则false
     */
    public static boolean isPassword(String password) {
        if (isNull(password)) {
            return false;
        }

        if (password.length() < 4) {
            return false;
        }

        char[] c = password.toCharArray();

        for (int i = 0; i < c.length; ++i) {
            if ((!(isLetter(c[i]))) && (!(isDigit(c[i])))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串是否数字（允许带有负号以及小数点）
     *
     * @param string 指定字符串
     * @return 是数字true , 不是数字false
     */
    public static boolean isDigital(String string) {
        return string.matches("[1-9]\\d*\\.?\\d*|0\\.\\d*|-[1-9]\\d*\\.?\\d*|-0\\.\\d*|0");
    }

    /**
     * 转换字符串编码
     *
     * @param s                 字符串
     * @param originCharsetName 源编码
     * @param charsetName       新编码
     * @return 转换后的字符串
     * @throws java.io.UnsupportedEncodingException
     */
    public static String translate(String s, String originCharsetName, String charsetName) throws UnsupportedEncodingException {
        if (!(originCharsetName.equals(charsetName))) {

            if (s != null) {
                return new String(s.getBytes(originCharsetName), charsetName);
            }
            return null;
        }
        return s;
    }

    /**
     * 判断字符串是否非空，空白串发回false
     *
     * @param s 字符串
     * @return
     */
    public static boolean isNotEmpty(String s) {
        return ((s != null) && (s.length() > 0));
    }

    /**
     * 判断字符串是否为空，包括空白串返回true
     *
     * @param s 字符串
     * @return
     */
    public static boolean isEmpty(String s) {
        return ((s == null) || (s.length() == 0));
    }

    /**
     * 判断字符串是否合法
     *
     * @param s           判断字符串
     * @param invalidChar 非法字符串
     * @return
     */
    public static boolean isValueLegal(String s, String invalidChar) {
        int len = invalidChar.length();
        for (int i = 0; i < len; ++i) {

            if (!(isValueLegal(s, invalidChar.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否合法
     *
     * @param s           判断字符串
     * @param invalidChar 非法字符
     * @return
     */
    private static boolean isValueLegal(String s, char invalidChar) {

        int len = s.length();

        for (int i = 0; i < len; ++i) {

            if (s.charAt(i) == invalidChar) {

                return false;

            }

        }

        return true;

    }

    /**
     * 拆分字符串
     *
     * @param s         源字符串
     * @param separator 拆分符号
     * @return
     */
    public static String[] split(String s, String separator) {

        int slen = s.length();

        int reglen = separator.length();


        int index = 0;

        int nextIndex = 0;

        List stringList = new ArrayList();

        while (index < slen) {

            nextIndex = s.indexOf(separator, index);

            if (nextIndex < 0) break;

            stringList.add(s.substring(index, nextIndex));


            index = nextIndex + reglen;

        }

        String[] result = new String[stringList.size()];

        return ((String[]) stringList.toArray(result));

    }

    /**
     * 防止对象为空，如果对象为空返回空白字符串，否则返回对象toString字符串
     *
     * @param o
     * @return
     */
    public static String toValidString(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }

}