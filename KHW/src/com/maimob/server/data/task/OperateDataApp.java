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

public class OperateDataApp {

	OptimizationTask ot;

	public OperateDataApp(OptimizationTask ot) {
		ot.setProgressMsg("开始运行");
		this.ot = ot;
		this.StartDate = ot.getStartDate();
		this.endDate = ot.getEndDate();
		this.channel = ot.getChannel();
		this.optimization = ot.getOptimization();
		if (this.optimization == 0)
			this.proportion = 1;
		else if (this.optimization > 0) {
			this.proportion = Float.parseFloat("0." + (100 - ot.getOptimization()));
		}

	}

	public static void main(String[] args) {

		Map ss = new HashMap();
		ss.put("id", "1517918294658");
//		ss.put("channel", "baojie_BJJR1026");
		ss.put("startDate", "2017-01-01");
		ss.put("endDate", "2018-02-15");
		ss.put("optimization", "-1");
		ss.put("tableId", "30");
		ss.put("adminId", "1516704387763");

		// String content = JSONObject.toJSONString(ss);
		// System.out.println(content);

		OptimizationTask ot = new OptimizationTask(ss);
		OperateDataApp pd = new OperateDataApp(ot);
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

	String table = "operate_reportform_app";
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
			table = "operate_reportform_app";
			nextJg = 3600000;
		} else if (queryType == 1) {
			nextJg = 3600000l * 24l;
			dateFormat = "yyyy-MM-dd";
			table = "operate_reportform_app";
		} else if (queryType == 2) {
			nextJg = 3600000l * 24l * 20l;
			dateFormat = "yyyy-MM";
			table = "operate_reportform_app";
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
			
			

			OperateDao od = new OperateDao();
			od.updateTaskLog("operate_reportform_app",endDate);
			od.close();

			ot.setStatus(2);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private int getOp(long queryDate, String channelId) {

		List<Optimization> ops = Optimization.getOptimizationList(Long.parseLong(channelId))
				.get(Long.parseLong(channelId));

		int opt = -1;
		if (ops != null && ops.size() > 0) {
			for (int i = 0; i < ops.size(); i++) {
				Optimization op = ops.get(i);
				if (op.endTime == 0)
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
			this.proportion = Float.parseFloat("0." + (100 - opt));
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

			
			String sql = " SELECT b.channel ,  idApplyStatus,debitCardStatus,homeTelArea,relaName1, personImgPath,vedioImgPath ,b.resultStatus  FROM db_loans.loans_userinfo a,loans_user b" + 
					" where  b.registerTime like '"+queryTime+"%'  and a.customerId = b.customerId ;";
			
			
			List<Map<String, String>> register_upload = ld.Query(sql);

			for (int i = 0; i < register_upload.size(); i++) {
				Map<String, String> row = register_upload.get(i);

				String channel = row.get("channel");
				String idApplyStatus = row.get("idApplyStatus");
				String debitCardStatus = row.get("debitCardStatus");

				String homeTelArea = row.get("homeTelArea");
				String relaName1 = row.get("relaName1");
				String personImgPath = row.get("personImgPath");
				String vedioImgPath = row.get("vedioImgPath");
				String resultStatus = row.get("resultStatus");

				long[] channelData = null;
				if (data.get(channel) == null) {
					channelData = new long[15];
					data.put(channel, channelData);
				} else {
					channelData = data.get(channel);
				}
				channelData[0] ++ ;// 注册人数
				
				if(idApplyStatus != null && idApplyStatus.equals("00"))
				{
					channelData[1] ++ ;// 身份证验证成功
				}

				if(debitCardStatus != null && debitCardStatus.equals("0"))
				{
					channelData[2] ++ ;// 绑卡成功
				}

				if(!StringUtils.isStrEmpty(homeTelArea))
				{
					channelData[3] ++ ;// 信息完成
				}

				if(!StringUtils.isStrEmpty(relaName1 ))
				{
					channelData[4] ++ ;// 联系人完成
				}

				if(!StringUtils.isStrEmpty(personImgPath ))
				{
					channelData[5] ++ ;// 人脸识别完成
				}
				if(!StringUtils.isStrEmpty(vedioImgPath ))
				{
					channelData[6] ++ ;// 视频拍摄完成
				}
				if(!StringUtils.isStrEmpty(resultStatus ))
				{
					channelData[7] ++ ;// 进件完成
					if(resultStatus.equals("102"))
						channelData[8] ++;
				}
			}

			String sql3 = "delete from "+table+" where date = '" + queryTime + "' ";
			od.Update(sql3);
			
			for (Entry<String, long[]> entry : data.entrySet()) {
				String channel = entry.getKey();
				long[] dd = entry.getValue();
				String channelId = "0";
				String proxyId = "0";
				String adminId = "0";
				String date = queryTime;
				String channelName = "";
				String attribute = "0";
				String type = "0";
				String subdivision = "0";
				if (channels.get(channel) == null) {

					String sql1 = " select * from operate_channel where channel='" + channel + "' ";

					List<Map<String, String>> channelList = od.Query(sql1);
					for (int i = 0; i < channelList.size(); i++) {
						Map<String, String> channelobj = channelList.get(i);
						channels.put(channelobj.get("channel"), channelobj);
					}
				}

				if (channels.get(channel) != null) {
					channelId = channels.get(channel).get("id");
					proxyId = channels.get(channel).get("proxyId");
					adminId = channels.get(channel).get("adminId");
					channelName = channels.get(channel).get("channelName");
					attribute = channels.get(channel).get("attribute");
					type = channels.get(channel).get("type");
					subdivision = channels.get(channel).get("subdivision");

				}
				
				long register = dd[0];// 注册人数
				long idcard = dd[1];// 身份证验证成功
				String idcardConversion = getBL(idcard,register);//传证转化
				long debitCard = dd[2];// 绑卡
				String debitCardConversion = getBL(debitCard,idcard);//绑卡转化
				long homeJob = dd[3];// 基本信息
				String homeJobConversion = getBL(homeJob,debitCard);//信息转化
				long contacts = dd[4];//联系人
				String contactsConversion = getBL(contacts,homeJob);//联系人转化
				long person = dd[5];//活体照片
				String personConversion = getBL(person,contacts);;//活体转化

				long vedio = dd[6];//视频录制
				String vedioConversion = getBL(vedio,homeJob);// 视频转化
				long upload = dd[7];// 进件数
				String uploadConversion = getBL(upload,vedio);//进件转化
				long account = dd[8];//开户数
				long unaccount = upload - account;// 退回数
				String accountConversion = getBL(account,upload);//开户转化
				String accountAllConversion = getBL(account,register);//开户总转化
				String lostConversion = getBL(register-account,register);//注册流失率

				String month = date.substring(0, date.lastIndexOf("-"));

				String insertSql = "insert into " + table
						+ " (channelId,channel,date,month,register,idcard,idcardConversion,debitCard,debitCardConversion,homeJob,homeJobConversion,contacts"
						+ ",contactsConversion,person,personConversion,vedio,vedioConversion,upload,uploadConversion,account"
						+ ",unaccount,accountConversion,accountAllConversion,lostConversion,adminId,channelName,channelAttribute,channelType,subdivision,proxyId)"
						+ "values(" + channelId + ",'" + channel + "','" + date + "','" + month + "'," + register
						+ "," + idcard + ",'" + idcardConversion + "'," + debitCard + ",'" + debitCardConversion + "'," + homeJob
						+ ",'" + homeJobConversion + "'," + contacts + ",'" + contactsConversion + "'," + person + ",'"
						+ personConversion + "'," + vedio + ",'" + vedioConversion  + "'," + upload + ",'" + uploadConversion + "'," + account + ","
						+ unaccount + ",'" + accountConversion + "','" + accountAllConversion + "','" + lostConversion + "',"
						+ adminId + ",'" + channelName + "'" + "," + attribute + "," + type + "," + subdivision + "," + proxyId + ")";
				try {
					od.Update(insertSql);

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(insertSql);
				}

			}

			ot.setProgress((this.pross * step) + this.pross);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			od.close();
			ld.close();
		}

		return true;
	}
	
	public String getBL(long l1,long l2)
	{
		double idcardConversion = (l1*1.0)/l2;//传证转化
		String bl = (idcardConversion * 100)+"";
		if(bl.length() > bl.indexOf(".") +2)
			bl = bl.substring(0, bl.indexOf(".")+2);
		return bl+"%";
	}

	public double getCost(long outFirstGetPer, long outRegister, long outFirstGetSum, long outAccount, long outUpload,
			String rewardid) {
		List<Map<String, String>> rs = this.reward.get(rewardid);
		if (rs != null && rs.size() > 0) {
			Map<String, String> r = rs.get(0);
			String typeId = r.get("typeId");
			float price = Float.parseFloat(r.get("price"));
			// '26', 'CPA 单价'
			// '27', 'CPS 首提'
			// '28', 'CPS 比例'
			// '29', 'CPS 开户'
			// '33', 'CPA 进件'

			// cpa单价 按注册
			// cps首提 按首提人数
			// cps比例 按首提金额
			// cps开户 按授信人数

			double cost = 0;

			if (typeId.equals("34"))
				return 0;

			long num = 0;
			if (typeId.equals("26")) {
				num = outRegister;
			} else if (typeId.equals("27")) {
				num = outFirstGetPer;
			} else if (typeId.equals("28")) {
				price = price / 100;
				num = outFirstGetSum;
			}

			else if (typeId.equals("29")) {
				num = outAccount;
			} else if (typeId.equals("33")) {
				num = outUpload;
			}

			if (rs.size() == 1) {
				cost = price * num;
			} else {
				int smax = 0;
				for (int i = 0; i < rs.size(); i++) {
					Map<String, String> r1 = rs.get(i);
					float price1 = Float.parseFloat(r1.get("price"));
					int max = Integer.parseInt(r1.get("max"));

					if (max >= num) {
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

	public static long stringToLong(String strTime, String formatType) {
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
