package com.gunmm.dao;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Complain;

public interface ComplainDao {

	// 新建投诉
	public JSONObject addComplainComplain(Complain complain);

	// 根据站点id拿投诉信息
	public Complain getComplainById(String complainId);

	// 更新投诉
	public JSONObject updateComplainInfo(Complain complain);

	// 根据投诉id查询相关详情信息
	public JSONObject getComplainDetailById(String complainId, String type);
}
