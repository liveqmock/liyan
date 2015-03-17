package com.innofi.component.uploader.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.innofi.framework.exception.BaseException;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.service.IBaseService;
import com.innofi.component.report.pdf.PdfToSwfConverter;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.component.uploader.IdfUploadProcessor;
import com.innofi.component.uploader.pojo.SysUploadFile;
import com.innofi.component.uploader.service.ISysUploadFileService;
import com.innofi.framework.utils.file.DocToPdfConverter;
import com.innofi.framework.utils.file.FileUtil;
import com.innofi.framework.utils.openoffice.OpenOfficeUtil;
import com.innofi.framework.utils.validate.Validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 功能/ 模块：todo
 *
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 为dorado7界面维护操作的提供支持，实现SysUploadFile对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component(value = "sysUploadFileAction")
public class SysUploadFileAction extends BaseActionImpl {
    @Resource
    private ISysUploadFileService sysUploadFileSevice;

    public IBaseService getBusinessService() {
        return sysUploadFileSevice;
    }

    @DataProvider
    public void findSysUploadFiles(Page page, Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

        if (parameter != null) {
            addDateRangePropertyFilter(propertyFilters, "crtDate", parameter);

            addPropertyFilter(propertyFilters, "crtOrgCode", parameter.get("crtOrgCode"), (String) parameter.get("qMcrtOrgCode"), true);
            addPropertyFilter(propertyFilters, "crtUserCode", parameter.get("crtUserCode"), (String) parameter.get("qMcrtUserCode"), true);
            addPropertyFilter(propertyFilters, "id", parameter.get("id"), (String) parameter.get("qMid"), true);
            addPropertyFilter(propertyFilters, "fileName", parameter.get("fileName"), (String) parameter.get("qMfileName"), true);
            addPropertyFilter(propertyFilters, "filePath", parameter.get("filePath"), (String) parameter.get("qMfilePath"), true);
            addPropertyFilter(propertyFilters, "fileSize", parameter.get("fileSize"), (String) parameter.get("qMfileSize"), true);
            addPropertyFilter(propertyFilters, "fileType", parameter.get("fileType"), (String) parameter.get("qMfileType"), true);
            addDateRangePropertyFilter(propertyFilters, "updDate", parameter);
            addPropertyFilter(propertyFilters, "updOrgCode", parameter.get("updOrgCode"), (String) parameter.get("qMupdOrgCode"), true);
            addPropertyFilter(propertyFilters, "updUserCode", parameter.get("updUserCode"), (String) parameter.get("qMupdUserCode"), true);
        }

        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        sysUploadFileSevice.findByPage_Filters(propertyFilters, null, innofiPage);
    }

    @DataProvider
    public void findSysUploadFilesByIds(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        String sysuploadIds = (String) parameter.get("ids");
        if (StringUtils.isBlank(sysuploadIds)) {
            sysuploadIds = "noIds";
        }
        List<String> sysUploadIdList = new ArrayList(1);
        if (sysuploadIds.indexOf(",") > -1) {
            sysUploadIdList = java.util.Arrays.asList(sysuploadIds.split(","));
        } else {
            sysUploadIdList.add(sysuploadIds);
        }
        if (parameter != null) {
            addPropertyFilter(propertyFilters, "id", sysUploadIdList, QueryConstants.IN, true);
            addPropertyFilter(propertyFilters, "fileBindFlg", IdfUploadProcessor.BIND_FLAG_YES, QueryConstants.EQUAL, true);
        }
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysUploadFileSevice.findByPage_Filters(propertyFilters, null, innofiPage);

    }

    @DataResolver
    public void saveSysUploadFiles(Collection<SysUploadFile> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
			SysUploadFile sysUploadFile = (SysUploadFile) iterator.next();
            EntityState state = EntityUtils.getState(sysUploadFile);
            if (EntityState.DELETED.equals(state)) {
            	sysUploadFileSevice.delete(sysUploadFile);
            } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
            	sysUploadFileSevice.update(sysUploadFile);
            } else if (EntityState.NEW.equals(state)) {
            	sysUploadFileSevice.save(sysUploadFile);
            }
		}
    }

    @Expose
    public void delSysUploadFilesByIds(Map<String, Object> parameter) throws Exception {
        String ids = (String) parameter.get("ids");
        String[] id = ids.split(",");
        for (int i = 0; i < id.length; i++) {
            sysUploadFileSevice.delete(id[i]);
        }
    }

    @Expose
    public String copySysUploadFile(String uploadFileId) {
        return sysUploadFileSevice.copyUploadFile(uploadFileId);
    }

    @Expose
    public String createSwfForLook(Map<String, Object> parameter) {
        String uploadFileId = (String) parameter.get("id");
        SysUploadFile sysUploadFile = sysUploadFileSevice.get(uploadFileId);
        if (null == sysUploadFile) {
            throw new ClientRunnableException("dorado.MessageBox.alert('所查看文件未上传！')");
        }
        String fileType = sysUploadFile.getFileType();

        if (!fileType.equals("doc") && !fileType.equals("docx") && !fileType.equalsIgnoreCase("pdf")) {
            throw new ClientRunnableException("dorado.MessageBox.alert('在线查看目前只支持word和PDF文件！')");
        }

        String fileSwfPath = sysUploadFile.getFileSwfPath();
        Blob swfBlob = sysUploadFile.getFileSwfB();
        if (fileSwfPath == null && swfBlob == null) {
            String filePath = ContextHolder.getSystemProperties().getString("download.tempfile.storepath") + "/";//获取系统临时存放文件夹路径
            String swfId = UUID.randomUUID() + "";                                                               //swf文件名称
            String tempFilePath = filePath + swfId + "." + fileType;                                             //转换文件路径
            if (!FileUtil.exists(tempFilePath)) {
	            File fileTemp = new File(tempFilePath);
	            Blob lobContent = sysUploadFile.getFileContentB();
	            try {
	                fileTemp.createNewFile();
	                FileUtil.write(fileTemp, lobContent.getBytes(1, (int) lobContent.length()));
	            } catch (Exception e) {
	                e.printStackTrace();
	                throw new BaseException("附件预览保存临时文件出现异常!");
	            }
	            OpenOfficeUtil.startOpenOffice();
	            DocToPdfConverter docToPdfConverter = new DocToPdfConverter(tempFilePath);
	            PdfToSwfConverter pdfToSwfConverter = ContextHolder.getSpringBean("mdf.pdfToSwfConverter");
	            IdfUploadProcessor.processOnlinePreview(sysUploadFile.getFileSaveType(), sysUploadFile.getFileType(), sysUploadFileSevice, sysUploadFile, swfId, docToPdfConverter, pdfToSwfConverter);
            }
        }

        if (Validator.isEmpty(sysUploadFile.getFileSwfName())) {
            throw new ClientRunnableException("dorado.MessageBox.alert('该文件格式存在问题，请先下载附件修改正确后，再次上传；')");
        }

        return ContextHolder.getSystemProperties().getString("mdf.uploader.swflook") + "/" + sysUploadFile.getFileSwfName();
    }

}
