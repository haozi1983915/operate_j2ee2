package com.maimob.server.data.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import com.maimob.server.db.entity.OptimizationTask;
import com.maimob.server.importData.dao.LoansDao;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.StringUtils;

public class OperateData2 {

	@PostConstruct
	public void init() {
		
		System.out.println(1111);
		
	}
	
	
	OptimizationTask ot;

	public OperateData2(OptimizationTask ot) {
		ot.setProgressMsg("开始运行");
		this.ot = ot;
		this.StartDate = ot.getStartDate();
		this.endDate = ot.getEndDate();
		this.channel = ot.getChannel(); 
	}
	
	
	
	

	public static void main(String[] args) {
		
		Map ss = new HashMap();
		ss.put("id", "1517918294658");
//		ss.put("channel", "woaika_lianjie");
		ss.put("startDate", "2018-02-01");
		ss.put("endDate", "2018-02-01");
		ss.put("optimization", "0");
		ss.put("tableId", "30");
		ss.put("adminId", "1516704387763");
		

//		String content = JSONObject.toJSONString(ss);
//		System.out.println(content);
		
		
		OptimizationTask ot = new OptimizationTask(ss);
		OperateData2 pd = new OperateData2(ot);
		pd.Statistics();
	}

	public void Statistics() {
		for (int i = 1; i <= 1; i++) {
			queryType = i;
			StatisticalRegister();
		}
	}

	Map<String, List<Map<String, String>>> reward = new HashMap<String, List<Map<String, String>>>();
	Map<String, Map<String, String>> channels = new HashMap<String, Map<String, String>>();
	long queryType = 2;// 0,小时 1，天 2，月

	String table = "operate_reportform"; 
	long nextJg = 0;
	// String dateFormat = "yyyy-MM-dd HH";
	String dateFormat = "yyyy-MM-dd";
	String StartDate = "2018-01-30";
	String endDate = "2018-01-30";
	String channel = "";  

	float pross = 100;

	int step = 0;


	// 统计注册
	private void StatisticalRegister() {


		if (queryType == 0) {
			dateFormat = "yyyy-MM-dd HH";
			table = "operate_reportform";
			nextJg = 3600000;
		} else if (queryType == 1) {
			nextJg = 3600000l * 24l;
			dateFormat = "yyyy-MM-dd";
			table = "operate_reportform";
		} else if (queryType == 2) {
			nextJg = 3600000l * 24l * 20l;
			dateFormat = "yyyy-MM";
			table = "operate_reportform";
		}

		try {
			ot.setStatus(1);

			String statisticalTime = "";

			if (queryType == 0) {
				statisticalTime = StartDate.substring(0, 13);
			} else if (queryType == 1) {
				statisticalTime = StartDate;
			} else if (queryType == 2) {
				statisticalTime = StartDate.substring(0, 7);
			}

			String queryTime = statisticalTime;

			float day = AppTools.daysBetween(StartDate, endDate);
			ot.setDays((int) day);
			pross = 100.0f / (day);

			while (true) {
				try {

					if (endDate.equals(queryTime)) {
						System.out.println(queryTime);
						LastStatistical(queryTime);
						break;
					} else if (!endDate.equals(queryTime)) {
						System.out.println(queryTime);
						LastStatistical(queryTime);
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
 

	// String queryTime_day = "";

	private String next(String queryTime) {
		try {

			long time = stringToLong(queryTime, dateFormat);
			if (queryType == 2) {
				queryTime = queryTime + "-20";
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

	private boolean LastStatistical(String queryTime) {

		LoansDao ld = new LoansDao();
		OperateDao od = new OperateDao();
		try {
			
			ot.setStep(step);
			Map<String, long[]> data = new HashMap<String, long[]>();
			ot.setRunDate(queryTime);

			String where = "";
			if (!StringUtils.isStrEmpty(channel))
				where = " and channel = '" + channel + "' ";
			String sql = "SELECT channel,resultStatus,count(1)cou  FROM db_loans.loans_user where registerTime like '"
					+ queryTime + "%'  " + where + "  group by channel,resultStatus";
			List<Map<String, String>> register_upload = ld.Query(sql);

			for (int i = 0; i < register_upload.size(); i++) {
				Map<String, String> row = register_upload.get(i);

				String channel = row.get("channel");
				String resultStatus = row.get("resultStatus");
				long cou = Long.parseLong(row.get("cou"));

				long[] channelData = null;
				if (data.get(channel) == null) {
					channelData = new long[15];
					data.put(channel, channelData);
				} else {
					channelData = data.get(channel);
				}
				channelData[0] += cou;// 注册人数

				if (!StringUtils.isStrEmpty(resultStatus))
					channelData[1] += cou;// 进件人数
			}
			ot.setProgress((this.pross * step) + (this.pross / 5));
			
			

			where = "";
			if (!StringUtils.isStrEmpty(channel))
				where = " and b.channel = '" + channel + "' ";
			sql = " select c.channel,count(1)cou from " + 
					" (SELECT b.channel  FROM loans_logininfo a,loans_user b where  a.customerId = b.customerId  " + where + " and b.registerTime  like '" + queryTime + "%' )c " + 
					" group by c.channel ";
			List<Map<String, String>> register_activation = ld.Query(sql);

			for (int i = 0; i < register_activation.size(); i++) {
				Map<String, String> row = register_activation.get(i);

				String channel = row.get("channel");
				long cou = Long.parseLong(row.get("cou"));

				long[] channelData = null;
				if (data.get(channel) == null) {
					channelData = new long[15];
					data.put(channel, channelData);
				} else {
					channelData = data.get(channel);
				}
				channelData[12] += cou;// 登录激活人数

			}
			
			
			
			
			

			where = "";
			if (!StringUtils.isStrEmpty(channel))
				where = " and b.channel = '" + channel + "' ";

			sql = " select channel,count(1) cou from "
					+ "( SELECT b.channel ,c.baseTotCreLine  FROM db_loans.loans_acctstatus a,  db_loans.loans_user b  "
					+ "where  a.customerId = b.customerId " + where
					+ "   and b.registerTime like '" + queryTime + "%'" + " )" + "c  group by channel ";
			List<Map<String, String>> accountlist = ld.Query(sql);
			for (int i = 0; i < accountlist.size(); i++) {
				Map<String, String> row = accountlist.get(i);
				String channel = row.get("channel");
				long cou = Long.parseLong(row.get("cou"));

				String ss2 = row.get("sum");
				if (ss2.contains("."))
					ss2 = ss2.substring(0, ss2.indexOf("."));

				long sum = Long.parseLong(ss2) / 100;

				long[] channelData = null;
				if (data.get(channel) == null) {
					channelData = new long[15];
					data.put(channel, channelData);
				} else {
					channelData = data.get(channel);
				}
				channelData[2] = cou;// 开户数
//				channelData[4] = sum;// 授信总额
//				channelData[5] = sum / cou;// 人均额度

			}

			ot.setProgress((this.pross * step) + (this.pross / 5) * 2);

			where = "";
			if (!StringUtils.isStrEmpty(channel))
				where = " and b.channel = '" + channel + "' ";
			
			sql = "select c.channel,count(1)cou from (" + 
					"SELECT   b.channel   FROM   db_loans.loans_cashextract a,  db_loans.loans_user b " + 
					"where  a.customerId = b.customerId " + where + "  and b.registerTime like '" + queryTime + "%' " + 
					") c group by channel";
			
			List<Map<String, String>> loanlist = ld.Query(sql);
			for (int i = 0; i < loanlist.size(); i++) {
				Map<String, String> row = loanlist.get(i);
				String channel = row.get("channel");
				long cou = Long.parseLong(row.get("cou"));
				
				long[] channelData = null;
				if (data.get(channel) == null) {
					channelData = new long[15];
					data.put(channel, channelData);
				} else {
					channelData = data.get(channel);
				}
				channelData[3] = cou;// 放款人数 

			}

			ot.setProgress((this.pross * step) + (this.pross / 5) * 3);

			
			
			
			
			
			
			

			ot.setProgress((this.pross * step) + (this.pross / 5) * 4);

			if (queryType == 0) {
			} else if (queryType == 1) {
				where = "";
				if (!StringUtils.isStrEmpty(channel))
					where = " and channel = '" + channel + "' ";
				
				String sql3 = "delete from operate_reportform where    date = '"
						+ queryTime + "'  "+where+" ";
				od.Update(sql3);

			}  
			

			for (Entry<String, long[]> entry : data.entrySet()) {
				String channel = entry.getKey();
				long[] dd = entry.getValue();
				long ish5 = 0;
				String channelId = "0";
				String proxyId = "0";
				String rewardId = "0";
				String adminId = "0";
				long h5Register = 0;


				String date = queryTime;
				
				String channelName = "";
				String attribute = "0";
				String type = "0";
				String subdivision = "0";
				String showTime = "0";
				if (channels.get(channel) == null) {

					String sql1 = " select * from operate_channel where channel='"+channel+"' ";

					List<Map<String, String>> channelList = od.Query(sql1);
					for(int i = 0;i < channelList.size();i++)
					{
						Map<String, String> channelobj = channelList.get(i);
						channels.put(channelobj.get("channel"), channelobj);
					}
				}
				
				
				if (channels.get(channel) != null) {
					ish5 = 1;
					channelId = channels.get(channel).get("id");
					proxyId = channels.get(channel).get("proxyId");
					rewardId = channels.get(channel).get("rewardId");
					adminId = channels.get(channel).get("adminId");

					channelName = channels.get(channel).get("channelName");
					 attribute = channels.get(channel).get("attribute");
					 type = channels.get(channel).get("type");
					 subdivision = channels.get(channel).get("subdivision");

					 String synchronous = channels.get(channel).get("synchronous");
					 if(synchronous.equals("0"))
					 {
						 
						 showTime = next(date)+" 11:00:00";
					 }
					h5Register = dd[0];
					
					String sql1 = " select * from operate_reward where id="+rewardId+" ";

					List<Map<String, String>> rewardList = od.Query(sql1);
					reward.put(rewardId, rewardList);
				}
				
				
				long register = dd[0];// 注册人数
				long activation = dd[4];// 复贷总额
				long upload = dd[1];// 进件人数
				long account = dd[2];// 开户数
				long loan = dd[3];// 放款人数
				
				String activationConversion = bl(activation, register);

				String uploadConversion = bl(upload, register);


				String accountConversion = bl(account, upload);

				String loanConversion = bl(loan, account);
								
				String month = date.substring(0, date.lastIndexOf("-"));

				
//
//				String insertSql = "insert into " + table
//						+ " (channelId,channel,date,month,h5Register,activation,outActivation,register,outRegister,upload,outUpload,uploadConversion"
//						+ ",account,outAccount,accountConversion,loan,outLoan,loanConversion,loaner"
//						+ ",credit,outCredit,perCapitaCredit,outPerCapitaCredit,firstGetPer,outFirstGetPer,firstGetSum,outFirstGetSum2,outFirstGetSum"
//						+ ",firstPerCapitaCredit,secondGetPer,secondGetPi,secondGetSum,secondPerCapitaCredit,channelSum,"
//						+ "outChannelSum,channelCapitaCredit,income,cost,grossProfit,grossProfitRate,proxyId,optimization,costType,adminId,channelName,channelAttribute,channelType,subdivision,showTime)"
//						+ "values(" + channelId + ",'" + channel + "','" + date + "','" + month + "'," + h5Register + "," + activation
//						+ "," + outActivation + "," + register + "," + outRegister + "," + upload + "," + outUpload + "," + uploadConversion
//						+ "," + account + "," + outAccount + "," + accountConversion + "," + loan + "," + outLoan + ","
//						+ loanConversion + "," + loaner + "," + credit + "," + outCredit + "," + perCapitaCredit + ","
//						+ outPerCapitaCredit + "," + firstGetPer + "," + outFirstGetPer + "," + firstGetSum + ","
//						+ outFirstGetSum2 + "," + outFirstGetSum + "," + firstPerCapitaCredit + "," + secondGetPer + "," + secondGetPi + ","
//						+ secondGetSum + "," + secondPerCapitaCredit + "," + channelSum + "," + outChannelSum + ","
//						+ channelCapitaCredit + "," + income+ "," + cost+ "," + grossProfit+ "," + grossProfitRate+ ","+proxyId+ ","+showOP+ ","+costType+ ",'"+adminId+ "','"+channelName+ "'"
//						+ ","+attribute+ ","+type+ ","+subdivision+ ",'" + showTime+ "')";
//				try {
//					od.Update(insertSql);
//					if(proportion != 1)
//					{
//						//保存最后一次优化比例
//						String sql2 = "update operate_data_log set optimization="+proportion+" where channel= "+channel+" and date = '"+date+"' ";
//						int yx = od.Update(sql2);
//						if(yx==0)
//						{
//							sql2 = "insert into operate_data_log(optimization,channel,date) values("+proportion+" ,"+channel+" , '"+date+"') ";
//							yx = od.Update(sql2);
//						}
//					}
//
//				} catch (Exception e) {
//					System.out.println(insertSql);
//				}

			}

			ot.setProgress((this.pross * step) + this.pross);
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			od.close();
			ld.close();
		}

		return true;
	}
	
	
	public double getCost(long firstGetPer, long register,String rewardid)
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
			double cost = 0;
			
			long num = 0;
			if(typeId.equals("26"))
			{
				num = register;
			}
			else if(typeId.equals("27"))
			{
				num = firstGetPer;
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
					
					if(max < num)
					{
						cost += (max - smax) * price1;
					}
					else
					{
						cost += (num - smax) * price1;
						break;
					}
					smax = max;
				}
			}
			return cost;
		}
		
		return 0;
	}
	
	

	public String bl(long l1, long l2) {
		if (l2 == 0 && l1 == 0)
			return "0";

		else if (l2 == 0 && l1 != 0)
			l2 = 1;

		String c = ((l1 * 1.0 / l2) * 100) + "";

		if (c.contains("."))
			c = c.substring(0, c.indexOf("."));

		return c;
	}

	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}

	public static long dateToLong(Date date) {
		return date.getTime();
	}

	public static long stringToLong(String strTime, String formatType)   {
		try {
			Date date = stringToDate(strTime, formatType); // String类型转成date类型
			if (date == null) {
				return 0;
			} else {
				long currentTime = dateToLong(date); // date类型转成long类型
				return currentTime;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	// long类型转换为String类型
	// currentTime要转换的long类型的时间
	// formatType要转换的string类型的时间格式
	public static String longToString(long currentTime, String formatType) throws ParseException {
		Date date = longToDate(currentTime, formatType); // long类型转成Date类型
		String strTime = dateToString(date, formatType); // date类型转成String
		return strTime;
	}

	public static Date longToDate(long currentTime, String formatType) throws ParseException {
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
		return date;
	}

	public static Date stringToDate(String strTime, String formatType) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}

}
