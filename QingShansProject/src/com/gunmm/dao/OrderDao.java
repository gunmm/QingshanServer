package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Order;
import com.gunmm.model.User;
import com.gunmm.responseModel.OrderListModel;

public interface OrderDao {
	// 新建订单
	public JSONObject addOrder(Order order);
	
	//取消订单
    public String cancelOrderById(String orderId);
	
	// 抢单
	public String robOrder(String driverId, String orderId);
	
	//根据订单ID拿订单
	public Order getOrderById(String orderId);

	
	//查询200公里以内司机
	public List<User> queryDriverForOrder(Order order);
	
	//根据userid查询订单列表
	public List<OrderListModel> getOrderListByUserId(String userId);


}
