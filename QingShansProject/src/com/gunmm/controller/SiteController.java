package com.gunmm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.CityDao;
import com.gunmm.dao.SiteDao;
import com.gunmm.dao.UserDao;
import com.gunmm.impl.CityImpl;
import com.gunmm.impl.SiteImpl;
import com.gunmm.impl.UserDaoImpl;
import com.gunmm.model.Site;
import com.gunmm.model.User;
import com.gunmm.responseModel.SiteListModel;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class SiteController {

	// 添加站点
	@SuppressWarnings("unused")
	@RequestMapping("/addSite")
	@ResponseBody
	private JSONObject addSite(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		Site site = new Site();
		site = JSONObject.parseObject(object.toJSONString(), Site.class);
		
		UserDao userDao = new UserDaoImpl();
		if(userDao.judgeUserByPhone(site.getLawsManPhone())) {
			return JSONUtils.responseToJsonString("0", "法人手机号已被注册！", "法人手机号已被注册！", "");
		}

		if (site == null) {
			return JSONUtils.responseToJsonString("0", "未接收到数据", "添加失败！", "");
		}
		
		SiteDao siteDao = new SiteImpl();
		JSONObject jsonObj = siteDao.addSite(site);
		String result_code = jsonObj.getString("result_code");

		if (!"1".equals(result_code)) {
			return jsonObj;
		}
		String addSiteId = jsonObj.getString("object");
		
		
		User user = new User();
		user.setPhoneNumber(site.getLawsManPhone());
		user.setUserIdCardNumber(site.getLawsManIdCardNumber());
		user.setNickname(site.getLawsManName());
		user.setType("3");
		user.setBelongSiteId(addSiteId);
		user.setPassword("e10adc3949ba59abbe56e057f20f883e");
		
		JSONObject jsonObj2 = userDao.addUser(user);
		String result_code2 = jsonObj2.getString("result_code");

		if (!"1".equals(result_code2)) {
			siteDao.deleteSiteById(addSiteId);
			return jsonObj2;
		}
		return JSONUtils.responseToJsonString("1", "", "添加成功！", addSiteId);
	}

	// 删除站点
	@RequestMapping("/deleteSite")
	@ResponseBody
	private JSONObject deleteSite(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String siteId = object.getString("siteId");
		SiteDao siteDao = new SiteImpl();
		Site site = siteDao.getSiteById(siteId);
		JSONObject jsonObj = siteDao.deleteSiteById(siteId);
		String result_code = jsonObj.getString("result_code");
		
		if ("1".equals(result_code)) {
			if (site != null) {
				UserDao userDao = new UserDaoImpl();
				User user = userDao.getUserByPhone(site.getLawsManPhone());
				if (user != null) {
					userDao.deleteMaster(user.getUserId());
				}
			}
			
		}
		return jsonObj;
	}

	// 编辑站点
	@RequestMapping("/editSite")
	@ResponseBody
	private JSONObject editSite(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String siteId = object.getString("siteId");
		String siteName = object.getString("siteName");
		String businessLicenseNumber = object.getString("businessLicenseNumber");
		String siteProvince = object.getString("siteProvince");
		String siteCity = object.getString("siteCity");
		String address = object.getString("address");
		String lawsManName = object.getString("lawsManName");
		String lawsManIdCardNumber = object.getString("lawsManIdCardNumber");
		String lawsManPhone = object.getString("lawsManPhone");
		String businessLicensePhoto = object.getString("businessLicensePhoto");
		String leaseContractPhoto = object.getString("leaseContractPhoto");
		Double childToSuperRate = object.getDouble("childToSuperRate");
		

		SiteDao siteDao = new SiteImpl();
		Site site = siteDao.getSiteById(siteId);
		site.setSiteName(siteName);
		site.setBusinessLicenseNumber(businessLicenseNumber);
		site.setSiteProvince(siteProvince);
		site.setSiteCity(siteCity);
		site.setAddress(address);
		site.setLawsManName(lawsManName);
		site.setLawsManIdCardNumber(lawsManIdCardNumber);
		site.setLawsManPhone(lawsManPhone);
		site.setBusinessLicensePhoto(businessLicensePhoto);
		site.setLeaseContractPhoto(leaseContractPhoto);
		site.setChildToSuperRate(childToSuperRate);
		
		if ("2".equals(site.getSiteType())) {
			site.setSiteCheckStatus("0");
		}
		
		return siteDao.updateSiteInfo(site);
	}

	// 审核站点
	@RequestMapping("/checkSite")
	@ResponseBody
	private JSONObject checkSite(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String siteId = object.getString("siteId");
		String siteCheckStatus = object.getString("siteCheckStatus");

		SiteDao siteDao = new SiteImpl();
		Site site = siteDao.getSiteById(siteId);
		site.setSiteCheckStatus(siteCheckStatus);

		return siteDao.updateSiteInfo(site);
	}

	// 根据ID查寻站点信息
	@RequestMapping("/getInfoById")
	@ResponseBody
	private JSONObject getInfoById(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String siteId = object.getString("siteId");

		if (siteId == null) {
			return JSONUtils.responseToJsonString("0", "参数错误！", "查询失败！", "");
		}

		SiteDao siteDao = new SiteImpl();
		Site site = siteDao.getSiteById(siteId);
		
		CityDao cityDao = new CityImpl();
		site.setSiteProvinceName(cityDao.getPrvinceByid(site.getSiteProvince()));
		site.setSiteCityName(cityDao.getCityByid(site.getSiteProvince(), site.getSiteCity()));
		if (site.getSuperSiteId() != null) {
			Site superSite = siteDao.getSiteById(site.getSuperSiteId());
			site.setSuperSiteName(superSite.getSiteName());
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("site", site);
		jsonObject.put("driverList", null);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}

	// 查询站点列表
	@RequestMapping("/getSiteList")
	@ResponseBody
	private JSONObject getBigOrderInfo(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String rows = object.getString("rows");
		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		String filterSiteName = object.getString("filterSiteName");
		String filterLawsManName = object.getString("filterLawsManName");
		String filterBeginTime = object.getString("filterBeginTime");
		String filterEndTime = object.getString("filterEndTime");
		String superSiteId = object.getString("superSiteId");

		SiteDao siteDao = new SiteImpl();
		List<SiteListModel> siteList = siteDao.getSiteList(page, rows, filterSiteName, filterLawsManName, filterBeginTime,
				filterEndTime, superSiteId);

		Long siteCount = siteDao.getSiteCount(filterSiteName, filterLawsManName, filterBeginTime, filterEndTime, superSiteId);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("siteCount", siteCount);
		jsonObject.put("siteList", siteList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}
}
