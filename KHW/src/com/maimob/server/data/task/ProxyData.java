package com.maimob.server.data.task;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.maimob.server.db.entity.OptimizationTask;
import com.maimob.server.importData.dao.LoansDao;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.StringUtils; 

public class ProxyData {
	
	LoansDao ld = new LoansDao();
	OperateDao od = new OperateDao();
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
			this.proportion = (float) (((100-ot.getOptimization())*1.0f)/100.0f);
		}
		
	}
	
	
	public static void main(String[] args) {
		Map<String,String> ss = new HashMap<String,String>();
		ss.put("id", "1517918294658");
		ss.put("channel", "boluodai_ledaikuan");
		ss.put("channelId", "3");
		ss.put("startDate", "2018-01-16");
		ss.put("endDate", "2018-02-15");
		ss.put("optimization", "30");
		ss.put("tableId", "30");
		ss.put("adminId", "1516704387763");
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
					if(optimization < 0)
					{
						long queryDate = this.stringToLong(queryTime, "yyyy-MM-dd");
						getOp(queryDate);
					}
					
					if(endDate.equals(queryTime))
					{
						System.out.println(queryTime);
						LastStatistical(queryTime);
						save(queryTime,0);
						break;
					}
					else if(!endDate.equals(queryTime))
					{
						System.out.println(queryTime);
						LastStatistical(queryTime);
						save(queryTime,1);
						queryTime = next(queryTime);
					}
					step++;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			ot.setStatus(2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void getOp(long queryDate)
	{
		int opt = -1;
		if(ops != null && ops.size() > 0)
		{
			for(int i = 0;i < ops.size();i++)
			{
				Optimization op = ops.get(i);
				
				if(queryDate >= op.startTime && queryDate < op.endTime)
				{
					opt = op.optimization;
				}
			}
		}
		if(opt <= 0 )
		{
			this.proportion = 1;
		}
		else
		{
			this.proportion = (float) (((100-opt)*1.0f)/100.0f);
		}
		
	}
	

	
	private void save(String queryTime,long finish)
	{
		String sql = "select date from operate_data_log where date = '"+queryTime+"'";
		try {
			if(od.Query(sql).size() == 0)
			{
				String sqlInsert = "insert into operate_data_log(date,finish) values('"+queryTime+"',"+finish+")";
			}
			else
			{

				String sqlUpdate = "update operate_data_log set finish="+finish+" where date='"+queryTime+"' ";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
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
	
	private boolean LastStatistical(String queryTime)
	{

		try {
			ot.setStep(step);
			 Map<String,long[]> data = new HashMap<String,long[]>();  
			 ot.setRunDate(queryTime);
			
			String sql = "SELECT channel,resultStatus,count(1)cou  FROM db_loans.loans_user where registerTime like '"+queryTime+"%' and channel = '"+channel+"'   group by channel,resultStatus";
			List<Map<String,String>> register_upload = ld.Query(sql);
			
			for(int i = 0;i < register_upload.size();i++)
			{
				Map<String,String> row = register_upload.get(i);
				
				String channel = row.get("channel");
				String resultStatus = row.get("resultStatus");
				long cou = Long.parseLong(row.get("cou"));
				
				long[] channelData = null;
				if(data.get(channel) == null)
				{
					channelData = new long[10];
					data.put(channel, channelData);
				}
				else
				{
					channelData = data.get(channel);
				}
				channelData[0] += cou;//注册人数
				
				if(!StringUtils.isStrEmpty(resultStatus))
					channelData[1] += cou;//进件人数
			}
			ot.setProgress((this.pross * step) +(this.pross/5));
			
			
			sql =    " select channel,count(1) cou , sum(c.baseTotCreLine) sum from "+
				"( SELECT b.channel ,c.baseTotCreLine  FROM db_loans.loans_acctstatus a,  db_loans.loans_user b  , db_loans.loans_loanacctinfo c  where  a.customerId = b.customerId and  a.customerId = c.customerId and b.channel = '"+channel+"'  and a.loanApproveDate like '"+queryTime+"%'"
						+ " )"+
				"c  group by channel ";
			List<Map<String,String>> accountlist = ld.Query(sql);
			for(int i = 0;i < accountlist.size();i++)
			{
				Map<String,String> row = accountlist.get(i);
				String channel = row.get("channel");
				long cou = Long.parseLong(row.get("cou"));
				
				

				String ss2 = row.get("sum");
				if(ss2.contains("."))
					ss2 = ss2.substring(0, ss2.indexOf("."));
				
				long sum = Long.parseLong(ss2)/100;
				
				
				
				long[] channelData = null;
				if(data.get(channel) == null)
				{
					channelData = new long[10];
					data.put(channel, channelData);
				}
				else
				{
					channelData = data.get(channel);
				}
				channelData[2] = cou;//开户数
				channelData[4] = sum;//授信总额
				channelData[5] = sum/cou;//人均额度
				
			}

			ot.setProgress((this.pross * step) +(this.pross/5)*2);

			sql =   "select channel,count(1) cou,sum(amount) sum, sum(c.baseTotCreLine) sum2 from "+
					"(SELECT a.amount , b.channel  ,c.baseTotCreLine   FROM      db_loans.loans_cashextract a,  db_loans.loans_user b , db_loans.loans_loanacctinfo c   where  a.customerId = b.customerId  and  a.customerId = c.customerId and b.channel = '"+channel+"'  and   a.transTime    like '"+queryTime+"%' )  "+
					"c  group by channel ";
			List<Map<String,String>> loanlist = ld.Query(sql);
			for(int i = 0;i < loanlist.size();i++)
			{
				Map<String,String> row = loanlist.get(i);
				String channel = row.get("channel");
				long cou = Long.parseLong(row.get("cou"));
				long sum = Long.parseLong(row.get("sum"))/100;
				
				String ss2 = row.get("sum2");
				if(ss2.contains("."))
					ss2 = ss2.substring(0, ss2.indexOf("."));
				long sum2 = Long.parseLong(ss2)/100;
				long[] channelData = null;
				if(data.get(channel) == null)
				{
					channelData = new long[10];
					data.put(channel, channelData);
				}
				else
				{
					channelData = data.get(channel);
				}
				channelData[3] = cou;//放款人数
				channelData[8] = sum;//渠道提现总额
				channelData[9] = sum2;//渠道授信总额
				

				channelData[6] = cou;//首提人数
				channelData[7] = sum2;//首提总额
			}
			
			ot.setProgress((this.pross * step) +(this.pross/5)*3);

			sql =   " select channel,count(1) cou,sum(c.baseTotCreLine) sum from  "+
					" (SELECT c.baseTotCreLine , b.channel    FROM      db_loans.loans_cashextract a,  db_loans.loans_user b , db_loans.loans_loanacctinfo c  where  a.customerId = b.customerId   and  a.customerId = c.customerId   and  a.customerId = c.customerId and b.channel = '"+channel+"'   and a.customerId  in "+
					" (SELECT    customerId   FROM      db_loans.loans_cashextract a  where a.transTime    < '"+queryTime+"'   ) "+
					" and a.transTime  like '"+queryTime+"%' ) "+
					" c  group by channel ";

			List<Map<String,String>> firsloan = ld.Query(sql);
			Map<String,Long> sumMap = new HashMap<String,Long>();
			Map<String,Long> couMap = new HashMap<String,Long>();
			
			for(int i = 0;i < firsloan.size();i++)
			{
				Map<String,String> row = firsloan.get(i);
				String channel = row.get("channel");
				long cou = Long.parseLong(row.get("cou"));//复贷人数
				

				String ss2 = row.get("sum");
				if(ss2.contains("."))
					ss2 = ss2.substring(0, ss2.indexOf("."));
				
				long sum = Long.parseLong(ss2)/100;//复贷总额
				long[] channelData = null;
				if(data.get(channel) == null)
				{
					channelData = new long[10];
					data.put(channel, channelData);
				}
				else
				{
					channelData = data.get(channel);
				}
				cou = channelData[6] - cou;//放款人数 - 复贷人数 = 首提人数
				sum = channelData[7] - sum;//渠道授信总额 - 复贷总额 = 首提总额
				
				channelData[6] = cou;//首提人数
				channelData[7] = sum;//首提总额
			}
			
			

			ot.setProgress((this.pross * step) +(this.pross/5)*4);
			


			if(queryType==0)
			{
			}
			else if(queryType==1)
			{
				String sql3 = "delete from operate_reportform_day where channelId='"+channelId+"' and date = '"+queryTime+"'";
				od.Update(sql3);
				
			}
			else if(queryType==2)
			{
				String sql3 = "delete from operate_reportform_month where channelId='"+channelId+"' and  date = '"+queryTime+"'";
				od.Update(sql3);
			}
			
			
			
			for (Entry<String, long[]> entry : data.entrySet()) {
				String channel = entry.getKey();
				long[] dd = entry.getValue();
				long ish5 = 0;
				String channelId = "0";
				long h5Register = 0;
				if(channels.get(channel) != null)
				{
					ish5 = 1;
					channelId = channels.get(channel).get("id");
					h5Register = dd[0];
				}
				
				long register = dd[0];//注册人数
				long upload = dd[1];//进件人数
				long account = dd[2];//开户数
				long loan = dd[3];//放款人数
				long credit = dd[4];//授信总额
				long perCapitaCredit = dd[5];//人均额度
				long firstGetPer = dd[6];//首提人数
				long firstGetSum = dd[7];//首提总额
				long channelSum = dd[8];//渠道提现总额
				String date = queryTime;
				
				register = (long) (this.proportion*register);
				upload = (long) (this.proportion*upload);
				account = (long) (this.proportion*account);
				loan = (long) (this.proportion*loan);
				credit = (long) (this.proportion*credit);
				credit = credit/100*100;
				
				firstGetPer = (long) (this.proportion*firstGetPer);
				firstGetSum = (long) (this.proportion*firstGetSum);
				firstGetSum = firstGetSum/100*100;
				channelSum = (long) (this.proportion*channelSum);
				channelSum = channelSum/100*100;
				
				System.out.println(11);

				String insertSql = "insert into "+table+" (channelId,channel,date,h5Register,register,upload,account,loan,credit,perCapitaCredit,firstGetPer,firstGetSum,channelSum)"
						+ "values("+channelId+",'"+channel+"','"+date+"',"+h5Register+","+register+","+upload+","+account+","+loan+","+credit+","+perCapitaCredit+","+firstGetPer+","+firstGetSum+","
								+ ""+channelSum+")";
				try {
					od.Update(insertSql);
					
				} catch (Exception e) {
					System.out.println(insertSql);
				}
				
			}

			ot.setProgress((this.pross * step) +this.pross);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
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
