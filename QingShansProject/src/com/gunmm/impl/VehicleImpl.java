package com.gunmm.impl;

import java.util.Date;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.VehicleDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.Vehicle;
import com.gunmm.utils.JSONUtils;

public class VehicleImpl implements VehicleDao {

	@Override
	public JSONObject addVehicle(Vehicle vehicle) {
		// TODO Auto-generated method stub
		vehicle.setVehicleId(UUID.randomUUID().toString());
		vehicle.setCreateTime(new Date());
		vehicle.setUpdateTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(vehicle);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "添加成功！", vehicle.getVehicleId());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "添加失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@Override
	public JSONObject updateVehicleInfo(Vehicle vehicle) {
		// TODO Auto-generated method stub
		vehicle.setUpdateTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(vehicle);
			tx.commit();

			return JSONUtils.responseToJsonString("1", "", "更新信息成功！", vehicle);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "更新信息失败！", vehicle);
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

}
