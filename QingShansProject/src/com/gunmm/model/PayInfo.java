package com.gunmm.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class PayInfo {
	private String recordId;
	private String deviceId;
	private String product;
	private String environment;
	private String receipt_type;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date(0);   //创建时间
	
	public PayInfo() {

	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getReceipt_type() {
		return receipt_type;
	}

	public void setReceipt_type(String receipt_type) {
		this.receipt_type = receipt_type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	
	
}
