package com.gunmm.model;

public class DictionaryModel {

	private String recordId;
	private String name;
	private String keyText;
	private String valueText;
	private String description;

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

	

	
}
