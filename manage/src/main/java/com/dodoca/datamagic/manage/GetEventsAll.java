package com.dodoca.datamagic.manage;

import com.dodoca.datamagic.common.utils.ConfigUtils;
import com.dodoca.datamagic.common.utils.ConstantUtil;
import com.dodoca.datamagic.core.model.Bookmark;
import com.dodoca.datamagic.core.model.Dashboard;
import com.dodoca.datamagic.core.service.DashboardService;
import com.dodoca.datamagic.core.service.impl.DashboardServiceImpl;
import com.dodoca.datamagic.core.vo.Item;
import com.google.gson.Gson;

import java.util.*;

/**
 * Created by admin on 2017/3/23.
 */
public class GetEventsAll {

    private static DashboardService dashboardService = new DashboardServiceImpl();

    /**
     * @param args
     * args[0] 项目名称
     * args[1] path
     */
    public static void main(String[] args) throws Exception{
        String project = args[0].toString();
        ConfigUtils.load(args[1]);
        String[] str = ConstantUtil.getArray("dashIds");
        List<String> list = new ArrayList<String>(Arrays.asList(str));
        List<List<Item>> itemList = new ArrayList<List<Item>>();
        List<Bookmark> bookmarkList = new ArrayList<Bookmark>();
        Set<String> final_events = new HashSet<String>();

        for (String id : list) {
            Dashboard dashboards = dashboardService.get(id, project);
            List<Item> items = dashboards.getItems();
            itemList.add(items);
        }
        for (List<Item> it : itemList) {
            for(Item item : it){
                Bookmark bookmark = item.getBookmark();
                bookmarkList.add(bookmark);
            }
        }
        for (Bookmark bookmark : bookmarkList) {
            String data = bookmark.getData();
            Gson gs = new Gson();
            Map map = gs.fromJson(data, Map.class);
            for (Object key : map.keySet()) {
                //System.err.println(key+"___________________"+map.get(key));
                if (key.equals("measures")) {
                    List<Map<String, Object>> expressionList = (List<Map<String, Object>>) map.get(key);
                    for (Map<String, Object> expression : expressionList) {
                        List<String> events = (List<String>) expression.get("events");
                        if (events == null) {
                            if (expression.get("event_name") == null) {
                                continue;
                            } else {
                                final_events.add((String) expression.get("event_name"));
                            }
                        } else {
                            for (String event : events) {
                                final_events.add(event.toString());
                            }
                        }
                    }
                } else if (key.equals("first_event")) {
                    Map<String, Object> tmpMap = (Map<String, Object>) map.get(key);
                    Object event_name = tmpMap.get("event_name");
                    //System.err.println(event_name);
                    final_events.add(event_name.toString());
                } else if (key.equals("second_event")) {
                    Map<String, Object> tmpMap = (Map<String, Object>) map.get(key);
                    Object event_name = tmpMap.get("event_name");
                    //System.err.println(event_name);
                    final_events.add(event_name.toString());
                } else {
                    continue;
                }
            }
        }
        for (String strings : final_events) {
            System.err.println(strings);
        }
        System.err.println("所有事件共计：\n" + final_events.size()+"个");
    }
}
