package com.maimob.server.controller.logic;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.daoImpl.DaoWhere;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.DateTimeUtils;
import com.maimob.server.utils.ExportMapExcel;
import com.maimob.server.utils.StringUtils;

public class ConclusionLogic extends Logic{

	private DaoService dao;
//	OperateDao od = new OperateDao();
	public ConclusionLogic(DaoService dao) {
		super(dao);
		this.dao = dao;
	}

	//获取查询数据的条件   app列表
	public String getDataDailyPara(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;
				
		List<Dictionary> appList = Cache.getDicList(1);
		baseResponse.setAppList(appList);                                        //产品列表
		
		return this.toJson();
	}
	
	public String getConclusionData(String json) {
		
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;
		
		JSONObject whereJson = JSONObject.parseObject(json); 
		String date = whereJson.getString("date");
		String appId = whereJson.getString("appId");
		String channelType = whereJson.getString("channelType");
		List<Long> ids = new ArrayList<Long>();
		String minDate = date;   //默认按一天查询
		List<Dictionary> channelTypeList = Cache.getDicList(4);
		List<Map<String,String>> reportformDay = null;
		List<Map<String,String>> reportforms = null;
		
//		OperateDao od = new OperateDao();
		try {
		if("1".equals(channelType)) {
			//市场包括 免费流量 和 付费流量 两个id 
			ids.add(channelTypeList.get(1).getId());
			ids.add(channelTypeList.get(4).getId());

			String sql = "select date,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
			+"	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer)firstGetPer,sum(loaner)loaner," 
			+"  round(sum(channelSum)/sum(loaner),2)perCapital,sum(channelSum)channelSum," 
			+"  sum(income)income,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(grossProfit),2)grossProfit,round(sum(grossProfit)/sum(income)*100,4)grossProfitRate," 
			+"  round(sum(if(cost2=0,cost,cost2))/sum(register),2) registerCpa, round(sum(if(cost2=0,cost,cost2))/sum(account),2) accountCpa,round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI" 
			+"  from operate_reportform where channelType in (" + ids.get(0) + "," + ids.get(1) + ") and appId = " + appId 
			+" and date >= '" + minDate + "' and date <= '" + date + "' group by date";
			reportformDay = od.Query(sql);
//			reportformDay.get(0).put("date", minDate);
			reportformDay.get(0).put("channelName", "total");
			reportformDay.get(0).put("channel", "total");
			List<Map<String,String>> reportform = od.getMarketData(ids,minDate,date,appId);
			reportformDay.addAll(reportform);
			reportforms = od.getChannels(ids,appId,null);
//			baseResponse.setChannelNameList(channelNameList);
			
//			reportforms = getMarketData(ids,whereJson);
		}else if("2".equals(channelType)) {
			//bd数据报表
			Long id = channelTypeList.get(2).getId();
			ids.add(id);
//			reportformDay = getBdData( id, json);
			String where = od.getWhere(id, minDate, date, appId);
			//查询总数
			String sql1 = "select sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4)uploadConversion," 
					+ " sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer,"  
					+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
					+ " round(sum(channelSum)/sum(loaner),2)channelPer,sum(income)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
					+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit,round(sum(grossProfit)/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2," 
					+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 , round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2,"
					+ "round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI,sum(firstIncome)firstIncome,"  
					+ " round(sum(firstIncome)/sum(income),2) ROIFirst from operate_reportform " + where ;
			String sql2 = "select date,mainChannelName channelName,mainChannel channel,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4)uploadConversion," 
							+ " sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer,"  
							+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
							+ " round(sum(channelSum)/sum(loaner),2)channelPer,round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
							+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit,round(sum(grossProfit)/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2," 
							+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 ,round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2,"
							+ " round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI,sum(firstIncome)firstIncome,"  
							+ " round(sum(firstIncome)/sum(if(cost2=0,cost,cost2)),2) ROIFirst from operate_reportform " + where + " group by date,mainChannelName,mainChannel";
			reportformDay = od.Query(sql1);
			reportformDay.get(0).put("date", minDate);
			reportformDay.get(0).put("channelName","total");
			reportformDay.get(0).put("channel","total");
			reportformDay.addAll(od.Query(sql2));
			
			reportforms = od.getChannels(ids,appId,null);
//			baseResponse.setChannelNameList(channelNameList);
//			reportforms = getBdData(id, whereJson);
		}else if("6".equals(channelType)){
			//运营数据报表
//			Long id = channelTypeList.get(6).getId();
			String where = od.getWhere(null, minDate, date, appId);
			String sql = "SELECT date,sum(register) register,sum(upload) upload,sum(account) account,sum(loan) loan, sum(loaner) loaner,"
							+ " sum(channelSum) channelSum,round(sum(income),2) income,round(sum(if(cost2=0,cost,cost2)),2) cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2) grossProfit,"  
							+ " round(sum(upload)/sum(register)*100,4) uploadConversion, round(sum(account)/sum(upload)*100,4) accountConversion," 
							+ " round(sum(loan)/sum(account)*100,4) loanConversion,round(sum(channelSum)/sum(loaner),2) perCapitaCredit,"
							+ "	round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate FROM operate_reportform " + where
							+ "	group by date";
			reportformDay = od.Query(sql);
			reportforms = getOperateData(null, whereJson);
		}else if("7".equals(channelType)){
			String sql = "select date,sum(loaner)loaner,sum(firstGetPer)firstGetPer,sum(secondGetPi)secondGetPi,sum(channelSum)channelSum,sum(firstGetSum)firstGetSum,sum(secondGetSum)secondGetSum,"  
							+ " round(sum(firstGetSum)/sum(firstGetPer),2)firstPer,round(sum(secondGetSum)/sum(secondGetPi),2)secondPer,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit," 
							+ " sum(firstIncome)firstIncome,round(sum(firstIncome)-sum(if(cost2=0,cost,cost2)),2)firstProfit,sum(secondIncome)secondIncome,round(sum(secondIncome)-sum(if(cost2=0,cost,cost2)),2)secondProfit,"  
							+ " round(sum(firstIncome)/sum(income)*100,4) firstPrcent,round(sum(secondIncome)/sum(income)*100,4) secondPrcent,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate" 
							+ " from operate_reportform where date >= '" + minDate + "' and date <= '" + date + "' and appId = " + appId + " group by date";
			reportformDay = od.Query(sql);
			reportforms = getSecondData(whereJson);
			
		}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		//在计算结果  分母为零时 结果为null 修正为 0
		if(reportforms != null) {
			for (Map<String, String> map : reportforms) {
				for (String key : map.keySet()) {
					if(map.get(key) == null) {
						map.put(key, "0");
					}
				}
			}
		}
		for (Map<String, String> map : reportformDay) {
			for (String key : map.keySet()) {
				if(map.get(key) == null) {
					map.put(key, "0");
				}
			}
		}
		
		baseResponse.setFirstPage(reportformDay);
		baseResponse.setReportforms_admin(reportforms);
		baseResponse.setStatus(0);
		
		return this.toJson();
		
	}
	
	//获取市场数据
//	public List<Map<String,String>> getMarketData(List<Long> ids,JSONObject whereJson) {
//		
//		String date = whereJson.getString("date");
//		String appId = whereJson.getString("appId");
//		int dateType = Integer.parseInt(whereJson.getString("dateType"));
//		List<Map<String,String>> reportforms = null;
//	
//		OperateDao od = new OperateDao();
//		String minDate = null;
//		try {
//			//表示按七天查询
//			if(dateType == 1) {
//				minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
//				reportforms = od.getMarketData(ids,minDate,date,appId);
//			} 
//			//表示按5周查询
//			else if(dateType == 2) {
//				minDate = DateTimeUtils.getThisWeekMonday(date);
//				reportforms = od.getMarketData(ids,minDate,date,appId);
//				for(int i = 0;i < 4;i++) {
//					date = DateTimeUtils.getDateBeforeOneWeek(minDate,1);
//					minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
//					List<Map<String,String>> reportform = od.getMarketData(ids,minDate,date,appId);
//					reportforms.addAll(reportform);
//				}
//			} 
//			//表示按4个月查询
//			else if(dateType == 3){
//				String maxMonth = DateTimeUtils.getYearMonth(date,0);
//				String minMonth = DateTimeUtils.getYearMonth(date,4);
//				reportforms = od.getMarketDataByMonth(ids,minMonth,maxMonth,appId);
//			}
//			else if(dateType == 0){
//				minDate= whereJson.getString("minDate");
//				String maxDate= whereJson.getString("maxDate");		
//				reportforms = od.getMarketData(ids,minDate,maxDate,appId);
//			}
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		
//		return reportforms;
//                                     
//	}
	
	//获取运营数据
	public List<Map<String,String>> getOperateData(Long id,JSONObject whereJson) throws SQLException {
		
		String date = whereJson.getString("date");
		String appId = whereJson.getString("appId");
		int dateType = Integer.parseInt(whereJson.getString("dateType"));
		List<Map<String,String>> reportforms = null;
	
		OperateDao od = new OperateDao();
		String minDate = null;
		String where = null;
		String sql = null;
		try {
			//表示按七天查询
			if(dateType == 1) {
				minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
				where = od.getWhere(id, minDate, date, appId);
				sql = "select *,round(upload/register*100,4) uploadConversion, round(account/upload*100,4) accountConversion,"
						+ " round(loan/account*100,4) loanConversion,round(channelSum/loaner*100,4) perCapitaCredit,"
						+ " round(grossProfit/income*100,4) grossProfitRate from "
						+ " (SELECT date,sum(register) register,sum(upload) upload,sum(account) account,sum(loan) loan, " 
						+ "	sum(loaner) loaner,sum(channelSum) channelSum,round(sum(income),2) income,round(sum(if(cost2=0,cost,cost2)),2) cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2) grossProfit"
						+ " FROM operate_reportform " + where + " group by date)a";
				reportforms = od.Query(sql);
			} 
			//表示按5周查询
			else if(dateType == 2) {
				minDate = DateTimeUtils.getThisWeekMonday(date);
				where = od.getWhere(id, minDate, date, appId);
				sql = "SELECT sum(register) register,sum(upload) upload,sum(account) account,sum(loan) loan, "
						+ " sum(loaner) loaner,sum(channelSum) channelSum,round(sum(income),2) income,round(sum(if(cost2=0,cost,cost2)),2) cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2) grossProfit,"
	                    + " round(sum(upload)/sum(register)*100,4) uploadConversion, round(sum(account)/sum(upload)*100,4) accountConversion,"
	                    + " round(sum(loan)/sum(account)*100,4) loanConversion,round(sum(channelSum)/sum(loaner),2) perCapitaCredit,"
	                    + " round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate "
	                    + " FROM operate_reportform " + where ;
				reportforms = od.Query(sql);
				reportforms.get(0).put("date",minDate+"~"+date);//最后加进来
				for(int i = 0;i < 4;i++) {
					date = DateTimeUtils.getDateBeforeOneWeek(minDate,1);
					minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
					where = od.getWhere(id, minDate, date, appId);
					sql = "SELECT sum(register) register,sum(upload) upload,sum(account) account,sum(loan) loan, "
							+ " sum(loaner) loaner,sum(channelSum) channelSum,round(sum(income),2) income,round(sum(if(cost2=0,cost,cost2)),2) cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2) grossProfit,"
		                    + " round(sum(upload)/sum(register)*100,4) uploadConversion, round(sum(account)/sum(upload)*100,4) accountConversion,"
		                    + " round(sum(loan)/sum(account)*100,4) loanConversion,round(sum(channelSum)/sum(loaner),4) perCapitaCredit,"
		                    + " round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate "
		                    + " FROM operate_reportform " + where ;
					List<Map<String,String>> reportform = od.Query(sql);
					reportform.get(0).put("date", minDate+"~"+date);
					reportforms.addAll(reportform);
				}
				Collections.reverse(reportforms);
			} 
			//表示按4个月查询
			else if(dateType == 3){
				String maxMonth = DateTimeUtils.getYearMonth(date,0);
				String minMonth = DateTimeUtils.getYearMonth(date,3);
				where = od.getWhereByMonth(id,minMonth,maxMonth,appId);
				sql = "SELECT month,sum(register) register,sum(upload) upload,sum(account) account,sum(loan) loan, "
						+ " sum(loaner) loaner,sum(channelSum) channelSum,round(sum(income),2) income,round(sum(if(cost2=0,cost,cost2)),2) cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2) grossProfit,"
	                    + " round(sum(upload)/sum(register)*100,4) uploadConversion, round(sum(account)/sum(upload)*100,4) accountConversion,"
	                    + " round(sum(loan)/sum(account)*100,4) loanConversion,round(sum(channelSum)/sum(loaner),2) perCapitaCredit,"
	                    + "round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate "
	                    + " FROM operate_reportform " + where + " group by month";
				reportforms = od.Query(sql);
				for (Map<String, String> map : reportforms) {
					map.put("date", map.get("month"));
				}
			}
			else if(dateType == 0) {
				minDate = whereJson.getString("minDate");
				String maxDate = whereJson.getString("maxDate");
				where = od.getWhere(id, minDate, maxDate, appId);
				sql = "select *,round(upload/register*100,4) uploadConversion, round(account/upload*100,4) accountConversion,"
						+ " round(loan/account*100,4) loanConversion,round(channelSum/loaner*100,4) perCapitaCredit,"
						+ " round(grossProfit/income*100,4) grossProfitRate from "
						+ " (SELECT date,sum(register) register,sum(upload) upload,sum(account) account,sum(loan) loan, " 
						+ "	sum(loaner) loaner,sum(channelSum) channelSum,round(sum(income),2) income,round(sum(if(cost2=0,cost,cost2)),2) cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2) grossProfit"
						+ " FROM operate_reportform " + where + " group by date)a";
				reportforms = od.Query(sql);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return reportforms;
                                     
	}
	
	//获取BD数据二级渠道
		public List<Map<String,String>> getBdDataByChannelName(Long id,JSONObject whereJson,String channelName) throws SQLException {
			
			String date = whereJson.getString("date");
			String appId = whereJson.getString("appId");
			int dateType = Integer.parseInt(whereJson.getString("dateType"));
			List<Map<String,String>> reportforms = null;
		
			OperateDao od = new OperateDao();
			String minDate = null;
			String where = null;
			String sql = null;
			try {
				//表示按七天查询
				if(dateType == 1) {
					minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
					where = od.getWhere(id, minDate, date, appId);
					sql = "select date,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4)uploadConversion," 
								+ "	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer," 
								+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
								+ " round(sum(channelSum)/sum(loaner),2)channelPer,round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
								+ " round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2,"  
								+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 ,round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2,"
								+ " round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI,sum(firstIncome)firstIncome," 
								+ " round(sum(firstIncome)/sum(income)*100,2) ROIfirst "
								+ " from operate_reportform " + where + "  and channelName = '" + channelName + "' group by date";
					reportforms = od.Query(sql);
				} 
				//表示按5周查询
				else if(dateType == 2) {
					minDate = DateTimeUtils.getThisWeekMonday(date);
					where = od.getWhere(id, minDate, date, appId);
					sql = "select sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
							+ "	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer," 
							+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
							+ " round(sum(channelSum)/sum(loaner),2)channelPer,round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
							+ " round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2,"  
							+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 , round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2,"
							+ " round(sum(income)/sum(if(cost2=0,cost,cost2)),4) ROI,sum(firstIncome)firstIncome," 
							+ " round(sum(firstIncome)/sum(income)*100,4) ROIfirst "
							+ " from operate_reportform " + where + "  and channelName = '" + channelName + "'";
					reportforms = od.Query(sql);
					reportforms.get(0).put("date", minDate+"~"+date);
					for(int i = 0;i < 4;i++) {
						date = DateTimeUtils.getDateBeforeOneWeek(minDate,1);
						minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
						where = od.getWhere(id, minDate, date, appId);
						sql = "select sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
								+ "	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer," 
								+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
								+ " round(sum(channelSum)/sum(loaner),4)channelPer,round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
								+ " round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2,"  
								+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 , round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2,"
								+ " round(sum(income)/sum(if(cost2=0,cost,cost2)),4) ROI,sum(firstIncome)firstIncome," 
								+ " round(sum(firstIncome)/sum(income),4) ROIfirst "
								+ " from operate_reportform " + where + "  and channelName = '" + channelName + "'";
						List<Map<String,String>> reportform = od.Query(sql);
					
						reportform.get(0).put("date", minDate+"~"+date);
						
						reportforms.addAll(reportform);
					}
					Collections.reverse(reportforms);
				} 
				//表示按4个月查询
				else if(dateType == 3){
					String maxMonth = DateTimeUtils.getYearMonth(date,0);
					String minMonth = DateTimeUtils.getYearMonth(date,3);
					where = od.getWhereByMonth(id,minMonth,maxMonth,appId);
					sql = "select month date,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4)uploadConversion," 
							+ "	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer," 
							+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
							+ " round(sum(channelSum)/sum(loaner),2)channelPer,round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
							+ " round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2,"  
							+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 , round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2,"
							+ " round(sum(income)/sum(if(cost2=0,cost,cost2)),4) ROI,sum(firstIncome)firstIncome," 
							+ " round(sum(firstIncome)/sum(income),4) ROIfirst "
							+ " from operate_reportform " + where + "  and channelName = '" + channelName + "' group by month";
					reportforms = od.Query(sql);
				}
				else if(dateType == 0) {
					minDate = whereJson.getString("minDate");
					String maxDate = whereJson.getString("maxDate");
					where = od.getWhere(id, minDate, maxDate, appId);
					sql = "select date,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4)uploadConversion," 
							+ "	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer," 
							+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
							+ " round(sum(channelSum)/sum(loaner),2)channelPer,round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
							+ " round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2,"  
							+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 ,round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2,"
							+ " round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI,sum(firstIncome)firstIncome," 
							+ " round(sum(firstIncome)/sum(income)*100,2) ROIfirst "
							+ " from operate_reportform " + where + "  and channelName = '" + channelName + "' group by date";
				reportforms = od.Query(sql);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			return reportforms;
	                                     
	                                     
		}
		
		//获取BD数据主渠道
				public List<Map<String,String>> getBdDataByMainChannelName(Long id,JSONObject whereJson,String channelName) throws SQLException {
					
					String date = whereJson.getString("date");
					String appId = whereJson.getString("appId");
					int dateType = Integer.parseInt(whereJson.getString("dateType"));
					List<Map<String,String>> reportforms = null;
				
					OperateDao od = new OperateDao();
					String minDate = null;
					String where = null;
					String sql = null;
					try {
						//表示按七天查询
						if(dateType == 1) {
							minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
							where = od.getWhere(id, minDate, date, appId);
							sql = "select date,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4)uploadConversion," 
										+ "	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer," 
										+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
										+ " round(sum(channelSum)/sum(loaner),4)channelPer,round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
										+ " round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2,"  
										+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 , round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2,"
										+ " round(sum(income)/sum(if(cost2=0,cost,cost2)),4) ROI,sum(firstIncome)firstIncome," 
										+ " round(sum(firstIncome)/sum(income),2) ROIfirst "
										+ " from operate_reportform " + where + "  and mainChannelName = '" + channelName + "' group by date";
							reportforms = od.Query(sql);
						} 
						//表示按5周查询
						else if(dateType == 2) {
							minDate = DateTimeUtils.getThisWeekMonday(date);
							where = od.getWhere(id, minDate, date, appId);
							sql = "select sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
									+ "	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer," 
									+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
									+ " round(sum(channelSum)/sum(loaner),4)channelPer,round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
									+ " round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2,"  
									+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 , round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2, "
									+ " round(sum(income)/sum(if(cost2=0,cost,cost2)),4) ROI,sum(firstIncome)firstIncome," 
									+ " round(sum(firstIncome)/sum(income),4) ROIfirst "
									+ " from operate_reportform " + where + "  and mainChannelName = '" + channelName + "'";
							reportforms = od.Query(sql);
							reportforms.get(0).put("date", minDate+"~"+date);
							for(int i = 0;i < 4;i++) {
								date = DateTimeUtils.getDateBeforeOneWeek(minDate,1);
								minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
								where = od.getWhere(id, minDate, date, appId);
								sql = "select sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
										+ "	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer," 
										+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
										+ " round(sum(channelSum)/sum(loaner),4)channelPer,round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
										+ " round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2,"  
										+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 , round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2,"
										+ " round(sum(income)/sum(if(cost2=0,cost,cost2)),4) ROI,sum(firstIncome)firstIncome," 
										+ " round(sum(firstIncome)/sum(income),4) ROIfirst "
										+ " from operate_reportform " + where + "  and mainChannelName = '" + channelName + "'";
								List<Map<String,String>> reportform = od.Query(sql);
							
								reportform.get(0).put("date", minDate+"~"+date);
								
								reportforms.addAll(reportform);
							}
							Collections.reverse(reportforms);
						} 
						//表示按4个月查询
						else if(dateType == 3){
							String maxMonth = DateTimeUtils.getYearMonth(date,0);
							String minMonth = DateTimeUtils.getYearMonth(date,3);
							where = od.getWhereByMonth(id,minMonth,maxMonth,appId);
							sql = "select month date,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4)uploadConversion," 
									+ "	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer," 
									+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
									+ " round(sum(channelSum)/sum(loaner),2)channelPer,round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
									+ " round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2,"  
									+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 ,round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2,"
									+ " round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI,sum(firstIncome)firstIncome," 
									+ " round(sum(firstIncome)/sum(income),2) ROIfirst "
									+ " from operate_reportform " + where + "  and mainChannelName = '" + channelName + "' group by month";
							reportforms = od.Query(sql);
						}
						else if(dateType == 0) {
							minDate = whereJson.getString("minDate");
							String maxDate = whereJson.getString("maxDate");
							where = od.getWhere(id, minDate, maxDate, appId);
							sql = "select date,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4)uploadConversion," 
									+ "	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer," 
									+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
									+ " round(sum(channelSum)/sum(loaner),4)channelPer,round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
									+ " round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2,"  
									+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 , round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2,"
									+ " round(sum(income)/sum(if(cost2=0,cost,cost2)),4) ROI,sum(firstIncome)firstIncome," 
									+ " round(sum(firstIncome)/sum(income),2) ROIfirst "
									+ " from operate_reportform " + where + "  and mainChannelName = '" + channelName + "' group by date";
							reportforms = od.Query(sql);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					return reportforms;
			                                     
			                                     
				}
				
				//获取市场数据二级渠道
				public List<Map<String,String>> getMarketDataByChannelName(List<Long> ids,JSONObject whereJson,String channelName) throws SQLException {
					
					String date = whereJson.getString("date");
					String appId = whereJson.getString("appId");
					int dateType = Integer.parseInt(whereJson.getString("dateType"));
					List<Map<String,String>> reportforms = null;
				
					OperateDao od = new OperateDao();
					String minDate = null;
					String where = "";
					where += " where channelType in ( ";
					int i = 0;
					for (Object id : ids) {
						if (i == 0)
							where += id;
						else
							where += "," + id;
						i++;
					}
					where += ")";
					String sql = null;
					try {
						//表示按七天查询
						if(dateType == 1) {
							minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
							sql = "select date,channelName,channel,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
									+"	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer)firstGetPer,sum(loaner)loaner," 
									+"  round(sum(channelSum)/sum(loaner),2)perCapital,sum(channelSum)channelSum," 
									+"  round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate," 
									+"  round(sum(cost)/sum(register),2) registerCpa, round(sum(cost)/sum(account),2) accountCpa,round(sum(income)/sum(cost),2) ROI" 
									+"  from operate_reportform " + where + "  and date >= '" + minDate + "' and date <= '" + date + "' and appId = " + appId 
									+ " and channelName = '" + channelName + "' group by date,channelName,channel";
							reportforms = od.Query(sql);
						} 
						//表示按5周查询
						else if(dateType == 2) {
							minDate = DateTimeUtils.getThisWeekMonday(date);
							sql = "select channelName,channel,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
									+"	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer)firstGetPer,sum(loaner)loaner," 
									+"  round(sum(channelSum)/sum(loaner),2)perCapital,sum(channelSum)channelSum," 
									+"  round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate," 
									+"  round(sum(cost)/sum(register),2) registerCpa, round(sum(cost)/sum(account),2) accountCpa,round(sum(income)/sum(cost),2) ROI" 
									+"  from operate_reportform " + where + "  and date >= '" + minDate + "' and date <= '" + date + "' and appId = " + appId 
									+ " and channelName = '" + channelName + "' group by channelName,channel";
							reportforms = od.Query(sql);
							reportforms.get(0).put("date", minDate+"~"+date);
							for(int j = 0;j < 4;j++) {
								date = DateTimeUtils.getDateBeforeOneWeek(minDate,1);
								minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
								sql = "select channelName,channel,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
										+"	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer)firstGetPer,sum(loaner)loaner," 
										+"  round(sum(channelSum)/sum(loaner),2)perCapital,sum(channelSum)channelSum," 
										+"  round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate," 
										+"  round(sum(cost)/sum(register),2) registerCpa, round(sum(cost)/sum(account),2) accountCpa,round(sum(income)/sum(cost),2) ROI" 
										+"  from operate_reportform " + where + "  and date >= '" + minDate + "' and date <= '" + date + "' and appId = " + appId 
										+ " and channelName = '" + channelName + "' group by channelName,channel";
								List<Map<String,String>> reportform = od.Query(sql);
							
								reportform.get(0).put("date", minDate+"~"+date);
								
								reportforms.addAll(reportform);
							}
							Collections.reverse(reportforms);
						} 
						//表示按4个月查询
						else if(dateType == 3){
							String maxMonth = DateTimeUtils.getYearMonth(date,0);
							String minMonth = DateTimeUtils.getYearMonth(date,4);
							sql = "select month date,channelName,channel,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
									+"	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer)firstGetPer,sum(loaner)loaner," 
									+"  round(sum(channelSum)/sum(loaner),2)perCapital,sum(channelSum)channelSum," 
									+"  round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate," 
									+"  round(sum(cost)/sum(register),2) registerCpa, round(sum(cost)/sum(account),2) accountCpa,round(sum(income)/sum(cost),2) ROI" 
									+"  from operate_reportform " + where + "  and month >= '" + minMonth + "' and month <= '" + maxMonth + "' and appId = " + appId 
									+ " and channelName = '" + channelName + "' group by month,channelName,channel";
							reportforms = od.Query(sql);
							for (Map<String, String> map : reportforms) {
								map.put("date", map.get("month"));
							}
						}
						else if(dateType == 0) {
							minDate = whereJson.getString("minDate");
							String maxDate = whereJson.getString("maxDate");
//							where = od.getWhere(id, minDate, maxDate, appId);
							sql = "select date,channelName,channel,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
									+"	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer)firstGetPer,sum(loaner)loaner," 
									+"  round(sum(channelSum)/sum(loaner),2)perCapital,sum(channelSum)channelSum," 
									+"  round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate," 
									+"  round(sum(if(cost2=0,cost,cost2))/sum(register),2) registerCpa, round(sum(if(cost2=0,cost,cost2))/sum(account),2) accountCpa,round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI" 
									+"  from operate_reportform " + where + "  and date >= '" + minDate + "' and date <= '" + maxDate + "' and appId = " + appId 
									+ " and channelName = '" + channelName + "' group by date,channelName,channel";
							reportforms = od.Query(sql);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					return reportforms;
			                                     
			                                     
				}
				
			//获取市场数据主渠道
			public List<Map<String,String>> getMarketDataByMainChannelName(List<Long> ids,JSONObject whereJson,String channelName) throws SQLException {
				String date = whereJson.getString("date");
				String appId = whereJson.getString("appId");
				int dateType = Integer.parseInt(whereJson.getString("dateType"));
				List<Map<String,String>> reportforms = null;
	
				OperateDao od = new OperateDao();
				String minDate = null;
				String where = "";
				
				where += " where channelType in ( ";
				int i = 0;
				for (Object id : ids) {
					if (i == 0)
						where += id;
					else
						where += "," + id;
					i++;
				}
				where += ")";
				String sql = null;
				try {
					//表示按七天查询
					if(dateType == 1) {
						minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
						sql = "select date,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
								+"	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer)firstGetPer,sum(loaner)loaner," 
								+"  round(sum(channelSum)/sum(loaner),2)perCapital,sum(channelSum)channelSum," 
								+"  round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate," 
								+"  round(sum(if(cost2=0,cost,cost2))/sum(register),2) registerCpa, round(sum(if(cost2=0,cost,cost2))/sum(account),2) accountCpa,round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI" 
								+"  from operate_reportform " + where + "  and date >= '" + minDate + "' and date <= '" + date + "' and appId = " + appId 
								+ " and mainChannelName = '" + channelName + "' group by date";
						reportforms = od.Query(sql);
					} 
					//表示按5周查询
					else if(dateType == 2) {
						minDate = DateTimeUtils.getThisWeekMonday(date);
						sql = "select sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
								+"	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer)firstGetPer,sum(loaner)loaner," 
								+"  round(sum(channelSum)/sum(loaner),2)perCapital,sum(channelSum)channelSum," 
								+"  round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate," 
								+"  round(sum(if(cost2=0,cost,cost2))/sum(register),2) registerCpa, round(sum(if(cost2=0,cost,cost2))/sum(account),2) accountCpa,round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI" 
								+"  from operate_reportform " + where + "  and date >= '" + minDate + "' and date <= '" + date + "' and appId = " + appId 
								+ " and mainChannelName = '" + channelName + "' ";
						reportforms = od.Query(sql);
						reportforms.get(0).put("date", minDate+"~"+date);
						for(int j = 0;j < 4;j++) {
							date = DateTimeUtils.getDateBeforeOneWeek(minDate,1);
							minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
							sql = "select sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
									+"	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer)firstGetPer,sum(loaner)loaner," 
									+"  round(sum(channelSum)/sum(loaner),2)perCapital,sum(channelSum)channelSum," 
									+"  round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate," 
									+"  round(sum(if(cost2=0,cost,cost2))/sum(register),2) registerCpa, round(sum(if(cost2=0,cost,cost2))/sum(account),2) accountCpa,round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI" 
									+"  from operate_reportform " + where + "  and date >= '" + minDate + "' and date <= '" + date + "' and appId = " + appId 
									+ " and mainChannelName = '" + channelName + "' ";
							List<Map<String,String>> reportform = od.Query(sql);
	
							reportform.get(0).put("date", minDate+"~"+date);
	
							reportforms.addAll(reportform);
						}
						Collections.reverse(reportforms);
					} 
					//表示按4个月查询
					else if(dateType == 3){
						String maxMonth = DateTimeUtils.getYearMonth(date,0);
						String minMonth = DateTimeUtils.getYearMonth(date,4);
						sql = "select month date,mainChannelName,mainChannel,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
								+"	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer)firstGetPer,sum(loaner)loaner," 
								+"  round(sum(channelSum)/sum(loaner),2)perCapital,sum(channelSum)channelSum," 
								+"  round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate," 
								+"  round(sum(if(cost2=0,cost,cost2))/sum(register),2) registerCpa, round(sum(if(cost2=0,cost,cost2))/sum(account),2) accountCpa,round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI" 
								+"  from operate_reportform " + where + "  and month >= '" + minMonth + "' and month <= '" + maxMonth + "' and appId = " + appId 
								+ " and mainChannelName = '" + channelName + "' group by month,mainChannelName,mainChannel";
								
						reportforms = od.Query(sql);
					}
					else if(dateType == 0) {
						minDate = whereJson.getString("minDate");
						String maxDate = whereJson.getString("maxDate");
						sql = "select date,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
								+"	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer)firstGetPer,sum(loaner)loaner," 
								+"  round(sum(channelSum)/sum(loaner),2)perCapital,sum(channelSum)channelSum," 
								+"  round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate," 
								+"  round(sum(if(cost2=0,cost,cost2))/sum(register),2) registerCpa, round(sum(if(cost2=0,cost,cost2))/sum(account),2) accountCpa,round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI" 
								+"  from operate_reportform " + where + "  and date >= '" + minDate + "' and date <= '" + maxDate + "' and appId = " + appId 
								+ " and mainChannelName = '" + channelName + "' group by date";
						reportforms = od.Query(sql);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
	
				return reportforms;
	
	
			}
		
		//获取续贷数据
		public List<Map<String,String>> getSecondData(JSONObject whereJson) throws SQLException {
			
			String date = whereJson.getString("date");
			String appId = whereJson.getString("appId");
			int dateType = Integer.parseInt(whereJson.getString("dateType"));
			List<Map<String,String>> reportforms = null;
		
			OperateDao od = new OperateDao();
			String minDate = null;
			String where = null;
			String sql = null;
			
			try {
				//表示按七天查询
				if(dateType == 1) {
					minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
					where = od.getWhere(null, minDate, date, appId);
					sql = "select date,sum(loaner)loaner,sum(firstGetPer)firstGetPer,sum(secondGetPi)secondGetPi,sum(channelSum)channelSum,sum(firstGetSum)firstGetSum,sum(secondGetSum)secondGetSum,"  
							+ " round(sum(firstGetSum)/sum(firstGetPer),4)firstPer,round(sum(secondGetSum)/sum(secondGetPi),4)secondPer,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit," 
							+ " round(sum(firstIncome),2)firstIncome,round(sum(firstIncome)-sum(if(cost2=0,cost,cost2)),2)firstProfit,round(sum(secondIncome),2)secondIncome,round(sum(secondIncome)-sum(if(cost2=0,cost,cost2)),2)secondProfit,"  
							+ " round(sum(firstIncome)/sum(income),4) firstPrcent,round(sum(secondIncome)/sum(income),4) secondPrcent,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income),4)grossProfitRate" 
							+ " from operate_reportform " + where + " group by date";
					reportforms = od.Query(sql);
				} 
				//表示按5周查询
				else if(dateType == 2) {
					minDate = DateTimeUtils.getThisWeekMonday(date);
					where = od.getWhere(null, minDate, date, appId);
					sql = "select sum(loaner)loaner,sum(firstGetPer)firstGetPer,sum(secondGetPi)secondGetPi,sum(channelSum)channelSum,sum(firstGetSum)firstGetSum,sum(secondGetSum)secondGetSum,"  
							+ " round(sum(firstGetSum)/sum(firstGetPer),4)firstPer,round(sum(secondGetSum)/sum(secondGetPi),4)secondPer,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit," 
							+ " round(sum(firstIncome),2)firstIncome,round(sum(firstIncome)-sum(if(cost2=0,cost,cost2)),2)firstProfit,round(sum(secondIncome),2)secondIncome,round(sum(secondIncome)-sum(if(cost2=0,cost,cost2)),2)secondProfit,"  
							+ " round(sum(firstIncome)/sum(income),4) firstPrcent,round(sum(secondIncome)/sum(income),4) secondPrcent,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income),4)grossProfitRate" 
							+ " from operate_reportform " + where ;
					reportforms = od.Query(sql);
					reportforms.get(0).put("date", minDate+"~"+date);
					for(int i = 0;i < 4;i++) {
						date = DateTimeUtils.getDateBeforeOneWeek(minDate,1);
						minDate = DateTimeUtils.getDateBeforeOneWeek(date,6);
						where = od.getWhere(null, minDate, date, appId);
						sql = "select sum(loaner)loaner,sum(firstGetPer)firstGetPer,sum(secondGetPi)secondGetPi,sum(channelSum)channelSum,sum(firstGetSum)firstGetSum,sum(secondGetSum)secondGetSum,"  
								+ " round(sum(firstGetSum)/sum(firstGetPer),4)firstPer,round(sum(secondGetSum)/sum(secondGetPi),4)secondPer,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit," 
								+ " round(sum(firstIncome),2)firstIncome,round(sum(firstIncome)-sum(if(cost2=0,cost,cost2)),2)firstProfit,round(sum(secondIncome),2)secondIncome,round(sum(secondIncome)-sum(if(cost2=0,cost,cost2)),2)secondProfit,"  
								+ " round(sum(firstIncome)/sum(income),4) firstPrcent,round(sum(secondIncome)/sum(income),4) secondPrcent,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income),4)grossProfitRate" 
								+ " from operate_reportform " + where ;
						List<Map<String,String>> reportform = od.Query(sql);
					
						reportform.get(0).put("date", minDate+"~"+date);
						
						reportforms.addAll(reportform);
					}
					Collections.reverse(reportforms);
				} 
				//表示按4个月查询
				else if(dateType == 3){
					String maxMonth = DateTimeUtils.getYearMonth(date,0);
					String minMonth = DateTimeUtils.getYearMonth(date,3);
					where = od.getWhereByMonth(null,minMonth,maxMonth,appId);
					sql = "select month,sum(loaner)loaner,sum(firstGetPer)firstGetPer,sum(secondGetPi)secondGetPi,sum(channelSum)channelSum,sum(firstGetSum)firstGetSum,sum(secondGetSum)secondGetSum,"  
							+ " round(sum(firstGetSum)/sum(firstGetPer),4)firstPer,round(sum(secondGetSum)/sum(secondGetPi),4)secondPer,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit," 
							+ " round(sum(firstIncome),2)firstIncome,round(sum(firstIncome)-sum(if(cost2=0,cost,cost2)),2)firstProfit,round(sum(secondIncome),2)secondIncome,round(sum(secondIncome)-sum(if(cost2=0,cost,cost2)),2)secondProfit,"  
							+ " round(sum(firstIncome)/sum(income),4) firstPrcent,round(sum(secondIncome)/sum(income),4) secondPrcent,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income),4)grossProfitRate" 
							+ " from operate_reportform " + where + " group by month";
					reportforms = od.Query(sql);
					for (Map<String, String> map : reportforms) {
						map.put("date", map.get("month"));
					}
				}
				else if(dateType == 0) {
					minDate = whereJson.getString("minDate");
					String maxDate = whereJson.getString("maxDate");
					where = od.getWhere(null, minDate, maxDate, appId);
					sql = "select date,sum(loaner)loaner,sum(firstGetPer)firstGetPer,sum(secondGetPi)secondGetPi,sum(channelSum)channelSum,sum(firstGetSum)firstGetSum,sum(secondGetSum)secondGetSum,"  
							+ " round(sum(firstGetSum)/sum(firstGetPer),4)firstPer,round(sum(secondGetSum)/sum(secondGetPi),4)secondPer,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit," 
							+ " round(sum(firstIncome),2)firstIncome,round(sum(firstIncome)-sum(if(cost2=0,cost,cost2)),2)firstProfit,round(sum(secondIncome),2)secondIncome,round(sum(secondIncome)-sum(if(cost2=0,cost,cost2)),2)secondProfit,"  
							+ " round(sum(firstIncome)/sum(income),4) firstPrcent,round(sum(secondIncome)/sum(income),4) secondPrcent,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income),4)grossProfitRate" 
							+ " from operate_reportform " + where + " group by date";
					reportforms = od.Query(sql);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			return reportforms;
		}
		
		//bd曲线数据
		public String getBdCurveData(String json){
			
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return check;
			JSONObject whereJson = JSONObject.parseObject(json); 
			
			Long id = Cache.getDicList(4).get(2).getId();
			List<Map<String,String>> reportforms = new ArrayList<Map<String,String>>();
//			boolean flag = true;//默认按照主渠道查询
			JSONArray channelNames = whereJson.getJSONArray("channelName");
			for (Object object : channelNames) {
				List<Map<String,String>> reportform = null;
				try {
					//一个个渠道查询
					if(object.toString().indexOf("--") != -1) {
	//					flag= false;//按照二级渠道查询
						String channel = object.toString().replaceAll("--", "");
							reportform = getBdDataByChannelName(id,whereJson,channel);
					}
					else {
						//按主渠道插查询
						reportform = getBdDataByMainChannelName(id,whereJson,object.toString());
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(reportform != null) {
					for (Map<String, String> map : reportform) {
						for(String key:map.keySet()) {
							if(map.get(key) == null) {
								map.put(key, "0");
							}
						}
					}
				}
				Map<String,String> map = new HashMap<String,String>();
				map.put("name", object.toString());
				map.put("value", JSONObject.toJSONString(reportform));
				reportforms.add(map);
			}
			
			baseResponse.setReportforms_admin(reportforms);
		return this.toJson();

		}
		
		public String getMarketCurveData(String json){
			
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return check;
			JSONObject whereJson = JSONObject.parseObject(json); 
			
			List<Long> ids = new ArrayList<Long>();
			ids.add(Cache.getDicList(4).get(1).getId());
			ids.add(Cache.getDicList(4).get(4).getId());
			List<Map<String,String>> reportforms = new ArrayList<Map<String,String>>();
//			boolean flag = true;//默认按照主渠道查询
			JSONArray channelNames = whereJson.getJSONArray("channelName");
			for (Object object : channelNames) {
				List<Map<String,String>> reportform = null;
				try {
					//一个个渠道查询
					if(object.toString().indexOf("--") != -1) {
	//					flag= false;//按照二级渠道查询
						String channel = object.toString().replaceAll("--", "");
							reportform = getMarketDataByChannelName(ids,whereJson,channel);
					}
					else {
						//按主渠道插查询
						reportform = getMarketDataByMainChannelName(ids,whereJson,object.toString());
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (Map<String, String> map : reportform) {
					for(String key:map.keySet()) {
						if(map.get(key) == null) {
							map.put(key, "0");
						}
					}
				}
				Map<String,String> map = new HashMap<String,String>();
				map.put("name", object.toString());
				map.put("value", JSONObject.toJSONString(reportform));
				reportforms.add(map);
			}
			
			baseResponse.setReportforms_admin(reportforms);
		return this.toJson();

		}
		
//	public static void main(String[] args) {
//		try {
//			System.out.println(getThisMonth("2018-04-09",1));
//			System.out.println(getThisMonth("2018-04-09",0));
//			System.out.println(getThisMonth("2018-04-09",4));
//			System.out.println(getThisMonth("2018-04-09",5));
//			System.out.println(getThisMonth("2018-04-09",15));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
		
		//添加评论
		public String addComment(String json) {
			
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return check;
			JSONObject whereJson = JSONObject.parseObject(json);
			OperateDao od = new OperateDao();
			int n = od.addCommentList(whereJson);
			if(n > 0) {
				baseResponse.setStatusMsg("评论提交成功!");
			}else {
				baseResponse.setStatusMsg("评论提交失败!");
			}
			return this.toJson();
		}
		//获取评论
		public String getCommentList(String json){
			
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return check;
			JSONObject whereJson = JSONObject.parseObject(json);
			OperateDao od = new OperateDao();
			
			List<Map<String,String>> commentList = od.getCommentList(whereJson);
			baseResponse.setReportforms_admin(commentList);
			
			return this.toJson();
			
		}
		public String dailyHistory(String json) {
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return check;
			List<Dictionary> appList = Cache.getDicList(1); 
			baseResponse.setAppList(appList);
			return this.toJson();
		}
			
		//获取日报历史的运营和续贷的数据
		public List<Map<String,String>> getOperateAndSecondDataList(String json) {
//			String check = this.CheckJson(json);
//			if(!StringUtils.isStrEmpty(check))
//				return check;
			JSONObject whereJson = JSONObject.parseObject(json);
			
			String appId = whereJson.getString("appId");
			String minDate = whereJson.getString("minDate");
			String maxDate = whereJson.getString("maxDate");
			String channelType = whereJson.getString("channelType");
			List<Map<String,String>> reportforms = null;
			List<Map<String,String>> reportformDay = null;
			String sql = "";
			String hql = "";
//			List<Dictionary> channelTypeList = Cache.getDicList(4);
			if("6".equals(channelType)){
				//运营数据报表
//				Long id = channelTypeList.get(6).getId();
				String where = od.getWhere(null, minDate, maxDate, appId);
				sql = "SELECT sum(register) register,sum(upload) upload,sum(account) account,sum(loan) loan, sum(loaner) loaner,"
								+ " sum(channelSum) channelSum,round(sum(income),2) income,round(sum(if(cost2=0,cost,cost2)),2) cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2) grossProfit,"  
								+ " round(sum(upload)/sum(register)*100,4) uploadConversion, round(sum(account)/sum(upload)*100,4) accountConversion," 
								+ " round(sum(loan)/sum(account)*100,4) loanConversion,round(sum(channelSum)/sum(loaner),2) perCapitaCredit,"
								+ "	round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate FROM operate_reportform " + where;
				hql = "SELECT date,sum(register) register,sum(upload) upload,sum(account) account,sum(loan) loan, sum(loaner) loaner,"
						+ " sum(channelSum) channelSum,round(sum(income),2) income,round(sum(if(cost2=0,cost,cost2)),2) cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2) grossProfit,"  
						+ " round(sum(upload)/sum(register)*100,4) uploadConversion, round(sum(account)/sum(upload)*100,4) accountConversion," 
						+ " round(sum(loan)/sum(account)*100,4) loanConversion,round(sum(channelSum)/sum(loaner),2) perCapitaCredit,"
						+ "	round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate FROM operate_reportform " + where + " group by date";
			}else {
				//续贷数据
				sql = "select sum(loaner)loaner,sum(firstGetPer)firstGetPer,sum(secondGetPi)secondGetPi,sum(channelSum)channelSum,sum(firstGetSum)firstGetSum,sum(secondGetSum)secondGetSum,"  
						+ " round(sum(firstGetSum)/sum(firstGetPer),2)firstPer,round(sum(secondGetSum)/sum(secondGetPi),2)secondPer,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit," 
						+ " round(sum(firstIncome),2)firstIncome,round(sum(firstIncome)-sum(if(cost2=0,cost,cost2)),2)firstProfit,round(sum(secondIncome),2)secondIncome,round(sum(secondIncome)-sum(if(cost2=0,cost,cost2)),2)secondProfit,"  
						+ " round(sum(firstIncome)/sum(income)*100,4) firstPrcent,round(sum(secondIncome)/sum(income)*100,4) secondPrcent,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate" 
						+ " from operate_reportform where date >= '" + minDate + "' and date <= '" + maxDate + "' and appId = " + appId;
				hql  = "select date,sum(loaner)loaner,sum(firstGetPer)firstGetPer,sum(secondGetPi)secondGetPi,sum(channelSum)channelSum,sum(firstGetSum)firstGetSum,sum(secondGetSum)secondGetSum,"  
						+ " round(sum(firstGetSum)/sum(firstGetPer),2)firstPer,round(sum(secondGetSum)/sum(secondGetPi),2)secondPer,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,"  
						+ " round(sum(firstIncome),2)firstIncome,round(sum(firstIncome)-sum(if(cost2=0,cost,cost2)),2)firstProfit,round(sum(secondIncome),2)secondIncome,round(sum(secondIncome)-sum(if(cost2=0,cost,cost2)),2)secondProfit,"  
						+ " round(sum(firstIncome)/sum(income)*100,4) firstPrcent,round(sum(secondIncome)/sum(income)*100,4) secondPrcent,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate" 
						+ " from operate_reportform where date >= '" + minDate + "' and date <= '" + maxDate + "' and appId = " + appId + " group by date";
			}
			OperateDao od = new OperateDao();
			
			try {
				reportforms = od.Query(sql);
				reportformDay = od.Query(hql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(reportforms != null) {
				reportforms.get(0).put("date", "total");
				
				reportforms.addAll(reportformDay);
				
				for (Map<String, String> map : reportforms) {
					for (String key : map.keySet()) {
						if(map.get(key) == null) {
							map.put(key, "0");
						}
					}
				}
			}
//			baseResponse.setReportforms_admin(reportforms);
			return reportforms;
			
		}
		
		public String getOperateAndSecondData(String json) {
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return check;
			List<Map<String,String>> reportforms = getOperateAndSecondDataList(json);
			
			baseResponse.setReportforms_admin(reportforms);
			return this.toJson();
			
		}
		
//		public String exportBdAndMarketData(String json) {
//			List<Map<String,String>> reportform = getOperateAndSecondDataList(json);
//			
//		}
		
		//获取日报历史的bd和市场的数据
		public String getBdAndMarketData(String json) {
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return check;
			JSONObject whereJson = JSONObject.parseObject(json);
			OperateDao od = new OperateDao();

			List<Map<String,String>> reportforms = od.getBdAndMarketData(whereJson);

			baseResponse.setReportforms_admin(reportforms);
			return this.toJson();
		}

		//获取日报历史的bd和市场的数据详情
		public String getBdAndMarketDeatailData(String json) {
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return check;
			JSONObject whereJson = JSONObject.parseObject(json);
			OperateDao od = new OperateDao();

			List<Map<String,String>> reportforms = od.getBdAndMarketDeatailData(whereJson);

			baseResponse.setReportforms_admin(reportforms);
			return this.toJson();
		}
		
		//获取业绩查询需要的各种参数
		public String getPerformancePara(String json) {
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return check;
			JSONObject whereJson = JSONObject.parseObject(json);
			OperateDao od = new OperateDao();
			//查询渠道商
			List<Proxy> proxyList = dao.findProxyName();   //渠道商
			String appId = whereJson.getString("appId");    
			List<Map<String,String>> reportforms = od.getChannels(null,appId,null);     //渠道名称和渠道号
			List<Map<String,String>> adminList = od.getChannelAdmin();                   //负责人
			List<Dictionary> appList = Cache.getDicList(1);                             //产品
			
			baseResponse.setAppList(appList);
			baseResponse.setReportforms_admin(adminList);
			baseResponse.setReportforms_operate(reportforms);
			baseResponse.setProxyList(proxyList);
			return this.toJson();
		}
		
		//通过渠道商Id查找相关渠道数据
		public String getChannels(String json) {
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return check;
			JSONObject whereJson = JSONObject.parseObject(json);
			OperateDao od = new OperateDao();
			
			String proxyId = whereJson.getString("proxyId");
			String appId = whereJson.getString("appId");
			List<Map<String,String>> reportforms = od.getChannels(null,appId,proxyId);
			
			baseResponse.setReportforms_admin(reportforms);
			return this.toJson();
		}
		
		//业绩查询
		public String getPerformance(String json) {
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return check;
			JSONObject whereJson = JSONObject.parseObject(json);
			OperateDao od = new OperateDao();
			
			List<Map<String,String>> reportforms = od.getPerformance(whereJson);
			baseResponse.setReportforms_admin(reportforms);
			return this.toJson();
			
		}
		
		//业绩查询报表下载
		public void exportPerformance(String json,HttpServletResponse response) {
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return ;
			JSONObject whereJson = JSONObject.parseObject(json);
			OperateDao od = new OperateDao();
			
			List<Map<String,String>> reportforms = od.getPerformance(whereJson);
			
			JSONArray arr = whereJson.getJSONArray("tag");
			

	        boolean proxyflag = false;
	        boolean channelNameflag = false;
	        boolean channelflag = false;
	        boolean adminflag = false;
	        boolean outflag = false;
	        boolean appflag = false;
	        
	        if("[]".equals(arr.toString())) {
			}
			else {
				proxyflag = DaoWhere.ischose("渠道商", whereJson);
				channelNameflag = DaoWhere.ischose("渠道", whereJson);
				channelflag = DaoWhere.ischose("渠道号", whereJson);
				adminflag = DaoWhere.ischose("负责人", whereJson);
				appflag = DaoWhere.ischose("APP", whereJson);
				outflag = DaoWhere.ischose("外部", whereJson);

			}
	        
	        
	        List<String> listName = new ArrayList<>();
	        listName.add("日期");
	        listName.add("渠道商");
	        listName.add("产品");
	        listName.add("渠道");
	        listName.add("渠道号");
	        listName.add("负责人");
	        listName.add("外部注册");
	        listName.add("外部开户");
	        listName.add("外部进件");
	        listName.add("外部首提人数");
	        listName.add("外部首提金额");
	        listName.add("外部提现总额");
	        listName.add("提现总额");
	        listName.add("收入");
	        listName.add("成本");
	        listName.add("毛利");
	        listName.add("毛利率");
	        
	        
	        List<String> listId = new ArrayList<>();
	        listId.add("date");           //时间
	        listId.add("company");
	        listId.add("app");
	        listId.add("channelName");    //渠道
	        listId.add("channel");        //渠道号
	        listId.add("admin");      //负责人
	        listId.add("outRegister");     //外部注册
	        listId.add("outAccount");        //外部开户
	        listId.add("outUpload");         //外部进件
	        listId.add("outFirstGetPer");      //外部首提人数
	        listId.add("outFirstGetSum");         //外部首提总额
	        listId.add("outChannelSum");      //外部提现总额
	        listId.add("channelSum");      //提现总额
	        listId.add("income");      //收入
	        listId.add("cost");            //成本
	        listId.add("grossProfit");            //毛利
	        listId.add("grossProfitRate");            //毛利率
	        
	        if(!proxyflag) {
	        	listName.remove("渠道商");
	        	listId.remove("company");   
	        	
	        }
	        
	        if(!channelNameflag) {
	        	listName.remove("渠道");
	        	listId.remove("channelName");  
	        }
	        if(!channelflag) {
	        	listName.remove("渠道号");
	        	listId.remove("channel");  
	        }
	        if(!adminflag) {
	        	listName.remove("负责人");
	        	listId.remove("admin");  
	        }
	        if(!appflag) {
	        	listName.remove("产品");
	        	listId.remove("app");  
	        }
	        if(!outflag) {
	        	  listName.remove("外部注册");
	  	        listName.remove("外部开户");
	  	        listName.remove("外部进件");
	  	        listName.remove("外部首提人数");
	  	        listName.remove("外部首提金额");
	  	        listName.remove("外部提现总额");
	  	      listId.remove("outRegister");     //外部注册
		        listId.remove("outAccount");        //外部开户
		        listId.remove("outUpload");         //外部进件
		        listId.remove("outFirstGetPer");      //外部首提人数
		        listId.remove("outFirstGetSum");         //外部首提总额
		        listId.remove("outChannelSum");      //外部提现总额
	        }
	        ExportMapExcel exportExcelUtil = new ExportMapExcel();
	        exportExcelUtil.exportExcelString("业绩查询报表",listName,listId,reportforms,response);
	        
	        
		}

		//运营数据日报下载
		public void exportOperateData(String json,HttpServletResponse response) {
			
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return ;
			JSONObject whereJson = JSONObject.parseObject(json);
			OperateDao od = new OperateDao();
			String date = whereJson.getString("date");
//			String maxDate = whereJson.getString("maxDate");
			String appId =  whereJson.getString("appId");
			
//			List<Dictionary> channelTypeList = Cache.getDicList(4);
//			Long id = channelTypeList.get(6).getId();
			
			String where = od.getWhere(null, date, date, appId);
			String sql = "select *,round(upload/register*100,4) uploadConversion, round(account/upload*100,4) accountConversion,"
					+ " round(loan/account*100,4) loanConversion,round(channelSum/loaner*100,4) perCapitaCredit,"
					+ " round(grossProfit/income*100,4) grossProfitRate from "
					+ " (SELECT date,sum(register) register,sum(upload) upload,sum(account) account,sum(loan) loan, " 
					+ "	sum(loaner) loaner,sum(channelSum) channelSum,round(sum(income),2) income,round(sum(if(cost2=0,cost,cost2)),2) cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2) grossProfit"
					+ " FROM operate_reportform " + where + " group by date)a";
			List<Map<String,String>> reportforms = null;
			
			try {
				reportforms = od.Query(sql);
				for (Map<String, String> map : reportforms) {
					for (String key : map.keySet()) {
						if(map.get(key) == null) {
							map.put(key, "0");
						}
					}
				}
				for (Map<String, String> map : reportforms) {
					map.put("uploadConversion", map.get("uploadConversion")+"%");
					map.put("accountConversion", map.get("accountConversion")+"%");
					map.put("loanConversion", map.get("loanConversion")+"%");
					map.put("grossProfitRate", map.get("grossProfitRate")+"%");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			List<String> listName = new ArrayList<>();
	        listName.add("日期");
	        listName.add("注册");
	        listName.add("进件");
	        listName.add("进件转化率");
	        listName.add("开户");
	        listName.add("开户转化率");
	        listName.add("提现");
	        
	        listName.add("放款总笔数");
	        listName.add("提现总金额");
	        listName.add("放款笔均金额");
	        
	        listName.add("收入");
	        listName.add("成本");
	        listName.add("毛利");
	        listName.add("毛利率");
	        
	        
	        List<String> listId = new ArrayList<>();
	        listId.add("date");           //时间
	        listId.add("register");
	        listId.add("upload");
	        listId.add("uploadConversion");    //渠道
	        listId.add("account");        //渠道号
	        listId.add("accountConversion");      //负责人
	        listId.add("loan");     //外部注册
//	        listId.add("loanConversion");        //外部开户
	        listId.add("loaner");         //外部进件
	        listId.add("channelSum");      //外部首提人数
	        listId.add("perCapitaCredit");         //外部首提总额
	        listId.add("income");      //收入
	        listId.add("cost");            //成本
	        listId.add("grossProfit");            //毛利
	        listId.add("grossProfitRate");            //毛利率
	        
	        ExportMapExcel exportExcelUtil = new ExportMapExcel();
	        exportExcelUtil.exportExcelString("运营数据日报",listName,listId,reportforms,response);
	        
	        
	        
		}
		//获取bd报表
		public List<Map<String,String>> getBdData(Long id,String json){
			
			JSONObject whereJson = JSONObject.parseObject(json);
			
			String date = whereJson.getString("date");
//			String maxDate = whereJson.getString("maxDate");
			String appId =  whereJson.getString("appId");
			String where = od.getWhere(id, date, date, appId);
			//查询总数
			String sql1 = "select sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4)uploadConversion," 
					+ " sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer,"  
					+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
					+ " round(sum(channelSum)/sum(loaner),2)channelPer,round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
					+ " round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2," 
					+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 , round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2,"
					+ "round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI,sum(firstIncome)firstIncome,"  
					+ " round(sum(firstIncome)/sum(income),2) ROIfirst from operate_reportform " + where ;
			String sql2 = "select date,mainChannelName channelName,mainChannel channel,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4)uploadConversion," 
							+ " sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer) firstGetPer,"  
							+ " sum(firstGetSum)firstGetSum,sum(outFirstGetSum)outFirstGetSum,sum(loaner)loaner,sum(channelSum)channelSum," 
							+ " round(sum(channelSum)/sum(loaner),2)channelPer,round(sum(income),2)income,round(sum(if(cost2=0,cost,cost2)),2)cost,"  
							+ " round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate,round(sum(if(cost2=0,cost,cost2)),2)cost2," 
							+ " round(sum(income)-sum(if(cost2=0,cost,cost2)),2) grossProfit2 ,round((sum(income)-sum(if(cost2=0,cost,cost2)))/sum(income)*100,4) grossProfitRate2,"
							+ " round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI,sum(firstIncome)firstIncome,"  
							+ " round(sum(firstIncome)/sum(if(cost2=0,cost,cost2)),2) ROIfirst from operate_reportform " + where + " group by date,mainChannelName,mainChannel";
			
			List<Map<String,String>> reportform = null;
			
			try {
				reportform = od.Query(sql1);
				reportform.get(0).put("date", date);
				reportform.get(0).put("channelName","total");
				reportform.get(0).put("channel","total");
				reportform.addAll(od.Query(sql2));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return reportform;
			
			
		}
		
		//bd日报下载
		public void exportBdData(String json,HttpServletResponse response) {
			
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return ;
			
			
			List<Dictionary> channelTypeList = Cache.getDicList(4);
			Long id = channelTypeList.get(2).getId();
			
			List<Map<String,String>> reportforms = getBdData(id,json);
			for (Map<String, String> map : reportforms) {
				for (String key : map.keySet()) {
					if(map.get(key) == null) {
						map.put(key, "0");
					}
				}
			}
			for (Map<String, String> map : reportforms) {
				map.put("uploadConversion", map.get("uploadConversion")+"%");
				map.put("accountConversion", map.get("accountConversion")+"%");
				map.put("grossProfitRate", map.get("grossProfitRate")+"%");
				map.put("grossProfitRate2", map.get("grossProfitRate2")+"%");
			}
//			reportforms = od.getChannels(ids,appId,null);
			
			List<String> listName = new ArrayList<>();
	        listName.add("日期");
	        listName.add("推广渠道");
	        listName.add("渠道号(一级)");
	        listName.add("注册");
	        listName.add("进件");
	        listName.add("进件转化率");
	        listName.add("开户");
	        listName.add("开户转化率");
	        
	        listName.add("渠道首提人数");
	        listName.add("首提总金额");
	        listName.add("外部首提总金额");
	        listName.add("渠道放款总笔数");
	        listName.add("渠道提现总金额");
	        listName.add("渠道笔均金额");
	        
	        listName.add("收入");
	        listName.add("成本");
	        listName.add("毛利");
	        listName.add("毛利率");
	        
	        listName.add("优化后成本");
	        listName.add("优化后毛利");
	        listName.add("优化后毛利率");
	        listName.add("roi");
	        listName.add("首贷收入");
	        listName.add("roi(首贷)");
	        
	        List<String> listId = new ArrayList<>();
	        listId.add("date");           //时间
	        listId.add("channelName");
	        listId.add("channel");
	        listId.add("register");
	        listId.add("upload");
	        listId.add("uploadConversion");    //渠道
	        listId.add("account");      //开户
	        listId.add("accountConversion");      //开户转户
	        
	        listId.add("firstGetPer");        //首提总金额
	        listId.add("firstGetSum");      //外部首提总金额
	        listId.add("outFirstGetSum");     //外部注册
	        listId.add("loaner");
	        listId.add("channelSum");        //渠道提现总金额
	        listId.add("channelPer");         //外部进件
	       
	        
	        listId.add("income");      //收入
	        listId.add("cost");            //成本
	        listId.add("grossProfit");            //毛利
	        listId.add("grossProfitRate");            //毛利率
	        
	        listId.add("cost2");         //外部进件
	        listId.add("grossProfit2");      //外部首提人数
	        listId.add("grossProfitRate2");         //外部首提总额
	        listId.add("ROI");      //外部提现总额
	        listId.add("firstIncome");      //提现总额
	        listId.add("ROIfirst");      //收入
	    
	        
	        ExportMapExcel exportExcelUtil = new ExportMapExcel();
	        exportExcelUtil.exportExcelString("bd日报",listName,listId,reportforms,response);
	        
		}
		//续贷数据下载
		public void exportSecondData(String json,HttpServletResponse response) {
			
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return ;
			
			JSONObject whereJson = JSONObject.parseObject(json);
			
			String date = whereJson.getString("date");
			String appId = whereJson.getString("appId");
//			String maxDate = whereJson.getString("maxDate");
		
			String sql = "select date,sum(loaner)loaner,sum(firstGetPer)firstGetPer,sum(secondGetPi)secondGetPi,sum(channelSum)channelSum,sum(firstGetSum)firstGetSum,sum(secondGetSum)secondGetSum,"  
				+ " round(sum(firstGetSum)/sum(firstGetPer),2)firstPer,round(sum(secondGetSum)/sum(secondGetPi),2)secondPer,round(sum(if(cost2=0,cost,cost2)),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit," 
				+ " sum(firstIncome)firstIncome,round(sum(firstIncome)-sum(if(cost2=0,cost,cost2)),2)firstProfit,round(sum(secondIncome),2)secondIncome,round(sum(secondIncome)-sum(if(cost2=0,cost,cost2)),2)secondProfit,"  
				+ " round(sum(firstIncome)/sum(income)*100,4) firstPrcent,round(sum(secondIncome)/sum(income)*100,4) secondPrcent,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate" 
				+ " from operate_reportform where date >= '" + date + "' and date <= '" + date + "' and appId = " + appId + " group by date";
			
			List<Map<String,String>> reportforms = null;
			
			try {
				reportforms = od.Query(sql);
				for (Map<String, String> map : reportforms) {
					for (String key : map.keySet()) {
						if(map.get(key) == null) {
							map.put(key, "0");
						}
					}
				}
				for (Map<String, String> map : reportforms) {
					map.put("firstPrcent", map.get("firstPrcent")+"%");
					map.put("secondPrcent", map.get("secondPrcent")+"%");
					map.put("grossProfitRate", map.get("grossProfitRate")+"%");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			List<String> listName = new ArrayList<>();
	        listName.add("日期");
	        listName.add("放款总笔数");
	        listName.add("首提笔数");
	        listName.add("续贷笔数");
	        listName.add("提现总额");
	        listName.add("首提总额");
	        listName.add("续贷总额");
	        listName.add("首提笔均");
	        
	        listName.add("续贷笔均");
	        
	       
	        listName.add("成本");
	        listName.add("毛利");
	        listName.add("首贷收入");
	        listName.add("首贷毛利");
	        
	        listName.add("续贷收入");
	        listName.add("续贷毛利");
	        listName.add("首贷毛利占比");
	        listName.add("续贷毛利占比");
	        
	        listName.add("毛利率");
	        
	 
	        List<String> listId = new ArrayList<>();
	        listId.add("date");           //时间
	        listId.add("loaner");
	        listId.add("firstGetPer");
	        listId.add("secondGetPi");
	        listId.add("channelSum");
	        listId.add("firstGetSum");    //渠道
	        listId.add("secondGetSum");      //开户
	        listId.add("firstPer");      //开户转户
	        
	        listId.add("secondPer");        //首提总金额
	        listId.add("cost");      //外部首提总金额
	        listId.add("grossProfit");     //外部注册
	        listId.add("firstIncome");
	        listId.add("firstProfit");        //渠道提现总金额
	        listId.add("secondIncome");         //外部进件
	       
	        
	        listId.add("secondProfit");      //收入
	        listId.add("firstPrcent");            //成本
	        listId.add("secondPrcent");            //毛利
	        listId.add("grossProfitRate");            //毛利率
	        

	        ExportMapExcel exportExcelUtil = new ExportMapExcel();
	        exportExcelUtil.exportExcelString("续贷日报",listName,listId,reportforms,response);
	        
		}
		
		//市场数据 报表下载
		public void exportMarketData(String json,HttpServletResponse response) {
			
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return ;
			
			JSONObject whereJson = JSONObject.parseObject(json);
			
			List<Long> ids = new ArrayList<Long>();
			String date = whereJson.getString("date");   
//			String maxDate = whereJson.getString("maxDate"); 
			String appId = whereJson.getString("appId"); 
			List<Dictionary> channelTypeList = Cache.getDicList(4);
			ids.add(channelTypeList.get(1).getId());
			ids.add(channelTypeList.get(4).getId());
//			List<Map<String,String>> reportformDay = null;
			List<Map<String,String>> reportforms = null;
			
			String sql = "select date,sum(register)register,sum(upload)upload,round(sum(upload)/sum(register)*100,4) uploadConversion," 
					+"	sum(account)account,round(sum(account)/sum(upload)*100,4) accountConversion,sum(firstGetPer)firstGetPer,sum(loaner)loaner," 
					+"  round(sum(channelSum)/sum(loaner),2)perCapital,sum(channelSum)channelSum," 
					+"  round(sum(income),2)income,round(sum(cost),2)cost,round(sum(income) - sum(if(cost2=0,cost,cost2)),2)grossProfit,round((sum(income) - sum(if(cost2=0,cost,cost2)))/sum(income)*100,4)grossProfitRate," 
					+"  round(sum(if(cost2=0,cost,cost2))/sum(register),2) registerCpa, round(sum(if(cost2=0,cost,cost2))/sum(account),2) accountCpa,round(sum(income)/sum(if(cost2=0,cost,cost2)),2) ROI" 
					+"  from operate_reportform where channelType in (" + ids.get(0) + "," + ids.get(1) + ") and appId = " + appId 
					+" and date >= '" + date + "' and date <= '" + date + "' group by date";
					try {
						reportforms = od.Query(sql);
						reportforms.get(0).put("channelName", "total");
						reportforms.get(0).put("channel", "total");
						List<Map<String,String>> reportform = od.getMarketData(ids,date,date,appId);
						reportforms.addAll(reportform);
						
						for (Map<String, String> map : reportforms) {
							for (String key : map.keySet()) {
								if(map.get(key) == null) {
									map.put(key, "0");
								}
							}
						}
						for (Map<String, String> map : reportform) {
							map.put("uploadConversion", map.get("uploadConversion")+"%");
							map.put("accountConversion", map.get("accountConversion")+"%");
							map.put("grossProfitRate", map.get("grossProfitRate")+"%");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					reportformDay.get(0).put("date", minDate);
					
					List<String> listName = new ArrayList<>();
					listName.add("日期");
			        listName.add("推广渠道");
			        listName.add("渠道号");
			        listName.add("注册");
			        listName.add("进件");
			        listName.add("开户");
			        listName.add("首提人数");
			        listName.add("提现笔均金额");
			        
			        listName.add("渠道放款总笔数");
			        listName.add("渠道提现总金额");
			       
			        listName.add("收入");
					listName.add("成本");
			        listName.add("毛利");
			        listName.add("毛利率");
			        listName.add("进件转化率");
			        
			        listName.add("开户转化率");
			        listName.add("注册CPA");
			        listName.add("开户CPA");
			        listName.add("ROI");
			      List<String> listId = new ArrayList<>();
			        listId.add("date");          
			        listId.add("channelName");
			        listId.add("channel");
			        listId.add("register");
			        listId.add("upload");
			        listId.add("account");   
			        listId.add("firstGetPer");     
			        listId.add("perCapital");      
			        
			        listId.add("loaner");        
			        listId.add("channelSum");      
			        listId.add("income");     
			        listId.add("cost");
			        listId.add("grossProfit");       
			        listId.add("grossProfitRate");         
			       
			        
			        listId.add("uploadConversion");     
			        listId.add("accountConversion");            
			        listId.add("registerCpa");            
			        listId.add("accountCpa");            
					listId.add("ROI"); 
			        

			        ExportMapExcel exportExcelUtil = new ExportMapExcel();
			        exportExcelUtil.exportExcelString("市场日报",listName,listId,reportforms,response);
					
		}
		
		//运营和续贷日报历史下载
		public void exportOperateAndSecondData(String json,HttpServletResponse response) {
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return ;
			
			List<Map<String,String>> reportforms = getOperateAndSecondDataList(json);
			for (Map<String, String> map : reportforms) {
				for (String key : map.keySet()) {
					if(map.get(key) == null) {
						map.put(key, "0");
					}
				}
			}
			for (Map<String, String> map : reportforms) {
				map.put("uploadConversion", map.get("uploadConversion")+"%");
				map.put("accountConversion", map.get("accountConversion")+"%");
				map.put("loanConversion", map.get("loanConversion")+"%");
				map.put("grossProfitRate", map.get("grossProfitRate")+"%");
				map.put("firstPrcent", map.get("firstPrcent")+'%');
				map.put("secondPrcent", map.get("secondPrcent")+'%');
//				map.put("grossProfitRate", map.get("grossProfitRate")+'%');
			}
			JSONObject whereJson = JSONObject.parseObject(json);
			String channelType = whereJson.getString("channelType");
			List<String> listName = new ArrayList<>();
			List<String> listId = new ArrayList<>();
			String name = "";
			if("6".equals(channelType)){
				 listName.add("日期");
			        listName.add("注册");
			        listName.add("进件");
			        listName.add("进件转化率");
			        listName.add("开户");
			        listName.add("开户转化率");
			        listName.add("提现");
			        listName.add("放款总笔数");
			        
			        listName.add("提现总金额");
			        listName.add("放款笔均金额");
			       
			        listName.add("收入");
					listName.add("成本");
			        listName.add("毛利");
			        listName.add("毛利率");
			        
		        listId.add("date");          
		        listId.add("register");
		        listId.add("upload");
		        listId.add("uploadConversion");
		        listId.add("account");   
		        listId.add("accountConversion");   
		        listId.add("loan");     
		        listId.add("loaner");      
		        
		        listId.add("channelSum");        
		        listId.add("perCapitaCredit");      
		        listId.add("income");     
		        listId.add("cost");
		        listId.add("grossProfit");       
		        listId.add("grossProfitRate");         
		       

		        name = "运营日报历史报表";
			}else {
				listName.add("日期");
		        listName.add("放款总笔数");
		        listName.add("首提笔数");
		        listName.add("续贷笔数");
		        listName.add("提现总额");
		        listName.add("首提总额");
		        listName.add("续贷总额");
		        listName.add("首提笔均");
		        
		        listName.add("续贷笔均");
				listName.add("成本");
		        listName.add("毛利");
		        listName.add("首贷收入");
		        listName.add("首贷毛利");
		        
		        listName.add("续贷收入");
		        listName.add("续贷毛利");
		        listName.add("首贷毛利占比");
		        listName.add("续贷毛利占比");
				listName.add("毛利率");
				//
				listId.add("date");          
		        listId.add("loaner");
		        listId.add("firstGetPer");
		        listId.add("secondGetPi");
		        listId.add("channelSum");   
		        listId.add("firstGetSum");   
		        listId.add("secondGetSum");     
		        listId.add("firstPer");      
		        
		        listId.add("secondPer");        
		        listId.add("cost");      
		        listId.add("grossProfit");     
		        listId.add("firstIncome");
		        listId.add("firstProfit");       
		        listId.add("secondIncome");   
		        
		        listId.add("secondProfit");   
		        listId.add("firstPrcent");   
		        listId.add("secondPrcent");     
		        listId.add("grossProfitRate");   
		        name = "续贷历史报表";
			}
	        

	        ExportMapExcel exportExcelUtil = new ExportMapExcel();
	        exportExcelUtil.exportExcelString(name,listName,listId,reportforms,response);
			
			
		}
		
		//bd和市场日报历史下载
		public void exportgetBdAndMarketData(String json,HttpServletResponse response) {
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return ;
			JSONObject whereJson = JSONObject.parseObject(json);
			String channelType = whereJson.getString("channelType");
			List<Map<String,String>> reportforms = od.getBdAndMarketData(whereJson);
			for (Map<String, String> map : reportforms) {
				for (String key : map.keySet()) {
					if(map.get(key) == null) {
						map.put(key, "0");
					}
				}
			}
			for (Map<String, String> map : reportforms) {
				map.put("uploadConversion", map.get("uploadConversion")+"%");
				map.put("accountConversion", map.get("accountConversion")+"%");
				map.put("grossProfitRate", map.get("grossProfitRate")+"%");
				map.put("grossProfitRate2", map.get("grossProfitRate2")+"%");
			}
			List<String> listName = new ArrayList<>();
			 List<String> listId = new ArrayList<>();
			 String name = "";
			 if("2".equals(channelType)){
				listName.add("日期");
		        listName.add("推广渠道");
		        listName.add("渠道号(一级)");
		        listName.add("注册");
		        listName.add("进件");
		        listName.add("进件转化率");
		        listName.add("开户");
		        listName.add("开户转化率");
		        
		        listName.add("渠道首提人数");
				listName.add("首提总金额");
		        listName.add("外部首提总金额");
		        listName.add("渠道放款总笔数");
		        listName.add("渠道提现总金额");
		        
		        listName.add("渠道笔均金额");
		        listName.add("收入");
		        listName.add("成本");
		        listName.add("毛利");
				listName.add("毛利率");
				listName.add("优化后成本");
		        listName.add("优化后毛利");
		        listName.add("优化后毛利率");
				listName.add("roi");
				listName.add("首贷收入");
				listName.add("roi首贷");
				
				listId.add("date");          
		        listId.add("channelName");
		        listId.add("channel");
		        listId.add("register");
		        listId.add("upload");
		        listId.add("uploadConversion");
		        listId.add("account");   
		        listId.add("accountConversion");     
		        listId.add("firstGetPer");      
		        
		        listId.add("firstGetSum");        
		        listId.add("outFirstGetSum");      
		        listId.add("loaner");     
		        listId.add("channelSum");
		        listId.add("perCapitaCredit");       
		        listId.add("income");        
		        listId.add("cost");     
		        listId.add("grossProfit");            
		        listId.add("grossProfitRate");            
		        listId.add("cost2");            
		        listId.add("grossProfit2"); 
		        listId.add("grossProfitRate2");                       
		        listId.add("ROI"); 
		        listId.add("firstIncome");            
		        listId.add("ROIFirst");            

		        name = "bd日报历史";
			 }
			 else {
				 listName.add("日期");
			        listName.add("推广渠道");
			        listName.add("渠道号");
			        listName.add("注册");
			        listName.add("进件");
					listName.add("开户");
			        listName.add("首提人数");
					listName.add("提现笔均金额");
			        listName.add("渠道放款总笔数");
			        listName.add("渠道提现总金额");
			        listName.add("收入");
			        listName.add("成本");
			        listName.add("毛利");
					listName.add("毛利率");
					 listName.add("进件转化率");
					  listName.add("开户转化率");
					listName.add("注册CPA");
					listName.add("开户CPA");
					listName.add("ROI");
			 
			        listId.add("date");          
			        listId.add("channelName");
			        listId.add("channel");
			        listId.add("register");
			        listId.add("upload");
			        listId.add("account");   
			        listId.add("firstGetPer");     //首提人数
			        listId.add("perCapitaCredit");      //提现笔均金额
			        listId.add("loaner");
			        listId.add("channelSum");        
			        listId.add("income");        
			        listId.add("cost");
			        listId.add("grossProfit");       
			        listId.add("grossProfitRate");         
			        listId.add("uploadConversion");     
			        listId.add("accountConversion");            
			        listId.add("registerCpa");            
			        listId.add("accountCpa");            
					listId.add("ROI"); 
					name = "市场日报历史";
			 }
	        

	        ExportMapExcel exportExcelUtil = new ExportMapExcel();
	        exportExcelUtil.exportExcelString(name,listName,listId,reportforms,response);
		}
		
		//bd和市场数据详情下载
		public void exportBdAndMarketDeatailData(String json,HttpServletResponse response) {
			String check = this.CheckJson(json);
			if(!StringUtils.isStrEmpty(check))
				return ;
			JSONObject whereJson = JSONObject.parseObject(json);
			List<Map<String,String>> reportforms =	od.getBdAndMarketDeatailData(whereJson);
			for (Map<String, String> map : reportforms) {
				for (String key : map.keySet()) {
					if(map.get(key) == null) {
						map.put(key, "0");
					}
				}
			}
			for (Map<String, String> map : reportforms) {
				map.put("uploadConversion", map.get("uploadConversion")+"%");
				map.put("accountConversion", map.get("accountConversion")+"%");
				map.put("grossProfitRate", map.get("grossProfitRate")+"%");
				map.put("grossProfitRate2", map.get("grossProfitRate2")+"%");
			}
			String channelType = whereJson.getString("channelType");
			List<String> listName = new ArrayList<>();
			 List<String> listId = new ArrayList<>();
			 String name = "";
			 if("2".equals(channelType)){
			//
				 listName.add("日期");
			        listName.add("推广渠道");
			        listName.add("渠道号(一级)");
			        listName.add("注册");
			        listName.add("进件");
			        listName.add("进件转化率");
			        listName.add("开户");
			        listName.add("开户转化率");
			        
			        listName.add("渠道首提人数");
					listName.add("首提总金额");
			        listName.add("外部首提总金额");
			        listName.add("渠道放款总笔数");
			        listName.add("渠道提现总金额");
			        
			        listName.add("渠道笔均金额");
			        listName.add("收入");
			        listName.add("成本");
			        listName.add("毛利");
					listName.add("毛利率");
					listName.add("优化后成本");
			        listName.add("优化后毛利");
			        listName.add("优化后毛利率");
					listName.add("roi");
					listName.add("首贷收入");
					listName.add("roi首贷");
					
					listId.add("date");          
			        listId.add("channelName");
			        listId.add("channel");
			        listId.add("register");
			        listId.add("upload");
			        listId.add("uploadConversion");
			        listId.add("account");   
			        listId.add("accountConversion");     
			        listId.add("firstGetPer");      
			        
			        listId.add("firstGetSum");        
			        listId.add("outFirstGetSum");      
			        listId.add("loaner");     
			        listId.add("channelSum");
			        listId.add("perCapitaCredit");       
			        listId.add("income");        
			        listId.add("cost");     
			        listId.add("grossProfit");            
			        listId.add("grossProfitRate");            
			        listId.add("cost2");            
			        listId.add("grossProfit2"); 
			        listId.add("grossProfitRate2");                       
			        listId.add("ROI"); 
			        listId.add("firstIncome");            
			        listId.add("ROIFirst");            

			        name = "bd日报历史详情";
			 }else {
				 //市场详情
				 listName.add("日期");
				 listName.add("推广渠道");
				 listName.add("渠道号");
				 listName.add("注册");
				 listName.add("进件");
				 listName.add("开户");
				 listName.add("首提人数");
				 listName.add("提现笔均金额");

				 listName.add("渠道放款总笔数");
				 listName.add("渠道提现总金额");

				 listName.add("收入");
				 listName.add("成本");
				 listName.add("毛利");
				 listName.add("毛利率");
				 listName.add("进件转化率");

				 listName.add("开户转化率");
				 listName.add("注册CPA");
				 listName.add("开户CPA");
				 listName.add("ROI");

				 listId.add("date");          
				 listId.add("channelName");
				 listId.add("channel");
				 listId.add("register");
				 listId.add("upload");
				 listId.add("account");   
				 listId.add("firstGetPer");     
				 listId.add("perCapitaCredit");      

				 listId.add("loaner");        
				 listId.add("channelSum");      
				 listId.add("income");     
				 listId.add("cost");
				 listId.add("grossProfit");       
				 listId.add("grossProfitRate");         


				 listId.add("uploadConversion");     
				 listId.add("accountConversion");            
				 listId.add("registerCpa");            
				 listId.add("accountCpa");            
				 listId.add("ROI"); 
				 
				 name = "市场日报历史详情";
			 }
	        

	        ExportMapExcel exportExcelUtil = new ExportMapExcel();
	        exportExcelUtil.exportExcelString(name,listName,listId,reportforms,response);
		}
}
