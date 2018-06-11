package com.gunmm.model;

public class DictionaryModel {

	private String recordId;
	private String name;
	private String keyText;
	private String valueText;
	private String description;
	
	private Double startPrice; //起步价
	private Double unitPrice;  //单价
	private Double startDistance;  //起步距离



	

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
	
	

	

	
}
