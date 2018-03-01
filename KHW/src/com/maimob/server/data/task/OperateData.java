package com.maimob.server.data.task;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.entity.OptimizationTask;
import com.maimob.server.importData.dao.LoansDao;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.StringUtils;

public class OperateData  {
   
	
	OptimizationTask ot;

	public OperateData(OptimizationTask ot) {
		ot.setProgressMsg("开始运行");
		this.ot = ot;
		this.StartDate = ot.getStartDate();
		this.endDate = ot.getEndDate();
		this.channel = ot.getChannel();
		this.optimization = ot.getOptimization();
		if (this.optimization == 0)
			this.proportion = 1;
		else if (this.optimization > 0) {
			this.proportion = Float.parseFloat("0."+(100 - ot.getOptimization()));
		}

	}
	
	
	
	

	public static void main(String[] args) {
		
		Map ss = new HashMap();
		ss.put("id", "1517918294658");
//		ss.put("channel", "duorong_cesyy");
		ss.put("startDate", "2018-02-28");
		ss.put("endDate", "2018-02-28");
		ss.put("optimization", "-1");
		ss.put("tableId", "30");
		ss.put("adminId", "1516704387763");
		

//		String content = JSONObject.toJSONString(ss);
//		System.out.println(content);
		
		
		OptimizationTask ot = new OptimizationTask(ss);
		OperateData pd = new OperateData(ot);
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
	int optimization = -1;
	long nextJg = 0;
	// String dateFormat = "yyyy-MM-dd HH";
	String dateFormat = "yyyy-MM-dd";
	String StartDate = "2018-01-30";
	String endDate = "2018-01-30";
	String channel = ""; 
	double proportion = 0;

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

	private int getOp(long queryDate,String channelId) {

		List<Optimization> ops = Optimization.getOptimizationList(Long.parseLong(channelId)).get(Long.parseLong(channelId));

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

			sql = " select channel,count(1) cou , sum(c.baseTotCreLine) sum from "
					+ "( SELECT b.channel ,c.baseTotCreLine  FROM db_loans.loans_acctstatus a,  db_loans.loans_user b  , db_loans.loans_loanacctinfo c  "
					+ "where  a.customerId = b.customerId and  a.customerId = c.customerId  " + where
					+ "   and a.loanApproveDate like '" + queryTime + "%'" + " )" + "c  group by channel ";
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
				channelData[4] = sum;// 授信总额
				channelData[5] = sum / cou;// 人均额度

			}

			ot.setProgress((this.pross * step) + (this.pross / 5) * 2);

			where = "";
			if (!StringUtils.isStrEmpty(channel))
				where = " and b.channel = '" + channel + "' ";
			sql = "select channel,count(1) cou,sum(amount) sum, sum(c.baseTotCreLine) sum2 from "
					+ "(SELECT a.amount , b.channel  ,c.baseTotCreLine FROM   db_loans.loans_cashextract a,  db_loans.loans_user b , db_loans.loans_loanacctinfo c  "
					+ " where  a.customerId = b.customerId  and  a.customerId = c.customerId " + where
					+ " and   a.transTime    like '" + queryTime + "%' )  " + "c  group by channel ";
			List<Map<String, String>> loanlist = ld.Query(sql);
			for (int i = 0; i < loanlist.size(); i++) {
				Map<String, String> row = loanlist.get(i);
				String channel = row.get("channel");
				long cou = Long.parseLong(row.get("cou"));
				long sum = Long.parseLong(row.get("sum")) / 100;

				String ss2 = row.get("sum2");
				if (ss2.contains("."))
					ss2 = ss2.substring(0, ss2.indexOf("."));
				long sum2 = Long.parseLong(ss2) / 100;
				long[] channelData = null;
				if (data.get(channel) == null) {
					channelData = new long[15];
					data.put(channel, channelData);
				} else {
					channelData = data.get(channel);
				}
				channelData[3] = cou;// 放款人数
				channelData[8] = sum;// 渠道提现总额
				
				channelData[9] = sum2;// 渠道授信总额
				
				channelData[10] = cou;// 复贷人数
				channelData[11] = sum;// 复贷总额

			}

			ot.setProgress((this.pross * step) + (this.pross / 5) * 3);

			where = "";
			if (!StringUtils.isStrEmpty(channel))
				where = " and b.channel = '" + channel + "' ";
			sql = " select channel,count(1) cou,sum( amount ) sum from  "
					+ " (SELECT  a.amount , b.channel    FROM      db_loans.loans_cashextract a,  db_loans.loans_user b "
					+ "where  a.customerId = b.customerId "
					+ where + "   and a.customerId not in "
					+ " (SELECT customerId  FROM  db_loans.loans_cashextract a  where a.transTime    < '"
					+ queryTime + "'   ) " + " and a.transTime  like '" + queryTime + "%' ) " + " c  group by channel ";

			List<Map<String, String>> firsloan = ld.Query(sql);

			for (int i = 0; i < firsloan.size(); i++) {
				Map<String, String> row = firsloan.get(i);
				String channel = row.get("channel");
				long cou = Long.parseLong(row.get("cou"));// 复贷人数

				String ss2 = row.get("sum");
				if (ss2.contains("."))
					ss2 = ss2.substring(0, ss2.indexOf("."));

				long sum = Long.parseLong(ss2) / 100;// 复贷总额
				long[] channelData = null;
				if (data.get(channel) == null) {
					channelData = new long[15];
					data.put(channel, channelData);
				} else {
					channelData = data.get(channel);
				}

				channelData[6] = cou;// 首提人数
				channelData[7] = sum;// 首提总额

				channelData[10] = channelData[10]-channelData[6];// 复贷人数
				channelData[11] = channelData[11]-channelData[7];// 复贷总额


			}
			
			

			where = "";//提现大于1万=1万
			if (!StringUtils.isStrEmpty(channel))
				where = " and b.channel = '" + channel + "' ";
			sql = " SELECT  a.amount  , b.channel FROM db_loans.loans_cashextract a, db_loans.loans_user b " + 
					"where  a.customerId = b.customerId " + 
					"and a.transTime  like '"+queryTime+"%'  "+where+" "
					+ "and a.customerId not in (SELECT customerId FROM db_loans.loans_cashextract a where a.transTime < '"+queryTime+"' ) ";

			List<Map<String, String>> loanlist2 = ld.Query(sql);

			for (int i = 0; i < loanlist2.size(); i++) {
				Map<String, String> row = loanlist2.get(i);
				String channel = row.get("channel");
				
				String ss2 = row.get("amount");
				if (ss2.contains("."))
					ss2 = ss2.substring(0, ss2.indexOf("."));

				long baseTotCreLine = Long.parseLong(ss2) / 100;// 复贷总额
				

				long[] channelData = null;
				if (data.get(channel) == null) {
					channelData = new long[15];
					data.put(channel, channelData);
				} else {
					channelData = data.get(channel);
				}
				if(baseTotCreLine > 10000)
					baseTotCreLine = 10000;
				channelData[13] += baseTotCreLine;// 复贷人数
			}
			
			
			
			
			
			

			ot.setProgress((this.pross * step) + (this.pross / 5) * 4);

			if (queryType == 0) {
			} else if (queryType == 1) {
				where = "";
				if (!StringUtils.isStrEmpty(channel))
					where = " and channel = '" + channel + "' ";
				
				String sql3 = "delete from operate_reportform where    date = '"
						+ queryTime + "'  "+where+" ";
				od.Update(sql3);

			} else if (queryType == 2) {
				where = "";
				if (!StringUtils.isStrEmpty(channel))
					where = " and channel = '" + channel + "' ";
				String sql3 = "delete from operate_reportform_month where  date = '"
						+ queryTime + "'  "+where;
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
				

				
				
				


				List<Map<String,String>> or_cost = od.Query("select * from operate_data_log where channelId="
				+channelId+" and date = '"+date+"' ");
				
				

				int showOP = optimization;
				if (optimization == -1) {//-1用设置的比例  -2用最近一次的比例
					long queryDate = this.stringToLong(queryTime, "yyyy-MM-dd");
					showOP = getOp(queryDate,channelId);
				}
				
				if(or_cost.size()>0)
				{
					String optimization = or_cost.get(0).get("optimization");
					
					
					if(this.optimization == -2 && !StringUtils.isStrEmpty(optimization))//-2 用最近的优化比例
					{
						try {
							int pi = Integer.parseInt(optimization);
							if(pi > 0)
								this.proportion = pi;
						} catch (Exception e) {
							// TODO: handle exception
						}
						
					}
					
				}
				
				long register = dd[0];// 注册人数

				long activation = dd[12];// 复贷总额
				long upload = dd[1];// 进件人数
				long account = dd[2];// 开户数
				long loan = dd[3];// 放款人数
				long credit = dd[4];// 授信总额
				long perCapitaCredit = dd[5];// 人均额度
				long firstGetPer = dd[6];// 首提人数
				long firstGetSum = dd[7];// 首提总额
				long channelSum = dd[8];// 渠道提现总额

				long secondGetPer = dd[10];// 复贷人数
				long secondGetPi = dd[10];// 复贷人数
				long secondGetSum = dd[11];// 复贷总额
				long outFirstGetSum2 = dd[13];// 复贷总额



				long outRegister = (long) (this.proportion * register);// 外部注册人数


				long outActivation = (long) (this.proportion * activation);;
				
				
				if (account != 0)
					perCapitaCredit = credit / account;// 人均额度

				String activationConversion = bl(activation, register);

				String uploadConversion = bl(upload, register);

				long outAccount = (long) (this.proportion * account);

				long outUpload = (long) (this.proportion * upload);
				long outLoan = (long) (this.proportion * loan);
				long outCredit = (long) (this.proportion * credit);
				outCredit = outCredit / 100 * 100;

				String accountConversion = bl(account, upload);

				String loanConversion = bl(loan, account);
				long loaner = loan;

				long outPerCapitaCredit = 0;
				if (outAccount != 0)
					outPerCapitaCredit = outCredit / outAccount;// 人均额度

				long outFirstGetPer = (long) (this.proportion * firstGetPer);
				long outFirstGetSum = (long) (this.proportion * outFirstGetSum2);
				outFirstGetSum = outFirstGetSum / 100 * 100;

				long firstPerCapitaCredit = 0;
				if (firstGetPer != 0)
					firstPerCapitaCredit = firstGetSum / firstGetPer;// 首贷人均额度

				long outChannelSum = (long) (this.proportion * channelSum);
				outChannelSum = outChannelSum / 100 * 100;

				long secondPerCapitaCredit = 0;
				if (secondGetPer != 0)
					secondPerCapitaCredit = secondGetSum / secondGetPer;// 续贷人均额度

				long channelCapitaCredit = 0;
				if (loan != 0)
					channelCapitaCredit = channelSum / loan;

				double income = channelSum * 0.025;
				int costType = 0;
				double cost = this.getCost(  outFirstGetPer,   outRegister,  outFirstGetSum,  outAccount, rewardId);
				
				
				double grossProfit = income - cost;
				double grossProfitRate = 0;
				if(income != 0)
					grossProfitRate = grossProfit/income;
				
				String month = date.substring(0, date.lastIndexOf("-"));

				

				String insertSql = "insert into " + table
						+ " (channelId,channel,date,month,h5Register,activation,outActivation,register,outRegister,upload,outUpload,uploadConversion"
						+ ",account,outAccount,accountConversion,loan,outLoan,loanConversion,loaner"
						+ ",credit,outCredit,perCapitaCredit,outPerCapitaCredit,firstGetPer,outFirstGetPer,firstGetSum,outFirstGetSum2,outFirstGetSum"
						+ ",firstPerCapitaCredit,secondGetPer,secondGetPi,secondGetSum,secondPerCapitaCredit,channelSum,"
						+ "outChannelSum,channelCapitaCredit,income,cost,grossProfit,grossProfitRate,proxyId,optimization,costType,adminId,channelName,channelAttribute,channelType,subdivision,showTime)"
						+ "values(" + channelId + ",'" + channel + "','" + date + "','" + month + "'," + h5Register + "," + activation
						+ "," + outActivation + "," + register + "," + outRegister + "," + upload + "," + outUpload + "," + uploadConversion
						+ "," + account + "," + outAccount + "," + accountConversion + "," + loan + "," + outLoan + ","
						+ loanConversion + "," + loaner + "," + credit + "," + outCredit + "," + perCapitaCredit + ","
						+ outPerCapitaCredit + "," + firstGetPer + "," + outFirstGetPer + "," + firstGetSum + ","
						+ outFirstGetSum2 + "," + outFirstGetSum + "," + firstPerCapitaCredit + "," + secondGetPer + "," + secondGetPi + ","
						+ secondGetSum + "," + secondPerCapitaCredit + "," + channelSum + "," + outChannelSum + ","
						+ channelCapitaCredit + "," + income+ "," + cost+ "," + grossProfit+ "," + grossProfitRate+ ","+proxyId+ ","+showOP+ ","+costType+ ",'"+adminId+ "','"+channelName+ "'"
						+ ","+attribute+ ","+type+ ","+subdivision+ ",'" + showTime+ "')";
				try {
					od.Update(insertSql);
					if(proportion != 1)
					{
						//保存最后一次优化比例
						String sql2 = "update operate_data_log set optimization="+proportion+" where channel= '"+channel+"' and date = '"+date+"' ";
						int yx = od.Update(sql2);
						if(yx==0)
						{
							sql2 = "insert into operate_data_log(optimization,channel,date) values("+proportion+" ,'"+channel+"' , '"+date+"') ";
							yx = od.Update(sql2);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(insertSql);
				}

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
