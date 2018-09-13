package com.gunmm.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.gunmm.dao.MessageDao;
import com.gunmm.dao.OrderDao;
import com.gunmm.dao.PushDao;
import com.gunmm.dao.UserDao;
import com.gunmm.dao.VehicleDao;
import com.gunmm.model.MessageModel;
import com.gunmm.model.Order;
import com.gunmm.model.User;
import com.gunmm.model.Vehicle;
import com.gunmm.responseModel.NearbyDriverListModel;
import com.gunmm.utils.JPushUtils;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;

public class PushDaoImpl implements PushDao {

	@Override
	public void pushForOrder(Order order, List<NearbyDriverListModel> pushDriverList) {
		// TODO Auto-generated method stub
		// 推送中自定义信息
		Map<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("type", order.getType());
		hashmap.put("orderId", order.getOrderId());
		hashmap.put("note", order.getNote());
		hashmap.put("sendDetailAddress", order.getSendDetailAddress());
		hashmap.put("receiveDetailAddress", order.getReceiveDetailAddress());
		hashmap.put("price", order.getPrice().toString());
		hashmap.put("distance", order.getDistance().toString());

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(order.getCreateTime());
		hashmap.put("createTime", dateString);
		if (order.getAppointTime() != null) {
			String appointTime = formatter.format(order.getAppointTime());
			hashmap.put("appointTime", appointTime);
		}

		for (NearbyDriverListModel nearbyDriverListModel : pushDriverList) {
			MessageModel messageModel = new MessageModel();
			messageModel.setMessageId(UUID.randomUUID().toString());
			messageModel.setMessageType("newOrderNotify");
			messageModel.setReceiveUserId(nearbyDriverListModel.getUserId());
			messageModel.setAlias(nearbyDriverListModel.getAccessToken());
			messageModel.setOrderId(order.getOrderId());
			messageModel.setOrderStatus(order.getStatus());
			messageModel.setOrderType(order.getType());
			messageModel.setOrderAppointStatus(order.getAppointStatus());

			MessageDao messageDao = new MessageImpl();
			messageDao.addMessage(messageModel);

			hashmap.put("messageId", messageModel.getMessageId());
			hashmap.put("toSendDistance", nearbyDriverListModel.getDistance().toString());

			if ("iOS".equals(nearbyDriverListModel.getLoginPlate())) {
				List<String> person = new ArrayList<>();
				person.add(nearbyDriverListModel.getAccessToken());
				try {
					PushResult resultIOS = JPushUtils.getJPushClient()
							.sendPush(JPushUtils.buildPushPayLoad(person, "newOrderNotify", "有新的订单", "iOS", hashmap));
					System.out.println("Got result iOS- " + resultIOS);

				} catch (APIConnectionException e) {
					// Connection error, should retry later
					System.out.println("错误:" + e);
				} catch (APIRequestException e) {
					// Should review the error, and fix the request
					System.out.println("错误  状态：" + e.getStatus() + "    错误码：" + e.getErrorCode() + "   错误信息："
							+ e.getErrorMessage());
				}
			} else {
				List<String> person = new ArrayList<>();
				person.add(nearbyDriverListModel.getAccessToken());
				try {
					PushResult resultAndroid = JPushUtils.getJPushClient().sendPush(
							JPushUtils.buildPushPayLoad(person, "newOrderNotify", "有新的订单", "android", hashmap));
					System.out.println("Got result android- " + resultAndroid);

				} catch (APIConnectionException e) {
					// Connection error, should retry later
					System.out.println("错误:" + e);
				} catch (APIRequestException e) {
					// Should review the error, and fix the request
					System.out.println("错误  状态：" + e.getStatus() + "    错误码：" + e.getErrorCode() + "   错误信息："
							+ e.getErrorMessage());
				}
			}
		}

	}

	@Override
	public void updateOrderSuccessPushForUser(String orderId) {
		// TODO Auto-generated method stub
		OrderDao orderDao = new OrderDaoImpl();
		Order order = orderDao.getOrderById(orderId);

		if (order != null) {
			UserDao userDao = new UserDaoImpl();
			User goodsMan = userDao.getUserById(order.getCreateManId());
			User driver = userDao.getUserById(order.getDriverId());
			VehicleDao vehicleDao = new VehicleImpl();
			Vehicle vehicle = vehicleDao.getVehicleById(driver.getVehicleId());
			List<String> person = new ArrayList<>();
			person.add(goodsMan.getAccessToken());
			String plateStr = "";
			if ("iOS".equals(goodsMan.getLoginPlate())) {
				plateStr = "iOS";
			} else {
				plateStr = "android";

			}

			MessageModel messageModel = new MessageModel();
			messageModel.setMessageId(UUID.randomUUID().toString());
			messageModel.setMessageType("OrderBeReceivedNotify");
			messageModel.setReceiveUserId(order.getCreateManId());
			messageModel.setAlias(goodsMan.getAccessToken());
			messageModel.setOrderId(order.getOrderId());
			messageModel.setOrderStatus(order.getStatus());
			messageModel.setOrderType(order.getType());
			messageModel.setOrderAppointStatus(order.getAppointStatus());
			MessageDao messageDao = new MessageImpl();
			messageDao.addMessage(messageModel);

			Map<String, String> hashmap = new HashMap<String, String>();
			hashmap.put("status", order.getStatus());
			hashmap.put("appointStatus", order.getAppointStatus());
			hashmap.put("type", order.getType());
			hashmap.put("orderId", order.getOrderId());
			hashmap.put("sendAddress", order.getSendDetailAddress());
			hashmap.put("receiveAddress", order.getReceiveDetailAddress());
			hashmap.put("driverName", driver.getNickname());
			hashmap.put("driverPhone", driver.getPhoneNumber());
			hashmap.put("plateNumber", vehicle.getPlateNumber());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createTime = formatter.format(order.getCreateTime());
			hashmap.put("createTime", createTime);

			if (order.getAppointTime() != null) {
				String appointTime = formatter.format(order.getAppointTime());
				hashmap.put("appointTime", appointTime);
			}
			hashmap.put("messageId", messageModel.getMessageId());

			try {
				PushResult result = JPushUtils.getJPushClient().sendPush(
						JPushUtils.buildPushPayLoad(person, "OrderBeReceivedNotify", "订单状态更新", plateStr, hashmap));
				System.out.println("Got result - " + result);

			} catch (APIConnectionException e) {
				// Connection error, should retry later
				System.out.println("错误:" + e);
			} catch (APIRequestException e) {
				// Should review the error, and fix the request
				System.out.println(
						"错误  状态：" + e.getStatus() + "    错误码：" + e.getErrorCode() + "   错误信息：" + e.getErrorMessage());
			}
		}
	}

	@Override
	public void cancelSuccessPushForUser(String orderId) {
		// TODO Auto-generated method stub
		OrderDao orderDao = new OrderDaoImpl();
		Order order = orderDao.getOrderById(orderId);

		if (order != null && order.getDriverId() != null) {
			List<String> person = new ArrayList<>();
			UserDao userDao = new UserDaoImpl();
			User driver = userDao.getUserById(order.getDriverId());
			person.add(driver.getAccessToken());

			String plateStr = "";
			if ("iOS".equals(driver.getLoginPlate())) {
				plateStr = "iOS";
			} else {
				plateStr = "android";

			}

			MessageModel messageModel = new MessageModel();
			messageModel.setMessageId(UUID.randomUUID().toString());
			messageModel.setMessageType("OrderBeCanceledNotify");
			messageModel.setReceiveUserId(order.getDriverId());
			messageModel.setAlias(driver.getAccessToken());
			messageModel.setOrderId(order.getOrderId());
			messageModel.setOrderStatus(order.getStatus());
			messageModel.setOrderType(order.getType());
			messageModel.setOrderAppointStatus(order.getAppointStatus());

			MessageDao messageDao = new MessageImpl();
			messageDao.addMessage(messageModel);

			Map<String, String> hashmap = new HashMap<String, String>();
			hashmap.put("status", order.getStatus());
			hashmap.put("orderId", order.getOrderId());
			hashmap.put("type", order.getType());
			hashmap.put("sendAddress", order.getSendAddress());
			hashmap.put("receiveAddress", order.getReceiveAddress());
			hashmap.put("linkMan", order.getLinkMan());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(order.getCreateTime());
			hashmap.put("createTime", dateString);
			if (order.getAppointTime() != null) {
				String appointTime = formatter.format(order.getAppointTime());
				hashmap.put("appointTime", appointTime);
			}
			hashmap.put("messageId", messageModel.getMessageId());

			try {
				PushResult result = JPushUtils.getJPushClient().sendPush(
						JPushUtils.buildPushPayLoad(person, "OrderBeCanceledNotify", "订单被取消", plateStr, hashmap));
				System.out.println("Got result - " + result);

			} catch (APIConnectionException e) {
				// Connection error, should retry later
				System.out.println("错误:" + e);
			} catch (APIRequestException e) {
				// Should review the error, and fix the request
				System.out.println(
						"错误  状态：" + e.getStatus() + "    错误码：" + e.getErrorCode() + "   错误信息：" + e.getErrorMessage());
			}
		}
	}

	// 司机开始执行预约订单时推给下单人
	@Override
	public void beginAppointOrderPushForUser(String orderId) {
		// TODO Auto-generated method stub
		OrderDao orderDao = new OrderDaoImpl();
		Order order = orderDao.getOrderById(orderId);

		if (order != null) {
			UserDao userDao = new UserDaoImpl();
			User driver = userDao.getUserById(order.getDriverId());
			User goodsMan = userDao.getUserById(order.getCreateManId());
			VehicleDao vehicleDao = new VehicleImpl();
			Vehicle vehicle = vehicleDao.getVehicleById(driver.getVehicleId());

			List<String> person = new ArrayList<>();
			person.add(goodsMan.getAccessToken());

			String plateStr = "";
			if ("iOS".equals(goodsMan.getLoginPlate())) {
				plateStr = "iOS";
			} else {
				plateStr = "android";
			}

			MessageModel messageModel = new MessageModel();
			messageModel.setMessageId(UUID.randomUUID().toString());
			messageModel.setMessageType("AppointOrderBeginNotify");
			messageModel.setReceiveUserId(order.getCreateManId());
			messageModel.setAlias(goodsMan.getAccessToken());
			messageModel.setOrderId(order.getOrderId());
			messageModel.setOrderStatus(order.getStatus());
			messageModel.setOrderType(order.getType());
			messageModel.setOrderAppointStatus(order.getAppointStatus());

			MessageDao messageDao = new MessageImpl();
			messageDao.addMessage(messageModel);

			Map<String, String> hashmap = new HashMap<String, String>();
			hashmap.put("type", order.getType());
			hashmap.put("orderId", order.getOrderId());
			hashmap.put("sendAddress", order.getSendDetailAddress());
			hashmap.put("receiveAddress", order.getReceiveDetailAddress());
			hashmap.put("driverName", driver.getNickname());
			hashmap.put("driverPhone", driver.getPhoneNumber());
			hashmap.put("plateNumber", vehicle.getPlateNumber());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createTime = formatter.format(order.getCreateTime());
			hashmap.put("createTime", createTime);

			if (order.getAppointTime() != null) {
				String appointTime = formatter.format(order.getAppointTime());
				hashmap.put("appointTime", appointTime);
			}
			hashmap.put("messageId", messageModel.getMessageId());

			try {
				PushResult result = JPushUtils.getJPushClient().sendPush(
						JPushUtils.buildPushPayLoad(person, "AppointOrderBeginNotify", "预约订单开始执行", plateStr, hashmap));
				System.out.println("Got result - " + result);

			} catch (APIConnectionException e) {
				// Connection error, should retry later
				System.out.println("错误:" + e);
			} catch (APIRequestException e) {
				// Should review the error, and fix the request
				System.out.println(
						"错误  状态：" + e.getStatus() + "    错误码：" + e.getErrorCode() + "   错误信息：" + e.getErrorMessage());
			}
		}
	}

	// 投诉处理后推送给投诉人
	public void complainHasBeManage(String createManId, String complainId, String complainType) {
		UserDao userDao = new UserDaoImpl();
		User user = userDao.getUserById(createManId);

		List<String> person = new ArrayList<>();
		person.add(user.getAccessToken());

		String plateStr = "";
		if ("iOS".equals(user.getLoginPlate())) {
			plateStr = "iOS";
		} else {
			plateStr = "android";
		}

		MessageModel messageModel = new MessageModel();
		messageModel.setMessageId(UUID.randomUUID().toString());
		messageModel.setMessageType("ComplainHasBeManage");
		messageModel.setReceiveUserId(createManId);
		messageModel.setAlias(user.getAccessToken());
		
		MessageDao messageDao = new MessageImpl();
		messageDao.addMessage(messageModel);

		Map<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("complainId", complainId);
		hashmap.put("complainType", complainType);
		hashmap.put("messageId", messageModel.getMessageId());

		try {
			PushResult result = JPushUtils.getJPushClient().sendPush(
					JPushUtils.buildPushPayLoad(person, "ComplainHasBeManage", "投诉被处理", plateStr, hashmap));
			System.out.println("Got result - " + result);

		} catch (APIConnectionException e) {
			// Connection error, should retry later
			System.out.println("错误:" + e);
		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			System.out.println(
					"错误  状态：" + e.getStatus() + "    错误码：" + e.getErrorCode() + "   错误信息：" + e.getErrorMessage());
		}

			
	}

}
