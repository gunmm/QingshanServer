package com.gunmm.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.PingMuUserDao;
import com.gunmm.impl.PingMuImpl;
import com.gunmm.model.PingMuUser;
import com.gunmm.utils.JSONUtils;

@Controller
public class PingMuUserController {

	@RequestMapping("/addPingMuUser")
	@ResponseBody
	private JSONObject addPingMuUser(HttpServletRequest request) {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			httpServletRequest.setCharacterEncoding("utf-8");
			byte[] data = JSONUtils.readInputStream(httpServletRequest);
			JSONObject body = JSONUtils.getBody(data);
			PingMuUser pingMuUser = new PingMuUser();
			pingMuUser = JSONObject.parseObject(body.toJSONString(), PingMuUser.class);
			PingMuUserDao pingMuUserDao = new PingMuImpl();
			return pingMuUserDao.addUser(pingMuUser);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "操作失败！", "");
		}
	}

	@RequestMapping("/updatePingMuUser")
	@ResponseBody
	private JSONObject updatePingMuUser(HttpServletRequest request) {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			httpServletRequest.setCharacterEncoding("utf-8");
			byte[] data = JSONUtils.readInputStream(httpServletRequest);
			JSONObject body = JSONUtils.getBody(data);
			PingMuUser pingMuUser = new PingMuUser();
			pingMuUser = JSONObject.parseObject(body.toJSONString(), PingMuUser.class);
			PingMuUserDao pingMuUserDao = new PingMuImpl();
			PingMuUser pingMuUserMid = pingMuUserDao.getUserById(pingMuUser.getDeviceId());
			if (pingMuUserMid != null) {
				pingMuUser.setCreateTime(pingMuUserMid.getCreateTime());
			}
			return pingMuUserDao.updateUser(pingMuUser);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "操作失败！", "");
		}
	}
	
	// 查询
	@RequestMapping("/getBaiDuDictionary")
	@ResponseBody
	private JSONObject getOrderCarList(HttpServletRequest request) {
		PingMuUserDao pingMuUserDao = new PingMuImpl();
		String baiduString = "0";
		baiduString = pingMuUserDao.getBaiduString();
		return JSONUtils.responseToJsonString("1", "", "查询成功！", baiduString);
	}

}