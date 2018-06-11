package com.gunmm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.MessageDao;
import com.gunmm.impl.MessageImpl;
import com.gunmm.model.MessageModel;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class MessageController {

	// 查询
	@RequestMapping("/getMessageList")
	@ResponseBody
	private JSONObject getMessageList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String userId = object.getString("userId");
		String rows = object.getString("rows");

		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		if(userId == null) {
			return JSONUtils.responseToJsonString("0", "参数错误！", "查询失败！", "");
		}

		MessageDao messageDao = new MessageImpl();
		List<MessageModel> messageList = messageDao.getMessageListByUserId(userId, page, rows);		
		return JSONUtils.responseToJsonString("1", "", "查询成功！", messageList);
	}
	
	//设置已读
	@RequestMapping("/setMessageRead")
	@ResponseBody
	private JSONObject setMessageRead(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String messageId = object.getString("messageId");

		MessageDao messageDao = new MessageImpl();
		MessageModel messageModel = messageDao.getMessageById(messageId);
		messageModel.setIsRead("1");
		
		return messageDao.updateMessageInfo(messageModel);
	}
	
	
	//查询未读消息数
	@RequestMapping("/queryUnreadMessageCount")
	@ResponseBody
	private JSONObject queryUnreadMessageCount(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String userId = object.getString("userId");

		MessageDao messageDao = new MessageImpl();

		return messageDao.queryUnreadMessageCount(userId);
	}
	
}
