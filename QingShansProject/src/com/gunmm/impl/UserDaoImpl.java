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
		user.setUpdateTime(new Date());
		user.setScore(5.0);
		user.setStatus("2");
//		user.setDriverCertificationStatus("0");
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
	public JSONObject getVerificationCode(String phoneNumber, String type) {
		// TODO Auto-generated method stub
		if ("0".equals(type)) {
			if(judgeUserByPhone(phoneNumber)) {
				return JSONUtils.responseToJsonString("0", "", "电话号码已存在！", "");
			}
		}else if ("1".equals(type)) {
			if(!judgeUserByPhone(phoneNumber)) {
				return JSONUtils.responseToJsonString("0", "", "电话号码未注册！", "");
			}
		}
		
		return JSONUtils.responseToJsonString("1", "", "验证码发送成功！", "123456");
	}

	
	//登陆
	@Override
	public JSONObject login(String phoneNumber, String password, String plateform) {
		
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
				//设置
				user.setLoginPlate(plateform);
				user.setLastLoginTime(new Date());
				user.setAccessToken(UUID.randomUUID().toString());
				updateUserInfo(user);
				user.setPassword(null);
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
	
	@Override
	public JSONObject resetPassword(String phoneNumber, String password) {
		// TODO Auto-generated method stub
		if(!judgeUserByPhone(phoneNumber)) {
			return JSONUtils.responseToJsonString("0", "电话号码未注册！", "电话号码未注册！", "");
		}
		Transaction tx = null;
		User user = getUserByPhone(phoneNumber);
		if (user == null) {
			return JSONUtils.responseToJsonString("0", "系统异常！", "根据已存在电话号码查询异常！", "");
		}
		user.setPassword(password);
		user.setUpdateTime(new Date());
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "修改密码成功！", "");

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
	public User getUserByPhone(String phoneNumber) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		User user = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			hql = "from User where phoneNumber = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, phoneNumber);
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
	public JSONObject updateUserInfo(User user) {
		// TODO Auto-generated method stub
		user.setUpdateTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();
			
			return JSONUtils.responseToJsonString("1", "", "更新信息成功！", user);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "更新信息失败！", user);
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

}
