package com.gunmm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.UserDao;
import com.gunmm.impl.UserDaoImpl;
import com.gunmm.model.User;
import com.gunmm.utils.JSONUtils;


@Controller
public class UserController {
	
	@RequestMapping("/register")
	@ResponseBody
	private JSONObject addUser(HttpServletRequest request) {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			httpServletRequest.setCharacterEncoding("utf-8");
			byte[] data =JSONUtils.readInputStream(httpServletRequest);
			JSONObject body = JSONUtils.getBody(data);
			User user = new User();
			user = JSONObject.parseObject(body.toJSONString(), User.class);
			UserDao userDao = new UserDaoImpl();
			return userDao.addUser(user);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "注册失败！", "");
		}
	}
	
	@RequestMapping("/getCode")
	@ResponseBody
	private JSONObject getCode(HttpServletRequest request) {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			httpServletRequest.setCharacterEncoding("utf-8");
			byte[] data =JSONUtils.readInputStream(httpServletRequest);
			JSONObject body = JSONUtils.getBody(data);
			String phoneNumber = body.getString("phoneNumber");
			UserDao userDao = new UserDaoImpl();
			return userDao.getVerificationCode(phoneNumber);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "验证码发送失败！", "");
		}
	}
	
	@RequestMapping("/login")
	@ResponseBody
	private JSONObject login(HttpServletRequest request) {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			httpServletRequest.setCharacterEncoding("utf-8");
			byte[] data =JSONUtils.readInputStream(httpServletRequest);
			JSONObject body = JSONUtils.getBody(data);
			String phoneNumber = body.getString("phoneNumber");
			String password = body.getString("password");
			UserDao userDao = new UserDaoImpl();
			return userDao.login(phoneNumber, password);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "登陆失败！", "");
		}
		
	}

}
