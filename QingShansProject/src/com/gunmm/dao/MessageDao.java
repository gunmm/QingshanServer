package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.MessageModel;

public interface MessageDao {

	
	//查询消息列表
	public List<MessageModel> getMessageListByUserId(String userId, String page, String rows);
	
	//添加消息记录
	public JSONObject addMessage(MessageModel messageModel);
	
	
	//更新消息信息
	public JSONObject updateMessageInfo(MessageModel messageModel);
	
	// 根据订单ID拿Message
	public MessageModel getMessageById(String messageId);
	
	//查询未读消息数
	public JSONObject queryUnreadMessageCount(String userId);

	
}
