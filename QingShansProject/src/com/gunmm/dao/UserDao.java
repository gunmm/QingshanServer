package com.gunmm.dao;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.User;

public interface UserDao {
	// 获取验证码
    public JSONObject getVerificationCode(String phoneNumber, String type);
	
	// 注册
	public JSONObject addUser(User user);
	
	// 登陆
	public JSONObject login(String phoneNumber, String password, String plateform);
	
	//重置密码
	public JSONObject resetPassword(String phoneNumber, String password);
	
	//根据id取user
	public User getUserById(String userId);
	
	// 根据phone取user
	public User getUserByPhone(String phoneNumber);
	
	//更新user信息
	public JSONObject updateUserInfo(User user);


}
