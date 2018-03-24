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
		while(isrun)
		{
			OperateDao od = new OperateDao();
			try {

				
//				sdf = new SimpleDateFormat("yyyy-MM");
//				String month = sdf.format(new Date()); 
				
				String month = "2018-03";
				
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
//					服务名称格式：产品名称+一级渠道名称+归属期间
					
					String service_name = app+"_"+mainChannelName+"_"+month;
					String supplier_id = channelFinance.get("supplier_id");
					if(StringUtils.isStrEmpty(supplier_id))
					{
						supplier_id = this.getId(supplier, "supplier_id");
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
					
					WebResult wr = this.set_income(invoice_title, "中银消费金融有限公司", service_name, month, income);
					if(!wr.getCode().equals("1"))
					od.saveFinanceLog( "set_income", proxyid, wr.getMsg());
					
					wr = this.set_cost(invoice_title, supplier_id, service_name, month, cost2,pay);
					if(!wr.getCode().equals("1"))
					od.saveFinanceLog( "set_cost", proxyid, wr.getMsg());
					
					
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
