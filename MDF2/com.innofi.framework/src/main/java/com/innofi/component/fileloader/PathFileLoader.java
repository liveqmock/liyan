package com.innofi.component.fileloader;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 文件系统文件加载器
 * Copyright (c) 2010 正信嘉华管理顾问有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public class PathFileLoader extends FileLoader {

    private File file;
    private String realPath;

    public PathFileLoader() {
        super(null);
    }

    /**
     * 文件系统文件加载器构造方法
     *
     * @param root 文件系统路径
     */
    public PathFileLoader(String root) {
        super(root);
    }

    protected void doSetFile(String filepath) {
        if (this.rootPath == null) {
            this.realPath = filepath;
        } else {
            this.realPath = this.rootPath + filepath;
        }
        this.file = new File(this.realPath);
    }

    /**
     * 判断指定路径的文件是否存在
     *
     * @return true 存在、false 不存在
     */
    public boolean exists() {
        return this.file.exists();
    }

    /**
     * 获得文件最后修改时间
     *
     * @return 返回文件最后的修改时间
     * @throws java.io.IOException
     */
    public long getLastModified() throws IOException {
        return this.file.lastModified();
    }

    /**
     * 获得指定文件的输入流
     *
     * @return 输入流对象
     * @throws java.io.IOException
     */
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    /**
     * 获得文件的输出流
     *
     * @return 该方法无法
     * @throws java.io.IOException
     */
    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(this.file);
    }

    /**
     * 获得文件的相对路径
     *
     * @return 文件的相对路径
     */
    public String getRealPath() {
        return this.realPath;
    }

    /**
     * 获得资源文件的url
     *
     * @return 返回资源文件的url
     * @throws java.io.IOException
     */
    public URL getURL() {
        try {
            return new URL("file:/" + this.realPath);
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获得文件名称
     *
     * @return
     */
    public String getFileName() {
        return this.file.getName();
    }

    /**
     * 获得文件内容的字节码数组
     *
     * @return 字节码数组
     */
    public byte[] getFileContentOfBytes() throws IOException {
        return getBytes(getInputStream());
    }

    /**
     * 获得文件对象
     *
     * @return
     */
    public File getFile() throws IOException {
        return this.file;
    }

    /**
     * 从指定的输入流中读取内容，记录在byte数组中
     *
     * @param in 输入流
     * @return 以byte数组形式返回读取的输入流内容
     * @throws java.io.IOException
     */
    private byte[] getBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int c = in.read();
        while (c != -1) {
            out.write(c);
            c = in.read();
        }
        out.close();
        return out.toByteArray();
    }

}