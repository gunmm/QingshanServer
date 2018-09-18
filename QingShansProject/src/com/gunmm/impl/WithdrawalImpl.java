package com.gunmm.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.WithdrawalDao;
import com.gunmm.db.MyHibernateSessionFactory;
import com.gunmm.model.Withdrawal;
import com.gunmm.responseModel.WithDrawalFinishedListModel;
import com.gunmm.responseModel.WithdrawalListModel;
import com.gunmm.responseModel.WithdrawalOrderListModel;
import com.gunmm.utils.JSONUtils;

public class WithdrawalImpl implements WithdrawalDao {

	// 获取父站点待提现列表
	@SuppressWarnings("unchecked")
	public List<WithdrawalListModel> getSiteWithdrawalList(String dataStr, String page, String rows,
			String filterSiteName, String filterLowsManName) {
		List<WithdrawalListModel> withdrawalList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			String beginStr = dataStr + "-01 00:00:00";
			String endStr = dataStr + "-31 23:59:59";
			sql = "SELECT *,"
					+ "convert((ifnull(registerDriverFee,0) + ifnull(registerGoodsManFee,0) + ifnull(childDriverFee,0) + ifnull(childGoodsManFee,0)),decimal(12,2)) AS totalFee "
					+ "FROM " + "(SELECT  superSite.siteId, " + "superSite.siteName,superSite.childToSuperRate,"
					+ "`user`.bankCardNumber," + "`user`.userId," + "`user`.nickname,"
					+ "convert((SELECT SUM(servicePrice) " + "FROM `order` "
					+ "WHERE `order`.driverId in (SELECT `user`.userId FROM `user` WHERE `user`.belongSiteId = superSite.siteId AND `user`.type = '6') "
					+ "AND `order`.`status` = '4' " + "AND `order`.finishTime > '" + beginStr + "' "
					+ "AND `order`.finishTime < '" + endStr + "' " + "AND `order`.driverSiteWithdrawStatus = '0'"
					+ ") * 0.3,decimal(12,2)) AS registerDriverFee," + "convert((SELECT SUM(servicePrice) "
					+ "FROM `order` "
					+ "WHERE `order`.createManId in (SELECT `user`.userId FROM `user` WHERE `user`.belongSiteId = superSite.siteId AND `user`.type = '5') "
					+ "AND `order`.`STATUS` = '4' " + "AND `order`.finishTime > '" + beginStr + "' "
					+ "AND `order`.finishTime < '" + endStr + "' " + "AND `order`.masterSiteWithdrawStatus = '0'"
					+ ") * 0.5,decimal(12,2)) AS registerGoodsManFee," +

					"(SELECT SUM(registerDriverFeeChile) " + "FROM (" + "SELECT " + "childSite.SUPERSITEID,"
					+ "convert((SELECT SUM(servicePrice) " + "FROM `order` "
					+ "WHERE `order`.DRIVERID in (SELECT `user`.USERID FROM `user` WHERE `user`.BELONGSITEID = childSite.SITEID AND `user`.TYPE = '6') "
					+ "AND `order`.`STATUS` = '4' " + "AND `order`.finishTime > '" + beginStr + "' "
					+ "AND `order`.finishTime < '" + endStr + "' " + "AND `order`.driverSiteWithdrawStatus = '0'"
					+ ") * 0.3, decimal(12,2)) AS registerDriverFeeChile " + "FROM site childSite "
					+ "WHERE childSite.SITETYPE = '2'" + ") temp " + "WHERE temp.SUPERSITEID = superSite.SITEID "
					+ ") AS childDriverFee," +

					"(SELECT SUM(registerGoodsManFeeChild) " + "FROM (" + "SELECT " + "childSite.SUPERSITEID,"
					+ "convert((SELECT SUM(servicePrice) " + "FROM `order` "
					+ "WHERE `order`.CREATEMANID in (SELECT `user`.USERID FROM `user` WHERE `user`.BELONGSITEID = childSite.SITEID AND `user`.TYPE = '5') "
					+ "AND `order`.`STATUS` = '4' " + "AND `order`.finishTime > '" + beginStr + "' "
					+ "AND `order`.finishTime < '" + endStr + "' " + "AND `order`.masterSiteWithdrawStatus = '0'"
					+ ")* 0.5 ,decimal(12,2)) AS registerGoodsManFeeChild " + "FROM site childSite "
					+ "WHERE childSite.SITETYPE = '2'" + ") temp " + "WHERE temp.SUPERSITEID = superSite.SITEID"
					+ ") AS childGoodsManFee " +

					"FROM site superSite,`user`"
					+ "WHERE superSite.SITETYPE = '1' AND `user`.BELONGSITEID = superSite.SITEID AND `user`.TYPE = '3' AND superSite.siteName like '%"
					+ filterSiteName + "%' AND `user`.nickname like '%" + filterLowsManName + "%') midTabel "
					+ "ORDER BY totalFee DESC " + "LIMIT " + page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(WithdrawalListModel.class);

			withdrawalList = query.list();

			tx.commit();
			return withdrawalList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return withdrawalList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询父站点待提现列表条数
	public Long getSiteWithdrawalListCount(String dataStr, String filterSiteName, String filterLowsManName) {
		Transaction tx = null;
		Long withdrawalCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT  count(*)" + "FROM site superSite,`user`"
					+ "WHERE superSite.SITETYPE = '1' AND `user`.BELONGSITEID = superSite.SITEID AND `user`.TYPE = '3' AND superSite.siteName like '%"
					+ filterSiteName + "%' AND `user`.nickname like '%" + filterLowsManName + "%' ";
			SQLQuery query = session.createSQLQuery(sql);
			withdrawalCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return withdrawalCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return withdrawalCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 获取子站点待提现列表
	@SuppressWarnings("unchecked")
	public List<WithdrawalListModel> getChildSiteWithdrawalList(String dataStr, String siteId, String type, String page,
			String rows, String filterSiteName, String filterLowsManName) {
		List<WithdrawalListModel> withdrawalList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String typeStr = "";
			if (type.equals("3")) {
				typeStr = "registerDriverFee+0";
			} else if (type.equals("4")) {
				typeStr = "registerGoodsManFee+0";
			} else {
				typeStr = "totalFee";
			}
			String beginStr = dataStr + "-01 00:00:00";
			String endStr = dataStr + "-31 23:59:59";

			sql = "SELECT *,"
					+ "convert((ifnull(registerDriverFee,0) + ifnull(registerGoodsManFee,0)),decimal(12,2)) AS totalFee "
					+ "FROM (SELECT siteId,siteName,childToSuperRate,`user`.USERID,`user`.NICKNAME, `user`.bankCardNumber, null AS childDriverFee, null AS childGoodsManFee,"
					+ "convert((SELECT SUM(servicePrice) " + "FROM `order` "
					+ "WHERE `order`.DRIVERID in (SELECT `user`.USERID " + "FROM `user` "
					+ "WHERE `user`.BELONGSITEID = site.SITEID AND `user`.TYPE = '6') " + "AND `order`.`STATUS` = '4' "
					+ "AND `order`.finishTime > '" + beginStr + "' " + "AND `order`.finishTime < '" + endStr + "' "
					+ "AND `order`.driverSiteWithdrawStatus = '0'" + ") * 0.3 ,decimal(12,2)) AS registerDriverFee,"
					+ "convert((SELECT SUM(servicePrice) " + "FROM `order` "
					+ "WHERE `order`.createManId in (SELECT `user`.USERID " + "FROM `user` "
					+ "WHERE `user`.BELONGSITEID = site.SITEID AND `user`.TYPE = '5') " + "AND `order`.`STATUS` = '4' "
					+ "AND `order`.finishTime > '" + beginStr + "' " + "AND `order`.finishTime < '" + endStr + "' "
					+ "AND `order`.masterSiteWithdrawStatus = '0'" + ") * 0.5 ,decimal(12,2)) AS registerGoodsManFee "
					+ "FROM site,`user` " + "WHERE site.SITETYPE = '2' AND site.SUPERSITEID = '" + siteId + "' "
					+ "AND `user`.BELONGSITEID = site.SITEID AND `user`.TYPE = '3' AND site.siteName like '%"
					+ filterSiteName + "%' AND `user`.nickname like '%" + filterLowsManName + "%') midTabel "
					+ "ORDER BY " + typeStr + " DESC " + "LIMIT " + page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(WithdrawalListModel.class);

			withdrawalList = query.list();

			tx.commit();
			return withdrawalList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return withdrawalList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询子站点待提现列表条数
	public Long getChildSiteWithdrawalListCount(String dataStr, String siteId, String filterSiteName,
			String filterLowsManName) {
		Transaction tx = null;
		Long withdrawalCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT  count(*)" + "FROM site,`user` " + "WHERE site.SITETYPE = '2' AND site.SUPERSITEID = '"
					+ siteId + "' AND `user`.BELONGSITEID = site.SITEID AND `user`.TYPE = '3' AND site.siteName like '%"
					+ filterSiteName + "%' AND `user`.nickname like '%" + filterLowsManName + "%' ";
			SQLQuery query = session.createSQLQuery(sql);
			withdrawalCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return withdrawalCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return withdrawalCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 获取子站点待提现列表 站点视角
	@SuppressWarnings("unchecked")
	public List<WithdrawalListModel> getChildSiteWithdrawalListForSite(String dataStr, String siteId, String type,
			String page, String rows, String filterSiteName, String filterLowsManName) {
		List<WithdrawalListModel> withdrawalList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String typeStr = "totalFee";

			String beginStr = dataStr + "-01 00:00:00";
			String endStr = dataStr + "-31 23:59:59";

			sql = "SELECT *,"
					+ "convert((ifnull(registerDriverFee,0) + ifnull(registerGoodsManFee,0)),decimal(12,2)) AS totalFee "
					+ "FROM (SELECT siteId,siteName,childToSuperRate,`user`.USERID,`user`.NICKNAME, `user`.bankCardNumber, null AS childDriverFee, null AS childGoodsManFee,"
					+ "convert((SELECT SUM(servicePrice) " + "FROM `order` "
					+ "WHERE `order`.DRIVERID in (SELECT `user`.USERID " + "FROM `user` "
					+ "WHERE `user`.BELONGSITEID = site.SITEID AND `user`.TYPE = '6') " + "AND `order`.`STATUS` = '4' "
					+ "AND `order`.finishTime > '" + beginStr + "' " + "AND `order`.finishTime < '" + endStr + "' "
					+ ") * 0.3 ,decimal(12,2)) AS registerDriverFee," + "convert((SELECT SUM(servicePrice) "
					+ "FROM `order` " + "WHERE `order`.createManId in (SELECT `user`.USERID " + "FROM `user` "
					+ "WHERE `user`.BELONGSITEID = site.SITEID AND `user`.TYPE = '5') " + "AND `order`.`STATUS` = '4' "
					+ "AND `order`.finishTime > '" + beginStr + "' " + "AND `order`.finishTime < '" + endStr + "' "
					+ ") * 0.5 ,decimal(12,2)) AS registerGoodsManFee " + "FROM site,`user` "
					+ "WHERE site.SITETYPE = '2' AND site.SUPERSITEID = '" + siteId + "' "
					+ "AND `user`.BELONGSITEID = site.SITEID AND `user`.TYPE = '3' AND site.siteName like '%"
					+ filterSiteName + "%' AND `user`.nickname like '%" + filterLowsManName + "%') midTabel "
					+ "ORDER BY " + typeStr + " DESC " + "LIMIT " + page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(WithdrawalListModel.class);

			withdrawalList = query.list();

			tx.commit();
			return withdrawalList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return withdrawalList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询子站点待提现列表条数 站点视角
	public Long getChildSiteWithdrawalListCountForSite(String dataStr, String siteId, String filterSiteName,
			String filterLowsManName) {
		Transaction tx = null;
		Long withdrawalCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT  count(*)" + "FROM site,`user` " + "WHERE site.SITETYPE = '2' AND site.SUPERSITEID = '"
					+ siteId + "' AND `user`.BELONGSITEID = site.SITEID AND `user`.TYPE = '3' AND site.siteName like '%"
					+ filterSiteName + "%' AND `user`.nickname like '%" + filterLowsManName + "%' ";
			SQLQuery query = session.createSQLQuery(sql);
			withdrawalCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return withdrawalCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return withdrawalCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 获取父站点司机总提成订单对应的订单记录列表
	@SuppressWarnings("unchecked")
	public List<WithdrawalOrderListModel> getTotalDriverItemOrderListBySiteId(String dataStr, String siteId,
			String page, String rows, String isChildSiteList) {
		List<WithdrawalOrderListModel> withdrawalOrderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			String statusSqlStr = "AND `order`.driverSiteWithdrawStatus = '0' ";
			if ("1".equals(isChildSiteList)) {
				statusSqlStr = "";
			}
			String beginStr = dataStr + "-01 00:00:00";
			String endStr = dataStr + "-31 23:59:59";
			sql = "SELECT orderId,carType,distance,price,servicePrice,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType limit 1) AS carTypeName,"
					+ "finishTime " + "FROM `order` "
					+ "WHERE `order`.DRIVERID in (SELECT `user`.USERID FROM `user` WHERE `user`.BELONGSITEID = '"
					+ siteId + "' AND `user`.TYPE = '6') " + "AND `order`.`STATUS` = '4' "
					+ "AND `order`.finishTime > '" + beginStr + "' " + "AND `order`.finishTime < '" + endStr + "' "
					+ statusSqlStr + "ORDER BY finishTime desc " + "LIMIT " + page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(WithdrawalOrderListModel.class);

			withdrawalOrderList = query.list();

			tx.commit();
			return withdrawalOrderList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return withdrawalOrderList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}

	}

	// 查询父站点货主总提成订单对应的订单记录列表条数
	public Long getTotalDriverItemOrderListCount(String dataStr, String siteId, String isChildSiteList) {
		Transaction tx = null;
		Long withdrawalCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String statusSqlStr = "AND `order`.driverSiteWithdrawStatus = '0' ";
			if ("1".equals(isChildSiteList)) {
				statusSqlStr = "";
			}
			String beginStr = dataStr + "-01 00:00:00";
			String endStr = dataStr + "-31 23:59:59";
			sql = "SELECT  count(*)" + "FROM `order` "
					+ "WHERE `order`.DRIVERID in (SELECT `user`.USERID FROM `user` WHERE `user`.BELONGSITEID = '"
					+ siteId + "' AND `user`.TYPE = '6') " + "AND `order`.`STATUS` = '4' "
					+ "AND `order`.finishTime > '" + beginStr + "' " + "AND `order`.finishTime < '" + endStr + "' "
					+ statusSqlStr;
			SQLQuery query = session.createSQLQuery(sql);
			withdrawalCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return withdrawalCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return withdrawalCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}

	}

	// 获取父站点货主总提成订单对应的订单记录列表
	@SuppressWarnings("unchecked")
	public List<WithdrawalOrderListModel> getTotalMasterItemOrderListBySiteId(String dataStr, String siteId,
			String page, String rows, String isChildSiteList) {
		List<WithdrawalOrderListModel> withdrawalOrderList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String statusSqlStr = "AND `order`.masterSiteWithdrawStatus = '0' ";
			if ("1".equals(isChildSiteList)) {
				statusSqlStr = "";
			}
			String beginStr = dataStr + "-01 00:00:00";
			String endStr = dataStr + "-31 23:59:59";
			sql = "SELECT orderId,carType,distance,price,servicePrice,"
					+ "(select valueText from DictionaryModel where name = '车辆类型' and keyText = `order`.carType limit 1) AS carTypeName,"
					+ "finishTime " + "FROM `order` "
					+ "WHERE `order`.createManId in (SELECT `user`.USERID FROM `user` WHERE `user`.BELONGSITEID = '"
					+ siteId + "' AND `user`.TYPE = '5') " + "AND `order`.`STATUS` = '4' "
					+ "AND `order`.finishTime > '" + beginStr + "' " + "AND `order`.finishTime < '" + endStr + "' "
					+ statusSqlStr + "ORDER BY finishTime desc " + "LIMIT " + page + "," + rows;

			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(WithdrawalOrderListModel.class);

			withdrawalOrderList = query.list();

			tx.commit();
			return withdrawalOrderList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return withdrawalOrderList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询父站点货主总提成订单对应的订单记录列表条数
	public Long getTotalMasterItemOrderListCount(String dataStr, String siteId, String isChildSiteList) {
		Transaction tx = null;
		Long withdrawalCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			String statusSqlStr = "AND `order`.masterSiteWithdrawStatus = '0' ";
			if ("1".equals(isChildSiteList)) {
				statusSqlStr = "";
			}
			String beginStr = dataStr + "-01 00:00:00";
			String endStr = dataStr + "-31 23:59:59";
			sql = "SELECT  count(*)" + "FROM `order` "
					+ "WHERE `order`.createManId in (SELECT `user`.USERID FROM `user` WHERE `user`.BELONGSITEID = '"
					+ siteId + "' AND `user`.TYPE = '5') " + "AND `order`.`STATUS` = '4' "
					+ "AND `order`.finishTime > '" + beginStr + "' " + "AND `order`.finishTime < '" + endStr + "' "
					+ statusSqlStr;
			SQLQuery query = session.createSQLQuery(sql);
			withdrawalCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return withdrawalCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return withdrawalCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 新建提现记录
	public JSONObject addWithdrawal(Withdrawal withdrawal) {
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(withdrawal);
			tx.commit();
			return JSONUtils.responseToJsonString("1", "", "操作成功！", withdrawal.getWithdrawalId());
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

	// 编辑提现记录
	public JSONObject editWithdrawal(Withdrawal withdrawal) {
		Transaction tx = null;
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(withdrawal);
			tx.commit();

			return JSONUtils.responseToJsonString("1", "", "更新信息成功！", "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return JSONUtils.responseToJsonString("0", e.getCause().getMessage(), "更新信息失败！", "");
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

	// 获取提现记录by id
	public Withdrawal getWithdrawalById(String withdrawalId) {
		Transaction tx = null;
		Withdrawal withdrawal = null;
		String hql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "from Withdrawal where withdrawalId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, withdrawalId);
			withdrawal = (Withdrawal) query.uniqueResult();

			tx.commit();
			return withdrawal;

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

	// 删除提现记录
	public JSONObject deleteWithdrawal(String withdrawalId) {
		Transaction tx = null;
		Withdrawal withdrawal = getWithdrawalById(withdrawalId);

		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.delete(withdrawal);
			tx.commit();

			if (withdrawal != null) {
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

	// 查询已提现列表
	@SuppressWarnings("unchecked")
	public List<WithDrawalFinishedListModel> getFinishedWithDrawalList(String bankcardNumber, String page, String rows)	 {
		List<WithDrawalFinishedListModel> withdrawalFinishedList = null;
		Transaction tx = null;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			
			sql = "SELECT *," + 
					      "(SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = withdrawal.TOUSERID) as toUserName," + 
					      "(SELECT site.SITENAME FROM site WHERE site.SITEID = withdrawal.toSiteId) AS siteName," + 
					      "(SELECT `user`.NICKNAME FROM `user` WHERE `user`.USERID = withdrawal.OPRATIONUSERID) as oprationUserName " + 
				  "FROM withdrawal " + 
				  "WHERE withdrawal.TOBANKNUMBER like '%" + bankcardNumber + "%' " + "ORDER BY withdrawalTime desc " + "LIMIT " + page + "," + rows;
					
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(WithDrawalFinishedListModel.class);

			withdrawalFinishedList = query.list();

			tx.commit();
			return withdrawalFinishedList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return withdrawalFinishedList;
		} finally {
			if (tx != null) {
				tx = null;
			}

		}
	}

	// 查询已提现列表条数
	public Long getFinishedWithDrawalListCount(String bankcardNumber) {
		Transaction tx = null;
		Long withdrawalCount = (long) 0;
		String sql = "";
		try {
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();

			sql = "SELECT  count(*)" + "FROM withdrawal "
					+ "WHERE withdrawal.TOBANKNUMBER like '%" + bankcardNumber + "%' " ;
					
			SQLQuery query = session.createSQLQuery(sql);
			withdrawalCount = ((BigInteger) query.uniqueResult()).longValue();

			tx.commit();
			return withdrawalCount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tx.commit();
			return withdrawalCount;
		} finally {
			if (tx != null) {
				tx = null;
			}
		}
	}

}
