package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Order;
import com.gunmm.responseModel.NearbyDriverListModel;
import com.gunmm.responseModel.OrderListModel;

public interface OrderDao {
	// 新建订单
	public JSONObject addOrder(Order order);
	
	//取消订单
    public JSONObject cancelOrderById(String orderId);
	
	// 抢单
	public JSONObject robOrder(String driverId, String orderId);
	
	//根据订单ID拿订单
	public Order getOrderById(String orderId);

	
	//查询300公里以内司机
	public List<NearbyDriverListModel> queryDriverForOrder(Order order);
	
	//根据userid查询订单列表
	public List<OrderListModel> getOrderListByUserId(String userId, String page, String rows);
	
	//查询司机订单列表
	public List<OrderListModel> getDriverOrderListByDriverId(String driverId, String page, String rows, String condition);

	
	//司机设置预约订单开始执行
	public JSONObject setAppointOrderBegin(String driverId, String orderId);
	
	//更新订单信息
	public JSONObject updateOrderInfo(Order order);
	
	//查询带部分司机信息的订单信息
	public OrderListModel getBigOrderInfo(String orderId);
	
	
	//将所有已被抢单但是付款超时订单状态置0
	public JSONObject setTimeOutOrderStatus();
	
	//查询所有未被接单的订单
	public List<Order> getUnReceiveOrderList();
	
	
	




}
