package com.gunmm.dao;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.User;

public interface UserDao {
	// 获取验证码
    public JSONObject getVerificationCode(String phoneNumber);
	
	// 注册
	public JSONObject addUser(User user);
	
	// 登陆
	public JSONObject login(String phoneNumber, String password);
	
	//根据id取user
	public User getUserById(String userId);
	
	//设置user状态status
	public void setUserStatus(String status, User user);


}
