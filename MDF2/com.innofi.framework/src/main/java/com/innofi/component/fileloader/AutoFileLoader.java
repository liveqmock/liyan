package com.innofi.component.fileloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 默认文件加载器
 * Copyright (c) 2010 正信嘉华管理顾问有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public class AutoFileLoader extends FileLoader {

    public static final String CLASSPATH_PREFIX = "classpath:"; //类路径前缀
    public static final String FILE_PREFIX1 = "file:";//文件系统前缀1
    public static final String FILE_PREFIX2 = "file://";//文件系统前缀2
    public static final String DATABASE_PREFIX = "database:";//数据库前缀
    private FileLoader fileLoader;  //文件加载器

    public AutoFileLoader() {
        super(null);
    }

    public AutoFileLoader(String root) {
        super(root);
    }

    /**
     * 获得资源文件加载器
     *
     * @return 资源文件加载器
     */
    private FileLoader getResourceFileLoader() {
        if (this.fileLoader == null) {
            this.fileLoader = new ResourceFileLoader();
        }
        return this.fileLoader;
    }

    /**
     * 获得绝对路径文件加载器
     *
     * @return 绝对路径文件加载器
     */
    private FileLoader getPathFileLoader() {
        if (this.fileLoader == null) {
            this.fileLoader = new PathFileLoader();
        }
        return this.fileLoader;
    }

    private FileLoader getDatabaseFileLoader(){
        if(this.fileLoader == null){
            this.fileLoader = new DataBaseFileLoader();
        }
        return this.fileLoader;
    }

    /**
     * 设置要打开的文件
     *
     * @param filePath
     */
    protected void doSetFile(String filePath) {
        String realFilePath;
        if (this.rootPath != null) {
            filePath = this.rootPath + filePath;
        }
        if (filePath.startsWith(CLASSPATH_PREFIX)) {
            this.fileLoader = getResourceFileLoader();
            realFilePath = filePath.substring(CLASSPATH_PREFIX.length());
        } else if (filePath.startsWith(FILE_PREFIX2)) {
            this.fileLoader = getPathFileLoader();
            realFilePath = filePath.substring(FILE_PREFIX2.length());
        } else if (filePath.startsWith(FILE_PREFIX1)) {
            this.fileLoader = getPathFileLoader();
            realFilePath = filePath.substring(FILE_PREFIX1.length());
        } else if (filePath.startsWith(DATABASE_PREFIX)) {
            this.fileLoader = getDatabaseFileLoader();
            realFilePath = filePath.substring(DATABASE_PREFIX.length());
        } else {
            this.fileLoader = getPathFileLoader();
            realFilePath = filePath;
        }
        this.fileLoader.setFile(realFilePath);
    }

    /**
     * 判断指定路径的文件是否存在
     *
     * @return true存在，false不存在
     */
    public boolean exists() {
        return this.fileLoader.exists();
    }

    /**
     * 获取文件的最后修改日期
     *
     * @return 文件的最后修改日期
     * @throws java.io.IOException
     */
    public long getLastModified() throws IOException {
        return this.fileLoader.getLastModified();
    }

    /**
     * 获取文件的输入流
     *
     * @return 文件的输入流
     * @throws java.io.IOException
     */
    public InputStream getInputStream() throws IOException {
        return this.fileLoader.getInputStream();
    }

    /**
     * 获取文件的输出流
     *
     * @return 文件输出流
     * @throws java.io.IOException
     */
    public OutputStream getOutputStream() throws IOException {
        return this.fileLoader.getOutputStream();
    }

    /**
     * 文件位置的描述
     *
     * @return
     */
    public String getRealPath() {
        return this.fileLoader.getRealPath();
    }

    /**
     * 取得文件的URL连接器
     *
     * @return URL
     * @throws java.io.IOException
     */
    public URL getURL() throws IOException {
        return this.fileLoader.getURL();
    }

    /**
     * 获得文件名称
     *
     * @return
     */
    public String getFileName() throws IOException {
        return this.fileLoader.getFileName();
    }

    /**
     * 获得文件内容的字节码数组
     *
     * @return 字节码数组
     */
    public byte[] getFileContentOfBytes() throws IOException {
        return this.fileLoader.getFileContentOfBytes();
    }

    /**
     * 获得文件对象
     *
     * @return
     */
    public File getFile() throws IOException {
        return this.fileLoader.getFile();
    }

    /**
     * 获得数据库文件加载器
     *
     * @return 资源文件加载器
     */
    public FileLoader getDataBaseFileLoader() {
        return new DataBaseFileLoader();
    }

}