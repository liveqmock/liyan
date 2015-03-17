package com.innofi.framework.utils.rtf;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: gui
 * Date: 2010-1-21
 * Time: 10:02:22
 * To change this template use File | Settings | File Templates.
 */
public class RTFUtil {
    public RTFUtil() {
    }

    public static void main(String args[]) throws IOException {
        HashMap map = new HashMap();
        String[] atext = {"aa1", "aa2"};
        String[] btext = {"bb11", "bb22"};
        String[] ctext = {"cc11", "cc22"};
        String[] dtext = {"dd1", "dd2"};
        String[] etext = {"ee1", "ee2"};
        String[] ftext = {"项目1", "审计分项内容"};
        String[] gtext = {"测试目标", "测试目标"};
        String[] htext = {"项目1", "取证对象"};
        String[] itext = {"项目1", "样本/总体"};
        String[] jtext = {"项目1", "审计过程及结果"};
        String[] ktext = {"项目1", "审计取证索引"};
        String[] ltext = {"项目1", "备注"};
        String[] mtext = {"项目1", "附件"};
        String[] ntext = {"项目1", "专业小组组长复核意见"};
        String[] otext = {"项目1", "主审人复核意见"};
        String[] ptext = {"项目1", "审计组组长复核意见"};
        String[] qtext = {"2007/12/12", "2007/12/12"};
        String[] rtext = {"2007/12/12", "2007/12/12"};
        String[] stext = {"2007/12/12", "2007/12/12"};

        map.put("a", atext);
        map.put("x", btext);
        map.put("c", ctext);
        map.put("d", dtext);
        map.put("e", etext);
        map.put("f", ftext);
        map.put("g", gtext);
        map.put("h", htext);
        map.put("i", itext);
        map.put("j", jtext);
        map.put("k", ktext);
        map.put("l", ltext);
        map.put("m", mtext);
        map.put("n", ntext);
        map.put("o", otext);
        map.put("p", ptext);
        map.put("q", qtext);
        map.put("r", rtext);
        map.put("s", stext);

        File outFile = new File("D://ais//wordTemplate//test.doc");
        //File templateFile = new File("D://ais//wordTemplate//test.rtf");
        String templateFile = "D://ais//wordTemplate//test.rtf";
        createByTemplate(templateFile, map, outFile);
    }

    /**
     * 通过模版创建RTF文件<br>
     * 模版中的变量写法说明:<br>
     * 1、<$x$> x是变量,<$$>用于将变量括起来.<br>
     * 2、<$for[y,i]$> <$y[i]$> <$z[i]$> <$for[i]$> y,z都是变量(一维数组),i是循环变量,<$$>用于将变量括起来.<br>
     * 2、<$for[x,i,j]$> <$x[i,j]$> <$z[i,j]$> <$for[i,j]$>
     * x,z都是变量(二维数组),i,j是循环变量,<$$>用于将变量括起来.<br>
     *
     * @param templateFile 模板文件
     * @param map          HashMap {key=变量名,value=变量名(循环输出时,可以是数组)}
     * @param outFile      输出文件
     * @throws Exception
     */
    public static void createByTemplate(String templateFile, Map map, File outFile) throws IOException {
        // 模板文件的输入流
        InputStream in = null;
        // 缓存输出流
        ByteArrayOutputStream bos = null;
        try {
            //fis = new FileInputStream(templateFile);
            //System.out.println(templateFile);
            in = RTFUtil.class.getClassLoader().getResourceAsStream(templateFile);
            bos = new ByteArrayOutputStream();
            byte[] data = new byte[8096];
            int iRead = -1;
            while ((iRead = in.read(data)) != -1) {
                bos.write(data, 0, iRead);
                bos.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException("--- read template file error ---");
        } finally {
            if (in != null) {
                in.close();
            }
            bos.close();
        }
        // 输出文件的输出流
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outFile);
            // 替换其中的变量内容
            replaceText(bos.toByteArray(), fos, map, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("--- write file error ---");
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 替换文本
     *
     * @param all    byte[]
     * @param fos    OutputStream 输出流
     * @param map    HashMap 映射{key,value}
     * @param iIndex int
     * @param jIndex int
     * @throws Exception
     */
    private static void replaceText(byte all[], OutputStream fos, Map map, int iIndex, int jIndex) throws IOException {
        int iStart = 2;
        int iEnd = -2;
        boolean isPrint = true;
        for (int i = 0; i < all.length; i++) {

            if (i == all.length - 1) {
                iStart = i + 3;
                isPrint = true;
            } else if (all[i] == '<' && all[i + 1] == '$') {
                iStart = i + 2;
                i = i++;
                isPrint = true;
            } else if (all[i] == '$' && all[i + 1] == '>') {
                iEnd = i;
                i++;
            }

            if (isPrint) {
                fos.write(all, iEnd + 2, (iStart - 2) - (iEnd + 2));
                isPrint = false;
            }
            if (iEnd > iStart) {
                // 输出
                byte aTmp[] = new byte[iEnd - iStart];
                System.arraycopy(all, iStart, aTmp, 0, aTmp.length);
                String tmp = new String(aTmp);
                if (tmp.indexOf("if[") > -1) // 若是循环
                {
                    // if[abc] 或 if[abc(i)]
                    int rightBracket = tmp.indexOf("]");
                    int leftBracket = tmp.indexOf("(");
                    String fieldName = tmp.substring(tmp.indexOf("[") + 1,
                            rightBracket);
                    String varI = "";
                    if (leftBracket < rightBracket && leftBracket > -1) {
                        fieldName = tmp.substring(tmp.indexOf("[") + 1,
                                leftBracket);
                        varI = tmp.substring(leftBracket + 1, tmp.indexOf(")"));
                    }
                    String endStr = "<$if[" + fieldName;
                    if (!varI.equals(""))
                        endStr += "(" + varI + ")";
                    endStr += ".end]$>";
                    while (true) {
                        byte tt[] = new byte[endStr.length()];
                        System.arraycopy(all, i, tt, 0, tt.length);
                        if (new String(tt).equals(endStr)) {
                            byte aIfData[] = new byte[i - (iEnd + 2)];
                            System.arraycopy(all, (iEnd + 2), aIfData, 0,
                                    aIfData.length);
                            String tmpFieldName = fieldName;
                            String tmpValue = tmpFieldName;
                            if (map.get(tmpFieldName) != null) {
                                Object a = map.get(fieldName);
                                if (a instanceof String[]) {
                                    Object b[] = (Object[]) map.get(fieldName);
                                    if (!varI.equals(""))
                                        tmpValue = String.valueOf(b[iIndex]);
                                } else
                                    tmpValue = String.valueOf(a);
                            }
                            if (tmpValue.equalsIgnoreCase("true"))
                                replaceText(aIfData, fos, map, 0, 0);
                            i = i + endStr.length();
                            break;
                        }
                        i++;
                    }
                    iEnd = i - 2;
                    iStart = i + 2;
                } else if (tmp.indexOf("for[") > -1) // 若是循环
                {
                    String fieldName = tmp.substring(tmp.indexOf("[") + 1, tmp
                            .indexOf(","));
                    // System.out.println(fieldName);
                    String varName = tmp.substring(tmp.indexOf(",") + 1, tmp
                            .length() - 1);
                    String endStr = "<$for[" + varName + "]$>";
                    while (true) {
                        byte tt[] = new byte[endStr.length()];
                        //System.out.println("all:"+all + "-i:"+i + "-tt:" + tt + "-tt.length:" + tt.length);
                        System.arraycopy(all, i, tt, 0, tt.length);
                        if (new String(tt).equals(endStr)) {
                            byte aForData[] = new byte[i - (iEnd + 2)];
                            System.arraycopy(all, (iEnd + 2), aForData, 0,
                                    aForData.length);

                            // 递归

                            Object a[] = (Object[]) map.get(fieldName);
                            if (a != null) {
                                if (varName.split(",").length > 1)
                                    for (int j = 0; j < ((String[]) a[iIndex]).length; j++)
                                        replaceText(aForData, fos, map, iIndex,
                                                j);
                                else {
                                    // System.out.println("here");
                                    for (int j = 0; j < a.length; j++)
                                        replaceText(aForData, fos, map, j, 0);
                                }
                            } else {
                                replaceText(aForData, fos, map, 0, 0);
                                // System.out.println("there");
                            }
                            i = i + endStr.length();
                            break;
                        }
                        i++;
                    }
                    iEnd = i - 2;
                    iStart = i + 2;
                } else // 若是一般变量
                {
                    String fieldName = tmp;
                    String str = "";
                    if (fieldName.indexOf("[") > -1)
                        fieldName = fieldName.substring(0, fieldName
                                .indexOf("["));
                    if (map.get(fieldName) != null) {
                        if (tmp.indexOf("[") > -1) {
                            Object a[] = (Object[]) map.get(fieldName);
                            if (a[iIndex] instanceof String[]) {
                                str = String.valueOf(
                                        ((String[]) a[iIndex])[jIndex])
                                        .replaceAll("\\\\", "/");
                                if (str.indexOf("/par") != -1) {
                                    str = String
                                            .valueOf(((String[]) a[iIndex])[jIndex]);
                                }
                                fos.write(str.getBytes());
                            } else {
                                str = String.valueOf(a[iIndex]).replaceAll(
                                        "\\\\", "/");
                                if (str.indexOf("/par") != -1) {
                                    str = String.valueOf(a[iIndex]);
                                }

                                fos.write(str.getBytes());
                            }
                        } else {
                            str = String.valueOf(map.get(fieldName))
                                    .replaceAll("\\\\", "/");
                            if (str.indexOf("/par") != -1) {
                                str = String.valueOf(map.get(fieldName));
                            }

                            fos.write(str.getBytes());
                        }
                    } else {
                        str = ("<$" + tmp + "$>").replaceAll("\\\\", "/");
                        if (str.indexOf("/par") != -1) {
                            str = ("<$" + tmp + "$>");
                        }
                        fos.write(str.getBytes());
                    }
                    iStart = iEnd + 2;
                }
            }
        }
    }
}
