package com.maimob.server.importData.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.daoImpl.DaoWhere;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.AdminPermission;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Operate_reportform;
import com.maimob.server.db.entity.UserPermission;
import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.StringUtils;

import freemarker.template.utility.StringUtil;

public class OperateDao extends Dao {

	public static void main(String[] args) {
		 System.out.println("36010219830915".substring(6,10));;
	}
	
	public OperateDao() {

		super("db_operate");

	}

	public List<Operate_reportform> findSumFormDay(List<Long> adminids, JSONObject jobj,String time) {

		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);
		String where1 = where[0];

		if (adminids == null || adminids.size() == 0) {
			where1 += " and en.channelId > 0 ";

		} else if (adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String sql = " select  count(1) cou  from   (  "
				+ " select  distinct en.channel   from operate_reportform en   " + where1
				+ " and en.channelid > 0 " + " ) a   ";

		String cou = "";
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			cou = ordList.get(0).get("cou");
		} catch (Exception e) {
			e.printStackTrace();
		}

		String hql = "select '' adminiName,'' ChannelName," + " SUM( h5Click) h5Click ,  "
				+ " SUM(en.outRegister) h5Register ,  " + " SUM(en.outActivation) activation ,  "
				+ " SUM(en.outRegister) register ,  " + "  SUM(en.outUpload) upload ,  "
				+ "  SUM(en.outAccount) account ,  " + "  SUM(en.outLoan) loan ,  " + "  SUM(en.outCredit) credit ,  "
				+ "  SUM(en.outPerCapitaCredit) perCapitaCredit ,  " + "  SUM(en.outFirstGetPer) firstGetPer ,  "
				+ "  SUM(en.outFirstGetSum) firstGetSum ,  " + "  SUM(en.outChannelSum) channelSum "
				+ "from operate_reportform en ";
		hql += where1;

		List<Operate_reportform> hj = map_obj(hql, where[3], 0, null, null);
		hj.get(0).setChannel(cou + "个渠道");
		
		
 
		

		return hj;
	}
	
	
	

	public List<Map<String, String>> findSumFormDayOperate(List<Long> adminids, JSONObject jobj,String time) {

		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);
		String where1 = where[0];

		if (adminids == null || adminids.size() == 0) {
			where1 += " and en.channelId > 0 ";

		} else if (adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String sql = " select  count(1) cou  from   (   "
				+ " select distinct channel from operate_reportform en    " + where1
				+ " and en.channelid > 0   )b   ";

		String cou = "";
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			cou = ordList.get(0).get("cou");
		} catch (Exception e) {
			e.printStackTrace();
		}
//
//		String hql = "select '' adminiName,'' ChannelName," + " SUM( h5Click) h5Click ,  "
//				+ " SUM(en.outRegister) h5Register ,  " + " SUM(en.outActivation) activation ,  "
//				+ " SUM(en.outRegister) register ,  " + "  SUM(en.outUpload) upload ,  "
//				+ "  SUM(en.outAccount) account ,  " + "  SUM(en.outLoan) loan ,  " + "  SUM(en.outCredit) credit ,  "
//				+ "  SUM(en.outPerCapitaCredit) perCapitaCredit ,  " + "  SUM(en.outFirstGetPer) firstGetPer ,  "
//				+ "  SUM(en.outFirstGetSum) firstGetSum ,  " + "  SUM(en.outChannelSum) channelSum "
//				+ "from operate_reportform en ";
//		hql += where1;
//
//		List<Operate_reportform> hj = map_obj(hql, where[3], 0, null, null);
//		hj.get(0).setChannel(cou + "个渠道");
		
		

		String hql = " select  "
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( outActivation) outActivation ,  " + " sum( register) register ,  " + " sum( outRegister) outRegister ,  " 
				+ " sum( upload) upload ,  "+ " sum( outUpload) outUpload ,  " + " sum( account) account ,  " + " sum( outAccount) outAccount ,  " 
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(outFirstGetPer) outFirstGetPer ,  " + " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum ,  "
				+ " sum(outChannelSum) outChannelSum ,  " + " sum(income) income ,  " + "sum(firstIncome) firstIncome ," + "sum(secondIncome) secondIncome ,"
				+ " sum(en.outFirstGetSum) outFirstGetSum ,  " + " sum(cost) cost, sum(  if(cost2=0,cost,cost2) )cost2  " + " from operate_reportform en  ";

		hql += where1;
		

		List<Map<String, String>> list = map_obj3(hql," / "+where[3]+"天",null,null);
		list.get(0).put("channel", cou+"个渠道");
		
		
		return list;

	}

	public long findFormCou(List<Long> channelids,List<Long> adminids, JSONObject jobj, String dateType,String time) {


		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and a.channelId > 0 ";
		}
		else if (adminids != null && adminids.size() > 0) {
			where1 += " and a.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and a.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String group = DaoWhere.getFromGroup(jobj);
		String group1 = DaoWhere.getFromGroupNothing(jobj);
		
		String hql = "";

	 	if(dateType.equals("1"))
	 	{
	 		hql = "select count(1) cou from ( select date from operate_reportform a  " + where1 +" group by date"+group+" )a";
	 	}
	 	else
	 	if(dateType.equals("2"))
	 	{
	 		hql = "select count(1) cou from ( select month from operate_reportform a  " + where1 +" group by month"+group+" )a";
	 	}
	 	else {
	 		hql = "select count(1) cou from ( select 1 from operate_reportform a  " + where1 +" group by "+group1+" )a";
	 	}
		
		
		long channelSum = 0;
		try {

			List<Map<String, String>> ordList = this.Query(hql);
			String cou = ordList.get(0).get("cou");
			channelSum = Long.parseLong(cou);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return channelSum;
	}
	

	public long findFormCouApp(List<Long> channelids,List<Long> adminids, JSONObject jobj, String dateType,String time) {


		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and a.channelId > 0 ";
		}
		else if (adminids != null && adminids.size() > 0) {
			where1 += " and a.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and a.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String hql = " select count(1) cou from operate_reportform_app a " + where1 +" ";
	 	if(!dateType.equals("1"))
	 		hql = "select count(1) cou from ( select channelid from operate_reportform_app a  " + where1 +" group by channel,month )a";
		
		
		long channelSum = 0;
		try {

			List<Map<String, String>> ordList = this.Query(hql);
			String cou = ordList.get(0).get("cou");
			channelSum = Long.parseLong(cou);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return channelSum;
	}

	public List<Operate_reportform> findAdminSumFormDay(List<Long> adminids, JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if (adminids == null || adminids.size() == 0) {
			where1 += " and en.channelId > 0 ";

		} else if (adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String sql = "  select adminid ,count(1) cou  from   ( "+
				"select en.adminid ,  en.proxyid   from operate_reportform en  "+ where1 +"  group by en.adminid ,en.proxyid"+
				") b  group by b.adminid   ";

		Map<String, String> ad_pr = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				String adminid = ordMap.get("adminid");
				String cou = ordMap.get("cou");
				ad_pr.put(adminid, cou);
			}
		} catch (Exception e) {
		}

		sql = " select adminid ,count(1) cou   from   (   select en.adminid ,  en.id channel  from operate_reportform en  " + where1
				+ "     group by  adminid , channel ) b "
				+ " group by b.adminid  ";

		Map<String, String> ad_ch = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				String adminid = ordMap.get("adminid");
				String cou = ordMap.get("cou");
				ad_ch.put(adminid, cou);
			}
		} catch (Exception e) {
		}

		String hql = " select   " + " (select name from operate_admin b where  b.id = en.adminid) adminiName, "
				+ "   adminid, " + "'' ChannelName," + " SUM( h5Click) h5Click ,  "
				+ " SUM(outRegister) h5Register ,  " + " SUM(outActivation) activation ,  "
				+ " SUM(outRegister) register ,  " + " SUM(outUpload) upload ,  " + " SUM(outAccount) account ,  "
				+ " SUM(outLoan) loan ,  " + " SUM(outCredit) credit ,  "
				+ " SUM(outPerCapitaCredit) perCapitaCredit ,  " + " SUM(outFirstGetPer) firstGetPer ,  "
				+ " SUM(outFirstGetSum) firstGetSum ,  " + " SUM(outChannelSum) channelSum  from operate_reportform en " + where1
				+ "  group by en.adminid";

		return map_obj(hql, where[3], 1, ad_pr, ad_ch);
	}
	
	public List<Map<String, String>> findAdminSumFormDayOperate(List<Long> adminids, JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if (adminids == null || adminids.size() == 0) {
			where1 += " and en.channelId > 0 ";

		} else if (adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String sql = "  select adminid ,count(1) cou  from   ( "+
				"select en.adminid ,  en.proxyid   from operate_reportform en  "+ where1 +"  group by en.adminid ,en.proxyid"+
				") b  group by b.adminid   ";

		Map<String, String> ad_pr = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				String adminid = ordMap.get("adminid");
				String cou = ordMap.get("cou");
				ad_pr.put(adminid, cou);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sql = " select adminid ,count(1) cou   from   (   select en.adminid ,   channel  from operate_reportform en  " + where1
				+ "     group by  adminid , channel ) b "
				+ " group by b.adminid  ";

		Map<String, String> ad_ch = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				String adminid = ordMap.get("adminid");
				String cou = ordMap.get("cou");
				ad_ch.put(adminid, cou);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

//		String hql = " select   " + " (select name from operate_admin b where  b.id = a.adminid1) adminiName, "
//				+ "  (a.adminid1) as adminid, " + "'' ChannelName," + " SUM( h5Click) h5Click ,  "
//				+ " SUM(outRegister) h5Register ,  " + " SUM(outActivation) activation ,  "
//				+ " SUM(outRegister) register ,  " + " SUM(outUpload) upload ,  " + " SUM(outAccount) account ,  "
//				+ " SUM(outLoan) loan ,  " + " SUM(outCredit) credit ,  "
//				+ " SUM(outPerCapitaCredit) perCapitaCredit ,  " + " SUM(outFirstGetPer) firstGetPer ,  "
//				+ " SUM(outFirstGetSum) firstGetSum ,  " + " SUM(outChannelSum) channelSum " + " from " + " ( "
//				+ " select c.adminid adminid1,  en.* from operate_reportform en , operate_channel c " + where1
//				+ " and en.channelid = c.id and en.channelid > 0  " + " ) a  group by a. adminid1";
//
//		return map_obj(hql, where[3], 1, ad_pr, ad_ch);
//		
		


		String hql = " select adminId , (select name from operate_admin b where  b.id = en.adminid) adminName,"
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( outActivation) outActivation ,  " + " sum( register) register ,  " + " sum( outRegister) outRegister ,  " 
				+ " sum( upload) upload ,  "+ " sum( outUpload) outUpload ,  " + " sum( account) account ,  " + " sum( outAccount) outAccount ,  " 
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(outFirstGetPer) outFirstGetPer ,  " + " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum ,  "
				+ " sum(outChannelSum) outChannelSum ,  " + " sum(income) income ,  " + "sum(firstIncome) firstIncome ," + "sum(secondIncome) secondIncome ," 
				+ " sum(en.outFirstGetSum) outFirstGetSum ,  " + " sum(cost) cost , sum(  if(cost2=0,cost,cost2) )cost2 " + " from operate_reportform en "+where1+" group by adminid ";
		

		return map_obj3(hql," / "+where[3]+"天", ad_pr, ad_ch);
		
		
	}
	
	

	public List<Operate_reportform> findForm(List<Long> channelids,List<Long> adminids, JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

 
		String group = DaoWhere.getFromGroup(jobj);

		String hql = " select  date,app ,"
				+ " sum( h5Click) h5Click ,  " + " sum(en.outRegister) h5Register , "
				+ " sum(en.outActivation) activation ,  " + " sum(en.outRegister) register ,  "
				+ " sum(en.outUpload) upload ,  " + " sum(en.outAccount) account ,  " + " sum(en.outLoan) loan ,  "
				+ " sum(en.outCredit) credit ,  " + " sum(en.outPerCapitaCredit) perCapitaCredit ,  "
				+ " sum(en.outFirstGetPer) firstGetPer ,  " + " sum(en.outFirstGetSum) firstGetSum ,  "
				+ " sum(en.outChannelSum) channelSum "+group + " from operate_reportform en " + where1 + " group by date,app "+group +" limit " + where[1]
				+ "," + where[2];
		
		
		return map_obj2(hql,"");
	}
	//代理系统报表中心下载按天查找
	public List<Operate_reportform> findFormDayAll(List<Long> channelids,List<Long> adminids, JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

 
		String group = DaoWhere.getFromGroup(jobj);

		String hql = " select  date,app ,"
				+ " sum( h5Click) h5Click ,  " + " sum(en.outRegister) h5Register , "
				+ " sum(en.outActivation) activation ,  " + " sum(en.outRegister) register ,  "
				+ " sum(en.outUpload) upload ,  " + " sum(en.outAccount) account ,  " + " sum(en.outLoan) loan ,  "
				+ " sum(en.outCredit) credit ,  " + " sum(en.outPerCapitaCredit) perCapitaCredit ,  "
				+ " sum(en.outFirstGetPer) firstGetPer ,  " + " sum(en.outFirstGetSum) firstGetSum ,  "
				+ " sum(en.outChannelSum) channelSum "+group + " from operate_reportform en " + where1 + " group by date,app "+group ;
		
		
		return map_obj2(hql,"");
	}

	public List<Map<String, String>> findFormOperate(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String group = DaoWhere.getFromGroup(jobj);
		String hql = " select  date,app ,"
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( outActivation) outActivation ,  " + " sum( register) register ,  " + " sum( outRegister) outRegister ,  " 
				+ " sum( upload) upload ,  sum(outUpload) outUpload , " + " sum( account) account ,  " + " sum( outAccount) outAccount ,  " 
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(outFirstGetPer) outFirstGetPer ,  " + " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum ,  "

				+ " sum(outChannelSum) outChannelSum ,  " + " sum(income) income ,  " 
				+ "sum(firstIncome) firstIncome ," + "sum(secondIncome) secondIncome ,"+ " sum(en.outFirstGetSum) outFirstGetSum ,  "
				+ " sum(cost) cost, sum(  if(cost2=0,cost,cost2) )cost2  "+group + " from operate_reportform en " + where1 + " group by  date,app "+group+" limit " + where[1]

				+ "," + where[2];

		
		
		
		return map_obj3(hql,"",null,null);
	}
	

	public List<Map<String, String>> findFormOperateApp(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String group = DaoWhere.getFromAppGroup(jobj);

		String hql =" select  date,app,"
		+ " sum( register) register ,  " + " sum( login) login ,  " + " sum( idcard) idcard ,  " + " sum( debitCard) debitCard ,  " 
		+ " sum( homeJob) homeJob ,  " + " sum( contacts) contacts ,  " + " sum( vedio) vedio ,  " 
		+ " sum( upload) upload ,  " + " sum( unaccount) unaccount ,  " + " sum( account) account "+ group
		+ " from operate_reportform_app en " + where1 + " group by date,app " + group + " limit " + where[1]
		+ "," + where[2];
		
//		String hql = " select en.* from operate_reportform_app en " + where1 + "  order by date  limit " + where[1] + "," + where[2];

		return map_obj4(hql,"",null,null);
	}
	
	
	

	public List<Operate_reportform> findFormMonth(List<Long> channelids,List<Long> adminids, JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and en.channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String group = DaoWhere.getFromGroup(jobj);

		String hql = " select  trim(month) date,app,"
				+ " sum( h5Click) h5Click ,  " + " sum(en.outRegister) h5Register ,  "
				+ " sum(en.outActivation) activation ,  " + " sum(en.outRegister) register ,  "
				+ " sum(en.outUpload) upload ,  " + " sum(en.outAccount) account ,  " + " sum(en.outLoan) loan ,  "
				+ " sum(en.outCredit) credit ,  " + " sum(en.outPerCapitaCredit) perCapitaCredit ,  "
				+ " sum(en.outFirstGetPer) firstGetPer ,  " + " sum(en.outFirstGetSum) firstGetSum ,  "
				+ " sum(en.outChannelSum) channelSum "+group + " from operate_reportform en " + where1 + " group by month "+group +",app limit " + where[1]
				+ "," + where[2];

		return map_obj2(hql," / "+where[3]+"天");
	}
	
	public List<Operate_reportform> findFormMonthNothing(List<Long> channelids,List<Long> adminids, JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and en.channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String group = DaoWhere.getFromGroupNothing(jobj);

		
		
		String hql = " select  app,"
				+ " sum( h5Click) h5Click ,  " + " sum(en.outRegister) h5Register ,  "
				+ " sum(en.outActivation) activation ,  " + " sum(en.outRegister) register ,  "
				+ " sum(en.outUpload) upload ,  " + " sum(en.outAccount) account ,  " + " sum(en.outLoan) loan ,  "
				+ " sum(en.outCredit) credit ,  " + " sum(en.outPerCapitaCredit) perCapitaCredit ,  "
				+ " sum(en.outFirstGetPer) firstGetPer ,  " + " sum(en.outFirstGetSum) firstGetSum ,  "
				+ " sum(en.outChannelSum) channelSum, "+group + " from operate_reportform en " + where1 + " group by "+group +",app";

		return map_obj2(hql," / "+where[3]+"天");
	}
	
	public List<Operate_reportform> findFormNothing(List<Long> channelids,List<Long> adminids, JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and en.channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String group = DaoWhere.getFromGroupNothing(jobj);

		String hql = " select  app,"
				+ " sum( h5Click) h5Click ,  " + " sum(en.outRegister) h5Register ,  "
				+ " sum(en.outActivation) activation ,  " + " sum(en.outRegister) register ,  "
				+ " sum(en.outUpload) upload ,  " + " sum(en.outAccount) account ,  " + " sum(en.outLoan) loan ,  "
				+ " sum(en.outCredit) credit ,  " + " sum(en.outPerCapitaCredit) perCapitaCredit ,  "
				+ " sum(en.outFirstGetPer) firstGetPer ,  " + " sum(en.outFirstGetSum) firstGetSum ,  "
				+ " sum(en.outChannelSum) channelSum , "+group + " from operate_reportform en " + where1 + " group by  "+group +",app limit " + where[1]
				+ "," + where[2];

//		List<Map<String, String>> ordList = null;
//		try {
//			ordList = this.Query(hql);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return map_obj2(hql," / "+where[3]+"天");
	}
	
	

	public List<Map<String, String>>  findFormMonthOperate(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and en.channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}


		String group = DaoWhere.getFromGroup(jobj);
		String hql = " select  trim(month) date,app,"
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( outActivation) outActivation ,  " + " sum( register) register ,  " + " sum( outRegister) outRegister ,  " 
				+ " sum( upload) upload ,  sum(outUpload) outUpload , " + " sum( account) account ,  " + " sum( outAccount) outAccount ,  " 
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(outFirstGetPer) outFirstGetPer ,  " + " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum ,  "
				+ " sum(outChannelSum) outChannelSum ,  " + " sum(income) income ,  " + "sum(firstIncome) firstIncome ," + "sum(secondIncome) secondIncome ," 
				+ " sum(en.outFirstGetSum) outFirstGetSum ,  " + " sum(cost) cost, sum(  if(cost2=0,cost,cost2) )cost2 "+group + " from operate_reportform en " + where1 + " group by  month,app "+group+" limit " + where[1]
				+ "," + where[2];

		return map_obj3(hql," / "+where[3]+"天",null,null);
	}
	
	public List<Map<String, String>>  findFormNothing(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
		
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and en.channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}


		String group = DaoWhere.getFromGroup(jobj);
		String hql = " select  app,"
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( outActivation) outActivation ,  " + " sum( register) register ,  " + " sum( outRegister) outRegister ,  " 
				+ " sum( upload) upload ,  sum(outUpload) outUpload , " + " sum( account) account ,  " + " sum( outAccount) outAccount ,  " 
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(outFirstGetPer) outFirstGetPer ,  " + " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum ,  "
				+ " sum(outChannelSum) outChannelSum ,  " + " sum(income) income ,  " + "sum(firstIncome) firstIncome ," + "sum(secondIncome) secondIncome ," 
				+ " sum(en.outFirstGetSum) outFirstGetSum ,  " + " sum(cost) cost, sum(  if(cost2=0,cost,cost2) )cost2  "+group + " from operate_reportform en "
				+ where1 + " group by app "+group+" limit " + where[1]
				+ "," + where[2];

		return map_obj3(hql," / "+where[3]+"天",null,null);
	}
	
	public List<Map<String, String>>  findFormMonthOperateApp(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and en.channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String group = DaoWhere.getFromAppGroup(jobj);

		String hql = " select month date,channel,app,"
				+ " sum( register) register ,  " + " sum( login) login ,  " + " sum( idcard) idcard ,  " + " sum( debitCard) debitCard ,  " 
				+ " sum( homeJob) homeJob ,  " + " sum( contacts) contacts ,  " + " sum( vedio) vedio ,  " 
				+ " sum( upload) upload ,  " + " sum( unaccount) unaccount ,  " + " sum( account) account "+ group
				+ " from operate_reportform_app en " + where1 + " group by month,channel,app " + group + " limit " + where[1]
				+ "," + where[2];

		return map_obj4(hql," / "+where[3]+"天",null,null);
	}
	
	
	public List<Map<String, String>>  findFormMonthOperateAppNothing(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and en.channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String group = DaoWhere.getFromAppGroup(jobj);
		String hql = " select channel,app,"
				+ " sum( register) register ,  " + " sum( login) login ,  " + " sum( idcard) idcard ,  " + " sum( debitCard) debitCard ,  " 
				+ " sum( homeJob) homeJob ,  " + " sum( contacts) contacts ,  " + " sum( vedio) vedio ,  " 
				+ " sum( upload) upload ,  " + " sum( unaccount) unaccount ,  " + " sum( account) account "+ group 
				+ " from operate_reportform_app en " + where1 + " group by channel,app " + group + " limit " + where[1]
				+ "," + where[2];

		return map_obj4(hql," / "+where[3]+"天",null,null);
	}
	
	
	
	

	public List<Operate_reportform> findProxySumFormDay(List<Long> channelids, JSONObject jobj,String time) {

		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);
		String where1 = where[0];

		if (channelids.size() == 0) {
			where1 += " and en.channelId > 0 ";

		} else if (channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String sql = " select proxyid ,count(1) cou " + " from  " + " (select proxyid,channelid from  "
				+ " (select c.proxyid  ,  c.id channelid from operate_reportform_day en , operate_channel c  " + where1
				+ " and en.channelid = c.id and en.channelid > 0 ) a " + " group by a.proxyid,a.channelid )b "
				+ " group by  proxyid  ";

		Map<String, String> pr_ch = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				String proxyid = ordMap.get("proxyid");
				String cou = ordMap.get("cou");
				pr_ch.put(proxyid, cou);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sql = " select proxyid ,count(1) cou " + " from  " + " (select proxyid,adminid from  "
				+ " (select c.proxyid  ,  c.adminid from operate_reportform_day en , operate_channel c  " + where1
				+ " and en.channelid = c.id and en.channelid > 0 ) a " + " group by a.proxyid,a.adminid )b "
				+ " group by  proxyid  ";

		Map<String, String> pr_ad = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				String proxyid = ordMap.get("proxyid");
				String cou = ordMap.get("cou");
				pr_ad.put(proxyid, cou);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String hql = " select " + "proxyid," + " '' adminiName, "
				+ " (select   company from operate_proxy b where  b.id = a.proxyid) ChannelName, "
				+ " SUM(h5Click) h5Click ," + " SUM(h5Register) h5Register ," + " SUM(activation) activation ,"
				+ " SUM(register) register ," + " SUM(upload) upload ," + " SUM(account) account ,"
				+ " SUM(loan) loan ," + " SUM(credit) credit ," + " SUM(perCapitaCredit) perCapitaCredit ,"
				+ " SUM(firstGetPer) firstGetPer ," + " SUM(firstGetSum) firstGetSum ," + " SUM(channelSum) channelSum "
				+ " from " + " ( " + " select   c.proxyid ,  en.* from operate_reportform_day en , operate_channel c  "
				+ where1 + " and en.channelid = c.id and en.channelid > 0  " + " ) a  group by a. proxyid";

		return map_obj(hql, where[3], 2, pr_ch, pr_ad);
	}

	// public List<Operate_reportform> findChannelSumFormDay(List<Long>
	// channelids,JSONObject jobj){
	// String[] where = DaoWhere.getFromWhereForFrom(jobj,1);
	//
	// String hql =
	// " select " +
	// " '' adminiName, "+
	// " (select company from operate_proxy b where b.id = a.proxyid) ChannelName,
	// "+
	// " SUM(h5Click) h5Click ,"+
	// " SUM(h5Register) h5Register ,"+
	// " SUM(activation) activation ,"+
	// " SUM(register) register ,"+
	// " SUM(upload) upload ,"+
	// " SUM(account) account ,"+
	// " SUM(loan) loan ,"+
	// " SUM(credit) credit ,"+
	// " SUM(perCapitaCredit) perCapitaCredit ,"+
	// " SUM(firstGetPer) firstGetPer ,"+
	// " SUM(firstGetSum) firstGetSum ,"+
	// " SUM(channelSum) channelSum "+
	// " from "+
	// " ( "+
	// " select c.proxyid , en.* from operate_reportform_day en , operate_channel c
	// "+where+" en.channelid = c.id and en.channelid > 0 "+
	// " ) a group by a. proxyid";
	//
	// return map_obj(hql);
	// }

	public List<Operate_reportform> map_obj(String hql, String day, int type, Map<String, String> map1,
			Map<String, String> map2) {
		List<Operate_reportform> ords = new ArrayList<Operate_reportform>();
		try {
			List<Map<String, String>> ordList = this.Query(hql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				Operate_reportform ord = new Operate_reportform();
				ord.setAdminName(ordMap.get("adminiName"));
				ord.setChannelName(ordMap.get("ChannelName"));
				long h5Click = 0;
				try {

					h5Click = Long.parseLong(ordMap.get("h5Click"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				ord.setH5Click(h5Click);

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

				long perCapitaCredit = 0;
				if (account > 0)
					perCapitaCredit = (credit / account);

				ord.setPerCapitaCredit(perCapitaCredit);

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
				

				String uploadConversion = bl(upload, register);
				String accountConversion = bl(account, upload);
				String loanConversion = bl(loan, account);
				ord.setAccountConversion(Integer.parseInt(accountConversion));
				ord.setUploadConversion(Integer.parseInt(uploadConversion));
				ord.setLoanConversion(Integer.parseInt(loanConversion));
				
				ord.setChannelSum(channelSum);
				ord.setDate(day + "天");

				if (map1 != null) {
					if (type == 1) {
						String val1 = ordMap.get("adminid");
						if (val1 == null)
							val1 = ordMap.get("adminId");
						String val2 = map1.get(val1);
						ord.setChannelName(val2 + "个公司");

						String val3 = map2.get(val1);
						ord.setChannel(val3 + "个渠道");

					} else if (type == 2) {
						String val1 = ordMap.get("proxyid");
						String val2 = map1.get(val1);
						ord.setChannel(val2 + "个渠道");

						String val3 = map2.get(val1);
						ord.setAdminName(val3 + "个负责人");

					}

				}

				ords.add(ord);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ords;
	}

	public List<Operate_reportform> map_obj2(String hql,String days) {
		List<Operate_reportform> ords = new ArrayList<Operate_reportform>();
		try {
			List<Map<String, String>> ordList = this.Query(hql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				Operate_reportform ord = new Operate_reportform();
				ord.setChannel(ordMap.get("channel"));
				if(ordMap.get("channelId") != null)
				{
					ord.setChannelId(Long.parseLong(ordMap.get("channelId")));
	
			    		Channel channel = Cache.getChannelCatche(ord.getChannelId());
			    		if(channel != null)
			    		{
			    			ord.setChannel(channel.getChannel());
			    			ord.setChannelName(channel.getChannelName());
			    		}
					
				}
				
				if(ordMap.get("adminId") != null)
				{
					Admin admin = Cache.getAdminCatche(Long.parseLong(ordMap.get("adminId")));
		        		if(admin != null)
		        			ord.setAdminName(admin.getName());
				}
				
				if(ordMap.get("channelAttribute") != null)
				{

		        		String type = "";
		        		Dictionary dic = Cache.getDic(Long.parseLong(ordMap.get("channelAttribute")));
		        		if(dic != null)
		        			type+=dic.getName().substring(0,1)+" ";
		        		
		        		dic = Cache.getDic(Long.parseLong(ordMap.get("channelType")));
		        		if(dic != null)
		        			type+=dic.getName()+" ";
	
		        		dic = Cache.getDic(Long.parseLong(ordMap.get("subdivision")));
		        		if(dic != null)
		        			type+=dic.getName()+" ";
		        		
		        		ord.setChannelType(type);
					
				}
				
				
				long h5Click = 0;
				try {

					h5Click = Long.parseLong(ordMap.get("h5Click"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				ord.setH5Click(h5Click);

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

				long perCapitaCredit = 0;
				if (account > 0)
					perCapitaCredit = (credit / account);

				ord.setPerCapitaCredit(perCapitaCredit);

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
				ord.setChannelSum(channelSum);
				ord.setDate(ordMap.get("date")+days);
				ord.setApp(ordMap.get("app"));
				String uploadConversion = bl(upload, register);
				String accountConversion = bl(account, upload);
				String loanConversion = bl(loan, account);
				ord.setAccountConversion(Integer.parseInt(accountConversion));
				ord.setUploadConversion(Integer.parseInt(uploadConversion));
				ord.setLoanConversion(Integer.parseInt(loanConversion));
				
				ords.add(ord);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ords;
	}
	
	

	public List<Map<String, String>> map_obj3(String hql,String days, Map<String, String> map1,
			Map<String, String> map2) {
		List<Map<String, String>> ordList = null;
		try {
			ordList = this.Query(hql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				
				
				if(ordMap.get("channelId") != null)
				{
	
			    		Channel channel = Cache.getChannelCatche(Long.parseLong(ordMap.get("channelId")));
			    		if(channel != null)
			    		{
			    			ordMap.put("channelName", channel.getChannelName());
			    			ordMap.put("channel", channel.getChannel());
			    		}
					
				}
				
				if(ordMap.get("adminId") != null)
				{
					Admin admin = Cache.getAdminCatche(Long.parseLong(ordMap.get("adminId")));
		        		if(admin != null)
		        			ordMap.put("adminName", admin.getName());
				}
				
				if(ordMap.get("channelAttribute") != null)
				{

		        		String type = "";
		        		Dictionary dic = Cache.getDic(Long.parseLong(ordMap.get("channelAttribute")));
		        		if(dic != null)
		        			type+=dic.getName().substring(0,1)+" ";
		        		
		        		dic = Cache.getDic(Long.parseLong(ordMap.get("channelType")));
		        		if(dic != null)
		        			type+=dic.getName()+" ";
	
		        		dic = Cache.getDic(Long.parseLong(ordMap.get("subdivision")));
		        		if(dic != null)
		        			type+=dic.getName()+" ";
		        		 
		    			ordMap.put("channelType", type);
					
				}
				
				
				
				long h5Click = 0;
				try {

					h5Click = Long.parseLong(ordMap.get("h5Click"));
				} catch (Exception e) {
					// TODO: handle exception
				} 

				long h5Register = 0;
				try {

					h5Register = Long.parseLong(ordMap.get("h5Register"));
				} catch (Exception e) {
					// TODO: handle exception
				}
 

				long activation = 0;
				try {

					activation = Long.parseLong(ordMap.get("activation"));
				} catch (Exception e) {
					// TODO: handle exception
				}
 
				long register = 0;
				try {

					register = Long.parseLong(ordMap.get("register"));
				} catch (Exception e) {
					// TODO: handle exception
				} 
				long upload = 0;
				try {

					upload = Long.parseLong(ordMap.get("upload"));
				} catch (Exception e) {
					// TODO: handle exception
				} 
				long account = 0;
				try {

					account = Long.parseLong(ordMap.get("account"));
				} catch (Exception e) {
					// TODO: handle exception
				} 
				long loan = 0;
				try {

					loan = Long.parseLong(ordMap.get("loan"));
				} catch (Exception e) {
					// TODO: handle exception
				} 
				long credit = 0;
				try {

					credit = Long.parseLong(ordMap.get("credit"));
				} catch (Exception e) {
					// TODO: handle exception
				} 

				long perCapitaCredit = 0;
				if (account > 0)
					perCapitaCredit = (credit / account);

				ordMap.put("perCapitaCredit", perCapitaCredit+"");

				long firstGetPer = 0;
				try {

					firstGetPer = Long.parseLong(ordMap.get("firstGetPer"));
				} catch (Exception e) {
					// TODO: handle exception
				}
 
				long firstGetSum = 0;
				try {

					firstGetSum = Long.parseLong(ordMap.get("firstGetSum"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				long firstPerCapitaCredit = 0;
				if (firstGetPer > 0)
					firstPerCapitaCredit = (firstGetSum / firstGetPer);

				ordMap.put("firstPerCapitaCredit", firstPerCapitaCredit+"");
				
 
				long channelSum = 0;
				try {

					channelSum = Long.parseLong(ordMap.get("channelSum"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				

				long secondGetPi = 0;
				try {

					secondGetPi = Long.parseLong(ordMap.get("secondGetPi"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				long optimization = 0;
				try {
					optimization = Long.parseLong(ordMap.get("optimization"));
					if(optimization == 1) {
						ordMap.put("optimization","0");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
 
				long secondGetSum = 0;
				try {

					secondGetSum = Long.parseLong(ordMap.get("secondGetSum"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				long secondPerCapitaCredit = 0;
				if (secondGetPi > 0)
					secondPerCapitaCredit = (secondGetSum / secondGetPi);

				ordMap.put("secondPerCapitaCredit", secondPerCapitaCredit+"");
				
				long channelCapitaCredit = 0;
				if (loan != 0)
					channelCapitaCredit = channelSum / loan;

				ordMap.put("channelCapitaCredit", channelCapitaCredit+"");
				
				 String datestr = ordMap.get("date");
				 if(datestr == null)
					 datestr = "";
				ordMap.put("date",datestr+days);
				
				double income = 0;
				try {
					String istr = ordMap.get("income");

					income = Double.parseDouble(istr);
					long lincome = (long) income;
					 
					income = lincome;
					 
				} catch (Exception e) {
					// TODO: handle exception
				}

				double cost = 0;
				try {

					cost = Double.parseDouble(ordMap.get("cost"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				double cost2 = 0;
				try {

					cost2 = Double.parseDouble(ordMap.get("cost2"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				double grossProfit = income - cost;
				if(cost2 != 0)
					grossProfit = income - cost2;
				double grossProfitRate = 0;
				String grossProfitRateStr = "";
				if(income != 0)
				{
					grossProfitRate = grossProfit/income*100;
					
					grossProfitRateStr = grossProfitRate+"";
					
					if(grossProfitRateStr.indexOf(".") != -1 && grossProfitRateStr.length() - grossProfitRateStr.indexOf(".") > 4)
					{
						grossProfitRateStr = grossProfitRateStr.substring(0, grossProfitRateStr.indexOf(".")+4);
					}
				}
				
				String grossProfitStr = grossProfit+"";

				if(grossProfitStr.contains("."))
				{
					grossProfitStr = grossProfitStr.substring(0, grossProfitStr.indexOf("."));
				}
				
				
				
				ordMap.put("grossProfit", grossProfitStr);
				ordMap.put("grossProfitRate", grossProfitRateStr+"%");

				String activationConversion = bl(activation, register);
				String uploadConversion = bl(upload, activation);
				String accountConversion = bl(account, upload);
				String loanConversion = bl(loan, account);
				
				ordMap.put("activationConversion", activationConversion+"%");
				ordMap.put("uploadConversion", uploadConversion+"%");
				ordMap.put("accountConversion", accountConversion+"%");
				ordMap.put("loanConversion", loanConversion+"%");
				

				if (map1 != null) { 
					String val1 = ordMap.get("adminId");
					if (val1 == null)
						val1 = ordMap.get("adminId");
					String val2 = map1.get(val1); 
					ordMap.put("channelName", val2 + "个公司");
					String val3 = map2.get(val1);
					ordMap.put("channel", val3 + "个渠道");
 

				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ordList;
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

	public List<Map<String, String>> getAllTask() {

		String sql = " select * from operate_optimization_task where status < 2 order by id desc limit 0,1 ";

		List<Map<String, String>> tasks = null;
		try {
			tasks = this.Query(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tasks;
	}

	public void updateTask(long id, int status) {

		String sql = " update operate_optimization_task set status=" + status + " where id=" + id + " ";
		try {
			this.Update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	public void updateCost(JSONObject jobj)
	{
		
//		List<Dictionary> applist = Cache.getDicList(1);
//		Map<String,Long> apps = new HashMap<String,Long>();
//		for(Dictionary dic:applist)
//		{
//			dic.getName();
//			apps.put(dic.getName(), dic.getId());
//		}

		JSONArray data = jobj.getJSONArray("fileData");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String update = df.format(new Date());
		
		JSONArray errordata = new JSONArray();
		for(int i = 0;i < data.size();i++)
		{
			JSONObject d = (JSONObject) data.get(i);
			
			String channel = d.getString("channel");
			String date = d.getString("date");
			String cost = d.getString("cost");
			String appid = d.getString("appid");
//			String appid = "0";
//			if(!StringUtils.isStrEmpty(app))
//			{
//				appid= apps.get(app)+"";
//			}
			
			
			if(cost == null)
				cost = "0";
			else
			{
				cost = cost.replaceAll("¥", "");
				cost = cost.replaceAll(",", "");
			}
			
			Channel c = Cache.getChannelCatche(channel);
			String channelName = "";
			long id = 0;
			if(c != null)
			{
				id = c.getId();
				channelName = c.getChannelName();
			}
			else
			{
				errordata.add(d);
				
			}

			try {
				if(!cost.equals("0") && !StringUtils.isStrEmpty(appid))
				{
					
					//保存最后一次优化比例
					String sql2 = "update operate_data_log set channelId= "+id+",cost="+cost+", channel='"+channel+"',channelName='"+channelName+"',updateTime='"+update+"'  where channel= '"+channel+"' and date = '"+date+"' and appid="+appid+" ";
					int yx = this.Update(sql2);
					if(yx==0)
					{
						sql2 = "insert into operate_data_log(cost,channelId,date,channel,channelName,updateTime,appid) values("+cost+" ,"+id+" , '"+date+"', '"+channel+"', '"+channelName+"', '"+update+"',"+appid+") ";
						yx = this.Update(sql2);
					}
					 sql2 = "update operate_reportform set cost2="+cost+"   where channel= '"+channel+"' and date = '"+date+"' and appid="+appid+" ";
					 yx = this.Update(sql2);
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
	
		//return errordata;
	}

	public int getCostCou(JSONObject jobj)
	{
		String[] where = DaoWhere.getCostWhere(jobj, 1);
		String where1 = where[0];
		int cou = 0;
		String sql = "select count(1) cou from operate_data_log "+where1 +" and cost>0 " ;
		List<Map<String,String>> dl = null;
		try {
			dl = this.Query(sql);
			cou = Integer.parseInt(dl.get(0).get("cou"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cou;
	}
	

	public List<Map<String,String>> getCost(JSONObject jobj)
	{
		String[] where = DaoWhere.getCostWhere(jobj, 1);
		String where1 = where[0];
 
		String sql = "select * from operate_data_log "+where1 +" and cost>0  limit "+where[1]+","+where[2];
		List<Map<String,String>> dl = null;
		try {
			dl = this.Query(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dl;
	}
	


	public void deleteCost(JSONObject jobj)
	{

		JSONArray data = jobj.getJSONArray("deletions");
 
		
		JSONArray errordata = new JSONArray();
		for(int i = 0;i < data.size();i++)
		{
			JSONObject d = (JSONObject) data.get(i);
			
			String channel = d.getString("channel");
			String date = d.getString("date");
			String sql = "delete from operate_data_log where channel='"+channel+"' and date='"+date+"' ";
			try {
				this.Update(sql);

				String sql2 = "update operate_reportform set cost2=0   where channel= '"+channel+"' and date = '"+date+"' ";
				this.Update(sql2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} 
 
		 
	}
	
	
	
	

	
	
	public List<Operate_reportform> findFormDay(List<Long> channelids,List<Long> adminids, JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);


		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}


		String hql = " select date,channelId,channel," 
				+ " truncate( h5Click,0) h5Click ,  " + " truncate(outRegister,0) h5Register ,  " + " truncate( outActivation,0) activation ,  "
				+ " truncate( outRegister,0) register ,  " + " truncate( outUpload,0) upload ,  " + " truncate( outAccount,0) account ,  "
				+ " truncate( outLoan,0) loan ,  " + " truncate(outCredit,0) credit ,  "
				+ " truncate( outPerCapitaCredit,0) perCapitaCredit ,  " + " truncate( outFirstGetPer,0) firstGetPer ,  "
				+ " truncate( outFirstGetSum,0) firstGetSum ,  " + " truncate( outChannelSum,0) channelSum "
				+ " from operate_reportform en " + where1 ;

		return map_obj2(hql,"");
	}
	
	public List<Operate_reportform> findFormMon(List<Long> channelids,List<Long> adminids, JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and en.channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String group = DaoWhere.getFromGroup(jobj);
		String hql = " select app,trim(month) date,"
				+ " sum( h5Click) h5Click ,  " + " sum(en.outRegister) h5Register ,  "
				+ " sum(en.outActivation) activation ,  " + " sum(en.outRegister) register ,  "
				+ " sum(en.outUpload) upload ,  " + " sum(en.outAccount) account ,  " + " sum(en.outLoan) loan ,  "
				+ " sum(en.outCredit) credit ,  " + " sum(en.outPerCapitaCredit) perCapitaCredit ,  "
				+ " sum(en.outFirstGetPer) firstGetPer ,  " + " sum(en.outFirstGetSum) firstGetSum ,  "
				+ " sum(en.outChannelSum) channelSum " + group + " from operate_reportform en " + where1 + " group by app,month " + group;

		return map_obj2(hql," / "+where[3]+"天");

	} 

	public List<Map<String, String>> findFormOperateAll(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}


		String group = DaoWhere.getFromGroup(jobj);
		String hql = " select  date,app,"
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( outActivation) outActivation ,  " + " sum( register) register ,  " + " sum( outRegister) outRegister ,  " 
				+ " sum( upload) upload ,  sum(outUpload) outUpload , " + " sum( account) account ,  " + " sum( outAccount) outAccount ,  " 
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(outFirstGetPer) outFirstGetPer ,  " + " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum ,  "
				+ " sum(outChannelSum) outChannelSum ,  " + " sum(income) income ,  " + "sum(firstIncome) firstIncome ," + "sum(secondIncome) secondIncome ,"
				+ " sum(en.outFirstGetSum) outFirstGetSum ,  " + " sum(cost) cost ,sum(cost2) cost2 "+group + " from operate_reportform en " + where1 + " group by  date,app"+group;

		return map_obj3(hql,"",null,null);
	}


	public List<Map<String, String>>  findFormMonthOperateAll(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and en.channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}


		String group = DaoWhere.getFromGroup(jobj);
		String hql = " select  trim(month) date,app,"
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( outActivation) outActivation ,  " + " sum( register) register ,  " + " sum( outRegister) outRegister ,  " 
				+ " sum( upload) upload ,  sum(outUpload) outUpload , " + " sum( account) account ,  " + " sum( outAccount) outAccount ,  " 
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(outFirstGetPer) outFirstGetPer ,  " + " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum ,  "
				+ " sum(outChannelSum) outChannelSum ,  " + " sum(income) income ,  " + "sum(firstIncome) firstIncome ," + "sum(secondIncome) secondIncome ," 
				+ " sum(en.outFirstGetSum) outFirstGetSum ,  " + " sum(cost) cost ,sum(cost2) cost2 "+group + " from operate_reportform en " + where1 + " group by  month,app "+group;

		return map_obj3(hql," / "+where[3]+"天",null,null);
	}

	public List<Map<String, String>>  findFormMonthOperateAllNothing(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and en.channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}


		String group = DaoWhere.getFromGroupNothing(jobj);
		String hql = " select  app,"
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( outActivation) outActivation ,  " + " sum( register) register ,  " + " sum( outRegister) outRegister ,  " 
				+ " sum( upload) upload ,  sum(outUpload) outUpload , " + " sum( account) account ,  " + " sum( outAccount) outAccount ,  " 
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(outFirstGetPer) outFirstGetPer ,  " + " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum ,  "
				+ " sum(outChannelSum) outChannelSum ,  " + " sum(income) income ,  "  + "sum(firstIncome) firstIncome ," + "sum(secondIncome) secondIncome ," 
				+ " sum(en.outFirstGetSum) outFirstGetSum ,  " + " sum(cost) cost ,sum(cost2) cost2, "+group + " from operate_reportform en " + where1 + " group by  app, "+group;

		return map_obj3(hql," / "+where[3]+"天",null,null);
	}
	
	public List<Operate_reportform> findFormMonthAll(List<Long> channelids,List<Long> adminids, JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and en.channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}


		String hql = " select channelid,trim(month) date,"
				+ " sum( h5Click) h5Click ,  " + " sum(en.outRegister) h5Register ,  "
				+ " sum(en.outActivation) activation ,  " + " sum(en.outRegister) register ,  "
				+ " sum(en.outUpload) upload ,  " + " sum(en.outAccount) account ,  " + " sum(en.outLoan) loan ,  "
				+ " sum(en.outCredit) credit ,  " + " sum(en.outPerCapitaCredit) perCapitaCredit ,  "
				+ " sum(en.outFirstGetPer) firstGetPer ,  " + " sum(en.outFirstGetSum) firstGetSum ,  "
				+ " sum(en.outChannelSum) channelSum " + " from operate_reportform en " + where1 + " group by channelid,month " ;

		return map_obj2(hql," / "+where[3]+"天");
	}
	
	public List<Operate_reportform> findFormAll(List<Long> channelids,List<Long> adminids, JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}


		String hql = " select date,channelId,channel," 
				+ " truncate( h5Click,0) h5Click ,  " + " truncate(outRegister,0) h5Register ,  " + " truncate( outActivation,0) activation ,  "
				+ " truncate( outRegister,0) register ,  " + " truncate( outUpload,0) upload ,  " + " truncate( outAccount,0) account ,  "
				+ " truncate( outLoan,0) loan ,  " + " truncate(outCredit,0) credit ,  "
				+ " truncate( outPerCapitaCredit,0) perCapitaCredit ,  " + " truncate( outFirstGetPer,0) firstGetPer ,  "
				+ " truncate( outFirstGetSum,0) firstGetSum ,  " + " truncate( outChannelSum,0) channelSum "
				+ " from operate_reportform en " + where1 ;

		return map_obj2(hql,"");
	}
	
	public void updateTaskLog(String table,String taskDate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		
		String sql = "insert into operate_datatask_log (tableName,date,taskDate) values('"+table+"' ,'"+now+"','"+taskDate+"'); ";
		
		try {
			this.Update(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTaskLog()
	{
		String sql = "select * from operate_datatask_log order by date desc limit 0,20; ";
		StringBuffer msg = new StringBuffer();
		try {
			List<Map<String,String>> logs = 	this.Query(sql);
			for(int i = 0;i < logs.size();i++)
			{
				msg.append(logs.get(i).get("date")+" "+logs.get(i).get("tableName")+" "+logs.get(i).get("taskDate")+"<br>");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg.toString();
	}
	
	
	
	

	public List<Map<String, String>> findSumFormDayOperateAPP(List<Long> adminids, JSONObject jobj,String time) {

		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);
		String where1 = where[0];

		if (adminids == null || adminids.size() == 0) {
			where1 += " and en.channelId > 0 ";

		} else if (adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String sql = "select  count(1) cou  from   ( " + 
				" select distinct  channelid    from operate_reportform en " +where1 +
				" and  en.channelid > 0  )b ";
		String cou = "";
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			cou = ordList.get(0).get("cou");
		} catch (Exception e) {
			e.printStackTrace();
		}

		String hql = " select  "
				+ " sum( register) register ,  " + " sum( login) login ,  " + " sum( idcard) idcard ,  " + " sum( debitCard) debitCard ,  " 
				+ " sum( homeJob) homeJob ,  " + " sum( contacts) contacts ,  " + " sum( vedio) vedio ,  " 
				+ " sum( upload) upload ,  " + " sum( unaccount) unaccount ,  " + " sum( account) account "
				+" from operate_reportform_app en  ";

		hql += where1;
		
		List<Map<String, String>> list = map_obj4(hql," / "+where[3]+"天",null,null);
		list.get(0).put("channel", cou+"个渠道");
		return list;

	}
	
	
	public List<Map<String, String>> findAdminSumFormDayOperateApp(List<Long> adminids, JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if (adminids == null || adminids.size() == 0) {
			where1 += " and en.channelId > 0 ";
		} else if (adminids.size() > 0) {
			where1 += " and en.adminId in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String sql = "  select adminId ,count(1) cou  from   ( "+
				"select en.adminId ,  en.proxyid   from operate_reportform_app en  "+ where1 +"  group by en.adminId ,en.proxyid"+
				") b  group by b.adminId   ";

		Map<String, String> ad_pr = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				String adminid = ordMap.get("adminId");
				String cou = ordMap.get("cou");
				ad_pr.put(adminid, cou);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sql = " select adminId ,count(1) cou   from   (   select en.adminId ,   channel  from operate_reportform_app  en  " + where1
				+ "     group by  adminId , channel ) b "
				+ " group by b.adminId  ";

		Map<String, String> ad_ch = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				String adminid = ordMap.get("adminId");
				String cou = ordMap.get("cou");
				ad_ch.put(adminid, cou);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		String hql = " select  adminId, (select name from operate_admin b where  b.id = en.adminId) adminName,"
				+ " sum( register) register ,  " + " sum( login) login ,  " + " sum( idcard) idcard ,  " + " sum( debitCard) debitCard ,  " 
				+ " sum( homeJob) homeJob ,  " + " sum( contacts) contacts ,  " + " sum( vedio) vedio ,  " 
				+ " sum( upload) upload ,  " + " sum( unaccount) unaccount ,  " + " sum( account) account "
				+ " from operate_reportform_app en "+where1+" group by adminId ";
		

		return map_obj4(hql," / "+where[3]+"天", ad_pr, ad_ch);
		
		
	}
	

	public List<Map<String, String>> map_obj4(String hql,String days, Map<String, String> map1,
			Map<String, String> map2) {
		List<Map<String, String>> ordList = null;
		try {
			ordList = this.Query(hql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				
				if(ordMap.get("channel") != null)
				{
		    			Channel channel = Cache.getChannelCatche(ordMap.get("channel"));
		    		
		    		
			    		if(channel != null)
			    		{
			    			ordMap.put("channelName", channel.getChannelName());
			        		Admin admin = Cache.getAdminCatche(channel.getAdminId());
			        		if(admin != null)
			        		{
				    			ordMap.put("adminName", admin.getName());
				    		}
			    			ordMap.put("channel", channel.getChannel());
			    			
			        		String type = "";
			        		Dictionary dic = Cache.getDic(channel.getAttribute());
			        		if(dic != null)
			        			type+=dic.getName().substring(0,1)+" ";
			        		
			        		dic = Cache.getDic(channel.getType());
			        		if(dic != null)
			        			type+=dic.getName()+" ";
		
			        		dic = Cache.getDic(channel.getSubdivision());
			        		if(dic != null)
			        			type+=dic.getName()+" ";
	
			    			ordMap.put("channelType", type);
			    			
			    		}
	    	
				}
				
				
				
				long register = 0;
				try {

					register = Long.parseLong(ordMap.get("register"));
				} catch (Exception e) {
					// TODO: handle exception
				} 
				long idcard = 0;
				try {

					idcard = Long.parseLong(ordMap.get("idcard"));
				} catch (Exception e) {
					// TODO: handle exception
				}
 

				long debitCard = 0;
				try {

					debitCard = Long.parseLong(ordMap.get("debitCard"));
				} catch (Exception e) {
					// TODO: handle exception
				}
 

				long homeJob = 0;
				try {

					homeJob = Long.parseLong(ordMap.get("homeJob"));
				} catch (Exception e) {
					// TODO: handle exception
				}
 
				long contacts = 0;
				try {

					contacts = Long.parseLong(ordMap.get("contacts"));
				} catch (Exception e) {
					// TODO: handle exception
				} 
				long vedio = 0;
				try {
					vedio = Long.parseLong(ordMap.get("vedio"));
				} catch (Exception e) {
					// TODO: handle exception
				} 
				long upload = 0;
				try {

					upload = Long.parseLong(ordMap.get("upload"));
				} catch (Exception e) {
					// TODO: handle exception
				} 
				long unaccount = 0;
				try {

					unaccount = Long.parseLong(ordMap.get("unaccount"));
				} catch (Exception e) {
					// TODO: handle exception
				} 
				long account = 0;
				try {

					account = Long.parseLong(ordMap.get("account"));
				} catch (Exception e) {
					// TODO: handle exception
				} 


				String idcardConversion = getBL(idcard,register);//传证转化
				String debitCardConversion = getBL(debitCard,idcard);//绑卡转化
				String homeJobConversion = getBL(homeJob,debitCard);//信息转化
				String contactsConversion = getBL(contacts,homeJob);//联系人转化
				String vedioConversion = getBL(vedio,homeJob);// 视频转化
				String uploadConversion = getBL(upload,vedio);//进件转化
				String accountConversion = getBL(account,upload);//开户转化
				String accountAllConversion = getBL(account,register);//开户总转化
				String lostConversion = getBL(register-account,register);//注册流失率
				
				
				ordMap.put("idcardConversion", idcardConversion);
				ordMap.put("debitCardConversion", debitCardConversion);
				ordMap.put("homeJobConversion", homeJobConversion);
				ordMap.put("contactsConversion", contactsConversion);
				ordMap.put("vedioConversion", vedioConversion);
				ordMap.put("uploadConversion", uploadConversion);
				ordMap.put("accountConversion", accountConversion);
				ordMap.put("accountAllConversion", accountAllConversion);
				ordMap.put("lostConversion", lostConversion);

				 String datestr = ordMap.get("date");
				 if(datestr == null)
					 datestr = "";
				ordMap.put("date",datestr+days);

				if (map1 != null) { 
					String val1 = ordMap.get("adminId");
					if (val1 == null)
						val1 = ordMap.get("adminId");
					String val2 = map1.get(val1); 
					ordMap.put("channelName", val2 + "个公司");
					String val3 = map2.get(val1);
					ordMap.put("channel", val3 + "个渠道");
 

				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ordList;
	}

	public String getBL(long l1,long l2)
	{
		if(l2 == 0)
			l2 = 1;
		double idcardConversion = (l1*1.0)/l2;//传证转化
		String bl = (idcardConversion * 100)+"";
		if(bl.length() > bl.indexOf(".") +2)
			bl = bl.substring(0, bl.indexOf(".")+2);
		return bl+"%";
	}
	
	//流程转化表查找数据（按天查询）
	public List<Map<String, String>> findFormOperateAppDown(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String group = DaoWhere.getFromGroup(jobj);

		String hql =" select  date,channel,app,"
		+ " sum( register) register ,  " + " sum( login) login ,  " + " sum( idcard) idcard ,  " + " sum( debitCard) debitCard ,  " 
		+ " sum( homeJob) homeJob ,  " + " sum( contacts) contacts ,  " + " sum( vedio) vedio ,  " 
		+ " sum( upload) upload ,  " + " sum( unaccount) unaccount ,  " + " sum( account) account "+ group
		+ " from operate_reportform_app en " + where1 + " group by date,channel,app " + group ;

//		String hql = " select en.* from operate_reportform_app en " + where1 + "  order by date ";

		return map_obj4(hql,"",null,null);
	}
	
	//流程转化表查找数据（按月查询）
	public List<Map<String, String>>  findFormMonthOperateAppDown(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and en.channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}


		String group = DaoWhere.getFromGroup(jobj);

		String hql = " select month date,channel,app,"
				+ " sum( register) register ,  " + " sum( login) login ,  " + " sum( idcard) idcard ,  " + " sum( debitCard) debitCard ,  " 
				+ " sum( homeJob) homeJob ,  " + " sum( contacts) contacts ,  " + " sum( vedio) vedio ,  " 
				+ " sum( upload) upload ,  " + " sum( unaccount) unaccount ,  " + " sum( account) account "+ group
				+ " from operate_reportform_app en " + where1 + " group by month,channel,app " + group ;

		return map_obj4(hql," / "+where[3]+"天",null,null);
	}
	
	//流程转化表查找数据（不按月不按天查询）
		public List<Map<String, String>>  findFormMonthOperateAppDownNothing(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
			String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

			String where1 = where[0];

			if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
				where1 += " and en.channelId > 0 ";

			}else if (adminids != null && adminids.size() > 0) {
				where1 += " and en.adminid in ( ";
				int i = 0;
				for (long id : adminids) {
					if (i == 0)
						where1 += id;
					else
						where1 += "," + id;
					i++;
				}
				where1 += ")";
			}
			else if (channelids != null && channelids.size() > 0) {
				where1 += " and en.channelId in ( ";
				int i = 0;
				for (long id : channelids) {
					if (i == 0)
						where1 += id;
					else
						where1 += "," + id;
					i++;
				}
				where1 += ")";
			}


			String group = DaoWhere.getFromGroup(jobj);
			String hql = " select channel,app,"
					+ " sum( register) register ,  " + " sum( login) login ,  " + " sum( idcard) idcard ,  " + " sum( debitCard) debitCard ,  " 
					+ " sum( homeJob) homeJob ,  " + " sum( contacts) contacts ,  " + " sum( vedio) vedio ,  " 
					+ " sum( upload) upload ,  " + " sum( unaccount) unaccount ,  " + " sum( account) account "+ group 
					+ " from operate_reportform_app en " + where1 + " group by channel,app " + group ;

			return map_obj4(hql," / "+where[3]+"天",null,null);
		}
	
	public void findPermissionByType(String type)
	{
		String sql = "select * from operate_permission where type="+type;
		
	}
	//取得渠道权限
	/**
	 * 
	 * @param type 功能组ID
	 * @param adminid 管理员ID
	 * @param optype 
	 * @return
	 */
	public List<Map<String, String>>  getAdminPermission(String type,String adminid,String optype) {
		
		String hql = " select  if(a.id is null,0,a.id ) id , b.name ,   if( b.allshow = 0,0, if(b.isuse = 0,1,if(a.show is null,0,a.show))) `show` ,a.adminid  " + 
				" from  operate_permission b left join " + 
				" (select   * from operate_admin_permission where adminid = "+adminid+"  and  optype = "+optype+"    ) " + 
				" a  on a.name = b.name  where  b.optype = "+optype+"    and b.type="+type+"  ";
		
		List<Map<String, String>> aps = null;
		try {
			aps = this.Query(hql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return aps;
	}

	//取得渠道权限
//	public List<Map<String, String>>  getAdminPermission(String adminid ,String optype) {
//		
//		String hql = " select  if(a.id is null,0,a.id ) id , b.name ,b.type , " + 
//					" if(a.show is null,0,a.show)  `show` ,a.adminid " + 
//					" from  operate_permission b left join operate_admin_permission a  on a.name = b.name and b.optype = "+optype+" and a.adminid = "+adminid+"  ";
//
//		List<Map<String, String>> aps = null;
//		try {
//			aps = this.Query(hql);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//	
//		return aps;
//	}
	 
	

	//取得渠道权限
	public List<UserPermission>  getAllAdminPermission(String adminid ,String optype) {
		
		String hql = " select   b.meta , b.name ,   if( b.allshow = 0,0, if(b.isuse = 0,1,if(a.show is null,0,a.show))) `show`  " + 
				" from  operate_permission b left join " + 
				" (select   * from operate_admin_permission where adminid = "+adminid+" and  optype = "+optype+"    ) " + 
				" a  on a.name = b.name  where  b.optype = "+optype+" and b.type!=1521006432292  and b.id != 96  ";
		
		if(adminid.equals("1517197192342") || adminid.equals("1516704387763") || adminid.equals("1519871387848"))
		{

			hql = " select   b.meta , b.name ,   1 `show`  " + 
					" from  operate_permission b left join " + 
					" (select   * from operate_admin_permission where adminid = "+adminid+"  ) " + 
					" a  on a.name = b.name  where  b.optype = "+optype+"   ";
			
		} 
		
		
		
	    List<UserPermission> UserPermissionList = new ArrayList<UserPermission>();
		List<Map<String, String>> aps = null;
		try {
			aps = this.Query(hql);
		    
		    Map<String, UserPermission> Live1 = new HashMap<String, UserPermission>();
		    Map<String, UserPermission> Live2 = new HashMap<String, UserPermission>();
			for(int i = aps.size()-1;i >= 0;i--)
			{
				UserPermission up = new UserPermission(aps.get(i));
				if(up.getMeta().length() == 2)
				{
					UserPermissionList.add(up);
					Live1.put(up.getMeta(), up);
					aps.remove(i);
				}
			}
			

			for(int i = aps.size()-1;i >= 0;i--)
			{
				UserPermission up = new UserPermission(aps.get(i));
				if(up.getMeta().length() == 4)
				{
					String meta = up.getMeta();
					String superMeta = meta.substring(0,2);
					UserPermission superlive = Live1.get(superMeta);
					if(superlive != null)
					{
						superlive.addPermission(up);
					}
					Live2.put(up.getMeta(), up);
					aps.remove(i);
				}
			}
			
			for(int i = aps.size()-1;i >= 0;i--)
			{
				UserPermission up = new UserPermission(aps.get(i));
				if(up.getMeta().length() == 6)
				{
					String meta = up.getMeta();
					String superMeta = meta.substring(0,4);
					UserPermission superlive = Live2.get(superMeta);
					if(superlive != null)
					{
						superlive.addPermission(up);
					}
					aps.remove(i);
				}
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return UserPermissionList;
	}
	
	

	//取得渠道权限
	public long  addAllAdminPermission(AdminPermission per) {
		int upCou = 0;
		long id = 0;
		if(per.getId()==0)
		{
			per.setId(AppTools.getId());
			String hql = " insert into  operate_admin_permission(id,name,adminId,type,`show`,updateAdminId,updateTime,opType)"
					+ "values("+per.getId()+",'"+per.getName()+"',"+per.getAdminId()+","+per.getType()+","+per.getShow()+","+per.getUpdateAdminId()+",'"+per.getUpdateTime()+"',"+per.getOpType()+") ";
			try {
				upCou = this.Update(hql);
				id = per.getId();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else 
		{
			String hql = " update operate_admin_permission set name='"+per.getName()+"',adminId="+per.getAdminId()+",type="+per.getType()+",`show`="+per.getShow()+",updateAdminId="+per.getUpdateAdminId()+""
					+ ",updateTime='"+per.getUpdateTime()+"',opType="+per.getOpType()+" where id="+per.getId();
			try {
				upCou = this.Update(hql);
				id = per.getId();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		return id;
		
	}
	
	/**
	 * 按逻辑查询
	 * @param adminids
	 * @param jobj
	 * @param time
	 * @return
	 */
	public List<Map<String, String>> findSumFormDayOperateLogic(List<Long> adminids, JSONObject jobj,String time) {

		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);
		String where1 = where[0];

		if (adminids == null || adminids.size() == 0) {
			where1 += " and en.channelId > 0 ";

		} else if (adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String sql = " select  count(1) cou  from   (  select channelid  from   ( "
				+ " select c.id channelid    from operate_reportform_logic en , operate_channel c  " + where1
				+ " and en.channelid = c.id and en.channelid > 0 " + " ) a  group by a.channelid  )b   ";

		String cou = "";
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			cou = ordList.get(0).get("cou");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		String hql = " select  "
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( register) register ,  " + " sum( upload) upload ,  " + " sum( account) account ,  "  
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum  "
				+ " from operate_reportform_logic en  ";

		hql += where1;
		

		List<Map<String, String>> list = map_obj_logic(hql," / "+where[3]+"天",null,null);
		list.get(0).put("channel", cou+"个渠道");
		
		
		return list;

	}
	
	/**
	 * 按逻辑查询
	 * @param adminids
	 * @param jobj
	 * @param time
	 * @return
	 */
	public List<Map<String, String>> findAdminSumFormDayOperateLogic(List<Long> adminids, JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if (adminids == null || adminids.size() == 0) {
			where1 += " and en.channelId > 0 ";

		} else if (adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String sql = "  select adminid ,count(1) cou  from   ( "+
				"select en.adminid ,  en.proxyid   from operate_reportform_logic en  "+ where1 +"  group by en.adminid ,en.proxyid"+
				") b  group by b.adminid   ";

		Map<String, String> ad_pr = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				String adminid = ordMap.get("adminid");
				String cou = ordMap.get("cou");
				ad_pr.put(adminid, cou);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sql = " select adminid ,count(1) cou   from   (   select en.adminid , channel  from operate_reportform_logic en  " + where1
				+ "     group by  adminid , channel ) b " + " group by b.adminid  ";

		Map<String, String> ad_ch = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				String adminid = ordMap.get("adminid");
				String cou = ordMap.get("cou");
				ad_ch.put(adminid, cou);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String hql = " select  adminid, (select name from operate_admin b where  b.id = en.adminid) adminName,"
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( register) register ,  "  + " sum( upload) upload ,  " + " sum( account) account ,  "
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum  "
				+ " from operate_reportform_logic en "+where1+" group by adminid ";
		

		return map_obj_logic(hql," / "+where[3]+"天", ad_pr, ad_ch);

	}
	
	/**
	 * 按逻辑查询
	 * @param hql
	 * @param days
	 * @param map1
	 * @param map2
	 * @return
	 */
	public List<Map<String, String>> map_obj_logic(String hql,String days, Map<String, String> map1,
			Map<String, String> map2) {
		List<Map<String, String>> ordList = null;
		try {
			ordList = this.Query(hql);
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);
				
				if(ordMap.get("channel") != null)
				{ 
		    		Channel channel = Cache.getChannelCatche(ordMap.get("channel"));
		    		
		    		
		    		if(channel != null)
		    		{
		    			ordMap.put("channelName", channel.getChannelName());
		        		Admin admin = Cache.getAdminCatche(channel.getAdminId());
		        		if(admin != null)
		        		{
			    			ordMap.put("adminName", admin.getName());
			    		}
		    			ordMap.put("channel", channel.getChannel());
		    			
		        		String type = "";
		        		Dictionary dic = Cache.getDic(channel.getAttribute());
		        		if(dic != null)
		        			type+=dic.getName().substring(0,1)+" ";
		        		
		        		dic = Cache.getDic(channel.getType());
		        		if(dic != null)
		        			type+=dic.getName()+" ";
	
		        		dic = Cache.getDic(channel.getSubdivision());
		        		if(dic != null)
		        			type+=dic.getName()+" ";

		    			ordMap.put("channelType", type);
		    			
		    		}
	    	
				}
				
				
				
				long h5Click = 0;
				try {

					h5Click = Long.parseLong(ordMap.get("h5Click"));
				} catch (Exception e) {
					// TODO: handle exception
				} 

				long h5Register = 0;
				try {

					h5Register = Long.parseLong(ordMap.get("h5Register"));
				} catch (Exception e) {
					// TODO: handle exception
				}
 

				long activation = 0;
				try {

					activation = Long.parseLong(ordMap.get("activation"));
				} catch (Exception e) {
					// TODO: handle exception
				}
 
				long register = 0;
				try {

					register = Long.parseLong(ordMap.get("register"));
				} catch (Exception e) {
					// TODO: handle exception
				} 
				long upload = 0;
				try {

					upload = Long.parseLong(ordMap.get("upload"));
				} catch (Exception e) {
					// TODO: handle exception
				} 
				long account = 0;
				try {

					account = Long.parseLong(ordMap.get("account"));
				} catch (Exception e) {
					// TODO: handle exception
				} 
				long loan = 0;
				try {

					loan = Long.parseLong(ordMap.get("loan"));
				} catch (Exception e) {
					// TODO: handle exception
				} 
				long credit = 0;
				try {

					credit = Long.parseLong(ordMap.get("credit"));
				} catch (Exception e) {
					// TODO: handle exception
				} 

				long perCapitaCredit = 0;
				if (account > 0)
					perCapitaCredit = (credit / account);

				ordMap.put("perCapitaCredit", perCapitaCredit+"");

				long firstGetPer = 0;
				try {

					firstGetPer = Long.parseLong(ordMap.get("firstGetPer"));
				} catch (Exception e) {
					// TODO: handle exception
				}
 
				long firstGetSum = 0;
				try {

					firstGetSum = Long.parseLong(ordMap.get("firstGetSum"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				long firstPerCapitaCredit = 0;
				if (firstGetPer > 0)
					firstPerCapitaCredit = (firstGetSum / firstGetPer);

				ordMap.put("firstPerCapitaCredit", firstPerCapitaCredit+"");
				
 
				long channelSum = 0;
				try {

					channelSum = Long.parseLong(ordMap.get("channelSum"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				

				long secondGetPi = 0;
				try {

					secondGetPi = Long.parseLong(ordMap.get("secondGetPi"));
				} catch (Exception e) {
					// TODO: handle exception
				}
 
				long secondGetSum = 0;
				try {

					secondGetSum = Long.parseLong(ordMap.get("secondGetSum"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				long secondPerCapitaCredit = 0;
				if (secondGetPi > 0)
					secondPerCapitaCredit = (secondGetSum / secondGetPi);

				ordMap.put("secondPerCapitaCredit", secondPerCapitaCredit+"");
				
				long channelCapitaCredit = 0;
				if (loan != 0)
					channelCapitaCredit = channelSum / loan;

				ordMap.put("channelCapitaCredit", channelCapitaCredit+"");
				
				 String datestr = ordMap.get("date");
				 if(datestr == null)
					 datestr = "";
				ordMap.put("date",datestr+days);
				
//				double income = 0;
//				try {
//					String istr = ordMap.get("income");
//					income = Double.parseDouble(istr);
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//
//				double cost = 0;
//				try {
//
//					cost = Double.parseDouble(ordMap.get("cost"));
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//
//				double cost2 = 0;
//				try {
//
//					cost2 = Double.parseDouble(ordMap.get("cost2"));
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//				
//				double grossProfit = income - cost;
//				if(cost2 != 0)
//					grossProfit = income - cost2;
//				double grossProfitRate = 0;
//				String grossProfitRateStr = "";
//				if(income != 0)
//				{
//					grossProfitRate = grossProfit/income*100;
//					
//					grossProfitRateStr = grossProfitRate+"";
//					
//					if(grossProfitRateStr.indexOf(".") != -1 && grossProfitRateStr.length() - grossProfitRateStr.indexOf(".") > 4)
//					{
//						grossProfitRateStr = grossProfitRateStr.substring(0, grossProfitRateStr.indexOf(".")+4);
//					}
//				}
//				
//				
//				ordMap.put("grossProfit", grossProfit+"");
//				ordMap.put("grossProfitRate", grossProfitRateStr+"%");

				String activationConversion = bl(activation, register);
				String uploadConversion = bl(upload, activation);
				String accountConversion = bl(account, upload);
				String loanConversion = bl(loan, account);
				
				ordMap.put("activationConversion", activationConversion+"%");
				ordMap.put("uploadConversion", uploadConversion+"%");
				ordMap.put("accountConversion", accountConversion+"%");
				ordMap.put("loanConversion", loanConversion+"%");

				if (map1 != null) { 
					String val1 = ordMap.get("adminId");
					if (val1 == null)
						val1 = ordMap.get("adminId");
					String val2 = map1.get(val1); 
					ordMap.put("channelName", val2 + "个公司");
					String val3 = map2.get(val1);
					ordMap.put("channel", val3 + "个渠道");
 

				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ordList;
	}
	

	public List<Map<String, String>> getFirstPage( JSONObject jobj,String time) {

		
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);
		String where1 = where[0];
		where1+= "  and channelId > 0  ";
		String hql = " select date,   sum( register) register ,  " + " sum( outRegister) outRegister ,  " 
				+ " sum( upload) upload ,  "+ " sum( outUpload) outUpload ,  " + " sum( account) account ,  " + " sum( outAccount) outAccount ,  " 
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(outFirstGetPer) outFirstGetPer ,  " 
				+ " sum(en.outFirstGetSum) outFirstGetSum    "
				+ " from operate_reportform en  ";

		hql += where1  +" group by date " ;
		
		List<Map<String, String>> ordList = null;
		try {
			ordList = this.Query(hql);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ordList;

	}
	
	
	
	
	
	
	public long findFormCouByDay(JSONObject jobj, String dateType,String time) {
		String[] where = DaoWhere.getFromWhereForAdmin(jobj,time);
	 	String hql = "select count(1) cou from ( select id from operate_reportform a  " + where[0] + where[4] +")a";
		
		long channelSum = 0;
		try {

			List<Map<String, String>> ordList = this.Query(hql);
			String cou = ordList.get(0).get("cou");
			channelSum = Long.parseLong(cou);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return channelSum;
	}
	 

	public List<Map<String, String>> findFormByAdmin(JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);
		String where1 = where[0];
		JSONArray adminIdList = jobj.getJSONArray("adminIdList");
		where1 += " and a.channelId > 0 ";
		
		if (adminIdList != null && adminIdList.size() > 0) {
			where1 += " and a.adminid in ( "; 
			for (int i = 0;i < adminIdList.size();i++) {
				if (i == 0)
				{
					where1 += adminIdList.get(i).toString();
				}
				else
				{
					where1 += "," + adminIdList.get(i).toString();
				}
			}
			where1 += ")";
			
		}
		String group = DaoWhere.getFromChannelGroup(jobj);
 		String hql = "select (select name from operate_admin where id = a.adminid) adminName,adminid,app"+group
 				+ ", sum( outRegister) register ,  " 
				+ " sum(outUpload) upload ,  "
 				+ " sum(outAccount) account ,  " 
				+ " sum(outFirstGetPer) firstGetPer ,  " 
				+ " sum(outFirstGetSum) firstGetSum "
 				+ " from operate_reportform a "+where1+" group by appid,app"+group ;
		
	
		List<Map<String, String>> ordList = null;
		try {
			ordList = this.Query(hql);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return ordList;
	}


	public long findRegisterCouFormByDate(JSONObject jobj) {
		String  where = DaoWhere.getFromWhereForHjByDate(jobj);
		 
		where += " and a.channelId > 0 ";
		 
 		String hql = "select "
 				+ " sum( outRegister) register "
 				+ " from operate_reportform a "+where+" " ;
		
		List<Map<String, String>> ordList = null;
		long registerCou = 0;
		try {
			ordList = this.Query(hql);
			if(ordList != null && ordList.size() > 0)
			{
				registerCou = Long.parseLong(ordList.get(0).get("register"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return registerCou;
	}
	
	public List<Map<String, String>> findFormByAll(JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);
		String where1 = where[0];
		JSONArray adminIdList = jobj.getJSONArray("adminIdList");
		where1 += " and a.channelId > 0 ";
		 
 		String hql = "select   "
 				+ " sum( outRegister) register ,  " 
				+ " sum(outUpload) upload ,  "
 				+ " sum(outAccount) account ,  " 
				+ " sum(outFirstGetPer) firstGetPer ,  " 
				+ " sum(outFirstGetSum) firstGetSum  "
 				+ " from operate_reportform a "+where1+" " ;
		
		List<Map<String, String>> ordList = null;
		try {
			ordList = this.Query(hql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ordList;
	}
	
	/**
	 * 按月统计负责人kpi
	 * @param jobj
	 * @param ids
	 * @return
	 */
	public List<Map<String, String>> findkpi(JSONObject jobj,List<Long> ids) {
		List<Map<String, String>> ordList = null;
		
		String where2 = "";
		String maxDate = jobj.getString("maxDate");
	    String minDate = jobj.getString("minDate");
	    String where1 ="where month >= '" + minDate + "' and month <= '" + maxDate + "'";
	    if(minDate.equals(maxDate)) {
	    		where1 = "where month = '" + minDate + "'";
	    }
		
		
		if (ids != null && ids.size() > 0) {
			where2 += " and adminid in ( "; 
			for (int i = 0;i < ids.size();i++) {
				if (i == 0)
				{
					where2 += ids.get(i).toString();
				}
				else
				{
					where2 += "," + ids.get(i).toString();
				}
			}
			where2 += ")";
		}
		
		String sql = "select  b.adminid adminid,(select ad.name from operate_admin ad where ad.id = b.adminId) name,b.month,registerkpi from operate_business_kpi op , "
						+ " (select adminid,month,max(updateTime)updatetime from operate_business_kpi "+ where1 + where2 +"  group by adminid,month)b"
						+ " where op.adminid = b.adminid and op.updatetime = b.updatetime";
		
		try {
			ordList = this.Query(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return ordList;
	}
	
	/**
	 * 查找该负责人的kpi修改记录  adminid只有一个
	 * @param jobj
	 * @return
	 */
	public List<Map<String, String>> findkpidetail(JSONObject jobj) {
		List<Map<String, String>> ordList = null;
		String adminid = jobj.getString("adminid");
	    String month = jobj.getString("month");
	    String where = " where adminid = " + adminid + " and month = '" + month +"'";
	    		String sql = "select (select ad.name from operate_admin ad where ad.id = op.adminId) name ,registerkpi,"
	    				+ "updateTime,(select ad.name from operate_admin ad where ad.id = op.updateadminid) updatename "
	    				+ " from operate_business_kpi op " + where;
	    		try {
	    			ordList = this.Query(sql);
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    		
	    		
	    		return ordList;
	}

	/**
	 * 按负责人总计kpi  只是第一行
	 * @param jobj
	 * @return
	 */
	public List<Map<String, String>> findkpiAll(JSONObject jobj, List<Long> ids) {

		List<Map<String, String>> ordList = null;

	    String minDate = jobj.getString("minDate");
	    String maxDate = jobj.getString("maxDate");
	    
	    String where1 = " where month >= '" + minDate + "' and month <= '" + maxDate + "'";
	    String date = minDate + "--" + maxDate;
	    if(minDate.equals(maxDate)) {
	    		date	 = minDate;
	    		where1 = "where month = '" + minDate +  "'";
	    }
	    
	    String where2 = "";
	    
	    
	    if (ids != null && ids.size() > 0) {
			where2 += " and adminid in ( "; 
			for (int i = 0;i < ids.size();i++) {
				if (i == 0)
				{
					where2 += ids.get(i).toString();
				}
				else
				{
					where2 += "," + ids.get(i).toString();
				}
			}
			where2 += ")";
		}
	    
	    
	    String sql = "select register,kpisum,register/kpisum*100 bit from "
	    		+ "( select sum(outregister) register from operate_reportform " + where1 + where2
	    		+") a, ( select sum(registerkpi) kpisum from operate_business_kpi " + where1 + where2
	    		+ " and updateTime in (select max(updateTime)updatetime from db_operate.operate_business_kpi group by adminid,month)) b";
	  

	    try {
			ordList = this.Query(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	   
	    if(ordList.size() > 0) {
	    		ordList.get(0).put("month", "总计");
	    		ordList.get(0).put("name", "-");
	    }
	    	
	    		return ordList;
	}
	
	/**
	 * 按月统计kpi
	 * @param jobj
	 * @param ids
	 * @return
	 */
	public List<Map<String, String>> findkpibymonth(JSONObject jobj, List<Long> ids) {
		List<Map<String, String>> ordList = null;

	    String minDate = jobj.getString("minDate");
	    String maxDate = jobj.getString("maxDate");
	    
	    String where1 = " where month >= '" + minDate + "' and month <= '" + maxDate + "'";
	    
	    if(minDate.equals(maxDate)) {
	    	
	    		where1 = "where month = '" + minDate +  "'";
	    }
	    
	    String where2 = "";
	    
	    
	    if (ids != null && ids.size() > 0) {
			where2 += " and adminid in ( "; 
			for (int i = 0;i < ids.size();i++) {
				if (i == 0)
				{
					where2 += ids.get(i).toString();
				}
				else
				{
					where2 += "," + ids.get(i).toString();
				}
			}
			where2 += ")";
		}
	    
	  
	    String sql = "select a.month month,register,kpisum,register/kpisum*100 bit from "
	    		+ "( select month,sum(outregister) register from operate_reportform " + where1 + where2
	    		+ " group by month) a, ( select month,sum(registerkpi) kpisum from operate_business_kpi " + where1 + where2
	    		+ " and updateTime in (select max(updateTime)updatetime from db_operate.operate_business_kpi group by adminid,month) group by month) b"
	    		+ " where a.month = b.month";
	    		
	    		try {
	    			ordList = this.Query(sql);
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    		return ordList;
	}
	
	/**
	 * 按负责人查询
	 * @param jobj
	 * @param ids
	 * @return
	 */
	public List<Map<String, String>> findkpibyAdmins(JSONObject jobj, List<Long> ids) {
		List<Map<String, String>> ordList = null;

	    String minDate = jobj.getString("minDate");
	    String maxDate = jobj.getString("maxDate");
	    
	    String where1 = " where month >= '" + minDate + "' and month <= '" + maxDate + "'";
	    String date = minDate + "--" + maxDate;
	    if(minDate.equals(maxDate)) {
	    		date	 = minDate;
	    		where1 = "where month = '" + minDate +  "'";
	    }
	    
	    String where2 = "";
	    
	    
	    if (ids != null && ids.size() > 0) {
			where2 += " and adminid in ( "; 
			for (int i = 0;i < ids.size();i++) {
				if (i == 0)
				{
					where2 += ids.get(i).toString();
				}
				else
				{
					where2 += "," + ids.get(i).toString();
				}
			}
			where2 += ")";
		}
	    
	  
	    String sql = "select a.adminid adminid,(select name from operate_admin ad where ad.id = a.adminid)name,register,kpisum,register/kpisum*100 bit from "
	    		+ "( select adminid,sum(outregister) register from operate_reportform " + where1 + where2
	    		+ " group by adminid) a, ( select adminid,sum(registerkpi) kpisum from operate_business_kpi " + where1 + where2
	    		+ " and updateTime in (select max(updateTime)updatetime from db_operate.operate_business_kpi group by adminid,month) group by adminid) b " 
	    		+ " where a.adminid = b.adminid";
	    		
	 
	    		try {
	    			ordList = this.Query(sql);
	    		
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    		return ordList;
	}
	
	/**
	 * 按月按负责人查询
	 * @param jobj
	 * @param ids
	 * @return
	 */
	public List<Map<String, String>> findkpibyall(JSONObject jobj, List<Long> ids) {
		List<Map<String, String>> ordList = null;

	    String minDate = jobj.getString("minDate");
	    String maxDate = jobj.getString("maxDate");
	    
	    String where1 = " where month >= '" + minDate + "' and month <= '" + maxDate + "'";
	    String date = minDate + "--" + maxDate;
	    if(minDate.equals(maxDate)) {
	    		date	 = minDate;
	    		where1 = "where month = '" + minDate +  "'";
	    }
	    
	    String where2 = "";
	    
	    
	    if (ids != null && ids.size() > 0) {
			where2 += " and adminid in ( "; 
			for (int i = 0;i < ids.size();i++) {
				if (i == 0)
				{
					where2 += ids.get(i).toString();
				}
				else
				{
					where2 += "," + ids.get(i).toString();
				}
			}
			where2 += ")";
		}
	    
	  
	    String sql = "select a.month month,a.adminid adminid,(select name from operate_admin ad where ad.id = a.adminid)name,register,kpisum,register/kpisum*100 bit from "
	    		+ "( select month,adminid,sum(outregister) register from operate_reportform " + where1 + where2
	    		+ " group by month,adminid) a, ( select month,adminid,sum(registerkpi) kpisum from operate_business_kpi " + where1 + where2
	    		+ " and updateTime in (select max(updateTime)updatetime from db_operate.operate_business_kpi group by adminid,month) group by month,adminid) b " 
	    		+ " where a.adminid = b.adminid and a.month = b.month";
	    		
	 
	    		try {
	    			ordList = this.Query(sql);
	    		
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    		return ordList;
	}
	
	
	public List<Map<String, String>> findFormByMainChannel(JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);
		String where1 = where[0];
		JSONArray adminIdList = jobj.getJSONArray("adminIdList");
		where1 += " and a.channelId > 0 ";

		String group = DaoWhere.getFromChannelGroup(jobj);
 		String hql = "select (select name from operate_admin where id = a.adminid) adminName "+group
 				+ ", (select channelName from operate_channel where channel = a.mainChannel) mainChannelName,mainChannel,appid,app,channelAttribute,channelType,subdivision "
 				+ ", sum( outRegister) register ,  " 
				+ " sum(outUpload) upload ,  "
 				+ " sum(outAccount) account ,  " 
				+ " sum(outFirstGetPer) firstGetPer ,  " 
				+ " sum(outFirstGetSum) firstGetSum  "
 				+ " from operate_reportform a "+where1+" group by  mainChannel,appid,app,channelAttribute,channelType,subdivision"+group ;
	
	
		List<Map<String, String>> ordList = null;
		try {
			ordList = this.Query(hql);
			
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);

				String type = "";
        		Dictionary dic = Cache.getDic(Long.parseLong(ordMap.get("channelAttribute")));
        		if(dic != null)
        			type+=dic.getName().substring(0,1)+" ";
        		
        		dic = Cache.getDic(Long.parseLong(ordMap.get("channelType")));
        		if(dic != null)
        			type+=dic.getName()+" ";

        		dic = Cache.getDic(Long.parseLong(ordMap.get("subdivision")));
        		if(dic != null)
        			type+=dic.getName()+" ";
        		ordMap.put("channelTypeName", type);
        		
	    	
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return ordList;
	}

	

	public List<Map<String, String>> findFormByChannel(JSONObject jobj,String time) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);
		String where1 = where[0];
		JSONArray adminIdList = jobj.getJSONArray("adminIdList");
		where1 += " and a.channelId > 0 ";

		String group = DaoWhere.getFromChannelGroup(jobj);
 		String hql = "select (select name from operate_admin where id = a.adminid) adminName "+group
 				+ ", (select channelName from operate_channel where channel = a.channel) channelName,channel,appid,app,channelAttribute,channelType,subdivision "
 				+ ", sum( outRegister) register ,  " 
				+ " sum(outUpload) upload ,  "
 				+ " sum(outAccount) account ,  " 
				+ " sum(outFirstGetPer) firstGetPer ,  " 
				+ " sum(outFirstGetSum) firstGetSum  "
 				+ " from operate_reportform a "+where1+" group by channel,appid,app,channelAttribute,channelType,subdivision"+group ;
	
	
		List<Map<String, String>> ordList = null;
		try {
			ordList = this.Query(hql);
			
			for (int i = 0; i < ordList.size(); i++) {
				Map<String, String> ordMap = ordList.get(i);

				String type = "";
        		Dictionary dic = Cache.getDic(Long.parseLong(ordMap.get("channelAttribute")));
        		if(dic != null)
        			type+=dic.getName().substring(0,1)+" ";
        		
        		dic = Cache.getDic(Long.parseLong(ordMap.get("channelType")));
        		if(dic != null)
        			type+=dic.getName()+" ";

        		dic = Cache.getDic(Long.parseLong(ordMap.get("subdivision")));
        		if(dic != null)
        			type+=dic.getName()+" ";
        		ordMap.put("channelTypeName", type);
        		
	    	
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return ordList;
	}

	
	
	
	
	
	
	
	
	/**
	 * 按逻辑查询
	 * @param channelids
	 * @param adminids
	 * @param jobj
	 * @param dateType
	 * @param time
	 * @return
	 */
	public long findFormCouLogic(List<Long> channelids,List<Long> adminids, JSONObject jobj, String dateType,String time) {


		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,time);

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and a.channelId > 0 ";
		}
		else if (adminids != null && adminids.size() > 0) {
			where1 += " and a.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and a.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}

		String hql = " select count(1) cou from operate_reportform_logic a " + where1 +" ";
	 	if(!dateType.equals("1"))
	 		hql = "select count(1) cou from ( select channelid from operate_reportform_logic a  " + where1 +" group by channel,month )a";
		
		
		long channelSum = 0;
		try {

			List<Map<String, String>> ordList = this.Query(hql);
			String cou = ordList.get(0).get("cou");
			channelSum = Long.parseLong(cou);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return channelSum;
	}

	/**
	 * 按逻辑查询
	 * @param channelids
	 * @param adminids
	 * @param jobj
	 * @return
	 */
	
	public List<Map<String, String>> findFormOperateLogic(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}


		String hql = " select en.* from operate_reportform_logic en " + where1 + "  order by date  limit " + where[1] + "," + where[2];

		return map_obj_logic(hql,"",null,null);
	}

	/**
	 * 按逻辑查询
	 * @param channelids
	 * @param adminids
	 * @param jobj
	 * @return
	 */
	public List<Map<String, String>>  findFormMonthOperateLogic(List<Long> channelids,List<Long> adminids, JSONObject jobj) {
		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,"");

		String where1 = where[0];

		if ((channelids == null || channelids.size() == 0) && (adminids == null || adminids.size() == 0)) {
			where1 += " and en.channelId > 0 ";

		}else if (adminids != null && adminids.size() > 0) {
			where1 += " and en.adminid in ( ";
			int i = 0;
			for (long id : adminids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}
		else if (channelids != null && channelids.size() > 0) {
			where1 += " and en.channelId in ( ";
			int i = 0;
			for (long id : channelids) {
				if (i == 0)
					where1 += id;
				else
					where1 += "," + id;
				i++;
			}
			where1 += ")";
		}


		String hql = " select channel,trim(month) date,"
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( register) register ,  " + " sum( upload) upload , " + " sum( account) account ,  "  
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum ,  "
				+ " from operate_reportform_logic en " + where1 + " group by channel,month limit " + where[1] + "," + where[2];

		return map_obj_logic(hql," / "+where[3]+"天",null,null);
	}
	
	
	
	public String getFinanceId(String name,String type)
	{
		String finaceId="";
		String sql = "select finaceId from operate_finance_mapping where finaceName='"+name+"' and idType='"+type+"'   ";
		List<Map<String, String>> finaceIds;
		try {
			finaceIds = this.Query(sql);
			if(finaceIds != null && finaceIds.size() > 0)
			{
				 finaceId=finaceIds.get(0).get("finaceId");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return finaceId;
	}
	

	public List<Map<String, String>> getChannelFinance(String date )
	{
		String sql = " select a.*  ,(select company from operate_proxy c where  c.id=a.proxyid) supplier ,(select supplier_id from operate_proxy c where  c.id=a.proxyid) supplier_id ,   (select   name  from operate_dictionary  c where c.id = a.appid)    app,  "
				+ "   if(  b.pay=38,1,if(b.pay=37,2,null)    ) pay,     (select   company  from operate_balance_account c where c.id = b.companyId)    invoice_title from  "
				+ " (SELECT mainChannelName,appid,proxyid , sum(income) income ,sum(cost)cost ,sum(  if(cost2=0,cost,cost2) )cost2 , "
				+ " (select name from operate_admin d where d.id=adminid) adminName "
				+ " FROM db_operate.operate_reportform  where showtime != '9999-01-01 11:00:00' and  date = '"+date+"' "
				+ " group by  mainChannelName,adminid,appid,proxyid) a "
				+ " left join   operate_pay_company  b  on a.proxyid = b.proxyid and a.appid= b.appid  ";
		List<Map<String, String>> ChannelFinance=null;
		try {
			ChannelFinance = this.Query(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ChannelFinance;
	}
	

	public List<Map<String, String>> getChannelFinanceIncome(String date )
	{
		String sql = "select (select   name  from operate_dictionary  c where c.id = a.appid)    app,(select   other3  from operate_dictionary  c where c.id = a.appid) customer\n" + 
				",appid    ,invoice_title, sum(income) income from \n" + 
				"(\n" + 
				" select a.income ,\n" + 
				" a.appid,\n" + 
				" (select   company  from operate_balance_account c where c.id = b.companyId)    invoice_title \n" + 
				" from  \n" + 
				" (\n" + 
				"	SELECT  appid, sum(income) income  ,proxyid\n" + 
				"	FROM db_operate.operate_reportform  where  date = '"+date+"'  group by  appid ,proxyid\n" + 
				" ) a left join   operate_pay_company  b  on a.proxyid = b.proxyid and a.appid= b.appid  \n" + 
				") a group by appid ";
		List<Map<String, String>> ChannelFinance=null;
		try {
			ChannelFinance = this.Query(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ChannelFinance;
	}


	public List<Map<String, String>> getChannelFinanceByMonth(String month,String mainChannel,String appId )
	{
		String where = "";
		if(!StringUtils.isStrEmpty(mainChannel))
			where = " and mainChannel='"+mainChannel+"' and appId="+appId+" ";
		String sql = " select a.*  ,(select company from operate_proxy c where  c.id=a.proxyid) supplier ,(select supplier_id from operate_proxy c where  c.id=a.proxyid) supplier_id , "
				+ "  (select   name  from operate_dictionary  c where c.id = a.appid)    app,  "
				+ "   if(  b.pay=38,1,if(b.pay=37,2,null)    ) pay,     (select   company  from operate_balance_account c where c.id = b.companyId)    invoice_title,companyId from  "
				+ " (SELECT mainChannel,mainChannelName,appid,proxyid,adminid , sum(income) income ,sum(cost)cost ,sum(  if(cost2=0,cost,cost2) )cost2 ,"
				+ " (select name from operate_admin d where d.id=adminid) adminName  FROM db_operate.operate_reportform "
				+ " where  month = '"+month+"' "+where+" "
				+ " group by  mainChannelName,adminid,appid,proxyid) a "
				+ " left join   operate_pay_company  b  on a.proxyid = b.proxyid and a.appid= b.appid  ";
		List<Map<String, String>> ChannelFinance=null;
		try {
			ChannelFinance = this.Query(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ChannelFinance;
	}
	
	public List<Map<String, String>> getpartnerBillListByMonth(String month){
		String sql = "select a.appId appId,c.name app,a.companyId companyId,b.company company,b.ourCompanyId ourCompanyId,d.company ourCompany, "
					+ "cooperationContent,b.name cooperationType,month,sum(cost)cost from " 
					+ " (SELECT *,left(dateNew,7)month FROM operate_costing)a," 
					+ " (select operate_partner.*,name from operate_partner, operate_dictionary where operate_partner.cooperationType = operate_dictionary.id)b," 
					+ " operate_dictionary c,operate_balance_account d " 
					+ " where a.appId = c.id and a.companyId = b.id and a.month = '" + month 
					+ "' and d.id = b.ourCompanyId group by app,company,cooperationContent";
		
		List<Map<String, String>> partnerBillList=null;
		try {
			partnerBillList = this.Query(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return partnerBillList;
		
	}

	public void saveSupplier_id(String supplier_id,String proxyid)
	{
		String sql = " update operate_proxy set supplier_id='"+supplier_id+"' where id="+proxyid+";   ";
		try {
			this.Update(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	


	public List<Map<String,String>> getSupplier(String proxyid)
	{
		String sql = " select * from operate_proxy where id="+proxyid+";   ";
		List<Map<String,String>> supplierlist = null;
		try {
			supplierlist = this.Query(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return supplierlist;
	}
	
	

	public void saveFinanceLog( String type,String proxyid,String msg)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date());
		String sql = " insert into operate_finance_log (date,type,proxyid,msg)values('"+date+"','"+type+"',"+proxyid+",'"+msg+"');   ";
		try {
			this.Update(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

 
	public void saveBill(String product,String proxyid,String appid,String payCompany,String payCompanyid,String adminId,String proxyName,String mainChannelName,String mainChannel,
			String month,String cost)
	{
		String sql = " insert into operate_bill (  product,  proxyid,  appid,  payCompany,payCompanyid,  adminId,  proxyName,  mainChannelName, mainChannel, " + 
				" month,  cost,  createTime) values('"+product+"',  "+proxyid+",  "+appid+",  '"+payCompany+"',  '"+payCompanyid+"',  "+adminId+",  '"+proxyName+"', "
						+ " '"+mainChannelName+"','"+mainChannel+"',  '"+month+"', "+ cost+ ")";
		
		try {
			this.Update(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void savePartnerBill(String appId,String app,String companyId,String company,String ourCompanyId,String ourCompany,String cooperationContent,String cooperationType,String month,String cost){
		String sql = " insert into operate_partnerbill ( appId,app,  companyId,company,  ourCompanyId,  ourCompany, cooperationContext,cooperationType," + 
				" month, cost) values("+appId+",  '"+app+"',  "+companyId+",  '"+company+"',  "+ourCompanyId+",  '"+ourCompany+"',  '"+cooperationContent+"', '"
						+ cooperationType + "', '"+month+"'," + cost + " )";
		
		try {
			this.Update(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void updateBill(String id,String product,String proxyid,String appid,String payCompany,String payCompanyid,String adminId,String proxyName,String mainChannelName,String mainChannel,
			String month,String cost,String createTime )
	{
//		String sql = " insert into operate_bill (  product,  proxyid,  appid,  payCompany,payCompanyid,  adminId,  proxyName,  mainChannelName, mainChannel, " + 
//				" month,  cost,  createTime) values('"+product+"',  "+proxyid+",  "+appid+",  '"+payCompany+"',  '"+payCompanyid+"',  "+adminId+",  '"+proxyName+"', "
//						+ " '"+mainChannelName+"','"+mainChannel+"',  '"+month+"', "+ cost+",  '"+createTime+"' )";
		
		String sql = "update operate_bill set product='"+product+"',  proxyid="+proxyid+",  appid="+appid+",  payCompany='"+payCompany+"',payCompanyid='"+payCompanyid+"', "
				+ " adminId= "+adminId+",  proxyName='"+proxyName+"',  mainChannelName='"+mainChannelName+"', mainChannel='"+mainChannel+"',  month='"+month+"',  cost="+ cost+","
				+ "  createTime='"+createTime+"' where id="+id;
		
		
		try {
			this.Update(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Map<String,String>> getBill(JSONObject jobj,String adminid)
	{
		String[] where = DaoWhere.getBillWhere(jobj, 1);
		String sql = " SELECT *,(select name from operate_admin a where a.id = b.adminid) adminname,"
				+ "  (select  adminid from  operate_bill_log where   id = (select max(id ) from  operate_bill_log  where   billid=b.id )       )  logadminid,0 isUpdate"+
		" FROM db_operate.operate_bill b    "+where[0];
		List<Map<String,String>> billlist = null;
		try {
			billlist = this.Query(sql);
			sql = " SELECT * FROM db_operate.operate_finance_step  b where admin="+adminid+"     " ;
			List<Map<String,String>> steplist = this.Query(sql);
			
			String adminstep = "0";
			if(steplist != null && steplist.size()>0)
				adminstep = steplist.get(0).get("order");
			
			for(int i = 0;i < billlist.size();i++)
			{
				String step = billlist.get(i).get("step");
				if(step.equals("2"))
				{
					String adminId = billlist.get(i).get("adminId");
					if(adminId.equals(adminid))
					{
						billlist.get(i).put("isUpdate", "1");
					}
					
				}
				else if(step.equals("3"))
				{
					if(adminstep.equals(step))
					{
						String logadminid = billlist.get(i).get("logadminid");
						if(!logadminid.equals(adminid))
						{
							billlist.get(i).put("isUpdate", "1");
						}
					}
					
				}
				else
				{
					if(adminstep.equals(step))
					{
						billlist.get(i).put("isUpdate", "1");
					}
					
				}
				
				
				
			}
			
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return billlist;
	}

	public int hasBillStep( String adminid,int order)
	{
		String sql = " select 1 from operate_finance_step a where  a.order="+order+" and admin = "+adminid;
		List<Map<String,String>> billlist = null;
		int isHas = 0;
		try {
			billlist = this.Query(sql);
			if(billlist != null && billlist.size() > 0)
				isHas = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isHas;
	}

	public List<Map<String,String>> getBillById(String id)
	{
		String sql = "SELECT * FROM db_operate.operate_bill b where id = "+id+" ";
		List<Map<String,String>> billlist = null;
		try {
			billlist = this.Query(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return billlist;
	}
	

	public void UpdatetBillStatus(String billid,String adminid,String status,int oldStep,int newStep,int score,int billStatus)
	{ 
		String sql = "update operate_bill set step="+newStep+",score="+score+",status="+billStatus+"  where id = "+billid+"  ";
		try { 
			this.Update(sql);
			
			sql = "insert into operate_bill_log(billid,adminid,step,status,date) values("+billid+","+adminid+","+oldStep+","+status+",NOW())";
			this.Update(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	public List<Map<String,String>> getBillParameter(JSONObject jobj)
	{
		String[] where = DaoWhere.getBillWhere(jobj, 1);
		String sql = "SELECT mainChannelName,proxyName,adminid,(select name from operate_admin a where a.id = b.adminid) adminname FROM db_operate.operate_bill b "+where[0];
		List<Map<String,String>> billlist = null;
		try {
			billlist = this.Query(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return billlist;
	}
	
	



	public List<Map<String,String>> getBillDetail(String month,String proxyid,String appid)
	{
//		String sql = "select channelName,channel ,appid, sum( if(cost2=0,cost,cost2) )cost2   ,max(date) maxdate,min(date) mindate " + 
//				",(select rewardId from operate_channel a where a.channel = b.channel  and appid="+appid+"  ) rewardId,sum(outFirstGetPer)outFirstGetPer" + 
//				", sum(outRegister)outRegister, sum(outFirstGetSum)outFirstGetSum, sum(outAccount)outAccount, sum(outUpload)outUpload" + 
//				" from  operate_reportform b  where   month = '"+month+"'  and  proxyid="+proxyid+" and appid="+appid+"  group by channelName,channel,month,appid";
		
		String sql = " select channelName,channel ,appid, sum(  cost2  )cost2   ,max(date) maxdate,min(date) mindate  , rewardId , " + 
				"     sum(outFirstGetPer)outFirstGetPer  " + 
				"  , sum(outRegister)outRegister,  " + 
				"  sum(outFirstGetSum)outFirstGetSum, " + 
				"  sum(outAccount)outAccount, " + 
				"  sum(outUpload)outUpload  " + 
				"  from  ( " + 
				"  select channelName,channel ,appid, " + 
				" date, " + 
				" if(cost2=0,cost,cost2) cost2, " + 
				" outUpload,outFirstGetPer,outRegister,outFirstGetSum,outAccount, " + 
				" (select  max(id)   from  operate_reward a  where a.channelid = b.channelid  and b.date >= FROM_UNIXTIME(a.date/1000,'%Y-%m-%d')        ) rewardId " + 
				"  from  operate_reportform b  where   month = '"+month+"'  and  proxyid="+proxyid+" and appid="+appid+"  " + 
				"  )  a  group by  channelName,channel,rewardId ,appid ";
		
		
		List<Map<String,String>> billlist = null;
		try {
			billlist = this.Query(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return billlist;
	}

	
	
	

	public List<Map<String,String>> getReward(String id)
	{
		String sql = "select  * from operate_reward where   id = "+id+"  ";
		List<Map<String,String>> rewardlist = null;
		try {
			rewardlist = this.Query(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rewardlist;
	}
	
	public List<Map<String,String>> getBillStep(String billid)
	{
		String sql = "select a.order,  " + 
				"	if(admin != 0,admin ,(select adminid from operate_bill where   id = "+billid+" )) adminId, " + 
				"	if(admin != 0,(select  Name from operate_admin d where d.id = admin ) " + 
				"	,(select  Name from operate_admin d where d.id = (select adminid from operate_bill where   id = "+billid+" ) )) adminName, " + 
				"	a.display,b.billid,date , (select  Name from operate_dictionary d where d.id = b.status )   status  " + 
				"	from operate_finance_step a left join (" + 
				" select * from    operate_bill_log b where id in(" + 
				"select  max(id) id   from operate_bill_log b where b.billid = "+billid+" group by step,adminid)" + 
				" ) b on a.order = b.step   and (a.admin=b.adminid || ( b.step=2 ) ) and b.billid = "+billid+" left join operate_bill c on b.billid = c.id ";
		List<Map<String,String>> billSteps = null;
		try {
			billSteps = this.Query(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return billSteps;
	}


	/**
	 * 按条件查找合作方详情
	 * @param jobj
	 * @return
	 */
	public List<Map<String, String>> findPartnerDetail(JSONObject jobj) {
		List<Map<String, String>> ordList = null;

		String company = jobj.getString("company");
    	String cooperationType = jobj.getString("cooperationType");
    	String cooperationContent = jobj.getString("cooperationContent");
    	
    	String where = "where 1=1";
    	if(company != null && !"".equals(company)) {
    		where += " and company = '" + company + "'";
    	}
    	if(cooperationType != null && !"".equals(cooperationType)) {
    		where += " and cooperationType = " + cooperationType;
    	}
    	if(cooperationContent != null && !"".equals(cooperationContent)) {
    		where += " and cooperationContent like " + "'%"+cooperationContent+"%'";
    	}
   
	    String sql = "select b.* , name , ourCompany , taxpayerNo , ourAddress ,  ourPhone ,  ourAccountNo , ourBank , ourInvoiceType"
	    				+ " from operate_dictionary dic, (select * from operate_partner " + where + " ) b , "
	    				+ "(select operate_balance_account.id companyId , company ourCompany,taxpayerNo,address ourAddress,phone ourPhone ,accountNo ourAccountNo ,"
	    				+ "bank ourBank ,name ourInvoiceType from  operate_balance_account, operate_dictionary where operate_balance_account.invoiceTypeId = operate_dictionary.id)c " 
	    				+ " where dic.id = b.cooperationType and b.ourCompanyId = c.companyId ";
    	  
	    try {
			ordList = this.Query(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	   
	    	
	    		return ordList;
	}
	
	
	/**
	 * 获取我方公司详细信息
	 * @param jobj
	 * @return
	 */
	public List<Map<String, String>> findBalanceAccountDetail(JSONObject jobj) {
		List<Map<String, String>> ordList = null;

		String id = jobj.getString("ourCompanyId");
    	
	    String sql = "select com.*,name from  operate_balance_account com,operate_dictionary od "
	    				+ " where com.invoiceTypeId = od.id and com.id = " + id;
	    		
	    try {
			ordList = this.Query(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	   
	    	
	    	return ordList;
	}
	
	/**
	 * 按条件查找运营成本数据详情
	 * @param jobj
	 * @return
	 */
	public List<Map<String, String>> findCostDetail(JSONObject jobj) {
		List<Map<String, String>> ordList = null;

		String company = jobj.getString("company");
    	String appId = jobj.getString("appId");
    	String cooperationContext = jobj.getString("cooperationContext");
    	String minDate = jobj.getString("minDate");
    	String maxDate = jobj.getString("maxDate");
    	
    	String where = "where 1=1";
    	if(company != null && !"".equals(company)) {
    		where += " and company = '" + company + "'";
    	}
    	if(appId != null && !"".equals(appId)) {
    		where += " and appId = " + appId;
    	}
    	if(cooperationContext != null && !"".equals(cooperationContext)) {
    		where += " and cooperationContent like " + "'%"+cooperationContext+"%'";
    	}
    	if(minDate != null && !"".equals(minDate) && maxDate != null && !"".equals(maxDate)) {
    		where += " and createTime >= '" + minDate + "' and createTime <= '" + maxDate + "'";
    	}
    	
	    
	    String sql = "select b.*,name,company from operate_dictionary dic, "
	    				+ "(select * from operate_costing " + where
	    				+ " ) b,operate_partner c where dic.id = b.appId and b.companyId = c.id";
	  

	    try {
			ordList = this.Query(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	   
	    return ordList;
	}
	
	/**
	 * 获取表operate_datatask_log内各表的最新记录
	 * @return
	 */
	public List<Map<String, String>> findTableDate() {
		
		List<Map<String, String>> ordList = null;
		
		//取数据库表内各个统计表的刷新记录的最新时间
		String sql = "SELECT tableName,max(date) recordTime,now() now,ROUND(unix_timestamp(now()) - unix_timestamp(max(date))) mistime"
					+ " FROM operate_datatask_log group by tableName";


		try {
			ordList = this.Query(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ordList;
	}
	

	/**
	 * 按条件查找合作方账单
	 * @param jobj
	 * @return
	 */
	public List<Map<String, String>> findPartnerBill(JSONObject jobj) {
		List<Map<String, String>> ordList = null;

		String cooperationCompany = jobj.getString("cooperationCompany");            //合作方公司名称 cooperationCompany  与表内proxyName匹配
		String payCompany = jobj.getString("payCompany");                            //我方公司名称 payCompany   与表内payCompany匹配
		String appId = jobj.getString("appId");                                      //产品id
    	String cooperationType = jobj.getString("cooperationType");                  //合作方式名称   与表内mainChannelName  匹配
    	String  status = jobj.getString("status");                                   //账单状态
    	String minDate = jobj.getString("minDate");                                  //开始时间
    	String maxDate = jobj.getString("maxDate");									 //结束时间
    	
    	String where = "where type = 1";
    	if(cooperationCompany != null && !"".equals(cooperationCompany)) {
    		where += " and proxyName = '" + cooperationCompany + "'";
    	}
    	if(payCompany != null && !"".equals(payCompany)) {
    		where += " and payCompany = '" + payCompany + "'";
    	}
    	if(appId != null && !"".equals(appId)) {
    		where += " and appId = " + appId ;
    	}
    	if(cooperationType != null && !"".equals(cooperationType)) {
    		where += " and mainChannelName = '" + cooperationType + "'";
    	}
    	if(status != null && !"".equals(status)) {
    		where += " and status = " + status ;
    	}
    	if(minDate != null && !"".equals(minDate) && maxDate != null && !"".equals(maxDate)) {
    		where += " and createTime >= '" + minDate + "' and createTime <= '" + maxDate + "'";
    	}
    	
   
	    String sql = "select id,product,appId,payCompany,proxyName,MainChannelName,cost,createTime,status,step,score,type from operate_bill " + where;
    	  
	    try {
			ordList = this.Query(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	   
	    	
	    		return ordList;
	}

	
}





























