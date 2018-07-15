package com.gunmm.dao;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Vehicle;

public interface VehicleDao {
	
	// 新建站点
		public JSONObject addVehicle(Vehicle vehicle);
		
		// 更新站点
		public JSONObject updateVehicleInfo(Vehicle vehicle);

}
