package com.maimob.server.data.task;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.maimob.server.db.entity.Operate_reportform;
import com.maimob.server.db.entity.OptimizationTask;
import com.maimob.server.importData.dao.LoansDao;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.StringUtils; 

public class ProxyData {
	
	OptimizationTask ot;
	public ProxyData(OptimizationTask ot) {
		ot.setProgressMsg("开始运行");
		this.ot = ot;
		this.StartDate = ot.getStartDate();
		this.endDate = ot.getEndDate();
		this.channel = ot.getChannel();
		this.channelId = ot.getChannelId();
		this.optimization = ot.getOptimization();
		if(this.optimization == 0)
			this.proportion = 1;
		else if(this.optimization > 0)
		{
			this.proportion = Float.parseFloat("0."+(100 - ot.getOptimization()));
		}
		
	}
	
	
	public static void main(String[] args) {
		Map<String,String> ss = new HashMap<String,String>();
		ss.put("id", "1517918294658");
		ss.put("channel", "boluodai_waibuqudao");
		ss.put("channelId", "1116");
		ss.put("startDate", "2018-01-28");
		ss.put("endDate", "2018-01-28");
		ss.put("optimization", "10");
		OptimizationTask ot = new OptimizationTask (ss);
		ProxyData pd = new ProxyData(ot);
		pd.Statistics();
	}
	
	
	public void Statistics()
	{
		for(int i = 1;i <= 1 ;i++)
		{
			queryType = i;
			StatisticalRegister();
		}
	}
	
	Map<String, Map<String, String>> channels = new HashMap<String, Map<String, String>>();
	long queryType = 2;//0,小时 	1，天 	2，月
	
	String table = "operate_reportform_day";
	int optimization = -1;
	long nextJg = 0;
//	String dateFormat = "yyyy-MM-dd HH";
	String dateFormat = "yyyy-MM-dd";
	String StartDate = "2018-01-30";
	String endDate = "2018-01-30";
	String channel = "";
	long channelId = 0;
	double proportion = 0;

	float pross = 100;
	
	int step = 0;
	
	List<Optimization> ops = null;
	
	//统计注册
	private void StatisticalRegister()
	{

		ops = Optimization.getOptimizationList(channelId).get(channelId);
		
		if(queryType==0)
		{
			dateFormat = "yyyy-MM-dd HH";
			table = "operate_reportform_hour";
			nextJg = 3600000;
		}
		else if(queryType==1)
		{
			nextJg = 3600000l*24l;
			dateFormat = "yyyy-MM-dd";
			table = "operate_reportform_day";
		}
		else if(queryType==2)
		{
			nextJg = 3600000l*24l*20l;
			dateFormat = "yyyy-MM";
			table = "operate_reportform_month";
		}
		
        
		try {
			ot.setStatus(1);
			
			String statisticalTime = "";
			
			if(queryType==0)
			{
				statisticalTime = StartDate.substring(0, 13);
			}
			else if(queryType==1)
			{
				statisticalTime = StartDate;
			}
			else if(queryType==2)
			{
				statisticalTime = StartDate.substring(0, 7);
			}

			String queryTime = statisticalTime;
			
	        float day = AppTools.daysBetween(StartDate,endDate);
	        ot.setDays((int)day);
	        pross = 100.0f/(day);

			while(true)
			{
				try {
					
					if(endDate.equals(queryTime))
					{
						System.out.println(queryTime);
						LastStatistical(queryTime); 
						break;
					}
					else if(!endDate.equals(queryTime))
					{
						System.out.println(queryTime);
						LastStatistical(queryTime); 
						queryTime = next(queryTime);
					}
					step++;
					ot.setRunDate(queryTime);
			        ot.setProgress(step*pross);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			ot.setStatus(2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private int getOp(long queryDate)
	{
		int opt = -1;
		if (ops != null && ops.size() > 0) {
			for (int i = 0; i < ops.size(); i++) {
				Optimization op = ops.get(i);
				if(op.endTime==0)
					op.endTime = Long.MAX_VALUE;

				if (queryDate >= op.startTime && queryDate < op.endTime) {
					opt = op.optimization;
					break;
				}
			}
		}
		if (opt <= 0) {
			opt = 1;
			this.proportion = 1;
		} else {
			this.proportion = Float.parseFloat("0."+(100 - opt));
		}
		return opt;
	}
	

	
//	private void save(String queryTime,long finish)
//	{
//		String sql = "select date from operate_data_log where date = '"+queryTime+"'";
//		try {
//			if(od.Query(sql).size() == 0)
//			{
//				String sqlInsert = "insert into operate_data_log(date,finish) values('"+queryTime+"',"+finish+")";
//			}
//			else
//			{
//
//				String sqlUpdate = "update operate_data_log set finish="+finish+" where date='"+queryTime+"' ";
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		
//		
//		
//	}
	
//	String queryTime_day = "";
	

	private String next(String queryTime) 
	{
		try {
 
			long time = stringToLong(queryTime, dateFormat);
			if(queryType==2)
			{
				queryTime = queryTime+"-20";
				time = stringToLong(queryTime, "yyyy-MM-dd");
			}
			
			time += nextJg;
			
			queryTime = longToString(time, dateFormat);
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return queryTime;
	}

	Map<String, List<Map<String, String>>> reward = new HashMap<String, List<Map<String, String>>>();
	private boolean LastStatistical(String queryTime)
	{

		OperateDao od = new OperateDao();
		try {

			ot.setStep(step);
			ot.setRunDate(queryTime);
			
			String sql3 = " select * from operate_channel where channel='"+channel+"' ";
			String rewardId = "";
			List<Map<String, String>> cs = od.Query(sql3);
			if(cs != null && cs.size() > 0)
			{
				rewardId = cs.get(0).get("rewardId");
			}
			
			if(!StringUtils.isStrEmpty(rewardId))
			{
				String sql1 = " select * from operate_reward where id="+rewardId+" ";

				List<Map<String, String>> rewardList = od.Query(sql1);
				reward.put(rewardId, rewardList);
			}
			
			String sql = "select * from operate_reportform where channel='"+channel+"'  and date = '"+queryTime+"'";
			List<Map<String,String>> ordList = od.Query(sql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				Operate_reportform ord = new Operate_reportform(); 
				String id = ordMap.get("id");
				long h5Register = 0;
				try {

					h5Register = Long.parseLong(ordMap.get("h5Register"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				ord.setH5Register(h5Register);

				long activation = 0;
				try {

					activation = Long.parseLong(ordMap.get("activation"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				ord.setActivation(activation);
				long register = 0;
				try {

					register = Long.parseLong(ordMap.get("register"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				ord.setRegister(register);
				long upload = 0;
				try {

					upload = Long.parseLong(ordMap.get("upload"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				ord.setUpload(upload);
				long account = 0;
				try {

					account = Long.parseLong(ordMap.get("account"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				ord.setAccount(account);
				long loan = 0;
				try {

					loan = Long.parseLong(ordMap.get("loan"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				ord.setLoan(loan);
				long credit = 0;
				try {

					credit = Long.parseLong(ordMap.get("credit"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				ord.setCredit(credit);

			

				long firstGetPer = 0;
				try {

					firstGetPer = Long.parseLong(ordMap.get("firstGetPer"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				ord.setFirstGetPer(firstGetPer);
				long firstGetSum = 0;
				try {

					firstGetSum = Long.parseLong(ordMap.get("firstGetSum"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				ord.setFirstGetSum(firstGetSum);
				long channelSum = 0;
				try {
					channelSum = Long.parseLong(ordMap.get("channelSum"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				int showOP = optimization;
				if (optimization == -1) {//-1用设置的比例  -2用最近一次的比例
					long queryDate = this.stringToLong(queryTime, "yyyy-MM-dd");
					showOP = getOp(queryDate);
				}
				
				
				
				int outActivation = (int) (activation * this.proportion);
				int outRegister = (int) (register * this.proportion);
				int outUpload = (int) (upload * this.proportion);
				int outAccount = (int) (account * this.proportion);
				int outLoan = (int) (loan * this.proportion);
				int outCredit = (int) (credit * this.proportion);
				
				if(outAccount == 0)
					outCredit = 0;
				
				
				long outPerCapitaCredit = 0;
				if (outAccount > 0)
					outPerCapitaCredit = (outCredit / outAccount);
				else if (outAccount == 0)
				{
					outPerCapitaCredit = 0;
				}
				
				int outFirstGetPer = (int) (firstGetPer * this.proportion);
				
				int outFirstGetSum = (int) (firstGetSum * this.proportion);
				if (outFirstGetPer == 0)
				{
					outFirstGetSum = 0;
				}
				
				int outChannelSum = (int) (channelSum * this.proportion);

				if(outLoan == 0)
					outChannelSum = 0;
				
				
				
				
				double income = 0;
				try {
					income = Double.parseDouble(ordMap.get("income"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				double cost = 0;
				if(!StringUtils.isStrEmpty(rewardId))
				{
					cost = this.getCost(  outFirstGetPer,   outRegister,  outFirstGetSum,  outAccount, rewardId);
					
				}
				
				double grossProfit = income - cost;
				double grossProfitRate = 0;
				if(income != 0)
					grossProfitRate = grossProfit/income;
				
				
				
				
				String insert = "update operate_reportform set outActivation = "+outActivation+",outRegister = "+outRegister+",outUpload = "+outUpload+","
						+ "outAccount = "+outAccount+",outLoan = "+outLoan+",outCredit = "+outCredit+",outPerCapitaCredit = "+outPerCapitaCredit+",outFirstGetPer = "+outFirstGetPer+
						",outFirstGetSum = "+outFirstGetSum+",outChannelSum = "+outChannelSum+",optimization = "+showOP+",cost="+cost+",grossProfit="+grossProfit+",grossProfitRate="+grossProfitRate+" where id="+id;
				try {
					od.Update(insert);
					if(proportion != 1)
					{
						//保存最后一次优化比例
						String sql2 = "update operate_data_log set optimization="+proportion+" where channel= '"+channel+"' and date = '"+queryTime+"' ";
						int yx = od.Update(sql2);
						if(yx==0)
						{
							sql2 = "insert into operate_data_log(optimization,channel,date) values("+proportion+" ,'"+channel+"' , '"+queryTime+"') ";
							yx = od.Update(sql2);
						}
					}

				} catch (Exception e) {
					System.out.println(insert);
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			od.close();
		}
		
		return true;
	}
	
	
	
	public double getCost(long outFirstGetPer, long outRegister,long outFirstGetSum,long outAccount,String rewardid)
	{
		List<Map<String, String>> rs = this.reward.get(rewardid);
		if(rs != null && rs.size() > 0)
		{
			Map<String, String> r = rs.get(0);
			String typeId = r.get("typeId");
			float price = Float.parseFloat(r.get("price"));
//			'26', 'CPA 单价'
//			'27', 'CPS 首提'
//			'28', 'CPS 比例'
//			'29', 'CPS 开户'
			
//			cpa单价    按注册
//			cps首提   按首提人数
//			cps比例   按首提金额
//			cps开户   按授信人数
			
			double cost = 0;
			
			long num = 0;
			if(typeId.equals("26"))
			{
				num = outRegister;
			}
			else if(typeId.equals("27"))
			{
				num = outFirstGetPer;
			}
			else if(typeId.equals("28"))
			{
				price = price/100;
				num = outFirstGetSum;
			}

			else if(typeId.equals("29"))
			{
				num = outAccount;
			}
			
			
			if(rs.size() == 1)
			{
				cost = price*num;
			}
			else {
				int smax = 0;
				for(int i = 0;i < rs.size();i++)
				{
					Map<String, String> r1 = rs.get(i);
					float price1 = Float.parseFloat(r1.get("price"));
					int max = Integer.parseInt(r1.get("max"));
					
					if(max >= num)
					{
						cost = num * price1;
						break;
					}
					smax = max;
				}
			}
			return cost;
		}
		
		return 0;
	}
	
	
	
	
	
	
	
	

	public static String dateToString(Date data, String formatType) {
 		return new SimpleDateFormat(formatType).format(data);
 	}
	public static long dateToLong(Date date) {
 		return date.getTime();
 	}

 	public static long stringToLong(String strTime, String formatType)
 			throws ParseException {
 		Date date = stringToDate(strTime, formatType); // String类型转成date类型
 		if (date == null) {
 			return 0;
 		} else {
 			long currentTime = dateToLong(date); // date类型转成long类型
 			return currentTime;
 		}
 	}

	
 	// long类型转换为String类型
 	// currentTime要转换的long类型的时间
 	// formatType要转换的string类型的时间格式
 	public static String longToString(long currentTime, String formatType) throws ParseException {
 		Date date = longToDate(currentTime, formatType); // long类型转成Date类型
 		String strTime = dateToString(date, formatType); // date类型转成String
 		return strTime;
 	}
 	public static Date longToDate(long currentTime, String formatType)
 			throws ParseException {
 		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
 		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
 		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
 		return date;
 	}
 	public static Date stringToDate(String strTime, String formatType)
 			throws ParseException {
 		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
 		Date date = null;
 		date = formatter.parse(strTime);
 		return date;
 	}
	
	
	
	
	


	
	

}
