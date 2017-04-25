package com.dodoca.datamagic.common.service;

/**
 * 获取书签数据的处理接口
 * Created by lifei on 2016/12/29.
 *
 */
public interface DataMagicHandle {

    /**
     * 通过http获取书签数据，如果token为null，则通过admin获取数据
     * @param bookmarkID
     * @param data
     * @param token
     * @return
     */
    com.dodoca.datamagic.utils.model.BaseResponse handle(String bookmarkID, String data, String token);
}
