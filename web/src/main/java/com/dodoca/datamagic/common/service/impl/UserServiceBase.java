package com.dodoca.datamagic.common.service.impl;

import com.dodoca.datamagic.common.dao.UserDao;
import com.dodoca.datamagic.utils.model.BaseResponse;
import com.dodoca.datamagic.utils.model.User;
import com.dodoca.datamagic.common.service.UserServie;
import com.dodoca.datamagic.common.utils.ConstantUtil;
import com.dodoca.datamagic.common.utils.DateUtils;
import com.dodoca.datamagic.utils.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lifei on 2017/2/14.
 */
public abstract class UserServiceBase implements UserServie {

    private Logger logger = Logger.getLogger(UserServiceBase.class);
    private static final int INTERVAL_MINUTE = 500;

    @Autowired
    private UserDao userDao;

    public List<User> get() throws Exception {

        return userDao.get();
    }

    public User getById(String id) throws Exception {

        return userDao.getById(id);
    }

    public String save(User user) throws Exception {

        if(null == user){
            return null;
        }
        //密码加密
        if(StringUtils.isEmpty(user.getPassword())){
            user.setPassword( Md5Util.getMD5Str(user.getUsername() + "123456"));
        }else {
            user.setPassword(Md5Util.getMD5Str(user.getPassword()));
        }

        //设置用户所属项目
        if(StringUtils.isEmpty(user.getProject())){
            user.setProject(ConstantUtil.PROJECT);
        }

        return userDao.save(user);
    }

    public boolean save(List<User> userList) throws Exception {

        return userDao.save(userList);
    }

    public boolean update(User user) {

        return userDao.update(user);
    }


    public boolean delete(List<User> userList) {
        return userDao.delete(userList);
    }

    public List<User> get(int rows, int page) {
        List<User> users = userDao.get(rows, page,ConstantUtil.PROJECT);
        if (users.isEmpty()) {
            logger.debug("用户数量为0");
            return null;
        } else {
            logger.debug("查询分页用户数据成功");
            return users;
        }
    }
    
    /**
     * 通过用户名 like 模糊查询 用户
     */
    public List<User> getByBlurName(String username) {
        return userDao.getByBlurName(username);
    }

    public void saveToDataMagic(User user) throws Exception {

        if (null == user) {
            throw new Exception("用户创建失败");
        }
        List<User> userList = new ArrayList<User>();
        userList.add(user);

        BaseResponse clientResponse = DataMagicUtil.insertUser(userList);
        if (clientResponse.getStatus() != 200) {
            //datamagic中创建用户失败，删除中间系统的用户数据
            delete(userList);
            logger.debug("在datamagic中创建的用户：" + user);
            throw new Exception("用户创建失败");
        }
        List<Map<String, Object>> list = JSONUtil.jsonToObject(clientResponse.getData(), List.class);
        logger.debug(list);
        user.setId(StringUtils.objectToString(list.get(0).get("id")).replace(".0", ""));
    }

    public List<User> deleteFromDataMagic(List<User> userList) throws Exception {
    	//TODO 删除魔方中用户
        return null;
    }

    public User updateDataMagicRole(String id,String role) {

        //TODO 根据用户ID更新role
        return null;
    }

    public User getUser(Map<String, Object> userInfo) {

        //参数校验
        if (userInfo == null) {
            return null;
        }
        //判断是否是通过加密传输用户信息
        if (!StringUtils.isEmpty(StringUtils.objectToString(userInfo.get("token")))) {
            userInfo = analysisToken(StringUtils.objectToString(userInfo.get("token")));
            if (null == userInfo) {
                return null;
            }
            userInfo.put("project", ConstantUtil.PROJECT);
        }

        //shop用户
        if (!StringUtils.isEmpty(StringUtils.objectToString(userInfo.get("username")))) {
            User user = new User();
            user.setUsername(StringUtils.objectToString(userInfo.get("username")));
            user.setShopId(StringUtils.objectToString(userInfo.get("shop_id")));
            user.setShopLineId(StringUtils.objectToString(userInfo.get("shop_line_id")));
            return user;
        }

        return null;
    }

    /**
     * 解密用户信息，并判断加密信息是否过期
     *
     * @param token
     * @return
     */
    protected Map<String, Object> analysisToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Map<String, Object> userMap = JSONUtil.jsonToObject( SecurityUtil.decrypt(token), Map.class);
        if (null == userMap || StringUtils.isEmpty((String)userMap.get("created_at"))) {
            return null;
        }
        long timeDifference = System.currentTimeMillis() - DateUtils.stringToDate((String)userMap.get("created_at")).getTime();
        if (INTERVAL_MINUTE * 60 * 1000 < timeDifference) {
            return null;
        }
        return (Map<String, Object>) userMap.get("userinfo");
    }
    
    /**
     * 判断用户是否存在
     * @param username
     * @return
     */
    public boolean exists(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        if (null == getByName(username)) {
            return false;
        }
        return true;
    }

    public void createUserAndDashboardsAuth(User user) throws Exception {

        if(null == user){
            throw new Exception("请登录");
        }
        //使用数据魔方权限校验
        if (!isValidDatamagicAuthority(user)) {
            throw new Exception("没有使用数据魔方的权限");
        }

        //设置用户密码,如果没有密码的话，初始化为用户名加123456，否则为原密码
        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(user.getUsername() + "123456");
        }

        String userID = save(user);

        if (StringUtils.isEmpty(userID)) {
            throw new Exception("用户创建失败");
        }

        //4.创建用户至数据魔方
        saveToDataMagic(user);
        logger.debug(user);
        //5.授权概览
        dashboardsAuth(user);
    }

    /**
     * 获取项目用户总数
     * @return
     */
    public int count() {
        return userDao.count(ConstantUtil.PROJECT);
    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("token","aSW+xYrGXiUnkcUxVo4Abegm+wbDgBgqNh0OgTMmq82TDPolOvxFraSnG2bY7tA4Tucd1juojK/HGvTDH7t2AGgKBwWECZbsrq80Zi+2HD+VC9yFAwXL8eAmNWtG0XV4918R/SU9QcYkDMsQOWivaw==");
//        User user = UserServiceBase.getUser(map);
//        System.out.println(user);
    }
}
