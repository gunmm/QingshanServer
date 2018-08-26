package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Withdrawal;
import com.gunmm.responseModel.WithDrawalFinishedListModel;
import com.gunmm.responseModel.WithdrawalListModel;
import com.gunmm.responseModel.WithdrawalOrderListModel;

public interface WithdrawalDao {

	// 获取父站点待提现列表
	public List<WithdrawalListModel> getSiteWithdrawalList(String dataStr, String page, String rows,
			String filterSiteName, String filterLowsManName);

	// 查询父站点待提现列表条数
	public Long getSiteWithdrawalListCount(String dataStr, String filterSiteName, String filterLowsManName);

	// 获取子站点待提现列表 平台视角
	public List<WithdrawalListModel> getChildSiteWithdrawalList(String dataStr, String siteId, String type, String page,
			String rows, String filterSiteName, String filterLowsManName);

	// 查询子站点待提现列表条数 平台视角
	public Long getChildSiteWithdrawalListCount(String dataStr, String siteId, String filterSiteName,
			String filterLowsManName);

	// 获取子站点待提现列表 站点视角
	public List<WithdrawalListModel> getChildSiteWithdrawalListForSite(String dataStr, String siteId, String type,
			String page, String rows, String filterSiteName, String filterLowsManName);

	// 查询子站点待提现列表条数 平台视角
	public Long getChildSiteWithdrawalListCountForSite(String dataStr, String siteId, String filterSiteName,
			String filterLowsManName);

	// 获取父站点司机总提成订单对应的订单记录列表
	public List<WithdrawalOrderListModel> getTotalDriverItemOrderListBySiteId(String dataStr, String siteId,
			String page, String rows, String isChildSiteList);

	// 查询父站点司机总提成订单对应的订单记录列表条数
	public Long getTotalDriverItemOrderListCount(String dataStr, String siteId, String isChildSiteList);

	// 获取父站点货主总提成订单对应的订单记录列表
	public List<WithdrawalOrderListModel> getTotalMasterItemOrderListBySiteId(String dataStr, String siteId,
			String page, String rows, String isChildSiteList);

	// 查询父站点货主总提成订单对应的订单记录列表条数
	public Long getTotalMasterItemOrderListCount(String dataStr, String siteId, String isChildSiteList);

	// 新建提现记录
	public JSONObject addWithdrawal(Withdrawal withdrawal);

	// 编辑提现记录
	public JSONObject editWithdrawal(Withdrawal withdrawal);

	// 获取提现记录by id
	public Withdrawal getWithdrawalById(String withdrawalId);

	// 删除提现记录
	public JSONObject deleteWithdrawal(String withdrawalId);

	// 查询已提现列表
	public List<WithDrawalFinishedListModel> getFinishedWithDrawalList(String bankcardNumber, String page, String rows);

	// 查询已提现列表条数 
	public Long getFinishedWithDrawalListCount(String bankcardNumber);

}
