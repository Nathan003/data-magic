package com.dodoca.datamagic.manage;

import com.dodoca.datamagic.core.DataMagicUtil;
import com.dodoca.datamagic.utils.JSONUtil;
import com.dodoca.datamagic.utils.model.Bookmark;
import com.dodoca.datamagic.utils.model.Dashboard;
import com.dodoca.datamagic.utils.vo.Item;
import com.google.gson.Gson;

import java.util.*;

/**
 * Created by admin on 2017/4/26.
 */
public class EventMapping {
    public static void main(String[] args) {
        String project = args[0];
        String target = args[1];
        Set bookmarkSet1 = new TreeSet(  );
        Set dashboardSet = new TreeSet(  );
        Set bookmarkSet = new TreeSet(  );
        ArrayList<String> ids = new ArrayList<String>();
        String data = DataMagicUtil.getDashboardsAll( project ).getData();
        List list = JSONUtil.jsonToObject( data, List.class );
        for (int i =0 ;i < list.size();i++){
            Map<String,Object> map = (Map<String, Object>) list.get( i );
            ids.add( ""+map.get( "id" ) );
        }
        List<List<Item>> itemList = new ArrayList<List<Item>>();
        List<Bookmark> bookmarkList = new ArrayList<Bookmark>();
        Set<String> final_events = new HashSet<String>();
        for (String id : ids) {
            Dashboard dashboards = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById(id, project).getData(), Dashboard.class);
            List<Item> items = dashboards.getItems();
            itemList.add(items);
        }
        for (List<Item> it : itemList) {
            for (Item item : it) {
                Bookmark bookmark = item.getBookmark();
                bookmarkList.add(bookmark);
            }
        }
        for (Bookmark bookmark : bookmarkList) {
            String datas = bookmark.getData();
            String[] dashboards = bookmark.getDashboards();
            String id = bookmark.getId();
            String name = bookmark.getName();
            Gson gs = new Gson();
            Map map = gs.fromJson(datas, Map.class);
            for (Object key : map.keySet()) {
                if (key.equals("measures")) {
                    List<Map<String, Object>> expressionList = (List<Map<String, Object>>) map.get(key);
                    for (Map<String, Object> expression : expressionList) {
                        List<String> events = (List<String>) expression.get("events");
                        if (events == null) {
                            if (expression.get("event_name") == null) {
                                continue;
                            } else {
                                final_events.add((String) expression.get("event_name"));
                                if ((expression.get("event_name")).equals( target )){
                                    dashboardSet.addAll( Arrays.asList( dashboards ) );
                                    bookmarkSet1.add( id+" : "+name.replace( " -" ,"")+" " );
                                    bookmarkSet.add( id );
                                }else {
                                    continue;
                                }
                            }
                        } else {
                            for (String event : events) {
                                final_events.add(event.toString());
                                if (event.toString().equals( target )){
                                    dashboardSet.addAll( Arrays.asList( dashboards ) );
                                    bookmarkSet1.add( id+" : "+name.replace( " -" ,"")+" " );
                                    bookmarkSet.add( id );
                                }else {
                                    continue;
                                }
                            }
                        }
                    }
                } else if (key.equals("first_event")) {
                    Map<String, Object> tmpMap = (Map<String, Object>) map.get(key);
                    Object event_name = tmpMap.get("event_name");
                    final_events.add(event_name.toString());
                    if (event_name.toString().equals( target )){
                        dashboardSet.addAll( Arrays.asList( dashboards ) );
                        bookmarkSet1.add( id+" : "+name.replace( " -" ,"")+" " );
                        bookmarkSet.add( id );
                    }else {
                        continue;
                    }
                } else if (key.equals("second_event")) {
                    Map<String, Object> tmpMap = (Map<String, Object>) map.get(key);
                    Object event_name = tmpMap.get("event_name");
                    final_events.add(event_name.toString());
                    if (event_name.toString().equals( target )){
                        dashboardSet.addAll( Arrays.asList( dashboards ) );
                        bookmarkSet1.add( id+" : "+name.replace( " -" ,"")+" " );
                        bookmarkSet.add( id );
                    }else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        Set dashboardSet1 = new TreeSet(  );
        for (Object dashid:dashboardSet) {
            Dashboard dashboard = JSONUtil.jsonToObject( DataMagicUtil.getDashboardById( (String) dashid, project ).getData(), Dashboard.class );
            String name = dashboard.getName();
            dashboardSet1.add( dashid+" : "+name.replace( " -" ,"") );
        }
        HashMap<String, Set<String>> bookmarkIdMap = new HashMap<String, Set<String>>();
        for (Object dashid:dashboardSet) {
            Set<String> bookmarkIdList = new TreeSet<String>();
            Dashboard dashboard = JSONUtil.jsonToObject( DataMagicUtil.getDashboardById( (String) dashid, project ).getData(), Dashboard.class );
            List<Item> items = dashboard.getItems();
            for (Item item:items){
                Bookmark bookmark = item.getBookmark();
                String bookmarkId = bookmark.getId();
                bookmarkIdList.add( bookmarkId );
            }
            bookmarkIdMap.put( (String)dashid ,bookmarkIdList );
        }
        Map<String,Set<String>> tmp1 = new TreeMap<String, Set<String>>(  );
        for (String key :bookmarkIdMap.keySet()) {
            Dashboard dashboard = JSONUtil.jsonToObject( DataMagicUtil.getDashboardById( key, project ).getData(), Dashboard.class );
            String dashname = dashboard.getName();
            Set<String> tmp = new TreeSet<String>(  );
            Set<String> bookidList = bookmarkIdMap.get( key );
            for (Object bookid:bookmarkSet) {
                if (bookidList.contains( (String)bookid )){
                    Bookmark bookmark = JSONUtil.jsonToObject( DataMagicUtil.getBookmarkByAdmin( (String) bookid, project ).getData(), Bookmark.class );
                    String bookname = bookmark.getName();
                    tmp.add( (String)bookid+":"+bookname.replace( " -" ,"") );
                }else {
                    continue;
                }
            }
            tmp1.put( key+":"+dashname.replace( " -" ,""),tmp );
        }
        System.err.println( "\n \n \n \n" );
        System.err.println( "事件 "+target+" 所涉及到的书签分别为：");
        for (String key:tmp1.keySet()) {
            System.err.println( key+" 下的 "+tmp1.get( key )+"书签 " );
        }
    }

}
