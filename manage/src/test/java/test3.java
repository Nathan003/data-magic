import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dodoca.datamagic.common.HttpClientUtils;
import com.dodoca.datamagic.common.JSONUtil;
import com.dodoca.datamagic.common.model.BaseResponse;
import com.dodoca.datamagic.core.vo.EventDetail;
import com.dodoca.datamagic.core.vo.EventProperty;
import com.dodoca.datamagic.core.vo.EventRequest;
import com.dodoca.datamagic.core.vo.UserProperty;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        for (int j = 0; j < list.size();j++){
            eventRequest.setCname(list.get(j).getCname());
            eventRequest.setVisible(list.get(j).getVisible());
            eventRequest.setEventId(list.get(j).getId());
            eventRequest.setTag(list.get(j).getTag());
            eventRequest.setEventProperty(eventProperties);
            String url1 = "http://data.wxrrd.com:8107/api/event/"+list.get(j).getId()+"/meta?project=wxrrd_test_product_new";
            String eventdata = JSONUtil.objectToJson(eventRequest);
            HttpClientUtils.post(url1, eventdata, "sensorsdata-token", getAdminToken("wxrrd_test_product_new"));
        }
    }


    @Test
    public void userProperty(){
        String url = "http://data.wxrrd.com:8107/api/property/user/properties?show_all=true&cache=false&project=wxrrd_test_product_new";
        BaseResponse response = HttpClientUtils.get(url, null, "sensorsdata-token", getAdminToken("wxrrd_test_product_new"));
        List<Map<String,Object>> list = JSONUtil.jsonToObject(response.getData(), List.class);
        UserProperty userProperty = new UserProperty();
        List<UserProperty> userProperties = new ArrayList<UserProperty>();
        for (int i = 0;i<list.size();i++){
            Map<String, Object> map = list.get(i);
            System.err.println("显示名称"+map.get("cname"));
            System.err.println("是否在使用："+map.get("is_in_use"));
            System.err.println("id："+map.get("id"));
            userProperty.setCname(map.get("cname").toString());
            userProperty.setIsInUse(map.get("is_in_use").toString());
            userProperty.setPropertyId(map.get("id").toString());
            userProperties.add(userProperty);
        }
        String userdata = JSONUtil.objectToJson(userProperties);
        HttpClientUtils.post(url, userdata, "sensorsdata-token", getAdminToken("wxrrd_test_product_new"));
    }



}
