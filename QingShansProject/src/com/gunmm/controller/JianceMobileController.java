package com.gunmm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.JianceCheckDao;
import com.gunmm.impl.JianceCheckImpl;
import com.gunmm.model.JianceCheck;
import com.gunmm.responseModel.JianceListModel;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class JianceMobileController {

	// 查询站点列表
	@RequestMapping("/getJianceList")
	@ResponseBody
	private JSONObject getBigOrderInfo(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String rows = object.getString("rows");
		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		String masterPhone = object.getString("masterPhone");
		String carPlateNumber = object.getString("carPlateNumber");
		String isDaiBan = object.getString("isDaiBan");
		String queryTime = object.getString("queryTime");

		JianceCheckDao jianceCheckDao = new JianceCheckImpl();
		List<JianceListModel> siteList = jianceCheckDao.getJianceCheckList(page, rows, masterPhone, carPlateNumber,
				isDaiBan, queryTime);

		Long siteCount = jianceCheckDao.getJianceCheckCount(masterPhone, carPlateNumber, isDaiBan, queryTime);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("JianceCheckCount", siteCount);
		jsonObject.put("JianceCheckList", siteList);
		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}

	// 完成检测
	@RequestMapping("/finishCheck")
	@ResponseBody
	private JSONObject finishCheck(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String recordId = object.getString("recordId");

		JianceCheckDao jianceCheckDao = new JianceCheckImpl();
		JianceCheck jianceCheck = jianceCheckDao.getJianceCheckById(recordId);
		jianceCheck.setStatus("1");
		return jianceCheckDao.updateJianceCheck(jianceCheck);
	}

}
