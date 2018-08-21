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
import com.gunmm.responseModel.NearbyDriverListModel;
import com.gunmm.responseModel.OrderListModel;
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
					+ "where user.vehicleId = vehicle.vehicleId and user.type = '6' and user.status = '0' and vehicle.vehicleType = '"
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
			if ("1".equals(user.getStatus()) && "1".equals(order.getType())) {
				return JSONUtils.responseToJsonString("0", "", "不可重复接实时订单！", "");
			}
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
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType) AS carTypeName "
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
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType) AS carTypeName "
					+ "FROM " + "`order` LEFT JOIN user ON `order`.driverId = user.userId,vehicle "
					+ "where `order`.driverId = '" + driverId + "' and user.vehicleId = vehicle.vehicleId " + condition
					+ "ORDER BY updateTime desc " + "LIMIT " + page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(OrderListModel.class);

			orderList = query.list();

			tx.commit();
			return orderList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
			return orderCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询站点订单列表
	@SuppressWarnings("unchecked")
	public List<OrderListModel> getSiteOrderList(String siteId, String page, String rows) {
		List<OrderListModel> orderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT `order`.*," + "user.nickname," + "user.phoneNumber," + "user.personImageUrl," + "user.score,"
					+ "vehicle.nowLongitude," + "vehicle.nowLatitude," + "vehicle.plateNumber,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType) AS carTypeName "
					+ "FROM `order` LEFT JOIN user ON `order`.driverId = user.userId LEFT JOIN vehicle ON user.vehicleId = vehicle.vehicleId "
					+ "where `order`.driverId in (SELECT `user`.userId FROM `user` WHERE `user`.belongSiteId = '"
					+ siteId + "' AND `user`.type = '6') OR "
					+ "`order`.createManId in (SELECT `user`.userId FROM `user` WHERE `user`.belongSiteId = '" + siteId
					+ "' AND `user`.type = '5') " + "ORDER BY updateTime desc " + "LIMIT " + page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(OrderListModel.class);

			orderList = query.list();

			tx.commit();
			return orderList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "更新信息失败！", order);
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@Override
	public OrderListModel getBigOrderInfo(String orderId) {
		// TODO Auto-generated method stub
		OrderListModel orderListModel = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT `order`.*," + "user.nickname," + "user.phoneNumber," + "user.personImageUrl," + "user.score,"
					+ "vehicle.nowLongitude," + "vehicle.nowLatitude," + "vehicle.plateNumber,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType) AS carTypeName "
					+ "FROM "
					+ "`order` LEFT JOIN user ON `order`.driverId = user.userId LEFT JOIN vehicle ON user.vehicleId = vehicle.vehicleId "
					+ "where `order`.orderId = '" + orderId + "'";

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(OrderListModel.class);
			orderListModel = (OrderListModel) query.uniqueResult();

			tx.commit();
			return orderListModel;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String beginStr = dataStr + "-01 00:00:00";
			String endStr = dataStr + "-31 23:59:59";
			sql = "UPDATE `order` " + 
				  "SET `order`.WITHDRAWMONEYSTATUS='1',`order`.withdrawalId='"+ withdrawalId +"' " + 
				  "WHERE `order`.CREATETIME < '"+ endStr +"' AND `order`.CREATETIME > '"+ beginStr +"' AND `order`.withdrawMoneyStatus = '0' " + 
					     "AND (" + 
					     "(`order`.DRIVERID in (SELECT `user`.USERID FROM `user` WHERE `user`.BELONGSITEID = '"+ siteId +"' AND `user`.TYPE = '6')) OR " + 
					     "(`order`.CREATEMANID in (SELECT `user`.USERID FROM `user` WHERE `user`.BELONGSITEID = '"+ siteId +"' AND `user`.TYPE = '5')) OR " + 
					     "(`order`.DRIVERID in (SELECT `user`.USERID " + 
					                           "FROM `user` " + 
					                           "WHERE `user`.TYPE = '6' " + 
					                           "AND `user`.BELONGSITEID in" + 
					                                                     "(SELECT site.SITEID " + 
					                                                       "FROM site " + 
					                                                       "WHERE site.superSiteId = '"+ siteId +"' " + 
					                                                     ") " + 
					                           ") " + 
					    ") OR " + 
					    "(`order`.CREATEMANID in (SELECT `user`.USERID " + 
					                             "FROM `user` " + 
					                             "WHERE `user`.TYPE = '5' " + 
					                             "AND `user`.BELONGSITEID in " + 
					                                                         "(SELECT site.SITEID " + 
					                                                         "FROM site " + 
					                                                         "WHERE site.superSiteId = '"+ siteId +"' " + 
					                                                         ") " + 
					                             ") " + 
					    " ) " + 
					 " )";
			SQLQuery query = session.createSQLQuery(sql);
			query.executeUpdate();

			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "操作成功！", "");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "操作失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

}
