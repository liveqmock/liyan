package com.innofi.component.download;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.innofi.framework.exception.FileDownloadException;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.component.uploader.pojo.SysUploadFile;
import com.innofi.component.uploader.service.ISysUploadFileService;
import com.innofi.framework.utils.date.DateUtil;
import com.innofi.framework.utils.file.FileUtil;
import com.innofi.framework.utils.http.ContentTypeTool;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;

/**
 * 文件下载工具
 * gl company copyright 2010
 * User: liumy
 * Date: 2010-2-11
 * Time: 22:42:08
 */
public class DownloadTool {

    /**
     * 多文件下载时，需要先将文件打成zip文件，此路径为临时存放zip文件的目录，默认classpath下tmp目录
     */
    public static final String DOWNLOAD_TEMPZIP_STOREPATH = "download.tempzip.storepath";

    /**
     * 当文件存储在数据库时，用户下载数据库文件，需将数据库文件先放在文件系统的某个目录，此路径为临时文件存放的目录
     */
    public static final String DOWNLOAD_TEMPFILE_STOREPATH = "download.tempfile.storepath";

    /**
     * 多文件下载时，需将文件打包成zip文件下载，此参数指定生成的zip文件多长时间后进行清理，默认0直接删除，单位（分钟）。
     */
    public static final String DOWNLOAD_TMPZIP_DELTIME = "download.tmpzip.deltime";

    /**
     * 当文件存储在数据库时，用户下载数据库文件，此参数指定生成的临时文件多长时间后进行清理，默认0直接删除，单位（分钟）
     */
    public static final String DOWNLOAD_TMPFILE_DELTIME = "download.tmpfile.deltime";


    private static Logger _log = LoggerFactory.getLogger(DownloadTool.class);

    /**
     * http 响应对象
     */
    private HttpServletResponse response = null;

    /**
     * 压缩文件下载时，设定压缩文件暂时保存的路径（路径为绝对路径）
     */
    private String tmpZipStorePath = null;

    /**
     * 存储在数据库中的文件在文件系统临时存放路径（路径为绝对路径）
     */
    private String tmpFileStorePath = null;

    /**
     * 下载文件所在路径
     */
    private String downloadFilePath = null;

    public DownloadTool(HttpServletResponse resp) {
        this.response = resp;
        //多文件下载时，需要先将文件打成zip文件，此路径为临时存放zip文件的目录
        String tmpZipPath = ContextHolder.getSystemProperties().getString(DOWNLOAD_TEMPZIP_STOREPATH, "c:/grc/tmpzip");

        this.tmpZipStorePath = tmpZipPath;//设置临时存放zip文件的目录

        _log.debug("--------------------------zip 文件临时存放目录路径:[" + tmpZipStorePath + "]---------------------------------");
        //如果目录存在则创建临时目录
        if (!FileUtil.exists(tmpZipStorePath)) {
            _log.debug("--------------------------zip 文件临时存放路径不存在，创建临时存放目录:[" + tmpZipStorePath + "]---------------------------------");
            FileUtil.mkdirs(tmpZipStorePath);
        }

        //当文件存储在数据库时，用户下载数据库文件，需将数据库文件先放在文件系统的某个目录，此路径为临时文件存放的目录
        String tmpFilePath = ContextHolder.getSystemProperties().getString(DOWNLOAD_TEMPFILE_STOREPATH, "c:/grc/tmpzip");

        this.tmpFileStorePath = tmpFilePath;//设置数据库文件临时的目录
        _log.debug("--------------------------文件临时存放目录路径:[" + tmpFileStorePath + "]---------------------------------");

        //如果目录存在则创建临时目录
        if (!FileUtil.exists(tmpFileStorePath)) {
            _log.debug("--------------------------创建文件临时存放目录路径:[" + tmpFileStorePath + "]---------------------------------");
            FileUtil.mkdirs(tmpFileStorePath);
        }
    }

    /**
     * 下载文件中是否有文件存储在数据库中
     */
    private boolean hasDataBaseFile;

    /**
     * 处理文件下载
     *
     * @param fileIds        文件唯一标识列表
     * @param displayName    下载文件显示名称
     * @param contarinHidden 是否包含隐藏文件
     * @return 返回值：0 下载成功；-1 未知异常；-2 未找到指定文件；-20 IO异常。
     */
    public void processDownload(String[] fileIds, String displayName, boolean contarinHidden) throws FileDownloadException {
        try {
            List fileList = new ArrayList();
            loadFileList(fileIds, fileList);
            coreProcess(fileIds, fileList, displayName, contarinHidden);
        } catch (FileNotFoundException e) {
            FileDownloadException fdException = new FileDownloadException(e);
            //SystemLoggerFactory.getLogger().logError(fdException);
            throw fdException;
        } catch (IOException e) {
            FileDownloadException fdException = new FileDownloadException(e);
            //SystemLoggerFactory.getLogger().logError(fdException);
            throw fdException;
        } catch (Exception e) {
            FileDownloadException fdException = new FileDownloadException(e);
            // SystemLoggerFactory.getLogger().logError(fdException);
            throw fdException;
        }
    }

    /**
     * 根据指定文件列表，加载该列表中的文件及其子文件
     *
     * @param fileIds  文件唯一标识列表
     * @param fileList 保存文件的列表存储空间
     * @return 返回文件列表
     * @throws IOException
     * @throws SQLException
     */
    private void loadFileList(String[] fileIds, List fileList) throws IOException, SQLException {
    	List<String> dbFileIdList = new ArrayList<String>();
    	for(int i = 0 ; i < fileIds.length ; i++){
    		String fileId = fileIds[i];
    		if (FileUtil.exists(fileId)) {//如果存在则在文件系统中取，不存在则在数据库中取
                File file = new File(fileId);
                fileList.add(file);
            } else {
            	dbFileIdList.add(fileId);
            }
    	}
    	if(dbFileIdList.size()>0){
            ISysUploadFileService uploadFileService = ContextHolder.getSpringBean("sysUploadFileService");
            List<SysUploadFile> sysFileList = uploadFileService.findByIds(dbFileIdList);
            for (int i = 0; i < sysFileList.size(); i++) {//便利所有文件对象。
                SysUploadFile uploadFile = sysFileList.get(i);
                String filePath = uploadFile.getFilePath();
                String fileName = uploadFile.getFileName();
                String pathName = filePath + File.separator + fileName;
                if(!FileUtil.exists(pathName)){
                	 String clobContent = uploadFile.getFileContentC();
                     Blob blobContent = uploadFile.getFileContentB();
                     String fileSaveType = uploadFile.getFileSaveType();
                 	 byte[] contentBytes = null;
                     if ("CLOB".equalsIgnoreCase(fileSaveType)) {
                         contentBytes = clobContent.getBytes();
                     } else if ("BLOB".equalsIgnoreCase(fileSaveType)) {
                         if (null != blobContent || blobContent.getBinaryStream().read() > 0) {
                             contentBytes = blobContent.getBytes(0, (int) blobContent.length());
                         }
                     }
                	 File file = new File(this.tmpFileStorePath + "/" + fileName);
                     if (!file.exists()) file.createNewFile();
                     FileUtil.write(file, contentBytes);
                     fileList.add(file);
                     hasDataBaseFile = true;
                }else{
                	fileList.add(new File(pathName));
                }
            }
    	}
    }


    /**
     * 文件下载核心处理方法
     *
     * @param fileIds        文件唯一标识列表
     * @param fileList       文件对象列表
     * @param displayName    下载文件显示名称
     * @param contrainHidden 是否包含隐藏文件
     * @return 返回值：0 下载成功；-1 未定义ZIP文件存放路径；-2 未定义文件加载器 ；3 未找到指定文件；-20 IO异常。
     * @throws IOException
     */
    private void coreProcess(String fileIds[], List fileList, String displayName, boolean contrainHidden) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        displayName = setFileDisplayName(fileIds, fileList, displayName);

        byte[] content = new byte[1024 * 4];
        InputStream inStream = null;
        OutputStream outStream = null;
        long fileSize = -1;

        if (fileList.size() > 1 || ((File) fileList.get(0)).isDirectory()) {
            String zipFilePath = tmpZipStorePath + "/" + displayName;
            fileSize = FileUtil.packageZipFile(fileList, zipFilePath, contrainHidden);
            downloadFilePath = zipFilePath;
        } else {
            fileSize = ((File) fileList.get(0)).length();
            downloadFilePath = StringUtil.replace(((File) fileList.get(0)).getPath(), "\\", "/");
        }

        _log.debug(">>>>>[innofi-framework]: download file path is [" + downloadFilePath + "]");
        if (downloadFilePath != null) {//文件下载
            String extFix = downloadFilePath.substring(downloadFilePath.lastIndexOf(".") + 1, downloadFilePath.length());
            String contentType = ContentTypeTool.getContentType(extFix);
            if (Validator.isNull(contentType)) {
                contentType = "application/x-msdownload";
            }

            response.setContentType(contentType);
            displayName = new String(displayName.getBytes("gb2312"), "ISO-8859-1");//中文系统这里要用GB2312
            response.setHeader("Content-Disposition", "attachment;filename=" + displayName + "");
            response.setHeader("Pragma", "No-cache");
            response.addDateHeader("Expires", 0);


            try {
                inStream = new FileInputStream(downloadFilePath);
                outStream = response.getOutputStream();
                int read = -1;
                while ((read = inStream.read(content)) > 0) {
                    outStream.write(content, 0, read);
                }
                inStream.close();
            } catch (Exception e) {
                FileDownloadException fde = new FileDownloadException(e);
                //SystemLoggerFactory.getLogger().logError(fde);
            } finally {
                if (inStream != null) inStream.close();
            }
            //多文件下载时，需将文件打包成zip文件下载，此参数指定生成的zip文件多长时间后进行清理，默认0直接删除，单位（分钟）。
            Integer deleteTime = ContextHolder.getSystemProperties().getInteger(DOWNLOAD_TMPZIP_DELTIME, 0);
            //清理生成的zip文件
            if (deleteTime == 0 && (downloadFilePath.endsWith(".zip") || downloadFilePath.endsWith(".ZIP"))) {
                FileUtil.delete(downloadFilePath, false);
            }
            //当文件存储在数据库时，用户下载数据库文件，此参数指定生成的临时文件多长时间后进行清理，默认0直接删除，单位（分钟）
            deleteTime = ContextHolder.getSystemProperties().getInteger(DOWNLOAD_TMPFILE_DELTIME, 0);
            if (hasDataBaseFile && deleteTime == 0) {
                for (int i = 0; i < fileList.size(); i++) {
                    File f = (File) fileList.get(i);
                    if (f.getPath().indexOf(tmpFileStorePath) > -1) {
                        FileUtil.delete(f, false);
                    }
                }
            }
            //暂不作 数据库保存处理...
            /*    List processtorList = DownloadFileAfterProcesstorRegister.getProcesstorList();
            for (int i = 0; i < processtorList.size(); i++) {
                String className = (String) processtorList.get(i);
                Class cl = Class.forName(className);
                Constructor constr = cl.getConstructor();
                IDownloadFileAfterProcesstor processtor = (IDownloadFileAfterProcesstor) constr.newInstance();
                processtor.afterProcessing(displayName, fileIds, fileList.size(), new Date(), extFix, fileSize);
            }*/
        }
    }

    /**
     * 设置下载文件显示名称
     *
     * @param fileIds     文件唯一标识列表
     * @param fileList    文件对象列表
     * @param displayName 显示名称
     * @return 显示名称
     */
    private String setFileDisplayName(String[] fileIds, List fileList, String displayName) {
        //if ((fileList.size() > 1 || (fileList.size() == 1 && ((File) fileList.get(0)).isDirectory()))) {
    	if(fileList.size() > 1){
    		if(Validator.isNull(displayName)){
            displayName = DateUtil.formatDate("yyyyMMddHHmmss", new Date(System.currentTimeMillis())) + ".zip";
    		}else{
    			displayName+= ".zip";
    		}
        }/*else if (Validator.isNull(displayName) && fileList.size() == 1 && !((File) fileList.get(0)).isDirectory()) {
            String tmp = StringUtil.replace(fileIds[0], "\\", "/");
            displayName = tmp.substring(tmp.lastIndexOf("/") + 1);
        }*/else if (fileList.size() == 1 && !((File) fileList.get(0)).isDirectory()) {
            File file = (File) fileList.get(0);
            String fileName = file.getName();
            //String tmp = StringUtil.replace(fileList.get(0).toString(), "\\", "/");
            // displayName = tmp.substring(tmp.lastIndexOf("/") + 1);
            displayName = fileName;
        }
        return displayName;
    }

}
