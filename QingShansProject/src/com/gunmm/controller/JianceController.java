package com.gunmm.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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
			String openId = body.getString("openId");
			JianceCheck jianceCheck = new JianceCheck();
			jianceCheck = JSONObject.parseObject(body.toJSONString(), JianceCheck.class);
			if (jianceCheck != null) {
				jianceCheck.setStatus("0");
				JianceCheckDao jianceCheckDao = new JianceCheckImpl();
				JSONObject jsonObj = jianceCheckDao.addJianceCheck(jianceCheck);
				String result_code = jsonObj.getString("result_code");

				if ("1".equals(result_code) && openId != null) {
					JSONObject dataObject = new JSONObject();

					JSONObject firstObject = new JSONObject();
					firstObject.put("value", "尊敬的车主，您已预约成功");
					firstObject.put("color", "#FFFFFF");
					dataObject.put("first", firstObject);

					 JSONObject keyword1 = new JSONObject();
					 keyword1.put("value", jianceCheck.getPlateNumber());
					 keyword1.put("color", "#173177");
					 dataObject.put("keyword1", keyword1);
					
					 JSONObject keyword2 = new JSONObject();
					 keyword2.put("value", jianceCheck.getMasterName());
					 keyword2.put("color", "#173177");
					 dataObject.put("keyword2", keyword2);
					
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					 String str = sdf.format(jianceCheck.getCheckTime());
					 JSONObject keyword3 = new JSONObject();
					 keyword3.put("value", str);
					 keyword3.put("color", "#173177");
					 dataObject.put("keyword3", keyword3);

					JSONObject keyword4 = new JSONObject();
					keyword4.put("value", "渭南庆山监测站");
					keyword4.put("color", "#173177");
					dataObject.put("keyword4", keyword4);

					JSONObject keyword5 = new JSONObject();
					keyword5.put("value", "暂无");
					keyword5.put("color", "#173177");
					dataObject.put("keyword5", keyword5);

					JSONObject remark = new JSONObject();
					remark.put("value", "本站竭诚欢迎您的光临，为了能够给您提供更优质的服务，请在预约时间前到达本站办理相关手续，详情致电13571320777");
					remark.put("color", "#173177");
					dataObject.put("remark", remark);

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("touser", openId);
					jsonObject.put("template_id", "EWZDsNGm5xnMmusF1CT96DpMxtzIFSd3vTL1dPmzPhg");
					jsonObject.put("url", "https://www.amap.com/search?id=BV10944051&city=610502&geoobj=116.24873%7C39.816441%7C116.671703%7C39.998683&query_type=IDQ&query=%E5%BA%86%E5%B1%B1%E9%9B%86%E5%9B%A2(%E5%85%AC%E4%BA%A4%E7%AB%99)&zoom=12");
					jsonObject.put("topcolor", "#FF0000");
					jsonObject.put("data", dataObject);
					JSONObject tokenObject = getQuery("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxefda176107cb06b8&secret=05aa2b9c4177d4174c37a2d623fdaada", "");
					String access_token = tokenObject.getString("access_token");
					if (access_token != null) {
						post("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + access_token,
								jsonObject.toJSONString());
					}
				}
				return jsonObj;
			}
			return JSONUtils.responseToJsonString("0", "", "jianceCheck is null！", "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "操作失败！", "");
		}
	}

	private String post(String strURL, String params) {
		BufferedReader reader = null;
		try {
			URL url = new URL(strURL);// 创建连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
			connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
			connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
			connection.connect();
			// 一定要用BufferedReader 来接收响应， 使用字节来接收响应的方法是接收不到内容的
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
			out.append(params);

			out.flush();
			out.close();
			// 读取响应
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			String res = "";
			while ((line = reader.readLine()) != null) {
				res += line;
			}
			reader.close();

			return res;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error"; // 自定义错误信息
	}

	// 获取url
	@RequestMapping("/getWXUrl")
	@ResponseBody
	private JSONObject getWXUrl(HttpServletRequest request) {
		String redirect_uri = "";
		try {
			redirect_uri = URLEncoder.encode("http://www.qingshanjituan.cn/jiance/jianceAdd.html", "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			redirect_uri = "";
		}
		String authorUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxefda176107cb06b8&redirect_uri="
				+ redirect_uri + "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
		return JSONUtils.responseToJsonString("1", "", "查询成功！", authorUrl);
	}

	// 获取getWXPara
	@RequestMapping("/getWXPara")
	@ResponseBody
	private JSONObject getWXPara(HttpServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		try {
			httpServletRequest.setCharacterEncoding("utf-8");
			byte[] data = JSONUtils.readInputStream(httpServletRequest);
			JSONObject body = JSONUtils.getBody(data);
			String code = body.getString("code");
			String res = "";
			JSONObject resultObject = null;
			StringBuffer buffer = new StringBuffer();
			try {
				URL url = new URL(
						"https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxefda176107cb06b8&secret=05aa2b9c4177d4174c37a2d623fdaada&code="
								+ code + "&grant_type=authorization_code");
				HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
				System.out.println(urlCon.getResponseCode());
				if (200 == urlCon.getResponseCode()) {
					InputStream is = urlCon.getInputStream();
					InputStreamReader isr = new InputStreamReader(is, "utf-8");
					BufferedReader br = new BufferedReader(isr);
					String str = null;
					while ((str = br.readLine()) != null) {
						buffer.append(str);
					}
					br.close();
					isr.close();
					is.close();
					res = buffer.toString();
					resultObject = JSONObject.parseObject(res);
				} else {
					throw new Exception("连接失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return JSONUtils.responseToJsonString("1", "", "查询成功！", resultObject);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "操作失败！", "");

		}
	}

	private JSONObject getQuery(String strURL, String params) {
		BufferedReader reader = null;
		try {
			URL url = new URL(strURL);// 创建连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("GET"); // 设置请求方式
			connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
			connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
			connection.connect();
			// 一定要用BufferedReader 来接收响应， 使用字节来接收响应的方法是接收不到内容的
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
			out.append(params);

			out.flush();
			out.close();
			// 读取响应
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			String res = "";
			while ((line = reader.readLine()) != null) {
				res += line;
			}
			reader.close();

			JSONObject resultObject = JSONObject.parseObject(res);
			return resultObject;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONObject();
	}

}
