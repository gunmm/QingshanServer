package com.gunmm.model;

public class DictionaryModel {

	private String recordId;
	private String name;
	private String keyText;
	private String valueText;
	private String description;
	private String cityName;
	
	private Double startPrice; //起步价
	private Double unitPrice;  //单价
	private Double startDistance;  //起步距离
	
	private String width; //车辆宽度
	private String size;   //车辆大小  1：微型车，2：轻型车（默认值），3：中型车，4：重型车
	private String weight;   //货车核定载重
	private String axis;   //车辆轴数
	private String height;  //车辆高度
	private String load;   //车辆总重
	private String strategy;  //驾车选择策略


	

	public DictionaryModel() {

	}

	@Override
	public String toString() {
		return "DictionaryModel [recordId=" + recordId + ", name=" + name + ", keyText=" + keyText + ", valueText="
				+ valueText + ", description=" + description + "]";
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeyText() {
		return keyText;
	}

	public void setKeyText(String keyText) {
		this.keyText = keyText;
	}

	public String getValueText() {
		return valueText;
	}

	public void setValueText(String valueText) {
		this.valueText = valueText;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Double getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(Double startPrice) {
		this.startPrice = startPrice;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getStartDistance() {
		return startDistance;
	}

	public void setStartDistance(Double startDistance) {
		this.startDistance = startDistance;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getLoad() {
		return load;
	}

	public void setLoad(String load) {
		this.load = load;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	
}
