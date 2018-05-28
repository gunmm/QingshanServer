package com.gunmm.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.utils.JSONUtils;


/**
 * Servlet Filter implementation class UTFFilter
 */
@WebFilter("/mobile/*")
public class UTFFilter implements Filter {

    /**
     * Default constructor. 
     */
    public UTFFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @throws IOException 
	 * @throws  
	 * @throws IOException 
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
				
		byte[] data;
		
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpServletResponse.setContentType("text/html;charset=utf-8");
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			httpServletRequest.setCharacterEncoding("utf-8");
			data =JSONUtils.readInputStream(httpServletRequest);
			JSONObject head = JSONUtils.getHead(data);
			
			if (head.getString("token").length() > 0) {
				JSONObject body = JSONUtils.getBody(data);
				request.setAttribute("body", body);
				chain.doFilter(request, response);
			}else {
				PrintWriter out = httpServletResponse.getWriter();
				out.println(JSONUtils.responseToJsonString("0", "token无效", "请求失败！", "").toString());
				out.flush();
				out.close();
				return;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			PrintWriter out = httpServletResponse.getWriter();
			out.println(JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "请求异常！", "").toString());
			out.flush();
			out.close();
			return;
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
