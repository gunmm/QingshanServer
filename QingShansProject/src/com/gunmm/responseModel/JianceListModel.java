package com.gunmm.responseModel;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class JianceListModel {
	
	private String recordId;
	private String plateNumberType;
	private String plateNumberString;
	private String plateNumber;
	private String masterName;
	private String masterPhone;
	private String carSkeletonNumber;
	private String carDistance;	
	private String masterAddress;	
	private String status;	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date(0);   //创建时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updataTime = new Date(0);   //更新时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date checkTime = new Date(0);   //更新时间
	
	public JianceListModel() {

	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getPlateNumberType() {
		return plateNumberType;
	}

	public void setPlateNumberType(String plateNumberType) {
		this.plateNumberType = plateNumberType;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public String getMasterPhone() {
		return masterPhone;
	}

	public void setMasterPhone(String masterPhone) {
		this.masterPhone = masterPhone;
	}

	public String getCarSkeletonNumber() {
		return carSkeletonNumber;
	}

	public void setCarSkeletonNumber(String carSkeletonNumber) {
		this.carSkeletonNumber = carSkeletonNumber;
	}

	public String getCarDistance() {
		return carDistance;
	}

	public void setCarDistance(String carDistance) {
		this.carDistance = carDistance;
	}

	public String getMasterAddress() {
		return masterAddress;
	}

	public void setMasterAddress(String masterAddress) {
		this.masterAddress = masterAddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdataTime() {
		return updataTime;
	}

	public void setUpdataTime(Date updataTime) {
		this.updataTime = updataTime;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getPlateNumberString() {
		return plateNumberString;
	}

	public void setPlateNumberString(String plateNumberString) {
		this.plateNumberString = plateNumberString;
	}
	
}
