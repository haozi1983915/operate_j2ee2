package com.maimob.server.controller.logic;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
	
	//获取下载账单的相关信息
//	public String getExportData(String json) {
//		String check = this.CheckJson(json);
//		if(!StringUtils.isStrEmpty(check))
//			return check;
//
//		JSONObject whereJson = JSONObject.parseObject(json); 
//		
//		OperateDao od = new OperateDao();
//		List<Map<String, String>> partnerIds = od.findpartnerIds(whereJson);
//		for (Map<String, String> map : partnerIds) {
//			String companyId = map.get("companyId");
//			String ourCompanyId = map.get("ourCompanyId");
//		}
//		baseResponse.setReportforms_admin(billDetail);   
//		
//		return this.toJson();
//	}
	
	
	//获取下载账单的相关信息
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

}
