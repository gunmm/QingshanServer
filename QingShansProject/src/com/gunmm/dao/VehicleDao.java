package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Vehicle;
import com.gunmm.responseModel.VehicleListModel;

public interface VehicleDao {

	// 新建车辆
	public JSONObject addVehicle(Vehicle vehicle);

	// 根据id取车辆
	public Vehicle getVehicleById(String vehicleId);

	// 根据bindingDriverId取车辆
	public Vehicle getVehicleByBindingDriverId(String bindingDriverId);

	// 删除车辆
	public JSONObject deleteVehicle(String vehicleId);

	// 更新车辆
	public JSONObject updateVehicleInfo(Vehicle vehicle);

	// 查询地图车辆列表
	public List<VehicleListModel> getVehicleListBySiteId(String siteId, String phoneNumber, String plateNumber);

	// 判断车牌号是否被注册
	public boolean judgeVehicleByPlateNumber(String plateNumber);

	// 判断营运证号是否被注册
	public boolean judgeVehicleByOperationCertificateNumber(String operationCertificateNumber);

	// 判断行驶证证号是否被注册
	public boolean judgeVehicleByDrivingCardNumber(String drivingCardNumber);

	// 判断车辆登记证证号是否被注册
	public boolean judgeVehicleByVehicleRegistrationNumber(String vehicleRegistrationNumber);

}
