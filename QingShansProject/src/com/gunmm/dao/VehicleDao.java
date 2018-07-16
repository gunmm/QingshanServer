package com.gunmm.dao;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Vehicle;

public interface VehicleDao {

	// 新建车辆
	public JSONObject addVehicle(Vehicle vehicle);
	
	// 根据id取车辆
	public Vehicle getVehicleById(String vehicleId);
	
	//删除车辆
	public JSONObject deleteVehicle(String vehicleId);

	// 更新车辆
	public JSONObject updateVehicleInfo(Vehicle vehicle);

	// 判断车牌号是否被注册
	public boolean judgeVehicleByPlateNumber(String plateNumber);

	// 判断营运证号是否被注册
	public boolean judgeVehicleByOperationCertificateNumber(String operationCertificateNumber);
	
	// 判断行驶证证号是否被注册
	public boolean judgeVehicleByDrivingCardNumber(String drivingCardNumber);
	
	// 判断车辆登记证证号是否被注册
	public boolean judgeVehicleByVehicleRegistrationNumber(String vehicleRegistrationNumber);

}
