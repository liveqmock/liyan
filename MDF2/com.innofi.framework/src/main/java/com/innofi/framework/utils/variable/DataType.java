package com.innofi.framework.utils.variable;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * 数据类型对象类
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public final class DataType {

    /**
     * 未知数据类型名称""
     */
    public static final String UNKNOWN_NAME = "";

    /**
     * 未知数据类型常量值0
     */
    public static final int UNKNOWN = 0;

    /**
     * String数据类型常量名称String
     */
    public static final String STRING_NAME = "String";

    /**
     * String数据类型String类型常量值1
     */
    public static final int STRING = 1;

    /**
     * Byte数据类型常量名称Byte
     */
    public static final String BYTE_NAME = "Byte";

    /**
     * Byte数据类型Byte类型常量值2
     */
    public static final int BYTE = 2;

    /**
     * Short数据类型常量名称Short
     */
    public static final String SHORT_NAME = "Short";

    /**
     * Short数据类型Short类型常量值3
     */
    public static final int SHORT = 3;

    /**
     * Integer数据类型常量名称Integer
     */
    public static final String INT_NAME = "Integer";

    /**
     * Integer数据类型Integer类型常量值4
     */
    public static final int INTEGER = 4;

    /**
     * Long数据类型常量名称Long
     */
    public static final String LONG_NAME = "Long";

    /**
     * Long数据类型Long类型常量值5
     */
    public static final int LONG = 5;

    /**
     * Float数据类型常量名称Float
     */
    public static final String FLOAT_NAME = "Float";

    /**
     * Float数据类型Float类型常量值6
     */
    public static final int FLOAT = 6;

    /**
     * Double数据类型常量名称
     */
    public static final String DOUBLE_NAME = "Double";

    /**
     * Double数据类型常量7
     */
    public static final int DOUBLE = 7;

    /**
     * Bigdecimal数据类型常量名称Bigdecimal
     */
    public static final String BIGDECIMAL_NAME = "Bigdecimal";

    /**
     * Bigdecimal数据类型值8
     */
    public static final int BIGDECIMAL = 8;

    /**
     * Boolean数据类型常量名称Boolean
     */
    public static final String BOOLEAN_NAME = "Boolean";

    /**
     * Boolean数据类型Boolean类型常量值9
     */
    public static final int BOOLEAN = 9;

    /**
     * Date数据类型常量名称Date
     */
    public static final String DATE_NAME = "Date";

    /**
     * Date数据类型Date类型常量值10
     */
    public static final int DATE = 10;

    /**
     * Time数据类型常量名称Time
     */
    public static final String TIME_NAME = "Time";

    /**
     * Time数据类型Time类型常量值11
     */
    public static final int TIME = 11;

    /**
     * Datetime数据类型常量名称Datetime
     */
    public static final String DATETIME_NAME = "Datetime";

    /**
     * Datetime数据类型Datetime类型常量值12
     */
    public static final int DATETIME = 12;

    /**
     * Binary数据类型常量名称Binary
     */
    public static final String BINARY_NAME = "binary";

    /**
     * Binary数据类型Binary类型常量值20
     */
    public static final int BINARY = 20;

    /**
     * 判断指定的数据类型int类型常量值所表示的是否为Number类型
     *
     * @param dataType 数据类型int类型常量值
     * @return number类型返回true 非number类型返回false
     */
    public static boolean isNumberType(int dataType) {
        return ((2 <= dataType) && (dataType <= 8));
    }

    /**
     * 判断指定的数据类型int类型常量值所表示的是否为Date类型
     *
     * @param dataType 数据类型int类型常量值
     * @return date类型返回true 非date类型返回false
     */
    public static boolean isDateType(int dataType) {
        return ((10 <= dataType) && (dataType <= 12));
    }

    /**
     * 判断指定的class是否为基础数据类型
     *
     * @param clazz 指定类的class
     * @return 基础数据类型返回false 非基础数据类型true
     */
    public static boolean isBaseDataType(Class clazz) {
        return ((!(clazz.isPrimitive())) && (!(clazz.equals(String.class))) && (!(Number.class.isAssignableFrom(clazz))) && (!(Boolean.class.isAssignableFrom(clazz))) && (!(java.util.Date.class.isAssignableFrom(clazz))));
    }

    /**
     * 根据数据类型的class获得数据类型int常量值
     *
     * @param clazz 数据类型class
     * @return 数据类型int常量值
     */
    public static int parse(Class clazz) {
        if (clazz.equals(String.class)) {
            return STRING;
        }
        if (clazz.equals(Integer.TYPE)) {
            return INTEGER;
        }
        if (clazz.equals(Boolean.TYPE)) {
            return BOOLEAN;
        }
        if (clazz.equals(Float.TYPE)) {
            return FLOAT;
        }
        if (clazz.equals(java.util.Date.class)) {
            return DATE;
        }
        if (clazz.equals(java.sql.Date.class)) {
            return DATE;
        }
        if (clazz.equals(Time.class)) {
            return TIME;
        }
        if (clazz.equals(Timestamp.class)) {
            return DATETIME;
        }
        if (clazz.equals(Long.TYPE)) {
            return LONG;
        }
        if (clazz.equals(Double.TYPE)) {
            return DOUBLE;
        }
        if (clazz.equals(Byte.TYPE)) {
            return BYTE;
        }
        if (clazz.equals(Short.TYPE)) {
            return SHORT;
        }
        if (clazz.equals(BigDecimal.class)) {
            return BIGDECIMAL;
        }
        return UNKNOWN;
    }

    /**
     * 根据数据类型名称获得数据类型int常量值
     *
     * @param name 数据类型名称
     * @return 数据类型名称所表示的int常量值
     */
    public static int nameToType(String name) {
        if ("String".equals(name)) {
            return STRING;
        }
        if ("Boolean".equals(name)) {
            return BOOLEAN;
        }
        if ("Integer".equals(name)) {
            return INTEGER;
        }
        if ("Float".equals(name)) {
            return FLOAT;
        }
        if ("Date".equals(name)) {
            return DATE;
        }
        if ("Time".equals(name)) {
            return TIME;
        }
        if ("Datetime".equals(name)) {
            return DATETIME;
        }
        if ("Double".equals(name)) {
            return DOUBLE;
        }
        if ("Long".equals(name)) {
            return LONG;
        }
        if ("Byte".equals(name)) {
            return BYTE;
        }
        if ("Short".equals(name)) {
            return SHORT;
        }
        if ("BigDecimal".equals(name)) {
            return BIGDECIMAL;
        }
        if ("Binary".equals(name)) {
            return BINARY;
        }
        return UNKNOWN;
    }

    /**
     * 根据数据类型int常量值获得数据类型常量名称
     *
     * @param type 数据类型int类型常量值
     * @return 常量值所表示数据类型名称
     */
    public static String typeToName(int type) {
        switch (type) {
            case STRING:
                return "String";
            case BOOLEAN:
                return "Boolean";
            case INTEGER:
                return "Integer";
            case FLOAT:
                return "Float";
            case DATE:
                return "Date";
            case TIME:
                return "Time";
            case DATETIME:
                return "Datetime";
            case DOUBLE:
                return "Double";
            case LONG:
                return "Long";
            case BYTE:
                return "Byte";
            case SHORT:
                return "Short";
            case BIGDECIMAL:
                return "Bigdecimal";
            case BINARY:
                return "Binary";
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
        }
        return "";
    }

    /**
     * 根据数据类型int常量值返回对应的数据类型的Class
     *
     * @param type 数据类型int常量值
     * @return 数据类型对应的Class
     */
    public static Class typeToClass(int type) {
        switch (type) {
            case STRING:
                return String.class;
            case BOOLEAN:
                return Boolean.class;
            case INTEGER:
                return Integer.class;
            case FLOAT:
                return Float.class;
            case DATE:
            case TIME:
            case DATETIME:
                return java.util.Date.class;
            case DOUBLE:
                return Double.class;
            case LONG:
                return Long.class;
            case BYTE:
                return Byte.class;
            case SHORT:
                return Short.class;
            case BIGDECIMAL:
                return BigDecimal.class;
            case 20:
                return null;
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
        }
        return null;
    }

}



