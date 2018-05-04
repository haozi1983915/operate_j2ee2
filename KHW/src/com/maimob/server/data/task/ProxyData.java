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
		this.optimization = ot.getOptimization();
		if(this.optimization == 0)
			this.proportion = 1;
		else if(this.optimization > 0)
		{
			this.proportion = Float.parseFloat("0."+(100 - ot.getOptimization()));
		}
		
	}
	
	
	public static void main(String[] args) {
//		Map<String,String> ss = new HashMap<String,String>();
//		ss.put("id", "1517918294658");
////		ss.put("channel", "Lmrwei_lianjie");
//		ss.put("startDate", "2018-04-01");
//		ss.put("endDate", "2018-05-02");
//		ss.put("optimization", "-2");
//		OptimizationTask ot = new OptimizationTask (ss);
//		ProxyData pd = new ProxyData(ot);
//		pd.Statistics();
		
		String date = "2018-04-01";
		long d = AppTools.stringToLong(date, "yyyy-MM-dd");
		System.out.println(d);
		
		
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
	
	String table = "operate_reportform";
	int optimization = -1;
	long nextJg = 0;
//	String dateFormat = "yyyy-MM-dd HH";
	String dateFormat = "yyyy-MM-dd";
	String StartDate = "2018-01-30";
	String endDate = "2018-01-30";
	String channel = ""; 
	double proportion = 1;

	float pross = 100;
	
	int step = 0;
	 
	
	//统计注册
	private void StatisticalRegister()
	{

			nextJg = 3600000l*24l;
			dateFormat = "yyyy-MM-dd";
			table = "operate_reportform "; 
		
        
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
	
	private int getOp(long queryDate,List<Optimization> ops)
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
			String where = "";
			where = "";
			if(!StringUtils.isStrEmpty(channel))
				where = " channel='"+channel+"'  and ";
			String sql = "select * from operate_reportform where "+where+" date = '"+queryTime+"'";
			List<Map<String,String>> ordList = od.Query(sql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				Operate_reportform ord = new Operate_reportform(); 
				String id = ordMap.get("id");
				String channel = ordMap.get("channel");
				
				

				if(!StringUtils.isStrEmpty(channel))
					where = " where channel='"+channel+"' ";
					
				
				String sql3 = " select * from operate_channel "+where;
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
				

				String seosql = "select id from operate_channel where channel='"+channel+"'";
				List<Map<String,String>> sChannel = od.Query(seosql);
				String channelId = "";
				if(sChannel!=null && sChannel.size()>0 )
				{
					channelId = sChannel.get(0).get("id");
				}
				else
				{
					continue;
				}
				
				
				List<Optimization> ops = Optimization.getOptimizationList(channelId).get(channelId);
				 

				long h5Register = 0;
				try {

					h5Register = Long.parseLong(ordMap.get("h5Register"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				long oldoptimization = 0;
				try {

					oldoptimization = Long.parseLong(ordMap.get("optimization"));
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
				long outFirstGetSum2 = 0;
				try {

					outFirstGetSum2 = Long.parseLong(ordMap.get("outFirstGetSum2"));
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
				
				long showOP = optimization;
				if (optimization == -1) {//-2只跑成本  -1用最近一次的比例
//					long queryDate = this.stringToLong(queryTime, "yyyy-MM-dd");
//					showOP = getOp(queryDate,ops);
					
					if(oldoptimization>2)
					{
						showOP = oldoptimization;
						this.proportion = (oldoptimization*0.01);
					}
					else
					{
						showOP = 0;
						this.proportion = 1;
					}
					
				}
				

 

				long outActivation = 0;
				try {

					outActivation = Long.parseLong(ordMap.get("outActivation"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				long outRegister = 0;
				try {

					outRegister = Long.parseLong(ordMap.get("outRegister"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				long outUpload = 0;
				try {

					outUpload = Long.parseLong(ordMap.get("outUpload"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				long outAccount = 0;
				try {

					outAccount = Long.parseLong(ordMap.get("outAccount"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				long outLoan = 0;
				try {

					outLoan = Long.parseLong(ordMap.get("outLoan"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				

				long outCredit = 0;
				try {

					outCredit = Long.parseLong(ordMap.get("outCredit"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				

				 
				if(optimization != -2)
				{
					 outActivation = (int) (activation * this.proportion);
					 outRegister = (int) (register * this.proportion);
					 outUpload = (int) (upload * this.proportion);
					 outAccount = (int) (account * this.proportion);
					 outLoan = (int) (loan * this.proportion);
					 outCredit = (int) (credit * this.proportion);
				}

				outAccount = account;
				if(account > 3)
				{
					outAccount = (long) (this.proportion * account);
				}
				
				
				if(outAccount == 0)
					outCredit = 0;
				
				
				long outPerCapitaCredit = 0;
				if (outAccount > 0)
					outPerCapitaCredit = (outCredit / outAccount);
				else if (outAccount == 0)
				{
					outPerCapitaCredit = 0;
				}

				long outFirstGetPer = firstGetPer;
				long outFirstGetSum = outFirstGetSum2;
				if(firstGetPer > 3)
				{
					outFirstGetPer = (long) (this.proportion * firstGetPer);
					outFirstGetSum = (long) (this.proportion * outFirstGetSum2);
					outFirstGetSum = outFirstGetSum / 100 * 100;
					
					if(outFirstGetPer != 0 && outFirstGetSum / outFirstGetPer < 500 )
						outFirstGetSum = outFirstGetPer * 500;
				}
				
				
				if(outFirstGetPer == 0)
					outFirstGetSum = 0;
				
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
					cost = this.getCost(  outFirstGetPer, outRegister,outFirstGetSum,  outAccount,outUpload, rewardId);
				}
				

				double cost3 = 0;
				if (!StringUtils.isStrEmpty(rewardId)) {
					cost3 = this.getCost(firstGetPer, register, firstGetSum, account, upload, rewardId);
				}
				
				double grossProfit = income - cost;
				double grossProfitRate = 0;
				if(income != 0)
					grossProfitRate = grossProfit/income;
				

				String showTime = ordMap.get("showTime");
				if(showTime.startsWith("9999"))
				{
					cost = 0;
					cost3 = 0;
				}

				if(optimization == -2)
				{
					String updateIncome = "update "+ table +" set cost="+cost+", cost3="+cost3+"  where id="+id;
					try {
						od.Update(updateIncome);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(optimization == -3)
				{
					String updateIncome = "update "+ table +" set showTime='9999-01-01 11:00:00'   where id="+id;
					try {
						od.Update(updateIncome);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(optimization == -4)
				{
					String updateIncome = "update "+ table +" set showTime='"+queryTime+" 11:00:00'   where id="+id;
					try {
						od.Update(updateIncome);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else
				{
					String insert = "update operate_reportform set outActivation = "+outActivation+",outRegister = "+outRegister+",outUpload = "+outUpload+","
							+ "outAccount = "+outAccount+",outLoan = "+outLoan+",outCredit = "+outCredit+",outPerCapitaCredit = "+outPerCapitaCredit+",outFirstGetPer = "+outFirstGetPer+
							",outFirstGetSum = "+outFirstGetSum+",outChannelSum = "+outChannelSum+",optimization = "+showOP+",cost="+cost+",cost3="+cost3+",grossProfit="+grossProfit+",grossProfitRate="+grossProfitRate+" where id="+id;
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
				
				
				
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			od.close();
		}
		
		return true;
	}
	
	
	
	public double getCost(long outFirstGetPer, long outRegister,long outFirstGetSum,long outAccount,long outUpload,String rewardid)
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
//			'33', 'CPA 进件'
			
//			cpa单价    按注册
//			cps首提   按首提人数
//			cps比例   按首提金额
//			cps开户   按授信人数
			
			double cost = 0;
			
			if(typeId.equals("34"))
				return 0;
			
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
			else if(typeId.equals("33"))
			{
				num = outUpload;
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
					

					if (typeId.equals("28")) {
						price1 = price1 / 100;
					}
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
	
	
	
	
	


	
	

