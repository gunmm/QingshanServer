package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Site;

public interface SiteDao {

	// 新建站点
	public JSONObject addSite(Site site);
	
	//删除站点
	public JSONObject deleteSiteById(String siteId);
	
	//查询站点列表
	public List<Site> getSiteList(String page, String rows);
	
	//查询站点条数
	public Long getSiteCount();
	
	//根据站点id拿站点信息
	public Site getSiteById(String siteId);
	
	// 更新站点
	public JSONObject updateSiteInfo(Site site);

}
