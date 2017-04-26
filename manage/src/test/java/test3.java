import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dodoca.datamagic.core.DataMagicUtil;
import com.dodoca.datamagic.utils.HttpClientUtils;
import com.dodoca.datamagic.utils.JSONUtil;
import com.dodoca.datamagic.utils.model.BaseResponse ;

import com.dodoca.datamagic.utils.model.Bookmark;
import com.dodoca.datamagic.utils.model.Dashboard;
import com.dodoca.datamagic.utils.vo.*;
import com.google.gson.Gson;
import org.junit.Test;
import org.w3c.dom.ls.LSException;
import scala.collection.parallel.ParIterableLike;

import java.util.*;

import static com.dodoca.datamagic.core.DataMagicUtil.getAdminToken;

/**
 * Created by admin on 2017/3/28.
 */
public class test3 {
    @Test
    public void eventsDetail(){
        String[] name = {"shop_created_wxd","shop_sale_order1019","shop_receivables_order1019","shop_sale_order_detail1019",
                "shop_receivables_order_detail1019","shop_register_success","shop_status1102","shop_register"};
        List<String> eventNameList = Arrays.asList(name);
        List<String> idList = new ArrayList<String>();
        List<EventDetail> list = new ArrayList<EventDetail>();
        String url = "http://data.wxrrd.com:8107/api/events/all?project=wxrrd_test_product_new";
        BaseResponse response = HttpClientUtils.get(url, null, "sensorsdata-token", getAdminToken("wxrrd_test_product_new"));
        String data = response.getData();
        System.err.println( data );
        JSONArray array = JSONArray.parseArray(data);
        for (int i=0;i<array.size();i++){
            JSONObject jsonObject = array.getJSONObject(i);
            EventDetail eventDetail = JSONUtil.jsonToObject(jsonObject.toString(), EventDetail.class);
            list.add(eventDetail);
        }
        for (int j = 0; j < list.size();j++){
            if (eventNameList.contains(list.get(j).getName())){
                idList.add(list.get(j).getId());
            }
        }
        EventProperty eventProperty = new EventProperty();
        List<EventProperty> eventProperties = new ArrayList<EventProperty>();
        for (int i=0 ;i< idList.size();i++){
            String url1 = "http://data.wxrrd.com:8107/api/event/"+idList.get(i)+"/properties?cache=false&show_all=true&project=wxrrd_test_product_new";
            BaseResponse response1 = HttpClientUtils.get(url1, null, "sensorsdata-token", getAdminToken("wxrrd_test_product_new"));
            Map<String,List<Object>> map = JSONUtil.jsonToObject(response1.getData(), Map.class);
            List<Object> event = map.get("event");
            for (int j=0;j<event.size();j++){
                Map<String,Object> map1 = (Map<String, Object>) event.get(j);
                eventProperty.setCname((String) map1.get("cname"));
                eventProperty.setPropertyId(map1.get("id").toString());
                eventProperty.setIsInUse(map1.get("is_in_use").toString());
                if (!map1.get("data_type").equals("string")){
                    eventProperty.setUnit(map1.get("unit").toString());
                }
                eventProperties.add(eventProperty);
            }
        }
        EventRequest eventRequest = new EventRequest();
        //保存
//        for (int j = 0; j < list.size();j++){
//            eventRequest.setCname(list.get(j).getCname());
//            eventRequest.setVisible(list.get(j).getVisible());
//            eventRequest.setEventId(list.get(j).getId());
//            eventRequest.setTag(list.get(j).getTag());
//            eventRequest.setEventProperty(eventProperties);
//            String url1 = "http://data.wxrrd.com:8107/api/event/"+list.get(j).getId()+"/meta?project=wxrrd_test_product_new";
//            String eventdata = JSONUtil.objectToJson(eventRequest);
//            HttpClientUtils.post(url1, eventdata, "sensorsdata-token", getAdminToken("wxrrd_test_product_new"));
//        }
    }


    @Test
    public void userProperty(){
        String url = "http://data.wxrrd.com:8107/api/property/user/properties?show_all=true&cache=false&project=wxrrd_test_product_new";
        String url1 = "http://data.wxrrd.com:8107/api/property/user/properties&project=wxrrd_test_product_new";
        BaseResponse response = HttpClientUtils.get(url, null, "sensorsdata-token", getAdminToken("wxrrd_test_product_new"));
        System.err.println( response.getData() );
        List<Map<String,Object>> list = JSONUtil.jsonToObject(response.getData(), List.class);
        List<UserProperty> userProperties = new ArrayList<UserProperty>();
        for (int i = 0;i<list.size();i++){
            Map<String, Object> map = list.get(i);
            System.err.println("显示名称:"+map.get("cname"));
            System.err.println("是否在使用："+map.get("is_in_use"));
            System.err.println("id："+map.get("id"));
            UserProperty userProperty = new UserProperty();
            userProperty.setCname(map.get("cname").toString());
            userProperty.setIsInUse(map.get("is_in_use").toString());
            userProperty.setPropertyId(map.get("id").toString());
            userProperties.add(userProperty);
        }
        for (int i=0; i<userProperties.size();i++) {
            UserProperty tmp = userProperties.get( i );
            System.err.println( tmp.getPropertyId() );

        }
        String userdata = JSONUtil.objectToJson(userProperties);
        userdata = "{\"property\":" + userdata + "}";
        System.err.println( userdata );
        //System.err.println( HttpClientUtils.post(url, userdata, "sensorsdata-token", getAdminToken("wxrrd_test_product_new")).getData() );
    }



    @Test
    public void  zhibiao(){
        String event = "pay_order1207";
        String project ="wxrrd_datamagic";
        String url = "http://data.wxrrd.com:8107/api/event/properties?events="+event+"&method=mixed&project="+project;
        List<String> cnameList = new ArrayList<String>(  );
        BaseResponse baseResponse = HttpClientUtils.get( url, null, "sensorsdata-token", getAdminToken( project ) );
//        String str = "订单套餐优惠金额\n" +
//                "订单主键ID\n" +
//                "是否是人人电商\n" +
//                "商铺二级行业名称（当时）\n" +
//                "订单夺宝优惠金额\n" +
//                "商铺分类主键ID\n" +
//                "订单余额抵扣金额\n" +
//                "订单特殊优惠总金额\n" +
//                "订单通用总优惠\n" +
//                "订单砍价优惠金额\n" +
//                "订单所有优惠活动类型\n" +
//                "订单商家改价优惠金额\n" +
//                "用户主键ID\n" +
//                "订单会员优惠金额\n" +
//                "商铺主键ID\n" +
//                "用户public_id\n" +
//                "订单所有优惠活动类型幂和\n" +
//                "订单竞猜优惠金额\n" +
//                "订单帮免优惠金额\n" +
//                "推客小店名称\n" +
//                "订单创建时间\n" +
//                "订单复购优惠金额\n" +
//                "订单第三方支付金额\n" +
//                "订单满就送优惠金额\n" +
//                "订单流水号\n" +
//                "IP\n" +
//                "商铺一级行业名称（当时）\n" +
//                "推客小店主键ID\n" +
//                "订单优惠总金额\n" +
//                "订单拼团优惠金额\n" +
//                "订单实付金额\n" +
//                "订单状态\n" +
//                "订单优惠券优惠金额\n" +
//                "订单是否有效\n" +
//                "订单余额抵扣\n" +
//                "订单支付方式\n" +
//                "订单原价\n" +
//                "订单秒杀优惠金额\n" +
//                "订单积分抵扣优惠金额\n" +
//                "订单运费\n" +
//                "商铺名称\n" +
//                "订单成交金额\n" +
//                "订单拍卖优惠金额\n" +
//                "订单商品总数量\n";
//
//        List<String> strings = Arrays.asList( str.split( "\n" ) );
        String data = baseResponse.getData();

        Map<String,Object> objectMap = JSONUtil.jsonToObject( data,Map.class );

        Map<String,Object> original = (Map<String, Object>) objectMap.get( "original" );

        Map<String,List<Object>> events = (Map<String, List<Object>>) original.get( event );

        List<Object> eventList = events.get( "event" );


        for (int i = 0; i < eventList.size();i++ ){
            Map<String,Object> zhibiaoMap = (Map<String, Object>) eventList.get( i );
            if ((Boolean)(zhibiaoMap.get( "is_dimension" ))){
                String cname = (String) zhibiaoMap.get( "cname" );
                cnameList.add( cname );
                System.err.println( cname );
            }
        }
//        for (int i = 0;i <cnameList.size();i++){
//            String s = cnameList.get( i );
//            if (strings.contains( s )){
//                System.out.println( s+"在指标集合中！");
//            }else {
//                System.err.println( s+"不属于指标集合！" );
//            }
//        }
        //System.err.println( eventList );
    }


    @Test
    public void test(){
        String target = "pay_order1207";
        String project = "wxrrd_datamagic";
        Set dashboardSet = new TreeSet(  );
        Set bookmarkSet = new TreeSet(  );
        ArrayList<String> ids = new ArrayList<String>();
        String data = DataMagicUtil.getDashboardsAll( project ).getData();
        List list = JSONUtil.jsonToObject( data, List.class );
        for (int i =0 ;i < list.size();i++){
            Map<String,Object> map = (Map<String, Object>) list.get( i );
            ids.add( ""+map.get( "id" ) );
            //System.err.println( map.get( "id" ) );
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
            //System.err.println( Arrays.toString( dashboards ) );
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
                                    //System.err.println( expression.get("event_name")+"+"+Arrays.toString( dashboards ) ) ;
                                    dashboardSet.addAll( Arrays.asList( dashboards ) );
                                    bookmarkSet.add( id+" : "+name.replace( " -" ,"") );
                                }else {
                                    continue;
                                }
                            }
                        } else {
                            for (String event : events) {
                                final_events.add(event.toString());
                                if (event.toString().equals( target )){
                                    //System.err.println(event.toString() +"+"+Arrays.toString( dashboards ) ) ;
                                    dashboardSet.addAll( Arrays.asList( dashboards ) );
                                    bookmarkSet.add( id+" : "+name.replace( " -" ,"") );
                                }else {
                                    continue;
                                }
                            }
                        }
                    }
                } else if (key.equals("first_event")) {
                    Map<String, Object> tmpMap = (Map<String, Object>) map.get(key);
                    Object event_name = tmpMap.get("event_name");
                    //System.err.println(event_name);
                    final_events.add(event_name.toString());
                    if (event_name.toString().equals( target )){
                        //System.err.println(event_name.toString()+"+"+ Arrays.toString( dashboards ) ) ;
                        dashboardSet.addAll( Arrays.asList( dashboards ) );
                        bookmarkSet.add( id+" : "+name.replace( " -" ,"") );
                    }else {
                        continue;
                    }
                } else if (key.equals("second_event")) {
                    Map<String, Object> tmpMap = (Map<String, Object>) map.get(key);
                    Object event_name = tmpMap.get("event_name");
                    //System.err.println(event_name);
                    final_events.add(event_name.toString());
                    if (event_name.toString().equals( target )){
                        //System.err.println(event_name.toString() +"+"+Arrays.toString( dashboards ) ) ;
                        dashboardSet.addAll( Arrays.asList( dashboards ) );
                        bookmarkSet.add( id+" : "+name.replace( " -" ,"") );
                    }else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        for (String strings : final_events) {
            System.err.println(strings);
        }
        Set dashboardSet1 = new TreeSet(  );
        for (Object dashid:dashboardSet) {
            Dashboard dashboard = JSONUtil.jsonToObject( DataMagicUtil.getDashboardById( (String) dashid, project ).getData(), Dashboard.class );
            String name = dashboard.getName();
            dashboardSet1.add( dashid+" : "+name.replace( " -" ,"") );
        }
        System.err.println( "事件："+target+"  所涉及到的所有概览分别是："+dashboardSet1 );
        System.err.println( "事件："+target+"  所涉及到的所有书签分别是："+bookmarkSet );
        //System.err.println("该项目中所有的事件数量为：  " + final_events.size());
    }
}
