package com.gunmm.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.model.Invoice;
import com.gunmm.responseModel.InvoiceResModel;

public interface InvoiceDao {

	// 添加发票
	public JSONObject addInvoiceDao(Invoice invoice);

	// 根据ID拿invoice
	public Invoice getInvoiceById(String invoiceId);

	// 更新订单信息
	public JSONObject updateInvoiceInfo(Invoice invoice);

	// 删除invoice
	public JSONObject deleteInvoice(String invoiceId);

	// 根据invoiceid查询invoice信息和部分订单信息
	public JSONObject getInvoiceDetailById(String invoiceId);

	// 查询发票列表
	public List<InvoiceResModel> getInvoiceList(String status, String invoiceType, String receiverName, String page, String rows);

	// 查询发票列表条数
	public Long getInvoiceListCount(String status, String invoiceType, String receiverName);

}
