package com.gunmm.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;



public class JSONUtils {
	public static JSONObject responseToJsonString(String runStatus, Object reason, String result, Object object) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result_code", runStatus);
		jsonObject.put("reason", reason);
		jsonObject.put("result", result);
		jsonObject.put("object", object);

		return jsonObject;
	}
	
	/**
	 * 获取输入流中的数据,不打印base64编码
	 * @Title: readInputStream
	 * @Description:
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(HttpServletRequest request) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        InputStream inStream = request.getInputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//网页的二进制数据
        outStream.flush();
        outStream.close();
        inStream.close();
        return data;
	}
	/**
	 * 获取报文头信息
	 * @param data
	 * @return
	 */
	public static JSONObject getHead(byte[] data){
		String dataString;
		try {
			dataString = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		JSONObject obj = JSONObject.parseObject(dataString);
		return JSONObject.parseObject(obj.getString("head"));
	}

	/**
	 * 获取body
	 * @param data
	 * @return
	 */
	public static JSONObject getBody(byte[] data){
		String dataString;
		try {
			dataString = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		JSONObject obj = JSONObject.parseObject(dataString);
		return JSONObject.parseObject(obj.getString("body"));
	}
}
