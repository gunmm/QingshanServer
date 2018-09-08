package com.gunmm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.ComplainDao;
import com.gunmm.dao.OrderDao;
import com.gunmm.dao.UserDao;
import com.gunmm.impl.ComplainImpl;
import com.gunmm.impl.OrderDaoImpl;
import com.gunmm.impl.UserDaoImpl;
import com.gunmm.model.Complain;
import com.gunmm.model.Order;
import com.gunmm.model.User;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class ComplainController {

	// 添加投诉
	@RequestMapping("/addComplain")
	@ResponseBody
	private JSONObject addComplain(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String createManId = object.getString("createManId");
		String orderId = object.getString("orderId");
		String type = object.getString("type");
		String note = object.getString("note");

		Complain complain = new Complain();
		complain.setCreateManId(createManId);
		complain.setOrderId(orderId);
		complain.setType(type);
		complain.setNote(note);

		ComplainDao complainDao = new ComplainImpl();

		JSONObject jsonObj = complainDao.addComplainComplain(complain);
		String result_code = jsonObj.getString("result_code");

		if (!"1".equals(result_code)) {
			return jsonObj;
		}
		String complainId = jsonObj.getString("object");
		OrderDao orderDao = new OrderDaoImpl();
		Order order = orderDao.getOrderById(orderId);
		if ("1".equals(type)) {
			order.setSiteComplaintId(complainId);
		} else {
			order.setDriverComplaintId(complainId);
			order.setStatus("8");
			UserDao userDao = new UserDaoImpl();
			User driver = userDao.getUserById(order.getDriverId());
			driver.setStatus("0");
			userDao.updateUserInfo(driver);
		}

		JSONObject jsonObj2 = orderDao.updateOrderInfo(order);
		String result_code2 = jsonObj2.getString("result_code");
		if (!"1".equals(result_code2)) {
			return jsonObj2;
		} else {
			return JSONUtils.responseToJsonString("1", "", "添加成功！", complain.getRecordId());
		}
	}

	// 编辑投诉
	@RequestMapping("/editComplain")
	@ResponseBody
	private JSONObject editComplain(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String complainId = object.getString("complainId");
		String manageMan = object.getString("manageMan");
		String manageStatus = object.getString("manageStatus");
		String manageDetail = object.getString("manageDetail");

		ComplainDao complainDao = new ComplainImpl();
		Complain complain = complainDao.getComplainById(complainId);
		complain.setManageMan(manageMan);
		complain.setManageStatus(manageStatus);
		complain.setManageDetail(manageDetail);
		return complainDao.updateComplainInfo(complain);
	}

	// 根据投诉id查询相关详情信息
	@RequestMapping("/getComplainDetailById")
	@ResponseBody
	private JSONObject getComplainDetailById(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String complainId = object.getString("complainId");
		String type = object.getString("type");
		ComplainDao complainDao = new ComplainImpl();
		return complainDao.getComplainDetailById(complainId, type);
	}

}
