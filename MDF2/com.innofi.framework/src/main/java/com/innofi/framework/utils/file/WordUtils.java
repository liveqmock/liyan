package com.innofi.framework.utils.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
//import com.innofi.utils.FileUtil;
import com.innofi.framework.utils.file.FileUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

public class WordUtils {
	
	static Logger logger = LoggerFactory.getLogger(WordUtils.class.getName());
	/**
	 * 读取HTML文件内容
	 * @throws IOException 
	 * 
	 */
	public static String getHtmlContent(String filePath) throws IOException{
		InputStream in = null;
		String result = null;
		try {
			in = new FileInputStream(filePath);
			byte[] array = FileUtil.getBytes(in);
			result = new String(array, "gb2312");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	/**
	 * 读取需要合并的HTML文件内容
	 * 
	 */
	public static String getBodyContent(String filePath) throws IOException{
		File f = new File(filePath);
        if (!f.exists()) {
            logger.error("------file not exist!------"+filePath);
            return "";
        }
        String htmlCode = getHtmlContent(filePath);
        String htmlContent = "";
		// 处理body
		int bodyIndex = htmlCode.indexOf("<BODY");

		int bodyEndIndex = htmlCode.indexOf("</HTML>");

		if (bodyIndex != -1 && bodyEndIndex != -1) {
			htmlContent = htmlCode.substring(bodyIndex, bodyEndIndex);
			if(htmlContent.indexOf(">") != -1){
				htmlContent = htmlContent.substring(htmlContent.indexOf(">")+1);
			}
		}
		// 去除多余的代码
		htmlContent = htmlContent.replaceAll("<!--[\\S\\s]*?-->","");
		//bodyCode = bodyCode.replaceAll("<!\\[if[\\S\\s]*?<!\\[endif\\]>","");
		htmlContent = StringUtils.replace(htmlContent, "<![if !vml]>", "");
		htmlContent = StringUtils.replace(htmlContent, "<![endif]>", "");
		htmlContent = StringUtils.replace(htmlContent, "<![if !mso]>", "");
		htmlContent = StringUtils.replace(htmlContent, "<![if !supportLists]>", "");
		
		htmlCode = null;
		return htmlContent;
	}
	
	/**
	 * 转换word类型的正文和红头文件为html类型，然后再将两个类型的文档合并两个HTML文件。
	 * @param newfilePath	合并后的路径
	 * @param hfilePath		红透文件路径
	 * @param zfilePath		正文路径。
	 * @throws IOException
	 */
	public static void createNewHtml(String newfilePath,String hfilePath, String zfilePath) throws IOException{
		File hf = new File(hfilePath);
		File zf = new File(zfilePath);
        if (!hf.exists()) {
            logger.error("------file not exist!------"+hfilePath);
            //return "";
        }
        if (!zf.exists()) {
            logger.error("------file not exist!------"+zfilePath);
            //return "";
        }
        String hContent = getHtmlContent(hfilePath);
        String zContent = getHtmlContent(zfilePath);
        String htmlContent = "";
		// 处理body
		int zbodyIndex = zContent.indexOf("<BODY");
		int zbodyEndIndex = zContent.indexOf("</BODY>");
		int hbodyEndIndex = hContent.indexOf("</BODY>");
		if (zbodyIndex != -1 && zbodyEndIndex != -1) {
			zContent = zContent.substring(zbodyIndex, zbodyEndIndex);
			if(zContent.indexOf(">") != -1){
				zContent = zContent.substring(zContent.indexOf(">")+1);
			}
			hContent = hContent.substring(0, hbodyEndIndex);
			htmlContent = hContent.concat(zContent).concat("</BODY></HTML>");
		}
		FileUtils.writeStringToFile(new File(newfilePath), htmlContent,"gb2312");
		System.out.println("===========exit===");
	}

	
	/**
	 * 
	* @Title: addLink 
	* @Description: 给html文件里面的《》添加超链接 
	* @param @param file 文件路径
	* @param @throws UnsupportedEncodingException
	* @param @throws FileNotFoundException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public static void addLink(String file) throws UnsupportedEncodingException, FileNotFoundException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file+"##tmp",true), "GBK"));
		PrintWriter out = new PrintWriter(bw,true); 
		String str="";
		String fileName="";
		Matcher matcher =null;
		try {
			while (br.ready()) {
				str=br.readLine();
				matcher = Pattern.compile("《.*?》").matcher(Pattern.compile("<A HREF=.*?《.*?</A>").matcher(Pattern.compile("<A HREF=.*?《.*?>").matcher(Pattern.compile("<A NAME=.*?《.*?</A>").matcher(str).replaceAll("")).replaceAll("")).replaceAll(""));
				while(matcher.find()){
					fileName=matcher.group();
					//str=str.replace(fileName, "<a onclick=\"javascript:window.opener.newWindow('document.page?cmd=viewAttach&documentName="+Pattern.compile("<.*?>").matcher(fileName).replaceAll("").replace("《", "").replace("》", "")+"',this)\"  style=\"cursor:hand;color:"+Constants.linkColor+"; text-decoration:none;\">"+Pattern.compile("<.*?>").matcher(fileName).replaceAll("")+"</a>");
 					str=str.replace(fileName, "<a href=\"http://localhost:8080/cbgrc/"+Pattern.compile("<.*?>").matcher(fileName).replaceAll("").replace("《", "").replace("》", "")+"\"  >"+Pattern.compile("<.*?>").matcher(fileName).replaceAll("")+"</a>");

				}
				out.println(str);
			}
			br.close();
			out.close(); 
			bw.close();
			File oldFile = new File(file);
			File tmpFile= new File(file+"toBeDelete");
			File editedFile = new File(file+"##tmp");
			oldFile.renameTo(tmpFile);
			editedFile.renameTo(oldFile);
			tmpFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 *文件转换
	 */
	  public static void fileConverter(String inputFilePath,String outputFilePath) {   
		  	File inputFile = new File(inputFilePath);
		 	File outputFile = new File(outputFilePath);
		 	OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);   
	         try {   
	             connection.connect();   
	             DocumentConverter converter = new OpenOfficeDocumentConverter(connection);   
	             converter.convert(inputFile, outputFile);   
	         } catch (ConnectException cex) {   
	             cex.printStackTrace();   
	         } finally {   
	             // close the connection   
	             if (connection != null) {   
	                 connection.disconnect();   
	                 connection = null;   
	             }   
	         }   
	     }  

	  /**
	   * 
	   * @param files 		pdf文件数组 要合並文件數組(絕對路徑如{ "e:\\1.pdf"，"e:\\2.pdf"， ，。。。。。。 "e:\\3.pdf"})
	   * @param newfile		合並後新產生的文件絕對路徑如e:\\temp.pdf,請自己刪除用過後不再用的文件請 *
	   * @return			產生成功返回true, 否則返回false
	   */
	  	public static boolean mergePdfFiles(String[] files, String newfile) {
	  		boolean retValue = false;
	  		Document document = null;
	  		try {
	  			document = new Document(new PdfReader(files[0]).getPageSize(1));
	  			PdfCopy copy = new PdfCopy(document, new FileOutputStream(newfile));
	  			document.open();
	  			for (int i = 0; i < files.length; i++) {
	  				PdfReader reader = new PdfReader(files[i]);
	  				int n = reader.getNumberOfPages();
	  				for (int j = 1; j <= n; j++) {
	  					document.newPage();
	  					PdfImportedPage page = copy.getImportedPage(reader, j);
	  					copy.addPage(page);
	  				}
	  			}
	  			retValue = true;
	  		} catch (Exception e) {
	  			e.printStackTrace();
	  		} finally {
	  			document.close();
	  		}
	  		return retValue;
	  	}
	 

	public static void main(String[] args) throws Exception {
		
		
		//String[] files = { "d:\\1.pdf", "d:\\2.pdf"};  
		//String savepath = "d:\\temp.pdf";  
		//mergePdfFiles(files, savepath); 
		//OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
		 //connection.connect();   
        // DocumentConverter converter = new OpenOfficeDocumentConverter(connection); 
         
//         fileConverter("d://中国银监会关于印发《商业银行投资保险公司股权试点管理办法》的通知（2009-11-5）.doc","d://中国银监会关于印发《商业银行投资保险公司股权试点管理办法》的通知（2009-11-5）.html");
//         fileConverter("d://中国银监会关于印发《商业银行流动性风险管理指引》的通知（2009-9-28）.doc","d://中国银监会关于印发《商业银行流动性风险管理指引》的通知（2009-9-28）.html");
//
//        
//	     createNewHtml("D:/测试合并文件.html","D:/中国银监会关于印发《商业银行投资保险公司股权试点管理办法》的通知（2009-11-5）.html","D:/中国银监会关于印发《商业银行流动性风险管理指引》的通知（2009-9-28）.html");
//	     
//	     fileConverter("D:/测试合并文件.html","D:/测试合并文件.doc");
	     
	  //  fileConverter("D:/test.pptx","D:/1213.pdf");
	  
//		String file = "D:/通知.html";
//		String body0 = WordUtils.getBodyContent(file);
//		System.out.println("=========body0======="+body0);
//		String file1 = "D:/通知正文.html";
//		String body1 = WordUtils.getBodyContent(file1);
//		System.out.println("=========body1======="+body1);
//		
//		String body = body0.concat(body1);
//		
//		System.out.println("=========body======="+body);
//		
//		FileUtils.writeStringToFile(new File("D:/通知正文1.html"), body0);
//		System.out.println("=========exit=======");
	}
}
