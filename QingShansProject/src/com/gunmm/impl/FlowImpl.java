package com.gunmm.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gunmm.dao.FlowDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.Flow;

public class FlowImpl implements FlowDao {

	// 新建flow
	public void addFlow(Flow flow) {
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(flow);
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

	// 更新flow
	public void updateFlow(Flow flow) {
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(flow);
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
	
	//根据订单Id查询所有相关flow
    @SuppressWarnings("unchecked")
	public List<Flow> getFlowListByOrderId(String orderId, String driverId) {
    	List<Flow> flowList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "select * " 
			    + "from flow "
			    + "where flow.orderId = '"+ orderId +"' and flow.driverId = '"+ driverId +"'";
					

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(Flow.class);

			flowList = query.list();

			tx.commit();
			return flowList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return flowList;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
    }

	// 根据订单Id将所有相关flow状态修改
	public void setFlowStatusByOrderId(String orderId, String status) {
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "update flow " + 
				  "set flow.flowStatus = '"+ status +"' " + 
				  "where flow.orderId = '"+ orderId +"' and flow.flowStatus = '0'";
			SQLQuery query = session.createSQLQuery(sql);
			query.executeUpdate();
			
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

	// 根据司机Id查询flow
	public Flow getFlowByDriverId(String driverId, String orderId) {
		Transaction tx = null;
		Flow flow = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from Flow where driverId = ? and orderId = ? and flowStatus = '0'";
			Query query = session.createQuery(hql);
			query.setParameter(0, driverId);
			query.setParameter(1, orderId);
			flow = (Flow) query.uniqueResult();

			tx.commit();
			return flow;

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
}
