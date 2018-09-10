package com.gunmm.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.DriverWithdrawalDao;
import com.gunmm.dao.OrderDao;
import com.gunmm.impl.DriverWithdrawalImpl;
import com.gunmm.impl.OrderDaoImpl;
import com.gunmm.model.DriverWithdrawal;
import com.gunmm.responseModel.DriverWithdrawalListModel;
import com.gunmm.responseModel.OrderListModel;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class DriverwithdrawalController {

	// 添加司机提现记录
	@RequestMapping("/addDriverWithdrawal")
	@ResponseBody
	private JSONObject addDriverWithdrawal(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		JSONObject driverWithdrawalObject = object.getJSONObject("driverWithdrawal");

		String driverId = object.getString("driverId");

		DriverWithdrawal driverWithdrawal = JSONObject.parseObject(driverWithdrawalObject.toJSONString(),
				DriverWithdrawal.class);
		driverWithdrawal.setDriverWithdrawalId(UUID.randomUUID().toString());
		driverWithdrawal.setDriverWithdrawalStatus("0");
		driverWithdrawal.setDriverWithdrawalTime(new Date());
		DriverWithdrawalDao driverWithdrawalDao = new DriverWithdrawalImpl();

		JSONObject jsonObj = driverWithdrawalDao.addDriverWithdrawal(driverWithdrawal);
		String driverWithdrawalId = jsonObj.getString("object");
		if (driverWithdrawalId.length() > 0) {
			OrderDao orderDao = new OrderDaoImpl();

			return orderDao.setOnlineOrderDriverWithdrawalStatus("1", driverWithdrawalId, driverId);
		} else {
			driverWithdrawalDao.deleteDriverWithdrawal(driverWithdrawal);
			return JSONUtils.responseToJsonString("0", "", "提现操作失败！", "");
		}
	}

	// 查询司机可提现列表及总提现数额
	@RequestMapping("/queryDriverWithDrawalOrderListAndAmount")
	@ResponseBody
	private JSONObject queryDriverWithDrawalOrderListAndAmount(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		String driverId = object.getString("driverId");

		OrderDao orderDao = new OrderDaoImpl();
		List<OrderListModel> orderList = orderDao.getDriverWithDrawalOrderList(driverId);

		Double orderAmount = orderDao.getDriverWithdrawalAmount(driverId);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("orderAmount", orderAmount);
		jsonObject.put("orderList", orderList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);

	}

	// 查询Pc提现列表
	@RequestMapping("/queryPCDriverWithDrawalList")
	@ResponseBody
	private JSONObject queryPCDriverWithDrawalList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String rows = object.getString("rows");
		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));
		String toUserName = object.getString("toUserName");
		String bankCardNumber = object.getString("bankCardNumber");

		DriverWithdrawalDao driverWithdrawalDao = new DriverWithdrawalImpl();
		List<DriverWithdrawalListModel> driverWithdrawalListModelList = driverWithdrawalDao
				.getDriverWithdrawalList(null, page, rows, toUserName, bankCardNumber);

		Long orderCount = driverWithdrawalDao.getDriverWithdrawalListCount(null, toUserName, bankCardNumber);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("orderCount", orderCount);
		jsonObject.put("driverWithdrawalListModelList", driverWithdrawalListModelList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}

	// 查询移动端提现列表
	@RequestMapping("/queryMobileDriverWithDrawalList")
	@ResponseBody
	private JSONObject queryMobileDriverWithDrawalList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String rows = object.getString("rows");
		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));
		String driverId = object.getString("driverId");

		DriverWithdrawalDao driverWithdrawalDao = new DriverWithdrawalImpl();
		List<DriverWithdrawalListModel> driverWithdrawalListModelList = driverWithdrawalDao
				.getDriverWithdrawalList(driverId, page, rows, "", "");

		return JSONUtils.responseToJsonString("1", "", "查询成功！", driverWithdrawalListModelList);
	}

	// pc管理员确认司机打款
	@RequestMapping("/managerConfirmDriverWithdrawal")
	@ResponseBody
	private JSONObject managerConfirmDriverWithdrawal(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String driverWithdrawalId = object.getString("driverWithdrawalId");
		String oprationUserId = object.getString("oprationUserId");

		DriverWithdrawalDao driverWithdrawalDao = new DriverWithdrawalImpl();
		DriverWithdrawal driverWithdrawal = driverWithdrawalDao.getDriverWithdrawalById(driverWithdrawalId);

		OrderDao orderDao = new OrderDaoImpl();
		JSONObject jsonObject = orderDao.setOnlineOrderDriverWithdrawalStatus("2", driverWithdrawalId,
				driverWithdrawal.getToUserId());
		String result_code = jsonObject.getString("result_code");
		if ("1".equals(result_code)) {
			driverWithdrawal.setDriverWithdrawalStatus("1");
			driverWithdrawal.setOprationUserId(oprationUserId);
			driverWithdrawalDao.editDriverWithdrawal(driverWithdrawal);
			return JSONUtils.responseToJsonString("1", "", "操作成功！", "");
		}
		return jsonObject;

	}

	// PC端获取司机提现订单列表
	@RequestMapping("/getPcDriverWithdrawalOrderList")
	@ResponseBody
	private JSONObject getPcDriverWithdrawalOrderList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String driverWithdrawalId = object.getString("driverWithdrawalId");
		String rows = object.getString("rows");
		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		OrderDao orderDao = new OrderDaoImpl();
		List<OrderListModel> orderList = orderDao.getDriverWithdrawaledOrderList(driverWithdrawalId, page, rows);

		Long orderCount = orderDao.getDriverWithdrawaledOrderListCount(driverWithdrawalId);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("orderCount", orderCount);
		jsonObject.put("orderList", orderList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}

	// 移动端获取司机提现订单列表
	@RequestMapping("/getMobileDriverWithdrawalOrderList")
	@ResponseBody
	private JSONObject getMobileDriverWithdrawalOrderList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String driverWithdrawalId = object.getString("driverWithdrawalId");

		OrderDao orderDao = new OrderDaoImpl();
		List<OrderListModel> orderList = orderDao.getMobileDriverWithdrawaledOrderList(driverWithdrawalId);


		return JSONUtils.responseToJsonString("1", "", "查询成功！", orderList);
	}

}
