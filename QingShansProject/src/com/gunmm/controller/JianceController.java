package com.gunmm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.DictionaryDao;
import com.gunmm.dao.JianceCheckDao;
import com.gunmm.impl.DictionaryImpl;
import com.gunmm.impl.JianceCheckImpl;
import com.gunmm.model.DictionaryModel;
import com.gunmm.model.JianceCheck;
import com.gunmm.utils.JSONUtils;

@Controller
public class JianceController {
	@RequestMapping("/getJianceType")
	@ResponseBody
	private JSONObject getJianceType(HttpServletRequest request) {
		DictionaryDao dictionaryDao = new DictionaryImpl();
		List<DictionaryModel> dictionaryList = dictionaryDao.getDictionaryListByName("检测类型");
		return JSONUtils.responseToJsonString("1", "", "查询成功！", dictionaryList);
	}

	@RequestMapping("/addCheck")
	@ResponseBody
	private JSONObject addCheck(HttpServletRequest request) {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			httpServletRequest.setCharacterEncoding("utf-8");
			byte[] data = JSONUtils.readInputStream(httpServletRequest);
			JSONObject body = JSONUtils.getBody(data);
			JianceCheck jianceCheck = new JianceCheck();
			jianceCheck = JSONObject.parseObject(body.toJSONString(), JianceCheck.class);
			if (jianceCheck != null) {
				jianceCheck.setStatus("0");
				JianceCheckDao jianceCheckDao = new JianceCheckImpl();
				return jianceCheckDao.addJianceCheck(jianceCheck);
			}
			return JSONUtils.responseToJsonString("0", "", "jianceCheck is null！", "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "操作失败！", "");
		}
	}
}
