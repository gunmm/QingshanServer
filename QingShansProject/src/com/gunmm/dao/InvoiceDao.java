package com.gunmm.dao;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Invoice;

public interface InvoiceDao {
	
	// 添加发票
	public JSONObject addInvoiceDao(Invoice invoice);
	
	//根据ID拿invoice
	public Invoice getInvoiceById(String invoiceId);
	
	//更新订单信息
	public JSONObject updateInvoiceInfo(Invoice invoice);
	
	// 删除invoice
	public JSONObject deleteInvoice(String invoiceId);

}
