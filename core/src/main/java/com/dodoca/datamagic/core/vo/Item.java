package com.dodoca.datamagic.core.vo;

import com.dodoca.datamagic.core.model.Bookmark;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
}
