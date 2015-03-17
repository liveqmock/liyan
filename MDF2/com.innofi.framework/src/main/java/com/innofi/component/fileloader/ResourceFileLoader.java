package com.innofi.component.fileloader;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.URL;


/**
 * Classpath资源文件加载器
 * Copyright (c) 2010 正信嘉华管理顾问有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public class ResourceFileLoader extends FileLoader {
    private URL url;
    private String realPath;
    private static long buildNo = -1L;


    public ResourceFileLoader() {
        super(null);
    }

    /**
     * 资源文件加载器构造方法
     *
     * @param root classpath下的某个路径
     */
    public ResourceFileLoader(String root) {
        super(root);
    }

    protected void doSetFile(String filePath) {
        if (this.rootPath == null) {
            this.realPath = filePath;
        } else {
            this.realPath = this.rootPath + filePath;
        }
        this.url = null;
    }

    protected URL doGetURL() throws IOException {
        if (this.url == null) {
            this.url = super.getClass().getResource(this.realPath);
            if (this.url == null) {
                ClassLoader cl = super.getClass().getClassLoader();
                while (cl != null) {
                    this.url = cl.getResource(this.realPath);
                    if (this.url != null) {
                        break;
                    }
                    cl = cl.getParent();
                }
                if (this.url == null) {
                    this.url = ClassLoader.getSystemResource(this.realPath);
                }
            }
            if (this.url == null) {
                throw new FileNotFoundException(this.realPath);
            }
        }
        return this.url;
    }

    /**
     * 判断指定路径的文件是否存在
     *
     * @return true 存在、false 不存在
     */
    public boolean exists() {
        try {
            return (doGetURL() != null);
        } catch (Exception ex) {
        }
        return false;
    }

    /**
     * 获得指定文件的输入流
     *
     * @return 输入流对象
     * @throws java.io.IOException
     */
    public InputStream getInputStream() throws IOException {
        return doGetURL().openStream();
    }

    /**
     * 获得文件的输出流,资源文件无法获得输出流
     *
     * @return 该方法无法
     * @throws java.io.IOException
     */
    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException();
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
    public URL getURL() throws IOException {
        return doGetURL();
    }

    /**
     * 获得文件名称
     *
     * @return
     */
    public String getFileName() throws IOException {
        return getFile().getName();
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
        String filePath = "";
        URL url = getURL();
        File f = null;
        if (url.toString().indexOf(AutoFileLoader.FILE_PREFIX1) > -1) {
            filePath = url.toString().substring(AutoFileLoader.FILE_PREFIX1.length());
        } else if (url.toString().indexOf(AutoFileLoader.FILE_PREFIX2) > -1) {
            filePath = url.toString().substring(AutoFileLoader.FILE_PREFIX2.length());
        }
        if (!StringUtils.isBlank(filePath)) {
            f = new File(filePath);
        }
        return f;
    }

    /**
     * 获得文件最后修改时间
     *
     * @return 返回文件最后的修改时间
     * @throws java.io.IOException
     */
    public long getLastModified() throws IOException {
        return getFile().lastModified();
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

    public static void main(String[] args) throws IOException {
        FileLoader f = FileLoaderFactory.createFileLoader("META-INF");
        f.setFile("framework-ctx.xml");
    }

}