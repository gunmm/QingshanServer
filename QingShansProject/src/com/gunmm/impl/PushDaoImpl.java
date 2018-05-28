package com.gunmm.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gunmm.dao.OrderDao;
import com.gunmm.dao.PushDao;
import com.gunmm.dao.UserDao;
import com.gunmm.model.Order;
import com.gunmm.model.User;
import com.gunmm.utils.JPushUtils;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;

public class PushDaoImpl implements PushDao {

	@Override
	public void pushForOrder(Order order) {
		// TODO Auto-generated method stub
		//推送中自定义信息
		Map<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("type", order.getType());
		hashmap.put("linkMan", order.getLinkMan());
		hashmap.put("carType", order.getCarType());
		hashmap.put("carTypeName", order.getCarTypeName());
		hashmap.put("note", order.getNote());
		hashmap.put("sendAddress", order.getSendAddress());
		hashmap.put("receiveAddress", order.getReceiveAddress());
		hashmap.put("orderId", order.getOrderId());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(order.getCreateTime());
		hashmap.put("createTime", dateString);
		if (order.getAppointTime() != null) {
			String appointTime = formatter.format(order.getAppointTime());
			hashmap.put("appointTime", appointTime);
		}
		



		//司机列表
		List<String> person = new ArrayList<>();
		OrderDao orderDao = new OrderDaoImpl();
		List<User> userList = orderDao.queryDriverForOrder(order);
		for (User user : userList) {
			person.add(user.getPhoneNumber());
		}
	
		try {
	        PushResult result = JPushUtils.getJPushClient().sendPush(
					JPushUtils.buildPushPayLoad(person, "newOrderNotify", "有新的订单", "iOS", hashmap));
	        System.out.println("Got result - " + result);

	    } catch (APIConnectionException e) {
	        // Connection error, should retry later
	        System.out.println("错误:"+e);
	    } catch (APIRequestException e) {
	        // Should review the error, and fix the request
	        System.out.println("错误  状态："+e.getStatus()+ "    错误码："+e.getErrorCode()+"   错误信息："+e.getErrorMessage());
	    }
	}

	@Override
	public void RobSuccessPushForUser(String orderId) {
		// TODO Auto-generated method stub
		OrderDao orderDao = new OrderDaoImpl();
		Order order = orderDao.getOrderById(orderId);
		
		if(order != null && order.getLinkPhone() != null) {
			List<String> person = new ArrayList<>();
			person.add(order.getLinkPhone());
			
			UserDao userDao = new UserDaoImpl();
			User user = userDao.getUserById(order.getDriverId());
			
			Map<String, String> hashmap = new HashMap<String, String>();
			hashmap.put("status", order.getStatus());
			hashmap.put("type", order.getType());
			hashmap.put("orderId", order.getOrderId());
			hashmap.put("sendAddress", order.getSendAddress());
			hashmap.put("receiveAddress", order.getReceiveAddress());
			hashmap.put("driverName", user.getNickname());
			hashmap.put("driverPhone", user.getPhoneNumber());
			hashmap.put("plateNumber", user.getPlateNumber());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createTime = formatter.format(order.getCreateTime());
			hashmap.put("createTime", createTime);
			
			if (order.getAppointTime() != null) {
				String appointTime = formatter.format(order.getAppointTime());
				hashmap.put("appointTime", appointTime);
			}

			try {
		        PushResult result = JPushUtils.getJPushClient().sendPush(
						JPushUtils.buildPushPayLoad(person, "OrderBeReceivedNotify", "订单状态更新", "iOS", hashmap));
		        System.out.println("Got result - " + result);

		    } catch (APIConnectionException e) {
		        // Connection error, should retry later
		        System.out.println("错误:"+e);
		    } catch (APIRequestException e) {
		        // Should review the error, and fix the request
		        System.out.println("错误  状态："+e.getStatus()+ "    错误码："+e.getErrorCode()+"   错误信息："+e.getErrorMessage());
		    }
		}
	}

	@Override
	public void cancelSuccessPushForUser(String orderId) {
		// TODO Auto-generated method stub
		OrderDao orderDao = new OrderDaoImpl();
		Order order = orderDao.getOrderById(orderId);
		
		if(order != null && order.getDriverId() != null) {
			List<String> person = new ArrayList<>();
			UserDao userDao = new UserDaoImpl();
			User user = userDao.getUserById(order.getDriverId());
			person.add(user.getPhoneNumber());

			Map<String, String> hashmap = new HashMap<String, String>();
			hashmap.put("status", order.getStatus());
			hashmap.put("orderId", order.getOrderId());
			hashmap.put("type", order.getType());
			hashmap.put("sendAddress", order.getSendAddress());
			hashmap.put("receiveAddress", order.getReceiveAddress());
			hashmap.put("linkMan", order.getLinkPhone());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(order.getCreateTime());
			hashmap.put("createTime", dateString);
			if (order.getAppointTime() != null) {
				String appointTime = formatter.format(order.getAppointTime());
				hashmap.put("appointTime", appointTime);
			}
			
			try {
		        PushResult result = JPushUtils.getJPushClient().sendPush(
						JPushUtils.buildPushPayLoad(person, "OrderBeCanceledNotify", "订单被取消", "iOS", hashmap));
		        System.out.println("Got result - " + result);

		    } catch (APIConnectionException e) {
		        // Connection error, should retry later
		        System.out.println("错误:"+e);
		    } catch (APIRequestException e) {
		        // Should review the error, and fix the request
		        System.out.println("错误  状态："+e.getStatus()+ "    错误码："+e.getErrorCode()+"   错误信息："+e.getErrorMessage());
		    }
		}
	}

}
