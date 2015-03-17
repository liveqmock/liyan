package com.innofi.component.uploader.service.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.exception.BaseException;
import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.file.FileUtil;
import com.innofi.framework.utils.reflect.ReflectionUtil;
import com.innofi.framework.utils.string.StringUtil;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.component.uploader.IdfUploadProcessor;
import com.innofi.component.uploader.pojo.SysUploadFile;
import com.innofi.component.uploader.service.ISysUploadFileService;
import org.springframework.web.multipart.MultipartFile;


/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Component(value = "sysUploadFileService")
public class SysUploadFileServiceImpl extends BaseServiceImpl<SysUploadFile, String> implements ISysUploadFileService {
    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    /**
     * @see com.innofi.component.uploader.service.ISysUploadFileService#createBlobByInputStream(java.io.InputStream)
     */
    public Blob createBlobByInputStream(InputStream is) {
        return getDaoSupport().getHibernateDao().createBlobByInputStream(is);
    }

    /**
     * @see com.innofi.component.uploader.service.ISysUploadFileService#bdUploadFiles(java.util.List)
     */
    public synchronized void bdUploadFiles(String fileIds) {
    	List<String> idsList = new ArrayList();
    	if(fileIds.indexOf(",")>-1){
    		String [] idArray = StringUtil.split(",");
    		for(String id : idArray){
    			idsList.add(id);
    		}
    	}else {
			idsList.add(fileIds);
		}
        List<SysUploadFile> fileList = this.findByIds(idsList);
        for (SysUploadFile uploadFile : fileList) {
            uploadFile.setFileBindFlg(IdfUploadProcessor.BIND_FLAG_YES);
            this.update(uploadFile);
        }
    }

    /**
     * @see com.innofi.component.uploader.service.ISysUploadFileService#unBdUploadFile(java.util.List)
     */
    public synchronized void unBdUploadFile(String fileIds) {
    	List<String> idsList = new ArrayList();
    	if(fileIds.indexOf(",")>-1){
    		String [] idArray = StringUtil.split(",");
    		for(String id : idArray){
    			idsList.add(id);
    		}
    	}else {
			idsList.add(fileIds);
		}
        List<SysUploadFile> fileList = this.findByIds(idsList);
        for (SysUploadFile uploadFile : fileList) {
            uploadFile.setFileBindFlg(IdfUploadProcessor.BIND_FLAG_NO);
            this.update(uploadFile);
        }
    }

    /**
     * @see com.innofi.component.uploader.service.ISysUploadFileService#unBdUploadFile(java.util.List)
     */
    public void clear0bind() {
        this.deleteByPropertyFilters(buildPropertyFilter("fileBindFlg", IdfUploadProcessor.BIND_FLAG_NO, QueryConstants.EQUAL));
    }

    /**
     * @see com.innofi.component.uploader.service.ISysUploadFileService#copyUploadFile(String)
     */
    public String copyUploadFile(String uploadFileId) {
        SysUploadFile sourceSysUploadFile = get(uploadFileId);
        if (null == sourceSysUploadFile) return "没有找到,id为[" + uploadFileId + "]附件!";
        SysUploadFile targetSysUploadFile = new SysUploadFile();
        ReflectionUtil.copyProperties(sourceSysUploadFile, targetSysUploadFile);
        this.save(targetSysUploadFile);
        return targetSysUploadFile.getId();
    }

    /**
     * @see com.innofi.component.uploader.service.ISysUploadFileService#saveUploadFile(org.springframework.web.multipart.MultipartFile, String, String, Long, String)
     */
    public SysUploadFile saveUploadFile(MultipartFile file, String fileSaveType, String fileName, Long fileSize, String fileType) {
        SysUploadFile sysUploadFile = new SysUploadFile();
        sysUploadFile.setFileName(fileName);
        sysUploadFile.setFileType(fileType);
        sysUploadFile.setFileSize(fileSize);
        sysUploadFile.setFileBindFlg(IdfUploadProcessor.BIND_FLAG_NO);//设置绑定标记为未绑定

        if (FrameworkConstants.FILE_UPLOAD_SAVE_TYPE_CLOB.equalsIgnoreCase(fileSaveType)) {// CLOB字符串的保存
            try {
                sysUploadFile.setFileSaveType(FrameworkConstants.FILE_UPLOAD_SAVE_TYPE_CLOB);
                sysUploadFile.setFileContentC(IOUtils.toString(file.getInputStream()));
            } catch (IOException e) {
                throw new BaseException("附件保存CLOB类型时出现异常!");
            }
        } else if (FrameworkConstants.FILE_UPLOAD_SAVE_TYPE_BLOB.equalsIgnoreCase(fileSaveType)) {//BLOB二进制的保存
            try {
                InputStream is = file.getInputStream();
                sysUploadFile.setFileContentB(createBlobByInputStream(is));
                sysUploadFile.setFileSaveType(FrameworkConstants.FILE_UPLOAD_SAVE_TYPE_BLOB);
            } catch (Exception e) {
                throw new BaseException("附件保存BLOB类型时出现异常!");
            }
        } else {//文件类型保存
            File folder = new File(fileSaveType);
            sysUploadFile.setFilePath(fileSaveType+"/"+fileName);//保存路径
            sysUploadFile.setFileSaveType("FILE");

            try {
                File dest = new File(fileSaveType, fileName);//保存到文件路径
                if (!folder.exists()) {
                    folder.mkdir();
                }
                file.transferTo(dest);
            } catch (Exception e) {
                throw new BaseException("附件保存文件类型时出现异常!");
            }
            
        }
        save(sysUploadFile);
        ConsoleUtil.info("上传附件，附件信息" + sysUploadFile.toString());
        return sysUploadFile;
    }

    @Override
    public SysUploadFile saveUploadFile(FileInputStream inputStream, String fileSaveType, String fileName, Long fileSize, String fileType) {
        SysUploadFile sysUploadFile = new SysUploadFile();
        sysUploadFile.setFileName(fileName);
        sysUploadFile.setFileType(fileType);
        sysUploadFile.setFileSize(fileSize);
        sysUploadFile.setFileBindFlg(IdfUploadProcessor.BIND_FLAG_NO);//设置绑定标记为未绑定

        if (FrameworkConstants.FILE_UPLOAD_SAVE_TYPE_CLOB.equalsIgnoreCase(fileSaveType)) {// CLOB字符串的保存
            try {
                sysUploadFile.setFileSaveType(FrameworkConstants.FILE_UPLOAD_SAVE_TYPE_CLOB);
                sysUploadFile.setFileContentC(IOUtils.toString(inputStream));
            } catch (IOException e) {
                throw new BaseException("附件保存CLOB类型时出现异常!");
            }
        } else if (FrameworkConstants.FILE_UPLOAD_SAVE_TYPE_BLOB.equalsIgnoreCase(fileSaveType)) {//BLOB二进制的保存
            try {
                sysUploadFile.setFileContentB(createBlobByInputStream(inputStream));
                sysUploadFile.setFileSaveType(FrameworkConstants.FILE_UPLOAD_SAVE_TYPE_BLOB);
            } catch (Exception e) {
                throw new BaseException("附件保存BLOB类型时出现异常!");
            }
        } else {//文件类型保存
            File folder = new File(fileSaveType);
            sysUploadFile.setFilePath(fileSaveType+"/"+fileName);//保存路径
            sysUploadFile.setFileSaveType("FILE");

            try {
                File dest = new File(fileSaveType, fileName);//保存到文件路径
                if (!folder.exists()) {
                    folder.mkdir();
                }
                FileUtil.write(dest,inputStream);
            } catch (Exception e) {
                throw new BaseException("附件保存文件类型时出现异常!");
            }

        }
        save(sysUploadFile);
        ConsoleUtil.info("上传附件，附件信息" + sysUploadFile.toString());
        return sysUploadFile;
    }

    /**
     * 删除上传文件
     */
    private static String matches = "[A-Za-z]:\\\\[^:?\"><*]*";

    public void delete(String id) {
        SysUploadFile file = get(id);
        if (FrameworkConstants.FILE_UPLOAD_SAVE_TYPE_FILE.equals(file.getFileSaveType()) && file.getFilePath().matches(matches)) {//以路径存储的 删除服务器文件
            List<SysUploadFile> exitlist = this.findByHql(" from SysUploadFile u where u.fileBindFlg = '1' and u.filePath = ? and u.fileName = ? ", file.getFilePath(), file.getFileName());
            if (exitlist.size() == 0) { //如果有已经绑定的文件，禁止在磁盘上删除。
                ConsoleUtil.info("[清除已上传的磁盘文件信息--]" + file.getFilePath() + File.pathSeparator + file.getFileName());
                this.deleteFile(file.getFilePath() + File.pathSeparator + file.getFileName());
            }
        }
        this.delete(file);
    }

    /*
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     */
    private void deleteFile(String sPath) {
        File file = new File(sPath);// 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

}

