package com.gunmm.dao;

import com.gunmm.model.Order;

public interface PushDao {
	
	//新建订单推送给三百公里内司机
	public void pushForOrder(Order order);

	//接单后推送给下单人
	public void RobSuccessPushForUser(String orderId);
	
	//取消订单后推送给对应司机
	public void cancelSuccessPushForUser(String orderId);

}
