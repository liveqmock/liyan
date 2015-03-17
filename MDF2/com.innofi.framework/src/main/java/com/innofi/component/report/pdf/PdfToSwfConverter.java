/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.util.StringUtils;

import com.innofi.framework.spring.context.ContextHolder;

/**
 * 将给出的pdf文件利用pdftoswf工具转换成swf文件存放在操作系统的临时目录下以供使用
 *
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public class PdfToSwfConverter {

    private String pdfToSwfPath;
    private String xpdfPath;

    /**
     * 将给定的pdf文件转换为swf文件，同时将生成的文件存储在系统的临时目录当中，<br>
     * 最后返回包含swf文件名在内的生成的swf文件路径全名
     *
     * @param sourcePdf 要转换的pdf文件全名
     * @param targetSwf 转换后swf文件的存放路径
     * @return 转换后的swf文件全名
     * @throws Exception
     */
    public String convert(String sourcePdf, String targetSwf) throws Exception {
        String tmpDir = ContextHolder.getIdfTempFileStorePath();
        File tempFile = new File(tmpDir);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }

        if (!StringUtils.hasText(targetSwf)) {
            targetSwf = tmpDir + "/" + sourcePdf.substring(sourcePdf.lastIndexOf("/") + 1, sourcePdf.length() - 4) + ".swf";
        }

        Process process = null;
        File pdf = new File(sourcePdf);
        long pdfSize = pdf.length() / 1024 * 1024;//单位M
        if (this.isWindows()) {
            StringBuffer command = new StringBuffer(pdfToSwfPath + " -t " + sourcePdf+" -o " + targetSwf);
            command.append(" -s flashversion=9 ");
            if (pdfSize > 1) {//文件大于1M时，添加该参数增加转换效率
                //command.append("  -s poly2bitmap ");   //会引起显示格式错误
            }

            command.append(" -s languagedir=" + xpdfPath );
            System.out.println("**********************cmd:" + command);

            process = Runtime.getRuntime().exec(command.toString());
        } else {
            String[] cmd = null;
            if (pdfSize > 1) {
/*                cmd = new String[10];
                cmd[0] = pdfToSwfPath;
                cmd[1] = " " + sourcePdf + " ";
                cmd[2] = " -s";
                cmd[3] = " poly2bitmap";
                cmd[4] = " -s";
                cmd[5] = " languagedir=" + xpdfPath + "";
                cmd[6] = " -T";
                cmd[7] = " 9";
                cmd[8] = " -o";
                cmd[9] = targetSwf;*/
                cmd = new String[8];
                cmd[0] = pdfToSwfPath;
                cmd[1] = " " + sourcePdf + " ";
                cmd[2] = " -s";
                cmd[3] = " languagedir=" + xpdfPath + "";
                cmd[4] = " -T";
                cmd[5] = " 9";
                cmd[6] = " -o";
                cmd[7] = targetSwf;
            } else {
                cmd = new String[8];
                cmd[0] = pdfToSwfPath;
                cmd[1] = " " + sourcePdf + " ";
                cmd[2] = " -s";
                cmd[3] = " languagedir=" + xpdfPath + "";
                cmd[4] = " -T";
                cmd[5] = " 9";
                cmd[6] = " -o";
                cmd[7] = targetSwf;
            }
            process = Runtime.getRuntime().exec(cmd);
        }

        new DoOutput(process.getInputStream()).start();
        new DoOutput(process.getErrorStream()).start();
        process.waitFor();
        return targetSwf;
    }

    private boolean isWindows() {
        String p = System.getProperty("os.name");
        return p.toLowerCase().indexOf("windows") >= 0 ? true : false;
    }

    public String getPdfToSwfPath() {
        return pdfToSwfPath;
    }

    public void setPdfToSwfPath(String pdfToSwfPath) {
        this.pdfToSwfPath = pdfToSwfPath;
    }

    public String getXpdfPath() {
        return xpdfPath;
    }

    public void setXpdfPath(String xpdfPath) {
        this.xpdfPath = xpdfPath;
    }


    private class DoOutput extends Thread {
        public InputStream is;

        public DoOutput(InputStream is) {
            this.is = is;
        }

        public void run() {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    this.is));
            try {
                // 这里并没有对流的内容进行处理，只是读了一遍
                @SuppressWarnings("unused")
                String str = null;
                while ((str = br.readLine()) != null)
                    ;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
