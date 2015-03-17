package com.innofi.framework.utils.codec;

/**
 * Encrypt编码工具类
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public class EncryptCode {

    private static byte charToByte(char c) {
        return (byte) Character.getNumericValue(c);
    }

    private static char byteToChar(byte b) {
        if (b < 10) {
            return (char) (b + 48);
        }

        return (char) (b + 55);
    }

    private static byte slideByte(byte b, int slide) {
        int i = (b + slide) % 36;
        if (i < 0) {
            return (byte) (i + 36);
        }

        return (byte) i;
    }

    private static byte unslideByte(byte b, int slide) {
        int i = (b - slide) % 36;
        if (i < 0) {
            return (byte) (i + 36);
        }

        return (byte) i;
    }

    /**
     * 将制定字符串进行编码
     *
     * @param plainText 指定字符串
     * @param Key1      编码的key
     * @param Key2      编码的key
     * @return 编码后的字符串
     */
    public static String encode(String plainText, int Key1, int Key2) {
        StringBuffer result = new StringBuffer();

        plainText = plainText.toUpperCase();

        int len = plainText.length();
        int[] vc1 = new int[len + 8];
        int check = 0;
        for (int i = 0; i < len; ++i) {
            vc1[i] = charToByte(plainText.charAt(i));

            check += vc1[i];
            check %= Integer.parseInt("7FFFFF00", 16);
        }

        String HexS = Integer.toHexString(check);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 8 - HexS.length(); ++i) {
            sb.append('0');
        }
        sb.append(HexS);
        String s = sb.toString();
        for (int i = 0; i < 8; ++i) {
            vc1[(len + i)] = charToByte(s.charAt(i));
        }

        len = vc1.length;
        int[] vc2 = new int[len * 2];
        for (int i = 0; i < len; ++i) {
            int r = Math.round((float) Math.random() * 35.0F);

            vc2[(i * 2)] = r;
            vc2[(i * 2 + 1)] = slideByte((byte) vc1[i], r);
        }

        len = vc2.length;
        for (int i = 0; i < len; ++i) {
            result.append(byteToChar(slideByte((byte) vc2[i], Math.round(Key2 * (i + 1) * Key1 / 3))));
        }

        return result.toString();
    }

    /**
     * 将制定字符串进行解码
     *
     * @param encodedText 指定字符串
     * @param Key1        解码的key
     * @param Key2        解码的key
     * @return 解码后的字符串
     */
    public static String decode(String encodedText, int Key1, int Key2) {
        StringBuffer result = new StringBuffer();

        int len = encodedText.length();
        int[] vc2 = new int[len];
        for (int i = 0; i < len; ++i) {
            vc2[i] = unslideByte((byte) (charToByte(encodedText.charAt(i)) - 1), Math.round(Key2 * (i + 1) * Key1 / 3));
        }

        len /= 2;
        int[] vc1 = new int[len];
        for (int i = 0; i < len; ++i) {
            int r = vc2[(i * 2)];

            vc1[i] = unslideByte((byte) vc2[(i * 2 + 1)], r);
        }

        len = vc1.length;
        int check = 0;
        for (int i = 0; i < len - 8; ++i) {
            result.append(byteToChar((byte) vc1[i]));

            check += vc1[i];
            check %= Integer.parseInt("7FFFFF00", 16);
        }

        String s = "";
        for (int i = len - 8; i < len; ++i) {
            s = s + byteToChar((byte) vc1[i]);
        }

        if (Integer.parseInt(s, 16) != check) {
            result.setLength(0);
        }
        return result.toString();
    }

    public static void main(String args[]) {
        String password = "8A7FF91859371FA9";

    }
}