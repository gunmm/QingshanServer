package com.gunmm.timer;

import java.util.TimerTask;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.OrderDao;
import com.gunmm.impl.OrderDaoImpl;

public class PushNewOrderTimer extends TimerTask {
	
	public PushNewOrderTimer() {
        super();
   }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("===============定时任务方法开始执行=================================");
		//清理支付超时的订单
		OrderDao orderDao = new OrderDaoImpl();
		JSONObject jsonObj = orderDao.setTimeOutOrderStatus();
		System.out.println(jsonObj.getString("result"));
		
		//查询所有未被接单的订单
		
		
		
	}
	

}
