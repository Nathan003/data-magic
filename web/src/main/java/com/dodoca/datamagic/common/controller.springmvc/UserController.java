package com.dodoca.datamagic.common.controller.springmvc;

import com.dodoca.datamagic.utils.model.BaseResponse ;
import com.dodoca.datamagic.utils.model.User ;
import com.dodoca.datamagic.common.service.UserServie;
import com.dodoca.datamagic.common.utils.*;
import com.dodoca.datamagic.utils.DataMagicUtil;
import com.dodoca.datamagic.utils.JSONUtil;
import com.dodoca.datamagic.utils.Md5Util;
import com.dodoca.datamagic.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 增加如下功能：
 * 		后台管理员才能够登陆后台 (后台用户role 对应为-1)
 * 		后台创建修改删除的用户 需要 在神策系统中进行同步修改或者删除
 * updated on 2017/02/08
 * @author huhongda
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class);
    private JsonObject jsonObj;
    @Autowired
    private UserServie userServie;

    /**
     * 添加用户
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {

        String userName = request.getHeader("userName");

        try {
            //1.校验用户是否登录

            //2.校验需要保存的用户信息
            verifiedSave(user);

            //3.保存用户信息至本系统
            String userID = userServie.save(user);
            if (StringUtils.isEmpty(userID)) {
                return BaseResponse.getError(response,"用户创建失败");
            }

            //4.创建用户至数据魔方
            List<User> userList = new ArrayList<User>();
            userList.add(user);
            BaseResponse clientResponse = DataMagicUtil.insertUser(userList);
            clientResponse.copyToHttpServletResponse(response);
            return clientResponse.getData();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return BaseResponse.getError(response,e.getMessage());
        }

    }
    
    //该接口用来 后台界面添加用户
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public boolean saveUser(User user, HttpServletRequest request, HttpServletResponse response){

    	try {
            if(StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())){
                return false;
            }
    		// 如果商铺的运营组ID为空，则设置为null
    		if(StringUtils.isEmpty(user.getShopLineId())){
                user.setShopLineId(null);
            }

            //创建非系统后台管理用户
            if(user.getRole() != -1){
                // 当神策中创建 用户失败时，会将中间系统中用该用户删除
                userServie.createUserAndDashboardsAuth(user);
                logger.debug("---->后台系统创建用户并在神策中创建,用户名:"+user.getUsername()+"---->用户ID:"+user.getId());
            }else{
                //创建系统后台管理用户
                if(StringUtils.isEmpty(userServie.save(user))){
                    return false;
                }
            }

        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    	return true;

    }
    
    private void verifiedSave(User user) throws Exception {
        if (StringUtils.isEmpty(user.getUsername()) || user.getRole() < 1 || user.getRole() > 2) {
            throw new Exception("用户名不能为空，role只能是1,2");
        }
        user.setPassword(user.getUsername());
    }

    //通过id查找user
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String findById(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response){
        User user = null;
        try {
        	//response.addCookie(new Cookie("sessionid", "12131")); //可以添加cookie
            user = userServie.getById(id);
            return JSONUtil.objectToJson(user);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return BaseResponse.getError(response,e.getMessage());
        }

    }
    @RequestMapping(value = "/checkUsername", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkUsername(@RequestBody Map<String,Object> params, HttpServletRequest request, HttpServletResponse response) {

        String token = (String)params.get("token");
        String username = (String)params.get("username");
        User user = userServie.getUser(params);
        if(StringUtils.isEmpty(username) || !username.equals(user.getUsername())){
            return false;
        }
        return true;
    }

        /**
         * 根据用户名 和项目名称获取用户信息
         * @param username
         * @param request
         * @param response
         * @return
         */
    @RequestMapping(value = "/project/{project}/name/{username}", method = RequestMethod.GET)
    @ResponseBody
    public String findByUserName(@PathVariable("username") String username,@PathVariable("project") String project, HttpServletRequest request, HttpServletResponse response) {

        try {
            User user = userServie.getByName(username);
            return JSONUtil.objectToJson(user);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return BaseResponse.getError(response,e.getMessage());
        }

    }
    
    //批量删除 用户
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    @ResponseBody
    public boolean deleteUser(String data, HttpServletRequest request, HttpServletResponse response) {
        // 传过来是Json 数据
    	// 将data json格式的转为对象list
    	Gson gson = new Gson();
        List<User> users = gson.fromJson(data, new TypeToken<List<User>>(){}.getType());
    	for (User user : users) {
    		logger.debug("删除用户===>>id:"+user.getId()+"===>>用户名"+user.getUsername());
		}
    	boolean result = false;
    	//执行批量删除的逻辑
    	result = userServie.delete(users);
    	try {
    		//TODO 在魔方中删除相应的用户
			userServie.deleteFromDataMagic(users);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    //查询所有
    @RequestMapping(value = "/findAllByPage", method = RequestMethod.POST)
    @ResponseBody
    public String findAllByPage(int rows,int page,HttpServletRequest request, HttpServletResponse response) {
        List<User> users = null;
        //所有用户的总数
        int total = 0;
			
        try {
        	total = userServie.count();
            users = userServie.get(rows, page);
            Map<String,Object> rs = new HashMap<String, Object>();
            rs.put("total",total);
            rs.put("rows", users);
            logger.debug("分页查询===>>total:"+total+"===>>rows:"+users.size());
            response.setStatus(200);
            return JSONUtil.objectToJson(rs);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return BaseResponse.getError(response,e.getMessage());
        }
    }
    
    //更新用户
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public boolean updateUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        //传过来是Json 数据
    	//User user = new Gson().fromJson(data, User.class);
    	
    	// 首先根据 project 名称 和 用户名称 查找该用户的 角色,如果是从后台管理员变为神策用户 则添加，否者对
    	User userFind;
		try {
			userFind = userServie.getById(user.getId());
			if(-1 == userFind.getRole() && -1 != user.getRole()){
	    		// 如果开始为 后台用户，更改为 神策用户，进行插入神策用户
					userServie.saveToDataMagic(user);
					return false;
	    	}else{
	    		//TODO 更新神策用户
	    		userServie.updateDataMagicRole(user.getId(), user.getRole() + "");
	    	}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    	
    	//更新神策上用户的密码 或者角色
    	//将全部的用户查询出来
    	
    	/**
    	 * 在所有结果集中 根据名称 查询出 用户id，然后根据id 更新 用户角色 http://123.56.25.140:8107/api/account/880
    	 * post : {"role":2}
    	 */
    	
    	return userServie.update(user);
    }
    
    //模糊查询
    @RequestMapping(value = "/findUser", method = RequestMethod.POST)
    @ResponseBody
    public Map findUserBlur(String username, HttpServletRequest request, HttpServletResponse response) {
		List<User> userList = userServie.getByBlurName(username);
		Map<String, Object> map =  new HashMap<String, Object>();
		int total = 0;
	     //所有用户的总数
		total = userList.size();
		map.put("total", total);
		map.put("rows", userList);
		map.put("total", total);
    	return map;
    }
    
    /**
     * 验证该用户是否已经存在，添加用户时 进行校验
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/checkUserExist",method = RequestMethod.POST)
    @ResponseBody
    public boolean checkUserExist(User user, HttpServletRequest request, HttpServletResponse response){
    	try {
    		User checkUser =  userServie.getByName(user.getUsername());
    		if(null == checkUser)
    			//没有该用户，返回false
    			return false;
    		else 
    			return true;
		} catch (Exception e) {
			logger.debug(e.getMessage()+"校验");
			return true;
		}
    }
    
    @RequestMapping(value = "/backgroundLogin", method = RequestMethod.POST)
    @ResponseBody
    public String backgroundLogin(User user, HttpServletRequest request, HttpServletResponse response){
    	String result = "";
    	try {
    		//根据姓名查找
    		String username = user.getUsername();
    		User loginUser = userServie.getByName(user.getUsername());
    		if(username !=null && !username.isEmpty()){
    			//后台系统的管理员才能够登陆
    			if(loginUser.getRole()==-1 && loginUser.getPassword().equals( Md5Util.getMD5Str(user.getPassword()))){
                	//密码正确，并且为管理员，设置session
    				HttpSession session =  request.getSession();
    				session.setAttribute("loginUser",user.getUsername());
    				//设置session生命周期为1小时
    				session.setMaxInactiveInterval(3600);
    				result= "0";//表示登陆成功
                }else{
                	result= "2";//表示密码错误 或者不是系统管理员
                }
    		}
            
        } catch (Exception e) {
            logger.debug(e.getMessage());
            result= "3";//表示输入错误，即改用户名不存在
        }
    	return result;

    }
}