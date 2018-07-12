package com.gunmm.model;

public class City {

	private String id;              // id 
	private String name; 
	private String parent_id;
	private String sort;
	private String deep;
	
	public City() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDeep() {
		return deep;
	}

	public void setDeep(String deep) {
		this.deep = deep;
	}
	
	
	

}
