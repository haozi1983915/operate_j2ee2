package com.maimob.server.importData.data;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maimob.server.importData.dao.LoansDao;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.StringUtils; 

public class InportData extends Thread  {
	

	LoansDao ld = new LoansDao();
	OperateDao od = new OperateDao();
	
	
	
	public static void main(String[] args) {
		
		InportData id = new InportData();
		id.StatisticalRegister();
		
		
		
		
		
		
	}
	
	
	//统计注册
	public void StatisticalRegister()
	{
		try {
			String sql = " select * from operate_data_log order by date desc limit 0,1  ";
			
			List<Map<String, String>> datalogList = od.Query(sql);
			
			String statisticalTime = "";
			int year = 0;
			int month = 0;
			int day = 0;
			int hour = 0;
			int finish = 0;
			if(datalogList.size() > 0)
			{
				statisticalTime = datalogList.get(0).get("date");
				year = Integer.parseInt(datalogList.get(0).get("year"));
				month = Integer.parseInt(datalogList.get(0).get("month"));
				day = Integer.parseInt(datalogList.get(0).get("day"));
				hour = Integer.parseInt(datalogList.get(0).get("hour"));
				finish = Integer.parseInt(datalogList.get(0).get("finish"));
			}
			else
			{
				String sql2 = "SELECT  min(registertime) minTime FROM db_loans.loans_user   ";

				List<Map<String, String>> minTimeOther = ld.Query(sql2);
				//'2017-08-09 20:33:16'
				String minTime = minTimeOther.get(0).get("minTime");
				if(minTime.length() == 19)
				{
					statisticalTime = minTime.substring(0, 10);
					year = Integer.parseInt(minTime.substring(0, 4));
					month = Integer.parseInt(minTime.substring(5, 7));
					day = Integer.parseInt(minTime.substring(8, 10));
					hour = Integer.parseInt(minTime.substring(11, 13));
					finish = 0;
				}
			}

			String queryTime = statisticalTime;
			if(finish == 1)
				queryTime = nextHour(queryTime);
			
			while(true)
			{

				String nowTime = dateToString(new Date(), dateFormat);

				if(nowTime.equals(queryTime))
				{
					save(queryTime,0);
					break;
					
				}
				else if(!nowTime.equals(queryTime))
				{
					System.out.println(queryTime);
					LastStatistical(queryTime);
					save(queryTime,1);
					queryTime = nextDay(queryTime);
				}
				
				
				
				
				
				
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}

//	String dateFormat = "yyyy-MM-dd HH";
	String dateFormat = "yyyy-MM-dd";
	
	public void save(String queryTime,int finish)
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
	

	public String nextDay(String queryTime) 
	{
		try {
			long time = stringToLong(queryTime, dateFormat);
			
			time += 3600000*24;
			
			queryTime = longToString(time, dateFormat);
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return queryTime;
	}
	
	public String nextHour(String queryTime) 
	{
		try {
			long time = stringToLong(queryTime, dateFormat);
			
			time += 3600000;
			
			queryTime = longToString(time, dateFormat);
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return queryTime;
	}
	
	public boolean LastStatistical(String queryTime)
	{

		try {
			 Map<String,int[]> data = new HashMap<String,int[]>();  
			
			String sql = "SELECT channel,resultStatus,count(1)cou  FROM db_loans.loans_user where registerTime like '"+queryTime+"%'   group by channel,resultStatus";
			List<Map<String,String>> register_upload = ld.Query(sql);
			
			for(int i = 0;i < register_upload.size();i++)
			{
				Map<String,String> row = register_upload.get(i);
				
				String channel = row.get("channel");
				String resultStatus = row.get("resultStatus");
				int cou = Integer.parseInt(row.get("cou"));
				
				int[] channelData = null;
				if(data.get(channel) == null)
				{
					channelData = new int[10];
					data.put(channel, channelData);
				}
				else
				{
					channelData = data.get(channel);
				}
				channelData[0] += cou;
				
				if(!StringUtils.isStrEmpty(resultStatus))
					channelData[1] += cou;
			}
			
			
			sql =    " select channel,count(1) cou , sum(c.baseUsedCreLine) sum from "+
				"( SELECT b.channel ,c.baseUsedCreLine  FROM db_loans.loans_acctstatus a,  db_loans.loans_user b  , db_loans.loans_loanacctinfo c  where  a.customerId = b.customerId and  a.customerId = c.customerId and   a.loanApproveDate    like '"+queryTime+"%' )"+
				"c  group by channel ";
			List<Map<String,String>> account = ld.Query(sql);
			for(int i = 0;i < account.size();i++)
			{
				Map<String,String> row = account.get(i);
				String channel = row.get("channel");
				int cou = Integer.parseInt(row.get("cou"));
				int sum = Integer.parseInt(row.get("cou"));
				int[] channelData = null;
				if(data.get(channel) == null)
				{
					channelData = new int[10];
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
			

			sql =   "select channel,count(1) cou,sum(amount) sum, sum(c.baseUsedCreLine) sum2 from "+
					"(SELECT a.amount , b.channel  ,c.baseUsedCreLine   FROM      db_loans.loans_cashextract a,  db_loans.loans_user b , db_loans.loans_loanacctinfo c   where  a.customerId = b.customerId  and  a.customerId = c.customerId  and   a.transTime    like '"+queryTime+"%' )  "+
					"c  group by channel ";
			List<Map<String,String>> loan = ld.Query(sql);
			for(int i = 0;i < loan.size();i++)
			{
				Map<String,String> row = account.get(i);
				String channel = row.get("channel");
				int cou = Integer.parseInt(row.get("cou"));
				int sum = Integer.parseInt(row.get("sum"));
				int sum2 = Integer.parseInt(row.get("sum2"));
				int[] channelData = null;
				if(data.get(channel) == null)
				{
					channelData = new int[10];
					data.put(channel, channelData);
				}
				else
				{
					channelData = data.get(channel);
				}
				channelData[3] = cou;//放款人数
				channelData[8] = sum;//渠道提现总额
				channelData[9] = sum2;//渠道授信总额
			}
			
			

			sql =   " select channel,count(1) cou,sum(c.baseUsedCreLine) sum from  "+
					" (SELECT c.baseUsedCreLine , b.channel    FROM      db_loans.loans_cashextract a,  db_loans.loans_user b   where  a.customerId = b.customerId   and  a.customerId = c.customerId    and a.customerId  in "+
					" (SELECT    customerId   FROM      db_loans.loans_cashextract a  where a.transTime    < '2017-11-03'   ) "+
					"    and   a.transTime    like '2017-11-03%' ) "+
					" c  group by channel ";

			List<Map<String,String>> firsloan = ld.Query(sql);
			for(int i = 0;i < loan.size();i++)
			{
				Map<String,String> row = account.get(i);
				String channel = row.get("channel");
				int cou = Integer.parseInt(row.get("cou"));//复贷人数
				int sum = Integer.parseInt(row.get("sum"));//复贷总额
				int[] channelData = null;
				if(data.get(channel) == null)
				{
					channelData = new int[10];
					data.put(channel, channelData);
				}
				else
				{
					channelData = data.get(channel);
				}
				cou = channelData[3] - cou;//放款人数 - 复贷人数 = 首提人数
				sum = channelData[9] - sum;//渠道授信总额 - 复贷总额 = 首提总额
				
				channelData[6] = cou;//首提人数
				channelData[7] = sum;//渠道提现总额
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
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
