package com.gunmm.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Flow {
	private String flowId;
	private String flowStatus; //状态  0:刚新建  1:已被别人抢单  2:已抢到单
	private String orderId;
	private String driverId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date(0);; // 创建时间
	
	
	public Flow() {

	}


	public String getFlowId() {
		return flowId;
	}


	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}


	public String getFlowStatus() {
		return flowStatus;
	}


	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getDriverId() {
		return driverId;
	}


	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
	
}
