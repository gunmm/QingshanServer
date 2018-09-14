package com.gunmm.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.ComplainDao;
import com.gunmm.dao.OrderDao;
import com.gunmm.dao.PushDao;
import com.gunmm.dao.UserDao;
import com.gunmm.impl.ComplainImpl;
import com.gunmm.impl.OrderDaoImpl;
import com.gunmm.impl.PushDaoImpl;
import com.gunmm.impl.UserDaoImpl;
import com.gunmm.model.Complain;
import com.gunmm.model.Order;
import com.gunmm.model.User;
import com.gunmm.responseModel.ComplainResModel;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class ComplainController {
	static Complain manageComplain = null;

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

	// 端获取投诉列表
	@RequestMapping("/getComplainList")
	@ResponseBody
	private JSONObject getComplainList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String type = object.getString("type");
		String manageStatus = object.getString("manageStatus");

		String rows = object.getString("rows");

		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		ComplainDao complainDao = new ComplainImpl();
		List<ComplainResModel> omplainResModelList = complainDao.getComplainList(type, manageStatus, page, rows);

		Long complainCount = complainDao.getComplainListCount(type, manageStatus);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("complainCount", complainCount);
		jsonObject.put("complainResModelList", omplainResModelList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}

	// 管理员处理投诉
	@RequestMapping("/manageComplain")
	@ResponseBody
	private JSONObject manageComplain(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String complainId = object.getString("complainId");
		String manageMan = object.getString("manageMan");
		String manageDetail = object.getString("manageDetail");
		String manageDriver = object.getString("manageDriver");
		String manageMaster = object.getString("manageMaster");
		String manageOrder = object.getString("manageOrder");

		ComplainDao complainDao = new ComplainImpl();
		Complain complain = complainDao.getComplainById(complainId);

		if (!"0".equals(manageDriver) || !"0".equals(manageMaster)) {
			OrderDao orderDao = new OrderDaoImpl();
			Order order = orderDao.getOrderById(complain.getOrderId());
			UserDao userDao = new UserDaoImpl();
			if (!"0".equals(manageDriver)) {
				User driver = userDao.getUserById(order.getDriverId());
				if ("1".equals(manageDriver)) {  //评分减一
					if (driver.getScore() >= 1) {
						driver.setScore(driver.getScore() - 1);
					}else {
						driver.setScore(0.0);
					}
					
				} else { //拉黑
					driver.setBlackStatus("1");
				}
				JSONObject jsonObj = userDao.updateUserInfo(driver);
				String result_code = jsonObj.getString("result_code");
				if (!"1".equals(result_code)) {
					return JSONUtils.responseToJsonString("0", "", "处理司机操作失败！", "");
				}
				

			} 
			if (!"0".equals(manageMaster)) {
				User master = userDao.getUserById(order.getCreateManId());
				if ("1".equals(manageMaster)) {//评分减一
					if (master.getScore() >= 1) {
						master.setScore(master.getScore() - 1);
					}else {
						master.setScore(0.0);
					}
				} else {//拉黑
					master.setBlackStatus("1");
				}
				JSONObject jsonObj = userDao.updateUserInfo(master);
				String result_code = jsonObj.getString("result_code");
				if (!"1".equals(result_code)) {
					return JSONUtils.responseToJsonString("0", "", "处理货主操作失败！", "");
				}
			}

		}

		if (!"0".equals(manageOrder)) {
			OrderDao orderDao = new OrderDaoImpl();
			Order order = orderDao.getOrderById(complain.getOrderId());
			if ("1".equals(manageOrder)) {//变为取消状态
				order.setStatus("9");
			} else {//变为异常状态
				order.setStatus("8");
			}
			JSONObject jsonObj = orderDao.updateOrderInfo(order);
			String result_code = jsonObj.getString("result_code");
			if (!"1".equals(result_code)) {
				return JSONUtils.responseToJsonString("0", "", "处理订单操作失败！", "");
			}
			
		}

		complain.setManageStatus("1");
		complain.setManageMan(manageMan);
		complain.setManageDetail(manageDetail);
		complain.setManageDriver(manageDriver);
		complain.setManageMaster(manageMaster);
		complain.setManageOrder(manageOrder);
		complain.setUpdateTime(new Date());

		JSONObject jsonObj = complainDao.updateComplainInfo(complain);
		
		String result_code = jsonObj.getString("result_code");
		String reason = jsonObj.getString("reason");

		manageComplain = complain;
		if ("1".equals(result_code)) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					PushDao pushDao = new PushDaoImpl();
					pushDao.complainHasBeManage(manageComplain.getCreateManId(), manageComplain.getRecordId(), manageComplain.getType());
				}
			});
			t.start();
			return JSONUtils.responseToJsonString("1", "", "处理成功！", "");
		} else {
			return JSONUtils.responseToJsonString("0", reason, "处理失败！", "");
		}
	}

}
