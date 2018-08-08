package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.User;
import com.gunmm.responseModel.DriverListModel;
import com.gunmm.responseModel.ManageListModel;
import com.gunmm.responseModel.MasterListModel;

public interface UserDao {
	// 获取accesstoken
	public String getaccessTokenById(String userId);

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

	// 根据id取user and driver
	public DriverListModel getUserDriverById(String userId);

	// 根据phone取user
	public User getUserByPhone(String phoneNumber);

	// 更新user信息
	public JSONObject updateUserInfo(User user);

	// 查询的司机列表
	public List<DriverListModel> getDriverListBySiteId(String page, String rows, String siteId, String filterDriverName,
			String filterPlateNumber);

	// 查询司机条数
	public Long getDriverCount(String siteId, String filterDriverName, String filterPlateNumber);

	// 根据ID查司机详情信息
	public JSONObject getDriverInfoByDriverId(String driverId);

	// 添加司机
	public JSONObject addDriver(User user);

	// 删除司机
	public JSONObject deleteDriver(String driverId);

	// 查询的货主列表
	public List<MasterListModel> getMasterListBySiteId(String page, String rows, String siteId, String filterMasterName,
			String filterPhoneNumber);

	// 查询货主条数
	public Long getMasterCount(String siteId, String filterMasterName, String filterPhoneNumber);

	// 根据ID查货主详情信息
	public JSONObject getMasterInfoByMasterId(String masterId);

	// 添加货主
	public JSONObject addMaster(User user);

	// 删除货主
	public JSONObject deleteMaster(String masterId);

	// 查询管理员列表
	public List<ManageListModel> getManageListBySiteId(String page, String rows, String siteId, String filterNickName,
			String filterPhoneNumber);

	// 查询管理员条数
	public Long getManageCount(String siteId, String filterNickName, String filterPhoneNumber);

	// 添加管理员
	public JSONObject addManage(User user);

	// 判断手机号是否已经注册
	public boolean judgeUserByPhone(String phoneNumber);

	// 判断身份证号是否已经注册
	public boolean judgeUserIdCardNumber(String userIdCardNumber);

	// 判断驾驶证号是否已经注册
	public boolean judgeDriverLicenseNumber(String driverLicenseNumber);

}
