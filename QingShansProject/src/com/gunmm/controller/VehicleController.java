package com.gunmm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.VehicleDao;
import com.gunmm.impl.VehicleImpl;
import com.gunmm.responseModel.VehicleListModel;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class VehicleController {
	
	@RequestMapping("/getVehicleList")
	@ResponseBody
	private JSONObject getVehicleList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");

		String siteId = object.getString("siteId");
		String phoneNumber = object.getString("phoneNumber");
		String plateNumber = object.getString("plateNumber");

		
		VehicleDao vehicleDao = new VehicleImpl();
		List<VehicleListModel> vehicleList = vehicleDao.getVehicleListBySiteId(siteId, phoneNumber, plateNumber);

		
		return JSONUtils.responseToJsonString("1", "", "查询成功！", vehicleList);
	}


}
