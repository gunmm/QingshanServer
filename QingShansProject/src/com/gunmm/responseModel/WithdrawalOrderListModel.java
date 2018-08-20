package com.gunmm.responseModel;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class WithdrawalOrderListModel {
	private String orderId;   //订单ID
	private String carType;   //车辆类型
	private Double distance;   //距离
	private Double price;   //价格
	private Double servicePrice;   //服务费
	private String carTypeName;   //车辆类型
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date finishTime;              // 完成时间
	
	
	public WithdrawalOrderListModel() {

	}


	


	public String getOrderId() {
		return orderId;
	}





	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}





	public String getCarType() {
		return carType;
	}


	public void setCarType(String carType) {
		this.carType = carType;
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





	public Date getFinishTime() {
		return finishTime;
	}





	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}


	
	
	
	

}
