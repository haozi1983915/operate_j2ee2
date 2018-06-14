package com.maimob.server.controller.logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.entity.BalanceAccount;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Partner;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.StringUtils;

public class PartnerBillLogic extends Logic{
	
	private DaoService dao;

	public PartnerBillLogic(DaoService dao) {
		super(dao);
		this.dao = dao;
	}
	
	//获取查询需要的参数列表
	public String getBillParameter(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;


		List<Partner> partnerList = dao.findAllPartners();
		baseResponse.setPartnerList(partnerList);                                 //合作方List
			
		List<BalanceAccount> balanceAccountList = dao.findAllBalanceAccount();       
		baseResponse.setBalanceAccountList(balanceAccountList);                    //我方公司List
			
		List<Dictionary> appList = Cache.getDicList(1);
		baseResponse.setAppList(appList);                                        //产品列表
			
		List<Dictionary> billStatusList = Cache.getDicList(20);
		baseResponse.setBillStatusList(billStatusList);                           //账单状态
			
		List<Dictionary> payList = Cache.getDicList(17);
		baseResponse.setPayList(payList);                            //合作方式
		
		return this.toJson();
	}
	
	//查询合作方账单
	public String getPartnerBill(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
		
		OperateDao od = new OperateDao();
		List<Map<String, String>> billDetail = od.findPartnerBill(whereJson);
		baseResponse.setReportforms_admin(billDetail);   
		
		return this.toJson();
	}
	
	//更新合作方志账单的状态 和 实际金额
	public String updatePartnerBill(String json) {
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
				return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
		
		String id = whereJson.getString("id");
		String status = whereJson.getString("status");
		String actualCost = whereJson.getString("actualCost");
		
		String sql = "update operate_partnerbill set  status="+ status +",  actualCost = " + actualCost + " where id = " + id;
		OperateDao od = new OperateDao();
		int n = 0;
		try {
			n = od.Update(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(n > 0) {
			baseResponse.setStatusMsg("修改成功！");
		}else {
			baseResponse.setStatusMsg("修改失败！");
		}
		return this.toJson();
	}
	//更改运营成本的同步状态
	public String updateCosting(String json) {
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
				return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
		String id = whereJson.getString("id");
		String synchronizeSwitch = whereJson.getString("synchronizeSwitch");
		
		String sql = "update operate_costing set  synchronizeSwitch="+ synchronizeSwitch  + " where id = " + id;
//		OperateDao od = new OperateDao();
		int n = 0;
		try {
			n = od.Update(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(n > 0) {
			baseResponse.setStatusMsg("修改成功！");
		}else {
			baseResponse.setStatusMsg("修改失败！");
		}
		return this.toJson();
	}
	
	//查询产品表的配置
	public String getTableColumns(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;


		List<Dictionary> appList = Cache.getDicList(1);
		baseResponse.setAppList(appList);                
		JSONObject whereJson = JSONObject.parseObject(json);
		String system = whereJson.getString("system");
		List<Dictionary> columns = new ArrayList<Dictionary>();
		if("2".equals(system)) {
			columns = dao.findDictionaryByType("23");  //代理系统数据详情表字段
		}
		else {
			columns = dao.findDictionaryByType("22");   //运营系统数据详情表字段
		}
		List<Dictionary> transColumns = dao.findDictionaryByType("21");   //流程转化表字段
		baseResponse.setChannelType(columns);   
		baseResponse.setPayList(transColumns);
		 
		
		String sql = "select appId,name,onlyType,columns from db_operate.operate_app_table where type = 21 and system = " + system;
		String hql = "select appId,name,onlyType,columns from db_operate.operate_app_table where type = 22 and system = " + system;
		List<Map<String,String>> reportforms_operate = null;     // 流程转化表
		List<Map<String,String>> reportforms_admin = null;       // 数据详情表
		try {
			reportforms_operate = od.Query(sql);
			reportforms_admin = od.Query(hql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		baseResponse.setReportforms_admin(reportforms_admin);
		baseResponse.setReportforms_operate(reportforms_operate);
		return this.toJson();
	}
	
	public String updateTableColumns(String json) {
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;
		
		JSONObject whereJson = JSONObject.parseObject(json); 
		String type = whereJson.getString("type");
		String name = whereJson.getString("appName");
		String onlyType = whereJson.getString("onlyType");
		String system = whereJson.getString("system");
		JSONArray column = whereJson.getJSONArray("columns");
		String columns = "";
		if(column != null && column.size() > 0) {
			for (Object object : column) {
				columns += object + ",";
			}
		}
		if(columns.endsWith(",")) {
			columns = columns.substring(0, columns.length()-1);
		}
		String sql = "";
		if(onlyType == null) {
			sql = "update operate_app_table set columns = '"+ columns + "' where system = " + system + " and type = " + type + " and name = '" + name + "'";
		}else {
			sql = "update operate_app_table set columns = '"+ columns + "' where system = " + system + " and onlyType = '" + onlyType + "'";
		}
		
		int n = 0;
		try {
			n = od.Update(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(n > 0) {
			baseResponse.setStatusMsg("修改成功！");
		}else {
			baseResponse.setStatusMsg("修改失败！");
		}
		return this.toJson();
	}

}
