package com.dodoca.datamagic.core.service;

import com.dodoca.datamagic.core.model.Dashboard;

import java.util.List;

/**
 * Created by lifei on 2017/3/13.
 */
public interface DashboardService {

    /**
     * 获取项目的所有概览
     * @return
     */
    List<Dashboard> get();

    /**
     * 获取某个概览的详情
     * @param id
     * @return
     */
    Dashboard get(String id);
    Dashboard get(String id,String project);
    /**
     * 添加概览
     * @param data 格式：{"name": "转化漏斗分析"，"is_public": 0 // 0表示仅自己可见，1表示所有用户可见 }
     * @return
     */
    String save(String data);

    String save(String data,String project);

    /**
     * 删除概览
     * @param id 概览ID
     * @return
     */
    boolean delete(String id);


}
