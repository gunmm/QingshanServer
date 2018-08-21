package com.gunmm.responseModel;

public class WithdrawalListModel {

	private String siteId;   //站点ID
	private String siteName;   //站点名称
	private Double childToSuperRate;   // 子站点给父站点的分成率 
	private String bankCardNumber;   //站点银行卡号
	private String userId;   //站点法人id
	private String nickname;   //站点法人姓名
	private Double registerDriverFee;   //站点司机提成总和
	private Double registerGoodsManFee;   //站点货主提成总和
	private Double childDriverFee;   //子站点司机提成总和
	private Double childGoodsManFee;   //子站点货主提成总和
	private Double totalFee;   //总费用
	
	
	public WithdrawalListModel() {

	}


	public String getSiteId() {
		return siteId;
	}


	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}


	public String getSiteName() {
		return siteName;
	}


	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}


	public String getBankCardNumber() {
		return bankCardNumber;
	}


	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	


	public Double getChildToSuperRate() {
		return childToSuperRate;
	}


	public void setChildToSuperRate(Double childToSuperRate) {
		this.childToSuperRate = childToSuperRate;
	}


	public Double getRegisterDriverFee() {
		return registerDriverFee;
	}


	public void setRegisterDriverFee(Double registerDriverFee) {
		this.registerDriverFee = registerDriverFee;
	}


	public Double getRegisterGoodsManFee() {
		return registerGoodsManFee;
	}


	public void setRegisterGoodsManFee(Double registerGoodsManFee) {
		this.registerGoodsManFee = registerGoodsManFee;
	}


	public Double getChildDriverFee() {
		return childDriverFee;
	}


	public void setChildDriverFee(Double childDriverFee) {
		this.childDriverFee = childDriverFee;
	}


	public Double getChildGoodsManFee() {
		return childGoodsManFee;
	}


	public void setChildGoodsManFee(Double childGoodsManFee) {
		this.childGoodsManFee = childGoodsManFee;
	}


	public Double getTotalFee() {
		return totalFee;
	}


	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}


	
	
	
	
	
}
