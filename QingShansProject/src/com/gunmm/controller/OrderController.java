package com.gunmm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.DictionaryDao;
import com.gunmm.dao.OrderDao;
import com.gunmm.dao.PushDao;
import com.gunmm.dao.UserDao;
import com.gunmm.impl.DictionaryImpl;
import com.gunmm.impl.OrderDaoImpl;
import com.gunmm.impl.PushDaoImpl;
import com.gunmm.impl.UserDaoImpl;
import com.gunmm.model.Order;
import com.gunmm.model.User;
import com.gunmm.responseModel.OrderListModel;
import com.gunmm.utils.JSONUtils;


@Controller
@RequestMapping("/mobile")
public class OrderController {
	static Order addOrder = null;
	static String robOrderId = null;
	static String cancelOrderId = null;


	//添加订单
	@RequestMapping("/addOrder")
	@ResponseBody
	private JSONObject addOrder(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		addOrder = new Order();
		addOrder = JSONObject.parseObject(object.toJSONString(), Order.class);

		if (addOrder == null) {
			return JSONUtils.responseToJsonString("0", "未接收到数据", "下单失败！", "");
		}
		OrderDao orderDao = new OrderDaoImpl();
		JSONObject jsonObj = orderDao.addOrder(addOrder);
		String result_code = jsonObj.getString("result_code");
		if ("1".equals(result_code)) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					DictionaryDao dictionaryDao = new DictionaryImpl();
					addOrder.setCarTypeName(dictionaryDao.getValueTextByNameAndkey("车辆类型", addOrder.getCarType()));
					PushDao pushDao = new PushDaoImpl();
					pushDao.pushForOrder(addOrder);
				}
			});
			t.start();
		}
		return jsonObj;
	}
	
	//取消订单
		@RequestMapping("/cancelOrder")
		@ResponseBody
		private JSONObject cancelOrder(HttpServletRequest request) {
			JSONObject object = (JSONObject) request.getAttribute("body");
			cancelOrderId = object.getString("orderId");

			if(cancelOrderId == null) {
				return JSONUtils.responseToJsonString("0", "参数错误，订单取消失败！", "取消订单失败！", "");
			}
			OrderDao orderDao = new OrderDaoImpl();
			String result = orderDao.cancelOrderById(cancelOrderId);
			if (result.equals("success")) {
				Thread t = new Thread(new Runnable() {
					public void run() {
						PushDao pushDao = new PushDaoImpl();
						pushDao.cancelSuccessPushForUser(cancelOrderId);
					}
				});
				t.start();
				return JSONUtils.responseToJsonString("1", "", "订单取消成功！", "");
			} else {
				return JSONUtils.responseToJsonString("0", result, "订单取消失败！", "");
			}

		}
	
	//抢单
	@RequestMapping("/robOrder")
	@ResponseBody
	private JSONObject robOrder(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String driverId = object.getString("driverId");
		robOrderId = object.getString("orderId");

		if (driverId == null || robOrderId == null) {
			return JSONUtils.responseToJsonString("0", "未接收到数据，抢单失败！", "抢单失败！", "");
		}
		OrderDao orderDao = new OrderDaoImpl();
		String result = orderDao.robOrder(driverId, robOrderId);
		if (result.equals("success")) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					PushDao pushDao = new PushDaoImpl();
					pushDao.RobSuccessPushForUser(robOrderId);
				}
			});
			t.start();
			return JSONUtils.responseToJsonString("1", "", "抢单成功！", "");
		} else {
			return JSONUtils.responseToJsonString("0", result, "抢单失败！", "");
		}

	}
	
	// 查询订单起点附近车辆
	@RequestMapping("/getOrderCarList")
	@ResponseBody
	private JSONObject getOrderCarList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String orderId = object.getString("orderId");

		
		OrderDao orderDao = new OrderDaoImpl();

		Order midOrder = orderDao.getOrderById(orderId);

		List<User> userList = orderDao.queryDriverForOrder(midOrder);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", userList);
	}
	
	// 根据订单ID查被接单的订单信息和司机信息
	@RequestMapping("/getOnWayOrder")
	@ResponseBody
	private JSONObject getOnWayOrder(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String orderId = object.getString("orderId");

		if(orderId == null) {
			return JSONUtils.responseToJsonString("0", "参数错误！", "查询失败！", "");
		}
		
		OrderDao orderDao = new OrderDaoImpl();
		Order wayOrder = orderDao.getOrderById(orderId);
		DictionaryDao dictionaryDao = new DictionaryImpl();

		

		UserDao userDao = new UserDaoImpl();
		User wayDriver = userDao.getUserById(wayOrder.getDriverId());
		wayOrder.setCarTypeName(dictionaryDao.getValueTextByNameAndkey("车辆类型", wayOrder.getCarType()));

		if (wayOrder != null && wayDriver != null) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("order", wayOrder);
			jsonObject.put("driver", wayDriver);
			return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
		} else {
			return JSONUtils.responseToJsonString("0", "", "查询失败！", "");
		}
	}
	
	// 订单列表
	@RequestMapping("/getOrderList")
	@ResponseBody
	private JSONObject getOrderList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String userId = object.getString("userId");
		String rows = object.getString("rows");

		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		if(userId == null) {
			return JSONUtils.responseToJsonString("0", "参数错误！", "查询失败！", "");
		}

		OrderDao orderDao = new OrderDaoImpl();
		List<OrderListModel> orderList = orderDao.getOrderListByUserId(userId, page, rows);
		
		
		return JSONUtils.responseToJsonString("1", "", "查询成功！", orderList);
	}
	
	//司机订单列表
	@RequestMapping("/getDriverOrderList")
	@ResponseBody
	private JSONObject getDriverOrderList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String userId = object.getString("userId");
		String rows = object.getString("rows");

		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		if(userId == null) {
			return JSONUtils.responseToJsonString("0", "参数错误！", "查询失败！", "");
		}

		OrderDao orderDao = new OrderDaoImpl();
		List<OrderListModel> orderList = orderDao.getOrderListByUserId(userId, page, rows);
		
		
		return JSONUtils.responseToJsonString("1", "", "查询成功！", orderList);
	}
		
		
}
