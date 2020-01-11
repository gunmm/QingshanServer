package com.gunmm.dao;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.PayInfo;
import com.gunmm.model.PingMuUser;

public interface PingMuUserDao {

	// 添加
	public JSONObject addUser(PingMuUser pingMuUser);

	// 更新
	public JSONObject updateUser(PingMuUser pingMuUser);

	// 根据id取user
	public PingMuUser getUserById(String deviceId);

	// 查询是否显示广告
	public String getBaiduString();
	
	// 添加
	public JSONObject addPay(PayInfo payInfo);

}
