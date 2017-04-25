package com.dodoca.datamagic.common.controller.springmvc;

import com.dodoca.datamagic.common.service.DataMagicService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author huhongda
 *
 */
@Controller
@RequestMapping("/dashboards")
public class DashboardController {	
	@Autowired
    private DataMagicService dataMagicService;

    private Logger logger = Logger.getLogger(DashboardController.class);
    
    /**
     * 用户概览授权页面 ,返回所有概览和相应用户对应的概览信息
     * @param username 传入的用户名
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/authorize", method = RequestMethod.POST)
    @ResponseBody
	public Map<String, Object>  dashboardAuth(String username, HttpServletRequest request, HttpServletResponse response){
		
    	Map<String, Object> result = new HashMap<String, Object>();
    	result = dataMagicService.getDashboards(username);
    	return result;
	}
    
    /**
     * 保存某个 用户的 概览
     * @param dashboards
     * @param username
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/saveDashboardAuth", method = RequestMethod.POST)
    @ResponseBody
    public String saveDashboardAuth(Integer[] dashboards,String username ,HttpServletRequest request, HttpServletResponse response){
    	
    	//根据用户名 获取他的概览,对传过来的概览id进行授权
    	String data1 = dataMagicService.getUserDashboards(username).getData();
    	
    	List<Map<String, Object>> data = new Gson().fromJson(data1, new TypeToken<List<Map<String, Object>>>() {}.getType());
    	
    	for (Map<String, Object> map : data) {
			System.out.println((int)Double.parseDouble(map.get("id").toString()));
		}
    	return "success";
    }
    
}
