package com.innofi.framework.utils.encode;

import javax.swing.*;
import java.io.*;

/**
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2011-1-13
 * found time: 13:03:37
 */
public class EncodeTransform {
/*    public static void main(String[] args) throws IOException {
        FileUtil.copyDirectory("D:/workspace/bpde/codebuilder/javaSource","E:/trasformAfter");

    }*/

    public static void main(String[] args) {
        String path = null;
        String posfix = null;
        String oldEncoding = null;
        String localEncoding = null;
        do {
            path = JOptionPane.showInputDialog("请输入原文件所在的目录");
        } while (path == null);
        do {
            posfix = JOptionPane.showInputDialog("请输入原文件的后缀名");
        } while (posfix == null);
        do {
            oldEncoding = JOptionPane.showInputDialog("请输入原文件编码方式，比如GBK，UTF8");
        } while (oldEncoding == null);
        do {
            localEncoding = JOptionPane.showInputDialog("请输入本机默认编码方式，比如GBK，UTF8");
        } while (localEncoding == null);
        convert(new File(path), posfix, oldEncoding, localEncoding);
    }

    /**
     * 批量转换文件编码
     *
     * @param file        文件对象
     * @param posfix      转换文件后缀 java 、txt 、 sqldialect ...
     * @param oldEncoding 源文件编码
     * @param newEncoding 新编码
     */
    public static void convert(File file, final String posfix,
                               String oldEncoding, String newEncoding) {
        File[] fileName = file.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return true;
                }
                if (pathname.getName().endsWith(posfix)) {
                    return true;
                }
                return false;
            }
        });

        for (File ff : fileName) {
            if (ff.isDirectory()) {
                convert(ff, posfix, oldEncoding, newEncoding);
            } else {
                File temp = new File(ff.getName() + ",temp");
                fileIO(ff, oldEncoding, temp, newEncoding);
                ff.delete();
                fileIO(temp, newEncoding, ff, newEncoding);
                temp.delete();
            }
        }
    }

    public static void fileIO(File ff, String oldEncoding, File temp, String newEncoding) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            fis = new FileInputStream(ff);
            isr = new InputStreamReader(fis, oldEncoding);
            br = new BufferedReader(isr);

            fos = new FileOutputStream(temp);
            osw = new OutputStreamWriter(fos, newEncoding);
            PrintWriter pw = new PrintWriter(osw);

            String str = null;
            while ((str = br.readLine()) != null) {
                System.out.println(str);
                pw.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
            }
            if (bw != null) try {
                bw.close();
            } catch (IOException e) {
            }
            if (osw != null) try {
                osw.close();
            } catch (IOException e) {
            }
            if (isr != null) try {
                isr.close();
            } catch (IOException e) {
            }
            if (fos != null) try {
                fos.close();
            } catch (IOException e) {
            }
            if (fis != null) try {
                fis.close();
            } catch (IOException e) {
            }
        }
    }

}

