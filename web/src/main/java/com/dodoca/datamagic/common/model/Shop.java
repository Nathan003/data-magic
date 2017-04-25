package com.dodoca.datamagic.common.model;


/**
 * Created by lifei on 2016/12/14.
 */

public class Shop {

    private String shopId;
    private String shopName; //商铺名称
    private String shopCate1Id; //商铺一级行业类别ID
    private String shopCate1Name;//商铺一级行业类别
    private String shopCate2Id;//商铺二级行业类别ID
    private String shopCate2Name; //商铺二级行业类别
    private String shopLineId; //KPI运营组ID
    private String shopLine; //KPI运营组
    private int lv; //商铺等级

    public int getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        if(null == lv){
            lv = 0;
        }
        this.lv = lv;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopCate1Id() {
        return shopCate1Id;
    }

    public void setShopCate1Id(String shopCate1Id) {
        this.shopCate1Id = shopCate1Id;
    }

    public String getShopCate1Name() {
        return shopCate1Name;
    }

    public void setShopCate1Name(String shopCate1Name) {
        this.shopCate1Name = shopCate1Name;
    }

    public String getShopCate2Id() {
        return shopCate2Id;
    }

    public void setShopCate2Id(String shopCate2Id) {
        this.shopCate2Id = shopCate2Id;
    }

    public String getShopCate2Name() {
        return shopCate2Name;
    }

    public void setShopCate2Name(String shopCate2Name) {
        this.shopCate2Name = shopCate2Name;
    }

    public String getShopLineId() {
        return shopLineId;
    }

    public void setShopLineId(String shopLineId) {
        this.shopLineId = shopLineId;
    }

    public String getShopLine() {
        return shopLine;
    }

    public void setShopLine(String shopLine) {
        this.shopLine = shopLine;
    }
}
