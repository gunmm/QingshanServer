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
import com.gunmm.dao.FlowDao;
import com.gunmm.dao.InvoiceDao;
import com.gunmm.dao.OrderDao;
import com.gunmm.dao.UserDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.Flow;
import com.gunmm.model.Invoice;
import com.gunmm.model.Order;
import com.gunmm.model.User;
import com.gunmm.model.Vehicle;
import com.gunmm.responseModel.NearbyDriverListModel;
import com.gunmm.responseModel.OrderListModel;
import com.gunmm.responseModel.OrderListModelForSite;
import com.gunmm.responseModel.WithdrawalFinishedOrderListModel;
import com.gunmm.utils.JSONUtils;

public class OrderDaoImpl implements OrderDao {

	@Override // 添加订单
	public JSONObject addOrder(Order order) {
		// TODO Auto-generated method stub
		order.setOrderId(UUID.randomUUID().toString());
		order.setCreateTime(new Date());
		order.setUpdateTime(new Date());
		order.setStatus("0");
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(order);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "下单成功！", order.getOrderId());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "下单失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override // 请求300公里以内司机
	public List<NearbyDriverListModel> queryDriverForOrder(Order order) {
		// TODO Auto-generated method stub
		List<NearbyDriverListModel> driverList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "select user.userId,user.accessToken,user.loginPlate,vehicle.plateNumber,vehicle.nowLatitude,vehicle.nowLongitude,"
					+ "ACOS(SIN((" + order.getSendLatitude()
					+ " * 3.1415) / 180 ) *SIN((vehicle.nowLatitude * 3.1415) / 180 ) +COS((" + order.getSendLatitude()
					+ " * 3.1415) / 180 ) * COS((vehicle.nowLatitude * 3.1415) / 180 ) *COS(("
					+ order.getSendLongitude()
					+ "* 3.1415) / 180 - (vehicle.nowLongitude * 3.1415) / 180 ) ) * 6380 AS distance "
					+ "FROM user,vehicle "
					+ "where user.userId = vehicle.bindingDriverId and (user.status = '0' or user.status = '1') and user.blackStatus is NULL and user.score>0 and vehicle.vehicleType = '"
					+ order.getCarType() + "' " + "and (ACOS(SIN((" + order.getSendLatitude()
					+ " * 3.1415) / 180 ) *SIN((vehicle.nowLatitude * 3.1415) / 180 ) +COS((" + order.getSendLatitude()
					+ " * 3.1415) / 180 ) * COS((vehicle.nowLatitude * 3.1415) / 180 ) *COS(("
					+ order.getSendLongitude()
					+ "* 3.1415) / 180 - (vehicle.nowLongitude * 3.1415) / 180 ) ) * 6380) < 300 "
					+ "order by (ACOS(SIN((" + order.getSendLatitude()
					+ " * 3.1415) / 180 ) *SIN((vehicle.nowLatitude * 3.1415) / 180 ) +COS((" + order.getSendLatitude()
					+ " * 3.1415) / 180 ) * COS((vehicle.nowLatitude * 3.1415) / 180 ) *COS(("
					+ order.getSendLongitude() + "* 3.1415) / 180 - (vehicle.nowLongitude * 3.1415) / 180 ) ) * 6380)";
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(NearbyDriverListModel.class);

			driverList = query.list();

			tx.commit();
			return driverList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return driverList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	@Override
	public JSONObject robOrder(String driverId, String orderId) {
		Order order = getOrderById(orderId);
		if (order == null) {
			return JSONUtils.responseToJsonString("0", "", "对应订单不存在！", "");
		} else if (!order.getStatus().equals("0")) {
			if (order.getStatus().equals("9")) {
				return JSONUtils.responseToJsonString("0", "", "订单已被取消！", "");
			}
			return JSONUtils.responseToJsonString("0", "", "订单已被抢走！", "");
		} else {
			UserDao userDao = new UserDaoImpl();
			User user = userDao.getUserById(driverId);

			// 注释掉之前只可接一个订单的代码
			// if ("1".equals(user.getStatus()) && "1".equals(order.getType())) {
			// return JSONUtils.responseToJsonString("0", "", "不可重复接实时订单！", "");
			// }
			order.setStatus("1");
			order.setDriverId(driverId);
			order.setUpdateTime(new Date());
			Date now = new Date();
			order.setTimeOut(new Date(now.getTime() + 300000));
			if ("2".equals(order.getType())) {
				order.setAppointStatus("0");
			}
			JSONObject jsonObj = updateOrderInfo(order);
			String result_code = jsonObj.getString("result_code");
			String reason = jsonObj.getString("reason");

			if ("1".equals(result_code)) {
				if (user != null && "1".equals(order.getType())) {
					user.setStatus("1");
					userDao.updateUserInfo(user);
				}
				FlowDao flowDao = new FlowImpl();
				Flow flow = flowDao.getFlowByDriverId(driverId, orderId);
				if (flow != null) {
					flow.setFlowStatus("2");
					flowDao.updateFlow(flow);
					flowDao.setFlowStatusByOrderId(orderId, "1");
				}

				return JSONUtils.responseToJsonString("1", "", "抢单成功！", "");
			} else {
				return JSONUtils.responseToJsonString("0", reason, "抢单失败！", "");

			}

		}

	}

	// 根据订单ID拿订单
	@Override
	public Order getOrderById(String orderId) {
		Transaction tx = null;
		Order order = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from Order where orderId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, orderId);
			order = (Order) query.uniqueResult();

			tx.commit();
			return order;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return null;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@SuppressWarnings("unused")
	@Override
	public JSONObject cancelOrderById(String orderId) {
		// TODO Auto-generated method stub
		Order order = getOrderById(orderId);
		InvoiceDao invoiceDao = new InvoiceImpl();
		if (order.getInvoiceId() != null) {
			Invoice invoice = invoiceDao.getInvoiceById(order.getInvoiceId());
			invoice.setStatus("9");
			invoiceDao.updateInvoiceInfo(invoice);
		}

		if (order == null) {
			return JSONUtils.responseToJsonString("0", "对应订单不存在！", "取消订单失败！", "");
		} else {
			order.setStatus("9");
			order.setUpdateTime(new Date());
			JSONObject jsonObj = updateOrderInfo(order);
			String result_code = jsonObj.getString("result_code");
			String reason = jsonObj.getString("reason");

			if ("1".equals(result_code)) {
				if (order.getDriverId() != null) {
					UserDao userDao = new UserDaoImpl();
					User user = userDao.getUserById(order.getDriverId());
					user.setStatus("0");
					userDao.updateUserInfo(user);
				}
				return JSONUtils.responseToJsonString("1", "", "取消订单成功！", "");
			} else {
				return JSONUtils.responseToJsonString("0", reason, "取消订单失败！", "");
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderListModel> getOrderListByUserId(String userId, String page, String rows) {
		// TODO Auto-generated method stub
		List<OrderListModel> orderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT `order`.*," + "user.nickname," + "user.phoneNumber," + "user.personImageUrl," + "user.score,"
					+ "vehicle.nowLatitude," + "vehicle.nowLongitude," + "vehicle.plateNumber,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType limit 1) AS carTypeName "
					+ "FROM `order` LEFT JOIN user ON `order`.driverId = user.userId LEFT JOIN vehicle ON user.vehicleId = vehicle.vehicleId "
					+ "where `order`.createManId = '" + userId + "' " + "ORDER BY updateTime desc " + "LIMIT " + page
					+ "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(OrderListModel.class);

			orderList = query.list();

			tx.commit();
			return orderList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询货主订单总条数
	public Long getMasterOrderCount(String userId) {
		Transaction tx = null;
		Long orderCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "select count(*) " + "FROM `order` " + "where `order`.createManId = '" + userId + "' ";

			SQLQuery query = session.createSQLQuery(sql);
			orderCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return orderCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderListModel> getDriverOrderListByDriverId(String driverId, String page, String rows,
			String condition) {
		// TODO Auto-generated method stub
		List<OrderListModel> orderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT `order`.*," + "user.nickname," + "user.phoneNumber," + "user.personImageUrl," + "user.score,"
					+ "vehicle.nowLongitude," + "vehicle.nowLatitude," + "vehicle.plateNumber,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType limit 1) AS carTypeName "
					+ "FROM " + "`order` LEFT JOIN user ON `order`.driverId = user.userId,vehicle "
					+ "where (`order`.driverId = '" + driverId
					+ "' or `order`.driverId in (select userId from user where user.type = '6' and user.superDriver = '"
					+ driverId + "')) and user.vehicleId = vehicle.vehicleId " + condition + "ORDER BY updateTime desc "
					+ "LIMIT " + page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(OrderListModel.class);

			orderList = query.list();

			tx.commit();
			return orderList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderList;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<OrderListModel> getDriverFindOrderListByDriverId(Vehicle vehicle, String page, String rows) {
		List<OrderListModel> orderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT `order`.*," + "null AS nickname," + "null AS phoneNumber," + "null AS personImageUrl," + "null AS score,"
					+ "null AS nowLongitude," + "null AS nowLatitude," + "null AS plateNumber,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType limit 1) AS carTypeName "
					+ "FROM " + "`order` "
					+ "where `order`.status = '0' "
					+ "ORDER BY (ACOS(SIN((" + vehicle.getNowLatitude() + 
					 " * 3.1415) / 180 ) * SIN((`order`.sendLatitude * 3.1415) / 180 ) +COS((" + vehicle.getNowLatitude() + 
					 " * 3.1415) / 180 ) * COS((`order`.sendLatitude * 3.1415) / 180 ) *COS((" + 
					 vehicle.getNowLongitude() + "* 3.1415) / 180 - (`order`.sendLongitude * 3.1415) / 180 ) ) * 6380) "
					+ "LIMIT " + page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(OrderListModel.class);

			orderList = query.list();

			tx.commit();
			return orderList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderList;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询司机订单总条数
	public Long getDriverOrderCount(String driverId) {
		Transaction tx = null;
		Long orderCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "select count(*) " + "FROM `order` " + "where `order`.driverId = '" + driverId + "' ";

			SQLQuery query = session.createSQLQuery(sql);
			orderCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return orderCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询站点订单列表
	@SuppressWarnings("unchecked")
	public List<OrderListModelForSite> getSiteOrderList(String siteId, String page, String rows) {
		List<OrderListModelForSite> orderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT `order`.*," + "user.nickname," + "user.phoneNumber," + "user.personImageUrl," + "user.score,"
					+ "vehicle.nowLongitude," + "vehicle.nowLatitude," + "vehicle.plateNumber,"

					+ "(" + "CASE "
					+ "WHEN ((SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID) = '"
					+ siteId
					+ "' AND (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.createManId) = '"
					+ siteId + "') THEN '3' "
					+ "WHEN (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID) = '"
					+ siteId + "' THEN '2' " + "ELSE '1' END " + ") AS orderRoleBelong,"

					+ "(select nickname from user where user.userId = `order`.createManId) AS createManName,"
					+ "(select score from user where user.userId = `order`.createManId) AS createManScore,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType limit 1) AS carTypeName "
					+ "FROM `order` LEFT JOIN user ON `order`.driverId = user.userId LEFT JOIN vehicle ON user.vehicleId = vehicle.vehicleId "
					+ "where `order`.driverId in (SELECT `user`.userId FROM `user` WHERE `user`.belongSiteId = '"
					+ siteId + "' AND `user`.type = '6') OR "
					+ "`order`.createManId in (SELECT `user`.userId FROM `user` WHERE `user`.belongSiteId = '" + siteId
					+ "' AND `user`.type = '5') " + "ORDER BY updateTime desc " + "LIMIT " + page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(OrderListModelForSite.class);

			orderList = query.list();

			tx.commit();
			return orderList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderList;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询站点订单总条数
	public Long getSiteOrderCount(String siteId) {
		Transaction tx = null;
		Long orderCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "select count(*) "
					+ "FROM `order` LEFT JOIN user ON `order`.driverId = user.userId LEFT JOIN vehicle ON user.vehicleId = vehicle.vehicleId "
					+ "where `order`.driverId in (SELECT `user`.userId FROM `user` WHERE `user`.belongSiteId = '"
					+ siteId + "' AND `user`.type = '6') OR "
					+ "`order`.createManId in (SELECT `user`.userId FROM `user` WHERE `user`.belongSiteId = '" + siteId
					+ "' AND `user`.type = '5')";

			SQLQuery query = session.createSQLQuery(sql);
			orderCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return orderCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 司机设置预约订单开始执行
	@Override
	public JSONObject setAppointOrderBegin(String driverId, String orderId) {
		// TODO Auto-generated method stub
		Order order = getOrderById(orderId);
		UserDao userDao = new UserDaoImpl();
		User driver = userDao.getUserById(driverId);
		if (order == null || driver == null) {
			return JSONUtils.responseToJsonString("0", "对应订单或司机不存在！", "执行失败！", "");
		} else {
			order.setAppointStatus("1");
			order.setUpdateTime(new Date());
			JSONObject jsonObj = updateOrderInfo(order);
			String result_code = jsonObj.getString("result_code");
			String reason = jsonObj.getString("reason");
			if ("1".equals(result_code)) {
				driver.setStatus("1");
				userDao.updateUserInfo(driver);
				return JSONUtils.responseToJsonString("1", "", "执行成功！", "");
			} else {
				return JSONUtils.responseToJsonString("0", reason, "执行失败！", "");
			}
		}
	}

	@Override
	public JSONObject updateOrderInfo(Order order) {
		// TODO Auto-generated method stub
		order.setUpdateTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(order);
			tx.commit();

			return JSONUtils.responseToJsonString("1", "", "更新信息成功！", order);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "更新信息失败！", order);
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@Override
	public OrderListModelForSite getBigOrderInfo(String orderId) {
		// TODO Auto-generated method stub
		OrderListModelForSite orderListModel = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT `order`.*," + "user.nickname," + "user.phoneNumber," + "user.personImageUrl," + "user.score,"
					+ "vehicle.nowLongitude," + "vehicle.nowLatitude," + "vehicle.plateNumber,"
					+ "'1' AS orderRoleBelong,"
					+ "(select nickname from user where user.userId = `order`.createManId) AS createManName,"
					+ "(select score from user where user.userId = `order`.createManId) AS createManScore,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType limit 1) AS carTypeName "
					+ "FROM "
					+ "`order` LEFT JOIN user ON `order`.driverId = user.userId LEFT JOIN vehicle ON user.vehicleId = vehicle.vehicleId "
					+ "where `order`.orderId = '" + orderId + "'";

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(OrderListModelForSite.class);
			orderListModel = (OrderListModelForSite) query.uniqueResult();

			tx.commit();
			return orderListModel;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderListModel;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 将所有已被抢单但是付款超时订单状态置0
	public JSONObject setTimeOutOrderStatus() {
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "update `order`,user "
					+ "set `order`.status = '0',`order`.timeOut = NULL,`order`.driverId = NULL,`order`.driverId = NULL,user.status = '0',user.score = user.score-0.1 "
					+ "where `order`.status = '1' AND NOW() >= `order`.timeOut and user.userId = order.driverId ";
			SQLQuery query = session.createSQLQuery(sql);
			query.executeUpdate();

			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "超时订单操作成功！", "");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "超时订单操作失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询所有未被接单的订单
	@SuppressWarnings("unchecked")
	public List<Order> getUnReceiveOrderList() {
		List<Order> orderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "select * " + "FROM `order` " + "where `order`.status = '0'";

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(Order.class);

			orderList = query.list();

			tx.commit();
			return orderList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderList;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 设置指定时间段内订单提现状态
	public JSONObject setWithdrawal(String dataStr, String withdrawalId, String siteId) {
		Transaction tx = null;
		String sql1 = "";
		String sql2 = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String beginStr = dataStr + "-01 00:00:00";
			String endStr = dataStr + "-31 23:59:59";
			sql1 = "UPDATE `order` SET `order`.masterSiteWithdrawStatus='1',`order`.masterSiteWithdrawId='"
					+ withdrawalId + "' " + "WHERE `order`.finishTime < '" + endStr + "' AND `order`.finishTime > '"
					+ beginStr + "' AND `order`.masterSiteWithdrawStatus = '0' AND `order`.status = '4' " + "AND ("
					+ "(`order`.CREATEMANID in (SELECT `user`.USERID FROM `user` WHERE `user`.BELONGSITEID = '" + siteId
					+ "' AND `user`.TYPE = '5')) OR " + "(`order`.CREATEMANID in (SELECT `user`.USERID "
					+ "FROM `user` " + "WHERE `user`.TYPE = '5' " + "AND `user`.BELONGSITEID in "
					+ "(SELECT site.SITEID " + "FROM site " + "WHERE site.superSiteId = '" + siteId + "' " + ") " + ") "
					+ " ) " + " ) ";
			sql2 = "UPDATE `order` SET `order`.driverSiteWithdrawStatus='1',`order`.driverSiteWithdrawId='"
					+ withdrawalId + "' " + "WHERE `order`.finishTime < '" + endStr + "' AND `order`.finishTime > '"
					+ beginStr + "' AND `order`.driverSiteWithdrawStatus = '0' AND `order`.status = '4' " + "AND ("
					+ "(`order`.DRIVERID in (SELECT `user`.USERID FROM `user` WHERE `user`.BELONGSITEID = '" + siteId
					+ "' AND `user`.TYPE = '6')) OR " + "(`order`.DRIVERID in (SELECT `user`.USERID " + "FROM `user` "
					+ "WHERE `user`.TYPE = '6' " + "AND `user`.BELONGSITEID in" + "(SELECT site.SITEID " + "FROM site "
					+ "WHERE site.superSiteId = '" + siteId + "' " + ") " + ") " + ")  " + " )";
			SQLQuery query = session.createSQLQuery(sql1);
			query.executeUpdate();

			query = session.createSQLQuery(sql2);
			query.executeUpdate();

			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "操作成功！", "");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "操作失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询已提现记录对应订单列表
	@SuppressWarnings("unchecked")
	public List<WithdrawalFinishedOrderListModel> getWithDrawalFinishedOrderList(String siteId, String withdrawalId,
			String page, String rows) {
		List<WithdrawalFinishedOrderListModel> orderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT `order`.ORDERID,`order`.DISTANCE,`order`.PRICE,convert(`order`.servicePrice,decimal(12,2)) AS SERVICEPRICE,`order`.masterSiteWithdrawStatus,`order`.driverSiteWithdrawStatus,`order`.finishTime,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType limit 1) AS carTypeName,"
					+ "(SELECT site.SITENAME FROM site WHERE site.SITEID = (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID)) AS driverSiteName,"
					+ "(SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID) AS driverSiteId,"
					+ "(SELECT site.SITENAME FROM site WHERE site.SITEID = (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.CREATEMANID)) AS masterSiteName,"
					+ "(SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.CREATEMANID) AS masterSiteId,"
					+ "(" + "CASE "
					+ "WHEN (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID) = '"
					+ siteId + "' THEN '本站点' "
					+ "WHEN (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID) IN (SELECT site.SITEID FROM site WHERE site.SUPERSITEID = '"
					+ siteId + "') THEN '子站点' " + "ELSE '其他站点' END " + ") AS driverBelongSiteType," + "(" + "CASE "
					+ "WHEN (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.CREATEMANID) = '"
					+ siteId + "' THEN '本站点' "
					+ "WHEN (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.CREATEMANID) IN (SELECT site.SITEID FROM site WHERE site.SUPERSITEID = '"
					+ siteId + "') THEN '子站点' " + "ELSE '其他站点' END " + ") AS masterBelongSiteType " +

					"FROM `order` " + "WHERE (`order`.driverSiteWithdrawId = '" + withdrawalId
					+ "' OR `order`.masterSiteWithdrawId = '" + withdrawalId + "') "
					+ "ORDER BY `order`.finishTime desc " + "LIMIT " + page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(WithdrawalFinishedOrderListModel.class);

			orderList = query.list();

			tx.commit();
			return orderList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderList;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询已提现记录对应订单列表总条数
	public Long getWithDrawalFinishedOrderListCount(String siteId, String withdrawalId) {
		Transaction tx = null;
		Long orderCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "select count(*) " + "FROM `order` " + "WHERE (`order`.driverSiteWithdrawStatus = '" + withdrawalId
					+ "' OR `order`.masterSiteWithdrawStatus = '" + withdrawalId + "') ";

			SQLQuery query = session.createSQLQuery(sql);
			orderCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return orderCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询指定站点及子站点的已完成订单列表
	@SuppressWarnings("unchecked")
	public List<WithdrawalFinishedOrderListModel> getFinishedOrderList(String siteId, String queryTime, String page,
			String rows) {
		List<WithdrawalFinishedOrderListModel> orderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String beginStr = queryTime + "-01 00:00:00";
			String endStr = queryTime + "-31 23:59:59";

			sql = "SELECT `order`.ORDERID,`order`.DISTANCE,`order`.PRICE,convert(`order`.servicePrice,decimal(12,2)) AS SERVICEPRICE,`order`.masterSiteWithdrawStatus,`order`.driverSiteWithdrawStatus,`order`.FINISHTIME,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType limit 1) AS carTypeName,"
					+ "(SELECT site.SITENAME FROM site WHERE site.SITEID = (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID)) AS driverSiteName,"
					+ "(SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID) AS driverSiteId,"
					+ "(SELECT site.SITENAME FROM site WHERE site.SITEID = (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.CREATEMANID)) AS masterSiteName,"
					+ "(SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.CREATEMANID) AS masterSiteId,"
					+ "(" + "CASE "
					+ "WHEN (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID) = '"
					+ siteId + "' THEN '本站点' "
					+ "WHEN (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID) IN (SELECT site.SITEID FROM site WHERE site.SUPERSITEID = '"
					+ siteId + "') THEN '子站点'  " + "ELSE '其他站点' END " + ") AS driverBelongSiteType, " + "(" + "CASE "
					+ "WHEN (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.CREATEMANID) = '"
					+ siteId + "' THEN '本站点' "
					+ "WHEN (SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.CREATEMANID) IN (SELECT site.SITEID FROM site WHERE site.SUPERSITEID = '"
					+ siteId + "') THEN '子站点'  " + "ELSE '其他站点' END " + ") AS masterBelongSiteType " + "FROM `order` "
					+ "WHERE `order`.`STATUS` = '4' AND  " + "("
					+ "(SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID) = '" + siteId
					+ "' OR  "
					+ "(SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.CREATEMANID) = '" + siteId
					+ "' OR  "
					+ "(SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID) IN (SELECT site.SITEID FROM site WHERE site.SUPERSITEID = '"
					+ siteId + "') OR "
					+ "(SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.CREATEMANID) IN (SELECT site.SITEID FROM site WHERE site.SUPERSITEID = '"
					+ siteId + "') " + ")" + "AND `order`.finishTime > '" + beginStr + "' "
					+ "AND `order`.finishTime < '" + endStr + "' " + "ORDER BY `order`.finishTime desc " + "LIMIT "
					+ page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(WithdrawalFinishedOrderListModel.class);

			orderList = query.list();

			tx.commit();
			return orderList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderList;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询指定站点及子站点的已完成订单列表条数
	public Long getFinishedOrderListCount(String siteId, String queryTime) {
		Transaction tx = null;
		Long orderCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			String beginStr = queryTime + "-01 00:00:00";
			String endStr = queryTime + "-31 23:59:59";
			sql = "select count(*) " + "FROM `order` " + "WHERE `order`.`STATUS` = '4' AND  " + "("
					+ "(SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID) = '" + siteId
					+ "' OR  "
					+ "(SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.CREATEMANID) = '" + siteId
					+ "' OR  "
					+ "(SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.DRIVERID) IN (SELECT site.SITEID FROM site WHERE site.SUPERSITEID = '"
					+ siteId + "') OR "
					+ "(SELECT `user`.BELONGSITEID FROM `user` WHERE `user`.USERID = `order`.CREATEMANID) IN (SELECT site.SITEID FROM site WHERE site.SUPERSITEID = '"
					+ siteId + "') " + ")" + "AND `order`.finishTime > '" + beginStr + "' "
					+ "AND `order`.finishTime < '" + endStr + "' ";

			SQLQuery query = session.createSQLQuery(sql);
			orderCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return orderCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 更新线上支付订单的 司机 提现状态
	public JSONObject setOnlineOrderDriverWithdrawalStatus(String driverWithdrawalStatus, String driverWithdrawalId,
			String driverId) {
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "UPDATE `order` " + "SET `order`.driverWithdrawalStatus='" + driverWithdrawalStatus
					+ "',`order`.driverWithdrawalId='" + driverWithdrawalId + "' "
					+ "WHERE `order`.`STATUS` = '4' AND `order`.FREIGHTFEEPAYSTATUS = '1' AND `order`.driverWithdrawalStatus = '0' AND (`order`.DRIVERID = '"
					+ driverId
					+ "' or `order`.driverId in (select userId from user where user.type = '6' and user.superDriver = '"
					+ driverId + "'))";
			SQLQuery query = session.createSQLQuery(sql);
			query.executeUpdate();

			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "操作成功！", "");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "操作失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 根据司机id查询司机可提现的订单总额
	public Double getDriverWithdrawalAmount(String driverId) {
		Transaction tx = null;
		Double orderCount = (double) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT SUM(price) from `order` "
					+ "WHERE `order`.`STATUS` = '4' AND `order`.FREIGHTFEEPAYSTATUS = '1' AND `order`.driverWithdrawalStatus = '0' AND (`order`.DRIVERID = '"
					+ driverId
					+ "' or `order`.DRIVERID in (select userId from user where user.type = '6' and user.superDriver = '"
					+ driverId + "'))";

			SQLQuery query = session.createSQLQuery(sql);
			orderCount = (Double) query.uniqueResult();

			tx.commit();
			return orderCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 根据司机id查询司机可提现的订单
	@SuppressWarnings("unchecked")
	public List<OrderListModel> getDriverWithDrawalOrderList(String driverId) {
		List<OrderListModel> orderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT `order`.*," + "user.nickname," + "user.phoneNumber," + "user.personImageUrl," + "user.score,"
					+ "vehicle.nowLongitude," + "vehicle.nowLatitude," + "vehicle.plateNumber,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType limit 1) AS carTypeName "
					+ "FROM `order` LEFT JOIN user ON `order`.driverId = user.userId LEFT JOIN vehicle ON user.vehicleId = vehicle.vehicleId "
					+ "WHERE `order`.`STATUS` = '4' AND `order`.FREIGHTFEEPAYSTATUS = '1' AND `order`.driverWithdrawalStatus = '0' AND (`order`.DRIVERID = '"
					+ driverId
					+ "' or `order`.DRIVERID in (select userId from user where user.type = '6' and user.superDriver = '"
					+ driverId + "')) " + "ORDER BY `order`.finishTime desc ";

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(OrderListModel.class);

			orderList = query.list();

			tx.commit();
			return orderList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderList;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 获取司机提现订单列表
	@SuppressWarnings("unchecked")
	public List<OrderListModel> getDriverWithdrawaledOrderList(String driverWithdrawalId, String page, String rows) {
		List<OrderListModel> orderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT `order`.*," + "user.nickname," + "user.phoneNumber," + "user.personImageUrl," + "user.score,"
					+ "vehicle.nowLongitude," + "vehicle.nowLatitude," + "vehicle.plateNumber,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType limit 1) AS carTypeName "
					+ "FROM `order` LEFT JOIN user ON `order`.driverId = user.userId LEFT JOIN vehicle ON user.vehicleId = vehicle.vehicleId "
					+ "WHERE `order`.`STATUS` = '4' AND `order`.FREIGHTFEEPAYSTATUS = '1' AND `order`.driverWithdrawalId = '"
					+ driverWithdrawalId + "' " + "ORDER BY `order`.finishTime desc " + "LIMIT " + page + "," + rows;
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(OrderListModel.class);

			orderList = query.list();

			tx.commit();
			return orderList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderList;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 获取司机提现订单列表列表条数
	public Long getDriverWithdrawaledOrderListCount(String driverWithdrawalId) {
		Transaction tx = null;
		Long orderCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "select count(*) " + "FROM `order` "
					+ "WHERE `order`.`STATUS` = '4' AND `order`.FREIGHTFEEPAYSTATUS = '1' AND `order`.driverWithdrawalId = '"
					+ driverWithdrawalId + "' ";

			SQLQuery query = session.createSQLQuery(sql);
			orderCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return orderCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 获取Mobile司机提现订单列表
	@SuppressWarnings("unchecked")
	public List<OrderListModel> getMobileDriverWithdrawaledOrderList(String driverWithdrawalId) {
		List<OrderListModel> orderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT `order`.*," + "user.nickname," + "user.phoneNumber," + "user.personImageUrl," + "user.score,"
					+ "vehicle.nowLongitude," + "vehicle.nowLatitude," + "vehicle.plateNumber,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType limit 1) AS carTypeName "
					+ "FROM `order` LEFT JOIN user ON `order`.driverId = user.userId LEFT JOIN vehicle ON user.vehicleId = vehicle.vehicleId "
					+ "WHERE `order`.`STATUS` = '4' AND `order`.FREIGHTFEEPAYSTATUS = '1' AND `order`.driverWithdrawalId = '"
					+ driverWithdrawalId + "' " + "ORDER BY `order`.finishTime desc ";
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(OrderListModel.class);

			orderList = query.list();

			tx.commit();
			return orderList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderList;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询要被车主去除的小司机有没有未提现的订单
	@SuppressWarnings("unchecked")
	public List<Order> getSmallDriverUnWithdrawedOrderList(String smallDriverId) {
		List<Order> orderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT * FROM `order` " + "WHERE `order`.driverId = '" + smallDriverId
					+ "' AND `order`.masterSiteWithdrawStatus = '0' AND `order`.driverSiteWithdrawStatus = '0'";

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(Order.class);

			orderList = query.list();

			tx.commit();
			return orderList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return orderList;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

}
