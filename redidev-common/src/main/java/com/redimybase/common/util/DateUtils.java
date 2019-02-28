package com.redimybase.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具辅助类.
 * <p>描述:封装常用的日期处理函数</p>
 * Created by Irany(欧书剑) 2017/7/19 0019 12:00
 */
public class DateUtils {
    /** FULLDATETIME_PATTERN(String):yyyy-MM-dd HH:mm:ss. */
    public static final String FULLDATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss.S";
    /** DATETIME_PATTERN(String):yyyy-MM-dd HH:mm:ss. */
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /** DATE_PATTERN(String):yyyy-MM-dd. */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    /** MONTH_PATTERN(String):yyyy-MM. */
    public static final String MONTH_PATTERN = "yyyy-MM";

    /**
     * 构造函数.
     */
   private DateUtils() {}

    /**
     * 转换为java.util.Date对象.
     * @param value 带转换对象
     * @return 对应的Date对象
     * @throws Exception
     */
    public static final Date toDate(Object value) throws Exception {
        if (value == null) return null;

        Date result = null;

        if (value instanceof String) {
            if (StringUtils.isNotEmpty((String)value)) {
                result = org.apache.commons.lang3.time.DateUtils.parseDate((String) value, DATE_PATTERN, DATETIME_PATTERN, FULLDATETIME_PATTERN, MONTH_PATTERN);

                if (result == null && StringUtils.isNotEmpty((String)value)) {
                    try {
                        result = new Date(new Long((String) value));
                    } catch (Exception e) {
                        throw e;
                    }
                }
            }
        } else if (value instanceof Object[]) {
            Object[] array = (Object[]) value;

            if ((array != null) && (array.length >= 1)) {
                value = array[0];
                result = toDate(value);
            }
        } else if (Date.class.isAssignableFrom(value.getClass())) {
            result = (Date) value;
        }

        return result;
    }

    /**
     * 转换为java.util.Date对象.
     * @param value 带转换对象
     * @param pattern 日期格式
     * @return 对应的Date对象
     */
    public static final Date toDate(Object value, String pattern) {
        if (value == null) return null;

        Date result = null;

        if (value instanceof String) {
            if (StringUtils.isNotEmpty((String)value)) {
                try {
                    result = org.apache.commons.lang3.time.DateUtils.parseDate((String) value, pattern);
                } catch (ParseException e) {}

                if (result == null && StringUtils.isNotEmpty((String)value)) {
                    result = new Date(new Long((String) value));
                }
            }
        } else if (value instanceof Object[]) {
            Object[] array = (Object[]) value;

            if ((array != null) && (array.length >= 1)) {
                value = array[0];
                result = toDate(value, pattern);
            }
        } else if (Date.class.isAssignableFrom(value.getClass())) {
            result = (Date) value;
        }

        return result;
    }

    /**
     * 获取<code>date</code>同一年的第一天.
     */
    public static final Date getFirstDateOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    /**
     * 获取<code>date</code>同一年的最后一天.
     */
    public static final Date getLastDateOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getFirstDateOfYear(date));
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return cal.getTime();
    }

    /**
     * 给<code>date</code>追加<code>year</code>年.
     */
    public static final Date addYear(Date date, int year) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year);
        return cal.getTime();
    }

    /**
     * 获取<code>date</code>同月份的第一天.
     */
    public static final Date getFirstDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 获取<code>date</code>同月份的第后一天.
     */
    public static final Date getLastDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getFirstDateOfMonth(date));
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return cal.getTime();
    }

    /**
     * 给<code>date</code>追加<code>month</code>月.
     */
    public static final Date addMonth(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    /**
     * 获取<code>date</code>同周的星期一.
     */
    public static final Date getFirstDateOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    /**
     * 获取<code>date</code>同周的星期日.
     */
    public static final Date getLastDateOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTime();
    }

    /**
     * 给<code>date</code>追加<code>week</code>周.
     */
    public static final Date addWeek(Date date, int week) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.WEEK_OF_YEAR, week);
        return cal.getTime();
    }

    /**
     * 给<code>date</code>追加<code>day</code>天.
     */
    public static final Date addDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);

        return c.getTime();
    }

    /**
     * 给<code>date</code>追加<code>hour</code>小时.
     */
    public static final Date addHour(Date date, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hour);

        return c.getTime();
    }

    /**
     * 给<code>date</code>追加<code>minute</code>分钟.
     */
    public static final Date addMinute(Date date, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minute);

        return c.getTime();
    }

    /**
     * 转换为字符串.
     */
    public static final String toString(Date date, String pattern) {
        String result = null;

        if (date != null) {
            result = DateFormatUtils.format(date, pattern);
        }

        return result;
    }
}
