package com.dodoca.datamagic.common.dao;

import com.dodoca.datamagic.common.model.Shop;

/**
 * Created by lifei on 2016/12/14.
 */
public interface ShopDao {

    /**
     * 根据shopID查询商铺
     * @param shopID
     * @return
     */
    Shop getById(String shopID);
}
