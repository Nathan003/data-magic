package com.dodoca.datamagic.common.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 处理用户获取书签数据的模板类
 * Created by lifei on 2016/12/29.
 */
public interface DataMagicService {

    /**
     * 处理用户获取书签数据模板方法
     * @param params
     * @param request
     * @param response
     * @param dataMagicHandle
     * @throws IOException
     */
    public void service(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response,DataMagicHandle dataMagicHandle) throws IOException;
    
    /**
     * 概览授权
     * 通过不同用户username去获取token 不同的概览,map中包含所有的概览信息和 传入的该用户token对应的概览 
     * @param username
     * @return
     */
    public Map<String, Object> getDashboards(String username);
    
    public com.dodoca.datamagic.utils.model.BaseResponse getUserDashboards(String username);
    
    public com.dodoca.datamagic.utils.model.BaseResponse getAllDashboards();
}
