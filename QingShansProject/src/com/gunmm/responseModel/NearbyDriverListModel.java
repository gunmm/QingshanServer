package com.gunmm.responseModel;

public class NearbyDriverListModel {
	private String userId;
	private String plateNumber;
	private Double nowLatitude = 0.0; //车辆目前纬度 
	private Double nowLongitude = 0.0; //车辆目前经度
	private Double distance = 0.0; //车辆目前距离
	
	public NearbyDriverListModel() {

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public Double getNowLatitude() {
		return nowLatitude;
	}

	public void setNowLatitude(Double nowLatitude) {
		this.nowLatitude = nowLatitude;
	}

	public Double getNowLongitude() {
		return nowLongitude;
	}

	public void setNowLongitude(Double nowLongitude) {
		this.nowLongitude = nowLongitude;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
	
	
	
	
}
