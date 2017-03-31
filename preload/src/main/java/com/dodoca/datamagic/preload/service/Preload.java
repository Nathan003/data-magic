package com.dodoca.datamagic.preload.service;

import com.dodoca.datamagic.common.ConfigUtils;
import com.dodoca.datamagic.common.ConstantUtil;
import com.dodoca.datamagic.common.DateUtil;
import com.dodoca.datamagic.common.JSONUtil;
import com.dodoca.datamagic.common.model.BaseResponse;
import com.dodoca.datamagic.core.DataMagicUtil;
import com.dodoca.datamagic.core.ParamUtil;
import com.dodoca.datamagic.core.model.Bookmark;
import com.dodoca.datamagic.core.model.Dashboard;
import com.dodoca.datamagic.core.service.DashboardService;
import com.dodoca.datamagic.core.service.impl.DashboardServiceImpl;
import com.dodoca.datamagic.core.vo.Item;
import org.apache.log4j.Logger;

import java.util.HashMap;
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
        int ago = Integer.parseInt(args[1]);//过去多少天的数据
        int limit = 0;//加载多少条
        try {
            limit = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            logger.debug("获取limit失败" + e.getMessage());
        }
        ConfigUtils.load(args[3]);//加载配置文件获取项目
        ConstantUtil.setProject();//设置项目
        System.out.println(ConstantUtil.PROJECT);
        System.out.println(ConstantUtil.getValue("login_uri"));
        //初始化获取shop和bookmarkID
        long start2 = System.currentTimeMillis();
        //固定长度的线程池
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threads);
        //预加载概览里的书签
        System.out.println(ConfigUtils.getArray("dashboardIds"));
        preloading(fixedThreadPool, limit, ago,ConfigUtils.getArray("dashboardIds"),ConfigUtils.getArray("shopIds"));
        long start3 = System.currentTimeMillis();
        logger.debug("概览加载" + (-ago) + "天总耗时：" + (start3 - start2));
        fixedThreadPool.shutdown();


    }

    public static void preloading(ExecutorService executorService, final int limit, final int ago,String[] dashboardIds,String[] shopIds) throws Exception {
        final int finalLimit = limit;
        final int finalAgo = ago;
        final Map<String, Bookmark> bookMarkMap = getLoadBookmarks(dashboardIds);
        if(bookMarkMap.isEmpty()){
            logger.debug("没有需要预加载的书签");
            throw new Exception("没有需要预加载的书签");
        }

        for (final String shopId : shopIds){
            logger.debug("开始加载shop：" + shopId);
            long start = System.currentTimeMillis();
            for (final String bookmarkId : bookMarkMap.keySet()){
                executorService.execute(new Runnable() {
                    public void run() {
                        getBookmarkData(shopId,ConfigUtils.getValue("replaceReg"),bookMarkMap.get(bookmarkId),ago,limit);
                    }
                });
            }

            long start1 = System.currentTimeMillis();
            logger.debug("加载shop：" + shopId + "耗时：" + (start1 - start));
        }
    }

    /**
     * 获取概览里的
     * @param dashboardIds
     * @return
     */
    public static Map<String, Bookmark> getLoadBookmarks(String[] dashboardIds){

        Map<String,Bookmark> rs = new HashMap<String, Bookmark>();

        for (String dashboardId : dashboardIds){
            Dashboard dashboard = dashboardService.get(dashboardId);
            if(null == dashboard){
               continue;
            }
            List<Item> Items = dashboard.getItems();
            for (Item item : Items){
                rs.put(item.getBookmark().getId(),item.getBookmark());
            }
        }
        return rs;

    }
    public static String getBookmarkData(String shopId,String replaceReg,Bookmark bookmark,int agoDays,int limit){

        String data = bookmark.getData();
        data = data.replaceAll("\\\\", "").replaceAll("\\\"\\{", "{").replaceAll("\\}\\\"", "}");
        logger.debug("修改前的参数:" + data);
        Map<String, Object> map = JSONUtil.jsonToObject(data, Map.class);
        if (limit > 0) {
            map.put("limit", limit);
        }

        //替换shopID
        ParamUtil.replaceAllFilter(map, replaceReg, shopId);
        if (agoDays < 0) {
            ParamUtil.replaceFromDate(map, DateUtil.getDate(agoDays));
            ParamUtil.replaceToDate(map, DateUtil.getDate(-1));
        }

        data = JSONUtil.objectToJson(map);
        logger.debug("修改后的参数:" + data);
        //3.请求数据
        long start = System.currentTimeMillis();
        BaseResponse response = DataMagicUtil.reportSegmentation(bookmark.getId(), data);
        long start1 = System.currentTimeMillis();
        logger.debug("replace耗时:" + (start1 - start) + ",bookmarkID:" + bookmark.getId() + ",replace:" + shopId + ",limit:" + limit + ",状态码:" + response.getStatus());
        if(500 == response.getStatus()){
            logger.debug("bookmarkID:" + bookmark.getId() + ",replace:" + shopId + ",limit:" + limit + ",状态码：" + response.getStatus() + "返回的错误信息:" + response.getData() + ",请求的参数:" + data);
        }
        return response.getData();
    }
}
