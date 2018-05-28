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
	public String robOrder(String driverId, String orderId) {
		Order order = getOrderById(orderId);
		if(order == null) {
			return "对应订单不存在！";
		} else if (!order.getStatus().equals("0")) {
			if (order.getStatus().equals("9")) {
				return "订单取消！";
			}
			return "订单已被抢走！";
		} else {
			order.setStatus("1");
			order.setDriverId(driverId);
			Transaction tx = null;
			try {
				Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
				tx = session.beginTransaction();
				session.update(order);
				tx.commit();
				
				UserDao userDao = new UserDaoImpl();
				User user = userDao.getUserById(driverId);
				if(user != null) {
					userDao.setUserStatus("1", user);
				}
				return "success";
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return e.getCause().getMessage();
			} finally {
				if (tx != null) {
					tx = null;
				}
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
	public String cancelOrderById(String orderId) {
		// TODO Auto-generated method stub
		Order order = getOrderById(orderId);
		if(order == null) {
			return "对应订单不存在！";
		}  else {
			order.setStatus("9");
			Transaction tx = null;
			try {
				Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
				tx = session.beginTransaction();
				session.update(order);
				tx.commit();
				
				if(order.getDriverId()!=null) {
					UserDao userDao = new UserDaoImpl();
					userDao.setUserStatus("0", userDao.getUserById(order.getDriverId()));
				}
				
				return "success";
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return e.getCause().getMessage();
			} finally {
				if (tx != null) {
					tx = null;
				}
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
	
	

	
}
