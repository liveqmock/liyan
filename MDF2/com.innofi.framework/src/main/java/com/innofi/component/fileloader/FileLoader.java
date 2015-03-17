package com.innofi.component.fileloader;

import com.innofi.framework.utils.string.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 文件加载器基类
 * Copyright (c) 2010 正信嘉华管理顾问有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public abstract class FileLoader {

    protected String rootPath;  //文件加载器根路径
    private String filePath;    //文件所在路径

    private FileLoader() {
    }

    public FileLoader(String rootPath) {
        if (StringUtil.hasText(rootPath)) {
            rootPath = "";
        } else if (!(rootPath.endsWith("/"))) {
            rootPath = rootPath + '/';
        }
        this.rootPath = rootPath;
    }

    /**
     * 设置要打开的文件
     *
     * @param filePath
     */
    protected abstract void doSetFile(String filePath);

    public void setFile(String filePath) {
        this.filePath = filePath;
        doSetFile(filePath);
    }

    /**
     * 判断指定路径的文件是否存在
     *
     * @return true存在，false不存在
     */
    public abstract boolean exists();

    /**
     * 获取文件的最后修改日期
     *
     * @return 文件的最后修改日期
     * @throws java.io.IOException
     */
    public abstract long getLastModified()
            throws IOException;

    /**
     * 获取文件的输入流
     *
     * @return 文件的输入流
     * @throws java.io.IOException
     */
    public abstract InputStream getInputStream()
            throws IOException;

    /**
     * 获取文件的输出流
     *
     * @return 文件输出流
     * @throws java.io.IOException
     */
    public abstract OutputStream getOutputStream() throws IOException;

    /**
     * 取得根路径
     *
     * @return
     */
    public String getRootPath() {
        return this.rootPath;
    }

    /**
     * 文件位置的描述
     *
     * @return
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * 取得文件的实际位置
     *
     * @return
     */
    public abstract String getRealPath();

    /**
     * 取得文件的URL连接器
     *
     * @return URL
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public abstract URL getURL() throws IOException;

    /**
     * 获得文件名称
     *
     * @return
     */
    public abstract String getFileName() throws IOException;

    /**
     * 获得文件内容的字节码数组
     *
     * @return 字节码数组
     */
    public abstract byte[] getFileContentOfBytes() throws IOException;

    /**
     * 获得文件对象
     *
     * @return
     */
    public abstract File getFile() throws IOException;
}