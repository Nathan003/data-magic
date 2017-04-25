package com.dodoca.datamagic.common.utils;

import com.dodoca.datamagic.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lifei on 2017/1/10.
 */
public class DateUtils {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date stringToDate(String date){

        if(StringUtils.isEmpty(date)){
            return null;
        }
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
