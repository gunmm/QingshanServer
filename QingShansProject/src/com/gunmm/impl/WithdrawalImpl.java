package com.gunmm.impl;

import java.util.Date;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.WithdrawalDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.Withdrawal;
import com.gunmm.utils.JSONUtils;

public class WithdrawalImpl implements WithdrawalDao {

	// 新建提现记录
	public Withdrawal addWithdrawal(String dataStr, String toUserId, String oprationUserId) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setWithdrawalId(UUID.randomUUID().toString());
		withdrawal.setToUserId(toUserId);
		withdrawal.setOprationUserId(oprationUserId);
		withdrawal.setPeriodOfTime(dataStr);
		withdrawal.setWithdrawalTime(new Date());

		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(withdrawal);
			tx.commit();
			return withdrawal;
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

	// 编辑提现记录
	public JSONObject editWithdrawal(Withdrawal withdrawal) {
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(withdrawal);
			tx.commit();

			return JSONUtils.responseToJsonString("1", "", "更新信息成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "更新信息失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 获取提现记录by id
	public Withdrawal getWithdrawalById(String withdrawalId) {
		Transaction tx = null;
		Withdrawal withdrawal = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			hql = "from Withdrawal where withdrawalId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, withdrawalId);
			withdrawal = (Withdrawal) query.uniqueResult();
			
			tx.commit();
			return withdrawal;

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

	// 提现本月9号之前的订单 （九号之前创建，并且已经完成的订单）
	@Override
	public JSONObject withdrawalBeforeOrder(String dataStr, String withdrawalId) {
		// TODO Auto-generated method stub
		return null;
	}

}
