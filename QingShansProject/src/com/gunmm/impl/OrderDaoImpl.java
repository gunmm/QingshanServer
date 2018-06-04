package com.gunmm.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.OrderDao;
import com.gunmm.dao.UserDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.Order;
import com.gunmm.model.User;
import com.gunmm.responseModel.OrderListModel;
import com.gunmm.utils.JSONUtils;

public class OrderDaoImpl implements OrderDao {

	@Override  //添加订单
	public JSONObject addOrder(Order order) {
		// TODO Auto-generated method stub
		order.setOrderId(UUID.randomUUID().toString());
		order.setCreateTime(new Date());
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
	@Override   //请求300公里以内司机
	public List<User> queryDriverForOrder(Order order) {
		// TODO Auto-generated method stub
		List<User> userList = null;
		Transaction tx = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			hql = 
					"select new User(userId,phoneNumber,nickname,personImageUrl,plateNumber,vehicleType,loginPlate,nowLatitude,nowLongitude," + 
					"(ACOS(SIN(("+order.getSendLatitude()+" * 3.1415) / 180 ) *SIN((NOWLATITUDE * 3.1415) / 180 ) +COS(("+order.getSendLatitude()+" * 3.1415) / 180 ) * COS((NOWLATITUDE * 3.1415) / 180 ) *COS(("+order.getSendLongitude()+"* 3.1415) / 180 - (NOWLONGITUDE * 3.1415) / 180 ) ) * 6380) AS distance,score) " + 
					"from User " + 
					"WHERE (ACOS(SIN(("+order.getSendLatitude()+" * 3.1415) / 180 ) *SIN((NOWLATITUDE * 3.1415) / 180 ) +COS(("+order.getSendLatitude()+" * 3.1415) / 180 ) * COS((NOWLATITUDE * 3.1415) / 180 ) *COS(("+order.getSendLongitude()+"* 3.1415) / 180 - (NOWLONGITUDE * 3.1415) / 180 ) ) * 6380) < 300 AND type = '2' AND status = '0' AND vehicleType = '"+order.getCarType()+"' "
					+"order by (ACOS(SIN(("+order.getSendLatitude()+" * 3.1415) / 180 ) *SIN((NOWLATITUDE * 3.1415) / 180 ) +COS(("+order.getSendLatitude()+" * 3.1415) / 180 ) * COS((NOWLATITUDE * 3.1415) / 180 ) *COS(("+order.getSendLongitude()+"* 3.1415) / 180 - (NOWLONGITUDE * 3.1415) / 180 ) ) * 6380)";
			Query query = session.createQuery(hql);
			query.setMaxResults(30);
			userList = query.list();
			
			
			tx.commit();
			return userList;
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return userList;
		} finally {
			if (tx != null) {
				tx = null;
			}
			
		}
	}

	

	@Override
	public JSONObject robOrder(String driverId, String orderId) {
		Order order = getOrderById(orderId);
		if(order == null) {
			return JSONUtils.responseToJsonString("0", "", "对应订单不存在！", "");
		} else if (!order.getStatus().equals("0")) {
			if (order.getStatus().equals("9")) {
				return JSONUtils.responseToJsonString("0", "订单已被取消！", "订单已被取消！", "");
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
			if ("2".equals(order.getType())) {
				order.setAppointStatus("0");
			}
			JSONObject jsonObj = updateOrderInfo(order);
			String result_code = jsonObj.getString("result_code");
			String reason = jsonObj.getString("reason");

			if ("1".equals(result_code)) {
				if(user != null && "1".equals(order.getType())) {
					user.setStatus("1");
					userDao.updateUserInfo(user);
				}
				return JSONUtils.responseToJsonString("1", "", "抢单成功！", "");
			}else {
				return JSONUtils.responseToJsonString("0", reason, "抢单失败！", "");

			}
			
		}
		
	}

	//根据订单ID拿订单
	@Override
	public Order getOrderById(String orderId) {
		Transaction tx = null;
		Order order = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
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

	@Override
	public JSONObject cancelOrderById(String orderId) {
		// TODO Auto-generated method stub
		Order order = getOrderById(orderId);
		if(order == null) {
			return JSONUtils.responseToJsonString("0", "对应订单不存在！", "取消订单失败！", "");
		}  else {
			order.setStatus("9");
			JSONObject jsonObj = updateOrderInfo(order);
			String result_code = jsonObj.getString("result_code");
			String reason = jsonObj.getString("reason");

			if ("1".equals(result_code)) {
				if(order.getDriverId()!=null) {
					UserDao userDao = new UserDaoImpl();
					User user = userDao.getUserById(order.getDriverId());
					user.setStatus("0");
					userDao.updateUserInfo(user);
				}
				return JSONUtils.responseToJsonString("1", "", "取消订单成功！", "");
			}else {
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
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT "+
					"`order`.*,"+
					"user.nickname,"+
					"user.phoneNumber,"+
					"user.personImageUrl,"+
					"user.nowLatitude,"+
					"user.nowLongitude,"+
					"user.plateNumber,"+
					"user.score,"+
					"(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType) AS carTypeName "+
				"FROM "+
				"`order` LEFT JOIN user ON `order`.driverId = user.userId "+
				"where `order`.createManId = '"+userId+"' "+
				"ORDER BY CREATETIME desc " + 
				"LIMIT "+page+","+rows;
					
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

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderListModel> getDriverOrderListByDriverId(String driverId, String page, String rows) {
		// TODO Auto-generated method stub
		List<OrderListModel> orderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT "+
					"`order`.*,"+
					"user.nickname,"+
					"user.phoneNumber,"+
					"user.personImageUrl,"+
					"user.nowLatitude,"+
					"user.nowLongitude,"+
					"user.plateNumber,"+
					"user.score,"+
					"(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType) AS carTypeName "+
				"FROM "+
				"`order` LEFT JOIN user ON `order`.driverId = user.userId "+
				"where `order`.driverId = '"+driverId+"' "+
				"ORDER BY CREATETIME desc " + 
				"LIMIT "+page+","+rows;
					
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

	//司机设置预约订单开始执行
	@Override
	public JSONObject setAppointOrderBegin(String driverId, String orderId) {
		// TODO Auto-generated method stub
		Order order = getOrderById(orderId);
		UserDao userDao = new UserDaoImpl();
		User driver = userDao.getUserById(driverId);
		if(order == null || driver == null) {
			return JSONUtils.responseToJsonString("0", "对应订单或司机不存在！", "执行失败！", "");
		} else {
			order.setAppointStatus("1");
			JSONObject jsonObj = updateOrderInfo(order);
			String result_code = jsonObj.getString("result_code");
			String reason = jsonObj.getString("reason");
			if ("1".equals(result_code)) {
				driver.setStatus("1");
				userDao.updateUserInfo(driver);
				return JSONUtils.responseToJsonString("1", "", "执行成功！", "");
			}else {
				return JSONUtils.responseToJsonString("0", reason, "执行失败！", "");
			}
		}
	}

	@Override
	public JSONObject updateOrderInfo(Order order) {
		// TODO Auto-generated method stub
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
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT "+
					"`order`.*,"+
					"user.nickname,"+
					"user.phoneNumber,"+
					"user.personImageUrl,"+
					"user.nowLatitude,"+
					"user.nowLongitude,"+
					"user.plateNumber,"+
					"user.score,"+
					"(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType) AS carTypeName "+
				"FROM "+
				"`order` LEFT JOIN user ON `order`.driverId = user.userId "+
				"where `order`.orderId = '"+orderId+"'";
				
					
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
	
	

	
}
