package com.innofi.framework.utils.file;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.innofi.framework.exception.BaseException;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;
import org.slf4j.Logger;

import java.io.File;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          word转换pdf使用openOffice实现
 * @found date: 2010-11-23
 * @found time: 20:40:56
 */
public class DocToPdfConverter {

    private String filePath;
    private String fileName;
    public File pdfFile;
    public File docFile;


    public DocToPdfConverter(String filePath) {
        init(filePath);
    }

    /**
     * 重新设置 file
     *
     * @param filePath
     */
    public void setFile(String filePath) {
        init(filePath);
    }

    /*
     * 初始化
     *
     * @param filePath
     */
    private void init(String filePath) {
        this.filePath = filePath;
        fileName = this.filePath.substring(0, this.filePath.lastIndexOf("."));
        docFile = new File(this.filePath);
        pdfFile = new File(fileName + ".pdf");
    }

    /**
     * 将doc、docx转换为pdf格式
     *
     * @throws Exception
     */
    public void docToPdf() {
        if (docFile.exists()) {
            OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
            try {
                connection.connect();
                DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
                converter.convert(docFile, pdfFile);
                connection.disconnect();
                ConsoleUtil.info("word转换pdf成功，pdf输出路径：" + pdfFile.getPath());
            } catch (java.net.ConnectException e) {
                ConsoleUtil.error("****swf转换异常，openoffice服务未启动！****");
                throw new BaseException(e);
            } finally {
                connection.disconnect();
            }
        } else {
            ConsoleUtil.error("文件转换器异常，需要转换的文档不存在，无法转换****");
        }
    }

    public File getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(File pdfFile) {
        this.pdfFile = pdfFile;
    }

    public File getDocFile() {
        return docFile;
    }

    public void setDocFile(File docFile) {
        this.docFile = docFile;
    }
}