package com.gunmm.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Site {

	private String siteId;              // 站点id 
	private String superSiteId;        // 父站点id 
	private String superSiteName;        // 父站点名字   表里没有的字段  hbm文件中也没有   仅用来返回数据
	private Double childToSuperRate = 0.0;   // 子站点给父站点的分成率 
	private String siteType;           // 站点类型    用来区分是否是子站点1：父站点  2：子站点
	private String siteCheckStatus;    // 站点审核状态 0:未审核  1:审核通过  2:审核未通过
	private String siteName;           // 站点名称
	private String businessLicenseNumber;   // 营业执照
	private String businessLicensePhoto;    // 营业执照照片
	private String lawsManName;        // 法人姓名
	private String lawsManIdCardNumber;   // 法人身份证号
	private String lawsManPhone;   // 法人电话
	private String lawsManIdCardFront;     // 法人身份证正面照片 
	private String lawsManIdCardBehind;   // 法人身份证反面照片 
	private String leaseContractPhoto;   // 场地租赁合同或土地证照片
	private String siteProvince;              // 省 
	private String siteCity;              // 市
	private String siteProvinceName;              // 省 
	private String siteCityName;              // 市 
	private String siteArea;              // 区
	private String address;              //  详细地址
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date(0);              // 创建时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime = new Date(0);              // 更新时间
	
	
	public Site() {

	}


	public String getSiteId() {
		return siteId;
	}


	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}


	public String getSuperSiteId() {
		return superSiteId;
	}


	public void setSuperSiteId(String superSiteId) {
		this.superSiteId = superSiteId;
	}


	public Double getChildToSuperRate() {
		return childToSuperRate;
	}


	public void setChildToSuperRate(Double childToSuperRate) {
		this.childToSuperRate = childToSuperRate;
	}


	public String getSiteType() {
		return siteType;
	}


	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}


	public String getSiteCheckStatus() {
		return siteCheckStatus;
	}


	public void setSiteCheckStatus(String siteCheckStatus) {
		this.siteCheckStatus = siteCheckStatus;
	}


	public String getSiteName() {
		return siteName;
	}


	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}


	public String getBusinessLicenseNumber() {
		return businessLicenseNumber;
	}


	public void setBusinessLicenseNumber(String businessLicenseNumber) {
		this.businessLicenseNumber = businessLicenseNumber;
	}


	public String getBusinessLicensePhoto() {
		return businessLicensePhoto;
	}


	public void setBusinessLicensePhoto(String businessLicensePhoto) {
		this.businessLicensePhoto = businessLicensePhoto;
	}


	public String getLawsManName() {
		return lawsManName;
	}


	public void setLawsManName(String lawsManName) {
		this.lawsManName = lawsManName;
	}


	public String getLawsManIdCardNumber() {
		return lawsManIdCardNumber;
	}


	public void setLawsManIdCardNumber(String lawsManIdCardNumber) {
		this.lawsManIdCardNumber = lawsManIdCardNumber;
	}


	public String getLawsManPhone() {
		return lawsManPhone;
	}


	public void setLawsManPhone(String lawsManPhone) {
		this.lawsManPhone = lawsManPhone;
	}


	public String getLawsManIdCardFront() {
		return lawsManIdCardFront;
	}


	public void setLawsManIdCardFront(String lawsManIdCardFront) {
		this.lawsManIdCardFront = lawsManIdCardFront;
	}


	public String getLawsManIdCardBehind() {
		return lawsManIdCardBehind;
	}


	public void setLawsManIdCardBehind(String lawsManIdCardBehind) {
		this.lawsManIdCardBehind = lawsManIdCardBehind;
	}


	public String getLeaseContractPhoto() {
		return leaseContractPhoto;
	}


	public void setLeaseContractPhoto(String leaseContractPhoto) {
		this.leaseContractPhoto = leaseContractPhoto;
	}


	public String getSiteProvince() {
		return siteProvince;
	}


	public void setSiteProvince(String siteProvince) {
		this.siteProvince = siteProvince;
	}


	public String getSiteCity() {
		return siteCity;
	}


	public void setSiteCity(String siteCity) {
		this.siteCity = siteCity;
	}


	public String getSiteArea() {
		return siteArea;
	}


	public void setSiteArea(String siteArea) {
		this.siteArea = siteArea;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
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


	public String getSuperSiteName() {
		return superSiteName;
	}


	public void setSuperSiteName(String superSiteName) {
		this.superSiteName = superSiteName;
	}


	public String getSiteProvinceName() {
		return siteProvinceName;
	}


	public void setSiteProvinceName(String siteProvinceName) {
		this.siteProvinceName = siteProvinceName;
	}


	public String getSiteCityName() {
		return siteCityName;
	}


	public void setSiteCityName(String siteCityName) {
		this.siteCityName = siteCityName;
	}
	
	

	

}
