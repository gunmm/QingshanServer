package com.gunmm.dao;

import java.util.List;

import com.gunmm.model.Flow;

public interface FlowDao {
	
	//新建flow
	public void addFlow(Flow flow);
	
	//更新flow
	public void updateFlow(Flow flow);
	
	//根据订单Id和司机id查询所有相关flow
    public List<Flow> getFlowListByOrderId(String orderId, String driverId);
	
	//根据订单Id将所有相关flow状态修改
	public void setFlowStatusByOrderId(String orderId, String status);
	
	//根据司机Id查询flow
	public Flow getFlowByDriverId(String driverId, String orderId);
}
