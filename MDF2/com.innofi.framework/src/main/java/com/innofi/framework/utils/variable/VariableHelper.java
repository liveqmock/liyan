package com.innofi.framework.utils.variable;

import com.innofi.framework.utils.date.DateUtil;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

/**
 * 变量工具类
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2007-9-14
 * found time: 18:05:49
 */
public final class VariableHelper {

    /**
     * 将byte简单类型转换为对象类型
     *
     * @param value 数值
     * @return 类型对应的对象
     */
    public static Object toObject(byte value) {
        return new Byte(value);
    }

    /**
     * 将short简单类型转换为对象类型
     *
     * @param value 数值
     * @return 类型对应的对象
     */
    public static Object toObject(short value) {
        return new Short(value);
    }

    /**
     * 将int简单类型转换为对象类型
     *
     * @param value 数值
     * @return 类型对应的对象
     */
    public static Object toObject(int value) {
        return new Integer(value);
    }

    /**
     * 将long简单类型转换为对象类型
     *
     * @param value 数值
     * @return 类型对应的对象
     */
    public static Object toObject(long value) {
        return new Long(value);
    }

    /**
     * 将float简单类型转换为对象类型
     *
     * @param value 数值
     * @return 类型对应的对象
     */
    public static Object toObject(float value) {
        return new Float(value);
    }

    /**
     * 将double简单类型转换为对象类型
     *
     * @param value 数值
     * @return 类型对应的对象
     */
    public static Object toObject(double value) {
        return new Double(value);
    }

    /**
     * 将boolean简单类型转换为对象类型
     *
     * @param value 数值
     * @return 类型对应的对象
     */
    public static Object toObject(boolean value) {
        return new Boolean(value);
    }

    /**
     * 将Date简单类型转换为对象类型
     *
     * @param value 数值
     * @return 类型对应的对象类型
     */
    public static Object toObject(Date value) {
        return value;
    }

    /**
     * 将BigDecimal简单类型转换为对象类型
     *
     * @param value 数值
     * @return 类型对应的对象类型
     */
    public static Object toObject(BigDecimal value) {
        return value;
    }

    /**
     * 将String类型转换为对象类型
     *
     * @param value 数值
     * @return 类型对应的对象类型
     */
    public static Object toObject(String value) {
        return value;
    }

    /**
     * 将Object对象解析为String类型的值
     *
     * @param o 被解析的对象
     * @return String类型的值 ,o为null返回null，o为Date类型将o对应的毫秒转换为String类型返回，其他类型调用toString方法
     */
    public static String parseString(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Date) {
            return String.valueOf(((Date) o).getTime());
        }

        return o.toString();
    }

    /**
     * 将Object对象解析为byte类型的值
     *
     * @param o 被解析的对象
     * @return byte类型的值, o为null、false、""返回0,true返回1,数字返回其对应的值
     */
    public static byte parseByte(Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof Number) {
            return ((Number) o).byteValue();
        }
        if (o instanceof Boolean) {
            return (byte) ((((Boolean) o).booleanValue()) ? 1 : 0);
        }

        String s = o.toString();
        if (s.equals("")) {
            return 0;
        }

        return Byte.parseByte(s);
    }

    /**
     * 将Object对象解析为short类型的值
     *
     * @param o 被解析的对象
     * @return short类型的值, o为null、false、""返回0,true返回1,数字返回其对应的值
     */
    public static short parseShort(Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof Number) {
            return ((Number) o).shortValue();
        }
        if (o instanceof Boolean) {
            return (short) ((((Boolean) o).booleanValue()) ? 1 : 0);
        }

        String s = o.toString();
        if (s.equals("")) {
            return 0;
        }

        return Short.parseShort(s);
    }

    /**
     * 将Object对象解析为int类型的值
     *
     * @param o 被解析的对象
     * @return int类型的值, o为null、false、""返回0,true返回1,数字返回其对应的值
     */
    public static int parseInt(Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof Number) {
            return ((Number) o).intValue();
        }
        if (o instanceof Boolean) {
            return ((((Boolean) o).booleanValue()) ? 1 : 0);
        }

        String s = o.toString();
        if (s.equals("")) {
            return 0;
        }

        return Integer.parseInt(s);
    }

    /**
     * 将Object对象解析为long类型的值
     *
     * @param o 被解析的对象
     * @return long类型的值, o为null、false、""返回0L,true返回1L,数字返回其对应的值
     */
    public static long parseLong(Object o) {
        if (o == null) {
            return 0L;
        }
        if (o instanceof Number) {
            return ((Number) o).longValue();
        }
        if (o instanceof Boolean) {
            return ((((Boolean) o).booleanValue()) ? 1L : 0L);
        }
        if (o instanceof Date) {
            return ((Date) o).getTime();
        }

        String s = o.toString();
        if (s.equals("")) {
            return 0L;
        }

        return Long.parseLong(s);
    }

    /**
     * 将Object对象解析为float类型的值
     *
     * @param o 被解析的对象
     * @return float类型的值, o为null、false、""返回0.0F,true返回1.0F,数字返回其对应的值
     */
    public static float parseFloat(Object o) {
        if (o == null) {
            return 0.0F;
        }
        if (o instanceof Number) {
            return ((Number) o).floatValue();
        }
        if (o instanceof Boolean) {
            return ((((Boolean) o).booleanValue()) ? 1.0F : 0.0F);
        }

        String s = o.toString();
        if (s.equals("")) {
            return 0.0F;
        }

        return Float.parseFloat(s);
    }

    /**
     * 将Object对象解析为double类型的值
     *
     * @param o 被解析的对象
     * @return double类型的值, o为null、false、""返回0.0D,true返回1.0D,数字返回其对应的值
     */
    public static double parseDouble(Object o) {
        if (o == null) {
            return 0.0D;
        }
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        if (o instanceof Boolean) {
            return ((((Boolean) o).booleanValue()) ? 1.0D : 0.0D);
        }

        String s = o.toString();
        if (s.equals("")) {
            return 0.0D;
        }

        return Double.parseDouble(s);
    }

    /**
     * 将Object对象解析为BigDecimal类型的值
     *
     * @param o 被解析的对象
     * @return BigDecimal类型的值, o为null、false、""返回0,true返回1,数字返回其对应的值
     */
    public static BigDecimal parseBigDecimal(Object o) {
        if (o == null) {
            return BigDecimal.valueOf(0L);
        }
        if (o instanceof BigDecimal) {
            return ((BigDecimal) o);
        }
        if (o instanceof Number) {
            return BigDecimal.valueOf(((Number) o).longValue());
        }
        if (o instanceof Boolean) {
            return BigDecimal.valueOf((((Boolean) o).booleanValue()) ? 1L : 0L);
        }

        String s = o.toString();
        if (s.equals("")) {
            return BigDecimal.valueOf(0L);
        }

        return new BigDecimal(s);
    }

    /**
     * 将String类型的对象解析为boolean类型的值
     *
     * @param s Stirng类型的被解析对象
     * @return true、1、t、T、y、Y返回true，否则false
     */
    public static boolean parseBoolean(String s) {
        if (s == null) {
            return false;
        }
        return (s.equalsIgnoreCase("true") || (s.equals("1")) || s.equalsIgnoreCase("T") || s.equalsIgnoreCase("Y"));
    }

    /**
     * 将Object类型的对象解析为boolean类型的值
     *
     * @param o Object类型的对象
     * @return o为null返回false ，o为String类型的值：true、1、-1、t、T、y、Y返回true，否则false
     */
    public static boolean parseBoolean(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
        }

        return parseBoolean(o.toString());
    }

    /**
     * 判断给定的字符串是否为数字
     *
     * @param s 字符串
     * @return 是返回true，不是返回false
     */
    private static boolean isNumeric(String s) {
        int length = s.length();
        for (int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            if ((((c < '0') || (c > '9'))) && (c != '.') && ((
                    (i != 0) || (c != '-')))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 将制定对象转换为日期对象
     *
     * @param o 目标对象
     * @return 日期对象
     * @throws java.text.ParseException
     */
    public static Date parseDate(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Date) {
            return ((Date) o);
        }
        if (o instanceof Number) {
            return new Date(((Number) o).longValue());
        }

        String s = String.valueOf(o);
        if (Validator.isNotNull(s)) {
            if (isNumeric(s)) {
                long time = Long.parseLong(s);
                return new Date(time);
            }

            int len = s.length();
            try {
                if (len < 19) {
                    if (s.indexOf(":") > 0) {
                        return DateUtil.dateTimeFormat.parse(s);
                    }

                    return DateUtil.dateForamt1.parse(s);
                }

                return DateUtil.dateTimeFormat.parse(s);
            } catch (ParseException ex) {
                ex.printStackTrace();
                return null;
            }

        }
        return null;
    }

    /**
     * 根据数据类型获得变形后的对象方法
     *
     * @param dataType 数据类型
     * @param value    数据对象
     * @return 转换后的对象
     */
    public static Object translate(int dataType, Object value) {
        if ((value == null) || ((value instanceof String) && (((String) value).length() == 0))) {
            if (dataType == DataType.STRING) {
                return value;
            }
            return null;
        }

        switch (dataType) {
            case DataType.STRING:
                return parseString(value);
            case DataType.BOOLEAN:
                return toObject(parseBoolean(value));
            case DataType.DATE:
            case DataType.TIME:
            case DataType.DATETIME:
                return parseDate(value);
            case DataType.INTEGER:
                return toObject(parseInt(value));
            case DataType.DOUBLE:
                return toObject(parseDouble(value));
            case DataType.LONG:
                return toObject(parseLong(value));
            case DataType.FLOAT:
                return toObject(parseFloat(value));
            case DataType.BIGDECIMAL:
                return parseBigDecimal(value);
            case DataType.BYTE:
                return toObject(parseByte(value));
            case DataType.SHORT:
                return toObject(parseShort(value));
        }
        return value;
    }


    public static Object translate(String className , Object value) {
        if(className.equals("java.lang.Integer")){
            return toObject(parseInt(StringUtil.trimWhole(parseString(value))));
        }else if(className.equals("java.lang.Boolean")){
            return toObject(parseBoolean(StringUtil.trimWhole(parseString(value))));
        }else if(className.equals("java.lang.Double")){
            return toObject(parseDouble(StringUtil.trimWhole(parseString(value))));
        }else if(className.equals("java.lang.Long")){
            return toObject(parseLong(StringUtil.trimWhole(parseString(value))));
        }else if(className.equals("java.math.BigDecimal")){
            return parseBigDecimal(StringUtil.trimWhole(parseString(value)));
        }else if(className.equals("java.lang.Byte")){
            return toObject(parseByte(StringUtil.trimWhole(parseString(value))));
        }else if(className.equals("java.lang.Short")){
            return toObject(parseShort(StringUtil.trimWhole(parseString(value))));
        }else if(className.equals("java.util.Date")){
            return parseDate(StringUtil.trimWhole(parseString(value)));
        }

        return parseString(value);
    }





}