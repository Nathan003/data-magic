package com.dodoca.datamagic.utils;


import com.dodoca.datamagic.utils.model.User ;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lifei on 2016/12/14.
 */
public class ParamUtil {

    /**
     * 根据用户信息修改用户书签的参数
     *
     * @param user
     * @param data
     * @return
     */
    public static String modifyParams(User user, Map<String, Object> data) {
        if (null == data) {
            return "{}";
        }
        if(null == user){
            return "{}";
        }
        String project = user.getProject();
//        data = data.replaceAll("\\\\", "").replaceAll("\\\"\\{", "{").replaceAll("\\}\\\"", "}");
        //替换shopID
        replaceAllFilter(data, ConstantUtil.getValue(project + ".shop_id"), user.getShopId());
        //替换shop一级分类ID
        replaceAllFilter(data, ConstantUtil.getValue(project + ".shop_cate1_id"), user.getShopCate1Id());
        //替换shop二级分类ID
        replaceAllFilter(data, ConstantUtil.getValue(project + ".shop_cate2_id"), user.getShopCate2Id());
        //替换shop一级分类名称
        replaceAllFilter(data, ConstantUtil.getValue(project + ".shop_cate1_name"), user.getShopCate1Name());
        //替换shop二级分类名称
        replaceAllFilter(data, ConstantUtil.getValue(project + ".shop_cate2_name"), user.getShopCate2Name());
        //替换shopLineId
        if ("0".equals(user.getShopLineId())) {
            removeAllFilter(data, ConstantUtil.getValue(project + ".shop_line_id"));
            removeAllFilter(data, ConstantUtil.getValue(project + ".shop_line"));
        } else {
            replaceAllFilter(data, ConstantUtil.getValue(project + ".shop_line_id"), user.getShopLineId());
            //替换shopLine
            replaceAllFilter(data, ConstantUtil.getValue(project + ".shop_line"), user.getShopLine());
        }
        return JSONUtil.objectToJson(data);
    }

    private static void removeAllFilter(Map<String, Object> data, String reg) {
        if (data == null || data.size() == 0 || null == reg) {
            return;
        }
        //普通过滤条件
        removeFilter((Map<String, Object>) data.get("filter"), reg);
        //用户留存初始行为过滤条件
        Map<String, Object> event = (Map) data.get("first_event");
        if (event != null) {
            removeFilter((Map<String, Object>) event.get("filter"), reg);
        }
        //用户留存后续行为过滤条件
        event = (Map) data.get("second_event");
        if (event != null) {
            removeFilter((Map<String, Object>) event.get("filter"), reg);
        }
    }

    private static void removeFilter(Map<String, Object> filter, String reg) {
        if (null == filter  || filter.size() == 0 || null == reg) {
            return;
        }
        List<Map<String, Object>> conditions = (List<Map<String, Object>>) filter.get("conditions");
        Iterator<Map<String, Object>> it = conditions.iterator();
        while (it.hasNext()) {
            if (it.next().get("field").toString().toLowerCase().contains(reg.toLowerCase())) {
                it.remove();
            }
        }

    }

    public static void replaceAllFilter(Map<String, Object> data, String reg, String value) {
        if (data == null  || data.size() == 0 || null == reg) {
            return;
        }
        //普通过滤条件
        replaceFilter((Map<String, Object>) data.get("filter"), reg, value);
        //用户留存初始行为过滤条件
        Map<String, Object> event = (Map) data.get("first_event");
        if (event != null) {
            replaceFilter((Map<String, Object>) event.get("filter"), reg, value);
        }
        //用户留存后续行为过滤条件
        event = (Map) data.get("second_event");
        if (event != null) {
            replaceFilter((Map<String, Object>) event.get("filter"), reg, value);
        }

    }

    private static void replaceFilter(Map<String, Object> filter, String reg, String value) {
        if (null == filter || filter.size() == 0 || null == reg) {
            return;
        }
        List<String> valueList = new ArrayList<String>();
        valueList.add(value);
        List<Map<String, Object>> conditions = (List<Map<String, Object>>) filter.get("conditions");
        for (Map<String, Object> condition : conditions) {
            if (condition.get("field").toString().toLowerCase().contains(reg.toLowerCase())
                    && ((List)condition.get("params")).size() > 0) {
                condition.put("params", valueList);
            }
        }
    }

    public static void replaceFromDate(Map<String, Object> data, String value) {
        data.put("from_date", value);
    }

    public static void replaceToDate(Map<String, Object> data, String value) {
        data.put("to_date", value);
    }

    public static void replaceLimit(Map<String, Object> data, int value) {
        data.put("to_date", value);
    }

    public static void main(String[] args) {
        String str = "{\"measures\":[{\"event_name\":\"pay_order1018\",\"aggregator\":\"SUM\",\"field\":\"event.pay_order1018.real_amount\"}],\"unit\":\"day\",\"filter\":{\"conditions\":[{\"field\":\"event.pay_order1018.SHOP_id\",\"function\":\"equal\",\"params\":[\"13289359\"]}]},\"sampling_factor\":64,\"from_date\":\"2016-12-01\",\"to_date\":\"2016-12-07\",\"bookmarkid\":\"299\",\"username\":\"18217615434\",\"use_cache\":true}";
        Map<String, Object> data = JSONUtil.jsonToObject(str, Map.class);
        User user = new User();
        user.setShopLineId("0");
        removeFilter(data, ConstantUtil.getValue("wxrrd_test_product_new.shop_id"));
        System.out.println(JSONUtil.objectToJson(data));
    }
}
