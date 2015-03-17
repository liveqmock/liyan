package com.innofi.framework.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;

import com.innofi.framework.spring.context.ContextHolder;

public class ControlFile {
    private static final Logger logger = LoggerFactory.getLogger(ControlFile.class);

    /**
     * 将BLOB写入文件。
     *
     * @param filePath 文件路径
     * @param fileId   附件id
     * @param blob     BLOB类型数据。
     * @throws SQLException
     * @throws IOException
     */
    public void createFileForBlob(String filePath, String fileId, String fileType, Blob blob) throws SQLException, IOException {

        String docFilePath = filePath + fileId + "." + fileType;
        InputStream ins = blob.getBinaryStream();
        //用文件模拟输出流
        File file = new File(docFilePath);
        OutputStream fout = new FileOutputStream(file);
        //下面将BLOB数据写入文件
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = ins.read(b)) != -1) {
            fout.write(b, 0, len);
        }
        //依次关闭
        fout.close();
        ins.close();
    }

    /**
     * 将BLOB转换成文件
     *
     * @param filePath 文件路径
     * @param blob     BLOB类型数据。
     * @throws SQLException
     * @throws IOException
     */
    public static void createFileForBlob(String filePath, Blob blob) throws SQLException, IOException {
        InputStream ins = blob.getBinaryStream();
        //用文件模拟输出流
        File file = new File(filePath);
        OutputStream fout = new FileOutputStream(file);
        //下面将BLOB数据写入文件
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = ins.read(b)) != -1) {
            fout.write(b, 0, len);
        }
        //依次关闭
        fout.close();
        ins.close();
    }

    /**
     * 判断文档类型是否需要转换为swf文件
     *
     * @param fileType
     * @return 真或假
     */
    public static Boolean isToSwf(String fileType) {
        String fileTypes = ContextHolder.getSystemProperties().getString("file.type.toswf");
        List<String> typeList = java.util.Arrays.asList(fileTypes.split(","));
        for (String fileTypeString : typeList) {
            if (fileTypeString.toLowerCase().equals(fileType.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过模块名称和文件名称创建目录的方法
     *
     * @param module
     * @param fileName
     * @return
     */
    public String createDir(String module, String fileName) {
        String uploadpath = ContextHolder.getSystemProperties().getString("file.uploadpath");
        String uploadModulePath = uploadpath + "/" + module;
        String filePath = uploadModulePath + "/" + fileName;
        //创建目录
        File dirRoot = new File(uploadpath);
        if (!dirRoot.exists())
            dirRoot.mkdirs();
        File dirModule = new File(uploadModulePath);
        if (!dirModule.exists())
            dirModule.mkdirs();
        return filePath;
    }

    public static String createDir(String filePath) {

        //创建目录
        File dirRoot = new File(filePath);
        if (!dirRoot.exists())
            dirRoot.mkdirs();

        return filePath;
    }

    /**
     * 通过文件名和文件路径删除文件。
     *
     * @param fileName
     * @param path
     */
    public static void deleteFile(String path) {

        File file = new File(path);
        if (file.exists()) {
            boolean d = file.delete();
            if (d) {
                System.out.println("删除成功！");
            } else {
                System.out.println("删除失败！");
            }
        }
    }

    /**
     * 通过文件路径获取word文档的内容。
     *
     * @param filePath
     * @return
     * @throws IOException
     * @throws XmlException
     * @throws OpenXML4JException
     */
    public static String getWordContent(String filePath) throws IOException, XmlException, OpenXML4JException {
        String content = "";
        String type = filePath.substring(filePath.lastIndexOf("."), filePath.length());
        if (".doc".equalsIgnoreCase(type)) {
            FileInputStream in = new FileInputStream(filePath);
            WordExtractor ex = new WordExtractor(in);
            content = ex.getText();
            in.close();
        }
        if (".docx".equalsIgnoreCase(type)) {
            OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
            POIXMLTextExtractor ex = new XWPFWordExtractor(opcPackage);
            content = ex.getText();
        }
        return content;
    }

    /**
     * 通过文件名称获取自己的书名。
     *
     * @param fileName
     * @return
     */
    public static String getSelfBooKName(String fileName) {
        String bookname = "";
        Pattern p = Pattern.compile("(《[^》]*》)");
        Matcher m = p.matcher(fileName);
        while (m.find()) {
            bookname = m.group().substring(1, m.group().length() - 1);
        }
        return bookname;
    }


    /**
     * 在文本中查找带书名号的内容。
     *
     * @param content
     * @return Map 返回带书名号的map
     */
    public static Map getBookName(String content) {
        List<String> listBookName = new ArrayList<String>();
        List<String> listJCBookName = new ArrayList<String>();
        Pattern p = Pattern.compile("(《[^》]*》)");
        Pattern p1 = Pattern.compile("(简称《[^》]*》)");
        Matcher m = p.matcher(content);
        int i = 0;
        while (m.find()) {
            String bookname = m.group().substring(1, m.group().length() - 1);
            String str1 = bookname.replaceAll(" ", "");
            listBookName.add(str1 + "#" + i);
            i++;
        }

        Matcher m1 = p1.matcher(content);
        String str2 = "";
        while (m1.find()) {
            String name = m1.group().substring(3, m1.group().length() - 1);
            str2 = name.replaceAll(" ", "");
        }
        List<String> newlistBookName = new ArrayList<String>();
        for (int j = 0; j < listBookName.size(); j++) {
            if (!listBookName.get(j).substring(0, listBookName.get(j).lastIndexOf("#")).equals(str2)) {
                newlistBookName.add(listBookName.get(j));
            }
        }
        Map map = new HashMap<String, Integer>();
        for (int l = 0; l < newlistBookName.size(); l++) {
            String name = newlistBookName.get(l).substring(0, newlistBookName.get(l).lastIndexOf("#"));
            String order = newlistBookName.get(l).substring(newlistBookName.get(l).lastIndexOf("#") + 1, newlistBookName.get(l).length());
            map.put(name, order);
        }
        return map;
    }

    /**
     * 读PDF文件，使用了pdfbox开源项目
     *
     * @param fileName
     */
    public static String getPDFContent(String filePath) {
        File file = new File(filePath);
        FileInputStream in = null;
        String result = "";
        try {
            in = new FileInputStream(filePath);
            // 新建一个PDF解析器对象   
            PDFParser parser = new PDFParser(in);
            // 对PDF文件进行解析   
            parser.parse();
            // 获取解析后得到的PDF文档对象   
            PDDocument pdfdocument = parser.getPDDocument();
            // 新建一个PDF文本剥离器   
            PDFTextStripper stripper = new PDFTextStripper();
            // 从PDF文档对象中剥离文本   
            result = stripper.getText(pdfdocument);

        } catch (Exception e) {
            logger.debug("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
        return result;
    }

    /**
     * @param @param  inputPath
     * @param @param  outputPath
     * @param @return 设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: renameFile
     * @Description: 对文件重命名
     */
    public static boolean renameFile(String inputPath, String outputPath) {
        File file1 = new File(inputPath);
        File file2 = new File(outputPath);
        boolean flag = file1.renameTo(file2);
        return flag;
    }

    public static String filePathByMonth(String module) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String moth = formatter.format(date);
        String filePath = ContextHolder.getSystemProperties().getString("file.uploadpath") + "/" + module + "/" + moth + "/";
        //创建目录
        File dirRoot = new File(filePath);
        if (!dirRoot.exists())
            dirRoot.mkdirs();

        return filePath;
    }

    /**
     * 删除指定文件夹以及文件夹下所有文件.
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹下所有文件.
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除指定类型以外的所有文件.
     *
     * @param path 文件绝对路径
     * @param type 不删除的文件类型
     * @return
     */
    public static void delAllFileExceptOneType(String path, String type) {

        File file = new File(path);
        if (!file.exists()) {
            logger.debug("没有可删除的文件!");
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                String fileType = temp.getName().substring(temp.getName().lastIndexOf(".") + 1, temp.getName().length());
                if (!type.equals(fileType)) {
                    temp.delete();
                    logger.debug("删除文件《" + temp.getName() + "》成功！");
                }
            }
        }
    }

    public static String ListToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                // 如果值是list类型则调用自己
                if (list.get(i) instanceof List) {
                    sb.append(ListToString((List<?>) list.get(i)));
                    sb.append(",");

                } else {
                    sb.append(list.get(i));
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }
}
