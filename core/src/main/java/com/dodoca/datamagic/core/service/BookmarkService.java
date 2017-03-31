package com.dodoca.datamagic.core.service;

import com.dodoca.datamagic.core.model.Bookmark;

/**
 * Created by lifei on 2017/3/13.
 */
public interface BookmarkService {

    /**
     * 根据ID获取书签信息
     * @param id
     * @return
     */
    BookmarkService get(String id);

    /**
     * 保存书签到概览
     * @param bookmark
     * @return
     */
    String save(Bookmark bookmark);

    String save(Bookmark bookmark,String project);

    /**
     * 更新书签，包括更新书签的定义
     * @param bookmark
     * @return
     */
    boolean update(Bookmark bookmark);

    boolean update(Bookmark bookmark,String project);

    /**
     * 删除书签
     * @param id 概览ID
     * @return
     */
    boolean delete(String id);
}
