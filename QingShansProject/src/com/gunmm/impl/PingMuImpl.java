package com.gunmm.impl;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.PingMuUserDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.PingMuUser;
import com.gunmm.utils.JSONUtils;

public class PingMuImpl implements PingMuUserDao {

	@Override
	public JSONObject addUser(PingMuUser pingMuUser) {
		// TODO Auto-generated method stub
		pingMuUser.setCreateTime(new Date());
		pingMuUser.setUpdataTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(pingMuUser);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "操作成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "addUser PingMuImpl操作失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@Override
	public JSONObject updateUser(PingMuUser pingMuUser) {
		// TODO Auto-generated method stub
		pingMuUser.setUpdataTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(pingMuUser);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "操作成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "updateUserImp操作失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@Override
	public PingMuUser getUserById(String deviceId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		PingMuUser pingMuUser = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from PingMuUser where deviceId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, deviceId);
			pingMuUser = (PingMuUser) query.uniqueResult();
			tx.commit();

			return pingMuUser;

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
	public String getBaiduString() {
		// TODO Auto-generated method stub
		Transaction tx = null;
		String valueText = "0";
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select valueText from DictionaryModel where name = '百度类型' and keyText = '99'";
			Query query = session.createQuery(hql);
			valueText = (String) query.uniqueResult();

			tx.commit();
			return valueText;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "0";
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

}
