package com.innofi.component.uploader;

import com.bstek.dorado.uploader.UploadProcessor;

import com.innofi.framework.exception.BaseException;
import com.innofi.component.report.pdf.PdfToSwfConverter;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.component.uploader.pojo.SysUploadFile;
import com.innofi.component.uploader.service.ISysUploadFileService;
import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.file.DocToPdfConverter;
import com.innofi.framework.utils.file.FileUtil;
import com.innofi.framework.utils.validate.Validator;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          word转换pdf通用实现，使用openoffice作为基础
 * @found date: 2010-11-23
 * @found time: 20:40:56
 */
public class IdfUploadProcessor implements UploadProcessor {

    public static final String BIND_FLAG_YES = "1"; //上传附件是否与业务对象绑定 是
    public static final String BIND_FLAG_NO = "0";  //上传附件是否与业务对象绑定 否

    @Override
    public Object process(MultipartFile file, HttpServletRequest req, HttpServletResponse res) {
        String fileSaveType = req.getParameter("fileSaveType");                                                			//文件保存类型
        String onlineView = req.getParameter("onlineView");                                                    			//是否在线预览
        
        String originalName = file.getOriginalFilename();                                                               //文件名称
        Long fileSize = file.getSize();                                                                                 //文件大小
        String fileType = originalName.substring(originalName.lastIndexOf(".") + 1);                                    //文件类型
        ISysUploadFileService uploadFileService = ContextHolder.getSpringBean("sysUploadFileService");                  //上传文件服务
        SysUploadFile sysUploadFile = uploadFileService.saveUploadFile(file, fileSaveType, originalName, fileSize, fileType);//保存附件信息


        String filePath = ContextHolder.getSystemProperties().getString("download.tempfile.storepath") + "/";//获取系统临时存放文件夹路径
        File tempFileDir = new File(filePath);
        if(!tempFileDir.exists()){
        	FileUtil.mkdirs(tempFileDir.getPath());
        }
        
        String swfId = UUID.randomUUID() + "";                                                      //swf文件名称
        String tempFilePath = sysUploadFile.getFilePath();
        if(Validator.isEmpty(tempFilePath)){
	        tempFilePath = filePath + swfId + "." + fileType;                                    //转换文件路径
	        File fileTemp = new File(tempFilePath);
	        try {
	        	fileTemp.createNewFile();
	            file.transferTo(fileTemp);                                                              //将文件先放到临时文件夹进行对应转换
	        } catch (Exception e) {
	        	e.printStackTrace();
	            throw new BaseException("附件预览保存临时文件出现异常!");
	        }
        }
        
        

        try {
        	if(onlineView==null||!"false".equals(onlineView)){
/*                OpenOfficeUtil.startOpenOffice();
    	        DocToPdfConverter docToPdfConverter = new DocToPdfConverter(tempFilePath);
    	        PdfToSwfConverter pdfToSwfConverter = ContextHolder.getSpringBean("mdf.pdfToSwfConverter");
    	        *//*
    	         * 处理DOC 文件 以及 PDF文件在线显示的问题,将对应文件保存成SWF格式 保存到 上传表中。
    	         *//*
                synchronized(pdfToSwfConverter){
    	            processOnlinePreview(fileSaveType, fileType, uploadFileService, sysUploadFile, swfId, docToPdfConverter, pdfToSwfConverter);
                }*/
            }
            res.getWriter().print("{fileId:'" + sysUploadFile.getId() + "'}");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*
     * 处理DOC 文件 以及 PDF文件在线显示的问题,将对应文件保存成SWF格式 保存到 上传表中。
     */
    public static void processOnlinePreview(String fileSaveType, String fileType, ISysUploadFileService uploadFileService, SysUploadFile sysUploadFile, String swfId, DocToPdfConverter docToPdfConverter, PdfToSwfConverter pdfToSwfConverter) {

            if (fileType.equalsIgnoreCase("doc") || fileType.equalsIgnoreCase("docx") || fileType.equalsIgnoreCase("pdf")) {
                if (fileType.equalsIgnoreCase("doc") || fileType.equalsIgnoreCase("docx")) {
                    docToPdfConverter.docToPdf();
                }
                String swflookPath = ContextHolder.getWebRootPath() + ContextHolder.getSystemProperties().getString("mdf.uploader.swflook") + "/";
                File fileDir = new File(ContextHolder.getWebRootPath() + "/" + swflookPath);
                if (!fileDir.exists()) {
                    FileUtil.mkdirs(fileDir.getPath());
                }
                String swfName = swfId + ".swf";

                try {
                    pdfToSwfConverter.convert(docToPdfConverter.getPdfFile().getPath(), swflookPath + swfName);
                    File swfFile = new File(swflookPath + swfName);
                    InputStream isSwf = new FileInputStream(swfFile);
                    sysUploadFile.setFileSwfName(swfName);
                    sysUploadFile.setFileSwfPath(swflookPath + swfName);
                    sysUploadFile.setFileSwfB(uploadFileService.createBlobByInputStream(isSwf));
                    docToPdfConverter.getDocFile().delete();
                    docToPdfConverter.getPdfFile().delete();
                    uploadFileService.update(sysUploadFile);
                } catch (Exception e) {
                	e.printStackTrace();
                    ConsoleUtil.error("pdf转换swf出现异常");
                }
            }
    }
}
