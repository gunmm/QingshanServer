package com.gunmm.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class PingMuUser {
	private String deviceId;
	private String pingMuVersion;
	private String deviceVersion;
	
	private String systemVersion;
	private String name;
	private String model;
	private String localizedModel;
	private String systemName;	
	private String deviceString;	
	private String signValue;	
	private String lastUrl;	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date downLoadTime = new Date(0);   //开始计费时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date expirationTime = new Date(0);   //到期时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date(0);   //创建时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updataTime = new Date(0);   //更新时间
	
	public PingMuUser() {

	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPingMuVersion() {
		return pingMuVersion;
	}

	public void setPingMuVersion(String pingMuVersion) {
		this.pingMuVersion = pingMuVersion;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLocalizedModel() {
		return localizedModel;
	}

	public void setLocalizedModel(String localizedModel) {
		this.localizedModel = localizedModel;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getDeviceString() {
		return deviceString;
	}

	public void setDeviceString(String deviceString) {
		this.deviceString = deviceString;
	}

	public String getSignValue() {
		return signValue;
	}

	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}

	public String getLastUrl() {
		return lastUrl;
	}

	public void setLastUrl(String lastUrl) {
		this.lastUrl = lastUrl;
	}

	public Date getDownLoadTime() {
		return downLoadTime;
	}

	public void setDownLoadTime(Date downLoadTime) {
		this.downLoadTime = downLoadTime;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	
		
}
