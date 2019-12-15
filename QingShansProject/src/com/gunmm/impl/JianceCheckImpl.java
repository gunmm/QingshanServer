package com.gunmm.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.JianceCheckDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.JianceCheck;
import com.gunmm.responseModel.JianceListModel;
import com.gunmm.utils.JSONUtils;

public class JianceCheckImpl implements JianceCheckDao {

	@Override
	public JSONObject addJianceCheck(JianceCheck jianceCheck) {
		// TODO Auto-generated method stub
		jianceCheck.setRecordId(UUID.randomUUID().toString());
		jianceCheck.setCreateTime(new Date());
		jianceCheck.setUpdataTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(jianceCheck);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "操作成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "addJianceCheck操作失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@Override
	public JSONObject updateJianceCheck(JianceCheck jianceCheck) {
		// TODO Auto-generated method stub
		jianceCheck.setUpdataTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(jianceCheck);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "操作成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "updateJianceCheck操作失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@Override
	public JianceCheck getJianceCheckById(String recordId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		JianceCheck jianceCheck = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from JianceCheck where recordId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, recordId);
			jianceCheck = (JianceCheck) query.uniqueResult();
			tx.commit();
			return jianceCheck;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<JianceListModel> getJianceCheckList(String page, String rows, String masterPhone, String carPlateNumber,
			String isDaiBan) {
		// TODO Auto-generated method stub
		List<JianceListModel> jianceCheckList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String sql1 = "SELECT JianceCheck.*," 
					+ "(select valueText from DictionaryModel where name = '检测类型' and keyText = JianceCheck.plateNumberType limit 1) AS plateNumberString "
				     + "FROM JianceCheck "
				     + "where JianceCheck.masterPhone like '%"+masterPhone+"%' and JianceCheck.plateNumber like '%"+carPlateNumber+"%' ";
			String sql2 = "";
			if(isDaiBan.length() > 0) {
				sql2 = "and (JianceCheck.status = '0') ";
			}
			
			String sql3 = "ORDER BY updataTime desc "
				     + "LIMIT " + page + "," + rows;
			sql = sql1 + sql2 +sql3;
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(JianceListModel.class);

			jianceCheckList = query.list();
			tx.commit();
			
			return jianceCheckList;

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			
			return jianceCheckList;
		} finally {
			
			if (tx != null) {
				tx = null;
			}

		}
	}

	@Override
	public Long getJianceCheckCount(String masterPhone, String carPlateNumber, String isDaiBan) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		Long siteCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String sql1 = "select count(*) from JianceCheck " 
				     + "where JianceCheck.masterPhone like '%"+masterPhone+"%' and JianceCheck.plateNumber like '%"+carPlateNumber+"%' ";
			String sql2 = "";
			if(isDaiBan.length() > 0) {
				sql2 = "and (JianceCheck.status = '0') ";
			}
			
			sql = sql1 + sql2;
			
			SQLQuery query = session.createSQLQuery(sql);
			siteCount = ((BigInteger)query.uniqueResult()).longValue();

			tx.commit();
			return siteCount;

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return siteCount;
		} finally {
			
			if (tx != null) {
				tx = null;
			}
		}
	}

}
