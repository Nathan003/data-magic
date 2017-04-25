package com.dodoca.datamagic.common.service;

import com.dodoca.datamagic.common.model.Shop;

/**
 * Created by lifei on 2016/12/14.
 */
public interface ShopService {

    /**
     * 根据id获取商铺
     * @param shopId
     * @return
     */
    Shop get(String shopId);
}
