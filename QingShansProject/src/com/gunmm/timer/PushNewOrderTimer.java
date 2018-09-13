package com.gunmm.timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

import com.gunmm.dao.FlowDao;
import com.gunmm.dao.OrderDao;
import com.gunmm.dao.PushDao;
import com.gunmm.impl.FlowImpl;
import com.gunmm.impl.OrderDaoImpl;
import com.gunmm.impl.PushDaoImpl;
import com.gunmm.model.Flow;
import com.gunmm.model.Order;
import com.gunmm.responseModel.NearbyDriverListModel;

public class PushNewOrderTimer extends TimerTask {
	
	public PushNewOrderTimer() {
        super();
   }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(new Date());
		System.out.println(dateString + "===============定时任务方法开始执行=================================");
		Thread t = new Thread(new Runnable() {
			public void run() {
				//清理支付超时的订单
				OrderDao orderDao = new OrderDaoImpl();
				orderDao.setTimeOutOrderStatus();
				//查询所有未被接单的订单
				List<Order> orderList = orderDao.getUnReceiveOrderList();
				for (Order order : orderList) {
					List<NearbyDriverListModel> driverList = orderDao.queryDriverForOrder(order);
					int count = 0;
					FlowDao flowDao = new FlowImpl();
					List<NearbyDriverListModel> pushDriverList = new ArrayList<NearbyDriverListModel>();
					for (NearbyDriverListModel nearbyDriverListModel : driverList) {
						if (count > 5) {
							break;
						}
						List<Flow> flowList = flowDao.getFlowListByOrderId(order.getOrderId(), nearbyDriverListModel.getUserId());
						boolean signStatus = true;
						for (Flow flow : flowList) {
							if ("0".equals(flow.getFlowStatus()) || "2".equals(flow.getFlowStatus())) {
								signStatus = false;
								break;
							}
						}
						if (signStatus == true) {
							pushDriverList.add(nearbyDriverListModel);
							Flow flow = new Flow();
							flow.setFlowId(UUID.randomUUID().toString());
							flow.setDriverId(nearbyDriverListModel.getUserId());
							flow.setOrderId(order.getOrderId());
							flow.setFlowStatus("0");
							flowDao.addFlow(flow);
							count ++;
						}
						
					}
					
					PushDao pushDao = new PushDaoImpl();
					pushDao.pushForOrder(order, pushDriverList);
				}
			}
		});
		t.start();
		
		String dateString2 = formatter.format(new Date());
		System.out.println(dateString2 + "===============结束执行=================================");
		
		
		
	}
	

}
