package com.dodoca.datamagic.core.vo;

import com.dodoca.datamagic.common.utils.DateUtil;
import com.dodoca.datamagic.common.utils.JSONUtil;
import com.dodoca.datamagic.core.model.Bookmark;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by lifei on 2017/3/14.
 */
@JsonIgnoreProperties
public class Item {
    private Bookmark bookmark;
    private String config;
    @JsonProperty(value = "bookmark_id")
    private String bookmarkId;

    public Bookmark getBookmark() {
        return bookmark;
    }

    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(String bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    /**
     * 获取书签数据的起始时间
     * @return
     */
    public String getFromDate() {
        Map<String,Object> configMap = JSONUtil.jsonToObject(config,Map.class);
        if(configMap.isEmpty()){
            return null;
        }
        String widgetTime = (String)configMap.get("widgetTime");
        if("0 day".equals(widgetTime)){//今天
            return DateUtil.getDate(0);
        }else if("1 day".equals(widgetTime)){//昨天
            return DateUtil.getDate(-1);
        }else if("7 day".equals(widgetTime)){//过去7天
            return DateUtil.getDate(-7);
        }else if("30 day".equals(widgetTime)){//过去30天
            return DateUtil.getDate(-30);
        }else if("-1 day".equals(widgetTime)){//上线至今
            return "1970-01-01";
        }else if("last week".equals(widgetTime)){//上周
            return DateUtil.getWeekDay(-1, Calendar.MONDAY);
        }else if("0 week".equals(widgetTime)){//本周
            return DateUtil.getWeekDay(0, Calendar.MONDAY);
        }else if("last month".equals(widgetTime)){//上月
            return DateUtil.getMonthDay(-1,1);
        }else if("0 month".equals(widgetTime)){//本月
            return DateUtil.getMonthDay(0,1);
        }else if("last year".equals(widgetTime)){//去年
            return DateUtil.getYearDay(-1,1);
        }else if("0 year".equals(widgetTime)){//本年
            return DateUtil.getYearDay(0,1);
        }
        return null;
    }

    /**
     * 获取书签数据的结束时间
     * @return
     */
    public String getEndDate() {
        Map<String,Object> configMap = JSONUtil.jsonToObject(config,Map.class);
        if(configMap.isEmpty()){
            return null;
        }
        String widgetTime = (String)configMap.get("widgetTime");
        if("0 day".equals(widgetTime)){//今天
            return DateUtil.getDate(0);
        }else if("1 day".equals(widgetTime)){//昨天
            return DateUtil.getDate(-1);
        }else if("7 day".equals(widgetTime)){//过去7天
            return DateUtil.getDate(0);
        }else if("30 day".equals(widgetTime)){//过去30天
            return DateUtil.getDate(0);
        }else if("-1 day".equals(widgetTime)){//上线至今
            return DateUtil.getDate(0);
        }else if("last week".equals(widgetTime)){//上周
            return DateUtil.getWeekDay(-1, Calendar.SUNDAY);
        }else if("0 week".equals(widgetTime)){//本周
            return DateUtil.getWeekDay(0, Calendar.SUNDAY);
        }else if("last month".equals(widgetTime)){//上月
            return DateUtil.getMonthDay(0,0);
        }else if("0 month".equals(widgetTime)){//本月
            return DateUtil.getMonthDay(1,0);
        }else if("last year".equals(widgetTime)){//去年
            return DateUtil.getYearDay(0,0);
        }else if("0 year".equals(widgetTime)){//本年
            return DateUtil.getYearDay(1,0);
        }
        return null;
    }

    public void setConfig(String importDay, boolean isAppend) {
        Map<String,Object> configMap = JSONUtil.jsonToObject(config,Map.class);
        if(!isAppend){
            setConfig(importDay);
            return;
        }
        configMap.put("widgetTime",importDay);
        setConfig(JSONUtil.objectToJson(configMap));
    }
}
