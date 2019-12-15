package com.gunmm.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class User {
	private String userId;
	private String accessToken;

	private String phoneNumber; //联系电话   注册电话
	private String password;
	private String nickname;   //用户名
	private String personImageUrl;   //用户图像
	private String belongSiteId;   //所属站点id
	
	private String siteType;   //所属站点类型  只用于存数据  表中没有
	
	
	private String loginPlate; //登陆平台

	private String type;   // 0：超级管理员，1：平台管理员，2：平台投诉业务处理人员，3：站点管理员，4：站点业务员，5：货源方，6：司机 7：检测管理员，8：检测员工
	private String userIdCardNumber; //身份证
	
	private String driverType;//司机类型 //1: 车主司机  2:小司机
	private String superDriver;//父司机
	
	private String status; //状态       司机是否在拉货  1：在拉货 0：未拉货  2：下班  3：没车司机
	private String vehicleId; //车辆id
	private String driverLicenseNumber; //司机驾驶证号
	private String driverQualificationNumber; //司机资格证号
	private Double nowLatitude = 0.0; //司机目前纬度 
	private Double nowLongitude = 0.0; //司机目前经度
	private String bankCardNumber; //银行卡号
	
	
	 
	
	private String mainGoodsName; //主要经营货物名称
	private String businessCardNumber; //营业执照号
	 
	
	private Double score = 5.0;  //分值。默认10：   投诉一次减0.1
	
	private String deleteStatus; //是否被删除
	private String blackStatus; //是否被拉黑
	
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date(0);   //创建时间
    
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime = new Date(0);   //更新时间
    
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime = new Date(0);   //最后一次登录时间
        
    
	public User() {

	}






	public String getUserId() {
		return userId;
	}






	public void setUserId(String userId) {
		this.userId = userId;
	}






	public String getAccessToken() {
		return accessToken;
	}






	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
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






	public String getBelongSiteId() {
		return belongSiteId;
	}






	public void setBelongSiteId(String belongSiteId) {
		this.belongSiteId = belongSiteId;
	}






	public String getLoginPlate() {
		return loginPlate;
	}






	public void setLoginPlate(String loginPlate) {
		this.loginPlate = loginPlate;
	}






	public String getType() {
		return type;
	}






	public void setType(String type) {
		this.type = type;
	}






	public String getUserIdCardNumber() {
		return userIdCardNumber;
	}






	public void setUserIdCardNumber(String userIdCardNumber) {
		this.userIdCardNumber = userIdCardNumber;
	}




	


	public String getDriverType() {
		return driverType;
	}






	public void setDriverType(String driverType) {
		this.driverType = driverType;
	}






	public String getSuperDriver() {
		return superDriver;
	}






	public void setSuperDriver(String superDriver) {
		this.superDriver = superDriver;
	}






	public String getStatus() {
		return status;
	}






	public void setStatus(String status) {
		this.status = status;
	}






	public String getVehicleId() {
		return vehicleId;
	}






	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}





	public String getDriverLicenseNumber() {
		return driverLicenseNumber;
	}






	public void setDriverLicenseNumber(String driverLicenseNumber) {
		this.driverLicenseNumber = driverLicenseNumber;
	}






	public String getDriverQualificationNumber() {
		return driverQualificationNumber;
	}






	public void setDriverQualificationNumber(String driverQualificationNumber) {
		this.driverQualificationNumber = driverQualificationNumber;
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






	public String getBankCardNumber() {
		return bankCardNumber;
	}






	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}






	public String getMainGoodsName() {
		return mainGoodsName;
	}






	public void setMainGoodsName(String mainGoodsName) {
		this.mainGoodsName = mainGoodsName;
	}






	public String getBusinessCardNumber() {
		return businessCardNumber;
	}






	public void setBusinessCardNumber(String businessCardNumber) {
		this.businessCardNumber = businessCardNumber;
	}






	public Double getScore() {
		return score;
	}






	public void setScore(Double score) {
		this.score = score;
	}






	public String getDeleteStatus() {
		return deleteStatus;
	}






	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}






	public String getBlackStatus() {
		return blackStatus;
	}






	public void setBlackStatus(String blackStatus) {
		this.blackStatus = blackStatus;
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






	public Date getLastLoginTime() {
		return lastLoginTime;
	}






	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}






	public String getSiteType() {
		return siteType;
	}






	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	

	
	
	



	
	

	
}
