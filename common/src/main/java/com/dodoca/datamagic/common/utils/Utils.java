package com.dodoca.datamagic.common.utils;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lifei on 2016/12/7.
 */
public class Utils {

    private static Logger logger = Logger.getLogger(Utils.class);

    public static void replaceFilter(Map<String,Object> data,String reg,String value){
        List<String> valueList = new ArrayList<String>();
        valueList.add(value);
        Map<String, Object> filter =  (Map<String,Object>)data.get("filter");
        List<Map<String,Object>> conditions = (List<Map<String,Object>>)filter.get("conditions");
        for (Map<String,Object> condition:conditions){
            if(condition.get("field").toString().contains(reg)){
                condition.put("params",valueList);
            }
        }
    }

    public static void replaceFromDate(Map<String,Object> data,String value){
        data.put("from_date",value);
    }

    public static void replaceToDate(Map<String,Object> data,String value){
        data.put("to_date",value);
    }

    public static void replaceLimit(Map<String,Object> data,int value){
        data.put("to_date",value);
    }


    public static void main(String[] args) {
        String str = "{\"measures\":[{\"event_name\":\"pay_order1018\",\"aggregator\":\"SUM\",\"field\":\"event.pay_order1018.real_amount\"}],\"unit\":\"day\",\"filter\":{\"conditions\":[{\"field\":\"event.pay_order1018.SHOP_id\",\"function\":\"equal\",\"params\":[\"13289359\"]}]},\"sampling_factor\":64,\"from_date\":\"2016-12-01\",\"to_date\":\"2016-12-07\",\"bookmarkid\":\"299\",\"username\":\"18217615434\",\"use_cache\":true}";
//        String rs = Utils.replaceAll(str,"SHOP_id","1234567890");
//        System.out.println(rs);

    }
}
