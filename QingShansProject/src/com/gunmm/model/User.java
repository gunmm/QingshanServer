package com.gunmm.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class User {
	private String userId;
	private String phoneNumber; //联系电话   注册电话
	private String password;
	private String nickname;   //用户名
	private String personImageUrl;   //用户图像

	private String type;   //用户类型0.总管理员   1.站点   2.司机
	private String status; //状态       司机是否在拉货  1：在拉货 0：未拉货
	private String plateNumber; //车牌号
	private String vehicleType; //车辆类型
	
	private String driverCertificationStatus;   //司机认证0.未通过认证   1.认证中  2.认证通过
	private String driverLicenseImageUrl;   //驾驶证照片
	private String driverVehicleImageUrl;   //行驶证照片
	private String driverThirdImageUrl;   //备用照片
	
	private String loginPlate; //登陆平台




	
	private String address; //站点地址
	private Double addressLatitude = 0.0; //纬度   39.12
	private Double addressLongitude = 0.0; //经度116.46
	
	private Double nowLatitude = 0.0; //司机目前纬度 
	private Double nowLongitude = 0.0; //司机目前经度
	
	private Double distance = 0.0; //司机目前距离

	
	private Double score = 5.0;  //分值。默认10：   投诉一次减一
	
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;   //创建时间
    
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;   //最后一次登录时间
    
    
    
    
    
    
	public User() {

	}
	
	public User(String userId, String phoneNumber, String nickname, String personImageUrl, String plateNumber,
			String vehicleType, String loginPlate, Double nowLatitude, Double nowLongitude, Double distance,
			Double score) {
		super();
		this.userId = userId;
		this.phoneNumber = phoneNumber;
		this.nickname = nickname;
		this.personImageUrl = personImageUrl;
		this.plateNumber = plateNumber;
		this.vehicleType = vehicleType;
		this.loginPlate = loginPlate;
		this.nowLatitude = nowLatitude;
		this.nowLongitude = nowLongitude;
		this.distance = distance;
		this.score = score;
	}



	@Override
	public String toString() {
		return "User [userId=" + userId + ", phoneNumber=" + phoneNumber + ", password=" + password + ", nickname="
				+ nickname + ", personImageUrl=" + personImageUrl + ", type=" + type + ", status=" + status
				+ ", plateNumber=" + plateNumber + ", vehicleType=" + vehicleType + ", driverCertificationStatus="
				+ driverCertificationStatus + ", driverLicenseImageUrl=" + driverLicenseImageUrl
				+ ", driverVehicleImageUrl=" + driverVehicleImageUrl + ", driverThirdImageUrl=" + driverThirdImageUrl
				+ ", loginPlate=" + loginPlate + ", address=" + address + ", addressLatitude=" + addressLatitude
				+ ", addressLongitude=" + addressLongitude + ", nowLatitude=" + nowLatitude + ", nowLongitude="
				+ nowLongitude + ", distance=" + distance + ", score=" + score + ", createTime=" + createTime
				+ ", lastLoginTime=" + lastLoginTime + "]";
	}



	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getPhoneNumber() {
		return phoneNumber;
	}



	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getNickname() {
		return nickname;
	}



	public void setNickname(String nickname) {
		this.nickname = nickname;
	}



	public String getPersonImageUrl() {
		return personImageUrl;
	}



	public void setPersonImageUrl(String personImageUrl) {
		this.personImageUrl = personImageUrl;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
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



	public String getDriverCertificationStatus() {
		return driverCertificationStatus;
	}



	public void setDriverCertificationStatus(String driverCertificationStatus) {
		this.driverCertificationStatus = driverCertificationStatus;
	}



	public String getDriverLicenseImageUrl() {
		return driverLicenseImageUrl;
	}



	public void setDriverLicenseImageUrl(String driverLicenseImageUrl) {
		this.driverLicenseImageUrl = driverLicenseImageUrl;
	}



	public String getDriverVehicleImageUrl() {
		return driverVehicleImageUrl;
	}



	public void setDriverVehicleImageUrl(String driverVehicleImageUrl) {
		this.driverVehicleImageUrl = driverVehicleImageUrl;
	}



	public String getDriverThirdImageUrl() {
		return driverThirdImageUrl;
	}



	public void setDriverThirdImageUrl(String driverThirdImageUrl) {
		this.driverThirdImageUrl = driverThirdImageUrl;
	}



	public String getLoginPlate() {
		return loginPlate;
	}



	public void setLoginPlate(String loginPlate) {
		this.loginPlate = loginPlate;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public Double getAddressLatitude() {
		return addressLatitude;
	}



	public void setAddressLatitude(Double addressLatitude) {
		this.addressLatitude = addressLatitude;
	}



	public Double getAddressLongitude() {
		return addressLongitude;
	}



	public void setAddressLongitude(Double addressLongitude) {
		this.addressLongitude = addressLongitude;
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



	public Double getScore() {
		return score;
	}



	public void setScore(Double score) {
		this.score = score;
	}



	public Date getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



	public Date getLastLoginTime() {
		return lastLoginTime;
	}



	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	

	
}
