package com.dodoca.datamagic.common.utils;

import com.dodoca.dataMagic.common.model.BaseResponse ;
import com.dodoca.dataMagic.common.model.User;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lifei on 2016/11/24.
 */
public class DataMagicUtil {

    private static Logger log = Logger.getLogger(DataMagicUtil.class);

    public static Map<String, String> tokenMap = new HashMap<String, String>(); //保存token

    public static BaseResponse insertUser(List<User> userList) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.user_uri")
                + "?project=" + ConstantUtil.PROJECT + "&token=" + ConstantUtil.getProjectValue("api_secret");
        return HttpClientUtils.put(url, JSONUtil.objectToJson(userList), null);
    }

    /**
     * 用户登录
     *
     * @param username
     * @return
     */
    public static BaseResponse login(String username) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.login_uri")
         + "?project=" + ConstantUtil.PROJECT + "&token=" + ConstantUtil.getProjectValue("api_secret");
        JsonObject data = new JsonObject();
        data.addProperty("username", username);
        data.addProperty("expires", 365);
        BaseResponse baseResponse = HttpClientUtils.post(url, data.toString(), null);
        //保存登录的token
        saveToken(username, baseResponse.getToken());
        return baseResponse;
    }

    /**
     * admin用户登录数据魔方
     *
     * @return
     */
    public static String adminLogin() {
        BaseResponse response = login("admin");

        if (response.getStatus() != 200) {
            return null;
        }
        String data = response.getData();
        return JSONUtil.jsonToObject(data, Map.class).get("token").toString();
    }

    /**
     * 注销登录
     *
     * @param username
     * @return
     */
    public static BaseResponse loginout(String username) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.login_uri");
        JsonObject data = new JsonObject();
        BaseResponse baseResponse = HttpClientUtils.post(url, data.toString(), "sensorsdata-token", getToken(username));
        //注销用户登录的token
        removeToken(username);
        return baseResponse;
    }

    /**
     * 保存token
     *
     * @param username
     * @param token
     */
    public static void saveToken(String username, String token) {
        if (!StringUtils.isEmpty(token)) {
            tokenMap.put(username + "_" + ConstantUtil.PROJECT, token);
        }
    }

    /**
     * @param username
     */
    public static void removeToken(String username) {
        tokenMap.remove(username + "_" + ConstantUtil.PROJECT);
    }

    /**
     * 获取用户的登录token
     *
     * @param username
     * @return
     */
    public static String getToken(String username) {
        return tokenMap.get(username + "_" + ConstantUtil.PROJECT);
    }

    public static String getAdminToken() {
        String token = getToken("admin");
        if (StringUtils.isEmpty(token)) {
            return adminLogin();
        }
        return token;
    }

    public static void dashboardsAuth(String userId, String dashboard) {

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(dashboard)) {
            return;
        }
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.dashvoards_uri") + "/" + dashboard + "/users/" + userId
                + "?project=" + ConstantUtil.PROJECT + "&token=" + ConstantUtil.getProjectValue("api_secret");
        BaseResponse response = HttpClientUtils.put(url, null, "sensorsdata-token", adminLogin());
        log.debug(response.getData());
    }

    public static BaseResponse getBookmark(String bookmarkID, String username) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.bookmarks_uri") + "/bookmark/" + bookmarkID
                + "?project=" + ConstantUtil.PROJECT;
        BaseResponse response = HttpClientUtils.get(url, null, "sensorsdata-token",getToken(username));
        log.debug(response.getStatus() + response.getData());
        return response;

    }

    /**
     * 根据cookie名从request中获取cookie值
     *
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
    	String value = "";
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                value =  cookie.getValue();
            }
        }
        return value;
    }

    /**
     * 通过API token获取书签数据
     *
     * @param data
     * @return
     */
    public static BaseResponse reportSegmentation(String bookmarkID, String data) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.segmentation_uri") + "/?bookmarkId=" + bookmarkID
                    + "&project=" + ConstantUtil.PROJECT + "&token=" + ConstantUtil.getProjectValue("api_secret");
        BaseResponse response = HttpClientUtils.post(url, data, null);
        return response;
    }

    /**
     * 通过用户token获取数据
     *
     * @param bookmarkID
     * @param data
     * @param token
     * @return
     */
    public static BaseResponse reportSegmentation(String bookmarkID, String data, String token) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.segmentation_uri") + "/?bookmarkId=" + bookmarkID
                + "&project=" + ConstantUtil.PROJECT;
        BaseResponse response = HttpClientUtils.post(url, data, "sensorsdata-token", token);
        return response;
    }

    /**
     * 通过admin用户获取漏斗分析数据
     *
     * @param bookmarkID
     * @param data
     * @return
     */
    public static BaseResponse reportFunnel(String funnelId,String bookmarkID, String data) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.funnel_uri") + "/" + funnelId + "/report?bookmarkId=" + bookmarkID
                + "&project=" + ConstantUtil.PROJECT + "&token=" + ConstantUtil.getProjectValue("api_secret");
        BaseResponse response = HttpClientUtils.post(url, data, null);
        return response;
    }

    /**
     * 通过用户token获取漏斗分析数据
     *
     * @param bookmarkID
     * @param data
     * @param token
     * @return
     */
    public static BaseResponse reportFunnel(String funnelId,String bookmarkID, String data, String token) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.funnel_uri") + "/" + funnelId + "/report?bookmarkId=" + bookmarkID
                + "&project=" + ConstantUtil.PROJECT;
        BaseResponse response = HttpClientUtils.post(url, data, "sensorsdata-token", token);
        return response;
    }

    /**
     * 通过admin用户获取分布分析数据
     *
     * @param bookmarkID
     * @param data
     * @return
     */
    public static BaseResponse reportAddiction(String bookmarkID, String data) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.addiction_uri") + "/?bookmarkId=" + bookmarkID
                + "&project=" + ConstantUtil.PROJECT + "&token=" + ConstantUtil.getProjectValue("api_secret");
        BaseResponse response = HttpClientUtils.post(url, data, null);
        return response;
    }

    /**
     * 通过用户token获取分布分析数据
     *
     * @param bookmarkID
     * @param data
     * @param token
     * @return
     */
    public static BaseResponse reportAddiction(String bookmarkID, String data, String token) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.addiction_uri") + "/?bookmarkId=" + bookmarkID
                + "&project=" + ConstantUtil.PROJECT;
        BaseResponse response = HttpClientUtils.post(url, data, "sensorsdata-token", token);
        return response;
    }

    /**
     * 通过admin用户获取留存分析数据
     *
     * @param bookmarkID
     * @param data
     * @return
     */
    public static BaseResponse reportRetention(String bookmarkID, String data) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.retention_uri") + "/?bookmarkId=" + bookmarkID
                + "&project=" + ConstantUtil.PROJECT + "&token=" + ConstantUtil.getProjectValue("api_secret");
        BaseResponse response = HttpClientUtils.post(url, data, null);
        return response;
    }

    /**
     * 通过用户token获取留存分析数据
     *
     * @param bookmarkID
     * @param data
     * @param token
     * @return
     */
    public static BaseResponse reportRetention(String bookmarkID, String data, String token) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.retention_uri") + "/?bookmarkId=" + bookmarkID
                + "&project=" + ConstantUtil.PROJECT;
        BaseResponse response = HttpClientUtils.post(url, data, "sensorsdata-token", token);
        return response;
    }

    /**
     * 获取概览信息
     * @param token
     * @return
     */
    public static BaseResponse getDashboards(String token){
    	String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.dashvoards_uri") + "/?project=" + ConstantUtil.PROJECT;
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("Cookie", "sensorsdata-token" + "_" + ConstantUtil.PROJECT + "=" + token);
        BaseResponse response = HttpClientUtils.get(url, "", header);
        return response;
    }
    
    public static void main(String[] args) throws Exception {

    }

	public static BaseResponse deleteUser(List<User> userList) {
		String url = "";
		for (User user : userList) {
			url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.user_uri")
                    + "/" + user.getId() +"?project=" + ConstantUtil.PROJECT + "&token=" + ConstantUtil.getProjectValue("api_secret");
		}
        return HttpClientUtils.delete(url, JSONUtil.objectToJson(userList), null);
	}

}
