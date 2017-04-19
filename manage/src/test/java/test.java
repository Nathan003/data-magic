import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dodoca.datamagic.common.utils.HttpClientUtils;
import com.dodoca.datamagic.common.utils.JSONUtil;
import com.dodoca.datamagic.common.model.BaseResponse;
import com.dodoca.datamagic.core.DataMagicUtil;
import com.dodoca.datamagic.core.ParamUtil;
import com.dodoca.datamagic.core.model.Bookmark;
import com.dodoca.datamagic.core.model.Dashboard;
import com.dodoca.datamagic.core.vo.EventDetail;
import com.dodoca.datamagic.core.vo.Item;
import com.dodoca.datamagic.es.bean.bean.EsClusterBean;
import com.google.gson.Gson;
import org.elasticsearch.action.index.IndexResponse;
import org.junit.Test;

import java.util.*;

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
        //Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById("146","wxrrd_test_product_new").getData(),Dashboard.class);
        Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById("258","wxrrd_datamagic").getData(),Dashboard.class);
        List<Item> items = dashboard.getItems();
        for (Item item : items) {
            String[] dashboards = item.getBookmark().getDashboards();
            //System.out.println(item.getBookmark().getName());
            String data=item.getBookmark().getData();
            data = data.replaceAll("\\\\", "").replaceAll("\\\"\\{", "{").replaceAll("\\}\\\"", "}");
            Map<String, Object> map = JSONUtil.jsonToObject(data, Map.class);
            Map filter =(Map) map.get("filter");
            if (filter.size() > 0 ){
                List<Map<String,Object>> conditions = (List)filter.get("conditions");
                if (conditions.toString().contains("SHOP_id")){
                    ParamUtil.replaceAllFilter(map,"event.$Anything.SHOP_id","111111");
                    data = JSONUtil.objectToJson(map);
                    BaseResponse response = DataMagicUtil.reportSegmentation(item.getBookmark().getId(), data);
                    JSONObject jsonData = JSONObject.parseObject(response.getData());
                    IndexResponse indexResponse = EsClusterBean
                            .getInstance()
                            .getTransportClient()
                            .prepareIndex(EsClusterBean.getInstance().getIndex(),
                                    EsClusterBean.getInstance().getTypes(), "")
                            .setSource(jsonData.toString()).get();
                    if (indexResponse.isCreated()){
                        System.err.println("插入成功！");
                    }else {
                        System.err.println("插入失败！");
                    }
                    System.err.println("回应"+response.getData());
                }else {
                    continue;
                }
            }else {
                BaseResponse response = DataMagicUtil.reportSegmentation(item.getBookmark().getId(), data);
                JSONObject jsonData = JSONObject.parseObject(response.getData());
                IndexResponse indexResponse = EsClusterBean
                        .getInstance()
                        .getTransportClient()
                        .prepareIndex(EsClusterBean.getInstance().getIndex(),
                                EsClusterBean.getInstance().getTypes(), "")
                        .setSource(jsonData.toString()).get();
                if (indexResponse.isCreated()){
                    System.err.println("插入成功！");
                }else {
                    System.err.println("插入失败！");
                }
                System.err.println("回应"+response.getData());
                continue;
            }
        }
    }

    @Test
    public void test10(){
        Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById("258","wxrrd_datamagic").getData(),Dashboard.class);
        List<Item> items = dashboard.getItems();
        for (Item item : items) {
            String data=item.getBookmark().getData();
            System.err.println(data);
        }
    }

    @Test
    public void test11(){
        Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById("146","wxrrd_test_product_new").getData(),Dashboard.class);
        List<Item> items = dashboard.getItems();
        for (Item item : items) {
            String data=item.getBookmark().getData();
            System.err.println("请求："+data);
            BaseResponse baseResponse = DataMagicUtil.reportSegmentation(item.getBookmark().getId(), data, "wxrrd_test_product_new");
            System.err.println("回应："+baseResponse.getData());
        }
    }

    @Test
    public void test12(){
        Bookmark bookmark = JSONUtil.jsonToObject(DataMagicUtil.getBookmarkByAdmin("618", "wxrrd_test_product_new").getData(), Bookmark.class);
        String data = bookmark.getData();
        data = data.replaceAll("\\\\", "").replaceAll("\\\"\\{", "{").replaceAll("\\}\\\"", "}");
        System.err.println(data);
        Map<String, Object> map = JSONUtil.jsonToObject(data, Map.class);
        ParamUtil.replaceAllFilter(map,"user.TUIJIAN_SHOP_shop_line" , "神盾局");
        data = JSONUtil.objectToJson(map);
        System.err.println(data);
        BaseResponse response = DataMagicUtil.reportSegmentation("618", data,"wxrrd_test_product_new");
        System.err.println(response.getData());
    }

    @Test
    public void test5() {
        BaseResponse wxd_datamagic = DataMagicUtil.addBookmark("{\"id\":null,\"type\":\"/segmentation/\",\"name\":\"下单金额GMV (含未支付)  - 平台 - 微小店\",\"data\":\"{\\\"measures\\\":[{\\\"field\\\":\\\"event.shop_sale_order1019.real_amount\\\",\\\"aggregator\\\":\\\"SUM\\\",\\\"event_name\\\":\\\"shop_sale_order1019\\\"}],\\\"unit\\\":\\\"day\\\",\\\"filter\\\":{\\\"conditions\\\":[{\\\"field\\\":\\\"user.shop_type\\\",\\\"function\\\":\\\"equal\\\",\\\"params\\\":[\\\"微小店\\\"]}]},\\\"chartsType\\\":\\\"line\\\",\\\"sampling_factor\\\":64,\\\"from_date\\\":\\\"2016-11-04\\\",\\\"to_date\\\":\\\"2017-03-12\\\",\\\"bookmarkid\\\":\\\"848\\\",\\\"rollup_date\\\":\\\"false\\\"}\",\"time\":null,\"dashboards\":[\"249\"],\"project\":null,\"create_time\":null,\"user_id\":null,\"related_events\":null,\"project_id\":null}", "wxd_datamagic");
        String idStr = wxd_datamagic.getData();
        Gson gs = new Gson();
        Map<String, Double> idMap = gs.fromJson(idStr, Map.class);
        double d = idMap.get("id");
        int i = (int) d;
        String id = String.valueOf(i);
        System.err.println(id);

    }


    @Test
    public void test6() {
        //Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById("220","wxd_datamagic").getData(),Dashboard.class);
        Dashboard dashboard = JSONUtil.jsonToObject(DataMagicUtil.getDashboardById("158","wxrrd_test_product_new").getData(),Dashboard.class);
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
        for (int i= 2423;i<=2452;i++){

            DataMagicUtil.deleteBookmark((""+i),"wxd_datamagic");
        }

        //DataMagicUtil.getBookmarkByAdmin("2356","wxd_datamagic").getData();
//        ArrayList<String> strings = new ArrayList<String>();
//        strings.add("1");
//        strings.add("2");
//        System.err.println(strings);
        //String jsonString = "[{\"virtual\":false,\"visible\":true,\"pinyin\":\"confirm_order_goods_ump\",\"name\":\"confirm_order_goods_ump\",\"cname\":\"confirm_order_goods_ump\",\"id\":713,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"confirm_order_ump\",\"name\":\"confirm_order_ump\",\"cname\":\"confirm_order_ump\",\"id\":714,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"guider_get_comission\",\"name\":\"guider_get_comission\",\"cname\":\"guider_get_comission\",\"id\":837,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"pay_order_goods_ump\",\"name\":\"pay_order_goods_ump\",\"cname\":\"pay_order_goods_ump\",\"id\":715,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"pay_order_ump\",\"name\":\"pay_order_ump\",\"cname\":\"pay_order_ump\",\"id\":716,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"ge ren zhong xin liu lan shi jian\",\"name\":\"personal_center_browse1021\",\"cname\":\"个人中心浏览事件\",\"id\":590,\"tag\":[]},{\"virtual\":true,\"visible\":true,\"pinyin\":\"ren yi ye mian liu lan  PV\",\"name\":\"PV\",\"cname\":\"任意页面浏览 PV\",\"id\":510,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"qi ta liu lan shi jian\",\"name\":\"other_browse1021\",\"cname\":\"其他浏览事件\",\"id\":594,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"lie biao liu lan shi jian\",\"name\":\"list_browse1021\",\"cname\":\"列表浏览事件\",\"id\":585,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"qu xiao ding dan\",\"name\":\"cancel_order1022\",\"cname\":\"取消订单\",\"id\":597,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pin liu lan shi jian\",\"name\":\"goods_browse1021\",\"cname\":\"商品浏览事件\",\"id\":583,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu KPI\",\"name\":\"shop_status_kpi11\",\"cname\":\"商铺KPI\",\"id\":628,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu zhu ye liu lan shi jian\",\"name\":\"shop_homepage_browse1021\",\"cname\":\"商铺主页浏览事件\",\"id\":592,\"tag\":[]},{\"virtual\":true,\"visible\":true,\"pinyin\":\"shang pu ru zhu _ wei xiao dian\",\"name\":\"shop_created_wxd\",\"cname\":\"商铺入驻_微小店\",\"id\":737,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu ru zhu shi jian\",\"name\":\"shop_created\",\"cname\":\"商铺入驻事件\",\"id\":736,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu chuang jian pin tuan\",\"name\":\"shop_create_pintuan1019\",\"cname\":\"商铺创建拼团\",\"id\":529,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu chuang jian miao sha\",\"name\":\"shop_create_skill1019\",\"cname\":\"商铺创建秒杀\",\"id\":512,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu mai dan\",\"name\":\"shop_sale_order1019\",\"cname\":\"商铺卖单\",\"id\":544,\"tag\":[]},{\"virtual\":true,\"visible\":true,\"pinyin\":\"shang pu mai dan qu chu huo dao fu kuan he da e ding dan\",\"name\":\"shop_receivables_order_online_amountLT50K\",\"cname\":\"商铺卖单去除货到付款和大额订单\",\"id\":643,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu mai dan xiang qing\",\"name\":\"shop_sale_order_detail1019\",\"cname\":\"商铺卖单详情\",\"id\":548,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu fa huo\",\"name\":\"shop_send_goods1019\",\"cname\":\"商铺发货\",\"id\":545,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu qu xiao ding dan\",\"name\":\"shop_back_order1022\",\"cname\":\"商铺取消订单\",\"id\":596,\"tag\":[]},{\"virtual\":true,\"visible\":true,\"pinyin\":\"shang pu qu xiao ding dan yi zhi fu _ qu chu gao e ding dan\",\"name\":\"shop_back_order_ispaid\",\"cname\":\"商铺取消订单已支付_去除高额订单\",\"id\":642,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu tuan yuan can tuan shi bai tui kuan\",\"name\":\"shop_pintuan_member_join_refund1019\",\"cname\":\"商铺团员参团失败退款\",\"id\":540,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu pin tuan tuan yuan can yu\",\"name\":\"shop_pintuan_member_join1019\",\"cname\":\"商铺拼团团员参与\",\"id\":537,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu pin tuan tuan yuan can yu zhi fu shi bai\",\"name\":\"shop_pintuan_member_join_pay_fail1019\",\"cname\":\"商铺拼团团员参与支付失败\",\"id\":538,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu pin tuan tuan yuan can yu zhi fu cheng gong\",\"name\":\"shop_pintuan_member_join_pay1019\",\"cname\":\"商铺拼团团员参与支付成功\",\"id\":539,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu pin tuan tuan yuan can tuan cheng gong\",\"name\":\"shop_pintuan_member_join_success1019\",\"cname\":\"商铺拼团团员参团成功\",\"id\":541,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu pin tuan tuan chang fa qi\",\"name\":\"shop_pintuan_initiate1019\",\"cname\":\"商铺拼团团长发起\",\"id\":531,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu pin tuan tuan chang fa qi zhong\",\"name\":\"shop_pintuan_initiate_ing1019\",\"cname\":\"商铺拼团团长发起中\",\"id\":533,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu pin tuan tuan chang fa qi shi bai\",\"name\":\"shop_pintuan_initiate_fail1019\",\"cname\":\"商铺拼团团长发起失败\",\"id\":532,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu pin tuan tuan chang fa qi cheng gong\",\"name\":\"shop_pintuan_initiate_success1019\",\"cname\":\"商铺拼团团长发起成功\",\"id\":534,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu pin tuan ceng ji she zhi\",\"name\":\"shop_pintuan_item1019\",\"cname\":\"商铺拼团层级设置\",\"id\":536,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu zhi fu yong jin\",\"name\":\"shop_pay_guider_amount1019\",\"cname\":\"商铺支付佣金\",\"id\":547,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu shou kuan\",\"name\":\"shop_receivables_order1019\",\"cname\":\"商铺收款\",\"id\":543,\"tag\":[]},{\"virtual\":true,\"visible\":true,\"pinyin\":\"shang pu shou kuan _ qu chu huo dao fu kuan\",\"name\":\"shop_receivables_order_online\",\"cname\":\"商铺收款_去除货到付款\",\"id\":619,\"tag\":[]},{\"virtual\":true,\"visible\":true,\"pinyin\":\"shang pu shou kuan qu chu gao e ding dan\",\"name\":\"shop_receivables_order_amountLT50K\",\"cname\":\"商铺收款去除高额订单\",\"id\":629,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu shou kuan xiang qing\",\"name\":\"shop_receivables_order_detail1019\",\"cname\":\"商铺收款详情\",\"id\":546,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu xin zeng tui ke\",\"name\":\"shop_insert_guider1019\",\"cname\":\"商铺新增推客\",\"id\":542,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu zhu ce\",\"name\":\"shop_register\",\"cname\":\"商铺注册\",\"id\":732,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu zhu ce cheng gong\",\"name\":\"shop_register_success\",\"cname\":\"商铺注册成功\",\"id\":731,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu zhuang tai\",\"name\":\"shop_status1102\",\"cname\":\"商铺状态\",\"id\":626,\"tag\":[]},{\"virtual\":true,\"visible\":true,\"pinyin\":\"shang pu miao sha chuang jian he can yu\",\"name\":\"shop_skill_create_and_join\",\"cname\":\"商铺秒杀创建和参与\",\"id\":613,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu miao sha yong hu can yu\",\"name\":\"shop_skill_join1019\",\"cname\":\"商铺秒杀用户参与\",\"id\":513,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu miao sha yong hu can yu yi qu xiao\",\"name\":\"shop_skill_member_cancel1019\",\"cname\":\"商铺秒杀用户参与已取消\",\"id\":518,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"shang pu miao sha yong hu can yu yi zhi fu\",\"name\":\"shop_skill_member_pay1019\",\"cname\":\"商铺秒杀用户参与已支付\",\"id\":521,\"tag\":[]},{\"virtual\":true,\"visible\":true,\"pinyin\":\"shang pu jie duan 0\",\"name\":\"shop_status_stage_0\",\"cname\":\"商铺阶段0\",\"id\":644,\"tag\":[]},{\"virtual\":true,\"visible\":true,\"pinyin\":\"shang pu jie duan 1\",\"name\":\"shop_status_stage_1\",\"cname\":\"商铺阶段1\",\"id\":645,\"tag\":[]},{\"virtual\":true,\"visible\":true,\"pinyin\":\"shang pu jie duan 2\",\"name\":\"shopd_status_stage_2\",\"cname\":\"商铺阶段2\",\"id\":646,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"cheng wei tui ke\",\"name\":\"become_guider1018\",\"cname\":\"成为推客\",\"id\":490,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"tui ke zhong xin liu lan shi jian\",\"name\":\"guider_center_browse1021\",\"cname\":\"推客中心浏览事件\",\"id\":584,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"sou suo jie guo ye liu lan shi jian\",\"name\":\"search_result_browse1021\",\"cname\":\"搜索结果页浏览事件\",\"id\":593,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"zhi fu bao zhi fu liu lan shi jian\",\"name\":\"alipay_pay_browse1021\",\"cname\":\"支付宝支付浏览事件\",\"id\":581,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"zhi fu jie guo liu lan shi jian\",\"name\":\"pay_result_browse1021\",\"cname\":\"支付结果浏览事件\",\"id\":589,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"zhi fu ding dan\",\"name\":\"pay_order1018\",\"cname\":\"支付订单\",\"id\":498,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"huo dong liu lan shi jian\",\"name\":\"activity_browse1021\",\"cname\":\"活动浏览事件\",\"id\":580,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"yong hu can yu pin tuan\",\"name\":\"pintuan_member_join\",\"cname\":\"用户参与拼团\",\"id\":637,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"yong hu can yu pin tuan zhi fu shi bai\",\"name\":\"pintuan_member_join_pay_fail\",\"cname\":\"用户参与拼团支付失败\",\"id\":638,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"yong hu can yu miao sha\",\"name\":\"skill_join\",\"cname\":\"用户参与秒杀\",\"id\":630,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"yong hu can yu miao sha yi qu xiao\",\"name\":\"skill_member_cancel\",\"cname\":\"用户参与秒杀已取消\",\"id\":631,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"yong hu can yu miao sha yi zhi fu\",\"name\":\"skill_member_pay\",\"cname\":\"用户参与秒杀已支付\",\"id\":632,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"yong hu can tuan shi bai tui kuan\",\"name\":\"pintuan_member_join_refund\",\"cname\":\"用户参团失败退款\",\"id\":640,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"yong hu can tuan cheng gong\",\"name\":\"pintuan_member_join_success\",\"cname\":\"用户参团成功\",\"id\":641,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"yong hu fa qi pin tuan\",\"name\":\"pintuan_initiate\",\"cname\":\"用户发起拼团\",\"id\":633,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"yong hu fa qi pin tuan shi bai\",\"name\":\"pintuan_initiate_fail\",\"cname\":\"用户发起拼团失败\",\"id\":634,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"yong hu fa qi pin tuan cheng gong\",\"name\":\"pintuan_initiate_success\",\"cname\":\"用户发起拼团成功\",\"id\":636,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"yong hu fa qi pin tuan jin xing zhong\",\"name\":\"pintuan_initiate_ing\",\"cname\":\"用户发起拼团进行中\",\"id\":635,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"yong hu pin tuan can yu zhi fu cheng gong\",\"name\":\"pintuan_member_join_pay\",\"cname\":\"用户拼团参与支付成功\",\"id\":639,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"que ren ding dan\",\"name\":\"confirm_order1018\",\"cname\":\"确认订单\",\"id\":496,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"que ren ding dan xiang qing\",\"name\":\"confirm_order_detail1018\",\"cname\":\"确认订单详情\",\"id\":507,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"huo qu yong jin\",\"name\":\"get_guider_amount1018\",\"cname\":\"获取佣金\",\"id\":503,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"huo qu yong jin xin\",\"name\":\"get_guider_amount_new\",\"cname\":\"获取佣金新\",\"id\":738,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"ding dan lie biao liu lan shi jian\",\"name\":\"order_list_browse1021\",\"cname\":\"订单列表浏览事件\",\"id\":588,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"ding dan yi fa huo\",\"name\":\"send_goods1018\",\"cname\":\"订单已发货\",\"id\":500,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"ding dan zhi fu xiang qing\",\"name\":\"pay_order_detail1018\",\"cname\":\"订单支付详情\",\"id\":508,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"ding dan ping lun liu lan shi jian\",\"name\":\"order_comment_browse1021\",\"cname\":\"订单评论浏览事件\",\"id\":586,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"ding dan xiang qing liu lan shi jian\",\"name\":\"order_detail_browse1021\",\"cname\":\"订单详情浏览事件\",\"id\":587,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"gou wu che liu lan shi jian\",\"name\":\"cart_browse1021\",\"cname\":\"购物车浏览事件\",\"id\":582,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"tui kuan\",\"name\":\"refund1018\",\"cname\":\"退款\",\"id\":509,\"tag\":[]},{\"virtual\":false,\"visible\":true,\"pinyin\":\"tui kuan liu lan shi jian\",\"name\":\"refund_browse1021\",\"cname\":\"退款浏览事件\",\"id\":591,\"tag\":[]}]";
        // DataMagicUtil.deleteBookmark("2356","wxd_datamagic");
    }

}