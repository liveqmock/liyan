/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.field.action;

import java.io.PrintWriter;

import com.innofi.component.metadata.table.service.IMdTableService;
import com.innofi.framework.action.impl.BaseActionImpl;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 功能/ 模块：模版导入模块
 *
 * @author steve.pu@innofi.com.cn
 * @version 2.0 12-12-29
 *          导入EXCEL处理
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component("uploadExcelAction")
public class UploadExcelAction extends BaseActionImpl {
	@Resource
    private IMdTableService mdTableServcie;

    public void doUploadExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
    		
    		request.setCharacterEncoding("UTF-8");
    		String filePath = request.getParameter("file");
    		String mdmId = request.getParameter("mdmId");
    		filePath = new String(filePath.getBytes("ISO8859-1"),"UTF-8");
    		boolean flag = mdTableServcie.saveExcelData(filePath);
    		response.setContentType("text/html;charset=UTF-8");  
    		//response.setCharacterEncoding("UTF-8");
    		PrintWriter out = this.getResponse().getWriter(); 
    		String url = request.getContextPath()+"/com.innofi.component.metadata.table.view.MdTableManage.d?mdmId="+mdmId+"&randomId="+Math.random();
    		if(flag)
    		{
    			out.println("<script>parent.dorado.MessageBox.alert('导入成功！');</script>"); 
    			out.println("<script>document.location='"+url+"'</script>");
    			
    		}
    		else {
    			out.println("<script>parent.dorado.MessageBox.alert('导入失败！');</script>"); 
    			out.println("<script>document.location='"+url+"'</script>");
			}

    }

   
}
