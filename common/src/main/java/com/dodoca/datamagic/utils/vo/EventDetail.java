package com.dodoca.datamagic.utils.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by admin on 2017/3/28.
 */
@JsonIgnoreProperties
public class EventDetail {
    private String id;
    private String virtual;
    private String visible;
    private String[] tag;
    private String name;
    private String cname;
    private String pinyin;


    public String getVirtual() {
        return virtual;
    }

    public void setVirtual(String virtual) {
        this.virtual = virtual;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String[] getTag() {
        return tag;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
