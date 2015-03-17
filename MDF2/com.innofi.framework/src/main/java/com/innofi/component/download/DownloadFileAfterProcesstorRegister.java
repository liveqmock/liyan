package com.innofi.component.download;


import com.innofi.framework.spring.context.ContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2010 正信嘉华管理顾问有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-11-21
 * found time: 11:38:54
 */
public class DownloadFileAfterProcesstorRegister {

    private static String defaultProcesstor = "com.innofi.component.downloadmanagement.DefaultDownloadFileAfterProcesstor";

    private static List<String> processtorList = new ArrayList<String>();

    private static void init (){
        String[] processtors = ContextHolder.getSystemProperties().getArray("download.afterprocesstor");
        for (int i = processtors.length - 1; i >= 0; i--) {
            String processtor = processtors[i];
            if (!defaultProcesstor.equals(processtor)) {
                register(processtor);
            }
        }
    }

    /**
     * 注册后续处理实现
     *
     * @param processtorClassName
     */
    public static void register(String processtorClassName) {
        processtorList.add(processtorClassName);
    }

    /**
     * 取消后续处理实现
     *
     * @param processtorClassName
     */
    public static void unRegistor(String processtorClassName) {
        processtorList.remove(processtorClassName);
    }

    public static List<String> getProcesstorList() {
        init();
        if (processtorList.size() == 0) {
            processtorList.add(defaultProcesstor);
        }
        return processtorList;
    }

}
