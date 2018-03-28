package com.maimob.server.data.task;

import java.util.List;
import java.util.Map;

import com.maimob.server.finance.WebResult;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.StringUtils;

public class CreateBill {

	public static void main(String[] args) {
		
		
	}
	
	
	public void create(String month)
	{
		OperateDao od = new OperateDao();
		try {
			
			List<Map<String, String>> channelFinanceList =  od.getChannelFinance(month);
			
			for(Map<String, String> channelFinance:channelFinanceList)
			{
				String invoice_title = channelFinance.get("invoice_title");
				String supplier = channelFinance.get("supplier");
				String pay = channelFinance.get("pay");
				String income = channelFinance.get("income");
				String cost2 = channelFinance.get("cost2");
				String proxyid = channelFinance.get("proxyid");

				String app = channelFinance.get("app");
				String mainChannelName = channelFinance.get("mainChannelName");
//				服务名称格式：产品名称+一级渠道名称+归属期间
				
				String service_name = app+"_"+mainChannelName+"_"+month;
				
				
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			od.close();
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
