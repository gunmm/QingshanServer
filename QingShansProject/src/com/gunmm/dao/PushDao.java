package com.gunmm.dao;

import com.gunmm.model.Order;

public interface PushDao {
	
	//新建订单推送给三百公里内司机
	public void pushForOrder(Order order);

	//订单信息更新后推送给下单人
	public void updateOrderSuccessPushForUser(String orderId);
	
	//取消订单后推送给对应司机
	public void cancelSuccessPushForUser(String orderId);
	
	//司机开始执行预约订单时推给下单人
	public void beginAppointOrderPushForUser(String orderId);


}
