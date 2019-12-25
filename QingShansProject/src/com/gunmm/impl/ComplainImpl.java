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
			if (null != tx) {
				try {
					tx.rollback();
				} catch (Exception re) {
					// use logging framework here
					re.printStackTrace();
				}
			}
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
			if (null != tx) {
				try {
					tx.rollback();
				} catch (Exception re) {
					// use logging framework here
					re.printStackTrace();
				}
			}
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
			if (null != tx) {
				try {
					tx.rollback();
				} catch (Exception re) {
					// use logging framework here
					re.printStackTrace();
				}
			}
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
			if ("1".equals(type)) { // 货主投诉
				complainType = "siteComplaintId";
				complainedMan = "driverId";
			} else {
				complainType = "driverComplaintId";
				complainedMan = "createManId";
			}
			sql = "SELECT complain.*,`order`.status as orderStatus,`order`.price,`order`.servicePrice,`order`.distance,"
					+ "(SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = complain.createManId) as createManName,"
					+ "(SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = complain.manageMan) as manageManName,"
					+ "(SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = `order`." + complainedMan
					+ ") as complainedManName," + "`order`." + complainedMan + " as complainedManId "
					+ "FROM complain,`order`  " + "where complain.recordId = '" + complainId + "' and `order`."
					+ complainType + " = '" + complainId + "'";

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(ComplainResModel.class);
			complainResModel = (ComplainResModel) query.uniqueResult();

			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "查询成功！", complainResModel);

		} catch (Exception e) {
			// TODO: handle exception
			if (null != tx) {
				try {
					tx.rollback();
				} catch (Exception re) {
					// use logging framework here
					re.printStackTrace();
				}
			}
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "查询失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询投诉列表
	@SuppressWarnings("unchecked")
	public List<ComplainResModel> getComplainList(String type, String manageStatus, String page, String rows) {
		List<ComplainResModel> complainResModelList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT complain.*,`order`.status as orderStatus,`order`.price,`order`.servicePrice,`order`.distance,"
					+ "(SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = complain.createManId) as createManName,"
					+ "(SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = complain.manageMan) as manageManName,"

					+ "(" + "CASE "
					+ "WHEN (complain.type = '1') THEN (SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = `order`.driverId) "
					+ "ELSE (SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = `order`.createManId) END ) AS complainedManName,"

					+ "(" + "CASE " + "WHEN (complain.type = '1') THEN `order`.driverId "
					+ "ELSE `order`.createManId END ) AS complainedManId "

					+ "FROM complain,`order`  where (CASE WHEN (complain.type = '1') THEN `order`.siteComplaintId = complain.recordId ELSE `order`.driverComplaintId = complain.recordId END)"
					+ "AND complain.type like '%" + type + "%' AND complain.manageStatus like '%" + manageStatus + "%' "
					+ "ORDER BY updateTime desc " + "LIMIT " + page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(ComplainResModel.class);

			complainResModelList = query.list();

			tx.commit();
			return complainResModelList;

		} catch (Exception e) {
			// TODO: handle exception
			if (null != tx) {
				try {
					tx.rollback();
				} catch (Exception re) {
					// use logging framework here
					re.printStackTrace();
				}
			}
			e.printStackTrace();
			return complainResModelList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询投诉列表条数
	public Long getComplainListCount(String type, String manageStatus) {
		Transaction tx = null;
		Long complainCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT  count(*)"
					+ "FROM complain,`order`  where (CASE WHEN (complain.type = '1') THEN `order`.siteComplaintId = complain.recordId ELSE `order`.driverComplaintId = complain.recordId END)"
					+ "AND complain.type like '%" + type + "%' AND complain.manageStatus like '%" + manageStatus + "%'";

			SQLQuery query = session.createSQLQuery(sql);
			complainCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return complainCount;

		} catch (Exception e) {
			// TODO: handle exception
			if (null != tx) {
				try {
					tx.rollback();
				} catch (Exception re) {
					// use logging framework here
					re.printStackTrace();
				}
			}
			e.printStackTrace();
			return complainCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}
}
