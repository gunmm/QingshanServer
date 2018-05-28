package com.gunmm.dao;

import java.util.List;

import com.gunmm.model.DictionaryModel;

public interface DictionaryDao {

	//获得列表
	public List<DictionaryModel> getDictionaryListByName(String name);

}
