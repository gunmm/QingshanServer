package com.gunmm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gunmm.dao.InvoiceDao;
import com.gunmm.impl.InvoiceImpl;

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

}
