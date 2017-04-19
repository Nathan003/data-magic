package com.dodoca.datamagic.manage;

import com.dodoca.datamagic.common.utils.ConfigUtils;
import com.dodoca.datamagic.common.utils.ConstantUtil;
import com.dodoca.datamagic.common.utils.JSONUtil;
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
        Map<String, Bookmark> map = new HashMap<String, Bookmark>();
        Map<String, String> idMap = new HashMap<String, String>();

        for (int i = 0; i < oldDashIdList.size(); i++) {
            idMap.put(oldDashIdList.get(i), newDashIdList.get(i));
        }

        for (String id : oldDashIdList) {
            Dashboard dashboard = dashboardService.get(id, oldProject);
            List<Item> items = dashboard.getItems();
            for (Item item : items) {
                Bookmark bookmark = item.getBookmark();
                String id1 = bookmark.getId();

                String type = bookmark.getType();
                String name = bookmark.getName();
                String data = bookmark.getData();
                String[] dashboards = bookmark.getDashboards();
                String[] relatedEvents = bookmark.getRelatedEvents();
                List<String> dashboardsList = new ArrayList<String>(Arrays.asList(dashboards));

                for (int i = 0; i < dashboardsList.size(); i++) {
                    for (String key : idMap.keySet()) {
                        if (!((idMap.keySet()).contains(dashboardsList.get(i)))) {
                            dashboardsList.remove(i);
                        } else {
                            if (!dashboards[i].equals(key)) {
                                continue;
                            } else {
                                dashboardsList.set(i, idMap.get(key));
                                break;
                            }
                        }
                    }
                }
                dashboards = (String[]) dashboardsList.toArray(new String[dashboardsList.size()]);

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
                map1.put("filter",null);
                map1.put("measures", new_measures);
                data = JSONUtil.objectToJson(map1);



                Bookmark newbookMark = new Bookmark();
                newbookMark.setType(type);
                newbookMark.setName(name);
                newbookMark.setData(data);
                newbookMark.setDashboards(dashboards);
                newbookMark.setRelatedEvents(relatedEvents);


                map.put(id1, newbookMark);
            }
        }

        ArrayList<String> saveIds = new ArrayList<String>();
        for (String key : map.keySet()){
            Bookmark bookmark = map.get(key);
            String save_id = bookmarkService.save(bookmark, newProject);
            saveIds.add(save_id);
            System.err.println(saveIds);
        }

    }
}
