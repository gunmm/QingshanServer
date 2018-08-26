package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.DriverWithdrawal;
import com.gunmm.responseModel.DriverWithdrawalListModel;

public interface DriverWithdrawalDao {

	// 新建司机提现记录
	public JSONObject addDriverWithdrawal(DriverWithdrawal driverWithdrawal);

	// 编辑司机提现记录
	public JSONObject editDriverWithdrawal(DriverWithdrawal driverWithdrawal);

	// 获取司机提现记录by id
	public DriverWithdrawal getDriverWithdrawalById(String driverWithdrawalId);

	// 删除司机提现记录
	public JSONObject deleteDriverWithdrawal(DriverWithdrawal driverWithdrawal);

	// 查询司机提现记录列表
	public List<DriverWithdrawalListModel> getDriverWithdrawalList(String driverId, String page, String rows, String toUserName, String bankCardNumber);

	// 查询司机提现记录列表总条数
	public Long getDriverWithdrawalListCount(String driverId, String toUserName, String bankCardNumber);
	
	
}
