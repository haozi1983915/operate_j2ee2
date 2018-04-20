package com.maimob.server.db.daoImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.entity.BalanceAccount;
import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.StringUtils;

public class DaoWhere {
	
	

    public static String[] getAdminWhere(String json,int type)
    {

        String[] wherestr = new String[3];
        StringBuffer where = new StringBuffer();
        where.append(" 1=1 ");
        if(!json.equals(""))
        {
            try {
            	json = URLDecoder.decode(json, "utf-8");
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}
            
            JSONObject jobj = JSONObject.parseObject(json);
            String name = jobj.getString("name");
            String email = jobj.getString("email");
            String departmentId = jobj.getString("departmentId");
            String level = jobj.getString("level");
            int pageid = Integer.parseInt(jobj.getString("pageId"));
            int page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));

            if(!StringUtils.isStrEmpty(name))
            {
            	where.append(" and name like '%"+name+"%' ");
            }

            if(!StringUtils.isStrEmpty(email))
            {
            	where.append(" and email like '%"+email+"%' ");
            }

            if(!StringUtils.isStrEmpty(departmentId))
            {
            	where.append(" and departmentId = "+departmentId+" ");
            }

            if(!StringUtils.isStrEmpty(level))
            {
            	where.append(" and level = "+level+" ");
            }
            
            if(where.length() > 0)
            {
            	wherestr[0] = " where "+where.toString();
            }
            if(type == 1)
            {
            	wherestr[0] += " order by level asc ";
            	wherestr[1] = (pageid*page_AdminCou)+"";
            	wherestr[2] = (page_AdminCou)+"";
            	
            }
        }
        
    	return wherestr;
    }
    
    
	


    public static String[] getProxyWhere(JSONObject jobj,int type)
    {
        String[] wherestr = new String[3];
        String where = new String();

        int pageid = 0;
        int page_AdminCou = 0;
        try {
            pageid = Integer.parseInt(jobj.getString("pageId"));
            page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));
		} catch (Exception e) {
			// TODO: handle exception
		}

        String id = jobj.getString("id");
        if(!StringUtils.isStrEmpty(id))
        {
        	where += " and id = "+id+" ";
        }
        else
        {
            String company = jobj.getString("company");
            if(!StringUtils.isStrEmpty(company))
            {
            	where += " and company like '%"+company+"%' ";
            }
        }
        
        if(where.toString().equals(""))
        {

            where = " channelCou = 0 or 1=1 "+where;
        	
        }
        else
        {
            where = " 1=1 "+where;
        }
        
        

        
         
        
        
        if(where.length() > 0)
        {
        	wherestr[0] = " where "+where.toString();
        }
        if(type == 1)
        {
        	wherestr[1] = (pageid*page_AdminCou)+"";
        	wherestr[2] = (page_AdminCou)+"";
        }
    
        
    	return wherestr;
    }
    
    
    public static String[] getBalanceAccountWhere(JSONObject jobj )
    {
        String[] wherestr = new String[3];
        StringBuffer where = new StringBuffer();
        where.append(" 1=1 ");
  
        String taxpayerNo = jobj.getString("taxpayerNo");
        
        if(!StringUtils.isStrEmpty(taxpayerNo))
        {
            where.append(" and taxpayerNo like '%"+taxpayerNo.trim()+"%' ");
        }
 

        String company = jobj.getString("company");
        
        if(!StringUtils.isStrEmpty(company))
        {
            where.append(" and company like '%"+company.trim()+"%' ");
        }
        

        String accountNo = jobj.getString("accountNo");
        
        if(!StringUtils.isStrEmpty(accountNo))
        {
            where.append(" and accountNo like '%"+accountNo.trim()+"%' ");
        }
        
        String attributeId = jobj.getString("attributeId");
        if(!StringUtils.isStrEmpty(attributeId))
        {
            where.append(" and attributeId = "+attributeId+"  ");
        }
 
    
        

        
        if(where.length() > 0)
        {
        		wherestr[0] = " where "+where.toString();
        } 
    
        
    	return wherestr;
    }
    


    public static String[] getChannelWhere(JSONObject jobj,int type)
    {
        String[] wherestr = new String[3];
        StringBuffer where = new StringBuffer();
        where.append(" 1=1 ");

        int pageid = 0;
        int page_AdminCou = 0;
        try {
            pageid = Integer.parseInt(jobj.getString("pageId"));
            page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));
		} catch (Exception e) {
			// TODO: handle exception
		}

        String proxyId = jobj.getString("proxyId");
        if(!StringUtils.isStrEmpty(proxyId))
        {
            where.append(" and proxyId = "+proxyId+" ");
        }

        String appid = jobj.getString("appid");
        
        if(!StringUtils.isStrEmpty(appid))
        {
            where.append(" and appid like '%"+appid.trim()+"%' ");
        }
        
        
        String channelName = jobj.getString("channelName");
        
        if(!StringUtils.isStrEmpty(channelName))
        {
            where.append(" and channelName like '%"+channelName.trim()+"%' ");
        }

        String channel = jobj.getString("channel");
        
        if(!StringUtils.isStrEmpty(channel))
        {

	    		if(channel.startsWith("-"))
	    		{
	    			channel = channel.replaceAll("-", "");
	                where.append(" and  channel = '"+channel+"' ");
	    		}
	    		else
	    		{
	            where.append(" and channel like '%"+channel+"%' ");
	    		}
        	
        }
        

        String channelNo = jobj.getString("channelNo");
        
        if(!StringUtils.isStrEmpty(channelNo))
        {
            where.append(" and channelNo like '%"+channelNo.trim()+"%' ");
        }
        

        String attribute = jobj.getString("attribute");
        if(!StringUtils.isStrEmpty(attribute))
        {
            where.append(" and attribute = "+attribute+" ");
        }
        

        String channeltype = jobj.getString("type");
        if(!StringUtils.isStrEmpty(channeltype))
        {
            where.append(" and type = "+channeltype+"  ");
        }
        

        String subdivision = jobj.getString("subdivision");
        if(!StringUtils.isStrEmpty(subdivision))
        {
            where.append(" and subdivision = "+subdivision+"  ");
        }

        String level = jobj.getString("level");
        if(!StringUtils.isStrEmpty(level))
        {
            where.append(" and level = "+level+" ");
        }

        String adminId = jobj.getString("adminId");
        if(!StringUtils.isStrEmpty(adminId))
        {
            where.append(" and adminId = "+adminId+" ");
        }

        String status = jobj.getString("status");
        if(!StringUtils.isStrEmpty(status))
        {
            where.append(" and status = "+status+" ");
        }
    	
    
        

        
        if(where.length() > 0)
        {
        	wherestr[0] = " where "+where.toString();
        }
        if(type == 1)
        {
        	wherestr[1] = (pageid*page_AdminCou)+"";
        	wherestr[2] = (page_AdminCou)+"";
        }
    
        
    	return wherestr;
    }
    
	


    public static String[] getFromWhereForFrom(JSONObject jobj,int type)
    {
        String[] wherestr = new String[4];
        StringBuffer where = new StringBuffer();
        where.append(" 1=1 ");
        
        int pageid = 0;
        int page_AdminCou = 0;
        try {
            pageid = Integer.parseInt(jobj.getString("pageId"));
            page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		String dateType = jobj.getString("dateType");
		
        String maxDate = jobj.getString("maxDate");

        String minDate = jobj.getString("minDate");
        
        if(dateType.equals("2"))
        {
        	maxDate = maxDate.substring(0,maxDate.lastIndexOf("-"));
        }
        if(dateType.equals("2"))
        {
        	minDate = minDate.substring(0,minDate.lastIndexOf("-"));
        }
        
        
        int day = AppTools.daysBetween(minDate,maxDate);
        
        wherestr[3] = day+"";
        if(!StringUtils.isStrEmpty(maxDate) && minDate.equals(maxDate))
        {
            where.append(" and date = '"+maxDate+"' ");
        }
        else
        {
            if(!StringUtils.isStrEmpty(maxDate))
            {
                where.append(" and date <= '"+maxDate+"' ");
            }
            if(!StringUtils.isStrEmpty(minDate))
            {
                where.append(" and date >= '"+minDate+"' ");
            }

        	
        }

        
        if(where.length() > 0)
        {
        	wherestr[0] = " where "+where.toString();
        }
        if(type == 1)
        {
        	wherestr[1] = (pageid*page_AdminCou)+"";
        	wherestr[2] = (page_AdminCou)+"";
        }
    
        
    	return wherestr;
    }

    public static String  getFromGroup(JSONObject jobj)
    {
//    	:["渠道","渠道号","渠道分类","负责人","H5","额度"]
    		String[] cs = jobj.getString("tag").split(",");
    		
    		StringBuffer group = new StringBuffer();
    		
    		for(String s:cs)
    		{
    			if(s.indexOf("渠道号") != -1)
    			{
    				group.append(",channelId");
    			}
    			else if(s.indexOf("渠道分类") != -1)
    			{
    				group.append(",channelAttribute,channelType,subdivision,optimization,rewardTypeId");
    			}
    			else if(s.indexOf("负责人") != -1)
    			{
    				group.append(",adminId");
    			}
    		}
    		
    		return group.toString();
    		
    }

    public static String  getFromAppGroup(JSONObject jobj)
    {
//    	:["渠道","渠道号","渠道分类","负责人","H5","额度"]
    		String[] cs = jobj.getString("tag").split(",");
    		
    		StringBuffer group = new StringBuffer();
    		
    		for(String s:cs)
    		{
    			if(s.indexOf("渠道号") != -1)
    			{
    				group.append(",channel");
    			}
    			else if(s.indexOf("渠道分类") != -1)
    			{
    				group.append(",channelAttribute,channelType,subdivision");
    			}
    			else if(s.indexOf("负责人") != -1)
    			{
    				group.append(",adminId");
    			}
    		}
    		
    		return group.toString();
    		
    }
    
    
    public static String  getFromGroupNothing(JSONObject jobj)
    {
//    	:["渠道","渠道号","渠道分类","负责人","H5","额度"]
    		String[] cs = jobj.getString("tag").split(",");
    		
    		StringBuffer group = new StringBuffer();
    		
    		for(String s:cs)
    		{
    			if(s.indexOf("渠道号") != -1)
    			{
    				group.append("channelId");
    			}
    			else if(s.indexOf("渠道分类") != -1)
    			{
    				group.append(",channelAttribute,channelType,subdivision");
    			}
    			else if(s.indexOf("负责人") != -1)
    			{
    				group.append(",adminId");
    			}
    		}
    		
    		return group.toString();
    		
    }
    

    public static boolean isHj(JSONObject jobj)
    {
//    	:["渠道","渠道号","渠道分类","负责人","H5","额度"]
    		String cs = jobj.getString("tag");
    		
    		boolean isok = false;
    		if(cs.contains("总计"))
    			isok = true;
    		
    		return isok;
    }
    public static boolean ischose(String str,JSONObject jobj)
    {
//    	:["渠道","渠道号","渠道分类","负责人","H5","额度"]
    		String cs = jobj.getString("tag");
    		
    		boolean ischose = false;
    		if(cs.contains(str))
    			ischose = true;
    		
    		return ischose;
    }

    public static String  getFromChannelGroup(JSONObject jobj)
    {
//    	:["负责人"]
    		String[] cs = jobj.getString("tag").split(",");
    		
    		StringBuffer group = new StringBuffer();
    		
    		for(String s:cs)
    		{
    			if(s.indexOf("负责人") != -1)
    			{
    				group.append(",adminid");
    			}
    		}
    		
    		return group.toString();
    		
    }

    
    

    public static String getFromWhereForHjByDate(JSONObject jobj)
    {

        StringBuffer where = new StringBuffer();
        where.append(" where 1=1 ");
        String maxDate = jobj.getString("maxDate");

        String minDate = jobj.getString("minDate");
        
        
        int day = AppTools.daysBetween(minDate,maxDate); 
        if(!StringUtils.isStrEmpty(maxDate) && minDate.equals(maxDate))
        {
            where.append(" and date = '"+maxDate+"' ");
        }
        else
        {
            if(!StringUtils.isStrEmpty(maxDate))
            {
                where.append(" and date <= '"+maxDate+"' ");
            }
            if(!StringUtils.isStrEmpty(minDate))
            {
                where.append(" and date >= '"+minDate+"' ");
            }

        	
        }

        String channelAttribute = jobj.getString("attribute");
        if(!StringUtils.isStrEmpty(channelAttribute))
        {
            where.append(" and channelAttribute = "+channelAttribute+" ");
        }

        String channelType = jobj.getString("channelType");
        
        if(!StringUtils.isStrEmpty(channelType))
        {
        		String[] stype = channelType.split(",");
        		
            where.append(" and channelType = "+channelType+" ");
        }
        

        String appid = jobj.getString("appId");
        if(!StringUtils.isStrEmpty(appid))
        {
            where.append(" and appid = "+appid+" ");
        }

        String subdivision = jobj.getString("subdivision");
        if(!StringUtils.isStrEmpty(subdivision))
        {
            where.append(" and subdivision = "+subdivision+" ");
        }
        String adminId = jobj.getString("adminId");
        if(!StringUtils.isStrEmpty(adminId))
        {
            where.append(" and adminId = "+adminId+" ");
        }
        

        String channellogin = jobj.getString("channellogin");
        String channel = jobj.getString("channel");
        if(channellogin == null)
        {
            if(!StringUtils.isStrEmpty(channel))
            {
            		if(channel.startsWith("-"))
            		{
            			channel = channel.replaceAll("-", "");
                        where.append(" and en.channel = '"+channel+"' ");
            		}
            		else
            		{
                    where.append(" and en.channel like '%"+channel+"%' ");
            		}
            }
        }
        else
        {
            if(!StringUtils.isStrEmpty(channel))
            {
                where.append(" and en.channel = '"+channel+"' ");
            }
        }
        

        String appId = jobj.getString("appId"); 
        if(!StringUtils.isStrEmpty(appId))
        {
            where.append(" and appid = "+appId+" ");
        }
        
    	return where.toString();
    }
    

    public static String[] getFromWhereForHj(JSONObject jobj,int type,String showTime)
    {
        String[] wherestr = new String[6];
        StringBuffer where = new StringBuffer();
        if(!StringUtils.isStrEmpty(showTime))

            where.append(" showTime<'"+showTime+"' ");
        else
        where.append(" 1=1 ");
        
        int pageid = 0;
        int page_AdminCou = 0;
        try {
            pageid = Integer.parseInt(jobj.getString("pageId"));
            page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		
        String maxDate = jobj.getString("maxDate");

        String minDate = jobj.getString("minDate");
        
        
        int day = AppTools.daysBetween(minDate,maxDate);
        
        wherestr[3] = day+"";
        if(!StringUtils.isStrEmpty(maxDate) && minDate.equals(maxDate))
        {
            where.append(" and date = '"+maxDate+"' ");
        }
        else
        {
            if(!StringUtils.isStrEmpty(maxDate))
            {
                where.append(" and date <= '"+maxDate+"' ");
            }
            if(!StringUtils.isStrEmpty(minDate))
            {
                where.append(" and date >= '"+minDate+"' ");
            }

        	
        }
        

        String channelAttribute = jobj.getString("attribute");
        if(!StringUtils.isStrEmpty(channelAttribute))
        {
            where.append(" and channelAttribute = "+channelAttribute+" ");
        }

        String channelType = jobj.getString("type");
        if(!StringUtils.isStrEmpty(channelType))
        {
            where.append(" and channelType = "+channelType+" ");
        }
        

        String appid = jobj.getString("appId");
        if(!StringUtils.isStrEmpty(appid))
        {
            where.append(" and appid = "+appid+" ");
        }

        String subdivision = jobj.getString("subdivision");
        if(!StringUtils.isStrEmpty(subdivision))
        {
            where.append(" and subdivision = "+subdivision+" ");
        }
        String adminId = jobj.getString("adminId");
        if(!StringUtils.isStrEmpty(adminId))
        {
            where.append(" and adminId = "+adminId+" ");
        }
        

        JSONArray adminIdList = jobj.getJSONArray("adminIdList");
//        String adminId = jobj.getString("adminId");

        if(adminIdList != null && adminIdList.size()>0) {
            where.append(" and adminId in (");
        
            for (int i = 0; i < adminIdList.size(); i++) {

	            	if (i == 0){
	            		where.append(adminIdList.get(i).toString());
	            	}
	            	else{
	            		where.append("," + adminIdList.get(i).toString());
	            	}
            }
            where.append(" )");
        }
      

        String channellogin = jobj.getString("channellogin");
        String channel = jobj.getString("channel");
        if(channellogin == null)
        {
            if(!StringUtils.isStrEmpty(channel))
            {
            		if(channel.startsWith("-"))
            		{
            			channel = channel.replaceAll("-", "");
                        where.append(" and  channel = '"+channel+"' ");
            		}
            		else
            		{
                    where.append(" and  channel like '%"+channel+"%' ");
            		}
            }
        }
        else
        {
            if(!StringUtils.isStrEmpty(channel))
            {
                where.append(" and  channel = '"+channel+"' ");
            }
        }
        

        String mainChannel = jobj.getString("mainChannel"); 
        if(!StringUtils.isStrEmpty(mainChannel))
        {
        		if(mainChannel.startsWith("-"))
        		{
        				mainChannel = mainChannel.replaceAll("-", "");
                    where.append(" and  mainChannel = '"+mainChannel+"' ");
        		}
        		else
        		{
                where.append(" and  mainChannel like '%"+mainChannel+"%' ");
        		}
        } 
        

        String channelName = jobj.getString("channelName");
        if(!StringUtils.isStrEmpty(channelName))
        {
            where.append(" and  channelName like '%"+channelName+"%' ");
        }
        


        String mainChannelName = jobj.getString("mainChannelName");
        if(!StringUtils.isStrEmpty(mainChannelName))
        {
            where.append(" and  mainChannelName like '%"+mainChannelName+"%' ");
        }
        
        String rewardTypeId = jobj.getString("rewardTypeId");
        if(!StringUtils.isStrEmpty(rewardTypeId))
        {
            where.append(" and  rewardTypeId = '"+rewardTypeId+"' ");
        }
        
        if(where.length() > 0)
        {
        	wherestr[0] = " where "+where.toString();
        }
        if(type == 1)
        {
        	wherestr[1] = (pageid*page_AdminCou)+"";
        	wherestr[2] = (page_AdminCou)+"";
        }
    
        
    		return wherestr;
    }

    
    public static String[] getFromWhereForAdmin(JSONObject jobj,String showTime)
    {

		String[] where = DaoWhere.getFromWhereForHj(jobj, 1,showTime);

		String where1 = where[0];
		
		JSONArray adminIdList = jobj.getJSONArray("adminIdList");
		where1 += " and a.channelId > 0 ";
		
		String group = " group by date ";

		String groupCol = " date ";
		if(jobj.getString("mainChannel") != null)
		{
			group += ",mainChannel";
			groupCol = ",mainChannel";
		}

		if(jobj.getString("attribute") != null)
		{
			group += ",mainChannel";
			groupCol = ",mainChannel";
		}
		
		
		
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
			

			if(jobj.getString("adminid") != null)
			{
				group += ",adminid";
				groupCol = ",adminid";
			}
		}
		where[0] = where1;
		where[4] = group;
		where[5] = groupCol;
		
    		return where;
    }
	


    public static String[] getOptimizationTaskWhere(JSONObject jobj)
    {
        String[] wherestr = new String[4];
        StringBuffer where = new StringBuffer();
        where.append(" 1=1 ");
        
        int pageid = 0;
        int page_AdminCou = 0;
        try {
            pageid = Integer.parseInt(jobj.getString("pageId"));
            page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		
        String maxDate = jobj.getString("maxDate");

        String minDate = jobj.getString("minDate");
        
        
        int day = AppTools.daysBetween(minDate,maxDate);
        
        wherestr[3] = day+"";
        if(!StringUtils.isStrEmpty(maxDate) && minDate.equals(maxDate))
        {
            where.append(" and (startdate = '"+maxDate+"' or enddate = '"+maxDate+"' ) ");
        }
        else if(!StringUtils.isStrEmpty(maxDate) && !StringUtils.isStrEmpty(minDate))
        {
            where.append(" and ( startdate <= '"+maxDate+"' and startdate >= '"+minDate+"'  or  enddate <= '"+maxDate+"' and enddate >= '"+minDate+"' ) ");
        }
        

        String tableId = jobj.getString("tableId");
        
        
        if(!StringUtils.isStrEmpty(tableId))
        {
            where.append(" and tableId = "+tableId+" ");
        }
        

        
        if(where.length() > 0)
        {
        	wherestr[0] = " where "+where.toString();
        }
    
        
    	return wherestr;
    }
    



    public static String[] getCostWhere(JSONObject jobj,int type)
    {
        String[] wherestr = new String[4];
        StringBuffer where = new StringBuffer();
        where.append(" 1=1 ");
        
        int pageid = 0;
        int page_AdminCou = 0;
        try {
            pageid = Integer.parseInt(jobj.getString("pageId"));
            page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));
		} catch (Exception e) {
			// TODO: handle exception
		}

        String maxDate = jobj.getString("maxDate");

        String minDate = jobj.getString("minDate");
        
        if(!StringUtils.isStrEmpty(maxDate) && minDate.equals(maxDate))
        {
            where.append(" and date = '"+maxDate+"' ");
        }
        else
        {
            if(!StringUtils.isStrEmpty(maxDate))
            {
                where.append(" and date <= '"+maxDate+"' ");
            }
            if(!StringUtils.isStrEmpty(minDate))
            {
                where.append(" and date >= '"+minDate+"' ");
            }
        }

        String channel = jobj.getString("channel");
        if(!StringUtils.isStrEmpty(channel))
        {
            where.append(" and channel like '%"+channel+"%' ");
        }


        String channelName = jobj.getString("channelName");
        if(!StringUtils.isStrEmpty(channelName))
        {
            where.append(" and channelName = '%"+channelName+"%' ");
        }

        
        if(where.length() > 0)
        {
        	wherestr[0] = " where "+where.toString();
        }
        
        if(type == 1)
        {
        	wherestr[1] = (pageid*page_AdminCou)+"";
        	wherestr[2] = (page_AdminCou)+"";
        }
    
        
    	return wherestr;
    }
    

    public static String[] getBillWhere(JSONObject jobj,int type)
    {
        String[] wherestr = new String[4];
        StringBuffer where = new StringBuffer();
        where.append(" 1=1 ");
        
        int pageid = 0;
        int page_AdminCou = 0;
        try {
            pageid = Integer.parseInt(jobj.getString("pageId"));
            page_AdminCou = Integer.parseInt(jobj.getString("pageSize"));
		} catch (Exception e) {
			// TODO: handle exception
		}

        String maxDate = jobj.getString("maxDate");

        String minDate = jobj.getString("minDate");
        
        if(!StringUtils.isStrEmpty(maxDate) && minDate.equals(maxDate))
        {
            where.append(" and month = '"+maxDate+"' ");
        }
        else
        {
            if(!StringUtils.isStrEmpty(maxDate))
            {
                where.append(" and month <= '"+maxDate+"' ");
            }
            if(!StringUtils.isStrEmpty(minDate))
            {
                where.append(" and month >= '"+minDate+"' ");
            }
        }

        String proxyName = jobj.getString("proxyName");
        if(!StringUtils.isStrEmpty(proxyName))
        {
            where.append(" and proxyName like '%"+proxyName+"%' ");
        }

        String id = jobj.getString("id");
        if(!StringUtils.isStrEmpty(id))
        {
            where.append(" and id = "+id+" ");
        }

        String mainChannelName = jobj.getString("mainChannelName");
        if(!StringUtils.isStrEmpty(mainChannelName))
        {
            where.append(" and mainChannelName like '%"+mainChannelName+"%' ");
        }

        String payCompany = jobj.getString("payCompany");
        if(!StringUtils.isStrEmpty(payCompany))
        {
            where.append(" and payCompany = '"+payCompany+"' ");
        }

        String appId = jobj.getString("appId");
        if(!StringUtils.isStrEmpty(appId))
        {
            where.append(" and appId = "+appId+" ");
        }
        
        
        String adminId = jobj.getString("adminId");
        if(!StringUtils.isStrEmpty(adminId))
        {
            where.append(" and adminId = "+adminId+" ");
        }

        String status = jobj.getString("status");
        if(!StringUtils.isStrEmpty(status))
        {
            where.append(" and status = "+status+" ");
        }
        
        
        
        if(where.length() > 0)
        {
        		wherestr[0] = " where "+where.toString();
        }
        
        if(type == 1)
        {
	        	wherestr[1] = (pageid*page_AdminCou)+"";
	        	wherestr[2] = (page_AdminCou)+"";
        }
        return wherestr;
    }
    



}
