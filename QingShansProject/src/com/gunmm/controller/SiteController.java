package com.gunmm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.SiteDao;
import com.gunmm.impl.SiteImpl;
import com.gunmm.model.Site;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class SiteController {

	// 添加站点
	@RequestMapping("/addSite")
	@ResponseBody
	private JSONObject addOrder(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		Site site = new Site();
		site = JSONObject.parseObject(object.toJSONString(), Site.class);

		if (site == null) {
			return JSONUtils.responseToJsonString("0", "未接收到数据", "添加失败！", "");
		}

		SiteDao siteDao = new SiteImpl();
		return siteDao.addOrder(site);
	}
	
	// 查询站点列表
	@RequestMapping("/getSiteList")
	@ResponseBody
	private JSONObject getBigOrderInfo(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String rows = object.getString("rows");
		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		SiteDao siteDao = new SiteImpl();
		List<Site> siteList = siteDao.getSiteList(page, rows);
		
		Long siteCount = siteDao.getSiteCount();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("siteCount", siteCount);
		jsonObject.put("siteList", siteList);
		

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}
}
