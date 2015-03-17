package com.innofi.component.fileloader;

import junit.framework.TestCase;

import java.io.IOException;

/**
 * Copyright (c) 2011 北京正信嘉华管理顾问有限公司
 * all rights reserved.
 * author: liumy
 * found date: 12-3-2
 * found time: 下午5:02
 * 类描述
 */
public class TestFileLoader extends TestCase{

    public void testFileSystemLoader() throws IOException {
        FileLoader loader = FileLoaderFactory.createResourceFileLoader();
        loader.setFile("c:/工作目录/a.vsd");
        loader.getInputStream();
    }



}
