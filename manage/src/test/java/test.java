import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dodoca.datamagic.common.HttpClientUtils;
import com.dodoca.datamagic.common.JSONUtil;
import com.dodoca.datamagic.common.model.BaseResponse;
import com.dodoca.datamagic.core.DataMagicUtil;
import com.dodoca.datamagic.core.model.Bookmark;
import com.dodoca.datamagic.core.model.Dashboard;
import com.dodoca.datamagic.core.vo.*;
import com.google.gson.Gson;
import org.junit.Test;

import java.util.*;

import static com.dodoca.datamagic.core.DataMagicUtil.dashboardsAuth;
import static com.dodoca.datamagic.core.DataMagicUtil.getAdminToken;

/**
 * Created by admin on 2017/3/15.
 */


public class test {


    /**
     * 获得指定概览
     */
    @Test
    public void test() {
        System.err.println(DataMagicUtil.getDashboardById("125", "wxrrd_datamagic").getData());
        System.out.println("***************************************************\n***************************************************");
        //System.err.print(DataMagicUtil.getDashboardById("100", "wxrrd_test").getData());
    }

    /**
     * 获得指定原概览中的items
     */
    @Test
    public void test1() {

//        Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById("158", "wxrrd_test_product_new").getData(), Dashboard.class);
//        //System.err.println(dashboard.getCreateTime().toString());
//        List<Item> items = dashboard.getItems();
//        for (Item item : items) {
//            System.out.println(item.getBookmark().getId());
//            System.err.println(item.getBookmark().getData());
//        }          http://data.wxrrd.com:8107/api/dashboards/125?project=wxrrd_datamagic
        List<EventDetail> list = new ArrayList<EventDetail>();
        String url = "http://data.wxrrd.com:8107/api/events/all?project=wxrrd_datamagic";
        BaseResponse response = HttpClientUtils.get(url, null, "sensorsdata-token", getAdminToken("wxrrd_datamagic"));
//        log.debug(response.getData());
        //System.err.println(response.getData());
        String data = response.getData();
        JSONArray array = JSONArray.parseArray(data);
        for (int i=0;i<array.size();i++){
            JSONObject jsonObject = array.getJSONObject(i);
           // System.err.println(jsonObject.toString());
            EventDetail eventDetail = JSONUtil.jsonToObject(jsonObject.toString(), EventDetail.class);
            list.add(eventDetail);
            //System.err.println(eventDetail.toString());
            System.err.println("事件ID："+eventDetail.getId()+"  显示名："+eventDetail.getCname()+"  属性名："+eventDetail.getName());
        }


    }







    /**
     * 替换新概览中的item对象，然后再获取item对象里的书签对象
     * 再将其替换
     */
    @Test
    public void test2() {
        Dashboard dashboard_old = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById("130", "wxrrd_datamagic").getData(), Dashboard.class);
        Dashboard dashboard_new = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById("102", "wxrrd_test").getData(), Dashboard.class);

        List<Item> items = dashboard_old.getItems();
        dashboard_new.setItems(items);

        List<Item> items1 = dashboard_new.getItems();
        for (Item item : items1
                ) {

            Bookmark bookmark = item.getBookmark();
            String type = bookmark.getType();
            String name = bookmark.getName();
            String data = bookmark.getData();
            String createTime = bookmark.getCreateTime();
            String userId = bookmark.getUserId();
            String[] dashboards = bookmark.getDashboards();
            String projectId = bookmark.getProjectId();
            String[] relatedEvents = bookmark.getRelatedEvents();

            dashboards[0] = "102";

            Bookmark newbookMark = new Bookmark();
            newbookMark.setType(type);
            newbookMark.setName(name);
            newbookMark.setData(data);
            //newbookMark.setCreateTime(createTime);
            //newbookMark.setUserId(userId);
            newbookMark.setDashboards(dashboards);
            newbookMark.setRelatedEvents(relatedEvents);
            //newbookMark.setProjectId(projectId);

            String data_new = JSONUtil.objectToJson(newbookMark);

            DataMagicUtil.addBookmark(data_new, "wxrrd_test");
            // System.err.print(item.getBookmark().getData().toString());
        }
    }


    /**
     * 批量转移
     */
    @Test
    public void test3() {
        String[] str = {""};
        List<String> list = new ArrayList<String>(Arrays.asList(str));
        List<String> newId = new ArrayList<String>();
        Map<String, String> map = new HashMap<String, String>();
        List<Item> items = new ArrayList<Item>();
        List<String> tmp = new ArrayList<String>();
        List<List<Item>> itemList = new ArrayList<List<Item>>();
        Dashboard dashboard_old = null;
        Map<String,List<Item>> dashMap = new HashMap<String, List<Item>>();
        for (String id : list) {
            //获取概览
            dashboard_old = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById(id, "wxrrd_test_product_new").getData(), Dashboard.class);
            System.err.print(DataMagicUtil.getDashboardById(id, "wxrrd_test_product_new").getData());
            String dashBoardName = dashboard_old.getName();
            String isPublic = dashboard_old.getIsPublic();
            //生成新概览的request
            Dashboard newDashboard = new Dashboard();
            newDashboard.setName(dashBoardName);
            newDashboard.setIsPublic(isPublic);
            //新增概览
            String idStr = DataMagicUtil.saveDashboard(JSONUtil.objectToJson(newDashboard), "wxd_datamagic").getData();
            Gson gs = new Gson();
            Map<String, Double> idMap = gs.fromJson(idStr, Map.class);
            double d = idMap.get("id");
            int i = (int) d;
            String new_id = String.valueOf(i);
            newId.add(new_id);
            map.put(id, new_id);
            //获取书签
            items = dashboard_old.getItems();
            dashMap.put(dashboard_old.getId(),items);
        }
        for (String str1 : dashMap.keySet()) {
            List<Item> items1 = dashMap.get(str1);
            for (Item item : items1) {

                Bookmark bookmark = item.getBookmark();
                String type = bookmark.getType();
                String name = bookmark.getName();
                String data = bookmark.getData();
                Map<String,Object> map1 = JSONUtil.jsonToObject(data, Map.class);
                List<Map<String,Object>> new_measures = new ArrayList<Map<String, Object>>();
                Map<String, Object> sob = new HashMap<String, Object>();
                if (map1.get("measures")==null){
                    if (map1.get("first_event")==null){
                        Map<String,Object> second_event = (Map<String, Object>) map1.get("second_event");
                        map1.put("second_event",second_event);
                    }else {
                        Map<String,Object> first_event = (Map<String, Object>) map1.get("first_event");
                        map1.put("first_event",first_event);
                    }
                }else {
                    List<Map<String,Object>> measures = (List<Map<String, Object>>) map1.get("measures");
                    for (int i= 0;i<measures.size();i++){
                        Map<String, Object> stringObjectMap = measures.get(i);
                        for (String obj:stringObjectMap.keySet()){
                            String string = stringObjectMap.get(obj).toString();
                            sob.put(obj,string.toLowerCase());
                        }
                        new_measures.add(sob);

                        List<Map<String,Object>> by_fields = (List<Map<String, Object>>) map1.get("by_fields");

                    }
                }
                map1.remove("bookmarkid");
                map1.put("measures",new_measures);
                String new_data = JSONUtil.objectToJson(map1);
                String[] dashboards = bookmark.getDashboards();
                String[] relatedEvents = bookmark.getRelatedEvents();
                //对取得的dashboardId进行处理
                for (String ids : map.keySet()){
                    if (str1.equals(ids)){
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
                String data_new = JSONUtil.objectToJson(newbookMark);
                DataMagicUtil.addBookmark(data_new, "wxd_datamagic");
            }
        }
    }

    @Test
    public void test4() {
        //Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById("243","wxd_datamagic").getData(),Dashboard.class);
        Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById("145","wxrrd_test_product_new").getData(),Dashboard.class);
        List<Item> items = dashboard.getItems();
        for (Item item : items) {
            String[] dashboards = item.getBookmark().getDashboards();
            for (String s:dashboards){
                System.err.print(s);
            }
            System.out.println(item.getBookmark().getName());
            System.err.println(item.getBookmark().getData());
            //System.out.println("bookID: " + item.getBookmark().getId());
            Map<String, Object> bookMap = JSONUtil.jsonToObject(item.getBookmark().getData(), Map.class);
            List<Map<String, Object>> expressionList = (List<Map<String, Object>>) bookMap.get("measures");
            //for (Map<String, Object> expression : expressionList) {
                //System.out.println((List<String>) expression.get("events"));
                //System.out.println((String) expression.get("event_name"));
            //}
        }
    }


    @Test
    public void test5() {
        DataMagicUtil.addBookmark("{\"id\":null,\"type\":\"/segmentation/\",\"name\":\"累计店铺数 - 平台 - 微小店\",\"data\":\"{\\\"measures\\\":[{\\\"event_name\\\":\\\"shop_status1102\\\",\\\"aggregator\\\":\\\"unique\\\"}],\\\"unit\\\":\\\"day\\\",\\\"filter\\\":{\\\"conditions\\\":[{\\\"field\\\":\\\"user.shop_type\\\",\\\"function\\\":\\\"equal\\\",\\\"params\\\":[\\\"微小店\\\"]}]},\\\"chartsType\\\":\\\"line\\\",\\\"sampling_factor\\\":64,\\\"from_date\\\":\\\"2016-11-04\\\",\\\"to_date\\\":\\\"2017-03-12\\\",\\\"bookmarkid\\\":\\\"848\\\",\\\"rollup_date\\\":\\\"false\\\"}\",\"time\":null,\"dashboards\":[\"223\"],\"project\":null,\"create_time\":null,\"user_id\":null,\"related_events\":null,\"project_id\":null}","wxd_datamagic");

    }


    @Test
    public void test6() {
        Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById("220","wxd_datamagic").getData(),Dashboard.class);
        //Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById("158","wxrrd_test_product_new").getData(),Dashboard.class);
        List<Item> items = dashboard.getItems();
        for (Item item : items) {
            System.out.println("______________"+item.getBookmark().getData());
            String data = item.getBookmark().getData();
            List<Map<String,Object>> new_measures = new ArrayList<Map<String, Object>>();
            Map<String, Object> sob = new HashMap<String, Object>();
                Map<String,Object> map1 = JSONUtil.jsonToObject(data, Map.class);
            if (map1.get("measures")==null){
                if (map1.get("first_event")==null){
                    Map<String,Object> second_event = (Map<String, Object>) map1.get("second_event");
                    map1.put("second_event",second_event);
                }else {
                    Map<String,Object> first_event = (Map<String, Object>) map1.get("first_event");
                    map1.put("first_event",first_event);
                }
            }else {
                List<Map<String,Object>> measures = (List<Map<String, Object>>) map1.get("measures");

                for (int i= 0;i<measures.size();i++){
                    Map<String, Object> stringObjectMap = measures.get(i);
                    for (String obj:stringObjectMap.keySet()){
                        String string = stringObjectMap.get(obj).toString();

                        //System.err.println(string.toLowerCase());
                        sob.put(obj,string.toLowerCase());
                    }
                    new_measures.add(sob);
                }
            }


                map1.put("measures",new_measures);
                map1.remove("bookmarkid");
                String new_data = JSONUtil.objectToJson(map1);
            System.err.println(new_data);
            System.err.println("map1:"+map1.size());

        }
    }

    @Test
    public void test7(){
//        for (int i= 2288;i<2297;i++){
//
//            DataMagicUtil.deleteBookmark((""+i),"wxd_datamagic");
//        }

        DataMagicUtil.getBookmarkByAdmin("2340","wxd_datamagic").getData();
    }
}