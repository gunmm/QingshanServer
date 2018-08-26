package com.gunmm.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class DriverWithdrawal {

	private String driverWithdrawalId;           //提现记录Id
	private String driverWithdrawalAmount;           //提现金额
	private String toBankNumber;           //提现银行卡
	private String toUserId;           //提现的用户ID
	private String oprationUserId;           //操作员Id
	
	private String driverWithdrawalStatus; //司机提现状态 0：已提交  1：管理员已打款
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date driverWithdrawalTime;   //提现时间
	
	
	
	public DriverWithdrawal() {

	}



	public String getDriverWithdrawalId() {
		return driverWithdrawalId;
	}



	public void setDriverWithdrawalId(String driverWithdrawalId) {
		this.driverWithdrawalId = driverWithdrawalId;
	}



	public String getDriverWithdrawalAmount() {
		return driverWithdrawalAmount;
	}



	public void setDriverWithdrawalAmount(String driverWithdrawalAmount) {
		this.driverWithdrawalAmount = driverWithdrawalAmount;
	}



	public String getToBankNumber() {
		return toBankNumber;
	}



	public void setToBankNumber(String toBankNumber) {
		this.toBankNumber = toBankNumber;
	}



	public String getToUserId() {
		return toUserId;
	}



	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}



	public String getOprationUserId() {
		return oprationUserId;
	}



	public void setOprationUserId(String oprationUserId) {
		this.oprationUserId = oprationUserId;
	}



	public String getDriverWithdrawalStatus() {
		return driverWithdrawalStatus;
	}



	public void setDriverWithdrawalStatus(String driverWithdrawalStatus) {
		this.driverWithdrawalStatus = driverWithdrawalStatus;
	}



	public Date getDriverWithdrawalTime() {
		return driverWithdrawalTime;
	}



	public void setDriverWithdrawalTime(Date driverWithdrawalTime) {
		this.driverWithdrawalTime = driverWithdrawalTime;
	}
	
	
}
