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
import com.gunmm.dao.InvoiceDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.Invoice;
import com.gunmm.responseModel.InvoiceResModel;
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
			if (null != tx && tx.isActive()) {
				try {
					tx.rollback();
				} catch (Exception re) {
					// use logging framework here
					re.printStackTrace();
				}
			}
			e.printStackTrace();
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

	// 根据invoiceid查询invoice信息和部分订单信息
	public JSONObject getInvoiceDetailById(String invoiceId) {
		InvoiceResModel invoiceResModel = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT invoice.*,`order`.finishTime,`order`.orderId,`order`.status as orderStatus "
					+ "FROM invoice,`order`  " + "where invoice.invoiceId = '" + invoiceId
					+ "' and `order`.invoiceId = '" + invoiceId + "'";

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(InvoiceResModel.class);
			invoiceResModel = (InvoiceResModel) query.uniqueResult();

			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "查询成功！", invoiceResModel);

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
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "查询失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询发票列表
	@SuppressWarnings("unchecked")
	public List<InvoiceResModel> getInvoiceList(String status, String invoiceType, String receiverName, String page,
			String rows) {
		List<InvoiceResModel> invoiceResModelList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			sql = "SELECT invoice.*,`order`.finishTime,`order`.orderId,`order`.status as orderStatus "
					+ "FROM invoice,`order`  " + "where `order`.invoiceId = invoice.invoiceId AND `order`.status = '4' "
					+ "AND invoice.status like '%" + status + "%' AND invoice.invoiceType like '%" + invoiceType
					+ "%'  AND invoice.receiverName like '%" + receiverName + "%' " + "ORDER BY finishTime desc "
					+ "LIMIT " + page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(InvoiceResModel.class);

			invoiceResModelList = query.list();

			tx.commit();
			return invoiceResModelList;

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
			return invoiceResModelList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询发票列表条数
	public Long getInvoiceListCount(String status, String invoiceType, String receiverName) {
		Transaction tx = null;
		Long invoiceCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT  count(*)" + "FROM invoice,`order`  " + "where `order`.invoiceId = invoice.invoiceId "
					+ "AND invoice.status like '%" + status + "%' AND invoice.invoiceType like '%" + invoiceType
					+ "%'  AND invoice.receiverName like '%" + receiverName + "%' ";

			SQLQuery query = session.createSQLQuery(sql);
			invoiceCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return invoiceCount;

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
			return invoiceCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

}
