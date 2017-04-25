package com.dodoca.datamagic.common.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FilterServlet extends HttpServlet implements Filter{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**  
     * 重定向的URL  
     */  
    private String redirectURl = null; 
	
	public void doFilter(ServletRequest sRequest, ServletResponse sResponse,
			FilterChain chain) throws IOException, ServletException {
		
			HttpServletRequest request = (HttpServletRequest) sRequest;   
	        HttpServletResponse response = (HttpServletResponse) sResponse;   
	        HttpSession session = request.getSession();   
	        //获取根目录所对应的绝对路径
	        String currentURL = request.getRequestURI();
	        String path = request.getHeader("Origin")+redirectURl;
	        System.out.println(request.getHeader("Origin"));
	        //是user.html 就判断session
	        if("/datamagic/user/findAllByPage.do".equals(currentURL)){
	        	// 如果回话中的用户为空,页面重新定向到登陆页面   
	        	if (session.getAttribute("loginUser") == null) {
	        		response.sendRedirect(path);
	        	}   
	        	// 会话中存在用户，则验证用户是否存在当前页面的权限   
	        	else {   
	        		chain.doFilter(request, response);
	        	}   
	        	
	        }else{
	        	//如果是登陆界面的话，直接放行
	        	chain.doFilter(request, response);
	        }
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		redirectURl = "/sjfw/login.html";
	}
	@Override
    public void destroy() {
    }
}
