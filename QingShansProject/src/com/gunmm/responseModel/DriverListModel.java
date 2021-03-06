package com.gunmm.responseModel;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class DriverListModel {

	private String userId;
	private String accessToken;

	private String phoneNumber; //联系电话   注册电话
	private String password;
	private String nickname;   //用户名
	private String personImageUrl;   //用户图像
	private String belongSiteId;   //所属站点id
	private String belongSiteName;   //所属站点名字
	
	
	private String loginPlate; //登陆平台

	private String type;   // 0：超级管理员，1：平台管理员，2：平台投诉业务处理人员，3：站点管理员，4：站点业务员，5：货源方，6：司机
	private String userIdCardNumber; //身份证
	
	private String driverType;//司机类型 //1: 车主司机  2:小司机
	private String superDriver;//父司机
	
	
	private String status; //状态       司机是否在拉货  1：在拉货 0：未拉货  2：下班
	private String vehicleId; //车辆id
	private String plateNumber;              // 车牌号
	private String vehicleType; //车辆类型
	private String vehicleTypeName; //车辆类型
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
	
	
	
	private String gpsType;              // gps类型
	private String gpsTypeName;  
	private String gpsSerialNumber;              // gps序列号
	
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
	
	private String vehicleBindingDriverId;              // 车辆当前绑定司机ID

	
	
	
	
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;   //创建时间
    
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;   //更新时间
    
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;   //最后一次登录时间

    public DriverListModel() {

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

	public String getBelongSiteName() {
		return belongSiteName;
	}

	public void setBelongSiteName(String belongSiteName) {
		this.belongSiteName = belongSiteName;
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


	public String getVehicleTypeName() {
		return vehicleTypeName;
	}


	public void setVehicleTypeName(String vehicleTypeName) {
		this.vehicleTypeName = vehicleTypeName;
	}


	public String getGpsType() {
		return gpsType;
	}


	public void setGpsType(String gpsType) {
		this.gpsType = gpsType;
	}


	public String getGpsSerialNumber() {
		return gpsSerialNumber;
	}


	public void setGpsSerialNumber(String gpsSerialNumber) {
		this.gpsSerialNumber = gpsSerialNumber;
	}
	
	


	public String getGpsTypeName() {
		return gpsTypeName;
	}


	public void setGpsTypeName(String gpsTypeName) {
		this.gpsTypeName = gpsTypeName;
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


	public String getVehicleBindingDriverId() {
		return vehicleBindingDriverId;
	}


	public void setVehicleBindingDriverId(String vehicleBindingDriverId) {
		this.vehicleBindingDriverId = vehicleBindingDriverId;
	}
	
	
    
    
    
    
}
