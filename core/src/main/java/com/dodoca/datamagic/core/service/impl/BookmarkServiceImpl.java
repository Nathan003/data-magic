package com.dodoca.datamagic.core.service.impl;

import com.dodoca.datamagic.common.JSONUtil;
import com.dodoca.datamagic.core.DataMagicUtil;
import com.dodoca.datamagic.core.model.Bookmark;
import com.dodoca.datamagic.core.service.BookmarkService;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by lifei on 2017/3/13.
 */
public class BookmarkServiceImpl implements BookmarkService {

    public BookmarkService get(String id) {


        BookmarkService bookmark = JSONUtil.jsonToObject(DataMagicUtil.getBookmarkByAdmin(id).getData(),BookmarkService.class);




        return  bookmark;
    }

    public String save(Bookmark bookmark) {
        String data = JSONUtil.objectToJson(bookmark);

        String idStr = DataMagicUtil.addBookmark(data).getData();
        Gson gs = new Gson();
        Map<String, Double> idMap = gs.fromJson(idStr, Map.class);
        double d = idMap.get("id");
        int i = (int) d;
        String id = String.valueOf(i);

        return id;
    }

    public String save(Bookmark bookmark,String project) {
        String data = JSONUtil.objectToJson(bookmark);

        String idStr = DataMagicUtil.addBookmark(data,project).getData();
        Gson gs = new Gson();
        Map<String, Double> idMap = gs.fromJson(idStr, Map.class);
        double d = idMap.get("id");
        int i = (int) d;
        String id = String.valueOf(i);

        return id;
    }

    public boolean update(Bookmark bookmark) {
        String id = bookmark.getId();
        String data = JSONUtil.objectToJson(bookmark);

        int status = DataMagicUtil.updateBookmark(id,data).getStatus();

        return status == 200;

    }

    public boolean update(Bookmark bookmark,String project) {
        String id = bookmark.getId();
        String data = JSONUtil.objectToJson(bookmark);

        int status = DataMagicUtil.updateBookmark(id,data,project).getStatus();

        return status == 200;

    }

    public boolean delete(String id) {
        int status = DataMagicUtil.deleteBookmark(id).getStatus();
        return status == 200;
    }
}
