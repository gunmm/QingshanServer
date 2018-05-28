package com.gunmm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.DictionaryDao;
import com.gunmm.impl.DictionaryImpl;
import com.gunmm.model.DictionaryModel;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class DictionaryController {
	// 查询
	@RequestMapping("/getDictionaryList")
	@ResponseBody
	private JSONObject getOrderCarList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String name = object.getString("name");

		DictionaryDao dictionaryDao = new DictionaryImpl();
		List<DictionaryModel> dictionaryList = dictionaryDao.getDictionaryListByName(name);
		
		return JSONUtils.responseToJsonString("1", "", "查询成功！", dictionaryList);
	}
}
