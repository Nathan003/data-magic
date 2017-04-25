package com.dodoca.datamagic.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by lifei on 2016/11/16.
 */
public class JSONUtil {

    private static Logger logger = Logger.getLogger(JSONUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    //private static Gson gson = new Gson();
    /**
     * 将一个对象转成json字符串
     * @param obj
     * @return
     */
    public static String objectToJson(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    /**
     * 将json字符串转成对象
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> clazz){
//        System.out.println(json);
        try {
            return objectMapper.readValue(json,clazz);
        } catch (IOException e) {
            logger.debug(e.getMessage());
            return null;
        }
//        return gson.fromJson(json,clazz);
    }



}
