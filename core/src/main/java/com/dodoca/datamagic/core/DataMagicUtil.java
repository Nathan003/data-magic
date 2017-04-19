package com.dodoca.datamagic.core;

import com.dodoca.datamagic.common.utils.ConstantUtil;
import com.dodoca.datamagic.common.utils.HttpClientUtils;
import com.dodoca.datamagic.common.utils.JSONUtil;
import com.dodoca.datamagic.common.model.BaseResponse;
import com.dodoca.datamagic.core.model.User;
import org.apache.commons.lang.StringUtils;
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
        return login(username,ConstantUtil.PROJECT);
    }

    public static BaseResponse login(String username,String project) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.login_uri")
                + "?project=" + project + "&token=" + ConstantUtil.getProjectValue("api_secret",project);
        Map<String,Object> data = new HashMap<String, Object>();
        data.put("username", username);
        data.put("expires", 365);
        BaseResponse baseResponse = HttpClientUtils.post(url, JSONUtil.objectToJson(data), null);
        //保存登录的token
        saveToken(username, baseResponse.getToken(),project);
        return baseResponse;
    }

    /**
     * admin用户登录数据魔方
     *
     * @return
     */
    public static String adminLogin() {
        return adminLogin(ConstantUtil.PROJECT);
    }

    public static String adminLogin(String project) {
        BaseResponse response = login("admin",project);

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
        BaseResponse baseResponse = HttpClientUtils.post(url, null, "sensorsdata-token", getToken(username));
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
       saveToken(username,token,ConstantUtil.PROJECT);
//        if (!StringUtils.isEmpty(token)) {
//            tokenMap.put(username + "_" + ConstantUtil.PROJECT, token);
//        }
    }

    public static void saveToken(String username, String token,String project) {
        if (!StringUtils.isEmpty(token)) {
            tokenMap.put(username + "_" + project, token);
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
        return getToken(username,ConstantUtil.PROJECT);
    }



    public static String getToken(String username,String project) {
        return tokenMap.get(username + "_" + project);
    }

    public static String getAdminToken() {
        return getAdminToken(ConstantUtil.PROJECT);
    }

    public static String getAdminToken(String project) {
        String token = getToken("admin",project);
        if (StringUtils.isEmpty(token)) {
            return adminLogin(project);
        }
        return token;
    }

    public static void dashboardsAuth(String userId, String dashboard) {

        dashboardsAuth(userId,dashboard,ConstantUtil.PROJECT);
    }


    public static void dashboardsAuth(String userId, String dashboard,String project) {

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(dashboard)) {
            return;
        }
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.dashvoards_uri") + "/" + dashboard + "/users/" + userId
                + "?project=" + project + "&token=" + ConstantUtil.getProjectValue("api_secret");
        BaseResponse response = HttpClientUtils.put(url, null, "sensorsdata-token", adminLogin(project));
        log.debug(response.getData());
    }

    public static BaseResponse getBookmark(String bookmarkID, String username) {
       return getBookmark(bookmarkID, username,ConstantUtil.PROJECT);

    }

    public static BaseResponse getBookmark(String bookmarkID, String username,String project) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.bookmarks_uri") + "/bookmark/" + bookmarkID
                + "?project=" + project;
        BaseResponse response = HttpClientUtils.get(url, null, "sensorsdata-token", getToken(username,project));
        log.debug(response.getStatus() + response.getData());
        return response;

    }


    public static BaseResponse getBookmarkByAdmin(String bookmarkID) {
      return   getBookmarkByAdmin(bookmarkID,ConstantUtil.PROJECT);

    }



    public static BaseResponse getBookmarkByAdmin(String bookmarkID,String project) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.bookmarks_uri") + "/bookmark/" + bookmarkID
                + "?project=" + project;
        BaseResponse response = HttpClientUtils.get(url, null, "sensorsdata-token", getAdminToken(project));
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
       return reportSegmentation(bookmarkID,data,ConstantUtil.PROJECT);
    }


    public static BaseResponse reportSegmentation(String bookmarkID, String data,String project) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.segmentation_uri") + "/?bookmarkId=" + bookmarkID
                + "&project=" + project + "&token=" + ConstantUtil.getProjectValue("api_secret",project);
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
    public static BaseResponse reportSegmentation(String bookmarkID, String data, String token,String project) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.segmentation_uri") + "/?bookmarkId=" + bookmarkID
                + "&project=" + project;
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
        return reportFunnel(funnelId,bookmarkID,ConstantUtil.PROJECT);
    }


    public static BaseResponse reportFunnel(String funnelId,String bookmarkID, String data,String project) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.funnel_uri") + "/" + funnelId + "/report?bookmarkId=" + bookmarkID
                + "&project=" + project + "&token=" + ConstantUtil.getProjectValue("api_secret",project);
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
    public static BaseResponse reportFunnel(String funnelId,String bookmarkID, String data, String token,String project) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.funnel_uri") + "/" + funnelId + "/report?bookmarkId=" + bookmarkID
                + "&project=" + project;
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
        return reportAddiction(bookmarkID, data,ConstantUtil.PROJECT);
    }


    public static BaseResponse reportAddiction(String bookmarkID, String data,String project) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.addiction_uri") + "/?bookmarkId=" + bookmarkID
                + "&project=" + project + "&token=" + ConstantUtil.getProjectValue("api_secret",project);
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
    public static BaseResponse reportAddiction(String bookmarkID, String data, String token,String project) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.addiction_uri") + "/?bookmarkId=" + bookmarkID
                + "&project=" + project;
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
        return reportRetention(bookmarkID, data,ConstantUtil.PROJECT);
    }

    public static BaseResponse reportRetention(String bookmarkID, String data,String project) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.retention_uri") + "/?bookmarkId=" + bookmarkID
                + "&project=" + project + "&token=" + ConstantUtil.getProjectValue("api_secret",project);
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
    public static BaseResponse reportRetention(String bookmarkID, String data, String token,String project) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.retention_uri") + "/?bookmarkId=" + bookmarkID
                + "&project=" + project;
        BaseResponse response = HttpClientUtils.post(url, data, "sensorsdata-token", token);
        return response;
    }

    /**
     * 获取概览信息
     * @param token
     * @return
     */
    public static BaseResponse getDashboards(String token){
    	return getDashboards(token,ConstantUtil.PROJECT);
    }


    public static BaseResponse getDashboards(String token,String project){
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.dashvoards_uri") + "/?project=" + project;
        Map<String, String> header = new HashMap<String, String>();
        header.put("Cookie", "sensorsdata-token" + "_" + project + "=" + token);
        BaseResponse response = HttpClientUtils.get(url, "", header);
        return response;
    }


	public static BaseResponse deleteUser(List<User> userList) {
       return deleteUser(userList,ConstantUtil.PROJECT);
    }


    public static BaseResponse deleteUser(List<User> userList,String project) {
        String url = "";
        for (User user : userList) {
            url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.user_uri")
                    + "/" + user.getId() +"?project=" + project + "&token=" + ConstantUtil.getProjectValue("api_secret",project);
        }
        return HttpClientUtils.delete(url, JSONUtil.objectToJson(userList), null);
    }


    /**
     * 根据概览id来获得概览
     * @param dashboardId
     * @return
     */

    public static BaseResponse getDashboardById(String dashboardId) {
        return getDashboardById(dashboardId,ConstantUtil.PROJECT);
    }

    public static BaseResponse getDashboardById(String dashboardId,String project) {
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.dashvoards_uri")+ "/" + dashboardId
                + "?project=" + project ;
        System.err.println(url);
        BaseResponse response = HttpClientUtils.get(url, null, "sensorsdata-token", getAdminToken(project));
//        log.debug(response.getData());
        return response;
    }


    /**
     * 新增书签（另存），不需要id
     * @return
     */
    public static BaseResponse addBookmark(String data){
        return addBookmark(data,ConstantUtil.PROJECT);
    }

    public static BaseResponse addBookmark(String data,String project){
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.bookmarks_uri") + "/bookmark/"
                + "?project=" + project ;
        BaseResponse response = HttpClientUtils.post(url, data, "sensorsdata-token", getAdminToken(project));
        return response;
    }

    /**
     * 更新bookmark
     * @param bookmarkId
     * @return
     */
    public static BaseResponse updateBookmark(String bookmarkId,String data){
        return updateBookmark(bookmarkId,data,ConstantUtil.PROJECT);
    }


    public static BaseResponse updateBookmark(String bookmarkId,String data,String project){
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.bookmarks_uri") + "/bookmark/" + bookmarkId
                + "?project=" + project ;
        BaseResponse response = HttpClientUtils.post(url, data, "sensorsdata-token", getAdminToken(project));
        return response;
    }


    /**
     * 删除概览中的书签
     *
     *
     */
    public static BaseResponse deleteBookmark(String bookmarkId){
        return deleteBookmark(bookmarkId,ConstantUtil.PROJECT);
    }


    public static BaseResponse deleteBookmark(String bookmarkId,String project){
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.bookmarks_uri") + "/bookmark/" + bookmarkId
                + "?project=" + project ;
        BaseResponse response = HttpClientUtils.delete(url, null, "sensorsdata-token", getAdminToken(project));
        return response;
    }


    /**
     * 获取所有概览
     *
     */
    public static BaseResponse getDashboardsAll(){
        return getDashboardsAll(ConstantUtil.PROJECT);
    }


    public static BaseResponse getDashboardsAll(String project){
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.dashvoards_uri")+"?project=" + project ;
        BaseResponse response = HttpClientUtils.get(url, null, "sensorsdata-token", getAdminToken(project));
        return response;
    }


    /**
     * 新增概览
     * @param data
     * @return
     */
    public  static  BaseResponse saveDashboard(String data){
        return saveDashboard(data,ConstantUtil.PROJECT);
    }

    public  static  BaseResponse saveDashboard(String data,String project){
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.dashvoards_uri")+"?project=" + project ;
        BaseResponse response = HttpClientUtils.post(url, data, "sensorsdata-token", getAdminToken(project));
        return response;
    }

    /**
     * 删除概览
     * @param dashboardId
     * @return
     */
    public static BaseResponse deleteDashboard(String dashboardId){
        return deleteDashboard(dashboardId,ConstantUtil.PROJECT);
    }


    public static BaseResponse deleteDashboard(String dashboardId,String project){
        String url = ConstantUtil.getValue("project_common.domain") + ConstantUtil.getValue("project_common.dashvoards_uri")+ "/" + dashboardId+"?project=" + project ;
        BaseResponse response = HttpClientUtils.delete(url, null, "sensorsdata-token", getAdminToken(project));
        return response;
    }




    public static void main(String[] args) throws Exception {
        System.out.println(login("admin"));
    }
}
