package com.dodoca.datamagic.utils;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by lifei on 2016/10/25.
 */
public class DateUtil {

    private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Logger logger = Logger.getLogger(DateUtil.class);

    /**
     * 获取相对于今天的前几天或者后几天
     * @param insertDay 如果是负数，则表示前几天；如果是正数，则表示后几天
     * @return
     */
    public static String getDate(int insertDay){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE,insertDay);
        return dayFormat.format(calendar.getTime());
    }

    /**
     * 返回两个"yyyy-MM-dd HH:mm:ss"或者"yyyy-MM-dd"格式的时间相差的天数，用一个所在的天减去另一个所在的天
     * @param startTime
     * @param endTime
     * @return
     */
    public static int differenceDay(String startTime, String endTime){

        try {
            Date startDay = dayFormat.parse(startTime);
            Date endDay = dayFormat.parse(endTime);
            return (int)(endDay.getTime() - startDay.getTime()) / (60 * 60 * 1000 * 24);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return 0;
    }

    /**
     * 获取距离当前第addWeek周的周weekDay的日期
     * @param addWeek 负数表示前几周，正数表示后几周
     * @param weekDay Calendar.SUNDAY = 1，Calendar.MONDAY = 2，Calendar.TUESDAY = 3，Calendar.WEDNESDAY = 4
     *    Calendar.THURSDAY = 5，Calendar.FRIDAY = 6，Calendar.SATURDAY = 7
     * @return
     */
    public static String getWeekDay(int addWeek,int weekDay){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, addWeek);
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        calendar.set(Calendar.DAY_OF_WEEK, weekDay);
        return dayFormat.format(calendar.getTime());
    }

    /**
     * 获取距离当前第addMonth月的第几日的日期
     * @param addMonth 负数表示前几月，正数表示后几月
     * @param monthDay 1表示第一天，0表示距离当前第addMonth月的上一月的最后一天
     * @return
     */
    public static String getMonthDay(int addMonth,int monthDay){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, addMonth);
        calendar.set(Calendar.DAY_OF_MONTH, monthDay);
        return dayFormat.format(calendar.getTime());
    }

    /**
     * 获取距离当前第addYear年的第几日的日期
     * @param addYear 负数表示前几年，正数表示后几年
     * @param yearDay 1表示第一天，0表示距离当前第addYear年的上一年的最后一天
     * @return
     */
    public static String getYearDay(int addYear,int yearDay){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, addYear);
        calendar.set(Calendar.DAY_OF_YEAR, yearDay);
        return dayFormat.format(calendar.getTime());
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(getWeekDay(0, 1));

    }
}
