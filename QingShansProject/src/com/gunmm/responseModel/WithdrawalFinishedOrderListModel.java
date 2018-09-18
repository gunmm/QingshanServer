package com.gunmm.responseModel;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class WithdrawalFinishedOrderListModel {
	private String orderId;   //订单ID
	private Double distance;   //距离
	private Double price;   //价格
	private Double servicePrice;   //服务费
	private String carTypeName;   //车辆类型
	private String driverSiteName;   //司机站点名称
	private String driverSiteId;   //司机站点id
	private String masterSiteName;   //货主站点名称
	private String masterSiteId;   //货主站点id
	private String driverBelongSiteType;   //司机所属类型
	private String masterBelongSiteType;   //货主所属类型
	private String masterSiteWithdrawStatus; //货主所属站点提现状态 0：未提现  1：已提现	
	private String driverSiteWithdrawStatus; //司机所属站点提现状态 0：未提现  1：已提现
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date finishTime; //完成时间  
	
	
	
	
	public WithdrawalFinishedOrderListModel() {

	}



	public String getOrderId() {
		return orderId;
	}



	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}



	public Double getDistance() {
		return distance;
	}



	public void setDistance(Double distance) {
		this.distance = distance;
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



	public String getCarTypeName() {
		return carTypeName;
	}



	public void setCarTypeName(String carTypeName) {
		this.carTypeName = carTypeName;
	}



	public String getDriverSiteName() {
		return driverSiteName;
	}



	public void setDriverSiteName(String driverSiteName) {
		this.driverSiteName = driverSiteName;
	}



	public String getDriverSiteId() {
		return driverSiteId;
	}



	public void setDriverSiteId(String driverSiteId) {
		this.driverSiteId = driverSiteId;
	}



	public String getMasterSiteName() {
		return masterSiteName;
	}



	public void setMasterSiteName(String masterSiteName) {
		this.masterSiteName = masterSiteName;
	}



	public String getMasterSiteId() {
		return masterSiteId;
	}



	public void setMasterSiteId(String masterSiteId) {
		this.masterSiteId = masterSiteId;
	}



	public String getDriverBelongSiteType() {
		return driverBelongSiteType;
	}



	public void setDriverBelongSiteType(String driverBelongSiteType) {
		this.driverBelongSiteType = driverBelongSiteType;
	}



	public String getMasterBelongSiteType() {
		return masterBelongSiteType;
	}



	public void setMasterBelongSiteType(String masterBelongSiteType) {
		this.masterBelongSiteType = masterBelongSiteType;
	}
	
	



	public String getMasterSiteWithdrawStatus() {
		return masterSiteWithdrawStatus;
	}



	public void setMasterSiteWithdrawStatus(String masterSiteWithdrawStatus) {
		this.masterSiteWithdrawStatus = masterSiteWithdrawStatus;
	}



	public String getDriverSiteWithdrawStatus() {
		return driverSiteWithdrawStatus;
	}



	public void setDriverSiteWithdrawStatus(String driverSiteWithdrawStatus) {
		this.driverSiteWithdrawStatus = driverSiteWithdrawStatus;
	}



	public Date getFinishTime() {
		return finishTime;
	}



	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	
	
	
	

}
