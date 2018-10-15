package com.gunmm.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.UserDao;
import com.gunmm.dao.VehicleDao;
import com.gunmm.impl.UserDaoImpl;
import com.gunmm.impl.VehicleImpl;
import com.gunmm.model.User;
import com.gunmm.model.Vehicle;
import com.gunmm.responseModel.DriverListModel;
import com.gunmm.responseModel.ManageListModel;
import com.gunmm.responseModel.MasterListModel;
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
		DriverListModel user = userDao.getUserDriverById(userId);
		user.setPassword("");
		return JSONUtils.responseToJsonString("1", "", "请求成功！", user);
	}

	// 根据userId更新user信息
	@RequestMapping("/updateUserInfo")
	@ResponseBody
	private JSONObject updateUserInfo(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String userId = object.getString("userId");
		UserDao userDao = new UserDaoImpl();
		User user = userDao.getUserById(userId);
		String personImageUrl = object.getString("personImageUrl");
		String nickname = object.getString("nickname");
		String bankCardNumber = object.getString("bankCardNumber");

		if (personImageUrl != null) {
			if (personImageUrl.length() > 0) {
				user.setPersonImageUrl(personImageUrl);
			}
		}

		if (nickname != null) {
			if (nickname.length() > 0) {
				user.setNickname(nickname);
			}
		}

		if (bankCardNumber != null) {
			if (bankCardNumber.length() > 0) {
				user.setBankCardNumber(bankCardNumber);
			}
		}

		user.setUpdateTime(new Date());
		return userDao.updateUserInfo(user);
	}

	// 站点管理员修改银行卡获取验证码
	@RequestMapping("/getBankCode")
	@ResponseBody
	private JSONObject getBankCode(HttpServletRequest request) {

		JSONObject object = (JSONObject) request.getAttribute("body");
		String userId = object.getString("userId");
		UserDao userDao = new UserDaoImpl();
		User user = userDao.getUserById(userId);
		return userDao.getVerificationCode(user.getPhoneNumber(), "1");
	}

	// 司机上下班
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

	// 司机更新位置
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

	// 查询司机列表
	@RequestMapping("/getDriverList")
	@ResponseBody
	private JSONObject getDriverList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String rows = object.getString("rows");
		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));
		String filterDriverName = object.getString("filterDriverName");
		String filterPlateNumber = object.getString("filterPlateNumber");

		String siteId = object.getString("siteId");

		UserDao userDao = new UserDaoImpl();
		List<DriverListModel> driverList = userDao.getDriverListBySiteId(page, rows, siteId, filterDriverName,
				filterPlateNumber);

		Long driverCount = userDao.getDriverCount(siteId, filterDriverName, filterPlateNumber);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("driverCount", driverCount);
		jsonObject.put("driverList", driverList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}

	// 根据id查询详情
	@RequestMapping("/getDriverInfoById")
	@ResponseBody
	private JSONObject getDriverInfoById(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		String driverId = object.getString("driverId");

		UserDao userDao = new UserDaoImpl();

		return userDao.getDriverInfoByDriverId(driverId);
	}

	// 添加司机
	@RequestMapping("/addDriver")
	@ResponseBody
	private JSONObject addDriver(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		JSONObject userObject = object.getJSONObject("user");
		JSONObject vehicleObject = object.getJSONObject("vehicle");
		Vehicle vehicle = new Vehicle();
		vehicle = JSONObject.parseObject(vehicleObject.toJSONString(), Vehicle.class);

		User user = new User();
		user = JSONObject.parseObject(userObject.toJSONString(), User.class);
		user.setDriverType("1");

		UserDao userDao = new UserDaoImpl();
		VehicleDao vehicleDao = new VehicleImpl();

		// 判断手机号
		if (userDao.judgeUserByPhone(user.getPhoneNumber())) {
			return JSONUtils.responseToJsonString("0", "手机号已被注册！", "手机号已被注册！", "");
		}

		// 判断身份证号
		if (userDao.judgeUserIdCardNumber(user.getUserIdCardNumber())) {
			return JSONUtils.responseToJsonString("0", "身份证号已被注册！", "身份证号已被注册！", "");
		}

		// 判断驾驶证
		if (userDao.judgeDriverLicenseNumber(user.getDriverLicenseNumber())) {
			return JSONUtils.responseToJsonString("0", "驾驶证号已被注册！", "驾驶证号已被注册！", "");
		}

		// 判断车牌号
		if (vehicleDao.judgeVehicleByPlateNumber(vehicle.getPlateNumber())) {
			return JSONUtils.responseToJsonString("0", "车牌号已被注册！", "车牌号已被注册！", "");
		}
		// 判断营运证
		if (vehicleDao.judgeVehicleByOperationCertificateNumber(vehicle.getOperationCertificateNumber())) {
			return JSONUtils.responseToJsonString("0", "营运证号已被注册！", "营运证号已被注册！", "");
		}

		// 判断行驶证
		if (vehicleDao.judgeVehicleByDrivingCardNumber(vehicle.getDrivingCardNumber())) {
			return JSONUtils.responseToJsonString("0", "行驶证号已被注册！", "行驶证号已被注册！", "");
		}

		// 判断车辆登记证
		if (vehicleDao.judgeVehicleByVehicleRegistrationNumber(vehicle.getVehicleRegistrationNumber())) {
			return JSONUtils.responseToJsonString("0", "车辆登记证号已被注册！", "车辆登记证号已被注册！", "");
		}

		JSONObject jsonObj = vehicleDao.addVehicle(vehicle);
		String result_code = jsonObj.getString("result_code");

		if (!"1".equals(result_code)) {
			return jsonObj;
		}
		String addVehicleId = jsonObj.getString("object");

		user.setVehicleId(addVehicleId);

		String userId = UUID.randomUUID().toString();
		user.setUserId(userId);
		JSONObject jsonObj2 = userDao.addDriver(user);
		String result_code2 = jsonObj2.getString("result_code");

		if (!"1".equals(result_code2)) {
			vehicleDao.deleteVehicle(addVehicleId);
			return jsonObj2;
		}
		vehicle.setBindingDriverId(userId);
		vehicleDao.updateVehicleInfo(vehicle);

		return JSONUtils.responseToJsonString("1", "", "注册成功！", "");
	}

	// 编辑司机
	@RequestMapping("/editDriver")
	@ResponseBody
	private JSONObject editDriver(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		JSONObject userObject = object.getJSONObject("user");
		JSONObject vehicleObject = object.getJSONObject("vehicle");
		Vehicle vehicle = new Vehicle();
		vehicle = JSONObject.parseObject(vehicleObject.toJSONString(), Vehicle.class);

		User user = new User();
		user = JSONObject.parseObject(userObject.toJSONString(), User.class);

		UserDao userDao = new UserDaoImpl();
		VehicleDao vehicleDao = new VehicleImpl();

		Vehicle newVehicle = vehicleDao.getVehicleById(vehicle.getVehicleId());
		newVehicle.setPlateNumber(vehicle.getPlateNumber());
		newVehicle.setVehicleType(vehicle.getVehicleType());
		newVehicle.setVehicleBrand(vehicle.getVehicleBrand());
		newVehicle.setVehicleModel(vehicle.getVehicleModel());
		newVehicle.setLoadWeight(vehicle.getLoadWeight());
		newVehicle.setVehicleMakeDate(vehicle.getVehicleMakeDate());
		newVehicle.setDrivingCardNumber(vehicle.getDrivingCardNumber());
		newVehicle.setVehicleRegistrationNumber(vehicle.getVehicleRegistrationNumber());
		newVehicle.setOperationCertificateNumber(vehicle.getOperationCertificateNumber());
		newVehicle.setGpsType(vehicle.getGpsType());
		newVehicle.setGpsSerialNumber(vehicle.getGpsSerialNumber());
		newVehicle.setVehiclePhoto(vehicle.getVehiclePhoto());
		newVehicle.setInsuranceNumber(vehicle.getInsuranceNumber());
		newVehicle.setBusinessLicenseNumber(vehicle.getBusinessLicenseNumber());
		newVehicle.setVehicleIdCardNumber(vehicle.getVehicleIdCardNumber());

		JSONObject jsonObj = vehicleDao.updateVehicleInfo(newVehicle);
		String result_code = jsonObj.getString("result_code");
		if (!"1".equals(result_code)) {
			return jsonObj;
		}

		User newUser = userDao.getUserById(user.getUserId());
		newUser.setNickname(user.getNickname());
		newUser.setPhoneNumber(user.getPhoneNumber());
		newUser.setUserIdCardNumber(user.getUserIdCardNumber());
		newUser.setDriverLicenseNumber(user.getDriverLicenseNumber());
		newUser.setDriverQualificationNumber(user.getDriverQualificationNumber());

		newUser.setUpdateTime(new Date());
		return userDao.updateUserInfo(newUser);

	}

	// 删除司机
	@RequestMapping("/deleteDriver")
	@ResponseBody
	private JSONObject deleteDriver(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		String driverId = object.getString("driverId");

		UserDao userDao = new UserDaoImpl();

		return userDao.deleteDriver(driverId);
	}

	// 车主绑定小司机
	@RequestMapping("/binderSmallDriver")
	@ResponseBody
	private JSONObject binderSmallDriver(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		String driverId = object.getString("driverId");
		String smallDriverId = object.getString("smallDriverId");

		UserDao userDao = new UserDaoImpl();
		User driver = userDao.getUserById(driverId);
		User smallDriver = userDao.getUserById(smallDriverId);
		smallDriver.setVehicleId(driver.getVehicleId());
		smallDriver.setBelongSiteId(driver.getBelongSiteId());
		smallDriver.setSuperDriver(driverId);

		VehicleDao vehicleDao = new VehicleImpl();
		Vehicle vehicle = vehicleDao.getVehicleById(driver.getVehicleId());
		vehicle.setBindingDriverId(smallDriverId);
		JSONObject jsonObj = vehicleDao.updateVehicleInfo(vehicle);
		String result_code = jsonObj.getString("result_code");
		if (!"1".equals(result_code)) {
			return jsonObj;
		}
		return userDao.updateUserInfo(smallDriver);
	}

	// 查询车主已绑定的小司机列表
	@RequestMapping("/getDriverBindSmallDriverList")
	@ResponseBody
	private JSONObject getDriverBindSmallDriverList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String driverId = object.getString("driverId");

		UserDao userDao = new UserDaoImpl();
		List<DriverListModel> driverList = userDao.getDriverBindSmallDriverList(driverId);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", driverList);
	}

	// 查询未被绑定的小司机列表
	@RequestMapping("/getUnBindSmallDriverList")
	@ResponseBody
	private JSONObject getUnBindSmallDriverList(HttpServletRequest request) {
//		JSONObject object = (JSONObject) request.getAttribute("body");
//		String rows = object.getString("rows");
//		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));
//		String filterDriverPhone = object.getString("filterDriverPhone");

		UserDao userDao = new UserDaoImpl();
		List<DriverListModel> driverList = userDao.getUnBindSmallDriverList();

		return JSONUtils.responseToJsonString("1", "", "查询成功！", driverList);
	}
	
	//删除绑定司机

	// 查询货主列表
	@RequestMapping("/getMasterList")
	@ResponseBody
	private JSONObject getMasterList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String rows = object.getString("rows");
		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));
		String filterMasterName = object.getString("filterMasterName");
		String filterPhoneNumber = object.getString("filterPhoneNumber");

		String siteId = object.getString("siteId");

		UserDao userDao = new UserDaoImpl();
		List<MasterListModel> masterList = userDao.getMasterListBySiteId(page, rows, siteId, filterMasterName,
				filterPhoneNumber);

		Long masterCount = userDao.getMasterCount(siteId, filterMasterName, filterPhoneNumber);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("masterCount", masterCount);
		jsonObject.put("masterList", masterList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}

	// 根据id查询详情
	@RequestMapping("/getMasterInfoById")
	@ResponseBody
	private JSONObject getMasterInfoById(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		String masterId = object.getString("masterId");

		UserDao userDao = new UserDaoImpl();

		return userDao.getMasterInfoByMasterId(masterId);
	}

	// 添加货主
	@RequestMapping("/addMaster")
	@ResponseBody
	private JSONObject addMaster(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		User user = new User();
		user = JSONObject.parseObject(object.toJSONString(), User.class);

		UserDao userDao = new UserDaoImpl();

		// 判断手机号
		if (userDao.judgeUserByPhone(user.getPhoneNumber())) {
			return JSONUtils.responseToJsonString("0", "手机号已被注册！", "手机号已被注册！", "");
		}

		// 判断身份证号
		if (userDao.judgeUserIdCardNumber(user.getUserIdCardNumber())) {
			return JSONUtils.responseToJsonString("0", "身份证号已被注册！", "身份证号已被注册！", "");
		}

		return userDao.addMaster(user);
	}

	// 编辑货主
	@RequestMapping("/editMaster")
	@ResponseBody
	private JSONObject editMaster(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		User user = new User();
		user = JSONObject.parseObject(object.toJSONString(), User.class);

		UserDao userDao = new UserDaoImpl();

		User newUser = userDao.getUserById(user.getUserId());
		newUser.setNickname(user.getNickname());
		newUser.setPhoneNumber(user.getPhoneNumber());
		newUser.setUserIdCardNumber(user.getUserIdCardNumber());
		newUser.setMainGoodsName(user.getMainGoodsName());
		newUser.setUpdateTime(new Date());
		return userDao.updateUserInfo(newUser);
	}

	// 删除货主
	@RequestMapping("/deleteMaster")
	@ResponseBody
	private JSONObject deleteMaster(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		String masterId = object.getString("masterId");

		UserDao userDao = new UserDaoImpl();
		return userDao.deleteMaster(masterId);
	}

	// 查询管理员列表
	@RequestMapping("/getManageList")
	@ResponseBody
	private JSONObject getManageList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String rows = object.getString("rows");
		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));
		String filterNickName = object.getString("filterNickName");
		String filterPhoneNumber = object.getString("filterPhoneNumber");

		String siteId = object.getString("siteId");

		UserDao userDao = new UserDaoImpl();
		List<ManageListModel> manageList = userDao.getManageListBySiteId(page, rows, siteId, filterNickName,
				filterPhoneNumber);

		Long manageCount = userDao.getManageCount(siteId, filterNickName, filterPhoneNumber);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("manageCount", manageCount);
		jsonObject.put("manageList", manageList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}

	// 添加管理员
	@RequestMapping("/addManage")
	@ResponseBody
	private JSONObject addManage(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		User user = new User();
		user = JSONObject.parseObject(object.toJSONString(), User.class);

		UserDao userDao = new UserDaoImpl();

		// 判断手机号
		if (userDao.judgeUserByPhone(user.getPhoneNumber())) {
			return JSONUtils.responseToJsonString("0", "手机号已被注册！", "手机号已被注册！", "");
		}

		// 判断身份证号
		if (userDao.judgeUserIdCardNumber(user.getUserIdCardNumber())) {
			return JSONUtils.responseToJsonString("0", "身份证号已被注册！", "身份证号已被注册！", "");
		}

		return userDao.addManage(user);
	}

	// 编辑管理员
	@RequestMapping("/editManage")
	@ResponseBody
	private JSONObject editManage(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		User user = new User();
		user = JSONObject.parseObject(object.toJSONString(), User.class);

		UserDao userDao = new UserDaoImpl();

		User newUser = userDao.getUserById(user.getUserId());
		newUser.setNickname(user.getNickname());
		newUser.setPhoneNumber(user.getPhoneNumber());
		newUser.setUserIdCardNumber(user.getUserIdCardNumber());
		newUser.setUpdateTime(new Date());
		return userDao.updateUserInfo(newUser);
	}

}
