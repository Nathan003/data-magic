package com.dodoca.datamagic.preload.service;

import com.dodoca.datamagic.utils.ConfigUtils;
import com.dodoca.datamagic.utils.ConstantUtil;
import com.dodoca.datamagic.utils.JSONUtil;
import com.dodoca.datamagic.core.DataMagicUtil;
import com.dodoca.datamagic.core.ParamUtil;
import com.dodoca.datamagic.core.service.DashboardService;
import com.dodoca.datamagic.core.service.impl.DashboardServiceImpl;
import com.dodoca.datamagic.utils.model.BaseResponse;
import com.dodoca.datamagic.utils.model.Bookmark;
import com.dodoca.datamagic.utils.model.Dashboard;
import com.dodoca.datamagic.utils.vo.Item;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lifei on 2017/3/13.
 */
public class Preload {

    private static Logger logger = Logger.getLogger(Preload.class);
    private static DashboardService dashboardService = new DashboardServiceImpl();

    public static void main(String[] args) throws Exception {

        //项目，shop，概览，时长，条数
        int threads = Integer.parseInt(args[0]);//几个线程
        int limit = 0;//加载多少条
        try {
            limit = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            logger.debug("获取limit失败" + e.getMessage());
        }
        final int finalLimit = limit;
        ConfigUtils.load(args[5]);//加载配置文件
        ConstantUtil.setProject();//设置项目
        //初始化获取shop和bookmarkID
        long start2 = System.currentTimeMillis();
        //固定长度的线程池
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threads);
        //预加载概览里的书签
        System.out.println(ConfigUtils.getArray(args[2]));//加载概览
        final String[] newValues = ConfigUtils.getArray(args[3]);//加载替换的新值
        final String replaceReg = ConfigUtils.getValue(args[4]);//记载替换的正则值
        final String importDay = args[6];//要预加载的时间范围
        //遍历概览
        for (String dashboardId : ConfigUtils.getArray(args[2])){
            final Dashboard dashboard = dashboardService.get(dashboardId);
            if(null == dashboard){
                continue;
            }
            //遍历要替换的值
            for (final String newVlaue : newValues) {
                fixedThreadPool.execute(new Runnable() {
                    public void run() {
                        preloadDashboard(dashboard, replaceReg, newVlaue, finalLimit,importDay);
                    }
                });
            }
        }
        long start3 = System.currentTimeMillis();
        logger.debug("概览加载总耗时：" + (start3 - start2));
        fixedThreadPool.shutdown();

    }

    /**
     * 加载概览里的书签，用newValues里的值替换以replaceReg结尾的filter的值
     * @param dashboard
     * @param replaceReg
     * @param newValue
     * @param limit
     */
    public static void preloadDashboard(Dashboard dashboard,String replaceReg,String newValue,int limit,String importDay){
        List<Item> itemList = dashboard.getItems();
        for (Item item : itemList){
            if(JSONUtil.jsonToObject(item.getConfig(),Map.class).isEmpty()){
                item.setConfig(dashboard.getConfig());
            }
            //设置要导入的时间范围
            boolean isAppend = true;
            item.setConfig(importDay,isAppend);
            preloadItem(item, replaceReg, newValue, limit);
        }

    }

    /**
     * 根据item的配置加载书签里的数据
     * @param item
     * @param replaceReg
     * @param newValue
     * @param limit
     */
    public static void preloadItem(Item item,String replaceReg,String newValue,int limit){

        preloadBookMark(item.getBookmark(),replaceReg,newValue,item.getFromDate(),item.getEndDate(),limit);
    }

    public static String preloadBookMark(Bookmark bookmark, String replaceReg, String newValue, String fromDate, String endDate, int limit){

        String data = bookmark.getData();
        data = data.replaceAll("\\\\", "").replaceAll("\\\"\\{", "{").replaceAll("\\}\\\"", "}");
        logger.debug("修改前的参数:" + data);
        Map<String, Object> map = JSONUtil.jsonToObject(data, Map.class);
        //设置加载的条数
        if (limit > 0) {
            map.put("limit", limit);
        }
        //替换时间
        ParamUtil.replaceFromDate(map, fromDate);
        ParamUtil.replaceToDate(map, endDate);
        //替换过滤值
        ParamUtil.replaceAllFilter(map, replaceReg, newValue);

        data = JSONUtil.objectToJson(map);
        logger.debug("修改后的参数:" + data);
        //3.请求数据
        long start = System.currentTimeMillis();
        BaseResponse response = DataMagicUtil.reportSegmentation(bookmark.getId(), data);
        long start1 = System.currentTimeMillis();
        logger.debug("replace耗时:" + (start1 - start) + ",bookmarkID:" + bookmark.getId() + ",replace:" + newValue + ",limit:" + limit + ",状态码:" + response.getStatus());
        if(500 == response.getStatus()){
            logger.debug("bookmarkID:" + bookmark.getId() + ",replace:" + newValue + ",limit:" + limit + ",状态码：" + response.getStatus() + "返回的错误信息:" + response.getData() + ",请求的参数:" + data);
        }
        return response.getData();
    }
}
