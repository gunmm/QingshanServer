package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Order;
import com.gunmm.responseModel.NearbyDriverListModel;
import com.gunmm.responseModel.OrderListModel;
import com.gunmm.responseModel.OrderListModelForSite;
import com.gunmm.responseModel.WithdrawalFinishedOrderListModel;

public interface OrderDao {
	// 新建订单
	public JSONObject addOrder(Order order);

	// 取消订单
	public JSONObject cancelOrderById(String orderId);

	// 抢单
	public JSONObject robOrder(String driverId, String orderId);

	// 根据订单ID拿订单
	public Order getOrderById(String orderId);

	// 查询300公里以内司机
	public List<NearbyDriverListModel> queryDriverForOrder(Order order);

	// 根据userid查询订单列表
	public List<OrderListModel> getOrderListByUserId(String userId, String page, String rows);

	// 查询货主订单总条数
	public Long getMasterOrderCount(String userId);

	// 查询司机订单列表
	public List<OrderListModel> getDriverOrderListByDriverId(String driverId, String page, String rows,
			String condition);

	// 查询司机订单总条数
	public Long getDriverOrderCount(String driverId);

	// 查询站点订单列表
	public List<OrderListModelForSite> getSiteOrderList(String siteId, String page, String rows);

	// 查询站点订单总条数
	public Long getSiteOrderCount(String siteId);

	// 司机设置预约订单开始执行
	public JSONObject setAppointOrderBegin(String driverId, String orderId);

	// 更新订单信息
	public JSONObject updateOrderInfo(Order order);

	// 查询带部分司机信息的订单信息
	public OrderListModel getBigOrderInfo(String orderId);

	// 将所有已被抢单但是付款超时订单状态置0
	public JSONObject setTimeOutOrderStatus();

	// 查询所有未被接单的订单
	public List<Order> getUnReceiveOrderList();

	// 设置指定时间段内订单提现状态
	public JSONObject setWithdrawal(String dataStr, String withdrawalId, String siteId);

	// 查询已提现记录对应订单列表
	public List<WithdrawalFinishedOrderListModel> getWithDrawalFinishedOrderList(String siteId, String withdrawalId,
			String page, String rows);

	// 查询已提现记录对应订单列表总条数
	public Long getWithDrawalFinishedOrderListCount(String siteId, String withdrawalId);

	// 查询指定站点及子站点的已完成订单列表
	public List<WithdrawalFinishedOrderListModel> getFinishedOrderList(String siteId, String queryTime, String page,
			String rows);

	// 查询指定站点及子站点的已完成订单列表条数
	public Long getFinishedOrderListCount(String siteId, String queryTime);

	// 更新线上支付订单的 司机 提现状态
	public JSONObject setOnlineOrderDriverWithdrawalStatus(String driverWithdrawalStatus, String driverWithdrawalId,
			String driverId);

	// 根据司机id查询司机可提现的订单总额
	public Double getDriverWithdrawalAmount(String driverId);

	// 根据司机id查询司机可提现的订单
	public List<OrderListModel> getDriverWithDrawalOrderList(String driverId);

	// 获取司机提现订单列表
	public List<OrderListModel> getDriverWithdrawaledOrderList(String driverWithdrawalId, String page, String rows);

	// 获取司机提现订单列表列表条数
	public Long getDriverWithdrawaledOrderListCount(String driverWithdrawalId);

	// 获取Mobile司机提现订单列表
	public List<OrderListModel> getMobileDriverWithdrawaledOrderList(String driverWithdrawalId);

}
