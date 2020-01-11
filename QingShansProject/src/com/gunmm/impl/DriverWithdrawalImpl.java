package com.gunmm.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.DriverWithdrawalDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.DriverWithdrawal;
import com.gunmm.responseModel.DriverWithdrawalListModel;
import com.gunmm.utils.JSONUtils;

public class DriverWithdrawalImpl implements DriverWithdrawalDao {

	// 新建司机提现记录
	public JSONObject addDriverWithdrawal(DriverWithdrawal driverWithdrawal) {
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(driverWithdrawal);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "操作成功！", driverWithdrawal.getDriverWithdrawalId());
		} catch (Exception e) {
			// TODO: handle exception
			if (null != tx && tx.isActive()) {
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

	// 编辑司机提现记录
	public JSONObject editDriverWithdrawal(DriverWithdrawal driverWithdrawal) {
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(driverWithdrawal);
			tx.commit();

			return JSONUtils.responseToJsonString("1", "", "更新信息成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			if (null != tx && tx.isActive()) {
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

	// 获取司机提现记录by id
	public DriverWithdrawal getDriverWithdrawalById(String driverWithdrawalId) {
		Transaction tx = null;
		DriverWithdrawal driverWithdrawal = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from DriverWithdrawal where driverWithdrawalId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, driverWithdrawalId);
			driverWithdrawal = (DriverWithdrawal) query.uniqueResult();

			tx.commit();
			return driverWithdrawal;

		} catch (Exception e) {
			// TODO: handle exception
			if (null != tx && tx.isActive()) {
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

	// 删除司机提现记录
	public JSONObject deleteDriverWithdrawal(DriverWithdrawal driverWithdrawal) {
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.delete(driverWithdrawal);
			tx.commit();

			if (driverWithdrawal != null) {
				return JSONUtils.responseToJsonString("1", "", "删除成功！", "");
			}
			return JSONUtils.responseToJsonString("0", "对应记录不存在", "删除失败！", "");

		} catch (Exception e) {
			// TODO: handle exception
			if (null != tx && tx.isActive()) {
				try {
					tx.rollback();
				} catch (Exception re) {
					// use logging framework here
					re.printStackTrace();
				}
			}
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "删除失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询司机提现记录列表
	@SuppressWarnings("unchecked")
	public List<DriverWithdrawalListModel> getDriverWithdrawalList(String driverId, String page, String rows,
			String toUserName, String bankCardNumber) {
		List<DriverWithdrawalListModel> driverWithdrawalListModel = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			String sql1 = "select *,"
					+ "(SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = driverWithdrawal.toUserId) AS toUserName,"
					+ "(SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = driverWithdrawal.oprationUserId) AS oprationUserName "
					+ "FROM driverWithdrawal WHERE driverWithdrawal.toBankNumber like '%" + bankCardNumber
					+ "%' AND (SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = driverWithdrawal.toUserId) like '%"
					+ toUserName + "%' ";

			String sql2 = "";
			if (driverId != null) {
				if (driverId.length() > 0) {
					sql2 = "AND toUserId = '" + driverId + "' ";
				}
			}
			String sql3 = "ORDER BY driverWithdrawalTime desc " + "LIMIT " + page + "," + rows;

			sql = sql1 + sql2 + sql3;
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(DriverWithdrawalListModel.class);

			driverWithdrawalListModel = query.list();

			tx.commit();
			return driverWithdrawalListModel;

		} catch (Exception e) {
			// TODO: handle exception
			if (null != tx && tx.isActive()) {
				try {
					tx.rollback();
				} catch (Exception re) {
					// use logging framework here
					re.printStackTrace();
				}
			}
			e.printStackTrace();
			return driverWithdrawalListModel;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询司机提现记录列表总条数
	public Long getDriverWithdrawalListCount(String driverId, String toUserName, String bankCardNumber) {
		Transaction tx = null;
		Long orderCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			String sql1 = "select count(*) FROM driverWithdrawal WHERE driverWithdrawal.toBankNumber like '%"
					+ bankCardNumber
					+ "%' AND (SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = driverWithdrawal.toUserId) like '%"
					+ toUserName + "%' ";

			String sql2 = "";
			if (driverId != null) {
				if (driverId.length() > 0) {
					sql2 = "AND toUserId = '" + driverId + "'";
				}
			}
			sql = sql1 + sql2;

			SQLQuery query = session.createSQLQuery(sql);
			orderCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return orderCount;

		} catch (Exception e) {
			// TODO: handle exception
			if (null != tx && tx.isActive()) {
				try {
					tx.rollback();
				} catch (Exception re) {
					// use logging framework here
					re.printStackTrace();
				}
			}
			e.printStackTrace();
			return orderCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}
}
