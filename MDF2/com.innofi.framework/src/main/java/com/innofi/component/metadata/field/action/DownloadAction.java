/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.field.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.spring.context.ContextHolder;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 功能/ 模块：登录模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0 12-12-29
 *          平台登录处理
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component("downloadAction")
public class DownloadAction extends BaseActionImpl {


    public void doDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setCharacterEncoding("UTF-8");
        String fileName = URLDecoder.decode(request.getParameter("fileName"), "UTF-8") + ".xls";
        String filedir = ContextHolder.getSystemProperties().getString("metadata.template.path", "c:/metadataTemplate");
        String filePath = filedir + "/" + fileName;
        InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
        byte[] data = new byte[fis.available()];
        fis.read(data);
        fis.close();
        fileName = fileName == null ? "无模版.png" : fileName;
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }


}
