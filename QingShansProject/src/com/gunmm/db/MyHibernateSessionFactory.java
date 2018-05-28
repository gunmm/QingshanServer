package com.gunmm.db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class MyHibernateSessionFactory {
	private static SessionFactory sessionFactory;

	private MyHibernateSessionFactory() {

	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			//创建配置对象
			Configuration config = new Configuration().configure();
			
			//创建服务注册对象
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
					.build();
			//创建会话工厂对象
			sessionFactory = config.buildSessionFactory(serviceRegistry);
			return sessionFactory;
		} else {
			return sessionFactory;
		}

	}
}
