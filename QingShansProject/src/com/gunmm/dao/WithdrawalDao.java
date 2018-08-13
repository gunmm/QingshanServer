package com.gunmm.dao;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Withdrawal;

public interface WithdrawalDao {

	// 新建提现记录
	public Withdrawal addWithdrawal(String dataStr, String toUserId, String oprationUserId);

	// 编辑提现记录
	public JSONObject editWithdrawal(Withdrawal withdrawal);
	
	//获取提现记录by id
	public Withdrawal getWithdrawalById(String withdrawalId);
	
	// 提现本月9号之前的订单 （九号之前创建，并且已经完成的订单）
	public JSONObject withdrawalBeforeOrder(String dataStr, String withdrawalId);
}
