package com.innofi.framework.utils.file;

import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * 文件处理工具类
 * Copyright (c) 2010 正信岳铭信息技术有限公司
 * all rights reserved.
 * author: jack.liu.innofi.com.cn
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public class FileUtil {

    private static Logger _log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 将源目录下的内容拷贝到目标目录下
     *
     * @param sourceDirName      源目录路径名称
     * @param destinationDirName 目标目录路径名称
     * @throws java.io.IOException
     */
    public static void copyDirectory(String sourceDirName, String destinationDirName) throws IOException {
        copyDirectory(new File(sourceDirName), new File(destinationDirName));
    }

    /**
     * 将源目录下的内容拷贝到目标目录下
     *
     * @param source      源目录对象
     * @param destination 目标目录对象
     * @throws java.io.IOException
     */
    public static void copyDirectory(File source, File destination) throws IOException {
        if ((source.exists()) && (source.isDirectory())) {
            if (!(destination.exists())) {
                destination.mkdirs();
            }

            File[] fileArray = source.listFiles();

            for (int i = 0; i < fileArray.length; ++i)
                if (fileArray[i].isDirectory()) {
                    copyDirectory(fileArray[i], new File(destination.getPath() + File.separator + fileArray[i].getName()));
                } else {
                    copyFile(fileArray[i], new File(destination.getPath() + File.separator + fileArray[i].getName()));
                }
        }
    }

    /**
     * 拷贝源文件到目标文件
     *
     * @param source      源文件路径名称
     * @param destination 目标文件路径名称
     * @throws java.io.IOException
     */
    public static void copyFile(String source, String destination) throws IOException {
        copyFile(source, destination, false);
    }

    /**
     * 拷贝源文件到目标文件
     *
     * @param source      源文件路径名称
     * @param destination 目标文件路径名称
     * @param lazy        若lazy为true，则先判断若源文件与目标文件内容一致则不再进行拷贝；若Lazy为false，则全盘拷贝。
     * @throws java.io.IOException
     */
    public static void copyFile(String source, String destination, boolean lazy) throws IOException {
        copyFile(new File(source), new File(destination), lazy);
    }

    /**
     * 拷贝源文件到目标文件
     *
     * @param source      源文件对象
     * @param destination 目标文件对象
     * @throws java.io.IOException
     */
    public static void copyFile(File source, File destination) throws IOException {
        copyFile(source, destination, false);
    }

    /**
     * 拷贝源文件到目标文件
     *
     * @param source      源文件对象
     * @param destination 目标文件对象
     * @param lazy        若lazy为true，先判断若源文件与目标文件内容一致则不再进行拷贝；若Lazy为false，则不进行比较。
     * @throws java.io.IOException
     */
    public static void copyFile(File source, File destination, boolean lazy) throws IOException {

        if (!(source.exists())) {
            ConsoleUtil.info("复制源文件【" + source.getAbsolutePath() + "】已不存在!");
            return;
        }

        if (lazy) {
            String oldContent = read(source);
            String newContent = read(destination);
            if ((oldContent == null) || (!(oldContent.equals(newContent)))) {
                copyFile(source, destination, false);
            }
        } else {
            if ((destination.getParentFile() != null) && (!(destination.getParentFile().exists()))) {
                destination.getParentFile().mkdirs();
            }
            FileChannel srcChannel = null;
            FileChannel dstChannel = null;
            try {
                ConsoleUtil.info("准备开始复制文件：源文件信息【路径:" + source.getAbsolutePath() + "大小:" + (source.length() / 1024) + "KB】");
                srcChannel = new FileInputStream(source).getChannel();
                dstChannel = new FileOutputStream(destination).getChannel();
                dstChannel.transferFrom(srcChannel, 0L, srcChannel.size());
                ConsoleUtil.info("文件复制成功：目标文件信息【路径:" + destination.getAbsolutePath() + "大小:" + (destination.length() / 1024) + "KB】");
            } finally {
                if (srcChannel != null) {
                    try {
                        srcChannel.close();
                    } catch (IOException e) {
                    }
                }
                if (dstChannel != null) {
                    try {
                        dstChannel.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    /**
     * 删除指定文件
     *
     * @param file      要被删除的文件路径
     * @param printInfo true打印删除文件信息,false不打印
     * @return 若删除成果返回true, 否则返回false
     */
    public static boolean delete(String file, boolean printInfo) {
        return delete(new File(file), printInfo);
    }

    /**
     * 删除指定文件
     *
     * @param file      要被删除的文件对象
     * @param printInfo true打印删除文件信息,false不打印
     * @return 若删除成果返回true, 否则返回false
     */
    public static boolean delete(File file, boolean printInfo) {
        if (file.exists()) {
            if (printInfo) {
                System.out.println("删除文件：源文件信息【路径:" + file.getAbsolutePath() + "大小:" + (file.length() / 1024) + "KB】");
            }
            return file.delete();
        }
        return false;
    }

    /**
     * 删除指定目录，包括其下所有文件以及子目录
     *
     * @param directory 要删除的目录路径
     */
    public static void delTree(String directory, boolean printInfo) {
        delTree(new File(directory), printInfo);
    }

    /**
     * 删除指定目录，包括其下所有文件以及子目录
     *
     * @param directory 要删除的目录对象
     */
    public static void delTree(File directory, boolean printInfo) {
        if ((directory.exists()) && (directory.isDirectory())) {
            File[] fileArray = directory.listFiles();

            for (int i = 0; i < fileArray.length; ++i) {
                if (fileArray[i].isDirectory()) {
                    delTree(fileArray[i], printInfo);
                } else {
                    delete(fileArray[i], printInfo);
                }
            }
            delete(directory, printInfo);
        }
    }

    /**
     * 删除指定目录下，Filter中指定文件
     *
     * @param directory  目标目录
     * @param fileFilter 文件过滤器
     */
    public static void delTree(File directory, FileFilter fileFilter, boolean printInfo) {
        if ((directory.exists()) && (directory.isDirectory()) && fileFilter != null) {
            File[] fileArray = directory.listFiles(fileFilter);
            for (int i = 0; i < fileArray.length; ++i) {
                if (fileArray[i].isDirectory()) {
                    delTree(fileArray[i], fileFilter, printInfo);
                } else {
                    delete(fileArray[i], printInfo);
                }
            }
        }
    }

    /**
     * 删除指定目录下，Filter中指定文件
     *
     * @param directory  目标目录
     * @param fileFilter 文件过滤器
     * @param printInfo  是否打印删除信息
     */
    public static void delTree(String directory, FileFilter fileFilter, boolean printInfo) {
        delTree(new File(directory), fileFilter, printInfo);
    }

    /**
     * 读取文件内容，记录在byte数组中
     *
     * @param file 要读取的文件对象
     * @return 以byte数组形式返回读取的文件内容
     * @throws java.io.IOException
     */
    public static byte[] getBytes(File file) throws IOException {
        if ((file == null) || (!(file.exists()))) {
            return null;
        }

        FileInputStream in = new FileInputStream(file);

        byte[] bytes = getBytes(in);

        in.close();

        return bytes;
    }

    /**
     * 从指定的输入流中读取内容，记录在byte数组中
     *
     * @param in 输入流
     * @return 以byte数组形式返回读取的输入流内容
     * @throws java.io.IOException
     */
    public static byte[] getBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int c = in.read();
        while (c != -1) {
            out.write(c);
            c = in.read();
        }
        out.close();
        return out.toByteArray();
    }

    /**
     * 取得指定文件的存放目录路径
     *
     * @param fullFileName 指定文件的全路径
     * @return 文件的存放目录路径
     */
    public static String getPath(String fullFileName) {
        int pos = fullFileName.lastIndexOf("/");

        if (pos == -1) {
            pos = fullFileName.lastIndexOf("\\");
        }

        String shortFileName = fullFileName.substring(0, pos);

        if (Validator.isNull(shortFileName)) {
            return "/";
        }

        return shortFileName;
    }

    /**
     * 取得指定文件的纯文件名（不带路径的）
     *
     * @param fullFileName 指定文件的全路径
     * @return 指定文件的纯文件名
     */
    public static String getShortFileName(String fullFileName) {
        int pos = fullFileName.lastIndexOf("/");

        if (pos == -1) {
            pos = fullFileName.lastIndexOf("\\");
        }

        String shortFileName = fullFileName.substring(pos + 1, fullFileName.length());

        return shortFileName;
    }

    /**
     * 判断指定文件路径下的文件是否存在
     *
     * @param fileName 指定文件路径
     * @return 若存在返回true，否则返回false
     * @throws java.io.IOException
     */
    public static boolean exists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * 取得指定目录下的文件目录名称
     *
     * @param fileName 指定文件目录名称
     * @return 文件目录名称数组
     * @throws java.io.IOException
     */
    public static String[] listDirNames(String fileName) throws IOException {
        return listDirNames(new File(fileName));
    }

    /**
     * 取得指定目录下的目录名称
     *
     * @param file 指定文件目录对象
     * @return 文件目录名称数组
     * @throws java.io.IOException
     */
    public static String[] listDirNames(File file) throws IOException {
        List dirs = new ArrayList();

        File[] fileArray = file.listFiles();

        for (int i = 0; i < fileArray.length; ++i) {
            if (fileArray[i].isDirectory()) {
                dirs.add(fileArray[i].getName());
            }
        }

        return ((String[]) dirs.toArray(new String[0]));
    }

    /**
     * 取得指定目录下的目录名称
     *
     * @param file 指定文件目录对象
     * @return 文件目录数组
     * @throws java.io.IOException
     */
    public static File[] listDirs(String file) throws IOException {
        return listDirs(new File(file));
    }

    /**
     * 取得指定目录下的目录名称
     *
     * @param file 指定文件目录对象
     * @return 文件目录数组
     * @throws java.io.IOException
     */
    public static File[] listDirs(File file) throws IOException {
        List<File> dirs = new ArrayList<File>();

        File[] fileArray = file.listFiles();

        for (int i = 0; i < fileArray.length; ++i) {
            if (fileArray[i].isDirectory()) {
                dirs.add(fileArray[i]);
            }
        }

        return (dirs.toArray(new File[0]));
    }

    /**
     * 取得指定目录下的文件名称
     *
     * @param fileName 指定目录名称
     * @return 文件名称数组
     * @throws java.io.IOException
     */
    public static String[] listFileNames(String fileName) throws IOException {
        if (Validator.isNull(fileName)) {
            return new String[0];
        }
        return listFileNames(new File(fileName));
    }

    /**
     * 取得指定目录下的文件对象
     *
     * @param file 指定目录对象
     * @return 文件名称数组
     * @throws java.io.IOException
     */
    public static String[] listFileNames(File file) throws IOException {
        List files = new ArrayList();

        File[] fileArray = file.listFiles();

        for (int i = 0; (fileArray != null) && (i < fileArray.length); ++i) {
            if (fileArray[i].isFile()) {
                files.add(fileArray[i].getName());
            }
        }

        return ((String[]) files.toArray(new String[0]));
    }

    /**
     * 取得指定目录下的文件对象
     *
     * @param file 指定目录对象
     * @return 文件数组
     * @throws java.io.IOException
     */
    public static File[] listFiles(String file) throws IOException {
        return listFiles(new File(file));
    }

    /**
     * 取得指定目录下的文件对象
     *
     * @param file 指定目录对象
     * @return 文件数组
     * @throws java.io.IOException
     */
    public static File[] listFiles(File file) throws IOException {
        List<File> files = new ArrayList();

        File[] fileArray = file.listFiles();

        for (int i = 0; (fileArray != null) && (i < fileArray.length); ++i) {
            if (fileArray[i].isFile()) {
                files.add(fileArray[i]);
            }
        }

        return (files.toArray(new File[0]));
    }

    /**
     * 在指定路径下建立子目录
     *
     * @param pathName 建立路径
     */
    public static void mkdirs(String pathName) {
        File file = new File(pathName);
        file.mkdirs();
    }

    /**
     * 将源文件，拷贝到目标文件
     *
     * @param sourceFileName      源文件路径名
     * @param destinationFileName 目标文件路径名
     * @return 拷贝结果成功返回true, 否则返回false
     */
    public static boolean move(String sourceFileName, String destinationFileName) {
        return move(new File(sourceFileName), new File(destinationFileName));
    }

    /**
     * 将源文件，拷贝到目标文件
     *
     * @param source      源文件对象
     * @param destination 目标文件对象
     * @return 拷贝结果成功返回true, 否则返回false
     */
    public static boolean move(File source, File destination) {
        if (!(source.exists())) {
            return false;
        }
        destination.delete();
        return source.renameTo(destination);
    }


    /**
     * 读取文件内容，以字符串形式返回
     *
     * @param fileName 文件路径名
     * @return 文件内容
     * @throws java.io.IOException
     */
    public static String read(String fileName) throws IOException {
        return read(new File(fileName));
    }

    /**
     * 读取文件内容，以字符串形式返回
     *
     * @param file 要读取的文件
     * @return 文件内容
     * @throws java.io.IOException
     */
    public static String read(File file) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
        } finally {
        	if(br!=null)
            br.close();
        }
        return sb.toString().trim();
    }

    /**
     * 读取文件内容，以字符串形式返回
     *
     * @param filePath 要读取的文件路径
     * @param encoding 文件编码
     * @return 文件内容
     * @throws java.io.IOException
     */
    public static String read(String filePath, String encoding) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
        } finally {
            br.close();
        }
        return sb.toString().trim();
    }

    /**
     * 将文件数组按照名称排序，文件目录在前，文件在后
     *
     * @param files 文件数组
     * @return 重新组织的文件数组
     */
    public static File[] sortFiles(File[] files) {
        Arrays.sort(files, new FileComparator());

        List directoryList = new ArrayList();
        List fileList = new ArrayList();

        for (int i = 0; i < files.length; ++i) {
            if (files[i].isDirectory())
                directoryList.add(files[i]);
            else {
                fileList.add(files[i]);
            }
        }

        directoryList.addAll(fileList);

        return ((File[]) directoryList.toArray(new File[0]));
    }

    /**
     * 将文件路径中的‘\\'替换为'/'
     *
     * @param fileName 文件路径
     * @return 替换后的文件路径
     */
    public static String replaceSeparator(String fileName) {
        return StringUtil.replace(fileName, '\\', "/");
    }

    /**
     * 将Reader流中的文件读出，逐行放入List中
     *
     * @param reader 文件输入流
     * @return 逐行读取出的文件内容
     * @throws java.io.IOException
     */
    public static List toList(Reader reader) throws IOException {
        List list = new ArrayList();
        BufferedReader br = new BufferedReader(reader);
        try {
            String line;

            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } finally {
            br.close();
        }

        return list;
    }

    /**
     * 将指定文件内容读出，逐行放入List中
     *
     * @param fileName 指定文件的路径名称
     * @return 逐行读取出的文件内容
     * @throws java.io.IOException
     */
    public static List toList(String fileName) throws IOException {
        return toList(new FileReader(fileName));
    }

    /**
     * 将文件内容转换为Properties对象
     *
     * @param fis 文件输入流
     * @return Properties对象
     */
    public static Properties toProperties(FileInputStream fis) throws Exception {
        Properties props = new Properties();
        try {
            props.load(fis);
        } finally {
            fis.close();
        }
        return props;
    }

    /**
     * 将文件内容转换为Properties对象
     *
     * @param fileName 文件名称
     * @return Properties对象
     */
    public static Properties toProperties(String fileName) throws Exception {
        return toProperties(new FileInputStream(fileName));
    }

    /**
     * 将输入流内容写入目标文件
     *
     * @param file        文件存放路径及名称
     * @param inputStream
     * @throws java.io.IOException
     */
    public static void write(File file, FileInputStream inputStream) {
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        try {
            srcChannel = inputStream.getChannel();
            dstChannel = new FileOutputStream(file).getChannel();
            dstChannel.transferFrom(srcChannel, 0L, srcChannel.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (srcChannel != null) {
                try {
                    srcChannel.close();
                } catch (IOException e) {
                }
            }
            if (dstChannel != null) {
                try {
                    dstChannel.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 将指定字符串写入文件对象中
     *
     * @param fileName 文件存放路径及名称
     * @param s        要写入内容
     * @throws java.io.IOException
     */
    public static void write(String fileName, String s) throws IOException {
        write(new File(fileName), s);
    }


    /**
     * 将指定字符串写入文件对象中
     *
     * @param pathName 存放文件目录路径
     * @param fileName 文件名称
     * @param s        要写入内容
     * @throws java.io.IOException
     */
    public static void write(String pathName, String fileName, String s) throws IOException {
        write(new File(pathName, fileName), s);
    }

    /**
     * 将字符串写入指定文件中
     *
     * @param file 要写入的文件对象
     * @param s    存放文件内容的字符串
     * @throws java.io.IOException
     */
    public static void write(File file, String s) throws IOException {
        if (file.getParent() != null) {
            mkdirs(file.getParent());
        }

        if (file.exists()) {
            String content = read(file);
            if (content.equals(s)) {
                return;
            }
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        try {
            bw.write(s);
            bw.flush();
        } finally {
            bw.close();
        }
    }

    /**
     * 将指定byte数组写入指定文件中
     *
     * @param fileName  要写入的文件路径名称
     * @param byteArray 存放文件内容的byte数组
     * @throws java.io.IOException
     */
    public static void write(String fileName, byte[] byteArray) throws IOException {
        write(new File(fileName), byteArray);
    }

    /**
     * 将指定byte数组写入指定文件中
     *
     * @param file      要写入的文件对象
     * @param byteArray 存放文件内容的byte数组
     * @throws java.io.IOException
     */
    public static void write(File file, byte[] byteArray) throws IOException {
        if (file.getParent() != null) {
            mkdirs(file.getParent());
        }
        FileOutputStream fos = new FileOutputStream(file);
        try {
            fos.write(byteArray);
            fos.close();
        } finally {
            fos.close();
        }
    }


    /**
     * 加载指定文件中的内容
     *
     * @param source 文件所在路径
     * @return 该文件对象的内容
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static String loadlingFileContents(String source) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(source));//读取源文件
        StringBuffer contentBuffer = new StringBuffer();
        synchronized (source) {
            String content = fileReader.readLine();
            while (content != null) {
                contentBuffer.append(content);
                content = fileReader.readLine();
            }
        }
        try {
            fileReader.close();
        } catch (Exception e) {
            if (fileReader != null) fileReader.close();
        }
        return contentBuffer.toString();
    }

    /**
     * 加载指定文件中的内容
     *
     * @param source File类型文件对象
     * @return 该文件对象的内容已List结构返回
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static List loadlingFileContentsForList(String source) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(source));//读取源文件
        List contentList = new ArrayList();
        synchronized (source) {
            String content = fileReader.readLine();
            while (content != null) {
                contentList.add(content + "\n");
                content = fileReader.readLine();
            }
        }
        try {
            fileReader.close();
        } catch (Exception e) {
            if (fileReader != null) fileReader.close();
        }
        return contentList;
    }


    /**
     * 根据文件实例递归查找指定扩展名称文件方法
     *
     * @param file          文件实例
     * @param extFixStr     文件扩展名，多个扩展名使用，分隔
     * @param containHidden true包含隐藏文件,false不包含隐藏文件
     * @throws java.io.IOException
     */
    public static List recursionLoadingFiles(File file, String extFixStr, boolean containHidden) throws IOException {
        final boolean flg = containHidden;
        String[] extFixArray = null;
        if (Validator.isNotNull(extFixStr)) {
            extFixArray = StringUtil.split(extFixStr, ",");
        }
        List dirList = new LinkedList();//目录列表
        List fileList = new ArrayList();//文件列表

        if (file.isDirectory()) {
            dirList.add(file);
            while (dirList.size() != 0) {
                File dir = (File) dirList.remove(0);
                File[] filesArray = dir.listFiles(new FileFilter() { //获得指定文件实例下非隐藏的目录和文件，；
                    public boolean accept(File f) {
                        if (f.isHidden()) return flg;
                        return true;
                    }
                });

                for (int i = 0; i < filesArray.length; i++) {
                    File tmpFile = filesArray[i];
                    if (tmpFile.isFile()) {
                        if (Validator.isNull(extFixStr)) {
                            fileList.add(tmpFile);
                        } else {
                            for (int j = 0; j < extFixArray.length; j++) {
                                String extFix = extFixArray[j];
                                if (tmpFile.getName().endsWith(extFix)) fileList.add(tmpFile);
                            }
                        }
                    } else {
                        dirList.add(tmpFile);
                    }
                }
            }
        }

        return fileList;
    }

    /**
     * 将指定文件打包成zip文件
     *
     * @param fileList      文件对象列表
     * @param zipFilePath   zip文件全路径
     * @param containHidden 是否包含隐藏文件
     * @return 返回文件大小
     */
    public static int packageZipFile(List fileList, String zipFilePath, boolean containHidden) throws IOException {
        int fileSize = 0;
        ZipOutputStream zos;
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) zipFile.createNewFile();
        zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zos.setEncoding("GBK");
        for (int i = 0; i < fileList.size(); i++) {
            File f = (File) fileList.get(i);
            fileSize += zip(zos, f, f.getName(), containHidden);
        }
        zos.close();
        return fileSize;
    }

    private static int zip(ZipOutputStream out, File f, String base, boolean contrainHidden) throws IOException {
        int fileSize = 0;
        final boolean flg = contrainHidden;
        if (f.isDirectory()) {
            File[] fl = f.listFiles(new FileFilter() { //获得指定文件实例下非隐藏的目录和文件，；
                public boolean accept(File f) {
                    if (f.isHidden()) return flg;
                    return true;
                }
            });
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                fileSize += zip(out, fl[i], base + fl[i].getName(), contrainHidden);
            }
        } else {
            ZipEntry ze = new ZipEntry(base);
            ze.setSize(f.length());
            ze.setTime(f.lastModified());
            fileSize += f.length();
            out.putNextEntry(ze);
            InputStream in = new BufferedInputStream(new FileInputStream(f));
            int bufSize = 1024 * 8;
            byte[] buf = new byte[bufSize];
            int b = -1;
            while ((b = in.read(buf, 0, buf.length)) != -1) {
                out.write(buf,0,b);
            }
            in.close();
        }
        return fileSize;
    }

    /**
     * 该方将两个路径，返回适用windows、unix拼接后的路径
     *
     * @param dir      文件目录
     * @param fileName 文件名称
     * @return
     */
    public static String joinPath(String dir, String fileName) {
        if (dir.indexOf("\\") >= 1) StringUtil.replace(dir, "\\", "/");
        if (dir.lastIndexOf("/") == dir.length() - 1) return dir + fileName;
        return dir + "/" + fileName;
    }

    /**
     * 获得文件的扩展名
     *
     * @param file 文件对象
     */
    public static String getFileExtName(File file) {
        return getFileExtName(file.getAbsolutePath());
    }

    /**
     * 获得文件的扩展名
     *
     * @param fileName 文件完成名称
     * @return 文件扩展名
     */
    public static String getFileExtName(String fileName) {
        if (fileName.indexOf(".") == -1)
            throw new RuntimeException("请传入完整的文件名称，传入的文件名【" + fileName + "】不包含【.】,无法获取文件扩展名!");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 删除指定目录下的svn文件
     *
     * @param filePath
     */
    public static void deleteSvn(String filePath) {
        File f = new File(filePath);
        File[] filesArray = f.listFiles();
        for (int i = 0; i < filesArray.length; i++) {
            File ff = filesArray[i];
            if (ff.isDirectory() && ff.getName().indexOf("svn") > -1 && ff.isHidden()) delTree(ff, false);
            else if (ff.isDirectory()) deleteSvn(ff.getPath());
        }
    }


    /**
     * 删除svn文件
     *
     * @param args
     */
    public static void main(String args[]) throws Exception {
        String filePath = "C:\\Users\\jack.liu\\Desktop\\2014.08.15bj_YL.zip";
        unzip(filePath,"e:\\2014.08.15bj_YL");
    }


    /**
     * 解压zip格式的压缩文件到当前文件夹
     *
     * @param zipFileName
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static synchronized void unzipFile(String zipFileName) throws Exception {
        try {
            File f = new File(zipFileName);
            ZipFile zipFile = new ZipFile(zipFileName,"GBK");
            if ((!f.exists()) && (f.length() <= 0)) {
                throw new Exception("要解压的文件不存在!");
            }
            System.out.println(zipFileName + " was uncompressing....");
            String strPath, gbkPath, strtemp;
            File tempFile = new File(f.getParent());
            strPath = tempFile.getAbsolutePath();
            java.util.Enumeration e = zipFile.getEntries();
            while (e.hasMoreElements()) {
                org.apache.tools.zip.ZipEntry zipEnt = (ZipEntry) e.nextElement();
                gbkPath = zipEnt.getName();
                System.out.println("uncomress:" + zipEnt.getName());
                if (zipEnt.isDirectory()) {
                    strtemp = strPath + "/" + gbkPath;
                    File dir = new File(strtemp);
                    dir.mkdirs();
                    continue;
                } else {
                    // 读写文件
                    InputStream is = zipFile.getInputStream(zipEnt);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    gbkPath = zipEnt.getName();
                    strtemp = strPath + "/" + gbkPath;

                    // 建目录
                    String strsubdir = gbkPath;
                    for (int i = 0; i < strsubdir.length(); i++) {
                        if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
                            String temp = strPath + "/"
                                    + strsubdir.substring(0, i);
                            File subdir = new File(temp);
                            if (!subdir.exists())
                                subdir.mkdir();
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(strtemp);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int c;
                    while ((c = bis.read()) != -1) {
                        bos.write((byte) c);
                    }
                    bos.close();
                    fos.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 解压zip格式的压缩文件到指定位置
     *
     * @param zipFileName 压缩文件
     * @param extPlace    解压目录
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static synchronized void unzip(String zipFileName, String extPlace)
            throws Exception {
        try {
            (new File(extPlace)).mkdirs();
            File f = new File(zipFileName);
            ZipFile zipFile = new ZipFile(zipFileName,"GBK");
            if ((!f.exists()) && (f.length() <= 0)) {
                throw new Exception("要解压的文件不存在!");
            }
            String strPath, gbkPath, strtemp;
            File tempFile = new File(extPlace);
            strPath = tempFile.getAbsolutePath();
            java.util.Enumeration e = zipFile.getEntries();
            while (e.hasMoreElements()) {
                org.apache.tools.zip.ZipEntry zipEnt = (ZipEntry) e
                        .nextElement();
                gbkPath = zipEnt.getName();
                if (zipEnt.isDirectory()) {
                    strtemp = strPath + File.separator + gbkPath;
                    File dir = new File(strtemp);
                    dir.mkdirs();
                    continue;
                } else {
                    // 读写文件
                    InputStream is = zipFile.getInputStream(zipEnt);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    gbkPath = zipEnt.getName();
                    strtemp = strPath + File.separator + gbkPath;

                    // 建目录
                    String strsubdir = gbkPath;
                    for (int i = 0; i < strsubdir.length(); i++) {
                        if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
                            String temp = strPath + File.separator
                                    + strsubdir.substring(0, i);
                            File subdir = new File(temp);
                            if (!subdir.exists())
                                subdir.mkdir();
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(strtemp);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int c;
                    while ((c = bis.read()) != -1) {
                        bos.write((byte) c);
                    }
                    bos.close();
                    fos.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}