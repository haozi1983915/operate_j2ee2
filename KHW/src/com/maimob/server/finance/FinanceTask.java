package com.maimob.server.finance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.maimob.server.importData.dao.OperateDao;



public class FinanceTask extends FinanceIdMapping {

	public static void main(String[] args) {
		FinanceTask ft = new FinanceTask();
		ft.start();
	}
	
	
	boolean isrun = true;
	@Override
	public void run() {
		
		long appLast = 0;
		while(isrun)
		{
			OperateDao od = new OperateDao();
			try {

				
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				String now1 = sdf.format(new Date());
//				sdf = new SimpleDateFormat("yyyy-MM");
//				String month = sdf.format(new Date()); 
				
				String month = "2018-03";
				
				List<Map<String, String>> channelFinanceList =  od.getChannelFinance(month);
				
				for(Map<String, String> channelFinance:channelFinanceList)
				{
//					
//					String sql = " select a.*  ,(select company from operate_proxy c where  c.id=a.proxyid) supplier ,   (select   name  from operate_dictionary  c where c.id = a.appid)    app,  "
//							+ "   if(  b.pay=38,1,if(b.pay=37,2,null)    ) pay,     (select   company  from operate_balance_account c where c.id = b.companyId)    invoice_title from  "
//							+ " (SELECT mainChannelName,appid,proxyid , sum(income) income ,sum(cost)cost ,sum(  if(cost2=0,cost,cost2) )cost2  FROM db_operate.operate_reportform  where  month = '"+month+"' "
//							+ " group by  mainChannelName,appid,proxyid) a "
//							+ " left join   operate_pay_company  b  on a.proxyid = b.proxyid and a.appid= b.appid  ";

					String invoice_title = channelFinance.get("invoice_title");
					String supplier = channelFinance.get("supplier");
					String pay = channelFinance.get("pay");
					String income = channelFinance.get("income");
					String cost2 = channelFinance.get("cost2");

					String app = channelFinance.get("app");
					String mainChannelName = channelFinance.get("mainChannelName");
//					服务名称格式：产品名称+一级渠道名称+归属期间
					
					String service_name = app+"_"+mainChannelName+"_"+month;
					
					
//					this.set_income(invoice_title, "中银消费金融有限公司", service_name, month, income);
					this.set_cost(invoice_title, supplier, service_name, month, cost2,pay);
					
					
				}
				
				
				sleep(300000);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			finally {
				od.close();
			}
		}
		
		
		
	
		
		
		
		
		
		
		
		
		
		
		
	}
	
}
