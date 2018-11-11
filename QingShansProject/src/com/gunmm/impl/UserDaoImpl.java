package com.gunmm.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.SiteDao;
import com.gunmm.dao.UserDao;
import com.gunmm.dao.VehicleDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.Site;
import com.gunmm.model.User;
import com.gunmm.responseModel.DriverListModel;
import com.gunmm.responseModel.ManageListModel;
import com.gunmm.responseModel.MasterListModel;
import com.gunmm.utils.ALMessageUtil;
import com.gunmm.utils.JSONUtils;

public class UserDaoImpl implements UserDao {

	// 获取accesstoken
	public String getaccessTokenById(String userId) {
		User user = getUserById(userId);
		if (user == null) {
			return "";
		}
		return user.getAccessToken();
	}

	// 注册管理员
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

		if ("6".equals(user.getType())) {
			user.setDriverType("2");
			user.setStatus("3");
		}

		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "注册成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
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
	public JSONObject getVerificationCode(String phoneNumber, String type, String msgModelType) {
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
		String verifyCode = String
                .valueOf(new Random().nextInt(899999) + 100000);//生成短信验证码
		ALMessageUtil util = new ALMessageUtil();
		boolean sendResult = false;
		if ("0".equals(msgModelType)) {
			sendResult = util.sendRegisterOrGetPasswordMsg(phoneNumber, verifyCode);
		}else if ("1".equals(msgModelType)) {
			sendResult = util.sendChangeBankNumberMsg(phoneNumber, verifyCode);
		}else if ("2".equals(msgModelType)) {
			sendResult = util.sendOrderBeRobMsg(phoneNumber, verifyCode);
		}
		
		if (sendResult) {
			return JSONUtils.responseToJsonString("1", "", "验证码发送成功！", verifyCode);
		}
		return JSONUtils.responseToJsonString("0", "", "验证码发送失败！", "");
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
				// 判断站点状态
				if ("3".equals(user.getType()) || "4".equals(user.getType())) {
					SiteDao siteDao = new SiteImpl();
					Site site = siteDao.getSiteById(user.getBelongSiteId());
					if (!"1".equals(site.getSiteCheckStatus())) {
						return JSONUtils.responseToJsonString("0", "", "站点信息审核中！", "");
					}

					user.setSiteType(site.getSiteType());
				}

				if (user.getBlackStatus() != null) {
					return JSONUtils.responseToJsonString("0", "", "账户被拉黑！请联系客服", "");
				}

				// 设置
				user.setLoginPlate(plateform);
				user.setLastLoginTime(new Date());
				user.setAccessToken(UUID.randomUUID().toString().replace("-", ""));
				updateUserInfo(user);
				user.setPassword(null);
				return JSONUtils.responseToJsonString("1", "", "登陆成功！", user);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
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
			tx.commit();
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
			tx.commit();
			e.printStackTrace();
			return null;
		} finally {

			if (tx != null) {
				tx = null;
			}
		}
	}

	// 根据id取user and driver
	public DriverListModel getUserDriverById(String userId) {
		DriverListModel user = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT user.*," + "vehicle.vehicleType," + "vehicle.plateNumber," + "vehicle.gpsType,"
					+ "vehicle.gpsSerialNumber," + "vehicle.drivingCardNumber," + "vehicle.vehicleRegistrationNumber,"
					+ "vehicle.operationCertificateNumber," + "vehicle.insuranceNumber,"
					+ "vehicle.vehicleIdCardNumber," + "vehicle.businessLicenseNumber," + "vehicle.vehicleBrand,"
					+ "vehicle.vehicleModel," + "vehicle.vehiclePhoto," + "vehicle.loadWeight,"
					+ "vehicle.vehicleMakeDate,"
					+ "vehicle.bindingDriverId AS vehicleBindingDriverId,"
					+ "(select siteName from site where user.belongSiteId = siteId) as belongSiteName,"
					+ "(select valueText from DictionaryModel where name = 'GPS类型' and keyText = vehicle.gpsType) AS gpsTypeName,"
					+ "vehicle.bindingDriverId AS vehicleBindingDriverId,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = vehicle.vehicleType limit 1) AS vehicleTypeName "
					+ "FROM user LEFT JOIN vehicle ON user.vehicleId = vehicle.vehicleId " + "where user.userId = '"
					+ userId + "'";

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(DriverListModel.class);

			user = (DriverListModel) query.uniqueResult();

			tx.commit();
			return user;

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return user;
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
			tx.commit();
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

		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();

			return JSONUtils.responseToJsonString("1", "", "更新信息成功！", user);
		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
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
	public List<DriverListModel> getDriverListBySiteId(String page, String rows, String siteId, String filterDriverName,
			String filterPlateNumber) {
		// TODO Auto-generated method stub
		List<DriverListModel> driverList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String sql1 = "SELECT user.*," + "vehicle.vehicleType," + "vehicle.gpsType," + "vehicle.gpsSerialNumber,"
					+ "vehicle.drivingCardNumber," + "vehicle.vehicleRegistrationNumber,"
					+ "vehicle.operationCertificateNumber," + "vehicle.insuranceNumber,"
					+ "vehicle.vehicleIdCardNumber," + "vehicle.businessLicenseNumber," + "vehicle.vehicleBrand,"
					+ "vehicle.vehicleModel," + "vehicle.vehiclePhoto," + "vehicle.loadWeight," + "vehicle.plateNumber,"
					+ "vehicle.vehicleMakeDate,"
					+ "vehicle.bindingDriverId AS vehicleBindingDriverId,"
					+ "(select description from DictionaryModel where name = '车辆类型' and keyText = vehicle.vehicleType limit 1) as vehicleTypeName,"
					+ "(select valueText from DictionaryModel where name = 'GPS类型' and keyText = vehicle.gpsType) as gpsTypeName,"
					+ "(select siteName from site where user.belongSiteId = siteId) as belongSiteName "
					+ "FROM user,vehicle " + "where user.vehicleId = vehicle.vehicleId and user.type = '6' "
					+ "and user.nickname like '%" + filterDriverName + "%' and vehicle.plateNumber like '%"
					+ filterPlateNumber + "%' ";

			String sql2 = "";
			if (siteId != null) {
				if (siteId.length() > 0) {
					sql2 = "and user.belongSiteId = '" + siteId + "' ";
				}

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
			tx.commit();
			e.printStackTrace();
			return driverList;
		} finally {

			if (tx != null) {
				tx = null;
			}

		}
	}

	// 根据ID查司机详情信息
	public JSONObject getDriverInfoByDriverId(String driverId) {
		DriverListModel driverListModel = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT user.*," + "vehicle.vehicleType," + "vehicle.gpsType," + "vehicle.gpsSerialNumber,"
					+ "vehicle.drivingCardNumber," + "vehicle.vehicleRegistrationNumber,"
					+ "vehicle.operationCertificateNumber," + "vehicle.insuranceNumber,"
					+ "vehicle.vehicleIdCardNumber," + "vehicle.businessLicenseNumber," + "vehicle.vehicleBrand,"
					+ "vehicle.vehicleModel," + "vehicle.vehiclePhoto," + "vehicle.loadWeight," + "vehicle.plateNumber,"
					+ "vehicle.vehicleMakeDate,"
					+ "vehicle.bindingDriverId AS vehicleBindingDriverId,"
					+ "(select description from DictionaryModel where name = '车辆类型' and keyText = vehicle.vehicleType limit 1) as vehicleTypeName,"
					+ "(select valueText from DictionaryModel where name = 'GPS类型' and keyText = vehicle.gpsType) as gpsTypeName,"
					+ "(select siteName from site where user.belongSiteId = siteId) as belongSiteName "
					+ "FROM user LEFT JOIN vehicle ON user.vehicleId = vehicle.vehicleId " + "where user.userId = '" + driverId
					+ "'";

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(DriverListModel.class);

			driverListModel = (DriverListModel) query.uniqueResult();
			tx.commit();

			return JSONUtils.responseToJsonString("1", "", "查询成功！", driverListModel);

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "查询失败！", "");
		} finally {

			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询司机条数
	@Override
	public Long getDriverCount(String siteId, String filterDriverName, String filterPlateNumber) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		Long driverCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String sql1 = "select count(*) from user,vehicle where user.vehicleId = vehicle.vehicleId and user.type = '6' "
					+ "and user.nickname like '%" + filterDriverName + "%' and vehicle.plateNumber like '%"
					+ filterPlateNumber + "%' ";

			String sql2 = "";
			if (siteId != null) {
				if (siteId.length() > 0) {
					sql2 = "and user.belongSiteId = '" + siteId + "' ";
				}
			}
			sql = sql1 + sql2;

			SQLQuery query = session.createSQLQuery(sql);
			driverCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();

			return driverCount;

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
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
			tx.commit();
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
			tx.commit();
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "删除失败！", "");
		} finally {

			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询车主已绑定的小司机列表
	@SuppressWarnings("unchecked")
	public List<DriverListModel> getDriverBindSmallDriverList(String driverId) {
		List<DriverListModel> driverList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT user.*," + "vehicle.vehicleType," + "vehicle.gpsType," + "vehicle.gpsSerialNumber,"
					+ "vehicle.drivingCardNumber," + "vehicle.vehicleRegistrationNumber,"
					+ "vehicle.operationCertificateNumber," + "vehicle.insuranceNumber,"
					+ "vehicle.vehicleIdCardNumber," + "vehicle.businessLicenseNumber," + "vehicle.vehicleBrand,"
					+ "vehicle.vehicleModel," + "vehicle.vehiclePhoto," + "vehicle.loadWeight," + "vehicle.plateNumber,"
					+ "vehicle.vehicleMakeDate,"
					+ "vehicle.bindingDriverId AS vehicleBindingDriverId,"
					+ "(select description from DictionaryModel where name = '车辆类型' and keyText = vehicle.vehicleType limit 1) as vehicleTypeName,"
					+ "(select valueText from DictionaryModel where name = 'GPS类型' and keyText = vehicle.gpsType) as gpsTypeName,"
					+ "(select siteName from site where user.belongSiteId = siteId) as belongSiteName "
					+ "FROM user,vehicle " + "where (user.superDriver = '" + driverId + "' or user.userId = '" + driverId + "') and user.vehicleId = vehicle.vehicleId ORDER BY nickname ";
			
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(DriverListModel.class);

			driverList = query.list();
			tx.commit();

			return driverList;

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return driverList;
		} finally {

			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询未被绑定的小司机列表
	@SuppressWarnings("unchecked")
	public List<DriverListModel> getUnBindSmallDriverList() {
		List<DriverListModel> driverList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT user.*," + "vehicle.vehicleType," + "vehicle.gpsType," + "vehicle.gpsSerialNumber,"
					+ "vehicle.drivingCardNumber," + "vehicle.vehicleRegistrationNumber,"
					+ "vehicle.operationCertificateNumber," + "vehicle.insuranceNumber,"
					+ "vehicle.vehicleIdCardNumber," + "vehicle.businessLicenseNumber," + "vehicle.vehicleBrand,"
					+ "vehicle.vehicleModel," + "vehicle.vehiclePhoto," + "vehicle.loadWeight," + "vehicle.plateNumber,"
					+ "vehicle.vehicleMakeDate,"
					+ "vehicle.bindingDriverId AS vehicleBindingDriverId,"
					+ "(select description from DictionaryModel where name = '车辆类型' and keyText = vehicle.vehicleType limit 1) as vehicleTypeName,"
					+ "(select valueText from DictionaryModel where name = 'GPS类型' and keyText = vehicle.gpsType) as gpsTypeName,"
					+ "(select siteName from site where user.belongSiteId = siteId) as belongSiteName "
					+ "FROM user LEFT JOIN vehicle ON user.vehicleId = vehicle.vehicleId " 
					+ "where user.type = '6' and user.driverType = '2' and user.superDriver IS NULL and user.NICKNAME IS NOT NULL and user.userIdCardNumber IS NOT NULL and user.driverLicenseNumber IS NOT NULL "
					+ "ORDER BY updateTime desc ";

			

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(DriverListModel.class);

			driverList = query.list();
			tx.commit();

			return driverList;

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return driverList;
		} finally {

			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询的货主列表
	@SuppressWarnings("unchecked")
	public List<MasterListModel> getMasterListBySiteId(String page, String rows, String siteId, String filterMasterName,
			String filterPhoneNumber) {
		List<MasterListModel> masterList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String sql1 = "SELECT user.*,"
					+ "(select siteName from site where user.belongSiteId = siteId) as belongSiteName " + "FROM user "
					+ "where user.type = '5' " + "and user.nickname like '%" + filterMasterName
					+ "%' and user.phoneNumber like '%" + filterPhoneNumber + "%' ";

			String sql2 = "";
			if (siteId != null) {
				if (siteId.length() > 0) {
					sql2 = "and user.belongSiteId = '" + siteId + "' ";
				}
			}

			String sql3 = "ORDER BY updateTime desc " + "LIMIT " + page + "," + rows;
			sql = sql1 + sql2 + sql3;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(MasterListModel.class);

			masterList = query.list();

			tx.commit();
			return masterList;

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return masterList;
		} finally {

			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询货主条数
	public Long getMasterCount(String siteId, String filterMasterName, String filterPhoneNumber) {
		Transaction tx = null;
		Long driverCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String sql1 = "select count(*) from user where user.type = '5' " + "and user.nickname like '%"
					+ filterMasterName + "%' and user.phoneNumber like '%" + filterPhoneNumber + "%' ";

			String sql2 = "";
			if (siteId != null) {
				if (siteId.length() > 0) {
					sql2 = "and user.belongSiteId = '" + siteId + "' ";
				}
			}
			sql = sql1 + sql2;

			SQLQuery query = session.createSQLQuery(sql);
			driverCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return driverCount;

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return driverCount;
		} finally {

			if (tx != null) {
				tx = null;
			}
		}
	}

	// 根据ID查货主详情信息
	public JSONObject getMasterInfoByMasterId(String masterId) {
		MasterListModel masterListModel = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT user.*," + "(select siteName from site where user.belongSiteId = siteId) as belongSiteName "
					+ "FROM user " + "where user.userId = '" + masterId + "'";

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(MasterListModel.class);

			masterListModel = (MasterListModel) query.uniqueResult();

			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "查询成功！", masterListModel);

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "查询失败！", "");
		} finally {

			if (tx != null) {
				tx = null;
			}

		}
	}

	// 添加货主
	public JSONObject addMaster(User user) {
		// TODO Auto-generated method stub
		user.setUserId(UUID.randomUUID().toString());

		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		user.setPassword("e10adc3949ba59abbe56e057f20f883e");
		user.setScore(5.0);
		user.setType("5");

		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "注册成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "注册失败！", "");
		} finally {

			if (tx != null) {
				tx = null;
			}
		}
	}

	// 删除货主
	public JSONObject deleteMaster(String masterId) {
		Transaction tx = null;
		User master = getUserById(masterId);

		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.delete(master);

			tx.commit();
			if (master != null) {
				return JSONUtils.responseToJsonString("1", "", "删除成功！", "");
			}
			return JSONUtils.responseToJsonString("0", "对应记录不存在", "删除失败！", "");

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "删除失败！", "");
		} finally {

			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询管理员列表
	@SuppressWarnings("unchecked")
	public List<ManageListModel> getManageListBySiteId(String page, String rows, String siteId, String filterNickName,
			String filterPhoneNumber) {
		List<ManageListModel> manageList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String sql1 = "SELECT userId, phoneNumber, nickname, personImageUrl, belongSiteId, userIdCardNumber, createTime, updateTime "
					+ "FROM user " + "where user.nickname like '%" + filterNickName + "%' and user.phoneNumber like '%"
					+ filterPhoneNumber + "%' ";

			String sql2 = "";
			if (siteId != null) {
				if (siteId.length() > 0) {
					sql2 = "and user.belongSiteId = '" + siteId + "' and user.type = '4' ";
				} else {
					sql2 = "and user.type = '1' ";
				}
			} else {
				sql2 = "and user.type = '1' ";
			}

			String sql3 = "ORDER BY updateTime desc " + "LIMIT " + page + "," + rows;
			sql = sql1 + sql2 + sql3;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(ManageListModel.class);

			manageList = query.list();
			tx.commit();

			return manageList;

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return manageList;
		} finally {

			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询管理员条数
	public Long getManageCount(String siteId, String filterNickName, String filterPhoneNumber) {
		Transaction tx = null;
		Long manageCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String sql1 = "select count(*) from user where user.nickname like '%" + filterNickName
					+ "%' and user.phoneNumber like '%" + filterPhoneNumber + "%' ";

			String sql2 = "";
			if (siteId != null) {
				if (siteId.length() > 0) {
					sql2 = "and user.belongSiteId = '" + siteId + "' and user.type = '4' ";
				} else {
					sql2 = "and user.type = '1' ";
				}
			} else {
				sql2 = "and user.type = '1' ";
			}
			sql = sql1 + sql2;

			SQLQuery query = session.createSQLQuery(sql);
			manageCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return manageCount;

		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return manageCount;
		} finally {

			if (tx != null) {
				tx = null;
			}
		}
	}

	// 添加管理员
	public JSONObject addManage(User user) {
		user.setUserId(UUID.randomUUID().toString());

		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		user.setPassword("e10adc3949ba59abbe56e057f20f883e");
		if (user.getBelongSiteId() != null) {
			if (user.getBelongSiteId().length() > 0) {
				user.setType("4");
			} else {
				user.setType("1");
			}
		} else {
			user.setType("1");
		}

		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "注册成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			tx.commit();
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "注册失败！", "");
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
			tx.commit();
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
			tx.commit();
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
			tx.commit();
			e.printStackTrace();
			return true;
		} finally {

			if (tx != null) {
				tx = null;
			}
		}
	}

}
