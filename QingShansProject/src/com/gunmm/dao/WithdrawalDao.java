package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Withdrawal;
import com.gunmm.responseModel.WithdrawalListModel;
import com.gunmm.responseModel.WithdrawalOrderListModel;

public interface WithdrawalDao {

	// 获取父站点待提现列表
	public List<WithdrawalListModel> getSiteWithdrawalList(String dataStr, String page, String rows,
			String filterSiteName, String filterLowsManName);

	// 查询父站点待提现列表条数
	public Long getSiteWithdrawalListCount(String dataStr, String filterSiteName, String filterLowsManName);

	// 获取子站点待提现列表
	public List<WithdrawalListModel> getChildSiteWithdrawalList(String dataStr, String siteId, String type, String page, String rows,
			String filterSiteName, String filterLowsManName);

	// 查询子站点待提现列表条数
	public Long getChildSiteWithdrawalListCount(String dataStr, String siteId, String filterSiteName, String filterLowsManName);

	// 获取父站点司机总提成订单对应的订单记录列表
	public List<WithdrawalOrderListModel> getTotalDriverItemOrderListBySiteId(String dataStr, String siteId,
			String page, String rows);

	// 查询父站点司机总提成订单对应的订单记录列表条数
	public Long getTotalDriverItemOrderListCount(String dataStr, String siteId);

	// 获取父站点货主总提成订单对应的订单记录列表
	public List<WithdrawalOrderListModel> getTotalMasterItemOrderListBySiteId(String dataStr, String siteId,
			String page, String rows);

	// 查询父站点货主总提成订单对应的订单记录列表条数
	public Long getTotalMasterItemOrderListCount(String dataStr, String siteId);

	// 新建提现记录
	public JSONObject addWithdrawal(Withdrawal withdrawal);

	// 编辑提现记录
	public JSONObject editWithdrawal(Withdrawal withdrawal);

	// 获取提现记录by id
	public Withdrawal getWithdrawalById(String withdrawalId);

	// 提现指定时间的订单 （九号之前创建，并且已经完成的订单）
	public JSONObject withdrawalBeforeOrder(String dataStr, String siteId, String withdrawalId);

}
