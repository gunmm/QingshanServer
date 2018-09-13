package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Complain;
import com.gunmm.responseModel.ComplainResModel;

public interface ComplainDao {

	// 新建投诉
	public JSONObject addComplainComplain(Complain complain);

	// 根据站点id拿投诉信息
	public Complain getComplainById(String complainId);

	// 更新投诉
	public JSONObject updateComplainInfo(Complain complain);

	// 根据投诉id查询相关详情信息
	public JSONObject getComplainDetailById(String complainId, String type);

	// 查询投诉列表
	public List<ComplainResModel> getComplainList(String type,String manageStatus, String page, String rows);

	// 查询投诉列表条数
	public Long getComplainListCount(String type, String manageStatus);

}
