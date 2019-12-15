package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.JianceCheck;
import com.gunmm.responseModel.JianceListModel;

public interface JianceCheckDao {
	
	// 添加
		public JSONObject addJianceCheck(JianceCheck jianceCheck);

		// 更新
		public JSONObject updateJianceCheck(JianceCheck jianceCheck);

		// 根据id取user
		public JianceCheck getJianceCheckById(String recordId);
		
		//查询检测单列表
		public List<JianceListModel> getJianceCheckList(String page, String rows, String masterPhone, String carPlateNumber, String isDaiBan);
		
		//查询检测单条数isDaiBan
		public Long getJianceCheckCount(String masterPhone, String carPlateNumber, String isDaiBan);

}
