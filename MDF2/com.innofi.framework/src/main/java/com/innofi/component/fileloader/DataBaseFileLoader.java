package com.innofi.component.fileloader;


//import com.innofi.component.filemanagement.app.pojo.FileInfo;
//import com.innofi.component.filemanagement.app.service.IFileInfoService;
//import com.innofi.framework.spring.SpringUtil;
//import com.innofi.framework.properties.SystemProperties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

//todo 集成文件管理服务

/**
 * 数据库文件加载器
 * Copyright (c) 2010 正信嘉华管理顾问有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-11-19
 * found time: 17:44:04
 */
public class DataBaseFileLoader extends FileLoader {

    //private FileInfo fileInfo;

    //private IFileInfoService fileInfoService;

    private String fileManageServiceName;

    public DataBaseFileLoader() {
        super(null);
        //   fileManageServiceName = SystemProperties.getString("file.manage.service");
        //    fileInfoService = (IFileInfoService) SpringUtil.getContext().getBean(fileManageServiceName);
    }

    public DataBaseFileLoader(String rootPath) {
        super(rootPath);
        //  fileManageServiceName = SystemProperties.getString("file.manage.service");
        //  fileInfoService = (IFileInfoService) SpringUtil.getContext().getBean(fileManageServiceName);
    }


    protected void doSetFile(String fileId) {
        //  fileInfo = fileInfoService.getFileInfoById(fileId);
    }

    public boolean exists() {
        // if (fileInfo == null) return false;
        return true;
    }

    public long getLastModified() throws IOException {
        //if(fileInfo.getUpdDate()!=null){
        //	return fileInfo.getUpdDate().getTime();
        //}
        return 0L;
    }

    public InputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    public String getRealPath() {
        throw new UnsupportedOperationException();
    }

    public URL getURL() throws IOException {
        throw new UnsupportedOperationException();
    }

    public String getFileName() {
        //return fileInfo.getName() + "." + fileInfo.getExtFix();
        return null;
    }

    public byte[] getFileContentOfBytes() {
        //return fileInfo.getContent();
        return null;
    }

    public File getFile() throws IOException {
        throw new UnsupportedOperationException();
    }

}
