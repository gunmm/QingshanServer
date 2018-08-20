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
import com.gunmm.dao.SiteDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.Site;
import com.gunmm.responseModel.SiteListModel;
import com.gunmm.utils.JSONUtils;

public class SiteImpl implements SiteDao {

	// 新建站点
	@Override
	public JSONObject addSite(Site site) {
		// TODO Auto-generated method stub
		if(judgeZhizhaoByNumber(site.getBusinessLicenseNumber())) {
			return JSONUtils.responseToJsonString("0", "营业执照已被注册！", "营业执照已被注册！添加失败！", "");
		}
		if(judgeIdCardByNumber(site.getLawsManIdCardNumber())) {
			return JSONUtils.responseToJsonString("0", "身份证已被注册！", "身份证已被注册！添加失败！", "");
		}
		site.setSiteId(UUID.randomUUID().toString());
		site.setCreateTime(new Date());
		site.setUpdateTime(new Date());
		if ("1".equals(site.getSiteType())) {
			site.setSiteCheckStatus("1");
		} else {
			site.setSiteCheckStatus("0");
		}
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(site);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "添加成功！", site.getSiteId());
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
	
	
	//查重营业执照
	private boolean judgeZhizhaoByNumber(String zhiZhaoNumber) {
		Transaction tx = null;
		String backZhiZhaoNumber = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			hql = "select businessLicenseNumber from Site where businessLicenseNumber = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, zhiZhaoNumber);
			backZhiZhaoNumber = (String) query.uniqueResult();
			
			tx.commit();
			if (backZhiZhaoNumber == null || "".equals(backZhiZhaoNumber)) {
				return false;
			}else {
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
	
	//查重法人身份证号
	private boolean judgeIdCardByNumber(String idCardNumber) {
		Transaction tx = null;
		String backIdCardNumber = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			hql = "select lawsManIdCardNumber from Site where lawsManIdCardNumber = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, idCardNumber);
			backIdCardNumber = (String) query.uniqueResult();
			
			tx.commit();
			if (backIdCardNumber == null || "".equals(backIdCardNumber)) {
				return false;
			}else {
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
	

	// 删除站点
	@Override
	public JSONObject deleteSiteById(String siteId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		Site site = getSiteById(siteId);
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.delete(site);
			tx.commit();

			if (site != null) {
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

	// 查询站点列表
	@SuppressWarnings("unchecked")
	@Override
	public List<SiteListModel> getSiteList(String page, String rows, String filterSiteName, String filterLawsManName, String filterBeginTime, String filterEndTime, String superSiteId) {
		// TODO Auto-generated method stub
		List<SiteListModel> siteList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String sql1 = "SELECT site.*," 
					 +"(select name from City where id = site.siteProvince and deep = 1) AS siteProvinceName,"
					 +"(select name from City where id = site.siteCity and parent_id = site.siteProvince and deep = 2) AS siteCityName,"
					 +"(select count(*) from site childSite where childSite.superSiteId = site.siteId) AS childCount "
				     + "FROM site "
				     + "where site.siteName like '%"+filterSiteName+"%' and site.lawsManName like '%"+filterLawsManName+"%' ";
			String sql2 = "";
			if(filterBeginTime.length() > 0 && filterEndTime.length() > 0) {
				sql2 = "and (site.createTime BETWEEN '"+filterBeginTime+" 00:00:00"+"' AND '"+filterEndTime+" 23:59:59"+"') ";
			}else if (filterBeginTime.length() > 0) {
				sql2 = "and (site.createTime > '"+filterBeginTime+" 00:00:00') ";
			}else if (filterEndTime.length() > 0) {
				sql2 = "and (site.createTime < '"+filterEndTime+" 23:59:59') ";
			}
			String sql4 = "";
			if (superSiteId != null) {
				if (superSiteId.length() > 0) {
					sql4 = "and site.superSiteId = '"+ superSiteId +"' ";
				}
			}
			String sql3 = "ORDER BY updateTime desc "
				     + "LIMIT " + page + "," + rows;
			sql = sql1 + sql2 + sql4 +sql3;
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(SiteListModel.class);

			siteList = query.list();

			tx.commit();
			return siteList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return siteList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 根据站点id拿站点信息
	@Override
	public Site getSiteById(String siteId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		Site site = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory()
					.getCurrentSession();
			tx = session.beginTransaction();
			hql = "from Site where siteId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, siteId);
			site = (Site) query.uniqueResult();
			
			tx.commit();
			return site;

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

	// 更新站点
	@Override
	public JSONObject updateSiteInfo(Site site) {
		// TODO Auto-generated method stub
		site.setUpdateTime(new Date());
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(site);
			tx.commit();

			return JSONUtils.responseToJsonString("1", "", "更新信息成功！", site);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "更新信息失败！", site);
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 查询站点条数
	@Override
	public Long getSiteCount(String filterSiteName, String filterLawsManName, String filterBeginTime, String filterEndTime, String superSiteId) {
		// TODO Auto-generated method stub
		Transaction tx = null;
		Long siteCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String sql1 = "select count(*) from site " 
				     + "where site.siteName like '%"+filterSiteName+"%' and site.lawsManName like '%"+filterLawsManName+"%' ";
			String sql2 = "";
			if(filterBeginTime.length() > 0 && filterEndTime.length() > 0) {
				sql2 = "and (site.createTime BETWEEN '"+filterBeginTime+" 00:00:00"+"' AND '"+filterEndTime+" 23:59:59"+"') ";
			}else if (filterBeginTime.length() > 0) {
				sql2 = "and (site.createTime > '"+filterBeginTime+" 00:00:00') ";
			}else if (filterEndTime.length() > 0) {
				sql2 = "and (site.createTime < '"+filterEndTime+" 23:59:59') ";
			}
			
			String sql4 = "";
			if (superSiteId != null) {
				if (superSiteId.length() > 0) {
					sql4 = "and site.superSiteId = '"+ superSiteId +"' ";
				}
			}
			sql = sql1 + sql4 + sql2;
			
			SQLQuery query = session.createSQLQuery(sql);
			siteCount = ((BigInteger)query.uniqueResult()).longValue();


			tx.commit();
			return siteCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return siteCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

}
