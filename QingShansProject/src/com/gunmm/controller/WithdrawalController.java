package com.gunmm.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.OrderDao;
import com.gunmm.dao.WithdrawalDao;
import com.gunmm.impl.OrderDaoImpl;
import com.gunmm.impl.WithdrawalImpl;
import com.gunmm.model.Withdrawal;
import com.gunmm.responseModel.WithdrawalListModel;
import com.gunmm.responseModel.WithdrawalOrderListModel;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class WithdrawalController {

	// 查询指定时间点的所有父站点的待提现金额列表
	@RequestMapping("/withdrawalBeforeOrder")
	@ResponseBody
	private JSONObject withdrawalBeforeOrder(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		// String dateString = formatter.format(new Date());

		String queryTime = object.getString("queryTime");
		if (queryTime == null || "".equals(queryTime)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -1);
			Date m = c.getTime();
			queryTime = format.format(m);
		}

		String filterSiteName = object.getString("filterSiteName");
		String filterLowsManName = object.getString("filterLowsManName");

		String rows = object.getString("rows");

		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));
		WithdrawalDao withdrawalDao = new WithdrawalImpl();
		List<WithdrawalListModel> withdrawalList = withdrawalDao.getSiteWithdrawalList(queryTime, page, rows,
				filterSiteName, filterLowsManName);
		Long withdrawalCount = withdrawalDao.getSiteWithdrawalListCount(queryTime, filterSiteName, filterLowsManName);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("withdrawalCount", withdrawalCount);
		jsonObject.put("withdrawalList", withdrawalList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);

	}

	// 查询指定时间点的指定父站点的所有子站点的待提现金额列表 平台视角
	@RequestMapping("/withdrawalChildWithSuperSiteId")
	@ResponseBody
	private JSONObject withdrawalChildWithSuperSiteId(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// String dateString = formatter.format(new Date()) + " 23:59:59";

		String queryTime = object.getString("queryTime");
		if (queryTime == null || "".equals(queryTime)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -1);
			Date m = c.getTime();
			queryTime = format.format(m);
		}
		String siteId = object.getString("siteId");
		String type = object.getString("type");
		String filterSiteName = object.getString("filterSiteName");
		String filterLowsManName = object.getString("filterLowsManName");

		String rows = object.getString("rows");
		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));
		WithdrawalDao withdrawalDao = new WithdrawalImpl();
		List<WithdrawalListModel> withdrawalList = withdrawalDao.getChildSiteWithdrawalList(queryTime, siteId, type,
				page, rows, filterSiteName, filterLowsManName);
		Long withdrawalCount = withdrawalDao.getChildSiteWithdrawalListCount(queryTime, siteId, filterSiteName,
				filterLowsManName);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("withdrawalCount", withdrawalCount);
		jsonObject.put("withdrawalList", withdrawalList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);

	}

	// 查询指定时间点的指定父站点的所有子站点的待提现金额列表 站点视角
	@RequestMapping("/withdrawalChildWithSuperSiteIdForSite")
	@ResponseBody
	private JSONObject withdrawalChildWithSuperSiteIdForSite(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		String queryTime = object.getString("queryTime");
		if (queryTime == null || "".equals(queryTime)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -1);
			Date m = c.getTime();
			queryTime = format.format(m);
		}
		String siteId = object.getString("siteId");
		String type = object.getString("type");
		String filterSiteName = object.getString("filterSiteName");
		String filterLowsManName = object.getString("filterLowsManName");

		String rows = object.getString("rows");
		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));
		WithdrawalDao withdrawalDao = new WithdrawalImpl();
		List<WithdrawalListModel> withdrawalList = withdrawalDao.getChildSiteWithdrawalListForSite(queryTime, siteId, type,
				page, rows, filterSiteName, filterLowsManName);
		Long withdrawalCount = withdrawalDao.getChildSiteWithdrawalListCountForSite(queryTime, siteId, filterSiteName,
				filterLowsManName);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("withdrawalCount", withdrawalCount);
		jsonObject.put("withdrawalList", withdrawalList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);

	}

	// 获取父站点司机总提成订单对应的订单记录列表
	@RequestMapping("/getTotalDriverItemOrderListBySiteId")
	@ResponseBody
	private JSONObject getTotalDriverItemOrderListBySiteId(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// String dateString = formatter.format(new Date()) + " 23:59:59";
		String queryTime = object.getString("queryTime");
		if (queryTime == null || "".equals(queryTime)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -1);
			Date m = c.getTime();
			queryTime = format.format(m);
		}

		String siteId = object.getString("siteId");
		String isChildSiteList = object.getString("isChildSiteList");

		String rows = object.getString("rows");

		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));
		WithdrawalDao withdrawalDao = new WithdrawalImpl();
		List<WithdrawalOrderListModel> withdrawalOrderList = withdrawalDao
				.getTotalDriverItemOrderListBySiteId(queryTime, siteId, page, rows, isChildSiteList);
		Long withdrawalCount = withdrawalDao.getTotalDriverItemOrderListCount(queryTime, siteId, isChildSiteList);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("withdrawalCount", withdrawalCount);
		jsonObject.put("withdrawalOrderList", withdrawalOrderList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);

	}

	// 获取父站点货主总提成订单对应的订单记录列表
	@RequestMapping("/getTotalMasterItemOrderListBySiteId")
	@ResponseBody
	private JSONObject getTotalMasterItemOrderListBySiteId(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// String dateString = formatter.format(new Date()) + " 23:59:59";
		String queryTime = object.getString("queryTime");
		if (queryTime == null || "".equals(queryTime)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -1);
			Date m = c.getTime();
			queryTime = format.format(m);
		}

		String siteId = object.getString("siteId");
		String isChildSiteList = object.getString("isChildSiteList");

		String rows = object.getString("rows");

		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));
		WithdrawalDao withdrawalDao = new WithdrawalImpl();
		List<WithdrawalOrderListModel> withdrawalOrderList = withdrawalDao
				.getTotalMasterItemOrderListBySiteId(queryTime, siteId, page, rows, isChildSiteList);
		Long withdrawalCount = withdrawalDao.getTotalMasterItemOrderListCount(queryTime, siteId, isChildSiteList);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("withdrawalCount", withdrawalCount);
		jsonObject.put("withdrawalOrderList", withdrawalOrderList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);

	}

	// 根据站点id和时间打款
	@RequestMapping("/withdrawalBySiteId")
	@ResponseBody
	private JSONObject withdrawalBySiteId(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		String siteId = object.getString("siteId");
		String withdrawalAmount = object.getString("withdrawalAmount");
		String toBankNumber = object.getString("toBankNumber");
		String toUserId = object.getString("toUserId");
		String oprationUserId = object.getString("oprationUserId");
		String periodOfTime = object.getString("periodOfTime");

		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setWithdrawalId(UUID.randomUUID().toString());
		withdrawal.setWithdrawalAmount(withdrawalAmount);
		withdrawal.setToBankNumber(toBankNumber);
		withdrawal.setToUserId(toUserId);
		withdrawal.setOprationUserId(oprationUserId);
		withdrawal.setPeriodOfTime(periodOfTime);
		withdrawal.setWithdrawalTime(new Date());

		WithdrawalDao withdrawalDao = new WithdrawalImpl();
		JSONObject jsonObj1 = withdrawalDao.addWithdrawal(withdrawal);
		String result_code1 = jsonObj1.getString("result_code");
		if ("1".equals(result_code1)) {
			// 修改所有相关订单的提现状态
			OrderDao orderDao = new OrderDaoImpl();
			JSONObject jsonObj2 = orderDao.setWithdrawal(periodOfTime, withdrawal.getWithdrawalId(), siteId);
			String result_code2 = jsonObj2.getString("result_code");
			if ("1".equals(result_code2)) {
				return JSONUtils.responseToJsonString("1", "", "操作成功！", "");
			} else {
				withdrawalDao.deleteWithdrawal(withdrawal.getWithdrawalId());
				return jsonObj2;
			}
		} else {
			return jsonObj1;
		}
	}
}
