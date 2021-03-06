package com.gunmm.responseModel;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class OrderListModelForSite {
	private String orderId;
	private String status; //订单状态 0：刚新建未被接单 1:已被抢单  2：已被接单  3：已发货  4：发货完成  9：订单取消
	private String type; //订单类型   0：全部   1：实时  2：预约
	private String appointStatus; //预约订单司机方的执行状态   0：未开始   1：已开始
	private String invoiceId; //发票id   

	private String createManId; //发布人id
	private String driverId; //接单司机id

	private String linkMan; //联系人
	private String linkPhone; //联系电话
	
	private String receiveMan; //发货人
	private String receivePhone; //发货联系电话
	
	private String carType; //车辆类型
	private String carTypeName; //车辆类型
	

	private String note; //备注


	private String sendCity; //发货城市
	private String sendAddress; //发货地址
	private String sendDetailAddress;
	private Double sendLatitude = 0.0; 
	private Double sendLongitude = 0.0; 
	
	private String receiveAddress; //送货目标地址
	private String receiveDetailAddress;
	private Double receiveLatitude = 0.0; 
	private Double receiveLongitude = 0.0; 
	
	private Double price = 0.0; //总运费用
	private Double servicePrice = 0.0; //服务费
	private Double distance = 0.0; //距离
	
	private String masterSiteWithdrawStatus; //货主所属站点提现状态 0：未提现  1：已提现
	private String masterSiteWithdrawId; //货主所属站点提现记录Id
	
	private String driverSiteWithdrawStatus; //司机所属站点提现状态 0：未提现  1：已提现
	private String driverSiteWithdrawId; //司机所属站点提现记录Id
	
	private String freightFeePayType; //运费  支付方式   1:支付宝支付    2:微信支付   3:现金支付
	private String freightFeePayStatus; //运费 支付状态   0:未支付   1:已支付
	private String freightFeePayId; //运费 支付号 

	private String serviceFeePayType; //服务费  支付方式   1:支付宝支付    2:微信支付
	private String serviceFeePayStatus; //服务费 支付状态   0:未支付   1:已支付
	private String serviceFeePayId; //服务费 支付号 
	

	private String commentContent;  //评价内容
	private Double commentStar = 0.0;  //评价星级


	private String driverCommentContent;  //货主评价内容
	private Double driverCommentStar = 0.0;  //货主评价星级
	
	private String siteComplaintId;  //站点投诉ID
	private String driverComplaintId; //司机投诉ID

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime; // 创建时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime; //更新时间  

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date appointTime; //预约时间  
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date finishTime; //完成时间  
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date timeOut; //超时时间  
	
	
	
	private String phoneNumber; //联系电话   注册电话
	private String nickname;   //用户名
	private String personImageUrl;   //用户图像
	
	private Double nowLatitude = 0.0; //司机目前纬度 
	private Double nowLongitude = 0.0; //司机目前经度
	private String plateNumber; //车牌号
	private Float score;  //分值。默认5： 
	
	private String orderRoleBelong; //订单对应人员    1：货主是本站点   2：司机是本站点   3：货主司机都是本站点
	
	private String createManName; //发布人name
	private Float createManScore; //发布人score
	
	public OrderListModelForSite() {

	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAppointStatus() {
		return appointStatus;
	}

	public void setAppointStatus(String appointStatus) {
		this.appointStatus = appointStatus;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getCreateManId() {
		return createManId;
	}

	public void setCreateManId(String createManId) {
		this.createManId = createManId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getReceiveMan() {
		return receiveMan;
	}

	public void setReceiveMan(String receiveMan) {
		this.receiveMan = receiveMan;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarTypeName() {
		return carTypeName;
	}

	public void setCarTypeName(String carTypeName) {
		this.carTypeName = carTypeName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSendCity() {
		return sendCity;
	}

	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public String getSendDetailAddress() {
		return sendDetailAddress;
	}

	public void setSendDetailAddress(String sendDetailAddress) {
		this.sendDetailAddress = sendDetailAddress;
	}

	public Double getSendLatitude() {
		return sendLatitude;
	}

	public void setSendLatitude(Double sendLatitude) {
		this.sendLatitude = sendLatitude;
	}

	public Double getSendLongitude() {
		return sendLongitude;
	}

	public void setSendLongitude(Double sendLongitude) {
		this.sendLongitude = sendLongitude;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getReceiveDetailAddress() {
		return receiveDetailAddress;
	}

	public void setReceiveDetailAddress(String receiveDetailAddress) {
		this.receiveDetailAddress = receiveDetailAddress;
	}

	public Double getReceiveLatitude() {
		return receiveLatitude;
	}

	public void setReceiveLatitude(Double receiveLatitude) {
		this.receiveLatitude = receiveLatitude;
	}

	public Double getReceiveLongitude() {
		return receiveLongitude;
	}

	public void setReceiveLongitude(Double receiveLongitude) {
		this.receiveLongitude = receiveLongitude;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(Double servicePrice) {
		this.servicePrice = servicePrice;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
	
	

	public String getMasterSiteWithdrawStatus() {
		return masterSiteWithdrawStatus;
	}

	public void setMasterSiteWithdrawStatus(String masterSiteWithdrawStatus) {
		this.masterSiteWithdrawStatus = masterSiteWithdrawStatus;
	}

	public String getMasterSiteWithdrawId() {
		return masterSiteWithdrawId;
	}

	public void setMasterSiteWithdrawId(String masterSiteWithdrawId) {
		this.masterSiteWithdrawId = masterSiteWithdrawId;
	}

	public String getDriverSiteWithdrawStatus() {
		return driverSiteWithdrawStatus;
	}

	public void setDriverSiteWithdrawStatus(String driverSiteWithdrawStatus) {
		this.driverSiteWithdrawStatus = driverSiteWithdrawStatus;
	}

	public String getDriverSiteWithdrawId() {
		return driverSiteWithdrawId;
	}

	public void setDriverSiteWithdrawId(String driverSiteWithdrawId) {
		this.driverSiteWithdrawId = driverSiteWithdrawId;
	}

	public String getFreightFeePayType() {
		return freightFeePayType;
	}

	public void setFreightFeePayType(String freightFeePayType) {
		this.freightFeePayType = freightFeePayType;
	}

	public String getFreightFeePayStatus() {
		return freightFeePayStatus;
	}

	public void setFreightFeePayStatus(String freightFeePayStatus) {
		this.freightFeePayStatus = freightFeePayStatus;
	}

	public String getFreightFeePayId() {
		return freightFeePayId;
	}

	public void setFreightFeePayId(String freightFeePayId) {
		this.freightFeePayId = freightFeePayId;
	}

	public String getServiceFeePayType() {
		return serviceFeePayType;
	}

	public void setServiceFeePayType(String serviceFeePayType) {
		this.serviceFeePayType = serviceFeePayType;
	}

	public String getServiceFeePayStatus() {
		return serviceFeePayStatus;
	}

	public void setServiceFeePayStatus(String serviceFeePayStatus) {
		this.serviceFeePayStatus = serviceFeePayStatus;
	}

	public String getServiceFeePayId() {
		return serviceFeePayId;
	}

	public void setServiceFeePayId(String serviceFeePayId) {
		this.serviceFeePayId = serviceFeePayId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Double getCommentStar() {
		return commentStar;
	}

	public void setCommentStar(Double commentStar) {
		this.commentStar = commentStar;
	}

	public String getDriverCommentContent() {
		return driverCommentContent;
	}

	public void setDriverCommentContent(String driverCommentContent) {
		this.driverCommentContent = driverCommentContent;
	}

	public Double getDriverCommentStar() {
		return driverCommentStar;
	}

	public void setDriverCommentStar(Double driverCommentStar) {
		this.driverCommentStar = driverCommentStar;
	}

	public String getSiteComplaintId() {
		return siteComplaintId;
	}

	public void setSiteComplaintId(String siteComplaintId) {
		this.siteComplaintId = siteComplaintId;
	}

	public String getDriverComplaintId() {
		return driverComplaintId;
	}

	public void setDriverComplaintId(String driverComplaintId) {
		this.driverComplaintId = driverComplaintId;
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

	public Date getAppointTime() {
		return appointTime;
	}

	public void setAppointTime(Date appointTime) {
		this.appointTime = appointTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Date getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Date timeOut) {
		this.timeOut = timeOut;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public String getOrderRoleBelong() {
		return orderRoleBelong;
	}

	public void setOrderRoleBelong(String orderRoleBelong) {
		this.orderRoleBelong = orderRoleBelong;
	}

	public String getCreateManName() {
		return createManName;
	}

	public void setCreateManName(String createManName) {
		this.createManName = createManName;
	}

	public Float getCreateManScore() {
		return createManScore;
	}

	public void setCreateManScore(Float createManScore) {
		this.createManScore = createManScore;
	}
	
	
	
	
}
