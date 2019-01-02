package com.xin.lv.yang.utils.utils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DataUtil {

    /**
     * 时间戳转成时间
     *
     * @param time 时间戳
     * @param str  格式
     * @return 转换的时间
     */
    public String longTimeToString(long time, String str) {
        SimpleDateFormat format = new SimpleDateFormat(str, Locale.US);
        return format.format(new Date(time));
    }

    /**
     * 获取今天往后一周的日期(几月几号)
     */
    public ArrayList getSevendate() {
        ArrayList dates = new ArrayList();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        for (int i = 0; i < 7; i++) {
            String mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
            String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + i);// 获取当前日份的日期号码
            String date = mMonth + "月" + mDay + "日";
            dates.add(date);
        }
        return dates;
    }

    /**
     * 获取今天往后一周的日期(几月几号)
     */
    public ArrayList getTime() {
        ArrayList dates = new ArrayList();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        for (int i = 0; i < 7; i++) {
            String mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
            String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + i);// 获取当前日份的日期号码
            String date;
            if (mMonth.equals("10")) {
                date = mYear + "-" + mMonth + "-" + mDay;
            } else if (mMonth.equals("11")) {
                date = mYear + "-" + mMonth + "-" + mDay;
            } else if (mMonth.equals("12")) {
                date = mYear + "-" + mMonth + "-" + mDay;
            } else {
                date = mYear + "-0" + mMonth + "-" + mDay;
            }

            dates.add(date);
        }
        return dates;
    }

    /**
     * 获取周几
     *
     * @param strDate 时间字符串
     * @return 周几字符串
     */
    public String getWeekByDateStr(String strDate, String type) {
        String[] ss = strDate.split(type);
        int year = Integer.parseInt(ss[0]);
        int month = Integer.parseInt(ss[1]);
        int day = Integer.parseInt(ss[2]);

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);

        String week = "";
        int weekIndex = c.get(Calendar.DAY_OF_WEEK);

        switch (weekIndex) {
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;
        }
        return week;
    }


    public static String longToTime(long l) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        String sss = String.valueOf(l);
        if (sss.length() == 13) {
            return format.format(new Date(l));
        } else {
            return format.format(new Date(l * 1000));
        }
    }


    public static String longToTime(long l, String type) {
        SimpleDateFormat format = new SimpleDateFormat(type, Locale.CHINA);

        return format.format(new Date(l * 1000));

    }

    public static long stringTimeToLong(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        try {
            Date d = simpleDateFormat.parse(date);
            return d.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
