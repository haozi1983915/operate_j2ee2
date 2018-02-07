package com.maimob.server.importData.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.daoImpl.DaoWhere;
import com.maimob.server.db.entity.Operate_reportform;

public class OperateDao extends Dao {

	public OperateDao() {
		
		super("db_operate");
		
	}
	

    public List<Operate_reportform> findSumFormDay(List<Long> channelids,JSONObject jobj){

    	String[] where = DaoWhere.getFromWhereForHj(jobj,1);
    	String where1 = where[0];
    	
    	if(channelids.size() == 0)
    	{
    		where1 += " and en.channelId > 0 ";
    		
    	}
    	else if(channelids.size() > 0)
    	{
    		where1 += " and en.channelId in ( ";
    		int i = 0;
    		for(long id:channelids)
    		{
    			if(i==0)
    				where1 += id;
    			else
        			where1 += ","+id;
    			i++;
    		}
    		where1 += ")";
    	}
    	
    	 String sql = " select  count(1) cou  from   ( "+
    			 " select channelid  from   ( "+
				" select c.id channelid    from Operate_reportform_day en , operate_channel c  "+
				where1 +
				" and en.channelid = c.id and en.channelid > 0 "+
				" ) a  group by a.channelid  )b   ";
    	 

    	 String cou = "";
 		try {
 			List<Map<String, String>> ordList = this.Query(sql);
 			cou = ordList.get(0).get("cou");
 		} catch (Exception e) {
 		}
    	
    	String hql = "select '' adminiName,'' ChannelName,SUM( h5Click) h5Click ,  "+
			" SUM(en.h5Register) h5Register ,  "+
			" SUM(en.activation) activation ,  "+
			" SUM(en.register) register ,  "+
			"  SUM(en.upload) upload ,  "+
			"  SUM(en.account) account ,  "+
			"  SUM(en.loan) loan ,  "+
			"  SUM(en.credit) credit ,  "+
			"  SUM(en.perCapitaCredit) perCapitaCredit ,  "+
			"  SUM(en.firstGetPer) firstGetPer ,  "+
			"  SUM(en.firstGetSum) firstGetSum ,  "+
			"  SUM(en.channelSum) channelSum from Operate_reportform_day en ";
    	hql += where1;

    	List<Operate_reportform> hj = map_obj(hql,where[3],0,null,null);
    	hj.get(0).setChannel(cou+"个渠道");

        return hj;
    }


	
	
	

    public List<Operate_reportform> findAdminSumFormDay(List<Long> channelids,JSONObject jobj){
    	String[] where = DaoWhere.getFromWhereForHj(jobj,1);
    	
    	String where1 = where[0];
    	
    	if(channelids.size() == 0)
    	{
    		where1 += " and en.channelId > 0 ";
    		
    	}
    	else if(channelids.size() > 0)
    	{
    		where1 += " and en.channelId in ( ";
    		int i = 0;
    		for(long id:channelids)
    		{
    			if(i==0)
    				where1 += id;
    			else
        			where1 += ","+id;
    			i++;
    		}
    		where1 += ")";
    	}
    	

		String sql = " select adminid ,count(1) cou "+
				" from  "+
				" ( select   adminid,proxyid from  "+
				" ( select c.adminid ,  c.proxyid proxyid from Operate_reportform_day en , operate_channel c  "+
				where1 + " and  en.channelid = c.id and en.channelid > 0     "+
				" ) a group by a.adminid ,a.proxyid) b "+
				" group by b.adminid  ";


		Map<String,String> ad_pr = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
	    	for(int i = 0;i < ordList.size();i++)
	    	{
	    		Map<String,String> ordMap = ordList.get(i);
	    		String adminid = ordMap.get("adminid");
	    		String cou = ordMap.get("cou");
	    		ad_pr.put(adminid, cou);
	    	}
		} catch (Exception e) {
		}
    	
		sql = " select adminid ,count(1) cou "+
				" from  "+
				" ( select   adminid,channelid from  "+
				" ( select c.adminid ,  c.id channelid  from Operate_reportform_day en , operate_channel c  "+
				where1 + " and  en.channelid = c.id and en.channelid > 0     "+
				" ) a group by a.adminid ,a.channelid) b "+
				" group by b.adminid  ";


		Map<String,String> ad_ch = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
	    	for(int i = 0;i < ordList.size();i++)
	    	{
	    		Map<String,String> ordMap = ordList.get(i);
	    		String adminid = ordMap.get("adminid");
	    		String cou = ordMap.get("cou");
	    		ad_ch.put(adminid, cou);
	    	}
		} catch (Exception e) {
		}
    	
    	
		
		
    	
    	
    	String hql = 
    			" select   " +
    			" (select name from operate_admin b where  b.id = a.adminid1) adminiName, "+
    			"  a.adminid1 adminid, " + 
    			"'' ChannelName,"+
    			" SUM(h5Click) h5Click ,"+
    			" SUM(h5Register) h5Register ,"+
    			" SUM(activation) activation ,"+
    			" SUM(register) register ,"+
    			" SUM(upload) upload ,"+
    			" SUM(account) account ,"+
    			" SUM(loan) loan ,"+
    			" SUM(credit) credit ,"+
    			" SUM(perCapitaCredit) perCapitaCredit ,"+
    			" SUM(firstGetPer) firstGetPer ,"+
    			" SUM(firstGetSum) firstGetSum ,"+
    			" SUM(channelSum) channelSum "+
    			" from "+
    			" ( "+
    			" select c.adminid adminid1,  en.* from Operate_reportform_day en , operate_channel c " + where1 + " and en.channelid = c.id and en.channelid > 0  "+
    			" ) a  group by a. adminid1";

        return map_obj(hql,where[3],1,ad_pr,ad_ch);
    }
    

    public List<Operate_reportform> findProxySumFormDay(List<Long> channelids,JSONObject jobj){

    	String[] where = DaoWhere.getFromWhereForHj(jobj,1);
    	String where1 = where[0];
    	
    	if(channelids.size() == 0)
    	{
    		where1 += " and en.channelId > 0 ";
    		
    	}
    	else if(channelids.size() > 0)
    	{
    		where1 += " and en.channelId in ( ";
    		int i = 0;
    		for(long id:channelids)
    		{
    			if(i==0)
    				where1 += id;
    			else
        			where1 += ","+id;
    			i++;
    		}
    		where1 += ")";
    	}
    	
		String sql = " select proxyid ,count(1) cou "+
				" from  "+
				" (select proxyid,channelid from  "+
				" (select c.proxyid  ,  c.id channelid from Operate_reportform_day en , operate_channel c  "+
				where1 + " and en.channelid = c.id and en.channelid > 0 ) a "+
				" group by a.proxyid,a.channelid )b "+
				" group by  proxyid  ";
		
		
		Map<String,String> pr_ch = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			for(int i = 0;i < ordList.size();i++)
			{
				Map<String,String> ordMap = ordList.get(i);
				String proxyid = ordMap.get("proxyid");
				String cou = ordMap.get("cou");
				pr_ch.put(proxyid, cou);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sql = " select proxyid ,count(1) cou "+
				" from  "+
				" (select proxyid,adminid from  "+
				" (select c.proxyid  ,  c.adminid from Operate_reportform_day en , operate_channel c  "+
				where1 + " and en.channelid = c.id and en.channelid > 0 ) a "+
				" group by a.proxyid,a.adminid )b "+
				" group by  proxyid  ";
		
		
		Map<String,String> pr_ad = new HashMap<String, String>();
		try {
			List<Map<String, String>> ordList = this.Query(sql);
			for(int i = 0;i < ordList.size();i++)
			{
				Map<String,String> ordMap = ordList.get(i);
				String proxyid = ordMap.get("proxyid");
				String cou = ordMap.get("cou");
				pr_ad.put(proxyid, cou);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
    	String hql = 
    			" select "
    			+ "proxyid," +
    			" '' adminiName, "+
    			" (select   company from operate_proxy b where  b.id = a.proxyid) ChannelName, "+
    			" SUM(h5Click) h5Click ,"+
    			" SUM(h5Register) h5Register ,"+
    			" SUM(activation) activation ,"+
    			" SUM(register) register ,"+
    			" SUM(upload) upload ,"+
    			" SUM(account) account ,"+
    			" SUM(loan) loan ,"+
    			" SUM(credit) credit ,"+
    			" SUM(perCapitaCredit) perCapitaCredit ,"+
    			" SUM(firstGetPer) firstGetPer ,"+
    			" SUM(firstGetSum) firstGetSum ,"+
    			" SUM(channelSum) channelSum "+
    			" from "+
    			" ( "+
    			" select   c.proxyid ,  en.* from Operate_reportform_day en , operate_channel c  "+where1+" and en.channelid = c.id and en.channelid > 0  "+
    			" ) a  group by a. proxyid";

        return map_obj(hql,where[3],2,pr_ch,pr_ad);
    }
    

    
    
//    public List<Operate_reportform> findChannelSumFormDay(List<Long> channelids,JSONObject jobj){
//    	String[] where = DaoWhere.getFromWhereForFrom(jobj,1);
//    	
//    	String hql = 
//    			" select " +
//    			" '' adminiName, "+
//    			" (select   company from operate_proxy b where  b.id = a.proxyid) ChannelName, "+
//    			" SUM(h5Click) h5Click ,"+
//    			" SUM(h5Register) h5Register ,"+
//    			" SUM(activation) activation ,"+
//    			" SUM(register) register ,"+
//    			" SUM(upload) upload ,"+
//    			" SUM(account) account ,"+
//    			" SUM(loan) loan ,"+
//    			" SUM(credit) credit ,"+
//    			" SUM(perCapitaCredit) perCapitaCredit ,"+
//    			" SUM(firstGetPer) firstGetPer ,"+
//    			" SUM(firstGetSum) firstGetSum ,"+
//    			" SUM(channelSum) channelSum "+
//    			" from "+
//    			" ( "+
//    			" select   c.proxyid ,  en.* from Operate_reportform_day en , operate_channel c  "+where+"  en.channelid = c.id and en.channelid > 0  "+
//    			" ) a  group by a. proxyid";
//
//        return map_obj(hql);
//    }
    
    
    
    
    
    public List<Operate_reportform> map_obj(String hql,String day,int type,Map<String,String> map1,Map<String,String> map2)
    {
    	List<Operate_reportform> ords = new ArrayList<Operate_reportform>();
    	try {
    		List<Map<String, String>> ordList = this.Query(hql);
        	for(int i = 0;i < ordList.size();i++)
        	{
        		Map<String,String> ordMap = ordList.get(i);
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
    			if(account > 0)
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
        		ord.setDate(day+"天");
        		
        		if(map1 != null)
        		{
        			if(type == 1)
        			{
                		String val1 = ordMap.get("adminid");
                		if(val1 == null)
                			val1 = ordMap.get("adminId");
                		String val2 = map1.get(val1);
            			ord.setChannelName(val2+"个公司");
            			
                		String val3 = map2.get(val1);
                		ord.setChannel(val3+"个渠道");
        				
        			}
        			else  if(type == 2)
        			{
                		String val1 = ordMap.get("proxyid");
                		String val2 = map1.get(val1);
            			ord.setChannel(val2+"个渠道");
            			
                		String val3 = map2.get(val1);
                		ord.setAdminName(val3+"个负责人");
        				
        			}
                		
            		
        		}
        		
        		ords.add(ord);
        	}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ords;
    }
    
    
    
    public List<Map<String, String>> getAllTask() 
    {

		String sql = " select * from operate_optimization_task where status < 2 order by id desc limit 0,1 ";
		
		List<Map<String, String>> tasks = null;
		try {
			tasks = this.Query(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return tasks;
    }
    
    
    
    

    public void updateTask(long id,int status) 
    {

		String sql = " update operate_optimization_task set status="+status+" where id="+id+" ";
		try {
			this.Update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    
    
    
    
    
}




















