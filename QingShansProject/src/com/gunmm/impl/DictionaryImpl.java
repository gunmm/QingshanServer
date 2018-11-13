package com.gunmm.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gunmm.dao.DictionaryDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.DictionaryModel;

public class DictionaryImpl implements DictionaryDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<DictionaryModel> getDictionaryListByName(String name, String cityName) {
		// TODO Auto-generated method stub
		List<DictionaryModel> dictionaryList = null;
		Transaction tx = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from DictionaryModel where name = '" + name + "' and cityName = '" + cityName + "' order by CAST(keyText as integer) asc";
			Query query = session.createQuery(hql);
			dictionaryList = query.list();

			tx.commit();
			return dictionaryList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return dictionaryList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	@Override
	public String getValueTextByNameAndkey(String name, String keyText) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		String valueText = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select valueText from DictionaryModel where name = '" + name + "' and keyText = '" + keyText + "'";
			Query query = session.createQuery(hql);
			valueText = (String) query.uniqueResult();

			tx.commit();
			return valueText;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return valueText;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 据订单的车辆类型和发货城市查询对应的价格model
	public DictionaryModel getPriceDicByNameAndVehicleType(String vehicleType, String cityName) {
		DictionaryModel dictionaryModel = null;
		Transaction tx = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from DictionaryModel where keyText = '" + vehicleType + "' and cityName = '" + cityName + "'";

			Query query = session.createQuery(hql);
			dictionaryModel = (DictionaryModel) query.uniqueResult();

			tx.commit();
			return dictionaryModel;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return dictionaryModel;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

}
