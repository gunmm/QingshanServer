package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.User;
import com.gunmm.responseModel.DriverListModel;

public interface UserDao {
	// 获取验证码
	public JSONObject getVerificationCode(String phoneNumber, String type);

	// 注册
	public JSONObject addUser(User user);

	// 登陆
	public JSONObject login(String phoneNumber, String password, String plateform);

	// 重置密码
	public JSONObject resetPassword(String phoneNumber, String password);

	// 根据id取user
	public User getUserById(String userId);

	// 根据phone取user
	public User getUserByPhone(String phoneNumber);

	// 更新user信息
	public JSONObject updateUserInfo(User user);

	// 查询的司机列表
	public List<DriverListModel> getDriverListBySiteId(String page, String rows, String siteId);

	// 查询司机条数
	public Long getDriverCount(String siteId);

	// 添加司机
	public JSONObject addDriver(User user);
	
	//删除司机
	public JSONObject deleteDriver(String driverId);
	
	// 判断手机号是否已经注册
	public boolean judgeUserByPhone(String phoneNumber);

	// 判断身份证号是否已经注册
	public boolean judgeUserIdCardNumber(String userIdCardNumber);

	// 判断驾驶证号是否已经注册
	public boolean judgeDriverLicenseNumber(String driverLicenseNumber);

}
