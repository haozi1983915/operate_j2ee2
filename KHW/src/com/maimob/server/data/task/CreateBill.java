package com.maimob.server.data.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.maimob.server.finance.WebResult;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.StringUtils;

public class CreateBill {

	public static void main(String[] args) {
		CreateBill cb = new CreateBill();
		cb.create("2018-03");
		
	}
	
	
	public void create(String month)
	{
		OperateDao od = new OperateDao();
		try {

			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createTime = sdf.format(new Date());
			List<Map<String, String>> channelFinanceList =  od.getChannelFinanceByMonth(month,"");
			
			for(Map<String, String> channelFinance:channelFinanceList)
			{
				String payCompanyid = channelFinance.getOrDefault("companyId", "0");
				if(StringUtils.isStrEmpty(payCompanyid))
					payCompanyid = "0";
				
				String payCompany = channelFinance.get("invoice_title");
				String proxyName = channelFinance.get("supplier");
				String pay = channelFinance.get("pay");
				String income = channelFinance.get("income");
				String cost2 = channelFinance.get("cost2");
				String proxyid = channelFinance.get("proxyid");
				
				double c2 = Double.parseDouble(cost2);

				String app = channelFinance.get("app");
				String appid = channelFinance.get("appid");
				String adminId = channelFinance.get("adminid");
				String mainChannelName = channelFinance.get("mainChannelName");
				String mainChannel = channelFinance.get("mainChannel");
				String adminName = channelFinance.get("adminName");

				if(mainChannelName != null && mainChannelName.contains("麦广"))
				{
					continue;
				}
				

				if(proxyName != null && proxyName.contains("麦广"))
				{
					continue;
				}
				
				
				
				if(proxyName == null)
				{
					continue;
				}
				
				
				
//				服务名称格式：产品名称+一级渠道名称+归属期间
				if(!StringUtils.isStrEmpty(mainChannelName) && c2>0)
				{
					String product = app+"_"+adminName+"_"+mainChannelName+"_"+month;
					od.saveBill(product, proxyid, appid, payCompany,payCompanyid, adminId, proxyName, mainChannelName,mainChannel, month, cost2, createTime );
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			od.close();
		}
		
	}
	

	public void RefreshBill(String billid)
	{
		OperateDao od = new OperateDao();
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createTime = sdf.format(new Date());
			List<Map<String, String>> billlist =  od.getBillById(billid);
			
			if(billlist != null && billlist.size() > 0)
			{
				Map<String, String> bill = billlist.get(0);
				String mainChannel = bill.get("mainChannel");
				String month = bill.get("month");

				List<Map<String, String>> channelFinanceList =  od.getChannelFinanceByMonth(month,mainChannel);
				
				for(Map<String, String> channelFinance:channelFinanceList)
				{
					String payCompanyid = channelFinance.getOrDefault("companyId", "0");
					if(StringUtils.isStrEmpty(payCompanyid))
						payCompanyid = "0";
					
					String payCompany = channelFinance.get("invoice_title");
					String proxyName = channelFinance.get("supplier");
					String pay = channelFinance.get("pay");
					String income = channelFinance.get("income");
					String cost2 = channelFinance.get("cost2");
					String proxyid = channelFinance.get("proxyid");
					
					double c2 = Double.parseDouble(cost2);

					String app = channelFinance.get("app");
					String appid = channelFinance.get("appid");
					String adminId = channelFinance.get("adminid");
					String mainChannelName = channelFinance.get("mainChannelName");
					String adminName = channelFinance.get("adminName");
					 
					
//					服务名称格式：产品名称+一级渠道名称+归属期间
					if(!StringUtils.isStrEmpty(mainChannelName) && c2>0)
					{
						String product = app+"_"+adminName+"_"+mainChannelName+"_"+month;
						od.updateBill(billid,product, proxyid, appid, payCompany,payCompanyid, adminId, proxyName, mainChannelName,mainChannel, month, cost2, createTime );
					}
					
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			od.close();
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
