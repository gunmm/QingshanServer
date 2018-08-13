package com.gunmm.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Withdrawal {
	
	private String withdrawalId;           //提现记录Id
	private String withdrawalAmount;           //提现金额
	private String toBankNumber;           //提现银行卡
	private String toUserId;           //被提现的用户ID
	private String oprationUserId;           //操作员Id
	private String periodOfTime;           //提现的时间段
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date withdrawalTime;   //提现时间
	
	
	public Withdrawal() {

	}


	public String getWithdrawalId() {
		return withdrawalId;
	}


	public void setWithdrawalId(String withdrawalId) {
		this.withdrawalId = withdrawalId;
	}


	public String getWithdrawalAmount() {
		return withdrawalAmount;
	}


	public void setWithdrawalAmount(String withdrawalAmount) {
		this.withdrawalAmount = withdrawalAmount;
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
	
	


	public String getPeriodOfTime() {
		return periodOfTime;
	}


	public void setPeriodOfTime(String periodOfTime) {
		this.periodOfTime = periodOfTime;
	}


	public Date getWithdrawalTime() {
		return withdrawalTime;
	}


	public void setWithdrawalTime(Date withdrawalTime) {
		this.withdrawalTime = withdrawalTime;
	}
	
	
	
	

}
