package com.dodoca.datamagic.utils.model;
// default package

import java.util.List;

/**
 * ShopLine entity. @author MyEclipse Persistence Tools
 */

public class ShopLine {

	private String shopLineId; //KPI运营组ID
	private String shopLine; //KPI运营组
	private List<Shop> shopList;

	public ShopLine() {
	}

	public ShopLine(String shopLineId, String shopLine) {
		this.shopLineId = shopLineId;
		this.shopLine = shopLine;
	}

	public String getShopLineId() {
		return this.shopLineId;
	}

	public void setShopLineId(String shopLineId) {
		this.shopLineId = shopLineId;
	}

	public String getShopLine() {
		return this.shopLine;
	}

	public void setShopLine(String shopLine) {
		this.shopLine = shopLine;
	}

	public List<Shop> getShopList() {
		return shopList;
	}

	public void setShopList(List<Shop> shopList) {
		this.shopList = shopList;
	}
}