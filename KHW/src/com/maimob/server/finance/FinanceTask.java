package com.maimob.server.finance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.StringUtils;



public class FinanceTask extends FinanceIdMapping {

	public static void main(String[] args) {
		String dd = "dd ";
		dd = dd.trim();
		
		FinanceTask ft = new FinanceTask();
		ft.start();
	}
	
	
	boolean isrun = true;
	@Override
	public void run() {
		
		long appLast = 0;
		

		
		String StartDate = "2018-06-29";
		String endDate = "2018-06-29";   
		update(StartDate, endDate,"");
		
	}
	
	
	public String update(String StartDate,String endDate,String name)
	{

		StringBuffer sb = new StringBuffer();
		OperateDao od = new OperateDao();
		try {
			String queryTime = StartDate;
			while (true) {
				try {

					if (endDate.equals(queryTime)) {
						System.out.println(queryTime);
						sb.append(insertIncomeData(queryTime));
						sb.append(insertCostData(queryTime,name));
						break;
					} else if (!endDate.equals(queryTime)) {
						System.out.println(queryTime);
						sb.append(insertIncomeData(queryTime));
						sb.append(insertCostData(queryTime,name));
						queryTime = next(queryTime);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			od.close();
		}
		
		return sb.toString();
	}
	
	
	private StringBuffer insertIncomeData(String date )
	{

		StringBuffer sb = new StringBuffer();
		String month = date.substring(0,7);  
		OperateDao od = new OperateDao();
		try {

			List<Map<String, String>> channelFinanceList =  od.getChannelFinanceIncome(date);
			
			for(Map<String, String> channelFinance:channelFinanceList)
			{
				String invoice_title = channelFinance.get("invoice_title");
				if(!StringUtils.isStrEmpty(invoice_title))
					invoice_title = invoice_title.trim(); 
				String income = channelFinance.get("income");
				if(!StringUtils.isStrEmpty(income))
					income = income.trim();  

				String app = channelFinance.get("app").trim(); 
//				服务名称格式：产品名称+一级渠道名称+归属期间
				 

				String customer = channelFinance.get("customer");
				if(!StringUtils.isStrEmpty(customer))
					customer = customer.trim();
				
				
				try {
					if(!StringUtils.isStrEmpty(invoice_title) && !StringUtils.isStrEmpty(invoice_title))
					{
						if(!StringUtils.isStrEmpty(income))
						{
							double incomeD = Double.parseDouble(income);
							if(incomeD > 0)
							{
								String service_name = app+"_"+customer+"_"+date;
								WebResult wr = this.set_income(invoice_title,customer , service_name, month, incomeD+"");
								if(!wr.getCode().equals("1"))
								od.saveFinanceLog( "set_income", "0", wr.getMsg());
								sb.append(service_name+"  "+incomeD+";");
								System.out.println(service_name+"  "+incomeD+";");
							}
						}
//						if(!StringUtils.isStrEmpty(cost2))
//						{
//							double cost = Double.parseDouble(cost2);
//							if(cost > 0)
//							{
//								String service_name = app+"_"+adminName+"_"+mainChannelName+"_"+date;
//								WebResult wr = this.set_cost(invoice_title, supplier_id, service_name, month, cost+"",pay);
//								if(!wr.getCode().equals("1"))
//								od.saveFinanceLog( "set_cost", proxyid, wr.getMsg());
//								System.out.println(service_name);
//							}
//						}
					}
					else
					{
//						报警   付款公司没配
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			od.close();
		}
		
		return sb;
		
	}

	private StringBuffer insertCostData(String date,String name )
	{

		StringBuffer sb = new StringBuffer();
		String month = date.substring(0,7); 
		OperateDao od = new OperateDao();
		try {
			List<Map<String, String>> channelFinanceList =  od.getChannelFinance(date);
			
			for(Map<String, String> channelFinance:channelFinanceList)
			{
				String invoice_title = channelFinance.get("invoice_title");
				if(!StringUtils.isStrEmpty(invoice_title))
					invoice_title = invoice_title.trim();
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
				if(mainChannelName != null && mainChannelName.contains("麦广"))
				{
					continue;
				}
				
				
				String adminName = channelFinance.get("adminName");
//				服务名称格式：产品名称+一级渠道名称+归属期间
				
				String supplier_id = channelFinance.get("supplier_id");
				 
				

				String supplier = channelFinance.get("supplier") ;
				

				if(supplier != null && supplier.contains("麦广"))
				{
					continue;
				}
				
				
				
				if(supplier == null)
				{
					continue;
				}
				
				
				
				if(!StringUtils.isStrEmpty(supplier))
					supplier = supplier.trim();
				
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
				try {
					if(!StringUtils.isStrEmpty(supplier) && !StringUtils.isStrEmpty(invoice_title))
					{
//						if(!StringUtils.isStrEmpty(income))
//						{
//							double incomeD = Double.parseDouble(income);
//							if(incomeD > 0)
//							{
//								String service_name = app+"_"+adminName+"_"+customer+"_"+date;
//								WebResult wr = this.set_income(invoice_title,customer , service_name, month, incomeD+"");
//								if(!wr.getCode().equals("1"))
//								od.saveFinanceLog( "set_income", proxyid, wr.getMsg());
	//
//								System.out.println(service_name);
//							}
//						}
						if(mainChannelName.contains("还卡"))
							System.out.println(11);
						
						if(!StringUtils.isStrEmpty(cost2))
						{
							double cost = Double.parseDouble(cost2);
//							if(!mainChannelName.equals("非凡信息"))
//								continue;
							
							if(cost > 0 )
							{
								String service_name = app+"_"+adminName+"_"+mainChannelName+"_"+date;
								boolean isUpdate = StringUtils.isStrEmpty(name);
								if(!isUpdate && service_name.contains(name))
								{
									isUpdate = true;
								}
//								if(service_name.contains("何柳蓉"))
//								{
//									System.out.println(service_name);
//									save("服务名1.txt",service_name);
//								}
								if(isUpdate)
								{
									WebResult wr = this.set_cost(invoice_title, supplier_id, service_name, month, cost+"",pay);
									if(!wr.getCode().equals("1"))
									od.saveFinanceLog( "set_cost", proxyid, wr.getMsg());
									System.out.println(service_name+"  "+cost+"   "+supplier+";");
									sb.append(service_name+"  "+cost+"   "+supplier+";");
								}
							}
							else if(cost == 0 )
							{

								String service_name = app+"_"+adminName+"_"+mainChannelName+"_"+date;
//								WebResult wr = this.set_cost(invoice_title, supplier_id, service_name, month, cost+"",pay);
//								if(!wr.getCode().equals("1"))
//								od.saveFinanceLog( "set_cost", proxyid, wr.getMsg());
//								System.out.println(service_name+"  "+cost+"   "+supplier+";");
//								sb.append(service_name+"  "+cost+"   "+supplier+";");
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
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			od.close();
		}

			
			
		return sb;
		
	}
	
	static BufferedWriter bw = null;
	public static void save(String fname,String str)
	{
		try {

			if(bw == null)
			{
				String path = "/Users/zhanghao/Downloads/电话注册/"+fname;
				File root = new File(path); 
				if(!root.exists())		
					root.createNewFile();
				bw = new BufferedWriter(new FileWriter(path, true));
			}
			bw.write(str +"\r\n ");
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
		
	}
	
	

	long nextJg = 3600000l * 24l;

	private String next(String queryTime) {
		try {

			long time = AppTools.stringToLong(queryTime, "yyyy-MM-dd");  
			time += nextJg;

			queryTime = AppTools.longToString(time, "yyyy-MM-dd");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return queryTime;
	}
	
	
	
	
}
