package com.gunmm.impl;

import java.util.Date;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.ComplainDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.Complain;
import com.gunmm.responseModel.ComplainResModel;
import com.gunmm.utils.JSONUtils;

public class ComplainImpl implements ComplainDao {

	// 新建投诉
	public JSONObject addComplainComplain(Complain complain) {
		complain.setRecordId(UUID.randomUUID().toString());
		complain.setCreateTime(new Date());
		complain.setUpdateTime(new Date());
		complain.setManageStatus("0");
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(complain);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "添加成功！", complain.getRecordId());
		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "操作失败！", "");
		} finally {

			if (tx != null) {
				tx = null;
			}
		}
	}

	// 根据站点id拿投诉信息
	public Complain getComplainById(String complainId) {
		Transaction tx = null;
		Complain complain = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from Complain where recordId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, complainId);
			complain = (Complain) query.uniqueResult();
			tx.commit();

			return complain;

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return null;
		} finally {

			if (tx != null) {
				tx = null;
			}
		}
	}

	// 更新投诉
	public JSONObject updateComplainInfo(Complain complain) {
		complain.setUpdateTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(complain);
			tx.commit();

			return JSONUtils.responseToJsonString("1", "", "更新信息成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "更新信息失败！", "");
		} finally {

			if (tx != null) {
				tx = null;
			}
		}
	}

	// 根据投诉id查询相关详情信息
	public JSONObject getComplainDetailById(String complainId, String type) {
		ComplainResModel complainResModel = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String complainType = "";
			String complainedMan = "";
			if ("1".equals(type)) { //货主投诉
				complainType = "siteComplaintId";
				complainedMan = "driverId";
			}else {
				complainType = "driverComplaintId";
				complainedMan = "createManId";
			}
			sql = "SELECT complain.*,`order`.status as orderStatus,`order`.price,`order`.servicePrice,`order`.distance,"
					+"(SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = complain.createManId) as createManName,"
					+"(SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = complain.manageMan) as manageManName,"
					+"(SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = `order`."+complainedMan+") as complainedManName,"
					+"`order`."+complainedMan+" as complainedManId "
					+ "FROM complain,`order`  "
					+ "where complain.recordId = '" + complainId + "' and `order`."+complainType+" = '" + complainId + "'";

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(ComplainResModel.class);
			complainResModel = (ComplainResModel) query.uniqueResult();

			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "查询成功！", complainResModel);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "查询失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}
}
