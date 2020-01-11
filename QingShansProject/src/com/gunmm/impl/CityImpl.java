package com.gunmm.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gunmm.dao.CityDao;
import com.gunmm.db.MyHibernateSessionFactory;

public class CityImpl implements CityDao {

	@Override
	public String getPrvinceByid(String prvinceId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		String provinceName = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select name from City where id = ? and deep = 1";
			Query query = session.createQuery(hql);
			query.setParameter(0, prvinceId);
			provinceName = (String) query.uniqueResult();

			tx.commit();
			return provinceName;
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
			return provinceName;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@Override
	public String getCityByid(String prvinceId, String cityId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		String cityName = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select name from City where id = ? and parent_id = ? and deep = 2";
			Query query = session.createQuery(hql);
			query.setParameter(0, cityId);
			query.setParameter(1, prvinceId);

			cityName = (String) query.uniqueResult();

			tx.commit();
			return cityName;
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
			return cityName;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

}
