package com.innofi.framework.utils.codec;

import java.io.*;

/**
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 * Base64编码、解码工具类
 */
public class Base64Code {

    /**
     * 将int型编码转换为char型字符
     *
     * @param sixbit int类型数字
     * @return 转换后char类型的字符
     */
    protected static char getChar(int sixbit) {
        if ((sixbit >= 0) && (sixbit <= 25)) {
            return (char) (65 + sixbit);
        }

        if ((sixbit >= 26) && (sixbit <= 51)) {
            return (char) (97 + sixbit - 26);
        }

        if ((sixbit >= 52) && (sixbit <= 61)) {
            return (char) (48 + sixbit - 52);
        }

        if (sixbit == 62) {
            return '+';
        }

        return ((sixbit != 63) ? '?' : '/');
    }

    /**
     * 将char型字符转换为int类型的编码
     *
     * @param c 字符      char类型字符
     * @return 数字编码   int类型编码
     */
    protected static int getValue(char c) {
        if ((c >= 'A') && (c <= 'Z')) {
            return (c - 'A');
        }

        if ((c >= 'a') && (c <= 'z')) {
            return (c - 'a' + 26);
        }

        if ((c >= '0') && (c <= '9')) {
            return (c - '0' + 52);
        }

        if (c == '+') {
            return 62;
        }

        if (c == '/') {
            return 63;
        }

        return ((c != '=') ? -1 : 0);
    }

    /**
     * 对byte字符数组用Base64方式进行编码
     *
     * @param raw 编码的byte数组
     * @return 编码后新的字符串
     */
    public static String encode(byte[] raw) {
        StringBuffer encoded = new StringBuffer();

        for (int i = 0; i < raw.length; i += 3) {
            encoded.append(encodeBlock(raw, i));
        }

        return encoded.toString();
    }

    /**
     * 对源字符串用Base64进行编码
     *
     * @param s 源字符串
     * @return 编码后新的字符串
     */
    public static String encode(String s) {
        return encode(s.getBytes());
    }

    /**
     * 将byte数组型数据，按照指定偏移量编码为char数组型数据
     *
     * @param raw    源byte数组
     * @param offset 偏移量
     * @return 编码后新的字符数组
     */
    protected static char[] encodeBlock(byte[] raw, int offset) {
        int block = 0;
        int slack = raw.length - offset - 1;
        int end = (slack < 2) ? slack : 2;

        for (int i = 0; i <= end; ++i) {
            byte b = raw[(offset + i)];

            int neuter = (b >= 0) ? b : b + 256;
            block += (neuter << 8 * (2 - i));
        }

        char[] base64 = new char[4];

        for (int i = 0; i < 4; ++i) {
            int sixbit = block >>> 6 * (3 - i) & 0x3F;
            base64[i] = getChar(sixbit);
        }

        if (slack < 1) {
            base64[2] = '=';
        }

        if (slack < 2) {
            base64[3] = '=';
        }

        return base64;
    }

    /**
     * 将String字符串解码为新的字符串
     *
     * @param base64 按base64规则编码的字符串
     * @return 解码后新的字符串
     */
    public static String decode(String base64) {
        int pad = 0;

        for (int i = base64.length() - 1; base64.charAt(i) == '='; --i) {
            ++pad;
        }

        int length = base64.length() * 6 / 8 - pad;
        byte[] raw = new byte[length];
        int rawindex = 0;

        for (int i = 0; i < base64.length(); i += 4) {
            int block = (getValue(base64.charAt(i)) << 18) + (getValue(base64.charAt(i + 1)) << 12) + (getValue(base64.charAt(i + 2)) << 6) + getValue(base64.charAt(i + 3));

            for (int j = 0; (j < 3) && (rawindex + j < raw.length); ++j) {
                raw[(rawindex + j)] = (byte) (block >> 8 * (2 - j) & 0xFF);
            }

            rawindex += 3;
        }

        return new String(raw);
    }

    /**
     * 将String字符串解码为新的字符串
     *
     * @param bytes 按base64规则编码的字符数组
     * @return 解码后新的字符串
     */
    public static String decode(byte[] bytes) {
        return decode(new String(bytes));
    }

    /**
     * 将object对象用流读出，经过解码后转换为String对象
     *
     * @param o
     * @return
     */
    public static String objectToString(Object o) {
        if (o == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(32000);
        try {
            ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(baos));

            os.flush();
            os.writeObject(o);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encode(baos.toByteArray());
    }

    /**
     * 将String对象经过编码转换为Object对象
     *
     * @param s
     * @return
     */
    public static Object stringToObject(String s) {
        if (s == null) {
            return null;
        }

        byte[] byteArray = decode(s).getBytes();

        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        try {
            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(bais));

            return is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}