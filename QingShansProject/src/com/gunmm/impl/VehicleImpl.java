package com.gunmm.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.VehicleDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.Vehicle;
import com.gunmm.responseModel.VehicleListModel;
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
			if (null != tx && tx.isActive()) {
				try {
					tx.rollback();
				} catch (Exception re) {
					// use logging framework here
					re.printStackTrace();
				}
			}
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "添加失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 根据id取车辆
	public Vehicle getVehicleById(String vehicleId) {
		Transaction tx = null;
		Vehicle vehicle = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from Vehicle where vehicleId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, vehicleId);
			vehicle = (Vehicle) query.uniqueResult();

			tx.commit();
			return vehicle;

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

	// 根据bindingDriverId取车辆
	public Vehicle getVehicleByBindingDriverId(String bindingDriverId) {
		Transaction tx = null;
		Vehicle vehicle = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from Vehicle where bindingDriverId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, bindingDriverId);
			vehicle = (Vehicle) query.uniqueResult();

			tx.commit();
			return vehicle;

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

	// 删除车辆
	public JSONObject deleteVehicle(String vehicleId) {
		Transaction tx = null;
		Vehicle vehicle = getVehicleById(vehicleId);
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.delete(vehicle);
			tx.commit();

			if (vehicle != null) {
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
			if (null != tx && tx.isActive()) {
				try {
					tx.rollback();
				} catch (Exception re) {
					// use logging framework here
					re.printStackTrace();
				}
			}
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "更新信息失败！", vehicle);
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询地图车辆列表
	@SuppressWarnings("unchecked")
	public List<VehicleListModel> getVehicleListBySiteId(String siteId, String phoneNumber, String plateNumber) {
		List<VehicleListModel> vehicleList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String sql1 = "SELECT vehicle.*,user.userId " + "FROM user,vehicle "
					+ "where user.userId = vehicle.bindingDriverId ";

			String sql2 = "";
			if (siteId != null) {
				if (siteId.length() > 0) {
					sql2 = "and user.belongSiteId = '" + siteId + "' ";
				}

			}
			String sql3 = "";
			if (phoneNumber != null) {
				if (phoneNumber.length() > 0) {
					sql3 = "and user.phoneNumber like '%" + phoneNumber + "%' ";
				}

			}

			String sql4 = "";
			if (plateNumber != null) {
				if (plateNumber.length() > 0) {
					sql4 = "and vehicle.plateNumber like '%" + plateNumber + "%' ";
				}

			}

			sql = sql1 + sql2 + sql3 + sql4;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(VehicleListModel.class);

			vehicleList = query.list();

			tx.commit();
			return vehicleList;

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
			return vehicleList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 判断车牌号是否被注册
	@SuppressWarnings("unused")
	public boolean judgeVehicleByPlateNumber(String plateNumber) {
		Transaction tx = null;
		String backPlateNumber = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select plateNumber from Vehicle where plateNumber = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, plateNumber);
			backPlateNumber = (String) query.uniqueResult();

			tx.commit();
			if (backPlateNumber == null || "".equals(backPlateNumber)) {
				return false;
			} else {
				return true;
			}

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
			return true;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 判断营运证号是否被注册
	@SuppressWarnings("unused")
	public boolean judgeVehicleByOperationCertificateNumber(String operationCertificateNumber) {
		Transaction tx = null;
		String backoperationCertificateNumber = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select plateNumber from Vehicle where operationCertificateNumber = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, operationCertificateNumber);
			backoperationCertificateNumber = (String) query.uniqueResult();

			tx.commit();
			if (backoperationCertificateNumber == null || "".equals(backoperationCertificateNumber)) {
				return false;
			} else {
				return true;
			}

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
			return true;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 判断行驶证证号是否被注册
	public boolean judgeVehicleByDrivingCardNumber(String drivingCardNumber) {
		Transaction tx = null;
		String backDrivingCardNumber = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select plateNumber from Vehicle where drivingCardNumber = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, drivingCardNumber);
			backDrivingCardNumber = (String) query.uniqueResult();

			tx.commit();
			if (backDrivingCardNumber == null || "".equals(backDrivingCardNumber)) {
				return false;
			} else {
				return true;
			}

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
			return true;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 判断车辆登记证证号是否被注册
	public boolean judgeVehicleByVehicleRegistrationNumber(String vehicleRegistrationNumber) {
		Transaction tx = null;
		String backVehicleRegistrationNumber = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select plateNumber from Vehicle where vehicleRegistrationNumber = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, vehicleRegistrationNumber);
			backVehicleRegistrationNumber = (String) query.uniqueResult();

			tx.commit();
			if (backVehicleRegistrationNumber == null || "".equals(backVehicleRegistrationNumber)) {
				return false;
			} else {
				return true;
			}

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
			return true;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

}
