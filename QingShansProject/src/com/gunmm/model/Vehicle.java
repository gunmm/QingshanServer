package com.gunmm.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Vehicle {

	private String vehicleId;              // 车辆id
	private String plateNumber;              // 车牌号
	private String vehicleType;              // 车辆类型
	private String drivingCardNumber;              // 行驶证号
	private String vehicleRegistrationNumber;              // 车辆登记证号
	private String operationCertificateNumber;              // 营运证号
	private String insuranceNumber;              // 登记保险证号
	private String vehicleIdCardNumber;              // 车主身份证
	private String businessLicenseNumber;              //  营业执照号
	private String vehicleBrand;              // 车辆品牌
	private String vehicleModel;              // 车辆型号
	private String vehiclePhoto;              // 车辆图片
	private String loadWeight;              // 车辆载重
	private String vehicleMakeDate;              // 车辆出厂日期
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;              // 创建时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;              // 更新时间
	
	
	public Vehicle() {

	}


	public String getVehicleId() {
		return vehicleId;
	}


	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}


	public String getPlateNumber() {
		return plateNumber;
	}


	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}


	public String getVehicleType() {
		return vehicleType;
	}


	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}


	public String getDrivingCardNumber() {
		return drivingCardNumber;
	}


	public void setDrivingCardNumber(String drivingCardNumber) {
		this.drivingCardNumber = drivingCardNumber;
	}


	public String getVehicleRegistrationNumber() {
		return vehicleRegistrationNumber;
	}


	public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
		this.vehicleRegistrationNumber = vehicleRegistrationNumber;
	}


	public String getOperationCertificateNumber() {
		return operationCertificateNumber;
	}


	public void setOperationCertificateNumber(String operationCertificateNumber) {
		this.operationCertificateNumber = operationCertificateNumber;
	}


	public String getInsuranceNumber() {
		return insuranceNumber;
	}


	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}


	public String getVehicleIdCardNumber() {
		return vehicleIdCardNumber;
	}


	public void setVehicleIdCardNumber(String vehicleIdCardNumber) {
		this.vehicleIdCardNumber = vehicleIdCardNumber;
	}


	public String getBusinessLicenseNumber() {
		return businessLicenseNumber;
	}


	public void setBusinessLicenseNumber(String businessLicenseNumber) {
		this.businessLicenseNumber = businessLicenseNumber;
	}


	public String getVehicleBrand() {
		return vehicleBrand;
	}


	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}


	public String getVehicleModel() {
		return vehicleModel;
	}


	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}


	public String getVehiclePhoto() {
		return vehiclePhoto;
	}


	public void setVehiclePhoto(String vehiclePhoto) {
		this.vehiclePhoto = vehiclePhoto;
	}


	public String getLoadWeight() {
		return loadWeight;
	}


	public void setLoadWeight(String loadWeight) {
		this.loadWeight = loadWeight;
	}


	public String getVehicleMakeDate() {
		return vehicleMakeDate;
	}


	public void setVehicleMakeDate(String vehicleMakeDate) {
		this.vehicleMakeDate = vehicleMakeDate;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
}
