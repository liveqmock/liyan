package com.innofi.framework.utils.console;

import java.text.MessageFormat;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 12-7-9
 * @found time: 下午7:55
 * <p/>
 * 控制台打印工具类
 */
public class ConsoleUtil {

    /**
     * 将信息输出到控制台，格式为>>>>>[innofi-framework]info: [{0}]<<<<<
     * @param o 输出的信息调用对象的.toString()方法
     */
    public static void info(Object o ) {
        System.out.println(MessageFormat.format(">>>>>[innofi-framework]info: [{0}]<<<<<", o.toString()));
    }

    /**
     * 将信息输出到控制台，格式为>>>>>[innofi-framework]warn: [{0}]<<<<<
     * @param o 输出的信息调用对象的.toString()方法
     */
    public static void warn(Object o){
        System.out.println(MessageFormat.format(">>>>>[innofi-framework]warn: [{0}]<<<<<", o.toString()));
    }

    /**
     * 将信息输出到控制台，格式为>>>>>[innofi-framework]error: [{0}]<<<<<
     * @param o 输出的信息调用对象的.toString()方法
     */
    public static void error(Object o ) {
        System.out.println(MessageFormat.format(">>>>>[innofi-framework]error: [{0}]<<<<<", o.toString()));
    }

    /**
     * 将信息输出到控制台，格式为>>>>>[innofi-framework]test: [{0}]<<<<<
     * @param o 输出的信息调用对象的.toString()方法
     */
    public static void test(Object o ) {
        System.out.println(MessageFormat.format(">>>>>[innofi-framework]test: [{0}]<<<<<", o.toString()));
    }

}
