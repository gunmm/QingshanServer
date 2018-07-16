package com.gunmm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.DictionaryDao;
import com.gunmm.dao.UserDao;
import com.gunmm.dao.VehicleDao;
import com.gunmm.impl.DictionaryImpl;
import com.gunmm.impl.UserDaoImpl;
import com.gunmm.impl.VehicleImpl;
import com.gunmm.model.User;
import com.gunmm.model.Vehicle;
import com.gunmm.responseModel.DriverListModel;
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

		DictionaryDao dictionaryDao = new DictionaryImpl();
		UserDao userDao = new UserDaoImpl();
		User user = userDao.getUserById(userId);
		// user.setCarTypeName(dictionaryDao.getValueTextByNameAndkey("车辆类型",
		// user.getVehicleType()));
		return JSONUtils.responseToJsonString("1", "", "请求成功！", user);
	}

	// 根据userId更新user信息
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
		// user.setPlateNumber(plateNumber);
		// user.setVehicleType(vehicleType);
		// user.setDriverLicenseImageUrl(driverLicenseImageUrl);
		// user.setDriverVehicleImageUrl(driverVehicleImageUrl);
		// user.setDriverThirdImageUrl(driverThirdImageUrl);
		// if ("2".equals(user.getDriverCertificationStatus())) {
		// user.setDriverCertificationStatus("2");
		// }else {
		// user.setDriverCertificationStatus("1");
		// }

		return userDao.updateUserInfo(user);
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

		String siteId = object.getString("siteId");

		UserDao userDao = new UserDaoImpl();
		List<DriverListModel> driverList = userDao.getDriverListBySiteId(page, rows, siteId);

		Long driverCount = userDao.getDriverCount(siteId);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("driverCount", driverCount);
		jsonObject.put("driverList", driverList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
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

		user.setVehicleId(jsonObj.getString("object"));

		return userDao.addDriver(user);
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
		newUser.setVehicleType(user.getVehicleType());
		
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

}
