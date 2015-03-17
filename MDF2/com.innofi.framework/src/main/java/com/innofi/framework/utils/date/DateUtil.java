package com.innofi.framework.utils.date;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 日期操作工具类
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public class DateUtil {

    private static Logger _log = LoggerFactory.getLogger(DateUtil.class);

    public static SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat dateForamt1 = new SimpleDateFormat("yyyy-MM-dd");

    public static SimpleDateFormat dateForamt2 = new SimpleDateFormat("yyyy/MM/dd");

    public static SimpleDateFormat dateForamt3 = new SimpleDateFormat("yyyyMMdd");

    public static String[] weekArray = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};

    private static SimpleDateFormat dateForamt4 = new SimpleDateFormat("HH:mm:ss");


    /**
     * 以"yyyy-MM-dd HH:mm:ss.SSS"格式转换指定日期对象
     *
     * @param date 指定日期对象
     * @return "yyyy-MM-dd HH:mm:ss.SSS"格式的日期字符串
     */
    public static String formatFullText(Date date) {
        return fullFormat.format(date);
    }

    /**
     * 以"yyyy-MM-dd HH:mm:ss格式转换指定日期对象
     *
     * @param date 指定日期对象
     * @return "yyyy-MM-dd HH:mm:ss"格式的日期字符串
     */
    public static String formatDateTime(Date date) {
        return dateTimeFormat.format(date);
    }

    /**
     * 以"yyyy-MM-dd"格式转换指定日期对象
     *
     * @param date 指定日期对象
     * @return "yyyy-MM-dd"格式的日期字符串
     */
    public static String formatDateWithDASH(Date date) {
        return dateForamt1.format(date);
    }

    /**
     * 以"yyyy/MM/dd"格式转换指定日期对象
     *
     * @param date 指定日期对象
     * @return "yyyy/MM/dd"格式的日期字符串
     */
    public static String formatDateWithSPLASH(Date date) {
        return dateForamt2.format(date);
    }

    /**
     * 以"yyyyMMdd"格式转换指定日期对象
     *
     * @param date 指定日期对象
     * @return "yyyyMMdd"格式的日期字符串
     */
    public static String formatDateWithShort(Date date) {
        return dateForamt3.format(date);
    }

    /**
     * 格式化日期显示格式
     *
     * @param pattern 日期格式 如:"yyyy-MM-dd HH:mm:ss"
     * @param date    日期对象
     * @return 格式化的日期字符串
     */
    public static String formatDate(String pattern, Date date) {
        SimpleDateFormat f = new SimpleDateFormat(pattern);
        return f.format(date);
    }


    /**
     * 以"yyyyMMdd"日期格式字符串获得该日期字符串所表示的Date对象
     *
     * @param dateStr 日期时间字符串
     * @return 日期字符串所表示的Date对象
     */
    public static Date getDateFromShortStr(String dateStr) {
        try {
            return dateForamt3.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("parse dateStr str err");
        }
    }

    /**
     * 以"yyyy-MM-dd"日期格式字符串获得该日期字符串所表示的Date对象
     *
     * @param dateStr 日期时间字符串
     * @return 日期字符串所表示的Date对象
     */
    public static Date getDateFromDASH(String dateStr) {
        try {
            return dateForamt1.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("parse dateStr str err");
        }
    }

    /**
     * 以"yyyy/MM/dd"日期格式字符串获得该日期字符串所表示的Date对象
     *
     * @param dateStr 日期时间字符串
     * @return 日期字符串所表示的Date对象
     */
    public static Date getDateFromSPLASH(String dateStr) {
        try {
            return dateForamt2.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("parse dateStr str err");
        }
    }

    /**
     * 以"yyyy-MM-dd HH:mm:ss"日期格式字符串获得该日期字符串所表示的Date对象
     *
     * @param dateStr 日期时间字符串
     * @return 日期字符串所表示的Date对象
     */
    public static Date getDateFromDateTime(String dateStr) {
        try {
            return dateTimeFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("parse dateStr str err");
        }
    }

    /**
     * 获得两个日期相差的天数
     *
     * @param date1 “yyyyMMdd”格式或者“yyyy-MM-dd”符串
     * @param date2 “yyyyMMdd”格式或者“yyyy-MM-dd”符串
     * @return 两个日期相差的天数
     */
    public static int getDaysBetween(String date1, String date2) {
        GregorianCalendar ca1 = null;
        GregorianCalendar ca2 = null;
        if (date1 != null && date2 != null && date1.length() == 8 && date2.length() == 8) {
            int iYear = Integer.parseInt(date1.substring(0, 4));
            int iMonth = Integer.parseInt(date1.substring(4, 6)) - 1;
            int iDay = Integer.parseInt(date1.substring(6, 8));
            ca1 = new GregorianCalendar(iYear, iMonth, iDay);

            iYear = Integer.parseInt(date2.substring(0, 4));
            iMonth = Integer.parseInt(date2.substring(4, 6)) - 1;
            iDay = Integer.parseInt(date2.substring(6, 8));
            ca2 = new GregorianCalendar(iYear, iMonth, iDay);
        } else if (date1 != null && date2 != null && date1.length() == 10 && date2.length() == 10) {
            int iYear = Integer.parseInt(date1.substring(0, 4));
            int iMonth = Integer.parseInt(date1.substring(5, 7)) - 1;
            int iDay = Integer.parseInt(date1.substring(8, 10));
            ca1 = new GregorianCalendar(iYear, iMonth, iDay);

            iYear = Integer.parseInt(date2.substring(0, 4));
            iMonth = Integer.parseInt(date2.substring(5, 7)) - 1;
            iDay = Integer.parseInt(date2.substring(8, 10));
            ca2 = new GregorianCalendar(iYear, iMonth, iDay);
        } else {
            return -9999999;
        }

        int year1 = ca1.get(1);
        int year2 = ca2.get(1);

        int dayofYear1 = ca1.get(6);
        int dayofYear2 = ca2.get(6);

        int days = 0;
        int ip = 0;
        for (int i = year1; i < year2; ++i) {
            if (isLeapYear(i)) {
                ip += 366;
            } else {
                ip += 365;
            }
        }

        int temp = ip + dayofYear2 - dayofYear1 + 1;
        return temp;
    }

    /**
     * 判断是否为润年
     *
     * @param year 年份
     * @return 闰年返回true，否则返回false
     */
    public static boolean isLeapYear(int year) {
        boolean isproyear = false;
        if ((((year % 400 == 0) ? 1 : 0) | (((year % 100 != 0) && (year % 4 == 0)) ? 1 : 0)) != 0)
            isproyear = true;
        else {
            isproyear = false;
        }
        return isproyear;
    }

    /**
     * 获得N天之后的日期
     *
     * @param date 当前日期
     * @param n    天数
     * @return 返回N后的日期
     */
    public static Date getAfterNDay(Date date, int n) throws Exception {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        long nDay = date.getTime() + n * 86400000L;
        return new Date(nDay);
    }

    /**
     * 根据日期字符串获得java.sqldialect.Date对象
     *
     * @param “yyyyMMdd”格式或者“yyyy-MM-dd”符串
     * @return 根据指定日期字符串所表示的日期创建日期对象
     */
    public static java.sql.Date getSqlDateFromString(String strDt) {
        if (strDt.length() == 8) {
            strDt = (strDt.substring(0, 4) + "-" + strDt.substring(4, 6) + "-" + strDt.substring(6, 8));
        } else if (strDt.length() == 10) {
            strDt = strDt.replace(' ', '-');
            strDt = strDt.replace('.', '-');
            strDt = strDt.replace('/', '-');
        }
        return java.sql.Date.valueOf(strDt);
    }

    /**
     * 根据日期字符串获得java.util.Date对象
     *
     * @param “yyyyMMdd”格式或者“yyyy-MM-dd”符串
     * @return 根据指定日期字符串创建的日期对象
     */
    public static Date getUtilDateFromString(String strDt) {
        Calendar ca = Calendar.getInstance();
        if (strDt.length() == 8) {
            ca.set(Integer.valueOf(strDt.substring(0, 4)), Integer.valueOf(strDt.substring(4, 6)), Integer.valueOf(strDt.substring(6, 8)));
        } else if (strDt.length() == 10) {
            ca.set(Integer.valueOf(strDt.substring(0, 4)), Integer.valueOf(strDt.substring(5, 7)), Integer.valueOf(strDt.substring(8, 10)));
        }
        return ca.getTime();
    }

    /**
     * 以"yyyy-MM-dd"格式转换”yyyymmdd“日期字符串
     *
     * @param date yyyymmdd格式日期字符串
     * @return yyyy-MM-dd"格式字符串
     */
    public static String getDateString(String date) {
        return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
    }

    /**
     * 获得系统当前日期
     *
     * @return 系统当前日期
     */
    public static Date getSystemDate() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 获得中国本地化格式系统当前日期
     *
     * @return 中国本地化格式系统当前日期
     */
    public static Date getSystemDateOfChina() {
        return Calendar.getInstance(Locale.CHINA).getTime();
    }

    /**
     * 根据本地化获得系统当前日期
     *
     * @param locale 本地化对象
     * @return 本地化获得系统当前日期
     */
    public static Date getSystemDate(Locale locale) {
        return Calendar.getInstance(locale).getTime();
    }

    /**
     * 获得系统当前日期是本年度中的第几天
     *
     * @return int  当日期是本年度中的第几天
     */
    public static int getDayOfYear() {
        Calendar ca = Calendar.getInstance();
        return ca.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获得系统当前日期是本月份中的第几天
     *
     * @return int
     */
    public static int getDayOfMonth() {
        Calendar ca = Calendar.getInstance();
        return ca.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得得某年某月有多少天
     *
     * @param year  年份
     * @param month 月份
     * @return
     */
    public static int getDaysOfYearMonth(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month - 1);//Calendar对象默认一月为0
        return time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
    }


    /**
     * 获得系统当前日期是本月份中的第几天
     *
     * @return int
     */
    public static int getDayOfWeek() {
        int n;
        Calendar ca = Calendar.getInstance();
        if (ca.get(Calendar.DAY_OF_WEEK) == 0) {
            n = 7;
        } else {
            n = ca.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return n;
    }

    /**
     * 根据日期字符串解析成Date对象
     * @param  date 日期字符串
     * @param  formats 日期格式
     * @return 返回日期对象
     */
    public static Date parseDate(String date, String... formats) {
        if (formats == null || formats.length == 0) {
            formats = new String[] { "yyyy-MM-dd" };
        }
        try {
            return DateUtils.parseDate(date, formats);
        } catch (ParseException e) {
            String format = Arrays.toString(formats);
            _log.error("parseDate出错：" + date + "[" + format);
            return null;
        }
    }

    /**
     * 获得相对时间方法
     *
     * @param date  指定日期
     * @param field 值可以是y,M,d,h,m,s,S
     * @param span  与指定日期的间隔
     * @return 返回相对的日期
     */
    public static Date getRelativeDate(Date date, String field, int span) {
        Date result = null;
        char charField = getCharField(field);
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            switch (charField) {
                case 'y':
                    calendar.add(1, span);
                    break;
                case 'M':
                    calendar.add(2, span);
                    break;
                case 'd':
                    calendar.add(5, span);
                    break;
                case 'w':
                    calendar.add(3, span);
                    break;
                case 'h':
                    calendar.add(10, span);
                    break;
                case 'm':
                    calendar.add(12, span);
                    break;
                case 's':
                    calendar.add(13, span);
                    break;
                case 'S':
                    calendar.add(14, span);
            }
            result = calendar.getTime();
        }
        return result;
    }

    private static char getCharField(String field) {
        if ("y".equals(field)) {
            return 'y';
        }
        if ("M".equals(field)) {
            return 'M';
        }
        if ("d".equals(field)) {
            return 'd';
        }
        if ("w".equals(field)) {
            return 'w';
        }
        if ("h".equals(field)) {
            return 'h';
        }
        if ("m".equals(field)) {
            return 'm';
        }
        if ("s".equals(field)) {
            return 's';
        }
        if ("S".equals(field)) {
            return 'S';
        }

        return '\0';
    }

}