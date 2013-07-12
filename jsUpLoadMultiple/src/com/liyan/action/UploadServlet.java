package com.liyan.action;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import sun.misc.BASE64Decoder;

public class UploadServlet extends HttpServlet{

	/**
	 * 将文件保存到服务器路径中
	 * 目前 40M以上无法上传
	 */
	private static final long serialVersionUID = 1537585345964493298L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String xmlfilename  = "" ; 
		String xmlfilepath = "" ;
		int xmlfilesize  = 0 ;
		byte[] xmlfilebytes = null ;
		int unit_size =1024 ; 
		try{
			SAXBuilder builder = new SAXBuilder();
			
			Document doc = builder.build(req.getInputStream());
			
			Element eroot = doc.getRootElement();
			//名称
			Iterator it  = eroot.getChildren("uploadfilename").iterator();
			while(it.hasNext()){
				xmlfilename = ((Element)it.next()).getText();
			}
			//路径
			Iterator it_path  = eroot.getChildren("uploadfilepath").iterator();
			while(it_path.hasNext()){
				xmlfilepath = ((Element)it_path.next()).getText();
			}
			//大小
			Iterator it_size  = eroot.getChildren("uploadfilesize").iterator();
			while(it_size.hasNext()){
				xmlfilesize = Integer.parseInt(((Element)it_size.next()).getText());
			}
			if(xmlfilesize>0){
				 int unit_count = 0 ;
				 xmlfilebytes = new byte[xmlfilesize];
				 Iterator it_content  = eroot.getChildren("uploadcontent").iterator();
				 while(it_content.hasNext()){
					 BASE64Decoder base64 = new BASE64Decoder();
					 byte[] xmlnodebytearray = base64.decodeBuffer(((Element)it_content.next()).getText());
					
					 if(xmlnodebytearray.length>= unit_size){
						 
						 System.arraycopy(xmlnodebytearray, 0, xmlfilebytes, unit_count*unit_size, unit_size);
						 
					 }else{
						 
						 System.arraycopy(xmlnodebytearray, 0, xmlfilebytes, unit_count*unit_size, xmlfilesize%unit_size);
						 
					 }
					 unit_count ++ ; 
				 }
			}
			xmlfilename  = xmlfilename.substring(xmlfilename.lastIndexOf("\\"),xmlfilename.length());
			FileOutputStream fos = new FileOutputStream(""+xmlfilename) ;
			fos.write(xmlfilebytes);
			fos.flush();
			fos.close();

		}catch(IOException e){
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
				
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
}
