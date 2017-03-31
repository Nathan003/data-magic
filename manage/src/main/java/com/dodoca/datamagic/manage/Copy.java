package com.dodoca.datamagic.manage;

import com.dodoca.datamagic.common.ConfigUtils;
import com.dodoca.datamagic.common.ConstantUtil;
import com.dodoca.datamagic.common.JSONUtil;
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
public class Copy {
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
        Map<String, String> map = new HashMap<String, String>();
        List<Item> items = new ArrayList<Item>();
        List<String> tmp = new ArrayList<String>();
        List<List<Item>> itemList = new ArrayList<List<Item>>();
        Map<String, List<Item>> dashMap = new HashMap<String, List<Item>>();
        Dashboard dashboard_old = null;
        for (String id : oldDashIdList
                ) {
            dashboard_old = dashboardService.get(id, oldProject);
            items = dashboard_old.getItems();
            dashMap.put(dashboard_old.getId(), items);
        }
        for (String oldId : oldDashIdList) {
            for (String newId : newDashIdList_tmp) {
                map.put(oldId, newId);
                newDashIdList_tmp.remove(newId);
                break;
            }
        }
        for (String str : dashMap.keySet()) {
            List<Item> items1 = dashMap.get(str);
            for (Item item : items1) {
                Bookmark bookmark = item.getBookmark();
                String type = bookmark.getType();
                String name = bookmark.getName();
                String data = bookmark.getData();
                Map<String, Object> map1 = JSONUtil.jsonToObject(data, Map.class);
                List<Map<String, Object>> new_measures = new ArrayList<Map<String, Object>>();
                Map<String, Object> sob = new HashMap<String, Object>();
                List<String> by_fields = null;
                if (map1.get("measures") == null) {
                    if (map1.get("first_event") == null) {
                        Map<String, Object> second_event = (Map<String, Object>) map1.get("second_event");
                        map1.put("second_event", second_event);
                    } else {
                        Map<String, Object> first_event = (Map<String, Object>) map1.get("first_event");
                        map1.put("first_event", first_event);
                    }
                } else {
                    List<Map<String, Object>> measures = (List<Map<String, Object>>) map1.get("measures");
                    for (int i = 0; i < measures.size(); i++) {
                        Map<String, Object> stringObjectMap = measures.get(i);
                        for (String obj : stringObjectMap.keySet()) {
                            String string = stringObjectMap.get(obj).toString();
                            sob.put(obj, string.toLowerCase());
                        }
                        if (map1.get("by_fields") == null) {
                            continue;
                        } else {
                            by_fields = (List<String>) map1.get("by_fields");
                            String lower = by_fields.get(0).toLowerCase();
                            by_fields.add(lower);
                            by_fields.remove(0);
                        }
                    }
                    new_measures.add(sob);
                }
                map1.put("by_fields", by_fields);
                map1.remove("bookmarkid");
                map1.put("measures", new_measures);
                String new_data = JSONUtil.objectToJson(map1);
                String[] dashboards = bookmark.getDashboards();
                String[] relatedEvents = bookmark.getRelatedEvents();
                //对取得的dashboardId进行处理
                for (String ids : map.keySet()) {
                    if (str.equals(ids)) {
                        tmp.add(map.get(ids));
                    }
                }
                String[] new_dashbordsId = (String[]) tmp.toArray(new String[0]);
                tmp.clear();
                Bookmark newbookMark = new Bookmark();
                newbookMark.setType(type);
                newbookMark.setName(name);
                newbookMark.setData(new_data);
                newbookMark.setDashboards(new_dashbordsId);
                newbookMark.setRelatedEvents(relatedEvents);


//                for (String newId : tmp) {
//                    Dashboard dashboard = dashboardService.get(newId, newProject);
//                    List<Item> items2 = dashboard.getItems();
//                    if (items2.equals(null)) {
//                        continue;
//                    } else {
//                        for (Item item2 : items2) {
//                            String name1 = item2.getBookmark().getName();
//                            if (!name1.equals(name)) {
//
//                            } else {
//                                continue;
//                            }
//                        }
//                    }
//                }

                bookmarkService.save(newbookMark, newProject);
            }
        }
    }
}
