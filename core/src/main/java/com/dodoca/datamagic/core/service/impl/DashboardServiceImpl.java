package com.dodoca.datamagic.core.service.impl;

import com.dodoca.datamagic.common.JSONUtil;
import com.dodoca.datamagic.core.DataMagicUtil;
import com.dodoca.datamagic.core.model.Dashboard;
import com.dodoca.datamagic.core.service.DashboardService;
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

        Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById(id).getData(),Dashboard.class);
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

}
