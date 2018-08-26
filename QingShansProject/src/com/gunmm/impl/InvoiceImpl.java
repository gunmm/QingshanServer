package com.gunmm.impl;

import java.util.Date;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.InvoiceDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.Invoice;
import com.gunmm.utils.JSONUtils;

public class InvoiceImpl implements InvoiceDao {

	// 添加发票
	public JSONObject addInvoiceDao(Invoice invoice) {
		invoice.setInvoiceId(UUID.randomUUID().toString());
		invoice.setStatus("0");
		invoice.setCreateTime(new Date());
		invoice.setUpdateTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(invoice);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "添加成功！", invoice.getInvoiceId());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "添加失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 根据ID拿invoice
	@Override
	public Invoice getInvoiceById(String invoiceId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		Invoice invoice = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from Invoice where invoiceId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, invoiceId);
			invoice = (Invoice) query.uniqueResult();

			tx.commit();
			return invoice;

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

	// 更新订单信息
	public JSONObject updateInvoiceInfo(Invoice invoice) {
		invoice.setUpdateTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(invoice);
			tx.commit();
			
			return JSONUtils.responseToJsonString("1", "", "更新信息成功！", invoice);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "更新信息失败！", invoice);
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 删除invoice
	@Override
	public JSONObject deleteInvoice(String invoiceId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		Invoice invoice = getInvoiceById(invoiceId);
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.delete(invoice);
			tx.commit();

			if (invoice != null) {
				return JSONUtils.responseToJsonString("1", "", "删除成功！", "");
			}
			return JSONUtils.responseToJsonString("0", "对应记录不存在", "删除失败！", "");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "删除失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

}
