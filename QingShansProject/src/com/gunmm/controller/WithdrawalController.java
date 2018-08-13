package com.gunmm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.WithdrawalDao;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class WithdrawalController {

	
	@RequestMapping("/withdrawalBeforeOrder")
	@ResponseBody
	private JSONObject withdrawalBeforeOrder(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		String dateString = formatter.format(new Date()) + "-09 23:59:59";
//		WithdrawalDao WithdrawalDao = new 
		
		

		
		return JSONUtils.responseToJsonString("1", "", "查询成功！", "");
	}
}
