package com.gunmm.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.InvoiceDao;
import com.gunmm.dao.OrderDao;
import com.gunmm.dao.PushDao;
import com.gunmm.dao.UserDao;
import com.gunmm.dao.VehicleDao;
import com.gunmm.impl.InvoiceImpl;
import com.gunmm.impl.OrderDaoImpl;
import com.gunmm.impl.PushDaoImpl;
import com.gunmm.impl.UserDaoImpl;
import com.gunmm.impl.VehicleImpl;
import com.gunmm.model.Invoice;
import com.gunmm.model.Order;
import com.gunmm.model.User;
import com.gunmm.model.Vehicle;
import com.gunmm.responseModel.NearbyDriverListModel;
import com.gunmm.responseModel.OrderListModel;
import com.gunmm.responseModel.OrderListModelForSite;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class OrderController {
	static String robOrderId = null;
	static String cancelOrderId = null;
	static String beginAppointOrderId = null;
	static String updateStatusOrderId = null;
	static String payServiceFeeOrderId = null;

	// 添加订单
	@RequestMapping("/addOrder")
	@ResponseBody
	private JSONObject addOrder(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		JSONObject orderObject = object.getJSONObject("orderParam");
		JSONObject invoiceObject = object.getJSONObject("invoiceParam");

		Invoice invoice = null;
		String invoiceId = null;
		if (invoiceObject != null) {
			invoice = new Invoice();
			invoice = JSONObject.parseObject(invoiceObject.toJSONString(), Invoice.class);

			InvoiceDao invoiceDao = new InvoiceImpl();
			JSONObject jsonObj = invoiceDao.addInvoiceDao(invoice);
			String result_code = jsonObj.getString("result_code");

			if ("1".equals(result_code)) {
				invoiceId = jsonObj.getString("object");
			} else {
				return jsonObj;
			}
		}

		Order addOrder = JSONObject.parseObject(orderObject.toJSONString(), Order.class);
		addOrder.setDriverSiteWithdrawStatus("0");
		addOrder.setMasterSiteWithdrawStatus("0");
		addOrder.setDriverWithdrawalStatus("0");
		addOrder.setInvoiceId(invoiceId);

		OrderDao orderDao = new OrderDaoImpl();
		return orderDao.addOrder(addOrder);
	}

	// 取消订单
	@RequestMapping("/cancelOrder")
	@ResponseBody
	private JSONObject cancelOrder(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		cancelOrderId = object.getString("orderId");

		if (cancelOrderId == null) {
			return JSONUtils.responseToJsonString("0", "参数错误，取消订单失败！", "取消订单失败！", "");
		}
		OrderDao orderDao = new OrderDaoImpl();
		JSONObject jsonObj = orderDao.cancelOrderById(cancelOrderId);
		String result_code = jsonObj.getString("result_code");
		if ("1".equals(result_code)) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					PushDao pushDao = new PushDaoImpl();
					pushDao.cancelSuccessPushForUser(cancelOrderId);
				}
			});
			t.start();
		}
		return jsonObj;
	}

	// 抢单
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
		JSONObject jsonObj = orderDao.robOrder(driverId, robOrderId);
		String result_code = jsonObj.getString("result_code");
		if ("1".equals(result_code)) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					PushDao pushDao = new PushDaoImpl();
					pushDao.updateOrderSuccessPushForUser(robOrderId);
				}
			});
			t.start();
		}
		return jsonObj;

	}

	// 查询订单起点附近车辆
	@RequestMapping("/getOrderCarList")
	@ResponseBody
	private JSONObject getOrderCarList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String orderId = object.getString("orderId");

		OrderDao orderDao = new OrderDaoImpl();

		Order midOrder = orderDao.getOrderById(orderId);

		List<NearbyDriverListModel> userList = orderDao.queryDriverForOrder(midOrder);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", userList);
	}

	// 根据订单ID查寻包含部分司机信息的大订单信息
	@RequestMapping("/getBigOrderInfo")
	@ResponseBody
	private JSONObject getBigOrderInfo(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String orderId = object.getString("orderId");

		if (orderId == null) {
			return JSONUtils.responseToJsonString("0", "参数错误！", "查询失败！", "");
		}

		OrderDao orderDao = new OrderDaoImpl();
		OrderListModelForSite orderListModel = orderDao.getBigOrderInfo(orderId);
		return JSONUtils.responseToJsonString("1", "", "查询成功！", orderListModel);
	}

	// 订单列表
	@RequestMapping("/getOrderList")
	@ResponseBody
	private JSONObject getOrderList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String userId = object.getString("userId");
		String rows = object.getString("rows");

		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		if (userId == null) {
			return JSONUtils.responseToJsonString("0", "参数错误！", "查询失败！", "");
		}

		OrderDao orderDao = new OrderDaoImpl();
		List<OrderListModel> orderList = orderDao.getOrderListByUserId(userId, page, rows);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", orderList);
	}

	// PC端获取货主订单列表
	@RequestMapping("/getPcMasterOrderList")
	@ResponseBody
	private JSONObject getPcMasterOrderList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String userId = object.getString("userId");
		String rows = object.getString("rows");

		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		OrderDao orderDao = new OrderDaoImpl();
		List<OrderListModel> orderList = orderDao.getOrderListByUserId(userId, page, rows);

		Long orderCount = orderDao.getMasterOrderCount(userId);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("orderCount", orderCount);
		jsonObject.put("orderList", orderList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}

	// 司机订单列表
	@RequestMapping("/getDriverOrderList")
	@ResponseBody
	private JSONObject getDriverOrderList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String userId = object.getString("userId");
		String condition = object.getString("condition");

		String rows = object.getString("rows");

		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		if (userId == null) {
			return JSONUtils.responseToJsonString("0", "参数错误！", "查询失败！", "");
		}

		OrderDao orderDao = new OrderDaoImpl();
		List<OrderListModel> orderList = orderDao.getDriverOrderListByDriverId(userId, page, rows, condition);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", orderList);
	}
	
	// 司机可抢订单列表   找货源
		@RequestMapping("/getFindOrderList")
		@ResponseBody
		private JSONObject getFindOrderList(HttpServletRequest request) {
			JSONObject object = (JSONObject) request.getAttribute("body");
			String userId = object.getString("userId");
			VehicleDao vehicleDao = new VehicleImpl();
			Vehicle vehicle = vehicleDao.getVehicleByBindingDriverId(userId);

			String rows = object.getString("rows");
			String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

			if (userId == null) {
				return JSONUtils.responseToJsonString("0", "参数错误！", "查询失败！", "");
			}

			OrderDao orderDao = new OrderDaoImpl();
			List<OrderListModel> orderList = orderDao.getDriverFindOrderListByDriverId(vehicle, page, rows);

			return JSONUtils.responseToJsonString("1", "", "查询成功！", orderList);
		}

	// PC端获取司机订单列表
	@RequestMapping("/getPcDriverOrderList")
	@ResponseBody
	private JSONObject getPcDriverOrderList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String driverId = object.getString("driverId");
		String rows = object.getString("rows");

		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		OrderDao orderDao = new OrderDaoImpl();
		List<OrderListModel> orderList = orderDao.getDriverOrderListByDriverId(driverId, page, rows, "");

		Long orderCount = orderDao.getDriverOrderCount(driverId);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("orderCount", orderCount);
		jsonObject.put("orderList", orderList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}

	// 站点订单列表
	@RequestMapping("/getSiteOrderList")
	@ResponseBody
	private JSONObject getSiteOrderList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String siteId = object.getString("siteId");
		String rows = object.getString("rows");

		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		OrderDao orderDao = new OrderDaoImpl();
		List<OrderListModelForSite> orderList = orderDao.getSiteOrderList(siteId, page, rows);

		Long orderCount = orderDao.getSiteOrderCount(siteId);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("orderCount", orderCount);
		jsonObject.put("orderList", orderList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}

	// 司机设置预约订单开始执行
	@RequestMapping("/setAppointOrderBegin")
	@ResponseBody
	private JSONObject setAppointOrderBegin(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String driverId = object.getString("userId");
		String orderId = object.getString("orderId");

		if (driverId == null || orderId == null) {
			return JSONUtils.responseToJsonString("0", "参数错误！", "执行失败！", "");
		}

		OrderDao orderDao = new OrderDaoImpl();
		beginAppointOrderId = orderId;
		JSONObject jsonObj = orderDao.setAppointOrderBegin(driverId, orderId);
		String result_code = jsonObj.getString("result_code");
		if ("1".equals(result_code)) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					PushDao pushDao = new PushDaoImpl();
					pushDao.beginAppointOrderPushForUser(beginAppointOrderId);
				}
			});
			t.start();
		}
		return jsonObj;
	}

	// 司机更新订单完成状态
	@RequestMapping("/updateOrderStatus")
	@ResponseBody
	private JSONObject updateOrderStatus(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String orderId = object.getString("orderId");
		String status = object.getString("status");

		if (orderId == null) {
			return JSONUtils.responseToJsonString("0", "参数错误！", "操作失败！", "");
		}

		OrderDao orderDao = new OrderDaoImpl();

		Order order = orderDao.getOrderById(orderId);
		if (order == null) {
			return JSONUtils.responseToJsonString("0", "未找到对应订单！", "操作失败！", "");
		}
		order.setStatus(status);
		if ("4".equals(status)) {
			UserDao userDao = new UserDaoImpl();
			User driver = userDao.getUserById(order.getDriverId());
			if ("1".equals(driver.getStatus())) {
				driver.setStatus("0");
			}

			userDao.updateUserInfo(driver);
			order.setFinishTime(new Date());
		}

		updateStatusOrderId = orderId;
		JSONObject jsonObj = orderDao.updateOrderInfo(order);
		String result_code = jsonObj.getString("result_code");
		String reason = jsonObj.getString("reason");

		if ("1".equals(result_code)) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					PushDao pushDao = new PushDaoImpl();
					pushDao.updateOrderSuccessPushForUser(updateStatusOrderId);
				}
			});
			t.start();
			return JSONUtils.responseToJsonString("1", "", "操作成功！", "");
		} else {
			return JSONUtils.responseToJsonString("0", reason, "操作失败！", "");
		}
	}

	// 货主评价订单
	@RequestMapping("/commentOrder")
	@ResponseBody
	private JSONObject commentOrder(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String orderId = object.getString("orderId");
		String commentContent = object.getString("commentContent");
		Double commentStar = object.getDouble("commentStar");

		if (orderId == null) {
			return JSONUtils.responseToJsonString("0", "参数错误！", "评价失败！", "");
		}

		OrderDao orderDao = new OrderDaoImpl();

		Order order = orderDao.getOrderById(orderId);
		if (order == null) {
			return JSONUtils.responseToJsonString("0", "未找到对应订单！", "评价失败！", "");
		}
		order.setCommentContent(commentContent);
		order.setCommentStar(commentStar);
		JSONObject jsonObj = orderDao.updateOrderInfo(order);

		String result_code = jsonObj.getString("result_code");
		String reason = jsonObj.getString("reason");

		UserDao userDao = new UserDaoImpl();
		User driver = userDao.getUserById(order.getDriverId());
		Double score = driver.getScore() - 0.5 + commentStar / 10;
		driver.setScore(score);
		userDao.updateUserInfo(driver);

		if ("1".equals(result_code)) {
			return JSONUtils.responseToJsonString("1", "", "评价成功！", "");
		} else {
			return JSONUtils.responseToJsonString("0", reason, "评价失败！", "");
		}

	}

	// 司机评价订单
	@RequestMapping("/driverCommentOrder")
	@ResponseBody
	private JSONObject driverCommentOrder(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String orderId = object.getString("orderId");
		String driverCommentContent = object.getString("driverCommentContent");
		Double driverCommentStar = object.getDouble("driverCommentStar");

		if (orderId == null) {
			return JSONUtils.responseToJsonString("0", "参数错误！", "评价失败！", "");
		}

		OrderDao orderDao = new OrderDaoImpl();

		Order order = orderDao.getOrderById(orderId);
		if (order == null) {
			return JSONUtils.responseToJsonString("0", "未找到对应订单！", "评价失败！", "");
		}
		order.setDriverCommentContent(driverCommentContent);
		order.setDriverCommentStar(driverCommentStar);

		JSONObject jsonObj = orderDao.updateOrderInfo(order);

		String result_code = jsonObj.getString("result_code");
		String reason = jsonObj.getString("reason");

		UserDao userDao = new UserDaoImpl();
		User master = userDao.getUserById(order.getCreateManId());
		Double score = master.getScore() - 0.5 + driverCommentStar / 10;
		master.setScore(score);
		userDao.updateUserInfo(master);

		if ("1".equals(result_code)) {
			return JSONUtils.responseToJsonString("1", "", "评价成功！", "");
		} else {
			return JSONUtils.responseToJsonString("0", reason, "评价失败！", "");
		}

	}

	// 司机支付订单服务费
	@RequestMapping("/driverPayOrderServiceFee")
	@ResponseBody
	private JSONObject driverPayOrderServiceFee(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String orderId = object.getString("orderId");
		String serviceFeePayType = object.getString("serviceFeePayType");
		String serviceFeePayId = object.getString("serviceFeePayId");
		payServiceFeeOrderId = orderId;
		OrderDao orderDao = new OrderDaoImpl();
		Order order = orderDao.getOrderById(orderId);
		order.setServiceFeePayId(serviceFeePayId);
		order.setServiceFeePayType(serviceFeePayType);
		order.setServiceFeePayStatus("1");
		order.setStatus("2");
		JSONObject jsonObj = orderDao.updateOrderInfo(order);

		String result_code = jsonObj.getString("result_code");
		String reason = jsonObj.getString("reason");

		if ("1".equals(result_code)) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					PushDao pushDao = new PushDaoImpl();
					pushDao.updateOrderSuccessPushForUser(payServiceFeeOrderId);
				}
			});
			t.start();
			return JSONUtils.responseToJsonString("1", "", "操作成功！", "");
		} else {
			return JSONUtils.responseToJsonString("0", reason, "操作失败！", "");
		}

	}

	// 司机放弃抢到的订单
	@RequestMapping("/driverGiveUpOrder")
	@ResponseBody
	private JSONObject driverGiveUpOrder(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String orderId = object.getString("orderId");
		String driverId = object.getString("driverId");

		OrderDao orderDao = new OrderDaoImpl();
		Order order = orderDao.getOrderById(orderId);
		order.setStatus("0");
		order.setTimeOut(new Date(0));
		order.setDriverId(null);
		JSONObject jsonObj = orderDao.updateOrderInfo(order);

		String result_code = jsonObj.getString("result_code");
		String reason = jsonObj.getString("reason");

		UserDao userDao = new UserDaoImpl();
		User user = userDao.getUserById(driverId);
		user.setStatus("0");
		user.setScore(user.getScore() - 0.1);
		userDao.updateUserInfo(user);

		if ("1".equals(result_code)) {
			return JSONUtils.responseToJsonString("1", "", "操作成功！", "");
		} else {
			return JSONUtils.responseToJsonString("0", reason, "操作失败！", "");
		}

	}

	// 订单协议接口
	@RequestMapping("/orderAgreement")
	@ResponseBody
	private JSONObject orderAgreement(HttpServletRequest request) {
		String masterAgreement = "1.一切移动客户端用户在下载并浏览APP手机APP软件时均被视为已经仔细阅读本条款并完全同意。凡以任何方式登陆本APP，或直接、间接使用本APP资料者，均被视为自愿接受本网站相关声明和用户服务协议的约束\n" + 
				"2.APP手机APP转载的内容并不代表APP手机APP之意见及观点，也不意味着本网赞同其观点或证实其内容的真实性\n" + 
				"3.APP手机APP转载的文字、图片、音视频等资料均由本APP用户提供，其真实性、准确性和合法性由信息发布人负责。APP手机APP不提供任何保证，并不承担任何法律责任。\n" + 
				"4、APP手机APP所转载的文字、图片、音视频等资料，如果侵犯了第三方的知识产权或其他权利，责任由作者或转载者本人承担，本APP对此不承担责任。\n" + 
				"5、APP手机APP不保证为向用户提供便利而设置的外部链接的准确性和完整性，同时，对于该外部链接指向的不由APP手机APP实际控制的任何网页上的内容，APP手机APP不承担任何责任。\n" + 
				"6、用户明确并同意其使用APP手机APP网络服务所存在的风险将完全由其本人承担；因其使用APP手机APP网络服务而产生的一切后果也由其本人承担，APP手机APP对此不承担任何责任。\n" + 
				"7、除APP手机APP注明之服务条款外，其它因不当使用本APP而导致的任何意外、疏忽、合约毁坏、诽谤、版权或其他知识产权侵犯及其所造成的任何损失，APP手机APP概不负责，亦不承担任何法律责任。\n" + 
				"8、对于因不可抗力或因黑客攻击、通讯线路中断等APP手机APP不能控制的原因造成的网络服务中断或其他缺陷，导致用户不能正常使用APP手机APP，APP手机APP不承担任何责任，但将尽力减少因此给用户造成的损失或影响。\n" + 
				"9、本声明未涉及的问题请参见国家有关法律法规，当本声明与国家有关法律法规冲突时，以国家法律法规为准。\n" + 
				"10、本网站相关声明版权及其修改权、更新权和最终解释权均属APP手机APP所有。\n";
		
		String driverAgreement = "1.一切移动客户端用户在下载并浏览APP手机APP软件时均被视为已经仔细阅读本条款并完全同意。凡以任何方式登陆本APP，或直接、间接使用本APP资料者，均被视为自愿接受本网站相关声明和用户服务协议的约束\n" + 
				"2.APP手机APP转载的内容并不代表APP手机APP之意见及观点，也不意味着本网赞同其观点或证实其内容的真实性\n" + 
				"3.APP手机APP转载的文字、图片、音视频等资料均由本APP用户提供，其真实性、准确性和合法性由信息发布人负责。APP手机APP不提供任何保证，并不承担任何法律责任。\n" + 
				"4、APP手机APP所转载的文字、图片、音视频等资料，如果侵犯了第三方的知识产权或其他权利，责任由作者或转载者本人承担，本APP对此不承担责任。\n" + 
				"5、APP手机APP不保证为向用户提供便利而设置的外部链接的准确性和完整性，同时，对于该外部链接指向的不由APP手机APP实际控制的任何网页上的内容，APP手机APP不承担任何责任。\n" + 
				"6、用户明确并同意其使用APP手机APP网络服务所存在的风险将完全由其本人承担；因其使用APP手机APP网络服务而产生的一切后果也由其本人承担，APP手机APP对此不承担任何责任。\n" + 
				"7、除APP手机APP注明之服务条款外，其它因不当使用本APP而导致的任何意外、疏忽、合约毁坏、诽谤、版权或其他知识产权侵犯及其所造成的任何损失，APP手机APP概不负责，亦不承担任何法律责任。\n" + 
				"8、对于因不可抗力或因黑客攻击、通讯线路中断等APP手机APP不能控制的原因造成的网络服务中断或其他缺陷，导致用户不能正常使用APP手机APP，APP手机APP不承担任何责任，但将尽力减少因此给用户造成的损失或影响。\n" + 
				"9、本声明未涉及的问题请参见国家有关法律法规，当本声明与国家有关法律法规冲突时，以国家法律法规为准。\n" + 
				"10、本网站相关声明版权及其修改权、更新权和最终解释权均属APP手机APP所有。\n";
		
		String servicePhone = "0913-2580119";
		
		Map<String,String> agreementMap = new HashMap<String,String>();
		agreementMap.put("masterAgreement", masterAgreement);
		agreementMap.put("driverAgreement", driverAgreement);
		agreementMap.put("servicePhone", servicePhone);

		
		return JSONUtils.responseToJsonString("1", "", "", agreementMap);
	}

}
