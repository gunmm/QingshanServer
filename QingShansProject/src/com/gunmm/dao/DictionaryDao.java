package com.gunmm.dao;

import java.util.List;

import com.gunmm.model.DictionaryModel;

public interface DictionaryDao {

	//获得列表
	public List<DictionaryModel> getDictionaryListByName(String name, String cityName);
	
	//根据name和keyText查询valuetext
	public String getValueTextByNameAndkey(String name, String keyText);


	//据订单的车辆类型和发货城市查询对应的价格model
	public DictionaryModel getPriceDicByNameAndVehicleType(String vehicleType, String cityName);
}
