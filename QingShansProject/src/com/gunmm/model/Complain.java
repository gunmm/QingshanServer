package com.gunmm.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Complain {

	private String recordId;
	private String createManId;               //投诉人
	private String orderId;               //订单
	private String type;               //投诉类型 1:货主投诉司机   2：司机投诉货主
	private String note;               //投诉详情
	private String manageMan;               //操作员
	private String manageStatus;               //处理状态   0:待处理  0:待处理  1：已处理
	private String manageDetail;               //处理详情 (文字描述)
	private String manageDriver;               //处理司机 0：不处理  1：评分减一  2：拉黑
	private String manageMaster;               //处理货主 0：不处理  1：评分减一  2：拉黑
	private String manageOrder;               //处理订单 0：不处理  1：变为取消状态  2：变为异常状态
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date(0);              // 创建时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime = new Date(0);            // 更新时间
	
	
	public Complain() {

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


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
