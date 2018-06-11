package com.gunmm.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.MessageDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.MessageModel;
import com.gunmm.utils.JSONUtils;

public class MessageImpl implements MessageDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<MessageModel> getMessageListByUserId(String userId, String page, String rows) {
		// TODO Auto-generated method stub

		List<MessageModel> messageList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT * "+
				"FROM messagemodel "+
				"where messagemodel.receiveUserId = '"+userId+"' "+
				"ORDER BY createTime desc " + 
				"LIMIT "+page+","+rows;
					
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(MessageModel.class);

			messageList = query.list();
			tx.commit();
			return messageList;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return messageList;
		} finally {
			if (tx != null) {
				tx = null;
			}
			
		}
	}

	@Override
	public JSONObject addMessage(MessageModel messageModel) {
		// TODO Auto-generated method stub
		messageModel.setCreateTime(new Date());
		messageModel.setIsRead("0");
		
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(messageModel);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "添加成功！", messageModel);
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
	public JSONObject updateMessageInfo(MessageModel messageModel) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(messageModel);
			tx.commit();
			
			return JSONUtils.responseToJsonString("1", "", "更新信息成功！", messageModel);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "更新信息失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	@Override
	public MessageModel getMessageById(String messageId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		MessageModel messageModel = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			hql = "from MessageModel where messageId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, messageId);
			messageModel = (MessageModel) query.uniqueResult();
			
			tx.commit();
			return messageModel;

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
	public JSONObject queryUnreadMessageCount(String userId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		String resultCount = "";
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			hql = "select count(*) " + 
				  "from MessageModel " + 
				  "where isRead = '0' and receiveUserId = '"+userId+"'";
			Query query = session.createQuery(hql);
			resultCount = query.uniqueResult().toString();
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "未读消息数查询成功！", resultCount);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "未读消息数查询失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

}
