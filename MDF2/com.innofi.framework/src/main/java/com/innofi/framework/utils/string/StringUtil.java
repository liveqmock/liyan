package com.innofi.framework.utils.string;

import com.innofi.framework.exception.FrameworkException;
import com.innofi.framework.exception.FrameworkRuntimeException;
import com.innofi.framework.utils.random.Randomizer;
import com.innofi.framework.utils.validate.Validator;

import java.io.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 字符串工具类
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public class StringUtil {

    /**
     * 标准文件字符集
     */
    private static final String STANDARD_CHARSET = "UTF-8";

    /**
     * boolean型常量false,可作为boolean型的默认值
     */
    public static final boolean DEFAULT_BOOLEAN = false;

    /**
     * double型常量0.0,可作为double型的默认值
     */
    public static final double DEFAULT_DOUBLE = 0.0D;

    /**
     * float型常量0,可作为float型的默认值
     */
    public static final float DEFAULT_FLOAT = 0.0F;

    /**
     * int型常量0,可作为int型的默认值
     */
    public static final int DEFAULT_INTEGER = 0;

    /**
     * long型常量0,可作为long型的默认值
     */
    public static final long DEFAULT_LONG = 0L;

    /**
     * short型常量0,可作为short型的默认值
     */
    public static final short DEFAULT_SHORT = 0;

    /**
     * String型常量"",可作为String型的默认值
     */
    public static final String DEFAULT_STRING = "";

    /**
     * String常量数组，表示booleans字符{"true", "t", "y", "on", "1"}
     */
    public static String[] BOOLEANS = {"true", "t", "y", "on", "1"};

    /**
     * 将给定字符串，转换为相应boolean型
     *
     * @param value 给定字符串
     * @return 相应boolean型常量
     */
    public static boolean getBoolean(String value) {
        return getBoolean(value, false);
    }

    /**
     * 将给定字符串，转换为相应boolean型
     *
     * @param value        给定字符串
     * @param defaultValue 如果不能直接转换为对应boolean型，则取的默认值
     * @return 相应boolean型常量
     */
    public static boolean getBoolean(String value, boolean defaultValue) {
        return get(value, defaultValue);
    }

    /**
     * 将给定的字符串转换为对应的Date对象
     *
     * @param value 给定的时间字符串
     * @param df    给定的时间字符串的格式
     * @return 若直接转换失败，则取的默认值
     */
    public static Date getDate(String value, DateFormat df) {
        return getDate(value, df, new Date());
    }

    /**
     * 将给定的字符串转换为对应的Date对象
     *
     * @param value 给定的时间字符串
     * @param df    给定的时间字符串的格式
     * @return 若直接转换失败，则取的默认值
     */
    public static Date getDate(String value, DateFormat df, Date defaultValue) {
        return get(value, df, defaultValue);
    }

    /**
     * 将给定字符串转换为对应double形式
     *
     * @param value 给定的字符串
     * @return 字符串对应的double形式的值
     */
    public static double getDouble(String value) {
        return getDouble(value, 0.0D);
    }

    /**
     * 将给定字符串转换为对应double形式
     *
     * @param value        给定的字符串
     * @param defaultValue 若直接转换失败，则取得的默认值
     * @return 字符串对应的double形式的值
     */
    public static double getDouble(String value, double defaultValue) {
        return get(value, defaultValue);
    }


    /**
     * 将给定字符串转换为对应float形式
     *
     * @param value 给定的字符串
     * @return 字符串对应的float形式的值
     */
    public static float getFloat(String value) {
        return getFloat(value, 0.0F);
    }

    /**
     * 将给定字符串转换为对应float形式
     *
     * @param value        给定的字符串
     * @param defaultValue 若直接转换失败，则取得的默认值
     * @return 字符串对应的float形式的值
     */
    public static float getFloat(String value, float defaultValue) {
        return get(value, defaultValue);
    }

    /**
     * 将给定字符串转换为对应int形式
     *
     * @param value 给定的字符串
     * @return 字符串对应的int形式的值
     */
    public static int getInteger(String value) {
        return getInteger(value, 0);
    }

    /**
     * 将给定字符串转换为对应int形式
     *
     * @param value        给定的字符串
     * @param defaultValue 若直接转换失败，则取得的默认值
     * @return 字符串对应的int形式的值
     */
    public static int getInteger(String value, int defaultValue) {
        return get(value, defaultValue);
    }

    /**
     * 将给定字符串转换为对应long形式
     *
     * @param value 给定的字符串
     * @return 字符串对应的long形式的值
     */
    public static long getLong(String value) {
        return getLong(value, 0L);
    }

    /**
     * 将给定字符串转换为对应long形式
     *
     * @param value        给定的字符串
     * @param defaultValue 若直接转换失败，则取得的默认值
     * @return 字符串对应的long形式的值
     */
    public static long getLong(String value, long defaultValue) {
        return get(value, defaultValue);
    }

    /**
     * 将给定字符串转换为对应short形式
     *
     * @param value 给定的字符串
     * @return 字符串对应的short形式的值
     */
    public static short getShort(String value) {
        return getShort(value, (short) 0);
    }

    /**
     * 将给定字符串转换为对应short形式
     *
     * @param value        给定的字符串
     * @param defaultValue 若直接转换失败，则取得的默认值
     * @return 字符串对应的short形式的值
     */
    public static short getShort(String value, short defaultValue) {
        return get(value, defaultValue);
    }

    /**
     * 将给定字符串转换为规范的String形式,即将"\r\n"以 "\n"替换（若直接转换失败，则取得的默认值）
     *
     * @param value 给定的字符串
     * @return 转换后的字符串
     */
    public static String getString(String value) {
        return getString(value, "");
    }

    /**
     * 将给定字符串转换为规范的String形式,即将"\r\n"以 "\n"替换
     *
     * @param value        给定的字符串
     * @param defaultValue 若直接转换失败，则取得的默认值
     * @return 转换后的字符串
     */
    public static String getString(String value, String defaultValue) {
        return get(value, defaultValue);
    }

    /**
     * 将给定字符串，转换为相应boolean型
     *
     * @param value        给定字符串
     * @param defaultValue 如果不能直接转换为对应boolean型，则取的默认值
     * @return 相应boolean型常量
     */
    public static boolean get(String value, boolean defaultValue) {
        if (value != null) {
            try {
                value = value.trim();
                return ((!(value.equalsIgnoreCase(BOOLEANS[0]))) && (!(value.equalsIgnoreCase(BOOLEANS[1]))) && (!(value.equalsIgnoreCase(BOOLEANS[2]))) && (!(value.equalsIgnoreCase(BOOLEANS[3]))) && (!(value.equalsIgnoreCase(BOOLEANS[4]))));
            } catch (Exception e) {
            }

        }

        return defaultValue;
    }

    /**
     * 将给定字符串，转换为相应Date型
     *
     * @param value        给定字符串
     * @param defaultValue 如果不能直接转换为对应Date型，则取的默认值
     * @return 相应Date型常量
     */
    public static Date get(String value, DateFormat df, Date defaultValue) {
        try {
            Date date = df.parse(value.trim());

            if (date != null) {
                return date;
            }
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * 将给定字符串，转换为相应double型
     *
     * @param value        给定字符串
     * @param defaultValue 如果不能直接转换为对应double型，则取的默认值
     * @return 相应double型常量
     */
    public static double get(String value, double defaultValue) {
        try {
            return Double.parseDouble(_trim(value));
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * 将给定字符串，转换为相应float型
     *
     * @param value        给定字符串
     * @param defaultValue 如果不能直接转换为对应float型，则取的默认值
     * @return 相应float型常量
     */
    public static float get(String value, float defaultValue) {
        try {
            return Float.parseFloat(_trim(value));
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * 将给定字符串，转换为相应int型
     *
     * @param value        给定字符串
     * @param defaultValue 如果不能直接转换为对应int型，则取的默认值
     * @return 相应int型常量
     */
    public static int get(String value, int defaultValue) {
        try {
            return Integer.parseInt(_trim(value));
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * 将给定字符串，转换为相应long型
     *
     * @param value        给定字符串
     * @param defaultValue 如果不能直接转换为对应long型，则取的默认值
     * @return 相应long型常量
     */
    public static long get(String value, long defaultValue) {
        try {
            return Long.parseLong(_trim(value));
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * 将给定字符串，转换为相应short型
     *
     * @param value        给定字符串
     * @param defaultValue 如果不能直接转换为对应short型，则取的默认值
     * @return 相应short型常量
     */
    public static short get(String value, short defaultValue) {
        try {
            return Short.parseShort(_trim(value));
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * 将给定字符串，转换为相应String型
     *
     * @param value        给定字符串
     * @param defaultValue 如果不能直接转换为对应String型，则取的默认值
     * @return 相应String型常量
     */
    public static String get(String value, String defaultValue) {
        if (value != null) {
            value = value.trim();
            value = StringUtil.replace(value, "\r\n", "\n");
            return value;
        }

        return defaultValue;
    }

    private static String _trim(String value) {
        if (value != null) {
            value = value.trim();

            StringBuffer sb = new StringBuffer();

            char[] charArray = value.toCharArray();

            for (int i = 0; i < charArray.length; ++i) {
                if ((!(Character.isDigit(charArray[i]))) && (((charArray[i] != '-') || (i != 0))) && (charArray[i] != '.')) {
                    continue;
                }

                sb.append(charArray[i]);
            }

            value = sb.toString();
        }

        return value;
    }


    /**
     * 取得指定子串在字符串中出现的次数。
     * <p/>
     * <p>
     * 如果字符串为<codebuilder>null</codebuilder>或空，则返回<codebuilder>0</codebuilder>。
     * <pre>
     * StringUtil.countMatches(null, *)       = 0
     * StringUtil.countMatches("", *)         = 0
     * StringUtil.countMatches("abba", null)  = 0
     * StringUtil.countMatches("abba", "")    = 0
     * StringUtil.countMatches("abba", "a")   = 2
     * StringUtil.countMatches("abba", "ab")  = 1
     * StringUtil.countMatches("abba", "xxx") = 0
     * </pre>
     * </p>
     *
     * @param str    要扫描的字符串
     * @param subStr 子字符串
     * @return 子串在字符串中出现的次数，如果字符串为<codebuilder>null</codebuilder>或空，则返回<codebuilder>0</codebuilder>
     */
    public static int countMatches(String str, String subStr) {
        if ((str == null) || (str.length() == 0) || (subStr == null) || (subStr.length() == 0)) {
            return 0;
        }

        int count = 0;
        int index = 0;

        while ((index = str.indexOf(subStr, index)) != -1) {
            count++;
            index += subStr.length();
        }

        return count;
    }

    /**
     * 过滤空参数
     *
     * @param param
     * @return
     */
    public static String filterParamNull(String param) {
        if (param == null || param.trim().equals("") || param.trim().equals("null")) {
            param = null;
        }
        return param;
    }

    /**
     * 判断源字符串中是否包含指定字符串
     *
     * @param s    源字符串
     * @param text 指定字符串
     * @return 若包含返回true, 否则返回false
     */
    public static boolean contains(String s, String text) {
        return contains(s, text, ",");
    }

    /**
     * 判断源字符串中是否包含指定字符串
     *
     * @param s         源字符串
     * @param text      指定字符串
     * @param delimiter 字串分隔符
     * @return 若包含返回true, 否则返回false
     */
    public static boolean contains(String s, String text, String delimiter) {
        if ((s == null) || (text == null) || (delimiter == null)) {
            return false;
        }

        if (!(s.endsWith(delimiter))) {
            s = s + delimiter;
        }

        int pos = s.indexOf(delimiter + text + delimiter);

        if (pos == -1) {
            return (!(s.startsWith(text + delimiter)));
        }

        return true;
    }

    /**
     * 返回源字串包含指定字串数量
     *
     * @param s    源字串
     * @param text 指定字串
     * @return 指定字串在源字串中的个数
     */
    public static int containCount(String s, String text) {
        if ((s == null) || (text == null)) {
            return 0;
        }

        int count = 0;

        int pos = s.indexOf(text);

        while (pos != -1) {
            pos = s.indexOf(text, pos + text.length());
            ++count;
        }

        return count;
    }

    /**
     * 判断源字串是否以指定字符结尾
     *
     * @param s   源字串
     * @param end 指定字符
     * @return 若源字串以指定字符结尾返回true, 否则返回false
     */
    public static boolean endsWith(String s, char end) {
        return endsWith(s, new Character(end).toString());
    }

    /**
     * 判断源字串是否以指定字符串结尾
     *
     * @param s   源字串
     * @param end 指定字符串
     * @return 若源字串以指定字符串结尾返回true, 否则返回false
     */
    public static boolean endsWith(String s, String end) {
        if ((s == null) || (end == null)) {
            return false;
        }

        if (end.length() > s.length()) {
            return false;
        }

        String temp = s.substring(s.length() - end.length(), s.length());

        return temp.equalsIgnoreCase(end);
    }

    /**
     * 提取源字串中的字符（英文字母）
     *
     * @param s 源字串
     * @return 提取字符组成的新字串
     */
    public static String extractChars(String s) {
        if (s == null) {
            return "";
        }

        char[] c = s.toCharArray();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < c.length; ++i) {
            if (Validator.isLetter(c[i])) {
                sb.append(c[i]);
            }
        }

        return sb.toString();
    }

    /**
     * 源字符串 字符串首字母大写
     *
     * @param source
     * @return
     */
    public static String upperFirst(String source) {
        String str = source.replaceFirst(source.substring(0, 1), source.substring(0, 1).toUpperCase());
        return str;
    }

    /**
     * 源字符串 字符串首字母小写
     *
     * @param source
     * @return
     */
    public static String lowerFirst(String source) {
        String str = source.replaceFirst(source.substring(0, 1), source.substring(0, 1).toLowerCase());
        return str;
    }

    /**
     * 提取源字串中的数字
     *
     * @param s 源字串
     * @return 提取数字组成的新字串
     */
    public static String extractDigits(String s) {
        if (s == null) {
            return "";
        }

        char[] c = s.toCharArray();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < c.length; ++i) {
            if (Validator.isDigit(c[i])) {
                sb.append(c[i]);
            }
        }

        return sb.toString();
    }

    /**
     * 提取源字串中第一个子串（以指定分隔符为分隔）
     *
     * @param s         源字串
     * @param delimiter 指定分隔符
     * @return 提取出的第一个子串
     */
    public static String extractFirst(String s, String delimiter) {
        if (s == null) {
            return null;
        }

        String[] array = split(s, delimiter);

        if (array.length > 0) {
            return array[0];
        }

        return null;
    }

    /**
     * 提取源字串中最后一个子串（以指定分隔符为分隔）
     *
     * @param s         源字串
     * @param delimiter 指定分隔符
     * @return 提取出的最后一个子串
     */
    public static String extractLast(String s, String delimiter) {
        if (s == null) {
            return null;
        }

        String[] array = split(s, delimiter);

        if (array.length > 0) {
            return array[(array.length - 1)];
        }

        return null;
    }

    /**
     * 将源字串转换为全部小写
     *
     * @param s 源字串
     * @return 转换为小写后的新字串
     */
    public static String lowerCase(String s) {
        if (s == null) {
            return null;
        }

        return s.toLowerCase();
    }

    /**
     * 将字符串list用逗号拼接转换为字符串
     *
     * @param list 源字符串list
     * @return 拼接后的新字串
     */
    public static String merge(List list) {
        return merge(list, ",");
    }

    /**
     * 将字符串list用分隔符拼接转换为字符串
     *
     * @param list      源字符串list
     * @param delimiter 指定分隔符
     * @return 拼接后的新字串
     */
    public static String merge(List list, String delimiter) {
        return merge((String[]) list.toArray(new String[0]), delimiter);
    }

    /**
     * 将字符串数组用逗号拼接转换为字符串
     *
     * @param array 源字符串数组
     * @return 拼接后的新字串
     */
    public static String merge(String[] array) {
        return merge(array, ",");
    }

    /**
     * 将字符串数组用分隔符拼接转换为字符串
     *
     * @param array     源字符串数组
     * @param delimiter 指定分隔符
     * @return 拼接后的新字串
     */
    public static String merge(String[] array, String delimiter) {
        if (array == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < array.length; ++i) {
            sb.append(array[i].trim());

            if (i + 1 != array.length) {
                sb.append(delimiter);
            }
        }

        return sb.toString();
    }

    /**
     * 随机重组源字符串
     *
     * @param s 元字符串
     * @return 重组后的字符串
     */
    public static String randomize(String s) {
        return Randomizer.getInstance().randomize(s);
    }

    /**
     * 读取指定类装载路径下，指定资源文件内容
     *
     * @param classLoader 类装载器
     * @param name        资源文件
     * @return 资源文件内容（字符串形式）
     * @throws java.io.IOException
     */
    public static String read(ClassLoader classLoader, String name)
            throws IOException {
        return read(classLoader.getResourceAsStream(name));
    }

    /**
     * 读取给定输入流内容
     *
     * @param is 输入流
     * @return 输入流内容（字符串形式）
     * @throws java.io.IOException
     */
    public static String read(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        StringBuffer sb = new StringBuffer();

        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line).append('\n');
        }

        br.close();

        return sb.toString().trim();
    }

    /**
     * 将源字串中的指定字符，用新字符替代
     *
     * @param s      源字串
     * @param oldSub 指定要被替代的字符
     * @param newSub 新字符
     * @return 替换后的新串
     */
    public static String replace(String s, char oldSub, char newSub) {
        return replace(s, oldSub, Character.toString(newSub));
    }

    /**
     * 将源字串中的指定字符，用新字符串替代
     *
     * @param s      源字串
     * @param oldSub 指定要被替代的字符
     * @param newSub 新字符串
     * @return 替换后的新串
     */
    public static String replace(String s, char oldSub, String newSub) {
        if ((s == null) || (newSub == null)) {
            return null;
        }

        char[] c = s.toCharArray();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < c.length; ++i) {
            if (c[i] == oldSub) {
                sb.append(newSub);
            } else {
                sb.append(c[i]);
            }
        }

        return sb.toString();
    }

    /**
     * 将源字串中的指定字符串，用新字符串替代
     *
     * @param s      源字串
     * @param oldSub 指定要被替代的字符串
     * @param newSub 新字符串
     * @return 替换后的新串
     */
    public static String replace(String s, String oldSub, String newSub) {
        if ((s == null) || (oldSub == null) || (newSub == null)) {
            return null;
        }

        int y = s.indexOf(oldSub);

        if (y >= 0) {
            StringBuffer sb = new StringBuffer();

            int length = oldSub.length();
            int x = 0;

            while (x <= y) {
                sb.append(s.substring(x, y));
                sb.append(newSub);
                x = y + length;
                y = s.indexOf(oldSub, x);
            }

            sb.append(s.substring(x));

            return sb.toString();
        }

        return s;
    }

    /**
     * 将源字符串中一组指定字串，用一组新串替换
     *
     * @param s       源字符串
     * @param oldSubs 要被替换的一组旧子字串
     * @param newSubs 一组新字串
     * @return 替换后的新字符串
     */
    public static String replace(String s, String[] oldSubs, String[] newSubs) {
        if ((s == null) || (oldSubs == null) || (newSubs == null)) {
            return null;
        }

        if (oldSubs.length != newSubs.length) {
            return s;
        }

        for (int i = 0; i < oldSubs.length; ++i) {
            s = replace(s, oldSubs[i], newSubs[i]);
        }

        return s;
    }

    /**
     * 将源字串颠倒，形成新字符串
     *
     * @param s 源字符串
     * @return 形成的新符字串
     */
    public static String reverse(String s) {
        if (s == null) {
            return null;
        }

        char[] c = s.toCharArray();
        char[] reverse = new char[c.length];

        for (int i = 0; i < c.length; ++i) {
            reverse[i] = c[(c.length - i - 1)];
        }

        return new String(reverse);
    }

    /**
     * 取源字串前20个字符，剩余用“…”显示
     *
     * @param s 源字串
     * @return 处理后的新串
     */
    public static String shorten(String s) {
        return shorten(s, 20);
    }

    /**
     * 取源字串指定长度字符，剩余用“…”显示
     *
     * @param s      源字串
     * @param length 指定长度
     * @return 处理后的新串
     */
    public static String shorten(String s, int length) {
        return shorten(s, length, "...");
    }

    /**
     * 取源字串前20个字符，剩余用指定省略字符显示
     *
     * @param s 源字串
     * @return 处理后的新串
     * @params suffix 指定省略字符
     */
    public static String shorten(String s, String suffix) {
        return shorten(s, 20, suffix);
    }

    /**
     * 取源字串指定长度字符，剩余用指定省略字符显示
     *
     * @param s      源字串
     * @param length 指定长度
     * @return 处理后的新串
     * @params suffix 指定省略字符
     */
    public static String shorten(String s, int length, String suffix) {
        if ((s == null) || (suffix == null)) {
            return null;
        }

        if (s.length() > length) {
            for (int j = length; j >= 0; --j) {
                if (Character.isWhitespace(s.charAt(j))) {
                    length = j;

                    break;
                }
            }

            s = s.substring(0, length) + suffix;
        }

        return s;
    }

    /**
     * 将源字串以逗号为分隔符，取得分隔后的字符数组
     *
     * @param s 源字符串
     * @return 分割后的字符串数组
     */
    public static String[] split(String s) {
        return split(s, ",");
    }

    /**
     * 将源字串用指定分隔符分隔，取得分隔后的字符数组
     *
     * @param s         源字符串
     * @param separator 分割符号
     * @return 分割后的字符串数组
     */
    public static String[] split(String s, String separator) {
        if ((s == null) || (separator == null)) {
            return new String[0];
        }

        s = s.trim();

        if (!(s.endsWith(separator))) {
            s = s + separator;
        }

        if (s.equals(separator)) {
            return new String[0];
        }

        List nodeValues = new ArrayList();

        if ((separator.equals("\n")) || (separator.equals("\r"))) {
            try {
                BufferedReader br = new BufferedReader(new StringReader(s));

                String line;

                while ((line = br.readLine()) != null) {

                    nodeValues.add(line);
                }

                br.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            int offset = 0;
            int pos = s.indexOf(separator, offset);

            while (pos != -1) {
                nodeValues.add(s.substring(offset, pos));

                offset = pos + separator.length();
                pos = s.indexOf(separator, offset);
            }
        }

        return ((String[]) nodeValues.toArray(new String[0]));
    }

    /**
     * 将源字串用指定分隔符分隔，分隔后转换boolean型数组
     *
     * @param s         源字符串
     * @param delimiter 分隔符
     * @param x         默认值
     * @return 返回分割后的boolean类型数组
     */
    public static boolean[] split(String s, String delimiter, boolean x) {
        String[] array = split(s, delimiter);
        boolean[] newArray = new boolean[array.length];

        for (int i = 0; i < array.length; ++i) {
            boolean value = x;
            try {
                value = Boolean.valueOf(array[i]).booleanValue();
            } catch (Exception e) {
            }
            newArray[i] = value;
        }

        return newArray;
    }

    /**
     * 将源字串用指定分隔符分隔，分隔后转换double型数组
     *
     * @param s         源字符串
     * @param delimiter 分隔符
     * @param x         默认值
     * @return 返回分割后的boolean类型数组
     */
    public static double[] split(String s, String delimiter, double x) {
        String[] array = split(s, delimiter);
        double[] newArray = new double[array.length];

        for (int i = 0; i < array.length; ++i) {
            double value = x;
            try {
                value = Double.parseDouble(array[i]);
            } catch (Exception e) {
            }
            newArray[i] = value;
        }

        return newArray;
    }

    /**
     * 将源字串用指定分隔符分隔，分隔后转换float型数组
     *
     * @param s         源字符串
     * @param delimiter 分隔符
     * @param x         默认值
     * @return 返回分割后的boolean类型数组
     */
    public static float[] split(String s, String delimiter, float x) {
        String[] array = split(s, delimiter);
        float[] newArray = new float[array.length];

        for (int i = 0; i < array.length; ++i) {
            float value = x;
            try {
                value = Float.parseFloat(array[i]);
            } catch (Exception e) {
            }
            newArray[i] = value;
        }

        return newArray;
    }

    /**
     * 将源字串用指定分隔符分隔，分隔后转换int型数组
     *
     * @param s         源字符串
     * @param delimiter 分隔符
     * @param x         默认值
     * @return 返回分割后的boolean类型数组
     */
    public static int[] split(String s, String delimiter, int x) {
        String[] array = split(s, delimiter);
        int[] newArray = new int[array.length];

        for (int i = 0; i < array.length; ++i) {
            int value = x;
            try {
                value = Integer.parseInt(array[i]);
            } catch (Exception e) {
            }
            newArray[i] = value;
        }

        return newArray;
    }

    /**
     * 将源字串用指定分隔符分隔，分隔后转换long型数组
     *
     * @param s         源字符串
     * @param delimiter 分隔符
     * @param x         默认值
     * @return 返回分割后的boolean类型数组
     */
    public static long[] split(String s, String delimiter, long x) {
        String[] array = split(s, delimiter);
        long[] newArray = new long[array.length];

        for (int i = 0; i < array.length; ++i) {
            long value = x;
            try {
                value = Long.parseLong(array[i]);
            } catch (Exception e) {
            }
            newArray[i] = value;
        }

        return newArray;
    }


    /**
     * 将源字串用指定分隔符分隔，分隔后转换short型数组
     *
     * @param s         源字符串
     * @param delimiter 分隔符
     * @param x         默认值
     * @return 返回分割后的boolean类型数组
     */
    public static short[] split(String s, String delimiter, short x) {
        String[] array = split(s, delimiter);
        short[] newArray = new short[array.length];

        for (int i = 0; i < array.length; ++i) {
            short value = x;
            try {
                value = Short.parseShort(array[i]);
            } catch (Exception e) {
            }
            newArray[i] = value;
        }

        return newArray;
    }

    /**
     * 判断源字串是否以指定字符开头
     *
     * @param s     源字串
     * @param begin 指定字符
     * @return 若源字串以指定字符开头返回true, 否则返回false
     */
    public static boolean startsWith(String s, char begin) {
        return startsWith(s, new Character(begin).toString());
    }

    /**
     * 判断源字串是否以指定字符串开头
     *
     * @param s     源字串
     * @param start 指定字符
     * @return 若源字串以指定字符串开头返回true, 否则返回false
     */
    public static boolean startsWith(String s, String start) {
        if ((s == null) || (start == null)) {
            return false;
        }

        if (start.length() > s.length()) {
            return false;
        }

        String temp = s.substring(0, start.length());

        return (!(temp.equalsIgnoreCase(start)));
    }

    /**
     * 去除源字串开头的空格
     *
     * @param s 源字串
     * @return 处理后的新字串
     */
    public static String trimLeading(String s) {
        if(s!=null){
            for (int i = 0; i < s.length(); ++i) {
                if (!(Character.isWhitespace(s.charAt(i)))) {
                    return s.substring(i, s.length());
                }
            }
        }else if(s==null){
            return null;
        }
        return "";
    }

    /**
     * 去除源字串尾部的空格
     *
     * @param s 源字串
     * @return 处理后的新字串
     */
    public static String trimTrailing(String s) {
        if(s!=null){
            for (int i = s.length() - 1; i >= 0; --i) {
                if (!(Character.isWhitespace(s.charAt(i)))) {
                    return s.substring(0, i + 1);
                }
            }
        }else if(s==null){
            return null;
        }
        return "";
    }

    /**
     * 去除源字串中全部的空格
     *
     * @param s 源字串
     * @return 处理后的新字串
     */
    public static String trimWhole(String s) {
        if(s==null)return "";
        s = s.trim();
        char[] exprArray = new char[s.length()];

        for (int i = s.length() - 1; i >= 0; --i) {
            exprArray[i] = s.charAt(i);
        }
        for (int i = s.length() - 1; i >= 0; --i) {
            if (exprArray[i] == ' ') {
                int j = i;
                while (j < exprArray.length - 1) {
                    exprArray[j] = exprArray[(j + 1)];
                    ++j;
                }
                exprArray[(exprArray.length - 1)] = ' ';
            }
        }

        StringBuffer exprStrBuf = new StringBuffer("");
        for (int i = 0; i < exprArray.length; ++i) {
            exprStrBuf.insert(i, exprArray[i]);
        }

        s = exprStrBuf.toString().trim();

        return s;
    }

    /**
     * 将源字串全部转换为大写
     *
     * @param s 源字串
     * @return 处理后的新字串
     */
    public static String upperCase(String s) {
        if (s == null) {
            return null;
        }

        return s.toUpperCase();
    }

    /**
     * 将源字串文本按指定长度80个字符折行（每80个字符插入一个回车符）
     *
     * @param text 源字串
     * @return 处理后的新字串
     */
    public static String wrap(String text) {
        return wrap(text, 80, "\n");
    }

    /**
     * 将源字串文本按指定长度的字符插入行分隔符
     *
     * @param text          源字串
     * @param length        长度
     * @param lineSeparator 分隔符
     * @return 处理后的新字串
     */
    public static String wrap(String text, int length, String lineSeparator) {
        if (text == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new StringReader(text));

            String s = "";

            while ((s = br.readLine()) != null) {
                if (s.length() == 0) {
                    sb.append(lineSeparator);
                }

                String[] tokens = s.split(" ");
                boolean firstWord = true;
                int curLineLength = 0;

                for (int i = 0; i < tokens.length; ++i) {
                    if (!(firstWord)) {
                        sb.append(" ");
                        ++curLineLength;
                    }

                    if (firstWord) {
                        sb.append(lineSeparator);
                    }

                    sb.append(tokens[i]);

                    curLineLength += tokens[i].length();

                    if (curLineLength >= length) {
                        firstWord = true;
                        curLineLength = 0;
                    } else {
                        firstWord = false;
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * 将ISO8859-1编码的字符串转换为指定编码的字符串
     *
     * @param s           字符串
     * @param charsetName 编码
     * @return 转换后的编码
     * @throws java.io.UnsupportedEncodingException
     */
    public static String translateFromISO(String s, String charsetName) throws UnsupportedEncodingException {
        if (s != null) {
            return new String(s.getBytes("ISO8859-1"), charsetName);
        }
        return null;
    }

    /**
     * 转换字符串的编码
     *
     * @param s                 字符串
     * @param originCharsetName 字符串现有编码
     * @param charsetName       新的编码
     * @return 返回新的编码的字符串
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
     * 校验字符串是否有效的
     *
     * @param s
     * @return
     */
    public static String validate(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

    /**
     * 判断字符串是否为合法字符串
     *
     * @param s           需校验的字符串
     * @param invalidChar 无效字符串
     * @return true有效字符串，false为无效字符串
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
     * Check whether the given CharSequence has actual text.
     * More specifically, returns <codebuilder>true</codebuilder> if the string not <codebuilder>null</codebuilder>,
     * its length is greater than 0, and it contains at least one non-whitespace character.
     * <p><pre>
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     *
     * @param str the CharSequence to check (may be <codebuilder>null</codebuilder>)
     * @return <codebuilder>true</codebuilder> if the CharSequence is not <codebuilder>null</codebuilder>,
     *         its length is greater than 0, and it does not contain whitespace only
     * @see Character#isWhitespace
     */
    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check that the given CharSequence is neither <codebuilder>null</codebuilder> nor of length 0.
     * Note: Will return <codebuilder>true</codebuilder> for a CharSequence that purely consists of whitespace.
     * <p><pre>
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     *
     * @param str the CharSequence to check (may be <codebuilder>null</codebuilder>)
     * @return <codebuilder>true</codebuilder> if the CharSequence is not null and has length
     * @see # hasText(String)
     */
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * 清除 ‘ ”
     * @param input
     * @return
     */
    public static String trimQuotes(String input) {
        if (!hasText(input)) return input;

        String result = input.trim();
        int len = result.length();

        char firstChar = result.charAt(0);
        char lastChar = result.charAt(len - 1);

        if ((firstChar == '"' && lastChar == '"') ||
                (firstChar == '\'' && lastChar == '\'') ||
                (firstChar == '`' && lastChar == '`') /* workaround the idiotic MySQL quoting */
                ) {
            return result.substring(1, len - 1);
        }
        return input;
    }

    /**
     * decode方法
     * @param value
     * @return
     * @throws Exception
     */
    public static String decode(String value) throws Exception {
        try {
            if (value == null)
                return null;
            return new String(value.getBytes("ISO-8859-1"),STANDARD_CHARSET);
        } catch (Exception ex) {
            return value;
        }
    }

    /**
     * 将String转换为boolean类型
     * @param aString
     * @return
     */
    public static boolean stringToBool(String aString)
    {
        if (aString == null) return false;
        return ("true".equalsIgnoreCase(aString) || "1".equals(aString) || "y".equalsIgnoreCase(aString) || "yes".equalsIgnoreCase(aString) || "on".equalsIgnoreCase(aString) );
    }

    /**
     * 生成序列方法
     *
     * @param entityId
     * @param parentSeq
     * @return
     */
    public static String generatorSeqNo(String entityId, String parentSeq) {
        String seqNo = "";
        if (Validator.isEmpty(parentSeq)) {
            seqNo = "." + entityId + ".";
        } else {
            seqNo = parentSeq + entityId + ".";
        }
        return seqNo;
    }

    /**
     * ************************************************************************
     * 将s_name 变为 sName;
     *
     * @param colName
     * @return
     */
    public static String convertPropertyName(String colName) {

        if(colName!=null){
            String[] str = colName.toLowerCase().split("_");

            StringBuffer beanS = new StringBuffer(str[0]);

            for (int i = 1; i < str.length; i++) {
                if ("".equals(str) || str == null) {
                    continue;
                }
                String info = str[i];
                try{
                	beanS.append(info.substring(0, 1).toUpperCase()+ info.substring(1));
                }catch (Exception e) {
                	throw new FrameworkRuntimeException("字段名称不符合规范["+colName+"]");
				}
            }
            return beanS.toString().substring(0,1).toLowerCase()+beanS.substring(1,beanS.length());
        }
        return "";
    }
}