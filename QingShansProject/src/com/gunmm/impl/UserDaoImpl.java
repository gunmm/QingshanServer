package com.gunmm.impl;

import java.util.Date;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.UserDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.User;
import com.gunmm.utils.JSONUtils;

public class UserDaoImpl implements UserDao {

	//注册
	@Override
	public JSONObject addUser(User user) {
		// TODO Auto-generated method stub
		if(judgeUserByPhone(user.getPhoneNumber())) {
			return JSONUtils.responseToJsonString("0", "", "电话号码已存在！", "");
		}
		
		user.setUserId(UUID.randomUUID().toString()); 

		user.setCreateTime(new Date());
		user.setScore(5.0);
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "注册成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "注册失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}
	
	//判断手机号是否已经注册
	private boolean judgeUserByPhone(String phoneNumber) {
		Transaction tx = null;
		String backPhoneNumber = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			hql = "select phoneNumber from User where phoneNumber = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, phoneNumber);
			backPhoneNumber = (String) query.uniqueResult();
			
			tx.commit();
			if (backPhoneNumber == null || "".equals(backPhoneNumber)) {
				return false;
			}else {
				return true;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return true;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	//发送验证码
	@Override
	public JSONObject getVerificationCode(String phoneNumber) {
		// TODO Auto-generated method stub
		return JSONUtils.responseToJsonString("1", "", "验证码发送成功！", "123456");
	}

	
	//登陆
	@Override
	public JSONObject login(String phoneNumber, String password) {
		
		if(!judgeUserByPhone(phoneNumber)) {
			return JSONUtils.responseToJsonString("0", "电话号码未注册！", "电话号码未注册！", "");
		}
		Transaction tx = null;
		User user = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			hql = "from User where phoneNumber = ? and password = ? ";
			Query query = session.createQuery(hql);
			query.setParameter(0, phoneNumber);
			query.setParameter(1, password);
			user = (User) query.uniqueResult();
			
			tx.commit();
			if (user == null) {
				return JSONUtils.responseToJsonString("0", "密码错误！", "密码错误！", "");
			}else {
				setUserLoginTime(user);
				return JSONUtils.responseToJsonString("1", "", "登陆成功！", user);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "登陆失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}		
	}
	
	private void setUserLoginTime(User user) {
		// TODO Auto-generated method stub
		user.setLastLoginTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@Override
	public User getUserById(String userId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		User user = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			hql = "from User where userId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, userId);
			user = (User) query.uniqueResult();
			
			tx.commit();
			return user;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@Override
	public void setUserStatus(String status, User user) {
		// TODO Auto-generated method stub
		user.setStatus(status);
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}
	

}
