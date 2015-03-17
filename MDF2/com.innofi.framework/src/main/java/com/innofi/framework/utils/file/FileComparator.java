package com.innofi.framework.utils.file;

import java.io.File;
import java.util.Comparator;

/**
 * 文件比较对象
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public class FileComparator implements Comparator {
    public int compare(Object obj1, Object obj2) {
        File file1 = (File) obj1;
        File file2 = (File) obj2;
        return file1.getName().compareTo(file2.getName());
    }
}