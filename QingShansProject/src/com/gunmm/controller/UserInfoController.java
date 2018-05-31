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
@RequestMapping("/mobile")
public class UserInfoController {

	// 根据userId获取user信息
	@RequestMapping("/getUserInfoById")
	@ResponseBody
	private JSONObject getUserInfoById(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String userId = object.getString("userId");

		if (userId == null) {
			return JSONUtils.responseToJsonString("0", "", "未接收到用户ID！", "");
		}

		UserDao userDao = new UserDaoImpl();
		User user = userDao.getUserById(userId);
		return JSONUtils.responseToJsonString("1", "", "请求成功！", user);
	}
	
	//根据userId更新user信息
	@RequestMapping("/updateUserInfo")
	@ResponseBody
	private JSONObject updateUserInfo(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String userId = object.getString("userId");
		String personImageUrl = object.getString("personImageUrl");
		String nickname = object.getString("nickname");
		String plateNumber = object.getString("plateNumber");
		String vehicleType = object.getString("vehicleType");
		String driverLicenseImageUrl = object.getString("driverLicenseImageUrl");
		String driverVehicleImageUrl = object.getString("driverVehicleImageUrl");
		String driverThirdImageUrl = object.getString("driverThirdImageUrl");
		
		UserDao userDao = new UserDaoImpl();
		User user = userDao.getUserById(userId);
		user.setPersonImageUrl(personImageUrl);
		user.setNickname(nickname);
		user.setPlateNumber(plateNumber);
		user.setVehicleType(vehicleType);
		user.setDriverLicenseImageUrl(driverLicenseImageUrl);
		user.setDriverVehicleImageUrl(driverVehicleImageUrl);
		user.setDriverThirdImageUrl(driverThirdImageUrl);
		return userDao.updateUserInfo(user);
	}
	
	//司机上下班
	@RequestMapping("/operateWork")
	@ResponseBody
	private JSONObject goToWork(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String userId = object.getString("userId");
		String status = object.getString("status");

		UserDao userDao = new UserDaoImpl();
		User user = userDao.getUserById(userId);
		user.setStatus(status);
		return userDao.updateUserInfo(user);
	}
	
	//司机更新位置
	@RequestMapping("/updateLocation")
	@ResponseBody
	private JSONObject updateLocation(HttpServletRequest request) {
			JSONObject object = (JSONObject) request.getAttribute("body");
			String userId = object.getString("userId");
			Double nowLatitude = object.getDouble("nowLatitude");
			Double nowLongitude = object.getDouble("nowLongitude");

			UserDao userDao = new UserDaoImpl();
			User user = userDao.getUserById(userId);
			user.setNowLatitude(nowLatitude);
			user.setNowLongitude(nowLongitude);
			return userDao.updateUserInfo(user);
		}
}
