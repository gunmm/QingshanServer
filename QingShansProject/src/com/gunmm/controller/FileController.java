package com.gunmm.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.utils.FileUtil;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class FileController {

	//上传图片
	@RequestMapping("/uploadImage")
	@ResponseBody
	private JSONObject getUserInfoById(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String imgStr = object.getString("imgStr");
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatStr = formatter.format(new Date());

		String str = request.getSession().getServletContext().getRealPath("/");
		String fileName = FileUtil.uploadBase64Img(imgStr, str+"/static/image"+
		File.separator+formatStr+File.separator);
		
		String urlStr = "static/image/" + formatStr +"/"+ fileName;
		if (fileName == null) {
			return JSONUtils.responseToJsonString("0", "", "上传失败！", "");
		}else {
			return JSONUtils.responseToJsonString("1", "", "上传成功！", urlStr);

		}
		
	}
}
