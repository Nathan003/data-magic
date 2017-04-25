package com.dodoca.datamagic.common.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.dodoca.datamagic.utils.model.BaseResponse;
import com.dodoca.datamagic.utils.model.User;
import com.dodoca.datamagic.common.service.DataMagicHandle;
import com.dodoca.datamagic.common.service.DataMagicService;
import com.dodoca.datamagic.common.service.ShopService;
import com.dodoca.datamagic.common.service.UserServie;
import com.dodoca.datamagic.common.utils.ConstantUtil;
import com.dodoca.datamagic.utils.DataMagicUtil;
import com.dodoca.datamagic.utils.JSONUtil;
import com.dodoca.datamagic.utils.ParamUtil;
import com.dodoca.datamagic.utils.ValidUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lifei on 2016/12/29.
 */
@Service
public class DataMagicServiceImpl implements DataMagicService {


    @Autowired
    private UserServie userService;

    @Autowired
    private ShopService shopService;

    private Logger logger = Logger.getLogger(DataMagicService.class);

    public void service(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, DataMagicHandle dataMagicHandle) throws IOException {

        long start = System.currentTimeMillis();
        logger.debug("访问" + request.getRequestURI() + "接口：" + start);
        //1.获取参数--shop_id
        String username = params.get("username") == null ? "" : params.get("username").toString();
        String bookmarkID = params.get("bookmarkid") == null ? "" : params.get("bookmarkid").toString();
        String shopID = params.get("shopId") == null ? "" : params.get("shopId").toString();

        if (StringUtils.isEmpty(username)) {
            logger.debug("请先登录");
            BaseResponse.sendMessage(response, BaseResponse.getLogin(response));
            return;
        }

        String token = DataMagicUtil.getCookieValue(request, "sensorsdata-token_" + ConstantUtil.PROJECT);
        String data = JSONUtil.objectToJson(params);


        //2.获取用户信息--shop用户或者运营组用户或者非本系统创建用户
        User user = null;
        try {
            user = userService.getByName(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user != null) {//通过本系统创建的用户
            //校验用户是否已登录
            if (!ValidUtils.validLogin(username, token)) {
                logger.debug("请先登录");
                BaseResponse.sendMessage(response, BaseResponse.getLogin(response));
                return;
            }

            //3.判断用户是否有查看shop的权限
            if (!userService.isValidShopAuthority(user, shopID)) {
                logger.debug("没有查看当前商铺的权限");
                BaseResponse.sendMessage(response, BaseResponse.getMessage(response, "没有查看当前商铺的权限"));
                return;
            }
            //给本次查询设置shopID
            user.setShopId(shopID);

            //4.校验用户对书签是否有查看权限
            if (!ValidUtils.isValidBookMarkAuthority(user, bookmarkID)) {
                logger.debug("没有查看书签的权限");
                BaseResponse.sendMessage(response, BaseResponse.getMessage(response, "没有查看该书签的权限"));
                return;
            }
            //4.替换参数
            data = ParamUtil.modifyParams(user, params);
            token = null;
        }

        //5.通过http获取数据
        BaseResponse reportResponse = dataMagicHandle.handle(bookmarkID, data, token);
        //5.返回数据
        reportResponse.copyToHttpServletResponse(response);
        String rs = reportResponse.getData();
        Writer out = response.getWriter();
        out.write(rs);
        out.flush();
        out.close();
        long start1 = System.currentTimeMillis();
        if (reportResponse.getStatus() != 200) {
            logger.debug("shopID:" + shopID + ",bookmarkID:" + bookmarkID + ",http获取数据返回的状态：" + reportResponse.getStatus() + ",返回的内容：" + reportResponse.getData() + ",访问/events/report接口耗时：" + (start1 - start));
        }
        logger.debug("shopID:" + shopID + ",bookmarkID:" + bookmarkID + ",http获取数据返回的状态：" + reportResponse.getStatus() + ",访问/events/report接口耗时：" + (start1 - start));
    }

    /**
     * 概览授权
     * map中包含所有概览信息和某个用户对应的概览
     */
    public Map<String, Object> getDashboards(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        //获取所有的用户概览
        DataMagicUtil.login(username);
        result.put("admin", getAllDashboards());
        //result.put("user", DataMagicUtil.getDashboards("5cadf4d51706a42af369c957a50adc612b3406b37410dbafd7f1b8abc115e03f512dddeecad3b6c06d9dbd4a28c35e5709eb5cfca137072dc4704afb86655d49"));
        result.put("user", getUserDashboards(username));
        //TODO dfasdkfsk
        return result;
    }

    /**
     * 获取所有的概览(用admin获取)
     *
     * @return
     */
    public BaseResponse getUserDashboards(String username) {
        //获取用户的token
        String token = DataMagicUtil.getToken(username);
        return DataMagicUtil.getDashboards(token);
    }

    /**
     * 获取所有的概览(用admin获取)
     *
     * @return
     */
    public BaseResponse getAllDashboards() {
        //获取admin的token
        String adminToken = DataMagicUtil.getAdminToken();
        return DataMagicUtil.getDashboards(adminToken);
    }
}
