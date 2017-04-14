package com.dodoca.datamagic.manage;

import com.dodoca.datamagic.common.ConfigUtils;
import com.dodoca.datamagic.common.ConstantUtil;
import com.dodoca.datamagic.core.model.Bookmark;
import com.dodoca.datamagic.core.model.Dashboard;
import com.dodoca.datamagic.core.service.BookmarkService;
import com.dodoca.datamagic.core.service.DashboardService;
import com.dodoca.datamagic.core.service.impl.BookmarkServiceImpl;
import com.dodoca.datamagic.core.service.impl.DashboardServiceImpl;
import com.dodoca.datamagic.core.vo.Item;

import java.util.*;

/**
 * Created by admin on 2017/3/17.
 */
public class CopyDashbordToProject {
    /**
     * @param args
     * args[0]=string oldproject
     * args[1]=string newproject
     * args[2]=string path
     */
    private static DashboardService dashboardService = new DashboardServiceImpl();
    private static BookmarkService bookmarkService = new BookmarkServiceImpl();

    public static void main(String[] args) throws Exception {
        String oldProject = args[0];
        String newProject = args[1];
        ConfigUtils.load(args[2]);
        String[] str1 = ConstantUtil.getArray("dashIds");
        List<String> oldDashIdList = new ArrayList<String>(Arrays.asList(str1));
        String[] str2 = ConstantUtil.getArray("dashIds_new");
        List<String> newDashIdList = new ArrayList<String>(Arrays.asList(str2));
        List<String> newDashIdList_tmp = new ArrayList<String>();
        newDashIdList_tmp.addAll(newDashIdList);
        //List<String> newId = new ArrayList<String>();
        Map<String, String> map = new HashMap<String, String>();
        List<Item> items = new ArrayList<Item>();
        List<String> tmp = new ArrayList<String>();
        List<List<Item>> itemList = new ArrayList<List<Item>>();
        Dashboard dashboard_old = null;
        for (String id : oldDashIdList
                ) {
            dashboard_old = dashboardService.get(id, oldProject);
//            String dashBoardName = dashboard_old.getName();
//            String isPublic = dashboard_old.getIsPublic();
            //生成新概览的request
//            Dashboard newDashboard = new Dashboard();
//            newDashboard.setName(dashBoardName);
//            newDashboard.setIsPublic(isPublic);
//            //新增概览
//            String new_id = dashboardService.save(JSONUtil.objectToJson(newDashboard), newProject);
//            newId.add(new_id);
//            map.put(id, new_id);
            //获取书签
            items = dashboard_old.getItems();
            itemList.add(items);
        }
        for (String oldId : oldDashIdList){
            for (String newId : newDashIdList_tmp){
                map.put(oldId, newId);
                newDashIdList_tmp.remove(newId);
                break;
            }
        }
        for (List<Item> it : itemList) {
//            for (String id_new : newDashIdList) {
//                Dashboard dashboard_new = dashboardService.get(id_new, newProject);
//                dashboard_new.setItems(it);
//                List<Item> items1 = dashboard_new.getItems();
//            }
            for (Item item : it) {
                Bookmark bookmark = item.getBookmark();
                String type = bookmark.getType();
                String name = bookmark.getName();
                String data = bookmark.getData();
                String[] dashboards = bookmark.getDashboards();
                String[] relatedEvents = bookmark.getRelatedEvents();
                //对取得的dashboardId进行处理


//                for (int i = 0; i < dashboards.length; i++) {
//                    for (String key : map.keySet()) {
//                        if (dashboards[i].equals(key)) {
//                            dashboards[i] = map.get(key);
//                        }
//                        tmp.add(map.get(key));
//                    }
//                }
//                List<String> subList = tmp.subList(0, map.size());
//                List<String> dashList = new ArrayList<String>(Arrays.asList(dashboards));
//                for (String string : dashboards) {
//                    if (!subList.contains(string)) {
//                        dashList.remove(string);
//                    }
//                }
//                tmp.clear();
                Bookmark newbookMark = new Bookmark();
                newbookMark.setType(type);
                newbookMark.setName(name);
                newbookMark.setData(data);
                newbookMark.setDashboards(dashboards);
                newbookMark.setRelatedEvents(relatedEvents);
                bookmarkService.save(newbookMark, newProject);
            }
        }
    }
}

