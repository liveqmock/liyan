package com.innofi.component.uploader.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.List;

import com.innofi.framework.service.IBaseService;
import com.innofi.component.uploader.pojo.SysUploadFile;
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
public interface ISysUploadFileService extends IBaseService<SysUploadFile, String> {

    /**
     * 根据Inputstream创建Blob对象
     *
     * @param is 输入流
     * @return Bolb对象
     */
    public Blob createBlobByInputStream(InputStream is);

    /**
     * 绑定附件信息
     *
     * @param fileIds 附件id字符串，多个以","分隔
     */
    public void bdUploadFiles(String fileIds);

    /**
     * 解绑附件信息
     *
     * @param fileIds 附件id字符串，多个以","分隔
     */
    public void unBdUploadFile(String fileIds);

    /**
     * 按id删除对象.
     *
     * @param id 对象id
     */
    public void delete(String id);

    /**
     * 清楚未绑定的信息
     */
    public void clear0bind();

    /**
     * 复制附件
     *
     * @param uploadFileId 目标附件id
     * @return 新生成附件id
     */
    public String copyUploadFile(String uploadFileId);


    /**
     * 保存上传文件
     * @param file 文件对象
     * @param fileSaveType 文件保存类型
     * @param fileName     文件名称
     * @param fileSize     文件大小
     * @param fileType     文件类型
     */
    public SysUploadFile saveUploadFile(MultipartFile file,String fileSaveType , String fileName , Long fileSize , String fileType);


    /**
     * 保存上传文件
     * @param inputStream  文件输入流
     * @param fileSaveType 文件保存类型
     * @param fileName     文件名称
     * @param fileSize     文件大小
     * @param fileType     文件类型
     */
    public SysUploadFile saveUploadFile(FileInputStream inputStream,String fileSaveType , String fileName , Long fileSize , String fileType);


}