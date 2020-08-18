package com.mysql.app.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/***
 * 日期转换工具
 *
 * */
public class DateUtil {
    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = year+"."+month;
        return date;
    }

    public static String getCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = year+"."+month+"."+day;
        return date;
    }

    public static String getCurrentDayStr(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = year+"年"+month+"月"+day+"日";
        return date;
    }

    public static String getCurrentMonthStr(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = year+"年"+month+"月";
        return date;
    }

    public static int[] getIntDay(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int[] date = new int[]{year,month,day};
        return date;
    }

    public static String parseTime(long time){
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }
}
