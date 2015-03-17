package com.innofi.framework.utils.comparator;

import java.util.Comparator;

/**
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 * 不区分大小写的字符串比较器
 * 可以处理null值(不能处理String.CASE_INSENSITIVE_ORDER)
 */
public class CaseInsensitiveComparator implements Comparator<String> {

    public static final CaseInsensitiveComparator INSTANCE = new CaseInsensitiveComparator();

    /**
     * 比较两个字符串
     * null值排序在非null值之后
     * i.e. compare(null, "something") returns -1
     * and compare("something", null) returns 1
     *
     * @param value1 比较的第一个字符串 可为空
     * @param value2 比较的第二个字符串 可为空
     * @return 如果两个值均为null 返回0
     */
    public int compare(String value1, String value2) {
        if (value1 == null && value2 == null) return 0;
        if (value1 == null) return -1;
        if (value2 == null) return 1;
        return value1.compareToIgnoreCase(value2);
    }

}


