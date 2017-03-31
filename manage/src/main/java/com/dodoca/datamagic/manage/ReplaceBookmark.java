package com.dodoca.datamagic.manage;

import com.dodoca.datamagic.core.model.Bookmark;
import com.dodoca.datamagic.core.model.Dashboard;
import com.dodoca.datamagic.core.service.BookmarkService;
import com.dodoca.datamagic.core.service.DashboardService;
import com.dodoca.datamagic.core.service.impl.BookmarkServiceImpl;
import com.dodoca.datamagic.core.service.impl.DashboardServiceImpl;
import com.dodoca.datamagic.core.vo.Item;

import java.util.List;

/**
 * Created by admin on 2017/3/14.
 */

/**
 * args[0]=string dashboardId
 * args[1]=string oldEvent
 * args[2]=string newEvent
 * args[3]=string project
 */
public class ReplaceBookmark {
    private static DashboardService dashboardService = new DashboardServiceImpl();
    private static BookmarkService bookmarkService = new BookmarkServiceImpl();

    public static void main(String[] args) throws Exception{
        String dashBoardId = args[0].toString();
        String oldEvent = args[1].toString();
        String newEvent = args[2].toString();
        String project = args[3].toString();
        Dashboard dashboard = dashboardService.get(dashBoardId,project);
        List<Item> items = dashboard.getItems();
        for (int i = 0;i<items.size();i++){
            Item item = items.get(i);
            Bookmark bookmark = item.getBookmark();
            String data = bookmark.getData();
            String replaceData = data.replace(oldEvent, newEvent);
            bookmark.setData(replaceData);
            bookmarkService.update(bookmark,project);
        }

    }
}
