package com.dodoca.datamagic.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by lifei on 2016/10/25.
 */
public class DateUtil {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取相对于今天的前几天或者后几天
     * @param insertDay 如果是负数，则表示前几天；如果是正数，则表示后几天
     * @return
     */
    public static String getDate(int insertDay){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE,insertDay);
        return format.format(calendar.getTime());
    }
}
