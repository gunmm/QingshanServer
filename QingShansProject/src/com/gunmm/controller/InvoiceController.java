package com.gunmm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.InvoiceDao;
import com.gunmm.impl.InvoiceImpl;
import com.gunmm.model.Invoice;
import com.gunmm.responseModel.InvoiceResModel;
import com.gunmm.utils.JSONUtils;

@Controller
@RequestMapping("/mobile")
public class InvoiceController {
	// 根据发票ID查寻发票详情
	@RequestMapping("/getInvoiceDetailById")
	@ResponseBody
	private JSONObject getInvoiceDetailById(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String invoiceId = object.getString("invoiceId");
		InvoiceDao invoiceDao = new InvoiceImpl();
		return invoiceDao.getInvoiceDetailById(invoiceId);
	}

	// 获取发票列表
	@RequestMapping("/getInvoiceList")
	@ResponseBody
	private JSONObject getInvoiceList(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String status = object.getString("status");
		String invoiceType = object.getString("invoiceType");
		String receiverName = object.getString("receiverName");

		String rows = object.getString("rows");

		String page = Integer.toString((Integer.parseInt(object.getString("page")) * Integer.parseInt(rows)));

		InvoiceDao invoiceDao = new InvoiceImpl();

		List<InvoiceResModel> invoiceResModelList = invoiceDao.getInvoiceList(status, invoiceType, receiverName, page,
				rows);

		Long invoiceCount = invoiceDao.getInvoiceListCount(status, invoiceType, receiverName);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("invoiceCount", invoiceCount);
		jsonObject.put("invoiceResModelList", invoiceResModelList);

		return JSONUtils.responseToJsonString("1", "", "查询成功！", jsonObject);
	}

	// 确认开发票
	@RequestMapping("/confirmInvoice")
	@ResponseBody
	private JSONObject confirmInvoice(HttpServletRequest request) {
		JSONObject object = (JSONObject) request.getAttribute("body");
		String invoiceId = object.getString("invoiceId");
		String expressCompanyName = object.getString("expressCompanyName");
		String expressNumber = object.getString("expressNumber");

		InvoiceDao invoiceDao = new InvoiceImpl();
		Invoice invoice = invoiceDao.getInvoiceById(invoiceId);
		invoice.setExpressCompanyName(expressCompanyName);
		invoice.setExpressNumber(expressNumber);
		invoice.setStatus("1");
		return invoiceDao.updateInvoiceInfo(invoice);
		
	}

}
