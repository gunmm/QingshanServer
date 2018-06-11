package com.gunmm.responseModel;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class OrderListModel {

	private String orderId;
	private String status; //订单状态 0：刚新建未被接单  1：已被接单  2：已发货  3：发货完成  9：订单取消
	private String type; //订单类型   0：全部   1：实时  2：预约
	private String appointStatus; //预约订单司机方的执行状态   0：未开始   1：已开始

	private String createManId; //发布人id
	private String driverId; //接单司机id

	private String linkMan; //联系人
	private String linkPhone; //联系电话
	
	private String carType; //车辆类型
	private String carTypeName; //车辆类型

	private String note; //备注


	
	private String sendAddress; //发货地址
	
	private Double sendLatitude = 0.0; 
	private Double sendLongitude = 0.0; 
	private String receiveAddress; //送货目标地址
	private Double receiveLatitude = 0.0; 
	private Double receiveLongitude = 0.0; 
	
	private Double price = 0.0; //费用
	private Double distance = 0.0; //距离

	private String payType; //支付方式   1:支付宝支付    2:微信支付   3:现金支付
	private String payStatus; //支付状态   0:未支付   1:已支付
	

	private String commentContent;  //评价内容
	private Double commentStar;  //评价星级
	

	
	private String siteComplaint;  //站点投诉
	private String driverComplaint; //司机投诉

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime; // 创建时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime; //更新时间  

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date appointTime; //预约时间  
	
	
	
	private String phoneNumber; //联系电话   注册电话
	private String nickname;   //用户名
	private String personImageUrl;   //用户图像
	
	private Double nowLatitude = 0.0; //司机目前纬度 
	private Double nowLongitude = 0.0; //司机目前经度
	private String plateNumber; //车牌号
	private Float score;  //分值。默认10：   投诉一次减一
	
	public OrderListModel() {

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

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
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

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
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

	public String getSiteComplaint() {
		return siteComplaint;
	}

	public void setSiteComplaint(String siteComplaint) {
		this.siteComplaint = siteComplaint;
	}

	public String getDriverComplaint() {
		return driverComplaint;
	}

	public void setDriverComplaint(String driverComplaint) {
		this.driverComplaint = driverComplaint;
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

	
}
