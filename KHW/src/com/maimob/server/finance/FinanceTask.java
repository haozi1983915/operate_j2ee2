package com.maimob.server.finance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.StringUtils;



public class FinanceTask extends FinanceIdMapping {

	public static void main(String[] args) {
		FinanceTask ft = new FinanceTask();
		ft.start();
	}
	
	
	boolean isrun = true;
	@Override
	public void run() {
		
		long appLast = 0;

		OperateDao od = new OperateDao();
		try {

			
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			String date = sdf.format(new Date()); 
//			sdf = new SimpleDateFormat("yyyy-MM");
//			String month = sdf.format(new Date());  
			

			String date = "2018-03-28";  
			String month = "2018-03";  
			
			List<Map<String, String>> channelFinanceList =  od.getChannelFinance(date);
			
			for(Map<String, String> channelFinance:channelFinanceList)
			{
				String invoice_title = channelFinance.get("invoice_title");
				if(!StringUtils.isStrEmpty(invoice_title))
					invoice_title = invoice_title.trim();
				String supplier = channelFinance.get("supplier") ;
				if(!StringUtils.isStrEmpty(supplier))
					supplier = supplier.trim();
				String pay = channelFinance.get("pay");
				if(!StringUtils.isStrEmpty(pay))
					pay = pay.trim();
				String income = channelFinance.get("income");
				if(!StringUtils.isStrEmpty(income))
					income = income.trim();
				String cost2 = channelFinance.get("cost2");
				if(!StringUtils.isStrEmpty(cost2))
					cost2 = cost2.trim();
				String proxyid = channelFinance.get("proxyid");
				if(!StringUtils.isStrEmpty(proxyid))
					proxyid = proxyid.trim();

				String app = channelFinance.get("app").trim();
				String mainChannelName = channelFinance.get("mainChannelName");
//				服务名称格式：产品名称+一级渠道名称+归属期间
				
				String supplier_id = channelFinance.get("supplier_id");
				if(StringUtils.isStrEmpty(supplier_id))
				{
					try {

						supplier_id = this.getId(supplier, "supplier_id");
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(StringUtils.isStrEmpty(supplier_id))
					{
						List<Map<String,String>> suppliers = od.getSupplier(proxyid);
						supplier_id = this.set_supplier(suppliers);
						
					}

					if(!StringUtils.isStrEmpty(supplier_id))
					{
						od.saveSupplier_id(supplier_id, proxyid);
					}
				}
				String customer = "中银消费金融有限公司";
				System.out.println(supplier+"    "+invoice_title);
				try {
					if(!StringUtils.isStrEmpty(supplier) && !StringUtils.isStrEmpty(invoice_title))
					{
						if(!StringUtils.isStrEmpty(income))
						{
							double incomeD = Double.parseDouble(income);
							if(incomeD > 0)
							{
								String service_name = app+"_"+customer+"_"+date;
								WebResult wr = this.set_income(invoice_title,customer , service_name, month, incomeD+"");
								if(!wr.getCode().equals("1"))
								od.saveFinanceLog( "set_income", proxyid, wr.getMsg());
							}
						}
						if(!StringUtils.isStrEmpty(cost2))
						{
							double cost = Double.parseDouble(cost2);
							if(cost > 0)
							{
								String service_name = app+"_"+mainChannelName+"_"+date;
								WebResult wr = this.set_cost(invoice_title, supplier_id, service_name, month, cost+"",pay);
								if(!wr.getCode().equals("1"))
								od.saveFinanceLog( "set_cost", proxyid, wr.getMsg());
							}
						}
					}
					else
					{
//						报警   付款公司没配
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
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
