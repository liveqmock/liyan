package com.innofi.framework.utils.codec;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.UnsupportedEncodingException;

/**
 * Escape编码工具类
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public final class EscapeUtils {
    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }

    /**
     * 将制定字符串进行编码
     *
     * @param plainText 字符串
     * @return 编码后的字符串
     */
    public static String escape(String plainText) {
        if (plainText == null) {
            return "";
        }

        int sz = plainText.length();
        StringBuffer encodedText = new StringBuffer(sz);
        for (int i = 0; i < sz; ++i) {
            char ch = plainText.charAt(i);

            if (ch > 4095) {
                encodedText.append("%u").append(hex(ch));
            } else if (ch > 255) {
                encodedText.append("%u0").append(hex(ch));
            } else if (ch > '') {
                encodedText.append("%u00").append(hex(ch));
            } else if (((ch >= '0') && (ch <= '9')) || ((ch >= 'A') && (ch <= 'Z')) || ((ch >= 'a') && (ch <= 'z'))) {
                encodedText.append(ch);
            } else if (ch > '\15') {
                encodedText.append("%").append(hex(ch));
            } else {
                encodedText.append("%0").append(hex(ch));
            }
        }

        return encodedText.toString();
    }

    /**
     * 将制定字符串进行解码
     *
     * @param encodedText 字符串
     * @return 解码后的字符串
     */
    public static String unescape(String encodedText) {
        if (encodedText == null) {
            return "";
        }
        StringBuffer unicode = new StringBuffer(4);
        StringBuffer plainText = new StringBuffer();
        int sz = encodedText.length();
        boolean hadSlash = false;
        boolean inCode = false;
        boolean inUnicode = false;
        char lastSlash = '\0';

        for (int i = 0; i < sz; ++i) {
            char ch = encodedText.charAt(i);
            if (inCode) {
                if (hadSlash) {
                    hadSlash = false;
                    if (ch == 'u') {
                        inUnicode = true;
                    }

                } else {
                    unicode.append(ch);
                    if (unicode.length() != ((inUnicode) ? 4 : 2))
                        continue;
                    try {
                        int value = Integer.parseInt(unicode.toString(), 16);
                        plainText.append((char) value);
                        unicode.setLength(0);
                        inCode = false;
                        inUnicode = false;
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Unable to parse unicode value: " + unicode);
                    }

                }

            } else if ((ch == '%') || (ch == '$')) {
                hadSlash = true;
                lastSlash = ch;
                inCode = true;
            } else {
                plainText.append(ch);
            }
        }
        if (hadSlash) {
            plainText.append(lastSlash);
        }

        return plainText.toString();
    }

    /**
     * 将Xml进行编码
     *
     * @param s xml格式的字符串
     * @return 编码后的xml字符串
     * @throws java.io.UnsupportedEncodingException
     */
    public static String escapeXml(String s) throws UnsupportedEncodingException {
        return StringEscapeUtils.escapeXml(s);
    }

}