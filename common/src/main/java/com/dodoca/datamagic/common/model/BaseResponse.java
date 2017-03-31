package com.dodoca.datamagic.common.model;

import com.dodoca.datamagic.common.ConstantUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lifei on 2016/11/24.
 */
public class BaseResponse {

    Logger logger = Logger.getLogger(BaseResponse.class);

    private HashMap<String, String> headers = new HashMap<String, String>();

    private List<Cookie> cookies = new ArrayList<Cookie>();

    private int status;

    private String data;

    public BaseResponse() {
    }

    public BaseResponse(int status, String data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public void setHeader(String name, String values) {
        headers.put(name, values);
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }


    public void copyToHttpServletResponse(HttpServletResponse response) {

        //复制cookie
        for (Cookie cookie : cookies) {
            logger.debug(cookie.getName() + ":" + cookie.getValue());
            response.addCookie(cookie);

        }
        //复制header
        for (String name : headers.keySet()) {
            if("Transfer-Encoding".equals(name)){
                continue;
            }
            logger.debug(name + ":" + headers.get(name));
            response.addHeader(name, headers.get(name));
        }
        response.setStatus(status);
    }

    public String getToken(){
        for (Cookie cookie : cookies){
            if(("sensorsdata-token" + "_" + ConstantUtil.PROJECT).equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return "";
    }
}

