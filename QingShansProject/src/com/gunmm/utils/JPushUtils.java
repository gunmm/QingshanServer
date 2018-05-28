package com.gunmm.utils;


import java.util.List;
import java.util.Map;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;


public class JPushUtils {

	private static JPushClient  jPushClient;
	
	private static String  MASTER_SECRET = "98fe942caebcc6720890d684";
	private static String  APP_KEY = "80be2f2358c278aa0f7def46";
	private static boolean  IS_PRODUCTION = false;




	


	public static JPushClient getJPushClient() {
		if (jPushClient == null) {
			//创建配置对象
		    jPushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());
			return jPushClient;
		} else {
			return jPushClient;
		}

	}
	
	
	public static PushPayload buildPushPayLoad(List<String> receivers, String notifyType,String msg, String deviceType,Map<String,String> param){
		
		PushPayload result = null;
		if ("android".equals(deviceType)) {// android
			result = PushPayload.newBuilder().setPlatform(Platform.android()).setAudience(Audience.alias(receivers))
					.setMessage(Message.newBuilder().setMsgContent(msg).addExtra("notifyType", notifyType)
							.addExtras(param).build())
					.setOptions(Options.newBuilder().setTimeToLive(120).build()).build();
		} else if ("iOS".equals(deviceType)) {// ios
			result = PushPayload.newBuilder().setPlatform(Platform.ios())
					.setAudience(Audience.alias(receivers))
					.setNotification(Notification.newBuilder()
							.addPlatformNotification(IosNotification.newBuilder().setAlert(msg)
									.addExtra("notifyType", notifyType).addExtras(param).setBadge(0)
									// .autoBadge()
									.setSound("default").build())
							.build())
					.setOptions(Options.newBuilder().setApnsProduction(IS_PRODUCTION).build()).build();

		}
		return result;
	   }


	
	
}
