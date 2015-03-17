package com.innofi.framework.utils.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.innofi.framework.spring.context.ContextHolder;

/*
 * doc docx格式转换
 * @author puxuanguo
 */
public class DocConverter {
	private static final Logger logger = LoggerFactory.getLogger(DocConverter.class);
	private static final int environment = ContextHolder.getSystemProperties().getInteger("mdf.environment");
	private String fileString;
	private String outputPath = "";// 输入路径，如果不设置就输出在默认位置
	private String fileName;
	public static File pdfFile;
	public static File swfFile;
	public static File docFile;

	/*
	 * 启动openoffice服务
	 */
	private static Process pro = null;
	public static void startOpenOffic() throws IOException{
		// logger.debug(PathUtil.getHomePath());
		String openoffice = ContextHolder.getSystemProperties().getString(
				"mdf.openoffice.path");
		// "C:/Program Files (x86)/OpenOffice.org 3/program/";
		logger.debug("openoffice==" + openoffice);
		String b = "socket,host=127.0.0.1,port=8100;urp;";
		String a = openoffice + "/soffice -headless -accept=" + b
				+ " -nofirststartwizard";
		logger.debug("a==" + a);
		
		try {
				pro = Runtime.getRuntime().exec(a);
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug("**startOpenOffic()**openOffice启动失败****");
			throw e;
		}
		
		
		logger.debug("a==openOffic服务已启动" + a);
	}
	
	public DocConverter(String fileString) {
		ini(fileString);
	}

	/*
	 * 重新设置 file
	 * 
	 * @param fileString
	 */
	public void setFile(String fileString) {
		ini(fileString);
	}

	/*
	 * 初始化
	 * 
	 * @param fileString
	 */
	private void ini(String fileString) {
		this.fileString = fileString;
		fileName = fileString.substring(0, fileString.lastIndexOf("."));
		docFile = new File(fileString);
		pdfFile = new File(fileName + ".pdf");
		swfFile = new File(fileName + ".swf");
	}

	/*
	 * 转为PDF
	 * 
	 * @param file
	 */
	private void doc2pdf() throws Exception {

		if (docFile.exists()) {
			// if(!pdfFile.exists())
			// {
			// DocConverter.startOpenOffic();
			OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
			try {
				connection.connect();
				DocumentConverter converter = new OpenOfficeDocumentConverter(
						connection);
				converter.convert(docFile, pdfFile);
				// close the connection
				connection.disconnect();
				if(null!=pro){
					pro.destroy();
				}
				logger.debug("****pdf转换成功，PDF输出：" + pdfFile.getPath() + "****");
			} catch (java.net.ConnectException e) {
				e.printStackTrace();
				logger.debug("****swf转换异常，openoffice服务未启动！****");
				throw e;
			} catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
				e.printStackTrace();
				logger.debug("****swf转换器异常，读取转换文件失败****");
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			// }
			// else
			// {
			// logger.debug("****已经转换为pdf，不需要再进行转化****");
			// }
		} else {
			logger.debug("****swf转换器异常，需要转换的文档不存在，无法转换****");
		}
	}

	/*
	 * 转换成swf
	 */
	private boolean pdf2swf() throws Exception {
		Runtime r = Runtime.getRuntime();
		// if(!swfFile.exists())
		// {
		if (pdfFile.exists()) {
			if (environment == 1)// windows环境处理
				{try {
					String swfpath = ContextHolder.getSystemProperties()
							.getString("mdf.uploader.swftools");// "d:/doworkspace/prc/WebContent/js/SWFTools/";////"//
					Process p = r.exec(swfpath + "/pdf2swf.exe "
							+ pdfFile.getPath() + " -o " + swfFile.getPath()
							+ " -f -T 9 -s storeallcharacters ");
					System.out.print(loadStream(p.getInputStream()));
					System.err.print(loadStream(p.getErrorStream()));
					System.err.println("****swf转换成功，文件输出：" + swfFile.getPath()
							+ "****");
					// if(pdfFile.exists())
					// {
					// pdfFile.delete();
					// }
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			} else if (environment == 2)// linux环境处理
			{
				try {
					Process p = r.exec("/pdf2swf " + pdfFile.getPath() + " -o "
									+ swfFile.getPath()
									+ " -T 9 -s storeallcharacters");
					System.out.print(loadStream(p.getInputStream()));
					System.err.print(loadStream(p.getErrorStream()));
					System.err.println("****swf转换成功，文件输出：" + swfFile.getPath()
							+ "****");
					// if(pdfFile.exists())
					// {
					// pdfFile.delete();
					// }
					return true;
				} catch (Exception e) {

					e.printStackTrace();
					throw e;
				}
			}
		} else {
			logger.debug("****pdf不存在，无法转换****");
			return false;
		}
		// }
		// else {
		// logger.debug("****swf已存在不需要转换****");
		// return true;
		// }
		return false;
	}

	static String loadStream(InputStream in) throws IOException {
		int ptr = 0;
		in = new BufferedInputStream(in);
		StringBuffer buffer = new StringBuffer();

		while ((ptr = in.read()) != -1) {
			buffer.append((char) ptr);
		}
		return buffer.toString();
	}

	/*
	 * 转换主方法
	 */
	public boolean conver() {
		boolean flag;
		// if(swfFile.exists())
		// {
		// logger.debug("****swf转换器开始工作，该文件已经转换为swf****");
		// return true;
		// }

		if (environment == 1) {
			logger.debug("****swf转换器开始工作，当前设置运行环境windows****");
		} else {
			logger.debug("****swf转换器开始工作，当前设置运行环境linux****");
		}

		try {

			String type = fileString.substring(fileString.lastIndexOf("."),
					fileString.length());
			if (".doc".equalsIgnoreCase(type) || ".docx".equalsIgnoreCase(type)) {
				doc2pdf();
				flag = pdf2swf();
			} else {
				flag = pdf2swf();
			}

		} catch (Exception e) {
			// TODO: Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		if (swfFile.exists() || flag) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 返回文件路径
	 * 
	 * @param s
	 */
	public String getswfPath() {
		if (swfFile.exists()) {
			String tempString = swfFile.getPath();
			tempString = tempString.replaceAll("\\\\", "/");
			return tempString;
		} else {
			return "";
		}
	}

	/*
	 * 设置输出路径
	 */
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		if (!outputPath.equals("")) {
			String realName = fileName.substring(fileName.lastIndexOf("/"),
					fileName.lastIndexOf("."));
			if (outputPath.charAt(outputPath.length()) == '/') {
				swfFile = new File(outputPath + realName + ".swf");
			} else {
				swfFile = new File(outputPath + realName + ".swf");
			}
		}
	}

	public static void main(String s[]) {
		DocConverter d = new DocConverter("D:/pxg.pdf");
		d.conver();
	}
}