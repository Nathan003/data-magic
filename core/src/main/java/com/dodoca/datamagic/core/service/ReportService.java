package com.dodoca.datamagic.core.service;

import com.dodoca.datamagic.utils.model.BaseResponse;
import com.dodoca.datamagic.utils.model.Bookmark;

/**
 * Created by lifei on 2017/3/13.
 */
public interface ReportService {

    /**
     * 获取书签数据
     * @param bookmark
     * @param token
     * @return
     */
    BaseResponse report(Bookmark bookmark, String token);

    /**
     * 获取漏斗的书签数据
     * @param funnelId
     * @param bookmarkId
     * @param data
     * @param token
     * @return
     */
    BaseResponse report(String funnelId,String bookmarkId,String data,String token);

    /**
     * 获取书签数据
     * @param bookmarkId
     * @param data
     * @param token
     * @return
     */
    BaseResponse report(String bookmarkId,String data,String token);
}
