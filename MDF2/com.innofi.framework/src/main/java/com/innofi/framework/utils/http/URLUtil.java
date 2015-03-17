package com.innofi.framework.utils.http;

public final class URLUtil {

    private static final char SEPARATOR = 47;

    /**
     * 去除url中最后一个'/'符号
     *
     * @param url
     * @return 返回处理后的url
     */
    public static String trimEndSeparator(String url) {
        StringBuffer sb = new StringBuffer(url);
        for (int i = 0; i < sb.length(); ++i) {
            if (sb.charAt(i) != SEPARATOR) {
                sb.delete(0, i);
                break;
            }
        }
        for (int i = sb.length() - 1; (i >= 0) && (sb.charAt(i) == SEPARATOR); --i) {
            sb.deleteCharAt(i);
        }
        return sb.toString();
    }

}