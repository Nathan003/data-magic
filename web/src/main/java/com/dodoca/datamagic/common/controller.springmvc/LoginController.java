package com.dodoca.datamagic.common.controller.springmvc;

import com.dodoca.datamagic.utils.model.BaseResponse ;
import com.dodoca.datamagic.utils.model.User ;
import com.dodoca.datamagic.common.service.UserServie;
import com.dodoca.datamagic.utils.DataMagicUtil;
import com.dodoca.datamagic.utils.JSONUtil;
import com.dodoca.datamagic.utils.Md5Util;
import com.dodoca.datamagic.utils.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by lifei on 2016/11/25.
 */
@Controller
@RequestMapping("/")
public class LoginController {

    private Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    private UserServie userServie;

    /**
     * 登录
     * @param userInfo
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody Map<String,Object> userInfo, HttpServletRequest request, HttpServletResponse response) {

        try {

            logger.debug("用户登录参数：" + userInfo);
            //2.获取用户信息
            User user = userServie.getUser(userInfo);
            //校验用户信息
            if(null == user){
                logger.debug("请登录");
                return BaseResponse.getLogin(response);
            }

            //3.检验用户是否已经存在
            if(!userServie.exists(user.getUsername())){
                try {
                    //用户不存在则创建用户并授权
                    user.setRole(2);//普通用户
                    userServie.createUserAndDashboardsAuth(user);
                }catch (Exception e){
                    return BaseResponse.getMessage(response, e.getMessage());
                }

            }
            user = userServie.getByName(user.getUsername());
            logger.debug(user);
            //3.登录数据魔方 
            BaseResponse clientResponse = DataMagicUtil.login(user.getUsername());
            logger.debug(clientResponse.getData());
            clientResponse.copyToHttpServletResponse(response);
            Map<String,Object> map = JSONUtil.jsonToObject(clientResponse.getData(),Map.class);
            map.put("project",user.getProject());
            map.put("user", user);
            return JSONUtil.objectToJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
            return BaseResponse.getError(response,e.getMessage());
        }
    }

    @RequestMapping(value = "/insidLogin",method = RequestMethod.POST)
    @ResponseBody
    public String insidLogin(@RequestBody Map<String,String> userInfo, HttpServletRequest request, HttpServletResponse response) {

        try {
            logger.debug("用户登录参数：" + userInfo);
            //获取参数
            String username = userInfo.get("username");
            String password = userInfo.get("password");
            if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
                return BaseResponse.getMessage(response, "用户名或者密码不能为空");
            }

            // 根据用户名称和项目名称查找
            User user = userServie.getByName(username);
            //3.检验用户是否已经存在
            if(null == user || !Md5Util.getMD5Str(password).equals(user.getPassword())){
                return BaseResponse.getMessage(response, "账号或者密码错误");
            }
            logger.debug(user);
            //3.登录数据魔方
            BaseResponse clientResponse = DataMagicUtil.login(user.getUsername());
            logger.debug(clientResponse.getData());
            clientResponse.copyToHttpServletResponse(response);
            Map<String,Object> map = JSONUtil.jsonToObject(clientResponse.getData(),Map.class);
            map.put("project",user.getProject());
            map.put("user", user);
                return JSONUtil.objectToJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
            return BaseResponse.getError(response,e.getMessage());
        }

    }

//    /**
//     * 退出登录
//     *
//     * @param sessionID
//     * @param request
//     * @param response
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "/loginout",method = RequestMethod.POST)
//    @ResponseBody
//    public String loginout(@RequestBody String sessionID, HttpServletRequest request, HttpServletResponse response) {
//
//        try {
//            //1.注销数据魔方

//            BaseResponse clientResponse = DataMagicUtil.loginout(user.getToken());
//            clientResponse.copyToHttpServletResponse(response);
//            logger.debug(clientResponse.getData());
//            return clientResponse.getData();
//        } catch (Exception e) {
//            logger.debug(e.getMessage());
//            return BaseResponse.getError(response,e.getMessage());
//        }
//    }

}
