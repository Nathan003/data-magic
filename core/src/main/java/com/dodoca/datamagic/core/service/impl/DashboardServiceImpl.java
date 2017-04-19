package com.dodoca.datamagic.core.service.impl;

import com.dodoca.datamagic.common.utils.JSONUtil;
import com.dodoca.datamagic.core.DataMagicUtil;
import com.dodoca.datamagic.core.model.Dashboard;
import com.dodoca.datamagic.core.service.DashboardService;
import com.dodoca.datamagic.core.vo.Item;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

/**
 * Created by lifei on 2017/3/13.
 */
public class DashboardServiceImpl implements DashboardService {

    public List<Dashboard> get() {

        List<Dashboard> list = (List<Dashboard>) DataMagicUtil.getDashboardsAll();

        return list;
    }

    public Dashboard get(String id) {

        Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById(id).getData(), Dashboard.class);
        return dashboard;
    }

    public Dashboard get(String id,String project) {

        Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById(id,project).getData(),Dashboard.class);
        return dashboard;
    }

    public String save(String data) {
        String idStr = DataMagicUtil.saveDashboard(data).getData();
        Gson gs = new Gson();
        Map<String, Double> idMap = gs.fromJson(idStr, Map.class);
        double d = idMap.get("id");
        int i = (int) d;
        String id = String.valueOf(i);
        return id;
    }

    public String save(String data,String project) {
        String idStr = DataMagicUtil.saveDashboard(data,project).getData();
        Gson gs = new Gson();
        Map<String, Double> idMap = gs.fromJson(idStr, Map.class);
        double d = idMap.get("id");
        int i = (int) d;
        String id = String.valueOf(i);
        return id;
    }

    public boolean delete(String id) {
        int status = DataMagicUtil.deleteDashboard(id).getStatus();

        return status == 200;
    }

    public static void main(String[] args) {
//        Dashboard dashboard = new DashboardServiceImpl().get("125");
//        List<Item> items = dashboard.getItems();
//        for (Item item : items){
//            System.out.println(item.getBookmark().getName() + ":" + item.getConfig());
//        }
        String str = "{\"bookmark\": {\"id\": 610,\"type\": \"/segmentation/\",\"name\": \"当日收款 - KA乱世佳人\",\"data\": \"\",\"create_time\": \"2016-11-19 09:04:38\",\"user_id\": 38,\"dashboards\": [109],\"project_id\": 22},\"config\": \"{}\",\"bookmark_id\": 0 }";

//        System.out.println(JSONUtil.objectToJson(new Item()));
        Item item = JSONUtil.jsonToObject(str,Item.class);
    }
}
