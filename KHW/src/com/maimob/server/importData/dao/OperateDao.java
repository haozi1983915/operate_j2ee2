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
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Operate_reportform;
import com.maimob.server.utils.Cache;

public class OperateDao extends Dao {

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

		String sql = " select  count(1) cou  from   (  select channelid  from   ( "
				+ " select c.id channelid    from operate_reportform en , operate_channel c  " + where1
				+ " and en.channelid = c.id and en.channelid > 0 " + " ) a  group by a.channelid  )b   ";

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

		String sql = " select  count(1) cou  from   (  select channelid  from   ( "
				+ " select c.id channelid    from operate_reportform en , operate_channel c  " + where1
				+ " and en.channelid = c.id and en.channelid > 0 " + " ) a  group by a.channelid  )b   ";

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
				+ " sum( upload) upload ,  " + " sum( account) account ,  " + " sum( outAccount) outAccount ,  " 
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(outFirstGetPer) outFirstGetPer ,  " + " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum ,  "
				+ " sum(outChannelSum) outChannelSum ,  " + " sum(income) income ,  "
				+ " sum(cost) cost " + " from operate_reportform en  ";

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

		String hql = " select count(1) cou from operate_reportform a " + where1 +" ";
	 	if(!dateType.equals("1"))
	 		hql = "select count(channelid)cou from ( select channelid from operate_reportform a  " + where1 +"  group by channelid )a";
		
		
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
		


		String hql = " select  adminid, (select name from operate_admin b where  b.id = en.adminid) adminName,"
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( outActivation) outActivation ,  " + " sum( register) register ,  " + " sum( outRegister) outRegister ,  " 
				+ " sum( upload) upload ,  " + " sum( account) account ,  " + " sum( outAccount) outAccount ,  " 
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(outFirstGetPer) outFirstGetPer ,  " + " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum ,  "
				+ " sum(outChannelSum) outChannelSum ,  " + " sum(income) income ,  "
				+ " sum(cost) cost " + " from operate_reportform en "+where1+" group by adminid ";
		

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


		String hql = " select date,channelId,channel," 
				+ " truncate( h5Click,0) h5Click ,  " + " truncate(outRegister,0) h5Register ,  " + " truncate( outActivation,0) activation ,  "
				+ " truncate( outRegister,0) register ,  " + " truncate( outUpload,0) upload ,  " + " truncate( outAccount,0) account ,  "
				+ " truncate( outLoan,0) loan ,  " + " truncate(outCredit,0) credit ,  "
				+ " truncate( outPerCapitaCredit,0) perCapitaCredit ,  " + " truncate( outFirstGetPer,0) firstGetPer ,  "
				+ " truncate( outFirstGetSum,0) firstGetSum ,  " + " truncate( outChannelSum,0) channelSum "
				+ " from operate_reportform en " + where1 + " order by date  limit " + where[1] + "," + where[2];

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


		String hql = " select en.* from operate_reportform en " + where1 + "  order by date  limit " + where[1] + "," + where[2];

		return map_obj3(hql,"",null,null);
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


		String hql = " select channelid,trim(month) date,"
				+ " sum( h5Click) h5Click ,  " + " sum(en.outRegister) h5Register ,  "
				+ " sum(en.outActivation) activation ,  " + " sum(en.outRegister) register ,  "
				+ " sum(en.outUpload) upload ,  " + " sum(en.outAccount) account ,  " + " sum(en.outLoan) loan ,  "
				+ " sum(en.outCredit) credit ,  " + " sum(en.outPerCapitaCredit) perCapitaCredit ,  "
				+ " sum(en.outFirstGetPer) firstGetPer ,  " + " sum(en.outFirstGetSum) firstGetSum ,  "
				+ " sum(en.outChannelSum) channelSum " + " from operate_reportform en " + where1 + " group by channelid,month limit " + where[1]
				+ "," + where[2];

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


		String hql = " select channel,trim(month) date,"
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( outActivation) outActivation ,  " + " sum( register) register ,  " + " sum( outRegister) outRegister ,  " 
				+ " sum( upload) upload ,  sum(outUpload) outUpload , " + " sum( account) account ,  " + " sum( outAccount) outAccount ,  " 
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(outFirstGetPer) outFirstGetPer ,  " + " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum ,  "
				+ " sum(outChannelSum) outChannelSum ,  " + " sum(income) income ,  "
				+ " sum(cost) cost " + " from operate_reportform en " + where1 + " group by channel,month limit " + where[1]
				+ "," + where[2];

		return map_obj3(hql," / "+where[3]+"天",null,null);
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
				
				ord.setChannelId(Long.parseLong(ordMap.get("channelId")));

		    		Channel channel = Cache.getChannelCatche(ord.getChannelId());
		    		if(channel != null)
		    		{
		    			ord.setChannel(channel.getChannel());
		    			ord.setChannelName(channel.getChannelName());
		        		Admin admin = Cache.getAdminCatche(channel.getAdminId());
		        		if(admin != null)
		        			ord.setAdminName(admin.getName());
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
				
				if(ordMap.get("channel") != null)
				{
					System.out.println(ordMap.get("channel"));
		    		Channel channel = Cache.getChannelCatche(ordMap.get("channel"));
		    		
		    		
		    		if(channel != null)
		    		{
		    			ordMap.put("channelName", channel.getChannelName());
		        		Admin admin = Cache.getAdminCatche(channel.getAdminId());
		    			ordMap.put("adminName", admin.getName());
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
				
				double income = 0;
				try {
					String istr = ordMap.get("income");
					income = Double.parseDouble(istr);
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
				
				
				ordMap.put("grossProfit", grossProfit+"");
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
				if(!cost.equals("0"))
				{
					
					//保存最后一次优化比例
					String sql2 = "update operate_data_log set channelId= "+id+",cost="+cost+", channel='"+channel+"',channelName='"+channelName+"',updateTime='"+update+"'  where channel= '"+channel+"' and date = '"+date+"' ";
					int yx = this.Update(sql2);
					if(yx==0)
					{
						sql2 = "insert into operate_data_log(cost,channelId,date,channel,channelName,updateTime) values("+cost+" ,"+id+" , '"+date+"', '"+channel+"', '"+channelName+"', '"+update+"') ";
						yx = this.Update(sql2);
					}
					 sql2 = "update operate_reportform set cost2="+cost+"   where channel= '"+channel+"' and date = '"+date+"' ";
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


		String hql = " select channelid,trim(month) date,"
				+ " sum( h5Click) h5Click ,  " + " sum(en.outRegister) h5Register ,  "
				+ " sum(en.outActivation) activation ,  " + " sum(en.outRegister) register ,  "
				+ " sum(en.outUpload) upload ,  " + " sum(en.outAccount) account ,  " + " sum(en.outLoan) loan ,  "
				+ " sum(en.outCredit) credit ,  " + " sum(en.outPerCapitaCredit) perCapitaCredit ,  "
				+ " sum(en.outFirstGetPer) firstGetPer ,  " + " sum(en.outFirstGetSum) firstGetSum ,  "
				+ " sum(en.outChannelSum) channelSum " + " from operate_reportform en " + where1 + " group by channelid,month " ;

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


		String hql = " select en.* from operate_reportform en " + where1 ;

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


		String hql = " select channel,trim(month) date,"
				+ " sum( h5Click) h5Click ,  " + " sum( h5Register) h5Register ,  " + " sum( activation) activation ,  " 
				+ " sum( outActivation) outActivation ,  " + " sum( register) register ,  " + " sum( outRegister) outRegister ,  " 
				+ " sum( upload) upload ,  sum(outUpload) outUpload , " + " sum( account) account ,  " + " sum( outAccount) outAccount ,  " 
				+ " sum( loan) loan ,  " + " sum( loaner) loaner ,  " + " sum( credit) credit ,  " 
				
				+ " sum(firstGetPer) firstGetPer ,  " + " sum(firstGetSum) firstGetSum ,  "
				+ " sum(outFirstGetPer) outFirstGetPer ,  " + " sum(secondGetPer) secondGetPer ,  " + " sum(secondGetPi) secondGetPi ,  "
				+ " sum(secondGetSum) secondGetSum ,  " + " sum(channelSum) channelSum ,  "
				+ " sum(outChannelSum) outChannelSum ,  " + " sum(income) income ,  "
				+ " sum(cost) cost " + " from operate_reportform en " + where1 + " group by channel,month ";

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
	
	public void updateTaskLog(String table)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		
		String sql = "insert into operate_datatask_log (tableName,date) values('"+table+"' ,'"+now+"');                   ";
		
		try {
			this.Update(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	
	
}
