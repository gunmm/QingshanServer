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
import com.gunmm.dao.UserDao;
import com.gunmm.dao.VehicleDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.User;
import com.gunmm.responseModel.DriverListModel;
import com.gunmm.utils.JSONUtils;

public class UserDaoImpl implements UserDao {

	// 注册
	@Override
	public JSONObject addUser(User user) {
		// TODO Auto-generated method stub
		if (judgeUserByPhone(user.getPhoneNumber())) {
			return JSONUtils.responseToJsonString("0", "", "电话号码已存在！", "");
		}

		user.setUserId(UUID.randomUUID().toString());

		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		user.setScore(5.0);
		user.setStatus("2");
		// user.setDriverCertificationStatus("0");
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "注册成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "注册失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 发送验证码
	@Override
	public JSONObject getVerificationCode(String phoneNumber, String type) {
		// TODO Auto-generated method stub
		if ("0".equals(type)) {
			if (judgeUserByPhone(phoneNumber)) {
				return JSONUtils.responseToJsonString("0", "", "电话号码已存在！", "");
			}
		} else if ("1".equals(type)) {
			if (!judgeUserByPhone(phoneNumber)) {
				return JSONUtils.responseToJsonString("0", "", "电话号码未注册！", "");
			}
		}

		return JSONUtils.responseToJsonString("1", "", "验证码发送成功！", "123456");
	}

	// 登陆
	@Override
	public JSONObject login(String phoneNumber, String password, String plateform) {

		if (!judgeUserByPhone(phoneNumber)) {
			return JSONUtils.responseToJsonString("0", "电话号码未注册！", "电话号码未注册！", "");
		}
		Transaction tx = null;
		User user = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from User where phoneNumber = ? and password = ? ";
			Query query = session.createQuery(hql);
			query.setParameter(0, phoneNumber);
			query.setParameter(1, password);
			user = (User) query.uniqueResult();

			tx.commit();
			if (user == null) {
				return JSONUtils.responseToJsonString("0", "密码错误！", "密码错误！", "");
			} else {
				// 设置
				user.setLoginPlate(plateform);
				user.setLastLoginTime(new Date());
				user.setAccessToken(UUID.randomUUID().toString());
				updateUserInfo(user);
				user.setPassword(null);
				return JSONUtils.responseToJsonString("1", "", "登陆成功！", user);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "登陆失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@Override
	public JSONObject resetPassword(String phoneNumber, String password) {
		// TODO Auto-generated method stub
		if (!judgeUserByPhone(phoneNumber)) {
			return JSONUtils.responseToJsonString("0", "电话号码未注册！", "电话号码未注册！", "");
		}
		Transaction tx = null;
		User user = getUserByPhone(phoneNumber);
		if (user == null) {
			return JSONUtils.responseToJsonString("0", "系统异常！", "根据已存在电话号码查询异常！", "");
		}
		user.setPassword(password);
		user.setUpdateTime(new Date());
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "修改密码成功！", "");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "登陆失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@Override
	public User getUserById(String userId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		User user = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from User where userId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, userId);
			user = (User) query.uniqueResult();

			tx.commit();
			return user;

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

	@Override
	public User getUserByPhone(String phoneNumber) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		User user = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from User where phoneNumber = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, phoneNumber);
			user = (User) query.uniqueResult();

			tx.commit();
			return user;

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

	@Override
	public JSONObject updateUserInfo(User user) {
		// TODO Auto-generated method stub
		user.setUpdateTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();

			return JSONUtils.responseToJsonString("1", "", "更新信息成功！", user);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "更新信息失败！", user);
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询的司机列表
	@SuppressWarnings("unchecked")
	@Override
	public List<DriverListModel> getDriverListBySiteId(String page, String rows, String siteId) {
		// TODO Auto-generated method stub
		List<DriverListModel> driverList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String sql1 = "SELECT user.*,"
					+ "vehicle.gpsType,"
					+ "vehicle.gpsSerialNumber,"
					+ "vehicle.drivingCardNumber,"
					+ "vehicle.vehicleRegistrationNumber,"
					+ "vehicle.operationCertificateNumber,"
					+ "vehicle.insuranceNumber,"
					+ "vehicle.vehicleIdCardNumber,"
					+ "vehicle.businessLicenseNumber,"
					+ "vehicle.vehicleBrand,"
					+ "vehicle.vehicleModel,"
					+ "vehicle.vehiclePhoto,"
					+ "vehicle.loadWeight,"
					+ "vehicle.vehicleMakeDate,"
					
					+ "(select description from DictionaryModel where name = '车辆类型' and keyText = user.vehicleType) as vehicleTypeName,"
					+ "(select siteName from site where user.belongSiteId = siteId) as belongSiteName,"
					+ "(select plateNumber from vehicle where vehicleId = user.vehicleId) as plateNumber "
					+ "FROM user,vehicle " 
					+ "where user.vehicleId = vehicle.vehicleId and user.type = '6' ";
			String sql2 = "";
			if (siteId != null) {
				sql2 = "and user.belongSiteId = '" + siteId + "' ";
			}

			String sql3 = "ORDER BY updateTime desc " + "LIMIT " + page + "," + rows;
			sql = sql1 + sql2 + sql3;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(DriverListModel.class);

			driverList = query.list();

			tx.commit();
			return driverList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return driverList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询司机条数
	@Override
	public Long getDriverCount(String siteId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		Long driverCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String sql1 = "select count(*) from user where user.type = '6' ";
			String sql2 = "";
			if (siteId != null) {
				sql2 = "and user.belongSiteId = '" + siteId + "' ";
			}
			sql = sql1 + sql2;

			SQLQuery query = session.createSQLQuery(sql);
			driverCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return driverCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return driverCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 添加司机
	@Override
	public JSONObject addDriver(User user) {
		// TODO Auto-generated method stub
		user.setUserId(UUID.randomUUID().toString());

		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		user.setPassword("e10adc3949ba59abbe56e057f20f883e");
		user.setScore(5.0);
		user.setStatus("2");
		user.setType("6");

		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "注册成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "注册失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 删除司机
	@SuppressWarnings("unused")
	public JSONObject deleteDriver(String driverId) {
		Transaction tx = null;
		User driver = getUserById(driverId);
		VehicleDao vehicleDao = new VehicleImpl();
		vehicleDao.deleteVehicle(driver.getVehicleId());
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.delete(driver);
			tx.commit();

			if (driver != null) {
				return JSONUtils.responseToJsonString("1", "", "删除成功！", "");
			}
			return JSONUtils.responseToJsonString("0", "对应记录不存在", "删除失败！", "");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "删除失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 判断手机号是否已经注册
	public boolean judgeUserByPhone(String phoneNumber) {
		Transaction tx = null;
		String backPhoneNumber = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select phoneNumber from User where phoneNumber = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, phoneNumber);
			backPhoneNumber = (String) query.uniqueResult();

			tx.commit();
			if (backPhoneNumber == null || "".equals(backPhoneNumber)) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return true;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 判断身份证号是否已经注册
	@SuppressWarnings("unused")
	public boolean judgeUserIdCardNumber(String userIdCardNumber) {
		Transaction tx = null;
		String backIdCardNumber = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select userIdCardNumber from User where userIdCardNumber = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, userIdCardNumber);
			backIdCardNumber = (String) query.uniqueResult();

			tx.commit();
			if (backIdCardNumber == null || "".equals(backIdCardNumber)) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return true;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 判断驾驶证号是否已经注册
	@SuppressWarnings("unused")
	public boolean judgeDriverLicenseNumber(String driverLicenseNumber) {
		Transaction tx = null;
		String backDriverLicenseNumber = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select userIdCardNumber from User where driverLicenseNumber = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, driverLicenseNumber);
			backDriverLicenseNumber = (String) query.uniqueResult();

			tx.commit();
			if (backDriverLicenseNumber == null || "".equals(backDriverLicenseNumber)) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return true;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

}
