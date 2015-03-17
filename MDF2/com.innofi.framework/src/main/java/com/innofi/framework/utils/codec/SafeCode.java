package com.innofi.framework.utils.codec;

/**
 * 安全码工具类
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public final class SafeCode {
    private static final int HEX_FLAG = 16;
    private static final int SIMPLE_CODE_LENGTH = 2;
    private static final int WIDE_CODE_LENGTH = 4;

    /**
     * 将给定字符串进行编码
     *
     * @param plainText 指定字符串
     * @return 安全码
     */
    public static String encode(String plainText) {
        if (plainText == null) {
            return "";
        }
        int maxSimpleChar = 255;
        int sz = plainText.length();
        StringBuffer encodedText = new StringBuffer(sz);
        for (int i = 0; i < sz; ++i) {
            String code;
            int j;
            int ch = plainText.charAt(i);
            if (ch > 255) {
                code = Integer.toString(ch, 16);
                encodedText.append('^');
                for (j = code.length(); j < 4; ++j) {
                    encodedText.append('0');
                }
                encodedText.append(code);
            } else if ((ch < 48) || ((ch > 57) && (ch < 65)) || ((ch > 90) && (ch < 97)) || (ch > 122)) {
                code = Integer.toString(ch, 16);
                encodedText.append('~');
                for (j = code.length(); j < 2; ++j) {
                    encodedText.append('0');
                }
                encodedText.append(code);
            } else {
                encodedText.append(plainText.charAt(i));
            }
        }
        return encodedText.toString();
    }


    /**
     * 将安全码解码成字符串
     *
     * @param encodedText 安全码
     * @return 字符串
     */
    public static String decode(String encodedText) {
        if (encodedText == null) {
            return "";
        }
        int sz = encodedText.length();
        StringBuffer plainText = new StringBuffer();
        for (int i = 0; i < sz; ++i) {
            String code;
            char ch = encodedText.charAt(i);
            switch (ch) {
                case '~':
                    code = encodedText.substring(i + 1, i + 4 - 1);
                    plainText.append((char) Integer.parseInt(code, 16));
                    i += 2;
                    break;
                case '^':
                    code = encodedText.substring(i + 1, i + 4 + 1);
                    plainText.append((char) Integer.parseInt(code, 16));
                    i += 4;
                    break;
                default:
                    plainText.append(ch);
            }
        }
        return plainText.toString();
    }

}