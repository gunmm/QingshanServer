package com.gunmm.responseModel;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ComplainResModel {

	private String recordId;
	private String createManId;               //投诉人
	private String createManName;               //投诉人姓名
	private String complainedManId;               //被投诉人id
	private String complainedManName;               //被投诉人姓名
	private String orderId;               //订单
	private String orderStatus;               //订单状态
	private Double price = 0.0; //总运费用
	private Double servicePrice = 0.0; //服务费
	private Double distance = 0.0; //距离
	
	private String type;               //投诉类型 1:货主投诉司机   2：司机投诉货主
	private String note;               //投诉详情
	private String manageMan;               //操作员
	private String manageManName;               //操作员姓名
	private String manageStatus;               //处理状态   0:待处理  1：已处理
	private String manageDetail;               //处理详情 (文字描述)
	private String manageDriver;               //处理司机 0：不处理  1：评分减一  2：拉黑
	private String manageMaster;               //处理货主 0：不处理  1：评分减一  2：拉黑
	private String manageOrder;               //处理订单 0：不处理  1：变为取消状态  2：变为异常状态
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;              // 创建时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;              // 更新时间
	
	
	
	
	
	public ComplainResModel() {

	}





	public String getRecordId() {
		return recordId;
	}





	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}





	public String getCreateManId() {
		return createManId;
	}





	public void setCreateManId(String createManId) {
		this.createManId = createManId;
	}





	public String getCreateManName() {
		return createManName;
	}





	public void setCreateManName(String createManName) {
		this.createManName = createManName;
	}





	public String getComplainedManId() {
		return complainedManId;
	}





	public void setComplainedManId(String complainedManId) {
		this.complainedManId = complainedManId;
	}





	public String getComplainedManName() {
		return complainedManName;
	}





	public void setComplainedManName(String complainedManName) {
		this.complainedManName = complainedManName;
	}





	public String getOrderId() {
		return orderId;
	}





	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}





	public String getOrderStatus() {
		return orderStatus;
	}





	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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





	public String getType() {
		return type;
	}





	public void setType(String type) {
		this.type = type;
	}





	public String getNote() {
		return note;
	}





	public void setNote(String note) {
		this.note = note;
	}





	public String getManageMan() {
		return manageMan;
	}





	public void setManageMan(String manageMan) {
		this.manageMan = manageMan;
	}





	public String getManageManName() {
		return manageManName;
	}





	public void setManageManName(String manageManName) {
		this.manageManName = manageManName;
	}





	public String getManageStatus() {
		return manageStatus;
	}





	public void setManageStatus(String manageStatus) {
		this.manageStatus = manageStatus;
	}





	public String getManageDetail() {
		return manageDetail;
	}





	public void setManageDetail(String manageDetail) {
		this.manageDetail = manageDetail;
	}


	


	public String getManageDriver() {
		return manageDriver;
	}





	public void setManageDriver(String manageDriver) {
		this.manageDriver = manageDriver;
	}





	public String getManageMaster() {
		return manageMaster;
	}





	public void setManageMaster(String manageMaster) {
		this.manageMaster = manageMaster;
	}





	public String getManageOrder() {
		return manageOrder;
	}





	public void setManageOrder(String manageOrder) {
		this.manageOrder = manageOrder;
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
