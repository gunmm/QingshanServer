package com.gunmm.listener;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.gunmm.timer.PushNewOrderTimer;

public class TimeListener implements ServletContextListener {
	private Timer timer = null;

	public TimeListener() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		timer.cancel();
		sce.getServletContext().log("定时器销毁"); 
		ServletContextListener.super.contextDestroyed(sce);
	}
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		timer = new Timer(true);  

		sce.getServletContext().log("定时器已启动");
		
		timer.schedule(new PushNewOrderTimer(), 0, 5000);  

		sce.getServletContext().log("已经添加任务调度表"); 
		
		
		ServletContextListener.super.contextInitialized(sce);
	}

}
