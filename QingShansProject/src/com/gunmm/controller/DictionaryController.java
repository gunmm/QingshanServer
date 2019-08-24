package com.gunmm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.DictionaryDao;
import com.gunmm.impl.DictionaryImpl;
import com.gunmm.model.DictionaryModel;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class DictionaryController {
	// 查询
	@RequestMapping("/getDictionaryList")
	@ResponseBody
	private JSONObject getOrderCarList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String name = object.getString("name");
		String cityName = object.getString("cityName");
		
		DictionaryDao dictionaryDao = new DictionaryImpl();
		List<DictionaryModel> dictionaryList = dictionaryDao.getDictionaryListByName(name, cityName);
		if (dictionaryList.size() == 0) {
			dictionaryList = dictionaryDao.getDictionaryListByName("车辆类型", "北京市");
		}
		return JSONUtils.responseToJsonString("1", "", "查询成功！", dictionaryList);
	}
	
	//根据订单的车辆类型和发货城市查询对应的价格model
	@RequestMapping("/getPriceDicByTypeAndCity")
	@ResponseBody
	private JSONObject getPriceDicByTypeAndCity(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String vehicleType = object.getString("vehicleType");
		String cityName = object.getString("cityName");
		
		DictionaryDao dictionaryDao = new DictionaryImpl();
		DictionaryModel dictionaryModel = dictionaryDao.getPriceDicByNameAndVehicleType(vehicleType, cityName);
		
		return JSONUtils.responseToJsonString("1", "", "查询成功！", dictionaryModel);
	}
}
